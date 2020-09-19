/*    */ package net.tangotek.tektopia.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ public class PacketVillagerThought
/*    */   implements IMessage {
/*    */   private EntityVillagerTek.VillagerThought thought;
/*    */   private float scale;
/*    */   private int entityId;
/*    */   
/*    */   public PacketVillagerThought() {}
/*    */   
/*    */   public PacketVillagerThought(EntityVillagerTek v, EntityVillagerTek.VillagerThought thought, float scale) {
/* 20 */     this.thought = thought;
/* 21 */     this.scale = scale;
/* 22 */     this.entityId = v.getEntityId();
/*    */   }
/*    */   
/*    */   public void toBytes(ByteBuf buf) {
/* 26 */     buf.writeInt(this.thought.getVal());
/* 27 */     buf.writeInt(this.entityId);
/* 28 */     buf.writeFloat(this.scale);
/*    */   }
/*    */   
/*    */   public void fromBytes(ByteBuf buf) {
/* 32 */     this.thought = EntityVillagerTek.VillagerThought.valueOf(buf.readInt());
/* 33 */     this.entityId = buf.readInt();
/* 34 */     this.scale = buf.readFloat();
/*    */   }
/*    */   
/* 37 */   public float getScale() { return this.scale; } public EntityVillagerTek.VillagerThought getThought() {
/* 38 */     return this.thought;
/*    */   }
/*    */   public EntityVillagerTek getVillager() {
/* 41 */     Entity ent = (Minecraft.getMinecraft()).world.getEntityByID(this.entityId);
/* 42 */     if (ent instanceof EntityVillagerTek) {
/* 43 */       EntityVillagerTek villager = (EntityVillagerTek)ent;
/* 44 */       return villager;
/*    */     } 
/*    */     
/* 47 */     return null;
/*    */   }
/*    */   
/*    */   public static class PacketVillagerThoughtHandler
/*    */     implements IMessageHandler<PacketVillagerThought, IMessage> {
/*    */     public IMessage onMessage(PacketVillagerThought message, MessageContext ctx) {
/* 53 */       Minecraft.getMinecraft().addScheduledTask(() -> {
/*    */             EntityVillagerTek villager = message.getVillager();
/*    */             
/*    */             if (villager != null) {
/*    */               villager.handleThought(message);
/*    */             }
/*    */           });
/* 60 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\network\PacketVillagerThought.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */