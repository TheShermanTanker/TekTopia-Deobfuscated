/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityBard;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureTavern;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityAIPerformTavern
/*     */   extends EntityAIMoveToBlock {
/*     */   private boolean arrived = false;
/*  19 */   private int castTime = 0;
/*     */   private EntityBard bard;
/*  21 */   private int happyGiven = 0;
/*     */   
/*     */   private BlockPos centerPatrons;
/*     */   private VillageStructureTavern tavern;
/*     */   
/*     */   public EntityAIPerformTavern(EntityVillagerTek entityIn) {
/*  27 */     super((EntityVillageNavigator)entityIn);
/*  28 */     this.bard = (EntityBard)entityIn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  34 */     if (this.bard.isAITick("perform_tavern") && this.bard.hasVillage() && !this.bard.isWorkTime() && !this.bard.isSleepingTime() && this.bard.getRNG().nextInt(4) == 0) {
/*  35 */       this.tavern = findTavern(this.bard.getPosition());
/*  36 */       if (this.tavern != null) {
/*  37 */         List<EntityVillagerTek> patrons = this.tavern.getEntitiesInside(EntityVillagerTek.class);
/*  38 */         patrons.remove(this.bard);
/*  39 */         if (patrons.size() < 2) {
/*  40 */           return false;
/*     */         }
/*  42 */         return super.shouldExecute();
/*     */       } 
/*     */       
/*  45 */       this.bard.setThought(EntityVillagerTek.VillagerThought.TAVERN);
/*     */     } 
/*     */ 
/*     */     
/*  49 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  54 */     BlockPos noteBlockPos = this.tavern.getUnoccupiedSpecialBlock(Blocks.NOTEBLOCK);
/*  55 */     if (noteBlockPos != null) {
/*  56 */       return noteBlockPos;
/*     */     }
/*     */ 
/*     */     
/*  60 */     return this.tavern.getRandomFloorTile();
/*     */   }
/*     */   
/*     */   private VillageStructureTavern findTavern(BlockPos pos) {
/*  64 */     VillageStructureTavern bestTavern = null;
/*  65 */     int crowd = -1;
/*  66 */     List<VillageStructure> taverns = this.bard.getVillage().getStructures(VillageStructureType.TAVERN);
/*  67 */     for (VillageStructure t : taverns) {
/*  68 */       VillageStructureTavern tavern = (VillageStructureTavern)t;
/*  69 */       if (tavern.getPerformingBard() == null) {
/*     */         
/*  71 */         if (tavern.isBlockInside(pos)) {
/*  72 */           return tavern;
/*     */         }
/*  74 */         List<EntityVillagerTek> patrons = tavern.getEntitiesInside(EntityVillagerTek.class);
/*  75 */         if (patrons.size() > crowd) {
/*  76 */           crowd = patrons.size();
/*  77 */           bestTavern = tavern;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  82 */     return bestTavern;
/*     */   }
/*     */ 
/*     */   
/*     */   private BlockPos getCenterPatrons() {
/*  87 */     BlockPos sumPos = BlockPos.ORIGIN;
/*  88 */     int count = 0;
/*  89 */     List<EntityVillagerTek> patrons = this.tavern.getEntitiesInside(EntityVillagerTek.class);
/*  90 */     for (EntityVillagerTek v : patrons) {
/*  91 */       if (v != this.bard) {
/*  92 */         count++;
/*  93 */         sumPos = sumPos.add((Vec3i)v.getPosition());
/*     */       } 
/*     */     } 
/*     */     
/*  97 */     if (count > 0) {
/*  98 */       return new BlockPos(sumPos.getX() / count, sumPos.getY() / count, sumPos.getZ() / count);
/*     */     }
/*     */     
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 106 */     if (!this.arrived) {
/* 107 */       tryPerform();
/*     */     }
/* 109 */     this.arrived = true;
/* 110 */     super.onArrival();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/* 116 */     this.tavern.setPerformingBard(this.bard);
/*     */ 
/*     */     
/* 119 */     this.bard.setPerformance(ModSoundEvents.Performance.getRandom(true, this.bard.getRNG()));
/* 120 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/* 126 */     return (this.castTime <= 0);
/*     */   }
/*     */   
/*     */   protected void tryPerform() {
/* 130 */     if (this.bard.isEntityAlive()) {
/*     */       
/* 132 */       ModSoundEvents.Performance performance = this.bard.getPerformance();
/* 133 */       this.castTime = performance.duration;
/* 134 */       this.centerPatrons = getCenterPatrons();
/* 135 */       this.bard.getNavigator().clearPath();
/* 136 */       this.bard.playServerAnimation(performance.anim);
/* 137 */       this.bard.playSound(performance.sound, 2.0F, 1.0F);
/* 138 */       this.bard.modifyHunger(-6);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 145 */     this.castTime--;
/*     */     
/* 147 */     if (this.castTime > 0 && this.bard.getRNG().nextInt(40) == 0) {
/* 148 */       distributeHappyNearby(16, 1, 3);
/* 149 */       this.bard.tryAddSkill(ProfessionType.BARD, 35);
/*     */     } 
/*     */     
/* 152 */     if (this.centerPatrons != null) {
/* 153 */       this.bard.faceLocation(this.centerPatrons.getX(), this.centerPatrons.getZ(), 90.0F);
/*     */     }
/*     */     
/* 156 */     if (this.castTime == 0) {
/*     */       
/* 158 */       distributeHappyNearby(2, 3, 5);
/*     */ 
/*     */       
/* 161 */       if (this.happyGiven > 8) {
/* 162 */         this.bard.modifyHappy(this.happyGiven / 2);
/*     */       } else {
/* 164 */         this.bard.modifyHappy(-5);
/*     */       } 
/*     */     } 
/* 167 */     super.updateTask();
/*     */   }
/*     */   
/*     */   private void distributeHappyNearby(int chance, int min, int max) {
/* 171 */     List<EntityVillagerTek> patrons = this.tavern.getEntitiesInside(EntityVillagerTek.class);
/* 172 */     for (EntityVillagerTek v : patrons) {
/* 173 */       if (v != this.bard && this.bard.getRNG().nextInt(chance) == 0) {
/* 174 */         v.cheerBeer(this.bard.getSkillLerp(ProfessionType.BARD, min, max));
/* 175 */         this.happyGiven++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 183 */     this.tavern.setPerformingBard(null);
/* 184 */     this.bard.stopServerAnimation((this.bard.getPerformance()).anim);
/* 185 */     this.bard.setPerformance(null);
/* 186 */     this.arrived = false;
/* 187 */     this.castTime = 0;
/* 188 */     this.happyGiven = 0;
/* 189 */     this.tavern = null;
/* 190 */     super.resetTask();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 195 */     if (this.castTime > 0) {
/* 196 */       return true;
/*     */     }
/* 198 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 203 */     this.bard.setMovementMode(this.bard.getDefaultMovement());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIPerformTavern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */