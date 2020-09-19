/*    */ package net.tangotek.tektopia.generation;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.BlockBed;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*    */ import net.minecraft.world.gen.structure.StructureVillagePieces;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ public class TekHouse2 extends StructureVillagePieces.WoodHut {
/*    */   private int villagersSpawned;
/*    */   
/*    */   public TekHouse2(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox bbox, EnumFacing facing) {
/* 19 */     super(start, type, rand, bbox, facing);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TekHouse2() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/* 29 */     boolean result = super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
/* 30 */     if (!this.isZombieInfested) {
/* 31 */       placeBedPiece(worldIn, structureBoundingBoxIn, 2, 1, 2, EnumFacing.NORTH, BlockBed.EnumPartType.FOOT);
/* 32 */       placeBedPiece(worldIn, structureBoundingBoxIn, 2, 1, 3, EnumFacing.NORTH, BlockBed.EnumPartType.HEAD);
/*    */       
/* 34 */       placeTorch(worldIn, EnumFacing.SOUTH, 1, 1, 3, structureBoundingBoxIn);
/*    */     } 
/* 36 */     return result;
/*    */   }
/*    */   
/*    */   public BlockPos getBlockPos(int x, int y, int z) {
/* 40 */     return new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createVillageDoor(World w, StructureBoundingBox bb, Random rand, int x, int y, int z, EnumFacing facing) {
/* 45 */     super.createVillageDoor(w, bb, rand, x, y, z, facing);
/* 46 */     TekStructureVillagePieces.addStructureFrame(w, bb, getBlockPos(x, y, z), VillageStructureType.HOME2);
/*    */   }
/*    */ 
/*    */   
/*    */   private void placeBedPiece(World worldIn, StructureBoundingBox bbox, int x, int y, int z, EnumFacing facing, BlockBed.EnumPartType partType) {
/* 51 */     IBlockState bedState = Blocks.BED.getDefaultState().withProperty((IProperty)BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty((IProperty)BlockBed.FACING, (Comparable)facing);
/* 52 */     setBlockState(worldIn, bedState.withProperty((IProperty)BlockBed.PART, (Comparable)partType), x, y, z, bbox);
/* 53 */     setBlockState(worldIn, Blocks.AIR.getDefaultState(), x, y + 1, z, bbox);
/*    */   }
/*    */   
/*    */   protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\generation\TekHouse2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */