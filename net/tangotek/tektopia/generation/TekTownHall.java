/*    */ package net.tangotek.tektopia.generation;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.BlockDoor;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*    */ import net.minecraft.world.gen.structure.StructureVillagePieces;
/*    */ import net.tangotek.tektopia.ModBlocks;
/*    */ import net.tangotek.tektopia.caps.IVillageData;
/*    */ import net.tangotek.tektopia.caps.VillageDataProvider;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ public class TekTownHall
/*    */   extends StructureVillagePieces.House1 {
/*    */   public TekTownHall(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox p_i45571_4_, EnumFacing facing) {
/* 21 */     super(start, type, rand, p_i45571_4_, facing);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TekTownHall() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/* 31 */     boolean result = super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
/*    */ 
/*    */     
/* 34 */     if (!this.isZombieInfested) {
/* 35 */       setBlockState(worldIn, Blocks.AIR.getDefaultState(), 6, 1, 4, structureBoundingBoxIn);
/* 36 */       setBlockState(worldIn, Blocks.AIR.getDefaultState(), 5, 1, 4, structureBoundingBoxIn);
/* 37 */       setBlockState(worldIn, Blocks.AIR.getDefaultState(), 4, 1, 4, structureBoundingBoxIn);
/* 38 */       setBlockState(worldIn, Blocks.AIR.getDefaultState(), 3, 1, 4, structureBoundingBoxIn);
/*    */       
/* 40 */       setBlockState(worldIn, Blocks.AIR.getDefaultState(), 7, 1, 4, structureBoundingBoxIn);
/* 41 */       setBlockState(worldIn, Blocks.AIR.getDefaultState(), 7, 1, 3, structureBoundingBoxIn);
/* 42 */       setBlockState(worldIn, Blocks.AIR.getDefaultState(), 7, 1, 2, structureBoundingBoxIn);
/*    */       
/* 44 */       setBlockState(worldIn, ModBlocks.blockChair.getDefaultState().withProperty((IProperty)BlockDoor.FACING, (Comparable)EnumFacing.WEST), 7, 1, 3, structureBoundingBoxIn);
/*    */       
/* 46 */       setBlockState(worldIn, ModBlocks.blockChair.getDefaultState().withProperty((IProperty)BlockDoor.FACING, (Comparable)EnumFacing.SOUTH), 4, 1, 4, structureBoundingBoxIn);
/*    */     } 
/* 48 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count) {}
/*    */ 
/*    */   
/*    */   public BlockPos getBlockPos(int x, int y, int z) {
/* 57 */     return new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createVillageDoor(World w, StructureBoundingBox bb, Random rand, int x, int y, int z, EnumFacing facing) {
/* 62 */     super.createVillageDoor(w, bb, rand, x, y, z, facing);
/* 63 */     EntityItemFrame itemFrame = TekStructureVillagePieces.addStructureFrame(w, bb, getBlockPos(x, y, z), VillageStructureType.TOWNHALL);
/* 64 */     if (itemFrame != null) {
/* 65 */       IVillageData vd = (IVillageData)itemFrame.getDisplayedItem().getCapability(VillageDataProvider.VILLAGE_DATA_CAPABILITY, null);
/* 66 */       if (vd != null)
/* 67 */         vd.skipStartingGifts(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\generation\TekTownHall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */