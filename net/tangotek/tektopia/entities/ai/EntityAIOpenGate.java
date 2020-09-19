/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFenceGate;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.pathfinding.Path;
/*     */ import net.minecraft.pathfinding.PathPoint;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.pathing.PathNavigateVillager2;
/*     */ 
/*     */ public class EntityAIOpenGate extends EntityAIBase {
/*     */   private EntityVillagerTek villager;
/*  18 */   protected BlockPos gatePosition = BlockPos.ORIGIN;
/*     */   
/*     */   protected BlockFenceGate gateBlock;
/*     */   
/*     */   boolean hasStoppedDoorInteraction;
/*     */   float entityPositionX;
/*     */   float entityPositionZ;
/*     */   int closeDoorTemporisation;
/*     */   
/*     */   public EntityAIOpenGate(EntityVillagerTek v) {
/*  28 */     this.villager = v;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  33 */     if (!this.villager.collidedHorizontally)
/*     */     {
/*  35 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  39 */     PathNavigateVillager2 pathNavigate = (PathNavigateVillager2)this.villager.getNavigator();
/*  40 */     Path path = pathNavigate.getPath();
/*     */     
/*  42 */     if (path != null && !path.isFinished() && pathNavigate.getEnterDoors()) {
/*     */       
/*  44 */       for (int i = 0; i < Math.min(path.getCurrentPathIndex() + 2, path.getCurrentPathLength()); i++) {
/*     */         
/*  46 */         PathPoint pathpoint = path.getPathPointFromIndex(i);
/*  47 */         this.gatePosition = new BlockPos(pathpoint.x, pathpoint.y + 1, pathpoint.z);
/*     */         
/*  49 */         if (this.villager.getDistanceSq(this.gatePosition.getX(), this.villager.posY, this.gatePosition.getZ()) <= 2.25D) {
/*     */           
/*  51 */           this.gateBlock = getBlockGate(this.gatePosition);
/*     */           
/*  53 */           if (this.gateBlock != null)
/*     */           {
/*  55 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/*  60 */       this.gatePosition = new BlockPos((Entity)this.villager);
/*  61 */       this.gateBlock = getBlockGate(this.gatePosition);
/*  62 */       return (this.gateBlock != null);
/*     */     } 
/*     */ 
/*     */     
/*  66 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  75 */     return (this.closeDoorTemporisation > 0 && !this.hasStoppedDoorInteraction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  83 */     this.closeDoorTemporisation = 20;
/*  84 */     toggleGate(this.gatePosition, true);
/*     */     
/*  86 */     this.hasStoppedDoorInteraction = false;
/*  87 */     this.entityPositionX = (float)((this.gatePosition.getX() + 0.5F) - this.villager.posX);
/*  88 */     this.entityPositionZ = (float)((this.gatePosition.getZ() + 0.5F) - this.villager.posZ);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  96 */     float f = (float)((this.gatePosition.getX() + 0.5F) - this.villager.posX);
/*  97 */     float f1 = (float)((this.gatePosition.getZ() + 0.5F) - this.villager.posZ);
/*  98 */     float f2 = this.entityPositionX * f + this.entityPositionZ * f1;
/*     */     
/* 100 */     if (f2 < 0.0F)
/*     */     {
/* 102 */       this.hasStoppedDoorInteraction = true;
/*     */     }
/*     */     
/* 105 */     this.closeDoorTemporisation--;
/* 106 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   private BlockFenceGate getBlockGate(BlockPos pos) {
/* 111 */     IBlockState iblockstate = this.villager.world.getBlockState(pos);
/* 112 */     Block block = iblockstate.getBlock();
/* 113 */     return (block instanceof BlockFenceGate) ? (BlockFenceGate)block : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 118 */     toggleGate(this.gatePosition, false);
/*     */   }
/*     */   
/*     */   private void toggleGate(BlockPos gatePosition, boolean open) {
/* 122 */     IBlockState state = this.villager.world.getBlockState(gatePosition);
/* 123 */     if (getBlockGate(gatePosition) != null && (
/* 124 */       (Boolean)state.getValue((IProperty)BlockFenceGate.OPEN)).booleanValue() != open) {
/* 125 */       state = state.withProperty((IProperty)BlockFenceGate.OPEN, Boolean.valueOf(open));
/* 126 */       this.villager.world.setBlockState(gatePosition, state, 10);
/* 127 */       this.villager.world.playEvent((EntityPlayer)null, ((Boolean)state.getValue((IProperty)BlockFenceGate.OPEN)).booleanValue() ? 1008 : 1014, gatePosition, 0);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIOpenGate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */