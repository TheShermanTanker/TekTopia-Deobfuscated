/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.tangotek.tektopia.ModPotions;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.entities.EntityCleric;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class EntityAIBless
/*     */   extends EntityAIFollow {
/*     */   private boolean arrived = false;
/*  20 */   private int castTime = 0;
/*     */   private EntityCleric cleric;
/*  22 */   private final int CAST_TIME = 42;
/*     */   private final double range;
/*  24 */   private EntityVillagerTek target = null;
/*  25 */   private int cooldownTick = 0;
/*  26 */   private final int COOLDOWN = 1200;
/*     */ 
/*     */   
/*     */   public EntityAIBless(EntityVillagerTek entityIn, double range) {
/*  30 */     super((EntityVillageNavigator)entityIn);
/*  31 */     this.cleric = (EntityCleric)entityIn;
/*  32 */     this.range = range;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  38 */     if (isCooldownReady() && this.cleric.isAITick("cast_bless") && this.cleric.hasVillage() && !this.cleric.shouldSleep()) {
/*  39 */       List<EntityVillagerTek> candidates = this.cleric.world.getEntitiesWithinAABB(EntityVillagerTek.class, this.cleric.getEntityBoundingBox().grow(25.0D, 8.0D, 25.0D), isBlessable());
/*  40 */       if (!candidates.isEmpty()) {
/*  41 */         this.target = candidates.get(0);
/*  42 */         if (this.target == this.cleric && candidates.size() > 1) {
/*  43 */           this.target = candidates.get(1);
/*     */         }
/*  45 */         return super.shouldExecute();
/*     */       } 
/*     */     } 
/*     */     
/*  49 */     return false;
/*     */   }
/*     */   
/*     */   private Predicate<EntityVillagerTek> isBlessable() {
/*  53 */     return v -> (v.getHappy() < v.getMaxHappy() && v != this.cleric && v.getBlessed() <= 0 && v.isRole(VillagerRole.VILLAGER) && !v.isSleeping());
/*     */   }
/*     */   
/*     */   private boolean isCooldownReady() {
/*  57 */     return (this.cleric.ticksExisted - this.cooldownTick > 1200);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/*  62 */     if (!this.arrived) {
/*  63 */       tryCast();
/*     */     }
/*  65 */     this.arrived = true;
/*  66 */     super.onArrival();
/*     */   }
/*     */   
/*     */   protected boolean inRange() {
/*  70 */     if (getFollowTarget() != null) {
/*  71 */       double distSq = getFollowTarget().getDistanceSq((Entity)this.cleric);
/*  72 */       return (distSq < this.range * this.range);
/*     */     } 
/*     */     
/*  75 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNearWalkPos() {
/*  80 */     return inRange();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/*  86 */     return (this.castTime <= 0);
/*     */   }
/*     */   
/*     */   protected void tryCast() {
/*  90 */     if (this.cleric.isEntityAlive() && this.followTarget.isEntityAlive() && inRange()) {
/*     */       
/*  92 */       this.castTime = 42;
/*     */       
/*  94 */       this.cleric.playServerAnimation("villager_cast_bless");
/*  95 */       this.cleric.modifyHunger(-3);
/*  96 */       this.cleric.getNavigator().clearPath();
/*  97 */       setArrived();
/*     */       
/*  99 */       this.cleric.setSpellTarget((Entity)this.target);
/* 100 */       this.cleric.playSound(ModSoundEvents.healingSource);
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
/*     */   public void updateTask() {
/* 113 */     this.castTime--;
/* 114 */     if (this.castTime == 0) {
/* 115 */       this.cleric.playSound(ModSoundEvents.villagerEnchant);
/* 116 */       if (this.target.isEntityAlive()) {
/* 117 */         PotionEffect blessEffect = new PotionEffect((Potion)ModPotions.potionBless, 6000, this.cleric.getBaseSkill(ProfessionType.CLERIC));
/* 118 */         this.target.addPotionEffect(blessEffect);
/* 119 */         this.target.playSound(ModSoundEvents.healingTarget);
/*     */         
/* 121 */         this.cleric.tryAddSkill(ProfessionType.CLERIC, 4);
/* 122 */         this.cooldownTick = this.cleric.ticksExisted;
/*     */       } 
/*     */     } 
/*     */     
/* 126 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   protected EntityLivingBase getFollowTarget() {
/* 131 */     return (EntityLivingBase)this.target;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldFollow() {
/* 136 */     if (this.castTime > 0) {
/* 137 */       return false;
/*     */     }
/* 139 */     return super.shouldFollow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 146 */     this.cleric.stopServerAnimation("villager_cast_bless");
/* 147 */     this.cleric.setSpellTarget(null);
/* 148 */     this.arrived = false;
/* 149 */     this.castTime = 0;
/* 150 */     super.resetTask();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 155 */     if (this.castTime > 0) {
/* 156 */       return true;
/*     */     }
/* 158 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 163 */     this.cleric.setMovementMode(this.cleric.getDefaultMovement());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIBless.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */