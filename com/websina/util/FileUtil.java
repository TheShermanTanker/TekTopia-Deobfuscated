/*     */ package com.websina.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import net.minecraft.client.Minecraft;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileUtil
/*     */ {
/*     */   private InputStream is;
/*     */   private String rawData;
/*     */   
/*     */   public FileUtil(String filename) throws FileNotFoundException {
/*  32 */     File inFile = new File((Minecraft.getMinecraft()).gameDir, filename);
/*  33 */     this.is = new FileInputStream(inFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileUtil(InputStream is) {
/*  43 */     this.is = is;
/*     */   }
/*     */   
/*     */   public String getRawData() {
/*  47 */     return this.rawData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void read(List<String> names, Properties prop) throws IOException {
/*  60 */     if (this.is == null) {
/*  61 */       throw new IOException("There is nothing to read from ...");
/*     */     }
/*  63 */     BufferedReader reader = new BufferedReader(new InputStreamReader(this.is));
/*     */ 
/*     */ 
/*     */     
/*  67 */     StringBuilder builder = new StringBuilder();
/*     */     String line;
/*  69 */     while ((line = reader.readLine()) != null) {
/*  70 */       String name, value; if (line.trim().indexOf('#') == 0 || line.trim().indexOf('!') == 0) {
/*     */         continue;
/*     */       }
/*  73 */       builder.append(line + System.getProperty("line.separator"));
/*  74 */       int index = line.indexOf('=');
/*  75 */       if (index > 0) {
/*  76 */         name = line.substring(0, index).trim();
/*  77 */         value = line.substring(++index).trim();
/*     */       } else {
/*  79 */         name = line.trim();
/*  80 */         value = "";
/*     */       } 
/*  82 */       names.add(name);
/*  83 */       prop.setProperty(name, loadConvert(value));
/*  84 */       this.rawData = builder.toString();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String loadConvert(String theString) {
/*  96 */     int len = theString.length();
/*  97 */     StringBuffer outBuffer = new StringBuffer(len);
/*     */     
/*  99 */     for (int x = 0; x < len; ) {
/* 100 */       char aChar = theString.charAt(x++);
/* 101 */       if (aChar == '\\') {
/* 102 */         aChar = theString.charAt(x++);
/* 103 */         if (aChar == 'u') {
/*     */           
/* 105 */           int value = 0;
/* 106 */           for (int i = 0; i < 4; i++) {
/* 107 */             aChar = theString.charAt(x++);
/* 108 */             switch (aChar) { case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7':
/*     */               case '8':
/*     */               case '9':
/* 111 */                 value = (value << 4) + aChar - 48; break;
/*     */               case 'a': case 'b': case 'c': case 'd':
/*     */               case 'e':
/*     */               case 'f':
/* 115 */                 value = (value << 4) + 10 + aChar - 97; break;
/*     */               case 'A': case 'B': case 'C': case 'D':
/*     */               case 'E':
/*     */               case 'F':
/* 119 */                 value = (value << 4) + 10 + aChar - 65;
/*     */                 break;
/*     */               default:
/* 122 */                 throw new IllegalArgumentException("Malformed \\uxxxx encoding."); }
/*     */           
/*     */           } 
/* 125 */           outBuffer.append((char)value); continue;
/*     */         } 
/* 127 */         if (aChar == 't') { aChar = '\t'; }
/* 128 */         else if (aChar == 'r') { aChar = '\r'; }
/* 129 */         else if (aChar == 'n') { aChar = '\n'; }
/* 130 */         else if (aChar == 'f') { aChar = '\f'; }
/* 131 */          outBuffer.append(aChar);
/*     */         continue;
/*     */       } 
/* 134 */       outBuffer.append(aChar);
/*     */     } 
/*     */     
/* 137 */     return outBuffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\com\websin\\util\FileUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */