/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ 
/*    */ public class VillageStructureKitchen
/*    */   extends VillageStructure {
/*    */   protected VillageStructureKitchen(World world, Village v, EntityItemFrame itemFrame) {
/* 13 */     super(world, v, itemFrame, VillageStructureType.KITCHEN, "Kitchen");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void scanSpecialBlock(BlockPos pos, Block block) {
/* 19 */     if (block == Blocks.CRAFTING_TABLE) {
/* 20 */       addSpecialBlock(Blocks.CRAFTING_TABLE, pos);
/*    */     }
/* 22 */     else if (block == Blocks.FURNACE || block == Blocks.LIT_FURNACE) {
/* 23 */       addSpecialBlock(Blocks.FURNACE, pos);
/*    */     } 
/*    */     
/* 26 */     super.scanSpecialBlock(pos, block);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureKitchen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */