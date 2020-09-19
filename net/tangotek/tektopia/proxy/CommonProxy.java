/*    */ package net.tangotek.tektopia.proxy;
/*    */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*    */ import com.leviathanstudio.craftstudio.common.network.ClientIAnimatedEventMessage;
/*    */ import com.leviathanstudio.craftstudio.common.network.ServerIAnimatedEventMessage;
/*    */ import java.util.concurrent.Callable;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.world.chunk.Chunk;
/*    */ import net.minecraftforge.common.capabilities.Capability;
/*    */ import net.minecraftforge.common.capabilities.CapabilityManager;
/*    */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*    */ import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.tangotek.tektopia.LicenseTracker;
/*    */ import net.tangotek.tektopia.ModEntities;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ import net.tangotek.tektopia.VillageClient;
/*    */ import net.tangotek.tektopia.caps.IPlayerLicense;
/*    */ import net.tangotek.tektopia.caps.IVillageData;
/*    */ import net.tangotek.tektopia.caps.PlayerLicense;
/*    */ import net.tangotek.tektopia.caps.VillageData;
/*    */ import net.tangotek.tektopia.network.PacketAIFilter;
/*    */ import net.tangotek.tektopia.network.PacketLicense;
/*    */ import net.tangotek.tektopia.network.PacketPathingNode;
/*    */ import net.tangotek.tektopia.network.PacketVillage;
/*    */ import net.tangotek.tektopia.network.PacketVillagerItemThought;
/*    */ import net.tangotek.tektopia.network.PacketVillagerThought;
/*    */ import net.tangotek.tektopia.pathing.PathingNodeClient;
/*    */ 
/*    */ public abstract class CommonProxy {
/* 30 */   public static int NETWORK_DISCRIMINATOR = 0;
/*    */   
/*    */   public void preInit(FMLPreInitializationEvent e) {
/* 33 */     TekVillager.NETWORK.registerMessage(TekClientAnimationHandler.ClientIAnimatedEventHandler.class, ClientIAnimatedEventMessage.class, NETWORK_DISCRIMINATOR++, Side.CLIENT);
/* 34 */     TekVillager.NETWORK.registerMessage(TekServerAnimationHandler.ServerIAnimatedEventHandler.class, ServerIAnimatedEventMessage.class, NETWORK_DISCRIMINATOR++, Side.SERVER);
/* 35 */     TekVillager.NETWORK.registerMessage(PacketVillagerItemThought.PacketVillagerItemThoughtHandler.class, PacketVillagerItemThought.class, NETWORK_DISCRIMINATOR++, Side.CLIENT);
/* 36 */     TekVillager.NETWORK.registerMessage(PacketVillagerThought.PacketVillagerThoughtHandler.class, PacketVillagerThought.class, NETWORK_DISCRIMINATOR++, Side.CLIENT);
/* 37 */     TekVillager.NETWORK.registerMessage(PacketPathingNode.PacketPathingNodeHandler.class, PacketPathingNode.class, NETWORK_DISCRIMINATOR++, Side.CLIENT);
/* 38 */     TekVillager.NETWORK.registerMessage(PacketVillage.PacketVillageHandler.class, PacketVillage.class, NETWORK_DISCRIMINATOR++, Side.CLIENT);
/* 39 */     TekVillager.NETWORK.registerMessage(PacketAIFilter.PacketAIFilterHandler.class, PacketAIFilter.class, NETWORK_DISCRIMINATOR++, Side.SERVER);
/* 40 */     TekVillager.NETWORK.registerMessage(PacketLicense.PacketLicenseHandler.class, PacketLicense.class, NETWORK_DISCRIMINATOR++, Side.SERVER);
/* 41 */     TekVillager.NETWORK.registerMessage(PacketLicense.PacketLicenseHandler.class, PacketLicense.class, NETWORK_DISCRIMINATOR++, Side.CLIENT);
/* 42 */     ModEntities.init();
/*    */     
/* 44 */     CapabilityManager.INSTANCE.register(IVillageData.class, (Capability.IStorage)new VillageData(), new VillageStorageFactory());
/* 45 */     CapabilityManager.INSTANCE.register(IPlayerLicense.class, (Capability.IStorage)new PlayerLicense(), new PlayerLicenseFactory());
/*    */     
/* 47 */     LicenseTracker.INSTANCE.setup();
/*    */   }
/*    */ 
/*    */   
/*    */   public void init(FMLInitializationEvent e) {
/* 52 */     TekStructureVillagePieces.registerVillagePieces();
/*    */   }
/*    */   public void postInit(FMLPostInitializationEvent e) {}
/*    */   
/*    */   public void handleNodeUpdate(PathingNodeClient node) {}
/*    */   
/*    */   public void handleVillage(VillageClient vc) {}
/*    */   
/*    */   public abstract <T extends com.leviathanstudio.craftstudio.common.animation.IAnimated> AnimationHandler<T> getNewAnimationHandler(Class<T> paramClass);
/*    */   
/*    */   private static class VillageStorageFactory implements Callable<IVillageData> { private VillageStorageFactory() {}
/*    */     
/*    */     public IVillageData call() throws Exception {
/* 65 */       return (IVillageData)new VillageData();
/*    */     } }
/*    */   
/*    */   private static class PlayerLicenseFactory implements Callable<IPlayerLicense> {
/*    */     private PlayerLicenseFactory() {}
/*    */     
/*    */     public IPlayerLicense call() throws Exception {
/* 72 */       return (IPlayerLicense)new PlayerLicense();
/*    */     }
/*    */   }
/*    */   
/*    */   public void registerItemRenderer(Item item, int meta, String id) {}
/*    */   
/*    */   public void registerModels() {}
/*    */   
/*    */   public void registerAnims() {}
/*    */   
/*    */   public void onChunkLoaded(Chunk ch) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\proxy\CommonProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */