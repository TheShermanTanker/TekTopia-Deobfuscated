/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.DataSerializers;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.util.SoundCategory;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAICraftItems;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIGatherCane;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIHarvestFarm;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAITillDirt;
/*     */ import net.tangotek.tektopia.entities.crafting.Recipe;
/*     */ import net.tangotek.tektopia.storage.ItemDesire;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityFarmer extends EntityVillagerTek {
/*  35 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityFarmer.class);
/*     */   
/*  37 */   private static final DataParameter<Boolean> HARVEST_WHEAT = EntityDataManager.createKey(EntityFarmer.class, DataSerializers.BOOLEAN);
/*  38 */   private static final DataParameter<Boolean> HARVEST_CARROT = EntityDataManager.createKey(EntityFarmer.class, DataSerializers.BOOLEAN);
/*  39 */   private static final DataParameter<Boolean> HARVEST_POTATO = EntityDataManager.createKey(EntityFarmer.class, DataSerializers.BOOLEAN);
/*  40 */   private static final DataParameter<Boolean> HARVEST_BEETROOT = EntityDataManager.createKey(EntityFarmer.class, DataSerializers.BOOLEAN);
/*  41 */   private static final DataParameter<Boolean> PLANT_WHEAT = EntityDataManager.createKey(EntityFarmer.class, DataSerializers.BOOLEAN);
/*  42 */   private static final DataParameter<Boolean> PLANT_CARROT = EntityDataManager.createKey(EntityFarmer.class, DataSerializers.BOOLEAN);
/*  43 */   private static final DataParameter<Boolean> PLANT_POTATO = EntityDataManager.createKey(EntityFarmer.class, DataSerializers.BOOLEAN);
/*  44 */   private static final DataParameter<Boolean> PLANT_BEETROOT = EntityDataManager.createKey(EntityFarmer.class, DataSerializers.BOOLEAN);
/*  45 */   private static final DataParameter<Boolean> TILL_DIRT = EntityDataManager.createKey(EntityFarmer.class, DataSerializers.BOOLEAN);
/*  46 */   private static final DataParameter<Boolean> GATHER_CANE = EntityDataManager.createKey(EntityFarmer.class, DataSerializers.BOOLEAN);
/*  47 */   private static List<Recipe> craftSet = buildCraftSet();
/*     */   
/*     */   static {
/*  50 */     animHandler.addAnim("tektopia", "villager_pickup", "farmer_m", true);
/*  51 */     animHandler.addAnim("tektopia", "villager_take", "farmer_m", true);
/*  52 */     animHandler.addAnim("tektopia", "villager_hoe", "farmer_m", true);
/*  53 */     animHandler.addAnim("tektopia", "villager_craft", "farmer_m", true);
/*  54 */     EntityVillagerTek.setupAnimations(animHandler, "farmer_m");
/*     */   }
/*     */   
/*  57 */   private static final Map<String, DataParameter<Boolean>> RECIPE_PARAMS = new HashMap<>();
/*     */   static {
/*  59 */     craftSet.forEach(r -> (DataParameter)RECIPE_PARAMS.put(r.getAiFilter(), EntityDataManager.createKey(EntityFarmer.class, DataSerializers.BOOLEAN)));
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityFarmer(World worldIn) {
/*  64 */     super(worldIn, ProfessionType.FARMER, VillagerRole.VILLAGER.value);
/*  65 */     if (this.world.isRemote) {
/*  66 */       addAnimationTrigger("tektopia:villager_pickup", 47, new Runnable()
/*     */           {
/*     */             public void run() {
/*  69 */               EntityFarmer.this.world.playSound(EntityFarmer.this.posX, EntityFarmer.this.posY, EntityFarmer.this.posZ, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, EntityFarmer.this.rand.nextFloat() * 0.4F + 0.8F, false);
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/*  76 */     return animHandler;
/*     */   }
/*     */   
/*     */   protected void entityInit() {
/*  80 */     super.entityInit();
/*  81 */     craftSet.forEach(r -> registerAIFilter(r.getAiFilter(), RECIPE_PARAMS.get(r.getAiFilter())));
/*  82 */     registerAIFilter("harvest_tile.crops", HARVEST_WHEAT);
/*  83 */     registerAIFilter("harvest_tile.carrots", HARVEST_CARROT);
/*  84 */     registerAIFilter("harvest_tile.potatoes", HARVEST_POTATO);
/*  85 */     registerAIFilter("harvest_tile.beetroots", HARVEST_BEETROOT);
/*     */     
/*  87 */     registerAIFilter("plant_tile.crops", PLANT_WHEAT);
/*  88 */     registerAIFilter("plant_tile.carrots", PLANT_CARROT);
/*  89 */     registerAIFilter("plant_tile.potatoes", PLANT_POTATO);
/*  90 */     registerAIFilter("plant_tile.beetroots", PLANT_BEETROOT);
/*     */     
/*  92 */     registerAIFilter("till_dirt", TILL_DIRT);
/*  93 */     registerAIFilter("gather_cane", GATHER_CANE);
/*     */   }
/*     */   
/*     */   protected void initEntityAI() {
/*  97 */     super.initEntityAI();
/*     */     
/*  99 */     getDesireSet().addItemDesire(new ItemDesire(Items.WHEAT_SEEDS, 1, 8, 16, p -> p.isAIFilterEnabled("plant_tile.crops")));
/* 100 */     getDesireSet().addItemDesire(new ItemDesire(Items.BEETROOT_SEEDS, 1, 8, 16, p -> p.isAIFilterEnabled("plant_tile.beetroots")));
/* 101 */     getDesireSet().addItemDesire(new ItemDesire(Items.POTATO, 1, 5, 12, p -> p.isAIFilterEnabled("plant_tile.potatoes")));
/* 102 */     getDesireSet().addItemDesire(new ItemDesire(Items.CARROT, 1, 5, 12, p -> p.isAIFilterEnabled("plant_tile.carrots")));
/* 103 */     getDesireSet().addItemDesire(new ItemDesire(Items.WHEAT, 0, 0, 12, null));
/* 104 */     getDesireSet().addItemDesire(new ItemDesire(Items.BEETROOT, 0, 0, 12, null));
/*     */     
/* 106 */     getDesireSet().addItemDesire((ItemDesire)new UpgradeItemDesire("Hoe", getBestHoe(), 1, 1, 1, p -> p.isWorkTime()));
/* 107 */     craftSet.forEach(r -> getDesireSet().addRecipeDesire(r));
/*     */     
/* 109 */     this.tasks.addTask(50, (EntityAIBase)new EntityAICraftItems(this, craftSet, "villager_craft", null, 80, VillageStructureType.STORAGE, Blocks.CRAFTING_TABLE, p -> p.isWorkTime()));
/*     */     
/* 111 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIPlantFarm(this));
/* 112 */     this.tasks.addTask(50, (EntityAIBase)new EntityAITillDirt(this, p -> p.isWorkTime()));
/* 113 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIGatherCane(this, 0));
/* 114 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIHarvestFarm(this, false));
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Recipe> buildCraftSet() {
/* 119 */     List<Recipe> recipes = new ArrayList<>();
/*     */ 
/*     */     
/* 122 */     List<ItemStack> ingredients = new ArrayList<>();
/* 123 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 124 */     Recipe recipe = new Recipe(ProfessionType.FARMER, "craft_wooden_hoe", 3, new ItemStack(Items.WOODEN_HOE, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.FARMER, 11, 2)), 1, v -> !hasHoe(v))
/*     */       {
/*     */         public ItemStack craft(EntityVillagerTek villager) {
/* 127 */           ItemStack result = super.craft(villager);
/* 128 */           villager.modifyHappy(-5);
/* 129 */           return result;
/*     */         }
/*     */       };
/* 132 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 135 */     return recipes;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
/* 141 */     getInventory().addItem(ModItems.makeTaggedItem(new ItemStack(Items.WOODEN_HOE), ItemTagType.VILLAGER));
/*     */     
/* 143 */     return super.onInitialSpawn(difficulty, livingdata);
/*     */   }
/*     */   
/*     */   public static Item getSeed(IBlockState blockState) {
/* 147 */     if (blockState.getBlock() == Blocks.WHEAT)
/* 148 */       return Items.WHEAT_SEEDS; 
/* 149 */     if (blockState.getBlock() == Blocks.POTATOES)
/* 150 */       return Items.POTATO; 
/* 151 */     if (blockState.getBlock() == Blocks.CARROTS)
/* 152 */       return Items.CARROT; 
/* 153 */     if (blockState.getBlock() == Blocks.BEETROOTS) {
/* 154 */       return Items.BEETROOT_SEEDS;
/*     */     }
/* 156 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ItemStack modifyPickUpStack(ItemStack itemStack) {
/* 162 */     if (itemStack.getItem() == Items.CARROT || itemStack.getItem() == Items.POTATO) {
/* 163 */       int skill = getSkill(getProfessionType());
/* 164 */       if (itemStack.getCount() >= 2 && 
/* 165 */         getRNG().nextInt(100) > skill) {
/* 166 */         itemStack.setCount(itemStack.getCount() - 1);
/*     */       
/*     */       }
/*     */     }
/* 170 */     else if (itemStack.getItem() == Items.WHEAT_SEEDS || itemStack.getItem() == Items.BEETROOT_SEEDS) {
/*     */       
/* 172 */       if (itemStack.getCount() > 1) {
/* 173 */         itemStack.setCount(1);
/*     */       }
/*     */     } 
/* 176 */     return itemStack;
/*     */   }
/*     */   
/*     */   protected boolean canVillagerPickupItem(ItemStack itemIn) {
/* 180 */     return (isHarvestItem().test(itemIn) || isSeed().test(itemIn) || super.canVillagerPickupItem(itemIn));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean hasHoe(EntityVillagerTek villager) {
/* 189 */     List<ItemStack> weaponList = villager.getInventory().getItems(getBestHoe(), 1);
/* 190 */     return !weaponList.isEmpty();
/*     */   }
/*     */   
/*     */   public static Function<ItemStack, Integer> getBestHoe() {
/* 194 */     return p -> (p.getItem() instanceof net.minecraft.item.ItemHoe) ? Integer.valueOf(p.getMaxDamage()) : Integer.valueOf(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Predicate<ItemStack> isSeed() {
/* 203 */     return p -> (p.getItem() == Items.WHEAT_SEEDS || p.getItem() == Items.BEETROOT_SEEDS);
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> isHarvestItem() {
/* 207 */     return p -> (p.getItem() == Items.WHEAT || p.getItem() == Items.CARROT || p.getItem() == Items.BEETROOT || p.getItem() == Items.POTATO || p.getItem() == Items.REEDS || super.isHarvestItem().test(p));
/*     */   }
/*     */   
/*     */   protected void cleanUpInventory() {
/* 211 */     super.cleanUpInventory();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityFarmer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */