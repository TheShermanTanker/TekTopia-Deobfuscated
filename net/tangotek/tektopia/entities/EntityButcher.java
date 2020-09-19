/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemTool;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.DataSerializers;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.SoundEvent;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.EntityTagType;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIAttachLeadToButcherAnimal;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAICraftItems;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAILeadAnimalToStructure;
/*     */ import net.tangotek.tektopia.entities.crafting.Recipe;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityButcher extends EntityVillagerTek {
/*  39 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityButcher.class);
/*  40 */   private static final DataParameter<Boolean> RETRIEVE_CHICKENS = EntityDataManager.createKey(EntityButcher.class, DataSerializers.BOOLEAN);
/*  41 */   private static final DataParameter<Boolean> RETRIEVE_COWS = EntityDataManager.createKey(EntityButcher.class, DataSerializers.BOOLEAN);
/*  42 */   private static final DataParameter<Boolean> RETRIEVE_PIGS = EntityDataManager.createKey(EntityButcher.class, DataSerializers.BOOLEAN);
/*  43 */   private static final DataParameter<Boolean> RETRIEVE_SHEEP = EntityDataManager.createKey(EntityButcher.class, DataSerializers.BOOLEAN);
/*  44 */   private static final DataParameter<Boolean> BUTCHER_ANIMALS = EntityDataManager.createKey(EntityButcher.class, DataSerializers.BOOLEAN);
/*  45 */   private static List<Recipe> craftSet = buildCraftSet();
/*     */   
/*     */   static {
/*  48 */     animHandler.addAnim("tektopia", "villager_take", "butcher_m", false);
/*  49 */     animHandler.addAnim("tektopia", "villager_chop", "butcher_m", false);
/*  50 */     animHandler.addAnim("tektopia", "villager_craft", "butcher_m", false);
/*  51 */     EntityVillagerTek.setupAnimations(animHandler, "butcher_m");
/*     */   }
/*     */   
/*  54 */   private static final Map<String, DataParameter<Boolean>> RECIPE_PARAMS = new HashMap<>();
/*     */   static {
/*  56 */     craftSet.forEach(r -> (DataParameter)RECIPE_PARAMS.put(r.getAiFilter(), EntityDataManager.createKey(EntityButcher.class, DataSerializers.BOOLEAN)));
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityButcher(World worldIn) {
/*  61 */     super(worldIn, ProfessionType.BUTCHER, VillagerRole.VILLAGER.value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/*  67 */     return animHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  72 */     super.applyEntityAttributes();
/*  73 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  78 */     super.entityInit();
/*  79 */     craftSet.forEach(r -> registerAIFilter(r.getAiFilter(), RECIPE_PARAMS.get(r.getAiFilter())));
/*  80 */     registerAIFilter("retrieve_chickens", RETRIEVE_CHICKENS);
/*  81 */     registerAIFilter("retrieve_cows", RETRIEVE_COWS);
/*  82 */     registerAIFilter("retrieve_pigs", RETRIEVE_PIGS);
/*  83 */     registerAIFilter("retrieve_sheep", RETRIEVE_SHEEP);
/*  84 */     registerAIFilter("butcher_animals", BUTCHER_ANIMALS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initEntityAI() {
/*  89 */     super.initEntityAI();
/*     */     
/*  91 */     getDesireSet().addItemDesire((ItemDesire)new UpgradeItemDesire("Axe", getBestAxe(), 1, 1, 1, p -> p.isWorkTime()));
/*  92 */     craftSet.forEach(r -> getDesireSet().addRecipeDesire(r));
/*     */     
/*  94 */     Runnable onHit = () -> {
/*     */         tryAddSkill(ProfessionType.BUTCHER, 8);
/*     */         
/*     */         modifyHunger(-5);
/*     */       };
/*  99 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIMeleeTarget(this, p -> getWeapon(this), EntityVillagerTek.VillagerThought.AXE, p -> (p.isWorkTime() && p.isAIFilterEnabled("butcher_animals")), onHit, ProfessionType.BUTCHER));
/* 100 */     this.tasks.addTask(50, (EntityAIBase)new EntityAILeadAnimalToStructure(this, VillageStructureType.BUTCHER, EntityTagType.BUTCHERED));
/* 101 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIAttachLeadToButcherAnimal(this, p -> p.isWorkTime()));
/* 102 */     this; this.tasks.addTask(50, (EntityAIBase)new EntityAICraftItems(this, craftSet, "villager_craft", null, 80, VillageStructureType.BUTCHER, Blocks.CRAFTING_TABLE, p -> p.isWorkTime()));
/*     */     
/* 104 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIKillInStructure(this, VillageStructureType.BUTCHER, EntityAnimal.class, isTarget(), p -> p.isWorkTime()));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
/* 110 */     getInventory().addItem(ModItems.makeTaggedItem(new ItemStack(Items.WOODEN_AXE), ItemTagType.VILLAGER));
/*     */     
/* 112 */     return super.onInitialSpawn(difficulty, livingdata);
/*     */   }
/*     */   
/*     */   public static Predicate<EntityLivingBase> isTarget() {
/* 116 */     return p -> (p instanceof net.minecraft.entity.passive.EntitySheep || p instanceof net.minecraft.entity.passive.EntityCow || p instanceof net.minecraft.entity.passive.EntityChicken || p instanceof net.minecraft.entity.passive.EntityPig);
/*     */   }
/*     */   
/*     */   public static Function<ItemStack, Integer> getBestAxe() {
/* 120 */     return p -> {
/*     */         if (p.getItem() instanceof net.minecraft.item.ItemAxe) {
/*     */           int score = p.getMaxDamage() * 10;
/*     */           if (ModItems.isTaggedItem(p, ItemTagType.VILLAGER)) {
/*     */             score++;
/*     */           }
/*     */           return Integer.valueOf(score);
/*     */         } 
/*     */         return Integer.valueOf(-1);
/*     */       };
/*     */   }
/*     */   
/*     */   private static List<Recipe> buildCraftSet() {
/* 133 */     List<Recipe> recipes = new ArrayList<>();
/*     */ 
/*     */     
/* 136 */     List<ItemStack> ingredients = new ArrayList<>();
/* 137 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 138 */     Recipe recipe = new Recipe(ProfessionType.BUTCHER, "craft_wooden_axe", 3, new ItemStack(Items.WOODEN_AXE, 1), ingredients, 1, 1, v -> Integer.valueOf(12 - v.getSkillLerp(ProfessionType.BUTCHER, 1, 10)), 1, p -> getWeapon(p).isEmpty())
/*     */       {
/*     */         public ItemStack craft(EntityVillagerTek villager) {
/* 141 */           ItemStack result = super.craft(villager);
/* 142 */           villager.modifyHappy(-5);
/* 143 */           return result;
/*     */         }
/*     */       };
/* 146 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 149 */     ingredients = new ArrayList<>();
/* 150 */     ingredients.add(new ItemStack(Items.LEATHER, 8));
/* 151 */     recipe = new Recipe(ProfessionType.BUTCHER, "craft_leather_chestplate", 2, new ItemStack((Item)Items.LEATHER_CHESTPLATE, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BUTCHER, 8, 4)), 1);
/* 152 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 155 */     ingredients = new ArrayList<>();
/* 156 */     ingredients.add(new ItemStack(Items.LEATHER, 7));
/* 157 */     recipe = new Recipe(ProfessionType.BUTCHER, "craft_leather_leggings", 2, new ItemStack((Item)Items.LEATHER_LEGGINGS, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BUTCHER, 7, 3)), 1);
/* 158 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 161 */     ingredients = new ArrayList<>();
/* 162 */     ingredients.add(new ItemStack(Items.LEATHER, 4));
/* 163 */     recipe = new Recipe(ProfessionType.BUTCHER, "craft_leather_boots", 3, new ItemStack((Item)Items.LEATHER_BOOTS, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BUTCHER, 4, 2)), 1);
/* 164 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 167 */     ingredients = new ArrayList<>();
/* 168 */     ingredients.add(new ItemStack(Items.LEATHER, 5));
/* 169 */     recipe = new Recipe(ProfessionType.BUTCHER, "craft_leather_helmet", 3, new ItemStack((Item)Items.LEATHER_HELMET, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.BUTCHER, 5, 3)), 1);
/* 170 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 173 */     return recipes;
/*     */   }
/*     */   
/*     */   private static int getToolBonusValue(ItemTool tool) {
/* 177 */     if (tool != null) {
/* 178 */       if (tool.getToolMaterialName().equals("STONE"))
/* 179 */         return 10; 
/* 180 */       if (tool.getToolMaterialName().equals("IRON"))
/* 181 */         return 30; 
/* 182 */       if (tool.getToolMaterialName().equals("DIAMOND")) {
/* 183 */         return 45;
/*     */       }
/*     */     } 
/*     */     
/* 187 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onKillEntity(EntityLivingBase entityLivingIn) {
/* 193 */     int bonusMeat = 0;
/* 194 */     int extraChance = getSkill(ProfessionType.BUTCHER);
/* 195 */     ItemStack tool = getWeapon(this);
/* 196 */     if (!tool.isEmpty()) {
/* 197 */       extraChance += getToolBonusValue((ItemTool)tool.getItem());
/*     */     }
/* 199 */     if (getRNG().nextInt(100) < extraChance) {
/* 200 */       bonusMeat++;
/*     */     }
/* 202 */     if (getRNG().nextInt(100) < extraChance) {
/* 203 */       bonusMeat++;
/*     */     }
/*     */     
/* 206 */     Item meat = null;
/* 207 */     SoundEvent soundEvent = null;
/* 208 */     if (entityLivingIn instanceof net.minecraft.entity.passive.EntityCow) {
/* 209 */       meat = Items.BEEF;
/* 210 */       soundEvent = SoundEvents.ENTITY_COW_DEATH;
/*     */     }
/* 212 */     else if (entityLivingIn instanceof net.minecraft.entity.passive.EntityPig) {
/* 213 */       meat = Items.PORKCHOP;
/* 214 */       soundEvent = SoundEvents.ENTITY_PIG_DEATH;
/*     */     }
/* 216 */     else if (entityLivingIn instanceof net.minecraft.entity.passive.EntitySheep) {
/* 217 */       meat = Items.MUTTON;
/* 218 */       soundEvent = SoundEvents.ENTITY_SHEEP_DEATH;
/*     */     }
/* 220 */     else if (entityLivingIn instanceof net.minecraft.entity.passive.EntityChicken) {
/* 221 */       meat = Items.CHICKEN;
/* 222 */       soundEvent = SoundEvents.ENTITY_CHICKEN_DEATH;
/*     */     } 
/*     */     
/* 225 */     if (soundEvent != null) {
/* 226 */       playSound(soundEvent, 2.0F, getSoundPitch());
/*     */     }
/*     */     
/* 229 */     if (bonusMeat > 0 && this.world.getGameRules().getBoolean("doMobLoot")) {
/* 230 */       ItemStack meatStack = new ItemStack(meat, bonusMeat);
/* 231 */       if (ModEntities.isTaggedEntity((Entity)entityLivingIn, EntityTagType.VILLAGER)) {
/* 232 */         ModItems.makeTaggedItem(meatStack, ItemTagType.VILLAGER);
/*     */       }
/* 234 */       entityLivingIn.entityDropItem(meatStack, bonusMeat);
/*     */     } 
/*     */     
/* 237 */     super.onKillEntity(entityLivingIn);
/*     */   }
/*     */   
/*     */   public static ItemStack getWeapon(EntityVillagerTek villager) {
/* 241 */     List<ItemStack> weaponList = villager.getInventory().getItems(getBestAxe(), 1);
/* 242 */     if (!weaponList.isEmpty()) {
/* 243 */       return weaponList.get(0);
/*     */     }
/* 245 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> isMeat() {
/* 249 */     return p -> (p.getItem() == Items.BEEF || p.getItem() == Items.PORKCHOP || p.getItem() == Items.MUTTON || p.getItem() == Items.CHICKEN);
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> isAnimalProduct() {
/* 253 */     return p -> (p.getItem() == Items.FEATHER || p.getItem() == Item.getItemFromBlock(Blocks.WOOL) || p.getItem() == Items.EGG || p.getItem() == Items.LEATHER);
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> isHarvestItem() {
/* 257 */     return p -> (isMeat().test(p) || isAnimalProduct().test(p));
/*     */   }
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn) {
/* 261 */     float damage = (float)getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
/* 262 */     boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), damage);
/* 263 */     return flag;
/*     */   }
/*     */   
/*     */   protected boolean canVillagerPickupItem(ItemStack itemIn) {
/* 267 */     return (isHarvestItem().test(itemIn) || super.canVillagerPickupItem(itemIn));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityButcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */