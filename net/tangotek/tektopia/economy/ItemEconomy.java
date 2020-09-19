/*     */ package net.tangotek.tektopia.economy;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Random;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.village.MerchantRecipe;
/*     */ import net.minecraft.village.MerchantRecipeList;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class ItemEconomy {
/*  21 */   private Random rnd = new Random();
/*  22 */   protected Map<String, ItemValue> items = new HashMap<>();
/*  23 */   protected MerchantRecipeList merchantList = new MerchantRecipeList();
/*     */   private boolean itemsDirty = true;
/*  25 */   private Queue<ItemValue> salesHistory = new LinkedList<>();
/*  26 */   private float totalMarketValue = 0.0F;
/*     */   private float totalSalesAppearanceWeight;
/*     */   private int tradesAvailableForProfs;
/*  29 */   private Set<ProfessionType> profsInVillage = new HashSet<>();
/*     */ 
/*     */   
/*     */   static final int MAX_ITEM_SALES = 5;
/*     */ 
/*     */   
/*     */   static final int ITEMS_FOR_SALE = 8;
/*     */ 
/*     */   
/*     */   public void addItem(ItemValue itemValue) {
/*  39 */     ItemValue existingValue = this.items.put(itemValue.getName(), itemValue);
/*  40 */     if (existingValue != null) {
/*  41 */       throw new InvalidParameterException("Duplicate marketId in economy: " + itemValue.getName());
/*     */     }
/*  43 */     this.totalMarketValue += itemValue.getBaseValue();
/*  44 */     this.totalSalesAppearanceWeight += itemValue.getAppearanceWeight();
/*  45 */     this.itemsDirty = true;
/*     */   }
/*     */   
/*     */   public boolean hasItems() {
/*  49 */     return !this.items.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public void refreshValues(Village village) {
/*  54 */     this.totalSalesAppearanceWeight = 0.0F;
/*  55 */     this.tradesAvailableForProfs = 0;
/*  56 */     this.items.values().forEach(iv -> iv.reset());
/*     */ 
/*     */     
/*  59 */     this.profsInVillage.clear();
/*  60 */     List<EntityVillagerTek> villagers = village.getWorld().getEntitiesWithinAABB(EntityVillagerTek.class, village.getAABB().grow(40.0D));
/*  61 */     for (EntityVillagerTek v : villagers) {
/*  62 */       this.profsInVillage.add(v.getProfessionType());
/*     */     }
/*     */     
/*  65 */     if (this.profsInVillage.isEmpty()) {
/*  66 */       boolean bool = true;
/*     */     }
/*     */ 
/*     */     
/*  70 */     int historyIndex = 1;
/*  71 */     for (ItemValue iv : this.salesHistory) {
/*  72 */       float reduction = iv.markDown(historyIndex / this.salesHistory.size());
/*  73 */       for (ItemValue iv2 : this.items.values()) {
/*  74 */         if (iv2 != iv)
/*     */         {
/*  76 */           iv2.markUp(iv2.getBaseValue() * reduction / (this.totalMarketValue - iv.getBaseValue()));
/*     */         }
/*     */       } 
/*  79 */       historyIndex++;
/*     */     } 
/*     */     
/*  82 */     for (ItemValue iv : this.items.values()) {
/*  83 */       this.totalSalesAppearanceWeight += iv.getAppearanceWeight();
/*  84 */       if (this.profsInVillage.contains(iv.getRequiredProfession())) {
/*  85 */         this.tradesAvailableForProfs++;
/*     */       }
/*     */     } 
/*  88 */     this.itemsDirty = false;
/*  89 */     this.merchantList = null;
/*     */   }
/*     */   
/*     */   public MerchantRecipeList getMerchantList(Village v, int stallLevel) {
/*  93 */     if (this.itemsDirty) {
/*  94 */       refreshValues(v);
/*     */     }
/*  96 */     if (this.merchantList == null) {
/*  97 */       v.debugOut("Generating Merchant List");
/*  98 */       this.merchantList = generateMerchantList(stallLevel);
/*     */     } 
/*     */     
/* 101 */     return this.merchantList;
/*     */   }
/*     */   
/*     */   private int getHistorySize(Village village) {
/* 105 */     return (int)MathHelper.clampedLerp(30.0D, 50.0D, village.getResidentCount() / 100.0D);
/*     */   }
/*     */   
/*     */   public void sellItem(MerchantRecipe recipe, Village village) {
/* 109 */     ItemValue itemValue = this.items.get(ItemValue.getName(recipe.getItemToBuy()));
/* 110 */     if (itemValue != null) {
/* 111 */       this.salesHistory.add(itemValue);
/* 112 */       if (this.salesHistory.size() > getHistorySize(village)) {
/* 113 */         this.salesHistory.poll();
/*     */       }
/* 115 */       this.itemsDirty = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MerchantRecipe createFoodTrade() {
/* 122 */     List<MerchantRecipe> tempList = new ArrayList<>();
/* 123 */     tempList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2), ItemStack.EMPTY, ModItems.createTaggedItem(Items.BAKED_POTATO, 15, ItemTagType.VILLAGER), 0, 1));
/* 124 */     tempList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2), ItemStack.EMPTY, ModItems.createTaggedItem(Items.COOKED_BEEF, 8, ItemTagType.VILLAGER), 0, 1));
/* 125 */     tempList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2), ItemStack.EMPTY, ModItems.createTaggedItem(Items.PUMPKIN_PIE, 10, ItemTagType.VILLAGER), 0, 1));
/* 126 */     tempList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2), ItemStack.EMPTY, ModItems.createTaggedItem(Items.COOKIE, 15, ItemTagType.VILLAGER), 0, 1));
/* 127 */     tempList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2), ItemStack.EMPTY, ModItems.createTaggedItem(Items.GOLDEN_CARROT, 6, ItemTagType.VILLAGER), 0, 1));
/*     */     
/* 129 */     return tempList.get(this.rnd.nextInt(tempList.size()));
/*     */   }
/*     */   
/*     */   private MerchantRecipeList generateMerchantList(int stallLevel) {
/* 133 */     float totalWeight = this.totalSalesAppearanceWeight;
/* 134 */     MerchantRecipeList outList = new MerchantRecipeList();
/*     */     
/* 136 */     if (this.salesHistory.size() < this.items.size())
/*     */     {
/* 138 */       outList.add(createFoodTrade());
/*     */     }
/*     */     
/* 141 */     List<ItemValue> workItems = new LinkedList<>(this.items.values());
/* 142 */     Collections.shuffle(workItems);
/* 143 */     int itemsForSale = 0;
/* 144 */     int MAX_TRADES = 8 + stallLevel * 2;
/* 145 */     while (itemsForSale < MAX_TRADES && !workItems.isEmpty()) {
/* 146 */       float appearanceRoll = this.rnd.nextFloat() * totalWeight;
/* 147 */       float count = 0.0F;
/* 148 */       ListIterator<ItemValue> itr = workItems.listIterator();
/* 149 */       label21: while (itr.hasNext()) {
/* 150 */         ItemValue iv = itr.next();
/* 151 */         count += iv.getAppearanceWeight();
/* 152 */         if (!itr.hasNext()) {
/*     */           
/* 154 */           System.out.println("Merchant economy count/weight mismatch");
/* 155 */           count = appearanceRoll + 1.0F;
/*     */         } 
/*     */         
/* 158 */         if (appearanceRoll < count) {
/*     */ 
/*     */           
/* 161 */           if (iv.isForSale()) { if (this.profsInVillage.contains(iv.getRequiredProfession())) {
/* 162 */               outList.add(new MerchantRecipe(ModItems.makeTaggedItem(iv.getItemStack(), ItemTagType.VILLAGER), ItemStack.EMPTY, new ItemStack(Items.EMERALD, iv.getCurrentValue()), 0, 5));
/* 163 */               itemsForSale++; break label21;
/*     */             }  break label21; }
/*     */           
/* 166 */           totalWeight -= iv.getAppearanceWeight();
/* 167 */           itr.remove();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 173 */     return outList;
/*     */   }
/*     */   
/*     */   public void report(StringBuilder builder) {
/* 177 */     builder.append("========== Item Economy Report ========== [" + this.totalMarketValue + "]\n");
/* 178 */     builder.append("    --History--\n");
/* 179 */     this.salesHistory.forEach(iv -> builder.append("       " + iv.getItemStack() + "\n"));
/*     */     
/* 181 */     builder.append("    --Item Values--\n");
/* 182 */     int sumValues = 0;
/* 183 */     for (ItemValue iv : this.items.values()) {
/* 184 */       builder.append("       " + iv + "\n");
/* 185 */       sumValues += iv.getCurrentValue();
/*     */     } 
/* 187 */     builder.append("     Current Value Sum: " + sumValues + "\n");
/*     */   }
/*     */   
/*     */   public int getSalesHistorySize() {
/* 191 */     return this.salesHistory.size();
/*     */   }
/*     */   
/*     */   public void writeNBT(NBTTagCompound compound) {
/* 195 */     NBTTagList historyList = new NBTTagList();
/* 196 */     for (ItemValue iv : this.salesHistory) {
/* 197 */       historyList.appendTag((NBTBase)new NBTTagString(iv.getName()));
/*     */     }
/* 199 */     compound.setTag("SalesHistory", (NBTBase)historyList);
/*     */   }
/*     */   
/*     */   public void readNBT(NBTTagCompound compound) {
/* 203 */     this.salesHistory.clear();
/* 204 */     NBTTagList nbttaglist = compound.getTagList("SalesHistory", 8);
/* 205 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/* 207 */       String name = nbttaglist.getStringTagAt(i);
/* 208 */       ItemValue iv = this.items.get(name);
/* 209 */       if (iv != null)
/* 210 */         this.salesHistory.add(iv); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\economy\ItemEconomy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */