/*    */ package net.tangotek.tektopia.items;
/*    */ 
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemSword;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ 
/*    */ public class ItemHammer extends ItemSword {
/*    */   private String name;
/*    */   
/*    */   public ItemHammer(Item.ToolMaterial material, String name) {
/* 11 */     super(material);
/* 12 */     setRegistryName(name);
/* 13 */     setTranslationKey(name);
/* 14 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public float getAttackDamage() {
/* 20 */     return 1.0F;
/*    */   }
/*    */   
/*    */   public void registerItemModel() {
/* 24 */     TekVillager.proxy.registerItemRenderer((Item)this, 0, this.name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\items\ItemHammer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */