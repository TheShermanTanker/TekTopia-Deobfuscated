/*    */ package com.websina.license;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LicenseNotFoundException
/*    */   extends Exception
/*    */ {
/*    */   private static final String MSG = "License Not Found";
/*    */   
/*    */   public LicenseNotFoundException(String msg) {
/* 24 */     super(msg);
/*    */   }
/*    */   
/*    */   public LicenseNotFoundException() {
/* 28 */     super("License Not Found");
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\com\websina\license\LicenseNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */