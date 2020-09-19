/*    */ package net.tangotek.tektopia.storage;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.inventory.EntityEquipmentSlot;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UpgradeEquipment
/*    */   extends ItemDesire
/*    */ {
/*    */   private Function<ItemStack, Integer> upgradeFunction;
/*    */   private final EntityEquipmentSlot equipSlot;
/* 16 */   private int currentScore = 0;
/*    */   
/*    */   public UpgradeEquipment(String name, Function<ItemStack, Integer> itemFunction, EntityEquipmentSlot equipSlot, Predicate<EntityVillagerTek> should) {
/* 19 */     super(name, itemFunction, 0, 0, 0, should);
/* 20 */     this.equipSlot = equipSlot;
/* 21 */     this.upgradeFunction = (p -> {
/*    */         int diff = ((Integer)this.neededItemFunction.apply(p)).intValue() - this.currentScore;
/*    */         return Integer.valueOf((diff > 0) ? diff : -1);
/*    */       });
/*    */   }
/*    */   
/*    */   protected void updateStorage(EntityVillagerTek villager) {
/* 28 */     if (this.storageDirty && villager.hasVillage()) {
/*    */       
/* 30 */       this.pickUpChest = villager.getVillage().getStorageChestWithItem(this.upgradeFunction);
/*    */       
/* 32 */       this.storageDirty = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void updateSelf(EntityVillagerTek villager) {
/* 37 */     if (this.selfDirty) {
/* 38 */       int oldScore = this.currentScore;
/* 39 */       villager.equipBestGear(this.equipSlot, this.neededItemFunction);
/*    */       
/* 41 */       this.currentScore = ((Integer)this.neededItemFunction.apply(villager.getItemStackFromSlot(this.equipSlot))).intValue();
/*    */ 
/*    */       
/* 44 */       if (this.currentScore != oldScore) {
/* 45 */         this.storageDirty = true;
/*    */       }
/*    */       
/* 48 */       this.selfDirty = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean shouldPickUp(EntityVillagerTek villager) {
/* 53 */     if ((this.shouldNeed == null || this.shouldNeed.test(villager)) && villager.hasVillage() && villager.isWorkTime()) {
/* 54 */       update(villager);
/* 55 */       if (this.pickUpChest != null) {
/* 56 */         return true;
/*    */       }
/*    */     } 
/*    */     
/* 60 */     return false;
/*    */   }
/*    */   
/*    */   protected Function<ItemStack, Integer> getStoragePickUpFunction() {
/* 64 */     return this.upgradeFunction;
/*    */   }
/*    */   
/*    */   protected int getQuantityToTake(EntityVillagerTek villager, ItemStack item) {
/* 68 */     return Math.min(1, item.getCount());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDeliverToStorage(EntityVillagerTek villager, ItemStack itemStack) {
/* 73 */     update(villager);
/* 74 */     return itemStack.getCount();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\storage\UpgradeEquipment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */