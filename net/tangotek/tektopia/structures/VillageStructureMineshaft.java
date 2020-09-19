/*     */ package net.tangotek.tektopia.structures;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.entities.EntityDruid;
/*     */ import net.tangotek.tektopia.entities.EntityMiner;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VillageStructureMineshaft
/*     */   extends VillageStructure
/*     */ {
/*  30 */   private EntityVillagerTek currentMiner = null;
/*  31 */   private EntityDruid druid = null;
/*     */   private BlockPos miningPos;
/*     */   
/*     */   protected VillageStructureMineshaft(World world, Village v, EntityItemFrame itemFrame) {
/*  35 */     super(world, v, itemFrame, VillageStructureType.MINESHAFT, "Mineshaft");
/*     */   }
/*     */   
/*     */   public int getTunnelLength() {
/*  39 */     return this.floorTiles.size();
/*     */   }
/*     */   
/*     */   public BlockPos getMiningPos() {
/*  43 */     return this.miningPos;
/*     */   }
/*     */   
/*     */   public BlockPos getWalkingPos() {
/*  47 */     return this.miningPos.offset(this.signFacing, 1);
/*     */   }
/*     */   
/*     */   public boolean adjustsVillageCenter() {
/*  51 */     return false;
/*     */   }
/*     */   
/*     */   protected void setupServerJobs() {
/*  55 */     addJob(new TickJob(20, 10, true, () -> updateOccupants()));
/*  56 */     super.setupServerJobs();
/*     */   }
/*     */   
/*     */   public void updateOccupants() {
/*  60 */     List<EntityVillagerTek> occupants = this.world.getEntitiesWithinAABB(EntityVillagerTek.class, this.aabb);
/*     */     
/*  62 */     if (occupants.isEmpty()) {
/*  63 */       setTunnelMiner((EntityVillagerTek)null);
/*  64 */     } else if (getTunnelMiner() == null) {
/*  65 */       setTunnelMiner(occupants.get(0));
/*  66 */     } else if (!occupants.contains(getTunnelMiner())) {
/*  67 */       setTunnelMiner((EntityVillagerTek)null);
/*     */     } 
/*  69 */     EntityVillagerTek miner = getTunnelMiner();
/*  70 */     if (miner != null) {
/*  71 */       tryPlaceTorch(miner);
/*     */     }
/*     */   }
/*     */   
/*     */   public void tryPlaceTorch(EntityVillagerTek miner) {
/*  76 */     BlockPos pos = miner.getPosition();
/*  77 */     if (miner.world.getLightFromNeighbors(pos) < 8)
/*     */     {
/*  79 */       if (!miner.getInventory().removeItems(EntityMiner.isTorch(), 1).isEmpty())
/*  80 */         miner.world.setBlockState(pos, Blocks.TORCH.getDefaultState()); 
/*     */     }
/*     */   }
/*     */   
/*     */   public EntityVillagerTek getTunnelMiner() {
/*  85 */     return this.currentMiner;
/*     */   }
/*     */   
/*     */   private void setTunnelMiner(EntityVillagerTek miner) {
/*  89 */     this.currentMiner = miner;
/*     */   }
/*     */   
/*     */   public void setDruid(EntityDruid d) {
/*  93 */     this.druid = d;
/*     */   }
/*     */   
/*     */   public EntityDruid getDruid() {
/*  97 */     return this.druid;
/*     */   }
/*     */   
/*     */   public void checkExtendTunnel() {
/* 101 */     while (isTunnel(this, this.miningPos)) {
/* 102 */       addFloorTile(this.miningPos);
/* 103 */       this.miningPos = this.miningPos.offset(this.signFacing, -1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void regrow(EntityVillagerTek villager) {
/* 108 */     if (getTunnelLength() > 3) {
/* 109 */       BlockPos regrowPos = getWalkingPos();
/* 110 */       regrowColumn(villager, regrowPos);
/* 111 */       regrowColumn(villager, regrowPos.offset(this.signFacing.rotateYCCW(), 1));
/* 112 */       regrowColumn(villager, regrowPos.offset(this.signFacing.rotateY(), 1));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     doFloorScan();
/*     */   }
/*     */   
/*     */   protected void regrowColumn(EntityVillagerTek villager, BlockPos pos) {
/* 143 */     regrowBlock(villager, pos.down());
/* 144 */     regrowBlock(villager, pos);
/* 145 */     regrowBlock(villager, pos.up());
/* 146 */     regrowBlock(villager, pos.up(2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void regrowBlock(EntityVillagerTek villager, BlockPos pos) {
/* 151 */     if (!isOre(villager.world, pos)) {
/* 152 */       if (villager.getRNG().nextInt(1000) < villager.getSkillLerp(ProfessionType.DRUID, 25, 40)) {
/* 153 */         this.world.setBlockState(pos, getRegrowBlock(villager).getDefaultState(), 2);
/*     */       } else {
/* 155 */         this.world.setBlockState(pos, Blocks.STONE.getDefaultState(), 2);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Block getRegrowBlock(EntityVillagerTek villager) {
/* 176 */     int oreRoll = villager.getRNG().nextInt(325);
/* 177 */     if (oreRoll < 10) {
/* 178 */       return Blocks.DIAMOND_ORE;
/*     */     }
/* 180 */     if (oreRoll < 20) {
/* 181 */       return Blocks.LAPIS_ORE;
/*     */     }
/* 183 */     if (oreRoll < 35) {
/* 184 */       return Blocks.GOLD_ORE;
/*     */     }
/* 186 */     if (oreRoll < 100) {
/* 187 */       return Blocks.IRON_ORE;
/*     */     }
/* 189 */     if (oreRoll < 200) {
/* 190 */       return Blocks.COAL_ORE;
/*     */     }
/*     */     
/* 193 */     if (villager.getRNG().nextInt(3) == 0) {
/* 194 */       return Blocks.REDSTONE_ORE;
/*     */     }
/* 196 */     return Blocks.STONE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onFloorScanStart() {
/* 202 */     this.miningPos = this.door.offset(this.signFacing, -1);
/* 203 */     super.onFloorScanStart();
/*     */   }
/*     */   
/*     */   public BlockPos findOre(BlockPos pos, int distance, EnumFacing fromDir) {
/* 207 */     if (distance > 3)
/* 208 */       return null; 
/* 209 */     if (this.world.isAirBlock(pos)) {
/* 210 */       BlockPos next = null;
/* 211 */       if (fromDir != EnumFacing.DOWN) {
/* 212 */         next = findOre(pos.up(), distance + 1, EnumFacing.UP);
/* 213 */         if (next != null) {
/* 214 */           return next;
/*     */         }
/*     */       } 
/* 217 */       if (fromDir != EnumFacing.UP) {
/* 218 */         next = findOre(pos.down(), distance + 1, EnumFacing.DOWN);
/* 219 */         if (next != null) {
/* 220 */           return next;
/*     */         }
/*     */       } 
/* 223 */       if (fromDir != this.signFacing.rotateY()) {
/* 224 */         next = findOre(pos.offset(this.signFacing.rotateYCCW(), 1), distance + 1, this.signFacing.rotateYCCW());
/* 225 */         if (next != null) {
/* 226 */           return next;
/*     */         }
/*     */       } 
/* 229 */       if (fromDir != this.signFacing.rotateYCCW()) {
/* 230 */         next = findOre(pos.offset(this.signFacing.rotateY(), 1), distance + 1, this.signFacing.rotateY());
/* 231 */         if (next != null) {
/* 232 */           return next;
/*     */         }
/*     */       }
/*     */     
/* 236 */     } else if (isOre(this.world, pos) && 
/* 237 */       canDig(this.world, pos)) {
/* 238 */       return pos;
/*     */     } 
/*     */     
/* 241 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean isOre(World world, BlockPos pos) {
/* 245 */     Block b = world.getBlockState(pos).getBlock();
/* 246 */     return (b == Blocks.COAL_ORE || b == Blocks.IRON_ORE || b == Blocks.DIAMOND_ORE || b == Blocks.GOLD_ORE || b == Blocks.LAPIS_ORE || b == Blocks.EMERALD_ORE || b == Blocks.REDSTONE_ORE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean canDig(World world, BlockPos pos) {
/* 251 */     if (world.getBlockState(pos.up()).getMaterial().isLiquid() || world
/* 252 */       .getBlockState(pos.west()).getMaterial().isLiquid() || world
/* 253 */       .getBlockState(pos.east()).getMaterial().isLiquid() || world
/* 254 */       .getBlockState(pos.north()).getMaterial().isLiquid() || world
/* 255 */       .getBlockState(pos.south()).getMaterial().isLiquid()) {
/* 256 */       return false;
/*     */     }
/* 258 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canMine() {
/* 262 */     if (this.miningPos == null) {
/* 263 */       return false;
/*     */     }
/* 265 */     if (!this.village.isInVillage(this.miningPos.offset(this.signFacing, -1))) {
/* 266 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 270 */     if (this.world.getBlockState(this.miningPos).getMaterial().isLiquid()) {
/* 271 */       return false;
/*     */     }
/* 273 */     if (this.world.getBlockState(this.miningPos.up()).getMaterial().isLiquid()) {
/* 274 */       return false;
/*     */     }
/* 276 */     if (isSolid(this, this.miningPos) && !canDig(this.world, this.miningPos)) {
/* 277 */       return false;
/*     */     }
/* 279 */     if (isSolid(this, this.miningPos.up()) && !canDig(this.world, this.miningPos.up())) {
/* 280 */       return false;
/*     */     }
/* 282 */     return true;
/*     */   }
/*     */   
/*     */   protected BlockPos findDoor() {
/* 286 */     BlockPos dp = null;
/*     */     
/* 288 */     dp = this.framePos.offset(this.signFacing, -1).offset(this.signFacing.rotateY(), 1).down();
/* 289 */     if (!isTunnel(this, dp)) {
/* 290 */       dp = this.framePos.offset(this.signFacing, -1).offset(this.signFacing.rotateY(), -1).down();
/* 291 */       if (!isTunnel(this, dp))
/* 292 */         dp = this.framePos.offset(this.signFacing, -1).down(2); 
/* 293 */       if (!isTunnel(this, dp)) {
/* 294 */         dp = null;
/*     */       }
/*     */     } 
/* 297 */     return dp;
/*     */   }
/*     */   
/*     */   public static boolean isTunnel(VillageStructureMineshaft mineshaft, BlockPos floorPos) {
/* 301 */     BlockPos upPos = floorPos.up();
/* 302 */     if (isAir(mineshaft, upPos) && isAir(mineshaft, floorPos) && 
/* 303 */       isSolid(mineshaft, upPos.up()) && isSolid(mineshaft, floorPos.down()) && 
/* 304 */       isSolid(mineshaft, upPos.offset(mineshaft.signFacing.rotateY(), 1)) && 
/* 305 */       isSolid(mineshaft, upPos.offset(mineshaft.signFacing.rotateY(), -1)) && 
/* 306 */       isSolid(mineshaft, floorPos.offset(mineshaft.signFacing.rotateY(), 1)) && 
/* 307 */       isSolid(mineshaft, floorPos.offset(mineshaft.signFacing.rotateY(), -1))) {
/* 308 */       return true;
/*     */     }
/*     */     
/* 311 */     return false;
/*     */   }
/*     */   
/*     */   public static BlockPos getTunnelFlaw(VillageStructureMineshaft mineshaft, BlockPos pos) {
/* 315 */     BlockPos testPos = pos.down();
/* 316 */     if (!isSolid(mineshaft, testPos)) {
/* 317 */       return testPos;
/*     */     }
/* 319 */     testPos = pos.offset(mineshaft.signFacing.rotateY(), 1);
/* 320 */     if (!isSolid(mineshaft, testPos)) {
/* 321 */       return testPos;
/*     */     }
/* 323 */     testPos = pos.offset(mineshaft.signFacing.rotateY(), -1);
/* 324 */     if (!isSolid(mineshaft, testPos)) {
/* 325 */       return testPos;
/*     */     }
/* 327 */     testPos = pos.up().offset(mineshaft.signFacing.rotateY(), 1);
/* 328 */     if (!isSolid(mineshaft, testPos)) {
/* 329 */       return testPos;
/*     */     }
/* 331 */     testPos = pos.up().offset(mineshaft.signFacing.rotateY(), -1);
/* 332 */     if (!isSolid(mineshaft, testPos)) {
/* 333 */       return testPos;
/*     */     }
/* 335 */     testPos = pos.up(2);
/* 336 */     if (!isSolid(mineshaft, testPos)) {
/* 337 */       return testPos;
/*     */     }
/* 339 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean isAir(VillageStructureMineshaft mineshaft, BlockPos pos) {
/* 343 */     return (mineshaft.world.getBlockState(pos).getMaterial() == Material.AIR || mineshaft.world.getBlockState(pos).getBlock() == Blocks.TORCH);
/*     */   }
/*     */   
/*     */   public static boolean isSolid(VillageStructureMineshaft mineshaft, BlockPos pos) {
/* 347 */     return mineshaft.world.getBlockState(pos).isFullBlock();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void scanFloor(BlockPos pos) {
/* 352 */     if (!this.floorTiles.contains(pos) && 
/* 353 */       isTunnel(this, pos) && 
/* 354 */       this.village.getPathingGraph().isInGraph(pos)) {
/* 355 */       this.miningPos = pos.offset(this.signFacing, -1);
/* 356 */       this.ceilingHeightSum += 2;
/* 357 */       addFloorTile(pos);
/*     */       
/* 359 */       scanFloor(pos.offset(this.signFacing, -1));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addFloorTile(BlockPos pos) {
/* 366 */     this.floorTiles.add(pos);
/* 367 */     this.aabb = this.aabb.union(new AxisAlignedBB(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean validate() {
/* 372 */     this.isValid = true;
/*     */     
/* 374 */     if (this.door == null)
/*     */     {
/* 376 */       this.isValid = false;
/*     */     }
/*     */     
/* 379 */     Entity e = this.world.getEntityByID(this.signEntityId);
/* 380 */     if (this.isValid && (e == null || !(e instanceof EntityItemFrame))) {
/* 381 */       debugOut("Mineshaft struct frame is missing or wrong type");
/* 382 */       this.isValid = false;
/*     */     } 
/*     */     
/* 385 */     EntityItemFrame itemFrame = (EntityItemFrame)e;
/* 386 */     if (this.isValid && itemFrame.getHangingPosition() != this.framePos) {
/* 387 */       debugOut("Mineshaft struct center has moved");
/* 388 */       this.isValid = false;
/*     */     } 
/*     */     
/* 391 */     if (this.isValid && !itemFrame.getDisplayedItem().isItemEqual(this.type.itemStack)) {
/* 392 */       debugOut("Mineshaft struct frame item has changed");
/* 393 */       this.isValid = false;
/*     */     } 
/*     */     
/* 396 */     if (!canMine())
/*     */     {
/* 398 */       this.isValid = false;
/*     */     }
/*     */     
/* 401 */     return this.isValid;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureMineshaft.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */