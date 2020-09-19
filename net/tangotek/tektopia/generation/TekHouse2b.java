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
/*    */ public class TekHouse2b
/*    */   extends StructureVillagePieces.House4Garden {
/*    */   private int villagersSpawned;
/*    */   
/*    */   public TekHouse2b(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox bbox, EnumFacing facing) {
/* 20 */     super(start, type, rand, bbox, facing);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TekHouse2b() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/* 30 */     boolean result = super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
/* 31 */     if (!this.isZombieInfested) {
/* 32 */       placeBedPiece(worldIn, structureBoundingBoxIn, 1, 1, 2, EnumFacing.NORTH, BlockBed.EnumPartType.FOOT);
/* 33 */       placeBedPiece(worldIn, structureBoundingBoxIn, 1, 1, 3, EnumFacing.NORTH, BlockBed.EnumPartType.HEAD);
/*    */       
/* 35 */       placeBedPiece(worldIn, structureBoundingBoxIn, 3, 1, 2, EnumFacing.NORTH, BlockBed.EnumPartType.FOOT);
/* 36 */       placeBedPiece(worldIn, structureBoundingBoxIn, 3, 1, 3, EnumFacing.NORTH, BlockBed.EnumPartType.HEAD);
/*    */       
/* 38 */       createVillageDoor(worldIn, structureBoundingBoxIn, randomIn, 2, 1, 0, EnumFacing.NORTH);
/*    */     } 
/* 40 */     return result;
/*    */   }
/*    */   
/*    */   public BlockPos getBlockPos(int x, int y, int z) {
/* 44 */     return new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createVillageDoor(World w, StructureBoundingBox bb, Random rand, int x, int y, int z, EnumFacing facing) {
/* 49 */     super.createVillageDoor(w, bb, rand, x, y, z, facing);
/* 50 */     TekStructureVillagePieces.addStructureFrame(w, bb, getBlockPos(x, y, z), VillageStructureType.HOME2);
/*    */   }
/*    */ 
/*    */   
/*    */   private void placeBedPiece(World worldIn, StructureBoundingBox bbox, int x, int y, int z, EnumFacing facing, BlockBed.EnumPartType partType) {
/* 55 */     IBlockState bedState = Blocks.BED.getDefaultState().withProperty((IProperty)BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty((IProperty)BlockBed.FACING, (Comparable)facing);
/* 56 */     setBlockState(worldIn, bedState.withProperty((IProperty)BlockBed.PART, (Comparable)partType), x, y, z, bbox);
/* 57 */     setBlockState(worldIn, Blocks.AIR.getDefaultState(), x, y + 1, z, bbox);
/*    */   }
/*    */   
/*    */   protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\generation\TekHouse2b.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */