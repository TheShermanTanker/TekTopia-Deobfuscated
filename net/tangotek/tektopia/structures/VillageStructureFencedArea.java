/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.pathing.BasePathingNode;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillageStructureFencedArea
/*    */   extends VillageStructure
/*    */ {
/*    */   protected VillageStructureFencedArea(World world, Village v, EntityItemFrame itemFrame, VillageStructureType t, String name) {
/* 16 */     super(world, v, itemFrame, t, name);
/*    */   }
/*    */   
/*    */   protected BlockPos findDoor() {
/* 20 */     BlockPos dp = null;
/*    */     
/* 22 */     dp = this.framePos.offset(this.signFacing, -1).offset(this.signFacing.rotateY(), 1);
/* 23 */     if (!isWoodDoor(this.world, dp) && !isGate(this.world, dp)) {
/* 24 */       dp = this.framePos.offset(this.signFacing, -1).offset(this.signFacing.rotateY(), -1);
/* 25 */       if (!isWoodDoor(this.world, dp) && !isGate(this.world, dp)) {
/* 26 */         dp = this.framePos.offset(this.signFacing, -1).down(2);
/* 27 */         if (!isWoodDoor(this.world, dp) && !isGate(this.world, dp)) {
/* 28 */           dp = null;
/*    */         }
/*    */       } 
/*    */     } 
/* 32 */     return dp;
/*    */   }
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int scanRoomHeight(BlockPos pos) {
/* 55 */     for (int i = 0; i < 10; i++) {
/* 56 */       BlockPos p = pos.up(i);
/* 57 */       Block b = this.world.getBlockState(p).getBlock();
/* 58 */       scanSpecialBlock(p, b);
/* 59 */       if (!BasePathingNode.isPassable(this.world, p) || isWoodDoor(this.world, pos) || isGate(this.world, pos)) {
/* 60 */         return i;
/*    */       }
/*    */     } 
/* 63 */     return 10;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureFencedArea.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */