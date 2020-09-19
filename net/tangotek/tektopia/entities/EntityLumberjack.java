/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
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
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIChopTree;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAICraftItems;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIPickUpItem;
/*     */ import net.tangotek.tektopia.entities.crafting.Recipe;
/*     */ import net.tangotek.tektopia.storage.ItemDesire;
/*     */ import net.tangotek.tektopia.storage.ItemDesireNoDeliver;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityLumberjack extends EntityVillagerTek {
/*  35 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityLumberjack.class);
/*     */   
/*  37 */   private static final DataParameter<Boolean> CHOP_TREE_OAK = EntityDataManager.createKey(EntityLumberjack.class, DataSerializers.BOOLEAN);
/*  38 */   private static final DataParameter<Boolean> CHOP_TREE_BIRCH = EntityDataManager.createKey(EntityLumberjack.class, DataSerializers.BOOLEAN);
/*  39 */   private static final DataParameter<Boolean> CHOP_TREE_JUNGLE = EntityDataManager.createKey(EntityLumberjack.class, DataSerializers.BOOLEAN);
/*  40 */   private static final DataParameter<Boolean> CHOP_TREE_SPRUCE = EntityDataManager.createKey(EntityLumberjack.class, DataSerializers.BOOLEAN);
/*     */   
/*  42 */   private static final DataParameter<Boolean> PICK_UP_OAK_SAPLINGS = EntityDataManager.createKey(EntityLumberjack.class, DataSerializers.BOOLEAN);
/*  43 */   private static final DataParameter<Boolean> PICK_UP_SPRUCE_SAPLINGS = EntityDataManager.createKey(EntityLumberjack.class, DataSerializers.BOOLEAN);
/*  44 */   private static final DataParameter<Boolean> PICK_UP_BIRCH_SAPLINGS = EntityDataManager.createKey(EntityLumberjack.class, DataSerializers.BOOLEAN);
/*  45 */   private static final DataParameter<Boolean> PICK_UP_JUNGLE_SAPLINGS = EntityDataManager.createKey(EntityLumberjack.class, DataSerializers.BOOLEAN);
/*  46 */   private static final DataParameter<Boolean> PICK_UP_ACACIA_SAPLINGS = EntityDataManager.createKey(EntityLumberjack.class, DataSerializers.BOOLEAN);
/*  47 */   private static final DataParameter<Boolean> PICK_UP_DARK_OAK_SAPLINGS = EntityDataManager.createKey(EntityLumberjack.class, DataSerializers.BOOLEAN);
/*  48 */   private static final DataParameter<Boolean> PICK_UP_APPLES = EntityDataManager.createKey(EntityLumberjack.class, DataSerializers.BOOLEAN);
/*     */   
/*  50 */   private static List<Recipe> craftSet = buildCraftSet();
/*     */   
/*     */   static {
/*  53 */     animHandler.addAnim("tektopia", "villager_chop", "lumberjack_m", true);
/*  54 */     animHandler.addAnim("tektopia", "villager_craft", "lumberjack_m", true);
/*  55 */     EntityVillagerTek.setupAnimations(animHandler, "lumberjack_m");
/*     */   }
/*     */   
/*  58 */   private static final Map<String, DataParameter<Boolean>> RECIPE_PARAMS = new HashMap<>();
/*     */   static {
/*  60 */     craftSet.forEach(r -> (DataParameter)RECIPE_PARAMS.put(r.getAiFilter(), EntityDataManager.createKey(EntityLumberjack.class, DataSerializers.BOOLEAN)));
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityLumberjack(World worldIn) {
/*  65 */     super(worldIn, ProfessionType.LUMBERJACK, VillagerRole.VILLAGER.value);
/*  66 */     if (this.world.isRemote) {
/*  67 */       addAnimationTrigger("tektopia:villager_chop", 48, new Runnable()
/*     */           {
/*     */             public void run() {
/*  70 */               EntityLumberjack.this.world.playSound(EntityLumberjack.this.posX, EntityLumberjack.this.posY, EntityLumberjack.this.posZ, SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 2.0F, EntityLumberjack.this.rand.nextFloat() * 0.2F + 0.9F, false);
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/*  77 */     return animHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
/*  84 */     getInventory().addItem(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 16, BlockPlanks.EnumType.OAK.getMetadata()));
/*     */     
/*  86 */     ItemStack axe = ModItems.makeTaggedItem(new ItemStack(Items.WOODEN_AXE), ItemTagType.VILLAGER);
/*     */     
/*  88 */     getInventory().addItem(axe);
/*     */     
/*  90 */     return super.onInitialSpawn(difficulty, livingdata);
/*     */   }
/*     */   
/*     */   protected static boolean hasAxe(EntityVillagerTek villager) {
/*  94 */     List<ItemStack> weaponList = villager.getInventory().getItems(getBestAxe(villager), 1);
/*  95 */     return !weaponList.isEmpty();
/*     */   }
/*     */   
/*     */   public static Function<ItemStack, Integer> getBestAxe(EntityVillagerTek villager) {
/*  99 */     return p -> (p.getItem() instanceof net.minecraft.item.ItemAxe) ? Integer.valueOf(50 - EntityAIChopTree.getChopCount(villager, p)) : Integer.valueOf(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/* 108 */     super.entityInit();
/* 109 */     craftSet.forEach(r -> registerAIFilter(r.getAiFilter(), RECIPE_PARAMS.get(r.getAiFilter())));
/*     */     
/* 111 */     registerAIFilter("chop_tree_oak", CHOP_TREE_OAK);
/* 112 */     registerAIFilter("chop_tree_birch", CHOP_TREE_BIRCH);
/* 113 */     registerAIFilter("chop_tree_jungle", CHOP_TREE_JUNGLE);
/* 114 */     registerAIFilter("chop_tree_spruce", CHOP_TREE_SPRUCE);
/*     */     
/* 116 */     registerAIFilter("pickup_oak_saplings", PICK_UP_OAK_SAPLINGS);
/* 117 */     registerAIFilter("pickup_spruce_saplings", PICK_UP_SPRUCE_SAPLINGS);
/* 118 */     registerAIFilter("pickup_birch_saplings", PICK_UP_BIRCH_SAPLINGS);
/* 119 */     registerAIFilter("pickup_jungle_saplings", PICK_UP_JUNGLE_SAPLINGS);
/* 120 */     registerAIFilter("pickup_acacia_saplings", PICK_UP_ACACIA_SAPLINGS);
/* 121 */     registerAIFilter("pickup_dark_oak_saplings", PICK_UP_DARK_OAK_SAPLINGS);
/* 122 */     registerAIFilter("pickup_apples", PICK_UP_APPLES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initEntityAI() {
/* 127 */     super.initEntityAI();
/*     */     
/* 129 */     getDesireSet().addItemDesire((ItemDesire)new UpgradeItemDesire("Axe", getBestAxe(this), 1, 1, 1, p -> p.isWorkTime()));
/* 130 */     getDesireSet().addItemDesire((ItemDesire)new ItemDesireNoDeliver("Sapling 0", isSapling(0), 0, 10, 60, null));
/* 131 */     getDesireSet().addItemDesire((ItemDesire)new ItemDesireNoDeliver("Sapling 1", isSapling(1), 0, 10, 60, null));
/* 132 */     getDesireSet().addItemDesire((ItemDesire)new ItemDesireNoDeliver("Sapling 2", isSapling(2), 0, 10, 60, null));
/* 133 */     getDesireSet().addItemDesire((ItemDesire)new ItemDesireNoDeliver("Sapling 3", isSapling(3), 0, 10, 60, null));
/* 134 */     getDesireSet().addItemDesire((ItemDesire)new ItemDesireNoDeliver("Sapling 4", isSapling(4), 0, 10, 60, null));
/* 135 */     getDesireSet().addItemDesire((ItemDesire)new ItemDesireNoDeliver("Sapling 5", isSapling(5), 0, 10, 60, null));
/*     */     
/* 137 */     craftSet.forEach(r -> getDesireSet().addRecipeDesire(r));
/*     */     
/* 139 */     this; this.tasks.addTask(50, (EntityAIBase)new EntityAICraftItems(this, craftSet, "villager_craft", null, 80, VillageStructureType.STORAGE, Blocks.CRAFTING_TABLE, p -> p.isWorkTime()));
/*     */     
/* 141 */     List<EntityAIPickUpItem.PickUpData> pickUpCounts = new ArrayList<>();
/* 142 */     pickUpCounts.add(new EntityAIPickUpItem.PickUpData(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 0), 60, "pickup_oak_saplings"));
/* 143 */     pickUpCounts.add(new EntityAIPickUpItem.PickUpData(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 1), 60, "pickup_spruce_saplings"));
/* 144 */     pickUpCounts.add(new EntityAIPickUpItem.PickUpData(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 2), 60, "pickup_birch_saplings"));
/* 145 */     pickUpCounts.add(new EntityAIPickUpItem.PickUpData(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 3), 60, "pickup_jungle_saplings"));
/* 146 */     pickUpCounts.add(new EntityAIPickUpItem.PickUpData(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 4), 60, "pickup_acacia_saplings"));
/* 147 */     pickUpCounts.add(new EntityAIPickUpItem.PickUpData(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 5), 60, "pickup_dark_oak_saplings"));
/* 148 */     pickUpCounts.add(new EntityAIPickUpItem.PickUpData(new ItemStack(Items.APPLE, 1), 10, "pickup_apples"));
/* 149 */     addTask(50, (EntityAIBase)new EntityAIPickUpItem(this, pickUpCounts, 1));
/*     */     
/* 151 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIChopTree(this, true));
/*     */   }
/*     */   
/*     */   private static List<Recipe> buildCraftSet() {
/* 155 */     List<Recipe> recipes = new ArrayList<>();
/*     */ 
/*     */     
/* 158 */     List<ItemStack> ingredients = new ArrayList<>();
/* 159 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 160 */     Recipe recipe = new Recipe(ProfessionType.LUMBERJACK, "craft_wooden_axe", 3, new ItemStack(Items.WOODEN_AXE, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.LUMBERJACK, 11, 2)), 1, v -> !hasAxe(v))
/*     */       {
/*     */         public ItemStack craft(EntityVillagerTek villager) {
/* 163 */           ItemStack result = super.craft(villager);
/* 164 */           villager.modifyHappy(-5);
/* 165 */           return result;
/*     */         }
/*     */       };
/*     */     
/* 169 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 172 */     return recipes;
/*     */   }
/*     */   
/*     */   public void onLivingUpdate() {
/* 176 */     super.onLivingUpdate();
/*     */   }
/*     */   
/*     */   protected boolean canVillagerPickupItem(ItemStack itemIn) {
/* 180 */     return (isHarvestItem().test(itemIn) || isSapling().test(itemIn) || itemIn.getItem() == Items.APPLE || super.canVillagerPickupItem(itemIn));
/*     */   }
/*     */   
/*     */   public static Predicate<ItemStack> isSapling() {
/* 184 */     return p -> (p.getItem() == Item.getItemFromBlock(Blocks.SAPLING));
/*     */   }
/*     */   
/*     */   public static Predicate<ItemStack> isSapling(int type) {
/* 188 */     return p -> (isSapling().test(p) && p.getMetadata() == type);
/*     */   }
/*     */   
/*     */   public Predicate<EntityVillagerTek> hasAxe() {
/* 192 */     return p -> !p.getInventory().getItem(getBestAxe(this)).isEmpty();
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> isHarvestItem() {
/* 196 */     return p -> (p.getItem() == Item.getItemFromBlock(Blocks.LOG) || p.getItem() == Item.getItemFromBlock(Blocks.LOG2) || super.isHarvestItem().test(p));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ItemStack modifyPickUpStack(ItemStack itemStack) {
/* 203 */     if (itemStack.getItem() == Items.APPLE) {
/* 204 */       ModItems.makeTaggedItem(itemStack, ItemTagType.VILLAGER);
/*     */     }
/*     */     
/* 207 */     return itemStack;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityLumberjack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */