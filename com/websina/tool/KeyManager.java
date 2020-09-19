/*    */ package com.websina.tool;
/*    */ 
/*    */ import com.websina.util.ByteHex;
/*    */ import com.websina.util.security.KeyUtil;
/*    */ import java.io.FileWriter;
/*    */ import java.io.IOException;
/*    */ import java.security.KeyPair;
/*    */ import java.util.Date;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KeyManager
/*    */ {
/*    */   public static void create() {
/* 34 */     KeyPair kp = KeyUtil.getKeyPair();
/* 35 */     byte[] pub = kp.getPublic().getEncoded();
/* 36 */     byte[] priv = kp.getPrivate().getEncoded();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 43 */     StringBuffer buf = (new StringBuffer("######## Key pair created on:")).append(new Date()).append("\npublic-key=").append(ByteHex.convert(pub)).append("\nprivate-key=").append(ByteHex.convert(priv)).append("\n\n");
/*    */     try {
/* 45 */       FileWriter out = new FileWriter("dsakey.cfg", true);
/* 46 */       out.write(buf.toString());
/* 47 */       out.flush();
/* 48 */       out.close();
/* 49 */     } catch (IOException e) {
/* 50 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 55 */     create();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\com\websina\tool\KeyManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */