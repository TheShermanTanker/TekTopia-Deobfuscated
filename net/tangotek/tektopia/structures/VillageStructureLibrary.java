/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ 
/*    */ 
/*    */ public class VillageStructureLibrary
/*    */   extends VillageStructure
/*    */ {
/*    */   protected VillageStructureLibrary(World world, Village v, EntityItemFrame itemFrame) {
/* 15 */     super(world, v, itemFrame, VillageStructureType.LIBRARY, "Library");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void scanSpecialBlock(BlockPos pos, Block block) {
/* 20 */     if (block == Blocks.ENCHANTING_TABLE) {
/* 21 */       addSpecialBlock(Blocks.ENCHANTING_TABLE, pos);
/*    */     }
/* 23 */     else if (block == Blocks.BOOKSHELF) {
/* 24 */       addSpecialBlock(Blocks.BOOKSHELF, pos);
/*    */     }
/* 26 */     else if (block == Blocks.CRAFTING_TABLE) {
/* 27 */       addSpecialBlock(Blocks.CRAFTING_TABLE, pos);
/*    */     } 
/*    */     
/* 30 */     super.scanSpecialBlock(pos, block);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 35 */     super.update();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureLibrary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */