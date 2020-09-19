/*    */ package net.tangotek.tektopia;
/*    */ 
/*    */ public enum VillagerRole {
/*  4 */   VILLAGER(1),
/*  5 */   DEFENDER(2),
/*  6 */   VENDOR(4),
/*  7 */   ENEMY(8),
/*  8 */   VISITOR(16);
/*    */   
/*    */   public final int value;
/*    */   
/*    */   VillagerRole(int v) {
/* 13 */     this.value = v;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\VillagerRole.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */