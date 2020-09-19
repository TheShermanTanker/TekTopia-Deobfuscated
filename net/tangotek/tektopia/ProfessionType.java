/*    */ package net.tangotek.tektopia;
/*    */ 
/*    */ public enum ProfessionType
/*    */ {
/*  5 */   BARD("bard", true),
/*  6 */   BLACKSMITH("blacksmith", true),
/*  7 */   BUTCHER("butcher", true),
/*  8 */   CHEF("chef", true),
/*  9 */   CLERIC("cleric", true),
/* 10 */   DRUID("druid", true),
/* 11 */   ENCHANTER("enchanter", true),
/* 12 */   FARMER("farmer", true),
/* 13 */   GUARD("guard", true),
/* 14 */   CAPTAIN("captain", false),
/* 15 */   LUMBERJACK("lumberjack", true),
/* 16 */   MINER("miner", true),
/* 17 */   RANCHER("rancher", true),
/* 18 */   TEACHER("teacher", true),
/* 19 */   CHILD("child", false),
/* 20 */   NITWIT("nitwit", false),
/* 21 */   NOMAD("nomad", false);
/*    */   
/*    */   public final String name;
/*    */   public final boolean canCopy;
/*    */   
/*    */   ProfessionType(String name, boolean copy) {
/* 27 */     this.name = name;
/* 28 */     this.canCopy = copy;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\ProfessionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */