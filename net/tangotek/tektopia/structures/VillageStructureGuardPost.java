/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ 
/*    */ public class VillageStructureGuardPost
/*    */   extends VillageStructure
/*    */ {
/* 12 */   protected long lastVisited = 0L;
/*    */   
/*    */   protected VillageStructureGuardPost(World world, Village v, EntityItemFrame itemFrame) {
/* 15 */     super(world, v, itemFrame, VillageStructureType.GUARD_POST, "Guard Post");
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doFloorScan() {
/* 20 */     super.doFloorScan();
/* 21 */     this.safeSpot = this.door;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean vacateSpecialBlock(BlockPos bp) {
/* 26 */     this.lastVisited = this.world.getTotalWorldTime();
/* 27 */     return super.vacateSpecialBlock(bp);
/*    */   }
/*    */   
/*    */   public long getTimeSinceVisit() {
/* 31 */     return this.world.getTotalWorldTime() - this.lastVisited;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void scanFloor(BlockPos pos) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockPos findDoor() {
/* 41 */     BlockPos testPos = this.framePos;
/* 42 */     int max = 10;
/* 43 */     while (!this.village.getPathingGraph().isInGraph(testPos) && max > 0) {
/* 44 */       testPos = testPos.down();
/* 45 */       max--;
/*    */     } 
/*    */     
/* 48 */     if (max > 0) {
/* 49 */       return testPos;
/*    */     }
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean validate() {
/* 56 */     this.isValid = true;
/*    */     
/* 58 */     if (this.door == null) {
/*    */       
/* 60 */       this.isValid = false;
/*    */     }
/* 62 */     else if (this.world.isBlockLoaded(this.door)) {
/*    */       
/* 64 */       Entity e = this.world.getEntityByID(this.signEntityId);
/* 65 */       if (this.isValid && (e == null || !(e instanceof EntityItemFrame))) {
/* 66 */         debugOut("Village struct frame is missing or wrong type | " + getFramePos());
/* 67 */         this.isValid = false;
/*    */       } 
/*    */       
/* 70 */       EntityItemFrame itemFrame = (EntityItemFrame)e;
/* 71 */       if (this.isValid && itemFrame.getHangingPosition() != this.framePos) {
/* 72 */         debugOut("Village struct center has moved" + getFramePos());
/* 73 */         this.isValid = false;
/*    */       } 
/*    */       
/* 76 */       if (this.isValid && !this.type.isItemEqual(itemFrame.getDisplayedItem())) {
/* 77 */         debugOut("Village struct frame item has changed" + getFramePos());
/* 78 */         this.isValid = false;
/*    */       } 
/*    */     } 
/*    */     
/* 82 */     return this.isValid;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureGuardPost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */