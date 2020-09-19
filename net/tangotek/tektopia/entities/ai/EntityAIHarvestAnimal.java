/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.EntityTagType;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModEntities;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureRancherPen;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityAIHarvestAnimal extends EntityAIFollow {
/*  28 */   private VillageStructureRancherPen rancherPen = null; private VillageStructureType penType;
/*     */   private ItemStack toolRequired;
/*     */   private ItemStack toolUsed;
/*  31 */   private EntityAnimal targetAnimal = null;
/*     */   private boolean active = false;
/*  33 */   private int harvestTime = 0;
/*     */   protected final EntityVillagerTek villager;
/*  35 */   public static long COW_MILK_TIME = 6000L;
/*     */   protected final Predicate<EntityVillagerTek> shouldPred;
/*     */   
/*     */   public EntityAIHarvestAnimal(EntityVillagerTek v, VillageStructureType penType, ItemStack toolRequired, Predicate<EntityVillagerTek> shouldPred) {
/*  39 */     super((EntityVillageNavigator)v);
/*  40 */     this.villager = v;
/*  41 */     this.penType = penType;
/*  42 */     this.toolRequired = toolRequired;
/*  43 */     this.toolUsed = null;
/*  44 */     this.shouldPred = shouldPred;
/*     */   }
/*     */ 
/*     */   
/*     */   protected EntityLivingBase getFollowTarget() {
/*  49 */     return (EntityLivingBase)this.targetAnimal;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  55 */     if (this.villager.isAITick() && this.villager.getVillage() != null && this.villager.isWorkTime() && this.shouldPred.test(this.villager)) {
/*  56 */       List<VillageStructure> structures = this.villager.getVillage().getStructures(this.penType);
/*  57 */       Collections.shuffle(structures);
/*  58 */       for (VillageStructure struct : structures) {
/*  59 */         this.rancherPen = (VillageStructureRancherPen)struct;
/*  60 */         List<EntityAnimal> animalList = this.rancherPen.getEntitiesInside(this.rancherPen.getAnimalClass());
/*  61 */         this.toolUsed = null;
/*     */ 
/*     */         
/*  64 */         if (!animalList.isEmpty()) {
/*  65 */           List<ItemStack> tools = this.villager.getInventory().getItems(p -> (p.getItem() == this.toolRequired.getItem()), 1);
/*  66 */           if (!tools.isEmpty()) {
/*  67 */             this.toolUsed = tools.get(0);
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/*  72 */         this.targetAnimal = null;
/*  73 */         double minDistance = Double.MAX_VALUE;
/*  74 */         for (EntityAnimal animal : animalList) {
/*  75 */           if (isAnimalHarvestable(animal) && !this.rancherPen.isAnimalScheduled(animal, VillageStructureRancherPen.AnimalScheduleType.HARVEST)) {
/*  76 */             double curDist = animal.getDistanceSq((Entity)this.villager);
/*  77 */             if (curDist < minDistance) {
/*  78 */               this.targetAnimal = animal;
/*  79 */               minDistance = curDist;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/*  84 */         if (this.targetAnimal != null) {
/*  85 */           if (this.toolUsed != null) {
/*  86 */             return super.shouldExecute();
/*     */           }
/*     */           
/*  89 */           this.villager.setThought(this.rancherPen.getNoHarvestThought());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  96 */     return false;
/*     */   }
/*     */   
/*     */   public void startExecuting() {
/* 100 */     this.active = true;
/* 101 */     this.villager.equipActionItem(this.toolUsed);
/* 102 */     this.rancherPen.scheduleAnimal(this.targetAnimal, VillageStructureRancherPen.AnimalScheduleType.HARVEST);
/* 103 */     super.startExecuting();
/*     */   }
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 107 */     return this.active;
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 112 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */   
/*     */   public void updateTask() {
/* 116 */     if (this.harvestTime > 0) {
/* 117 */       this.villager.getLookHelper().setLookPosition(this.destinationPos.getX(), this.destinationPos.getY(), this.destinationPos.getZ(), 30.0F, 30.0F);
/* 118 */       this.harvestTime--;
/*     */       
/* 120 */       if (this.harvestTime == 17) {
/* 121 */         this.villager.unequipActionItem(this.toolUsed);
/*     */       }
/* 123 */       if (this.harvestTime == 10) {
/* 124 */         harvestAnimal();
/*     */       }
/* 126 */       else if (this.harvestTime == 0) {
/* 127 */         this.active = false;
/*     */       } 
/*     */     } else {
/*     */       
/* 131 */       super.updateTask();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/* 137 */     return (this.harvestTime <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 142 */     startHarvesting();
/* 143 */     super.onArrival();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStuck() {
/* 148 */     this.active = false;
/* 149 */     super.onStuck();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPathFailed(BlockPos pos) {
/* 154 */     this.active = false;
/* 155 */     super.onPathFailed(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   private void startHarvesting() {
/* 160 */     int skillFactor = Math.max(this.villager.getSkillLerp(ProfessionType.RANCHER, 6, 1) - 1, 1);
/* 161 */     this.harvestTime = 34 * skillFactor;
/* 162 */     this.villager.getNavigator().clearPath();
/* 163 */     this.villager.playServerAnimation("villager_take");
/*     */   }
/*     */   
/*     */   private void stopHarvesting() {
/* 167 */     this.villager.stopServerAnimation("villager_take");
/*     */   }
/*     */   
/*     */   private boolean isAnimalHarvestable(EntityAnimal animal) {
/* 171 */     if (animal.isEntityAlive() && !animal.isChild()) {
/* 172 */       if (animal instanceof net.minecraft.entity.passive.EntityCow) {
/* 173 */         return ModEntities.isEntityUsable((Entity)animal, COW_MILK_TIME, this.villager.world.getTotalWorldTime());
/*     */       }
/* 175 */       if (animal instanceof EntitySheep) {
/* 176 */         EntitySheep sheep = (EntitySheep)animal;
/* 177 */         return !sheep.getSheared();
/*     */       } 
/*     */     } 
/*     */     
/* 181 */     return false;
/*     */   }
/*     */   
/*     */   private void harvestAnimal() {
/* 185 */     if (isAnimalHarvestable(this.targetAnimal)) {
/*     */       
/* 187 */       if (this.targetAnimal instanceof EntitySheep) {
/* 188 */         EntitySheep sheep = (EntitySheep)this.targetAnimal;
/* 189 */         sheep.setSheared(true);
/* 190 */         Random rng = this.villager.getRNG();
/*     */         
/* 192 */         this.villager.damageItem(this.toolUsed, 1);
/* 193 */         this.villager.tryAddSkill(ProfessionType.RANCHER, 5);
/*     */         
/* 195 */         int i = 1;
/* 196 */         if (rng.nextInt(100) < this.villager.getSkill(ProfessionType.RANCHER) && rng.nextInt(100) < this.villager.getSkill(ProfessionType.RANCHER)) {
/* 197 */           i++;
/*     */         }
/*     */         
/* 200 */         for (int j = 0; j < i; j++) {
/*     */           
/* 202 */           EntityItem entityitem = sheep.entityDropItem(ModItems.makeTaggedItem(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, sheep.getFleeceColor().getMetadata()), ItemTagType.VILLAGER), 1.0F);
/* 203 */           entityitem.motionY += (rng.nextFloat() * 0.05F);
/* 204 */           entityitem.motionX += ((rng.nextFloat() - rng.nextFloat()) * 0.1F);
/* 205 */           entityitem.motionZ += ((rng.nextFloat() - rng.nextFloat()) * 0.1F);
/*     */         } 
/*     */         
/* 208 */         this.villager.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
/*     */       }
/* 210 */       else if (this.targetAnimal instanceof net.minecraft.entity.passive.EntityCow && 
/* 211 */         ModEntities.isEntityUsable((Entity)this.targetAnimal, COW_MILK_TIME, this.villager.world.getTotalWorldTime())) {
/*     */         
/* 213 */         ModEntities.useEntity((Entity)this.targetAnimal, this.villager.world.getTotalWorldTime());
/*     */         
/* 215 */         this.villager.tryAddSkill(ProfessionType.RANCHER, 5);
/* 216 */         this.villager.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
/*     */         
/* 218 */         ItemStack milkItem = new ItemStack(Items.MILK_BUCKET);
/* 219 */         if (ModEntities.isTaggedEntity((Entity)this.targetAnimal, EntityTagType.VILLAGER) && ModItems.isTaggedItem(this.toolUsed, ItemTagType.VILLAGER)) {
/* 220 */           ModItems.makeTaggedItem(milkItem, ItemTagType.VILLAGER);
/*     */         }
/* 222 */         ItemStack addedItem = this.villager.getInventory().addItem(milkItem);
/* 223 */         if (addedItem == ItemStack.EMPTY) {
/* 224 */           this.villager.getInventory().removeItems(p -> ItemStack.areItemStacksEqual(this.toolUsed, p), 1);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 229 */       this.villager.throttledSadness(-3);
/*     */ 
/*     */       
/* 232 */       this.villager.modifyHunger(-3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 239 */     this.villager.pickupItems(5);
/*     */     
/* 241 */     this.rancherPen.clearAnimalSchedule(this.targetAnimal, VillageStructureRancherPen.AnimalScheduleType.HARVEST);
/* 242 */     this.rancherPen = null;
/*     */     
/* 244 */     this.harvestTime = 0;
/* 245 */     this.active = false;
/* 246 */     this.villager.unequipActionItem(this.toolUsed);
/* 247 */     this.toolUsed = null;
/* 248 */     stopHarvesting();
/* 249 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIHarvestAnimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */