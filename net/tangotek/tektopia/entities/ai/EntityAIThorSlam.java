/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityGuard;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIThorSlam
/*     */   extends EntityAIBase
/*     */ {
/*  21 */   private int attackTick = 0;
/*  22 */   private final int ATTACK_TICK_TIME = 80;
/*  23 */   private final int COOLDOWN = 200;
/*     */   private final double attackRange;
/*     */   private final Function<EntityVillagerTek, ItemStack> weaponFunc;
/*     */   private ItemStack weapon;
/*     */   protected final EntityGuard guard;
/*     */   private final Predicate<EntityVillagerTek> shouldPred;
/*  29 */   private long nextAttackTick = 0L;
/*     */ 
/*     */   
/*     */   public EntityAIThorSlam(EntityGuard entityIn, Function<EntityVillagerTek, ItemStack> func, double range, Predicate<EntityVillagerTek> shouldPred) {
/*  33 */     setMutexBits(1);
/*  34 */     this.shouldPred = shouldPred;
/*  35 */     this.guard = entityIn;
/*  36 */     this.attackRange = range;
/*  37 */     this.weaponFunc = func;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  42 */     if (this.guard.getAttackTarget() != null && this.guard.isAIFilterEnabled("guard_super_attack")) {
/*  43 */       if (this.guard.isStarving()) {
/*  44 */         return false;
/*     */       }
/*  46 */       if (this.guard.world.getTotalWorldTime() < this.nextAttackTick) {
/*  47 */         return false;
/*     */       }
/*  49 */       if (!this.shouldPred.test(this.guard)) {
/*  50 */         return false;
/*     */       }
/*     */       
/*  53 */       List<EntityLivingBase> hostiles = getTargets();
/*  54 */       if (hostiles.size() >= 2 && 
/*  55 */         this.guard.getRNG().nextInt(2) == 0) {
/*  56 */         this.weapon = this.weaponFunc.apply(this.guard);
/*  57 */         if (this.weapon.isEmpty()) {
/*  58 */           this.guard.setThought(EntityVillagerTek.VillagerThought.SWORD);
/*  59 */           return false;
/*     */         } 
/*     */         
/*  62 */         return true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   private List<EntityLivingBase> getTargets() {
/*  71 */     List<EntityLivingBase> hostiles = this.guard.world.getEntitiesWithinAABB(EntityLivingBase.class, this.guard.getEntityBoundingBox().grow(this.attackRange), this.guard.isSuitableTarget());
/*  72 */     return hostiles;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/*  78 */     return (this.attackTick <= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  84 */     super.startExecuting();
/*  85 */     tryAttack();
/*     */   }
/*     */   
/*     */   protected void tryAttack() {
/*  89 */     if (this.weapon != null && this.guard.isEntityAlive()) {
/*  90 */       this.attackTick = 80;
/*  91 */       this.nextAttackTick = this.guard.world.getTotalWorldTime() + this.guard.getSkillLerp(ProfessionType.GUARD, 200, 100);
/*     */       
/*  93 */       this.guard.faceEntity((Entity)this.guard.getAttackTarget(), 60.0F, 40.0F);
/*  94 */       this.guard.equipActionItem(this.weapon);
/*  95 */       this.guard.playServerAnimation("villager_thor_jump");
/*     */       
/*  97 */       if (this.guard.getHunger() > 2) {
/*  98 */         this.guard.modifyHunger(-1);
/*     */       }
/* 100 */       this.guard.playSound(ModSoundEvents.bigAttack);
/* 101 */       this.guard.getNavigator().clearPath();
/* 102 */       this.guard.addJob(new TickJob(40, 0, false, () -> {
/*     */               if (this.weapon != null && this.guard.isEntityAlive()) {
/*     */                 double knockbackMod = this.guard.getSkillLerp(ProfessionType.GUARD, 200, 400) / 100.0D;
/*     */                 List<EntityLivingBase> hostiles = getTargets();
/*     */                 hostiles.forEach(());
/*     */                 this.guard.damageItem(this.weapon, 1);
/*     */                 this.guard.tryAddSkill(ProfessionType.GUARD, 1);
/*     */                 this.guard.debugOut("Melee weapon: " + this.weapon.getItemDamage() + " / " + this.weapon.getMaxDamage());
/*     */                 this.guard.playSound(ModSoundEvents.slamGround);
/*     */               } 
/*     */             }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 122 */     this.attackTick--;
/* 123 */     super.updateTask();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 129 */     this.guard.unequipActionItem(this.weapon);
/* 130 */     this.guard.stopServerAnimation("villager_thor_jump");
/*     */     
/* 132 */     this.attackTick = 0;
/* 133 */     this.guard.pickupItems(5);
/* 134 */     this.weapon = null;
/* 135 */     super.resetTask();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 140 */     if (this.attackTick > 0) {
/* 141 */       return true;
/*     */     }
/* 143 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIThorSlam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */