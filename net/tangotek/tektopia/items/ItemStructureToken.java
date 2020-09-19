/*    */ package net.tangotek.tektopia.items;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.client.util.ITooltipFlag;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.text.TextFormatting;
/*    */ import net.minecraft.util.text.translation.I18n;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.common.capabilities.ICapabilityProvider;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import net.tangotek.tektopia.ItemTagType;
/*    */ import net.tangotek.tektopia.ModItems;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.caps.IVillageData;
/*    */ import net.tangotek.tektopia.caps.VillageDataProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemStructureToken
/*    */   extends Item
/*    */ {
/*    */   private int cost;
/*    */   private String name;
/*    */   
/*    */   public ItemStructureToken(String name, int cost) {
/* 36 */     setCreativeTab(CreativeTabs.MISC);
/* 37 */     setMaxStackSize(1);
/* 38 */     setRegistryName(name);
/* 39 */     setTranslationKey(name);
/* 40 */     this.name = name;
/* 41 */     this.cost = cost;
/*    */   }
/*    */   
/*    */   public int getCost(Village v) {
/* 45 */     float mult = Math.min((v.getTownData().getProfessionSales() / 5) * 0.2F, 10.0F);
/* 46 */     return (int)(this.cost * (1.0F + mult));
/*    */   }
/*    */   
/*    */   public void registerItemModel() {
/* 50 */     TekVillager.proxy.registerItemRenderer(this, 0, this.name);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
/* 56 */     if (equals(ModItems.structureTownHall)) {
/* 57 */       return (ICapabilityProvider)new VillageDataProvider();
/*    */     }
/* 59 */     return super.initCapabilities(stack, nbt);
/*    */   }
/*    */ 
/*    */   
/*    */   @SideOnly(Side.CLIENT)
/*    */   public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
/* 65 */     if (equals(ModItems.structureTownHall)) {
/* 66 */       IVillageData vd = (IVillageData)stack.getCapability(VillageDataProvider.VILLAGE_DATA_CAPABILITY, null);
/* 67 */       if (vd != null && 
/* 68 */         vd.getEconomy().getSalesHistorySize() > 0) {
/* 69 */         tooltip.add(TextFormatting.RED + I18n.translateToLocal("tooltips.saleshistory") + " " + vd.getEconomy().getSalesHistorySize());
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @SideOnly(Side.CLIENT)
/*    */   public boolean hasEffect(ItemStack stack) {
/* 85 */     return (stack.isOnItemFrame() && ModItems.isTaggedItem(stack, ItemTagType.STRUCTURE));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\items\ItemStructureToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */