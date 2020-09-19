/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.DataSerializers;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.util.SoundCategory;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAICraftItems;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIMining;
/*     */ import net.tangotek.tektopia.entities.crafting.Recipe;
/*     */ import net.tangotek.tektopia.storage.ItemDesire;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityMiner extends EntityVillagerTek {
/*  37 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityMiner.class);
/*     */   
/*  39 */   private static final DataParameter<Boolean> SMELT_CHARCOAL = EntityDataManager.createKey(EntityMiner.class, DataSerializers.BOOLEAN);
/*  40 */   private static final DataParameter<Boolean> MINING = EntityDataManager.createKey(EntityMiner.class, DataSerializers.BOOLEAN);
/*     */   
/*  42 */   private static List<Recipe> craftSet = buildCraftSet();
/*     */   
/*     */   private boolean isUnderground = false;
/*     */   
/*     */   static {
/*  47 */     animHandler.addAnim("tektopia", "villager_chop", "miner_m", true);
/*  48 */     animHandler.addAnim("tektopia", "villager_craft", "miner_m", true);
/*  49 */     EntityVillagerTek.setupAnimations(animHandler, "miner_m");
/*     */   }
/*     */   
/*  52 */   private static final Map<String, DataParameter<Boolean>> RECIPE_PARAMS = new HashMap<>();
/*     */   static {
/*  54 */     craftSet.forEach(r -> (DataParameter)RECIPE_PARAMS.put(r.getAiFilter(), EntityDataManager.createKey(EntityMiner.class, DataSerializers.BOOLEAN)));
/*     */   }
/*     */   
/*     */   public EntityMiner(World worldIn) {
/*  58 */     super(worldIn, ProfessionType.MINER, VillagerRole.VILLAGER.value);
/*  59 */     addAnimationTrigger("tektopia:villager_chop", 47, new Runnable()
/*     */         {
/*     */           public void run() {
/*  62 */             EntityMiner.this.world.playSound(EntityMiner.this.posX, EntityMiner.this.posY, EntityMiner.this.posZ, SoundEvents.BLOCK_STONE_HIT, SoundCategory.BLOCKS, 1.0F, EntityMiner.this.rand.nextFloat() * 0.4F + 0.8F, false);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/*  68 */     return animHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setupServerJobs() {
/*  73 */     super.setupServerJobs();
/*  74 */     addJob(new TickJob(20, 20, true, () -> updateUnderground()));
/*     */   }
/*     */   
/*     */   protected void entityInit() {
/*  78 */     super.entityInit();
/*  79 */     craftSet.forEach(r -> registerAIFilter(r.getAiFilter(), RECIPE_PARAMS.get(r.getAiFilter())));
/*  80 */     registerAIFilter("smelt_charcoal", SMELT_CHARCOAL);
/*  81 */     registerAIFilter("mining", MINING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initEntityAI() {
/*  86 */     super.initEntityAI();
/*     */     
/*  88 */     VillageStructureType[] smeltBuildings = { VillageStructureType.BLACKSMITH, VillageStructureType.STORAGE };
/*     */     
/*  90 */     Predicate<ItemStack> isLapis = p -> (p.getItem() == Items.DYE && p.getMetadata() == EnumDyeColor.BLUE.getDyeDamage());
/*     */     
/*  92 */     getDesireSet().addItemDesire(new ItemDesire(Blocks.TORCH, 10, 20, 30, null));
/*  93 */     getDesireSet().addItemDesire(new ItemDesire(Blocks.LOG, 0, 3, 10, p -> (!hasTorches(p, 10) && p.isAIFilterEnabled("smelt_charcoal"))));
/*  94 */     getDesireSet().addItemDesire((ItemDesire)new UpgradeItemDesire("Pick", getBestPick(this), 1, 1, 1, p -> p.isWorkTime()));
/*  95 */     getDesireSet().addItemDesire(new ItemDesire(Items.DIAMOND, 0, 0, 5, null));
/*  96 */     getDesireSet().addItemDesire(new ItemDesire(Blocks.IRON_ORE, 0, 0, 8, null));
/*  97 */     getDesireSet().addItemDesire(new ItemDesire(Blocks.GOLD_ORE, 0, 0, 8, null));
/*  98 */     getDesireSet().addItemDesire(new ItemDesire(Items.REDSTONE, 0, 0, 16, null));
/*  99 */     getDesireSet().addItemDesire(new ItemDesire("Lapis", isLapis, 0, 0, 16, null));
/* 100 */     craftSet.forEach(r -> getDesireSet().addRecipeDesire(r));
/*     */     
/* 102 */     this; addTask(50, (EntityAIBase)new EntityAICraftItems(this, craftSet, "villager_craft", null, 80, VillageStructureType.STORAGE, Blocks.CRAFTING_TABLE, p -> p.isWorkTime()));
/* 103 */     addTask(50, (EntityAIBase)new EntityAIEmptyFurnace(this, smeltBuildings, isSmeltProduct(), p -> !hasCoal(p, 8)));
/* 104 */     addTask(50, (EntityAIBase)new EntityAISmelting(this, smeltBuildings, p -> (!hasTorches(p, 10) && !hasCoal(p, 8) && p.isAIFilterEnabled("smelt_charcoal")), bestSmeltable(), () -> tryAddSkill(ProfessionType.MINER, 7)));
/* 105 */     addTask(50, (EntityAIBase)new EntityAIMining(this, p -> p.isWorkTime()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeliveryTime() {
/* 111 */     return (Village.isTimeOfDay(this.world, (WORK_START_TIME - 2000), WORK_END_TIME, this.sleepOffset) && !this.world.isRaining());
/*     */   }
/*     */   
/*     */   private static List<Recipe> buildCraftSet() {
/* 115 */     List<Recipe> recipes = new ArrayList<>();
/*     */ 
/*     */     
/* 118 */     List<ItemStack> ingredients = new ArrayList<>();
/* 119 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 120 */     Recipe recipe = new Recipe(ProfessionType.MINER, "craft_wooden_pickaxe", 3, new ItemStack(Items.WOODEN_PICKAXE, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.MINER, 11, 2)), 1, v -> !hasPick(v))
/*     */       {
/*     */         public ItemStack craft(EntityVillagerTek villager) {
/* 123 */           ItemStack result = super.craft(villager);
/* 124 */           villager.modifyHappy(-5);
/* 125 */           return result;
/*     */         }
/*     */       };
/* 128 */     recipes.add(recipe);
/*     */ 
/*     */ 
/*     */     
/* 132 */     ingredients = new ArrayList<>();
/* 133 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 134 */     ingredients.add(new ItemStack(Items.COAL, 8));
/* 135 */     recipe = new Recipe(ProfessionType.MINER, "craft_charcoal_torch", 7, new ItemStack(Item.getItemFromBlock(Blocks.TORCH), 32), ingredients, 1, 1, v -> Integer.valueOf(8 - v.getSkillLerp(ProfessionType.MINER, 1, 6)), 64, v -> !hasTorches(v, 10));
/* 136 */     recipes.add(recipe);
/*     */ 
/*     */ 
/*     */     
/* 140 */     ingredients = new ArrayList<>();
/* 141 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 142 */     ingredients.add(new ItemStack(Items.COAL, 8, 1));
/* 143 */     recipe = new Recipe(ProfessionType.MINER, "craft_coal_torch", 7, new ItemStack(Item.getItemFromBlock(Blocks.TORCH), 32), ingredients, 1, 1, v -> Integer.valueOf(8 - v.getSkillLerp(ProfessionType.MINER, 1, 6)), 64, v -> !hasTorches(v, 10));
/* 144 */     recipes.add(recipe);
/*     */     
/* 146 */     return recipes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
/* 153 */     getInventory().addItem(ModItems.createTaggedItem(Items.WOODEN_PICKAXE, ItemTagType.VILLAGER));
/* 154 */     getInventory().addItem(ModItems.createTaggedItem(Item.getItemFromBlock(Blocks.TORCH), 16, ItemTagType.VILLAGER));
/* 155 */     return super.onInitialSpawn(difficulty, livingdata);
/*     */   }
/*     */   
/*     */   protected void updateUnderground() {
/* 159 */     BlockPos pos = getPosition();
/* 160 */     this.isUnderground = (pos.getY() < 62 && !this.world.canSeeSky(pos));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void collideWithEntity(Entity entityIn) {
/* 166 */     if (entityIn instanceof EntityVillagerTek && 
/* 167 */       hasVillage()) {
/* 168 */       VillageStructure struct = getVillage().getStructure(getPosition());
/* 169 */       if (struct != null && struct.type == VillageStructureType.MINESHAFT) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 174 */     super.collideWithEntity(entityIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addVillagerPosition() {}
/*     */ 
/*     */   
/*     */   protected boolean canVillagerPickupItem(ItemStack itemIn) {
/* 182 */     return isHarvestItem().test(itemIn);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean hasTorches(EntityVillagerTek villager, int req) {
/* 187 */     int count = villager.getInventory().getItemCount(isTorch());
/* 188 */     return (count >= req);
/*     */   }
/*     */   
/*     */   protected static boolean hasCoal(EntityVillagerTek villager, int req) {
/* 192 */     int count = villager.getInventory().getItemCount(isCoal());
/* 193 */     return (count >= req);
/*     */   }
/*     */   
/*     */   protected static boolean hasPick(EntityVillagerTek villager) {
/* 197 */     List<ItemStack> weaponList = villager.getInventory().getItems(getBestPick(villager), 1);
/* 198 */     return !weaponList.isEmpty();
/*     */   }
/*     */   
/* 201 */   public static Predicate<ItemStack> isCoal() { return p -> (p.getItem() == Items.COAL); } public static Predicate<ItemStack> isTorch() {
/* 202 */     return p -> (p.getItem() == Item.getItemFromBlock(Blocks.TORCH));
/*     */   }
/*     */   public static Function<ItemStack, Integer> getBestPick(EntityVillagerTek villager) {
/* 205 */     return p -> (p.getItem() instanceof net.minecraft.item.ItemPickaxe) ? Integer.valueOf(50 - EntityAIMining.getSwingCount(villager, p)) : Integer.valueOf(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAIMoveSpeed() {
/* 216 */     float speed = super.getAIMoveSpeed();
/*     */     
/* 218 */     if (this.isUnderground) {
/* 219 */       speed *= getSkillLerp(ProfessionType.MINER, 100, 140) / 100.0F;
/*     */     }
/* 221 */     return speed;
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> isHarvestItem() {
/* 225 */     return p -> (p.getItem() == Item.getItemFromBlock(Blocks.IRON_ORE) || p.getItem() == Items.COAL || p.getItem() == Items.DYE || p.getItem() == Items.DIAMOND || p.getItem() == Items.REDSTONE || p.getItem() == Item.getItemFromBlock(Blocks.GOLD_ORE) || p.getItem() == Items.EMERALD || super.isHarvestItem().test(p));
/*     */   }
/*     */   
/*     */   private static Predicate<ItemStack> isSmeltProduct() {
/* 229 */     return p -> (p.getItem() == Items.COAL);
/*     */   }
/*     */   
/*     */   private static Function<ItemStack, Integer> bestSmeltable() {
/* 233 */     return p -> Integer.valueOf((p.getItem() == Item.getItemFromBlock(Blocks.LOG)) ? 1 : 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityMiner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */