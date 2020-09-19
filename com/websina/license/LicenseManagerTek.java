/*     */ package com.websina.license;
/*     */ 
/*     */ import com.websina.util.ByteHex;
/*     */ import com.websina.util.DateParser;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.util.Date;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LicenseManagerTek
/*     */   extends LicenseManager
/*     */ {
/*     */   private static final String PUBLIC_KEY = "308201B73082012C06072A8648CE3804013082011F02818100FD7F53811D75122952DF4A9C2EECE4E7F611B7523CEF4400C31E3F80B6512669455D402251FB593D8D58FABFC5F5BA30F6CB9B556CD7813B801D346FF26660B76B9950A5A49F9FE8047B1022C24FBBA9D7FEB7C61BF83B57E7C6A8A6150F04FB83F6D3C51EC3023554135A169132F675F3AE2B61D72AEFF22203199DD14801C70215009760508F15230BCCB292B982A2EB840BF0581CF502818100F7E1A085D69B3DDECBBCAB5C36B857B97994AFBBFA3AEA82F9574C0B3D0782675159578EBAD4594FE67107108180B449167123E84C281613B7CF09328CC8A6E13C167A8B547C8D28E0A3AE1E2BB3A675916EA37F0BFA213562F1FB627A01243BCCA4F1BEA8519089A883DFE15AE59F06928B665E807B552564014C3BFECF492A0381840002818032BBD1DCB3628DF73FAEA5CDFC0851753D4D6D9CFE7D444F46E7F3346B9BBBA3202F8830A46A2F7FC35DB8F6AB6CE273DEBA8914221A10CE0D15AA142D00C4FC3FD78BDA441DB6C51D4A67AA28EB0E571DC91CAE64B21606A4142EEEC7F433509358E60F52205EBAE5E8029F75AF89D619995572D3E6CF65D5D9AA809208AFA9";
/*     */   private static final int MS_A_DAY = 86400000;
/*     */   private byte[] key;
/*     */   private License lic;
/*     */   
/*     */   public LicenseManagerTek(String licData) {
/*     */     try {
/*  39 */       this.lic = License.loadLicense(licData);
/*  40 */     } catch (LicenseNotFoundException e) {
/*  41 */       throw new RuntimeException(e.getMessage());
/*     */     } 
/*     */     try {
/*  44 */       this.key = ByteHex.convert("308201B73082012C06072A8648CE3804013082011F02818100FD7F53811D75122952DF4A9C2EECE4E7F611B7523CEF4400C31E3F80B6512669455D402251FB593D8D58FABFC5F5BA30F6CB9B556CD7813B801D346FF26660B76B9950A5A49F9FE8047B1022C24FBBA9D7FEB7C61BF83B57E7C6A8A6150F04FB83F6D3C51EC3023554135A169132F675F3AE2B61D72AEFF22203199DD14801C70215009760508F15230BCCB292B982A2EB840BF0581CF502818100F7E1A085D69B3DDECBBCAB5C36B857B97994AFBBFA3AEA82F9574C0B3D0782675159578EBAD4594FE67107108180B449167123E84C281613B7CF09328CC8A6E13C167A8B547C8D28E0A3AE1E2BB3A675916EA37F0BFA213562F1FB627A01243BCCA4F1BEA8519089A883DFE15AE59F06928B665E807B552564014C3BFECF492A0381840002818032BBD1DCB3628DF73FAEA5CDFC0851753D4D6D9CFE7D444F46E7F3346B9BBBA3202F8830A46A2F7FC35DB8F6AB6CE273DEBA8914221A10CE0D15AA142D00C4FC3FD78BDA441DB6C51D4A67AA28EB0E571DC91CAE64B21606A4142EEEC7F433509358E60F52205EBAE5E8029F75AF89D619995572D3E6CF65D5D9AA809208AFA9");
/*  45 */     } catch (Exception e) {
/*  46 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public LicenseManagerTek() {
/*     */     try {
/*  52 */       this.lic = License.loadLicense();
/*  53 */     } catch (LicenseNotFoundException e) {
/*  54 */       throw new RuntimeException(e.getMessage());
/*     */     } 
/*     */     try {
/*  57 */       this.key = ByteHex.convert("308201B73082012C06072A8648CE3804013082011F02818100FD7F53811D75122952DF4A9C2EECE4E7F611B7523CEF4400C31E3F80B6512669455D402251FB593D8D58FABFC5F5BA30F6CB9B556CD7813B801D346FF26660B76B9950A5A49F9FE8047B1022C24FBBA9D7FEB7C61BF83B57E7C6A8A6150F04FB83F6D3C51EC3023554135A169132F675F3AE2B61D72AEFF22203199DD14801C70215009760508F15230BCCB292B982A2EB840BF0581CF502818100F7E1A085D69B3DDECBBCAB5C36B857B97994AFBBFA3AEA82F9574C0B3D0782675159578EBAD4594FE67107108180B449167123E84C281613B7CF09328CC8A6E13C167A8B547C8D28E0A3AE1E2BB3A675916EA37F0BFA213562F1FB627A01243BCCA4F1BEA8519089A883DFE15AE59F06928B665E807B552564014C3BFECF492A0381840002818032BBD1DCB3628DF73FAEA5CDFC0851753D4D6D9CFE7D444F46E7F3346B9BBBA3202F8830A46A2F7FC35DB8F6AB6CE273DEBA8914221A10CE0D15AA142D00C4FC3FD78BDA441DB6C51D4A67AA28EB0E571DC91CAE64B21606A4142EEEC7F433509358E60F52205EBAE5E8029F75AF89D619995572D3E6CF65D5D9AA809208AFA9");
/*  58 */     } catch (Exception e) {
/*  59 */       e.printStackTrace();
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
/*     */   public boolean isValid() throws GeneralSecurityException {
/*  71 */     return isLicenceValid(this.lic);
/*     */   }
/*     */   
/*     */   public boolean isLicenceValid(License lic) throws GeneralSecurityException {
/*  75 */     String signature = lic.getSignature();
/*  76 */     if (signature == null || signature.trim().length() == 0)
/*  77 */       return false; 
/*  78 */     boolean valid = SignatureUtil.verify(lic.format(), ByteHex.convert(signature), this.key);
/*  79 */     if (!valid)
/*  80 */       return false; 
/*  81 */     if (daysLeft() < 0) {
/*  82 */       return false;
/*     */     }
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int daysLeft() {
/*  94 */     String expiration = this.lic.getExpiration();
/*  95 */     if (expiration == null)
/*  96 */       return -1; 
/*  97 */     if (expiration.trim().length() == 0 || expiration
/*  98 */       .indexOf("never") != -1) {
/*  99 */       return 0;
/*     */     }
/* 101 */     Date licDate = DateParser.toUtilDate(expiration);
/* 102 */     long time = licDate.getTime() - System.currentTimeMillis();
/* 103 */     int days = 1 + (int)(time / 86400000L);
/* 104 */     return days;
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
/*     */ 
/*     */   
/*     */   public String getFeature(String name) {
/* 119 */     return this.lic.getFeature(name);
/*     */   }
/*     */   
/*     */   public String getLicense() {
/* 123 */     return this.lic.getRawData();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\com\websina\license\LicenseManagerTek.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */