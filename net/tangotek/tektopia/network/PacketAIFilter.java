/*    */ package net.tangotek.tektopia.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PacketAIFilter
/*    */   implements IMessage
/*    */ {
/*    */   private Integer entityId;
/*    */   private String aiFilter;
/*    */   private Boolean enabled;
/*    */   
/*    */   public PacketAIFilter() {}
/*    */   
/*    */   public PacketAIFilter(Entity ent, String filterName, boolean e) {
/* 28 */     this.entityId = Integer.valueOf(ent.getEntityId());
/* 29 */     this.aiFilter = filterName;
/* 30 */     this.enabled = Boolean.valueOf(e);
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buf) {
/* 35 */     PacketBuffer pb = new PacketBuffer(buf);
/* 36 */     pb.writeInt(this.entityId.intValue());
/* 37 */     pb.writeString(this.aiFilter);
/* 38 */     pb.writeBoolean(this.enabled.booleanValue());
/*    */   }
/*    */   
/*    */   public void fromBytes(ByteBuf buf) {
/* 42 */     PacketBuffer pb = new PacketBuffer(buf);
/*    */     
/* 44 */     this.entityId = Integer.valueOf(pb.readInt());
/* 45 */     this.aiFilter = pb.readString(30);
/* 46 */     this.enabled = Boolean.valueOf(buf.readBoolean());
/*    */   }
/*    */   
/*    */   public boolean getEnabled() {
/* 50 */     return this.enabled.booleanValue();
/*    */   }
/*    */   public String getAiFilter() {
/* 53 */     return this.aiFilter;
/*    */   }
/*    */   public EntityVillagerTek getVillager(World world) {
/* 56 */     Entity ent = world.getEntityByID(this.entityId.intValue());
/* 57 */     if (ent instanceof EntityVillagerTek) {
/* 58 */       EntityVillagerTek villager = (EntityVillagerTek)ent;
/* 59 */       return villager;
/*    */     } 
/*    */     
/* 62 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public static class PacketAIFilterHandler
/*    */     implements IMessageHandler<PacketAIFilter, IMessage>
/*    */   {
/*    */     public IMessage onMessage(PacketAIFilter message, MessageContext ctx) {
/* 70 */       EntityPlayerMP serverPlayer = (ctx.getServerHandler()).player;
/*    */ 
/*    */       
/* 73 */       serverPlayer.getServerWorld().addScheduledTask(() -> {
/*    */             EntityVillagerTek villager = message.getVillager((World)serverPlayer.getServerWorld());
/*    */             
/*    */             if (villager != null) {
/*    */               villager.setAIFilter(message.getAiFilter(), message.getEnabled());
/*    */               
/*    */               villager.equipBestGear();
/*    */               
/*    */               villager.getDesireSet().forceUpdate();
/*    */             } 
/*    */           });
/* 84 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\network\PacketAIFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */