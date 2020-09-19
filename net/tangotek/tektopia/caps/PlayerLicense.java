/*    */ package net.tangotek.tektopia.caps;
/*    */ 
/*    */ import com.websina.license.LicenseManager;
/*    */ import com.websina.license.LicenseManagerTek;
/*    */ import java.security.GeneralSecurityException;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.nbt.NBTBase;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraftforge.common.capabilities.Capability;
/*    */ import net.tangotek.tektopia.LicenseTracker;
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
/*    */ public class PlayerLicense
/*    */   implements IPlayerLicense, Capability.IStorage<IPlayerLicense>
/*    */ {
/*    */   private LicenseManager licenseManager;
/*    */   
/*    */   public String getLicenseData() {
/* 38 */     if (this.licenseManager != null) {
/* 39 */       return this.licenseManager.getLicense();
/*    */     }
/*    */     
/* 42 */     return null;
/*    */   }
/*    */   
/*    */   public void setLicenseData(String licData) {
/* 46 */     this.licenseManager = (LicenseManager)new LicenseManagerTek(licData);
/*    */     try {
/* 48 */       if (!this.licenseManager.isValid())
/*    */       {
/* 50 */         this.licenseManager = null;
/*    */       }
/* 52 */     } catch (GeneralSecurityException e) {
/* 53 */       System.err.println("GeneralSecurityException validating license key.");
/* 54 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasFeature(LicenseTracker.Feature feature) {
/* 60 */     if (this.licenseManager != null) {
/* 61 */       return (this.licenseManager.getFeature(feature.getName()) != null);
/*    */     }
/*    */     
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValid(String name) {
/* 69 */     if (this.licenseManager != null) {
/* 70 */       if (name.toLowerCase().startsWith("player")) {
/* 71 */         return true;
/*    */       }
/* 73 */       return this.licenseManager.getFeature("IGN").toLowerCase().equals(name.toLowerCase());
/*    */     } 
/*    */     
/* 76 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public NBTBase writeNBT(Capability<IPlayerLicense> capability, IPlayerLicense instance, EnumFacing side) {
/* 82 */     NBTTagCompound compound = new NBTTagCompound();
/* 83 */     return (NBTBase)compound;
/*    */   }
/*    */   
/*    */   public void readNBT(Capability<IPlayerLicense> capability, IPlayerLicense instance, EnumFacing side, NBTBase nbt) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\caps\PlayerLicense.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */