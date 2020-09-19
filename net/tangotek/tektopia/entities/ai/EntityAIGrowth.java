/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.IGrowable;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityDruid;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class EntityAIGrowth
/*     */   extends EntityAIMoveToBlock
/*     */ {
/*     */   private boolean arrived = false;
/*  20 */   private int castTime = 0;
/*  21 */   private int castIterations = 0; private EntityDruid druid; private final double range;
/*     */   private GrowthType growthType;
/*  23 */   private final int CAST_TIME = 90;
/*     */   private BlockPos growthPos;
/*     */   
/*  26 */   private enum GrowthType { FARM,
/*  27 */     SAPLING; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityAIGrowth(EntityVillagerTek entityIn, double range) {
/*  34 */     super((EntityVillageNavigator)entityIn);
/*  35 */     this.druid = (EntityDruid)entityIn;
/*  36 */     this.range = range;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  41 */     this.growthPos = null;
/*  42 */     if (this.druid.isAITick() && this.druid.getRNG().nextInt(10) == 0 && this.druid.hasVillage() && this.druid.isWorkTime() && this.druid.isGrowTime()) {
/*  43 */       boolean trySapling = this.druid.getRNG().nextBoolean();
/*  44 */       if (trySapling && this.druid.isAIFilterEnabled("cast_growth_trees")) {
/*     */         
/*  46 */         this.growthType = GrowthType.SAPLING;
/*  47 */         this.growthPos = this.druid.getVillage().requestBlock(Blocks.SAPLING);
/*     */       }
/*  49 */       else if (this.druid.isAIFilterEnabled("cast_growth_crops")) {
/*     */         
/*  51 */         this.growthType = GrowthType.FARM;
/*  52 */         BlockPos farmPos = this.druid.getVillage().requestFarmland(bp -> canBlockGrow(bp));
/*  53 */         if (farmPos != null) {
/*  54 */           this.growthPos = farmPos.up();
/*     */         }
/*     */       } 
/*     */       
/*  58 */       if (this.growthPos != null) {
/*  59 */         return super.shouldExecute();
/*     */       }
/*     */     } 
/*     */     
/*  63 */     return false;
/*     */   }
/*     */   
/*     */   private boolean canBlockGrow(BlockPos bp) {
/*  67 */     BlockPos cropPos = bp.up();
/*  68 */     IBlockState cropState = this.druid.world.getBlockState(cropPos);
/*  69 */     if (cropState.getBlock() instanceof IGrowable) {
/*  70 */       IGrowable growable = (IGrowable)cropState.getBlock();
/*  71 */       if (growable.canGrow(this.druid.world, cropPos, cropState, false)) {
/*  72 */         return true;
/*     */       }
/*     */     } 
/*  75 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  80 */     super.startExecuting();
/*  81 */     this.castIterations = MathHelper.clamp(this.druid.getSkill(ProfessionType.DRUID) / 20, 1, 5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  86 */     return this.growthPos;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/*  91 */     if (!this.arrived) {
/*  92 */       tryCast();
/*     */     }
/*  94 */     this.arrived = true;
/*  95 */     super.onArrival();
/*     */   }
/*     */   
/*     */   protected boolean inRange() {
/*  99 */     return (this.growthPos.distanceSq((Vec3i)this.druid.getPosition()) < this.range * this.range);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNearWalkPos() {
/* 104 */     return inRange();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/* 110 */     return (this.castTime <= 0);
/*     */   }
/*     */   
/*     */   protected void tryCast() {
/* 114 */     if (this.druid.isEntityAlive() && inRange()) {
/* 115 */       this.castTime = 90;
/* 116 */       this.druid.playServerAnimation("villager_cast_grow");
/* 117 */       this.druid.modifyHunger(-2);
/* 118 */       this.druid.getNavigator().clearPath();
/* 119 */       this.druid.setSpellBlock(this.growthPos);
/* 120 */       setArrived();
/*     */       
/* 122 */       this.druid.playSound(ModSoundEvents.healingSource);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 129 */     this.castTime--;
/* 130 */     if (this.castTime == 48) {
/* 131 */       this.druid.playSound(ModSoundEvents.villagerEnchant);
/* 132 */       this.druid.setSpellBlock(this.growthPos);
/*     */       
/* 134 */       if (this.growthType == GrowthType.SAPLING) {
/* 135 */         int growthCount = this.druid.getSkillLerp(ProfessionType.DRUID, 6, 20);
/* 136 */         for (int i = 0; i < growthCount; i++) {
/* 137 */           growBlock(this.growthPos);
/*     */         }
/*     */       }
/* 140 */       else if (this.growthType == GrowthType.FARM) {
/* 141 */         int range = this.druid.getGrowthRange();
/* 142 */         for (int x = -range; x <= range; x++) {
/* 143 */           for (int z = -range; z <= range; z++) {
/* 144 */             BlockPos localPos = this.growthPos.east(x).north(z);
/* 145 */             growBlock(localPos);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 150 */       if (this.castIterations == 0) {
/* 151 */         this.druid.tryAddSkill(ProfessionType.DRUID, 12);
/*     */       }
/*     */     } 
/* 154 */     if (this.castTime >= 0) {
/* 155 */       this.druid.faceLocation(this.growthPos.getX(), this.growthPos.getZ(), 30.0F);
/*     */     }
/* 157 */     if (this.castTime <= 0 && this.castIterations > 0) {
/* 158 */       this.castIterations--;
/* 159 */       this.druid.stopServerAnimation("villager_cast_grow");
/* 160 */       this.druid.setSpellBlock(null);
/* 161 */       tryCast();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 166 */     super.updateTask();
/*     */   }
/*     */   
/*     */   private void growBlock(BlockPos pos) {
/* 170 */     if (this.druid.hasVillage()) {
/* 171 */       IBlockState growthState = this.druid.getVillage().getWorld().getBlockState(pos);
/* 172 */       Block growthBlock = growthState.getBlock();
/* 173 */       if (growthBlock instanceof IGrowable) {
/* 174 */         IGrowable growable = (IGrowable)growthBlock;
/* 175 */         if (growable.canGrow(this.druid.world, pos, growthState, false) && 
/* 176 */           growable.canUseBonemeal(this.druid.world, this.druid.getRNG(), pos, growthState)) {
/* 177 */           growable.grow(this.druid.world, this.druid.getRNG(), pos, growthState);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 186 */     if (this.druid.hasVillage()) {
/* 187 */       this.druid.getVillage().releaseBlockClaim(Blocks.SAPLING, this.growthPos);
/*     */     }
/* 189 */     this.druid.setSpellBlock(null);
/* 190 */     this.druid.stopServerAnimation("villager_cast_grow");
/* 191 */     this.arrived = false;
/* 192 */     this.castTime = 0;
/* 193 */     super.resetTask();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 198 */     if (this.castTime > 0) {
/* 199 */       return true;
/*     */     }
/* 201 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 206 */     this.druid.setMovementMode(this.druid.getDefaultMovement());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIGrowth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */