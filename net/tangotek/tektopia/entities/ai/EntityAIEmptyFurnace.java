/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityFurnace;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityAIEmptyFurnace extends EntityAIMoveToBlock {
/*  19 */   private TileEntityFurnace furnace = null;
/*  20 */   private int pickupTime = -1;
/*     */   private final VillageStructureType[] structureTypes;
/*     */   private final Predicate<ItemStack> itemPred;
/*     */   private final EntityVillagerTek villager;
/*     */   protected final Predicate<EntityVillagerTek> shouldPred;
/*     */   
/*     */   public EntityAIEmptyFurnace(EntityVillagerTek v, VillageStructureType structure, Predicate<ItemStack> itemPred) {
/*  27 */     this(v, new VillageStructureType[] { structure }, itemPred, p -> true);
/*     */   }
/*     */   
/*     */   public EntityAIEmptyFurnace(EntityVillagerTek v, VillageStructureType[] structures, Predicate<ItemStack> itemPred, Predicate<EntityVillagerTek> shouldPred) {
/*  31 */     super((EntityVillageNavigator)v);
/*  32 */     this.structureTypes = structures;
/*  33 */     this.villager = v;
/*  34 */     this.itemPred = itemPred;
/*  35 */     this.shouldPred = shouldPred;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  40 */     if (this.villager.isAITick() && this.villager.hasVillage() && this.villager.isWorkTime() && this.villager.getInventory().hasSlotFree() && this.shouldPred.test(this.villager)) {
/*  41 */       List<VillageStructure> blacksmiths = this.villager.getVillage().getStructures(this.structureTypes);
/*  42 */       for (VillageStructure smith : blacksmiths) {
/*     */         
/*  44 */         List<BlockPos> furnaces = smith.getSpecialBlocks(Blocks.FURNACE);
/*  45 */         for (BlockPos pos : furnaces) {
/*  46 */           TileEntity te = this.villager.world.getTileEntity(pos);
/*  47 */           if (te instanceof TileEntityFurnace) {
/*  48 */             TileEntityFurnace furnace = (TileEntityFurnace)te;
/*  49 */             ItemStack inFurnaceStack = furnace.getStackInSlot(2);
/*  50 */             if (this.itemPred.test(inFurnaceStack)) {
/*  51 */               this.furnace = furnace;
/*  52 */               return super.shouldExecute();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  60 */     return false;
/*     */   }
/*     */   
/*     */   public void updateTask() {
/*  64 */     this.pickupTime--;
/*  65 */     if (this.pickupTime == 30) {
/*  66 */       extractItem();
/*     */     }
/*     */     
/*  69 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNearWalkPos() {
/*  74 */     return (this.navigator.getPosition().distanceSq((Vec3i)this.destinationPos) < 2.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  79 */     return this.furnace.getPos();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  85 */     if (this.pickupTime > 0) {
/*  86 */       return true;
/*     */     }
/*  88 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/*  93 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/*  99 */     this.pickupTime = 40;
/* 100 */     super.onArrival();
/*     */   }
/*     */   
/*     */   private void extractItem() {
/* 104 */     if (!this.furnace.isInvalid()) {
/* 105 */       ItemStack itemStack = this.furnace.removeStackFromSlot(2);
/* 106 */       if (itemStack != ItemStack.EMPTY) {
/*     */         
/* 108 */         ItemStack fuelItem = this.furnace.getStackInSlot(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 114 */         int villagerSmelt = this.furnace.getTileData().getInteger("villager_smelt");
/* 115 */         int villagerFuel = this.furnace.getTileData().getInteger("villager_fuel");
/* 116 */         int fuelRemaining = fuelItem.getCount() + (this.furnace.isBurning() ? 1 : 0);
/*     */         
/* 118 */         villagerSmelt -= itemStack.getCount();
/*     */         
/* 120 */         if (villagerSmelt >= 0 && villagerFuel > 0) {
/* 121 */           ModItems.makeTaggedItem(itemStack, ItemTagType.VILLAGER);
/*     */         }
/*     */         
/* 124 */         if (fuelRemaining > villagerFuel) {
/* 125 */           fuelRemaining = 0;
/*     */         }
/* 127 */         if (villagerSmelt > 0) {
/* 128 */           this.furnace.getTileData().setInteger("villager_smelt", villagerSmelt);
/*     */         } else {
/* 130 */           this.furnace.getTileData().removeTag("villager_smelt");
/*     */         } 
/* 132 */         if (fuelRemaining > 0) {
/* 133 */           this.furnace.getTileData().setInteger("villager_fuel", fuelRemaining);
/*     */         } else {
/* 135 */           this.furnace.getTileData().removeTag("villager_fuel");
/*     */         } 
/* 137 */         this.villager.getInventory().addItem(itemStack);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIEmptyFurnace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */