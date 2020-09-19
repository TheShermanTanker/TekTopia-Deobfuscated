/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.ai.EntityAIBase;
/*    */ import net.tangotek.tektopia.ModItems;
/*    */ import net.tangotek.tektopia.entities.EntityGuard;
/*    */ 
/*    */ public class EntityAISalute
/*    */   extends EntityAIBase {
/*    */   private EntityGuard guard;
/*    */   private EntityLivingBase saluteTarget;
/* 14 */   private long lastSalute = 0L;
/* 15 */   private int saluteTime = 0;
/*    */   
/*    */   public EntityAISalute(EntityGuard v) {
/* 18 */     this.guard = v;
/* 19 */     setMutexBits(1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 25 */     if (this.guard.isAITick("salute") && this.guard.world.getTotalWorldTime() - this.lastSalute > 2400L && !this.guard.isSleeping() && this.guard.getAttackTarget() == null && 
/* 26 */       this.guard.hasVillage() && !this.guard.getVillage().enemySeenRecently()) {
/*    */       
/* 28 */       this.saluteTarget = (EntityLivingBase)this.guard.world.getClosestPlayerToEntity((Entity)this.guard, 12.0D);
/* 29 */       if (this.saluteTarget != null) {
/* 30 */         return true;
/*    */       }
/*    */       
/* 33 */       if (!this.guard.isCaptain()) {
/*    */         
/* 35 */         List<EntityGuard> captains = this.guard.world.getEntitiesWithinAABB(EntityGuard.class, this.guard.getEntityBoundingBox().grow(12.0D), g -> g.isCaptain());
/* 36 */         if (!captains.isEmpty()) {
/* 37 */           this.saluteTarget = (EntityLivingBase)captains.get(0);
/* 38 */           return true;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 50 */     startSalute();
/* 51 */     super.startExecuting();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldContinueExecuting() {
/* 57 */     return (this.saluteTime > 0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 63 */     if (this.saluteTime > 0) {
/* 64 */       if (this.saluteTime == 10) {
/* 65 */         this.guard.stopServerAnimation("villager_salute");
/*    */         
/* 67 */         if (this.guard.getRNG().nextInt(3) == 0) {
/* 68 */           this.guard.modifyHappy(2);
/*    */         }
/*    */       } 
/* 71 */       this.guard.faceEntity((Entity)this.saluteTarget, 30.0F, 30.0F);
/* 72 */       this.saluteTime--;
/*    */     } 
/*    */     
/* 75 */     super.updateTask();
/*    */   }
/*    */ 
/*    */   
/*    */   private void startSalute() {
/* 80 */     this.lastSalute = this.guard.world.getTotalWorldTime();
/* 81 */     this.saluteTime = 50;
/* 82 */     this.guard.getNavigator().clearPath();
/* 83 */     this.guard.equipActionItem(ModItems.EMPTY_HAND_ITEM);
/* 84 */     this.guard.playServerAnimation("villager_salute");
/*    */   }
/*    */ 
/*    */   
/*    */   private void stopSalute() {
/* 89 */     this.guard.unequipActionItem(ModItems.EMPTY_HAND_ITEM);
/* 90 */     this.guard.stopServerAnimation("villager_salute");
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 95 */     stopSalute();
/* 96 */     this.saluteTime = 0;
/* 97 */     super.resetTask();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAISalute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */