/*    */ package net.tangotek.tektopia.pathing;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.client.renderer.BufferBuilder;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.ChunkPos;
/*    */ 
/*    */ 
/*    */ public class PathingOverlayChunk
/*    */ {
/*    */   private final ChunkPos chunkPos;
/* 14 */   private Map<BlockPos, PathingNodeClient> nodes = new HashMap<>();
/*    */   
/* 16 */   private final int RENDER_RADIUS = 30;
/* 17 */   private final int RENDER_RADIUS_SQ = 900;
/*    */   
/*    */   public PathingOverlayChunk(ChunkPos cp) {
/* 20 */     this.chunkPos = cp;
/*    */   }
/*    */   
/*    */   public void putNode(PathingNodeClient node) {
/* 24 */     if (node.isDestroyed) {
/* 25 */       this.nodes.remove(node.pos);
/*    */     }
/* 27 */     else if (this.nodes.put(node.pos, node) != null) {
/* 28 */       node.setAge(400);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void renderOverlays(WorldClient world, BufferBuilder vertexBuffer, double viewX, double viewY, double viewZ) {
/* 33 */     if (this.chunkPos.getXStart() - viewX > 30.0D || viewX - this.chunkPos.getXEnd() > 30.0D || this.chunkPos.getZStart() - viewZ > 30.0D || viewZ - this.chunkPos.getZEnd() > 30.0D) {
/*    */       return;
/*    */     }
/* 36 */     for (PathingNodeClient node : this.nodes.values()) {
/* 37 */       if (node.pos.distanceSq(viewX, viewY, viewZ) < 900.0D) {
/* 38 */         renderNode(world, vertexBuffer, node);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void renderNode(WorldClient world, BufferBuilder vertexBuffer, PathingNodeClient node) {
/* 46 */     for (int index = 0; index < 4; index++) {
/* 47 */       vertexBuffer.pos(node.getX(index), node.getY(index), node.getZ(index))
/* 48 */         .color(170, 170, 170, 140).endVertex();
/*    */     }
/*    */ 
/*    */     
/* 52 */     node.connections.forEach(conn -> {
/*    */           double innerEdge = 0.25D;
/*    */           double outerEdge = 0.75D;
/*    */           double width = 0.15D;
/*    */           double gap = 0.03D;
/*    */           double x = node.pos.getX() + 0.5D;
/*    */           double z = node.pos.getZ() + 0.5D;
/*    */           double y = node.pos.getY() + 0.05D;
/*    */           for (int index = 0; index < 4; index++) {
/*    */             if (conn.xOffset == 0) {
/*    */               if (conn.zOffset == 1) {
/*    */                 vertexBuffer.pos(x - 0.03D, y, z + 0.25D).color(98, 209, 83, 160).endVertex();
/*    */                 vertexBuffer.pos(x - 0.03D - 0.15D, y, z + 0.25D).color(98, 209, 83, 160).endVertex();
/*    */                 vertexBuffer.pos(x - 0.03D - 0.15D, y + conn.yOffset, z + 0.75D).color(230, 230, 110, 160).endVertex();
/*    */                 vertexBuffer.pos(x - 0.03D, y + conn.yOffset, z + 0.75D).color(230, 230, 110, 160).endVertex();
/*    */               } else {
/*    */                 vertexBuffer.pos(x + 0.03D, y, z - 0.25D).color(98, 209, 83, 160).endVertex();
/*    */                 vertexBuffer.pos(x + 0.03D + 0.15D, y, z - 0.25D).color(98, 209, 83, 160).endVertex();
/*    */                 vertexBuffer.pos(x + 0.03D + 0.15D, y + conn.yOffset, z - 0.75D).color(230, 230, 110, 160).endVertex();
/*    */                 vertexBuffer.pos(x + 0.03D, y + conn.yOffset, z - 0.75D).color(230, 230, 110, 160).endVertex();
/*    */               } 
/*    */             } else if (conn.xOffset == 1) {
/*    */               vertexBuffer.pos(x + 0.25D, y, z + 0.03D).color(98, 209, 83, 160).endVertex();
/*    */               vertexBuffer.pos(x + 0.25D, y, z + 0.03D + 0.15D).color(98, 209, 83, 160).endVertex();
/*    */               vertexBuffer.pos(x + 0.75D, y + conn.yOffset, z + 0.03D + 0.15D).color(230, 230, 110, 160).endVertex();
/*    */               vertexBuffer.pos(x + 0.75D, y + conn.yOffset, z + 0.03D).color(230, 230, 110, 160).endVertex();
/*    */             } else {
/*    */               vertexBuffer.pos(x - 0.25D, y, z - 0.03D).color(98, 209, 83, 160).endVertex();
/*    */               vertexBuffer.pos(x - 0.25D, y, z - 0.03D - 0.15D).color(98, 209, 83, 160).endVertex();
/*    */               vertexBuffer.pos(x - 0.75D, y + conn.yOffset, z - 0.03D - 0.15D).color(230, 230, 110, 160).endVertex();
/*    */               vertexBuffer.pos(x - 0.75D, y + conn.yOffset, z - 0.03D).color(230, 230, 110, 160).endVertex();
/*    */             } 
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\pathing\PathingOverlayChunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */