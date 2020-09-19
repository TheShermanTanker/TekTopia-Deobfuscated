/*     */ package net.tangotek.tektopia;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.event.entity.player.ItemTooltipEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.PlayerEvent;
/*     */ import net.minecraftforge.registries.IForgeRegistry;
/*     */ import net.minecraftforge.registries.IForgeRegistryEntry;
/*     */ import net.tangotek.tektopia.caps.IVillageData;
/*     */ import net.tangotek.tektopia.entities.EntityBlacksmith;
/*     */ import net.tangotek.tektopia.entities.EntityGuard;
/*     */ import net.tangotek.tektopia.entities.EntityNomad;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.items.ItemHammer;
/*     */ import net.tangotek.tektopia.items.ItemProfessionToken;
/*     */ import net.tangotek.tektopia.items.ItemRancherHat;
/*     */ import net.tangotek.tektopia.items.ItemStructureToken;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class ModItems {
/*  27 */   public static ItemHammer ironHammer = new ItemHammer(Item.ToolMaterial.IRON, "iron_hammer");
/*  28 */   public static ItemBeer beer = new ItemBeer("beer");
/*  29 */   public static ItemHeart heart = new ItemHeart("heart");
/*  30 */   public static ItemRancherHat rancherHat = new ItemRancherHat("rancher_hat");
/*     */   
/*  32 */   public static ItemStructureToken structureTavern = new ItemStructureToken("structure_tavern", 45);
/*  33 */   public static ItemStructureToken structureTownHall = new ItemStructureToken("structure_townhall", 0);
/*  34 */   public static ItemStructureToken structureSchool = new ItemStructureToken("structure_school", 35);
/*  35 */   public static ItemStructureToken structureStorage = new ItemStructureToken("structure_storage", 0);
/*  36 */   public static ItemStructureToken structureSheepPen = new ItemStructureToken("structure_sheeppen", 18);
/*  37 */   public static ItemStructureToken structureMineshaft = new ItemStructureToken("structure_mineshaft", 3);
/*  38 */   public static ItemStructureToken structurePigPen = new ItemStructureToken("structure_pigpen", 18);
/*  39 */   public static ItemStructureToken structureHome2 = new ItemStructureToken("structure_home2", 6);
/*  40 */   public static ItemStructureToken structureHome4 = new ItemStructureToken("structure_home4", 13);
/*  41 */   public static ItemStructureToken structureHome6 = new ItemStructureToken("structure_home6", 20);
/*  42 */   public static ItemStructureToken structureCowPen = new ItemStructureToken("structure_cowpen", 20);
/*  43 */   public static ItemStructureToken structureLibrary = new ItemStructureToken("structure_library", 30);
/*  44 */   public static ItemStructureToken structureChickenCoop = new ItemStructureToken("structure_chickencoop", 16);
/*  45 */   public static ItemStructureToken structureButcher = new ItemStructureToken("structure_butcher", 25);
/*  46 */   public static ItemStructureToken structureBlacksmith = new ItemStructureToken("structure_blacksmith", 30);
/*  47 */   public static ItemStructureToken structureGuardPost = new ItemStructureToken("structure_guard_post", 2);
/*  48 */   public static ItemStructureToken structureMerchantStall = new ItemStructureToken("structure_merchant_stall", 20);
/*  49 */   public static ItemStructureToken structureKitchen = new ItemStructureToken("structure_kitchen", 18);
/*  50 */   public static ItemStructureToken structureBarracks = new ItemStructureToken("structure_barracks", 35); public static ItemProfessionToken itemBard; public static ItemProfessionToken itemBlacksmith; public static ItemProfessionToken itemButcher; public static ItemProfessionToken itemChef; public static ItemProfessionToken itemCleric; public static ItemProfessionToken itemDruid; public static ItemProfessionToken itemEnchanter; public static ItemProfessionToken itemFarmer; public static ItemProfessionToken itemGuard; public static ItemProfessionToken itemLumberjack; public static ItemProfessionToken itemMiner; public static ItemProfessionToken itemRancher; public static ItemProfessionToken itemTeacher; public static ItemProfessionToken itemChild; public static ItemProfessionToken itemNitWit; public static ItemProfessionToken itemNomad; public static ItemProfessionToken itemCaptain;
/*     */   static {
/*  52 */     itemBard = new ItemProfessionToken("prof_bard", ProfessionType.BARD, (w, v) -> new EntityBard(w), 15);
/*  53 */     itemBlacksmith = new ItemProfessionToken("prof_blacksmith", ProfessionType.BLACKSMITH, (w, v) -> new EntityBlacksmith(w), 5);
/*  54 */     itemButcher = new ItemProfessionToken("prof_butcher", ProfessionType.BUTCHER, (w, v) -> new EntityButcher(w), 4);
/*  55 */     itemChef = new ItemProfessionToken("prof_chef", ProfessionType.CHEF, (w, v) -> new EntityChef(w), 7);
/*  56 */     itemCleric = new ItemProfessionToken("prof_cleric", ProfessionType.CLERIC, (w, v) -> new EntityCleric(w), 12);
/*  57 */     itemDruid = new ItemProfessionToken("prof_druid", ProfessionType.DRUID, (w, v) -> new EntityDruid(w), 12);
/*  58 */     itemEnchanter = new ItemProfessionToken("prof_enchanter", ProfessionType.ENCHANTER, (w, v) -> new EntityEnchanter(w), 15);
/*  59 */     itemFarmer = new ItemProfessionToken("prof_farmer", ProfessionType.FARMER, (w, v) -> new EntityFarmer(w), 3);
/*  60 */     itemGuard = new ItemProfessionToken("prof_guard", ProfessionType.GUARD, (w, v) -> new EntityGuard(w), 5);
/*  61 */     itemLumberjack = new ItemProfessionToken("prof_lumberjack", ProfessionType.LUMBERJACK, (w, v) -> new EntityLumberjack(w), 3);
/*  62 */     itemMiner = new ItemProfessionToken("prof_miner", ProfessionType.MINER, (w, v) -> new EntityMiner(w), 4);
/*  63 */     itemRancher = new ItemProfessionToken("prof_rancher", ProfessionType.RANCHER, (w, v) -> new EntityRancher(w), 4);
/*  64 */     itemTeacher = new ItemProfessionToken("prof_teacher", ProfessionType.TEACHER, (w, v) -> new EntityTeacher(w), 8);
/*  65 */     itemChild = new ItemProfessionToken("prof_child", ProfessionType.CHILD, (w, v) -> new EntityChild(w), 0);
/*  66 */     itemNitWit = new ItemProfessionToken("prof_nitwit", ProfessionType.NITWIT, (w, v) -> new EntityNitwit(w), 0);
/*  67 */     itemNomad = new ItemProfessionToken("prof_nomad", ProfessionType.NOMAD, (w, v) -> new EntityNomad(w), 0);
/*  68 */     itemCaptain = new ItemProfessionToken("prof_captain", ProfessionType.CAPTAIN, (w, v) -> { if (v instanceof EntityGuard && v.hasVillage()) { EntityGuard guard = (EntityGuard)v; if (!guard.isCaptain()) { List<EntityGuard> otherGuards = w.getEntitiesWithinAABB(EntityGuard.class, v.getVillage().getAABB().grow(50.0D)); otherGuards.stream().filter(()).forEach(()); guard.setCaptain(true); guard.modifyHappy(100); guard.playSound(SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 1.0F, 1.0F); }  }  return null; }3)
/*     */       {
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
/*     */         public int getCost(Village v)
/*     */         {
/*  89 */           if (v.getStructures(VillageStructureType.BARRACKS).isEmpty()) {
/*  90 */             return 0;
/*     */           }
/*  92 */           return super.getCost(v);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*  98 */   public static ItemStructureToken[] structureTokens = new ItemStructureToken[] { structureTavern, structureTownHall, structureSchool, structureStorage, structureSheepPen, structureMineshaft, structurePigPen, structureHome2, structureHome4, structureHome6, structureCowPen, structureLibrary, structureChickenCoop, structureButcher, structureBlacksmith, structureGuardPost, structureMerchantStall, structureKitchen, structureBarracks };
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
/*     */ 
/*     */ 
/*     */   
/* 120 */   public static Map<ProfessionType, ItemProfessionToken> professionTokens = new HashMap<>();
/*     */   
/*     */   static {
/* 123 */     addProfessionToken(itemBard);
/* 124 */     addProfessionToken(itemBlacksmith);
/* 125 */     addProfessionToken(itemButcher);
/* 126 */     addProfessionToken(itemChef);
/* 127 */     addProfessionToken(itemCleric);
/* 128 */     addProfessionToken(itemDruid);
/* 129 */     addProfessionToken(itemEnchanter);
/* 130 */     addProfessionToken(itemFarmer);
/* 131 */     addProfessionToken(itemGuard);
/* 132 */     addProfessionToken(itemLumberjack);
/* 133 */     addProfessionToken(itemMiner);
/* 134 */     addProfessionToken(itemRancher);
/* 135 */     addProfessionToken(itemTeacher);
/* 136 */     addProfessionToken(itemChild);
/* 137 */     addProfessionToken(itemNitWit);
/* 138 */     addProfessionToken(itemNomad);
/* 139 */     addProfessionToken(itemCaptain);
/*     */   }
/*     */   
/*     */   private static void addProfessionToken(ItemProfessionToken token) {
/* 143 */     professionTokens.put(token.getProfessionType(), token);
/*     */   }
/*     */   
/* 146 */   public static ItemStack EMPTY_HAND_ITEM = new ItemStack(Items.RECORD_11);
/*     */ 
/*     */   
/*     */   public static void register(IForgeRegistry<Item> registry) {
/* 150 */     System.out.println("Registering Items");
/* 151 */     registry.registerAll((IForgeRegistryEntry[])new Item[] { (Item)ironHammer, (Item)beer, (Item)heart, (Item)rancherHat });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     professionTokens.values().forEach(pt -> registry.register((IForgeRegistryEntry)pt));
/* 159 */     registry.registerAll((IForgeRegistryEntry[])structureTokens);
/*     */   }
/*     */   
/*     */   public static void registerModels() {
/* 163 */     ironHammer.registerItemModel();
/* 164 */     beer.registerItemModel();
/* 165 */     heart.registerItemModel();
/* 166 */     rancherHat.registerItemModel();
/*     */     
/* 168 */     for (ItemStructureToken token : structureTokens) {
/* 169 */       token.registerItemModel();
/*     */     }
/*     */     
/* 172 */     professionTokens.values().forEach(pt -> pt.registerItemModel());
/*     */   }
/*     */   
/*     */   public static ItemProfessionToken getProfessionToken(ProfessionType pt) {
/* 176 */     return professionTokens.get(pt);
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
/*     */   
/*     */   public static String getSkullAnimal(ItemStack skull) {
/* 216 */     if (skull.hasTagCompound()) {
/* 217 */       return skull.getTagCompound().getString("SkullName");
/*     */     }
/*     */     
/* 220 */     return "";
/*     */   }
/*     */   
/*     */   private static boolean isAllVillager(IInventory inv) {
/* 224 */     for (int i = 0; i < inv.getSizeInventory(); i++) {
/* 225 */       ItemStack itemStack = inv.getStackInSlot(i);
/* 226 */       if (!itemStack.isEmpty() && !isTaggedItem(itemStack, ItemTagType.VILLAGER))
/* 227 */         return false; 
/*     */     } 
/* 229 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isCompressionCraft(PlayerEvent.ItemCraftedEvent event) {
/* 233 */     Item item = event.crafting.getItem();
/* 234 */     if (item == Items.IRON_INGOT || item == Items.REDSTONE || item == Items.EMERALD || item == Items.WHEAT || item == Items.GOLD_INGOT || item == 
/* 235 */       Item.getItemFromBlock(Blocks.EMERALD_BLOCK) || item == 
/* 236 */       Item.getItemFromBlock(Blocks.IRON_BLOCK) || item == 
/* 237 */       Item.getItemFromBlock(Blocks.REDSTONE_BLOCK) || item == 
/* 238 */       Item.getItemFromBlock(Blocks.GOLD_BLOCK) || item == 
/* 239 */       Item.getItemFromBlock(Blocks.HAY_BLOCK)) {
/* 240 */       return true;
/*     */     }
/*     */     
/* 243 */     return false;
/*     */   }
/*     */   
/*     */   public static void onPlayerCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
/* 247 */     if (isAllVillager(event.craftMatrix) && isCompressionCraft(event)) {
/* 248 */       makeTaggedItem(event.crafting, ItemTagType.VILLAGER);
/*     */     }
/*     */   }
/*     */   
/*     */   public static ItemStack makeTaggedItem(ItemStack itemStack, ItemTagType tag, String displayName) {
/* 253 */     return makeTaggedItemInternal(itemStack, tag, displayName);
/*     */   }
/*     */   
/*     */   public static ItemStack makeTaggedItem(ItemStack itemStack, ItemTagType tag) {
/* 257 */     return makeTaggedItemInternal(itemStack, tag, itemStack.getDisplayName());
/*     */   }
/*     */   
/*     */   public static ItemStack createTaggedItem(Item item, int qty, ItemTagType tag) {
/* 261 */     ItemStack itemStack = new ItemStack(item, qty);
/* 262 */     return makeTaggedItemInternal(itemStack, tag, itemStack.getDisplayName());
/*     */   }
/*     */   
/*     */   public static ItemStack createTaggedItem(Item item, ItemTagType tag) {
/* 266 */     return createTaggedItem(item, 1, tag);
/*     */   }
/*     */   
/*     */   public static ItemStack untagItem(ItemStack itemStack, ItemTagType tag) {
/* 270 */     if (isTaggedItem(itemStack, tag)) {
/* 271 */       itemStack.getOrCreateSubCompound("village").setBoolean(tag.tag, false);
/*     */     }
/* 273 */     return itemStack;
/*     */   }
/*     */   
/*     */   private static ItemStack makeTaggedItemInternal(ItemStack itemStack, ItemTagType tag, String displayName) {
/* 277 */     if (!isTaggedItem(itemStack, tag)) {
/* 278 */       itemStack.getOrCreateSubCompound("village").setBoolean(tag.tag, true);
/*     */     }
/*     */     
/* 281 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static boolean isVillagerItemsEnabled() {
/* 285 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isTaggedItem(ItemStack itemStack, ItemTagType tag) {
/* 290 */     NBTTagCompound tagCompound = itemStack.getSubCompound("village");
/* 291 */     if (tagCompound != null) {
/* 292 */       return tagCompound.getBoolean(tag.tag);
/*     */     }
/*     */     
/* 295 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean canVillagerSee(ItemStack itemStack) {
/* 299 */     return (!isVillagerItemsEnabled() || isTaggedItem(itemStack, ItemTagType.VILLAGER));
/*     */   }
/*     */   
/*     */   public static void bindItemToVillage(ItemStack itemStack, Village v) {
/* 303 */     itemStack.getOrCreateSubCompound("village").setUniqueId("villageId", v.getTownData().getUUID());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isItemVillageBound(ItemStack itemStack, Village v) {
/* 308 */     IVillageData townData = v.getTownData();
/* 309 */     if (itemStack.getOrCreateSubCompound("village").hasUniqueId("villageId") && townData != null) {
/* 310 */       return itemStack.getOrCreateSubCompound("village").getUniqueId("villageId").equals(townData.getUUID());
/*     */     }
/* 312 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isItemVillageBound(ItemStack itemStack) {
/* 316 */     return itemStack.getOrCreateSubCompound("village").hasUniqueId("villageId");
/*     */   }
/*     */   
/*     */   public static void onItemToolTip(ItemTooltipEvent event) {
/* 320 */     if (isTaggedItem(event.getItemStack(), ItemTagType.VILLAGER) && 
/* 321 */       !event.getToolTip().isEmpty())
/* 322 */       event.getToolTip().set(0, TextFormatting.GREEN + (String)event.getToolTip().get(0)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\ModItems.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */