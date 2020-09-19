/*    */ package com.websina.util;
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
/*    */ public class UnicodeEncoder
/*    */ {
/* 21 */   private static StringBuffer charAccumulator = new StringBuffer(2); private static final char MIN_HIGH_SURROGATE = '?'; private static final char MAX_HIGH_SURROGATE = '?';
/*    */   
/*    */   public static String encode(String s) {
/* 24 */     StringBuffer sb = new StringBuffer();
/* 25 */     char[] chars = s.toCharArray();
/* 26 */     for (int i = 0; i < chars.length; i++) {
/* 27 */       if (chars[i] <= '') {
/* 28 */         sb.append(chars[i]);
/*    */       } else {
/*    */         
/* 31 */         int codePoint = Integer.MIN_VALUE;
/* 32 */         if (isLowSurrogate(chars[i]) || isHighSurrogate(chars[i])) {
/* 33 */           charAccumulator.append(chars[i]);
/*    */         } else {
/* 35 */           codePoint = chars[i];
/*    */         } 
/* 37 */         if (charAccumulator.length() == 2) {
/* 38 */           codePoint = toCodePoint(charAccumulator.charAt(0), charAccumulator.charAt(1));
/* 39 */           charAccumulator.setLength(0);
/*    */         } 
/* 41 */         if (charAccumulator.length() == 0)
/* 42 */           sb.append(encode(codePoint)); 
/*    */       } 
/*    */     } 
/* 45 */     return sb.toString();
/*    */   }
/*    */   private static final char MIN_LOW_SURROGATE = '?'; private static final char MAX_LOW_SURROGATE = '?'; private static final int MIN_SUPPLEMENTARY_CODE_POINT = 65536;
/*    */   public static String encode(int c) {
/* 49 */     StringBuffer sb = new StringBuffer(10);
/* 50 */     sb.append(Integer.toHexString(c));
/* 51 */     while (sb.length() < 4) {
/* 52 */       sb.insert(0, '0');
/*    */     }
/* 54 */     sb.insert(0, "\\u");
/* 55 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isHighSurrogate(char ch) {
/* 61 */     return (ch >= '?' && ch <= '?');
/*    */   }
/*    */   
/*    */   private static boolean isLowSurrogate(char ch) {
/* 65 */     return (ch >= '?' && ch <= '?');
/*    */   }
/*    */   
/*    */   private static int toCodePoint(char high, char low) {
/* 69 */     return (high - 55296 << 10) + low - 56320 + 65536;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\com\websin\\util\UnicodeEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */