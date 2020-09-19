/*     */ package com.websina.license;
/*     */ 
/*     */ import com.websina.util.FileUtil;
/*     */ import com.websina.util.UnicodeEncoder;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.FileWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class License
/*     */ {
/*     */   public static final String NEVER = "never";
/*     */   private static final String LICENSE_FILE = "tango_patreon.lic";
/*     */   private static final String EXPIRATION = "Expiration";
/*     */   private static final String SIGNATURE = "Signature";
/*     */   private List names;
/*     */   private Properties prop;
/*  36 */   private String data = null;
/*  37 */   private String rawData = null;
/*     */   
/*     */   private License() {
/*  40 */     this.names = new ArrayList();
/*  41 */     this.prop = new Properties();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static License newLicense() {
/*  51 */     return new License();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static License loadLicense() throws LicenseNotFoundException {
/*  60 */     License lic = new License();
/*  61 */     lic.readFile();
/*  62 */     return lic;
/*     */   }
/*     */ 
/*     */   
/*     */   public static License loadLicense(String data) throws LicenseNotFoundException {
/*  67 */     License lic = new License();
/*  68 */     lic.readData(data);
/*  69 */     return lic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void create() throws Exception {
/*  78 */     if (this.data == null || getSignature() == null) {
/*  79 */       throw new Exception("License is not formated ....");
/*     */     }
/*  81 */     FileWriter out = new FileWriter("tango_patreon.lic");
/*  82 */     out.write(this.data);
/*  83 */     out.write(10);
/*  84 */     out.write("Signature");
/*  85 */     out.write(61);
/*  86 */     out.write(getSignature());
/*  87 */     out.flush();
/*  88 */     out.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String format() {
/*  98 */     StringBuffer buf = new StringBuffer();
/*  99 */     Iterator<String> it = this.names.iterator();
/* 100 */     while (it.hasNext()) {
/* 101 */       String key = it.next();
/* 102 */       if (key.length() != 0 && !key.equals("Signature")) {
/* 103 */         String value = UnicodeEncoder.encode(this.prop.getProperty(key));
/* 104 */         buf.append(key).append('=').append(value).append('\n');
/*     */       } 
/*     */     } 
/* 107 */     this.data = buf.toString();
/* 108 */     return this.data;
/*     */   }
/*     */   
/*     */   public String getRawData() {
/* 112 */     return this.rawData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFeature(String name) {
/* 122 */     return this.prop.getProperty(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature(String name, String value) {
/* 131 */     if (!this.names.contains(name)) {
/* 132 */       this.names.add(name);
/*     */     }
/* 134 */     this.prop.setProperty(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExpiration() {
/* 143 */     return this.prop.getProperty("Expiration");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpiration(String date) {
/* 151 */     setFeature("Expiration", date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSignature() {
/* 160 */     return this.prop.getProperty("Signature");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSignature(String signature) {
/* 168 */     setFeature("Signature", signature);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readFile() throws LicenseNotFoundException {
/*     */     try {
/* 176 */       FileUtil fileUtil = new FileUtil("tango_patreon.lic");
/* 177 */       fileUtil.read(this.names, this.prop);
/* 178 */       this.rawData = fileUtil.getRawData();
/* 179 */     } catch (Exception e) {
/* 180 */       throw new LicenseNotFoundException();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readData(String data) throws LicenseNotFoundException {
/*     */     try {
/* 186 */       (new FileUtil(new ByteArrayInputStream(data.getBytes()))).read(this.names, this.prop);
/* 187 */       this.rawData = data;
/* 188 */     } catch (Exception e) {
/* 189 */       throw new LicenseNotFoundException();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\com\websina\license\License.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */