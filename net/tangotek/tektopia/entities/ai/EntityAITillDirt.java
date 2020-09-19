/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityFarmer;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class EntityAITillDirt extends EntityAIMoveToBlock {
/*     */   private boolean active = false;
/*  17 */   private int hoeTime = 0;
/*     */   
/*     */   private ItemStack bestTool;
/*     */   protected final EntityVillagerTek villager;
/*     */   
/*     */   public EntityAITillDirt(EntityVillagerTek entityIn, Predicate<EntityVillagerTek> shouldPred) {
/*  23 */     super((EntityVillageNavigator)entityIn);
/*  24 */     this.villager = entityIn;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  29 */     if (this.villager.getVillage() != null) {
/*  30 */       BlockPos cropPos = this.villager.getVillage().requestFarmland(block -> canTill(block));
/*  31 */       if (cropPos != null) {
/*  32 */         return cropPos.up();
/*     */       }
/*     */     } 
/*     */     
/*  36 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  42 */     if (this.villager.isAITick() && this.villager.hasVillage() && this.villager.isWorkTime() && this.villager.isAIFilterEnabled("till_dirt")) {
/*  43 */       List<ItemStack> toolList = this.villager.getInventory().getItems(EntityFarmer.getBestHoe(), 1);
/*  44 */       if (!toolList.isEmpty()) {
/*  45 */         this.bestTool = toolList.get(0);
/*  46 */         return super.shouldExecute();
/*     */       } 
/*  48 */       this.villager.setThought(EntityVillagerTek.VillagerThought.HOE);
/*  49 */       this.bestTool = null;
/*     */     } 
/*     */ 
/*     */     
/*  53 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  58 */     this.active = true;
/*  59 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  65 */     return this.active;
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/*  70 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  75 */     super.updateTask();
/*  76 */     if (this.hoeTime > 0) {
/*     */       
/*  78 */       this.hoeTime--;
/*  79 */       if (this.hoeTime == 10) {
/*     */         
/*  81 */         stopHoeing();
/*  82 */         if (canTill(this.destinationPos.down())) {
/*  83 */           this.villager.world.setBlockState(this.destinationPos.down(), Blocks.FARMLAND.getDefaultState(), 3);
/*  84 */           this.villager.world.setBlockToAir(this.destinationPos);
/*     */         } 
/*  86 */         this.villager.tryAddSkill(ProfessionType.FARMER, 14);
/*     */ 
/*     */         
/*  89 */         this.villager.damageItem(this.bestTool, 3);
/*  90 */         this.villager.modifyHunger(-2);
/*     */       
/*     */       }
/*  93 */       else if (this.hoeTime <= 0) {
/*  94 */         this.active = false;
/*  95 */         this.villager.throttledSadness(-3);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/* 102 */     return this.destinationPos;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStuck() {
/* 107 */     this.active = false;
/* 108 */     super.onStuck();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 113 */     startHoeing();
/* 114 */     super.onArrival();
/*     */   }
/*     */   
/*     */   private boolean canTill(BlockPos bp) {
/* 118 */     Block block = this.villager.world.getBlockState(bp).getBlock();
/* 119 */     if (block == Blocks.DIRT || block == Blocks.GRASS) {
/* 120 */       return this.villager.world.isAirBlock(bp.up());
/*     */     }
/*     */     
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void startHoeing() {
/* 128 */     if (canTill(this.destinationPos.down())) {
/* 129 */       int reductionTime = 40 * this.villager.getSkillLerp(ProfessionType.FARMER, 0, 3);
/* 130 */       this.hoeTime = 250 - reductionTime;
/*     */       
/* 132 */       if (this.bestTool.getItem() == Items.WOODEN_HOE) {
/* 133 */         this.hoeTime += 80;
/*     */       }
/* 135 */       this.villager.getNavigator().clearPath();
/* 136 */       this.villager.playServerAnimation("villager_hoe");
/* 137 */       this.villager.equipActionItem(this.bestTool);
/*     */     } else {
/*     */       
/* 140 */       this.active = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void stopHoeing() {
/* 146 */     this.villager.stopServerAnimation("villager_hoe");
/* 147 */     this.villager.unequipActionItem();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 152 */     super.resetTask();
/* 153 */     this.active = false;
/* 154 */     this.hoeTime = 0;
/* 155 */     stopHoeing();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAITillDirt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */