/*     */ package net.tangotek.tektopia.storage;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UpgradeItemDesire
/*     */   extends ItemDesire
/*     */ {
/*     */   int scoreCutoff;
/*  15 */   int currentlyHaveIdeal = 0;
/*     */   Function<ItemStack, Integer> upgradeFunction;
/*     */   
/*     */   public UpgradeItemDesire(String name, Function<ItemStack, Integer> itemFunction, int required, int ideal, int limit, Predicate<EntityVillagerTek> should) {
/*  19 */     super(name, itemFunction, required, ideal, limit, should);
/*  20 */     this.upgradeFunction = (p -> {
/*     */         int diff = ((Integer)this.neededItemFunction.apply(p)).intValue() - this.scoreCutoff;
/*     */         return Integer.valueOf((diff > 0) ? diff : -1);
/*     */       });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onStorageUpdated(EntityVillagerTek villager, ItemStack updatedItem) {
/*  28 */     if (((Integer)this.upgradeFunction.apply(updatedItem)).intValue() > 0) {
/*  29 */       this.storageDirty = true;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateStorage(EntityVillagerTek villager) {
/*  34 */     if (this.storageDirty && villager.hasVillage()) {
/*     */       
/*  36 */       this.pickUpChest = villager.getVillage().getStorageChestWithItem(this.upgradeFunction);
/*     */       
/*  38 */       this.storageDirty = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateSelf(EntityVillagerTek villager) {
/*  44 */     if (this.selfDirty && villager.hasVillage()) {
/*  45 */       List<ItemStack> itemList = villager.getInventory().getItems(this.neededItemFunction, 0);
/*  46 */       this.currentlyHave = 0;
/*  47 */       this.currentlyHaveIdeal = 0;
/*  48 */       int cutOff = 0;
/*  49 */       for (ItemStack item : itemList) {
/*  50 */         this.currentlyHave += item.getCount();
/*  51 */         if (this.currentlyHave >= this.idealCount && cutOff <= 0) {
/*  52 */           cutOff = ((Integer)this.neededItemFunction.apply(item)).intValue();
/*  53 */           this.currentlyHaveIdeal = this.currentlyHave;
/*     */         } 
/*     */       } 
/*     */       
/*  57 */       if (cutOff != this.scoreCutoff) {
/*  58 */         this.scoreCutoff = cutOff;
/*     */         
/*  60 */         this.storageDirty = true;
/*     */       } 
/*     */ 
/*     */       
/*  64 */       this.selfDirty = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldPickUp(EntityVillagerTek villager) {
/*  69 */     if ((this.shouldNeed == null || this.shouldNeed.test(villager)) && villager.hasVillage()) {
/*  70 */       update(villager);
/*     */       
/*  72 */       if (this.pickUpChest != null)
/*     */       {
/*  74 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  78 */     return false;
/*     */   }
/*     */   
/*     */   protected Function<ItemStack, Integer> getStoragePickUpFunction() {
/*  82 */     return this.upgradeFunction;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getQuantityToTake(EntityVillagerTek villager, ItemStack item) {
/*  87 */     int thisScore = ((Integer)this.neededItemFunction.apply(item)).intValue();
/*  88 */     if (thisScore < 0) {
/*  89 */       return item.getCount();
/*     */     }
/*  91 */     Function<ItemStack, Integer> betterThan = p -> {
/*     */         int diff = ((Integer)this.neededItemFunction.apply(p)).intValue() - thisScore;
/*     */         
/*     */         return Integer.valueOf((diff >= 0) ? 1 : -1);
/*     */       };
/*  96 */     int itemsToTake = this.idealCount - villager.getInventory().getItemCount(betterThan);
/*  97 */     return Math.min(itemsToTake, item.getCount());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeliverToStorage(EntityVillagerTek villager, ItemStack itemStack) {
/* 102 */     update(villager);
/* 103 */     int thisScore = ((Integer)this.neededItemFunction.apply(itemStack)).intValue();
/* 104 */     if (thisScore < 0) {
/* 105 */       return itemStack.getCount();
/*     */     }
/* 107 */     int thisLimit = villager.isStoragePriority() ? this.idealCount : this.limitCount;
/* 108 */     if (this.currentlyHave - thisLimit > 0) {
/* 109 */       if (thisScore < this.scoreCutoff)
/* 110 */         return Math.min(this.currentlyHave - this.idealCount, itemStack.getCount()); 
/* 111 */       if (thisScore == this.scoreCutoff) {
/* 112 */         return Math.min(this.currentlyHaveIdeal - this.idealCount, itemStack.getCount());
/*     */       }
/*     */     } 
/* 115 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\storage\UpgradeItemDesire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */