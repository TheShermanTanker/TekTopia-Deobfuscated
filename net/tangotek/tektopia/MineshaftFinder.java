/*    */ package net.tangotek.tektopia;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.BiPredicate;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ import net.tangotek.tektopia.structures.VillageStructure;
/*    */ import net.tangotek.tektopia.structures.VillageStructureMineshaft;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ public class MineshaftFinder
/*    */ {
/*    */   protected World world;
/*    */   private Village village;
/* 16 */   private int debugTick = 100;
/*    */   
/*    */   public MineshaftFinder(World w, Village v) {
/* 19 */     this.world = w;
/* 20 */     this.village = v;
/*    */   }
/*    */   
/*    */   public VillageStructureMineshaft requestMineshaft(EntityVillagerTek miner, Predicate<VillageStructureMineshaft> pred, BiPredicate<VillageStructureMineshaft, VillageStructureMineshaft> compare) {
/* 24 */     VillageStructureMineshaft bestTunnel = null;
/* 25 */     List<VillageStructure> mineshafts = this.village.getStructures(VillageStructureType.MINESHAFT);
/* 26 */     for (VillageStructure struct : mineshafts) {
/* 27 */       VillageStructureMineshaft mine = (VillageStructureMineshaft)struct;
/* 28 */       if (pred.test(mine)) {
/* 29 */         if (mine.getTunnelMiner() == miner)
/* 30 */           return mine; 
/* 31 */         if (mine.getTunnelMiner() == null && (
/* 32 */           bestTunnel == null || compare.test(mine, bestTunnel))) {
/* 33 */           bestTunnel = mine;
/*    */         }
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 39 */     return bestTunnel;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void update() {}
/*    */ 
/*    */ 
/*    */   
/*    */   private void debugInfo() {
/* 50 */     List<VillageStructure> mineshafts = this.village.getStructures(VillageStructureType.MINESHAFT);
/* 51 */     this.village.debugOut("Mineshafts: " + mineshafts.size());
/* 52 */     int i = 1;
/* 53 */     for (VillageStructure struct : mineshafts) {
/* 54 */       VillageStructureMineshaft mine = (VillageStructureMineshaft)struct;
/* 55 */       this.village.debugOut("       #" + i + "  Door=" + mine.getDoor() + "     Len=" + mine.getTunnelLength());
/*    */     } 
/*    */     
/* 58 */     this.debugTick = 140;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\MineshaftFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */