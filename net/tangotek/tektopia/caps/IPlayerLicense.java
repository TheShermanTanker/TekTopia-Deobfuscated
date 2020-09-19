package net.tangotek.tektopia.caps;

import net.tangotek.tektopia.LicenseTracker;

public interface IPlayerLicense {
  String getLicenseData();
  
  void setLicenseData(String paramString);
  
  boolean hasFeature(LicenseTracker.Feature paramFeature);
  
  boolean isValid(String paramString);
}


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\caps\IPlayerLicense.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */