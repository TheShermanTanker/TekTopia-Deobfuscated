/*    */ package net.tangotek.tektopia.economy;
/*    */ 
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.math.MathHelper;
/*    */ import net.tangotek.tektopia.ItemTagType;
/*    */ import net.tangotek.tektopia.ModItems;
/*    */ import net.tangotek.tektopia.ProfessionType;
/*    */ 
/*    */ public class ItemValue
/*    */ {
/*    */   private final ItemStack itemStack;
/*    */   private final int baseValue;
/*    */   private final int appearanceWeight;
/*    */   private int purchaseCount;
/*    */   private float markDown;
/*    */   private float markUp;
/*    */   private final ProfessionType requiredProfession;
/* 18 */   private final float PURCHASE_MIN_REDUCTION = 0.1F;
/* 19 */   private final float PURCHASE_MAX_REDUCTION = 0.3F;
/*    */   
/*    */   public ItemValue(ItemStack itemStack, int baseValue, int appearanceWeight, ProfessionType reqProf) {
/* 22 */     this.itemStack = ModItems.makeTaggedItem(itemStack, ItemTagType.VILLAGER);
/* 23 */     this.baseValue = baseValue;
/* 24 */     this.appearanceWeight = appearanceWeight;
/* 25 */     this.requiredProfession = reqProf;
/* 26 */     reset();
/*    */   }
/*    */   
/*    */   public String getName() {
/* 30 */     return getName(this.itemStack);
/*    */   }
/*    */   
/*    */   public static String getName(ItemStack stack) {
/* 34 */     return stack.getItem().getTranslationKey() + "_" + stack.getMetadata();
/*    */   }
/*    */   
/*    */   public ItemStack getItemStack() {
/* 38 */     return this.itemStack;
/*    */   }
/*    */   
/*    */   public ProfessionType getRequiredProfession() {
/* 42 */     return this.requiredProfession;
/*    */   }
/*    */   
/*    */   public int getBaseValue() {
/* 46 */     return this.baseValue;
/*    */   }
/*    */   
/*    */   public int getPurchaseCount() {
/* 50 */     return this.purchaseCount;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public float getAppearanceWeight() {
/* 56 */     return (this.appearanceWeight * getCurrentValue() / getBaseValue());
/*    */   }
/*    */   
/*    */   public int getCurrentValue() {
/* 60 */     return Math.max(1, (int)(this.baseValue - this.markDown + this.markUp));
/*    */   }
/*    */   
/*    */   public void reset() {
/* 64 */     this.purchaseCount = 0;
/* 65 */     this.markDown = 0.0F;
/* 66 */     this.markUp = 0.0F;
/*    */   }
/*    */   
/*    */   public float markDown(float ageLerp) {
/* 70 */     float reduction = (float)MathHelper.clampedLerp(0.10000000149011612D, 0.30000001192092896D, ageLerp) * getCurrentValue();
/* 71 */     this.markDown += reduction;
/* 72 */     this.purchaseCount++;
/* 73 */     return reduction;
/*    */   }
/*    */   
/*    */   public boolean isForSale() {
/* 77 */     return (this.purchaseCount < 8);
/*    */   }
/*    */   
/*    */   public void markUp(float value) {
/* 81 */     this.markUp += value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 86 */     String result = "[" + getName() + "]  pc: " + this.purchaseCount + "   markDown: " + this.markDown + "    markUp: " + this.markUp + "    price: " + getCurrentValue() + "(" + getBaseValue() + ")";
/* 87 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\economy\ItemValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */