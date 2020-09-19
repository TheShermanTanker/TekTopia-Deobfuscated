/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillageStructureFactory
/*    */ {
/*    */   public static VillageStructure create(VillageStructureType t, World w, Village v, EntityItemFrame i) {
/* 25 */     VillageStructure struct = t.create(w, v, i);
/* 26 */     struct.setup();
/* 27 */     return struct;
/*    */   }
/*    */   
/*    */   public static VillageStructureType getByItem(ItemStack i) {
/* 31 */     for (VillageStructureType t : VillageStructureType.values()) {
/* 32 */       if (t.isItemEqual(i)) {
/* 33 */         return t;
/*    */       }
/*    */     } 
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */