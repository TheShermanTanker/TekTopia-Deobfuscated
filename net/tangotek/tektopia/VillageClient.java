/*    */ package net.tangotek.tektopia;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ 
/*    */ 
/*    */ public class VillageClient
/*    */ {
/*    */   private BlockPos center;
/*    */   private int villageSize;
/*    */   
/*    */   public VillageClient(ByteBuf buf) {
/* 13 */     this.center = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
/* 14 */     this.villageSize = buf.readInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public VillageClient(Village village) {
/* 19 */     this.center = village.getOrigin();
/* 20 */     this.villageSize = village.getSize();
/*    */   }
/*    */   
/*    */   public int getMaxX() {
/* 24 */     return this.center.getX() + this.villageSize;
/*    */   }
/*    */   
/*    */   public int getMaxZ() {
/* 28 */     return this.center.getZ() + this.villageSize;
/*    */   }
/*    */   
/*    */   public int getMinX() {
/* 32 */     return this.center.getX() - this.villageSize;
/*    */   }
/*    */   
/*    */   public int getMinZ() {
/* 36 */     return this.center.getZ() - this.villageSize;
/*    */   }
/*    */   
/*    */   public void toBytes(ByteBuf buf) {
/* 40 */     buf.writeInt(this.center.getX());
/* 41 */     buf.writeInt(this.center.getY());
/* 42 */     buf.writeInt(this.center.getZ());
/*    */     
/* 44 */     buf.writeInt(this.villageSize);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\VillageClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */