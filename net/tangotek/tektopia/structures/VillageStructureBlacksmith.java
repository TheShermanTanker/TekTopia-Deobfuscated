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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillageStructureBlacksmith
/*    */   extends VillageStructure
/*    */ {
/*    */   protected VillageStructureBlacksmith(World world, Village v, EntityItemFrame itemFrame) {
/* 19 */     super(world, v, itemFrame, VillageStructureType.BLACKSMITH, "Blacksmith");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void scanSpecialBlock(BlockPos pos, Block block) {
/* 24 */     if (block == Blocks.ANVIL) {
/* 25 */       addSpecialBlock(Blocks.ANVIL, pos);
/*    */     }
/* 27 */     else if (block == Blocks.ANVIL) {
/* 28 */       addSpecialBlock(Blocks.ANVIL, pos);
/*    */     }
/* 30 */     else if (block == Blocks.FURNACE || block == Blocks.LIT_FURNACE) {
/* 31 */       addSpecialBlock(Blocks.FURNACE, pos);
/*    */     } 
/*    */     
/* 34 */     super.scanSpecialBlock(pos, block);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureBlacksmith.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */