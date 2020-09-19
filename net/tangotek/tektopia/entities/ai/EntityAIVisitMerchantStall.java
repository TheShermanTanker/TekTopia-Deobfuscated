/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.tangotek.tektopia.entities.EntityMerchant;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ import net.tangotek.tektopia.structures.VillageStructure;
/*    */ import net.tangotek.tektopia.structures.VillageStructureMerchantStall;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ public class EntityAIVisitMerchantStall
/*    */   extends EntityAIPatrolPoint
/*    */ {
/*    */   protected final EntityMerchant merchant;
/*    */   protected VillageStructureMerchantStall merchantStall;
/*    */   
/*    */   public EntityAIVisitMerchantStall(EntityMerchant v, Predicate<EntityVillagerTek> shouldPred, int distanceFromPoint, int waitTime) {
/* 20 */     super((EntityVillagerTek)v, shouldPred, distanceFromPoint, waitTime);
/* 21 */     this.merchant = v;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockPos getPatrolPoint() {
/* 27 */     List<VillageStructure> stalls = this.villager.getVillage().getStructures(VillageStructureType.MERCHANT_STALL);
/* 28 */     Collections.shuffle(stalls);
/*    */     
/* 30 */     if (!stalls.isEmpty()) {
/* 31 */       this.merchantStall = (VillageStructureMerchantStall)stalls.get(0);
/*    */     }
/*    */     
/* 34 */     if (this.merchantStall != null) {
/* 35 */       return this.merchantStall.getDoor();
/*    */     }
/* 37 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 42 */     this.merchant.setStall(1);
/* 43 */     super.startExecuting();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIVisitMerchantStall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */