/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.particle.Particle;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
/*     */ import net.minecraft.entity.monster.AbstractSkeleton;
/*     */ import net.minecraft.entity.monster.EntityHusk;
/*     */ import net.minecraft.entity.monster.EntityMob;
/*     */ import net.minecraft.entity.monster.EntityWitherSkeleton;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.SoundCategory;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.caps.IVillageData;
/*     */ import net.tangotek.tektopia.client.ParticleDarkness;
/*     */ import net.tangotek.tektopia.client.ParticleSkull;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIFollowLeader;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ public class EntityNecromancer extends EntityVillageNavigator implements IMob {
/*  45 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityNecromancer.class);
/*  46 */   private static final DataParameter<Integer> SPELL_TARGET = EntityDataManager.createKey(EntityNecromancer.class, DataSerializers.VARINT);
/*  47 */   private static final DataParameter<Integer> LEVEL = EntityDataManager.createKey(EntityNecromancer.class, DataSerializers.VARINT);
/*  48 */   private static final DataParameter<List<Integer>> MINIONS = EntityDataManager.createKey(EntityNecromancer.class, TekDataSerializers.INT_LIST);
/*     */   
/*     */   static {
/*  51 */     animHandler.addAnim("tektopia", "necro_walk", "necromancer", true);
/*  52 */     animHandler.addAnim("tektopia", "necro_idle", "necromancer", true);
/*  53 */     animHandler.addAnim("tektopia", "necro_summon", "necromancer", false);
/*  54 */     animHandler.addAnim("tektopia", "necro_siphon", "necromancer", false);
/*  55 */     animHandler.addAnim("tektopia", "necro_cast_forward", "necromancer", false);
/*     */   }
/*     */   
/*  58 */   private final int SKULL_COOLDOWN_NORMAL = 100;
/*  59 */   private final int SKULL_COOLDOWN_ATTACKED = 40;
/*     */ 
/*     */   
/*  62 */   private List<EntitySpiritSkull> skulls = new ArrayList<>();
/*  63 */   private int lastSkullGained = 0;
/*  64 */   private int skullCooldown = 100;
/*  65 */   private final int MAX_LEVEL = 5;
/*  66 */   private int maxSkulls = 1;
/*     */   private boolean isAngry = false;
/*     */   private BlockPos firstCheck;
/*     */   private boolean villagerDied = false;
/*     */   
/*     */   public EntityNecromancer(World worldIn) {
/*  72 */     super(worldIn, VillagerRole.ENEMY.value | VillagerRole.VISITOR.value);
/*  73 */     setSize(0.6F, 1.95F);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     setRotation(0.0F, 0.0F);
/*  80 */     if (this.world.isRemote) {
/*  81 */       addAnimationTriggerRange("tektopia:necro_summon", 102, 108, () -> skullParticles((Entity)this, 4));
/*     */ 
/*     */       
/*  84 */       addAnimationTrigger("tektopia:necro_summon", 1, () -> skullParticles((Entity)this, 1));
/*     */ 
/*     */       
/*  87 */       addAnimationTrigger("tektopia:necro_summon", 20, () -> skullParticles((Entity)this, 1));
/*     */ 
/*     */       
/*  90 */       addAnimationTrigger("tektopia:necro_summon", 40, () -> skullParticles((Entity)this, 1));
/*     */ 
/*     */       
/*  93 */       addAnimationTrigger("tektopia:necro_summon", 60, () -> skullParticles((Entity)this, 1));
/*     */ 
/*     */       
/*  96 */       addAnimationTrigger("tektopia:necro_summon", 80, () -> skullParticles((Entity)this, 1));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevel(int powerLevel) {
/* 104 */     int level = Math.max(1, Math.min(powerLevel, 5));
/* 105 */     this.dataManager.set(LEVEL, Integer.valueOf(level));
/* 106 */     this.maxSkulls = level;
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 110 */     return ((Integer)this.dataManager.get(LEVEL)).intValue();
/*     */   }
/*     */   
/*     */   protected void initEntityAI() {
/* 114 */     this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/* 115 */     this.tasks.addTask(9, (EntityAIBase)new EntityAISoulLink(this));
/* 116 */     this.tasks.addTask(9, (EntityAIBase)new EntityAIDeathCloud(this));
/* 117 */     this.tasks.addTask(9, (EntityAIBase)new EntityAISummonUndead(this, 1));
/* 118 */     this.tasks.addTask(10, (EntityAIBase)new EntityAINecroMove(this));
/*     */ 
/*     */ 
/*     */     
/* 122 */     this.targetTasks.addTask(2, (EntityAIBase)(new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
/* 123 */     this.targetTasks.addTask(3, (EntityAIBase)(new EntityAINearestAttackableTarget(this, EntityVillagerTek.class, false)).setUnseenMemoryTicks(300));
/* 124 */     this.targetTasks.addTask(5, (EntityAIBase)(new EntityAINearestAttackableTarget(this, EntityVillager.class, false)).setUnseenMemoryTicks(300));
/* 125 */     this.targetTasks.addTask(5, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setupServerJobs() {
/* 130 */     super.setupServerJobs();
/*     */     
/* 132 */     addJob(new TickJob(40, 50, true, () -> convertNearbyZombies()));
/* 133 */     addJob(new TickJob(75, 50, true, () -> convertNearbySkeletons()));
/*     */ 
/*     */     
/* 136 */     addJob(new TickJob(3, 3, true, () -> decaySurroundings()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasVillagerDied() {
/* 141 */     return this.villagerDied;
/*     */   }
/*     */ 
/*     */   
/*     */   public void notifyVillagerDeath() {
/* 146 */     this.villagerDied = true;
/*     */   }
/*     */   
/*     */   private void prepStuck() {
/* 150 */     this.firstCheck = getPosition();
/*     */   }
/*     */   
/*     */   private void checkStuck() {
/* 154 */     if (hasVillage() && this.firstCheck.distanceSq((Vec3i)getPosition()) < 2.0D) {
/* 155 */       IVillageData vd = this.village.getTownData();
/* 156 */       if (vd != null) {
/* 157 */         vd.setNomadsCheckedToday(true);
/* 158 */         vd.setMerchantCheckedToday(true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldDecayBlock(BlockPos bp) {
/* 167 */     Block testBlock = this.world.getBlockState(bp).getBlock();
/* 168 */     if (testBlock instanceof net.minecraft.block.BlockBush) {
/* 169 */       return true;
/*     */     }
/* 171 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void decaySurroundings() {
/* 176 */     for (int i = 0; i < 20; i++) {
/* 177 */       BlockPos testPos = new BlockPos(this.posX + getRNG().nextGaussian() * 8.0D, this.posY + getRNG().nextGaussian() * 0.6D, this.posZ + getRNG().nextGaussian() * 8.0D);
/* 178 */       if (shouldDecayBlock(testPos)) {
/* 179 */         this.world.setBlockToAir(testPos);
/*     */         return;
/*     */       } 
/*     */     } 
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
/*     */   protected void applyEntityAttributes() {
/* 201 */     super.applyEntityAttributes();
/* 202 */     getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
/* 203 */     getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
/*     */     
/* 205 */     this.dataManager.set(MINIONS, new ArrayList());
/*     */   }
/*     */   
/*     */   protected void entityInit() {
/* 209 */     this.dataManager.register(MINIONS, new ArrayList());
/* 210 */     this.dataManager.register(SPELL_TARGET, Integer.valueOf(0));
/* 211 */     this.dataManager.register(LEVEL, Integer.valueOf(1));
/* 212 */     super.entityInit();
/*     */   }
/*     */   
/*     */   public boolean isSpellcasting() {
/* 216 */     int entityId = ((Integer)this.dataManager.get(SPELL_TARGET)).intValue();
/* 217 */     return (entityId > 0);
/*     */   }
/*     */   
/*     */   public Entity getSpellTargetEntity() {
/* 221 */     int entityId = ((Integer)this.dataManager.get(SPELL_TARGET)).intValue();
/* 222 */     if (entityId > 0) {
/* 223 */       return this.world.getEntityByID(entityId);
/*     */     }
/* 225 */     return null;
/*     */   }
/*     */   
/*     */   public void setSpellTarget(Entity entity) {
/* 229 */     if (entity == null) {
/* 230 */       this.dataManager.set(SPELL_TARGET, Integer.valueOf(0));
/*     */     } else {
/* 232 */       this.dataManager.set(SPELL_TARGET, Integer.valueOf(entity.getEntityId()));
/*     */     } 
/*     */   }
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void skullParticles(Entity entity, int count) {
/* 237 */     for (int i = 0; i < count; i++) {
/* 238 */       Random rand = entity.world.rand;
/* 239 */       double motionY = Math.random() * 0.03D + 0.01D;
/* 240 */       Vec3d pos = new Vec3d(entity.posX + rand.nextGaussian() * 0.5D, entity.posY + rand.nextFloat(), entity.posZ + rand.nextGaussian() * 0.5D);
/* 241 */       ParticleSkull part = new ParticleSkull(entity.world, Minecraft.getMinecraft().getTextureManager(), pos, motionY);
/* 242 */       part.radius = rand.nextGaussian() * 0.1D;
/* 243 */       part.radiusGrow = 0.005D;
/* 244 */       part.torque = Math.random() * 0.04D - 0.02D;
/* 245 */       part.lifeTime = rand.nextInt(15) + 10;
/* 246 */       part.onUpdate();
/* 247 */       (Minecraft.getMinecraft()).effectRenderer.addEffect((Particle)part);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void minionsDefendMe(EntityLivingBase enemy) {
/* 252 */     List<EntityMob> minions = getMinions();
/* 253 */     for (EntityMob minion : minions) {
/* 254 */       minion.setAttackTarget(enemy);
/*     */     }
/*     */   }
/*     */   
/*     */   private void cleanMinions() {
/* 259 */     if (!this.world.isRemote) {
/* 260 */       List<Integer> idList = (List<Integer>)this.dataManager.get(MINIONS);
/* 261 */       if (idList.removeIf(entityId -> !isValidMinion(this.world.getEntityByID(entityId.intValue())))) {
/* 262 */         debugOut("Minion(s) removed. Now " + idList.size());
/* 263 */         this.dataManager.set(MINIONS, idList);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addMinion(EntityMob mob) {
/* 269 */     List<Integer> idList = (List<Integer>)this.dataManager.get(MINIONS);
/* 270 */     idList.add(Integer.valueOf(mob.getEntityId()));
/* 271 */     this.dataManager.set(MINIONS, idList);
/* 272 */     debugOut("Minion " + mob.getEntityId() + " added to list. Now " + idList.size());
/*     */   }
/*     */   
/*     */   private boolean isValidMinion(Entity e) {
/* 276 */     return (e != null && e instanceof EntityMob && e.isEntityAlive());
/*     */   }
/*     */   
/*     */   public List<EntityMob> getMinions() {
/* 280 */     List<Integer> idList = (List<Integer>)this.dataManager.get(MINIONS);
/* 281 */     List<EntityMob> mobList = new ArrayList<>();
/* 282 */     idList.forEach(id -> {
/*     */           EntityMob mob = (EntityMob)this.world.getEntityByID(id.intValue());
/*     */           if (isValidMinion((Entity)mob))
/*     */             mobList.add(mob); 
/*     */         });
/* 287 */     return mobList;
/*     */   }
/*     */   
/*     */   public void onUpdate() {
/* 291 */     super.onUpdate();
/*     */     
/* 293 */     if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
/* 294 */       setDead();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAIMoveSpeed() {
/* 300 */     return 0.22F;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 304 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isAITick() {
/* 308 */     return (super.isAITick() || this.hurtTime > 0);
/*     */   }
/*     */   
/*     */   public int getMaxSummons() {
/* 312 */     if (this.villagerDied) {
/* 313 */       return 0;
/*     */     }
/* 315 */     return 4 + getLevel();
/*     */   }
/*     */   
/*     */   public boolean isReadyForSkull() {
/* 319 */     if (this.skulls.isEmpty()) {
/* 320 */       return true;
/*     */     }
/* 322 */     if (this.skulls.size() < this.maxSkulls && !this.villagerDied && getRNG().nextInt(2) == 0 && this.ticksExisted - this.skullCooldown > this.lastSkullGained) {
/* 323 */       return true;
/*     */     }
/*     */     
/* 326 */     return false;
/*     */   }
/*     */   
/*     */   private void convertNearbyZombies() {
/* 330 */     if (!this.villagerDied) {
/* 331 */       List<EntityZombie> zombies = this.world.getEntitiesWithinAABB(EntityZombie.class, getEntityBoundingBox().grow((15 + getLevel() * 4)));
/* 332 */       zombies.forEach(z -> convertZombie(z));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void convertNearbySkeletons() {
/* 337 */     if (!this.villagerDied) {
/* 338 */       List<AbstractSkeleton> skels = this.world.getEntitiesWithinAABB(AbstractSkeleton.class, getEntityBoundingBox().grow((15 + getLevel() * 4)));
/* 339 */       skels.forEach(s -> convertSkeleton(s));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isMinion(EntityLivingBase mob) {
/* 344 */     return mob.getEntityData().hasUniqueId("master");
/*     */   }
/*     */   
/*     */   public static void makeMinion(EntityLivingBase minion, EntityNecromancer necro) {
/* 348 */     minion.getEntityData().setUniqueId("master", necro.getUniqueID());
/*     */   }
/*     */   
/*     */   public void convertZombie(EntityZombie z) {
/* 352 */     if (!isMinion((EntityLivingBase)z)) {
/* 353 */       z.tasks.taskEntries.removeIf(entry -> (entry.action.getClass() == EntityAIWanderAvoidWater.class));
/* 354 */       z.tasks.taskEntries.removeIf(entry -> (entry.action.getClass() == EntityAIMoveTowardsRestriction.class));
/* 355 */       z.tasks.taskEntries.removeIf(entry -> (entry.action.getClass() == EntityAIFollowLeader.class));
/* 356 */       z.tasks.addTask(5, (EntityAIBase)new EntityAIFollowLeader((EntityCreature)z, this, 8, 1.0D));
/* 357 */       z.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
/* 358 */       makeMinion((EntityLivingBase)z, this);
/*     */       
/* 360 */       debugOut("Converted nearby zombie | " + z.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void convertSkeleton(AbstractSkeleton skel) {
/* 367 */     if (!isMinion((EntityLivingBase)skel)) {
/* 368 */       skel.tasks.taskEntries.removeIf(entry -> (entry.action.getClass() == EntityAIWanderAvoidWater.class));
/* 369 */       skel.tasks.taskEntries.removeIf(entry -> (entry.action.getClass() == EntityAIFollowLeader.class));
/* 370 */       skel.tasks.addTask(5, (EntityAIBase)new EntityAIFollowLeader((EntityCreature)skel, this, 8, 1.0D));
/* 371 */       skel.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
/* 372 */       makeMinion((EntityLivingBase)skel, this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityWitherSkeleton createWitherSkeleton(BlockPos summonPos) {
/* 380 */     EntityWitherSkeleton skel = new EntityWitherSkeleton(this.world);
/*     */     
/* 382 */     convertSkeleton((AbstractSkeleton)skel);
/* 383 */     skel.setLocationAndAngles(summonPos.getX() + 0.5D, summonPos.getY(), summonPos.getZ() + 0.5D, this.rotationYaw, this.rotationPitch);
/* 384 */     skel.onInitialSpawn(this.world.getDifficultyForLocation(summonPos), null);
/* 385 */     skel.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 15));
/*     */     
/* 387 */     return skel;
/*     */   }
/*     */   
/*     */   public EntityZombie createZombie(BlockPos summonPos) {
/*     */     EntityHusk entityHusk;
/* 392 */     getVillage(); if (Village.isTimeOfDay(this.world, 13000L, 17000L)) {
/* 393 */       EntityZombie zombie = new EntityZombie(this.world);
/*     */     } else {
/* 395 */       entityHusk = new EntityHusk(this.world);
/*     */     } 
/* 397 */     convertZombie((EntityZombie)entityHusk);
/* 398 */     entityHusk.setLocationAndAngles(summonPos.getX() + 0.5D, summonPos.getY(), summonPos.getZ() + 0.5D, this.rotationYaw, this.rotationPitch);
/* 399 */     entityHusk.onInitialSpawn(this.world.getDifficultyForLocation(summonPos), null);
/* 400 */     entityHusk.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 15));
/*     */     
/* 402 */     return (EntityZombie)entityHusk;
/*     */   }
/*     */   
/*     */   public boolean isCreatureSkulled(EntityCreature creature) {
/* 406 */     this.skulls.removeIf(s -> (!s.isEntityAlive() || s.getDistanceSq((Entity)this) > 625.0D));
/* 407 */     return this.skulls.stream().anyMatch(s -> {
/*     */           EntityCreature c = s.getSkullCreature();
/* 409 */           return (c != null && c.equals(creature));
/*     */         });
/*     */   }
/*     */   
/*     */   public void addSkull(EntitySpiritSkull skull) {
/* 414 */     this.skulls.add(skull);
/* 415 */     this.lastSkullGained = this.ticksExisted;
/* 416 */     this.skullCooldown = getLevelCooldown(100);
/* 417 */     skull.setNecro(this);
/*     */     
/* 419 */     playSound(ModSoundEvents.deathFullSkulls, 1.3F, getRNG().nextFloat() * 0.4F + 0.8F);
/*     */   }
/*     */   
/*     */   public void releaseSkull(EntitySpiritSkull skull) {
/* 423 */     this.skulls.remove(skull);
/* 424 */     skull.setDead();
/* 425 */     skull.setNecro((EntityNecromancer)null);
/* 426 */     playSound(ModSoundEvents.deathSkullLeave);
/* 427 */     this.isAngry = true;
/*     */   }
/*     */   
/*     */   public boolean isAngry() {
/* 431 */     return this.isAngry;
/*     */   }
/*     */   
/*     */   public void onLivingUpdate() {
/* 435 */     super.onLivingUpdate();
/*     */     
/* 437 */     if (isWorldRemote()) {
/* 438 */       updateIdle(isWalking());
/*     */       
/* 440 */       spawnCloud((Entity)this, isWalking() ? 30 : 20);
/*     */       
/* 442 */       if (isSpellcasting()) {
/* 443 */         skullParticles((Entity)this, getLevel() * 2);
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 457 */       cleanMinions();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void spawnSmoke(Entity entity, float spread, int count) {
/* 469 */     for (int i = 0; i < count; i++) {
/* 470 */       double xOffset = entity.world.rand.nextGaussian() * spread;
/* 471 */       double zOffset = entity.world.rand.nextGaussian() * spread;
/*     */       
/* 473 */       Vec3d pos = new Vec3d(entity.posX + xOffset, entity.posY + Math.min(entity.world.rand.nextFloat(), entity.world.rand.nextFloat()) * 0.5D * entity.height, entity.posZ + zOffset);
/* 474 */       entity.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.x, pos.y, pos.z, 0.0D, 0.08D, 0.0D, new int[0]);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void spawnCloud(Vec3d pos, int count) {
/* 480 */     for (int i = 0; i < count; i++) {
/* 481 */       double motionY = Math.random() * 0.03D + 0.01D;
/* 482 */       Vec3d newPos = new Vec3d(pos.x + this.world.rand.nextGaussian() * 0.5D, pos.y, pos.z + this.world.rand.nextGaussian() * 0.5D);
/* 483 */       ParticleDarkness part = new ParticleDarkness(this.world, Minecraft.getMinecraft().getTextureManager(), newPos, motionY);
/* 484 */       part.radius = this.world.rand.nextGaussian() * 0.3D;
/* 485 */       part.radiusGrow = 0.015D;
/* 486 */       part.torque = Math.random() * 0.04D - 0.02D;
/* 487 */       part.lifeTime = this.world.rand.nextInt(15) + 10;
/* 488 */       part.onUpdate();
/* 489 */       (Minecraft.getMinecraft()).effectRenderer.addEffect((Particle)part);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void spawnCloud(Entity entity, int count) {
/* 496 */     for (int i = 0; i < count; i++) {
/* 497 */       double motionY = Math.random() * 0.03D + 0.01D;
/* 498 */       Vec3d pos = new Vec3d(entity.posX, entity.posY + Math.min(this.world.rand.nextFloat(), this.world.rand.nextFloat()) * 0.5D * entity.height, entity.posZ);
/* 499 */       ParticleDarkness part = new ParticleDarkness(this.world, Minecraft.getMinecraft().getTextureManager(), pos, motionY);
/* 500 */       part.radius = this.world.rand.nextGaussian() * 0.3D;
/*     */       
/* 502 */       part.radiusGrow = 0.015D;
/* 503 */       part.torque = Math.random() * 0.04D - 0.02D;
/* 504 */       part.lifeTime = this.world.rand.nextInt(15) + 15;
/*     */       
/* 506 */       part.onUpdate();
/* 507 */       (Minecraft.getMinecraft()).effectRenderer.addEffect((Particle)part);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   protected void startWalking() {
/* 514 */     playClientAnimation("necro_walk");
/*     */   }
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   protected void stopWalking() {
/* 520 */     stopClientAnimation("necro_walk");
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   protected void updateIdle(boolean isWalking) {
/* 525 */     if (!isCasting() && !isWalking) {
/* 526 */       if (!getAnimationHandler().isAnimationActive("tektopia", "necro_idle", this)) {
/* 527 */         getAnimationHandler().startAnimation("tektopia", "necro_idle", this);
/*     */       }
/*     */     }
/* 530 */     else if (getAnimationHandler().isAnimationActive("tektopia", "necro_idle", this)) {
/* 531 */       getAnimationHandler().stopAnimation("tektopia", "necro_idle", this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isCasting() {
/* 536 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canDespawn() {
/* 541 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 546 */     boolean livingAttack = source.getTrueSource() instanceof EntityLivingBase;
/* 547 */     if (livingAttack) {
/* 548 */       minionsDefendMe((EntityLivingBase)source.getTrueSource());
/*     */     }
/*     */ 
/*     */     
/* 552 */     List<EntitySpiritSkull> nearbySkullls = this.world.getEntitiesWithinAABB(EntitySpiritSkull.class, getEntityBoundingBox().grow(5.0D));
/* 553 */     if (nearbySkullls.isEmpty() || source == DamageSource.OUT_OF_WORLD) {
/* 554 */       this.skullCooldown -= 20;
/* 555 */       return super.attackEntityFrom(source, amount);
/*     */     } 
/*     */     
/* 558 */     playSound(ModSoundEvents.deathShield, 1.2F + getRNG().nextFloat() * 0.4F, getRNG().nextFloat() * 0.2F + 0.9F);
/* 559 */     addPotionEffect(new PotionEffect(MobEffects.GLOWING, 20));
/* 560 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLevelCooldown(int baseValue) {
/* 585 */     return baseValue - (int)(baseValue * MathHelper.clampedLerp(0.0D, 0.6D, getLevel() / 5.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDeath(DamageSource cause) {
/* 590 */     getMinions().stream().forEach(m -> m.setHealth(0.0F));
/* 591 */     playSound(ModSoundEvents.necroDead);
/* 592 */     super.onDeath(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 597 */     dropItem(Items.EMERALD, getLevel() * 4);
/*     */   }
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void handleStatusUpdate(byte id) {
/* 603 */     super.handleStatusUpdate(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRevengeTarget(@Nullable EntityLivingBase target) {
/* 612 */     super.setRevengeTarget(target);
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
/*     */   public boolean processInteract(EntityPlayer player, EnumHand hand) {
/* 624 */     ItemStack itemStack = player.getHeldItem(hand);
/*     */ 
/*     */     
/* 627 */     if (itemStack.getItem() == ModItems.beer && 
/* 628 */       getLevel() < 5) {
/* 629 */       setLevel(getLevel() + 1);
/* 630 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 634 */     return super.processInteract(player, hand);
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundCategory getSoundCategory() {
/* 639 */     return SoundCategory.HOSTILE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound compound) {
/* 645 */     super.writeEntityToNBT(compound);
/* 646 */     compound.setInteger("level", getLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound compound) {
/* 651 */     super.readEntityFromNBT(compound);
/* 652 */     setLevel(compound.getInteger("level"));
/*     */   }
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/* 656 */     return animHandler;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityNecromancer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */