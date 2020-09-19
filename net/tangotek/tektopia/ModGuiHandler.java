/*    */ package net.tangotek.tektopia;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.network.IGuiHandler;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ import net.tangotek.tektopia.gui.ContainerVillager;
/*    */ import net.tangotek.tektopia.gui.GuiVillager;
/*    */ 
/*    */ public class ModGuiHandler
/*    */   implements IGuiHandler {
/*    */   public static final int VILLAGER_INFO = 0;
/*    */   
/*    */   public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
/*    */     Entity ent;
/* 18 */     switch (ID) {
/*    */       
/*    */       case 0:
/* 21 */         ent = world.getEntityByID(x);
/* 22 */         if (ent instanceof EntityVillagerTek) {
/* 23 */           EntityVillagerTek villager = (EntityVillagerTek)ent;
/* 24 */           return (Container)new ContainerVillager(villager.getInventory());
/*    */         }  break;
/*    */     } 
/* 27 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
/*    */     Entity ent;
/* 33 */     switch (ID) {
/*    */       case 0:
/* 35 */         ent = world.getEntityByID(x);
/* 36 */         if (ent instanceof EntityVillagerTek) {
/* 37 */           EntityVillagerTek villager = (EntityVillagerTek)ent;
/* 38 */           return new GuiVillager(villager);
/*    */         } 
/*    */         break;
/*    */     } 
/* 42 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\ModGuiHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */