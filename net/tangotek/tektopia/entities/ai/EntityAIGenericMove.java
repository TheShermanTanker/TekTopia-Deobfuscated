/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.Vec3i;
/*    */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ public class EntityAIGenericMove extends EntityAIMoveToBlock {
/*    */   protected final Function<EntityVillagerTek, BlockPos> whereFunc;
/*    */   protected final Predicate<EntityVillagerTek> shouldPred;
/*    */   protected final Runnable resetRunner;
/*    */   protected final Runnable startRunner;
/*    */   protected final EntityVillagerTek villager;
/*    */   protected final EntityVillagerTek.MovementMode moveMode;
/*    */   
/*    */   public EntityAIGenericMove(EntityVillagerTek v, Predicate<EntityVillagerTek> shouldPred, Function<EntityVillagerTek, BlockPos> whereFunc, EntityVillagerTek.MovementMode moveMode, Runnable startRunner, Runnable resetRunner) {
/* 19 */     super((EntityVillageNavigator)v);
/* 20 */     this.villager = v;
/* 21 */     this.shouldPred = shouldPred;
/* 22 */     this.whereFunc = whereFunc;
/* 23 */     this.resetRunner = resetRunner;
/* 24 */     this.startRunner = startRunner;
/* 25 */     this.moveMode = moveMode;
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
/*    */   protected BlockPos getDestinationBlock() {
/* 38 */     BlockPos dest = this.whereFunc.apply(this.villager);
/* 39 */     return dest;
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 44 */     if (this.startRunner != null) {
/* 45 */       this.startRunner.run();
/*    */     }
/* 47 */     super.startExecuting();
/*    */   }
/*    */ 
/*    */   
/*    */   void updateMovementMode() {
/* 52 */     this.villager.setMovementMode(this.moveMode);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isNearWalkPos() {
/* 57 */     return (this.navigator.getPosition().distanceSq((Vec3i)this.destinationPos) < 4.0D);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onArrival() {
/* 63 */     super.onArrival();
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 68 */     if (this.resetRunner != null) {
/* 69 */       this.resetRunner.run();
/*    */     }
/* 71 */     super.resetTask();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIGenericMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */