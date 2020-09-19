/*    */ package net.tangotek.tektopia.items;
/*    */ 
/*    */ import net.minecraft.init.MobEffects;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemFood;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ 
/*    */ public class ItemBeer
/*    */   extends ItemFood {
/*    */   public ItemBeer(String name) {
/* 12 */     super(1, 4.0F, false);
/* 13 */     setRegistryName(name);
/* 14 */     setTranslationKey(name);
/* 15 */     setPotionEffect(new PotionEffect(MobEffects.NAUSEA, 160, 0), 0.5F);
/* 16 */     this.name = name;
/*    */   }
/*    */   private String name;
/*    */   public void registerItemModel() {
/* 20 */     TekVillager.proxy.registerItemRenderer((Item)this, 0, this.name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\items\ItemBeer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */