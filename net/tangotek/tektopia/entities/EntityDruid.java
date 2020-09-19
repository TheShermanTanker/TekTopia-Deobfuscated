/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.google.common.base.Optional;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.DataSerializers;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.SoundCategory;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIEarthReform;
/*     */ 
/*     */ public class EntityDruid extends EntityVillagerTek {
/*  24 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityDruid.class);
/*     */   
/*  26 */   private static final DataParameter<Optional<BlockPos>> SPELL_EARTH_REFORM = EntityDataManager.createKey(EntityDruid.class, DataSerializers.OPTIONAL_BLOCK_POS);
/*  27 */   protected static final DataParameter<Optional<BlockPos>> SPELL_BLOCK = EntityDataManager.createKey(EntityDruid.class, DataSerializers.OPTIONAL_BLOCK_POS);
/*     */   
/*  29 */   private static final DataParameter<Boolean> CAST_EARTH_REFORM = EntityDataManager.createKey(EntityDruid.class, DataSerializers.BOOLEAN);
/*  30 */   private static final DataParameter<Boolean> CAST_GROWTH_CROPS = EntityDataManager.createKey(EntityDruid.class, DataSerializers.BOOLEAN);
/*  31 */   private static final DataParameter<Boolean> CAST_GROWTH_TREES = EntityDataManager.createKey(EntityDruid.class, DataSerializers.BOOLEAN);
/*     */   
/*  33 */   private static final int[] blockStateIds = new int[] {
/*  34 */       Block.getStateId(Blocks.DIRT.getDefaultState()), 
/*  35 */       Block.getStateId(Blocks.DIRT.getDefaultState()), 
/*  36 */       Block.getStateId(Blocks.STONE.getDefaultState()), 
/*  37 */       Block.getStateId(Blocks.COBBLESTONE.getDefaultState())
/*     */     };
/*     */   
/*  40 */   private int earthReformTicks = 0;
/*  41 */   private int growthTicks = 0;
/*     */   private BlockPos clientSpellBlock;
/*     */   private boolean growthFirst = true;
/*     */   
/*     */   static {
/*  46 */     animHandler.addAnim("tektopia", "villager_cast_forward", "druid_m", false);
/*  47 */     animHandler.addAnim("tektopia", "villager_cast_grow", "druid_m", false);
/*     */     
/*  49 */     EntityVillagerTek.setupAnimations(animHandler, "druid_m");
/*     */   }
/*     */   
/*     */   public EntityDruid(World worldIn) {
/*  53 */     super(worldIn, ProfessionType.DRUID, VillagerRole.VILLAGER.value);
/*     */     
/*  55 */     Runnable runner = () -> this.world.playSound(this.posX, this.posY, this.posZ, ModSoundEvents.earthBlast, SoundCategory.NEUTRAL, 1.0F, this.rand.nextFloat() * 0.2F + 0.9F, false);
/*     */     
/*  57 */     if (this.world.isRemote)
/*  58 */       addAnimationTrigger("tektopia:villager_cast_forward", 64, runner); 
/*     */   }
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/*  62 */     return animHandler;
/*     */   }
/*     */   
/*     */   protected void initEntityAI() {
/*  66 */     super.initEntityAI();
/*     */     
/*  68 */     this.growthFirst = getRNG().nextBoolean();
/*  69 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIEarthReform(this));
/*  70 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIGrowth(this, 6.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  75 */     super.entityInit();
/*  76 */     this.dataManager.register(SPELL_EARTH_REFORM, Optional.absent());
/*  77 */     this.dataManager.register(SPELL_BLOCK, Optional.absent());
/*     */     
/*  79 */     registerAIFilter("cast_earth_reform", CAST_EARTH_REFORM);
/*  80 */     registerAIFilter("cast_growth_crops", CAST_GROWTH_CROPS);
/*  81 */     registerAIFilter("cast_growth_trees", CAST_GROWTH_TREES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomizeGoals() {
/*  86 */     super.randomizeGoals();
/*  87 */     this.growthFirst = getRNG().nextBoolean();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addVillagerPosition() {}
/*     */ 
/*     */   
/*     */   public BlockPos getSpellBlock() {
/*  95 */     return (BlockPos)((Optional)this.dataManager.get(SPELL_BLOCK)).orNull();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpellBlock(BlockPos castingPos) {
/* 100 */     if (castingPos == null) {
/* 101 */       this.dataManager.set(SPELL_BLOCK, Optional.absent());
/*     */     } else {
/* 103 */       this.dataManager.set(SPELL_BLOCK, Optional.of(castingPos));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setCastingEarthReform(BlockPos pos) {
/* 108 */     getDataManager().set(SPELL_EARTH_REFORM, Optional.fromNullable(pos));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockPos getCastingEarthReform() {
/* 114 */     return (BlockPos)((Optional)getDataManager().get(SPELL_EARTH_REFORM)).orNull();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 120 */     super.onLivingUpdate();
/*     */     
/* 122 */     if (this.world.isRemote && !isSleeping()) {
/* 123 */       this.clientSpellBlock = getSpellBlock();
/* 124 */       if (this.clientSpellBlock != null) {
/* 125 */         this.growthTicks++;
/* 126 */         int range = getGrowthRange();
/*     */         
/* 128 */         spawnRadialParticle(getPositionVector(), range, (this.growthTicks * 3), 4, EnumParticleTypes.TOTEM);
/*     */         
/* 130 */         int particles = (this.growthTicks > 40) ? 7 : 1;
/* 131 */         for (int i = 0; i < particles; i++) {
/* 132 */           this.world.spawnParticle(EnumParticleTypes.TOTEM, this.clientSpellBlock
/* 133 */               .getX() + 0.5D + getRNG().nextGaussian() * range, this.clientSpellBlock
/* 134 */               .getY() + 0.5D, this.clientSpellBlock
/* 135 */               .getZ() + 0.5D + getRNG().nextGaussian() * range, 
/* 136 */               getRNG().nextGaussian() * 0.1D, this.rand.nextFloat(), getRNG().nextGaussian() * 0.1D, new int[0]);
/*     */         }
/*     */       } else {
/*     */         
/* 140 */         this.growthTicks = 0;
/*     */       } 
/*     */       
/* 143 */       BlockPos earthPos = getCastingEarthReform();
/* 144 */       if (earthPos != null) {
/* 145 */         this.earthReformTicks++;
/* 146 */         if (this.earthReformTicks < 44) {
/* 147 */           int count = this.earthReformTicks / 3 + 1;
/* 148 */           for (int i = 0; i < count; i++) {
/* 149 */             double dx = getRNG().nextGaussian();
/* 150 */             double dz = getRNG().nextGaussian();
/* 151 */             double dy = getRNG().nextGaussian();
/* 152 */             if (i > 0) {
/* 153 */               this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + dx, this.posY + dy, this.posZ + dz, 0.0D, getRNG().nextFloat() * 0.5D, 0.0D, new int[] { blockStateIds[getRNG().nextInt(blockStateIds.length)] });
/*     */             } else {
/* 155 */               this.world.spawnParticle(EnumParticleTypes.TOTEM, this.posX + dx, this.posY + dy, this.posZ + dz, 0.0D, getRNG().nextFloat() * 0.5D, 0.0D, new int[] { blockStateIds[getRNG().nextInt(blockStateIds.length)] });
/*     */             } 
/*     */           } 
/* 158 */         } else if (this.earthReformTicks < 55) {
/* 159 */           Vec3d vec = new Vec3d(earthPos.getX(), earthPos.getY(), earthPos.getZ());
/* 160 */           vec = vec.subtract(getPositionVector()).normalize().scale(0.8D);
/*     */           
/* 162 */           for (int i = 0; i < 3; i++) {
/* 163 */             double dx = getRNG().nextGaussian() * 0.1D;
/* 164 */             double dy = getRNG().nextGaussian() * 0.1D;
/* 165 */             double dz = getRNG().nextGaussian() * 0.1D;
/* 166 */             this.world.spawnParticle(EnumParticleTypes.TOTEM, this.posX + dx + vec.x, this.posY + 1.3D + dy, this.posZ + dz + vec.z, vec.x * 4.0D, 0.0D, vec.z * 4.0D, new int[0]);
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 171 */         this.earthReformTicks = 0;
/*     */         
/* 173 */         if (getRNG().nextInt(10) == 0) {
/* 174 */           double dx = getRNG().nextGaussian() * 0.3D;
/* 175 */           double dz = getRNG().nextGaussian() * 0.3D;
/* 176 */           double dy = getRNG().nextFloat() * 0.5D;
/* 177 */           this.world.spawnParticle(EnumParticleTypes.TOTEM, this.posX + dx, this.posY + dy + 0.2D, this.posZ + dz, 0.0D, getRNG().nextFloat() * 0.25D, 0.0D, new int[] { blockStateIds[getRNG().nextInt(blockStateIds.length)] });
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void spawnRadialParticle(Vec3d pos, double radius, float angle, int count, EnumParticleTypes type) {
/* 186 */     for (int i = 0; i < count; i++) {
/* 187 */       this.world.spawnParticle(type, pos.x + 
/* 188 */           MathHelper.cos(angle + i) * radius, pos.y + 0.5D, pos.z + 
/*     */           
/* 190 */           MathHelper.sin(angle + i) * radius, 0.0D, this.rand
/* 191 */           .nextFloat() * 0.2D, 0.0D, new int[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getGrowthRange() {
/* 196 */     int range = MathHelper.clamp(getRNG().nextInt(getSkill(ProfessionType.DRUID)) / 30, 0, 2);
/* 197 */     return range;
/*     */   }
/*     */   
/*     */   public boolean isGrowTime() {
/* 201 */     if (!isAIFilterEnabled("cast_earth_reform")) {
/* 202 */       return Village.isTimeOfDay(this.world, 0L, 13000L);
/*     */     }
/* 204 */     if (this.growthFirst) {
/* 205 */       return Village.isTimeOfDay(this.world, 0L, 6000L);
/*     */     }
/* 207 */     return Village.isTimeOfDay(this.world, 6000L, 13000L);
/*     */   }
/*     */   
/*     */   public boolean isEarthReformTime() {
/* 211 */     if (!isAIFilterEnabled("cast_growth_trees") && !isAIFilterEnabled("cast_growth_crops")) {
/* 212 */       return Village.isTimeOfDay(this.world, 0L, 13000L);
/*     */     }
/* 214 */     if (this.growthFirst) {
/* 215 */       return Village.isTimeOfDay(this.world, 6000L, 13000L);
/*     */     }
/* 217 */     return Village.isTimeOfDay(this.world, 0L, 6000L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityDruid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */