/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.DataSerializers;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIFeedAnimal;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIHarvestAnimal;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIReturnLostAnimal;
/*     */ import net.tangotek.tektopia.storage.ItemDesire;
/*     */ import net.tangotek.tektopia.structures.VillageStructureChickenCoop;
/*     */ import net.tangotek.tektopia.structures.VillageStructureCowPen;
/*     */ import net.tangotek.tektopia.structures.VillageStructurePigPen;
/*     */ import net.tangotek.tektopia.structures.VillageStructureSheepPen;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityRancher extends EntityVillagerTek {
/*  27 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityRancher.class);
/*     */   
/*  29 */   private static final DataParameter<Boolean> RETURN_LOST_ANIMALS = EntityDataManager.createKey(EntityRancher.class, DataSerializers.BOOLEAN);
/*  30 */   private static final DataParameter<Boolean> FEED_COW = EntityDataManager.createKey(EntityRancher.class, DataSerializers.BOOLEAN);
/*  31 */   private static final DataParameter<Boolean> FEED_SHEEP = EntityDataManager.createKey(EntityRancher.class, DataSerializers.BOOLEAN);
/*  32 */   private static final DataParameter<Boolean> FEED_PIG = EntityDataManager.createKey(EntityRancher.class, DataSerializers.BOOLEAN);
/*  33 */   private static final DataParameter<Boolean> FEED_CHICKEN = EntityDataManager.createKey(EntityRancher.class, DataSerializers.BOOLEAN);
/*  34 */   private static final DataParameter<Boolean> HARVEST_COW = EntityDataManager.createKey(EntityRancher.class, DataSerializers.BOOLEAN);
/*  35 */   private static final DataParameter<Boolean> HARVEST_SHEEP = EntityDataManager.createKey(EntityRancher.class, DataSerializers.BOOLEAN);
/*     */   
/*     */   private VillageStructureType priorityPen;
/*     */   
/*     */   static {
/*  40 */     animHandler.addAnim("tektopia", "villager_take", "rancher_m", true);
/*  41 */     EntityVillagerTek.setupAnimations(animHandler, "rancher_m");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityRancher(World worldIn) {
/*  47 */     super(worldIn, ProfessionType.RANCHER, VillagerRole.VILLAGER.value);
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
/*     */   public AnimationHandler getAnimationHandler() {
/*  59 */     return animHandler;
/*     */   }
/*     */   
/*     */   protected void entityInit() {
/*  63 */     super.entityInit();
/*     */     
/*  65 */     registerAIFilter("return_lost_animals", RETURN_LOST_ANIMALS);
/*  66 */     registerAIFilter("feed_cow", FEED_COW);
/*  67 */     registerAIFilter("feed_sheep", FEED_SHEEP);
/*  68 */     registerAIFilter("feed_chicken", FEED_CHICKEN);
/*  69 */     registerAIFilter("feed_pig", FEED_PIG);
/*  70 */     registerAIFilter("harvest_cow", HARVEST_COW);
/*  71 */     registerAIFilter("harvest_sheep", HARVEST_SHEEP);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initEntityAI() {
/*  78 */     super.initEntityAI();
/*     */     
/*  80 */     getDesireSet().addItemDesire(new ItemDesire("CowFood", VillageStructureCowPen.isFood(), 1, 6, 16, v -> isAIFilterEnabled("feed_cow")));
/*  81 */     getDesireSet().addItemDesire(new ItemDesire("ChickenFood", VillageStructureChickenCoop.isFood(), 1, 6, 16, v -> isAIFilterEnabled("feed_chicken")));
/*  82 */     getDesireSet().addItemDesire(new ItemDesire("PigFood", VillageStructurePigPen.isFood(), 1, 6, 16, v -> isAIFilterEnabled("feed_pig")));
/*  83 */     getDesireSet().addItemDesire(new ItemDesire("SheepFood", VillageStructureSheepPen.isFood(), 1, 6, 16, v -> isAIFilterEnabled("feed_sheep")));
/*  84 */     getDesireSet().addItemDesire(new ItemDesire((Item)Items.SHEARS, 1, 1, 1, v -> isAIFilterEnabled("harvest_sheep")));
/*  85 */     getDesireSet().addItemDesire(new ItemDesire(Items.BUCKET, 1, 2, 2, v -> isAIFilterEnabled("harvest_cow")));
/*     */     
/*  87 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIAttachLeadToLostAnimal(this, p -> p.isWorkTime()));
/*  88 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIReturnLostAnimal(this));
/*     */     
/*  90 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIFeedAnimal(this, VillageStructureType.COW_PEN, 0, "feed_cow"));
/*  91 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIFeedAnimal(this, VillageStructureType.CHICKEN_COOP, 1, "feed_chicken"));
/*  92 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIFeedAnimal(this, VillageStructureType.SHEEP_PEN, 2, "feed_sheep"));
/*  93 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIFeedAnimal(this, VillageStructureType.PIG_PEN, 3, "feed_pig"));
/*  94 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIHarvestAnimal(this, VillageStructureType.COW_PEN, new ItemStack(Items.BUCKET), p -> (p.isWorkTime() && p.isAIFilterEnabled("harvest_cow") && p.getVillage().getStorageCount(()) <= 3)));
/*  95 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIHarvestAnimal(this, VillageStructureType.SHEEP_PEN, new ItemStack((Item)Items.SHEARS), p -> (p.isWorkTime() && p.isAIFilterEnabled("harvest_sheep"))));
/*     */   }
/*     */   
/*     */   public void onLivingUpdate() {
/*  99 */     super.onLivingUpdate();
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> isHarvestItem() {
/* 103 */     return p -> (p.getItem() == Items.MILK_BUCKET || p.getItem() == Items.EGG || p.getItem() == Item.getItemFromBlock(Blocks.WOOL) || super.isHarvestItem().test(p));
/*     */   }
/*     */   
/*     */   public void setPriorityPen(VillageStructureType t) {
/* 107 */     this.priorityPen = t;
/*     */   }
/*     */   
/*     */   public VillageStructureType getPriorityPen() {
/* 111 */     return this.priorityPen;
/*     */   }
/*     */   
/*     */   protected boolean canVillagerPickupItem(ItemStack itemIn) {
/* 115 */     return (itemIn.getItem() == Items.EGG || itemIn.getItem() == Item.getItemFromBlock(Blocks.WOOL) || super.canVillagerPickupItem(itemIn));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityRancher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */