/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.tangotek.tektopia.ModSoundEvents;
/*    */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntityAITavernVisit
/*    */   extends EntityAIWanderStructure
/*    */ {
/*    */   public EntityAITavernVisit(EntityVillagerTek v, Predicate<EntityVillagerTek> shouldPred) {
/* 14 */     super(v, EntityVillagerTek.findLocalTavern(), shouldPred, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 19 */     if (super.shouldExecute() && this.villager.isAIFilterEnabled("visit_tavern")) {
/* 20 */       return true;
/*    */     }
/*    */     
/* 23 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldContinueExecuting() {
/* 29 */     if (this.villager.isWorkTime() || this.villager.shouldSleep() || this.villager.getHappy() >= this.villager.getMaxHappy()) {
/* 30 */       return false;
/*    */     }
/*    */     
/* 33 */     return super.shouldContinueExecuting();
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 38 */     super.updateTask();
/*    */     
/* 40 */     if (hasArrived())
/*    */     {
/* 42 */       if (this.villager.isSitting()) {
/* 43 */         if (this.villager.getRNG().nextInt(300) == 0) {
/* 44 */           this.villager.modifyHappy(1);
/*    */           
/* 46 */           if (this.villager.world.getEntitiesWithinAABB(EntityVillageNavigator.class, this.villager.getEntityBoundingBox().grow(8.0D)).size() > 1) {
/* 47 */             this.villager.playSound(ModSoundEvents.villagerSocialize, 0.4F + this.villager.getRNG().nextFloat() * 0.7F, this.villager.getRNG().nextFloat() * 0.4F + 0.8F);
/*    */           }
/* 49 */           if (this.villager.getRNG().nextInt(5) == 0) {
/* 50 */             this.villager.cheerBeer(2);
/*    */           }
/*    */         }
/*    */       
/* 54 */       } else if (this.villager.getRNG().nextInt(400) == 0) {
/* 55 */         this.villager.modifyHappy(1);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAITavernVisit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */