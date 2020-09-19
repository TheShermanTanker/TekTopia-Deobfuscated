/*    */ package net.tangotek.tektopia.blockfinder;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.block.BlockLeaves;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ 
/*    */ public class TreeScanner
/*    */   extends BlockScanner {
/*    */   public TreeScanner(Village v, int scansPerTick) {
/* 15 */     super(Blocks.LOG, v, scansPerTick);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockPos testBlock(World w, BlockPos bp) {
/* 21 */     IBlockState blockState = w.getBlockState(bp);
/* 22 */     if (isLeaf(blockState)) {
/* 23 */       return findTreeFromLeaf(w, bp);
/*    */     }
/*    */     
/* 26 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void scanNearby(BlockPos bp) {
/* 31 */     for (BlockPos scanPos : BlockPos.getAllInBox(bp.getX() - 7, bp.getY() + 2, bp.getZ() - 7, bp.getX() + 7, bp.getY() + 2, bp.getZ() + 7))
/*    */     {
/* 33 */       scanBlock(scanPos);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected BlockPos findTreeFromLeaf(World world, BlockPos leafPos) {
/* 40 */     for (BlockPos bp : BlockPos.getAllInBox(leafPos.getX() - 2, leafPos.getY() - 1, leafPos.getZ() - 2, leafPos.getX() + 2, leafPos.getY() - 1, leafPos.getZ() + 2)) {
/* 41 */       BlockPos treePos = treeTest(world, bp);
/* 42 */       if (treePos != null) {
/* 43 */         return treePos;
/*    */       }
/*    */     } 
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public static BlockPos treeTest(World world, BlockPos bp) {
/* 51 */     while (isLog(world.getBlockState(bp))) {
/* 52 */       bp = bp.down();
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 57 */       if (world.getBlockState(bp).getBlock() == Blocks.DIRT) {
/*    */         
/* 59 */         BlockPos treePos = bp.up();
/*    */ 
/*    */         
/* 62 */         bp = bp.up(3);
/* 63 */         for (int i = 0; i < 9; i++) {
/*    */           
/* 65 */           IBlockState westBlock = world.getBlockState(bp.west());
/* 66 */           IBlockState eastBlock = world.getBlockState(bp.east());
/* 67 */           if ((isLeaf(westBlock) || isLog(westBlock)) && (isLeaf(eastBlock) || isLog(eastBlock))) {
/* 68 */             return treePos;
/*    */           }
/* 70 */           bp = bp.up();
/*    */         } 
/* 72 */         return null;
/*    */       } 
/*    */     } 
/*    */     
/* 76 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isLog(IBlockState blockState) {
/* 81 */     return (blockState.getBlock() == Blocks.LOG || blockState.getBlock() == Blocks.LOG2);
/*    */   }
/*    */   
/*    */   public static boolean isLeaf(IBlockState blockState) {
/* 85 */     return ((blockState.getBlock() == Blocks.LEAVES || blockState.getBlock() == Blocks.LEAVES2) && ((Boolean)blockState.getValue((IProperty)BlockLeaves.DECAYABLE)).booleanValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\blockfinder\TreeScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */