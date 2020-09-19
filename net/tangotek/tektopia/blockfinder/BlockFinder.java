/*    */ package net.tangotek.tektopia.blockfinder;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlockFinder
/*    */ {
/* 13 */   private int debugTick = 100;
/* 14 */   private Map<Block, BlockScanner> scanners = new HashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerBlockScanner(BlockScanner blockScanner) {
/* 20 */     this.scanners.put(blockScanner.getScanBlock(), blockScanner);
/*    */   }
/*    */   
/*    */   public boolean hasBlock(Block block) {
/* 24 */     BlockScanner scanner = this.scanners.get(block);
/* 25 */     return (scanner != null && scanner.hasBlocks());
/*    */   }
/*    */   
/*    */   public BlockPos requestBlock(Block block) {
/* 29 */     BlockScanner scanner = this.scanners.get(block);
/* 30 */     if (scanner != null && scanner.hasBlocks()) {
/* 31 */       BlockPos bp = scanner.requestBlock();
/* 32 */       return bp;
/*    */     } 
/*    */     
/* 35 */     return null;
/*    */   }
/*    */   
/*    */   public void releaseClaim(World world, Block block, BlockPos bp) {
/* 39 */     BlockScanner scanner = this.scanners.get(block);
/* 40 */     if (scanner != null) {
/* 41 */       scanner.releaseClaim(bp);
/*    */     }
/*    */   }
/*    */   
/*    */   public int getBlockCount(Block b) {
/* 46 */     BlockScanner scanner = this.scanners.get(b);
/* 47 */     if (scanner != null) {
/* 48 */       return scanner.getBlockCount();
/*    */     }
/*    */     
/* 51 */     return 0;
/*    */   }
/*    */   
/*    */   public void update() {
/* 55 */     this.scanners.forEach((k, v) -> v.update());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void debugOut() {
/* 64 */     this.scanners.forEach((k, v) -> System.out.println("    Block Finder: [" + k.getLocalizedName() + "]  " + v.getBlockCount()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\blockfinder\BlockFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */