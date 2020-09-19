/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityFurnace;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.storage.VillagerInventory;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityAISmelting
/*     */   extends EntityAIMoveToBlock {
/*  23 */   private TileEntityFurnace furnace = null;
/*  24 */   private int smeltTime = -1;
/*     */   private final VillageStructureType[] structureTypes;
/*     */   private final Function<ItemStack, Integer> getSmeltable;
/*     */   private final Runnable onSmelt;
/*     */   protected final EntityVillagerTek villager;
/*     */   protected final Predicate<EntityVillagerTek> shouldPred;
/*     */   
/*     */   public EntityAISmelting(EntityVillagerTek v, VillageStructureType structure, Predicate<EntityVillagerTek> shouldPred, Function<ItemStack, Integer> getSmeltable, Runnable onSmelt) {
/*  32 */     this(v, new VillageStructureType[] { structure }, shouldPred, getSmeltable, onSmelt);
/*     */   }
/*     */   
/*     */   public EntityAISmelting(EntityVillagerTek v, VillageStructureType[] structures, Predicate<EntityVillagerTek> shouldPred, Function<ItemStack, Integer> getSmeltable, Runnable onSmelt) {
/*  36 */     super((EntityVillageNavigator)v);
/*  37 */     this.villager = v;
/*  38 */     this.structureTypes = structures;
/*  39 */     this.getSmeltable = getSmeltable;
/*  40 */     this.onSmelt = onSmelt;
/*  41 */     this.shouldPred = shouldPred;
/*     */   }
/*     */   
/*     */   private ItemStack getFuel(TileEntityFurnace furnace) {
/*  45 */     return furnace.getStackInSlot(1);
/*     */   }
/*     */   
/*     */   private boolean hasFuel(TileEntityFurnace furnace) {
/*  49 */     return !getFuel(furnace).isEmpty();
/*     */   }
/*     */   
/*     */   private boolean canAddfuel(TileEntityFurnace furnace) {
/*  53 */     return (!furnace.isInvalid() && !hasFuel(furnace) && !furnace.isBurning());
/*     */   }
/*     */   
/*     */   private boolean canAddSmeltable(TileEntityFurnace furnace) {
/*  57 */     return (!furnace.isInvalid() && furnace.getStackInSlot(0).isEmpty() && hasFuel(furnace));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  62 */     if (this.villager.isAITick() && this.villager.hasVillage() && this.villager.isWorkTime() && this.shouldPred.test(this.villager)) {
/*  63 */       List<VillageStructure> structures = this.villager.getVillage().getStructures(this.structureTypes);
/*  64 */       for (VillageStructure smith : structures) {
/*  65 */         List<BlockPos> furnaces = smith.getSpecialBlocks(Blocks.FURNACE);
/*  66 */         for (BlockPos pos : furnaces) {
/*  67 */           TileEntity te = this.villager.world.getTileEntity(pos);
/*  68 */           if (te instanceof TileEntityFurnace) {
/*  69 */             TileEntityFurnace furnace = (TileEntityFurnace)te;
/*     */             
/*  71 */             if (canAddfuel(furnace)) {
/*  72 */               ItemStack fuelItem = getFuel(this.villager);
/*  73 */               if (!fuelItem.isEmpty()) {
/*  74 */                 this.furnace = furnace;
/*  75 */                 this.villager.equipActionItem(fuelItem);
/*  76 */                 return super.shouldExecute();
/*     */               } 
/*     */             } 
/*     */ 
/*     */             
/*  81 */             if (canAddSmeltable(furnace)) {
/*  82 */               ItemStack smeltItem = this.villager.getInventory().getItem(this.getSmeltable);
/*  83 */               if (!smeltItem.isEmpty()) {
/*  84 */                 this.furnace = furnace;
/*  85 */                 this.villager.equipActionItem(smeltItem);
/*  86 */                 return super.shouldExecute();
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 100 */     if (this.smeltTime > 0) {
/* 101 */       return true;
/*     */     }
/* 103 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 108 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/* 113 */     if (this.furnace != null) {
/* 114 */       return this.furnace.getPos();
/*     */     }
/* 116 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 121 */     this.smeltTime--;
/* 122 */     if (this.smeltTime == 5) {
/* 123 */       boolean done = depositFuel();
/* 124 */       if (!done) {
/* 125 */         done = depositSmeltable();
/*     */       }
/*     */     } 
/* 128 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 133 */     this.smeltTime = 40;
/* 134 */     super.onArrival();
/*     */   }
/*     */   
/*     */   private boolean depositFuel() {
/* 138 */     if (canAddfuel(this.furnace)) {
/* 139 */       List<ItemStack> fuel = this.villager.getInventory().removeItems(getBestFuel(), 1);
/* 140 */       if (!fuel.isEmpty() && fuel.get(0) != ItemStack.EMPTY) {
/* 141 */         ItemStack fuelItem = fuel.get(0);
/* 142 */         this.furnace.setInventorySlotContents(1, fuelItem);
/* 143 */         if (ModItems.isTaggedItem(fuelItem, ItemTagType.VILLAGER)) {
/* 144 */           this.furnace.getTileData().setInteger("villager_fuel", fuelItem.getCount());
/*     */         }
/*     */         
/* 147 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 151 */     return false;
/*     */   }
/*     */   
/*     */   private boolean depositSmeltable() {
/* 155 */     if (canAddSmeltable(this.furnace)) {
/* 156 */       int cookTime = getCookTime(this.furnace);
/*     */ 
/*     */       
/* 159 */       List<ItemStack> bestSmeltList = this.villager.getInventory().removeItems(this.getSmeltable, 1);
/* 160 */       if (!bestSmeltList.isEmpty() && bestSmeltList.get(0) != ItemStack.EMPTY) {
/* 161 */         ItemStack bestSmelt = bestSmeltList.get(0);
/*     */         
/* 163 */         int goalCount = cookTime / this.furnace.getCookTime(bestSmelt);
/*     */ 
/*     */         
/* 166 */         if (goalCount > 1) {
/* 167 */           bestSmeltList.addAll(this.villager.getInventory().removeItems(p -> ItemStack.areItemsEqual(p, bestSmelt), goalCount - 1));
/*     */         }
/* 169 */         int itemCount = VillagerInventory.countItems(bestSmeltList);
/*     */ 
/*     */         
/* 172 */         bestSmelt.setCount(itemCount);
/*     */         
/* 174 */         if (ModItems.isTaggedItem(bestSmelt, ItemTagType.VILLAGER)) {
/* 175 */           this.furnace.getTileData().setInteger("villager_smelt", bestSmelt.getCount());
/*     */         }
/*     */         
/* 178 */         this.furnace.setInventorySlotContents(0, bestSmelt);
/* 179 */         this.onSmelt.run();
/* 180 */         this.villager.throttledSadness(-2);
/* 181 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 185 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getCookTime(TileEntityFurnace furnace) {
/* 190 */     return TileEntityFurnace.getItemBurnTime(getFuel(furnace)) + furnace.getField(0);
/*     */   }
/*     */   
/*     */   public static ItemStack getFuel(EntityVillagerTek villager) {
/* 194 */     List<ItemStack> fuelItems = villager.getInventory().getItems(getBestFuel(), 1);
/* 195 */     if (fuelItems.size() > 0) {
/* 196 */       return fuelItems.get(0);
/*     */     }
/* 198 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Function<ItemStack, Integer> getBestFuel() {
/* 210 */     return p -> 
/* 211 */       (p.getItem() == Items.COAL || p.getItem() == Item.getItemFromBlock(Blocks.LOG)) ? Integer.valueOf(TileEntityFurnace.getItemBurnTime(p)) : Integer.valueOf(-1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAISmelting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */