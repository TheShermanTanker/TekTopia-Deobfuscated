/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDoor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.pathfinding.Path;
/*     */ import net.minecraft.pathfinding.PathPoint;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.pathing.PathNavigateVillager2;
/*     */ 
/*     */ public class EntityAIUseDoor
/*     */   extends EntityAIBase
/*     */ {
/*     */   boolean closeDoor;
/*     */   int closeDoorTimer;
/*     */   protected EntityLiving entity;
/*  23 */   protected BlockPos doorPosition = BlockPos.ORIGIN;
/*     */   
/*     */   protected BlockDoor doorBlock;
/*     */ 
/*     */   
/*     */   public EntityAIUseDoor(EntityLiving entitylivingIn, boolean shouldClose) {
/*  29 */     this.entity = entitylivingIn;
/*  30 */     this.closeDoor = shouldClose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  38 */     if (!this.entity.collidedHorizontally)
/*     */     {
/*  40 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  44 */     PathNavigateVillager2 pathNavigate = (PathNavigateVillager2)this.entity.getNavigator();
/*  45 */     Path path = pathNavigate.getPath();
/*     */     
/*  47 */     if (path != null && !path.isFinished() && pathNavigate.getEnterDoors()) {
/*     */       
/*  49 */       for (int i = 0; i < Math.min(path.getCurrentPathIndex() + 2, path.getCurrentPathLength()); i++) {
/*     */         
/*  51 */         PathPoint pathpoint = path.getPathPointFromIndex(i);
/*  52 */         this.doorPosition = new BlockPos(pathpoint.x, pathpoint.y + 1, pathpoint.z);
/*     */         
/*  54 */         if (this.entity.getDistanceSq(this.doorPosition.getX(), this.entity.posY, this.doorPosition.getZ()) <= 2.25D) {
/*     */           
/*  56 */           this.doorBlock = getBlockDoor(this.doorPosition);
/*     */           
/*  58 */           if (this.doorBlock != null)
/*     */           {
/*  60 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/*  65 */       this.doorPosition = (new BlockPos((Entity)this.entity)).up();
/*  66 */       this.doorBlock = getBlockDoor(this.doorPosition);
/*  67 */       return (this.doorBlock != null);
/*     */     } 
/*     */ 
/*     */     
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BlockDoor getBlockDoor(BlockPos pos) {
/*  79 */     IBlockState iblockstate = this.entity.world.getBlockState(pos);
/*  80 */     Block block = iblockstate.getBlock();
/*  81 */     return (block instanceof BlockDoor && iblockstate.getMaterial() == Material.WOOD) ? (BlockDoor)block : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  90 */     return (this.closeDoor && this.closeDoorTimer >= 0);
/*     */   }
/*     */   
/*     */   private boolean isDoorClear() {
/*  94 */     return this.entity.world.getEntitiesWithinAABB(EntityVillagerTek.class, new AxisAlignedBB(this.doorPosition)).isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/* 102 */     this.closeDoorTimer = 25;
/* 103 */     openDoor(true);
/*     */   }
/*     */   
/*     */   private void openDoor(boolean open) {
/* 107 */     this.doorBlock.toggleDoor(this.entity.world, this.doorPosition, open);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 115 */     if (this.closeDoor)
/*     */     {
/* 117 */       openDoor(false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 126 */     this.closeDoorTimer--;
/* 127 */     if (this.closeDoorTimer == 0 && 
/* 128 */       !isDoorClear()) {
/* 129 */       openDoor(true);
/* 130 */       this.closeDoorTimer = 25;
/*     */     } 
/*     */ 
/*     */     
/* 134 */     super.updateTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIUseDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */