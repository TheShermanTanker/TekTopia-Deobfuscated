/*    */ package net.tangotek.tektopia;
/*    */ 
/*    */ public enum EntityTagType {
/*  4 */   VILLAGER("villager", "Â§2"),
/*  5 */   BUTCHERED("butchered", "Â§1");
/*    */   
/*    */   public final String tag;
/*    */   public final String colorPrefx;
/*    */   
/*    */   EntityTagType(String tag, String colorPrefix) {
/* 11 */     this.tag = tag;
/* 12 */     this.colorPrefx = colorPrefix;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\EntityTagType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */