/*    */ package net.tangotek.tektopia.pathing;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ 
/*    */ 
/*    */ public class PathingNodeClient
/*    */ {
/*    */   public final BlockPos pos;
/*    */   public final boolean isDestroyed;
/* 13 */   public List<PathingNodeClientConnection> connections = new ArrayList<>(4);
/*    */   
/* 15 */   private int age = 0;
/*    */   
/*    */   public class PathingNodeClientConnection {
/*    */     public final byte xOffset;
/*    */     public final byte zOffset;
/*    */     public final byte yOffset;
/*    */     
/*    */     public PathingNodeClientConnection(byte x, byte y, byte z) {
/* 23 */       this.xOffset = x;
/* 24 */       this.yOffset = y;
/* 25 */       this.zOffset = z;
/*    */     }
/*    */   }
/*    */   
/*    */   public PathingNodeClient(ByteBuf buf) {
/* 30 */     this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
/* 31 */     this.isDestroyed = buf.readBoolean();
/* 32 */     int count = buf.readInt();
/* 33 */     for (int i = 0; i < count; i++) {
/* 34 */       PathingNodeClientConnection newConn = new PathingNodeClientConnection(buf.readByte(), buf.readByte(), buf.readByte());
/* 35 */       this.connections.add(newConn);
/*    */     } 
/*    */   }
/*    */   
/*    */   public PathingNodeClient(PathingNode node) {
/* 40 */     this.pos = node.getBlockPos();
/* 41 */     this.isDestroyed = node.isDestroyed();
/* 42 */     for (PathingNode connection : node.connections) {
/* 43 */       byte xOffset = (byte)(connection.cell.x - node.cell.x);
/* 44 */       byte yOffset = (byte)(connection.cell.y - node.cell.y);
/* 45 */       byte zOffset = (byte)(connection.cell.z - node.cell.z);
/* 46 */       this.connections.add(new PathingNodeClientConnection(xOffset, yOffset, zOffset));
/*    */     } 
/*    */   }
/*    */   
/*    */   public double getX(int index) {
/* 51 */     return (index < 2) ? (this.pos.getX() + 0.1D) : (this.pos.getX() + 0.9D);
/*    */   }
/*    */   
/*    */   public double getZ(int index) {
/* 55 */     return (index == 0 || index == 3) ? (this.pos.getZ() + 0.1D) : (this.pos.getZ() + 0.9D);
/*    */   }
/*    */   
/*    */   public double getY(int index) {
/* 59 */     return this.pos.getY() + 0.03D;
/*    */   }
/*    */   
/*    */   public int getAge() {
/* 63 */     this.age = Math.max(60, this.age--);
/* 64 */     return (this.age > 60) ? 255 : this.age;
/*    */   }
/*    */   
/*    */   public void setAge(int a) {
/* 68 */     this.age = a;
/*    */   }
/*    */   
/*    */   public void toBytes(ByteBuf buf) {
/* 72 */     buf.writeInt(this.pos.getX());
/* 73 */     buf.writeInt(this.pos.getY());
/* 74 */     buf.writeInt(this.pos.getZ());
/*    */     
/* 76 */     buf.writeBoolean(this.isDestroyed);
/*    */     
/* 78 */     buf.writeInt(this.connections.size());
/* 79 */     this.connections.forEach(c -> {
/*    */           buf.writeByte(c.xOffset);
/*    */           buf.writeByte(c.yOffset);
/*    */           buf.writeByte(c.zOffset);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\pathing\PathingNodeClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */