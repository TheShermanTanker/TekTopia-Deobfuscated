/*     */ package net.tangotek.tektopia.caps;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.block.BlockPlanks;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.capabilities.Capability;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.economy.ItemEconomy;
/*     */ import net.tangotek.tektopia.economy.ItemValue;
/*     */ 
/*     */ public class VillageData
/*     */   implements IVillageData, Capability.IStorage<IVillageData>
/*     */ {
/*  30 */   private long childSpawnTime = 0L;
/*  31 */   private int totalProfessionSales = 0;
/*     */   
/*     */   private boolean checkedMerchants = false;
/*     */   
/*     */   private boolean checkedNomads = false;
/*     */   private boolean startingGifts = false;
/*     */   private boolean isEmpty = true;
/*  38 */   protected ItemEconomy economy = new ItemEconomy();
/*  39 */   private UUID uuid = UUID.randomUUID();
/*     */ 
/*     */ 
/*     */   
/*     */   public void initEconomy() {
/*  44 */     if (this.economy.hasItems()) {
/*     */       return;
/*     */     }
/*  47 */     this.economy.addItem(new ItemValue(new ItemStack(Blocks.LOG, 64, BlockPlanks.EnumType.JUNGLE.getMetadata()), 4, 22, ProfessionType.LUMBERJACK));
/*  48 */     this.economy.addItem(new ItemValue(new ItemStack(Blocks.LOG, 64, BlockPlanks.EnumType.BIRCH.getMetadata()), 4, 22, ProfessionType.LUMBERJACK));
/*  49 */     this.economy.addItem(new ItemValue(new ItemStack(Blocks.LOG, 64, BlockPlanks.EnumType.OAK.getMetadata()), 4, 22, ProfessionType.LUMBERJACK));
/*  50 */     this.economy.addItem(new ItemValue(new ItemStack(Blocks.LOG, 64, BlockPlanks.EnumType.SPRUCE.getMetadata()), 4, 22, ProfessionType.LUMBERJACK));
/*     */     
/*  52 */     this.economy.addItem(new ItemValue(new ItemStack(Items.WHEAT, 64), 4, 20, ProfessionType.FARMER));
/*  53 */     this.economy.addItem(new ItemValue(new ItemStack(Items.POTATO, 64), 4, 20, ProfessionType.FARMER));
/*  54 */     this.economy.addItem(new ItemValue(new ItemStack(Items.BEETROOT, 64), 4, 20, ProfessionType.FARMER));
/*  55 */     this.economy.addItem(new ItemValue(new ItemStack(Items.CARROT, 64), 4, 20, ProfessionType.FARMER));
/*     */ 
/*     */     
/*  58 */     this.economy.addItem(new ItemValue(new ItemStack(Items.BREAD, 16), 8, 10, ProfessionType.CHEF));
/*     */ 
/*     */     
/*  61 */     this.economy.addItem(new ItemValue(new ItemStack(Items.CAKE, 1), 4, 5, ProfessionType.CHEF));
/*     */     
/*  63 */     this.economy.addItem(new ItemValue(new ItemStack(Items.COOKED_PORKCHOP, 32), 10, 15, ProfessionType.CHEF));
/*  64 */     this.economy.addItem(new ItemValue(new ItemStack(Items.COOKED_BEEF, 32), 10, 15, ProfessionType.CHEF));
/*  65 */     this.economy.addItem(new ItemValue(new ItemStack(Items.COOKED_CHICKEN, 32), 10, 15, ProfessionType.CHEF));
/*  66 */     this.economy.addItem(new ItemValue(new ItemStack(Items.COOKED_MUTTON, 32), 10, 15, ProfessionType.CHEF));
/*  67 */     this.economy.addItem(new ItemValue(new ItemStack(Items.BAKED_POTATO, 32), 4, 15, ProfessionType.CHEF));
/*     */     
/*  69 */     this.economy.addItem(new ItemValue(new ItemStack((Item)Items.LEATHER_HELMET, 1), 2, 10, ProfessionType.BUTCHER));
/*  70 */     this.economy.addItem(new ItemValue(new ItemStack((Item)Items.LEATHER_CHESTPLATE, 1), 3, 10, ProfessionType.BUTCHER));
/*  71 */     this.economy.addItem(new ItemValue(new ItemStack((Item)Items.LEATHER_LEGGINGS, 1), 3, 10, ProfessionType.BUTCHER));
/*  72 */     this.economy.addItem(new ItemValue(new ItemStack((Item)Items.LEATHER_BOOTS, 1), 2, 10, ProfessionType.BUTCHER));
/*     */     
/*  74 */     this.economy.addItem(new ItemValue(new ItemStack(Items.EGG, 16), 2, 20, ProfessionType.RANCHER));
/*  75 */     this.economy.addItem(new ItemValue(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 64), 7, 20, ProfessionType.RANCHER));
/*     */     
/*  77 */     this.economy.addItem(new ItemValue(new ItemStack(Items.REDSTONE, 64), 5, 20, ProfessionType.MINER));
/*  78 */     this.economy.addItem(new ItemValue(new ItemStack(Items.DIAMOND, 4), 12, 10, ProfessionType.MINER));
/*  79 */     this.economy.addItem(new ItemValue(new ItemStack(Items.DYE, 16, EnumDyeColor.BLUE.getDyeDamage()), 5, 15, ProfessionType.MINER));
/*     */ 
/*     */     
/*  82 */     this.economy.addItem(new ItemValue(new ItemStack(Items.GOLD_INGOT, 16), 12, 10, ProfessionType.BLACKSMITH));
/*  83 */     this.economy.addItem(new ItemValue(new ItemStack(Items.IRON_PICKAXE, 1), 5, 5, ProfessionType.BLACKSMITH));
/*  84 */     this.economy.addItem(new ItemValue(new ItemStack(Items.IRON_AXE, 1), 5, 5, ProfessionType.BLACKSMITH));
/*  85 */     this.economy.addItem(new ItemValue(new ItemStack(Items.IRON_SWORD, 1), 4, 5, ProfessionType.BLACKSMITH));
/*  86 */     this.economy.addItem(new ItemValue(new ItemStack((Item)Items.IRON_CHESTPLATE, 1), 10, 5, ProfessionType.BLACKSMITH));
/*  87 */     this.economy.addItem(new ItemValue(new ItemStack((Item)Items.IRON_LEGGINGS, 1), 9, 5, ProfessionType.BLACKSMITH));
/*  88 */     this.economy.addItem(new ItemValue(new ItemStack((Item)Items.IRON_HELMET, 1), 7, 5, ProfessionType.BLACKSMITH));
/*  89 */     this.economy.addItem(new ItemValue(new ItemStack((Item)Items.IRON_BOOTS, 1), 6, 5, ProfessionType.BLACKSMITH));
/*  90 */     this.economy.addItem(new ItemValue(new ItemStack(Items.DIAMOND_PICKAXE, 1), 10, 5, ProfessionType.BLACKSMITH));
/*  91 */     this.economy.addItem(new ItemValue(new ItemStack(Items.DIAMOND_AXE, 1), 10, 5, ProfessionType.BLACKSMITH));
/*  92 */     this.economy.addItem(new ItemValue(new ItemStack(Items.DIAMOND_SWORD, 1), 8, 5, ProfessionType.BLACKSMITH));
/*  93 */     this.economy.addItem(new ItemValue(new ItemStack((Item)Items.DIAMOND_CHESTPLATE, 1), 20, 5, ProfessionType.BLACKSMITH));
/*  94 */     this.economy.addItem(new ItemValue(new ItemStack((Item)Items.DIAMOND_LEGGINGS, 1), 18, 5, ProfessionType.BLACKSMITH));
/*  95 */     this.economy.addItem(new ItemValue(new ItemStack((Item)Items.DIAMOND_HELMET, 1), 14, 5, ProfessionType.BLACKSMITH));
/*  96 */     this.economy.addItem(new ItemValue(new ItemStack((Item)Items.DIAMOND_BOOTS, 1), 12, 5, ProfessionType.BLACKSMITH));
/*     */     
/*  98 */     this.economy.addItem(new ItemValue(new ItemStack(Items.PAPER, 16), 3, 20, ProfessionType.ENCHANTER));
/*  99 */     this.economy.addItem(new ItemValue(new ItemStack(Items.BOOK, 8), 8, 20, ProfessionType.ENCHANTER));
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
/*     */   public ItemEconomy getEconomy() {
/* 116 */     return this.economy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNomadsCheckedToday(boolean checked) {
/* 121 */     this.checkedNomads = checked;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getNomadsCheckedToday() {
/* 126 */     return this.checkedNomads;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMerchantCheckedToday(boolean checked) {
/* 131 */     this.checkedMerchants = checked;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getMerchantCheckedToday() {
/* 136 */     return this.checkedMerchants;
/*     */   }
/*     */ 
/*     */   
/*     */   public UUID getUUID() {
/* 141 */     return this.uuid;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChildReady(long totalWorldTime) {
/* 146 */     return (totalWorldTime > this.childSpawnTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public void childSpawned(World w) {
/* 151 */     this.childSpawnTime = w.getTotalWorldTime() + MathHelper.getInt(w.rand, 20000, 30000);
/*     */   }
/*     */ 
/*     */   
/*     */   public void incrementProfessionSales() {
/* 156 */     this.totalProfessionSales++;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getProfessionSales() {
/* 161 */     return this.totalProfessionSales;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean completedStartingGifts() {
/* 166 */     return this.startingGifts;
/*     */   }
/*     */ 
/*     */   
/*     */   public void skipStartingGifts() {
/* 171 */     this.startingGifts = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void executeStartingGifts(World world, Village village, BlockPos pos) {
/* 176 */     this.startingGifts = true;
/*     */     
/* 178 */     world.setBlockState(pos, Blocks.CHEST.getDefaultState());
/* 179 */     TileEntity tileEntity = world.getTileEntity(pos);
/* 180 */     if (tileEntity instanceof TileEntityChest) {
/* 181 */       TileEntityChest chest = (TileEntityChest)tileEntity;
/* 182 */       addGiftItem(chest, village, (Item)ModItems.structureStorage, 12);
/* 183 */       addGiftItem(chest, village, (Item)ModItems.structureHome2, 13);
/* 184 */       addGiftItem(chest, village, (Item)ModItems.itemFarmer, 14);
/* 185 */       addGiftItem(chest, village, (Item)ModItems.itemLumberjack, 15);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addGiftItem(TileEntityChest chest, Village village, Item item, int slot) {
/* 190 */     ItemStack itemStack = ModItems.createTaggedItem(item, ItemTagType.VILLAGER);
/* 191 */     ModItems.bindItemToVillage(itemStack, village);
/* 192 */     chest.setInventorySlotContents(slot, itemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeNBT(NBTTagCompound compound) {
/* 197 */     compound.setUniqueId("uuid", this.uuid);
/*     */     
/* 199 */     compound.setLong("childTime", this.childSpawnTime);
/* 200 */     compound.setBoolean("checkedMerchants", this.checkedMerchants);
/* 201 */     compound.setBoolean("checkedNomads", this.checkedNomads);
/* 202 */     compound.setInteger("totalProfessionSales", this.totalProfessionSales);
/* 203 */     compound.setBoolean("startingGifts", this.startingGifts);
/*     */ 
/*     */     
/* 206 */     if (this.economy != null) {
/* 207 */       this.economy.writeNBT(compound);
/*     */     }
/*     */   }
/*     */   
/*     */   public void readNBT(NBTTagCompound compound) {
/* 212 */     this.uuid = compound.getUniqueId("uuid");
/* 213 */     if (this.uuid.getLeastSignificantBits() == 0L && this.uuid.getMostSignificantBits() == 0L) {
/* 214 */       this.uuid = UUID.randomUUID();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 220 */     this.childSpawnTime = compound.getLong("childTime");
/* 221 */     this.checkedMerchants = compound.getBoolean("checkedMerchants");
/* 222 */     this.checkedNomads = compound.getBoolean("checkedNomads");
/* 223 */     this.totalProfessionSales = compound.getInteger("totalProfessionSales");
/* 224 */     this.startingGifts = compound.getBoolean("startingGifts");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     if (compound.hasKey("SalesHistory")) {
/* 230 */       initEconomy();
/* 231 */       getEconomy().readNBT(compound);
/*     */     } 
/*     */     
/* 234 */     this.isEmpty = false;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 238 */     return this.isEmpty;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public NBTBase writeNBT(Capability<IVillageData> capability, IVillageData instance, EnumFacing side) {
/* 244 */     NBTTagCompound compound = new NBTTagCompound();
/* 245 */     instance.writeNBT(compound);
/* 246 */     return (NBTBase)compound;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readNBT(Capability<IVillageData> capability, IVillageData instance, EnumFacing side, NBTBase nbt) {
/* 251 */     if (nbt instanceof NBTTagCompound) {
/* 252 */       NBTTagCompound compound = (NBTTagCompound)nbt;
/* 253 */       instance.readNBT(compound);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\caps\VillageData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */