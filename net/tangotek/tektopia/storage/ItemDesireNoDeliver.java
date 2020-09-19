/*    */ package net.tangotek.tektopia.storage;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemDesireNoDeliver
/*    */   extends ItemDesire
/*    */ {
/*    */   private final Predicate<ItemStack> pred;
/*    */   
/*    */   public ItemDesireNoDeliver(String name, Predicate<ItemStack> pred, int required, int ideal, int limit, Predicate<EntityVillagerTek> shouldNeed) {
/* 16 */     super(name, pred, required, ideal, limit, shouldNeed);
/* 17 */     this.pred = pred;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getDeliverToStorage(EntityVillagerTek villager, ItemStack itemStack) {
/* 23 */     if (((Integer)this.neededItemFunction.apply(itemStack)).intValue() < 0) {
/* 24 */       return itemStack.getCount();
/*    */     }
/* 26 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getItemsHave(EntityVillagerTek villager) {
/* 31 */     int result = super.getItemsHave(villager);
/* 32 */     if (result > this.limitCount) {
/* 33 */       villager.getInventory().deleteOverstock(this.pred, this.limitCount);
/* 34 */       result = this.limitCount;
/*    */     } 
/*    */     
/* 37 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\storage\ItemDesireNoDeliver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */