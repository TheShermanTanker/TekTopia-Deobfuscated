/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.pathfinding.Path;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.tangotek.tektopia.VillageManager;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.pathing.BasePathingNode;
/*     */ import net.tangotek.tektopia.pathing.PathNavigateVillager2;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ 
/*     */ public abstract class EntityAIMoveToBlock extends EntityAIBase {
/*  16 */   private static int STUCK_TIME = 40;
/*     */   protected EntityVillageNavigator navigator;
/*     */   private BlockPos walkPos;
/*     */   protected BlockPos destinationPos;
/*  20 */   private int pathUpdateTick = 20;
/*     */   private boolean arrived = false;
/*  22 */   private int stuckCheck = STUCK_TIME;
/*  23 */   private Vec3d stuckPos = Vec3d.ZERO;
/*     */   private boolean stuck = false;
/*  25 */   private int lastPathIndex = -1;
/*     */   private Vec3d lastNodePos;
/*     */   
/*     */   public EntityAIMoveToBlock(EntityVillageNavigator v) {
/*  29 */     this.navigator = v;
/*  30 */     setMutexBits(1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract BlockPos getDestinationBlock();
/*     */ 
/*     */   
/*     */   protected void onArrival() {}
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  41 */     if (this.navigator.hasVillage() && this.navigator.getNavigator() instanceof PathNavigateVillager2 && canNavigate()) {
/*  42 */       this.destinationPos = getDestinationBlock();
/*  43 */       if (this.destinationPos != null) {
/*  44 */         this.stuck = false;
/*  45 */         this.stuckPos = new Vec3d(0.0D, -400.0D, 0.0D);
/*  46 */         this.arrived = false;
/*  47 */         this.pathUpdateTick = 40;
/*  48 */         doMove();
/*  49 */         return !this.stuck;
/*     */       } 
/*     */     } 
/*     */     
/*  53 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean isNearWalkPos() {
/*  57 */     return (this.walkPos != null && this.walkPos.distanceSq((Vec3i)this.navigator.getPosition()) <= 1.0D);
/*     */   }
/*     */   
/*     */   protected boolean isNearDestination(double range) {
/*  61 */     return (this.destinationPos.distanceSq((Vec3i)this.navigator.getPosition()) < range * range);
/*     */   }
/*     */   
/*     */   protected boolean canNavigate() {
/*  65 */     return this.navigator.onGround;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  74 */     updateMovementMode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  85 */     return (!this.arrived && !this.stuck && this.navigator.canNavigate());
/*     */   }
/*     */   
/*     */   protected void updateFacing() {
/*  89 */     if (!this.arrived) {
/*  90 */       if (!this.navigator.getNavigator().noPath()) {
/*  91 */         Vec3d lookPos = this.navigator.getNavigator().getPath().getCurrentPos();
/*     */         
/*  93 */         this.navigator.faceLocation(lookPos.x, lookPos.z, 4.0F);
/*     */       }
/*     */     
/*  96 */     } else if (this.destinationPos != null) {
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   abstract void updateMovementMode();
/*     */   
/*     */   public void updateTask() {
/* 104 */     this.pathUpdateTick--;
/* 105 */     if (this.pathUpdateTick <= 0 && !this.arrived) {
/* 106 */       this.pathUpdateTick = 40;
/*     */       
/* 108 */       this.navigator.updateMovement(this.arrived);
/*     */     } 
/*     */     
/* 111 */     if (!this.arrived && isNearWalkPos()) {
/* 112 */       this.arrived = true;
/* 113 */       this.navigator.getNavigator().clearPath();
/*     */       
/* 115 */       onArrival();
/*     */     } 
/*     */     
/* 118 */     updateFacing();
/*     */     
/* 120 */     if (!this.arrived) {
/* 121 */       if (this.navigator.getNavigator().noPath()) {
/* 122 */         doMove();
/*     */       } else {
/* 124 */         int pathIndex = this.navigator.getNavigator().getPath().getCurrentPathIndex();
/* 125 */         if (this.lastPathIndex != pathIndex) {
/* 126 */           this.lastNodePos = this.navigator.getNavigator().getPath().getCurrentPos();
/* 127 */           this.lastPathIndex = pathIndex;
/*     */         } 
/*     */       } 
/*     */       
/* 131 */       this.stuckCheck--;
/* 132 */       if (this.stuckCheck < 0) {
/* 133 */         this.stuckCheck = STUCK_TIME;
/* 134 */         if (!this.navigator.getNavigator().noPath()) {
/* 135 */           this.stuck = (this.navigator.getPositionVector().squareDistanceTo(this.stuckPos) < 1.0D);
/* 136 */           this.stuckPos = this.navigator.getPositionVector();
/*     */         } else {
/*     */           
/* 139 */           this.navigator.debugOut("has no path?");
/*     */         } 
/*     */       } 
/*     */       
/* 143 */       if (this.stuck) {
/* 144 */         if (attemptStuckFix() && this.lastPathIndex >= 0) {
/*     */           
/* 146 */           this.navigator.getNavigator().clearPath();
/* 147 */           doMove();
/*     */         } else {
/* 149 */           onStuck();
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean attemptStuckFix() {
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStuck() {
/* 161 */     VillageManager.get(this.navigator.world).submitStuck(this.navigator.getPosition());
/*     */     
/* 163 */     Path path = this.navigator.getNavigator().getPath();
/* 164 */     if (path != null)
/*     */     {
/*     */       
/* 167 */       if (this.navigator.hasVillage() && this.navigator.getNavigator() instanceof PathNavigateVillager2) {
/* 168 */         PathNavigateVillager2 pathNavigateVillager2 = (PathNavigateVillager2)this.navigator.getNavigator();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 174 */     this.navigator.getNavigator().clearPath();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPathFailed(BlockPos pos) {
/* 179 */     VillageManager.get(this.navigator.world).submitStuck(this.navigator.getPosition());
/* 180 */     this.stuck = true;
/*     */   }
/*     */   
/*     */   public BlockPos getWalkPos() {
/* 184 */     return this.walkPos;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/* 189 */     BlockPos pos = this.destinationPos;
/* 190 */     BlockPos diff = this.navigator.getPosition().subtract((Vec3i)pos);
/* 191 */     EnumFacing facing = EnumFacing.getFacingFromVector(diff.getX(), 0.0F, diff.getZ());
/*     */     
/* 193 */     BlockPos testPos = pos.offset(facing);
/* 194 */     if (isWalkable(testPos, this.navigator)) {
/* 195 */       return testPos;
/*     */     }
/* 197 */     testPos = pos.offset(facing).offset(facing.rotateY());
/* 198 */     if (isWalkable(testPos, this.navigator)) {
/* 199 */       return testPos;
/*     */     }
/* 201 */     testPos = pos.offset(facing).offset(facing.rotateYCCW());
/* 202 */     if (isWalkable(testPos, this.navigator)) {
/* 203 */       return testPos;
/*     */     }
/* 205 */     testPos = pos.offset(facing.rotateY());
/* 206 */     if (isWalkable(testPos, this.navigator)) {
/* 207 */       return testPos;
/*     */     }
/* 209 */     testPos = pos.offset(facing.rotateYCCW());
/* 210 */     if (isWalkable(testPos, this.navigator)) {
/* 211 */       return testPos;
/*     */     }
/* 213 */     testPos = pos.offset(facing.getOpposite());
/* 214 */     if (isWalkable(testPos, this.navigator)) {
/* 215 */       return testPos;
/*     */     }
/* 217 */     testPos = pos.offset(facing.getOpposite()).offset(facing.rotateY());
/* 218 */     if (isWalkable(testPos, this.navigator)) {
/* 219 */       return testPos;
/*     */     }
/* 221 */     testPos = pos.offset(facing.getOpposite()).offset(facing.rotateYCCW());
/* 222 */     if (isWalkable(testPos, this.navigator)) {
/* 223 */       return testPos;
/*     */     }
/*     */     
/* 226 */     if (isWalkable(pos, this.navigator)) {
/* 227 */       return pos;
/*     */     }
/* 229 */     return null;
/*     */   }
/*     */   
/*     */   protected boolean isWalkable(BlockPos pos, EntityVillageNavigator nav) {
/* 233 */     if (nav.getVillage() != null) {
/* 234 */       BasePathingNode baseNode = nav.getVillage().getPathingGraph().getBaseNode(pos.getX(), pos.getY(), pos.getZ());
/* 235 */       if (baseNode != null) {
/* 236 */         if (VillageStructure.isWoodDoor(nav.world, pos) || VillageStructure.isGate(nav.world, pos)) {
/* 237 */           return false;
/*     */         }
/* 239 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 243 */     return false;
/*     */   }
/*     */   
/*     */   protected void doMove() {
/* 247 */     this.arrived = false;
/* 248 */     this.stuckCheck = STUCK_TIME;
/* 249 */     this.walkPos = findWalkPos();
/* 250 */     if (this.walkPos == null) {
/* 251 */       this.stuck = true;
/* 252 */     } else if (!isNearWalkPos() && canNavigate()) {
/* 253 */       boolean pathFound = this.navigator.getNavigator().tryMoveToXYZ(this.walkPos.getX(), this.walkPos.getY(), this.walkPos.getZ(), this.navigator.getAIMoveSpeed());
/* 254 */       if (pathFound) {
/*     */         
/* 256 */         this.navigator.getLookHelper().setLookPosition(this.walkPos.getX(), this.walkPos.getY(), this.walkPos.getZ(), 50.0F, this.navigator.getVerticalFaceSpeed());
/*     */       } else {
/* 258 */         onPathFailed(this.walkPos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   protected boolean hasArrived() {
/* 263 */     return this.arrived;
/*     */   }
/*     */   protected void setArrived() {
/* 266 */     this.arrived = true;
/*     */   }
/*     */   public void resetTask() {
/* 269 */     super.resetTask();
/* 270 */     this.arrived = false;
/* 271 */     this.stuckCheck = STUCK_TIME;
/* 272 */     this.navigator.resetMovement();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIMoveToBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */