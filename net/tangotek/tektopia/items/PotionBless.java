/*    */ package net.tangotek.tektopia.items;
/*    */ 
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.potion.Potion;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ 
/*    */ public class PotionBless
/*    */   extends Potion
/*    */ {
/*    */   public PotionBless(int color) {
/* 12 */     super(false, color);
/* 13 */     setRegistryName("tektopia", "village_bless");
/* 14 */     setPotionName("effect.village_bless.name");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isReady(int duration, int amplifier) {
/* 20 */     return (duration % 400 == 0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
/* 26 */     if (entityLivingBaseIn instanceof EntityVillagerTek) {
/* 27 */       EntityVillagerTek villager = (EntityVillagerTek)entityLivingBaseIn;
/*    */       
/* 29 */       villager.modifyHappy(1);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\items\PotionBless.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */