/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.ai.EntityAITarget;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ import net.tangotek.tektopia.structures.VillageStructure;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ public class EntityAIKillInStructure
/*    */   extends EntityAITarget {
/*    */   private final EntityVillagerTek villager;
/*    */   private final VillageStructureType structureType;
/*    */   private final Class<? extends EntityLivingBase> clazz;
/*    */   private EntityLivingBase targetEntity;
/*    */   private final Predicate<EntityVillagerTek> shouldPred;
/*    */   private final Predicate<EntityLivingBase> targetPred;
/*    */   
/*    */   public EntityAIKillInStructure(EntityVillagerTek villager, VillageStructureType structureType, Class<? extends EntityLivingBase> clazz, Predicate<EntityLivingBase> isTargetPred, Predicate<EntityVillagerTek> shouldPred) {
/* 22 */     super((EntityCreature)villager, false, false);
/* 23 */     this.villager = villager;
/* 24 */     this.structureType = structureType;
/* 25 */     this.clazz = clazz;
/* 26 */     setMutexBits(1);
/* 27 */     this.shouldPred = shouldPred;
/* 28 */     this.targetPred = isTargetPred;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 36 */     if (this.villager.isAITick() && this.villager.hasVillage() && this.shouldPred.test(this.villager)) {
/* 37 */       this.targetEntity = findTarget();
/* 38 */       return (this.targetEntity != null);
/*    */     } 
/*    */     
/* 41 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   private EntityLivingBase findTarget() {
/* 46 */     List<VillageStructure> structures = this.villager.getVillage().getStructures(this.structureType);
/* 47 */     for (VillageStructure struct : structures) {
/* 48 */       List<EntityLivingBase> entList = struct.getEntitiesInside(this.clazz);
/* 49 */       entList.removeIf(this.targetPred.negate());
/* 50 */       for (EntityLivingBase baseEnt : entList) {
/* 51 */         if (!baseEnt.isChild() && isSuitableTarget(baseEnt, false)) {
/* 52 */           return baseEnt;
/*    */         }
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 61 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldContinueExecuting() {
/* 66 */     boolean shouldContinue = super.shouldContinueExecuting();
/* 67 */     if (!shouldContinue && this.villager.hasVillage()) {
/* 68 */       this.targetEntity = findTarget();
/* 69 */       if (this.targetEntity != null) {
/* 70 */         this.taskOwner.setAttackTarget(this.targetEntity);
/* 71 */         shouldContinue = true;
/*    */       } 
/*    */     } 
/*    */     
/* 75 */     return shouldContinue;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 83 */     this.villager.setAttackTarget(this.targetEntity);
/* 84 */     this.villager.throttledSadness(-5);
/* 85 */     super.startExecuting();
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 90 */     this.targetEntity = null;
/* 91 */     super.resetTask();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIKillInStructure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */