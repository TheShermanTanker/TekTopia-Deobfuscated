/*    */ package net.tangotek.tektopia.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.tangotek.tektopia.LicenseTracker;
/*    */ import net.tangotek.tektopia.caps.IPlayerLicense;
/*    */ import net.tangotek.tektopia.caps.PlayerLicenseProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketLicense
/*    */   implements IMessage
/*    */ {
/*    */   private String data;
/*    */   private UUID playerUUID;
/*    */   
/*    */   public PacketLicense() {}
/*    */   
/*    */   public PacketLicense(UUID uuid, String licFormat) {
/* 33 */     this.playerUUID = uuid;
/* 34 */     this.data = licFormat;
/*    */   }
/*    */   
/*    */   public void toBytes(ByteBuf buf) {
/* 38 */     PacketBuffer pb = new PacketBuffer(buf);
/* 39 */     pb.writeUniqueId(this.playerUUID);
/* 40 */     pb.writeString(this.data);
/*    */   }
/*    */   
/*    */   public void fromBytes(ByteBuf buf) {
/* 44 */     PacketBuffer pb = new PacketBuffer(buf);
/* 45 */     this.playerUUID = pb.readUniqueId();
/* 46 */     this.data = pb.readString(1024);
/*    */   }
/*    */   public String getLicenseData() {
/* 49 */     return this.data;
/*    */   }
/*    */   public UUID getPlayerUUID() {
/* 52 */     return this.playerUUID;
/*    */   }
/*    */   
/*    */   public static class PacketLicenseHandler
/*    */     implements IMessageHandler<PacketLicense, IMessage> {
/*    */     public IMessage onMessage(PacketLicense message, MessageContext ctx) {
/* 58 */       if (ctx.side == Side.SERVER) {
/*    */         
/* 60 */         EntityPlayerMP serverPlayer = (ctx.getServerHandler()).player;
/*    */ 
/*    */         
/* 63 */         serverPlayer.getServerWorld().addScheduledTask(() -> LicenseTracker.INSTANCE.submitLicense(serverPlayer, message.getLicenseData()));
/*    */ 
/*    */         
/* 66 */         return null;
/*    */       } 
/*    */ 
/*    */       
/* 70 */       Minecraft.getMinecraft().addScheduledTask(() -> {
/*    */             EntityPlayer targetPlayer = (Minecraft.getMinecraft()).world.getPlayerEntityByUUID(message.getPlayerUUID());
/*    */             
/*    */             ((IPlayerLicense)targetPlayer.getCapability(PlayerLicenseProvider.PLAYER_LICENSE_CAPABILITY, null)).setLicenseData(message.getLicenseData());
/*    */           });
/*    */       
/* 76 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\network\PacketLicense.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */