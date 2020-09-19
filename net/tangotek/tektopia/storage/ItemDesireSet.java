/*     */ package net.tangotek.tektopia.storage;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.tangotek.tektopia.VillageManager;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.entities.crafting.Recipe;
/*     */ 
/*     */ 
/*     */ public class ItemDesireSet
/*     */ {
/*  15 */   protected List<ItemDesire> itemDesires = new ArrayList<>();
/*     */   protected boolean deliveryDirty = true;
/*  17 */   protected int deliveryId = 0;
/*  18 */   protected byte deliverySlot = -1;
/*  19 */   protected short deliveryCount = 0;
/*  20 */   protected int totalDeliverySize = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  27 */     this.itemDesires.clear();
/*     */   }
/*     */   
/*     */   public void addItemDesire(ItemDesire desire) {
/*  31 */     this.itemDesires.add(desire);
/*     */   }
/*     */   
/*     */   public void addRecipeDesire(Recipe r) {
/*  35 */     for (ItemStack need : r.getNeeds()) {
/*     */       Predicate<ItemStack> pred;
/*  37 */       if (need.getMetadata() == 99) {
/*  38 */         pred = (p -> (p.getItem() == need.getItem() && !p.isItemEnchanted()));
/*     */       } else {
/*  40 */         pred = (p -> (ItemStack.areItemsEqual(p, need) && !p.isItemEnchanted()));
/*     */       } 
/*  42 */       Predicate<EntityVillagerTek> shouldNeed = p -> p.isAIFilterEnabled(r.getAiFilter());
/*  43 */       if (r.shouldCraft != null) {
/*  44 */         shouldNeed = shouldNeed.and(r.shouldCraft);
/*     */       }
/*  46 */       addItemDesire(new ItemDesire(need.getItem().getTranslationKey(), pred, need.getCount(), need.getCount() * r.idealCount, need.getCount() * r.limitCount, shouldNeed));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void forceUpdate() {
/*  51 */     this.itemDesires.forEach(d -> d.forceUpdate());
/*  52 */     this.deliveryDirty = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onStorageUpdated(EntityVillagerTek villager, ItemStack storageItem) {
/*  57 */     this.itemDesires.forEach(d -> d.onStorageUpdated(villager, storageItem));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInventoryUpdated(EntityVillagerTek villager, ItemStack updatedItem) {
/*  62 */     this.itemDesires.forEach(d -> d.onInventoryUpdated(villager, updatedItem));
/*  63 */     this.deliveryDirty = true;
/*     */   }
/*     */   
/*     */   public ItemDesire getNeededDesire(EntityVillagerTek villager) {
/*  67 */     for (ItemDesire d : this.itemDesires) {
/*  68 */       if (d.shouldPickUp(villager)) {
/*  69 */         return d;
/*     */       }
/*     */     } 
/*     */     
/*  73 */     return null;
/*     */   }
/*     */   
/*     */   private void updateDeliveryList(EntityVillagerTek villager) {
/*  77 */     if (this.deliveryDirty && villager.hasVillage()) {
/*  78 */       this.deliverySlot = -1;
/*  79 */       this.deliveryCount = 0;
/*  80 */       this.totalDeliverySize = 0;
/*  81 */       int bestValue = 0;
/*     */       
/*  83 */       for (int i = villager.getInventory().getSizeInventory() - 1; i >= 0; i--) {
/*  84 */         ItemStack itemStack = villager.getInventory().getStackInSlot(i);
/*  85 */         if (!itemStack.isEmpty()) {
/*  86 */           int minDeliver = Integer.MAX_VALUE;
/*  87 */           for (ItemDesire desire : this.itemDesires) {
/*  88 */             int toDeliver = desire.getDeliverToStorage(villager, itemStack);
/*  89 */             if (toDeliver <= 0) {
/*     */               
/*  91 */               minDeliver = Integer.MAX_VALUE; break;
/*     */             } 
/*  93 */             if (toDeliver > 0 && toDeliver < minDeliver) {
/*  94 */               minDeliver = toDeliver;
/*     */             }
/*     */           } 
/*     */           
/*  98 */           if (minDeliver < Integer.MAX_VALUE) {
/*  99 */             int value = VillageManager.getItemValue(itemStack.getItem()) * minDeliver;
/*     */ 
/*     */             
/* 102 */             if (value > bestValue) {
/* 103 */               bestValue = value;
/* 104 */               this.deliveryCount = (short)minDeliver;
/* 105 */               this.deliverySlot = (byte)i;
/*     */             } 
/*     */             
/* 108 */             this.totalDeliverySize += value;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 117 */       this.deliveryDirty = false;
/* 118 */       this.deliveryId++;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getDeliveryId(EntityVillagerTek villager, int requiredDeliverSize) {
/* 123 */     updateDeliveryList(villager);
/* 124 */     if (this.totalDeliverySize >= requiredDeliverSize) {
/* 125 */       return this.deliveryId;
/*     */     }
/*     */     
/* 128 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isDeliveryMatch(int id) {
/* 132 */     return (this.deliveryId == id);
/*     */   }
/*     */   
/*     */   public ItemStack getDeliveryItemCopy(EntityVillagerTek villager) {
/* 136 */     updateDeliveryList(villager);
/*     */     
/* 138 */     if (this.deliverySlot >= 0) {
/*     */       
/* 140 */       ItemStack deliverItem = villager.getInventory().getStackInSlot(this.deliverySlot).copy();
/* 141 */       deliverItem.setCount(this.deliveryCount);
/* 142 */       return deliverItem;
/*     */     } 
/*     */     
/* 145 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean deliverItems(EntityVillagerTek villager, TileEntityChest destInv, int deliveryCheckId) {
/* 150 */     if (this.deliveryId != deliveryCheckId) {
/* 151 */       villager.debugOut("Delivery Id mismatch");
/* 152 */       this.deliveryDirty = true;
/* 153 */       return false;
/*     */     } 
/* 155 */     if (this.deliverySlot < 0) {
/* 156 */       villager.debugOut("Delivery FAILED. No active delivery.");
/* 157 */       this.deliveryDirty = true;
/* 158 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 163 */     ItemStack removedStack = villager.getInventory().decrStackSize(this.deliverySlot, this.deliveryCount);
/* 164 */     if (removedStack == ItemStack.EMPTY) {
/* 165 */       villager.debugOut("Delivery FAILED. Delivery item not found in villager inventory");
/* 166 */       this.deliveryDirty = true;
/* 167 */       return false;
/*     */     } 
/*     */     
/* 170 */     if (!deliverOneItem(removedStack, destInv, villager)) {
/*     */       
/* 172 */       villager.debugOut("Delivery FAILED. Returning to villager inventory " + removedStack);
/* 173 */       villager.getInventory().addItem(removedStack);
/* 174 */       this.deliveryDirty = true;
/* 175 */       return false;
/*     */     } 
/*     */     
/* 178 */     return true;
/*     */   }
/*     */   
/*     */   private boolean deliverOneItem(ItemStack sourceStack, TileEntityChest destChest, EntityVillagerTek villager) {
/* 182 */     int emptySlot = -1;
/* 183 */     for (int d = 0; d < destChest.getSizeInventory(); d++) {
/* 184 */       ItemStack destStack = destChest.getStackInSlot(d);
/*     */       
/* 186 */       if (destStack.isEmpty() && emptySlot < 0) {
/* 187 */         emptySlot = d;
/* 188 */       } else if (VillagerInventory.areItemsStackable(destStack, sourceStack)) {
/* 189 */         int k = Math.min(sourceStack.getCount(), destStack.getMaxStackSize() - destStack.getCount());
/* 190 */         if (k > 0) {
/*     */ 
/*     */           
/* 193 */           destStack.grow(k);
/*     */ 
/*     */           
/* 196 */           if (villager.hasVillage()) {
/* 197 */             villager.getVillage().onStorageChange(destChest, d, destStack);
/*     */           }
/* 199 */           villager.onInventoryUpdated(destStack);
/*     */           
/* 201 */           sourceStack.shrink(k);
/* 202 */           if (sourceStack.isEmpty()) {
/* 203 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 209 */     if (emptySlot >= 0) {
/*     */ 
/*     */       
/* 212 */       destChest.setInventorySlotContents(emptySlot, sourceStack);
/*     */ 
/*     */       
/* 215 */       if (villager.hasVillage()) {
/* 216 */         villager.getVillage().onStorageChange(destChest, emptySlot, sourceStack);
/*     */       }
/* 218 */       return true;
/*     */     } 
/*     */     
/* 221 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\storage\ItemDesireSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */