/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Enchantments;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemTool;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModBlocks;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityMiner;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.structures.VillageStructureMineshaft;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ public class EntityAIMining
/*     */   extends EntityAIMoveToBlock
/*     */ {
/*     */   private enum MiningPhase
/*     */   {
/*  33 */     WALKING_TO_TUNNEL,
/*  34 */     BREAK_TUNNEL,
/*  35 */     ORE_SCAN,
/*  36 */     REPAIR_TUNNEL;
/*     */   }
/*     */   
/*     */   private boolean active = false;
/*     */   
/*     */   private BlockPos miningPos;
/*     */   private BlockPos orePos;
/*     */   private VillageStructureMineshaft mineshaft;
/*  44 */   private int mineTime = 0;
/*  45 */   private MiningPhase phase = null;
/*  46 */   private ItemStack toolUsed = null;
/*     */   
/*     */   protected final EntityVillagerTek villager;
/*     */   private final Predicate<EntityVillagerTek> shouldPred;
/*     */   
/*     */   public EntityAIMining(EntityVillagerTek entityIn, Predicate<EntityVillagerTek> shouldPred) {
/*  52 */     super((EntityVillageNavigator)entityIn);
/*  53 */     this.villager = entityIn;
/*  54 */     this.shouldPred = shouldPred;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  59 */     if (this.villager.getVillage() != null) {
/*  60 */       this.mineshaft = this.villager.getVillage().requestMineshaft(this.villager, m -> m.canMine(), (C, B) -> (C.getTunnelLength() < B.getTunnelLength()));
/*  61 */       if (this.mineshaft != null) {
/*  62 */         return this.mineshaft.getWalkingPos();
/*     */       }
/*     */     } 
/*     */     
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/*  71 */     return this.destinationPos;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  77 */     if (this.villager.isAITick("mining") && this.villager.hasVillage() && !this.villager.isHungry() && this.shouldPred.test(this.villager)) {
/*  78 */       this.toolUsed = null;
/*  79 */       List<ItemStack> toolList = this.villager.getInventory().getItems(EntityMiner.getBestPick(this.villager), 1);
/*  80 */       if (!toolList.isEmpty()) {
/*  81 */         if (this.villager.getInventory().getItemCount(EntityMiner.isTorch()) < 10) {
/*  82 */           this.villager.setThought(EntityVillagerTek.VillagerThought.TORCH);
/*     */         } else {
/*     */           
/*  85 */           this.toolUsed = toolList.get(0);
/*  86 */           return super.shouldExecute();
/*     */         } 
/*     */       } else {
/*  89 */         this.villager.setThought(EntityVillagerTek.VillagerThought.PICK);
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateFacing() {
/*  98 */     if (this.phase == MiningPhase.WALKING_TO_TUNNEL) {
/*  99 */       super.updateFacing();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 108 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/* 113 */     this.active = true;
/* 114 */     this.miningPos = this.mineshaft.getMiningPos();
/* 115 */     this.phase = MiningPhase.WALKING_TO_TUNNEL;
/* 116 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 121 */     return (this.active && this.shouldPred.test(this.villager) && !this.villager.isHungry() && (this.mineshaft.getTunnelMiner() == null || this.mineshaft.getTunnelMiner() == this.villager) && !this.toolUsed.isEmpty());
/*     */   }
/*     */   
/*     */   public void updateTask() {
/* 125 */     super.updateTask();
/* 126 */     if (this.phase == MiningPhase.BREAK_TUNNEL) {
/* 127 */       if (this.mineTime > 0) {
/* 128 */         this.mineTime--;
/* 129 */         if (this.mineTime == 20) {
/* 130 */           stopMining();
/*     */           
/* 132 */           if (!VillageStructureMineshaft.isAir(this.mineshaft, this.miningPos.up())) {
/* 133 */             mineBlock(this.miningPos.up(), 15);
/* 134 */           } else if (!VillageStructureMineshaft.isAir(this.mineshaft, this.miningPos)) {
/* 135 */             mineBlock(this.miningPos, 15);
/*     */           } 
/* 137 */         } else if (this.mineTime == 1 && (
/* 138 */           !VillageStructureMineshaft.isAir(this.mineshaft, this.miningPos.up()) || !VillageStructureMineshaft.isAir(this.mineshaft, this.miningPos))) {
/* 139 */           startMining();
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 144 */         this.phase = MiningPhase.ORE_SCAN;
/*     */       }
/*     */     
/* 147 */     } else if (this.phase == MiningPhase.ORE_SCAN) {
/* 148 */       if (this.mineTime > 0) {
/* 149 */         this.mineTime--;
/* 150 */         if (this.mineTime == 20) {
/* 151 */           stopMining();
/* 152 */           mineBlock(this.orePos, 5);
/*     */         } 
/*     */       } else {
/*     */         
/* 156 */         this.orePos = this.mineshaft.findOre(this.miningPos, 0, EnumFacing.DOWN);
/* 157 */         if (this.orePos == null) {
/* 158 */           this.orePos = this.mineshaft.findOre(this.miningPos.up(), 0, EnumFacing.UP);
/*     */         }
/*     */         
/* 161 */         if (this.orePos == null) {
/* 162 */           this.phase = MiningPhase.REPAIR_TUNNEL;
/*     */         } else {
/*     */           
/* 165 */           startMining();
/*     */         } 
/*     */       } 
/* 168 */     } else if (this.phase == MiningPhase.REPAIR_TUNNEL) {
/* 169 */       if (this.mineTime > 0) {
/* 170 */         this.mineTime--;
/*     */       }
/* 172 */       else if (!repairTunnel(this.miningPos)) {
/* 173 */         this.mineshaft.tryPlaceTorch(this.villager);
/* 174 */         this.active = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStuck() {
/* 181 */     this.active = false;
/* 182 */     super.onStuck();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 187 */     if (this.phase == MiningPhase.WALKING_TO_TUNNEL) {
/*     */       
/* 189 */       this.phase = MiningPhase.BREAK_TUNNEL;
/* 190 */       if (!VillageStructureMineshaft.isAir(this.mineshaft, this.miningPos.up()) || !VillageStructureMineshaft.isAir(this.mineshaft, this.miningPos)) {
/* 191 */         startMining();
/*     */       }
/*     */     } 
/*     */     
/* 195 */     super.onArrival();
/*     */   }
/*     */   
/*     */   protected void onPathFailed(BlockPos pos) {
/* 199 */     if (this.phase == MiningPhase.WALKING_TO_TUNNEL && 
/* 200 */       this.villager.getVillage() != null) {
/* 201 */       System.err.println("Failed to find path to mining tunnel - " + this.miningPos);
/*     */     }
/*     */     
/* 204 */     super.onPathFailed(pos);
/*     */   }
/*     */   
/*     */   private boolean repairTunnel(BlockPos pos) {
/* 208 */     BlockPos flawPos = VillageStructureMineshaft.getTunnelFlaw(this.mineshaft, pos);
/* 209 */     if (flawPos != null) {
/* 210 */       this.villager.world.setBlockState(flawPos, Blocks.COBBLESTONE.getDefaultState(), 2);
/* 211 */       this.mineTime = 30;
/* 212 */       return true;
/*     */     } 
/*     */     
/* 215 */     return false;
/*     */   }
/*     */   
/*     */   private static int getToolMaterialValue(ItemTool tool) {
/* 219 */     if (tool.getToolMaterialName().equals("STONE"))
/* 220 */       return 15; 
/* 221 */     if (tool.getToolMaterialName().equals("IRON"))
/* 222 */       return 9; 
/* 223 */     if (tool.getToolMaterialName().equals("DIAMOND")) {
/* 224 */       return 6;
/*     */     }
/* 226 */     return 20;
/*     */   }
/*     */   
/*     */   public static int getSwingCount(EntityVillagerTek villager, ItemStack toolUsed) {
/* 230 */     int skill = villager.getSkill(ProfessionType.MINER);
/* 231 */     int enchantLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, toolUsed);
/* 232 */     int materialValue = getToolMaterialValue((ItemTool)toolUsed.getItem());
/* 233 */     float enchantMult = (10.0F - enchantLevel) / 10.0F;
/* 234 */     float damagePerChop = ((int)((skill + 50.0F) / materialValue * enchantMult) + 3);
/*     */     
/* 236 */     return Math.max(Math.round(80.0F / damagePerChop), 1);
/*     */   }
/*     */   
/*     */   private void startMining() {
/* 240 */     if (!this.toolUsed.isEmpty()) {
/* 241 */       int swingCount = getSwingCount(this.villager, this.toolUsed);
/* 242 */       this.mineTime = swingCount * 20 + 20;
/* 243 */       this.villager.getNavigator().clearPath();
/* 244 */       this.villager.equipActionItem(this.toolUsed);
/* 245 */       this.villager.playServerAnimation("villager_chop");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void stopMining() {
/* 250 */     this.villager.unequipActionItem();
/* 251 */     this.villager.stopServerAnimation("villager_chop");
/*     */   }
/*     */   
/*     */   private void mineBlock(BlockPos minePos, int skillChance) {
/* 255 */     boolean dropBlock = VillageStructureMineshaft.isOre(this.villager.world, minePos);
/* 256 */     if (this.villager.world.getBlockState(minePos).getBlock() == Blocks.STONE) {
/* 257 */       tryBonusOre(this.villager);
/*     */     }
/* 259 */     ModBlocks.villagerDestroyBlock(minePos, this.villager, dropBlock, (this.villager.world.getBlockState(minePos).getBlock() != Blocks.EMERALD_ORE));
/*     */     
/* 261 */     this.villager.modifyHunger(-1);
/* 262 */     this.villager.throttledSadness(-this.villager.getRNG().nextInt(2) - 1);
/* 263 */     this.villager.tryAddSkill(ProfessionType.MINER, skillChance);
/*     */ 
/*     */     
/* 266 */     if (this.villager.getRNG().nextInt(3) != 0) {
/* 267 */       this.villager.damageItem(this.toolUsed, 1);
/*     */     }
/* 269 */     this.villager.addJob(new TickJob(20, 0, false, () -> this.villager.pickupItems(4)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void tryBonusOre(EntityVillagerTek villager) {
/* 275 */     int skill = this.villager.getSkill(ProfessionType.MINER);
/* 276 */     float chance = 0.02F + skill / 100.0F * 0.04F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 285 */     Random rnd = this.villager.getRNG();
/* 286 */     if (rnd.nextFloat() <= chance) {
/* 287 */       ItemStack itemStack = oreToItem(VillageStructureMineshaft.getRegrowBlock(villager));
/* 288 */       villager.getInventory().addItem(ModItems.makeTaggedItem(itemStack, ItemTagType.VILLAGER));
/* 289 */       this.villager.modifyHappy(3);
/* 290 */       this.villager.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.2F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private ItemStack oreToItem(Block block) {
/* 295 */     if (block == Blocks.DIAMOND_ORE)
/* 296 */       return new ItemStack(Items.DIAMOND); 
/* 297 */     if (block == Blocks.LAPIS_ORE)
/* 298 */       return new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()); 
/* 299 */     if (block == Blocks.GOLD_ORE)
/* 300 */       return new ItemStack(Item.getItemFromBlock(Blocks.GOLD_ORE)); 
/* 301 */     if (block == Blocks.IRON_ORE)
/* 302 */       return new ItemStack(Item.getItemFromBlock(Blocks.IRON_ORE)); 
/* 303 */     if (block == Blocks.REDSTONE_ORE) {
/* 304 */       return new ItemStack(Items.REDSTONE);
/*     */     }
/* 306 */     return new ItemStack(Items.COAL);
/*     */   }
/*     */   
/*     */   public void resetTask() {
/* 310 */     super.resetTask();
/* 311 */     stopMining();
/* 312 */     this.mineshaft.checkExtendTunnel();
/* 313 */     this.mineshaft = null;
/* 314 */     this.miningPos = null;
/* 315 */     this.toolUsed = null;
/* 316 */     this.phase = MiningPhase.WALKING_TO_TUNNEL;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIMining.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */