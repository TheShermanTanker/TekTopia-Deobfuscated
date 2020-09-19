/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.DataSerializers;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.SoundCategory;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ 
/*     */ public class EntityCleric extends EntityVillagerTek {
/*  18 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityCleric.class);
/*     */   
/*  20 */   private static final DataParameter<Integer> SPELL_TARGET = EntityDataManager.createKey(EntityCleric.class, DataSerializers.VARINT);
/*  21 */   private static final DataParameter<Boolean> CAST_BLESS = EntityDataManager.createKey(EntityCleric.class, DataSerializers.BOOLEAN);
/*  22 */   private static final DataParameter<Boolean> CAST_HEAL = EntityDataManager.createKey(EntityCleric.class, DataSerializers.BOOLEAN);
/*  23 */   private int handParticleTick = 0;
/*     */   
/*     */   static {
/*  26 */     animHandler.addAnim("tektopia", "villager_summon", "cleric_m", false);
/*  27 */     animHandler.addAnim("tektopia", "villager_cast_bless", "cleric_m", false);
/*  28 */     EntityVillagerTek.setupAnimations(animHandler, "cleric_m");
/*     */   }
/*     */   
/*     */   public EntityCleric(World worldIn) {
/*  32 */     super(worldIn, ProfessionType.CLERIC, VillagerRole.VILLAGER.value | VillagerRole.DEFENDER.value);
/*     */     
/*  34 */     Runnable enchantRunner = new Runnable()
/*     */       {
/*     */         public void run() {
/*  37 */           EntityCleric.this.world.playSound(EntityCleric.this.posX, EntityCleric.this.posY, EntityCleric.this.posZ, ModSoundEvents.villagerEnchant, SoundCategory.NEUTRAL, 1.0F, EntityCleric.this.rand.nextFloat() * 0.2F + 0.9F, false);
/*     */         }
/*     */       };
/*     */     
/*  41 */     if (this.world.isRemote) {
/*  42 */       addAnimationTrigger("tektopia:villager_summon", 12, enchantRunner);
/*  43 */       addAnimationTrigger("tektopia:villager_summon", 20, new Runnable()
/*     */           {
/*     */             public void run() {
/*  46 */               EntityCleric.this.handParticleTick = 25;
/*     */             }
/*     */           });
/*  49 */       addAnimationTrigger("tektopia:villager_summon", 32, enchantRunner);
/*     */     } 
/*     */   }
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/*  54 */     return animHandler;
/*     */   }
/*     */   
/*     */   protected void initEntityAI() {
/*  58 */     super.initEntityAI();
/*     */     
/*  60 */     addTask(49, (EntityAIBase)new EntityAIHeal(this, 16.0D));
/*  61 */     addTask(50, (EntityAIBase)new EntityAIBless(this, 12.0D));
/*  62 */     addTask(50, (EntityAIBase)new EntityAIPatrolVillage(this, p -> !p.isSleepingTime()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void attachToVillage(Village v) {
/*  67 */     super.attachToVillage(v);
/*  68 */     this.sleepOffset = v.getNextClericSleepOffset();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addTask(int priority, EntityAIBase task) {
/*  73 */     if (task instanceof net.tangotek.tektopia.entities.ai.EntityAIFleeEntity) {
/*     */       return;
/*     */     }
/*  76 */     super.addTask(priority, task);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  81 */     super.entityInit();
/*  82 */     this.dataManager.register(SPELL_TARGET, Integer.valueOf(0));
/*     */     
/*  84 */     registerAIFilter("cast_bless", CAST_BLESS);
/*  85 */     registerAIFilter("cast_heal", CAST_HEAL);
/*     */   }
/*     */   
/*     */   public EntityVillagerTek.MovementMode getDefaultMovement() {
/*  89 */     if (this.village.enemySeenRecently()) {
/*  90 */       return EntityVillagerTek.MovementMode.RUN;
/*     */     }
/*  92 */     return super.getDefaultMovement();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSpellcasting() {
/*  97 */     int entityId = ((Integer)this.dataManager.get(SPELL_TARGET)).intValue();
/*  98 */     return (entityId > 0);
/*     */   }
/*     */   
/*     */   public Entity getSpellTargetEntity() {
/* 102 */     int entityId = ((Integer)this.dataManager.get(SPELL_TARGET)).intValue();
/* 103 */     if (entityId > 0) {
/* 104 */       return this.world.getEntityByID(entityId);
/*     */     }
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpellTarget(Entity entity) {
/* 111 */     if (entity == null) {
/* 112 */       this.dataManager.set(SPELL_TARGET, Integer.valueOf(0));
/*     */     } else {
/* 114 */       this.dataManager.set(SPELL_TARGET, Integer.valueOf(entity.getEntityId()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onUpdate() {
/* 119 */     super.onUpdate();
/* 120 */     if (this.world.isRemote && 
/* 121 */       isSpellcasting() && this.handParticleTick > 0) {
/* 122 */       double d0 = 0.7D;
/* 123 */       double d1 = 0.7D;
/* 124 */       double d2 = 0.7D;
/* 125 */       float f = this.renderYawOffset * 0.017453292F + MathHelper.cos(this.ticksExisted * 0.6662F) * 0.25F;
/* 126 */       float f1 = MathHelper.cos(f);
/* 127 */       float f2 = MathHelper.sin(f);
/* 128 */       this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + f1 * 0.6D, this.posY + 2.0D, this.posZ + f2 * 0.6D, d0, d1, d2, new int[] { 0, 1, 1 });
/* 129 */       this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX - f1 * 0.6D, this.posY + 2.0D, this.posZ - f2 * 0.6D, d0, d1, d2, new int[] { 0, 1, 1 });
/* 130 */       this.handParticleTick--;
/*     */       
/* 132 */       if (this.handParticleTick <= 4) {
/* 133 */         Entity spellTarget = getSpellTargetEntity();
/* 134 */         if (spellTarget != null)
/* 135 */           for (int i = 0; i < 20; i++) {
/* 136 */             double dx = getRNG().nextGaussian() * 0.5D;
/* 137 */             double dz = getRNG().nextGaussian() * 0.5D;
/* 138 */             this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, spellTarget.posX + dx, spellTarget.posY + getRNG().nextDouble() * 2.0D, spellTarget.posZ + dz, 0.0D, 0.8D, 0.0D, new int[] { 0, 1, 1 });
/*     */           }  
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityCleric.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */