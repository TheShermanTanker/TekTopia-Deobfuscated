/*    */ package net.tangotek.tektopia.items;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ import net.minecraft.client.util.ITooltipFlag;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.inventory.EntityEquipmentSlot;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemArmor;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.text.TextFormatting;
/*    */ import net.minecraft.util.text.translation.I18n;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import net.tangotek.tektopia.LicenseTracker;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ import net.tangotek.tektopia.caps.IPlayerLicense;
/*    */ import net.tangotek.tektopia.caps.PlayerLicenseProvider;
/*    */ import net.tangotek.tektopia.client.ModelRancherHat;
/*    */ 
/*    */ public class ItemRancherHat
/*    */   extends ItemArmor
/*    */ {
/*    */   private String name;
/*    */   private ModelRancherHat hatModel;
/*    */   
/*    */   public ItemRancherHat(String name) {
/* 34 */     super(Items.DIAMOND_HELMET.getArmorMaterial(), Items.DIAMOND_HELMET.renderIndex, EntityEquipmentSlot.HEAD);
/* 35 */     setCreativeTab(CreativeTabs.COMBAT);
/* 36 */     setRegistryName(name);
/* 37 */     setTranslationKey(name);
/* 38 */     this.name = name;
/*    */   }
/*    */   
/*    */   public void registerItemModel() {
/* 42 */     TekVillager.proxy.registerItemRenderer((Item)this, 0, this.name);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @SideOnly(Side.CLIENT)
/*    */   public ModelBiped getArmorModel(EntityLivingBase living, ItemStack stack, EntityEquipmentSlot slot, ModelBiped defaultModel) {
/* 49 */     if (!stack.isEmpty() && hasLicense((Entity)living, LicenseTracker.Feature.HATS)) {
/*    */       
/* 51 */       if (this.hatModel == null) {
/* 52 */         this.hatModel = new ModelRancherHat();
/*    */       }
/*    */       
/* 55 */       return (ModelBiped)this.hatModel;
/*    */     } 
/*    */     
/* 58 */     return null;
/*    */   }
/*    */   
/*    */   private boolean hasLicense(Entity ent, LicenseTracker.Feature feature) {
/* 62 */     if (ent instanceof AbstractClientPlayer) {
/* 63 */       AbstractClientPlayer player = (AbstractClientPlayer)ent;
/* 64 */       if (((IPlayerLicense)player.getCapability(PlayerLicenseProvider.PLAYER_LICENSE_CAPABILITY, null)).hasFeature(feature)) {
/* 65 */         return true;
/*    */       }
/*    */     } 
/*    */     
/* 69 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   @SideOnly(Side.CLIENT)
/*    */   public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
/* 78 */     if (hasLicense(entity, LicenseTracker.Feature.HATS)) {
/* 79 */       return "tektopia:textures/entity/rancher_hat.png";
/*    */     }
/* 81 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   @SideOnly(Side.CLIENT)
/*    */   public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
/* 87 */     tooltip.add(TextFormatting.YELLOW + "" + TextFormatting.BOLD + I18n.translateToLocal("item.rancher_hat.text1"));
/* 88 */     tooltip.add(TextFormatting.GOLD + "" + TextFormatting.ITALIC + I18n.translateToLocal("item.rancher_hat.text2"));
/* 89 */     tooltip.add(TextFormatting.GOLD + "" + TextFormatting.ITALIC + I18n.translateToLocal("item.rancher_hat.text3"));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\items\ItemRancherHat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */