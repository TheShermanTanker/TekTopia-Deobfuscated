/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.EntityTagType;
/*     */ import net.tangotek.tektopia.ModEntities;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityAILeadAnimalToStructure
/*     */   extends EntityAIMoveToBlock {
/*     */   private final VillageStructureType structureType;
/*     */   private VillageStructure destinationStructure;
/*     */   protected final EntityVillagerTek villager;
/*     */   private boolean active = false;
/*  20 */   private int pathTick = 0;
/*     */   private final EntityTagType entTagType;
/*     */   
/*     */   public EntityAILeadAnimalToStructure(EntityVillagerTek v, VillageStructureType structureType, EntityTagType tagType) {
/*  24 */     super((EntityVillageNavigator)v);
/*  25 */     this.villager = v;
/*  26 */     this.structureType = structureType;
/*  27 */     this.destinationStructure = null;
/*  28 */     this.entTagType = tagType;
/*     */   }
/*     */   
/*     */   protected VillageStructure getDestinationStructure() {
/*  32 */     return this.villager.getVillage().getNearestStructure(this.structureType, this.villager.getPosition());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  37 */     this.destinationPos = null;
/*  38 */     if (this.villager.getVillage() != null && this.villager.getLeadAnimal() != null && this.villager.isWorkTime()) {
/*  39 */       this.destinationStructure = getDestinationStructure();
/*  40 */       if (this.destinationStructure != null) {
/*  41 */         this.destinationPos = this.destinationStructure.getSafeSpot();
/*     */       }
/*     */     } 
/*     */     
/*  45 */     return this.destinationPos;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  50 */     if (this.villager.isAITick()) {
/*  51 */       return super.shouldExecute();
/*     */     }
/*  53 */     return false;
/*     */   }
/*     */   
/*     */   public void startExecuting() {
/*  57 */     this.active = true;
/*  58 */     this.pathTick = 30;
/*     */     
/*  60 */     super.startExecuting();
/*     */   }
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  64 */     return this.active;
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/*  69 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  74 */     if (this.villager.getLeadAnimal() != null) {
/*  75 */       if (this.villager.getLeadAnimal().getDistanceSq((Entity)this.villager) > 40.0D) {
/*  76 */         teleportAnimal(this.villager.getPosition());
/*     */       }
/*     */       
/*  79 */       if (!this.villager.getLeadAnimal().isEntityAlive()) {
/*  80 */         this.active = false;
/*  81 */       } else if (this.villager.getLeadAnimal().getLeashHolder() != this.villager) {
/*  82 */         this.active = false;
/*     */       } 
/*     */     } 
/*  85 */     this.pathTick--;
/*  86 */     if (this.pathTick <= 0) {
/*  87 */       updateAnimalPath();
/*  88 */       if (this.villager.getRNG().nextInt(8) == 0) {
/*  89 */         this.villager.modifyHunger(-1);
/*     */       }
/*  91 */       this.pathTick = 30;
/*     */     } 
/*     */     
/*  94 */     super.updateTask();
/*     */   }
/*     */   
/*     */   private void updateAnimalPath() {
/*  98 */     this.villager.getLeadAnimal().addPotionEffect(new PotionEffect(MobEffects.SPEED, 40));
/*  99 */     this.villager.getLeadAnimal().getNavigator().tryMoveToEntityLiving((Entity)this.villager, this.villager.getLeadAnimal().getAIMoveSpeed() * 1.2D);
/*     */   }
/*     */   
/*     */   private void teleportAnimal(BlockPos pos) {
/* 103 */     this.villager.getLeadAnimal().setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 108 */     this.active = false;
/* 109 */     super.onArrival();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStuck() {
/* 114 */     this.active = false;
/* 115 */     super.onStuck();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPathFailed(BlockPos pos) {
/* 120 */     this.active = false;
/* 121 */     super.onPathFailed(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 126 */     if (this.villager.getLeadAnimal() != null) {
/* 127 */       if (!this.destinationStructure.isBlockInside(this.villager.getLeadAnimal().getPosition())) {
/* 128 */         teleportAnimal(this.destinationStructure.getSafeSpot());
/*     */       }
/*     */ 
/*     */       
/* 132 */       if (this.entTagType != null) {
/* 133 */         ModEntities.makeTaggedEntity((Entity)this.villager.getLeadAnimal(), this.entTagType);
/*     */       }
/* 135 */       this.villager.getLeadAnimal().clearLeashed(true, false);
/* 136 */       this.villager.setLeadAnimal(null);
/*     */     } 
/*     */     
/* 139 */     this.active = false;
/* 140 */     this.destinationStructure = null;
/* 141 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAILeadAnimalToStructure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */