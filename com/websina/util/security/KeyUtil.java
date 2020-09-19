/*    */ package com.websina.util.security;
/*    */ 
/*    */ import java.security.KeyFactory;
/*    */ import java.security.KeyPair;
/*    */ import java.security.KeyPairGenerator;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.PublicKey;
/*    */ import java.security.SecureRandom;
/*    */ import java.security.spec.InvalidKeySpecException;
/*    */ import java.security.spec.PKCS8EncodedKeySpec;
/*    */ import java.security.spec.X509EncodedKeySpec;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KeyUtil
/*    */ {
/*    */   private static KeyPairGenerator kpg;
/*    */   private static KeyFactory kf;
/*    */   
/*    */   static {
/*    */     try {
/* 28 */       kpg = KeyPairGenerator.getInstance("DSA");
/* 29 */       SecureRandom sr = new SecureRandom();
/* 30 */       kpg.initialize(1024, new SecureRandom(sr.generateSeed(8)));
/* 31 */       kf = KeyFactory.getInstance("DSA");
/* 32 */     } catch (NoSuchAlgorithmException e) {
/* 33 */       throw new RuntimeException(e.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static PublicKey getPublic(byte[] encodedKey) throws InvalidKeySpecException {
/* 44 */     return kf.generatePublic(new X509EncodedKeySpec(encodedKey));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static PrivateKey getPrivate(byte[] encodedKey) throws InvalidKeySpecException {
/* 54 */     return kf.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static KeyPair getKeyPair() {
/* 62 */     return kpg.genKeyPair();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\com\websin\\util\security\KeyUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */