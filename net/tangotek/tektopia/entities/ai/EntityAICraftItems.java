/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.entities.crafting.Recipe;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityAICraftItems extends EntityAIMoveToBlock {
/*     */   protected final EntityVillagerTek villager;
/*     */   private final List<Recipe> recipes;
/*  18 */   protected Recipe activeRecipe = null;
/*     */   
/*     */   private final String craftAnimation;
/*     */   
/*     */   private final ItemStack heldItem;
/*     */   
/*     */   private final int craftTime;
/*     */   protected int timeRemaining;
/*     */   private int currentIteration;
/*     */   private final VillageStructureType structureType;
/*     */   private VillageStructure structure;
/*     */   private final Block machineBlock;
/*     */   private final Predicate<EntityVillagerTek> shouldPred;
/*     */   
/*     */   public EntityAICraftItems(EntityVillagerTek v, List<Recipe> recipes, String anim, ItemStack heldItem, int craftTime, VillageStructureType structureType, Block machineBlock, Predicate<EntityVillagerTek> shouldPred) {
/*  33 */     super((EntityVillageNavigator)v);
/*  34 */     this.villager = v;
/*  35 */     this.shouldPred = shouldPred;
/*  36 */     this.recipes = recipes;
/*  37 */     this.craftAnimation = anim;
/*  38 */     this.heldItem = heldItem;
/*  39 */     this.craftTime = craftTime;
/*  40 */     this.timeRemaining = 0;
/*  41 */     this.currentIteration = 0;
/*  42 */     this.structureType = structureType;
/*  43 */     this.machineBlock = machineBlock;
/*  44 */     this.structure = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  49 */     this.structure = null;
/*  50 */     if (this.structureType == null)
/*  51 */       return this.villager.getPosition(); 
/*  52 */     if (this.villager.hasVillage()) {
/*     */       
/*  54 */       List<VillageStructure> structs = this.villager.getVillage().getStructures(this.structureType);
/*  55 */       Collections.shuffle(structs);
/*  56 */       for (VillageStructure str : structs) {
/*  57 */         if (this.machineBlock != null) {
/*  58 */           BlockPos machinePos = str.getUnoccupiedSpecialBlock(this.machineBlock);
/*  59 */           if (machinePos != null) {
/*  60 */             this.structure = str;
/*  61 */             return machinePos;
/*     */           }  continue;
/*     */         } 
/*  64 */         return str.getRandomFloorTile();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  74 */     if (this.villager.isAITick() && this.villager.hasVillage() && this.shouldPred.test(this.villager) && this.villager.getInventory().hasSlotFree()) {
/*  75 */       for (Recipe r : this.recipes) {
/*  76 */         if (r.shouldCraft(this.villager) && r.hasItems(this.villager) && 
/*  77 */           super.shouldExecute()) {
/*  78 */           this.activeRecipe = r;
/*  79 */           return true;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  91 */     if (this.machineBlock != null && this.structure != null) {
/*  92 */       this.structure.occupySpecialBlock(this.destinationPos);
/*     */     }
/*  94 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 100 */     if (this.timeRemaining >= 0) {
/* 101 */       return true;
/*     */     }
/* 103 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 108 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 114 */     this.timeRemaining--;
/*     */     
/* 116 */     if (this.timeRemaining > 0) {
/* 117 */       this.villager.faceLocation(this.destinationPos.getX(), this.destinationPos.getZ(), 30.0F);
/*     */     }
/* 119 */     if (this.timeRemaining == this.craftTime) {
/* 120 */       startCraft();
/*     */     }
/* 122 */     if (this.timeRemaining == 1) {
/* 123 */       this.currentIteration++;
/* 124 */       stopCraft();
/* 125 */       if (this.currentIteration >= this.activeRecipe.getAnimationIterations(this.villager)) {
/* 126 */         if (this.villager.getInventory().hasSlotFree()) {
/* 127 */           this.villager.modifyHunger(-this.activeRecipe.getAnimationIterations(this.villager));
/* 128 */           this.villager.throttledSadness(-3);
/*     */ 
/*     */           
/* 131 */           ItemStack craftedItem = craftItem(this.villager);
/* 132 */           if (craftedItem != null) {
/* 133 */             ItemStack itemStack = this.villager.getInventory().addItem(craftedItem);
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 138 */         this.timeRemaining = this.craftTime + 10;
/*     */       } 
/*     */     } 
/*     */     
/* 142 */     super.updateTask();
/*     */   }
/*     */   
/*     */   protected ItemStack craftItem(EntityVillagerTek villager) {
/* 146 */     return this.activeRecipe.craft(villager);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 152 */     this.currentIteration = 0;
/* 153 */     this.timeRemaining = this.craftTime + 20;
/* 154 */     super.onArrival();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startCraft() {
/* 159 */     this.villager.getNavigator().clearPath();
/* 160 */     if (this.heldItem != null) {
/* 161 */       this.villager.equipActionItem(this.heldItem);
/*     */     } else {
/*     */       
/* 164 */       this.villager.equipActionItem(this.activeRecipe.getProduct());
/*     */     } 
/*     */     
/* 167 */     if (this.craftAnimation != null) {
/* 168 */       this.villager.playServerAnimation(this.craftAnimation);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void stopCraft() {
/* 173 */     if (this.heldItem != null) {
/* 174 */       this.villager.unequipActionItem(this.heldItem);
/*     */     } else {
/*     */       
/* 177 */       this.villager.unequipActionItem(this.activeRecipe.getProduct());
/*     */     } 
/*     */     
/* 180 */     if (this.craftAnimation != null) {
/* 181 */       this.villager.stopServerAnimation(this.craftAnimation);
/*     */     }
/*     */   }
/*     */   
/*     */   public void resetTask() {
/* 186 */     super.resetTask();
/*     */     
/* 188 */     if (this.machineBlock != null && this.structure != null) {
/* 189 */       this.structure.vacateSpecialBlock(this.destinationPos);
/*     */     }
/* 191 */     stopCraft();
/* 192 */     this.activeRecipe = null;
/* 193 */     this.timeRemaining = 0;
/* 194 */     this.currentIteration = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAICraftItems.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */