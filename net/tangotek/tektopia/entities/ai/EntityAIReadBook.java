/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityAIReadBook
/*     */   extends EntityAIMoveToBlock {
/*     */   private boolean active = false;
/*  16 */   private int readTime = 0;
/*  17 */   VillageStructure library = null;
/*     */   
/*     */   protected final EntityVillagerTek villager;
/*     */   private static final int READ_TIME = 200;
/*     */   
/*     */   public EntityAIReadBook(EntityVillagerTek entityIn) {
/*  23 */     super((EntityVillageNavigator)entityIn);
/*  24 */     this.villager = entityIn;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  29 */     this.library = this.villager.getVillage().getNearestStructure(VillageStructureType.LIBRARY, this.villager.getPosition());
/*  30 */     if (this.library != null) {
/*  31 */       BlockPos bookPos = this.library.getUnoccupiedSpecialBlock(Blocks.BOOKSHELF);
/*  32 */       if (bookPos != null) {
/*  33 */         return bookPos;
/*     */       }
/*     */ 
/*     */       
/*  37 */       this.villager.setThought(EntityVillagerTek.VillagerThought.BOOKSHELF);
/*     */     } 
/*     */ 
/*     */     
/*  41 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  47 */     if (this.villager.isAITick() && this.villager.hasVillage() && this.villager.isLearningTime() && 
/*  48 */       this.villager.getIntelligence() < this.villager.getMaxIntelligence()) {
/*  49 */       return super.shouldExecute();
/*     */     }
/*     */ 
/*     */     
/*  53 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  58 */     this.library.occupySpecialBlock(this.destinationPos);
/*  59 */     this.active = true;
/*  60 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  65 */     return this.active;
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/*  70 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  75 */     super.updateTask();
/*  76 */     if (this.readTime > 0) {
/*     */       
/*  78 */       if (this.readTime == 190) {
/*  79 */         this.villager.equipActionItem(new ItemStack(Items.BOOK));
/*     */       }
/*  81 */       if (this.villager.getRNG().nextInt(400) == 0) {
/*  82 */         this.villager.modifyHappy(2);
/*     */       }
/*     */ 
/*     */       
/*  86 */       int extra = (int)MathHelper.clampedLerp(0.0D, 3000.0D, this.villager.getIntelligence() / 100.0D);
/*     */       
/*  88 */       if (this.villager.getRNG().nextInt(500 + extra) == 0) {
/*  89 */         this.villager.addIntelligence(1);
/*     */       }
/*     */       
/*  92 */       this.readTime--;
/*  93 */       if (this.readTime == 10) {
/*  94 */         stopReading();
/*     */       }
/*  96 */       else if (this.readTime <= 0) {
/*  97 */         this.active = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStuck() {
/* 104 */     this.active = false;
/* 105 */     super.onStuck();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 110 */     startReading();
/* 111 */     super.onArrival();
/*     */   }
/*     */ 
/*     */   
/*     */   private void startReading() {
/* 116 */     this.readTime = 200;
/* 117 */     this.villager.getNavigator().clearPath();
/* 118 */     this.villager.playServerAnimation("villager_read");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void stopReading() {
/* 124 */     this.villager.stopServerAnimation("villager_read");
/* 125 */     this.villager.unequipActionItem();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 130 */     this.library.vacateSpecialBlock(this.destinationPos);
/* 131 */     this.active = false;
/* 132 */     this.readTime = 0;
/* 133 */     stopReading();
/* 134 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIReadBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */