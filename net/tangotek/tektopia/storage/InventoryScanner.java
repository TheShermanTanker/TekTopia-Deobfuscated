/*    */ package net.tangotek.tektopia.storage;
/*    */ 
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class InventoryScanner
/*    */ {
/*    */   private final IInventory inv;
/*  9 */   private int slot = 0;
/*    */   private final byte[] slotHashes;
/*    */   
/*    */   public InventoryScanner(IInventory i) {
/* 13 */     this.inv = i;
/* 14 */     this.slotHashes = new byte[i.getSizeInventory()];
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int tickSlot() {
/* 22 */     this.slot++;
/* 23 */     if (this.slot >= this.inv.getSizeInventory()) {
/* 24 */       this.slot = 0;
/*    */     }
/* 26 */     byte hash = getSlotHash(this.slot);
/* 27 */     if (hash != this.slotHashes[this.slot]) {
/* 28 */       this.slotHashes[this.slot] = hash;
/*    */ 
/*    */       
/* 31 */       return this.slot;
/*    */     } 
/*    */     
/* 34 */     return -1;
/*    */   }
/*    */   
/*    */   public void updateSlotSilent(int slot) {
/* 38 */     this.slotHashes[slot] = getSlotHash(slot);
/*    */   }
/*    */   
/*    */   public ItemStack getChangedItem() {
/* 42 */     return this.inv.getStackInSlot(this.slot);
/*    */   }
/*    */ 
/*    */   
/*    */   private byte getSlotHash(int slot) {
/* 47 */     return (byte)this.inv.getStackInSlot(slot).getCount();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\storage\InventoryScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */