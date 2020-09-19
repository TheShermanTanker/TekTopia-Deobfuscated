/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityAnimal;
/*    */ import net.tangotek.tektopia.EntityTagType;
/*    */ import net.tangotek.tektopia.ModEntities;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ import net.tangotek.tektopia.structures.VillageStructure;
/*    */ import net.tangotek.tektopia.structures.VillageStructureRancherPen;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ public class EntityAIAttachLeadToButcherAnimal
/*    */   extends EntityAIAttachLeadToAnimal {
/*    */   public EntityAIAttachLeadToButcherAnimal(EntityVillagerTek v, Predicate<EntityVillagerTek> shouldPred) {
/* 19 */     super(v, shouldPred);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 25 */     if (super.shouldExecute()) {
/* 26 */       VillageStructure struct = this.villager.getVillage().getNearestStructure(VillageStructureType.BUTCHER, this.villager.getPosition());
/* 27 */       if (struct != null && 
/* 28 */         struct.getEntitiesInside(EntityAnimal.class).size() <= 4) {
/* 29 */         return true;
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 34 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected EntityLivingBase getFollowTarget() {
/* 40 */     int rnd = this.villager.ticksExisted % 4;
/* 41 */     switch (rnd)
/*    */     { case 0:
/* 43 */         this.targetAnimal = findButcherAnimal(VillageStructureType.COW_PEN, "retrieve_cows");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 58 */         return (EntityLivingBase)this.targetAnimal;case 1: this.targetAnimal = findButcherAnimal(VillageStructureType.SHEEP_PEN, "retrieve_sheep"); return (EntityLivingBase)this.targetAnimal;case 2: this.targetAnimal = findButcherAnimal(VillageStructureType.PIG_PEN, "retrieve_pigs"); return (EntityLivingBase)this.targetAnimal;case 3: this.targetAnimal = findButcherAnimal(VillageStructureType.CHICKEN_COOP, "retrieve_chickens"); return (EntityLivingBase)this.targetAnimal; }  this.targetAnimal = null; return (EntityLivingBase)this.targetAnimal;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void attachLeash() {
/* 63 */     super.attachLeash();
/*    */   }
/*    */ 
/*    */   
/*    */   private EntityAnimal findButcherAnimal(VillageStructureType structType, String aiFilter) {
/* 68 */     if (this.villager.isAIFilterEnabled(aiFilter)) {
/* 69 */       List<VillageStructure> structures = this.villager.getVillage().getStructures(structType);
/* 70 */       Collections.shuffle(structures);
/* 71 */       for (VillageStructure struct : structures) {
/* 72 */         VillageStructureRancherPen rancherPen = (VillageStructureRancherPen)struct;
/* 73 */         List<EntityAnimal> animalList = rancherPen.getEntitiesInside(rancherPen.getAnimalClass());
/* 74 */         if (rancherPen.isPenFull(0.75F)) {
/* 75 */           Collections.shuffle(animalList);
/* 76 */           EntityAnimal villagerAnimal = null;
/* 77 */           for (EntityAnimal animal : animalList) {
/* 78 */             if (!animal.isChild()) {
/*    */               
/* 80 */               if (!ModEntities.isTaggedEntity((Entity)animal, EntityTagType.VILLAGER))
/* 81 */                 return animal; 
/* 82 */               if (villagerAnimal == null) {
/* 83 */                 villagerAnimal = animal;
/*    */               }
/*    */             } 
/*    */           } 
/* 87 */           if (villagerAnimal != null) {
/* 88 */             return villagerAnimal;
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 94 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void updateMovementMode() {
/* 99 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIAttachLeadToButcherAnimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */