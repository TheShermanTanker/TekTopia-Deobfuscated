/*     */ package net.tangotek.tektopia;
/*     */ 
/*     */ import com.websina.license.LicenseManagerTek;
/*     */ import java.security.GeneralSecurityException;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.common.capabilities.ICapabilityProvider;
/*     */ import net.minecraftforge.event.AttachCapabilitiesEvent;
/*     */ import net.minecraftforge.event.entity.EntityJoinWorldEvent;
/*     */ import net.minecraftforge.event.entity.player.PlayerEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import net.tangotek.tektopia.caps.IPlayerLicense;
/*     */ import net.tangotek.tektopia.caps.PlayerLicenseProvider;
/*     */ import net.tangotek.tektopia.network.PacketLicense;
/*     */ 
/*     */ public class LicenseTracker
/*     */ {
/*     */   public enum Feature
/*     */   {
/*  25 */     HATS("hats");
/*     */     private final String name;
/*     */     
/*     */     Feature(String n) {
/*  29 */       this.name = n;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  33 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*  37 */   public static final LicenseTracker INSTANCE = new LicenseTracker();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setup() {
/*  43 */     MinecraftForge.EVENT_BUS.register(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitLicense(EntityPlayerMP player, String licData) {
/*  48 */     IPlayerLicense pl = (IPlayerLicense)player.getCapability(PlayerLicenseProvider.PLAYER_LICENSE_CAPABILITY, null);
/*  49 */     pl.setLicenseData(licData);
/*  50 */     if (pl.isValid(player.getName())) {
/*  51 */       sendLicenseToTracking((Entity)player);
/*  52 */       sendLicenseToPlayer((Entity)player, player);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void sendLicenseToPlayer(Entity source, EntityPlayerMP target) {
/*  57 */     String licData = ((IPlayerLicense)source.getCapability(PlayerLicenseProvider.PLAYER_LICENSE_CAPABILITY, null)).getLicenseData();
/*  58 */     if (licData != null)
/*     */     {
/*  60 */       TekVillager.NETWORK.sendTo((IMessage)new PacketLicense(source.getUniqueID(), licData), target);
/*     */     }
/*     */   }
/*     */   
/*     */   private void sendLicenseToTracking(Entity source) {
/*  65 */     String licData = ((IPlayerLicense)source.getCapability(PlayerLicenseProvider.PLAYER_LICENSE_CAPABILITY, null)).getLicenseData();
/*  66 */     if (licData != null) {
/*  67 */       TekVillager.NETWORK.sendToAllTracking((IMessage)new PacketLicense(source.getUniqueID(), licData), source);
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPlayerStartTracking(PlayerEvent.StartTracking event) {
/*  73 */     if (!(event.getEntityPlayer().getEntityWorld()).isRemote && 
/*  74 */       event.getTarget() instanceof EntityPlayer) {
/*  75 */       INSTANCE.sendLicenseToPlayer(event.getTarget(), (EntityPlayerMP)event.getEntityPlayer());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void playerCapabilities(AttachCapabilitiesEvent<Entity> event) {
/*  82 */     if (event.getObject() instanceof EntityPlayer) {
/*  83 */       EntityPlayer player = (EntityPlayer)event.getObject();
/*     */       
/*  85 */       if (!player.hasCapability(PlayerLicenseProvider.PLAYER_LICENSE_CAPABILITY, null)) {
/*  86 */         event.addCapability(new ResourceLocation("tektopia", "PlayerLicense"), (ICapabilityProvider)new PlayerLicenseProvider());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPlayerClone(PlayerEvent.Clone event) {
/*  93 */     IPlayerLicense pl = (IPlayerLicense)event.getOriginal().getCapability(PlayerLicenseProvider.PLAYER_LICENSE_CAPABILITY, null);
/*  94 */     if (pl.isValid(event.getEntityPlayer().getName())) {
/*  95 */       String licData = pl.getLicenseData();
/*  96 */       if (licData != null) {
/*  97 */         ((IPlayerLicense)event.getEntityPlayer().getCapability(PlayerLicenseProvider.PLAYER_LICENSE_CAPABILITY, null)).setLicenseData(licData);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onEntityJoinWorld(EntityJoinWorldEvent event) {
/* 104 */     if ((event.getWorld()).isRemote && event.getEntity().getUniqueID().equals((Minecraft.getMinecraft()).player.getUniqueID()))
/*     */       
/*     */       try {
/*     */         
/* 108 */         LicenseManagerTek licenseManagerTek = new LicenseManagerTek();
/* 109 */         if (licenseManagerTek.isValid()) {
/* 110 */           System.out.println("Valid TekTopia licence file found! Sending to server");
/* 111 */           TekVillager.NETWORK.sendToServer((IMessage)new PacketLicense((Minecraft.getMinecraft()).player.getUniqueID(), licenseManagerTek.getLicense()));
/*     */         } else {
/* 113 */           System.err.println("Invalid TekTopia licence file");
/*     */         } 
/* 115 */       } catch (GeneralSecurityException gse) {
/* 116 */         System.out.println("GeneralSecurityException process TekTopia license file - " + gse.getLocalizedMessage());
/* 117 */       } catch (RuntimeException ex) {
/* 118 */         System.out.println("Missing or invalid TekTopia licence file. (This is not a problem)");
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\LicenseTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */