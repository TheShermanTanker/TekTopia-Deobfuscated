/*    */ package net.tangotek.tektopia.entities;
/*    */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.village.MerchantRecipe;
/*    */ import net.minecraft.village.MerchantRecipeList;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.ItemTagType;
/*    */ import net.tangotek.tektopia.ModItems;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ import net.tangotek.tektopia.items.ItemStructureToken;
/*    */ 
/*    */ public class EntityArchitect extends EntityVendor {
/* 14 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityArchitect.class);
/*    */   
/*    */   static {
/* 17 */     EntityVillagerTek.setupAnimations(animHandler, "architect_m");
/*    */   }
/*    */   
/*    */   public EntityArchitect(World worldIn) {
/* 21 */     super(worldIn, 0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void populateBuyingList() {
/* 27 */     if (this.buyingList == null && hasVillage()) {
/*    */       
/* 29 */       this.buyingList = new MerchantRecipeList();
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 34 */       for (ItemStructureToken token : ModItems.structureTokens) {
/* 35 */         if (token.getCost(getVillage()) > 0) {
/* 36 */           ItemStack tokenItem = ModItems.createTaggedItem((Item)token, ItemTagType.VILLAGER);
/* 37 */           ModItems.bindItemToVillage(tokenItem, this.village);
/* 38 */           this.buyingList.add(createMerchantRecipe(tokenItem, token.getCost(this.village)));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTranslationKey() {
/* 46 */     return "entity.villager.architect";
/*    */   }
/*    */   
/*    */   public AnimationHandler getAnimationHandler() {
/* 50 */     return animHandler;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void useRecipe(MerchantRecipe recipe) {
/* 57 */     super.useRecipe(recipe);
/*    */     
/* 59 */     if (hasVillage() && recipe.getItemToSell().getItem() instanceof ItemStructureToken)
/* 60 */       ModItems.bindItemToVillage(recipe.getItemToSell(), getVillage()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityArchitect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */