/*     */ package net.tangotek.tektopia.entities;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.EntityEquipmentSlot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.DataSerializers;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.SoundCategory;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.text.TextComponentTranslation;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIPickUpItem;
/*     */ import net.tangotek.tektopia.entities.crafting.Recipe;
/*     */ import net.tangotek.tektopia.storage.ItemDesire;
/*     */ import net.tangotek.tektopia.storage.UpgradeEquipment;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureBarracks;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityGuard extends EntityVillagerTek {
/*  44 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityGuard.class);
/*     */   
/*  46 */   private static final DataParameter<Boolean> SUPER_ATTACK = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*  47 */   private static final DataParameter<Boolean> SALUTE = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*  48 */   private static final DataParameter<Boolean> EAT_GOLDEN_APPLE = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*  49 */   private static final DataParameter<Boolean> PICKUP_EMERALDS = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*  50 */   private static final DataParameter<Boolean> PATROL_GUARD_POST = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*  51 */   private static final DataParameter<Boolean> PRACTICE_MELEE = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*     */   
/*  53 */   private static final DataParameter<Boolean> EQUIP_LEATHER_ARMOR = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*  54 */   private static final DataParameter<Boolean> EQUIP_IRON_ARMOR = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*  55 */   private static final DataParameter<Boolean> EQUIP_DIAMOND_ARMOR = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*  56 */   private static final DataParameter<Boolean> EQUIP_ENCHANTED_ARMOR = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*     */   
/*  58 */   private static final DataParameter<Boolean> EQUIP_IRON_SWORD = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*  59 */   private static final DataParameter<Boolean> EQUIP_DIAMOND_SWORD = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*  60 */   private static final DataParameter<Boolean> EQUIP_ENCHANTED_SWORD = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*     */   
/*  62 */   private static final DataParameter<Boolean> CAPTAIN = EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN);
/*     */   
/*  64 */   private static List<Recipe> craftSet = buildCraftSet();
/*     */   
/*  66 */   private static final int[] blockStateIds = new int[] {
/*  67 */       Block.getStateId(Blocks.DIRT.getDefaultState()), 
/*  68 */       Block.getStateId(Blocks.DIRT.getDefaultState()), 
/*  69 */       Block.getStateId(Blocks.STONE.getDefaultState()), 
/*  70 */       Block.getStateId(Blocks.COBBLESTONE.getDefaultState())
/*     */     };
/*     */   
/*  73 */   private static final Map<String, DataParameter<Boolean>> RECIPE_PARAMS = new HashMap<>();
/*     */   static {
/*  75 */     craftSet.forEach(r -> (DataParameter)RECIPE_PARAMS.put(r.getAiFilter(), EntityDataManager.createKey(EntityGuard.class, DataSerializers.BOOLEAN)));
/*     */ 
/*     */ 
/*     */     
/*  79 */     animHandler.addAnim("tektopia", "villager_chop", "guard_m", true);
/*  80 */     animHandler.addAnim("tektopia", "villager_thor_jump", "guard_m", false);
/*  81 */     animHandler.addAnim("tektopia", "villager_salute", "guard_m", true);
/*  82 */     animHandler.addAnim("tektopia", "villager_craft", "guard_m", false);
/*  83 */     EntityVillagerTek.setupAnimations(animHandler, "guard_m");
/*     */   }
/*     */   
/*  86 */   protected int wantsPractice = 0;
/*  87 */   protected int courageChance = 1;
/*     */ 
/*     */   
/*     */   public EntityGuard(World worldIn) {
/*  91 */     super(worldIn, ProfessionType.GUARD, VillagerRole.VILLAGER.value | VillagerRole.DEFENDER.value);
/*  92 */     addAnimationTrigger("tektopia:villager_chop", 48, new Runnable()
/*     */         {
/*     */           public void run() {
/*  95 */             EntityGuard.this.world.playSound(EntityGuard.this.posX, EntityGuard.this.posY, EntityGuard.this.posZ, SoundEvents.BLOCK_METAL_HIT, SoundCategory.NEUTRAL, 2.0F, EntityGuard.this.rand.nextFloat() * 0.2F + 0.9F, false);
/*     */           }
/*     */         });
/*     */     
/*  99 */     addAnimationTrigger("tektopia:villager_thor_jump", 120, new Runnable()
/*     */         {
/*     */           public void run() {
/* 102 */             Random rnd = EntityGuard.this.getRNG();
/*     */             
/* 104 */             for (int i = 0; i < 50; i++) {
/* 105 */               double dx = rnd.nextGaussian();
/* 106 */               double dz = rnd.nextGaussian();
/* 107 */               double dy = rnd.nextFloat() * 0.5D;
/* 108 */               double speedX = MathHelper.nextDouble(rnd, -0.2D, 0.2D);
/* 109 */               double speedY = rnd.nextDouble();
/* 110 */               double speedZ = MathHelper.nextDouble(rnd, -0.2D, 0.2D);
/* 111 */               EntityGuard.this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, EntityGuard.this.posX + dx, EntityGuard.this.posY + dy, EntityGuard.this.posZ + dz, speedX, speedY, speedZ, new int[] { EntityGuard.access$100()[rnd.nextInt((EntityGuard.access$100()).length)] });
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/* 118 */     return animHandler;
/*     */   }
/*     */   
/*     */   protected void initEntityAI() {
/* 122 */     super.initEntityAI();
/* 123 */     this; craftSet = buildCraftSet();
/*     */     
/* 125 */     Runnable onHit = () -> {
/*     */         tryAddSkill(ProfessionType.GUARD, getMeleeSkillChance());
/*     */         
/*     */         if (isCaptain() && isHostile().test(getAttackTarget())) {
/*     */           if (this.courageChance > 0 && getRNG().nextInt(this.courageChance) == 0) {
/*     */             EntityCaptainAura aura = new EntityCaptainAura(getEntityWorld(), getX(), getY(), getZ());
/*     */             
/*     */             aura.setRadius(3.0F);
/*     */             aura.setWaitTime(10);
/*     */             aura.setDuration(40);
/*     */             aura.setRadiusPerTick(getSkillLerp(ProfessionType.GUARD, 1, 6) / 10.0F);
/*     */             this.world.spawnEntity((Entity)aura);
/*     */             playSound(ModSoundEvents.courageAura);
/*     */             this.courageChance += 6;
/*     */           } else {
/*     */             this.courageChance = Math.max(this.courageChance - 1, 1);
/*     */           } 
/*     */         }
/*     */       };
/* 144 */     getDesireSet().addItemDesire(new ItemDesire(Items.GOLDEN_APPLE, 1, 1, 1, null));
/* 145 */     getDesireSet().addItemDesire((ItemDesire)new UpgradeEquipment("Weapon", getBestWeapon(this), EntityEquipmentSlot.MAINHAND, null));
/* 146 */     getDesireSet().addItemDesire((ItemDesire)new UpgradeEquipment("ArmorFeet", getBestArmor(this, EntityEquipmentSlot.FEET), EntityEquipmentSlot.FEET, null));
/* 147 */     getDesireSet().addItemDesire((ItemDesire)new UpgradeEquipment("ArmorHead", getBestArmor(this, EntityEquipmentSlot.HEAD), EntityEquipmentSlot.HEAD, null));
/* 148 */     getDesireSet().addItemDesire((ItemDesire)new UpgradeEquipment("ArmorChest", getBestArmor(this, EntityEquipmentSlot.CHEST), EntityEquipmentSlot.CHEST, null));
/* 149 */     getDesireSet().addItemDesire((ItemDesire)new UpgradeEquipment("ArmorLegs", getBestArmor(this, EntityEquipmentSlot.LEGS), EntityEquipmentSlot.LEGS, null));
/* 150 */     craftSet.forEach(r -> getDesireSet().addRecipeDesire(r));
/*     */     
/* 152 */     addTask(49, (EntityAIBase)new EntityAIEatGoldenApple(this));
/* 153 */     addTask(49, (EntityAIBase)new EntityAIThorSlam(this, p -> getWeapon(this), 3.0D, p -> !p.isSleepingTime()));
/* 154 */     addTask(49, (EntityAIBase)new EntityAIMeleeTarget(this, p -> getWeapon(this), EntityVillagerTek.VillagerThought.SWORD, p -> !p.isSleepingTime(), onHit, ProfessionType.GUARD));
/* 155 */     addTask(50, (EntityAIBase)new EntityAISalute(this));
/* 156 */     this; addTask(50, (EntityAIBase)new EntityAICraftItems(this, craftSet, "villager_craft", null, 80, VillageStructureType.STORAGE, Blocks.CRAFTING_TABLE, p -> (p.isWorkTime() && !hasWeapon(this))));
/*     */     
/* 158 */     List<EntityAIPickUpItem.PickUpData> pickUpCounts = new ArrayList<>();
/* 159 */     pickUpCounts.add(new EntityAIPickUpItem.PickUpData(new ItemStack(Items.EMERALD, 1, 0), 60, "pickup_emeralds"));
/* 160 */     addTask(50, (EntityAIBase)new EntityAIPickUpItem(this, pickUpCounts, 1));
/*     */ 
/*     */     
/* 163 */     addTask(50, (EntityAIBase)new EntityAIPatrolGuardPost(this, p -> (hasVillage() && p.isWorkTime() && hasWeapon(this)), 8, 60));
/* 164 */     addTask(50, (EntityAIBase)new EntityAIPatrolVillage(this, p -> hasWeapon(this)));
/* 165 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[0]));
/* 166 */     this.targetTasks.addTask(2, (EntityAIBase)new EntityAIProtectVillage(this, p -> !p.isSleeping()));
/* 167 */     this.targetTasks.addTask(3, (EntityAIBase)new EntityAITrainingTarget(this, p -> (p.isWorkTime() && wantsPractice() > 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/* 172 */     super.entityInit();
/* 173 */     craftSet.forEach(r -> registerAIFilter(r.getAiFilter(), RECIPE_PARAMS.get(r.getAiFilter())));
/*     */     
/* 175 */     registerAIFilter("guard_super_attack", SUPER_ATTACK);
/* 176 */     registerAIFilter("salute", SALUTE);
/* 177 */     registerAIFilter("eat_golden_apple", EAT_GOLDEN_APPLE);
/* 178 */     registerAIFilter("pickup_emeralds", PICKUP_EMERALDS);
/* 179 */     registerAIFilter("patrol_guard_post", PATROL_GUARD_POST);
/* 180 */     registerAIFilter("practice_melee", PRACTICE_MELEE);
/* 181 */     registerAIFilter("equip_leather_armor", EQUIP_LEATHER_ARMOR);
/* 182 */     registerAIFilter("equip_iron_armor", EQUIP_IRON_ARMOR);
/* 183 */     registerAIFilter("equip_diamond_armor", EQUIP_DIAMOND_ARMOR);
/* 184 */     registerAIFilter("equip_enchanted_armor", EQUIP_ENCHANTED_ARMOR);
/* 185 */     registerAIFilter("equip_iron_sword", EQUIP_IRON_SWORD);
/* 186 */     registerAIFilter("equip_diamond_sword", EQUIP_DIAMOND_SWORD);
/* 187 */     registerAIFilter("equip_enchanted_sword", EQUIP_ENCHANTED_SWORD);
/*     */     
/* 189 */     this.dataManager.register(CAPTAIN, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public EntityVillagerTek.MovementMode getDefaultMovement() {
/* 193 */     if (this.village.enemySeenRecently()) {
/* 194 */       return EntityVillagerTek.MovementMode.RUN;
/*     */     }
/* 196 */     return super.getDefaultMovement();
/*     */   }
/*     */   
/*     */   private static List<Recipe> buildCraftSet() {
/* 200 */     List<Recipe> recipes = new ArrayList<>();
/*     */ 
/*     */     
/* 203 */     List<ItemStack> ingredients = new ArrayList<>();
/* 204 */     ingredients.add(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, 99));
/* 205 */     Recipe recipe = new Recipe(ProfessionType.GUARD, "craft_wooden_sword", 3, new ItemStack(Items.WOODEN_SWORD, 1), ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.GUARD, 12, 3)), 1, v -> !hasWeapon(v))
/*     */       {
/*     */         public ItemStack craft(EntityVillagerTek villager) {
/* 208 */           ItemStack result = super.craft(villager);
/* 209 */           villager.modifyHappy(-5);
/* 210 */           return result;
/*     */         }
/*     */       };
/* 213 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 216 */     return recipes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
/* 223 */     getInventory().addItem(ModItems.createTaggedItem(Items.WOODEN_SWORD, ItemTagType.VILLAGER));
/* 224 */     equipBestGear();
/* 225 */     return super.onInitialSpawn(difficulty, livingdata);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setupServerJobs() {
/* 230 */     super.setupServerJobs();
/*     */ 
/*     */     
/* 233 */     addJob(new TickJob(320, 160, true, () -> setHealth(getHealth() + 1.0F)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void skillUpdated(ProfessionType pt) {
/* 238 */     if (pt == ProfessionType.GUARD) {
/* 239 */       getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(getSkillLerp(ProfessionType.GUARD, 1, 100) / 100.0D);
/* 240 */       getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getSkillLerp(ProfessionType.GUARD, 25, 35));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected int getMeleeSkillChance() {
/* 245 */     if (getAttackTarget() instanceof net.minecraft.entity.item.EntityArmorStand) {
/* 246 */       return 4;
/*     */     }
/* 248 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ITextComponent getDisplayName() {
/* 254 */     if (isCaptain()) {
/* 255 */       TextComponentTranslation textComponentTranslation = new TextComponentTranslation("title.captain", new Object[0]);
/* 256 */       textComponentTranslation.appendText(" " + super.getDisplayName().getUnformattedText());
/* 257 */       return (ITextComponent)textComponentTranslation;
/*     */     } 
/*     */     
/* 260 */     return super.getDisplayName();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addTask(int priority, EntityAIBase task) {
/* 265 */     super.addTask(priority, task);
/*     */   }
/*     */   
/*     */   protected boolean canVillagerPickupItem(ItemStack itemIn) {
/* 269 */     return (itemIn.getItem() == Items.EMERALD);
/*     */   }
/*     */ 
/*     */   
/*     */   public void equipBestGear() {
/* 274 */     equipBestGear(EntityEquipmentSlot.CHEST, getBestArmor(this, EntityEquipmentSlot.CHEST));
/* 275 */     equipBestGear(EntityEquipmentSlot.LEGS, getBestArmor(this, EntityEquipmentSlot.LEGS));
/* 276 */     equipBestGear(EntityEquipmentSlot.FEET, getBestArmor(this, EntityEquipmentSlot.FEET));
/* 277 */     equipBestGear(EntityEquipmentSlot.HEAD, getBestArmor(this, EntityEquipmentSlot.HEAD));
/* 278 */     equipBestGear(EntityEquipmentSlot.MAINHAND, getBestWeapon(this));
/*     */   }
/*     */   
/*     */   public static Function<ItemStack, Integer> getBestWeapon(EntityGuard guard) {
/* 282 */     return p -> {
/*     */         if (p.getItem() instanceof ItemSword) {
/*     */           ItemSword sword = (ItemSword)p.getItem();
/*     */           if (p.isItemEnchanted() && !guard.isAIFilterEnabled("equip_enchanted_sword")) {
/*     */             return Integer.valueOf(-1);
/*     */           }
/*     */           if (sword.getToolMaterialName().equals(Item.ToolMaterial.DIAMOND.name()) && !guard.isAIFilterEnabled("equip_diamond_sword")) {
/*     */             return Integer.valueOf(-1);
/*     */           }
/*     */           if (sword.getToolMaterialName().equals(Item.ToolMaterial.IRON.name()) && !guard.isAIFilterEnabled("equip_iron_sword")) {
/*     */             return Integer.valueOf(-1);
/*     */           }
/*     */           int score = (int)sword.getAttackDamage();
/*     */           score = (int)(score + EnchantmentHelper.getModifierForCreature(p, EnumCreatureAttribute.UNDEFINED));
/*     */           score = ++score * 10;
/*     */           if (ModItems.isTaggedItem(p, ItemTagType.VILLAGER)) {
/*     */             score++;
/*     */           }
/*     */           return Integer.valueOf(score);
/*     */         } 
/*     */         return Integer.valueOf(-1);
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static Function<ItemStack, Integer> getBestArmor(EntityGuard guard, EntityEquipmentSlot slot) {
/* 308 */     return p -> {
/*     */         if (p.getItem() instanceof ItemArmor) {
/*     */           ItemArmor armor = (ItemArmor)p.getItem();
/*     */           if (armor.armorType == slot) {
/*     */             if (p.isItemEnchanted() && !guard.isAIFilterEnabled("equip_enchanted_armor")) {
/*     */               return Integer.valueOf(-1);
/*     */             }
/*     */             if (armor.getArmorMaterial() == ItemArmor.ArmorMaterial.DIAMOND && !guard.isAIFilterEnabled("equip_diamond_armor")) {
/*     */               return Integer.valueOf(-1);
/*     */             }
/*     */             if (armor.getArmorMaterial() == ItemArmor.ArmorMaterial.IRON && !guard.isAIFilterEnabled("equip_iron_armor")) {
/*     */               return Integer.valueOf(-1);
/*     */             }
/*     */             if (armor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && !guard.isAIFilterEnabled("equip_leather_armor")) {
/*     */               return Integer.valueOf(-1);
/*     */             }
/*     */             int score = armor.getArmorMaterial().getDamageReductionAmount(armor.armorType);
/*     */             score += EnchantmentHelper.getEnchantmentModifierDamage(Arrays.asList(new ItemStack[] { p }, ), DamageSource.GENERIC);
/*     */             return Integer.valueOf(score);
/*     */           } 
/*     */         } 
/*     */         return Integer.valueOf(-1);
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCaptain() {
/* 335 */     return ((Boolean)this.dataManager.get(CAPTAIN)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCaptain(boolean capt) {
/* 340 */     this.dataManager.set(CAPTAIN, Boolean.valueOf(capt));
/*     */     
/* 342 */     if (capt && hasVillage()) {
/*     */       
/* 344 */       List<EntityGuard> otherGuards = this.world.getEntitiesWithinAABB(EntityGuard.class, getVillage().getAABB().grow(50.0D));
/* 345 */       otherGuards.stream().filter(g -> (g.isCaptain() && g != this && g.getVillage() == getVillage())).forEach(g -> g.setCaptain(false));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean hasWeapon(EntityVillagerTek villager) {
/* 350 */     return !getWeapon(villager).isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasPracticeTarget() {
/* 358 */     return getAttackTarget() instanceof net.minecraft.entity.item.EntityArmorStand;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static ItemStack getWeapon(EntityVillagerTek villager) {
/* 365 */     return villager.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/* 370 */     super.applyEntityAttributes();
/* 371 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
/*     */     
/* 373 */     getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.32D);
/* 374 */     getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canConvertProfession(ProfessionType pt) {
/* 379 */     if (pt == ProfessionType.CAPTAIN && !isCaptain()) {
/* 380 */       return true;
/*     */     }
/* 382 */     return super.canConvertProfession(pt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRevengeTarget(@Nullable EntityLivingBase livingBase) {
/* 388 */     super.setRevengeTarget(livingBase);
/* 389 */     if (livingBase != null && getAttackTarget() != null && getAttackTarget() != livingBase && !(livingBase instanceof net.minecraft.entity.player.EntityPlayer)) {
/* 390 */       double distTarget = getDistanceSq((Entity)getAttackTarget());
/* 391 */       double distRevenge = getDistanceSq((Entity)livingBase);
/* 392 */       if (distTarget > distRevenge * 2.0D) {
/* 393 */         setAttackTarget(livingBase);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttackTarget(@Nullable EntityLivingBase target) {
/* 401 */     super.setAttackTarget(target);
/* 402 */     if (hasVillage() && !(target instanceof net.minecraft.entity.item.EntityArmorStand)) {
/* 403 */       getVillage().addActiveDefender(this);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void randomizeGoals() {
/* 408 */     super.randomizeGoals();
/*     */     
/* 410 */     this.wantsPractice = 0;
/* 411 */     if (getRNG().nextInt(2) == 0) {
/* 412 */       this.wantsPractice = getRNG().nextInt(10) + 15;
/*     */     }
/*     */   }
/*     */   
/*     */   public void attachToVillage(Village v) {
/* 417 */     super.attachToVillage(v);
/* 418 */     this.sleepOffset = v.getNextGuardSleepOffset();
/*     */   }
/*     */   
/*     */   public int wantsPractice() {
/* 422 */     if (getSkill(ProfessionType.GUARD) < getIntelligence()) {
/* 423 */       return this.wantsPractice;
/*     */     }
/* 425 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void bedCheck() {
/* 430 */     if (hasVillage() && this.homeFrame != null) {
/* 431 */       VillageStructure struct = this.village.getStructureFromFrame(this.homeFrame);
/*     */ 
/*     */       
/* 434 */       if (!(struct instanceof VillageStructureBarracks)) {
/*     */         
/* 436 */         List<VillageStructure> barracks = this.village.getStructures(VillageStructureType.BARRACKS);
/* 437 */         VillageStructureBarracks availBarracks = barracks.stream().map(VillageStructureBarracks.class::cast).filter(b -> !b.isFull()).findAny().orElse(null);
/* 438 */         if (availBarracks != null)
/*     */         {
/* 440 */           clearHome();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 445 */     super.bedCheck();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onStopSleep() {
/* 450 */     super.onStopSleep();
/* 451 */     equipBestGear();
/*     */   }
/*     */   
/*     */   public void onLivingUpdate() {
/* 455 */     super.onLivingUpdate();
/*     */   }
/*     */   
/*     */   protected boolean canVillagerPickupItem(Item itemIn) {
/* 459 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Predicate<Entity> isSuitableTarget() {
/* 464 */     return e -> (isHostile().test(e) || e instanceof net.minecraft.entity.item.EntityArmorStand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn) {
/* 469 */     return attackEntityAsMob(entityIn, 1.0D);
/*     */   }
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn, double knockbackModifier) {
/* 473 */     this.wantsPractice--;
/*     */     
/* 475 */     float damage = (float)getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
/*     */ 
/*     */     
/* 478 */     damage *= getSkillLerp(ProfessionType.GUARD, 100, 200) / 100.0F;
/* 479 */     double knockback = 0.0D;
/*     */     
/* 481 */     if (entityIn instanceof EntityLivingBase) {
/* 482 */       damage += EnchantmentHelper.getModifierForCreature(getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
/* 483 */       knockback += EnchantmentHelper.getKnockbackModifier((EntityLivingBase)this);
/* 484 */       knockback += getSkillLerp(ProfessionType.GUARD, 1, 5) / 10.0D;
/* 485 */       knockback *= knockbackModifier;
/*     */     } 
/*     */     
/* 488 */     debugOut("Attacking " + entityIn.getDisplayName().getUnformattedText() + " | " + damage + " damage, " + knockback + " knock");
/* 489 */     boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), damage);
/*     */     
/* 491 */     if (flag) {
/* 492 */       if (knockback > 0.0D && entityIn instanceof EntityLivingBase) {
/* 493 */         ((EntityLivingBase)entityIn).knockBack((Entity)this, (float)knockback, MathHelper.sin(this.rotationYaw * 0.017453292F), -MathHelper.cos(this.rotationYaw * 0.017453292F));
/* 494 */         this.motionX *= 0.6D;
/* 495 */         this.motionZ *= 0.6D;
/*     */       } 
/*     */       
/* 498 */       int j = EnchantmentHelper.getFireAspectModifier((EntityLivingBase)this);
/*     */       
/* 500 */       if (j > 0) {
/* 501 */         entityIn.setFire(j * 4);
/*     */       }
/*     */       
/* 504 */       applyEnchantments((EntityLivingBase)this, entityIn);
/*     */     } 
/*     */     
/* 507 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFleeFrom(Entity e) {
/* 512 */     ItemStack equipped = getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
/* 513 */     if (equipped == ItemStack.EMPTY) {
/* 514 */       return super.isFleeFrom(e);
/*     */     }
/*     */     
/* 517 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound compound) {
/* 522 */     super.writeEntityToNBT(compound);
/* 523 */     compound.setBoolean("captain", isCaptain());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound compound) {
/* 528 */     super.readEntityFromNBT(compound);
/* 529 */     setCaptain(compound.getBoolean("captain"));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityGuard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */