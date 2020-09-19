/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityFarmer;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class EntityAIPlantFarm
/*     */   extends EntityAIMoveToBlock {
/*     */   private boolean active = false;
/*  18 */   private int plantTime = 0;
/*  19 */   private IBlockState plantState = null;
/*     */   
/*     */   protected final EntityVillagerTek villager;
/*     */   
/*     */   public EntityAIPlantFarm(EntityVillagerTek entityIn) {
/*  24 */     super((EntityVillageNavigator)entityIn);
/*  25 */     this.villager = entityIn;
/*     */   }
/*     */   
/*     */   private Predicate<BlockPos> isPlantable() {
/*  29 */     return bp -> (this.villager.world.getBlockState(bp).getBlock() == Blocks.FARMLAND && this.villager.world.isAirBlock(bp.up()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  34 */     BlockPos farmPos = this.villager.getVillage().requestFarmland(isPlantable());
/*  35 */     if (farmPos != null) {
/*  36 */       BlockPos cropPos = farmPos.up();
/*  37 */       this.plantState = checkNearbyCrops(cropPos);
/*  38 */       if (this.plantState != null && 
/*  39 */         this.villager.isAIFilterEnabled("plant_" + this.plantState.getBlock().getTranslationKey())) {
/*     */         
/*  41 */         Item seedItem = EntityFarmer.getSeed(this.plantState);
/*  42 */         Predicate<ItemStack> seedPred = i -> (i.getItem() == seedItem);
/*  43 */         if (this.villager.getInventory().getItemCount(seedPred) >= 1) {
/*  44 */           return cropPos;
/*     */         }
/*  46 */         this.villager.setItemThought(seedItem);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  52 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  58 */     if (this.villager.isAITick() && this.villager.hasVillage() && this.villager.isWorkTime()) {
/*  59 */       return super.shouldExecute();
/*     */     }
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  66 */     this.active = true;
/*  67 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  72 */     return this.active;
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/*  77 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  82 */     super.updateTask();
/*  83 */     if (this.plantTime > 0) {
/*     */       
/*  85 */       this.plantTime--;
/*  86 */       if (this.plantTime == 10) {
/*     */         
/*  88 */         stopPlanting();
/*  89 */         plantCrop();
/*     */       }
/*  91 */       else if (this.plantTime <= 0) {
/*  92 */         this.active = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStuck() {
/*  99 */     this.active = false;
/* 100 */     super.onStuck();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 105 */     startPlanting();
/* 106 */     super.onArrival();
/*     */   }
/*     */ 
/*     */   
/*     */   private void startPlanting() {
/* 111 */     float ANIMATION_TIME = 50.0F;
/*     */     
/* 113 */     if (isPlantable().test(this.destinationPos.down())) {
/* 114 */       int animationCycles = this.villager.getSkillLerp(ProfessionType.FARMER, 6, 1);
/* 115 */       this.plantTime = (int)(50.0F * animationCycles / 30.0F * 20.0F) + 10;
/* 116 */       this.villager.getNavigator().clearPath();
/* 117 */       this.villager.playServerAnimation("villager_take");
/*     */     } else {
/*     */       
/* 120 */       this.active = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void stopPlanting() {
/* 126 */     this.villager.stopServerAnimation("villager_take");
/*     */   }
/*     */ 
/*     */   
/*     */   private void plantCrop() {
/* 131 */     if (isNearDestination(4.0D) && isPlantable().test(this.destinationPos.down())) {
/* 132 */       this.villager.world.setBlockState(this.destinationPos, this.plantState, 2);
/* 133 */       this.villager.modifyHunger(-1);
/*     */ 
/*     */       
/* 136 */       Item seedItem = EntityFarmer.getSeed(this.plantState);
/* 137 */       this.villager.getInventory().removeItems(is -> (is.getItem() == seedItem), 1);
/*     */       
/* 139 */       this.villager.tryAddSkill(ProfessionType.FARMER, 16);
/*     */     } 
/*     */   }
/*     */   
/*     */   private IBlockState checkNearbyCrops(BlockPos pos) {
/* 144 */     IBlockState bestState = null;
/* 145 */     Block[] cropBlocks = { Blocks.WHEAT, Blocks.POTATOES, Blocks.CARROTS, Blocks.BEETROOTS };
/* 146 */     for (Block b : cropBlocks) {
/* 147 */       int count = countNearbyCrop(pos, b);
/* 148 */       if (count >= 2)
/* 149 */         return b.getDefaultState(); 
/* 150 */       if (count == 1 && bestState == null) {
/* 151 */         bestState = b.getDefaultState();
/*     */       }
/*     */     } 
/* 154 */     return bestState;
/*     */   }
/*     */   
/*     */   private int countNearbyCrop(BlockPos pos, Block block) {
/* 158 */     int count = 0;
/* 159 */     if (this.villager.world.getBlockState(pos.west()).getBlock() == block) {
/* 160 */       count++;
/*     */     }
/* 162 */     if (this.villager.world.getBlockState(pos.east()).getBlock() == block) {
/* 163 */       count++;
/*     */     }
/* 165 */     if (this.villager.world.getBlockState(pos.north()).getBlock() == block) {
/* 166 */       count++;
/*     */     }
/* 168 */     if (this.villager.world.getBlockState(pos.south()).getBlock() == block) {
/* 169 */       count++;
/*     */     }
/* 171 */     return count;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 176 */     this.active = false;
/* 177 */     stopPlanting();
/* 178 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIPlantFarm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */