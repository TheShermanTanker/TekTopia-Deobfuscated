/*    */ package com.websina.util;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
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
/*    */ public final class ByteHex
/*    */ {
/* 21 */   private static final char[] hexs = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
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
/*    */   public static String convert(byte[] bytes) {
/* 34 */     StringBuffer sb = new StringBuffer(bytes.length * 2);
/* 35 */     for (int i = 0; i < bytes.length; i++) {
/* 36 */       sb.append(hexs[bytes[i] >> 4 & 0xF]);
/* 37 */       sb.append(hexs[bytes[i] & 0xF]);
/*    */     } 
/* 39 */     return sb.toString();
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
/*    */   public static byte[] convert(String hex) {
/* 51 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 52 */     for (int i = 0; i < hex.length(); i += 2) {
/* 53 */       char c1 = hex.charAt(i);
/* 54 */       if (i + 1 >= hex.length())
/* 55 */         throw new IllegalArgumentException(); 
/* 56 */       char c2 = hex.charAt(i + 1);
/* 57 */       byte b = 0;
/* 58 */       if (c1 >= '0' && c1 <= '9') {
/* 59 */         b = (byte)(b + (c1 - 48) * 16);
/* 60 */       } else if (c1 >= 'a' && c1 <= 'f') {
/* 61 */         b = (byte)(b + (c1 - 97 + 10) * 16);
/* 62 */       } else if (c1 >= 'A' && c1 <= 'F') {
/* 63 */         b = (byte)(b + (c1 - 65 + 10) * 16);
/*    */       } else {
/* 65 */         throw new IllegalArgumentException();
/* 66 */       }  if (c2 >= '0' && c2 <= '9') {
/* 67 */         b = (byte)(b + c2 - 48);
/* 68 */       } else if (c2 >= 'a' && c2 <= 'f') {
/* 69 */         b = (byte)(b + c2 - 97 + 10);
/* 70 */       } else if (c2 >= 'A' && c2 <= 'F') {
/* 71 */         b = (byte)(b + c2 - 65 + 10);
/*    */       } else {
/* 73 */         throw new IllegalArgumentException();
/* 74 */       }  baos.write(b);
/*    */     } 
/* 76 */     return baos.toByteArray();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\com\websin\\util\ByteHex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */