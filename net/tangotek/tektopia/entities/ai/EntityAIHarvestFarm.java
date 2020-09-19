/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.ModBlocks;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.VillageFarm;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class EntityAIHarvestFarm extends EntityAIMoveToBlock {
/*     */   private final boolean replant;
/*     */   private boolean active = false;
/*  16 */   private int harvestTime = 0;
/*  17 */   private IBlockState replantState = null;
/*     */   
/*     */   protected final EntityVillagerTek villager;
/*     */   
/*     */   public EntityAIHarvestFarm(EntityVillagerTek entityIn, boolean replant) {
/*  22 */     super((EntityVillageNavigator)entityIn);
/*  23 */     this.replant = replant;
/*  24 */     this.villager = entityIn;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  29 */     if (this.villager.getVillage() != null) {
/*  30 */       BlockPos cropPos = this.villager.getVillage().requestMaxAgeCrop();
/*  31 */       if (cropPos != null && 
/*  32 */         this.villager.isAIFilterEnabled("harvest_" + this.villager.world.getBlockState(cropPos).getBlock().getTranslationKey())) {
/*  33 */         return cropPos;
/*     */       }
/*     */     } 
/*     */     
/*  37 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  43 */     if (this.villager.isAITick() && this.villager.hasVillage() && this.villager.isWorkTime()) {
/*  44 */       return super.shouldExecute();
/*     */     }
/*  46 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/*  51 */     return this.destinationPos;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  56 */     this.active = true;
/*  57 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  62 */     return this.active;
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/*  67 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  72 */     super.updateTask();
/*  73 */     if (this.harvestTime > 0) {
/*     */       
/*  75 */       this.harvestTime--;
/*  76 */       if (this.harvestTime == 30) {
/*     */         
/*  78 */         stopHarvesting();
/*  79 */         gatherResources();
/*     */       }
/*  81 */       else if (this.harvestTime == 0) {
/*     */ 
/*     */         
/*  84 */         this.villager.pickupItems(4);
/*  85 */         this.active = false;
/*     */         
/*  87 */         if (this.replant && this.replantState != null) {
/*  88 */           this.villager.world.setBlockState(this.destinationPos, this.replantState, 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/*  96 */     return (this.harvestTime > 30 || this.harvestTime <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStuck() {
/* 101 */     this.active = false;
/* 102 */     super.onStuck();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 107 */     startHarvesting();
/* 108 */     super.onArrival();
/*     */   }
/*     */ 
/*     */   
/*     */   private void startHarvesting() {
/* 113 */     float ANIMATION_TIME = 60.0F;
/* 114 */     if (VillageFarm.isMaxAgeCrop(this.villager.world, this.destinationPos)) {
/* 115 */       int animationCycles = this.villager.getSkillLerp(ProfessionType.FARMER, 7, 2);
/*     */       
/* 117 */       this.harvestTime = (int)(60.0F * animationCycles / 30.0F * 20.0F) + 30;
/*     */       
/* 119 */       this.villager.getNavigator().clearPath();
/* 120 */       this.villager.playServerAnimation("villager_pickup");
/*     */     } else {
/*     */       
/* 123 */       this.active = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void stopHarvesting() {
/* 129 */     this.villager.stopServerAnimation("villager_pickup");
/*     */   }
/*     */ 
/*     */   
/*     */   private void gatherResources() {
/* 134 */     if (isNearDestination(4.0D)) {
/*     */       
/* 136 */       Block block = this.villager.world.getBlockState(this.destinationPos).getBlock();
/*     */ 
/*     */       
/* 139 */       this.replantState = getReplantFromBlock(block);
/*     */       
/* 141 */       ModBlocks.villagerDestroyBlock(this.destinationPos, this.villager, true, true);
/*     */       
/* 143 */       int ruinerChance = this.villager.getSkillLerp(ProfessionType.FARMER, 2, 10);
/* 144 */       if (this.villager.world.rand.nextInt(ruinerChance) == 0) {
/*     */         
/* 146 */         this.villager.world.setBlockState(this.destinationPos.down(), Blocks.DIRT.getDefaultState());
/* 147 */         this.replantState = null;
/*     */       } 
/*     */       
/* 150 */       this.villager.tryAddSkill(ProfessionType.FARMER, 12);
/* 151 */       this.villager.throttledSadness(-2);
/* 152 */       this.villager.modifyHunger(-1);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected IBlockState getReplantFromBlock(Block block) {
/* 157 */     if (block == Blocks.WHEAT)
/* 158 */       return Blocks.WHEAT.getDefaultState(); 
/* 159 */     if (block == Blocks.POTATOES)
/* 160 */       return Blocks.POTATOES.getDefaultState(); 
/* 161 */     if (block == Blocks.CARROTS)
/* 162 */       return Blocks.CARROTS.getDefaultState(); 
/* 163 */     if (block == Blocks.BEETROOTS) {
/* 164 */       return Blocks.BEETROOTS.getDefaultState();
/*     */     }
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 171 */     stopHarvesting();
/* 172 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIHarvestFarm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */