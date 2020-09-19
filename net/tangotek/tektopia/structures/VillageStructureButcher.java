/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ 
/*    */ public class VillageStructureButcher
/*    */   extends VillageStructure {
/*    */   protected VillageStructureButcher(World world, Village v, EntityItemFrame itemFrame) {
/* 13 */     super(world, v, itemFrame, VillageStructureType.BUTCHER, "Butcher");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void scanSpecialBlock(BlockPos pos, Block block) {
/* 18 */     if (block == Blocks.CRAFTING_TABLE) {
/* 19 */       addSpecialBlock(Blocks.CRAFTING_TABLE, pos);
/*    */     }
/*    */     
/* 22 */     super.scanSpecialBlock(pos, block);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureButcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */