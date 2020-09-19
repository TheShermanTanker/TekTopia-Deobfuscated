/*     */ package net.tangotek.tektopia.structures;
/*     */ 
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.Village;
/*     */ 
/*     */ public enum VillageStructureType
/*     */ {
/*  12 */   STORAGE((new ItemStack((Item)ModItems.structureStorage)).setStackDisplayName("Storage"), 12)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  15 */       return new VillageStructureStorage(w, v, i);
/*     */     }
/*     */   },
/*  18 */   BLACKSMITH((new ItemStack((Item)ModItems.structureBlacksmith)).setStackDisplayName("Blacksmith"), 12)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  21 */       return new VillageStructureBlacksmith(w, v, i);
/*     */     }
/*     */   },
/*  24 */   MINESHAFT((new ItemStack((Item)ModItems.structureMineshaft)).setStackDisplayName("Mineshaft"), 0)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  27 */       return new VillageStructureMineshaft(w, v, i);
/*     */     }
/*     */   },
/*  30 */   TOWNHALL((new ItemStack((Item)ModItems.structureTownHall)).setStackDisplayName("Town Hall"), 12)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  33 */       return new VillageStructureTownHall(w, v, i);
/*     */     }
/*     */   },
/*  36 */   COW_PEN((new ItemStack((Item)ModItems.structureCowPen)).setStackDisplayName("Cow Pen"), 0)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  39 */       return new VillageStructureCowPen(w, v, i);
/*     */     }
/*     */   },
/*  42 */   SHEEP_PEN((new ItemStack((Item)ModItems.structureSheepPen)).setStackDisplayName("Sheep Pen"), 0)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  45 */       return new VillageStructureSheepPen(w, v, i);
/*     */     }
/*     */   },
/*  48 */   PIG_PEN((new ItemStack((Item)ModItems.structurePigPen)).setStackDisplayName("Pig Pen"), 0)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  51 */       return new VillageStructurePigPen(w, v, i);
/*     */     }
/*     */   },
/*  54 */   CHICKEN_COOP((new ItemStack((Item)ModItems.structureChickenCoop)).setStackDisplayName("Chicken Coop"), 0)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  57 */       return new VillageStructureChickenCoop(w, v, i);
/*     */     }
/*     */   },
/*  60 */   BUTCHER((new ItemStack((Item)ModItems.structureButcher)).setStackDisplayName("Butcher"), 12)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  63 */       return new VillageStructureButcher(w, v, i);
/*     */     }
/*     */   },
/*  66 */   TAVERN((new ItemStack((Item)ModItems.structureTavern)).setStackDisplayName("Tavern"), 0)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  69 */       return new VillageStructureTavern(w, v, i);
/*     */     }
/*     */   },
/*  72 */   LIBRARY((new ItemStack((Item)ModItems.structureLibrary)).setStackDisplayName("Library"), 12)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  75 */       return new VillageStructureLibrary(w, v, i);
/*     */     }
/*     */   },
/*  78 */   SCHOOL((new ItemStack((Item)ModItems.structureSchool)).setStackDisplayName("School"), 12)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  81 */       return new VillageStructureSchool(w, v, i);
/*     */     }
/*     */   },
/*  84 */   HOME2((new ItemStack((Item)ModItems.structureHome2)).setStackDisplayName("Home2"), 12)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  87 */       return new VillageStructureHome(w, v, i, HOME2, "Home", 2);
/*     */     }
/*     */   },
/*  90 */   HOME4((new ItemStack((Item)ModItems.structureHome4)).setStackDisplayName("Home4"), 12)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  93 */       return new VillageStructureHome(w, v, i, HOME4, "Home", 4);
/*     */     }
/*     */   },
/*  96 */   HOME6((new ItemStack((Item)ModItems.structureHome6)).setStackDisplayName("Home6"), 12)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/*  99 */       return new VillageStructureHome(w, v, i, HOME6, "Home", 6);
/*     */     }
/*     */   },
/* 102 */   GUARD_POST((new ItemStack((Item)ModItems.structureGuardPost)).setStackDisplayName("Guard Post"), 0)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/* 105 */       return new VillageStructureGuardPost(w, v, i);
/*     */     }
/*     */   },
/* 108 */   MERCHANT_STALL((new ItemStack((Item)ModItems.structureMerchantStall)).setStackDisplayName("Merchant Stall"), 0)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/* 111 */       return new VillageStructureMerchantStall(w, v, i);
/*     */     }
/*     */   },
/* 114 */   BARRACKS((new ItemStack((Item)ModItems.structureBarracks)).setStackDisplayName("Barracks"), 12)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/* 117 */       return new VillageStructureBarracks(w, v, i);
/*     */     }
/*     */   },
/* 120 */   KITCHEN((new ItemStack((Item)ModItems.structureKitchen)).setStackDisplayName("Kitchen"), 12)
/*     */   {
/*     */     public VillageStructure create(World w, Village v, EntityItemFrame i) {
/* 123 */       return new VillageStructureKitchen(w, v, i);
/*     */     }
/*     */   };
/*     */   
/*     */   public boolean isHome() {
/* 128 */     return (this == HOME2 || this == HOME4 || this == HOME6);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final String TOWN_HALL_NAME = "Town Hall";
/*     */   public final ItemStack itemStack;
/*     */   public final int tilesPerVillager;
/*     */   
/*     */   VillageStructureType(ItemStack i, int tilesPer) {
/* 137 */     this.itemStack = i;
/* 138 */     this.tilesPerVillager = tilesPer;
/*     */   }
/*     */   
/*     */   public boolean isItemEqual(ItemStack i) {
/* 142 */     return this.itemStack.isItemEqual(i);
/*     */   }
/*     */   
/*     */   public abstract VillageStructure create(World paramWorld, Village paramVillage, EntityItemFrame paramEntityItemFrame);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */