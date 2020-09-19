/*    */ package net.tangotek.tektopia.proxy;
/*    */ 
/*    */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*    */ import com.leviathanstudio.craftstudio.common.animation.IAnimated;
/*    */ import com.leviathanstudio.craftstudio.common.network.ClientIAnimatedEventMessage;
/*    */ import com.leviathanstudio.craftstudio.common.network.EnumIAnimatedEvent;
/*    */ import com.leviathanstudio.craftstudio.common.network.IAnimatedEventMessage;
/*    */ import com.leviathanstudio.craftstudio.common.network.ServerIAnimatedEventMessage;
/*    */ import com.leviathanstudio.craftstudio.server.animation.ServerAnimationHandler;
/*    */ import java.util.Iterator;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*    */ 
/*    */ public class TekServerAnimationHandler<T extends IAnimated> extends ServerAnimationHandler<T> {
/*    */   public void networkStartAnimation(String res, float startingFrame, T animatedElement, boolean clientSend) {
/* 22 */     if (animatedElement.isWorldRemote() == clientSend) {
/* 23 */       serverInitAnimation(res, startingFrame, (IAnimated)animatedElement);
/* 24 */       TekNetworkHelper.sendIAnimatedEvent(new IAnimatedEventMessage(EnumIAnimatedEvent.START_ANIM, (IAnimated)animatedElement, getAnimIdFromName(res), startingFrame));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void networkStopAnimation(String res, T animatedElement, boolean clientSend) {
/* 30 */     if (animatedElement.isWorldRemote() == clientSend) {
/* 31 */       serverStopAnimation(res, (IAnimated)animatedElement);
/* 32 */       TekNetworkHelper.sendIAnimatedEvent(new IAnimatedEventMessage(EnumIAnimatedEvent.STOP_ANIM, (IAnimated)animatedElement, getAnimIdFromName(res)));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void networkStopStartAnimation(String animToStop, String animToStart, float startingFrame, T animatedElement, boolean clientSend) {
/* 38 */     if (animatedElement.isWorldRemote() == clientSend) {
/* 39 */       serverStopStartAnimation(animToStop, animToStart, startingFrame, (IAnimated)animatedElement);
/* 40 */       TekNetworkHelper.sendIAnimatedEvent(new IAnimatedEventMessage(EnumIAnimatedEvent.STOP_START_ANIM, (IAnimated)animatedElement, getAnimIdFromName(animToStart), startingFrame, getAnimIdFromName(animToStop)));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static class ServerIAnimatedEventHandler
/*    */     extends IAnimatedEventMessage.IAnimatedEventHandler
/*    */     implements IMessageHandler<ServerIAnimatedEventMessage, ClientIAnimatedEventMessage>
/*    */   {
/*    */     public ClientIAnimatedEventMessage onMessage(ServerIAnimatedEventMessage message, MessageContext ctx) {
/* 50 */       EntityPlayerMP serverPlayer = (ctx.getServerHandler()).player;
/*    */ 
/*    */       
/* 53 */       serverPlayer.getServerWorld().addScheduledTask(() -> {
/*    */             if (onMessage((IAnimatedEventMessage)message, ctx)) {
/*    */               message.animated.getAnimationHandler();
/*    */               
/*    */               boolean success = AnimationHandler.onServerIAnimatedEvent((IAnimatedEventMessage)message);
/*    */               if (success && message.event != EnumIAnimatedEvent.ANSWER_START_ANIM.getId()) {
/*    */                 TekNetworkHelper.sendIAnimatedEvent((IAnimatedEventMessage)new ClientIAnimatedEventMessage((IAnimatedEventMessage)message));
/*    */               }
/*    */             } 
/*    */           });
/* 63 */       return null;
/*    */     }
/*    */     public Entity getEntityByUUID(MessageContext ctx, long most, long least) {
/*    */       Entity e;
/* 67 */       UUID uuid = new UUID(most, least);
/* 68 */       Iterator<Entity> var7 = (ctx.getServerHandler()).player.world.loadedEntityList.iterator();
/*    */ 
/*    */       
/*    */       do {
/* 72 */         if (!var7.hasNext()) {
/* 73 */           return null;
/*    */         }
/*    */         
/* 76 */         e = var7.next();
/* 77 */       } while (!e.getPersistentID().equals(uuid));
/*    */       
/* 79 */       return e;
/*    */     }
/*    */     
/*    */     public TileEntity getTileEntityByPos(MessageContext ctx, int x, int y, int z) {
/* 83 */       BlockPos pos = new BlockPos(x, y, z);
/* 84 */       return (ctx.getServerHandler()).player.world.getTileEntity(pos);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\proxy\TekServerAnimationHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */