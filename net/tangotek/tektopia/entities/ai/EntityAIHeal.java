/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityCleric;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class EntityAIHeal
/*     */   extends EntityAIFollow
/*     */ {
/*     */   private boolean arrived = false;
/*  16 */   private int castTime = 0;
/*     */   private EntityCleric cleric;
/*  18 */   private final int CAST_TIME = 80;
/*  19 */   private final int HUNGER_COST = 8;
/*     */   private final double healRange;
/*  21 */   private EntityVillagerTek healVillager = null;
/*     */ 
/*     */   
/*     */   public EntityAIHeal(EntityVillagerTek entityIn, double range) {
/*  25 */     super((EntityVillageNavigator)entityIn);
/*  26 */     this.cleric = (EntityCleric)entityIn;
/*  27 */     this.healRange = range;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  33 */     this.healVillager = null;
/*  34 */     if (this.cleric.isAIFilterEnabled("cast_heal") && this.cleric.hasVillage() && !this.cleric.isSleeping() && this.cleric.getHunger() > 16) {
/*     */       
/*  36 */       if (this.cleric.getHealth() < this.cleric.getMaxHealth()) {
/*  37 */         this.healVillager = (EntityVillagerTek)this.cleric;
/*     */       }
/*     */       
/*  40 */       if (this.healVillager == null) {
/*     */         
/*  42 */         this.healVillager = this.cleric.getVillage().getActiveDefender(this.cleric.getPosition());
/*     */ 
/*     */         
/*  45 */         if (this.healVillager != null && inRange() && this.healVillager.getHealth() >= this.healVillager.getMaxHealth())
/*  46 */           this.healVillager = null; 
/*     */       } 
/*  48 */       if (this.healVillager == null && this.cleric.isAITick()) {
/*     */         
/*  50 */         List<EntityVillagerTek> healCandidates = this.cleric.world.getEntitiesWithinAABB(EntityVillagerTek.class, this.cleric.getEntityBoundingBox().grow(30.0D, 8.0D, 30.0D), v -> (v.getHealth() < v.getMaxHealth()));
/*  51 */         if (!healCandidates.isEmpty()) {
/*  52 */           this.healVillager = healCandidates.get(0);
/*     */         }
/*     */       } 
/*     */       
/*  56 */       if (this.healVillager != null) {
/*  57 */         return super.shouldExecute();
/*     */       }
/*     */     } 
/*  60 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/*  65 */     if (!this.arrived) {
/*  66 */       tryHeal();
/*     */     }
/*  68 */     this.arrived = true;
/*  69 */     super.onArrival();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  75 */     super.startExecuting();
/*     */   }
/*     */   
/*     */   protected boolean inRange() {
/*  79 */     if (getFollowTarget() != null) {
/*  80 */       double distSq = getFollowTarget().getDistanceSq((Entity)this.cleric);
/*  81 */       return (distSq < this.healRange * this.healRange);
/*     */     } 
/*     */     
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNearWalkPos() {
/*  89 */     return inRange();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/*  95 */     return (this.castTime <= 0);
/*     */   }
/*     */   
/*     */   protected void tryHeal() {
/*  99 */     if (this.cleric.isEntityAlive() && this.followTarget.isEntityAlive() && inRange() && this.followTarget.getHealth() < this.followTarget.getMaxHealth()) {
/*     */       
/* 101 */       this.castTime = 80;
/*     */       
/* 103 */       this.cleric.playServerAnimation("villager_summon");
/* 104 */       this.cleric.modifyHunger(-8);
/* 105 */       this.cleric.getNavigator().clearPath();
/* 106 */       setArrived();
/*     */       
/* 108 */       this.cleric.setSpellTarget((Entity)this.healVillager);
/* 109 */       this.cleric.playSound(ModSoundEvents.healingSource);
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
/* 122 */     this.castTime--;
/* 123 */     if (this.castTime == 36) {
/* 124 */       this.cleric.playSound(ModSoundEvents.villagerEnchant);
/* 125 */       if (this.healVillager.isEntityAlive() && this.healVillager.getHealth() < this.healVillager.getMaxHealth()) {
/* 126 */         this.healVillager.heal(this.cleric.getSkillLerp(ProfessionType.CLERIC, 3, 6));
/* 127 */         this.healVillager.playSound(ModSoundEvents.healingTarget);
/* 128 */         this.healVillager.modifyHappy(10);
/* 129 */         this.cleric.modifyHappy(2);
/* 130 */         this.cleric.tryAddSkill(ProfessionType.CLERIC, 3);
/*     */       } 
/*     */     } 
/*     */     
/* 134 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   protected EntityLivingBase getFollowTarget() {
/* 139 */     return (EntityLivingBase)this.healVillager;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldFollow() {
/* 144 */     if (this.castTime > 0) {
/* 145 */       return false;
/*     */     }
/* 147 */     return super.shouldFollow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 154 */     this.cleric.setMovementMode(this.cleric.getDefaultMovement());
/* 155 */     this.cleric.stopServerAnimation("villager_summon");
/* 156 */     this.cleric.setSpellTarget(null);
/* 157 */     this.arrived = false;
/* 158 */     this.castTime = 0;
/* 159 */     super.resetTask();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 164 */     if (this.castTime > 0) {
/* 165 */       return true;
/*     */     }
/* 167 */     if (!this.healVillager.isEntityAlive()) {
/* 168 */       return false;
/*     */     }
/* 170 */     if (inRange() && this.healVillager.getHealth() >= this.healVillager.getMaxHealth()) {
/* 171 */       return false;
/*     */     }
/* 173 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 178 */     this.cleric.setMovementMode(EntityVillagerTek.MovementMode.RUN);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIHeal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */