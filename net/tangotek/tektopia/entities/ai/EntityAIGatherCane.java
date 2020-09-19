/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.tangotek.tektopia.ModBlocks;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.blockfinder.SugarCaneScanner;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class EntityAIGatherCane extends EntityAIMoveToBlock {
/*     */   private final EntityVillagerTek villager;
/*     */   private BlockPos targetPos;
/*     */   private boolean active = false;
/*  20 */   private int gatherTime = 0;
/*     */   
/*     */   private final int maxStorage;
/*     */   
/*     */   public EntityAIGatherCane(EntityVillagerTek entityIn, int maxStorage) {
/*  25 */     super((EntityVillageNavigator)entityIn);
/*  26 */     this.villager = entityIn;
/*  27 */     this.maxStorage = maxStorage;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  32 */     if (this.villager.getVillage() != null) {
/*  33 */       releaseBlockClaim();
/*     */       
/*  35 */       BlockPos bp = this.villager.getVillage().requestBlock((Block)Blocks.REEDS);
/*  36 */       if (bp != null) {
/*  37 */         this.villager.debugOut("GatherCane start " + bp);
/*  38 */         return bp;
/*     */       } 
/*     */     } 
/*     */     
/*  42 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/*  47 */     BlockPos diff = this.villager.getPosition().subtract((Vec3i)this.destinationPos);
/*  48 */     EnumFacing facing = EnumFacing.getFacingFromVector(diff.getX(), 0.0F, diff.getZ());
/*  49 */     BlockPos focusPos = this.destinationPos.offset(facing);
/*     */     
/*  51 */     if (isWalkable(focusPos, (EntityVillageNavigator)this.villager)) {
/*  52 */       return focusPos;
/*     */     }
/*     */     
/*  55 */     BlockPos closest = null;
/*  56 */     double minDist = Double.MAX_VALUE;
/*  57 */     for (int x = -1; x <= 1; x++) {
/*  58 */       for (int z = -1; z <= 1; z++) {
/*  59 */         BlockPos testPos = focusPos.add(x, 0, z);
/*  60 */         if (isWalkable(testPos, (EntityVillageNavigator)this.villager)) {
/*  61 */           double thisDist = testPos.distanceSq((Vec3i)focusPos);
/*  62 */           if (thisDist < minDist) {
/*  63 */             minDist = thisDist;
/*  64 */             closest = testPos;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  70 */     return closest;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  75 */     if (this.villager.isAITick() && this.villager.getVillage() != null && this.villager.isWorkTime() && this.villager.getRNG().nextInt(15) == 0 && this.villager.isAIFilterEnabled("gather_cane")) {
/*  76 */       int storageCount = this.villager.getVillage().getStorageCount(p -> (p.getItem() == Items.REEDS));
/*  77 */       if (this.maxStorage > 0 && storageCount >= this.maxStorage) {
/*  78 */         return false;
/*     */       }
/*  80 */       return super.shouldExecute();
/*     */     } 
/*     */     
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  88 */     this.targetPos = this.destinationPos;
/*  89 */     this.active = true;
/*     */     
/*  91 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  96 */     return this.active;
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 101 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */   
/*     */   public void updateTask() {
/* 105 */     if (this.gatherTime > 0) {
/* 106 */       this.villager.getLookHelper().setLookPosition(this.destinationPos.getX(), this.destinationPos.getY(), this.destinationPos.getZ(), 30.0F, 30.0F);
/* 107 */       this.gatherTime--;
/* 108 */       if (this.gatherTime == 10) {
/* 109 */         gatherBlock();
/*     */       }
/* 111 */       else if (this.gatherTime == 0) {
/*     */         
/* 113 */         this.villager.pickupItems(4);
/* 114 */         this.active = false;
/*     */       } 
/*     */     } else {
/*     */       
/* 118 */       super.updateTask();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/* 124 */     return (this.gatherTime <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStuck() {
/* 129 */     this.active = false;
/* 130 */     super.onStuck();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 135 */     if (isNearDestination(2.5D)) {
/* 136 */       this.villager.debugOut("GatherCane [ " + this.targetPos.getX() + ", " + this.targetPos.getZ() + "] Arrived at Destination: " + getWalkPos());
/* 137 */       if (SugarCaneScanner.getCaneStalk(this.villager.world, this.targetPos) != null) {
/* 138 */         startGathering();
/* 139 */         super.onArrival();
/*     */       } else {
/* 141 */         this.villager.debugOut("GatherCane [ " + this.targetPos.getX() + ", " + this.targetPos.getZ() + "] Uhhh.  No cane here?");
/* 142 */         this.active = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPathFailed(BlockPos pos) {
/* 149 */     this.active = false;
/* 150 */     super.onPathFailed(pos);
/*     */   }
/*     */   
/*     */   private void startGathering() {
/* 154 */     this.villager.debugOut("GatherCane [ " + this.targetPos.getX() + ", " + this.targetPos.getZ() + "] Starting");
/* 155 */     this.gatherTime = 36;
/* 156 */     this.villager.getNavigator().clearPath();
/*     */     
/* 158 */     this.villager.playServerAnimation("villager_take");
/*     */   }
/*     */ 
/*     */   
/*     */   private void stopChopping() {
/* 163 */     this.villager.stopServerAnimation("villager_take");
/*     */   }
/*     */   
/*     */   private void gatherBlock() {
/* 167 */     int count = 0;
/* 168 */     if (isNearDestination(3.0D)) {
/*     */ 
/*     */       
/* 171 */       BlockPos reedPos = this.targetPos;
/* 172 */       while (SugarCaneScanner.isCane(this.villager.world.getBlockState(reedPos.up()))) {
/* 173 */         reedPos = reedPos.up();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 178 */       while (SugarCaneScanner.isCane(this.villager.world.getBlockState(reedPos.down()))) {
/* 179 */         ModBlocks.villagerDestroyBlock(reedPos, this.villager, true, true);
/* 180 */         reedPos = reedPos.down();
/* 181 */         count++;
/*     */       } 
/*     */       
/* 184 */       this.villager.tryAddSkill(ProfessionType.FARMER, 9);
/* 185 */       this.villager.throttledSadness(-2);
/* 186 */       this.villager.modifyHunger(-2);
/*     */     } 
/* 188 */     this.villager.debugOut("GatherCane [ " + this.targetPos.getX() + ", " + this.targetPos.getZ() + "] Chopped: " + count);
/*     */   }
/*     */   
/*     */   private void releaseBlockClaim() {
/* 192 */     if (this.targetPos != null && this.villager.getVillage() != null) {
/* 193 */       this.villager.getVillage().releaseBlockClaim((Block)Blocks.REEDS, this.targetPos);
/* 194 */       this.targetPos = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetTask() {
/* 199 */     stopChopping();
/* 200 */     releaseBlockClaim();
/* 201 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIGatherCane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */