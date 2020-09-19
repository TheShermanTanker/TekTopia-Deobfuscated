package net.tangotek.tektopia.caps;

import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tangotek.tektopia.Village;
import net.tangotek.tektopia.economy.ItemEconomy;

public interface IVillageData {
  UUID getUUID();
  
  boolean isChildReady(long paramLong);
  
  void childSpawned(World paramWorld);
  
  ItemEconomy getEconomy();
  
  void initEconomy();
  
  void setNomadsCheckedToday(boolean paramBoolean);
  
  boolean getNomadsCheckedToday();
  
  void setMerchantCheckedToday(boolean paramBoolean);
  
  boolean getMerchantCheckedToday();
  
  void incrementProfessionSales();
  
  int getProfessionSales();
  
  boolean completedStartingGifts();
  
  void skipStartingGifts();
  
  void executeStartingGifts(World paramWorld, Village paramVillage, BlockPos paramBlockPos);
  
  void writeNBT(NBTTagCompound paramNBTTagCompound);
  
  void readNBT(NBTTagCompound paramNBTTagCompound);
  
  boolean isEmpty();
}


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\caps\IVillageData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */