/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import net.minecraft.block.BlockHorizontal;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class EntityAISleep extends EntityAIMoveToBlock {
/*  14 */   private int bedAxis = -1;
/*     */   protected final EntityVillagerTek villager;
/*     */   private boolean hasSleptToday = false;
/*     */   
/*     */   public EntityAISleep(EntityVillagerTek v) {
/*  19 */     super((EntityVillageNavigator)v);
/*  20 */     this.villager = v;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  25 */     if (this.villager.getBedPos() != null) {
/*  26 */       if (isWalkable(this.villager.getBedPos().up(), this.navigator)) {
/*  27 */         return this.villager.getBedPos().up();
/*     */       }
/*  29 */       return this.villager.getBedPos();
/*     */     } 
/*     */     
/*  32 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  37 */     if (this.villager.isAITick()) {
/*  38 */       if (!this.villager.isSleepingTime()) {
/*  39 */         this.hasSleptToday = false;
/*     */       }
/*     */       
/*  42 */       if (this.villager.hasVillage() && this.villager.getVillage().enemySeenRecently()) {
/*  43 */         return false;
/*     */       }
/*  45 */       if (this.villager.shouldSleep()) {
/*  46 */         this.bedAxis = getBedAxis();
/*  47 */         if (this.bedAxis >= 0) {
/*  48 */           if (!this.hasSleptToday) {
/*  49 */             return super.shouldExecute();
/*     */           }
/*  51 */           this.villager.setThought(EntityVillagerTek.VillagerThought.INSOMNIA);
/*     */         } else {
/*     */           
/*  54 */           this.villager.setThought(EntityVillagerTek.VillagerThought.BED);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  59 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  64 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  70 */     if (!this.villager.shouldSleep()) {
/*  71 */       return false;
/*     */     }
/*     */     
/*  74 */     if (this.villager.isSleeping() && this.bedAxis < 0) {
/*  75 */       return false;
/*     */     }
/*     */     
/*  78 */     if (hasArrived()) {
/*  79 */       return this.villager.isSleeping();
/*     */     }
/*     */     
/*  82 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/*  87 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  93 */     if (this.villager.isSleeping()) {
/*  94 */       this.hasSleptToday = true;
/*  95 */       if (this.villager.getRNG().nextInt(1200) == 0) {
/*  96 */         this.villager.heal(1.0F);
/*     */       }
/*     */       
/*  99 */       if (this.villager.getRNG().nextInt(30) == 0)
/*     */       {
/* 101 */         startSleep();
/*     */       }
/*     */     } 
/*     */     
/* 105 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/* 110 */     return this.destinationPos;
/*     */   }
/*     */   
/*     */   private int getBedAxis() {
/* 114 */     if (this.villager.getBedPos() != null) {
/* 115 */       IBlockState state = this.villager.world.isBlockLoaded(this.villager.getBedPos()) ? this.villager.world.getBlockState(this.villager.getBedPos()) : null;
/* 116 */       boolean isBed = (state != null && state.getBlock().isBed(state, (IBlockAccess)this.villager.world, this.villager.getBedPos(), null));
/* 117 */       EnumFacing enumfacing = (isBed && state.getBlock() instanceof BlockHorizontal) ? (EnumFacing)state.getValue((IProperty)BlockHorizontal.FACING) : null;
/* 118 */       if (enumfacing != null) {
/* 119 */         return enumfacing.getHorizontalIndex();
/*     */       }
/*     */     } 
/* 122 */     return -1;
/*     */   }
/*     */   
/*     */   private Vec3d getSleepPos() {
/* 126 */     BlockPos bp = this.villager.getBedPos().up();
/* 127 */     return new Vec3d(bp.getX() + 0.5D, bp.getY() - 0.3D, bp.getZ() + 0.5D);
/*     */   }
/*     */   
/*     */   private void moveToSleepPos() {
/* 131 */     Vec3d sleepPos = getSleepPos();
/* 132 */     if (this.villager.getPositionVector().squareDistanceTo(sleepPos.x, sleepPos.y, sleepPos.z) > 0.05D) {
/* 133 */       this.villager.setLocationAndAngles(sleepPos.x, sleepPos.y, sleepPos.z, this.villager.rotationYaw, this.villager.rotationPitch);
/* 134 */       this.villager.motionX = 0.0D;
/* 135 */       this.villager.motionY = 0.0D;
/* 136 */       this.villager.motionZ = 0.0D;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 142 */     startSleep();
/* 143 */     super.onArrival();
/*     */   }
/*     */   
/*     */   private void startSleep() {
/* 147 */     this.bedAxis = getBedAxis();
/* 148 */     if (this.bedAxis >= 0) {
/* 149 */       moveToSleepPos();
/* 150 */       this.villager.onStartSleep(this.bedAxis);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 156 */     this.villager.onStopSleep();
/* 157 */     this.bedAxis = -1;
/* 158 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAISleep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */