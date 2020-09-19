/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ 
/*    */ public class VillageStructureMerchantStall
/*    */   extends VillageStructure {
/* 11 */   protected long lastVisited = 0L;
/*    */   
/*    */   protected VillageStructureMerchantStall(World world, Village v, EntityItemFrame itemFrame) {
/* 14 */     super(world, v, itemFrame, VillageStructureType.MERCHANT_STALL, "Merchant Stall");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doFloorScan() {
/* 19 */     super.doFloorScan();
/* 20 */     this.safeSpot = this.door;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void scanFloor(BlockPos pos) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockPos findDoor() {
/* 30 */     BlockPos testPos = this.framePos;
/* 31 */     int max = 10;
/* 32 */     while (!this.village.getPathingGraph().isInGraph(testPos) && max > 0) {
/* 33 */       testPos = testPos.down();
/* 34 */       max--;
/*    */     } 
/*    */     
/* 37 */     if (max > 0) {
/* 38 */       return testPos;
/*    */     }
/* 40 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean validate() {
/* 45 */     this.isValid = true;
/*    */     
/* 47 */     if (this.door == null) {
/*    */       
/* 49 */       this.isValid = false;
/*    */     }
/* 51 */     else if (this.world.isBlockLoaded(this.door)) {
/*    */       
/* 53 */       Entity e = this.world.getEntityByID(this.signEntityId);
/* 54 */       if (this.isValid && (e == null || !(e instanceof EntityItemFrame))) {
/* 55 */         debugOut("Village struct frame is missing or wrong type | " + getFramePos());
/* 56 */         this.isValid = false;
/*    */       } 
/*    */       
/* 59 */       EntityItemFrame itemFrame = (EntityItemFrame)e;
/* 60 */       if (this.isValid && itemFrame.getHangingPosition() != this.framePos) {
/* 61 */         debugOut("Village struct center has moved" + getFramePos());
/* 62 */         this.isValid = false;
/*    */       } 
/*    */       
/* 65 */       if (this.isValid && !this.type.isItemEqual(itemFrame.getDisplayedItem())) {
/* 66 */         debugOut("Village struct frame item has changed" + getFramePos());
/* 67 */         this.isValid = false;
/*    */       } 
/*    */     } 
/*    */     
/* 71 */     return this.isValid;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureMerchantStall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */