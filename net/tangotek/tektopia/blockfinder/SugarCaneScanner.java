/*    */ package net.tangotek.tektopia.blockfinder;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ 
/*    */ public class SugarCaneScanner
/*    */   extends BlockScanner
/*    */ {
/*    */   public SugarCaneScanner(Village v, int scansPerTick) {
/* 14 */     super((Block)Blocks.REEDS, v, scansPerTick);
/*    */   }
/*    */ 
/*    */   
/*    */   public static BlockPos getCaneStalk(World w, BlockPos bp) {
/* 19 */     if (isCane(w.getBlockState(bp))) {
/*    */       
/*    */       do {
/* 22 */         bp = bp.down();
/* 23 */       } while (isCane(w.getBlockState(bp)));
/*    */ 
/*    */       
/* 26 */       Block downBlock = w.getBlockState(bp.down()).getBlock();
/* 27 */       if (downBlock == Blocks.GLOWSTONE) {
/* 28 */         return null;
/*    */       }
/*    */ 
/*    */       
/* 32 */       if (isCane(w.getBlockState(bp.up(2)))) {
/* 33 */         return bp.up();
/*    */       }
/*    */     } 
/* 36 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos testBlock(World w, BlockPos bp) {
/* 41 */     return getCaneStalk(w, bp);
/*    */   }
/*    */ 
/*    */   
/*    */   public void scanNearby(BlockPos bp) {
/* 46 */     for (BlockPos scanPos : BlockPos.getAllInBox(bp.getX() - 2, bp.getY(), bp.getZ() - 2, bp.getX() + 2, bp.getY(), bp.getZ() + 2))
/*    */     {
/* 48 */       scanBlock(scanPos);
/*    */     }
/*    */   }
/*    */   
/*    */   public static boolean isCane(IBlockState blockState) {
/* 53 */     return (blockState.getBlock() == Blocks.REEDS);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\blockfinder\SugarCaneScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */