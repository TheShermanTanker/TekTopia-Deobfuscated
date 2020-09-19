/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import net.minecraft.entity.ai.EntityAIBase;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntityAIIdleCheck
/*    */   extends EntityAIBase
/*    */ {
/*    */   protected final EntityVillagerTek villager;
/* 14 */   private int idleTicks = 0;
/*    */ 
/*    */   
/*    */   public EntityAIIdleCheck(EntityVillagerTek v) {
/* 18 */     this.villager = v;
/* 19 */     setMutexBits(7);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 24 */     if (this.villager.isAITick() && this.villager.hasVillage()) {
/* 25 */       return true;
/*    */     }
/*    */     
/* 28 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 33 */     this.idleTicks = 0;
/*    */   }
/*    */   
/*    */   public boolean shouldContinueExecuting() {
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 42 */     this.idleTicks++;
/* 43 */     if (this.idleTicks % 80 == 0) {
/* 44 */       this.villager.setStoragePriority();
/*    */     }
/* 46 */     this.villager.setIdle(this.idleTicks);
/*    */     
/* 48 */     if (this.idleTicks % 1200 == 0) {
/* 49 */       this.villager.debugOut("Idle for " + (this.idleTicks / 20) + " seconds");
/*    */     }
/*    */   }
/*    */   
/*    */   public void resetTask() {
/* 54 */     this.villager.setIdle(0);
/* 55 */     if (this.idleTicks >= 1200)
/* 56 */       this.villager.debugOut(" was idle for " + (this.idleTicks / 20) + " seconds."); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIIdleCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */