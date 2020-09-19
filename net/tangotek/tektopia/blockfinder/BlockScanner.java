/*     */ package net.tangotek.tektopia.blockfinder;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.Village;
/*     */ 
/*     */ public abstract class BlockScanner {
/*  14 */   private Random rng = new Random(); protected final Block scanBlock;
/*  15 */   private long tickCount = 0L;
/*  16 */   private long releaseTick = 0L;
/*  17 */   private List<BlockPos> recentBlocks = new LinkedList<>();
/*  18 */   private Map<BlockPos, Long> claimedBlocks = new HashMap<>(); protected Village village; private final int scansPerTick;
/*     */   protected Queue<BlockPos> scannedBlocks;
/*     */   
/*     */   public BlockScanner(Block scanBlock, Village village, int scansPerTick) {
/*  22 */     this.scannedBlocks = new PriorityQueue<>(50, Comparator.comparingInt(a -> (int)a.distanceSq((Vec3i)this.village.getCenter())));
/*     */ 
/*     */     
/*  25 */     this.scanBlock = scanBlock;
/*  26 */     this.village = village;
/*  27 */     this.scansPerTick = scansPerTick;
/*     */   }
/*     */   public Block getScanBlock() {
/*  30 */     return this.scanBlock;
/*     */   }
/*     */   public void update() {
/*  33 */     this.tickCount++;
/*     */ 
/*     */     
/*  36 */     if (!this.recentBlocks.isEmpty()) {
/*  37 */       BlockPos recent = this.recentBlocks.remove(0);
/*  38 */       scanNearby(recent);
/*     */     } 
/*     */     
/*  41 */     for (int i = 0; i < this.scansPerTick; i++) {
/*  42 */       scanRandomBlock(this.rng.nextFloat());
/*     */     }
/*  44 */     if (this.releaseTick-- < 0L) {
/*  45 */       this.releaseTick = 100L;
/*  46 */       releaseClaimedBlocks();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasBlocks() {
/*  51 */     return !this.scannedBlocks.isEmpty();
/*     */   }
/*     */   
/*     */   public int getBlockCount() {
/*  55 */     return this.scannedBlocks.size();
/*     */   }
/*     */   
/*     */   public BlockPos requestBlock() {
/*  59 */     while (!this.scannedBlocks.isEmpty()) {
/*  60 */       BlockPos bp = this.scannedBlocks.poll();
/*  61 */       if (this.village.getWorld().getBlockState(bp).getBlock().equals(this.scanBlock)) {
/*  62 */         this.claimedBlocks.put(bp, Long.valueOf(this.tickCount));
/*  63 */         return bp;
/*     */       } 
/*     */     } 
/*     */     
/*  67 */     return null;
/*     */   }
/*     */   
/*     */   public void releaseClaim(BlockPos bp) {
/*  71 */     this.claimedBlocks.remove(bp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void scanRandomBlock(float mod) {
/*  79 */     int radius = Math.max((int)(this.village.getSize() * mod), 20);
/*  80 */     int vertOffset = (int)(20.0F * mod) + 5;
/*  81 */     int X = this.village.getCenter().getX() + radius - this.rng.nextInt(radius * 2);
/*  82 */     int Y = MathHelper.getInt(this.rng, (int)(this.village.getAABB()).minY - vertOffset, (int)(this.village.getAABB()).maxY + vertOffset);
/*  83 */     int Z = this.village.getCenter().getZ() + radius - this.rng.nextInt(radius * 2);
/*  84 */     scanBlock(new BlockPos(X, Y, Z));
/*     */   }
/*     */   
/*     */   protected void scanBlock(BlockPos testPos) {
/*  88 */     if (this.village.isInVillage(testPos)) {
/*  89 */       BlockPos targetPos = testBlock(this.village.getWorld(), testPos);
/*  90 */       if (targetPos != null && 
/*  91 */         !this.scannedBlocks.contains(targetPos))
/*     */       {
/*  93 */         if (!this.claimedBlocks.containsKey(targetPos)) {
/*     */ 
/*     */           
/*  96 */           this.scannedBlocks.add(targetPos);
/*  97 */           this.recentBlocks.add(targetPos);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void releaseClaimedBlocks() {
/* 105 */     Iterator<Map.Entry<BlockPos, Long>> itr = this.claimedBlocks.entrySet().iterator();
/* 106 */     while (itr.hasNext()) {
/* 107 */       Map.Entry<BlockPos, Long> entry = itr.next();
/* 108 */       long timeClaimed = this.tickCount - ((Long)entry.getValue()).longValue();
/* 109 */       if (timeClaimed > 2400L) {
/*     */         
/* 111 */         itr.remove(); continue;
/*     */       } 
/* 113 */       if (timeClaimed > 600L);
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract BlockPos testBlock(World paramWorld, BlockPos paramBlockPos);
/*     */   
/*     */   protected abstract void scanNearby(BlockPos paramBlockPos);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\blockfinder\BlockScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */