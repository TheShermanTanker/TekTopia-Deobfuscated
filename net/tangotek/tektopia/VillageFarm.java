/*     */ package net.tangotek.tektopia;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockCrops;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.event.world.BlockEvent;
/*     */ 
/*     */ public class VillageFarm
/*     */ {
/*  21 */   private static int nextId = 1;
/*     */   private final int farmId;
/*  23 */   private int cropUpdateTick = 0;
/*     */   protected final World world;
/*  25 */   private int distanceToVillageCenter = 0;
/*     */   protected AxisAlignedBB aabb;
/*  27 */   protected List<BlockPos> fullCrops = new ArrayList<>();
/*  28 */   protected Set<BlockPos> activeFarmland = new HashSet<>();
/*  29 */   private int blocks = 0;
/*     */   private double weightScore;
/*  31 */   public static int MAX_AREA = 1000;
/*     */   
/*     */   public VillageFarm(World w, BlockPos startPos, Village v) {
/*  34 */     this.farmId = nextId;
/*  35 */     nextId++;
/*  36 */     this.world = w;
/*  37 */     this.aabb = new AxisAlignedBB(startPos, startPos);
/*  38 */     Set<BlockPos> tested = new HashSet<>();
/*  39 */     testFarmLand(startPos, tested);
/*  40 */     this.blocks = tested.size();
/*  41 */     this.distanceToVillageCenter = (int)getFarmCenter().distanceTo(new Vec3d(v.getCenter().getX(), v.getCenter().getY(), v.getCenter().getZ()));
/*  42 */     this.cropUpdateTick = 500 + this.world.rand.nextInt(500);
/*     */     
/*  44 */     updateWeightScore(v.getSize());
/*     */   }
/*     */   
/*     */   public int size() {
/*  48 */     return this.blocks;
/*     */   }
/*     */   
/*     */   public void updateWeightScore(int villageSize) {
/*  52 */     this.weightScore = villageSize / this.distanceToVillageCenter * this.blocks;
/*     */   }
/*     */   
/*     */   public double getWeightScore() {
/*  56 */     return this.weightScore;
/*     */   }
/*     */   
/*     */   private Vec3d getFarmCenter() {
/*  60 */     return new Vec3d(this.aabb.minX + (this.aabb.maxX - this.aabb.minX) * 0.5D, this.aabb.minY + (this.aabb.maxY - this.aabb.minY) * 0.5D, this.aabb.minZ + (this.aabb.maxZ - this.aabb.minZ) * 0.5D);
/*     */   }
/*     */   
/*     */   public boolean isBlockInside(BlockPos pos) {
/*  64 */     if (pos.getX() >= this.aabb.minX && pos.getX() <= this.aabb.maxX && 
/*  65 */       pos.getZ() >= this.aabb.minZ && pos.getZ() <= this.aabb.maxZ) {
/*  66 */       return (pos.getY() >= this.aabb.minY && pos.getY() <= this.aabb.maxY);
/*     */     }
/*     */ 
/*     */     
/*  70 */     return false;
/*     */   }
/*     */   
/*     */   public BlockPos getMaxAgeCrop() {
/*  74 */     while (!this.fullCrops.isEmpty()) {
/*  75 */       BlockPos pos = this.fullCrops.remove(this.world.rand.nextInt(this.fullCrops.size()));
/*  76 */       if (isMaxAgeCrop(this.world, pos)) {
/*  77 */         return pos;
/*     */       }
/*     */     } 
/*  80 */     return null;
/*     */   }
/*     */   
/*     */   public BlockPos getFarmland(Predicate<BlockPos> pred) {
/*  84 */     for (int i = 0; i < 20; i++) {
/*  85 */       int x = MathHelper.getInt(this.world.rand, (int)this.aabb.minX, (int)this.aabb.maxX);
/*  86 */       int z = MathHelper.getInt(this.world.rand, (int)this.aabb.minZ, (int)this.aabb.maxZ);
/*  87 */       BlockPos pos = new BlockPos(x, this.aabb.minY, z);
/*  88 */       if (isNearWater(pos) && isFarmlandAdjacent(pos) && 
/*  89 */         pred.test(pos) && 
/*  90 */         !this.activeFarmland.contains(pos)) {
/*  91 */         this.activeFarmland.add(pos);
/*  92 */         return pos;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  98 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isFarmlandAdjacent(BlockPos pos) {
/* 102 */     int count = 0;
/* 103 */     if (this.world.getBlockState(pos.west()).getBlock() == Blocks.FARMLAND) {
/* 104 */       count++;
/*     */     }
/* 106 */     if (this.world.getBlockState(pos.north()).getBlock() == Blocks.FARMLAND) {
/* 107 */       count++;
/*     */     }
/* 109 */     if (this.world.getBlockState(pos.south()).getBlock() == Blocks.FARMLAND) {
/* 110 */       count++;
/*     */     }
/* 112 */     if (this.world.getBlockState(pos.east()).getBlock() == Blocks.FARMLAND) {
/* 113 */       count++;
/*     */     }
/* 115 */     return (count >= 2);
/*     */   }
/*     */   
/*     */   private boolean isNearWater(BlockPos cropPos) {
/* 119 */     for (BlockPos testPos : BlockPos.getAllInBox(cropPos.add(-4, 0, -4), cropPos.add(4, 0, 4))) {
/* 120 */       Block testBlock = this.world.getBlockState(testPos).getBlock();
/* 121 */       if (testBlock == Blocks.WATER || testBlock == Blocks.FLOWING_WATER) {
/* 122 */         return true;
/*     */       }
/*     */     } 
/* 125 */     return false;
/*     */   }
/*     */   
/*     */   public int distanceToVillageCenter() {
/* 129 */     return this.distanceToVillageCenter;
/*     */   }
/*     */   
/*     */   public void update(BlockPos villageCenter) {
/* 133 */     this.cropUpdateTick--;
/* 134 */     if (this.cropUpdateTick <= 0) {
/* 135 */       refreshFullCrops();
/* 136 */       this.activeFarmland.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void testFarmLand(BlockPos pos, Set<BlockPos> tested) {
/* 141 */     if (isFarmLand(this.world, pos) && getArea() < MAX_AREA && !tested.contains(pos)) {
/* 142 */       this.aabb = this.aabb.union(new AxisAlignedBB(pos, pos));
/* 143 */       tested.add(pos);
/*     */       
/* 145 */       if (isMaxAgeCrop(this.world, pos.up())) {
/* 146 */         this.fullCrops.add(pos.up());
/*     */       }
/* 148 */       testFarmLand(pos.west(), tested);
/* 149 */       testFarmLand(pos.east(), tested);
/* 150 */       testFarmLand(pos.north(), tested);
/* 151 */       testFarmLand(pos.south(), tested);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onCropGrowEvent(BlockEvent.CropGrowEvent event) {
/* 156 */     if (isBlockInside(event.getPos().down()) && 
/* 157 */       isMaxAgeCrop(this.world, event.getPos()))
/*     */     {
/* 159 */       this.fullCrops.add(event.getPos());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void refreshFullCrops() {
/* 165 */     this.fullCrops.clear();
/* 166 */     for (int x = (int)this.aabb.minX; x <= (int)this.aabb.maxX; x++) {
/* 167 */       for (int z = (int)this.aabb.minZ; z <= (int)this.aabb.maxZ; z++) {
/* 168 */         BlockPos pos = (new BlockPos(x, this.aabb.minY, z)).up();
/* 169 */         if (isMaxAgeCrop(this.world, pos)) {
/* 170 */           this.fullCrops.add(pos);
/*     */         }
/*     */       } 
/*     */     } 
/* 174 */     this.cropUpdateTick = 250 + this.world.rand.nextInt(250);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getAABB() {
/* 179 */     return this.aabb;
/*     */   }
/*     */   
/*     */   public Vec3d getCenter() {
/* 183 */     return this.aabb.getCenter();
/*     */   }
/*     */   
/*     */   public String debug() {
/* 187 */     String result = "Farm #" + this.farmId + "  fullCrops: " + this.fullCrops.size() + " W: " + (int)getWeightScore() + "  C: " + new BlockPos(getCenter()) + " XZ: " + (this.aabb.maxX - this.aabb.minX) + ", " + (this.aabb.maxZ - this.aabb.minZ);
/* 188 */     return result;
/*     */   }
/*     */   
/*     */   public int getArea() {
/* 192 */     return (int)(this.aabb.maxX - this.aabb.minX) * (int)(this.aabb.maxZ - this.aabb.minZ);
/*     */   }
/*     */   
/*     */   public static boolean isFarmLand(World world, BlockPos pos) {
/* 196 */     Block block = world.getBlockState(pos).getBlock();
/* 197 */     return (block == Blocks.FARMLAND);
/*     */   }
/*     */   
/*     */   public static boolean isMaxAgeCrop(World world, BlockPos pos) {
/* 201 */     IBlockState iblockstate = world.getBlockState(pos);
/* 202 */     Block block = iblockstate.getBlock();
/* 203 */     return (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\VillageFarm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */