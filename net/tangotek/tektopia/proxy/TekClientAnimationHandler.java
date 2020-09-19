/*     */ package net.tangotek.tektopia.proxy;
/*     */ 
/*     */ import com.leviathanstudio.craftstudio.client.animation.ClientAnimationHandler;
/*     */ import com.leviathanstudio.craftstudio.common.animation.IAnimated;
/*     */ import com.leviathanstudio.craftstudio.common.animation.InfoChannel;
/*     */ import com.leviathanstudio.craftstudio.common.network.ClientIAnimatedEventMessage;
/*     */ import com.leviathanstudio.craftstudio.common.network.EnumIAnimatedEvent;
/*     */ import com.leviathanstudio.craftstudio.common.network.IAnimatedEventMessage;
/*     */ import com.leviathanstudio.craftstudio.common.network.ServerIAnimatedEventMessage;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/*     */ 
/*     */ 
/*     */ public class TekClientAnimationHandler<T extends IAnimated>
/*     */   extends ClientAnimationHandler<T>
/*     */ {
/*     */   public void networkStartAnimation(String res, float startingFrame, T animatedElement, boolean clientSend) {
/*  27 */     if (animatedElement.isWorldRemote() == clientSend) {
/*  28 */       serverInitAnimation(res, startingFrame, (IAnimated)animatedElement);
/*  29 */       TekNetworkHelper.sendIAnimatedEvent(new IAnimatedEventMessage(EnumIAnimatedEvent.START_ANIM, (IAnimated)animatedElement, getAnimIdFromName(res), startingFrame));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void networkStopAnimation(String res, T animatedElement, boolean clientSend) {
/*  35 */     if (animatedElement.isWorldRemote() == clientSend) {
/*  36 */       serverStopAnimation(res, (IAnimated)animatedElement);
/*  37 */       TekNetworkHelper.sendIAnimatedEvent(new IAnimatedEventMessage(EnumIAnimatedEvent.STOP_ANIM, (IAnimated)animatedElement, getAnimIdFromName(res)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void networkStopStartAnimation(String animToStop, String animToStart, float startingFrame, T animatedElement, boolean clientSend) {
/*  43 */     if (animatedElement.isWorldRemote() == clientSend) {
/*  44 */       serverStopStartAnimation(animToStop, animToStart, startingFrame, (IAnimated)animatedElement);
/*  45 */       TekNetworkHelper.sendIAnimatedEvent(new IAnimatedEventMessage(EnumIAnimatedEvent.STOP_START_ANIM, (IAnimated)animatedElement, getAnimIdFromName(animToStart), startingFrame, getAnimIdFromName(animToStop)));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class ClientIAnimatedEventHandler extends IAnimatedEventMessage.IAnimatedEventHandler implements IMessageHandler<ClientIAnimatedEventMessage, ServerIAnimatedEventMessage> {
/*  50 */     Map<UUID, Short> curAnimMap = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ServerIAnimatedEventMessage onMessage(ClientIAnimatedEventMessage message, MessageContext ctx) {
/*  56 */       Minecraft.getMinecraft().addScheduledTask(() -> {
/*     */             if (onMessage((IAnimatedEventMessage)message, ctx)) {
/*     */               UUID uuid = new UUID(message.most, message.least);
/*     */               
/*     */               boolean success = message.animated.getAnimationHandler().onClientIAnimatedEvent((IAnimatedEventMessage)message);
/*     */               
/*     */               if (!success || !(message.animated.getAnimationHandler() instanceof ClientAnimationHandler) || (message.event != EnumIAnimatedEvent.START_ANIM.getId() && message.event != EnumIAnimatedEvent.STOP_START_ANIM.getId())) {
/*     */                 if (message.event == EnumIAnimatedEvent.STOP_ANIM.getId()) {
/*     */                   Short short_ = this.curAnimMap.get(uuid);
/*     */                   
/*     */                   if (short_ != null && short_.shortValue() == message.animId) {
/*     */                     this.curAnimMap.remove(uuid);
/*     */                   }
/*     */                 } 
/*     */                 
/*     */                 return;
/*     */               } 
/*     */               
/*     */               ClientAnimationHandler hand = (ClientAnimationHandler)message.animated.getAnimationHandler();
/*     */               
/*     */               String animName = hand.getAnimNameFromId(message.animId);
/*     */               
/*     */               InfoChannel infoC = (InfoChannel)hand.getAnimChannels().get(animName);
/*     */               TekNetworkHelper.sendIAnimatedEvent((IAnimatedEventMessage)new ServerIAnimatedEventMessage(EnumIAnimatedEvent.ANSWER_START_ANIM, message.animated, message.animId, infoC.totalFrames));
/*     */               Short lastAnimId = this.curAnimMap.put(uuid, Short.valueOf(message.animId));
/*     */               if (lastAnimId != null && lastAnimId.shortValue() != message.animId) {
/*     */                 String curAnimName = hand.getAnimNameFromId(lastAnimId.shortValue());
/*     */                 hand.clientStopAnimation(curAnimName, message.animated);
/*     */               } 
/*     */             } 
/*     */           });
/*  87 */       return null;
/*     */     }
/*     */     public Entity getEntityByUUID(MessageContext ctx, long most, long least) {
/*     */       Entity e;
/*  91 */       UUID uuid = new UUID(most, least);
/*  92 */       Iterator<Entity> var7 = (Minecraft.getMinecraft()).world.loadedEntityList.iterator();
/*     */ 
/*     */       
/*     */       do {
/*  96 */         if (!var7.hasNext()) {
/*  97 */           return null;
/*     */         }
/*     */         
/* 100 */         e = var7.next();
/* 101 */       } while (!e.getPersistentID().equals(uuid));
/*     */       
/* 103 */       return e;
/*     */     }
/*     */     
/*     */     public TileEntity getTileEntityByPos(MessageContext ctx, int x, int y, int z) {
/* 107 */       BlockPos pos = new BlockPos(x, y, z);
/* 108 */       return (Minecraft.getMinecraft()).world.getTileEntity(pos);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\proxy\TekClientAnimationHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */