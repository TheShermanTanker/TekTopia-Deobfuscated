/*    */ package com.websina.license;
/*    */ 
/*    */ import com.websina.util.ByteHex;
/*    */ import com.websina.util.security.KeyUtil;
/*    */ import java.security.GeneralSecurityException;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.PublicKey;
/*    */ import java.security.Signature;
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
/*    */ public class SignatureUtil
/*    */ {
/*    */   private static final String algorithm = "DSA";
/*    */   
/*    */   public static String sign(String data, byte[] encodedPrivateKey) throws GeneralSecurityException {
/* 36 */     Signature sig = Signature.getInstance("DSA");
/* 37 */     PrivateKey key = KeyUtil.getPrivate(encodedPrivateKey);
/* 38 */     sig.initSign(key);
/* 39 */     sig.update(data.getBytes());
/* 40 */     byte[] result = sig.sign();
/* 41 */     return ByteHex.convert(result);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean verify(String data, byte[] signature, byte[] encodedPublicKey) throws GeneralSecurityException {
/* 53 */     Signature sig = Signature.getInstance("DSA");
/* 54 */     PublicKey key = KeyUtil.getPublic(encodedPublicKey);
/* 55 */     sig.initVerify(key);
/* 56 */     sig.update(data.getBytes());
/* 57 */     return sig.verify(signature);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\com\websina\license\SignatureUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */