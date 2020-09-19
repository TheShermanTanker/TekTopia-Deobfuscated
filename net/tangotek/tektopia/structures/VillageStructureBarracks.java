/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.item.EntityArmorStand;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillageStructureBarracks
/*    */   extends VillageStructureHome
/*    */ {
/*    */   protected VillageStructureBarracks(World world, Village v, EntityItemFrame itemFrame) {
/* 21 */     super(world, v, itemFrame, VillageStructureType.BARRACKS, "Barracks", 10);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onFloorScanStart() {
/* 26 */     List<EntityArmorStand> stands = getEntitiesInside(EntityArmorStand.class);
/*    */     
/* 28 */     stands.forEach(s -> addSpecialBlock(Blocks.MONSTER_EGG, s.getPosition()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void scanSpecialBlock(BlockPos pos, Block block) {
/* 35 */     super.scanSpecialBlock(pos, block);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canVillagerSleep(EntityVillagerTek villager) {
/* 40 */     return villager instanceof net.tangotek.tektopia.entities.EntityGuard;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 45 */     super.update();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureBarracks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */