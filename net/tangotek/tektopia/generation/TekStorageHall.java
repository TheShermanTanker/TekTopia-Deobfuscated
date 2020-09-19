/*    */ package net.tangotek.tektopia.generation;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*    */ import net.minecraft.world.gen.structure.StructureVillagePieces;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TekStorageHall
/*    */   extends StructureVillagePieces.House3
/*    */ {
/*    */   public TekStorageHall(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox bbox, EnumFacing facing) {
/* 20 */     super(start, type, rand, bbox, facing);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TekStorageHall() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/* 30 */     boolean result = super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
/* 31 */     if (!this.isZombieInfested) {
/* 32 */       setBlockState(worldIn, Blocks.CRAFTING_TABLE.getDefaultState(), 1, 1, 4, structureBoundingBoxIn);
/* 33 */       placeTorch(worldIn, EnumFacing.WEST, 7, 1, 6, structureBoundingBoxIn);
/*    */       
/* 35 */       generateChest(worldIn, structureBoundingBoxIn, randomIn, 4, 1, 9, TekVillager.VILLAGE_STORAGE);
/* 36 */       generateChest(worldIn, structureBoundingBoxIn, randomIn, 6, 1, 9, TekVillager.VILLAGE_STORAGE);
/* 37 */       generateChest(worldIn, structureBoundingBoxIn, randomIn, 2, 1, 4, TekVillager.VILLAGE_STORAGE);
/*    */     } 
/* 39 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count) {}
/*    */ 
/*    */   
/*    */   public BlockPos getBlockPos(int x, int y, int z) {
/* 48 */     return new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createVillageDoor(World w, StructureBoundingBox bb, Random rand, int x, int y, int z, EnumFacing facing) {
/* 53 */     super.createVillageDoor(w, bb, rand, x, y, z, facing);
/* 54 */     TekStructureVillagePieces.addStructureFrame(w, bb, getBlockPos(x, y, z), VillageStructureType.STORAGE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\generation\TekStorageHall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */