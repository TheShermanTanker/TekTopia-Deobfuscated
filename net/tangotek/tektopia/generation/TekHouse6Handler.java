/*    */ package net.tangotek.tektopia.generation;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*    */ import net.minecraft.world.gen.structure.StructureComponent;
/*    */ import net.minecraft.world.gen.structure.StructureVillagePieces;
/*    */ import net.minecraftforge.fml.common.registry.VillagerRegistry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TekHouse6Handler
/*    */   implements VillagerRegistry.IVillageCreationHandler
/*    */ {
/*    */   public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random parRandom, int size) {
/* 19 */     System.out.println("Getting village TekHouse6 piece weight");
/*    */     
/* 21 */     return new StructureVillagePieces.PieceWeight(getComponentClass(), 100, 2 + size * 2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<? extends StructureVillagePieces.Village> getComponentClass() {
/* 27 */     return (Class)TekHouse6.class;
/*    */   }
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight parPieceWeight, StructureVillagePieces.Start parStart, List<StructureComponent> parPiecesList, Random parRand, int parMinX, int parMinY, int parMinZ, EnumFacing parFacing, int parType) {
/* 50 */     System.out.println("TekHouse6 buildComponent() at " + parMinX + ", " + parMinY + ", " + parMinZ);
/*    */     
/* 52 */     StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(parMinX, parMinY, parMinZ, 0, 0, 0, 9, 7, 12, parFacing);
/* 53 */     return (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(parPiecesList, structureboundingbox) == null) ? (StructureVillagePieces.Village)new TekHouse6(parStart, parType, parRand, structureboundingbox, parFacing) : null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected static boolean canVillageGoDeeper(StructureBoundingBox structurebb) {
/* 58 */     return (structurebb != null && structurebb.minY > 10);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\generation\TekHouse6Handler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */