/*    */ package net.tangotek.tektopia.pathing;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.client.renderer.BufferBuilder;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.util.math.ChunkPos;
/*    */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PathingOverlayRenderer
/*    */ {
/* 25 */   private Map<ChunkPos, PathingOverlayChunk> chunks = new HashMap<>();
/*    */   private boolean enabled = false;
/*    */   
/*    */   public PathingOverlayRenderer() {
/* 29 */     MinecraftForge.EVENT_BUS.register(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleNodeUpdate(PathingNodeClient node) {
/* 34 */     this.enabled = (node != null);
/* 35 */     if (this.enabled) {
/* 36 */       ChunkPos chunkPos = new ChunkPos(node.pos);
/* 37 */       PathingOverlayChunk overlayChunk = this.chunks.get(chunkPos);
/* 38 */       if (overlayChunk == null) {
/* 39 */         overlayChunk = new PathingOverlayChunk(chunkPos);
/* 40 */         this.chunks.put(chunkPos, overlayChunk);
/*    */       } 
/*    */       
/* 43 */       overlayChunk.putNode(node);
/*    */     } else {
/*    */       
/* 46 */       this.chunks.clear();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void renderOverlays(RenderWorldLastEvent event) {
/* 54 */     if (!this.enabled) {
/*    */       return;
/*    */     }
/*    */     
/* 58 */     Minecraft minecraft = Minecraft.getMinecraft();
/* 59 */     WorldClient world = minecraft.world;
/* 60 */     EntityPlayerSP player = minecraft.player;
/* 61 */     if (world == null || player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 65 */     double partialTicks = event.getPartialTicks();
/* 66 */     double viewX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
/* 67 */     double viewY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
/* 68 */     double viewZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
/*    */     
/* 70 */     GlStateManager.pushMatrix();
/* 71 */     GlStateManager.disableTexture2D();
/* 72 */     GlStateManager.enableAlpha();
/* 73 */     GlStateManager.enableBlend();
/* 74 */     BufferBuilder vertexBuffer = Tessellator.getInstance().getBuffer();
/* 75 */     vertexBuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 76 */     vertexBuffer.setTranslation(-viewX, -viewY, -viewZ);
/*    */     
/* 78 */     renderOverlays(world, vertexBuffer, viewX, viewY, viewZ);
/*    */     
/* 80 */     vertexBuffer.setTranslation(0.0D, 0.0D, 0.0D);
/* 81 */     Tessellator.getInstance().draw();
/*    */     
/* 83 */     GlStateManager.enableTexture2D();
/* 84 */     GlStateManager.popMatrix();
/*    */   }
/*    */   
/*    */   private void renderOverlays(WorldClient world, BufferBuilder vertexBuffer, double viewX, double viewY, double viewZ) {
/* 88 */     this.chunks.values().forEach(c -> c.renderOverlays(world, vertexBuffer, viewX, viewY, viewZ));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\pathing\PathingOverlayRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */