/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.ai.EntityAITarget;
/*    */ import net.minecraft.entity.item.EntityArmorStand;
/*    */ import net.tangotek.tektopia.entities.EntityGuard;
/*    */ import net.tangotek.tektopia.structures.VillageStructure;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ public class EntityAITrainingTarget
/*    */   extends EntityAITarget
/*    */ {
/*    */   EntityGuard guard;
/*    */   EntityLivingBase targetEntity;
/*    */   VillageStructure structure;
/*    */   protected final Predicate<EntityGuard> shouldPred;
/*    */   
/*    */   public EntityAITrainingTarget(EntityGuard guard, Predicate<EntityGuard> shouldPred) {
/* 23 */     super((EntityCreature)guard, false, false);
/* 24 */     this.guard = guard;
/* 25 */     setMutexBits(1);
/* 26 */     this.shouldPred = shouldPred;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 34 */     if (this.guard.isAIFilterEnabled("practice_melee") && this.guard.hasVillage() && this.shouldPred.test(this.guard)) {
/* 35 */       List<VillageStructure> barracks = this.guard.getVillage().getStructures(VillageStructureType.BARRACKS);
/* 36 */       Collections.shuffle(barracks);
/* 37 */       for (VillageStructure str : barracks) {
/* 38 */         List<EntityArmorStand> armorStands = str.getEntitiesInside(EntityArmorStand.class);
/* 39 */         this.targetEntity = armorStands.stream().filter(a -> !str.isSpecialBlockOccupied(a.getPosition())).findAny().orElse(null);
/* 40 */         if (this.targetEntity != null) {
/* 41 */           this.structure = str;
/* 42 */           return true;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldContinueExecuting() {
/* 53 */     if (this.guard.wantsPractice() <= 0) {
/* 54 */       return false;
/*    */     }
/* 56 */     return super.shouldContinueExecuting();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected double getTargetDistance() {
/* 62 */     return 240.0D;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 70 */     this.guard.setAttackTarget(this.targetEntity);
/* 71 */     this.structure.occupySpecialBlock(this.targetEntity.getPosition());
/* 72 */     super.startExecuting();
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 77 */     super.resetTask();
/* 78 */     this.structure.vacateSpecialBlock(this.targetEntity.getPosition());
/* 79 */     this.targetEntity = null;
/* 80 */     this.structure = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAITrainingTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */