/*    */ package net.tangotek.tektopia.proxy;
/*    */ 
/*    */ import com.leviathanstudio.craftstudio.common.network.ClientIAnimatedEventMessage;
/*    */ import com.leviathanstudio.craftstudio.common.network.IAnimatedEventMessage;
/*    */ import com.leviathanstudio.craftstudio.common.network.ServerIAnimatedEventMessage;
/*    */ import net.minecraftforge.fml.common.network.NetworkRegistry;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TekNetworkHelper
/*    */ {
/*    */   public static final double EVENT_RANGE = 128.0D;
/*    */   
/*    */   public static void sendIAnimatedEvent(IAnimatedEventMessage message) {
/* 26 */     if (message.animated.isWorldRemote()) {
/*    */       
/* 28 */       TekVillager.NETWORK.sendToServer((IMessage)new ServerIAnimatedEventMessage(message));
/*    */     }
/*    */     else {
/*    */       
/* 32 */       TekVillager.NETWORK.sendToAllAround((IMessage)new ClientIAnimatedEventMessage(message), new NetworkRegistry.TargetPoint(message.animated.getDimension(), message.animated
/* 33 */             .getX(), message.animated.getY(), message.animated.getZ(), 128.0D));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\proxy\TekNetworkHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */