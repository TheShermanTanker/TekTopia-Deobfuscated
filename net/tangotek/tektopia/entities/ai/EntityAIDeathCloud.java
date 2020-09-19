/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.entities.EntityDeathCloud;
/*     */ import net.tangotek.tektopia.entities.EntityNecromancer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIDeathCloud
/*     */   extends EntityAIBase
/*     */ {
/*     */   private EntityNecromancer necro;
/*     */   private EntityPlayer targetPlayer;
/*  25 */   private int cooldownTick = 0;
/*     */   private int castTime;
/*     */   private BlockPos targetPos;
/*     */   private final Predicate<Entity> entityPredicate;
/*     */   
/*     */   public EntityAIDeathCloud(EntityNecromancer n) {
/*  31 */     this.necro = n;
/*  32 */     setMutexBits(1);
/*  33 */     this.entityPredicate = Predicates.and(EntitySelectors.CAN_AI_TARGET, e -> (e.isEntityAlive() && this.necro.getEntitySenses().canSee(e)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  38 */     if (this.necro.isAITick() && this.necro.ticksExisted > this.cooldownTick && !this.necro.hasVillagerDied()) {
/*  39 */       this.targetPlayer = this.necro.world.getClosestPlayer(this.necro.posX, this.necro.posY, this.necro.posZ, 16.0D, this.entityPredicate);
/*  40 */       if (this.targetPlayer != null) {
/*  41 */         this.targetPos = this.targetPlayer.getPosition();
/*  42 */         if (!this.targetPlayer.onGround && 
/*  43 */           this.necro.world.getBlockState(this.targetPos.down()).getBlock().equals(Blocks.AIR)) {
/*  44 */           this.targetPos = this.targetPos.down();
/*     */         }
/*  46 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/*  50 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  56 */     startCast();
/*  57 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  63 */     return (this.castTime > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/*  69 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  75 */     if (this.castTime > 0) {
/*  76 */       this.castTime--;
/*  77 */       if (this.castTime >= 37) {
/*     */         
/*  79 */         if (this.castTime == 37) {
/*  80 */           createCloud();
/*     */         }
/*  82 */         else if (this.castTime == 33) {
/*  83 */           this.necro.playSound(ModSoundEvents.deathCircle, this.necro.world.rand.nextFloat() * 0.4F + 1.2F, this.necro.world.rand.nextFloat() * 0.4F + 0.8F);
/*     */         } 
/*     */         
/*  86 */         if (this.castTime > 70) {
/*  87 */           this.necro.faceEntity((Entity)this.targetPlayer, 30.0F, 30.0F);
/*     */         }
/*     */       } 
/*     */       
/*  91 */       if (this.castTime == 0) {
/*  92 */         this.necro.stopServerAnimation("necro_cast_forward");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  97 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   private void createCloud() {
/* 102 */     EntityDeathCloud cloud = new EntityDeathCloud(this.necro.world, this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
/* 103 */     cloud.setRadius(3.0F);
/* 104 */     cloud.setWaitTime(10);
/* 105 */     cloud.setDuration(40 + this.necro.getLevel() * 30);
/* 106 */     cloud.setRadiusPerTick(0.03F);
/* 107 */     this.necro.world.spawnEntity((Entity)cloud);
/*     */     
/* 109 */     this.necro.playSound(ModSoundEvents.deathSummonEnd);
/*     */   }
/*     */ 
/*     */   
/*     */   private void startCast() {
/* 114 */     this.cooldownTick = this.necro.ticksExisted + this.necro.getLevelCooldown(this.necro.getRNG().nextInt(160) + 160);
/* 115 */     this.castTime = 80;
/* 116 */     this.necro.getNavigator().clearPath();
/* 117 */     this.necro.playServerAnimation("necro_cast_forward");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void stopCast() {
/* 123 */     this.necro.stopServerAnimation("necro_cast_forward");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 129 */     stopCast();
/* 130 */     this.castTime = 0;
/* 131 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIDeathCloud.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */