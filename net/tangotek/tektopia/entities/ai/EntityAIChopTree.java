/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.BlockLeaves;
/*     */ import net.minecraft.block.BlockOldLog;
/*     */ import net.minecraft.block.BlockPlanks;
/*     */ import net.minecraft.block.BlockSapling;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Enchantments;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemTool;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.tangotek.tektopia.ModBlocks;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.blockfinder.TreeScanner;
/*     */ import net.tangotek.tektopia.entities.EntityLumberjack;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class EntityAIChopTree
/*     */   extends EntityAIMoveToBlock {
/*     */   private final boolean replant;
/*     */   private final EntityVillagerTek villager;
/*     */   private BlockPlanks.EnumType treeType;
/*     */   private BlockPos treePos;
/*     */   private ItemStack bestAxe;
/*     */   private boolean active = false;
/*  35 */   private int chopTime = 0;
/*     */ 
/*     */   
/*     */   public EntityAIChopTree(EntityVillagerTek entityIn, boolean replant) {
/*  39 */     super((EntityVillageNavigator)entityIn);
/*  40 */     this.replant = replant;
/*  41 */     this.villager = entityIn;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  46 */     if (this.villager.getVillage() != null) {
/*  47 */       releaseTreeClaim();
/*     */       
/*  49 */       BlockPos treePos = this.villager.getVillage().requestBlock(Blocks.LOG);
/*     */       
/*  51 */       BlockPlanks.EnumType treeType = (BlockPlanks.EnumType)this.villager.world.getBlockState(treePos).getValue((IProperty)BlockOldLog.VARIANT);
/*  52 */       if (treePos != null && this.villager.isAIFilterEnabled("chop_tree_" + treeType.getName()))
/*     */       {
/*  54 */         return treePos;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  59 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/*  64 */     BlockPos diff = this.villager.getPosition().subtract((Vec3i)this.destinationPos);
/*  65 */     EnumFacing facing = EnumFacing.getFacingFromVector(diff.getX(), 0.0F, diff.getZ());
/*  66 */     BlockPos focusPos = this.destinationPos.offset(facing);
/*     */     
/*  68 */     if (isWalkable(focusPos, (EntityVillageNavigator)this.villager)) {
/*  69 */       return focusPos;
/*     */     }
/*     */     
/*  72 */     BlockPos closest = null;
/*  73 */     double minDist = Double.MAX_VALUE;
/*  74 */     for (int x = -3; x <= 3; x++) {
/*  75 */       for (int z = -3; z <= 3; z++) {
/*  76 */         BlockPos testPos = focusPos.add(x, 0, z);
/*  77 */         if (isWalkable(testPos, (EntityVillageNavigator)this.villager)) {
/*  78 */           double thisDist = testPos.distanceSq((Vec3i)focusPos);
/*  79 */           if (thisDist < minDist) {
/*  80 */             minDist = thisDist;
/*  81 */             closest = testPos;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  87 */     return closest;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  92 */     if (this.villager.isAITick() && this.villager.hasVillage() && this.villager.isWorkTime() && this.villager.getVillage().hasBlock(Blocks.LOG)) {
/*     */       
/*  94 */       List<ItemStack> axeList = this.villager.getInventory().getItems(EntityLumberjack.getBestAxe(this.villager), 1);
/*  95 */       if (!axeList.isEmpty()) {
/*  96 */         this.bestAxe = axeList.get(0);
/*  97 */         return super.shouldExecute();
/*     */       } 
/*     */       
/* 100 */       this.villager.setThought(EntityVillagerTek.VillagerThought.AXE);
/* 101 */       this.bestAxe = null;
/*     */     } 
/*     */ 
/*     */     
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/* 110 */     this.treePos = this.destinationPos;
/* 111 */     this.active = true;
/* 112 */     this.chopTime = 0;
/* 113 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 118 */     return (this.active && !this.bestAxe.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 123 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */   
/*     */   public void updateTask() {
/* 127 */     super.updateTask();
/* 128 */     if (this.chopTime > 0) {
/* 129 */       this.chopTime--;
/*     */       
/* 131 */       if (this.chopTime == 30) {
/* 132 */         stopChopping();
/* 133 */         chopTree();
/* 134 */       } else if (this.chopTime == 0) {
/*     */         
/* 136 */         pickUpItems();
/* 137 */         this.active = false;
/*     */         
/* 139 */         if (this.replant && this.treeType != null && consumeSaplingItem(this.treeType)) {
/* 140 */           this.villager.world.setBlockState(this.treePos, Blocks.SAPLING.getDefaultState().withProperty((IProperty)BlockSapling.TYPE, (Comparable)this.treeType), 2);
/*     */         }
/*     */       } 
/*     */       
/* 144 */       if (this.chopTime >= 30 && (
/* 145 */         this.chopTime - 30) % 20 == 0 && 
/* 146 */         this.villager.getRNG().nextInt(this.villager.getSkillLerp(ProfessionType.LUMBERJACK, 2, 6)) == 0) {
/* 147 */         this.villager.damageItem(this.bestAxe, 1);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 152 */       this.villager.faceLocation(this.treePos.getX(), this.treePos.getZ(), 30.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/* 159 */     return (this.chopTime > 30 || this.chopTime <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStuck() {
/* 164 */     this.active = false;
/* 165 */     super.onStuck();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean attemptStuckFix() {
/* 170 */     int trimmed = trimLeaves();
/* 171 */     return (trimmed > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 176 */     if (isNearDestination(2.5D)) {
/*     */       
/* 178 */       if (TreeScanner.treeTest(this.villager.world, this.treePos) != null) {
/* 179 */         startChopping();
/* 180 */         super.onArrival();
/*     */       } else {
/*     */         
/* 183 */         this.active = false;
/*     */       }
/*     */     
/* 186 */     } else if (isNearWalkPos()) {
/*     */       
/* 188 */       int trimmed = trimLeaves();
/* 189 */       if (trimmed > 0) {
/* 190 */         doMove();
/*     */       } else {
/* 192 */         this.active = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPathFailed(BlockPos pos) {
/* 199 */     this.active = false;
/* 200 */     super.onPathFailed(pos);
/*     */   }
/*     */   
/*     */   private static int getToolMaterialValue(ItemTool tool) {
/* 204 */     if (tool.getToolMaterialName().equals("STONE"))
/* 205 */       return 25; 
/* 206 */     if (tool.getToolMaterialName().equals("IRON"))
/* 207 */       return 35; 
/* 208 */     if (tool.getToolMaterialName().equals("DIAMOND")) {
/* 209 */       return 45;
/*     */     }
/* 211 */     return 15;
/*     */   }
/*     */   
/*     */   public static int getChopCount(EntityVillagerTek villager, ItemStack bestAxe) {
/* 215 */     int skill = villager.getSkill(ProfessionType.LUMBERJACK);
/* 216 */     int enchantLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, bestAxe);
/* 217 */     int materialValue = getToolMaterialValue((ItemTool)bestAxe.getItem());
/* 218 */     float damagePerChop = skill + 70.0F + materialValue + (enchantLevel * 8);
/* 219 */     return Math.max(Math.round(1500.0F / damagePerChop), 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void startChopping() {
/* 225 */     int chopCount = getChopCount(this.villager, this.bestAxe);
/* 226 */     this.chopTime = chopCount * 20 + 30;
/* 227 */     this.villager.getNavigator().clearPath();
/* 228 */     this.villager.equipActionItem(this.bestAxe);
/* 229 */     this.villager.playServerAnimation("villager_chop");
/*     */   }
/*     */   
/*     */   private void stopChopping() {
/* 233 */     this.villager.unequipActionItem();
/* 234 */     this.villager.stopServerAnimation("villager_chop");
/*     */   }
/*     */   
/*     */   private void chopTree() {
/* 238 */     int logsChopped = 0;
/* 239 */     IBlockState blockState = this.villager.world.getBlockState(this.treePos);
/* 240 */     if (TreeScanner.isLog(blockState)) {
/* 241 */       this.treeType = (BlockPlanks.EnumType)blockState.getValue((IProperty)BlockOldLog.VARIANT);
/* 242 */       switch (this.treeType) {
/*     */         case OAK:
/* 244 */           logsChopped = chopTreeOak(this.treePos);
/*     */           break;
/*     */         case BIRCH:
/*     */         case JUNGLE:
/*     */         case SPRUCE:
/* 249 */           logsChopped = chopTreeStraight(this.treePos);
/*     */           break;
/*     */       } 
/* 252 */       if (logsChopped > 0) {
/* 253 */         this.villager.throttledSadness(-2);
/* 254 */         this.villager.tryAddSkill(ProfessionType.LUMBERJACK, 7);
/*     */         
/* 256 */         this.villager.modifyHunger(-logsChopped / 2);
/* 257 */         this.villager.debugOut("ChopTree [ " + this.treePos.getX() + ", " + this.treePos.getZ() + "] Chopped: " + logsChopped);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int chopTreeOak(BlockPos treePos) {
/* 263 */     int count = 0;
/* 264 */     BlockPos trunkPos = treePos;
/* 265 */     int skill = this.villager.getSkill(ProfessionType.LUMBERJACK);
/* 266 */     while (chopLog(trunkPos, (count == 0) ? true : logDropCheck(skill), false)) {
/* 267 */       trunkPos = trunkPos.up();
/* 268 */       count++;
/*     */     } 
/*     */ 
/*     */     
/* 272 */     if (count >= 6) {
/* 273 */       trunkPos = treePos.up(3);
/* 274 */       for (BlockPos branchPos : BlockPos.getAllInBox(trunkPos.add(-5, 0, -5), trunkPos.add(5, 9, 5))) {
/* 275 */         if (chopLog(branchPos, logDropCheck(skill), true)) {
/* 276 */           count++;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 281 */     return count;
/*     */   }
/*     */   
/*     */   private int chopTreeStraight(BlockPos treePos) {
/* 285 */     int count = 0;
/* 286 */     int skill = this.villager.getSkill(ProfessionType.LUMBERJACK);
/* 287 */     while (chopLog(treePos, (count == 0) ? true : logDropCheck(skill), false)) {
/* 288 */       treePos = treePos.up();
/* 289 */       count++;
/*     */     } 
/*     */     
/* 292 */     return count;
/*     */   }
/*     */   
/*     */   private boolean logDropCheck(int skill) {
/* 296 */     return (this.villager.getRNG().nextInt(5) == 0);
/*     */   }
/*     */   
/*     */   private boolean hasAdjacentLeaf(BlockPos pos) {
/* 300 */     return (TreeScanner.isLeaf(this.villager.world.getBlockState(pos.west())) || 
/* 301 */       TreeScanner.isLeaf(this.villager.world.getBlockState(pos.east())) || 
/* 302 */       TreeScanner.isLeaf(this.villager.world.getBlockState(pos.north())) || 
/* 303 */       TreeScanner.isLeaf(this.villager.world.getBlockState(pos.south())) || 
/* 304 */       TreeScanner.isLeaf(this.villager.world.getBlockState(pos.up())));
/*     */   }
/*     */   
/*     */   private boolean chopLog(BlockPos pos, boolean dropBlock, boolean adjacentLeafCheck) {
/* 308 */     if (TreeScanner.isLog(this.villager.world.getBlockState(pos)) && (!adjacentLeafCheck || hasAdjacentLeaf(pos))) {
/* 309 */       ModBlocks.villagerDestroyBlock(pos, this.villager, dropBlock, true);
/*     */ 
/*     */       
/* 312 */       if (this.treeType == BlockPlanks.EnumType.JUNGLE && this.villager.getRNG().nextInt(8) == 0) {
/* 313 */         this.villager.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 3), 0.0F);
/*     */       }
/* 315 */       return true;
/*     */     } 
/*     */     
/* 318 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean consumeSaplingItem(BlockPlanks.EnumType blockType) {
/* 324 */     List<ItemStack> removedSaplings = this.villager.getInventory().removeItems(is -> (EntityLumberjack.isSapling().test(is) && is.getMetadata() == blockType.getMetadata()), 1);
/* 325 */     return !removedSaplings.isEmpty();
/*     */   }
/*     */   
/*     */   private int trimLeaves() {
/* 329 */     BlockPos pos = this.villager.getPosition();
/* 330 */     int trimmed = 0;
/* 331 */     for (BlockPos bp : BlockPos.getAllInBox(pos.getX() - 2, pos.getY(), pos.getZ() - 2, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2)) {
/* 332 */       IBlockState blockState = this.villager.world.getBlockState(bp);
/* 333 */       if (TreeScanner.isLeaf(blockState) && ((Boolean)blockState.getValue((IProperty)BlockLeaves.DECAYABLE)).booleanValue()) {
/* 334 */         blockState.getBlock().dropBlockAsItem(this.villager.world, bp, blockState, 0);
/* 335 */         this.villager.world.setBlockToAir(bp);
/* 336 */         trimmed++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 341 */     return trimmed;
/*     */   }
/*     */   
/*     */   private void pickUpItems() {
/* 345 */     if (!this.villager.isDead) {
/* 346 */       for (EntityItem entityitem : this.villager.world.getEntitiesWithinAABB(EntityItem.class, this.villager.getEntityBoundingBox().grow(8.0D, 12.0D, 8.0D))) {
/* 347 */         if (!entityitem.isDead && !entityitem.getItem().isEmpty() && !entityitem.cannotPickup()) {
/* 348 */           this.villager.updateEquipmentIfNeeded(entityitem);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void releaseTreeClaim() {
/* 355 */     if (this.treePos != null && this.villager.getVillage() != null) {
/* 356 */       this.villager.getVillage().releaseBlockClaim(Blocks.LOG, this.treePos);
/* 357 */       this.treePos = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetTask() {
/* 362 */     stopChopping();
/* 363 */     this.active = false;
/* 364 */     releaseTreeClaim();
/* 365 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIChopTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */