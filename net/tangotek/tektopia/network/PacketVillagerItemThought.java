/*    */ package net.tangotek.tektopia.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ import net.tangotek.tektopia.ModEntities;
/*    */ 
/*    */ public class PacketVillagerItemThought
/*    */   implements IMessage {
/*    */   private int itemId;
/*    */   private int entityId;
/*    */   
/*    */   public PacketVillagerItemThought() {}
/*    */   
/*    */   public PacketVillagerItemThought(Entity v, Item item) {
/* 20 */     this.itemId = Item.REGISTRY.getIDForObject(item);
/* 21 */     this.entityId = v.getEntityId();
/*    */   }
/*    */   
/*    */   public void toBytes(ByteBuf buf) {
/* 25 */     buf.writeInt(this.itemId);
/* 26 */     buf.writeInt(this.entityId);
/*    */   }
/*    */   
/*    */   public void fromBytes(ByteBuf buf) {
/* 30 */     this.itemId = buf.readInt();
/* 31 */     this.entityId = buf.readInt();
/*    */   }
/*    */   public Item getItem() {
/* 34 */     return (Item)Item.REGISTRY.getObjectById(this.itemId);
/*    */   }
/*    */   public int getEntityId() {
/* 37 */     return this.entityId;
/*    */   }
/*    */   
/*    */   public Entity getEntity() {
/* 41 */     Entity ent = (Minecraft.getMinecraft()).world.getEntityByID(this.entityId);
/* 42 */     return ent;
/*    */   }
/*    */ 
/*    */   
/*    */   public static class PacketVillagerItemThoughtHandler
/*    */     implements IMessageHandler<PacketVillagerItemThought, IMessage>
/*    */   {
/*    */     public IMessage onMessage(PacketVillagerItemThought message, MessageContext ctx) {
/* 50 */       Minecraft.getMinecraft().addScheduledTask(() -> {
/*    */             Entity ent = message.getEntity();
/*    */             
/*    */             if (ent != null) {
/*    */               ModEntities.handleItemThought(ent.world, ent, message.getItem());
/*    */             }
/*    */           });
/*    */       
/* 58 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\network\PacketVillagerItemThought.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */