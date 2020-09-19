/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.Vec3i;
/*    */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ public class EntityAIPatrolVillage extends EntityAIMoveToBlock {
/*    */   protected final EntityVillagerTek villager;
/*    */   
/*    */   public EntityAIPatrolVillage(EntityVillagerTek v, Predicate<EntityVillagerTek> shouldPred) {
/* 13 */     super((EntityVillageNavigator)v);
/* 14 */     this.villager = v;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 19 */     if (this.villager.isAITick() && this.villager.hasVillage() && this.villager.isWorkTime() && this.villager.getRNG().nextInt(10) == 0) {
/* 20 */       return super.shouldExecute();
/*    */     }
/*    */     
/* 23 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockPos getDestinationBlock() {
/* 28 */     BlockPos dest = this.villager.getVillage().getLastVillagerPos();
/* 29 */     return dest;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isNearWalkPos() {
/* 34 */     return (this.villager.getPosition().distanceSq((Vec3i)this.destinationPos) < 4.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   void updateMovementMode() {
/* 39 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onArrival() {
/* 45 */     this.villager.throttledSadness(-2);
/*    */     
/* 47 */     super.onArrival();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIPatrolVillage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */