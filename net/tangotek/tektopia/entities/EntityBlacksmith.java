/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
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
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAICraftItems;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIEmptyFurnace;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAISmelting;
/*     */ import net.tangotek.tektopia.entities.crafting.Recipe;
/*     */ import net.tangotek.tektopia.storage.ItemDesire;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityBlacksmith extends EntityVillagerTek {
/*  33 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityBlacksmith.class);
/*     */   
/*  35 */   private static final DataParameter<Boolean> SMELT_IRON = EntityDataManager.createKey(EntityBlacksmith.class, DataSerializers.BOOLEAN);
/*  36 */   private static final DataParameter<Boolean> SMELT_GOLD = EntityDataManager.createKey(EntityBlacksmith.class, DataSerializers.BOOLEAN);
/*  37 */   private static final DataParameter<Boolean> SMELT_CHARCOAL = EntityDataManager.createKey(EntityBlacksmith.class, DataSerializers.BOOLEAN);
/*     */   
/*  39 */   private static List<Recipe> craftSetAnvil = buildCraftSetAnvil();
/*  40 */   private static List<Recipe> craftSetBench = buildCraftSetBench();
/*     */   
/*  42 */   private static final Map<String, DataParameter<Boolean>> RECIPE_PARAMS = new HashMap<>();
/*     */   static {
/*  44 */     craftSetAnvil.forEach(r -> (DataParameter)RECIPE_PARAMS.put(r.getAiFilter(), EntityDataManager.createKey(EntityBlacksmith.class, DataSerializers.BOOLEAN)));
/*  45 */     craftSetBench.forEach(r -> (DataParameter)RECIPE_PARAMS.put(r.getAiFilter(), EntityDataManager.createKey(EntityBlacksmith.class, DataSerializers.BOOLEAN)));
/*     */ 
/*     */ 
/*     */     
/*  49 */     animHandler.addAnim("tektopia", "villager_hammer", "blacksmith_m", true);
/*  50 */     animHandler.addAnim("tektopia", "villager_craft", "blacksmith_m", true);
/*  51 */     EntityVillagerTek.setupAnimations(animHandler, "blacksmith_m");
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityBlacksmith(World worldIn) {
/*  56 */     super(worldIn, ProfessionType.BLACKSMITH, VillagerRole.VILLAGER.value);
/*  57 */     addAnimationTrigger("tektopia:villager_hammer", 47, new Runnable()
/*     */         {
/*     */           public void run() {
/*  60 */             EntityBlacksmith.this.world.playSound(EntityBlacksmith.this.posX, EntityBlacksmith.this.posY, EntityBlacksmith.this.posZ, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F + EntityBlacksmith.this.rand.nextFloat() * 0.5F, EntityBlacksmith.this.rand.nextFloat() * 0.4F + 0.8F, false);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/*  66 */     return animHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  71 */     super.entityInit();
/*  72 */     craftSetAnvil.forEach(r -> registerAIFilter(r.getAiFilter(), RECIPE_PARAMS.get(r.getAiFilter())));
/*  73 */     craftSetBench.forEach(r -> registerAIFilter(r.getAiFilter(), RECIPE_PARAMS.get(r.getAiFilter())));
/*  74 */     registerAIFilter("smelt_iron", SMELT_IRON);
/*  75 */     registerAIFilter("smelt_gold", SMELT_GOLD);
/*  76 */     registerAIFilter("smelt_charcoal", SMELT_CHARCOAL);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initEntityAI() {
/*  81 */     super.initEntityAI();
/*     */     
/*  83 */     getDesireSet().addItemDesire(new ItemDesire("Fuel", EntityAISmelting.getBestFuel(), 1, 6, 10, null));
/*  84 */     getDesireSet().addItemDesire(new ItemDesire(Blocks.IRON_ORE, 0, 8, 16, p -> p.isAIFilterEnabled("smelt_iron")));
/*  85 */     getDesireSet().addItemDesire(new ItemDesire(Blocks.GOLD_ORE, 0, 8, 16, p -> p.isAIFilterEnabled("smelt_gold")));
/*  86 */     getDesireSet().addItemDesire(new ItemDesire(Blocks.LOG, 0, 4, 8, p -> p.isAIFilterEnabled("smelt_charcoal")));
/*  87 */     craftSetAnvil.forEach(r -> getDesireSet().addRecipeDesire(r));
/*  88 */     craftSetBench.forEach(r -> getDesireSet().addRecipeDesire(r));
/*     */     
/*  90 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIEmptyFurnace(this, VillageStructureType.BLACKSMITH, isSmelted()));
/*  91 */     this; this.tasks.addTask(50, (EntityAIBase)new EntityAICraftItems(this, craftSetAnvil, "villager_hammer", new ItemStack((Item)ModItems.ironHammer, 1), 60, VillageStructureType.BLACKSMITH, Blocks.ANVIL, p -> p.isWorkTime()));
/*  92 */     this; this.tasks.addTask(50, (EntityAIBase)new EntityAICraftItems(this, craftSetBench, "villager_craft", null, 80, VillageStructureType.BLACKSMITH, Blocks.CRAFTING_TABLE, p -> p.isWorkTime()));
/*  93 */     this.tasks.addTask(50, (EntityAIBase)new EntityAISmelting(this, VillageStructureType.BLACKSMITH, p -> true, bestSmeltable(this), () -> tryAddSkill(ProfessionType.BLACKSMITH, 4)));
/*     */     
/*  95 */     addTask(50, (EntityAIBase)new EntityAISmelting(this, VillageStructureType.BLACKSMITH, p -> (!hasCoal(p, 1) && p.isAIFilterEnabled("smelt_charcoal")), p -> Integer.valueOf((p.getItem() == Item.getItemFromBlock(Blocks.LOG)) ? 1 : 0), () -> tryAddSkill(ProfessionType.BLACKSMITH, 10)));
/*     */   }
/*     */   
/*     */   private static List<Recipe> buildCraftSetAnvil() {
/*  99 */     List<Recipe> recipes = new ArrayList<>();
/*     */ 
/*     */     
/* 102 */     List<ItemStack> ingredients = new ArrayList<>();
/* 103 */     ingredients.add(new ItemStack(Items.DIAMOND, 2));
/* 104 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 105 */     Recipe recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_diamond_sword", 1, new ItemStack(Items.DIAMOND_SWORD, 1), ingredients, 2, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 9, 3)), 1);
/* 106 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 109 */     ingredients = new ArrayList<>();
/* 110 */     ingredients.add(new ItemStack(Items.DIAMOND, 4));
/* 111 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_diamond_boots", 1, new ItemStack((Item)Items.DIAMOND_BOOTS, 1), ingredients, 2, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 9, 3)), 1);
/* 112 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 115 */     ingredients = new ArrayList<>();
/* 116 */     ingredients.add(new ItemStack(Items.DIAMOND, 8));
/* 117 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_diamond_chestplate", 1, new ItemStack((Item)Items.DIAMOND_CHESTPLATE, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 12, 5)), 1);
/* 118 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 121 */     ingredients = new ArrayList<>();
/* 122 */     ingredients.add(new ItemStack(Items.DIAMOND, 7));
/* 123 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_diamond_leggings", 1, new ItemStack((Item)Items.DIAMOND_LEGGINGS, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 10, 4)), 1);
/* 124 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 127 */     ingredients = new ArrayList<>();
/* 128 */     ingredients.add(new ItemStack(Items.DIAMOND, 5));
/* 129 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_diamond_helmet", 1, new ItemStack((Item)Items.DIAMOND_HELMET, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 8, 3)), 1);
/* 130 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 133 */     ingredients = new ArrayList<>();
/* 134 */     ingredients.add(new ItemStack(Items.DIAMOND, 3));
/* 135 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 136 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_diamond_axe", 1, new ItemStack(Items.DIAMOND_AXE, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 9, 3)), 1);
/* 137 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 140 */     ingredients = new ArrayList<>();
/* 141 */     ingredients.add(new ItemStack(Items.DIAMOND, 3));
/* 142 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 143 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_diamond_pickaxe", 1, new ItemStack(Items.DIAMOND_PICKAXE, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 9, 3)), 1);
/* 144 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 147 */     ingredients = new ArrayList<>();
/* 148 */     ingredients.add(new ItemStack(Items.IRON_INGOT, 3));
/* 149 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 150 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_iron_axe", 2, new ItemStack(Items.IRON_AXE, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 9, 3)), 1);
/* 151 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 154 */     ingredients = new ArrayList<>();
/* 155 */     ingredients.add(new ItemStack(Items.IRON_INGOT, 3));
/* 156 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 157 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_iron_pickaxe", 2, new ItemStack(Items.IRON_PICKAXE, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 9, 3)), 1);
/* 158 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 161 */     ingredients = new ArrayList<>();
/* 162 */     ingredients.add(new ItemStack(Items.IRON_INGOT, 2));
/* 163 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 164 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_iron_sword", 2, new ItemStack(Items.IRON_SWORD, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 9, 3)), 1);
/* 165 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 168 */     ingredients = new ArrayList<>();
/* 169 */     ingredients.add(new ItemStack(Items.IRON_INGOT, 2));
/* 170 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 171 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_iron_hoe", 2, new ItemStack(Items.IRON_HOE, 1), ingredients, 2, 3, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 9, 3)), 1);
/* 172 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 175 */     ingredients = new ArrayList<>();
/* 176 */     ingredients.add(new ItemStack(Items.IRON_INGOT, 3));
/* 177 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_bucket", 3, new ItemStack(Items.BUCKET, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 6, 2)), 1);
/* 178 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 181 */     ingredients = new ArrayList<>();
/* 182 */     ingredients.add(new ItemStack(Items.IRON_INGOT, 2));
/* 183 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_shears", 3, new ItemStack((Item)Items.SHEARS, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 6, 2)), 2);
/* 184 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 187 */     ingredients = new ArrayList<>();
/* 188 */     ingredients.add(new ItemStack(Items.IRON_INGOT, 4));
/* 189 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_iron_boots", 2, new ItemStack((Item)Items.IRON_BOOTS, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 6, 2)), 1);
/* 190 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 193 */     ingredients = new ArrayList<>();
/* 194 */     ingredients.add(new ItemStack(Items.IRON_INGOT, 8));
/* 195 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_iron_chestplate", 1, new ItemStack((Item)Items.IRON_CHESTPLATE, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 12, 5)), 1);
/* 196 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 199 */     ingredients = new ArrayList<>();
/* 200 */     ingredients.add(new ItemStack(Items.IRON_INGOT, 7));
/* 201 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_iron_leggings", 1, new ItemStack((Item)Items.IRON_LEGGINGS, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 10, 4)), 1);
/* 202 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 205 */     ingredients = new ArrayList<>();
/* 206 */     ingredients.add(new ItemStack(Items.IRON_INGOT, 5));
/* 207 */     recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_iron_helmet", 2, new ItemStack((Item)Items.IRON_HELMET, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 8, 3)), 1);
/* 208 */     recipes.add(recipe);
/*     */ 
/*     */ 
/*     */     
/* 212 */     return recipes;
/*     */   }
/*     */   
/*     */   private static List<Recipe> buildCraftSetBench() {
/* 216 */     List<Recipe> recipes = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/* 220 */     List<ItemStack> ingredients = new ArrayList<>();
/* 221 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 222 */     ingredients.add(new ItemStack(Items.COAL, 8));
/* 223 */     Recipe recipe = new Recipe(ProfessionType.BLACKSMITH, "craft_torch", 9, new ItemStack(Item.getItemFromBlock(Blocks.TORCH), 32), ingredients, 1, 8, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BLACKSMITH, 5, 2)), 64);
/* 224 */     recipes.add(recipe);
/*     */     
/* 226 */     return recipes;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 231 */     super.onLivingUpdate();
/*     */   }
/*     */   
/*     */   protected void cleanUpInventory() {
/* 235 */     super.cleanUpInventory();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Predicate<ItemStack> isDeliverable() {
/* 240 */     return p -> {
/*     */         this;
/*     */         return craftSetAnvil.stream().anyMatch(());
/*     */       };
/*     */   } protected boolean canVillagerPickupItem(ItemStack itemIn) {
/* 245 */     return (isDeliverable().test(itemIn) || itemIn.getItem() == Items.COAL || itemIn.getItem() == Items.IRON_INGOT || itemIn.getItem() == Item.getItemFromBlock(Blocks.IRON_ORE) || super.canVillagerPickupItem(itemIn));
/*     */   }
/*     */   private static Predicate<ItemStack> isSmelted() {
/* 248 */     return p -> (p.getItem() == Items.IRON_INGOT || p.getItem() == Items.GOLD_INGOT || p.getItem() == Items.COAL);
/*     */   }
/*     */   
/*     */   protected static boolean hasCoal(EntityVillagerTek villager, int req) {
/* 252 */     int count = villager.getInventory().getItemCount(isCoal());
/* 253 */     return (count >= req);
/*     */   }
/*     */   public static Predicate<ItemStack> isCoal() {
/* 256 */     return p -> (p.getItem() == Items.COAL);
/*     */   }
/*     */   private static Function<ItemStack, Integer> bestSmeltable(EntityVillagerTek villager) {
/* 259 */     return p -> 
/* 260 */       (p.getItem() == Item.getItemFromBlock(Blocks.IRON_ORE) && villager.isAIFilterEnabled("smelt_iron")) ? Integer.valueOf(3) : (
/*     */       
/* 262 */       (p.getItem() == Item.getItemFromBlock(Blocks.GOLD_ORE) && villager.isAIFilterEnabled("smelt_gold")) ? Integer.valueOf(2) : Integer.valueOf(0));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityBlacksmith.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */