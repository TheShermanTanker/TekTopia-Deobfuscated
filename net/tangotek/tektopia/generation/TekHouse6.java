/*    */ package net.tangotek.tektopia.generation;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.BlockBed;
/*    */ import net.minecraft.block.BlockDoor;
/*    */ import net.minecraft.block.BlockFlowerPot;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*    */ import net.minecraft.world.gen.structure.StructureVillagePieces;
/*    */ import net.tangotek.tektopia.ModBlocks;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ public class TekHouse6 extends StructureVillagePieces.House3 {
/*    */   private int craftingIndex;
/*    */   private int villagersSpawned;
/*    */   
/*    */   public TekHouse6(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox bbox, EnumFacing facing) {
/* 23 */     super(start, type, rand, bbox, facing);
/* 24 */     this.craftingIndex = rand.nextInt(4);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TekHouse6() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
/* 34 */     boolean result = super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn);
/*    */     
/* 36 */     if (!this.isZombieInfested) {
/*    */       
/* 38 */       placeBedPiece(worldIn, structureBoundingBoxIn, 3, 1, 8, EnumFacing.NORTH, BlockBed.EnumPartType.FOOT);
/* 39 */       placeBedPiece(worldIn, structureBoundingBoxIn, 3, 1, 9, EnumFacing.NORTH, BlockBed.EnumPartType.HEAD);
/*    */       
/* 41 */       placeBedPiece(worldIn, structureBoundingBoxIn, 5, 1, 8, EnumFacing.NORTH, BlockBed.EnumPartType.FOOT);
/* 42 */       placeBedPiece(worldIn, structureBoundingBoxIn, 5, 1, 9, EnumFacing.NORTH, BlockBed.EnumPartType.HEAD);
/*    */       
/* 44 */       placeBedPiece(worldIn, structureBoundingBoxIn, 7, 1, 8, EnumFacing.NORTH, BlockBed.EnumPartType.FOOT);
/* 45 */       placeBedPiece(worldIn, structureBoundingBoxIn, 7, 1, 9, EnumFacing.NORTH, BlockBed.EnumPartType.HEAD);
/*    */       
/* 47 */       switch (this.craftingIndex) {
/*    */         case 0:
/* 49 */           setBlockState(worldIn, Blocks.CRAFTING_TABLE.getDefaultState(), 4, 1, 9, structureBoundingBoxIn);
/* 50 */           setBlockState(worldIn, Blocks.FLOWER_POT.getDefaultState().withProperty((IProperty)BlockFlowerPot.CONTENTS, (Comparable)BlockFlowerPot.EnumFlowerType.MUSHROOM_RED), 4, 2, 9, structureBoundingBoxIn);
/*    */           break;
/*    */         case 1:
/* 53 */           setBlockState(worldIn, Blocks.CRAFTING_TABLE.getDefaultState(), 6, 1, 9, structureBoundingBoxIn);
/*    */           break;
/*    */         case 2:
/* 56 */           setBlockState(worldIn, Blocks.CRAFTING_TABLE.getDefaultState(), 7, 1, 4, structureBoundingBoxIn);
/* 57 */           setBlockState(worldIn, Blocks.FLOWER_POT.getDefaultState().withProperty((IProperty)BlockFlowerPot.CONTENTS, (Comparable)BlockFlowerPot.EnumFlowerType.MUSHROOM_RED), 7, 2, 4, structureBoundingBoxIn);
/*    */           break;
/*    */         case 3:
/* 60 */           setBlockState(worldIn, Blocks.CRAFTING_TABLE.getDefaultState(), 7, 1, 2, structureBoundingBoxIn);
/*    */           break;
/*    */       } 
/*    */       
/* 64 */       if (randomIn.nextBoolean()) {
/* 65 */         setBlockState(worldIn, ModBlocks.blockChair.getDefaultState().withProperty((IProperty)BlockDoor.FACING, (Comparable)EnumFacing.SOUTH), 2, 1, 4, structureBoundingBoxIn);
/*    */       }
/* 67 */       if (randomIn.nextBoolean()) {
/* 68 */         setBlockState(worldIn, ModBlocks.blockChair.getDefaultState().withProperty((IProperty)BlockDoor.FACING, (Comparable)EnumFacing.NORTH), 4, 1, 1, structureBoundingBoxIn);
/*    */       }
/* 70 */       placeTorch(worldIn, EnumFacing.WEST, 7, 1, 6, structureBoundingBoxIn);
/*    */ 
/*    */       
/* 73 */       placeBedPiece(worldIn, structureBoundingBoxIn, 6, 1, 5, EnumFacing.EAST, BlockBed.EnumPartType.FOOT);
/* 74 */       placeBedPiece(worldIn, structureBoundingBoxIn, 7, 1, 5, EnumFacing.EAST, BlockBed.EnumPartType.HEAD);
/*    */       
/* 76 */       placeBedPiece(worldIn, structureBoundingBoxIn, 6, 1, 3, EnumFacing.EAST, BlockBed.EnumPartType.FOOT);
/* 77 */       placeBedPiece(worldIn, structureBoundingBoxIn, 7, 1, 3, EnumFacing.EAST, BlockBed.EnumPartType.HEAD);
/*    */       
/* 79 */       placeBedPiece(worldIn, structureBoundingBoxIn, 6, 1, 1, EnumFacing.EAST, BlockBed.EnumPartType.FOOT);
/* 80 */       placeBedPiece(worldIn, structureBoundingBoxIn, 7, 1, 1, EnumFacing.EAST, BlockBed.EnumPartType.HEAD);
/*    */     } 
/* 82 */     return result;
/*    */   }
/*    */   
/*    */   public BlockPos getBlockPos(int x, int y, int z) {
/* 86 */     return new BlockPos(getXWithOffset(x, z), getYWithOffset(y), getZWithOffset(x, z));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createVillageDoor(World w, StructureBoundingBox bb, Random rand, int x, int y, int z, EnumFacing facing) {
/* 91 */     super.createVillageDoor(w, bb, rand, x, y, z, facing);
/* 92 */     TekStructureVillagePieces.addStructureFrame(w, bb, getBlockPos(x, y, z), VillageStructureType.HOME6);
/*    */   }
/*    */   
/*    */   private void placeBedPiece(World worldIn, StructureBoundingBox bbox, int x, int y, int z, EnumFacing facing, BlockBed.EnumPartType partType) {
/* 96 */     setBlockState(worldIn, Blocks.AIR.getDefaultState(), x, y + 1, z, bbox);
/* 97 */     IBlockState bedState = Blocks.BED.getDefaultState().withProperty((IProperty)BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty((IProperty)BlockBed.FACING, (Comparable)facing);
/* 98 */     setBlockState(worldIn, bedState.withProperty((IProperty)BlockBed.PART, (Comparable)partType), x, y, z, bbox);
/*    */   }
/*    */   
/*    */   protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\generation\TekHouse6.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */