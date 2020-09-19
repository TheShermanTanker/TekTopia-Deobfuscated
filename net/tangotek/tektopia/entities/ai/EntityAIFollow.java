/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ 
/*     */ public abstract class EntityAIFollow extends EntityAIMoveToBlock {
/*     */   protected EntityLivingBase followTarget;
/*  12 */   protected int followUpdateTick = 40; protected BlockPos lastTargetPos;
/*     */   protected final EntityVillageNavigator navigator;
/*     */   
/*     */   public EntityAIFollow(EntityVillageNavigator entityIn) {
/*  16 */     super(entityIn);
/*  17 */     this.navigator = entityIn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  24 */     BlockPos pos = this.followTarget.getPosition();
/*     */ 
/*     */     
/*  27 */     if (isWalkable(pos, this.navigator)) {
/*  28 */       return pos;
/*     */     }
/*     */     
/*  31 */     Vec3d diff = this.followTarget.getPositionVector().subtract(new Vec3d(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D));
/*     */     
/*  33 */     BlockPos xOffset = pos.add(Math.signum(diff.x), 0.0D, 0.0D);
/*  34 */     if (isWalkable(xOffset, this.navigator)) {
/*  35 */       return xOffset;
/*     */     }
/*  37 */     BlockPos zOffset = pos.add(0.0D, 0.0D, Math.signum(diff.z));
/*  38 */     if (isWalkable(zOffset, this.navigator)) {
/*  39 */       return zOffset;
/*     */     }
/*  41 */     BlockPos bothOffset = pos.add(Math.signum(diff.x), 0.0D, Math.signum(diff.z));
/*  42 */     if (isWalkable(bothOffset, this.navigator)) {
/*  43 */       return bothOffset;
/*     */     }
/*     */     
/*  46 */     return pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  51 */     if (this.navigator.hasVillage()) {
/*  52 */       this.followTarget = getFollowTarget();
/*  53 */       if (this.followTarget == null) {
/*  54 */         return false;
/*     */       }
/*  56 */       if (!shouldFollow()) {
/*  57 */         return false;
/*     */       }
/*  59 */       return super.shouldExecute();
/*     */     } 
/*     */     
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  68 */     updateFollowTick();
/*  69 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNearWalkPos() {
/*  74 */     return (getWalkPos() != null && getWalkPos().distanceSq((Vec3i)this.navigator.getPosition()) < 2.5D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  79 */     if (!shouldFollow()) {
/*  80 */       return false;
/*     */     }
/*  82 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  87 */     this.followUpdateTick--;
/*  88 */     if (shouldFollow()) {
/*  89 */       if (this.followUpdateTick <= 0) {
/*  90 */         updateFollowTick();
/*  91 */         doMove();
/*     */       }
/*  93 */       else if (this.followTarget.getDistanceSq(this.lastTargetPos) > 25.0D) {
/*  94 */         doMove();
/*     */       } 
/*     */     }
/*     */     
/*  98 */     this.navigator.faceEntity((Entity)this.followTarget, 60.0F, 40.0F);
/*     */     
/* 100 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract EntityLivingBase getFollowTarget();
/*     */   
/*     */   protected void doMove() {
/* 107 */     this.destinationPos = getDestinationBlock();
/* 108 */     super.doMove();
/* 109 */     this.lastTargetPos = this.followTarget.getPosition();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 114 */     this.followTarget = null;
/* 115 */     super.resetTask();
/*     */   }
/*     */   
/*     */   protected boolean shouldFollow() {
/* 119 */     return this.followTarget.isEntityAlive();
/*     */   }
/*     */   
/*     */   private void updateFollowTick() {
/* 123 */     double distSq = this.followTarget.getDistanceSq((Entity)this.navigator);
/* 124 */     if (distSq < 100.0D) {
/* 125 */       this.followUpdateTick = 10;
/* 126 */     } else if (distSq < 400.0D) {
/* 127 */       this.followUpdateTick = 40;
/*     */     } else {
/* 129 */       this.followUpdateTick = 60;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIFollow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */