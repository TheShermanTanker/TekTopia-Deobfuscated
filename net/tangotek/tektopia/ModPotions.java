/*    */ package net.tangotek.tektopia;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.potion.Potion;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.minecraft.potion.PotionType;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraftforge.registries.IForgeRegistry;
/*    */ import net.minecraftforge.registries.IForgeRegistryEntry;
/*    */ import net.tangotek.tektopia.items.PotionBless;
/*    */ 
/*    */ 
/*    */ public class ModPotions
/*    */ {
/* 16 */   public static final PotionBless potionBless = new PotionBless((new Color(255, 255, 0)).getRGB());
/*    */   
/* 18 */   public static final PotionType potionBlessType = createPotionType(new PotionEffect((Potion)potionBless, 1));
/*    */ 
/*    */ 
/*    */   
/*    */   private static PotionType createPotionType(PotionEffect potionEffect) {
/* 23 */     return createPotionType(potionEffect, null);
/*    */   }
/*    */   
/*    */   private static PotionType createPotionType(PotionEffect potionEffect, @Nullable String namePrefix) {
/* 27 */     ResourceLocation potionTypeName, potionName = potionEffect.getPotion().getRegistryName();
/*    */ 
/*    */     
/* 30 */     if (namePrefix != null) {
/* 31 */       potionTypeName = new ResourceLocation(potionName.getNamespace(), namePrefix + potionName.getPath());
/*    */     } else {
/* 33 */       potionTypeName = potionName;
/*    */     } 
/*    */     
/* 36 */     return (PotionType)(new PotionType(potionName.toString(), new PotionEffect[] { potionEffect })).setRegistryName(potionTypeName);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void registerPotions(IForgeRegistry<Potion> registry) {
/* 41 */     registry.registerAll((IForgeRegistryEntry[])new Potion[] { (Potion)potionBless });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void registerPotionTypes(IForgeRegistry<PotionType> registry) {
/* 48 */     registry.registerAll((IForgeRegistryEntry[])new PotionType[] { potionBlessType });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\ModPotions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */