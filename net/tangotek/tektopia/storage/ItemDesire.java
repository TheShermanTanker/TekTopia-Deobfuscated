/*     */ package net.tangotek.tektopia.storage;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ 
/*     */ public class ItemDesire
/*     */ {
/*     */   protected boolean selfDirty = true;
/*     */   protected boolean storageDirty = true;
/*     */   protected final int idealCount;
/*     */   protected final int requiredCount;
/*     */   protected final int limitCount;
/*     */   protected int currentlyHave;
/*     */   protected Function<ItemStack, Integer> neededItemFunction;
/*     */   protected final Predicate<EntityVillagerTek> shouldNeed;
/*     */   private String debugName;
/*     */   protected TileEntityChest pickUpChest;
/*     */   
/*     */   public ItemDesire(Block block, int required, int ideal, int limit, Predicate<EntityVillagerTek> shouldNeed) {
/*  26 */     this(Item.getItemFromBlock(block), required, ideal, limit, shouldNeed);
/*     */   }
/*     */   
/*     */   public ItemDesire(Item item, int required, int ideal, int limit, Predicate<EntityVillagerTek> shouldNeed) {
/*  30 */     this(item.getTranslationKey(), p -> Integer.valueOf((p.getItem() == item) ? 1 : -1), required, ideal, limit, shouldNeed);
/*     */   }
/*     */   
/*     */   public ItemDesire(String name, Predicate<ItemStack> pred, int required, int ideal, int limit, Predicate<EntityVillagerTek> shouldNeed) {
/*  34 */     this(name, p -> Integer.valueOf(pred.test(p) ? 1 : -1), required, ideal, limit, shouldNeed);
/*     */   }
/*     */   
/*     */   public ItemDesire(String name, Function<ItemStack, Integer> itemFunction, int required, int ideal, int limit, Predicate<EntityVillagerTek> should) {
/*  38 */     this.neededItemFunction = itemFunction;
/*  39 */     this.idealCount = Math.max(ideal, required);
/*  40 */     this.limitCount = Math.max(this.idealCount, limit);
/*  41 */     this.requiredCount = required;
/*  42 */     this.shouldNeed = should;
/*  43 */     this.currentlyHave = 0;
/*  44 */     this.debugName = name;
/*     */   }
/*     */   
/*     */   public String name() {
/*  48 */     return this.debugName;
/*     */   }
/*     */   
/*     */   public void forceUpdate() {
/*  52 */     this.storageDirty = true;
/*  53 */     this.selfDirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInventoryUpdated(EntityVillagerTek villager, ItemStack updatedItem) {
/*  58 */     if (((Integer)this.neededItemFunction.apply(updatedItem)).intValue() > 0) {
/*  59 */       this.selfDirty = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onStorageUpdated(EntityVillagerTek villager, ItemStack updatedItem) {
/*  65 */     if (((Integer)this.neededItemFunction.apply(updatedItem)).intValue() > 0) {
/*  66 */       this.storageDirty = true;
/*     */     }
/*     */   }
/*     */   
/*     */   protected int getItemsHave(EntityVillagerTek villager) {
/*  71 */     return villager.getInventory().getItemCount(this.neededItemFunction);
/*     */   }
/*     */   
/*     */   protected void updateStorage(EntityVillagerTek villager) {
/*  75 */     if (this.storageDirty && villager.hasVillage()) {
/*  76 */       if (this.currentlyHave < this.idealCount) {
/*     */         
/*  78 */         this.pickUpChest = villager.getVillage().getStorageChestWithItem(this.neededItemFunction);
/*     */       }
/*     */       else {
/*     */         
/*  82 */         this.pickUpChest = null;
/*     */       } 
/*     */       
/*  85 */       this.storageDirty = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void updateSelf(EntityVillagerTek villager) {
/*  90 */     if (this.selfDirty) {
/*  91 */       int oldHave = this.currentlyHave;
/*  92 */       this.currentlyHave = getItemsHave(villager);
/*     */ 
/*     */       
/*  95 */       if (this.currentlyHave < oldHave) {
/*  96 */         this.storageDirty = true;
/*     */       }
/*     */       
/*  99 */       this.selfDirty = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void update(EntityVillagerTek villager) {
/* 104 */     updateSelf(villager);
/* 105 */     updateStorage(villager);
/*     */   }
/*     */   
/*     */   public TileEntityChest getPickUpChest(EntityVillagerTek villager) {
/* 109 */     update(villager);
/*     */     
/* 111 */     if (this.pickUpChest != null && this.pickUpChest.isInvalid()) {
/* 112 */       this.pickUpChest = null;
/*     */     }
/* 114 */     return this.pickUpChest;
/*     */   }
/*     */   
/*     */   public boolean shouldPickUp(EntityVillagerTek villager) {
/* 118 */     if ((this.shouldNeed == null || this.shouldNeed.test(villager)) && villager.hasVillage() && villager.isWorkTime()) {
/* 119 */       update(villager);
/* 120 */       if (this.currentlyHave < this.idealCount && this.pickUpChest != null) {
/* 121 */         if (villager.isStoragePriority())
/*     */         {
/* 123 */           return true;
/*     */         }
/* 125 */         if (this.currentlyHave < this.requiredCount)
/*     */         {
/* 127 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 132 */     return false;
/*     */   }
/*     */   
/*     */   protected Function<ItemStack, Integer> getStoragePickUpFunction() {
/* 136 */     return this.neededItemFunction;
/*     */   }
/*     */   
/*     */   protected int getQuantityToTake(EntityVillagerTek villager, ItemStack item) {
/* 140 */     return Math.min(this.idealCount - this.currentlyHave, item.getCount());
/*     */   }
/*     */   
/*     */   public ItemStack pickUpItems(EntityVillagerTek villager) {
/* 144 */     if (shouldPickUp(villager)) {
/* 145 */       TileEntityChest chest = getPickUpChest(villager);
/* 146 */       ItemStack bestItem = null;
/* 147 */       int bestSlot = 0;
/* 148 */       int bestScore = 0;
/*     */       
/* 150 */       for (int d = 0; d < chest.getSizeInventory(); d++) {
/* 151 */         ItemStack chestStack = chest.getStackInSlot(d);
/* 152 */         if (!chestStack.isEmpty()) {
/* 153 */           int thisScore = ((Integer)getStoragePickUpFunction().apply(chestStack)).intValue();
/* 154 */           if (thisScore > bestScore) {
/* 155 */             bestItem = chestStack;
/* 156 */             bestScore = thisScore;
/* 157 */             bestSlot = d;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 162 */       if (bestItem != null) {
/* 163 */         villager.throttledSadness(-2);
/*     */ 
/*     */ 
/*     */         
/* 167 */         int quantityToTake = getQuantityToTake(villager, bestItem);
/*     */ 
/*     */         
/* 170 */         villager.equipActionItem(bestItem);
/*     */         
/* 172 */         ItemStack newStack = bestItem.splitStack(quantityToTake);
/*     */         
/* 174 */         if (newStack.isEmpty()) {
/* 175 */           villager.getDesireSet().forceUpdate();
/*     */         }
/*     */         else {
/*     */           
/* 179 */           villager.getVillage().onStorageChange(chest, bestSlot, newStack);
/*     */         } 
/* 181 */         return newStack;
/*     */       } 
/*     */     } 
/*     */     
/* 185 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public int getDeliverToStorage(EntityVillagerTek villager, ItemStack itemStack) {
/* 189 */     update(villager);
/*     */ 
/*     */     
/* 192 */     if (((Integer)this.neededItemFunction.apply(itemStack)).intValue() < 0) {
/* 193 */       return itemStack.getCount();
/*     */     }
/* 195 */     int thisLimit = villager.isStoragePriority() ? this.idealCount : this.limitCount;
/*     */ 
/*     */     
/* 198 */     if (this.currentlyHave - thisLimit > 0)
/*     */     {
/* 200 */       return Math.min(this.currentlyHave - this.idealCount, itemStack.getCount());
/*     */     }
/*     */     
/* 203 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\storage\ItemDesire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */