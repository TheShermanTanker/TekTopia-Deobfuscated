/*    */ package net.tangotek.tektopia;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.nbt.NBTBase;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.nbt.NBTTagList;
/*    */ import net.minecraft.nbt.NBTTagString;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.storage.MapStorage;
/*    */ 
/*    */ public class TekTopiaGlobalData extends WorldSavedData {
/*    */   private static final String DATA_NAME = "tektopia_GlobalData";
/*    */   
/*    */   public enum PatreonSupportTier {
/* 17 */     Level1,
/* 18 */     Level2,
/* 19 */     Level3,
/* 20 */     Level4;
/*    */   }
/*    */   
/* 23 */   private Map<PatreonSupportTier, Set<UUID>> patreonTiers = new HashMap<>();
/*    */   
/* 25 */   private Map<UUID, String> licenseMap = new HashMap<>();
/*    */ 
/*    */   
/*    */   public TekTopiaGlobalData() {
/* 29 */     super("tektopia_GlobalData");
/*    */   }
/*    */   public TekTopiaGlobalData(String s) {
/* 32 */     super(s);
/*    */   }
/*    */   
/*    */   public static TekTopiaGlobalData get(World world) {
/* 36 */     MapStorage storage = world.getMapStorage();
/* 37 */     TekTopiaGlobalData instance = (TekTopiaGlobalData)storage.getOrLoadData(TekTopiaGlobalData.class, "tektopia_GlobalData");
/*    */     
/* 39 */     if (instance == null) {
/* 40 */       instance = new TekTopiaGlobalData();
/* 41 */       storage.setData("tektopia_GlobalData", instance);
/*    */     } 
/* 43 */     return instance;
/*    */   }
/*    */ 
/*    */   
/*    */   public void readFromNBT(NBTTagCompound nbt) {
/* 48 */     for (PatreonSupportTier tier : PatreonSupportTier.values()) {
/* 49 */       NBTTagList tagList = nbt.getTagList(tier.name(), 8);
/* 50 */       if (!tagList.isEmpty()) {
/* 51 */         Set<UUID> tierSet = new HashSet<>();
/* 52 */         for (int i = 0; i < tagList.tagCount(); i++) {
/* 53 */           tierSet.add(UUID.fromString(tagList.getStringTagAt(i)));
/*    */         }
/* 55 */         this.patreonTiers.put(tier, tierSet);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
/* 63 */     this.patreonTiers.forEach((key, value) -> {
/*    */           NBTTagList tagList = new NBTTagList();
/*    */           
/*    */           value.forEach(());
/*    */           compound.setTag(key.name(), (NBTBase)tagList);
/*    */         });
/* 69 */     return compound;
/*    */   }
/*    */   
/*    */   public String getPatreonLicense(UUID uuid) {
/* 73 */     return this.licenseMap.get(uuid);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submitPatreonLicense(UUID uuid, String licData) {
/* 78 */     this.licenseMap.put(uuid, licData);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\TekTopiaGlobalData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */