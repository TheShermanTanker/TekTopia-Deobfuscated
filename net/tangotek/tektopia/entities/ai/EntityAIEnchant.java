/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.enchantment.EnchantmentData;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemEnchantedBook;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityEnchanter;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.entities.crafting.Recipe;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIEnchant
/*     */   extends EntityAICraftItems
/*     */ {
/*     */   private EntityItem floatingItem;
/*     */   
/*     */   public EntityAIEnchant(EntityVillagerTek v, List<Recipe> recipes, String anim, ItemStack heldItem, int craftTime, VillageStructureType structureType, Block machineBlock, Predicate<EntityVillagerTek> shouldPred, int skillChance) {
/*  31 */     super(v, recipes, anim, heldItem, craftTime, structureType, machineBlock, shouldPred);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  37 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  43 */     super.updateTask();
/*     */     
/*  45 */     if (this.floatingItem != null) {
/*     */       
/*  47 */       this.floatingItem.prevRotationYaw = ++this.floatingItem.rotationYaw;
/*     */       
/*  49 */       this.floatingItem.motionY = 0.0D;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack craftItem(EntityVillagerTek villager) {
/*  55 */     ItemStack enchantedItem = super.craftItem(villager);
/*  56 */     if (enchantedItem != null && enchantedItem.isItemEnchanted()) {
/*  57 */       boolean wasVillagerItem = ModItems.isTaggedItem(enchantedItem, ItemTagType.VILLAGER);
/*  58 */       int enchantLevel = 5 + villager.getSkillLerp(ProfessionType.ENCHANTER, 1, 30);
/*  59 */       if (!wasVillagerItem) {
/*  60 */         enchantLevel = Math.max(1, enchantLevel / 5);
/*     */       }
/*  62 */       boolean isBook = false;
/*  63 */       List<EnchantmentData> list = new ArrayList<>();
/*     */ 
/*     */       
/*  66 */       enchantedItem = new ItemStack(enchantedItem.getItem(), 1, enchantedItem.getMetadata());
/*     */ 
/*     */       
/*  69 */       for (int i = 0; i < 10; i++) {
/*     */ 
/*     */         
/*  72 */         if (enchantedItem.getItem() == Items.ENCHANTED_BOOK) {
/*  73 */           list = EnchantmentHelper.buildEnchantmentList(villager.getRNG(), new ItemStack(Items.BOOK, 1, enchantedItem.getMetadata()), enchantLevel, true);
/*  74 */           isBook = true;
/*     */         } else {
/*  76 */           list = EnchantmentHelper.buildEnchantmentList(villager.getRNG(), enchantedItem, enchantLevel, true);
/*     */         } 
/*     */ 
/*     */         
/*  80 */         list.removeIf(e -> e.enchantment.isCurse());
/*     */ 
/*     */         
/*  83 */         if (!list.isEmpty()) {
/*     */           break;
/*     */         }
/*     */       } 
/*  87 */       for (EnchantmentData enchantmentdata : list) {
/*     */         
/*  89 */         if (isBook) {
/*     */           
/*  91 */           ItemEnchantedBook.addEnchantment(enchantedItem, enchantmentdata);
/*     */           
/*     */           continue;
/*     */         } 
/*  95 */         enchantedItem.addEnchantment(enchantmentdata.enchantment, enchantmentdata.enchantmentLevel);
/*     */       } 
/*     */ 
/*     */       
/*  99 */       if (wasVillagerItem)
/* 100 */         ModItems.makeTaggedItem(enchantedItem, ItemTagType.VILLAGER); 
/*     */     } 
/* 102 */     return enchantedItem;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/* 108 */     return super.shouldExecute();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 113 */     super.onArrival();
/* 114 */     clearFloater();
/*     */     
/* 116 */     this.floatingItem = new EntityItem(this.villager.world, this.destinationPos.getX() + 0.5D, this.destinationPos.getY() + 1.6D, this.destinationPos.getZ() + 0.5D, this.activeRecipe.getProduct().copy());
/* 117 */     this.floatingItem.setNoGravity(true);
/* 118 */     this.floatingItem.lifespan = 6000;
/* 119 */     this.floatingItem.setInfinitePickupDelay();
/* 120 */     this.floatingItem.motionX = 0.0D;
/* 121 */     this.floatingItem.motionY = 0.0D;
/* 122 */     this.floatingItem.motionZ = 0.0D;
/* 123 */     this.villager.addJob(new TickJob(16, 0, false, () -> {
/*     */             if (this.floatingItem != null)
/*     */               this.villager.world.spawnEntity((Entity)this.floatingItem); 
/*     */           }));
/*     */   }
/*     */   
/*     */   private void clearFloater() {
/* 130 */     if (this.floatingItem != null) {
/* 131 */       this.floatingItem.setDead();
/* 132 */       this.floatingItem = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startCraft() {
/* 138 */     super.startCraft();
/*     */     
/* 140 */     if (this.villager instanceof EntityEnchanter) {
/* 141 */       EntityEnchanter enchanter = (EntityEnchanter)this.villager;
/* 142 */       enchanter.setCasting(this.destinationPos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stopCraft() {
/* 148 */     super.stopCraft();
/*     */     
/* 150 */     if (this.villager instanceof EntityEnchanter) {
/* 151 */       EntityEnchanter enchanter = (EntityEnchanter)this.villager;
/* 152 */       enchanter.setCasting(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 159 */     clearFloater();
/* 160 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIEnchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */