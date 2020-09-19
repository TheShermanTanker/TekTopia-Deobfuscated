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
/*    */ public class EntityAIAttachLeadToLostAnimal
/*    */   extends EntityAIAttachLeadToAnimal
/*    */ {
/*    */   private int animalCheck;
/*    */   
/*    */   public EntityAIAttachLeadToLostAnimal(EntityVillagerTek v, Predicate<EntityVillagerTek> shouldPred) {
/* 22 */     super(v, shouldPred);
/*    */   }
/*    */ 
/*    */   
/*    */   protected EntityLivingBase getFollowTarget() {
/* 27 */     this.targetAnimal = null;
/* 28 */     if (this.villager.isAIFilterEnabled("return_lost_animals")) {
/* 29 */       this.animalCheck++;
/* 30 */       switch (this.animalCheck) {
/*    */         case 1:
/* 32 */           this.targetAnimal = findLostAnimal(VillageStructureType.COW_PEN);
/*    */           break;
/*    */         case 3:
/* 35 */           this.targetAnimal = findLostAnimal(VillageStructureType.SHEEP_PEN);
/*    */           break;
/*    */         case 5:
/* 38 */           this.targetAnimal = findLostAnimal(VillageStructureType.PIG_PEN);
/*    */           break;
/*    */         case 7:
/* 41 */           this.targetAnimal = findLostAnimal(VillageStructureType.CHICKEN_COOP);
/*    */           break;
/*    */         default:
/* 44 */           this.targetAnimal = null;
/*    */           break;
/*    */       } 
/* 47 */       if (this.animalCheck > 20) {
/* 48 */         this.animalCheck = 0;
/*    */       }
/*    */     } 
/* 51 */     return (EntityLivingBase)this.targetAnimal;
/*    */   }
/*    */   
/*    */   private EntityAnimal findLostAnimal(VillageStructureType structType) {
/* 55 */     List<VillageStructure> structList = this.villager.getVillage().getStructures(structType);
/* 56 */     if (!structList.isEmpty() && 
/* 57 */       structList.get(0) instanceof VillageStructureRancherPen) {
/* 58 */       VillageStructureRancherPen rancherPen = (VillageStructureRancherPen)structList.get(0);
/* 59 */       List<EntityAnimal> animalList = this.villager.world.getEntitiesWithinAABB(rancherPen.getAnimalClass(), this.villager.getEntityBoundingBox().grow(40.0D, 6.0D, 40.0D));
/*    */ 
/*    */       
/* 62 */       Collections.shuffle(animalList);
/* 63 */       for (EntityAnimal animal : animalList) {
/*    */ 
/*    */         
/* 66 */         if ((animal.getLeashHolder() == null || animal.getLeashHolder() instanceof net.tangotek.tektopia.entities.EntityMerchant) && !ModEntities.isTaggedEntity((Entity)animal, EntityTagType.BUTCHERED)) {
/* 67 */           boolean isLost = true;
/* 68 */           for (VillageStructure struct : structList) {
/* 69 */             if (struct.isBlockNear(animal.getPosition(), 1.5D)) {
/* 70 */               isLost = false;
/*    */             }
/*    */           } 
/* 73 */           if (isLost)
/*    */           {
/* 75 */             return animal;
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 82 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void updateMovementMode() {
/* 87 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIAttachLeadToLostAnimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */