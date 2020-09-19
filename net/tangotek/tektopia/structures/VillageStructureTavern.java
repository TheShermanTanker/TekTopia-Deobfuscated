/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.entities.EntityBard;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillageStructureTavern
/*    */   extends VillageStructure
/*    */ {
/* 18 */   private EntityBard performingBard = null;
/*    */   
/*    */   protected VillageStructureTavern(World world, Village v, EntityItemFrame itemFrame) {
/* 21 */     super(world, v, itemFrame, VillageStructureType.TAVERN, "Tavern");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void scanSpecialBlock(BlockPos pos, Block block) {
/* 27 */     if (block == Blocks.NOTEBLOCK) {
/* 28 */       addSpecialBlock(Blocks.NOTEBLOCK, pos);
/*    */     }
/*    */     
/* 31 */     super.scanSpecialBlock(pos, block);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 36 */     super.update();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPerformingBard(EntityBard bard) {
/* 41 */     this.performingBard = bard;
/*    */   }
/*    */   
/*    */   public EntityBard getPerformingBard() {
/* 45 */     return this.performingBard;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSitTime(EntityVillagerTek villager) {
/* 50 */     return 3000 + villager.getRNG().nextInt(2000);
/*    */   }
/*    */   
/*    */   protected boolean shouldVillagerSit(EntityVillagerTek villager) {
/* 54 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureTavern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */