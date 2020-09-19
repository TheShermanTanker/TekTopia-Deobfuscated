/*     */ package net.tangotek.tektopia;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
/*     */ import net.minecraftforge.event.entity.living.LivingDropsEvent;
/*     */ import net.minecraftforge.event.entity.living.LivingHurtEvent;
/*     */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*     */ import net.minecraftforge.fml.client.registry.RenderingRegistry;
/*     */ import net.minecraftforge.fml.common.gameevent.PlayerEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import net.minecraftforge.fml.common.registry.EntityRegistry;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import net.tangotek.tektopia.client.ParticleItemThought;
/*     */ import net.tangotek.tektopia.client.RenderChild;
/*     */ import net.tangotek.tektopia.client.RenderMerchant;
/*     */ import net.tangotek.tektopia.entities.EntityArchitect;
/*     */ import net.tangotek.tektopia.entities.EntityBard;
/*     */ import net.tangotek.tektopia.entities.EntityBlacksmith;
/*     */ import net.tangotek.tektopia.entities.EntityButcher;
/*     */ import net.tangotek.tektopia.entities.EntityChef;
/*     */ import net.tangotek.tektopia.entities.EntityChild;
/*     */ import net.tangotek.tektopia.entities.EntityCleric;
/*     */ import net.tangotek.tektopia.entities.EntityDruid;
/*     */ import net.tangotek.tektopia.entities.EntityEnchanter;
/*     */ import net.tangotek.tektopia.entities.EntityFarmer;
/*     */ import net.tangotek.tektopia.entities.EntityGuard;
/*     */ import net.tangotek.tektopia.entities.EntityLumberjack;
/*     */ import net.tangotek.tektopia.entities.EntityMerchant;
/*     */ import net.tangotek.tektopia.entities.EntityMiner;
/*     */ import net.tangotek.tektopia.entities.EntityNecromancer;
/*     */ import net.tangotek.tektopia.entities.EntityNitwit;
/*     */ import net.tangotek.tektopia.entities.EntityNomad;
/*     */ import net.tangotek.tektopia.entities.EntityRancher;
/*     */ import net.tangotek.tektopia.entities.EntitySpiritSkull;
/*     */ import net.tangotek.tektopia.entities.EntityTeacher;
/*     */ import net.tangotek.tektopia.entities.EntityTradesman;
/*     */ import net.tangotek.tektopia.network.PacketVillagerItemThought;
/*     */ 
/*     */ public class ModEntities {
/*  52 */   private static Map<Entity, Item> entityItemThoughts = new HashMap<>();
/*     */   public static final int MAX_ANIMAL_HUNGER = 100;
/*     */   
/*     */   public static void init() {
/*  56 */     int id = 1;
/*     */     
/*  58 */     System.out.println("Registering Entities");
/*     */     
/*  60 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "lumberjack"), EntityLumberjack.class, "Lumberjack", id++, TekVillager.instance, 128, 1, true, 10053120, 65280);
/*  61 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "farmer"), EntityFarmer.class, "Farmer", id++, TekVillager.instance, 128, 1, true, 4482560, 65348);
/*  62 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "miner"), EntityMiner.class, "Miner", id++, TekVillager.instance, 128, 1, true, 4460800, 7864132);
/*  63 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "blacksmith"), EntityBlacksmith.class, "Blacksmith", id++, TekVillager.instance, 128, 1, true, 2232610, 12320682);
/*  64 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "guard"), EntityGuard.class, "Guard", id++, TekVillager.instance, 128, 1, true, 2276130, 12268492);
/*  65 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "architect"), EntityArchitect.class, "Architect", id++, TekVillager.instance, 128, 1, true, 2276283, 1127202);
/*  66 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "tradesman"), EntityTradesman.class, "Tradesman", id++, TekVillager.instance, 128, 1, true, 7813339, 8631074);
/*  67 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "merchant"), EntityMerchant.class, "Merchant", id++, TekVillager.instance, 128, 1, true, 2271778, 7812095);
/*  68 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "nomad"), EntityNomad.class, "Nomad", id++, TekVillager.instance, 128, 1, true, 420386, 492095);
/*  69 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "rancher"), EntityRancher.class, "Rancher", id++, TekVillager.instance, 128, 1, true, 2254523, 1168210);
/*  70 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "butcher"), EntityButcher.class, "Butcher", id++, TekVillager.instance, 128, 1, true, 11167419, 1168210);
/*  71 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "enchanter"), EntityEnchanter.class, "Enchanter", id++, TekVillager.instance, 128, 1, true, 3368567, 1157714);
/*  72 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "child"), EntityChild.class, "Child", id++, TekVillager.instance, 128, 1, true, 3368465, 14527058);
/*  73 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "chef"), EntityChef.class, "Chef", id++, TekVillager.instance, 128, 1, true, 3370993, 4042514);
/*  74 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "teacher"), EntityTeacher.class, "Teacher", id++, TekVillager.instance, 128, 1, true, 2256881, 2273042);
/*  75 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "nitwit"), EntityNitwit.class, "Nitwit", id++, TekVillager.instance, 128, 1, true, 2261713, 2698914);
/*  76 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "cleric"), EntityCleric.class, "Cleric", id++, TekVillager.instance, 128, 1, true, 2261713, 2698914);
/*  77 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "druid"), EntityDruid.class, "Druid", id++, TekVillager.instance, 128, 1, true, 2261713, 2698914);
/*  78 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "bard"), EntityBard.class, "Bard", id++, TekVillager.instance, 128, 1, true, 2293745, 2935970);
/*     */     
/*  80 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "necromancer"), EntityNecromancer.class, "Necromancer", id++, TekVillager.instance, 128, 1, true, 2236962, 12255232);
/*  81 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "spirit_skull"), EntitySpiritSkull.class, "Spirit Skull", id++, TekVillager.instance, 64, 1, true);
/*  82 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "death_cloud"), EntityDeathCloud.class, "Death Cloud", id++, TekVillager.instance, 64, 1, true);
/*  83 */     EntityRegistry.registerModEntity(new ResourceLocation("tektopia", "captain_aura"), EntityCaptainAura.class, "Captain Aura", id++, TekVillager.instance, 64, 1, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public static void initModels() {
/*  95 */     RenderingRegistry.registerEntityRenderingHandler(EntityLumberjack.class, (IRenderFactory)RenderLumberjack.FACTORY);
/*  96 */     RenderingRegistry.registerEntityRenderingHandler(EntityFarmer.class, (IRenderFactory)RenderFarmer.FACTORY);
/*  97 */     RenderingRegistry.registerEntityRenderingHandler(EntityMiner.class, (IRenderFactory)RenderMiner.FACTORY);
/*  98 */     RenderingRegistry.registerEntityRenderingHandler(EntityBlacksmith.class, (IRenderFactory)RenderBlacksmith.FACTORY);
/*  99 */     RenderingRegistry.registerEntityRenderingHandler(EntityGuard.class, (IRenderFactory)RenderGuard.FACTORY);
/* 100 */     RenderingRegistry.registerEntityRenderingHandler(EntityArchitect.class, (IRenderFactory)RenderArchitect.FACTORY);
/* 101 */     RenderingRegistry.registerEntityRenderingHandler(EntityTradesman.class, (IRenderFactory)RenderTradesman.FACTORY);
/* 102 */     RenderingRegistry.registerEntityRenderingHandler(EntityRancher.class, (IRenderFactory)RenderRancher.FACTORY);
/* 103 */     RenderingRegistry.registerEntityRenderingHandler(EntityButcher.class, (IRenderFactory)RenderButcher.FACTORY);
/* 104 */     RenderingRegistry.registerEntityRenderingHandler(EntityMerchant.class, (IRenderFactory)RenderMerchant.FACTORY);
/* 105 */     RenderingRegistry.registerEntityRenderingHandler(EntityEnchanter.class, (IRenderFactory)RenderEnchanter.FACTORY);
/* 106 */     RenderingRegistry.registerEntityRenderingHandler(EntityChild.class, (IRenderFactory)RenderChild.FACTORY);
/* 107 */     RenderingRegistry.registerEntityRenderingHandler(EntityChef.class, (IRenderFactory)RenderChef.FACTORY);
/* 108 */     RenderingRegistry.registerEntityRenderingHandler(EntityTeacher.class, (IRenderFactory)RenderTeacher.FACTORY);
/* 109 */     RenderingRegistry.registerEntityRenderingHandler(EntityNitwit.class, (IRenderFactory)RenderNitwit.FACTORY);
/* 110 */     RenderingRegistry.registerEntityRenderingHandler(EntityNomad.class, (IRenderFactory)RenderNomad.FACTORY);
/* 111 */     RenderingRegistry.registerEntityRenderingHandler(EntityCleric.class, (IRenderFactory)RenderCleric.FACTORY);
/* 112 */     RenderingRegistry.registerEntityRenderingHandler(EntityDruid.class, (IRenderFactory)RenderDruid.FACTORY);
/* 113 */     RenderingRegistry.registerEntityRenderingHandler(EntityBard.class, (IRenderFactory)RenderBard.FACTORY);
/*     */     
/* 115 */     RenderingRegistry.registerEntityRenderingHandler(EntityNecromancer.class, (IRenderFactory)RenderNecromancer.FACTORY);
/* 116 */     RenderingRegistry.registerEntityRenderingHandler(EntitySpiritSkull.class, (IRenderFactory)RenderSpiritSkull.FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Entity makeTaggedEntity(Entity ent, EntityTagType tagType) {
/* 122 */     return makeTaggedEntity(ent, tagType, "");
/*     */   }
/*     */   
/*     */   private static Entity makeTaggedEntity(Entity ent, EntityTagType tagType, String displayName) {
/* 126 */     ent.getEntityData().setBoolean(tagType.tag, true);
/* 127 */     if (!displayName.isEmpty()) {
/* 128 */       ent.setCustomNameTag(displayName);
/*     */     }
/* 130 */     return ent;
/*     */   }
/*     */   
/*     */   public static boolean isTaggedEntity(Entity ent, EntityTagType tagType) {
/* 134 */     return ent.getEntityData().getBoolean(tagType.tag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sendItemThought(Entity e, Item i) {
/* 155 */     entityItemThoughts.put(e, i);
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public static void handleItemThought(World world, Entity ent, Item item) {
/* 160 */     ParticleItemThought itemThought = new ParticleItemThought(world, ent, item);
/* 161 */     (Minecraft.getMinecraft()).effectRenderer.addEffect((Particle)itemThought);
/*     */   }
/*     */   
/*     */   public static void onWorldTick(TickEvent.WorldTickEvent event) {
/* 165 */     processItemThoughts();
/*     */   }
/*     */   
/*     */   public static void processItemThoughts() {
/* 169 */     Iterator<Map.Entry<Entity, Item>> itr = entityItemThoughts.entrySet().iterator();
/* 170 */     while (itr.hasNext()) {
/* 171 */       Map.Entry<Entity, Item> eit = itr.next();
/* 172 */       Entity ent = eit.getKey();
/* 173 */       if (ent.isEntityAlive() && ent.ticksExisted % 80 == 0) {
/* 174 */         PacketVillagerItemThought msg = new PacketVillagerItemThought(ent, eit.getValue());
/* 175 */         TekVillager.NETWORK.sendToAllAround((IMessage)msg, new NetworkRegistry.TargetPoint(ent.world.provider.getDimension(), ent.posX, ent.posY + 0.5D, ent.posZ, 64.0D));
/* 176 */         itr.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
/* 182 */     NBTTagCompound nbt = event.player.getEntityData();
/* 183 */     boolean loggedInBefore = nbt.getBoolean("tektopia_loggedInBefore");
/* 184 */     if (!loggedInBefore) {
/* 185 */       givePlayerStarterBook(event.player);
/* 186 */       nbt.setBoolean("tektopia_loggedInBefore", true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void givePlayerStarterBook(EntityPlayer player) {
/* 192 */     ItemStack bookItem = new ItemStack(Items.WRITTEN_BOOK);
/* 193 */     String json = I18n.translateToLocal("starter_book.nbt");
/*     */     try {
/* 195 */       bookItem.setTagCompound(JsonToNBT.getTagFromJson(json));
/* 196 */     } catch (NBTException e) {
/* 197 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 200 */     player.addItemStackToInventory(bookItem);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void onBabySpawnEvent(BabyEntitySpawnEvent event) {
/* 206 */     if (event.getCausedByPlayer() == null && event.getChild() instanceof EntityAnimal) {
/* 207 */       makeTaggedEntity((Entity)event.getChild(), EntityTagType.VILLAGER);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void onLivingHurtEvent(LivingHurtEvent event) {
/* 226 */     if (isTaggedEntity(event.getEntity(), EntityTagType.VILLAGER))
/*     */     {
/* 228 */       if (event.getSource() == DamageSource.IN_WALL) {
/* 229 */         event.setCanceled(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void onLivingDropsEvent(LivingDropsEvent event) {
/* 235 */     if (event.getEntityLiving() instanceof EntityAnimal) {
/* 236 */       if (event.getSource() == DamageSource.STARVE) {
/*     */         
/* 238 */         event.setCanceled(true);
/*     */       }
/* 240 */       else if (event.getSource().getTrueSource() instanceof EntityButcher) {
/* 241 */         EntityButcher butcher = (EntityButcher)event.getSource().getTrueSource();
/* 242 */         if (butcher != null && isTaggedEntity((Entity)event.getEntityLiving(), EntityTagType.VILLAGER))
/*     */         {
/*     */           
/* 245 */           event.getDrops().forEach(d -> ModItems.makeTaggedItem(d.getItem(), ItemTagType.VILLAGER));
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getAnimalHunger(EntityAnimal animal) {
/* 253 */     int hunger = animal.getEntityData().getInteger("animal_hunger");
/* 254 */     if (hunger <= 0) {
/* 255 */       hunger = 100;
/* 256 */       setAnimalHunger(animal, hunger);
/*     */     } 
/*     */     
/* 259 */     return hunger;
/*     */   }
/*     */   
/*     */   public static int modifyAnimalHunger(EntityAnimal animal, int delta) {
/* 263 */     int hunger = getAnimalHunger(animal);
/* 264 */     hunger = Math.max(Math.min(hunger + delta, 100), 1);
/* 265 */     setAnimalHunger(animal, hunger);
/* 266 */     return hunger;
/*     */   }
/*     */   
/*     */   public static boolean isAnimalStarving(EntityAnimal animal) {
/* 270 */     return (getAnimalHunger(animal) == 1);
/*     */   }
/*     */   
/*     */   public static boolean isAnimalFull(EntityAnimal animal) {
/* 274 */     return (getAnimalHunger(animal) == 100);
/*     */   }
/*     */   
/*     */   public static boolean isAnimalHungry(EntityAnimal animal) {
/* 278 */     return (getAnimalHunger(animal) < 75.0D);
/*     */   }
/*     */   
/*     */   private static void setAnimalHunger(EntityAnimal animal, int value) {
/* 282 */     animal.getEntityData().setInteger("animal_hunger", value);
/*     */   }
/*     */   
/*     */   public static boolean isEntityUsable(Entity thing, long tickCooldown, long worldTimeNow) {
/* 286 */     return (getEntityUseTime(thing) + tickCooldown < worldTimeNow);
/*     */   }
/*     */   
/*     */   public static void useEntity(Entity thing, long worldTimeNow) {
/* 290 */     thing.getEntityData().setLong("entity_use_time", worldTimeNow);
/*     */   }
/*     */   
/*     */   private static long getEntityUseTime(Entity thing) {
/* 294 */     return thing.getEntityData().getLong("entity_use_time");
/*     */   }
/*     */   
/*     */   public static void onProjectileImpact(ProjectileImpactEvent event) {}
/*     */   
/*     */   public static void onAttackEntityEvent(AttackEntityEvent event) {}
/*     */   
/*     */   public static void onPlayerClientTick(TickEvent.PlayerTickEvent event) {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\ModEntities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */