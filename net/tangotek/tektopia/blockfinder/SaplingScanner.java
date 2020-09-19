/*    */ package net.tangotek.tektopia.blockfinder;
/*    */ 
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ 
/*    */ public class SaplingScanner
/*    */   extends BlockScanner
/*    */ {
/*    */   public SaplingScanner(Village v, int scansPerTick) {
/* 13 */     super(Blocks.SAPLING, v, scansPerTick);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockPos testBlock(World w, BlockPos bp) {
/* 19 */     IBlockState blockState = w.getBlockState(bp);
/* 20 */     if (isSapling(blockState)) {
/* 21 */       return bp;
/*    */     }
/*    */     
/* 24 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void scanNearby(BlockPos bp) {
/* 29 */     for (BlockPos scanPos : BlockPos.getAllInBox(bp.getX() - 7, bp.getY() - 2, bp.getZ() - 7, bp.getX() + 7, bp.getY() + 2, bp.getZ() + 7))
/*    */     {
/* 31 */       scanBlock(scanPos);
/*    */     }
/*    */   }
/*    */   
/*    */   public static boolean isSapling(IBlockState blockState) {
/* 36 */     return (blockState.getBlock() == Blocks.SAPLING);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\blockfinder\SaplingScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */