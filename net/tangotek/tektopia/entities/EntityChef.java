/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.DataSerializers;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAICraftItems;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAISmelting;
/*     */ import net.tangotek.tektopia.entities.crafting.Recipe;
/*     */ import net.tangotek.tektopia.storage.ItemDesire;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityChef extends EntityVillagerTek {
/*  30 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityChef.class);
/*     */   
/*  32 */   private static final DataParameter<Boolean> COOK_BEEF = EntityDataManager.createKey(EntityChef.class, DataSerializers.BOOLEAN);
/*  33 */   private static final DataParameter<Boolean> COOK_MUTTON = EntityDataManager.createKey(EntityChef.class, DataSerializers.BOOLEAN);
/*  34 */   private static final DataParameter<Boolean> COOK_CHICKEN = EntityDataManager.createKey(EntityChef.class, DataSerializers.BOOLEAN);
/*  35 */   private static final DataParameter<Boolean> COOK_PORK = EntityDataManager.createKey(EntityChef.class, DataSerializers.BOOLEAN);
/*  36 */   private static final DataParameter<Boolean> COOK_POTATO = EntityDataManager.createKey(EntityChef.class, DataSerializers.BOOLEAN);
/*  37 */   private static final DataParameter<Boolean> SMELT_CHARCOAL = EntityDataManager.createKey(EntityChef.class, DataSerializers.BOOLEAN);
/*     */   
/*  39 */   private static List<Recipe> craftSet = buildCraftSet();
/*     */   
/*  41 */   private static final Map<String, DataParameter<Boolean>> RECIPE_PARAMS = new HashMap<>();
/*     */   static {
/*  43 */     craftSet.forEach(r -> (DataParameter)RECIPE_PARAMS.put(r.getAiFilter(), EntityDataManager.createKey(EntityChef.class, DataSerializers.BOOLEAN)));
/*     */ 
/*     */ 
/*     */     
/*  47 */     animHandler.addAnim("tektopia", "villager_take", "chef_m", false);
/*  48 */     animHandler.addAnim("tektopia", "villager_cook", "chef_m", false);
/*  49 */     EntityVillagerTek.setupAnimations(animHandler, "chef_m");
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityChef(World worldIn) {
/*  54 */     super(worldIn, ProfessionType.CHEF, VillagerRole.VILLAGER.value);
/*     */   }
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/*  58 */     return animHandler;
/*     */   }
/*     */   
/*     */   protected void entityInit() {
/*  62 */     super.entityInit();
/*     */     
/*  64 */     registerAIFilter("cook_beef", COOK_BEEF);
/*  65 */     registerAIFilter("cook_pork", COOK_PORK);
/*  66 */     registerAIFilter("cook_mutton", COOK_MUTTON);
/*  67 */     registerAIFilter("cook_chicken", COOK_CHICKEN);
/*  68 */     registerAIFilter("cook_potato", COOK_POTATO);
/*  69 */     registerAIFilter("smelt_charcoal", SMELT_CHARCOAL);
/*  70 */     craftSet.forEach(r -> registerAIFilter(r.getAiFilter(), RECIPE_PARAMS.get(r.getAiFilter())));
/*     */   }
/*     */   
/*     */   protected void initEntityAI() {
/*  74 */     super.initEntityAI();
/*     */     
/*  76 */     getDesireSet().addItemDesire(new ItemDesire("Fuel", EntityAISmelting.getBestFuel(), 1, 6, 10, null));
/*  77 */     getDesireSet().addItemDesire(new ItemDesire(Blocks.LOG, 0, 4, 8, p -> p.isAIFilterEnabled("smelt_charcoal")));
/*  78 */     getDesireSet().addItemDesire(new ItemDesire("Cookable", bestCookable(this), 0, getSkillLerp(ProfessionType.CHEF, 1, 3) * 8, 0, null));
/*  79 */     craftSet.forEach(r -> getDesireSet().addRecipeDesire(r));
/*     */ 
/*     */     
/*  82 */     addTask(50, (EntityAIBase)new EntityAIEmptyFurnace(this, VillageStructureType.KITCHEN, takeFromFurnace()));
/*  83 */     addTask(50, (EntityAIBase)new EntityAISmelting(this, VillageStructureType.KITCHEN, p -> (!hasCoal(p, 1) && p.isAIFilterEnabled("smelt_charcoal")), p -> Integer.valueOf((p.getItem() == Item.getItemFromBlock(Blocks.LOG)) ? 1 : 0), () -> tryAddSkill(ProfessionType.CHEF, 10)));
/*  84 */     addTask(50, (EntityAIBase)new EntityAISmelting(this, VillageStructureType.KITCHEN, p -> true, bestCookable(this), () -> tryAddSkill(ProfessionType.CHEF, 3)));
/*     */     
/*  86 */     this; addTask(50, (EntityAIBase)new EntityAICraftItems(this, craftSet, "villager_cook", null, 80, VillageStructureType.KITCHEN, Blocks.CRAFTING_TABLE, p -> p.isWorkTime()));
/*     */   }
/*     */   
/*     */   public void onLivingUpdate() {
/*  90 */     super.onLivingUpdate();
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
/*     */ 
/*     */   
/*     */   private static List<Recipe> buildCraftSet() {
/* 111 */     List<Recipe> recipes = new ArrayList<>();
/*     */ 
/*     */     
/* 114 */     List<ItemStack> ingredients = new ArrayList<>();
/* 115 */     ingredients.add(new ItemStack(Items.GOLD_INGOT, 8));
/* 116 */     ingredients.add(new ItemStack(Items.APPLE, 1));
/* 117 */     Recipe recipe1 = new Recipe(ProfessionType.CHEF, "craft_golden_apple", 2, new ItemStack(Items.GOLDEN_APPLE, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.CHEF, 7, 2)), 16);
/* 118 */     recipes.add(recipe1);
/*     */ 
/*     */     
/* 121 */     ingredients = new ArrayList<>();
/* 122 */     ingredients.add(new ItemStack(Items.GOLD_INGOT, 1));
/* 123 */     ingredients.add(new ItemStack(Items.CARROT, 1));
/* 124 */     recipe1 = new Recipe(ProfessionType.CHEF, "craft_golden_carrot", 2, new ItemStack(Items.GOLDEN_CARROT, 1), ingredients, 3, 3, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.CHEF, 7, 2)), 16);
/* 125 */     recipes.add(recipe1);
/*     */ 
/*     */     
/* 128 */     ingredients = new ArrayList<>();
/* 129 */     ingredients.add(new ItemStack(Items.BEETROOT, 6));
/* 130 */     ingredients.add(new ItemStack(Items.BOWL, 1));
/* 131 */     recipe1 = new Recipe(ProfessionType.CHEF, "craft_beetroot_soup", 2, new ItemStack(Items.BEETROOT_SOUP, 1), ingredients, 2, 3, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.CHEF, 7, 2)), 15);
/* 132 */     recipes.add(recipe1);
/*     */ 
/*     */     
/* 135 */     ingredients = new ArrayList<>();
/* 136 */     ingredients.add(new ItemStack(Items.MILK_BUCKET, 3));
/* 137 */     ingredients.add(new ItemStack(Items.SUGAR, 2));
/* 138 */     ingredients.add(new ItemStack(Items.EGG, 1));
/* 139 */     ingredients.add(new ItemStack(Items.WHEAT, 3));
/* 140 */     recipe1 = new Recipe(ProfessionType.CHEF, "craft_cake", 1, new ItemStack(Items.CAKE, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.CHEF, 10, 5)), 1)
/*     */       {
/*     */         
/*     */         public ItemStack craft(EntityVillagerTek villager)
/*     */         {
/* 145 */           List<ItemStack> items = villager.getInventory().getItems(p -> (p.getItem() == Items.MILK_BUCKET), 3);
/* 146 */           int villagerMilks = (int)items.stream().filter(itemStack -> ModItems.isTaggedItem(itemStack, ItemTagType.VILLAGER)).count();
/*     */           
/* 148 */           ItemStack result = super.craft(villager);
/* 149 */           if (result != null) {
/* 150 */             int nonVillagerMilks = 3 - villagerMilks;
/* 151 */             if (villagerMilks > 0)
/*     */             {
/* 153 */               villager.getInventory().addItem(ModItems.makeTaggedItem(new ItemStack(Items.BUCKET, villagerMilks), ItemTagType.VILLAGER));
/*     */             }
/*     */             
/* 156 */             if (nonVillagerMilks > 0)
/*     */             {
/* 158 */               villager.getInventory().addItem(new ItemStack(Items.BUCKET, nonVillagerMilks));
/*     */             }
/*     */           } 
/*     */           
/* 162 */           return result;
/*     */         }
/*     */       };
/* 165 */     recipes.add(recipe1);
/*     */ 
/*     */     
/* 168 */     ingredients = new ArrayList<>();
/* 169 */     ingredients.add(new ItemStack(Items.WHEAT, 3));
/* 170 */     recipe1 = new Recipe(ProfessionType.CHEF, "craft_bread", 6, new ItemStack(Items.BREAD, 1), ingredients, 3, 5, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.CHEF, 5, 2)), 32);
/* 171 */     recipes.add(recipe1);
/*     */ 
/*     */     
/* 174 */     ingredients = new ArrayList<>();
/* 175 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 176 */     Predicate<EntityVillagerTek> pred = v -> !Recipe.hasPersonalGoal(v, new ItemStack(Items.BOWL, 3));
/* 177 */     Recipe recipe2 = new Recipe(ProfessionType.CHEF, "craft_wooden_bowl", 10, new ItemStack(Items.BOWL, 1), ingredients, 3, 3, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.CHEF, 5, 1)), 3, pred);
/* 178 */     recipes.add(recipe2);
/*     */ 
/*     */     
/* 181 */     ingredients = new ArrayList<>();
/* 182 */     ingredients.add(new ItemStack(Items.REEDS, 1));
/* 183 */     pred = (v -> !Recipe.hasPersonalGoal(v, new ItemStack(Items.SUGAR, 6)));
/* 184 */     recipe2 = new Recipe(ProfessionType.CHEF, "craft_sugar", 15, new ItemStack(Items.SUGAR, 1), ingredients, 3, 4, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.CHEF, 3, 1)), 32, pred);
/* 185 */     recipes.add(recipe2);
/*     */ 
/*     */     
/* 188 */     ingredients = new ArrayList<>();
/* 189 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.PUMPKIN), 1));
/* 190 */     ingredients.add(new ItemStack(Items.SUGAR, 1));
/* 191 */     ingredients.add(new ItemStack(Items.EGG, 1));
/* 192 */     Recipe recipe = new Recipe(ProfessionType.CHEF, "craft_pumpkin_pie", 2, new ItemStack(Items.PUMPKIN_PIE, 1), ingredients, 3, 4, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.CHEF, 9, 4)), 1);
/* 193 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 196 */     ingredients = new ArrayList<>();
/* 197 */     ingredients.add(new ItemStack(Items.WHEAT, 2));
/* 198 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()));
/* 199 */     recipe = new Recipe(ProfessionType.CHEF, "craft_cookie", 4, new ItemStack(Items.COOKIE, 1), ingredients, 3, 3, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.CHEF, 6, 2)), 3);
/* 200 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 203 */     return recipes;
/*     */   }
/*     */   
/*     */   private static Predicate<ItemStack> takeFromFurnace() {
/* 207 */     return p -> (isCooked().test(p) || p.getItem() == Items.COAL);
/*     */   }
/*     */   
/*     */   private static Predicate<ItemStack> isCooked() {
/* 211 */     return p -> (p.getItem() == Items.COOKED_CHICKEN || p.getItem() == Items.COOKED_BEEF || p.getItem() == Items.COOKED_MUTTON || p.getItem() == Items.COOKED_PORKCHOP || p.getItem() == Items.BAKED_POTATO);
/*     */   }
/*     */   
/*     */   protected static boolean hasCoal(EntityVillagerTek villager, int req) {
/* 215 */     int count = villager.getInventory().getItemCount(isCoal());
/* 216 */     return (count >= req);
/*     */   }
/*     */   public static Predicate<ItemStack> isCoal() {
/* 219 */     return p -> (p.getItem() == Items.COAL);
/*     */   }
/*     */   private static Function<ItemStack, Integer> bestCookable(EntityVillagerTek villager) {
/* 222 */     return p -> 
/* 223 */       (p.getItem() == Items.BEEF && villager.isAIFilterEnabled("cook_beef")) ? EntityVillagerTek.foodItemValue(null).apply(Items.COOKED_BEEF) : (
/*     */       
/* 225 */       (p.getItem() == Items.PORKCHOP && villager.isAIFilterEnabled("cook_pork")) ? EntityVillagerTek.foodItemValue(null).apply(Items.COOKED_PORKCHOP) : (
/*     */       
/* 227 */       (p.getItem() == Items.CHICKEN && villager.isAIFilterEnabled("cook_chicken")) ? EntityVillagerTek.foodItemValue(null).apply(Items.COOKED_CHICKEN) : (
/*     */       
/* 229 */       (p.getItem() == Items.MUTTON && villager.isAIFilterEnabled("cook_mutton")) ? EntityVillagerTek.foodItemValue(null).apply(Items.COOKED_MUTTON) : (
/*     */       
/* 231 */       (p.getItem() == Items.POTATO && villager.isAIFilterEnabled("cook_potato")) ? EntityVillagerTek.foodItemValue(null).apply(Items.BAKED_POTATO) : Integer.valueOf(-1)))));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityChef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */