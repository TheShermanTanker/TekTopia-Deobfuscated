/*     */ package net.tangotek.tektopia.structures;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ModBlocks;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.caps.IVillageData;
/*     */ import net.tangotek.tektopia.caps.VillageDataProvider;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.pathing.BasePathingNode;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ import net.tangotek.tektopia.tickjob.TickJobQueue;
/*     */ 
/*     */ public abstract class VillageStructure {
/*     */   protected BlockPos door;
/*     */   protected BlockPos framePos;
/*     */   protected EnumFacing signFacing;
/*  36 */   protected List<BlockPos> floorTiles = new ArrayList<>(); protected Village village; protected final int signEntityId; public final VillageStructureType type; protected final World world;
/*  37 */   protected int ceilingHeightSum = 0;
/*     */   protected boolean isValid = true;
/*     */   protected AxisAlignedBB aabb;
/*  40 */   protected Map<Block, List<BlockPos>> specialBlocks = new HashMap<>();
/*  41 */   protected static int MAX_FLOOR = 500;
/*  42 */   private static int MIN_FLOOR = 4;
/*     */   protected boolean specialAdded = false;
/*  44 */   protected TickJobQueue jobs = new TickJobQueue();
/*  45 */   protected BlockPos safeSpot = null;
/*  46 */   private Set<BlockPos> occupiedSpecials = new HashSet<>();
/*     */   
/*     */   protected VillageStructure(World world, Village v, EntityItemFrame itemFrame, VillageStructureType t, String name) {
/*  49 */     this.type = t;
/*  50 */     this.world = world;
/*  51 */     this.village = v;
/*  52 */     this.framePos = itemFrame.getHangingPosition();
/*  53 */     this.signFacing = itemFrame.facingDirection;
/*  54 */     this.signEntityId = itemFrame.getEntityId();
/*     */   }
/*     */   
/*     */   public void setup() {
/*  58 */     this.door = findDoor();
/*  59 */     if (this.door != null) {
/*  60 */       doFloorScan();
/*     */     }
/*     */     
/*  63 */     setupServerJobs();
/*  64 */     validate();
/*     */   }
/*     */   public Village getVillage() {
/*  67 */     return this.village;
/*     */   } public int getMaxAllowed() {
/*  69 */     return 0;
/*     */   }
/*     */   public void addJob(TickJob job) {
/*  72 */     if (this.world.isRemote) {
/*  73 */       throw new IllegalStateException("Cannot add tick jobs on client");
/*     */     }
/*  75 */     this.jobs.addJob(job);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setupServerJobs() {
/*  80 */     addJob(new TickJob(50, 100, true, () -> validate()));
/*  81 */     addJob(new TickJob(180, 120, true, () -> {
/*     */             if (this.isValid) {
/*     */               doFloorScan();
/*     */             }
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  90 */     this.jobs.tick();
/*     */   }
/*     */   
/*     */   protected void onFloorScanStart() {}
/*     */   
/*     */   protected void onFloorScanEnd() {}
/*     */   
/*     */   public BlockPos getDoorOutside() {
/*  98 */     return getDoorOutside(1);
/*     */   }
/*     */   
/*     */   public BlockPos getDoorOutside(int dist) {
/* 102 */     return this.door.offset(this.signFacing, dist);
/*     */   }
/*     */   
/*     */   public BlockPos getDoorInside() {
/* 106 */     return this.door.offset(this.signFacing, -1);
/*     */   }
/*     */   
/*     */   protected void doFloorScan() {
/* 110 */     this.specialBlocks.clear();
/* 111 */     this.safeSpot = null;
/* 112 */     this.aabb = new AxisAlignedBB(this.door, this.door.up(2));
/* 113 */     this.floorTiles.clear();
/* 114 */     this.ceilingHeightSum = 0;
/* 115 */     onFloorScanStart();
/* 116 */     scanFloor(getDoorInside());
/* 117 */     if (this.safeSpot == null) {
/* 118 */       this.safeSpot = getDoorInside();
/*     */     }
/* 120 */     onFloorScanEnd();
/*     */   }
/*     */   
/*     */   protected void scanFloor(BlockPos pos) {
/* 124 */     if (this.floorTiles.size() <= MAX_FLOOR && !this.floorTiles.contains(pos)) {
/* 125 */       int height = scanRoomHeight(pos);
/*     */       
/* 127 */       if (height >= 2 && !BasePathingNode.isPassable(this.world, pos.down())) {
/* 128 */         this.ceilingHeightSum += height;
/* 129 */         this.floorTiles.add(pos);
/* 130 */         AxisAlignedBB bbox = new AxisAlignedBB(pos);
/* 131 */         this.aabb = this.aabb.union(bbox);
/*     */         
/* 133 */         scanFloor(pos.west());
/* 134 */         scanFloor(pos.north());
/* 135 */         scanFloor(pos.east());
/* 136 */         scanFloor(pos.south());
/*     */         
/* 138 */         if (this.safeSpot == null && !this.world.collidesWithAnyBlock(new AxisAlignedBB((pos.getX() - 1), pos.getY(), (pos.getZ() - 1), (pos.getX() + 1), (pos.getY() + 1), (pos.getZ() + 1)))) {
/* 139 */           this.safeSpot = pos;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected int scanRoomHeight(BlockPos pos) {
/* 146 */     for (int i = 0; i < 30; i++) {
/* 147 */       BlockPos p = pos.up(i);
/* 148 */       Block b = this.world.getBlockState(p).getBlock();
/* 149 */       this.specialAdded = false;
/*     */       
/* 151 */       if (i == 0) {
/* 152 */         scanSpecialBlock(p, b);
/*     */       }
/* 154 */       if (!this.specialAdded && (
/* 155 */         !BasePathingNode.isPassable(this.world, p) || isWoodDoor(this.world, pos) || isGate(this.world, pos))) {
/* 156 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 160 */     return 0;
/*     */   }
/*     */   
/*     */   public BlockPos getSafeSpot() {
/* 164 */     return this.safeSpot;
/*     */   }
/*     */   
/*     */   protected void scanSpecialBlock(BlockPos pos, Block block) {
/* 168 */     if (block == ModBlocks.blockChair) {
/* 169 */       addSpecialBlock((Block)ModBlocks.blockChair, pos);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addSpecialBlock(Block block, BlockPos bp) {
/* 174 */     List<BlockPos> list = this.specialBlocks.get(block);
/* 175 */     if (list == null) {
/* 176 */       list = new ArrayList<>();
/* 177 */       this.specialBlocks.put(block, list);
/*     */     } 
/* 179 */     this.specialAdded = true;
/* 180 */     if (!list.contains(bp)) {
/* 181 */       list.add(bp);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getUnoccupiedSpecialBlock(Block block) {
/* 187 */     List<BlockPos> list = this.specialBlocks.get(block);
/* 188 */     if (list != null) {
/* 189 */       Collections.shuffle(list);
/* 190 */       return list.stream().filter(b -> !isSpecialBlockOccupied(b)).findAny().orElse(null);
/*     */     } 
/*     */     
/* 193 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BlockPos> getSpecialBlocks(Block block) {
/* 198 */     List<BlockPos> list = this.specialBlocks.get(block);
/* 199 */     if (list != null && !list.isEmpty()) {
/* 200 */       return list;
/*     */     }
/* 202 */     return new ArrayList<>();
/*     */   }
/*     */   
/*     */   public boolean vacateSpecialBlock(BlockPos bp) {
/* 206 */     return this.occupiedSpecials.remove(bp);
/*     */   }
/*     */   public boolean occupySpecialBlock(BlockPos bp) {
/* 209 */     return this.occupiedSpecials.add(bp);
/*     */   }
/*     */   
/*     */   public boolean isSpecialBlockOccupied(BlockPos bp) {
/* 213 */     return this.occupiedSpecials.contains(bp);
/*     */   }
/*     */   
/*     */   protected boolean shouldVillagerSit(EntityVillagerTek villager) {
/* 217 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos tryVillagerSit(EntityVillagerTek villager) {
/* 222 */     BlockPos result = null;
/* 223 */     if (shouldVillagerSit(villager)) {
/* 224 */       List<BlockPos> chairs = getSpecialBlocks((Block)ModBlocks.blockChair);
/* 225 */       if (!chairs.isEmpty()) {
/* 226 */         Collections.shuffle(chairs);
/* 227 */         Stream<BlockPos> availableChairs = chairs.stream().filter(c -> !isSpecialBlockOccupied(c));
/*     */         
/* 229 */         if (villager.getRNG().nextInt(3) == 0) {
/* 230 */           result = availableChairs.findAny().orElse(null);
/*     */         }
/*     */         else {
/*     */           
/* 234 */           BlockPos takenChair = chairs.stream().filter(c -> isSpecialBlockOccupied(c)).findAny().orElse(null);
/* 235 */           if (takenChair == null) {
/*     */             
/* 237 */             result = availableChairs.findAny().orElse(null);
/*     */           }
/*     */           else {
/*     */             
/* 241 */             result = availableChairs.min(Comparator.comparing(bp -> Double.valueOf(bp.distanceSq((Vec3i)takenChair)))).orElse(null);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 247 */     if (result != null) {
/* 248 */       occupySpecialBlock(result);
/*     */     }
/*     */     
/* 251 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isStructureOverlapped(VillageStructure other) {
/* 255 */     if (getAABB().intersects(other.getAABB()))
/*     */     {
/* 257 */       if (this.floorTiles.stream().anyMatch(f -> other.floorTiles.contains(f))) {
/* 258 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 262 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSitTime(EntityVillagerTek villager) {
/* 267 */     return 0;
/*     */   }
/*     */   
/*     */   protected BlockPos findDoor() {
/* 271 */     BlockPos dp = null;
/*     */     
/* 273 */     dp = this.framePos.offset(this.signFacing, -1).offset(this.signFacing.rotateY(), 1);
/* 274 */     if (!isWoodDoor(this.world, dp)) {
/* 275 */       dp = this.framePos.offset(this.signFacing, -1).offset(this.signFacing.rotateY(), -1);
/* 276 */       if (!isWoodDoor(this.world, dp)) {
/* 277 */         dp = this.framePos.offset(this.signFacing, -1).down(2);
/* 278 */         if (!isWoodDoor(this.world, dp)) {
/* 279 */           dp = null;
/*     */         }
/*     */       } 
/*     */     } 
/* 283 */     if (dp != null && 
/* 284 */       isWoodDoor(this.world, dp.down())) {
/* 285 */       dp = dp.down();
/*     */     }
/*     */     
/* 288 */     return dp;
/*     */   }
/*     */   
/*     */   public static boolean isWoodDoor(World world, BlockPos pos) {
/* 292 */     if (pos == null) {
/* 293 */       return false;
/*     */     }
/* 295 */     IBlockState iblockstate = world.getBlockState(pos);
/* 296 */     Block block = iblockstate.getBlock();
/*     */     
/* 298 */     if (block instanceof net.minecraft.block.BlockDoor) {
/* 299 */       return (iblockstate.getMaterial() == Material.WOOD);
/*     */     }
/* 301 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isGate(World world, BlockPos pos) {
/* 306 */     if (pos == null) {
/* 307 */       return false;
/*     */     }
/* 309 */     IBlockState iblockstate = world.getBlockState(pos);
/* 310 */     Block block = iblockstate.getBlock();
/* 311 */     return block instanceof net.minecraft.block.BlockFenceGate;
/*     */   }
/*     */   
/*     */   public boolean isBlockInside(BlockPos pos) {
/* 315 */     if (this.aabb.contains(new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D)))
/* 316 */       for (BlockPos p : this.floorTiles) {
/* 317 */         if (p.equals(pos)) {
/* 318 */           return true;
/*     */         }
/*     */       }  
/* 321 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isBlockNear(BlockPos pos, double dist) {
/* 325 */     if (this.aabb.grow(dist, dist, dist).contains(new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D))) {
/* 326 */       double distSq = dist * dist;
/* 327 */       for (BlockPos p : this.floorTiles) {
/* 328 */         if (p.distanceSq((Vec3i)pos) < distSq) {
/* 329 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 333 */     return false;
/*     */   }
/*     */   
/*     */   public List<EntityPlayer> getPlayersInside() {
/* 337 */     List<EntityPlayer> players = this.world.getPlayers(EntityPlayer.class, p -> this.aabb.contains(p.getPositionVector()));
/* 338 */     return players;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos getRandomFloorTile() {
/* 346 */     if (this.floorTiles.isEmpty()) {
/* 347 */       return null;
/*     */     }
/* 349 */     int index = this.world.rand.nextInt(this.floorTiles.size());
/*     */     
/* 351 */     BlockPos bp = this.floorTiles.get(index);
/* 352 */     if (BasePathingNode.isPassable(this.world, bp)) {
/* 353 */       return bp;
/*     */     }
/* 355 */     return null;
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getAABB() {
/* 359 */     return this.aabb;
/*     */   }
/*     */   public BlockPos getDoor() {
/* 362 */     return this.door;
/*     */   }
/*     */   public BlockPos getFramePos() {
/* 365 */     return this.framePos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDestroy() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCrowdedFactor() {
/* 376 */     int floorCount = this.floorTiles.size();
/* 377 */     float avgCeiling = this.ceilingHeightSum / floorCount;
/*     */     
/* 379 */     float modifier = 1.0F;
/*     */ 
/*     */     
/* 382 */     if (this.type.tilesPerVillager > 0) {
/* 383 */       int villagersInside = getEntitiesInside(EntityVillagerTek.class).size();
/* 384 */       int densityRatio = floorCount / villagersInside;
/* 385 */       if (densityRatio < this.type.tilesPerVillager) {
/* 386 */         float compare = densityRatio / this.type.tilesPerVillager;
/* 387 */         modifier *= (compare - 0.5F) * 2.0F;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 393 */     if (avgCeiling < 2.5D) {
/* 394 */       modifier *= 0.5F;
/*     */     }
/* 396 */     return 1.0F - modifier;
/*     */   }
/*     */   
/*     */   public EntityItemFrame getItemFrame() {
/* 400 */     Entity e = this.world.getEntityByID(this.signEntityId);
/* 401 */     if (e instanceof EntityItemFrame) {
/* 402 */       return (EntityItemFrame)e;
/*     */     }
/*     */     
/* 405 */     return null;
/*     */   }
/*     */   
/*     */   public IVillageData getData() {
/* 409 */     EntityItemFrame frame = getItemFrame();
/* 410 */     if (frame != null && frame.getDisplayedItem() != null) {
/* 411 */       IVillageData vd = (IVillageData)frame.getDisplayedItem().getCapability(VillageDataProvider.VILLAGE_DATA_CAPABILITY, null);
/* 412 */       return vd;
/*     */     } 
/*     */     
/* 415 */     return null;
/*     */   }
/*     */   
/*     */   public <T extends Entity> List<T> getEntitiesInside(Class<? extends T> clazz) {
/* 419 */     List<T> entList = this.world.getEntitiesWithinAABB(clazz, getAABB().grow(0.5D, 3.0D, 0.5D));
/* 420 */     ListIterator<T> itr = entList.listIterator();
/* 421 */     while (itr.hasNext()) {
/* 422 */       Entity entity = (Entity)itr.next();
/* 423 */       if (!isBlockNear(entity.getPosition(), 2.0D)) {
/* 424 */         itr.remove();
/*     */       }
/*     */     } 
/*     */     
/* 428 */     return entList;
/*     */   }
/*     */   
/*     */   public void debugOut(String text) {
/* 432 */     if (this.village != null) {
/* 433 */       this.village.debugOut("[" + this.type.name() + "] " + text);
/*     */     } else {
/* 435 */       System.out.println("[No Village] " + text);
/*     */     } 
/*     */   } public boolean isValid() {
/* 438 */     return this.isValid;
/*     */   }
/*     */   public boolean validate() {
/* 441 */     this.isValid = true;
/*     */     
/* 443 */     if (this.door == null) {
/*     */       
/* 445 */       this.isValid = false;
/*     */     }
/* 447 */     else if (this.world.isBlockLoaded(this.door)) {
/* 448 */       if (!isWoodDoor(this.world, this.door) && !isGate(this.world, this.door)) {
/* 449 */         debugOut("Village struct is missing its door " + getFramePos());
/* 450 */         this.isValid = false;
/*     */       } 
/*     */       
/* 453 */       if (this.isValid && this.floorTiles.size() > MAX_FLOOR) {
/* 454 */         debugOut("Village struct has too many floor tiles " + getFramePos());
/* 455 */         this.isValid = false;
/*     */       } 
/*     */       
/* 458 */       Entity e = this.world.getEntityByID(this.signEntityId);
/* 459 */       if (this.isValid && (e == null || !(e instanceof EntityItemFrame))) {
/* 460 */         debugOut("Village struct frame is missing or wrong type | " + getFramePos());
/* 461 */         this.isValid = false;
/*     */       } 
/*     */       
/* 464 */       EntityItemFrame itemFrame = (EntityItemFrame)e;
/* 465 */       if (this.isValid && itemFrame.getHangingPosition() != this.framePos) {
/* 466 */         debugOut("Village struct center has moved " + getFramePos());
/* 467 */         this.isValid = false;
/*     */       } 
/*     */       
/* 470 */       if (this.isValid && !this.type.isItemEqual(itemFrame.getDisplayedItem())) {
/* 471 */         debugOut("Village struct frame item has changed " + getFramePos());
/* 472 */         this.isValid = false;
/*     */       } 
/*     */       
/* 475 */       if (this.isValid && this.floorTiles.size() < MIN_FLOOR)
/*     */       {
/* 477 */         this.isValid = false;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 484 */     return this.isValid;
/*     */   }
/*     */   
/*     */   public void checkOccupiedBlocks() {
/* 488 */     Iterator<BlockPos> itr = this.occupiedSpecials.iterator();
/* 489 */     while (itr.hasNext()) {
/* 490 */       BlockPos bp = itr.next();
/* 491 */       List<EntityVillagerTek> villagers = this.world.getEntitiesWithinAABB(EntityVillagerTek.class, (new AxisAlignedBB(bp)).grow(1.0D));
/* 492 */       if (villagers.isEmpty()) {
/* 493 */         System.out.println("UnOccupying block " + bp + ". No villagers nearby");
/* 494 */         itr.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean adjustsVillageCenter() {
/* 500 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */