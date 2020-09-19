/*     */ package net.tangotek.tektopia;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.ChunkPos;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraft.world.IWorldEventListener;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.storage.MapStorage;
/*     */ import net.minecraft.world.storage.WorldSavedData;
/*     */ import net.minecraftforge.event.world.BlockEvent;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import net.tangotek.tektopia.caps.IVillageData;
/*     */ import net.tangotek.tektopia.network.PacketVillage;
/*     */ import net.tangotek.tektopia.pathing.PathingWorldListener;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureFactory;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ import net.tangotek.tektopia.tickjob.TickJobQueue;
/*     */ 
/*     */ public class VillageManager extends WorldSavedData {
/*  34 */   private long tick = 0L; protected World world;
/*  35 */   protected Set<Village> villages = new HashSet<>();
/*  36 */   protected Queue<AxisAlignedBB> scanBoxes = new LinkedList<>();
/*     */   protected BlockPos lastVillagerPos;
/*     */   private static final String DATA_IDENTIFIER = "tektopia_VillageManager";
/*  39 */   private final VillageStructureFactory structureFactory = new VillageStructureFactory();
/*     */   private final PathingWorldListener pathWorldListener;
/*  41 */   private BlockPos lastStuck = null;
/*     */   private boolean debugOn = false;
/*  43 */   protected TickJobQueue jobs = new TickJobQueue();
/*     */   
/*     */   public VillageManager(World worldIn) {
/*  46 */     super("tektopia_VillageManager");
/*  47 */     this.world = worldIn;
/*  48 */     this.pathWorldListener = new PathingWorldListener(this);
/*  49 */     this.world.addEventListener((IWorldEventListener)this.pathWorldListener);
/*     */     
/*  51 */     this.jobs.addJob(new TickJob(200, 0, true, () -> sendVillagesToClients()));
/*  52 */     this.jobs.addJob(new TickJob(100, 20, true, () -> processPlayerPositions(128, 32, 128)));
/*  53 */     this.jobs.addJob(new TickJob(15, 10, true, () -> processPlayerPositions(16, 6, 16)));
/*     */   }
/*     */   
/*     */   public static VillageManager get(World world) {
/*  57 */     MapStorage storage = world.getPerWorldStorage();
/*  58 */     VillageManager instance = (VillageManager)storage.getOrLoadData(VillageManager.class, "tektopia_VillageManager");
/*     */     
/*  60 */     if (instance == null) {
/*  61 */       instance = new VillageManager(world);
/*  62 */       storage.setData("tektopia_VillageManager", instance);
/*     */     } 
/*  64 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getItemValue(Item item) {
/*  69 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isChunkFullyLoaded(World world, BlockPos pos) {
/*  74 */     if (world.isRemote) return true; 
/*  75 */     long i = ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4);
/*  76 */     Chunk chunk = (Chunk)((ChunkProviderServer)world.getChunkProvider()).loadedChunks.get(i);
/*  77 */     return (chunk != null && !chunk.unloadQueued);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  82 */     this.tick++;
/*  83 */     this.jobs.tick();
/*     */     
/*  85 */     Iterator<Village> itr = this.villages.iterator();
/*  86 */     while (itr.hasNext()) {
/*  87 */       Village v = itr.next();
/*  88 */       if (!isChunkFullyLoaded(this.world, v.getOrigin()) || !v.isValid()) {
/*  89 */         IVillageData vd = v.getTownData();
/*  90 */         if (vd != null) {
/*  91 */           v.debugOut("Village REMOVED - [" + v.getName() + "]   " + vd.getUUID());
/*     */         }
/*  93 */         v.destroy();
/*  94 */         itr.remove();
/*     */         continue;
/*     */       } 
/*  97 */       v.update();
/*     */     } 
/*     */ 
/*     */     
/* 101 */     procesScanBoxes();
/*     */   }
/*     */   
/*     */   private void sendVillagesToClients() {
/* 105 */     List<EntityPlayerMP> players = this.world.getPlayers(EntityPlayerMP.class, EntitySelectors.IS_ALIVE);
/* 106 */     for (EntityPlayerMP p : players) {
/* 107 */       Village v = getNearestVillage(p.getPosition(), 200);
/* 108 */       TekVillager.NETWORK.sendTo((IMessage)new PacketVillage((v == null) ? null : new VillageClient(v)), p);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processPlayerPositions(int x, int y, int z) {
/* 113 */     List<EntityPlayerMP> players = this.world.getPlayers(EntityPlayerMP.class, EntitySelectors.IS_ALIVE);
/* 114 */     for (EntityPlayerMP p : players) {
/* 115 */       AxisAlignedBB aabb = p.getEntityBoundingBox().grow(x, y, z);
/* 116 */       addScanBox(aabb);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void procesScanBoxes() {
/* 121 */     while (!this.scanBoxes.isEmpty()) {
/* 122 */       AxisAlignedBB aabb = this.scanBoxes.poll();
/* 123 */       ListIterator<EntityItemFrame> itr = this.world.getEntitiesWithinAABB(EntityItemFrame.class, aabb).listIterator();
/*     */       
/* 125 */       while (itr.hasNext()) {
/* 126 */         EntityItemFrame itemFrame = itr.next();
/*     */         
/* 128 */         VillageStructureType structType = VillageStructureFactory.getByItem(itemFrame.getDisplayedItem());
/* 129 */         if (structType != null) {
/* 130 */           boolean validStructure = false;
/* 131 */           BlockPos framePos = itemFrame.getHangingPosition();
/* 132 */           Village village = getNearestVillage(framePos, 360);
/* 133 */           if (village == null && structType == VillageStructureType.TOWNHALL) {
/* 134 */             village = new Village(this.world, framePos);
/* 135 */             this.villages.add(village);
/*     */           } else {
/*     */             
/* 138 */             village = getVillageAt(framePos);
/*     */ 
/*     */             
/* 141 */             if (village != null && 
/* 142 */               !ModItems.isItemVillageBound(itemFrame.getDisplayedItem(), village))
/*     */             {
/*     */               
/* 145 */               if (!ModItems.isItemVillageBound(itemFrame.getDisplayedItem())) {
/*     */                 
/* 147 */                 village.debugOut("Binding Structure Marker to village - " + framePos);
/* 148 */                 ModItems.bindItemToVillage(itemFrame.getDisplayedItem(), village);
/*     */               }
/*     */               else {
/*     */                 
/* 152 */                 village.debugOut("Structure Marker " + itemFrame.getDisplayedItem().getTranslationKey() + " bound to INCORRECT village - " + framePos);
/* 153 */                 village = null;
/*     */               } 
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 159 */           if (village != null) {
/*     */ 
/*     */             
/* 162 */             VillageStructure struct = village.getStructureFromFrame(framePos);
/* 163 */             if (struct == null) {
/* 164 */               struct = VillageStructureFactory.create(structType, this.world, village, itemFrame);
/* 165 */               if (village.addStructure(struct)) {
/* 166 */                 validStructure = true;
/*     */               }
/*     */             } else {
/* 169 */               validStructure = true;
/*     */             } 
/*     */           } 
/*     */           
/* 173 */           if (validStructure && !ModItems.isTaggedItem(itemFrame.getDisplayedItem(), ItemTagType.STRUCTURE)) {
/* 174 */             ModItems.makeTaggedItem(itemFrame.getDisplayedItem(), ItemTagType.STRUCTURE);
/* 175 */             itemFrame.setDisplayedItem(itemFrame.getDisplayedItem()); continue;
/*     */           } 
/* 177 */           if (!validStructure && ModItems.isTaggedItem(itemFrame.getDisplayedItem(), ItemTagType.STRUCTURE)) {
/* 178 */             ModItems.untagItem(itemFrame.getDisplayedItem(), ItemTagType.STRUCTURE);
/* 179 */             itemFrame.setDisplayedItem(itemFrame.getDisplayedItem());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addScanBox(AxisAlignedBB aabb) {
/* 187 */     this.scanBoxes.add(aabb);
/*     */   }
/*     */   
/*     */   public Village getVillageAt(BlockPos blockPos) {
/* 191 */     for (Village v : this.villages) {
/* 192 */       if (v.isLoaded() && 
/* 193 */         v.isInVillage(blockPos)) {
/* 194 */         return v;
/*     */       }
/*     */     } 
/*     */     
/* 198 */     return null;
/*     */   }
/*     */   
/*     */   public Village getNearestVillage(BlockPos blockPos, int maxDist) {
/* 202 */     Village result = null;
/* 203 */     double closest = Double.MAX_VALUE;
/* 204 */     int maxDistSq = maxDist * maxDist;
/* 205 */     for (Village v : this.villages) {
/* 206 */       if (v.isLoaded()) {
/* 207 */         double distSq = v.getOrigin().distanceSq((Vec3i)blockPos);
/* 208 */         if (distSq < closest && distSq < maxDistSq) {
/* 209 */           closest = distSq;
/* 210 */           result = v;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 215 */     return result;
/*     */   }
/*     */   
/*     */   public List<Village> getVillagesNear(BlockPos blockPos, int maxDist) {
/* 219 */     List<Village> result = new ArrayList<>();
/* 220 */     int maxDistSq = maxDist * maxDist;
/* 221 */     for (Village v : this.villages) {
/* 222 */       if (v.isLoaded()) {
/* 223 */         double distSq = v.getOrigin().distanceSq((Vec3i)blockPos);
/* 224 */         if (distSq < maxDistSq) {
/* 225 */           result.add(v);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 230 */     return result;
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
/*     */   public boolean canSleepAt(BlockPos pos) {
/* 244 */     for (Village v : this.villages) {
/* 245 */       if (!v.canSleepAt(pos)) {
/* 246 */         return false;
/*     */       }
/*     */     } 
/* 249 */     return true;
/*     */   }
/*     */   
/*     */   public void setDebugOn(boolean debugOn) {
/* 253 */     this.debugOn = debugOn;
/*     */   }
/*     */   
/*     */   public boolean isDebugOn() {
/* 257 */     return this.debugOn;
/*     */   }
/*     */   
/*     */   public void submitStuck(BlockPos bp) {
/* 261 */     this.lastStuck = bp;
/*     */   }
/*     */   
/*     */   public BlockPos getLastStuck() {
/* 265 */     return this.lastStuck;
/*     */   }
/*     */   
/*     */   public void onBlockUpdate(World w, BlockPos bp) {
/* 269 */     for (Village v : this.villages)
/* 270 */       v.onBlockUpdate(w, bp); 
/*     */   }
/*     */   
/*     */   public void onCropGrowEvent(BlockEvent.CropGrowEvent event) {
/* 274 */     for (Village v : this.villages)
/* 275 */       v.onCropGrowEvent(event); 
/*     */   }
/*     */   
/*     */   public void villageReport(String reportType) {
/* 279 */     System.out.println("VILLAGE REPORT - [" + this.villages.size() + " villages]");
/* 280 */     for (Village v : this.villages) {
/* 281 */       if (v.isLoaded()) {
/* 282 */         v.villageReport(reportType);
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
/*     */   public void readFromNBT(NBTTagCompound nbt) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
/* 303 */     return compound;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\VillageManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */