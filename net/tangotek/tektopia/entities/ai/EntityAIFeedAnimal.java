/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.ModEntities;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityRancher;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureRancherPen;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityAIFeedAnimal
/*     */   extends EntityAIFollow {
/*     */   private VillageStructureType penType;
/*  21 */   private VillageStructureRancherPen rancherPen = null;
/*  22 */   private EntityAnimal feedingAnimal = null;
/*     */   private boolean active = false;
/*  24 */   private int feedTime = 0;
/*     */   private ItemStack foodItem;
/*     */   private final int modChance;
/*     */   private final String aiFilter;
/*     */   private final EntityRancher rancher;
/*     */   
/*     */   public EntityAIFeedAnimal(EntityRancher v, VillageStructureType penType, int modChance, String aiFilter) {
/*  31 */     super((EntityVillageNavigator)v);
/*  32 */     this.rancher = v;
/*  33 */     this.penType = penType;
/*  34 */     this.modChance = modChance;
/*  35 */     this.aiFilter = aiFilter;
/*     */   }
/*     */ 
/*     */   
/*     */   protected EntityLivingBase getFollowTarget() {
/*  40 */     return (EntityLivingBase)this.feedingAnimal;
/*     */   }
/*     */   
/*     */   private boolean isPriorityPen(EntityRancher rancher) {
/*  44 */     return (rancher.getPriorityPen() == this.penType);
/*     */   }
/*     */   
/*     */   private boolean useThisPen(EntityRancher rancher, int modChance) {
/*  48 */     if (rancher.getPriorityPen() != null) {
/*  49 */       if (isPriorityPen(rancher)) {
/*  50 */         return true;
/*     */       }
/*  52 */     } else if (rancher.ticksExisted % 4 == modChance) {
/*  53 */       return true;
/*     */     } 
/*  55 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  60 */     if (this.rancher.isAITick(this.aiFilter) && this.rancher.getVillage() != null && this.rancher.isWorkTime() && useThisPen(this.rancher, this.modChance)) {
/*  61 */       List<VillageStructure> structures = this.rancher.getVillage().getStructures(this.penType);
/*  62 */       Collections.shuffle(structures);
/*  63 */       for (VillageStructure struct : structures) {
/*  64 */         this.rancherPen = (VillageStructureRancherPen)struct;
/*  65 */         this.foodItem = null;
/*     */         
/*  67 */         List<EntityAnimal> animalList = this.rancherPen.getEntitiesInside(this.rancherPen.getAnimalClass());
/*  68 */         Collections.shuffle(animalList);
/*     */         
/*  70 */         if (!animalList.isEmpty()) {
/*  71 */           List<ItemStack> foodItems = this.rancher.getInventory().getItems(p -> ((EntityAnimal)animalList.get(0)).isBreedingItem(p), 1);
/*  72 */           if (!foodItems.isEmpty()) {
/*  73 */             this.foodItem = foodItems.get(0);
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/*  78 */         this.feedingAnimal = null;
/*  79 */         int minHunger = 100;
/*  80 */         for (EntityAnimal animal : animalList) {
/*  81 */           if (isAnimalFeedable(this.rancherPen, animal) && !this.rancherPen.isAnimalScheduled(animal, VillageStructureRancherPen.AnimalScheduleType.FEED)) {
/*  82 */             int curHunger = ModEntities.getAnimalHunger(animal);
/*  83 */             if (curHunger < minHunger) {
/*  84 */               this.feedingAnimal = animal;
/*  85 */               minHunger = curHunger;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/*  90 */         if (this.feedingAnimal != null) {
/*  91 */           if (this.foodItem == null) {
/*  92 */             this.rancher.setThought(this.rancherPen.getNoFoodThought());
/*     */             continue;
/*     */           } 
/*  95 */           return super.shouldExecute();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 101 */     return false;
/*     */   }
/*     */   
/*     */   public void startExecuting() {
/* 105 */     this.active = true;
/* 106 */     this.rancher.equipActionItem(this.foodItem);
/* 107 */     this.rancherPen.scheduleAnimal(this.feedingAnimal, VillageStructureRancherPen.AnimalScheduleType.FEED);
/* 108 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 113 */     return this.active;
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 118 */     this.rancher.setMovementMode(this.rancher.getDefaultMovement());
/*     */   }
/*     */   
/*     */   public void updateTask() {
/* 122 */     if (this.feedTime > 0) {
/* 123 */       this.rancher.getLookHelper().setLookPosition(this.destinationPos.getX(), this.destinationPos.getY(), this.destinationPos.getZ(), 30.0F, 30.0F);
/* 124 */       this.feedTime--;
/* 125 */       if (this.feedTime == 17) {
/* 126 */         this.rancher.unequipActionItem(this.foodItem);
/*     */       }
/* 128 */       if (this.feedTime == 0) {
/* 129 */         feedAnimal();
/* 130 */         this.active = false;
/*     */       } 
/*     */     } else {
/*     */       
/* 134 */       super.updateTask();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/* 140 */     return (this.feedTime <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 145 */     if (!isAnimalFeedable(this.rancherPen, this.feedingAnimal)) {
/* 146 */       this.active = false;
/*     */     }
/*     */     
/* 149 */     startFeeding();
/* 150 */     super.onArrival();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStuck() {
/* 155 */     this.active = false;
/* 156 */     super.onStuck();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPathFailed(BlockPos pos) {
/* 161 */     this.active = false;
/* 162 */     super.onPathFailed(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   private void startFeeding() {
/* 167 */     int skillFactor = Math.max(this.rancher.getSkillLerp(ProfessionType.RANCHER, 6, 1) - 1, 1);
/* 168 */     this.feedTime = 34 * skillFactor;
/* 169 */     this.rancher.getNavigator().clearPath();
/* 170 */     this.rancher.playServerAnimation("villager_take");
/*     */   }
/*     */   
/*     */   private void stopFeeding() {
/* 174 */     this.rancher.stopServerAnimation("villager_take");
/*     */   }
/*     */   
/*     */   private boolean isAnimalFeedable(VillageStructureRancherPen rancherPen, EntityAnimal animal) {
/* 178 */     if (animal.isEntityAlive() && (ModEntities.isAnimalHungry(animal) || isPriorityPen(this.rancher)) && 
/* 179 */       animal.getGrowingAge() <= 0 && !animal.isInLove()) {
/* 180 */       return true;
/*     */     }
/*     */     
/* 183 */     return false;
/*     */   }
/*     */   
/*     */   private void feedAnimal() {
/* 187 */     if (isAnimalFeedable(this.rancherPen, this.feedingAnimal)) {
/*     */       
/* 189 */       this.rancher.debugOut("feeding " + this.feedingAnimal.getClass().getSimpleName() + " at " + this.rancher.getPosition());
/* 190 */       ModEntities.modifyAnimalHunger(this.feedingAnimal, 100);
/*     */       
/* 192 */       this.rancher.tryAddSkill(ProfessionType.RANCHER, 5);
/*     */ 
/*     */       
/* 195 */       this.feedingAnimal.setHealth(this.feedingAnimal.getMaxHealth());
/*     */ 
/*     */       
/* 198 */       if (!this.rancherPen.isPenFull(1.0F) && 
/* 199 */         this.feedingAnimal.getRNG().nextInt(this.rancher.getSkillLerp(ProfessionType.RANCHER, 5, 1)) <= 1) {
/* 200 */         this.feedingAnimal.setInLove(null);
/*     */       }
/*     */ 
/*     */       
/* 204 */       this.rancher.pickupItems(5);
/*     */ 
/*     */       
/* 207 */       this.rancher.getInventory().removeItems(p -> this.feedingAnimal.isBreedingItem(p), 1);
/*     */       
/* 209 */       this.rancher.throttledSadness(-3);
/*     */ 
/*     */       
/* 212 */       this.rancher.modifyHunger(-this.rancherPen.getFeedCost((EntityVillagerTek)this.rancher));
/*     */       
/* 214 */       if (this.rancher.getPriorityPen() == null) {
/* 215 */         this.rancher.setPriorityPen(this.rancherPen.type);
/* 216 */       } else if (this.rancher.getPriorityPen() == this.rancherPen.type) {
/* 217 */         this.rancher.setPriorityPen(null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetTask() {
/* 223 */     this.rancherPen.clearAnimalSchedule(this.feedingAnimal, VillageStructureRancherPen.AnimalScheduleType.FEED);
/* 224 */     this.rancherPen = null;
/*     */     
/* 226 */     this.feedTime = 0;
/* 227 */     this.active = false;
/* 228 */     this.rancher.unequipActionItem(this.foodItem);
/* 229 */     this.foodItem = null;
/*     */     
/* 231 */     stopFeeding();
/* 232 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIFeedAnimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */