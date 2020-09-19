/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.Vec3i;
/*    */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ import net.tangotek.tektopia.pathing.BasePathingNode;
/*    */ 
/*    */ public abstract class EntityAIPatrolPoint
/*    */   extends EntityAIMoveToBlock {
/*    */   protected final Predicate<EntityVillagerTek> shouldPred;
/*    */   protected final EntityVillagerTek villager;
/*    */   protected final int distanceFromPoint;
/*    */   protected final int waitTime;
/*    */   private int waitRemaining;
/*    */   
/*    */   public EntityAIPatrolPoint(EntityVillagerTek v, Predicate<EntityVillagerTek> shouldPred, int distanceFromPoint, int waitTime) {
/* 19 */     super((EntityVillageNavigator)v);
/* 20 */     this.villager = v;
/* 21 */     this.distanceFromPoint = distanceFromPoint;
/* 22 */     this.shouldPred = shouldPred;
/*    */ 
/*    */     
/* 25 */     this.waitTime = 200;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 30 */     if (this.villager.isAITick() && this.navigator.hasVillage() && this.shouldPred.test(this.villager)) {
/* 31 */       return super.shouldExecute();
/*    */     }
/* 33 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldContinueExecuting() {
/* 39 */     if (this.waitRemaining > 0) {
/* 40 */       return true;
/*    */     }
/* 42 */     return super.shouldContinueExecuting();
/*    */   }
/*    */   
/*    */   protected abstract BlockPos getPatrolPoint();
/*    */   
/*    */   private BlockPos randomNearbyBlock(BlockPos pos, int xz) {
/* 48 */     return pos.add(this.villager.getRNG().nextInt(xz * 2) - xz, 0, this.villager.getRNG().nextInt(xz * 2) - xz);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockPos getDestinationBlock() {
/* 54 */     BlockPos point = getPatrolPoint();
/* 55 */     if (point != null && this.villager.getRNG().nextInt(4) > 0) {
/* 56 */       for (int i = 0; i < 20; i++) {
/* 57 */         BlockPos testBlock = randomNearbyBlock(point, this.distanceFromPoint);
/* 58 */         BasePathingNode baseNode = this.villager.getVillage().getPathingGraph().getNodeYRange(testBlock.getX(), testBlock.getY() - 5, testBlock.getY() + 5, testBlock.getZ());
/* 59 */         if (baseNode != null) {
/* 60 */           return baseNode.getBlockPos();
/*    */         }
/*    */       } 
/*    */     }
/*    */     
/* 65 */     return point;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onArrival() {
/* 70 */     this.waitRemaining = this.villager.getRNG().nextInt(this.waitTime) + this.waitTime;
/* 71 */     super.onArrival();
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 76 */     super.updateTask();
/* 77 */     if (hasArrived()) {
/* 78 */       this.waitRemaining--;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 84 */     this.waitRemaining = 0;
/* 85 */     super.startExecuting();
/*    */   }
/*    */ 
/*    */   
/*    */   void updateMovementMode() {
/* 90 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isNearWalkPos() {
/* 95 */     return (this.navigator.getPosition().distanceSq((Vec3i)this.destinationPos) < 4.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIPatrolPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */