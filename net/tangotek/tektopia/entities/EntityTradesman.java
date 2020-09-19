/*    */ package net.tangotek.tektopia.entities;
/*    */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.text.ITextComponent;
/*    */ import net.minecraft.util.text.TextComponentString;
/*    */ import net.minecraft.util.text.TextComponentTranslation;
/*    */ import net.minecraft.util.text.TextFormatting;
/*    */ import net.minecraft.util.text.event.ClickEvent;
/*    */ import net.minecraft.village.MerchantRecipe;
/*    */ import net.minecraft.village.MerchantRecipeList;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.ItemTagType;
/*    */ import net.tangotek.tektopia.ModItems;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ import net.tangotek.tektopia.items.ItemProfessionToken;
/*    */ 
/*    */ public class EntityTradesman extends EntityVendor {
/* 20 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityTradesman.class);
/*    */   
/*    */   static {
/* 23 */     EntityVillagerTek.setupAnimations(animHandler, "tradesman_m");
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityTradesman(World worldIn) {
/* 28 */     super(worldIn, 0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void populateBuyingList() {
/* 34 */     if (this.buyingList == null && hasVillage()) {
/*    */       
/* 36 */       this.buyingList = new MerchantRecipeList();
/*    */       
/* 38 */       for (ItemProfessionToken token : ModItems.professionTokens.values()) {
/* 39 */         if (token.getCost(this.village) > 0) {
/* 40 */           ItemStack tokenItem = ModItems.createTaggedItem((Item)token, ItemTagType.VILLAGER);
/* 41 */           ModItems.bindItemToVillage(tokenItem, this.village);
/* 42 */           this.buyingList.add(createMerchantRecipe(tokenItem, token.getCost(this.village)));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getTranslationKey() {
/* 50 */     return "entity.villager.tradesman";
/*    */   }
/*    */   
/*    */   public AnimationHandler getAnimationHandler() {
/* 54 */     return animHandler;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void useRecipe(MerchantRecipe recipe) {
/* 60 */     super.useRecipe(recipe);
/*    */     
/* 62 */     if (hasVillage() && recipe.getItemToSell().getItem() instanceof ItemProfessionToken) {
/* 63 */       getVillage().getTownData().incrementProfessionSales();
/* 64 */       ModItems.bindItemToVillage(recipe.getItemToSell(), getVillage());
/*    */       
/* 66 */       if (getVillage().getTownData().getProfessionSales() == 10) {
/* 67 */         TextComponentTranslation textComponentTranslation1 = new TextComponentTranslation("advertisement.patreonA1", new Object[0]);
/* 68 */         TextComponentString textComponentString1 = new TextComponentString(" ");
/*    */         
/* 70 */         TextComponentString textComponentString2 = new TextComponentString("patreon.com");
/* 71 */         ClickEvent click = new ClickEvent(ClickEvent.Action.OPEN_URL, "http://www.patreon.com/tangotek");
/* 72 */         textComponentString2.getStyle().setClickEvent(click);
/* 73 */         textComponentString2.getStyle().setUnderlined(Boolean.valueOf(true));
/* 74 */         textComponentString2.getStyle().setColor(TextFormatting.BLUE);
/*    */         
/* 76 */         TextComponentTranslation textComponentTranslation2 = new TextComponentTranslation("advertisement.patreonA2", new Object[0]);
/*    */         
/* 78 */         getVillage().playEvent(SoundEvents.ENTITY_PLAYER_LEVELUP, textComponentTranslation1.appendSibling((ITextComponent)textComponentString1).appendSibling((ITextComponent)textComponentString2).appendSibling((ITextComponent)textComponentString1).appendSibling((ITextComponent)textComponentTranslation2));
/*    */ 
/*    */         
/* 81 */         TextComponentTranslation textComponentTranslation3 = new TextComponentTranslation("advertisement.patreonB1", new Object[0]);
/*    */         
/* 83 */         TextComponentTranslation textComponentTranslation4 = new TextComponentTranslation("advertisement.patreonB2", new Object[0]);
/* 84 */         ClickEvent clickB = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://pbs.twimg.com/media/EBABNv8UEAAjceo?format=jpg&name=large");
/* 85 */         textComponentTranslation4.getStyle().setClickEvent(clickB);
/* 86 */         textComponentTranslation4.getStyle().setUnderlined(Boolean.valueOf(true));
/* 87 */         textComponentTranslation4.getStyle().setColor(TextFormatting.BLUE);
/*    */         
/* 89 */         TextComponentTranslation textComponentTranslation5 = new TextComponentTranslation("advertisement.patreonB3", new Object[0]);
/*    */         
/* 91 */         getVillage().sendChatMessage(textComponentTranslation3.appendSibling((ITextComponent)textComponentString1).appendSibling((ITextComponent)textComponentTranslation4).appendSibling((ITextComponent)textComponentString1).appendSibling((ITextComponent)textComponentTranslation5));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityTradesman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */