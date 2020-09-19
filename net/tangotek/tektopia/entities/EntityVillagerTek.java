/*      */ package net.tangotek.tektopia.entities;
/*      */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityLiving;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.IEntityLivingData;
/*      */ import net.minecraft.entity.SharedMonsterAttributes;
/*      */ import net.minecraft.entity.ai.EntityAIBase;
/*      */ import net.minecraft.entity.ai.EntityAITasks;
/*      */ import net.minecraft.entity.ai.attributes.IAttribute;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.ai.attributes.RangedAttribute;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.passive.EntityAnimal;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.EntityPlayerMP;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.init.SoundEvents;
/*      */ import net.minecraft.inventory.EntityEquipmentSlot;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.network.datasync.DataParameter;
/*      */ import net.minecraft.network.datasync.DataSerializers;
/*      */ import net.minecraft.network.datasync.EntityDataManager;
/*      */ import net.minecraft.potion.PotionEffect;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.EnumHand;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.SoundCategory;
/*      */ import net.minecraft.util.math.AxisAlignedBB;
/*      */ import net.minecraft.util.math.BlockPos;
/*      */ import net.minecraft.util.math.MathHelper;
/*      */ import net.minecraft.util.text.translation.I18n;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraftforge.fml.relauncher.Side;
/*      */ import net.minecraftforge.fml.relauncher.SideOnly;
/*      */ import net.tangotek.tektopia.ItemTagType;
/*      */ import net.tangotek.tektopia.ModItems;
/*      */ import net.tangotek.tektopia.ModPotions;
/*      */ import net.tangotek.tektopia.ModSoundEvents;
/*      */ import net.tangotek.tektopia.ProfessionType;
/*      */ import net.tangotek.tektopia.TekVillager;
/*      */ import net.tangotek.tektopia.Village;
/*      */ import net.tangotek.tektopia.VillageManager;
/*      */ import net.tangotek.tektopia.VillagerRole;
/*      */ import net.tangotek.tektopia.caps.IVillageData;
/*      */ import net.tangotek.tektopia.client.ParticleThought;
/*      */ import net.tangotek.tektopia.entities.ai.EntityAIEatFood;
/*      */ import net.tangotek.tektopia.items.ItemProfessionToken;
/*      */ import net.tangotek.tektopia.network.PacketVillagerThought;
/*      */ import net.tangotek.tektopia.storage.ItemDesireSet;
/*      */ import net.tangotek.tektopia.storage.VillagerInventory;
/*      */ import net.tangotek.tektopia.structures.VillageStructure;
/*      */ import net.tangotek.tektopia.structures.VillageStructureHome;
/*      */ import net.tangotek.tektopia.structures.VillageStructureType;
/*      */ import net.tangotek.tektopia.tickjob.TickJob;
/*      */ 
/*      */ public abstract class EntityVillagerTek extends EntityVillageNavigator {
/*   69 */   public static final IAttribute MAX_HUNGER = (IAttribute)(new RangedAttribute((IAttribute)null, "generic.hunger", 100.0D, 0.0D, 100.0D)).setDescription("Hunger").setShouldWatch(true);
/*   70 */   public static final IAttribute MAX_HAPPY = (IAttribute)(new RangedAttribute((IAttribute)null, "generic.happy", 100.0D, 0.0D, 100.0D)).setDescription("Happy").setShouldWatch(true);
/*   71 */   public static final IAttribute MAX_INTELLIGENCE = (IAttribute)(new RangedAttribute((IAttribute)null, "generic.intelligence", 100.0D, 0.0D, 100.0D)).setDescription("Intelligence").setShouldWatch(true);
/*   72 */   private static final DataParameter<Integer> HUNGER = EntityDataManager.createKey(EntityVillagerTek.class, DataSerializers.VARINT);
/*   73 */   private static final DataParameter<Integer> HAPPY = EntityDataManager.createKey(EntityVillagerTek.class, DataSerializers.VARINT);
/*   74 */   private static final DataParameter<Integer> INTELLIGENCE = EntityDataManager.createKey(EntityVillagerTek.class, DataSerializers.VARINT);
/*   75 */   private static final DataParameter<Integer> FORCE_AXIS = EntityDataManager.createKey(EntityVillagerTek.class, DataSerializers.VARINT);
/*   76 */   private static final DataParameter<Boolean> SLEEPING = EntityDataManager.createKey(EntityVillagerTek.class, DataSerializers.BOOLEAN);
/*   77 */   private static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(EntityVillagerTek.class, DataSerializers.BOOLEAN);
/*   78 */   private static final DataParameter<ItemStack> ACTION_ITEM = EntityDataManager.createKey(EntityVillagerTek.class, DataSerializers.ITEM_STACK);
/*   79 */   private static final DataParameter<Byte> MOVEMENT_MODE = EntityDataManager.createKey(EntityVillagerTek.class, DataSerializers.BYTE);
/*   80 */   private static final DataParameter<Integer> BLESSED = EntityDataManager.createKey(EntityVillagerTek.class, DataSerializers.VARINT);
/*      */ 
/*      */   
/*   83 */   private static final DataParameter<Boolean> VISIT_TAVERN = EntityDataManager.createKey(EntityVillagerTek.class, DataSerializers.BOOLEAN);
/*   84 */   private static final DataParameter<Boolean> READ_BOOK = EntityDataManager.createKey(EntityVillagerTek.class, DataSerializers.BOOLEAN);
/*      */ 
/*      */   
/*   87 */   public static int SLEEP_DURATION = 8000;
/*   88 */   public static int SLEEP_START_TIME = 16000;
/*   89 */   public static int SLEEP_END_TIME = SLEEP_START_TIME + SLEEP_DURATION;
/*   90 */   public static int WORK_START_TIME = 500;
/*   91 */   public static int WORK_END_TIME = 11500;
/*      */   
/*      */   private static final boolean ALL_MALES = false;
/*      */   private static final boolean ALL_FEMALES = false;
/*      */   protected static final int DEFAULT_JOB_PRIORITY = 50;
/*   96 */   private PacketVillagerThought nextThought = null;
/*      */   
/*   98 */   private static int[] recentEatPenalties = new int[] { 2, 0, -3, -7, -12, -18 };
/*      */   
/*  100 */   private static final Map<ProfessionType, DataParameter<Integer>> SKILLS = new EnumMap<>(ProfessionType.class); protected ItemDesireSet desireSet; protected VillagerInventory villagerInventory;
/*      */   static {
/*  102 */     for (ProfessionType pt : ProfessionType.values()) {
/*  103 */       SKILLS.put(pt, EntityDataManager.createKey(EntityVillagerTek.class, DataSerializers.VARINT));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  110 */   protected BlockPos bedPos = null;
/*  111 */   protected BlockPos homeFrame = null;
/*      */   
/*  113 */   protected EntityAnimal leadAnimal = null;
/*      */   private MovementMode lastMovementMode;
/*  115 */   private VillageStructure lastCrowdCheck = null;
/*      */   
/*  117 */   protected int sleepOffset = 0;
/*      */   
/*  119 */   protected int wantsLearning = 0;
/*      */   protected boolean wantsTavern = false;
/*  121 */   protected int lastSadTick = 0;
/*  122 */   protected int lastSadThrottle = 200;
/*  123 */   private int idle = 0;
/*  124 */   protected int daysAlive = 0;
/*  125 */   private int dayCheckTime = 0;
/*      */   
/*      */   private final ProfessionType professionType;
/*      */   private Map<String, DataParameter<Boolean>> aiFilters;
/*  129 */   private LinkedList<Integer> recentEats = new LinkedList<>();
/*      */   
/*      */   public enum MovementMode {
/*  132 */     WALK((byte)1, 1.0F, "villager_walk"),
/*  133 */     SKIP((byte)2, 1.1F, "villager_skip"),
/*  134 */     RUN((byte)3, 1.4F, "villager_run"),
/*  135 */     SULK((byte)4, 0.7F, "villager_walk_sad"); public float speedMult; public byte id; public String anim;
/*      */     
/*      */     MovementMode(byte id, float mult, String anim) {
/*  138 */       this.speedMult = mult;
/*  139 */       this.id = id;
/*  140 */       this.anim = anim;
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
/*      */ 
/*      */   
/*      */   public enum VillagerThought
/*      */   {
/*  157 */     BED(115, "red_bed.png"),
/*  158 */     HUNGRY(116, "food.png"),
/*  159 */     PICK(117, "iron_pick.png"),
/*  160 */     HOE(118, "iron_hoe.png"),
/*  161 */     AXE(119, "iron_axe.png"),
/*  162 */     SWORD(120, "iron_sword.png"),
/*  163 */     BOOKSHELF(121, "bookshelf.png"),
/*  164 */     PIG_FOOD(122, "pig_carrot.png"),
/*  165 */     SHEEP_FOOD(123, "sheep_wheat.png"),
/*  166 */     COW_FOOD(124, "cow_wheat.png"),
/*  167 */     CHICKEN_FOOD(125, "chicken_seeds.png"),
/*  168 */     BUCKET(126, "bucket.png"),
/*  169 */     SHEARS(127, "shears.png"),
/*  170 */     TAVERN(128, "structure_tavern.png"),
/*  171 */     NOTEBLOCK(129, "noteblock.png"),
/*  172 */     TEACHER(130, "prof_teacher.png"),
/*  173 */     TORCH(131, "torch.png"),
/*  174 */     INSOMNIA(132, "insomnia.png"),
/*  175 */     CROWDED(133, "crowded.png"),
/*  176 */     DO_NOT_USE(999, "meh.png");
/*      */     private int numVal;
/*      */     private String texture;
/*      */     
/*      */     VillagerThought(int val, String tex) {
/*  181 */       this.numVal = val;
/*  182 */       this.texture = tex;
/*      */     }
/*      */     
/*  185 */     public int getVal() { return this.numVal; }
/*  186 */     public String getTex() { return this.texture; } public float getScale() {
/*  187 */       return 1.0F;
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
/*      */   public EntityVillagerTek(World worldIn, ProfessionType profType, int roleMask) {
/*  201 */     super(worldIn, roleMask);
/*  202 */     this.professionType = profType;
/*  203 */     setCanPickUpLoot(true);
/*  204 */     this.villagerInventory = new VillagerInventory(this, "Items", false, 27);
/*  205 */     setSize(0.6F, 1.95F);
/*  206 */     setHunger(getMaxHunger());
/*  207 */     setHappy(getMaxHappy());
/*  208 */     setIntelligence(0);
/*      */     
/*  210 */     ModEntities.makeTaggedEntity((Entity)this, EntityTagType.VILLAGER);
/*      */     
/*  212 */     Runnable foodChomp = () -> this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.NEUTRAL, 1.0F, this.rand.nextFloat() * 0.2F + 0.9F, false);
/*      */ 
/*      */ 
/*      */     
/*  216 */     Runnable doneEating = () -> {
/*      */         unequipActionItem();
/*      */         if (getRNG().nextInt(12) == 0) {
/*      */           this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.NEUTRAL, 1.0F, this.rand.nextFloat() * 0.2F + 0.9F, false);
/*      */         }
/*      */       };
/*  222 */     if (this.world.isRemote) {
/*  223 */       addAnimationTrigger("tektopia:villager_eat", 25, foodChomp);
/*  224 */       addAnimationTrigger("tektopia:villager_eat", 50, foodChomp);
/*  225 */       addAnimationTrigger("tektopia:villager_eat", 75, foodChomp);
/*  226 */       addAnimationTrigger("tektopia:villager_eat", 90, doneEating);
/*      */     } 
/*      */     
/*  229 */     if (!worldIn.isRemote) {
/*  230 */       randomizeGoals();
/*  231 */       if (this.professionType != null && getBaseSkill(this.professionType) < 1) {
/*  232 */         setSkill(this.professionType, 1);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   protected static void setupAnimations(AnimationHandler animHandler, String modelName) {
/*  238 */     EntityVillageNavigator.setupAnimations(animHandler, modelName);
/*  239 */     animHandler.addAnim("tektopia", "villager_eat", modelName, false);
/*  240 */     animHandler.addAnim("tektopia", "villager_sleep", modelName, true);
/*  241 */     animHandler.addAnim("tektopia", "villager_sit", modelName, true);
/*  242 */     animHandler.addAnim("tektopia", "villager_sit_cheer", modelName, false);
/*  243 */     animHandler.addAnim("tektopia", "villager_walk", modelName, true);
/*  244 */     animHandler.addAnim("tektopia", "villager_walk_sad", modelName, true);
/*  245 */     animHandler.addAnim("tektopia", "villager_run", modelName, true);
/*  246 */     animHandler.addAnim("tektopia", "villager_read", modelName, false);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void initEntityAIBase() {
/*  251 */     Function<ItemStack, Integer> bestFood = p -> Integer.valueOf(EntityAIEatFood.getFoodScore(p.getItem(), this));
/*  252 */     getDesireSet().addItemDesire((ItemDesire)new UpgradeItemDesire("Food", bestFood, 1, 3, 4, p -> (p.isHungry() || p.isStoragePriority())));
/*      */     
/*  254 */     addTask(50, (EntityAIBase)new EntityAIEatFood(this));
/*  255 */     addTask(50, (EntityAIBase)new EntityAIRetrieveFromStorage2(this));
/*  256 */     addTask(50, (EntityAIBase)new EntityAISleep(this));
/*  257 */     addTask(50, (EntityAIBase)new EntityAIDeliverToStorage2(this));
/*  258 */     addTask(50, (EntityAIBase)new EntityAITavernVisit(this, p -> (p.wantsTavern() && !p.isWorkTime() && !p.shouldSleep())));
/*  259 */     addTask(50, (EntityAIBase)new EntityAIWanderStructure(this, getVillagerHome(), p -> (!p.isWorkTime() && !wantsTavern()), 12));
/*  260 */     addTask(50, (EntityAIBase)new EntityAIReadBook(this));
/*      */ 
/*      */     
/*  263 */     addTask(60, (EntityAIBase)new EntityAIGenericMove(this, p -> (p.isWorkTime() && p.hasVillage() && p.getIdle() > 100), v -> this.village.getLastVillagerPos(), MovementMode.WALK, null, null));
/*      */ 
/*      */     
/*  266 */     addTask(150, (EntityAIBase)new EntityAIIdleCheck(this));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void initEntityAI() {
/*  271 */     this.desireSet = new ItemDesireSet();
/*      */     
/*  273 */     addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  274 */     addTask(1, (EntityAIBase)new EntityAIFleeEntity(this, p -> isFleeFrom(p), 16.0F));
/*      */     
/*  276 */     addTask(15, (EntityAIBase)new EntityAIUseDoor((EntityLiving)this, true));
/*  277 */     addTask(15, (EntityAIBase)new EntityAIOpenGate(this));
/*      */     
/*  279 */     initEntityAIBase();
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
/*  284 */     setCustomNameTag(generateName());
/*      */     
/*  286 */     double intel = getRNG().nextGaussian() * 10.0D;
/*  287 */     if (intel < 0.0D) {
/*  288 */       intel *= 0.3D;
/*      */     }
/*  290 */     intel = Math.max(intel + 10.0D, 2.0D);
/*  291 */     setIntelligence((int)intel);
/*      */     
/*  293 */     return super.onInitialSpawn(difficulty, livingdata);
/*      */   }
/*      */   
/*      */   public String generateName() {
/*  297 */     String nameTytpe = isMale() ? "malename" : "femalename";
/*      */     
/*  299 */     String firstSTotal = I18n.translateToLocal("villager." + nameTytpe + ".total");
/*  300 */     int firstTotal = Integer.parseInt(firstSTotal);
/*  301 */     String lastSTotal = I18n.translateToLocal("villager.lastname.total");
/*  302 */     int lastTotal = Integer.parseInt(lastSTotal);
/*      */     
/*  304 */     String firstName = I18n.translateToLocal("villager." + nameTytpe + "." + getRNG().nextInt(firstTotal));
/*  305 */     String lastName = I18n.translateToLocal("villager.lastname." + getRNG().nextInt(lastTotal));
/*      */     
/*  307 */     return firstName + " " + lastName;
/*      */   }
/*      */   
/*      */   public String getLastName() {
/*  311 */     String name = getCustomNameTag();
/*  312 */     String[] splitNames = name.split("\\s+");
/*  313 */     if (splitNames.length >= 2) {
/*  314 */       return splitNames[1];
/*      */     }
/*      */     
/*  317 */     return "";
/*      */   }
/*      */   
/*      */   public String getFirstName() {
/*  321 */     String name = getCustomNameTag();
/*  322 */     String[] splitNames = name.split("\\s+");
/*  323 */     if (splitNames.length >= 2) {
/*  324 */       return splitNames[0];
/*      */     }
/*      */     
/*  327 */     return "";
/*      */   }
/*      */   
/*      */   public String getDebugName() {
/*  331 */     return getClass().getSimpleName() + getEntityId();
/*      */   }
/*      */   public ProfessionType getProfessionType() {
/*  334 */     return this.professionType;
/*      */   }
/*      */   protected boolean hasTavern() {
/*  337 */     return (hasVillage() && getVillage().hasStructure(VillageStructureType.TAVERN));
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean wantsTavern() {
/*  342 */     if (getHappy() >= 100 || !hasTavern()) {
/*  343 */       return false;
/*      */     }
/*  345 */     if (!isWorkTime() && (
/*  346 */       getHappy() < 70 || this.wantsTavern)) {
/*  347 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  351 */     if (getHappy() < 10) {
/*  352 */       return true;
/*      */     }
/*      */     
/*  355 */     return false;
/*      */   }
/*      */   
/*      */   protected void addTask(int priority, EntityAIBase task) {
/*  359 */     this.tasks.addTask(priority, task);
/*      */   }
/*      */   
/*      */   public boolean isDeliveryTime() {
/*  363 */     return isWorkTime();
/*      */   }
/*      */   
/*      */   private static class CleanUpRunnable implements Runnable {
/*      */     private final WeakReference<EntityVillagerTek> villager;
/*      */     
/*      */     public CleanUpRunnable(EntityVillagerTek v) {
/*  370 */       this.villager = new WeakReference<>(v);
/*      */     }
/*      */     public void run() {
/*  373 */       if (this.villager.get() != null)
/*  374 */         ((EntityVillagerTek)this.villager.get()).cleanUpInventory(); 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class GoalRandomizerRunnable implements Runnable {
/*      */     private final WeakReference<EntityVillagerTek> villager;
/*      */     
/*      */     public GoalRandomizerRunnable(EntityVillagerTek v) {
/*  382 */       this.villager = new WeakReference<>(v);
/*      */     }
/*      */     public void run() {
/*  385 */       if (this.villager.get() != null)
/*  386 */         ((EntityVillagerTek)this.villager.get()).randomizeGoals(); 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class BedCheckRunnable implements Runnable {
/*      */     private final WeakReference<EntityVillagerTek> villager;
/*      */     
/*      */     public BedCheckRunnable(EntityVillagerTek v) {
/*  394 */       this.villager = new WeakReference<>(v);
/*      */     }
/*      */     public void run() {
/*  397 */       if (this.villager.get() != null)
/*  398 */         ((EntityVillagerTek)this.villager.get()).bedCheck(); 
/*      */     }
/*      */   }
/*      */   
/*      */   protected void setupServerJobs() {
/*  403 */     super.setupServerJobs();
/*      */     
/*  405 */     addJob(new TickJob(60, 120, true, new CleanUpRunnable(this)));
/*  406 */     addJob(new TickJob(23900, 200, true, new GoalRandomizerRunnable(this)));
/*      */ 
/*      */     
/*  409 */     addJob(new TickJob(80, 10, true, () -> {
/*      */             if (isStarving()) {
/*      */               attackEntityFrom(DamageSource.STARVE, 1.0F);
/*      */             }
/*      */           }));
/*      */ 
/*      */     
/*  416 */     addJob(new TickJob(30, 30, true, () -> scanForEnemies()));
/*      */     
/*  418 */     addJob(new TickJob(100, 200, true, () -> fixOffGraph()));
/*      */     
/*  420 */     addJob(new TickJob(200, 300, true, () -> dayCheck()));
/*      */     
/*  422 */     addJob(new TickJob(200, 100, true, () -> crowdingCheck()));
/*      */ 
/*      */     
/*  425 */     addJob(new TickJob(40, 100, true, new BedCheckRunnable(this)));
/*      */ 
/*      */     
/*  428 */     addJob(new TickJob(20, 30, false, () -> {
/*      */             VillageManager vm = VillageManager.get(this.world);
/*      */             vm.addScanBox((new AxisAlignedBB(getPosition())).grow(64.0D));
/*      */           }));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applyEntityAttributes() {
/*  436 */     super.applyEntityAttributes();
/*  437 */     getAttributeMap().registerAttribute(MAX_HUNGER);
/*  438 */     getAttributeMap().registerAttribute(MAX_HAPPY);
/*  439 */     getAttributeMap().registerAttribute(MAX_INTELLIGENCE);
/*  440 */     getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
/*  441 */     getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
/*  442 */     getEntityAttribute(MAX_HUNGER).setBaseValue(100.0D);
/*  443 */     getEntityAttribute(MAX_HAPPY).setBaseValue(100.0D);
/*  444 */     getEntityAttribute(MAX_INTELLIGENCE).setBaseValue(100.0D);
/*  445 */     getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
/*      */     
/*  447 */     this.dataManager.set(HUNGER, Integer.valueOf(getMaxHunger()));
/*  448 */     this.dataManager.set(HAPPY, Integer.valueOf(getMaxHappy()));
/*  449 */     this.dataManager.set(INTELLIGENCE, Integer.valueOf(getMaxIntelligence()));
/*  450 */     this.dataManager.set(FORCE_AXIS, Integer.valueOf(-1));
/*  451 */     this.dataManager.set(SLEEPING, Boolean.valueOf(false));
/*  452 */     this.dataManager.set(SITTING, Boolean.valueOf(false));
/*  453 */     this.dataManager.set(ACTION_ITEM, ItemStack.EMPTY);
/*  454 */     this.dataManager.set(MOVEMENT_MODE, Byte.valueOf(MovementMode.WALK.id));
/*  455 */     this.dataManager.set(BLESSED, Integer.valueOf(0));
/*      */ 
/*      */     
/*  458 */     for (DataParameter<Integer> skill : SKILLS.values()) {
/*  459 */       this.dataManager.set(skill, Integer.valueOf(0));
/*      */     }
/*      */     
/*  462 */     for (DataParameter<Boolean> aiFilter : this.aiFilters.values()) {
/*  463 */       this.dataManager.set(aiFilter, Boolean.valueOf(true));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void entityInit() {
/*  469 */     this.aiFilters = new HashMap<>();
/*  470 */     registerAIFilter("read_book", READ_BOOK);
/*  471 */     registerAIFilter("visit_tavern", VISIT_TAVERN);
/*      */     
/*  473 */     this.dataManager.register(HUNGER, Integer.valueOf(0));
/*  474 */     this.dataManager.register(HAPPY, Integer.valueOf(0));
/*  475 */     this.dataManager.register(INTELLIGENCE, Integer.valueOf(1));
/*  476 */     this.dataManager.register(FORCE_AXIS, Integer.valueOf(-1));
/*  477 */     this.dataManager.register(SLEEPING, Boolean.valueOf(false));
/*  478 */     this.dataManager.register(SITTING, Boolean.valueOf(false));
/*  479 */     this.dataManager.register(ACTION_ITEM, ItemStack.EMPTY);
/*  480 */     this.dataManager.register(MOVEMENT_MODE, Byte.valueOf((byte)0));
/*  481 */     this.dataManager.register(BLESSED, Integer.valueOf(0));
/*      */     
/*  483 */     for (DataParameter<Integer> skill : SKILLS.values()) {
/*  484 */       this.dataManager.register(skill, Integer.valueOf(0));
/*      */     }
/*      */     
/*  487 */     super.entityInit();
/*  488 */     onStopSit();
/*      */   }
/*      */   
/*      */   public boolean isAITick(String aiFilter) {
/*  492 */     return (isAITick() && isAIFilterEnabled(aiFilter));
/*      */   }
/*      */   
/*      */   protected boolean villageHasStorageCount(Predicate<ItemStack> pred, int count) {
/*  496 */     if (hasVillage()) {
/*  497 */       return (getVillage().getStorageCount(pred) >= count);
/*      */     }
/*      */     
/*  500 */     return false;
/*      */   }
/*      */   
/*      */   protected void crowdingCheck() {
/*  504 */     if (!isSleeping()) {
/*  505 */       VillageStructure struct = getCurrentStructure();
/*  506 */       if (struct != this.lastCrowdCheck) {
/*  507 */         this.lastCrowdCheck = struct;
/*  508 */         if (struct != null) {
/*  509 */           float crowdFactor = struct.getCrowdedFactor();
/*  510 */           if (crowdFactor > 0.0F) {
/*  511 */             int CROWD_PENALTY = -5;
/*  512 */             setThought(VillagerThought.CROWDED);
/*  513 */             int penalty = (int)(-5.0F * crowdFactor);
/*  514 */             modifyHappy(penalty);
/*  515 */             debugOut("Crowding penalty [" + penalty + "] in " + struct.type.name() + " at " + getPosition());
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getDaysAlive() {
/*  523 */     return this.daysAlive;
/*      */   }
/*      */   
/*      */   protected void dayCheck() {
/*  527 */     int curTime = (int)Village.getTimeOfDay(this.world);
/*  528 */     if (curTime < this.dayCheckTime) {
/*  529 */       this.dayCheckTime = curTime;
/*  530 */       this.daysAlive++;
/*  531 */       onNewDay();
/*      */     } else {
/*      */       
/*  534 */       this.dayCheckTime = curTime;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void onNewDay() {
/*  539 */     randomizeGoals();
/*      */   }
/*      */   
/*      */   protected void fixOffGraph() {
/*  543 */     if (!hasVillage()) {
/*  544 */       Village v = VillageManager.get(this.world).getNearestVillage(getPosition(), 130);
/*  545 */       if (v != null) {
/*      */         
/*  547 */         BlockPos bestPos = null;
/*  548 */         double bestDist = Double.MAX_VALUE;
/*  549 */         for (BlockPos testPos : BlockPos.getAllInBox(getPosition().add(-3, -3, -3), getPosition().add(3, 3, 3))) {
/*  550 */           if (v.getPathingGraph().isInGraph(testPos)) {
/*  551 */             double dist = getPosition().distanceSq((Vec3i)testPos);
/*  552 */             if (bestPos == null || dist < bestDist) {
/*  553 */               bestPos = testPos;
/*  554 */               bestDist = dist;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/*  559 */         if (bestPos != null) {
/*  560 */           setPositionAndUpdate(bestPos.getX(), bestPos.getY(), bestPos.getZ());
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void scanForEnemies() {
/*  568 */     if (!isRole(VillagerRole.VENDOR) && !isRole(VillagerRole.VISITOR)) {
/*  569 */       ListIterator<EntityLiving> itr = this.world.getEntitiesWithinAABB(EntityLiving.class, getEntityBoundingBox().grow(30.0D, 6.0D, 30.0D), isEnemy()).listIterator();
/*  570 */       while (itr.hasNext()) {
/*  571 */         EntityLiving enemy = itr.next();
/*  572 */         if (canEntityBeSeen((Entity)enemy) && hasVillage()) {
/*  573 */           getVillage().addOrRenewEnemy((EntityLivingBase)enemy, 1);
/*      */         }
/*  575 */         IAttributeInstance attribute = enemy.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
/*  576 */         if (attribute != null) {
/*  577 */           double distMe = enemy.getDistanceSq((Entity)this);
/*  578 */           double enemyRange = attribute.getAttributeValue() * attribute.getAttributeValue();
/*      */           
/*  580 */           if (distMe < enemyRange) {
/*      */             
/*  582 */             if (enemy.getAttackTarget() == null) {
/*  583 */               enemy.setAttackTarget((EntityLivingBase)this);
/*      */               continue;
/*      */             } 
/*  586 */             double distTarget = enemy.getDistanceSq((Entity)enemy.getAttackTarget());
/*      */             
/*  588 */             if (distMe < distTarget) {
/*  589 */               enemy.setAttackTarget((EntityLivingBase)this); continue;
/*  590 */             }  if (hasVillage()) {
/*  591 */               boolean meIndoors = getVillage().isInStructure(getPosition());
/*  592 */               boolean currentTargetIndoors = getVillage().isInStructure(enemy.getAttackTarget().getPosition());
/*      */               
/*  594 */               if (!meIndoors && currentTargetIndoors) {
/*  595 */                 enemy.setAttackTarget((EntityLivingBase)this);
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void damageArmor(float damage) {
/*  607 */     if (damage < 1.0F)
/*      */     {
/*  609 */       damage = 1.0F;
/*      */     }
/*      */     
/*  612 */     int finalDmg = (int)damage;
/*  613 */     getArmorInventoryList().forEach(a -> {
/*      */           if (a.getItem() instanceof net.minecraft.item.ItemArmor && getRNG().nextBoolean()) {
/*      */             damageItem(a, finalDmg);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public void damageItem(ItemStack itemStack, int amount) {
/*  622 */     int itemDamage = ModItems.isTaggedItem(itemStack, ItemTagType.VILLAGER) ? amount : (amount * 5);
/*      */     
/*  624 */     ItemStack oldItem = null;
/*  625 */     if (itemStack.getItemDamage() + itemDamage >= itemStack.getMaxDamage()) {
/*  626 */       oldItem = itemStack.copy();
/*      */     }
/*  628 */     itemStack.damageItem(itemDamage, (EntityLivingBase)this);
/*      */     
/*  630 */     if (itemStack.isEmpty() && oldItem != null) {
/*  631 */       onInventoryUpdated(oldItem);
/*      */     }
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public Entity changeDimension(int dimensionIn, ITeleporter teleporter) {
/*  637 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBePushed() {
/*  642 */     if (isSitting() || isSleeping()) {
/*  643 */       return false;
/*      */     }
/*  645 */     return super.canBePushed();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void collideWithEntity(Entity entityIn) {
/*  650 */     if (!isSitting()) {
/*  651 */       if (entityIn instanceof EntityVillagerTek && 
/*  652 */         isDoorNearby(1, 1)) {
/*      */         return;
/*      */       }
/*      */       
/*  656 */       super.collideWithEntity(entityIn);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void collideWithNearbyEntities() {
/*  662 */     if (!isSitting() && !isSleeping())
/*  663 */       super.collideWithNearbyEntities(); 
/*      */   }
/*      */   
/*      */   protected boolean isDoorNearby(int xx, int zz) {
/*  667 */     for (int x = -xx; x <= xx; x++) {
/*  668 */       for (int z = -zz; z <= zz; z++) {
/*  669 */         BlockPos bp = getPosition().east(x).north(z);
/*  670 */         if (VillageStructure.isWoodDoor(this.world, bp) || VillageStructure.isGate(this.world, bp)) {
/*  671 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  676 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getAmbientSound() {
/*  681 */     if (isSleeping() && 
/*  682 */       this.world.rand.nextInt(2) == 0) {
/*  683 */       return ModSoundEvents.villagerSleep;
/*      */     }
/*  685 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void bedCheck() {
/*  690 */     if (hasVillage()) {
/*  691 */       if (getBedPos() != null && this.homeFrame != null) {
/*  692 */         VillageStructure struct = this.village.getStructureFromFrame(this.homeFrame);
/*  693 */         if (struct != null && struct instanceof VillageStructureHome) {
/*  694 */           VillageStructureHome home = (VillageStructureHome)struct;
/*  695 */           if (!home.canVillagerSleep(this)) {
/*  696 */             clearHome();
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/*  701 */       if (getBedPos() == null) {
/*  702 */         if (this.homeFrame != null) {
/*      */           
/*  704 */           VillageStructure struct = this.village.getStructureFromFrame(this.homeFrame);
/*  705 */           if (struct != null && struct instanceof VillageStructureHome) {
/*  706 */             VillageStructureHome home = (VillageStructureHome)struct;
/*  707 */             if (home.canVillagerSleep(this) && !home.isFull()) {
/*  708 */               this.homeFrame = null;
/*  709 */               home.addResident(this);
/*      */             } else {
/*  711 */               clearHome();
/*      */             } 
/*  713 */           } else if (this.ticksExisted > 200) {
/*      */             
/*  715 */             clearHome();
/*      */           } 
/*      */         } else {
/*  718 */           VillageStructureHome home = this.village.getAvailableHome(this);
/*  719 */           if (home != null) {
/*  720 */             home.addResident(this);
/*      */           }
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public VillageStructure getCurrentStructure() {
/*  728 */     if (hasVillage()) {
/*  729 */       return getVillage().getStructure(getPosition());
/*      */     }
/*  731 */     return null;
/*      */   }
/*      */   
/*      */   public static Function<EntityVillagerTek, VillageStructure> findLocalTavern() {
/*  735 */     return p -> {
/*      */         if (p.hasVillage()) {
/*      */           VillageStructure tavern = p.getVillage().getNearestStructure(VillageStructureType.TAVERN, p.getPosition());
/*      */           if (tavern != null) {
/*      */             return tavern;
/*      */           }
/*      */         } 
/*      */         return null;
/*      */       };
/*      */   }
/*      */   
/*      */   public static Function<EntityVillagerTek, VillageStructure> getVillagerHome() {
/*  747 */     return p -> p.hasHome() ? p.getVillage().getStructureFromFrame(p.homeFrame) : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void attachToVillage(Village v) {
/*  757 */     super.attachToVillage(v);
/*      */     
/*  759 */     this.sleepOffset = genOffset(400);
/*      */     
/*  761 */     if (isRole(VillagerRole.VILLAGER)) {
/*  762 */       v.addResident(this);
/*      */     }
/*      */     
/*  765 */     if (getIntelligence() < 1) {
/*  766 */       setIntelligence(1);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void detachVillage() {
/*  771 */     if (hasVillage()) {
/*  772 */       this.village.removeResident(this);
/*      */     }
/*  774 */     super.detachVillage();
/*      */   }
/*      */   
/*      */   protected int genOffset(int range) {
/*  778 */     return getRNG().nextInt(range) - range / 2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void randomizeGoals() {
/*  784 */     if (getRNG().nextInt(100) < 28 && getIntelligence() < getMaxIntelligence()) {
/*  785 */       this.wantsLearning = getRNG().nextInt(6) + 3;
/*      */     } else {
/*  787 */       this.wantsLearning = 0;
/*      */     } 
/*      */     
/*  790 */     this.wantsTavern = (getRNG().nextInt(10) < 3);
/*      */   }
/*      */   
/*      */   private static void cleanUpInventory(EntityVillagerTek v) {
/*  794 */     v.cleanUpInventory();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void cleanUpInventory() {}
/*      */   
/*      */   protected void onNewPotionEffect(PotionEffect effect) {
/*  801 */     if (effect.getPotion() == ModPotions.potionBless) {
/*  802 */       this.dataManager.set(BLESSED, Integer.valueOf(effect.getAmplifier()));
/*      */     }
/*      */     
/*  805 */     super.onNewPotionEffect(effect);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onFinishedPotionEffect(PotionEffect effect) {
/*  810 */     if (effect.getPotion() == ModPotions.potionBless) {
/*  811 */       this.dataManager.set(BLESSED, Integer.valueOf(0));
/*      */     }
/*      */     
/*  814 */     super.onFinishedPotionEffect(effect);
/*      */   }
/*      */   
/*      */   public void onLivingUpdate() {
/*  818 */     super.onLivingUpdate();
/*      */     
/*  820 */     if (getLeashed() && getRNG().nextInt((getHappy() < 50) ? 30 : 50) == 0) {
/*  821 */       modifyHappy(-1);
/*  822 */       if (getHappy() <= 0) {
/*  823 */         clearLeashed(true, true);
/*      */       }
/*      */     } 
/*      */     
/*  827 */     if (!isWorldRemote()) {
/*  828 */       this.lastSadTick++;
/*      */       
/*  830 */       if (this.nextThought != null && this.ticksExisted % 80 == 0) {
/*  831 */         TekVillager.NETWORK.sendToAllAround((IMessage)this.nextThought, new NetworkRegistry.TargetPoint(getDimension(), this.posX, this.posY, this.posZ, 64.0D));
/*  832 */         this.nextThought = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @SideOnly(Side.CLIENT)
/*      */   protected void startWalking() {
/*  840 */     MovementMode mode = getMovementMode();
/*  841 */     if (mode != this.lastMovementMode) {
/*  842 */       if (this.lastMovementMode != null) {
/*  843 */         stopWalking();
/*      */       }
/*      */       
/*  846 */       this.lastMovementMode = mode;
/*      */       
/*  848 */       if (mode != null) {
/*  849 */         playClientAnimation(mode.anim);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @SideOnly(Side.CLIENT)
/*      */   protected void stopWalking() {
/*  857 */     if (this.lastMovementMode != null) {
/*  858 */       stopClientAnimation(this.lastMovementMode.anim);
/*  859 */       this.lastMovementMode = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   public BlockPos getBedPos() {
/*  864 */     return this.bedPos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SideOnly(Side.CLIENT)
/*      */   public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
/*  873 */     super.setPositionAndRotationDirect(x, y, z, yaw, pitch, 1, teleport);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHome(BlockPos bedPos, BlockPos homeFrame) {
/*  878 */     this.bedPos = bedPos;
/*  879 */     this.homeFrame = homeFrame;
/*      */   }
/*      */   
/*      */   public void clearHome() {
/*  883 */     this.bedPos = null;
/*  884 */     this.homeFrame = null;
/*      */   }
/*      */   
/*      */   public boolean hasHome() {
/*  888 */     return (hasVillage() && this.homeFrame != null);
/*      */   }
/*      */   
/*      */   public VillageStructureHome getHome() {
/*  892 */     if (hasHome()) {
/*  893 */       VillageStructureHome home = (VillageStructureHome)this.village.getStructureFromFrame(this.homeFrame);
/*  894 */       return home;
/*      */     } 
/*      */     
/*  897 */     return null;
/*      */   }
/*      */   
/*      */   public void addVillagerPosition() {
/*  901 */     if (hasVillage()) {
/*  902 */       getVillage().addVillagerPosition(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public final int getMaxHunger() {
/*  907 */     return (int)getEntityAttribute(MAX_HUNGER).getAttributeValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getHunger() {
/*  912 */     return ((Integer)this.dataManager.get(HUNGER)).intValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHunger(int hunger) {
/*  917 */     if (isRole(VillagerRole.VILLAGER)) {
/*  918 */       this.dataManager.set(HUNGER, Integer.valueOf(MathHelper.clamp(hunger, 0, getMaxHunger())));
/*  919 */       if (hunger < 0 && isHungry())
/*      */       {
/*  921 */         setThought(VillagerThought.HUNGRY);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getMaxHappy() {
/*  928 */     return (int)getEntityAttribute(MAX_HAPPY).getAttributeValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getHappy() {
/*  933 */     return ((Integer)this.dataManager.get(HAPPY)).intValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public int setHappy(int happy) {
/*  938 */     int prevHappy = getHappy();
/*  939 */     this.dataManager.set(HAPPY, Integer.valueOf(MathHelper.clamp(happy, 0, getMaxHappy())));
/*  940 */     return getHappy() - prevHappy;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getMaxIntelligence() {
/*  945 */     return (int)getEntityAttribute(MAX_INTELLIGENCE).getAttributeValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getIntelligence() {
/*  950 */     int intel = Math.max(((Integer)this.dataManager.get(INTELLIGENCE)).intValue(), 1);
/*  951 */     return intel;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setIntelligence(int intel) {
/*  956 */     this.dataManager.set(INTELLIGENCE, Integer.valueOf(MathHelper.clamp(intel, 1, getMaxIntelligence())));
/*      */   }
/*      */   
/*      */   public int getBlessed() {
/*  960 */     return ((Integer)this.dataManager.get(BLESSED)).intValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSkill(ProfessionType pt) {
/*  965 */     int skill = getBaseSkill(pt);
/*      */     
/*  967 */     if (pt == getProfessionType()) {
/*  968 */       skill += getBlessed();
/*      */     }
/*  970 */     return Math.min(skill, 100);
/*      */   }
/*      */   
/*      */   public int getBaseSkill(ProfessionType pt) {
/*  974 */     int skill = ((Integer)this.dataManager.get(SKILLS.get(pt))).intValue();
/*  975 */     return Math.min(skill, 100);
/*      */   }
/*      */   
/*      */   public int getSkillLerp(ProfessionType pt, int min, int max) {
/*  979 */     if (min < max) {
/*  980 */       return (int)MathHelper.clampedLerp(min, max, getSkill(pt) / 100.0D);
/*      */     }
/*  982 */     return (int)MathHelper.clampedLerp(max, min, (100.0D - getSkill(pt)) / 100.0D);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSkill(ProfessionType pt, int val) {
/*  987 */     debugOut("Skill Change - " + pt.name + " --> " + val);
/*  988 */     this.dataManager.set(SKILLS.get(pt), Integer.valueOf(MathHelper.clamp(val, 0, 100)));
/*      */   }
/*      */   
/*      */   public int getForceAxis() {
/*  992 */     return ((Integer)this.dataManager.get(FORCE_AXIS)).intValue();
/*      */   }
/*      */   
/*      */   public void setForceAxis(int axes) {
/*  996 */     this.dataManager.set(FORCE_AXIS, Integer.valueOf(axes));
/*      */   }
/*      */   
/*      */   public boolean isSleeping() {
/* 1000 */     return ((Boolean)this.dataManager.get(SLEEPING)).booleanValue();
/*      */   }
/*      */   public boolean isSitting() {
/* 1003 */     return ((Boolean)this.dataManager.get(SITTING)).booleanValue();
/*      */   }
/*      */   
/*      */   public MovementMode getMovementMode() {
/* 1007 */     MovementMode mode = MovementMode.valueOf(((Byte)this.dataManager.get(MOVEMENT_MODE)).byteValue());
/* 1008 */     return mode;
/*      */   }
/*      */   
/*      */   public void setMovementMode(MovementMode mode) {
/* 1012 */     this.dataManager.set(MOVEMENT_MODE, Byte.valueOf(mode.id));
/*      */   }
/*      */   
/*      */   protected void setSleeping(boolean sleep) {
/* 1016 */     this.dataManager.set(SLEEPING, Boolean.valueOf(sleep));
/*      */   } public void setSitting(boolean sit) {
/* 1018 */     this.dataManager.set(SITTING, Boolean.valueOf(sit));
/*      */   }
/* 1020 */   public boolean isHungry() { return (getHunger() < 30); } public boolean isStarving() {
/* 1021 */     return (getHunger() <= 0);
/*      */   }
/*      */   public boolean shouldSleep() {
/* 1024 */     return (isSleepingTime() || (!isRole(VillagerRole.DEFENDER) && getHealth() < getMaxHealth() * 0.5F));
/*      */   }
/*      */   
/*      */   public boolean isSleepingTime() {
/* 1028 */     if (Village.isTimeOfDay(this.world, (SLEEP_START_TIME + this.sleepOffset + (wantsTavern() ? 4000 : 0)), (SLEEP_END_TIME + this.sleepOffset))) {
/* 1029 */       return true;
/*      */     }
/*      */     
/* 1032 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isWorkTime() {
/* 1036 */     return (Village.isTimeOfDay(this.world, WORK_START_TIME, WORK_END_TIME, this.sleepOffset) && !this.world.isRaining());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLearningTime() {
/* 1042 */     if (this.wantsLearning > 0 && Village.isTimeOfDay(this.world, (this.sleepOffset + 2500), (this.sleepOffset + 8000))) {
/* 1043 */       return isAIFilterEnabled("read_book");
/*      */     }
/*      */     
/* 1046 */     return false;
/*      */   }
/*      */   
/*      */   public void addIntelligence(int delta) {
/* 1050 */     if (delta > 0 && getIntelligence() < 100)
/*      */     {
/*      */       
/* 1053 */       if (getRNG().nextInt(100) * 2 > getIntelligence()) {
/* 1054 */         setIntelligence(getIntelligence() + delta);
/* 1055 */         setItemThought(Items.BOOK);
/* 1056 */         this.wantsLearning--;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public void addIntelligenceDelay(int delta, int ticks) {
/* 1062 */     addJob(new TickJob(ticks, 0, false, () -> addIntelligence(delta)));
/*      */   }
/*      */   
/*      */   public void incrementSkill(ProfessionType pt) {
/* 1066 */     setSkill(pt, getBaseSkill(pt) + 1);
/* 1067 */     setItemThought((Item)ModItems.getProfessionToken(pt));
/* 1068 */     skillUpdated(pt);
/*      */ 
/*      */     
/* 1071 */     if (!isChild()) {
/* 1072 */       List<EntityChild> children = this.world.getEntitiesWithinAABB(EntityChild.class, getEntityBoundingBox().grow(12.0D, 8.0D, 12.0D));
/* 1073 */       if (!children.isEmpty()) {
/* 1074 */         children.forEach(c -> {
/*      */               boolean proximityLearn = true;
/*      */               if (c.hasVillage()) {
/*      */                 VillageStructure struct = c.getVillage().getStructure(c.getPosition());
/*      */                 if (struct != null && struct instanceof net.tangotek.tektopia.structures.VillageStructureSchool) {
/*      */                   proximityLearn = false;
/*      */                 }
/*      */               } 
/*      */               if (proximityLearn && c.getSkill(pt) < getSkill(pt) / 2) {
/*      */                 int chance = Math.max(c.getBaseSkill(pt) / 2, 1);
/*      */                 if (c.getRNG().nextInt(chance) == 0) {
/*      */                   c.incrementSkill(pt);
/*      */                 }
/*      */               } 
/*      */             });
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setIdle(int idle) {
/* 1095 */     this.idle = idle;
/*      */   }
/*      */   
/*      */   public int getIdle() {
/* 1099 */     return this.idle;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void skillUpdated(ProfessionType pt) {}
/*      */   
/*      */   public void tryAddSkill(ProfessionType pt, int chance) {
/* 1106 */     if (!this.world.isRemote) {
/* 1107 */       double skill = getBaseSkill(pt);
/* 1108 */       if (skill < 100.0D) {
/* 1109 */         double intel = Math.max(getIntelligence(), 5.0D);
/*      */         
/* 1111 */         double gapMod = 1.0D / Math.pow(skill / intel, 2.0D);
/*      */ 
/*      */ 
/*      */         
/* 1115 */         double intMod = 0.2D;
/*      */         
/* 1117 */         double intCheck = Math.min(gapMod * 0.2D, 1.0D);
/*      */ 
/*      */         
/* 1120 */         int rate = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getGameRules().getInt("villagerSkillRate");
/*      */         
/* 1122 */         double roll = this.world.rand.nextDouble();
/* 1123 */         double rollModded = roll * rate / 100.0D;
/* 1124 */         if (rollModded <= intCheck && 
/* 1125 */           this.world.rand.nextInt(chance) == 0) {
/* 1126 */           incrementSkill(pt);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void modifyHungerDelay(int delta, int ticks) {
/* 1134 */     addJob(new TickJob(ticks, 0, false, () -> modifyHunger(delta)));
/*      */   }
/*      */ 
/*      */   
/*      */   public void modifyHunger(int delta) {
/* 1139 */     if (delta < 0 && hasVillage() && !getVillage().hasStructure(VillageStructureType.STORAGE)) {
/*      */       return;
/*      */     }
/* 1142 */     setHunger(getHunger() + delta);
/*      */   }
/*      */   
/*      */   public void modifyHappyDelay(int delta, int ticks) {
/* 1146 */     addJob(new TickJob(ticks, 0, false, () -> modifyHappy(delta)));
/*      */   }
/*      */   
/*      */   public void modifyHappy(int delta) {
/* 1150 */     if (delta > 0) {
/* 1151 */       this.world.setEntityState((Entity)this, (byte)14);
/*      */       
/* 1153 */       if (getRNG().nextInt(6) == 0 && !isSleeping()) {
/* 1154 */         playSound(ModSoundEvents.villagerHappy);
/*      */       }
/* 1156 */     } else if (delta < 0) {
/* 1157 */       this.world.setEntityState((Entity)this, (byte)13);
/* 1158 */       if (getRNG().nextInt(2) == 0 && !isSleeping()) {
/* 1159 */         playSound(ModSoundEvents.villagerAngry);
/*      */       }
/*      */     } 
/* 1162 */     int realChange = setHappy(getHappy() + delta);
/*      */     
/* 1164 */     if (hasVillage() && realChange != 0)
/* 1165 */       getVillage().trackHappy(realChange); 
/*      */   }
/*      */   
/*      */   public void throttledSadness(int delta) {
/* 1169 */     if (this.lastSadTick > this.lastSadThrottle && getRNG().nextBoolean()) {
/* 1170 */       addJob(new TickJob(20, 60, false, () -> modifyHappy(delta)));
/* 1171 */       this.lastSadTick = 0;
/* 1172 */       this.lastSadThrottle = 50 + getRNG().nextInt(100);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void cheerBeer(int happy) {
/* 1177 */     int offset = getRNG().nextInt(25);
/* 1178 */     if (!isPlayingAnimation("villager_sit_cheer")) {
/* 1179 */       if (isSitting()) {
/* 1180 */         addJob(new TickJob(offset, 0, false, () -> playServerAnimation("villager_sit_cheer")));
/* 1181 */         addJob(new TickJob(8 + offset, 0, false, () -> equipActionItem(new ItemStack((Item)ModItems.beer))));
/* 1182 */         addJob(new TickJob(52 + offset, 0, false, () -> unequipActionItem()));
/* 1183 */         addJob(new TickJob(58 + offset, 0, false, () -> {
/*      */                 if (isSitting()) {
/*      */                   playServerAnimation("villager_sit");
/*      */                 }
/*      */               }));
/*      */       } 
/* 1189 */       addJob(new TickJob(10 + offset * 2, 0, false, () -> playSound(ModSoundEvents.villagerHappy, 1.2F, getRNG().nextFloat() * 0.4F + 0.8F)));
/* 1190 */       addJob(new TickJob(40, 0, false, () -> modifyHappy(happy)));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTrackingPlayer(EntityPlayerMP player) {
/* 1197 */     super.addTrackingPlayer(player);
/*      */ 
/*      */     
/* 1200 */     if (!this.curAnim.isEmpty()) {
/* 1201 */       playServerAnimation(this.curAnim);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getAIMoveSpeed() {
/* 1208 */     float baseSpeed = 0.45F * (getMovementMode()).speedMult;
/*      */     
/* 1210 */     float modifiedSpeed = baseSpeed;
/*      */     
/* 1212 */     int blessed = getBlessed();
/* 1213 */     if (blessed > 0) {
/* 1214 */       modifiedSpeed = (float)(modifiedSpeed * (1.05D + blessed * 0.002D));
/*      */     }
/* 1216 */     return modifiedSpeed;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLeadAnimal(EntityAnimal animal) {
/* 1221 */     this.leadAnimal = animal;
/*      */   }
/*      */   
/*      */   public EntityAnimal getLeadAnimal() {
/* 1225 */     return this.leadAnimal;
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean canDespawn() {
/* 1230 */     return false;
/*      */   }
/*      */   
/*      */   public VillagerInventory getInventory() {
/* 1234 */     return this.villagerInventory;
/*      */   }
/*      */   
/*      */   public ItemDesireSet getDesireSet() {
/* 1238 */     return this.desireSet;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onStorageChange(ItemStack storageItem) {
/* 1243 */     this.desireSet.onStorageUpdated(this, storageItem);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onInventoryUpdated(ItemStack updatedItem) {
/* 1248 */     this.desireSet.onInventoryUpdated(this, updatedItem);
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean canVillagerPickupItem(ItemStack itemIn) {
/* 1253 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasNoGravity() {
/* 1258 */     return (isSitting() || super.hasNoGravity());
/*      */   }
/*      */   
/*      */   public double getSitOffset() {
/* 1262 */     return 0.0D;
/*      */   }
/*      */   
/*      */   public void onStartSit(int sitAxis) {
/* 1266 */     setForceAxis(sitAxis);
/* 1267 */     setSitting(true);
/* 1268 */     equipActionItem(ModItems.EMPTY_HAND_ITEM);
/* 1269 */     if (!this.curAnim.isEmpty() && this.curAnim != "villager_sit") {
/* 1270 */       stopServerAnimation(this.curAnim);
/*      */     }
/* 1272 */     playServerAnimation("villager_sit");
/*      */   }
/*      */   
/*      */   public void onStopSit() {
/* 1276 */     setSitting(false);
/* 1277 */     setForceAxis(-1);
/* 1278 */     stopServerAnimation("villager_sit");
/* 1279 */     setNoGravity(false);
/* 1280 */     unequipActionItem(ModItems.EMPTY_HAND_ITEM);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onStartSleep(int sleepAxis) {
/* 1285 */     setForceAxis(sleepAxis);
/* 1286 */     setSleeping(true);
/* 1287 */     if (!this.curAnim.isEmpty() && this.curAnim != "villager_sleep") {
/* 1288 */       stopServerAnimation(this.curAnim);
/*      */     }
/* 1290 */     playServerAnimation("villager_sleep");
/*      */   }
/*      */   
/*      */   public void onStopSleep() {
/* 1294 */     if (isSleeping() && 
/* 1295 */       !isSleepingTime()) {
/*      */ 
/*      */       
/* 1298 */       checkSpawnHeart();
/*      */       
/* 1300 */       modifyHappy(this.rand.nextInt(20) + 10);
/*      */     } 
/*      */ 
/*      */     
/* 1304 */     setForceAxis(-1);
/* 1305 */     setSleeping(false);
/* 1306 */     stopServerAnimation("villager_sleep");
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkSpawnHeart() {
/* 1311 */     int MIN_HAPPY = (int)(getMaxHappy() * 0.7F);
/* 1312 */     if (hasVillage() && getHappy() >= MIN_HAPPY) {
/*      */       
/* 1314 */       float happyFactor = (getMaxHappy() - getHappy()) / (getMaxHappy() - MIN_HAPPY);
/* 1315 */       int CONSTANT_DIFFICULTY = 15;
/* 1316 */       int chance = 15 + this.village.getResidentCount() + (int)(this.village.getResidentCount() * happyFactor * 2.0F);
/* 1317 */       if (getRNG().nextInt(chance) == 0 && 
/* 1318 */         hasVillage() && !isChild() && getProfessionType() != ProfessionType.NITWIT) {
/* 1319 */         IVillageData vd = getVillage().getTownData();
/* 1320 */         if (vd != null && this.bedPos != null && 
/* 1321 */           vd.isChildReady(this.world.getTotalWorldTime())) {
/* 1322 */           VillageStructure struct = getVillagerHome().apply(this);
/* 1323 */           if (struct != null && struct.type.isHome()) {
/* 1324 */             VillageStructureHome home = (VillageStructureHome)struct;
/* 1325 */             vd.childSpawned(this.world);
/* 1326 */             addJob(new TickJob(100, 0, false, () -> {
/*      */                     ItemStack itemHeart = ModItems.createTaggedItem((Item)ModItems.heart, ItemTagType.VILLAGER);
/*      */                     itemHeart.getSubCompound("village").setUniqueId("parent", getUniqueID());
/*      */                     EntityItem heartEntity = new EntityItem(this.world, this.bedPos.getX(), (this.bedPos.getY() + 1), this.bedPos.getZ(), itemHeart);
/*      */                     this.world.spawnEntity((Entity)heartEntity);
/*      */                   }));
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MovementMode getDefaultMovement() {
/* 1343 */     if (getHappy() < getMaxHappy() / 5) {
/* 1344 */       return MovementMode.SULK;
/*      */     }
/* 1346 */     return MovementMode.WALK;
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
/*      */   public void updateMovement(boolean arrived) {
/* 1362 */     if (this.world.rand.nextInt(50) == 0) {
/* 1363 */       modifyHunger(-1);
/*      */     }
/* 1365 */     if (!arrived)
/*      */     {
/*      */       
/* 1368 */       if (hasVillage() && getRNG().nextInt(20) == 0) {
/* 1369 */         addVillagerPosition();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void resetMovement() {
/* 1375 */     super.resetMovement();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected ItemStack modifyPickUpStack(ItemStack itemStack) {
/* 1381 */     return itemStack;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateEquipmentIfNeeded(EntityItem itemEntity) {
/* 1387 */     ItemStack itemStack = itemEntity.getItem();
/*      */     
/* 1389 */     if (canVillagerPickupItem(itemStack) && ModItems.canVillagerSee(itemStack)) {
/*      */       
/* 1391 */       ItemStack modStack = modifyPickUpStack(itemStack.copy());
/* 1392 */       if (!modStack.isEmpty()) {
/* 1393 */         ItemStack leftOverStack = this.villagerInventory.addItem(modStack);
/*      */         
/* 1395 */         if (leftOverStack.isEmpty()) {
/* 1396 */           itemEntity.setDead();
/*      */         } else {
/* 1398 */           int actuallyTaken = modStack.getCount() - leftOverStack.getCount();
/* 1399 */           int newCount = itemStack.getCount() - actuallyTaken;
/* 1400 */           if (newCount <= 0) {
/* 1401 */             itemEntity.setDead();
/*      */           } else {
/* 1403 */             itemStack.setCount(newCount);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEntityInvulnerable(DamageSource source) {
/* 1412 */     if (source == DamageSource.IN_WALL) {
/* 1413 */       return true;
/*      */     }
/*      */     
/* 1416 */     return super.isEntityInvulnerable(source);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 1421 */     float beforeHealth = getHealth();
/* 1422 */     if (super.attackEntityFrom(source, amount)) {
/* 1423 */       float afterHealth = getHealth();
/*      */       
/* 1425 */       float actualDamage = beforeHealth - afterHealth;
/* 1426 */       if (actualDamage > 0.0F) {
/*      */         
/* 1428 */         if (!isRole(VillagerRole.DEFENDER)) {
/* 1429 */           modifyHappy(-8);
/*      */         }
/* 1431 */         if (hasVillage() && actualDamage > 0.0F) {
/* 1432 */           getVillage().reportVillagerDamage(this, source, actualDamage);
/*      */         }
/* 1434 */         if (isSleeping()) {
/* 1435 */           onStopSleep();
/*      */         }
/*      */       } 
/*      */       
/* 1439 */       return true;
/*      */     } 
/*      */     
/* 1442 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onDeath(DamageSource cause) {
/* 1468 */     if (hasVillage() && getBedPos() != null) {
/* 1469 */       int happyMod = -25;
/* 1470 */       if (isChild()) {
/* 1471 */         happyMod *= 2;
/*      */       }
/* 1473 */       List<EntityVillagerTek> villagers = this.world.getEntitiesWithinAABB(EntityVillagerTek.class, getEntityBoundingBox().grow(200.0D), p -> (p != this));
/* 1474 */       for (EntityVillagerTek v : villagers) {
/* 1475 */         if (v.getVillage() == getVillage() && v.getBedPos() != null && !v.isSleeping()) {
/* 1476 */           v.modifyHappy(happyMod - getRNG().nextInt(15));
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1481 */     if (hasVillage() && isRole(VillagerRole.VILLAGER)) {
/* 1482 */       getVillage().reportVillagerDeath(this, cause);
/*      */     }
/* 1484 */     super.onDeath(cause);
/*      */     
/* 1486 */     if (!this.world.isRemote)
/* 1487 */       dropAllItems(); 
/*      */   }
/*      */   
/*      */   private void dropAllItems() {
/* 1491 */     VillagerInventory villagerInventory = getInventory();
/* 1492 */     for (int i = 0; i < villagerInventory.getSizeInventory(); i++) {
/*      */       
/* 1494 */       ItemStack itemStack = villagerInventory.getStackInSlot(i);
/* 1495 */       if (!itemStack.isEmpty())
/*      */       {
/* 1497 */         entityDropItem(itemStack, 0.5F);
/*      */       }
/*      */     } 
/* 1500 */     villagerInventory.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void pickupItems(int grow) {
/* 1510 */     for (EntityItem entityitem : this.world.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().grow(grow, 3.0D, grow))) {
/*      */       
/* 1512 */       if (!entityitem.isDead && !entityitem.getItem().isEmpty() && !entityitem.cannotPickup())
/*      */       {
/* 1514 */         updateEquipmentIfNeeded(entityitem);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setThought(VillagerThought thought) {
/* 1520 */     this.nextThought = new PacketVillagerThought(this, thought, thought.getScale());
/*      */   }
/*      */   
/*      */   public void setItemThought(Item item) {
/* 1524 */     ModEntities.sendItemThought((Entity)this, item);
/*      */   }
/*      */ 
/*      */   
/*      */   @SideOnly(Side.CLIENT)
/*      */   public void handleStatusUpdate(byte id) {
/* 1530 */     if (id == 12) {
/*      */       
/* 1532 */       spawnParticles(EnumParticleTypes.HEART);
/*      */     }
/* 1534 */     else if (id == 13) {
/*      */       
/* 1536 */       spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
/*      */     }
/* 1538 */     else if (id == 14) {
/*      */       
/* 1540 */       spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
/*      */     }
/*      */     else {
/*      */       
/* 1544 */       super.handleStatusUpdate(id);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   @SideOnly(Side.CLIENT)
/*      */   private void spawnParticles(EnumParticleTypes particleType) {
/* 1551 */     for (int i = 0; i < 5; i++) {
/*      */       
/* 1553 */       double d0 = this.rand.nextGaussian() * 0.02D;
/* 1554 */       double d1 = this.rand.nextGaussian() * 0.02D;
/* 1555 */       double d2 = this.rand.nextGaussian() * 0.02D;
/* 1556 */       this.world.spawnParticle(particleType, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 1.0D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
/*      */     } 
/*      */   }
/*      */   
/*      */   @SideOnly(Side.CLIENT)
/*      */   public void handleThought(PacketVillagerThought msg) {
/* 1562 */     ParticleThought part = new ParticleThought(this.world, Minecraft.getMinecraft().getTextureManager(), (Entity)this, msg.getScale(), msg.getThought().getTex());
/* 1563 */     (Minecraft.getMinecraft()).effectRenderer.addEffect((Particle)part);
/*      */   }
/*      */   
/*      */   public boolean processInteract(EntityPlayer player, EnumHand hand) {
/* 1567 */     ItemStack itemStack = player.getHeldItem(hand);
/*      */     
/* 1569 */     if (itemStack.getItem() == Items.NAME_TAG) {
/*      */       
/* 1571 */       itemStack.interactWithEntity(player, (EntityLivingBase)this, hand);
/* 1572 */       return true;
/*      */     } 
/* 1574 */     if (itemStack.getItem() instanceof ItemProfessionToken && hasVillage()) {
/* 1575 */       ItemProfessionToken token = (ItemProfessionToken)itemStack.getItem();
/* 1576 */       if (canConvertProfession(token.getProfessionType()) && (ModItems.isItemVillageBound(itemStack, getVillage()) || !ModItems.isItemVillageBound(itemStack)) && 
/* 1577 */         !this.world.isRemote) {
/* 1578 */         itemStack.shrink(1);
/* 1579 */         EntityVillagerTek villager = token.createVillager(this.world, this);
/* 1580 */         if (villager != null) {
/* 1581 */           villager.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/* 1582 */           villager.onInitialSpawn(this.world.getDifficultyForLocation(getPosition()), (IEntityLivingData)null);
/* 1583 */           villager.cloneFrom(this);
/*      */           
/* 1585 */           this.world.spawnEntity((Entity)villager);
/*      */           
/* 1587 */           player.playSound(SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 1.0F, 1.0F);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1597 */           return true;
/*      */         } 
/*      */         
/* 1600 */         return false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1608 */     if (!this.world.isRemote) {
/*      */       
/* 1610 */       player.openGui(TekVillager.instance, 0, this.world, getEntityId(), 0, 0);
/* 1611 */       getNavigator().clearPath();
/*      */     } 
/*      */     
/* 1614 */     if (player.isSneaking()) {
/* 1615 */       debugSpam();
/*      */     }
/*      */     
/* 1618 */     return true;
/*      */   }
/*      */   
/*      */   protected void debugSpam() {
/* 1622 */     debugOut("+ + + + + + + + + + + + + +");
/* 1623 */     debugOut("Debug for " + getDebugName());
/* 1624 */     getInventory().debugSpam();
/* 1625 */     for (EntityAITasks.EntityAITaskEntry task : this.tasks.taskEntries) {
/* 1626 */       if (task.using) {
/* 1627 */         debugOut("    Active Task: " + task.action.getClass().getSimpleName());
/*      */       }
/*      */     } 
/* 1630 */     debugOut("Hunger: " + getHunger());
/* 1631 */     debugOut("Happy: " + getHappy());
/* 1632 */     debugOut("Health: " + getHealth());
/* 1633 */     debugOut("Intelligence: " + getIntelligence());
/*      */     
/* 1635 */     for (ProfessionType pt : ProfessionType.values()) {
/* 1636 */       if (getBaseSkill(pt) > 0) {
/* 1637 */         debugOut("     " + pt.name + ": " + getBaseSkill(pt));
/*      */       }
/*      */     } 
/* 1640 */     debugOut("^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^");
/*      */   }
/*      */   
/*      */   protected void registerAIFilter(String filterName, DataParameter<Boolean> param) {
/* 1644 */     if (this.aiFilters.put(filterName, param) != null) {
/* 1645 */       debugOut("ERROR: registerAIFilter( " + filterName + " ).  Double registration");
/*      */     }
/* 1647 */     this.dataManager.register(param, Boolean.valueOf(true));
/*      */   }
/*      */   
/*      */   protected void removeAIFilter(String filterName) {
/* 1651 */     this.aiFilters.remove(filterName);
/*      */   }
/*      */   
/*      */   public List<String> getAIFilters() {
/* 1655 */     return new ArrayList<>(this.aiFilters.keySet());
/*      */   }
/*      */   
/*      */   public boolean isAIFilterEnabled(String filterName) {
/* 1659 */     DataParameter<Boolean> param = this.aiFilters.get(filterName);
/* 1660 */     if (param != null) {
/* 1661 */       boolean result = ((Boolean)this.dataManager.get(param)).booleanValue();
/* 1662 */       return result;
/*      */     } 
/*      */     
/* 1665 */     debugOut("ERROR: (isAIFilterEnabled) AI Filter " + filterName + " does not exist!");
/* 1666 */     debugOut("ERROR: (isAIFilterEnabled) AI Filter " + filterName + " does not exist!");
/* 1667 */     debugOut("ERROR: (isAIFilterEnabled) AI Filter " + filterName + " does not exist!");
/* 1668 */     debugOut("ERROR: (isAIFilterEnabled) AI Filter " + filterName + " does not exist!");
/* 1669 */     debugOut("ERROR: (isAIFilterEnabled) AI Filter " + filterName + " does not exist!");
/* 1670 */     return true;
/*      */   }
/*      */   
/*      */   public void setAIFilter(String filterName, boolean enabled) {
/* 1674 */     DataParameter<Boolean> param = this.aiFilters.get(filterName);
/* 1675 */     if (param != null) {
/* 1676 */       debugOut("AI Filer " + filterName + " -> " + enabled);
/* 1677 */       this.dataManager.set(param, Boolean.valueOf(enabled));
/*      */     } else {
/*      */       
/* 1680 */       debugOut("ERROR: (setAIFilter) AI Filter " + filterName + " does not exist!");
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean canConvertProfession(ProfessionType pt) {
/* 1685 */     return (pt != this.professionType && pt != ProfessionType.CAPTAIN);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRevengeTarget(@Nullable EntityLivingBase target) {
/* 1690 */     if (target instanceof EntityPlayer) {
/*      */       return;
/*      */     }
/* 1693 */     super.setRevengeTarget(target);
/*      */     
/* 1695 */     if (hasVillage() && target != null) {
/* 1696 */       getVillage().addOrRenewEnemy(target, 5);
/*      */       
/* 1698 */       if (isEntityAlive()) {
/* 1699 */         this.world.setEntityState((Entity)this, (byte)14);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void notifyDataManagerChange(DataParameter<?> key) {
/* 1706 */     super.notifyDataManagerChange(key);
/*      */     
/* 1708 */     if (MOVEMENT_MODE.equals(key))
/*      */     {
/* 1710 */       if (isWalking()) {
/* 1711 */         startWalking();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void equipActionItem(ItemStack toolItem) {
/* 1717 */     this.dataManager.set(ACTION_ITEM, toolItem.copy());
/*      */   }
/*      */   
/*      */   public ItemStack getActionItem() {
/* 1721 */     return (ItemStack)this.dataManager.get(ACTION_ITEM);
/*      */   }
/*      */ 
/*      */   
/*      */   public void unequipActionItem() {
/* 1726 */     this.dataManager.set(ACTION_ITEM, ItemStack.EMPTY);
/*      */   }
/*      */   
/*      */   public void unequipActionItem(ItemStack actionItem) {
/* 1730 */     if (actionItem != null && actionItem.getItem() == getActionItem().getItem())
/* 1731 */       this.dataManager.set(ACTION_ITEM, ItemStack.EMPTY); 
/*      */   }
/*      */   
/*      */   public Predicate<Entity> isEnemy() {
/* 1735 */     return e -> (isHostile().test(e) || (e instanceof EntityLivingBase && EntityNecromancer.isMinion((EntityLivingBase)e)));
/*      */   }
/*      */   
/*      */   public Predicate<Entity> isHostile() {
/* 1739 */     return e -> ((e instanceof net.minecraft.entity.monster.EntityZombie && !(e instanceof net.minecraft.entity.monster.EntityPigZombie)) || e instanceof net.minecraft.entity.monster.EntityWitherSkeleton || e instanceof net.minecraft.entity.monster.EntityEvoker || e instanceof net.minecraft.entity.monster.EntityVex || e instanceof net.minecraft.entity.monster.EntityVindicator || e instanceof EntityNecromancer);
/*      */   }
/*      */   
/*      */   public boolean isFleeFrom(Entity e) {
/* 1743 */     return isHostile().test(e);
/*      */   }
/*      */   
/*      */   public void addRecentEat(Item item) {
/* 1747 */     this.recentEats.add(Integer.valueOf(Item.getIdFromItem(item)));
/* 1748 */     while (this.recentEats.size() > 5) {
/* 1749 */       this.recentEats.remove();
/*      */     }
/*      */   }
/*      */   
/*      */   public int getRecentEatModifier(Item item) {
/* 1754 */     int itemId = Item.getIdFromItem(item);
/* 1755 */     int eatCount = MathHelper.clamp((int)this.recentEats.stream().filter(i -> (i.intValue() == itemId)).count(), 0, 5);
/* 1756 */     return recentEatPenalties[eatCount];
/*      */   }
/*      */   public static Function<ItemStack, Integer> foodBetterThan(EntityVillagerTek v, int foodValue) {
/* 1759 */     return p -> {
/*      */         int val = ((Integer)foodValue(v).apply(p)).intValue();
/*      */         return Integer.valueOf((val > foodValue) ? val : -1);
/*      */       };
/*      */   }
/*      */   
/*      */   public static Function<ItemStack, Integer> foodValue(EntityVillagerTek v) {
/* 1766 */     return p -> Integer.valueOf((int)((ModItems.isTaggedItem(p, ItemTagType.VILLAGER) ? 1.0F : 0.5F) * ((Integer)foodItemValue(v).apply(p.getItem())).intValue()));
/*      */   }
/*      */   
/*      */   public static Function<Item, Integer> foodItemValue(EntityVillagerTek v) {
/* 1770 */     return i -> Integer.valueOf(EntityAIEatFood.getFoodScore(i, v));
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
/*      */   public boolean isVillageMember(Village v) {
/* 1788 */     return (getVillage() == v && getBedPos() != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMale() {
/* 1796 */     return (getUniqueID().getLeastSignificantBits() % 2L == 0L);
/*      */   }
/*      */   
/*      */   public Predicate<ItemStack> isHarvestItem() {
/* 1800 */     return p -> false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void equipBestGear() {}
/*      */ 
/*      */   
/*      */   public void equipBestGear(EntityEquipmentSlot slot, Function<ItemStack, Integer> bestFunc) {
/* 1809 */     ItemStack bestItem = getItemStackFromSlot(slot);
/* 1810 */     int bestScore = ((Integer)bestFunc.apply(bestItem)).intValue();
/* 1811 */     int swapSlot = -1;
/*      */     
/* 1813 */     for (int i = 0; i < getInventory().getSizeInventory(); i++) {
/* 1814 */       ItemStack itemStack = getInventory().getStackInSlot(i);
/* 1815 */       int thisScore = ((Integer)bestFunc.apply(itemStack)).intValue();
/* 1816 */       if (thisScore > bestScore) {
/* 1817 */         bestScore = thisScore;
/* 1818 */         bestItem = itemStack;
/* 1819 */         swapSlot = i;
/*      */       } 
/*      */     } 
/*      */     
/* 1823 */     if (swapSlot >= 0) {
/* 1824 */       ItemStack oldGear = getItemStackFromSlot(slot);
/* 1825 */       setItemStackToSlot(slot, bestItem);
/* 1826 */       getInventory().setInventorySlotContents(swapSlot, oldGear);
/* 1827 */       debugOut("Equipping new gear: " + bestItem + "   Removing old gear: " + oldGear);
/*      */     }
/* 1829 */     else if (bestScore == -1) {
/* 1830 */       ItemStack oldGear = getItemStackFromSlot(slot);
/* 1831 */       if (!oldGear.isEmpty()) {
/* 1832 */         setItemStackToSlot(slot, ItemStack.EMPTY);
/* 1833 */         getInventory().addItem(oldGear);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void cloneFrom(EntityVillagerTek source) {
/* 1840 */     setCustomNameTag(source.getCustomNameTag());
/*      */ 
/*      */ 
/*      */     
/* 1844 */     while (source.isMale() != isMale()) {
/* 1845 */       setUniqueId(UUID.randomUUID());
/*      */     }
/*      */     
/* 1848 */     setHappy(source.getHappy());
/* 1849 */     setIntelligence(source.getIntelligence());
/* 1850 */     setHunger(source.getHunger());
/*      */     
/* 1852 */     this.villagerInventory.mergeItems(source.villagerInventory);
/* 1853 */     this.bedPos = source.bedPos;
/* 1854 */     this.homeFrame = source.homeFrame;
/* 1855 */     this.daysAlive = source.daysAlive;
/* 1856 */     this.dayCheckTime = source.dayCheckTime;
/*      */ 
/*      */     
/* 1859 */     source.applySkillsTo(this);
/*      */     
/* 1861 */     getInventory().cloneFrom(source.getInventory());
/*      */ 
/*      */ 
/*      */     
/* 1865 */     source.setDead();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applySkillsTo(EntityVillagerTek target) {
/* 1870 */     for (ProfessionType pt : SKILLS.keySet()) {
/* 1871 */       int skill = getBaseSkill(pt);
/* 1872 */       if (skill > target.getBaseSkill(pt) && pt.canCopy) {
/* 1873 */         target.setSkill(pt, skill);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeEntityToNBT(NBTTagCompound compound) {
/* 1880 */     super.writeEntityToNBT(compound);
/*      */     
/* 1882 */     compound.setInteger("happy", getHappy());
/* 1883 */     compound.setInteger("hunger", getHunger());
/* 1884 */     compound.setInteger("intelligence", getIntelligence());
/* 1885 */     compound.setInteger("daysAlive", this.daysAlive);
/* 1886 */     compound.setInteger("dayCheckTime", this.dayCheckTime);
/*      */     
/* 1888 */     for (ProfessionType pt : SKILLS.keySet()) {
/* 1889 */       if (pt.canCopy) {
/* 1890 */         compound.setInteger(pt.name, getBaseSkill(pt));
/*      */       }
/*      */     } 
/* 1893 */     for (String filterName : this.aiFilters.keySet()) {
/* 1894 */       compound.setBoolean("ai_" + filterName, isAIFilterEnabled(filterName));
/*      */     }
/*      */     
/* 1897 */     compound.setBoolean("hasHome", (this.homeFrame != null));
/* 1898 */     if (this.homeFrame != null) {
/* 1899 */       writeBlockPosNBT(compound, "homeFrame", this.homeFrame);
/*      */     }
/* 1901 */     compound.setIntArray("recentEats", this.recentEats.stream().mapToInt(i -> i.intValue()).toArray());
/* 1902 */     this.villagerInventory.writeNBT(compound);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void readEntityFromNBT(NBTTagCompound compound) {
/* 1908 */     super.readEntityFromNBT(compound);
/*      */     
/* 1910 */     setHappy(compound.getInteger("happy"));
/* 1911 */     setHunger(compound.getInteger("hunger"));
/* 1912 */     setIntelligence(compound.getInteger("intelligence"));
/* 1913 */     this.daysAlive = compound.getInteger("daysAlive");
/* 1914 */     this.dayCheckTime = compound.getInteger("dayCheckTime");
/*      */     
/* 1916 */     for (ProfessionType pt : SKILLS.keySet()) {
/* 1917 */       if (pt.canCopy) {
/* 1918 */         setSkill(pt, compound.getInteger(pt.name));
/*      */       }
/*      */     } 
/* 1921 */     for (String filterName : this.aiFilters.keySet()) {
/* 1922 */       String key = "ai_" + filterName;
/* 1923 */       if (compound.hasKey(key)) {
/* 1924 */         setAIFilter(filterName, compound.getBoolean("ai_" + filterName)); continue;
/*      */       } 
/* 1926 */       setAIFilter(filterName, true);
/*      */     } 
/*      */     
/* 1929 */     boolean hasHome = compound.getBoolean("hasHome");
/* 1930 */     if (hasHome) {
/* 1931 */       this.homeFrame = readBlockPosNBT(compound, "homeFrame");
/*      */     }
/* 1933 */     this.recentEats.clear();
/* 1934 */     int[] eats = compound.getIntArray("recentEats");
/* 1935 */     Arrays.stream(eats).forEach(i -> this.recentEats.add(Integer.valueOf(i)));
/*      */     
/* 1937 */     setNoGravity(false);
/*      */     
/* 1939 */     this.villagerInventory.readNBT(compound);
/* 1940 */     getDesireSet().forceUpdate();
/*      */   }
/*      */ 
/*      */   
/*      */   protected BlockPos readBlockPosNBT(NBTTagCompound compound, String key) {
/* 1945 */     NBTTagList nbttaglist = compound.getTagList(key, 6);
/* 1946 */     return new BlockPos(nbttaglist.getDoubleAt(0), nbttaglist.getDoubleAt(1), nbttaglist.getDoubleAt(2));
/*      */   }
/*      */   
/*      */   protected void writeBlockPosNBT(NBTTagCompound compound, String key, BlockPos val) {
/* 1950 */     compound.setTag(key, (NBTBase)newDoubleNBTList(new double[] { val.getX(), val.getY(), val.getZ() }));
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityVillagerTek.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */