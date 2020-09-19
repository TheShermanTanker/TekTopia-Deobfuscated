/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.monster.EntityMob;
/*     */ import net.minecraft.entity.monster.EntityWitherSkeleton;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.entities.EntityNecromancer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAISummonUndead
/*     */   extends EntityAIBase
/*     */ {
/*     */   private EntityNecromancer necro;
/*  23 */   private int summonTime = 0;
/*  24 */   private int cooldownTick = 0;
/*     */   protected final int checkInterval;
/*     */   private BlockPos summonPos;
/*  27 */   private final int MAX_SUMMONS = 50;
/*  28 */   private int summonCount = 0;
/*     */   
/*     */   public EntityAISummonUndead(EntityNecromancer necro, int checkInterval) {
/*  31 */     this.checkInterval = checkInterval;
/*  32 */     this.necro = necro;
/*  33 */     setMutexBits(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  38 */     if (this.necro.isAITick() && isSummonReady(this.necro) && 
/*  39 */       this.necro.getMinions().size() < this.necro.getMaxSummons()) {
/*  40 */       List<EntityZombie> zombs = this.necro.world.getEntitiesWithinAABB(EntityZombie.class, this.necro.getEntityBoundingBox().grow(30.0D, 10.0D, 30.0D));
/*  41 */       if (zombs.size() < this.necro.getLevel() * 2) {
/*  42 */         this.summonPos = findSummonPos(this.necro.getNavigator().noPath() ? 2.0F : 10.0F);
/*  43 */         return (this.summonPos != null);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  48 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isSummonReady(EntityNecromancer necro) {
/*  52 */     if (this.summonCount >= 50) {
/*  53 */       return false;
/*     */     }
/*  55 */     if (this.necro.getMinions().size() < 1) {
/*  56 */       return true;
/*     */     }
/*     */     
/*  59 */     if (this.necro.ticksExisted > this.cooldownTick && (
/*  60 */       this.necro.getMinions().size() < 1 || this.necro.getRNG().nextInt(this.checkInterval) == 0)) {
/*  61 */       return true;
/*     */     }
/*     */     
/*  64 */     return false;
/*     */   }
/*     */   
/*     */   public void startExecuting() {
/*  68 */     this.necro.getNavigator().clearPath();
/*  69 */     startSummoning();
/*  70 */     super.startExecuting();
/*     */   }
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  74 */     return (this.summonTime > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  79 */     if (this.summonTime > 0) {
/*  80 */       this.necro.faceLocation(this.summonPos.getX(), this.summonPos.getZ(), 100.0F);
/*  81 */       this.summonTime--;
/*  82 */       if (this.summonTime == 12) {
/*  83 */         spawnMinion();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private BlockPos findSummonPos(float forwardDist) {
/*  96 */     float distance = forwardDist;
/*  97 */     float f1 = MathHelper.sin(this.necro.rotationYaw * 0.017453292F);
/*  98 */     float f2 = MathHelper.cos(this.necro.rotationYaw * 0.017453292F);
/*  99 */     double behindX = (-distance * f1);
/* 100 */     double behindZ = (distance * f2);
/*     */     
/* 102 */     BlockPos testPos = new BlockPos(this.necro.posX + behindX, this.necro.posY, this.necro.posZ + behindZ);
/* 103 */     EntityZombie zombie = new EntityZombie(this.necro.world);
/* 104 */     int searchBoxXZ = 8;
/* 105 */     for (int l = 0; l < 50; l++) {
/* 106 */       int i1 = testPos.getX() + MathHelper.getInt(this.necro.world.rand, -searchBoxXZ, searchBoxXZ);
/* 107 */       int j1 = testPos.getY() + MathHelper.getInt(this.necro.world.rand, -5, 5);
/* 108 */       int k1 = testPos.getZ() + MathHelper.getInt(this.necro.world.rand, -searchBoxXZ, searchBoxXZ);
/*     */       
/* 110 */       BlockPos bp = new BlockPos(i1, j1, k1);
/* 111 */       if (this.necro.world.isSideSolid(bp.down(), EnumFacing.UP)) {
/* 112 */         zombie.setPosition(i1 + 0.5D, j1, k1 + 0.5D);
/* 113 */         if (zombie.isNotColliding() && (
/* 114 */           !this.necro.hasVillage() || !this.necro.getVillage().isInStructure(bp))) {
/* 115 */           return bp;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 121 */     return null;
/*     */   }
/*     */   
/*     */   private void startSummoning() {
/* 125 */     this.necro.debugOut("Summoning undead minion [ " + this.summonPos + "] Starting");
/* 126 */     this.summonTime = 47;
/* 127 */     this.necro.playServerAnimation("necro_summon");
/* 128 */     this.necro.playSound(ModSoundEvents.deathSummon, this.necro.getRNG().nextFloat() * 0.2F + 1.2F, this.necro.getRNG().nextFloat() * 0.2F + 0.9F);
/*     */   }
/*     */   
/*     */   private void stopSummoning() {
/* 132 */     this.necro.debugOut("Summoning complete [ " + this.summonPos + "] Starting");
/* 133 */     this.necro.stopServerAnimation("necro_summon");
/*     */   }
/*     */ 
/*     */   
/*     */   private void spawnMinion() {
/*     */     EntityZombie entityZombie;
/* 139 */     if (this.necro.getLevel() == 5 && this.necro.getRNG().nextBoolean()) {
/* 140 */       EntityWitherSkeleton entityWitherSkeleton = this.necro.createWitherSkeleton(this.summonPos);
/* 141 */     } else if (this.necro.getLevel() == 4 && this.necro.getRNG().nextInt(4) == 0) {
/* 142 */       EntityWitherSkeleton entityWitherSkeleton = this.necro.createWitherSkeleton(this.summonPos);
/*     */     } else {
/* 144 */       entityZombie = this.necro.createZombie(this.summonPos);
/*     */     } 
/* 146 */     this.necro.world.spawnEntity((Entity)entityZombie);
/* 147 */     this.necro.addMinion((EntityMob)entityZombie);
/* 148 */     entityZombie.playSound(ModSoundEvents.deathSummonTarget, this.necro.getRNG().nextFloat() * 0.4F + 0.8F, this.necro.getRNG().nextFloat() * 0.4F + 0.8F);
/*     */     
/* 150 */     this.summonCount++;
/* 151 */     this.necro.playSound(ModSoundEvents.deathSummonEnd);
/* 152 */     this.cooldownTick = this.necro.ticksExisted + this.necro.getLevelCooldown(MathHelper.getInt(this.necro.getRNG(), 30, 50) + this.necro.getMinions().size() * 20);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 157 */     this.summonTime = 0;
/* 158 */     stopSummoning();
/* 159 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAISummonUndead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */