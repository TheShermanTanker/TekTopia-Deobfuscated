/*     */ package net.tangotek.tektopia.entities.crafting;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.storage.VillagerInventory;
/*     */ 
/*     */ 
/*     */ public class Recipe
/*     */ {
/*     */   private final ProfessionType profession;
/*     */   private final int skillChance;
/*     */   private final ItemStack product;
/*     */   private final List<ItemStack> needs;
/*     */   private final int storageGoal;
/*     */   public final int idealCount;
/*     */   public final int limitCount;
/*     */   public final String aiFilter;
/*     */   public final Predicate<EntityVillagerTek> shouldCraft;
/*     */   private final Function<EntityVillagerTek, Integer> iterations;
/*     */   
/*     */   public Recipe(ProfessionType pt, String aiFilter, int skillChance, ItemStack itemProduct, List<ItemStack> needs, int idealCount, int limitCount, Function<EntityVillagerTek, Integer> animationIterations, int storageGoal) {
/*  28 */     this(pt, aiFilter, skillChance, itemProduct, needs, idealCount, limitCount, animationIterations, storageGoal, null);
/*     */   }
/*     */   
/*     */   public Recipe(ProfessionType pt, String aiFilter, int skillChance, ItemStack itemProduct, List<ItemStack> needs, int idealCount, int limitCount, Function<EntityVillagerTek, Integer> animationIterations, int storageGoal, Predicate<EntityVillagerTek> pred) {
/*  32 */     this.profession = pt;
/*  33 */     this.skillChance = skillChance;
/*  34 */     this.aiFilter = aiFilter;
/*  35 */     this.product = itemProduct;
/*  36 */     this.needs = needs;
/*  37 */     this.storageGoal = storageGoal;
/*     */     
/*  39 */     this.shouldCraft = pred;
/*  40 */     this.iterations = animationIterations;
/*  41 */     this.idealCount = idealCount;
/*  42 */     this.limitCount = limitCount;
/*     */   }
/*     */   
/*     */   public boolean hasItems(EntityVillagerTek villager) {
/*  46 */     for (ItemStack itemReq : this.needs) {
/*  47 */       int reqCount = villager.getInventory().getItemCount(p -> (p.getItem() == itemReq.getItem() && !p.isItemEnchanted()));
/*  48 */       if (reqCount < itemReq.getCount()) {
/*  49 */         return false;
/*     */       }
/*     */     } 
/*  52 */     return true;
/*     */   }
/*     */   
/*     */   public String getAiFilter() {
/*  56 */     return this.aiFilter;
/*     */   }
/*     */   
/*     */   public ItemStack craft(EntityVillagerTek villager) {
/*  60 */     boolean nonVillagerItems = false;
/*  61 */     for (ItemStack itemReq : this.needs) {
/*  62 */       List<ItemStack> items = villager.getInventory().removeItems(p -> (p.getItem() == itemReq.getItem()), itemReq.getCount());
/*  63 */       int total = VillagerInventory.countItems(items);
/*  64 */       if (total != itemReq.getCount()) {
/*  65 */         return null;
/*     */       }
/*  67 */       nonVillagerItems |= items.stream().anyMatch(itemStack -> !ModItems.isTaggedItem(itemStack, ItemTagType.VILLAGER));
/*     */     } 
/*     */     
/*  70 */     villager.tryAddSkill(this.profession, this.skillChance);
/*     */     
/*  72 */     villager.debugOut("has crafted: " + this.product.getItem().getTranslationKey());
/*     */     
/*  74 */     ItemStack result = this.product.copy();
/*  75 */     if (!nonVillagerItems) {
/*  76 */       ModItems.makeTaggedItem(result, ItemTagType.VILLAGER);
/*     */     }
/*  78 */     return result;
/*     */   }
/*     */   
/*     */   public ItemStack getProduct() {
/*  82 */     return this.product;
/*     */   }
/*     */   
/*     */   public Predicate<ItemStack> isNeed() {
/*  86 */     return p -> {
/*     */         for (ItemStack need : this.needs) {
/*     */           if (need.getItem() == p.getItem())
/*     */             return true; 
/*     */         } 
/*     */         return false;
/*     */       };
/*     */   }
/*     */   public List<ItemStack> getNeeds() {
/*  95 */     return this.needs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasPersonalGoal(EntityVillagerTek villager, ItemStack goalStack) {
/* 103 */     Predicate<ItemStack> pred = p -> (p.getItem() == goalStack.getItem());
/* 104 */     int count = villager.getInventory().getItemCount(pred);
/* 105 */     return (count >= goalStack.getCount());
/*     */   }
/*     */   
/*     */   public boolean shouldCraft(EntityVillagerTek villager) {
/* 109 */     if (!villager.hasVillage() || !villager.isAIFilterEnabled(this.aiFilter)) {
/* 110 */       return false;
/*     */     }
/* 112 */     int storageCount = villager.getVillage().getStorageCount(p -> (p.getItem() == getProduct().getItem() && p.isItemEnchanted() == getProduct().isItemEnchanted()));
/* 113 */     if (storageCount >= this.storageGoal) {
/* 114 */       return false;
/*     */     }
/* 116 */     if (this.shouldCraft != null && !this.shouldCraft.test(villager)) {
/* 117 */       return false;
/*     */     }
/* 119 */     return true;
/*     */   }
/*     */   public int getAnimationIterations(EntityVillagerTek v) {
/* 122 */     return ((Integer)this.iterations.apply(v)).intValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\crafting\Recipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */