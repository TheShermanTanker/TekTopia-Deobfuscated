/*    */ package net.tangotek.tektopia.pathing;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ 
/*    */ 
/*    */ public class PathingGraphListener
/*    */ {
/*    */   private EntityPlayerMP playerMP;
/*    */   
/*    */   public PathingGraphListener(EntityPlayerMP player) {
/* 11 */     this.playerMP = player;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void update(PathingGraph graph) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isPlayer(EntityPlayerMP player) {
/* 22 */     return (player == this.playerMP);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\pathing\PathingGraphListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */