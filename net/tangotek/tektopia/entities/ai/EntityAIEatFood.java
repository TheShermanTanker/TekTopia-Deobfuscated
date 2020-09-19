/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ 
/*     */ public class EntityAIEatFood
/*     */   extends EntityAIBase
/*     */ {
/*     */   private EntityVillagerTek villager;
/*     */   private ItemStack foodItem;
/*  20 */   private int eatTime = 0;
/*     */   
/*  22 */   public static final Map<Item, VillagerFood> villagerFood = new HashMap<>();
/*     */   
/*     */   public static class VillagerFood
/*     */   {
/*     */     private final int happy;
/*     */     private final int hunger;
/*     */     private final Item item;
/*     */     private final BiConsumer<EntityVillagerTek, ItemStack> postEat;
/*     */     
/*     */     public VillagerFood(Item item, int hunger, int happy) {
/*  32 */       this(item, hunger, happy, null);
/*     */     }
/*     */     
/*     */     public VillagerFood(Item item, int hunger, int happy, BiConsumer<EntityVillagerTek, ItemStack> post) {
/*  36 */       this.item = item;
/*  37 */       this.hunger = hunger;
/*  38 */       this.happy = happy;
/*  39 */       this.postEat = post;
/*     */     }
/*     */     
/*     */     public void eat(EntityVillagerTek v, ItemStack foodItem) {
/*  43 */       boolean isVFood = ModItems.isTaggedItem(foodItem, ItemTagType.VILLAGER);
/*     */       
/*  45 */       int hunger = getHunger(v);
/*  46 */       if (!isVFood) {
/*  47 */         hunger /= 2;
/*     */       }
/*  49 */       v.modifyHunger(hunger);
/*     */       
/*  51 */       if (isVFood) {
/*  52 */         int happy = getHappy(v);
/*  53 */         v.modifyHappy(happy);
/*     */       } 
/*     */       
/*  56 */       if (this.postEat != null) {
/*  57 */         this.postEat.accept(v, foodItem);
/*     */       }
/*  59 */       v.debugOut("Eating Food " + foodItem.getItem().getTranslationKey());
/*  60 */       v.addRecentEat(this.item);
/*     */     }
/*     */     
/*     */     public int getHappy(EntityVillagerTek villager) {
/*  64 */       int recentEatModifier = villager.getRecentEatModifier(this.item);
/*     */ 
/*     */ 
/*     */       
/*  68 */       return Math.max(this.happy + recentEatModifier, -3);
/*     */     }
/*     */     
/*     */     public int getHunger(EntityVillagerTek villager) {
/*  72 */       return this.hunger;
/*     */     }
/*     */     
/*     */     public Item getItem() {
/*  76 */       return this.item;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/*  82 */     BiConsumer<EntityVillagerTek, ItemStack> returnBowl = (v, i) -> {
/*     */         ItemStack bowl = new ItemStack(Items.BOWL);
/*     */         
/*     */         if (ModItems.isTaggedItem(i, ItemTagType.VILLAGER)) {
/*     */           ModItems.makeTaggedItem(bowl, ItemTagType.VILLAGER);
/*     */         }
/*     */         v.getInventory().addItem(bowl);
/*     */       };
/*  90 */     registerFood(Items.APPLE, 12, -1);
/*  91 */     registerFood(Items.BAKED_POTATO, 35, 1);
/*  92 */     registerFood(Items.BEETROOT, 7, -1);
/*  93 */     registerFood(Items.BEETROOT_SOUP, 50, 6, returnBowl);
/*  94 */     registerFood(Items.BREAD, 55, 4);
/*  95 */     registerFood(Items.CAKE, 7, 25);
/*  96 */     registerFood(Items.CARROT, 12, -1);
/*  97 */     registerFood(Items.COOKED_BEEF, 70, 14);
/*  98 */     registerFood(Items.COOKED_CHICKEN, 60, 6);
/*  99 */     registerFood(Items.COOKED_MUTTON, 66, 4);
/* 100 */     registerFood(Items.COOKED_PORKCHOP, 70, 14);
/* 101 */     registerFood(Items.COOKIE, 5, 16);
/* 102 */     registerFood(Items.GOLDEN_CARROT, 70, 20);
/* 103 */     registerFood(Items.MELON, 6, 3);
/* 104 */     registerFood(Items.MUSHROOM_STEW, 50, 4, returnBowl);
/* 105 */     registerFood(Items.POTATO, 7, -1);
/* 106 */     registerFood(Items.PUMPKIN_PIE, 35, 18);
/*     */   }
/*     */   
/*     */   private static void registerFood(Item item, int hunger, int happy) {
/* 110 */     registerFood(item, hunger, happy, null);
/*     */   }
/*     */   
/*     */   private static void registerFood(Item item, int hunger, int happy, BiConsumer<EntityVillagerTek, ItemStack> postEat) {
/* 114 */     VillagerFood food = new VillagerFood(item, hunger, happy, postEat);
/* 115 */     villagerFood.put(food.item, food);
/*     */   }
/*     */   
/*     */   public EntityAIEatFood(EntityVillagerTek v) {
/* 119 */     this.villager = v;
/* 120 */     setMutexBits(1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/* 131 */     if (this.villager.isAITick() && this.villager.isHungry() && !this.villager.isSleeping()) {
/* 132 */       this.foodItem = this.villager.getInventory().getItem(p -> Integer.valueOf(getFoodScore(p.getItem(), this.villager)));
/* 133 */       if (!this.foodItem.isEmpty()) {
/* 134 */         return true;
/*     */       }
/* 136 */       this.villager.setThought(EntityVillagerTek.VillagerThought.HUNGRY);
/*     */     } 
/*     */ 
/*     */     
/* 140 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getFoodScore(Item item, EntityVillagerTek v) {
/* 145 */     VillagerFood food = villagerFood.get(item);
/* 146 */     if (food != null) {
/* 147 */       return getFoodScore(food, v);
/*     */     }
/* 149 */     return -1;
/*     */   }
/*     */   
/*     */   public static int getFoodScore(VillagerFood food, EntityVillagerTek v) {
/* 153 */     if (v != null) {
/* 154 */       int happy = food.getHappy(v);
/* 155 */       int hunger = food.getHunger(v);
/* 156 */       if (v.getHunger() + hunger > v.getMaxHunger()) {
/* 157 */         hunger = 1;
/*     */       }
/*     */       
/* 160 */       if (v.getHappy() + happy > v.getMaxHappy()) {
/* 161 */         happy = 0;
/*     */       }
/*     */       
/* 164 */       int score = hunger;
/*     */       
/* 166 */       int happyPotential = happy * 5;
/* 167 */       float happyFactor = 1.0F;
/* 168 */       if (happyPotential > 0) {
/* 169 */         happyFactor = (v.getMaxHappy() - v.getHappy()) / v.getMaxHappy();
/*     */       }
/* 171 */       score += (int)(happyPotential * happyFactor);
/* 172 */       return Math.max(score, 1);
/*     */     } 
/*     */     
/* 175 */     return food.happy + food.hunger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/* 182 */     startEat();
/* 183 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 189 */     if (this.eatTime >= 0) {
/* 190 */       return true;
/*     */     }
/* 192 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 198 */     this.eatTime--;
/* 199 */     if (this.eatTime == 0 && 
/* 200 */       !this.villager.getInventory().removeItems(p -> ItemStack.areItemStacksEqual(p, this.foodItem), 1).isEmpty()) {
/*     */       
/* 202 */       VillagerFood food = villagerFood.get(this.foodItem.getItem());
/* 203 */       if (food != null) {
/* 204 */         food.eat(this.villager, this.foodItem);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 209 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   private void startEat() {
/* 214 */     this.eatTime = 80;
/* 215 */     this.villager.getNavigator().clearPath();
/* 216 */     this.villager.equipActionItem(this.foodItem);
/* 217 */     this.villager.playServerAnimation("villager_eat");
/*     */   }
/*     */ 
/*     */   
/*     */   private void stopEat() {
/* 222 */     this.villager.unequipActionItem(this.foodItem);
/* 223 */     this.villager.stopServerAnimation("villager_eat");
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 228 */     super.resetTask();
/* 229 */     stopEat();
/* 230 */     this.foodItem = null;
/* 231 */     this.eatTime = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIEatFood.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */