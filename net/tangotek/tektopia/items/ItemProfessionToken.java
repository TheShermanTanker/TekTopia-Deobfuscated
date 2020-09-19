/*    */ package net.tangotek.tektopia.items;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.ProfessionType;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemProfessionToken
/*    */   extends Item
/*    */ {
/*    */   private final String name;
/*    */   private final int cost;
/*    */   private final ProfessionType professionType;
/*    */   private BiFunction<World, EntityVillagerTek, EntityVillagerTek> villagerFunc;
/*    */   
/*    */   public ItemProfessionToken(String name, ProfessionType pt, BiFunction<World, EntityVillagerTek, EntityVillagerTek> biFunction, int emeraldCost) {
/* 24 */     setCreativeTab(CreativeTabs.MISC);
/* 25 */     setRegistryName(name);
/* 26 */     setMaxStackSize(1);
/* 27 */     setTranslationKey(name);
/* 28 */     this.name = name;
/* 29 */     this.professionType = pt;
/* 30 */     this.villagerFunc = biFunction;
/* 31 */     this.cost = emeraldCost;
/*    */   }
/*    */   
/*    */   public int getCost(Village v) {
/* 35 */     float mult = Math.min((v.getTownData().getProfessionSales() / 5) * 0.2F, 10.0F);
/* 36 */     return (int)(this.cost * (1.0F + mult));
/*    */   }
/*    */   
/*    */   public ProfessionType getProfessionType() {
/* 40 */     return this.professionType;
/*    */   }
/*    */   
/*    */   public EntityVillagerTek createVillager(World world, EntityVillagerTek clickedEntity) {
/* 44 */     return this.villagerFunc.apply(world, clickedEntity);
/*    */   }
/*    */   
/*    */   public void registerItemModel() {
/* 48 */     TekVillager.proxy.registerItemRenderer(this, 0, this.name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\items\ItemProfessionToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */