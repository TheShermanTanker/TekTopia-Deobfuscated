/*     */ package net.tangotek.tektopia;
/*     */ 
/*     */ import com.leviathanstudio.craftstudio.client.registry.CraftStudioLoader;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockCrops;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.command.ICommand;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.network.datasync.DataSerializers;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionType;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.SoundEvent;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*     */ import net.minecraft.world.gen.structure.StructureComponent;
/*     */ import net.minecraft.world.storage.loot.LootTableList;
/*     */ import net.minecraftforge.client.event.ModelRegistryEvent;
/*     */ import net.minecraftforge.event.RegistryEvent;
/*     */ import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
/*     */ import net.minecraftforge.event.entity.living.LivingDropsEvent;
/*     */ import net.minecraftforge.event.entity.player.ItemTooltipEvent;
/*     */ import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
/*     */ import net.minecraftforge.event.terraingen.PopulateChunkEvent;
/*     */ import net.minecraftforge.event.world.BlockEvent;
/*     */ import net.minecraftforge.fml.common.Mod;
/*     */ import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
/*     */ import net.minecraftforge.fml.common.Mod.EventHandler;
/*     */ import net.minecraftforge.fml.common.Mod.Instance;
/*     */ import net.minecraftforge.fml.common.SidedProxy;
/*     */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.PlayerEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import net.minecraftforge.fml.common.network.NetworkRegistry;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import net.tangotek.tektopia.commands.VillageCommands;
/*     */ import net.tangotek.tektopia.generation.TekStructureVillagePieces;
/*     */ import net.tangotek.tektopia.proxy.CommonProxy;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ @Mod(modid = "tektopia", name = "TekTopia", version = "1.1.0", useMetadata = true, updateJSON = "https://raw.githubusercontent.com/TangoTek/tektopia-public/master/update.json", acceptedMinecraftVersions = "[1.12.2]")
/*     */ public class TekVillager {
/*     */   public static final String MODID = "tektopia";
/*     */   
/*     */   public static <T extends com.leviathanstudio.craftstudio.common.animation.IAnimated> AnimationHandler<T> getNewAnimationHandler(Class<T> animatedClass) {
/*  59 */     return proxy.getNewAnimationHandler(animatedClass);
/*     */   }
/*     */   public static final String NAME = "TekTopia";
/*     */   static {
/*  63 */     DataSerializers.registerSerializer(TekDataSerializers.INT_LIST);
/*     */   }
/*     */   
/*  66 */   public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("tektopia");
/*     */   
/*  68 */   public static final ResourceLocation VILLAGE_STORAGE = LootTableList.register(new ResourceLocation("tektopia", "village_storage"));
/*     */   
/*     */   @Instance("tektopia")
/*     */   public static TekVillager instance;
/*     */   
/*     */   @SidedProxy(serverSide = "net.tangotek.tektopia.proxy.ServerProxy", clientSide = "net.tangotek.tektopia.proxy.ClientProxy")
/*     */   public static CommonProxy proxy;
/*     */   
/*     */   @EventHandler
/*     */   public void preInit(FMLPreInitializationEvent event) {
/*  78 */     System.out.println("TekTopia is loading!");
/*     */     
/*  80 */     this; proxy.preInit(event);
/*  81 */     NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onServerStarting(FMLServerStartingEvent evt) {
/*  87 */     VillageCommands vc = new VillageCommands();
/*  88 */     evt.registerServerCommand((ICommand)vc);
/*  89 */     vc.registerNodes();
/*     */     
/*  91 */     World world = evt.getServer().getEntityWorld();
/*     */     
/*  93 */     world.getGameRules().addGameRule("villagerItems", "false", GameRules.ValueType.BOOLEAN_VALUE);
/*  94 */     world.getGameRules().addGameRule("villagerSkillRate", "100", GameRules.ValueType.NUMERICAL_VALUE);
/*  95 */     world.getGameRules().addGameRule("villageRadius", "100", GameRules.ValueType.NUMERICAL_VALUE);
/*  96 */     world.getGameRules().addGameRule("villagerPenPercent", "100", GameRules.ValueType.NUMERICAL_VALUE);
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void init(FMLInitializationEvent event) {
/* 102 */     this; proxy.init(event);
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void postInit(FMLPostInitializationEvent event) {
/* 108 */     this; proxy.postInit(event);
/*     */   }
/*     */   
/*     */   @EventBusSubscriber
/*     */   public static class RegistrationHandler
/*     */   {
/* 114 */     static Map<Long, Set<VillageStructureType>> villageStructs = new HashMap<>();
/*     */ 
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void worldTick(TickEvent.WorldTickEvent event) {
/* 119 */       if (event.phase == TickEvent.Phase.START) {
/* 120 */         VillageManager.get(event.world).update();
/* 121 */         ModEntities.onWorldTick(event);
/*     */       } 
/*     */     }
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void registerBlocks(RegistryEvent.Register<Block> event) {
/* 127 */       ModBlocks.register(event.getRegistry());
/*     */     }
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void onChunkPopulate(PopulateChunkEvent.Post event) {
/* 132 */       if (event.isHasVillageGenerated()) {
/* 133 */         int i = event.getChunkX() * 16 + 8;
/* 134 */         int k = event.getChunkZ() * 16 + 8;
/*     */ 
/*     */         
/* 137 */         int chunkX = event.getChunkX() * 16;
/* 138 */         int chunkZ = event.getChunkZ() * 16;
/* 139 */         TekStructureVillagePieces.replaceVillagers(event.getWorld(), event.getRand(), chunkX, 0.0D, chunkZ, (chunkX + 25), 255.0D, (chunkZ + 25));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private static void setStructureBlock(StructureComponent comp, World world, IBlockState blockState, int x, int y, int z, StructureBoundingBox bbox) {
/*     */       try {
/* 146 */         Method setBlockStateMethod = StructureComponent.class.getDeclaredMethod("setBlockState", new Class[] { World.class, IBlockState.class, int.class, int.class, int.class, StructureBoundingBox.class });
/* 147 */         setBlockStateMethod.setAccessible(true);
/* 148 */         setBlockStateMethod.invoke(comp, new Object[] { world, blockState, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(z), bbox });
/*     */       }
/* 150 */       catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static boolean isNaturalBlock(IBlockState blockState) {
/* 156 */       return (blockState.getBlock() instanceof net.minecraft.block.BlockStone || blockState.getBlock() instanceof net.minecraft.block.BlockDirt || blockState.getBlock() instanceof net.minecraft.block.BlockSand);
/*     */     }
/*     */ 
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void onPlayerSleepEvent(PlayerSleepInBedEvent event) {
/* 162 */       if (!VillageManager.get(event.getEntityPlayer().getEntityWorld()).canSleepAt(event.getPos()))
/* 163 */         event.setResult(EntityPlayer.SleepResult.NOT_POSSIBLE_HERE); 
/*     */     }
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
/* 168 */       ModEntities.onPlayerLoggedIn(event);
/*     */     }
/*     */     
/*     */     @SubscribeEvent
/*     */     @SideOnly(Side.CLIENT)
/*     */     public static void onItemToolTip(ItemTooltipEvent event) {
/* 174 */       ModItems.onItemToolTip(event);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void onPlayerCraftEvent(PlayerEvent.ItemCraftedEvent event) {}
/*     */ 
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void onCropGrowEvent(BlockEvent.CropGrowEvent.Post event) {
/* 185 */       if (event.getState().getBlock() instanceof BlockCrops) {
/* 186 */         BlockCrops cropBlock = (BlockCrops)event.getState().getBlock();
/* 187 */         if (cropBlock.isMaxAge(event.getState())) {
/* 188 */           VillageManager.get(event.getWorld()).onCropGrowEvent((BlockEvent.CropGrowEvent)event);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void onHarvestDropsEvent(BlockEvent.HarvestDropsEvent event) {
/* 195 */       ModBlocks.onHarvestDropsEvent(event);
/*     */     }
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void onBabySpawnEvent(BabyEntitySpawnEvent event) {
/* 200 */       ModEntities.onBabySpawnEvent(event);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void onLivingDropsEvent(LivingDropsEvent event) {
/* 207 */       ModEntities.onLivingDropsEvent(event);
/*     */     }
/*     */ 
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void registerPotions(RegistryEvent.Register<Potion> event) {
/* 213 */       ModPotions.registerPotions(event.getRegistry());
/*     */     }
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
/* 218 */       ModPotions.registerPotionTypes(event.getRegistry());
/*     */     }
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
/* 223 */       ModSoundEvents.registerSounds(event.getRegistry());
/*     */     }
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void registerItems(RegistryEvent.Register<Item> event) {
/* 228 */       ModItems.register(event.getRegistry());
/* 229 */       ModBlocks.registerItemBlocks(event.getRegistry());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @SubscribeEvent
/*     */     public static void registerModels(ModelRegistryEvent event) {
/* 236 */       ModItems.registerModels();
/* 237 */       ModBlocks.registerModels();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @CraftStudioLoader
/*     */     public static void loadStuff() {
/* 244 */       TekVillager.proxy.registerModels();
/* 245 */       TekVillager.proxy.registerAnims();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\TekVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */