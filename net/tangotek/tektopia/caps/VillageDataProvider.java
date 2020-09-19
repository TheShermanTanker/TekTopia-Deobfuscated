/*    */ package net.tangotek.tektopia.caps;
/*    */ 
/*    */ import net.minecraft.nbt.NBTBase;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraftforge.common.capabilities.Capability;
/*    */ import net.minecraftforge.common.capabilities.CapabilityInject;
/*    */ import net.minecraftforge.common.capabilities.ICapabilitySerializable;
/*    */ 
/*    */ public class VillageDataProvider
/*    */   implements ICapabilitySerializable<NBTBase> {
/*    */   @CapabilityInject(IVillageData.class)
/* 12 */   public static final Capability<IVillageData> VILLAGE_DATA_CAPABILITY = null;
/*    */   
/* 14 */   private IVillageData instance = (IVillageData)VILLAGE_DATA_CAPABILITY.getDefaultInstance();
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
/* 19 */     return (capability == VILLAGE_DATA_CAPABILITY);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
/* 25 */     return (capability == VILLAGE_DATA_CAPABILITY) ? (T)VILLAGE_DATA_CAPABILITY.cast(this.instance) : null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public NBTBase serializeNBT() {
/* 31 */     return VILLAGE_DATA_CAPABILITY.getStorage().writeNBT(VILLAGE_DATA_CAPABILITY, this.instance, null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void deserializeNBT(NBTBase nbt) {
/* 37 */     VILLAGE_DATA_CAPABILITY.getStorage().readNBT(VILLAGE_DATA_CAPABILITY, this.instance, null, nbt);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\caps\VillageDataProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */