/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ 
/*     */ public class EntityAIMeleeTarget
/*     */   extends EntityAIFollow
/*     */ {
/*  18 */   private int attackTick = 0;
/*  19 */   private final int ATTACK_TICK_TIME = 24;
/*  20 */   private int attackTime = 24;
/*     */   
/*     */   private boolean arrived = false;
/*     */   private final double attackRange;
/*     */   private final Function<EntityVillagerTek, ItemStack> weaponFunc;
/*     */   private ItemStack weapon;
/*     */   private Runnable onHit;
/*     */   private final ProfessionType professionType;
/*     */   protected final EntityVillagerTek villager;
/*     */   private final Predicate<EntityVillagerTek> shouldPred;
/*     */   private final EntityVillagerTek.VillagerThought missingThought;
/*     */   
/*     */   public EntityAIMeleeTarget(EntityVillagerTek entityIn, Function<EntityVillagerTek, ItemStack> func, EntityVillagerTek.VillagerThought missingThought, Predicate<EntityVillagerTek> shouldPred, Runnable onHit, ProfessionType pt) {
/*  33 */     super((EntityVillageNavigator)entityIn);
/*  34 */     this.shouldPred = shouldPred;
/*  35 */     this.villager = entityIn;
/*  36 */     this.attackRange = 3.5D;
/*  37 */     this.weaponFunc = func;
/*  38 */     this.onHit = onHit;
/*  39 */     this.professionType = pt;
/*  40 */     this.missingThought = missingThought;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  46 */     if (this.villager.getHunger() < 10) {
/*  47 */       return false;
/*     */     }
/*  49 */     if (!this.shouldPred.test(this.villager)) {
/*  50 */       return false;
/*     */     }
/*  52 */     if (super.shouldExecute()) {
/*  53 */       this.weapon = this.weaponFunc.apply(this.villager);
/*  54 */       if (this.weapon.isEmpty()) {
/*  55 */         this.villager.setThought(this.missingThought);
/*  56 */         return false;
/*     */       } 
/*     */       
/*  59 */       return true;
/*     */     } 
/*     */     
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/*  67 */     if (!this.arrived) {
/*  68 */       tryAttack();
/*     */     }
/*  70 */     this.arrived = true;
/*  71 */     super.onArrival();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  77 */     this.villager.debugOut("Melee Start @" + this.villager.getPosition());
/*  78 */     this.villager.equipActionItem(this.weapon);
/*  79 */     super.startExecuting();
/*     */   }
/*     */   
/*     */   protected boolean inRange() {
/*  83 */     if (getFollowTarget() != null) {
/*  84 */       double moddedAttackRange = this.attackRange * this.villager.getSkillLerp(this.professionType, 100, 125) / 100.0D;
/*     */       
/*  86 */       if (this.villager.getAttackTarget() instanceof net.minecraft.entity.item.EntityArmorStand) {
/*  87 */         moddedAttackRange = 1.6D;
/*     */       }
/*  89 */       double distSq = getFollowTarget().getDistanceSq((Entity)this.villager);
/*  90 */       return (distSq < moddedAttackRange * moddedAttackRange);
/*     */     } 
/*     */     
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNearWalkPos() {
/*  98 */     return inRange();
/*     */   }
/*     */   
/*     */   protected void tryAttack() {
/* 102 */     if (this.weapon != null && this.villager.isEntityAlive() && this.followTarget.isEntityAlive() && inRange()) {
/*     */       
/* 104 */       this.attackTick = this.villager.getSkillLerp(this.professionType, 28, 24);
/* 105 */       this.attackTime = this.attackTick;
/*     */       
/* 107 */       this.navigator.faceEntity((Entity)getFollowTarget(), 60.0F, 40.0F);
/*     */       
/* 109 */       this.villager.debugOut("Melee Attack Anim Start - " + getFollowTarget().getEntityId());
/* 110 */       this.villager.playServerAnimation("villager_chop");
/*     */       
/* 112 */       if (this.villager.getHunger() > 2 && this.villager.getRNG().nextBoolean()) {
/* 113 */         this.villager.modifyHunger(-1);
/*     */       }
/* 115 */       this.villager.getNavigator().clearPath();
/* 116 */       setArrived();
/* 117 */       this.villager.addJob(new TickJob(16, 0, false, () -> {
/*     */               if (this.weapon != null && getFollowTarget() != null && getFollowTarget().isEntityAlive() && this.villager.isEntityAlive()) {
/*     */                 this.villager.attackEntityAsMob((Entity)getFollowTarget());
/*     */                 if (!(this.villager.getAttackTarget() instanceof net.minecraft.entity.item.EntityArmorStand)) {
/*     */                   this.villager.damageItem(this.weapon, 3);
/*     */                 }
/*     */                 this.onHit.run();
/*     */                 this.villager.playSound(ModSoundEvents.villagerGrunt);
/*     */                 this.villager.throttledSadness(-1);
/*     */               } 
/*     */             }));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 133 */     if (this.attackTick > 0) {
/* 134 */       this.attackTick--;
/* 135 */       if (this.attackTime - this.attackTick == 20) {
/* 136 */         this.villager.stopServerAnimation("villager_chop");
/*     */       }
/* 138 */     } else if (inRange()) {
/* 139 */       tryAttack();
/*     */     } 
/* 141 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   protected EntityLivingBase getFollowTarget() {
/* 146 */     return this.villager.getAttackTarget();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldFollow() {
/* 151 */     if (this.attackTick > 0) {
/* 152 */       return false;
/*     */     }
/* 154 */     return super.shouldFollow();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 160 */     this.villager.unequipActionItem(this.weapon);
/* 161 */     this.villager.stopServerAnimation("villager_chop");
/*     */     
/* 163 */     this.villager.debugOut("Melee End");
/* 164 */     this.attackTick = 0;
/* 165 */     this.villager.pickupItems(5);
/* 166 */     this.arrived = false;
/* 167 */     this.weapon = null;
/* 168 */     super.resetTask();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 173 */     if (this.attackTick > 0) {
/* 174 */       return true;
/*     */     }
/* 176 */     if (this.villager.getAttackTarget() == null) {
/* 177 */       return false;
/*     */     }
/* 179 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 184 */     if (this.villager.isHostile().test(this.followTarget)) {
/* 185 */       this.villager.setMovementMode(EntityVillagerTek.MovementMode.RUN);
/*     */     } else {
/* 187 */       this.villager.setMovementMode(EntityVillagerTek.MovementMode.WALK);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIMeleeTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */