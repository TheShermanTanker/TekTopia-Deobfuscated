/*    */ package net.tangotek.tektopia.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ import net.tangotek.tektopia.pathing.PathingNodeClient;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketPathingNode
/*    */   implements IMessage
/*    */ {
/*    */   private PathingNodeClient node;
/*    */   
/*    */   public PacketPathingNode() {}
/*    */   
/*    */   public PacketPathingNode(PathingNodeClient n) {
/* 23 */     this.node = n;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buf) {
/* 28 */     if (this.node != null) {
/* 29 */       this.node.toBytes(buf);
/*    */     }
/*    */   }
/*    */   
/*    */   public void fromBytes(ByteBuf buf) {
/* 34 */     if (buf.isReadable()) {
/* 35 */       this.node = new PathingNodeClient(buf);
/*    */     } else {
/* 37 */       this.node = null;
/*    */     } 
/*    */   } public PathingNodeClient getNode() {
/* 40 */     return this.node;
/*    */   }
/*    */   
/*    */   public static class PacketPathingNodeHandler
/*    */     implements IMessageHandler<PacketPathingNode, IMessage> {
/*    */     public IMessage onMessage(PacketPathingNode message, MessageContext ctx) {
/* 46 */       Minecraft.getMinecraft().addScheduledTask(() -> TekVillager.proxy.handleNodeUpdate(message.getNode()));
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 51 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\network\PacketPathingNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */