/*     */ package net.tangotek.tektopia.pathing;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.pathfinding.NodeProcessor;
/*     */ import net.minecraft.pathfinding.Path;
/*     */ import net.minecraft.pathfinding.PathFinder;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.pathfinding.PathNodeType;
/*     */ import net.minecraft.pathfinding.PathPoint;
/*     */ import net.minecraft.pathfinding.WalkNodeProcessor;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ 
/*     */ public class PathNavigateVillager2 extends PathNavigate {
/*  28 */   private int currentPointIndex = -1;
/*  29 */   private long pathUpdate = 0L;
/*  30 */   private int directPathThrottle = 0;
/*  31 */   private Queue<EntityArmorStand> lastDebugs = new LinkedList<>();
/*     */   
/*     */   public PathNavigateVillager2(EntityLiving entitylivingIn, World worldIn, boolean doors) {
/*  34 */     super(entitylivingIn, worldIn);
/*  35 */     this.nodeProcessor.setCanOpenDoors(doors);
/*  36 */     this.nodeProcessor.setCanEnterDoors(doors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canNavigate() {
/*  44 */     return (this.entity.onGround || (getCanSwim() && isInLiquid()) || this.entity.isRiding());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3d getEntityPosition() {
/*  49 */     return new Vec3d(this.entity.posX, getPathablePosY(), this.entity.posZ);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getPathToPos(BlockPos pos) {
/*  57 */     return super.getPathToPos(pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getPathToEntityLiving(Entity entityIn) {
/*  65 */     return getPathToPos(new BlockPos(entityIn));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getPathablePosY() {
/*  73 */     if (this.entity.isInWater() && getCanSwim()) {
/*     */       
/*  75 */       int i = (int)(this.entity.getEntityBoundingBox()).minY;
/*  76 */       Block block = this.world.getBlockState(new BlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ))).getBlock();
/*  77 */       int j = 0;
/*     */       
/*  79 */       while (block == Blocks.FLOWING_WATER || block == Blocks.WATER) {
/*     */         
/*  81 */         i++;
/*  82 */         block = this.world.getBlockState(new BlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ))).getBlock();
/*  83 */         j++;
/*     */         
/*  85 */         if (j > 16)
/*     */         {
/*  87 */           return (int)(this.entity.getEntityBoundingBox()).minY;
/*     */         }
/*     */       } 
/*     */       
/*  91 */       return i;
/*     */     } 
/*     */ 
/*     */     
/*  95 */     return (int)((this.entity.getEntityBoundingBox()).minY + 0.5D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBreakDoors(boolean canBreakDoors) {
/* 102 */     this.nodeProcessor.setCanOpenDoors(canBreakDoors);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEnterDoors(boolean enterDoors) {
/* 107 */     this.nodeProcessor.setCanEnterDoors(enterDoors);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getEnterDoors() {
/* 112 */     return this.nodeProcessor.getCanEnterDoors();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCanSwim(boolean canSwim) {
/* 117 */     this.nodeProcessor.setCanSwim(canSwim);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSwim() {
/* 122 */     return this.nodeProcessor.getCanSwim();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PathFinder getPathFinder() {
/* 137 */     this.nodeProcessor = (NodeProcessor)new WalkNodeProcessor();
/* 138 */     this.nodeProcessor.setCanEnterDoors(true);
/* 139 */     this.nodeProcessor.setCanOpenDoors(true);
/* 140 */     this.nodeProcessor.setCanSwim(true);
/*     */     
/* 142 */     EntityVillageNavigator villageNav = (EntityVillageNavigator)this.entity;
/* 143 */     return new PathFinder(villageNav);
/*     */   }
/*     */   
/*     */   public PathFinder getVillagerPathFinder() {
/* 147 */     return (PathFinder)getPathFinder();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkForStuck(Vec3d positionVec3) {}
/*     */ 
/*     */   
/*     */   public boolean setPath(@Nullable Path pathentityIn, double speedIn) {
/* 155 */     this.pathUpdate = System.currentTimeMillis();
/* 156 */     return super.setPath(pathentityIn, speedIn);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pathFollow() {
/* 161 */     super.pathFollow();
/*     */     
/* 163 */     if (this.currentPath.getCurrentPathIndex() != this.currentPointIndex && !this.currentPath.isFinished()) {
/* 164 */       this.currentPointIndex = this.currentPath.getCurrentPathIndex();
/* 165 */       BasePathingNode pathNode = null;
/* 166 */       if (this.currentPointIndex >= 0 && this.currentPointIndex < this.currentPath.getCurrentPathLength()) {
/* 167 */         PathPoint pathPoint = this.currentPath.getPathPointFromIndex(this.currentPointIndex);
/*     */         
/* 169 */         Village v = ((EntityVillageNavigator)this.entity).getVillage();
/* 170 */         if (pathPoint != null && v != null) {
/* 171 */           pathNode = v.getPathingGraph().getBaseNode(pathPoint.x, pathPoint.y, pathPoint.z);
/*     */         }
/*     */       } 
/*     */       
/* 175 */       if (pathNode == null || pathNode.getUpdateTick() > this.pathUpdate)
/*     */       {
/*     */         
/* 178 */         setPath((Path)null, 1.0D);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ) {
/* 187 */     this.directPathThrottle--;
/* 188 */     if (this.directPathThrottle <= 0) {
/* 189 */       this.directPathThrottle = 30 + this.world.rand.nextInt(15);
/* 190 */       Vec3d dir = posVec32.subtract(posVec31).normalize();
/*     */       
/* 192 */       double div = Math.max(Math.abs(dir.x), Math.abs(dir.z));
/* 193 */       double widthX = sizeX / div;
/* 194 */       double widthZ = sizeZ / div;
/*     */       
/* 196 */       dir = dir.scale(0.5D);
/* 197 */       Vec3d cur = posVec31;
/* 198 */       while (cur.squareDistanceTo(posVec32) > 1.0D) {
/* 199 */         if (!isSafeToStandAt(cur, widthX, sizeY, widthZ)) {
/* 200 */           return false;
/*     */         }
/* 202 */         cur = cur.add(dir);
/*     */       } 
/*     */       
/* 205 */       return true;
/*     */     } 
/*     */     
/* 208 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSafeToStandAt(Vec3d pos, double widthX, double height, double widthZ) {
/* 216 */     double halfX = widthX / 2.0D;
/* 217 */     double halfZ = widthZ / 2.0D;
/* 218 */     BlockPos corner1 = new BlockPos(pos.x - halfX, pos.y, pos.z - halfZ);
/* 219 */     BlockPos corner2 = new BlockPos(pos.x + halfX, pos.y + height, pos.z + halfZ);
/*     */     
/* 221 */     for (BlockPos blockPos : BlockPos.getAllInBox(corner1, corner2)) {
/* 222 */       IBlockState blockState = this.world.getBlockState(blockPos);
/* 223 */       Block block = blockState.getBlock();
/* 224 */       if (!block.isPassable((IBlockAccess)this.world, blockPos))
/*     */       {
/* 226 */         return false;
/*     */       }
/*     */       
/* 229 */       if (blockPos.getY() == corner1.getY()) {
/* 230 */         PathNodeType pathnodetype = this.nodeProcessor.getPathNodeType((IBlockAccess)this.world, blockPos.getX(), blockPos.getY() - 1, blockPos.getZ(), this.entity, 1, 2, 1, true, true);
/*     */         
/* 232 */         if (pathnodetype == PathNodeType.WATER) {
/* 233 */           return false;
/*     */         }
/*     */         
/* 236 */         if (pathnodetype == PathNodeType.LAVA) {
/* 237 */           return false;
/*     */         }
/*     */         
/* 240 */         if (pathnodetype == PathNodeType.OPEN) {
/* 241 */           return false;
/*     */         }
/*     */         
/* 244 */         pathnodetype = this.nodeProcessor.getPathNodeType((IBlockAccess)this.world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.entity, 1, 2, 1, true, true);
/* 245 */         float f = this.entity.getPathPriority(pathnodetype);
/*     */         
/* 247 */         if (f < 0.0F || f >= 8.0F) {
/* 248 */           return false;
/*     */         }
/*     */         
/* 251 */         if (pathnodetype == PathNodeType.DAMAGE_FIRE || pathnodetype == PathNodeType.DANGER_FIRE || pathnodetype == PathNodeType.DAMAGE_OTHER) {
/* 252 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 258 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\pathing\PathNavigateVillager2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */