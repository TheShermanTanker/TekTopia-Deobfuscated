/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityBard;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class EntityAIPerformWander
/*     */   extends EntityAIFollow
/*     */ {
/*     */   private boolean arrived = false;
/*  17 */   private int castTime = 0;
/*     */   private EntityVillagerTek target;
/*     */   private EntityBard bard;
/*  20 */   private int happyGiven = 0;
/*     */ 
/*     */   
/*     */   public EntityAIPerformWander(EntityVillagerTek entityIn) {
/*  24 */     super((EntityVillageNavigator)entityIn);
/*  25 */     this.bard = (EntityBard)entityIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  30 */     if (this.bard.isAITick("perform_wander") && this.bard.hasVillage() && this.bard.isWorkTime() && this.bard.getRNG().nextInt(8) == 0) {
/*  31 */       List<EntityVillagerTek> villagers = this.bard.world.getEntitiesWithinAABB(EntityVillagerTek.class, this.bard.getEntityBoundingBox().grow(30.0D, 8.0D, 30.0D), v -> (v.getHappy() < v.getMaxHappy() && !v.isEntityEqual((Entity)this.bard) && v.isVillageMember(this.bard.getVillage())));
/*  32 */       this.target = villagers.stream().min(Comparator.comparing(v -> Integer.valueOf(v.getHappy()))).orElse(null);
/*  33 */       if (this.target != null) {
/*  34 */         return super.shouldExecute();
/*     */       }
/*     */     } 
/*  37 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/*  42 */     if (!this.arrived) {
/*  43 */       tryPerform();
/*     */     }
/*  45 */     this.arrived = true;
/*  46 */     super.onArrival();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  52 */     this.bard.setPerformance(ModSoundEvents.Performance.getRandom(false, this.bard.getRNG()));
/*  53 */     super.startExecuting();
/*     */   }
/*     */   
/*     */   protected boolean inRange() {
/*  57 */     if (getFollowTarget() != null) {
/*  58 */       double distSq = getFollowTarget().getDistanceSq((Entity)this.bard);
/*  59 */       return (distSq < 36.0D);
/*     */     } 
/*     */     
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNearWalkPos() {
/*  67 */     return inRange();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/*  73 */     return (this.castTime <= 0);
/*     */   }
/*     */   
/*     */   protected void tryPerform() {
/*  77 */     if (this.bard.isEntityAlive() && this.followTarget.isEntityAlive() && inRange()) {
/*     */       
/*  79 */       ModSoundEvents.Performance performance = this.bard.getPerformance();
/*  80 */       this.castTime = performance.duration;
/*     */       
/*  82 */       this.bard.getNavigator().clearPath();
/*  83 */       this.bard.playServerAnimation(performance.anim);
/*  84 */       this.bard.playSound(performance.sound, 2.0F, 1.0F);
/*  85 */       this.bard.modifyHunger(-6);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  92 */     this.castTime--;
/*     */     
/*  94 */     if (this.castTime > 0 && this.bard.getRNG().nextInt(40) == 0) {
/*  95 */       distributeHappyNearby(8, 1, 1);
/*  96 */       this.bard.tryAddSkill(ProfessionType.BARD, 30);
/*     */     } 
/*     */ 
/*     */     
/* 100 */     if (this.castTime == 0) {
/*     */       
/* 102 */       distributeHappyNearby(2, 2, 3);
/*     */ 
/*     */       
/* 105 */       if (this.happyGiven > 3) {
/* 106 */         this.bard.modifyHappy(this.happyGiven);
/*     */       } else {
/* 108 */         this.bard.modifyHappy(-2);
/*     */       } 
/*     */     } 
/* 111 */     super.updateTask();
/*     */   }
/*     */   
/*     */   private void distributeHappyNearby(int chance, int min, int max) {
/* 115 */     List<EntityVillagerTek> nearby = this.bard.world.getEntitiesWithinAABB(EntityVillagerTek.class, this.bard.getEntityBoundingBox().grow(16.0D, 8.0D, 16.0D), v -> (v.getHappy() < v.getMaxHappy() && v.isVillageMember(this.bard.getVillage())));
/* 116 */     for (EntityVillagerTek v : nearby) {
/* 117 */       if (v != this.bard && this.bard.getRNG().nextInt(chance) == 0) {
/* 118 */         v.modifyHappy(this.bard.getSkillLerp(ProfessionType.BARD, min, max));
/* 119 */         this.happyGiven++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected EntityLivingBase getFollowTarget() {
/* 126 */     return (EntityLivingBase)this.target;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldFollow() {
/* 131 */     if (this.arrived) {
/* 132 */       return false;
/*     */     }
/* 134 */     return super.shouldFollow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 141 */     this.bard.stopServerAnimation((this.bard.getPerformance()).anim);
/* 142 */     this.bard.setPerformance(null);
/* 143 */     this.arrived = false;
/* 144 */     this.castTime = 0;
/* 145 */     this.happyGiven = 0;
/* 146 */     super.resetTask();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 151 */     if (this.castTime > 0) {
/* 152 */       return true;
/*     */     }
/* 154 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 159 */     this.bard.setMovementMode(this.bard.getDefaultMovement());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIPerformWander.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */