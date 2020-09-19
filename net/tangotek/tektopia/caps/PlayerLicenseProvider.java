/*    */ package net.tangotek.tektopia.caps;
/*    */ 
/*    */ import net.minecraft.nbt.NBTBase;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraftforge.common.capabilities.Capability;
/*    */ import net.minecraftforge.common.capabilities.CapabilityInject;
/*    */ import net.minecraftforge.common.capabilities.ICapabilitySerializable;
/*    */ 
/*    */ public class PlayerLicenseProvider
/*    */   implements ICapabilitySerializable<NBTBase> {
/*    */   @CapabilityInject(IPlayerLicense.class)
/* 12 */   public static final Capability<IPlayerLicense> PLAYER_LICENSE_CAPABILITY = null;
/*    */   
/* 14 */   private IPlayerLicense instance = (IPlayerLicense)PLAYER_LICENSE_CAPABILITY.getDefaultInstance();
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
/* 19 */     return (capability == PLAYER_LICENSE_CAPABILITY);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
/* 25 */     if (capability == PLAYER_LICENSE_CAPABILITY) {
/* 26 */       return (T)PLAYER_LICENSE_CAPABILITY.cast(this.instance);
/*    */     }
/* 28 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public NBTBase serializeNBT() {
/* 34 */     return PLAYER_LICENSE_CAPABILITY.getStorage().writeNBT(PLAYER_LICENSE_CAPABILITY, this.instance, null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void deserializeNBT(NBTBase nbt) {
/* 40 */     PLAYER_LICENSE_CAPABILITY.getStorage().readNBT(PLAYER_LICENSE_CAPABILITY, this.instance, null, nbt);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\caps\PlayerLicenseProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */