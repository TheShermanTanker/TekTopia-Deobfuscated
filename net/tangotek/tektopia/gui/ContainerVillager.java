/*    */ package net.tangotek.tektopia.gui;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.IContainerListener;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import net.tangotek.tektopia.storage.VillagerInventory;
/*    */ 
/*    */ public class ContainerVillager extends Container {
/*    */   private final IInventory villagerInventory;
/*    */   
/*    */   public ContainerVillager(VillagerInventory villagerInventory) {
/* 15 */     this.villagerInventory = (IInventory)villagerInventory;
/* 16 */     for (int i = 0; i < 3; i++) {
/*    */       
/* 18 */       for (int j = 0; j < 9; j++)
/*    */       {
/* 20 */         addSlotToContainer(new ReadOnlySlot((IInventory)villagerInventory, i * 9 + j, 9 + j * 18, 97 + i * 18));
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void addListener(IContainerListener listener) {
/* 27 */     super.addListener(listener);
/* 28 */     listener.sendAllWindowProperties(this, this.villagerInventory);
/*    */   }
/*    */ 
/*    */   
/*    */   @SideOnly(Side.CLIENT)
/*    */   public void updateProgressBar(int id, int data) {
/* 34 */     this.villagerInventory.setField(id, data);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canInteractWith(EntityPlayer playerIn) {
/* 42 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\gui\ContainerVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */