/*    */ package net.tangotek.tektopia.gui;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.inventory.Slot;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class ReadOnlySlot
/*    */   extends Slot {
/*    */   public ReadOnlySlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
/* 11 */     super(inventoryIn, index, xPosition, yPosition);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isItemValid(ItemStack par1ItemStack) {
/* 17 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canTakeStack(EntityPlayer playerIn) {
/* 22 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\gui\ReadOnlySlot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */