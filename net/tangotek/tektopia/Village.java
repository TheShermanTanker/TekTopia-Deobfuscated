/*      */ package net.tangotek.tektopia;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.function.BiPredicate;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.item.EntityArmorStand;
/*      */ import net.minecraft.entity.item.EntityItemFrame;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.EntityPlayerMP;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.init.SoundEvents;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.tileentity.TileEntityChest;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.SoundCategory;
/*      */ import net.minecraft.util.SoundEvent;
/*      */ import net.minecraft.util.math.AxisAlignedBB;
/*      */ import net.minecraft.util.math.BlockPos;
/*      */ import net.minecraft.util.math.MathHelper;
/*      */ import net.minecraft.util.math.Vec3i;
/*      */ import net.minecraft.util.text.ITextComponent;
/*      */ import net.minecraft.util.text.TextComponentString;
/*      */ import net.minecraft.village.MerchantRecipe;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraftforge.common.capabilities.CapabilityDispatcher;
/*      */ import net.minecraftforge.event.world.BlockEvent;
/*      */ import net.tangotek.tektopia.blockfinder.BlockFinder;
/*      */ import net.tangotek.tektopia.blockfinder.BlockScanner;
/*      */ import net.tangotek.tektopia.blockfinder.SaplingScanner;
/*      */ import net.tangotek.tektopia.blockfinder.TreeScanner;
/*      */ import net.tangotek.tektopia.caps.IVillageData;
/*      */ import net.tangotek.tektopia.caps.VillageData;
/*      */ import net.tangotek.tektopia.entities.EntityNecromancer;
/*      */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*      */ import net.tangotek.tektopia.pathing.BasePathingNode;
/*      */ import net.tangotek.tektopia.pathing.PathingGraph;
/*      */ import net.tangotek.tektopia.storage.InventoryScanner;
/*      */ import net.tangotek.tektopia.structures.VillageStructure;
/*      */ import net.tangotek.tektopia.structures.VillageStructureHome;
/*      */ import net.tangotek.tektopia.structures.VillageStructureMineshaft;
/*      */ import net.tangotek.tektopia.structures.VillageStructureType;
/*      */ import net.tangotek.tektopia.tickjob.TickJob;
/*      */ import net.tangotek.tektopia.tickjob.TickJobQueue;
/*      */ 
/*      */ public class Village {
/*   55 */   protected Map<VillageStructureType, List<VillageStructure>> structures = new HashMap<>();
/*      */   protected BlockFinder blockFinder;
/*      */   protected FarmFinder farmFinder;
/*      */   protected MineshaftFinder mineFinder;
/*      */   protected PathingGraph pathingGraph;
/*   60 */   private ArrayDeque<BlockPos> villagerPositions = new ArrayDeque<>();
/*      */   
/*   62 */   private long lastVillagerPosTime = 0L;
/*      */   private boolean isDestroyed = false;
/*      */   private World world;
/*   65 */   private List<TileEntityChest> storage = new ArrayList<>();
/*   66 */   private Map<BlockPos, InventoryScanner> storageScanners = new HashMap<>();
/*      */   private BlockPos center;
/*      */   private BlockPos origin;
/*      */   private CapabilityDispatcher capabilities;
/*   70 */   private int cleanTick = 0;
/*   71 */   private int tickCounter = 0;
/*      */   
/*   73 */   private int guardSleepOffset = 1000 - EntityVillagerTek.SLEEP_START_TIME;
/*   74 */   private int clericSleepOffset = 1000 - EntityVillagerTek.SLEEP_START_TIME;
/*      */   
/*      */   private AxisAlignedBB aabb;
/*   77 */   private List<VillageEnemy> enemies = new ArrayList<>();
/*   78 */   private BlockPos enemySighting = null;
/*   79 */   protected TickJobQueue jobs = new TickJobQueue();
/*      */   protected MerchantScheduler merchantScheduler;
/*      */   protected NomadScheduler nomadScheduler;
/*      */   protected RaidScheduler raidScheduler;
/*      */   public static final int VILLAGE_SIZE = 120;
/*      */   private IVillageData villageData;
/*   85 */   private static final VillageData emptyVillageData = new VillageData();
/*   86 */   private String villageName = "";
/*      */   private UUID villageUUID;
/*   88 */   private List<EntityVillagerTek> residents = new ArrayList<>();
/*   89 */   private Set<EntityVillagerTek> activeDefenders = new HashSet<>();
/*      */ 
/*      */   
/*      */   class VillageEnemy
/*      */   {
/*      */     public EntityLivingBase enemy;
/*      */     
/*      */     public int aggressionTime;
/*      */     public int threat;
/*      */     
/*      */     VillageEnemy(EntityLivingBase enemy, int agressionTimeIn) {
/*  100 */       this.enemy = enemy;
/*  101 */       this.aggressionTime = agressionTimeIn;
/*  102 */       this.threat = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public Village(World worldIn, BlockPos origin) {
/*  108 */     this.capabilities = null;
/*  109 */     this.blockFinder = new BlockFinder();
/*  110 */     this.farmFinder = new FarmFinder(worldIn, this);
/*  111 */     this.mineFinder = new MineshaftFinder(worldIn, this);
/*  112 */     this.origin = origin;
/*  113 */     this.pathingGraph = new PathingGraph(worldIn, this);
/*  114 */     this.world = worldIn;
/*  115 */     this.merchantScheduler = new MerchantScheduler(worldIn, this);
/*  116 */     this.nomadScheduler = new NomadScheduler(worldIn, this);
/*  117 */     this.raidScheduler = new RaidScheduler(worldIn, this);
/*  118 */     this.cleanTick = this.world.rand.nextInt(40) + 40;
/*      */ 
/*      */     
/*  121 */     this.blockFinder.registerBlockScanner((BlockScanner)new TreeScanner(this, 30));
/*  122 */     this.blockFinder.registerBlockScanner((BlockScanner)new SugarCaneScanner(this, 15));
/*  123 */     this.blockFinder.registerBlockScanner((BlockScanner)new SaplingScanner(this, 15));
/*      */     
/*  125 */     setupServerJobs();
/*      */   }
/*      */   
/*      */   public void addJob(TickJob job) {
/*  129 */     this.jobs.addJob(job);
/*      */   }
/*      */   
/*      */   public void debugOut(String text) {
/*  133 */     System.out.println("[" + this.villageName + "] " + text);
/*      */   }
/*      */   
/*      */   protected void setupServerJobs() {
/*  137 */     addJob(new TickJob(100, 100, true, () -> this.merchantScheduler.update()));
/*  138 */     addJob(new TickJob(200, 100, true, () -> this.nomadScheduler.update()));
/*      */     
/*  140 */     addJob(new TickJob(10, 5, true, () -> this.enemySighting = null));
/*  141 */     addJob(new TickJob(4000, 2000, true, () -> {
/*      */             IVillageData vd = getTownData();
/*      */             if (vd != null)
/*      */               vd.getEconomy().refreshValues(this); 
/*      */           }));
/*      */   }
/*      */   
/*      */   public ItemStack getVillageToken() {
/*  149 */     VillageStructure townHall = getNearestStructure(VillageStructureType.TOWNHALL, getOrigin());
/*  150 */     if (townHall != null) {
/*  151 */       EntityItemFrame frame = townHall.getItemFrame();
/*  152 */       if (frame != null) {
/*  153 */         return frame.getDisplayedItem();
/*      */       }
/*      */     } 
/*  156 */     return ItemStack.EMPTY;
/*      */   }
/*      */   
/*      */   public IVillageData getTownData() {
/*  160 */     if (this.origin != null) {
/*  161 */       VillageStructure townHall = getNearestStructure(VillageStructureType.TOWNHALL, this.origin);
/*  162 */       if (townHall != null) {
/*  163 */         this.villageData = townHall.getData();
/*  164 */         if (this.villageData != null) {
/*  165 */           return this.villageData;
/*      */         }
/*      */       } 
/*      */     } 
/*  169 */     debugOut("Returning EMPTY village data");
/*  170 */     return (IVillageData)emptyVillageData;
/*      */   }
/*      */   
/*      */   public BlockPos getCenter() {
/*  174 */     return this.center;
/*      */   }
/*      */   
/*      */   public BlockPos getOrigin() {
/*  178 */     return this.origin;
/*      */   }
/*      */   public World getWorld() {
/*  181 */     return this.world;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PathingGraph getPathingGraph() {
/*  188 */     return this.pathingGraph;
/*      */   }
/*      */   
/*      */   public boolean isLoaded() {
/*  192 */     if (getOrigin() != null) {
/*  193 */       return this.world.isBlockLoaded(getOrigin());
/*      */     }
/*  195 */     return false;
/*      */   }
/*      */   
/*      */   private boolean canAddStructure(VillageStructure struct) {
/*  199 */     if (isStructureValid(struct)) {
/*  200 */       List<VillageStructure> structs = this.structures.get(struct.type);
/*  201 */       if (structs == null || struct.getMaxAllowed() == 0 || structs.size() < struct.getMaxAllowed()) {
/*  202 */         return true;
/*      */       }
/*      */     } 
/*      */     
/*  206 */     return false;
/*      */   }
/*      */   
/*      */   public boolean addStructure(VillageStructure struct) {
/*  210 */     if (canAddStructure(struct)) {
/*  211 */       List<VillageStructure> structList = this.structures.get(struct.type);
/*  212 */       if (structList == null) {
/*  213 */         structList = new ArrayList<>();
/*  214 */         this.structures.put(struct.type, structList);
/*      */       } 
/*      */       
/*  217 */       if (!structList.contains(struct)) {
/*  218 */         if (structList.add(struct)) {
/*      */ 
/*      */           
/*  221 */           EntityItemFrame frame = struct.getItemFrame();
/*  222 */           if (frame != null && frame.getDisplayedItem() != null) {
/*  223 */             ModItems.bindItemToVillage(struct.getItemFrame().getDisplayedItem(), this);
/*      */           }
/*  225 */           if (struct.type == VillageStructureType.TOWNHALL && this.pathingGraph.nodeCount() <= 0) {
/*  226 */             this.pathingGraph.seedVillage(struct.getDoorOutside());
/*  227 */             this.origin = struct.getDoorOutside();
/*  228 */             addVillagerPosition(struct.getDoorOutside());
/*  229 */             ItemStack villageToken = getVillageToken();
/*  230 */             if (!villageToken.isEmpty() && villageToken.hasDisplayName() && !villageToken.getDisplayName().equals("Town Hall")) {
/*  231 */               this.villageName = villageToken.getDisplayName();
/*      */             } else {
/*  233 */               this.villageName = randomAlphaNumeric(3);
/*      */             } 
/*  235 */             this.villageUUID = getTownData().getUUID();
/*  236 */             debugOut("Village ADDED - [" + getName() + "]   " + this.villageUUID);
/*      */           } 
/*      */           
/*  239 */           debugOut("Adding structure " + struct.type + " [" + struct.getDoor() + "]");
/*      */           
/*  241 */           if (struct.adjustsVillageCenter()) {
/*  242 */             updateCenter();
/*      */           }
/*      */           
/*  245 */           return true;
/*      */         } 
/*      */       } else {
/*  248 */         debugOut("Tried adding structure that already existed - " + struct.type.name());
/*      */       } 
/*      */     } 
/*      */     
/*  252 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String randomAlphaNumeric(int count) {
/*  267 */     String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
/*  268 */     StringBuilder builder = new StringBuilder();
/*  269 */     while (count-- != 0) {
/*  270 */       int character = (int)(Math.random() * "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length());
/*  271 */       builder.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(character));
/*      */     } 
/*  273 */     return builder.toString();
/*      */   }
/*      */   
/*      */   public void forceRaid(int raidPoints) {
/*  277 */     this.raidScheduler.forceRaid(raidPoints);
/*      */   }
/*      */   
/*      */   public boolean canSleepAt(BlockPos pos) {
/*  281 */     if (VillageStructureHome.isBed(this.world, pos)) {
/*  282 */       List<VillageStructure> homes = getHomes();
/*  283 */       for (VillageStructure struct : homes) {
/*  284 */         VillageStructureHome home = (VillageStructureHome)struct;
/*  285 */         if (!home.canSleepAt(pos)) {
/*  286 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*  290 */     return true;
/*      */   }
/*      */   
/*      */   public int getNextGuardSleepOffset() {
/*  294 */     int result = this.guardSleepOffset;
/*  295 */     this.guardSleepOffset += 7677;
/*  296 */     if (this.guardSleepOffset > 24000) {
/*  297 */       this.guardSleepOffset -= 24000;
/*      */     }
/*  299 */     return result;
/*      */   }
/*      */   
/*      */   public int getNextClericSleepOffset() {
/*  303 */     int result = this.clericSleepOffset;
/*  304 */     this.clericSleepOffset += 7777;
/*  305 */     if (this.clericSleepOffset > 24000) {
/*  306 */       this.clericSleepOffset -= 24000;
/*      */     }
/*  308 */     return result;
/*      */   }
/*      */   
/*      */   public List<VillageStructure> getHomes() {
/*  312 */     return getStructures(new VillageStructureType[] { VillageStructureType.BARRACKS, VillageStructureType.HOME2, VillageStructureType.HOME4, VillageStructureType.HOME6 });
/*      */   }
/*      */   
/*      */   public List<VillageStructure> getStructures(VillageStructureType... types) {
/*  316 */     List<VillageStructure> outList = new ArrayList<>();
/*  317 */     for (VillageStructureType arg : types) {
/*  318 */       outList.addAll(getStructures(arg));
/*      */     }
/*  320 */     return outList;
/*      */   }
/*      */   
/*      */   public List<VillageStructure> getStructures(VillageStructureType type) {
/*  324 */     List<VillageStructure> structList = this.structures.get(type);
/*  325 */     if (structList != null && !structList.isEmpty()) {
/*  326 */       return new ArrayList<>(structList);
/*      */     }
/*      */     
/*  329 */     return new ArrayList<>();
/*      */   }
/*      */   
/*      */   public boolean hasStructure(VillageStructureType type) {
/*  333 */     List<VillageStructure> structList = this.structures.get(type);
/*  334 */     if (structList == null || structList.isEmpty()) {
/*  335 */       return false;
/*      */     }
/*  337 */     return true;
/*      */   }
/*      */   
/*      */   public VillageStructure getNearestStructure(VillageStructureType type, BlockPos pos) {
/*  341 */     List<VillageStructure> structList = getStructures(type);
/*  342 */     double min = Double.MAX_VALUE;
/*  343 */     VillageStructure closest = null;
/*  344 */     for (VillageStructure struct : structList) {
/*  345 */       double dist = pos.distanceSq((Vec3i)struct.getDoor());
/*  346 */       if (dist < min) {
/*  347 */         min = dist;
/*  348 */         closest = struct;
/*      */       } 
/*      */     } 
/*      */     
/*  352 */     return closest;
/*      */   }
/*      */   
/*      */   public boolean isStructureValid(VillageStructure struct) {
/*  356 */     if (!struct.isValid()) {
/*  357 */       return false;
/*      */     }
/*      */     
/*  360 */     int count = 0;
/*  361 */     for (List<VillageStructure> lst : this.structures.values()) {
/*  362 */       for (VillageStructure vs : lst) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  370 */         if (vs != struct && vs.isStructureOverlapped(struct)) {
/*  371 */           debugOut("Structures overlap | " + struct.type + "  " + vs.getAABB());
/*  372 */           return false;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  377 */     return true;
/*      */   }
/*      */   
/*      */   public void addVillagerPosition(EntityVillagerTek villager) {
/*  381 */     addVillagerPosition(villager.getPosition());
/*      */   }
/*      */   
/*      */   public void addVillagerPosition(BlockPos pos) {
/*  385 */     if (this.world.getTotalWorldTime() - this.lastVillagerPosTime > 20L && getStructure(pos) == null) {
/*  386 */       this.villagerPositions.addLast(pos);
/*  387 */       this.lastVillagerPosTime = this.world.getTotalWorldTime();
/*      */       
/*  389 */       while (this.villagerPositions.size() > 20) {
/*  390 */         this.villagerPositions.removeFirst();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public BlockPos getLastVillagerPos() {
/*  397 */     if (this.villagerPositions.isEmpty()) {
/*  398 */       return getOrigin();
/*      */     }
/*      */     
/*  401 */     BlockPos pos = this.villagerPositions.removeFirst();
/*  402 */     this.villagerPositions.addLast(pos);
/*  403 */     return pos;
/*      */   }
/*      */ 
/*      */   
/*      */   public AxisAlignedBB getAABB() {
/*  408 */     return this.aabb;
/*      */   }
/*      */   
/*      */   public boolean isInStructure(BlockPos bp) {
/*  412 */     return (getStructure(bp) != null);
/*      */   }
/*      */   
/*      */   public VillageStructure getStructure(BlockPos bp) {
/*  416 */     for (List<VillageStructure> lst : this.structures.values()) {
/*  417 */       for (VillageStructure vs : lst) {
/*  418 */         if (vs.isBlockInside(bp)) {
/*  419 */           return vs;
/*      */         }
/*      */       } 
/*      */     } 
/*  423 */     return null;
/*      */   }
/*      */   
/*      */   public VillageStructure getStructureFromFrame(BlockPos bp) {
/*  427 */     for (List<VillageStructure> lst : this.structures.values()) {
/*  428 */       for (VillageStructure vs : lst) {
/*  429 */         if (vs.getFramePos().equals(bp)) {
/*  430 */           return vs;
/*      */         }
/*      */       } 
/*      */     } 
/*  434 */     return null;
/*      */   }
/*      */   
/*      */   public void purchaseFromMerchant(MerchantRecipe recipe, EntityMerchant merchant, EntityPlayer player) {
/*  438 */     if (recipe.getItemToSell().getItem() == Items.SKULL) {
/*  439 */       String skullOwner = ModItems.getSkullAnimal(recipe.getItemToSell());
/*  440 */       if (!skullOwner.isEmpty()) {
/*  441 */         this.merchantScheduler.addOrder(skullOwner);
/*      */         
/*  443 */         player.inventory.setItemStack(ItemStack.EMPTY);
/*  444 */         player.inventory.clearMatchingItems(Items.SKULL, 3, 0, null);
/*      */ 
/*      */         
/*  447 */         player.sendMessage((ITextComponent)new TextComponentString("The merchant will return tomorrow with a " + skullOwner + "."));
/*      */       } 
/*      */     } 
/*      */     
/*  451 */     IVillageData vd = getTownData();
/*  452 */     if (vd != null) {
/*  453 */       debugOut("Player selling " + recipe.getItemToBuy().getItem().getTranslationKey() + " [" + player.getName() + "]");
/*  454 */       vd.getEconomy().sellItem(recipe, this);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void trackHappy(int delta) {}
/*      */ 
/*      */   
/*      */   public void resetStorage() {
/*  463 */     this.storage.clear();
/*  464 */     this.storageScanners.clear();
/*      */   }
/*      */   
/*      */   public void addStorageChest(TileEntityChest chest) {
/*  468 */     this.storage.add(chest);
/*  469 */     this.storageScanners.put(chest.getPos(), new InventoryScanner((IInventory)chest));
/*  470 */     debugOut("Storage Chest [" + chest.getPos() + "]   Added    (" + this.storage.size() + " total)");
/*      */   }
/*      */   
/*      */   public void removeStorageChest(TileEntityChest chest) {
/*  474 */     this.storage.remove(chest);
/*  475 */     this.storageScanners.remove(chest.getPos());
/*  476 */     debugOut("Storage Chest [" + chest.getPos() + "]   Removed    (" + this.storage.size() + " total)");
/*      */   }
/*      */   
/*      */   public int getStorageCount(Predicate<ItemStack> predicate) {
/*  480 */     int count = 0;
/*  481 */     for (TileEntityChest chest : this.storage) {
/*  482 */       if (!chest.isInvalid() && !chest.isEmpty()) {
/*  483 */         for (int i = 0; i < chest.getSizeInventory(); i++) {
/*  484 */           ItemStack itemStack = chest.getStackInSlot(i);
/*  485 */           if (predicate.test(itemStack)) {
/*  486 */             count += itemStack.getCount();
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*  491 */     return count;
/*      */   }
/*      */   
/*      */   public TileEntityChest getStorageChestWithItem(Function<ItemStack, Integer> function) {
/*  495 */     int bestResult = 0;
/*  496 */     TileEntityChest bestChest = null;
/*  497 */     for (TileEntityChest chest : this.storage) {
/*  498 */       if (!chest.isInvalid() && !chest.isEmpty()) {
/*  499 */         for (int i = 0; i < chest.getSizeInventory(); i++) {
/*  500 */           ItemStack itemStack = chest.getStackInSlot(i);
/*  501 */           if (!itemStack.isEmpty()) {
/*  502 */             int result = ((Integer)function.apply(itemStack)).intValue();
/*  503 */             if (result > bestResult) {
/*  504 */               bestResult = result;
/*  505 */               bestChest = chest;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  512 */     return bestChest;
/*      */   }
/*      */   
/*      */   private int chestCompareTo(TileEntityChest chest1, TileEntityChest chest2, BlockPos origin) {
/*  516 */     double d1 = chest1.getDistanceSq(origin.getX(), origin.getY(), origin.getZ());
/*  517 */     double d2 = chest2.getDistanceSq(origin.getX(), origin.getY(), origin.getZ());
/*  518 */     return Double.compare(d1, d2);
/*      */   }
/*      */   
/*      */   public TileEntityChest getAvailableStorageChest(ItemStack testStack, BlockPos origin) {
/*  522 */     TileEntityChest bestChest = null;
/*      */     
/*  524 */     this.storage.sort((chest1, chest2) -> chestCompareTo(chest1, chest2, origin));
/*  525 */     for (TileEntityChest chest : this.storage) {
/*  526 */       if (!chest.isInvalid()) {
/*  527 */         int openSlot = -1;
/*  528 */         boolean matchedThisChest = false;
/*  529 */         for (int i = 0; i < chest.getSizeInventory(); i++) {
/*  530 */           ItemStack itemStack = chest.getStackInSlot(i);
/*  531 */           if (itemStack.isEmpty() && openSlot < 0) {
/*  532 */             openSlot = i;
/*  533 */             if (matchedThisChest) {
/*  534 */               return chest;
/*      */             }
/*  536 */           } else if (VillagerInventory.areItemsStackable(itemStack, testStack)) {
/*  537 */             matchedThisChest = true;
/*  538 */             if (itemStack.getCount() < itemStack.getMaxStackSize()) {
/*  539 */               return chest;
/*      */             }
/*      */           } 
/*      */         } 
/*      */         
/*  544 */         if (openSlot >= 0) {
/*  545 */           if (matchedThisChest)
/*  546 */             return chest; 
/*  547 */           if (bestChest == null) {
/*  548 */             bestChest = chest;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*  553 */     return bestChest;
/*      */   }
/*      */   
/*      */   public VillageStructureHome getAvailableHome(EntityVillagerTek villager) {
/*  557 */     List<VillageStructure> homes = getHomes();
/*  558 */     for (VillageStructure struct : homes) {
/*  559 */       VillageStructureHome home = (VillageStructureHome)struct;
/*  560 */       if (!home.isFull() && home.canVillagerSleep(villager)) {
/*  561 */         return home;
/*      */       }
/*      */     } 
/*      */     
/*  565 */     return null;
/*      */   }
/*      */   
/*      */   public int getSize() {
/*  569 */     return 120;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void occupancySpam() {
/*  587 */     int beds = 0;
/*  588 */     int residents = 0;
/*  589 */     List<VillageStructure> homes = getHomes();
/*  590 */     for (VillageStructure struct : homes) {
/*  591 */       VillageStructureHome home = (VillageStructureHome)struct;
/*  592 */       beds += home.getMaxResidents();
/*  593 */       residents += home.getCurResidents();
/*      */     } 
/*      */     
/*  596 */     debugOut("Residents:  " + residents + " / " + beds);
/*      */   }
/*      */   
/*      */   public int getResidentCount() {
/*  600 */     return this.residents.size();
/*      */   }
/*      */   
/*      */   public String getName() {
/*  604 */     return this.villageName;
/*      */   }
/*      */   
/*      */   public void villageReport(String reportType) {
/*  608 */     IVillageData vd = getTownData();
/*  609 */     if (vd != null) {
/*  610 */       StringBuilder reportOut = new StringBuilder();
/*  611 */       reportOut.append("\n");
/*  612 */       reportOut.append("----- Village Report --[" + this.villageName + "]---------\n");
/*      */       
/*  614 */       if (isReportType(reportType, "homes")) {
/*  615 */         homeReport(reportOut);
/*      */       }
/*      */       
/*  618 */       if (isReportType(reportType, "happy")) {
/*  619 */         happyReport(reportOut);
/*      */       }
/*      */       
/*  622 */       if (isReportType(reportType, "hunger")) {
/*  623 */         hungerReport(reportOut);
/*      */       }
/*      */       
/*  626 */       if (isReportType(reportType, "levels")) {
/*  627 */         levelReport(reportOut);
/*      */       }
/*      */       
/*  630 */       if (isReportType(reportType, "economy")) {
/*  631 */         vd.getEconomy().report(reportOut);
/*      */       }
/*  633 */       reportOut.append("------------------------------------\n");
/*  634 */       debugOut(reportOut.toString());
/*      */     } else {
/*      */       
/*  637 */       System.err.println("==== Village Without VillageData?? ====");
/*      */     } 
/*      */   }
/*      */   
/*      */   private void homeReport(StringBuilder builder) {
/*  642 */     List<VillageStructure> homes = getHomes();
/*  643 */     builder.append("    " + homes.size() + " homes - " + this.residents.size() + " residents");
/*      */     
/*  645 */     for (VillageStructure struct : homes) {
/*  646 */       VillageStructureHome home = (VillageStructureHome)struct;
/*  647 */       home.villageReport(builder);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void happyReport(StringBuilder builder) {
/*  652 */     int HAPPY_BUCKETS = 5;
/*  653 */     int BUCKET_SIZE = 20;
/*  654 */     int[] happyBuckets = new int[5];
/*  655 */     for (EntityVillagerTek villager : this.residents) {
/*  656 */       int bucket = MathHelper.clamp(villager.getHappy() / 20, 0, 4);
/*  657 */       happyBuckets[bucket] = happyBuckets[bucket] + 1;
/*      */     } 
/*      */     
/*  660 */     for (int i = 0; i < 5; i++) {
/*  661 */       int step = 20 * i;
/*  662 */       builder.append("Happy " + String.format("%-2s", new Object[] { Integer.valueOf(step) }) + " - " + String.format("%-2s", new Object[] { Integer.valueOf(step + 20 - 1) }) + ") " + String.format("%-3s", new Object[] { Integer.valueOf(happyBuckets[i]) }));
/*      */       
/*  664 */       if (happyBuckets[i] > 0) {
/*  665 */         char[] chars = new char[happyBuckets[i]];
/*  666 */         Arrays.fill(chars, 'P');
/*  667 */         builder.append("  " + new String(chars));
/*      */       } 
/*  669 */       builder.append("\n");
/*      */     } 
/*      */   }
/*      */   
/*      */   private void hungerReport(StringBuilder builder) {
/*  674 */     int HUNGER_BUCKETS = 5;
/*  675 */     int BUCKET_SIZE = 20;
/*  676 */     int[] hungerBuckets = new int[5];
/*  677 */     for (EntityVillagerTek villager : this.residents) {
/*  678 */       int bucket = MathHelper.clamp(villager.getHunger() / 20, 0, 4);
/*  679 */       hungerBuckets[bucket] = hungerBuckets[bucket] + 1;
/*      */     } 
/*      */     
/*  682 */     for (int i = 0; i < 5; i++) {
/*  683 */       int step = 20 * i;
/*  684 */       builder.append("Hunger " + String.format("%-2s", new Object[] { Integer.valueOf(step) }) + " - " + String.format("%-2s", new Object[] { Integer.valueOf(step + 20 - 1) }) + ") " + String.format("%-3s", new Object[] { Integer.valueOf(hungerBuckets[i]) }));
/*      */       
/*  686 */       if (hungerBuckets[i] > 0) {
/*  687 */         char[] chars = new char[hungerBuckets[i]];
/*  688 */         Arrays.fill(chars, 'H');
/*  689 */         builder.append("  " + new String(chars));
/*      */       } 
/*  691 */       builder.append("\n");
/*      */     } 
/*      */   }
/*      */   
/*      */   private void levelReport(StringBuilder builder) {
/*      */     class ProfessionLevelTracker
/*      */     {
/*  698 */       public int ct = 0;
/*  699 */       public int sum = 0;
/*  700 */       public int days = 0;
/*      */       public ProfessionType pt;
/*  702 */       List<EntityVillagerTek> villagers = new ArrayList<>();
/*      */       ProfessionLevelTracker(ProfessionType pt) {
/*  704 */         this.pt = pt;
/*      */       }
/*      */       
/*  707 */       int getAvg() { return this.sum / this.ct; } float getSkillsPerDay() {
/*  708 */         return this.sum / this.days;
/*      */       }
/*      */       public void report(StringBuilder builder) {
/*  711 */         builder.append(String.format("%-15s", new Object[] { this.pt.name }) + " [" + String.format("%-2s", new Object[] { Integer.valueOf(getAvg()) }) + "]   [" + String.format("%.2f", new Object[] { Float.valueOf(getSkillsPerDay()) }) + " per day] ");
/*  712 */         this.villagers.forEach(v -> builder.append(String.format("%-2s", new Object[] { Integer.valueOf(v.getBaseSkill(this.pt)) }) + " "));
/*  713 */         builder.append("\n");
/*      */       }
/*      */     };
/*      */     
/*  717 */     Map<ProfessionType, ProfessionLevelTracker> levelMap = new HashMap<>();
/*      */ 
/*      */     
/*  720 */     for (EntityVillagerTek v : this.residents) {
/*  721 */       ProfessionLevelTracker tracker = levelMap.get(v.getProfessionType());
/*  722 */       if (tracker == null) {
/*  723 */         tracker = new ProfessionLevelTracker(v.getProfessionType());
/*  724 */         levelMap.put(v.getProfessionType(), tracker);
/*      */       } 
/*      */       
/*  727 */       int baseSkill = v.getBaseSkill(v.getProfessionType());
/*  728 */       tracker.ct++;
/*  729 */       tracker.sum += baseSkill;
/*  730 */       tracker.days += v.getDaysAlive();
/*  731 */       tracker.villagers.add(v);
/*      */     } 
/*      */     
/*  734 */     levelMap.entrySet().stream().sorted(Comparator.comparing(e -> Integer.valueOf(((ProfessionLevelTracker)e.getValue()).getAvg()))).forEach(e -> ((ProfessionLevelTracker)e.getValue()).report(builder));
/*      */   }
/*      */   
/*      */   private boolean isReportType(String reportType, String testType) {
/*  738 */     return (reportType.equals("all") || reportType.equals(testType));
/*      */   }
/*      */   
/*      */   public BlockPos getEdgeNode() {
/*  742 */     BasePathingNode node = this.pathingGraph.getEdgeNode(getOrigin(), Double.valueOf(getSize() * 0.9D));
/*  743 */     if (node != null) {
/*  744 */       BlockPos pos = node.getBlockPos();
/*  745 */       if (getStructure(pos) == null) {
/*  746 */         return pos;
/*      */       }
/*      */     } 
/*  749 */     return null;
/*      */   }
/*      */   
/*      */   private static long fixTime(long time) {
/*  753 */     while (time > 24000L) {
/*  754 */       time -= 24000L;
/*      */     }
/*  756 */     while (time < 0L) {
/*  757 */       time += 24000L;
/*      */     }
/*  759 */     return time;
/*      */   }
/*      */   
/*      */   public static boolean isTimeOfDay(World world, long startTime, long endTime, long offset) {
/*  763 */     startTime = fixTime(startTime + offset);
/*  764 */     endTime = fixTime(endTime + offset);
/*  765 */     long time = getTimeOfDay(world);
/*  766 */     if (endTime > startTime) {
/*  767 */       return (time >= startTime && time <= endTime);
/*      */     }
/*  769 */     return (time >= startTime || time <= endTime);
/*      */   }
/*      */   
/*      */   public static boolean isTimeOfDay(World world, long startTime, long endTime) {
/*  773 */     return isTimeOfDay(world, startTime, endTime, 0L);
/*      */   }
/*      */   
/*      */   public static long getTimeOfDay(World world) {
/*  777 */     return world.getWorldTime() % 24000L;
/*      */   }
/*      */   
/*      */   public static boolean isNightTime(World world) {
/*  781 */     long timeOfDay = getTimeOfDay(world);
/*  782 */     return (timeOfDay > 11500L || timeOfDay < 500L || timeOfDay < 200L || world.isRaining());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateCenter() {
/*  790 */     double farthestStructure = 0.0D;
/*  791 */     this.center = getOrigin();
/*  792 */     for (List<VillageStructure> lst : this.structures.values()) {
/*  793 */       for (VillageStructure struct : lst) {
/*  794 */         if (struct.adjustsVillageCenter()) {
/*  795 */           if (struct.type == VillageStructureType.STORAGE) {
/*  796 */             this.center = struct.getDoorOutside();
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  808 */           if (this.aabb == null) {
/*  809 */             this.aabb = struct.getAABB(); continue;
/*      */           } 
/*  811 */           this.aabb = this.aabb.union(struct.getAABB());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void destroy() {
/*  827 */     debugOut("== Village being destroyed ==");
/*  828 */     this.isDestroyed = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void cleanVillage() {
/*  844 */     for (List<VillageStructure> lst : this.structures.values()) {
/*  845 */       for (int i = lst.size() - 1; i >= 0; i--) {
/*  846 */         VillageStructure struct = lst.get(i);
/*  847 */         if (!isStructureValid(struct)) {
/*  848 */           debugOut("Removing structure " + struct.type + " [" + struct.getDoor() + "]");
/*  849 */           struct.onDestroy();
/*  850 */           lst.remove(i);
/*  851 */           if (struct.adjustsVillageCenter()) {
/*  852 */             updateCenter();
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  858 */     VillageManager vm = VillageManager.get(this.world);
/*  859 */     vm.addScanBox((new AxisAlignedBB(this.center)).grow(64.0D, 64.0D, 64.0D));
/*      */ 
/*      */     
/*  862 */     this.structures.entrySet().removeIf(entry -> ((List)entry.getValue()).isEmpty());
/*      */ 
/*      */     
/*  865 */     clearDeadEnemies();
/*  866 */     cleanDebugTurds();
/*  867 */     this.cleanTick = this.world.rand.nextInt(40) + 40;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void tickInventoryScanners() {
/*  875 */     for (InventoryScanner scanner : this.storageScanners.values()) {
/*  876 */       int changedSlot = scanner.tickSlot();
/*  877 */       if (changedSlot >= 0)
/*      */       {
/*  879 */         notifyResidentsStorageUpdate(scanner.getChangedItem());
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void notifyResidentsStorageUpdate(ItemStack updatedItem) {
/*  885 */     if (!updatedItem.isEmpty())
/*  886 */       this.residents.forEach(v -> v.onStorageChange(updatedItem)); 
/*      */   }
/*      */   
/*      */   public void onStorageChange(TileEntityChest chest, int slot, ItemStack updatedItem) {
/*  890 */     InventoryScanner scanner = this.storageScanners.get(chest.getPos());
/*  891 */     if (scanner != null) {
/*  892 */       scanner.updateSlotSilent(slot);
/*      */     }
/*  894 */     notifyResidentsStorageUpdate(updatedItem);
/*      */   }
/*      */   
/*      */   public void addResident(EntityVillagerTek v) {
/*  898 */     this.residents.add(v);
/*      */   }
/*      */   
/*      */   public void removeResident(EntityVillagerTek v) {
/*  902 */     this.residents.remove(v);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void update() {
/*  909 */     this.jobs.tick();
/*  910 */     this.blockFinder.update();
/*  911 */     this.farmFinder.update();
/*  912 */     this.mineFinder.update();
/*  913 */     this.pathingGraph.update();
/*  914 */     this.raidScheduler.update();
/*      */     
/*  916 */     tickInventoryScanners();
/*      */     
/*  918 */     for (List<VillageStructure> lst : this.structures.values()) {
/*  919 */       for (VillageStructure struct : lst) {
/*  920 */         struct.update();
/*      */       }
/*      */     } 
/*      */     
/*  924 */     this.tickCounter++;
/*  925 */     this.cleanTick--;
/*  926 */     if (this.cleanTick <= 0) {
/*  927 */       cleanVillage();
/*      */     }
/*      */ 
/*      */     
/*  931 */     if (!this.world.isDaytime()) {
/*  932 */       this.merchantScheduler.resetDay();
/*  933 */       this.nomadScheduler.resetDay();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isValid() {
/*  939 */     if (this.isDestroyed) {
/*  940 */       return false;
/*      */     }
/*  942 */     ItemStack villageToken = getVillageToken();
/*  943 */     if (villageToken.isEmpty()) {
/*  944 */       debugOut("Village has no Token");
/*  945 */       return false;
/*      */     } 
/*      */     
/*  948 */     if (!getTownData().getUUID().equals(this.villageUUID)) {
/*  949 */       debugOut("Village UUID changed.  Token swap?");
/*  950 */       return false;
/*      */     } 
/*      */     
/*  953 */     if (getOrigin() != null && !this.world.isBlockLoaded(getOrigin())) {
/*  954 */       debugOut("Village unloaded");
/*  955 */       return false;
/*      */     } 
/*      */     
/*  958 */     if (this.center == null) {
/*  959 */       System.err.println("Village has no center");
/*  960 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  968 */     if (this.structures.size() <= 0) {
/*  969 */       debugOut("Village has no structures");
/*  970 */       return false;
/*      */     } 
/*      */     
/*  973 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isInVillage(BlockPos pos) {
/*  977 */     return (Math.max(Math.abs(getOrigin().getZ() - pos.getZ()), Math.abs(getOrigin().getX() - pos.getX())) < 120);
/*      */   }
/*      */   
/*      */   public BlockPos getEnemySighting() {
/*  981 */     return this.enemySighting;
/*      */   }
/*      */   
/*      */   public boolean enemySeenRecently() {
/*  985 */     return !this.enemies.isEmpty();
/*      */   }
/*      */   
/*      */   private void clearDeadEnemies() {
/*  989 */     Iterator<VillageEnemy> iterator = this.enemies.iterator();
/*  990 */     while (iterator.hasNext()) {
/*      */       
/*  992 */       VillageEnemy enemy = iterator.next();
/*  993 */       if (!enemy.enemy.isEntityAlive() || Math.abs(this.tickCounter - enemy.aggressionTime) > 400)
/*      */       {
/*  995 */         iterator.remove();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void cleanDebugTurds() {
/* 1001 */     List<EntityArmorStand> stands = this.world.getEntitiesWithinAABB(EntityArmorStand.class, getAABB());
/* 1002 */     for (EntityArmorStand st : stands) {
/* 1003 */       if (st.getCustomNameTag().equals("PathNode") && st.ticksExisted > 400) {
/* 1004 */         st.setDead();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void addOrRenewEnemy(EntityLivingBase enemy, int threat) {
/* 1010 */     this.enemySighting = enemy.getPosition();
/*      */     
/* 1012 */     for (VillageEnemy existingEnemy : this.enemies) {
/*      */       
/* 1014 */       if (existingEnemy.enemy == enemy) {
/* 1015 */         existingEnemy.threat += threat;
/* 1016 */         existingEnemy.aggressionTime = this.tickCounter;
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 1021 */     this.enemies.add(new VillageEnemy(enemy, this.tickCounter));
/*      */   }
/*      */   
/*      */   public EntityLivingBase getEnemyTarget(EntityVillagerTek villager) {
/* 1025 */     double closest = Double.MAX_VALUE;
/* 1026 */     boolean foundMinion = false;
/* 1027 */     EntityLivingBase target = null;
/* 1028 */     for (VillageEnemy existingEnemy : this.enemies) {
/*      */       
/* 1030 */       if (existingEnemy.enemy.isEntityAlive()) {
/*      */         
/* 1032 */         boolean isMinion = EntityNecromancer.isMinion(existingEnemy.enemy);
/* 1033 */         if (isMinion && !foundMinion) {
/* 1034 */           closest = Double.MAX_VALUE;
/* 1035 */           target = null;
/*      */         } 
/*      */         
/* 1038 */         if (isMinion || !foundMinion) {
/* 1039 */           double dist = existingEnemy.enemy.getDistanceSq((Entity)villager);
/* 1040 */           if (dist < closest) {
/* 1041 */             closest = dist;
/* 1042 */             target = existingEnemy.enemy;
/*      */           } 
/*      */           
/* 1045 */           foundMinion = isMinion;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1050 */     return target;
/*      */   }
/*      */   
/*      */   private void cleanDefenders() {
/* 1054 */     this.activeDefenders.removeIf(d -> (d.getAttackTarget() == null || !d.getAttackTarget().isEntityAlive() || !d.isEntityAlive()));
/*      */   }
/*      */   
/*      */   public void addActiveDefender(EntityVillagerTek villager) {
/* 1058 */     this.activeDefenders.add(villager);
/*      */   }
/*      */   
/*      */   public EntityVillagerTek getActiveDefender(BlockPos pos) {
/* 1062 */     cleanDefenders();
/* 1063 */     EntityVillagerTek defender = this.activeDefenders.stream().min(Comparator.comparing(e -> Float.valueOf(e.getHealth()))).orElse(null);
/* 1064 */     return defender;
/*      */   }
/*      */   
/*      */   public void onBlockUpdate(World w, BlockPos bp) {
/* 1068 */     this.pathingGraph.onBlockUpdate(w, bp);
/*      */   }
/*      */   
/*      */   public void onCropGrowEvent(BlockEvent.CropGrowEvent event) {
/* 1072 */     if (isInVillage(event.getPos())) {
/* 1073 */       this.farmFinder.onCropGrowEvent(event);
/*      */     }
/*      */   }
/*      */   
/*      */   public void reportVillagerDamage(EntityVillagerTek villager, DamageSource damageSrc, float damageAmount) {
/* 1078 */     villager.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 200));
/* 1079 */     String damageMessage = villager.getDisplayName().getUnformattedText() + " [" + ((villager.getProfessionType() == null) ? "" : (villager.getProfessionType()).name) + "] has taken " + String.format("%.2f", new Object[] { Float.valueOf(damageAmount) }) + " damage";
/*      */     
/* 1081 */     if (damageSrc.getTrueSource() != null) {
/* 1082 */       damageMessage = damageMessage + " from " + damageSrc.getTrueSource().getDisplayName().getUnformattedText();
/*      */     }
/* 1084 */     String finalDamageMessage = damageMessage;
/*      */     
/* 1086 */     sendChatMessage(finalDamageMessage);
/*      */     
/* 1088 */     debugOut("[Villager Damage] " + finalDamageMessage);
/*      */   }
/*      */ 
/*      */   
/*      */   public void reportVillagerDeath(EntityVillagerTek villager, DamageSource damageSrc) {
/* 1093 */     String damageMessage = villager.getDisplayName().getUnformattedText() + " [" + (villager.getProfessionType()).name + "] has been killed by " + damageSrc.getDamageType() + " damage. [" + villager.getPosition().getX() + ", " + villager.getPosition().getY() + ", " + villager.getPosition().getZ() + "]";
/* 1094 */     playEvent(SoundEvents.ENTITY_VILLAGER_DEATH, (ITextComponent)new TextComponentString(damageMessage));
/*      */     
/* 1096 */     List<EntityNecromancer> necros = this.world.getEntitiesWithinAABB(EntityNecromancer.class, (new AxisAlignedBB(getOrigin())).grow(120.0D));
/* 1097 */     necros.forEach(n -> n.notifyVillagerDeath());
/*      */   }
/*      */   
/*      */   public void sendChatMessage(String msg) {
/* 1101 */     sendChatMessage((ITextComponent)new TextComponentString(msg));
/*      */   }
/*      */   
/*      */   public void sendChatMessage(ITextComponent textComponent) {
/* 1105 */     List<EntityPlayerMP> nearbyPlayers = this.world.getPlayers(EntityPlayerMP.class, p -> (p.getPosition().distanceSq((Vec3i)getOrigin()) < 40000.0D));
/* 1106 */     nearbyPlayers.stream().forEach(p -> p.sendMessage(textComponent));
/* 1107 */     debugOut(textComponent.getUnformattedText());
/*      */   }
/*      */   
/*      */   public void playEvent(SoundEvent soundEvent, ITextComponent textComponent) {
/* 1111 */     for (EntityPlayerMP entityPlayer : this.world.getEntitiesWithinAABB(EntityPlayerMP.class, getAABB().grow(50.0D))) {
/*      */       
/* 1113 */       entityPlayer.world.playSound(null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.MASTER, 1.2F, 1.0F);
/* 1114 */       entityPlayer.sendMessage(textComponent);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean hasBlock(Block b) {
/* 1119 */     return this.blockFinder.hasBlock(b);
/*      */   }
/*      */   
/*      */   public BlockPos requestBlock(Block b) {
/* 1123 */     return this.blockFinder.requestBlock(b);
/*      */   }
/*      */   
/*      */   public void releaseBlockClaim(Block block, BlockPos pos) {
/* 1127 */     this.blockFinder.releaseClaim(this.world, block, pos);
/*      */   }
/*      */   
/*      */   public VillageStructureMineshaft requestMineshaft(EntityVillagerTek miner, Predicate<VillageStructureMineshaft> pred, BiPredicate<VillageStructureMineshaft, VillageStructureMineshaft> compare) {
/* 1131 */     return this.mineFinder.requestMineshaft(miner, pred, compare);
/*      */   }
/*      */   public BlockPos requestMaxAgeCrop() {
/* 1134 */     return this.farmFinder.getMaxAgeCrop();
/*      */   } public BlockPos requestFarmland(Predicate<BlockPos> pred) {
/* 1136 */     return this.farmFinder.getFarmland(pred);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\Village.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */