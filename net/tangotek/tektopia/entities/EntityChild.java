/*     */ package net.tangotek.tektopia.entities;
/*     */ 
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.DataSerializers;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.util.SoundEvent;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIGenericMove;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIPlayTag;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAISchoolAttend;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityChild
/*     */   extends EntityVillagerTek
/*     */ {
/*  30 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityChild.class);
/*     */   
/*  32 */   private static final DataParameter<Byte> VARIATION = EntityDataManager.createKey(EntityChild.class, DataSerializers.BYTE);
/*  33 */   private static final DataParameter<Boolean> PLAY_TAG = EntityDataManager.createKey(EntityChild.class, DataSerializers.BOOLEAN);
/*  34 */   private static final DataParameter<Boolean> ATTEND_SCHOOL = EntityDataManager.createKey(EntityChild.class, DataSerializers.BOOLEAN);
/*     */   
/*  36 */   protected EntityChild chasedBy = null;
/*     */   protected boolean attendedSchoolToday = false;
/*     */   private int daysForAdult;
/*     */   
/*     */   static {
/*  41 */     animHandler.addAnim("tektopia", "villager_skip", "child_m", true);
/*  42 */     animHandler.addAnim("tektopia", "villager_sit_raise", "child_m", false);
/*  43 */     EntityVillagerTek.setupAnimations(animHandler, "child_m");
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityChild(World worldIn) {
/*  48 */     super(worldIn, ProfessionType.CHILD, VillagerRole.VILLAGER.value);
/*  49 */     setSize(this.width * 0.5F, this.height * 0.5F);
/*     */   }
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
/*     */   @Nullable
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
/*  64 */     setVariation(Byte.valueOf((byte)this.rand.nextInt(2)));
/*  65 */     return super.onInitialSpawn(difficulty, livingdata);
/*     */   }
/*     */   
/*     */   protected void entityInit() {
/*  69 */     super.entityInit();
/*  70 */     this.dataManager.register(VARIATION, Byte.valueOf((byte)this.rand.nextInt(2)));
/*  71 */     registerAIFilter("play_tag", PLAY_TAG);
/*  72 */     registerAIFilter("attend_school", ATTEND_SCHOOL);
/*     */     
/*  74 */     removeAIFilter("visit_tavern");
/*     */   }
/*     */   
/*     */   protected void initEntityAI() {
/*  78 */     super.initEntityAI();
/*     */     
/*  80 */     this.daysForAdult = 4 + getRNG().nextInt(2);
/*     */     
/*  82 */     addTask(50, (EntityAIBase)new EntityAISchoolAttend(this, p -> (p.isWorkTime() && needsSchoolToday())));
/*  83 */     addTask(50, (EntityAIBase)new EntityAIPlayTag(this));
/*  84 */     addTask(50, (EntityAIBase)new EntityAIGenericMove(this, p -> (p.isWorkTime() && p.hasVillage() && p.getRNG().nextInt(2) == 0), v -> this.village.getLastVillagerPos(), EntityVillagerTek.MovementMode.SKIP, null, null));
/*  85 */     addTask(50, (EntityAIBase)new EntityAIGenericMove(this, p -> (p.isWorkTime() && p.hasVillage()), v -> this.village.getLastVillagerPos(), EntityVillagerTek.MovementMode.WALK, null, null));
/*     */   }
/*     */   
/*     */   protected void onNewDay() {
/*  89 */     super.onNewDay();
/*  90 */     checkGrowAdult();
/*     */   }
/*     */ 
/*     */   
/*     */   public void attachToVillage(Village v) {
/*  95 */     super.attachToVillage(v);
/*  96 */     this.sleepOffset = genOffset(400) - 1200;
/*     */   }
/*     */   
/*     */   protected void setupServerJobs() {
/* 100 */     super.setupServerJobs();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addTask(int priority, EntityAIBase task) {
/* 106 */     if (task instanceof net.tangotek.tektopia.entities.ai.EntityAIReadBook) {
/*     */       return;
/*     */     }
/* 109 */     super.addTask(priority, task);
/*     */   }
/*     */   
/*     */   protected void setVariation(Byte v) {
/* 113 */     this.dataManager.set(VARIATION, Byte.valueOf(v.byteValue()));
/*     */   }
/*     */   
/*     */   public Byte getVariation() {
/* 117 */     return Byte.valueOf(((Byte)this.dataManager.get(VARIATION)).byteValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onStartSleep(int sleepAxis) {
/* 122 */     super.onStartSleep(sleepAxis);
/* 123 */     this.attendedSchoolToday = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onStartSit(int sitAxis) {
/* 128 */     VillageStructure curStruct = getCurrentStructure();
/* 129 */     if (curStruct != null && curStruct.type == VillageStructureType.SCHOOL) {
/* 130 */       this.attendedSchoolToday = true;
/*     */     }
/*     */     
/* 133 */     super.onStartSit(sitAxis);
/*     */   }
/*     */   
/*     */   public double getSitOffset() {
/* 137 */     return 0.24D;
/*     */   }
/*     */   
/*     */   public boolean needsSchoolToday() {
/* 141 */     return !this.attendedSchoolToday;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChild() {
/* 147 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canConvertProfession(ProfessionType pt) {
/* 152 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 158 */     return 0.87F;
/*     */   }
/*     */   
/*     */   protected boolean wantsTavern() {
/* 162 */     return false;
/*     */   }
/*     */   
/*     */   public void onLivingUpdate() {
/* 166 */     super.onLivingUpdate();
/*     */   }
/*     */   
/*     */   protected boolean canVillagerPickupItem(ItemStack itemIn) {
/* 170 */     return super.canVillagerPickupItem(itemIn);
/*     */   }
/*     */   
/*     */   protected void cleanUpInventory() {
/* 174 */     super.cleanUpInventory();
/*     */   }
/*     */   
/*     */   protected void checkGrowAdult() {
/* 178 */     if (this.daysAlive >= this.daysForAdult) {
/* 179 */       EntityVillagerTek villager = new EntityNitwit(this.world);
/* 180 */       villager.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
/* 181 */       villager.onInitialSpawn(this.world.getDifficultyForLocation(getPosition()), (IEntityLivingData)null);
/* 182 */       villager.cloneFrom(this);
/* 183 */       this.world.spawnEntity((Entity)villager);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAIMoveSpeed() {
/* 190 */     return super.getAIMoveSpeed() * 0.75F;
/*     */   }
/*     */   
/*     */   public void setChasedBy(EntityChild child) {
/* 194 */     this.chasedBy = child;
/*     */   }
/*     */   
/*     */   public EntityChild getChasedBy() {
/* 198 */     return this.chasedBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFleeFrom(Entity e) {
/* 203 */     return (e == this.chasedBy || super.isFleeFrom(e));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playSound(SoundEvent soundEvent) {
/* 209 */     playSound(soundEvent, getRNG().nextFloat() * 0.4F + 0.8F, getRNG().nextFloat() * 0.4F + 1.1F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound compound) {
/* 215 */     super.writeEntityToNBT(compound);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound compound) {
/* 220 */     super.readEntityFromNBT(compound);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityChild.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */