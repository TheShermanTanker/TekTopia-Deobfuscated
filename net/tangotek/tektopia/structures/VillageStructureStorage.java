/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.tileentity.TileEntityChest;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ 
/*    */ public class VillageStructureStorage
/*    */   extends VillageStructure
/*    */ {
/*    */   protected Map<BlockPos, TileEntityChest> storageBlocks;
/*    */   protected Map<BlockPos, TileEntityChest> newStorageBlocks;
/*    */   
/*    */   protected VillageStructureStorage(World world, Village v, EntityItemFrame itemFrame) {
/* 23 */     super(world, v, itemFrame, VillageStructureType.STORAGE, "Storage");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onFloorScanStart() {
/* 28 */     this.newStorageBlocks = new HashMap<>();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void scanSpecialBlock(BlockPos pos, Block block) {
/* 33 */     TileEntity te = this.world.getTileEntity(pos);
/* 34 */     if (te instanceof TileEntityChest) {
/* 35 */       this.newStorageBlocks.put(pos, (TileEntityChest)te);
/* 36 */       this.specialAdded = true;
/*    */     }
/* 38 */     else if (block == Blocks.CRAFTING_TABLE) {
/* 39 */       addSpecialBlock(Blocks.CRAFTING_TABLE, pos);
/*    */     }
/* 41 */     else if (block == Blocks.FURNACE || block == Blocks.LIT_FURNACE) {
/*    */       
/* 43 */       if (getUnoccupiedSpecialBlock(Blocks.FURNACE) == null) {
/* 44 */         addSpecialBlock(Blocks.FURNACE, pos);
/*    */       }
/*    */     } 
/*    */     
/* 48 */     super.scanSpecialBlock(pos, block);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onFloorScanEnd() {
/* 54 */     if (this.storageBlocks != null) {
/*    */       
/* 56 */       Set<BlockPos> removedKeys = new HashSet<>(this.storageBlocks.keySet());
/* 57 */       removedKeys.removeAll(this.newStorageBlocks.keySet());
/* 58 */       removedKeys.forEach(bp -> this.village.removeStorageChest(this.storageBlocks.get(bp)));
/*    */       
/* 60 */       Set<BlockPos> addedKeys = new HashSet<>(this.newStorageBlocks.keySet());
/* 61 */       addedKeys.removeAll(this.storageBlocks.keySet());
/* 62 */       addedKeys.forEach(bp -> this.village.addStorageChest(this.newStorageBlocks.get(bp)));
/*    */     } else {
/*    */       
/* 65 */       this.village.resetStorage();
/* 66 */       this.newStorageBlocks.values().forEach(chest -> this.village.addStorageChest(chest));
/*    */     } 
/*    */     
/* 69 */     this.storageBlocks = this.newStorageBlocks;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDestroy() {
/* 75 */     this.storageBlocks.values().forEach(chest -> this.village.removeStorageChest(chest));
/* 76 */     super.onDestroy();
/*    */   }
/*    */   
/*    */   public int getMaxAllowed() {
/* 80 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */