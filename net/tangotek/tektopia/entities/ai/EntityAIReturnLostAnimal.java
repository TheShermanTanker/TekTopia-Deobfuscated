/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.passive.EntityAnimal;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.Vec3i;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ import net.tangotek.tektopia.structures.VillageStructure;
/*    */ import net.tangotek.tektopia.structures.VillageStructureRancherPen;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ 
/*    */ public class EntityAIReturnLostAnimal
/*    */   extends EntityAILeadAnimalToStructure
/*    */ {
/*    */   public EntityAIReturnLostAnimal(EntityVillagerTek v) {
/* 18 */     super(v, VillageStructureType.HOME2, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VillageStructure getDestinationStructure() {
/* 23 */     return getDestinationStructure(this.villager.getVillage(), this.villager.getLeadAnimal(), this.villager.getPosition());
/*    */   }
/*    */   
/*    */   public static VillageStructure getDestinationStructure(Village village, EntityAnimal animal, BlockPos pos) {
/* 27 */     VillageStructure destStructure = null;
/* 28 */     if (village != null && animal != null) {
/* 29 */       destStructure = getAnimalHome(village, animal, VillageStructureType.COW_PEN, pos);
/*    */       
/* 31 */       if (destStructure == null) {
/* 32 */         destStructure = getAnimalHome(village, animal, VillageStructureType.SHEEP_PEN, pos);
/*    */       }
/* 34 */       if (destStructure == null) {
/* 35 */         destStructure = getAnimalHome(village, animal, VillageStructureType.PIG_PEN, pos);
/*    */       }
/* 37 */       if (destStructure == null) {
/* 38 */         destStructure = getAnimalHome(village, animal, VillageStructureType.CHICKEN_COOP, pos);
/*    */       }
/*    */     } 
/* 41 */     return destStructure;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static VillageStructure getAnimalHome(Village village, EntityAnimal animal, VillageStructureType structureType, BlockPos pos) {
/* 47 */     Double min = Double.valueOf(Double.MAX_VALUE);
/* 48 */     VillageStructureRancherPen closestPen = null;
/*    */     
/* 50 */     List<VillageStructure> structList = village.getStructures(structureType);
/* 51 */     for (VillageStructure struct : structList) {
/* 52 */       if (struct instanceof VillageStructureRancherPen) {
/* 53 */         VillageStructureRancherPen pen = (VillageStructureRancherPen)struct;
/* 54 */         if (pen.isAnimalHome(animal)) {
/* 55 */           Double dist = Double.valueOf(pen.getDoor().distanceSq((Vec3i)pos));
/* 56 */           if (dist.doubleValue() < min.doubleValue()) {
/* 57 */             min = dist;
/* 58 */             closestPen = pen;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 64 */     return (VillageStructure)closestPen;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIReturnLostAnimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */