package com.websina.license;

import java.security.GeneralSecurityException;

public abstract class LicenseManager {
  public abstract boolean isValid() throws GeneralSecurityException;
  
  public abstract int daysLeft();
  
  public abstract String getFeature(String paramString);
  
  public abstract String getLicense();
}


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\com\websina\license\LicenseManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */