/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIEatGoldenApple
/*     */   extends EntityAIBase
/*     */ {
/*     */   private EntityVillagerTek villager;
/*     */   private ItemStack foodItem;
/*  24 */   private int eatTime = 0;
/*     */ 
/*     */   
/*     */   public EntityAIEatGoldenApple(EntityVillagerTek v) {
/*  28 */     this.villager = v;
/*  29 */     setMutexBits(1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/*  35 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  40 */     if (this.villager.isAITick() && this.villager.getHealth() < this.villager.getMaxHealth() / 2.0F && this.villager.isAIFilterEnabled("eat_golden_apple")) {
/*  41 */       this.foodItem = this.villager.getInventory().getItem(p -> Integer.valueOf((p.getItem() == Items.GOLDEN_APPLE && ModItems.isTaggedItem(p, ItemTagType.VILLAGER)) ? 1 : 0));
/*  42 */       if (!this.foodItem.isEmpty()) {
/*  43 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  47 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  53 */     startEat();
/*  54 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  60 */     if (this.eatTime >= 0) {
/*  61 */       return true;
/*     */     }
/*  63 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  69 */     this.eatTime--;
/*  70 */     if (this.eatTime == 0 && 
/*  71 */       !this.villager.getInventory().removeItems(p -> ItemStack.areItemStacksEqual(p, this.foodItem), 1).isEmpty()) {
/*  72 */       this.foodItem.shrink(1);
/*  73 */       this.villager.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 1));
/*  74 */       this.villager.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 0));
/*     */     } 
/*     */ 
/*     */     
/*  78 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   private void startEat() {
/*  83 */     this.eatTime = 80;
/*  84 */     this.villager.getNavigator().clearPath();
/*  85 */     this.villager.equipActionItem(this.foodItem);
/*  86 */     this.villager.playServerAnimation("villager_eat");
/*     */   }
/*     */ 
/*     */   
/*     */   private void stopEat() {
/*  91 */     this.villager.unequipActionItem(this.foodItem);
/*  92 */     this.villager.stopServerAnimation("villager_eat");
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/*  97 */     super.resetTask();
/*  98 */     stopEat();
/*  99 */     this.foodItem = null;
/* 100 */     this.eatTime = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIEatGoldenApple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */