/*    */ package net.tangotek.tektopia.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ import net.tangotek.tektopia.VillageClient;
/*    */ 
/*    */ public class PacketVillage
/*    */   implements IMessage
/*    */ {
/*    */   private VillageClient villageClient;
/*    */   
/*    */   public PacketVillage() {}
/*    */   
/*    */   public PacketVillage(VillageClient vc) {
/* 19 */     this.villageClient = vc;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buf) {
/* 24 */     if (this.villageClient != null) {
/* 25 */       this.villageClient.toBytes(buf);
/*    */     }
/*    */   }
/*    */   
/*    */   public void fromBytes(ByteBuf buf) {
/* 30 */     if (buf.isReadable()) {
/* 31 */       this.villageClient = new VillageClient(buf);
/*    */     } else {
/* 33 */       this.villageClient = null;
/*    */     } 
/*    */   } public VillageClient getVillageClient() {
/* 36 */     return this.villageClient;
/*    */   }
/*    */   
/*    */   public static class PacketVillageHandler
/*    */     implements IMessageHandler<PacketVillage, IMessage> {
/*    */     public IMessage onMessage(PacketVillage message, MessageContext ctx) {
/* 42 */       Minecraft.getMinecraft().addScheduledTask(() -> TekVillager.proxy.handleVillage(message.getVillageClient()));
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 47 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\network\PacketVillage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */