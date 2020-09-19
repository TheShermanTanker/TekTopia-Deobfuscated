/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityDruid;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.structures.VillageStructureMineshaft;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIEarthReform
/*     */   extends EntityAIMoveToBlock
/*     */ {
/*     */   private boolean arrived = false;
/*  18 */   private EntityDruid druid = null;
/*  19 */   private int castTime = 0;
/*  20 */   private final int CAST_TIME = 80;
/*     */   
/*     */   private final EntityVillagerTek villager;
/*     */   private VillageStructureMineshaft mineshaft;
/*     */   
/*     */   public EntityAIEarthReform(EntityVillagerTek entityIn) {
/*  26 */     super((EntityVillageNavigator)entityIn);
/*  27 */     this.villager = entityIn;
/*  28 */     this.druid = (EntityDruid)entityIn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  34 */     if (this.villager.isAITick("cast_earth_reform") && this.villager.hasVillage() && this.villager.isWorkTime() && this.druid.isEarthReformTime()) {
/*  35 */       this.mineshaft = this.villager.getVillage().requestMineshaft(this.villager, m -> (m.getTunnelLength() > 3 && m.getPlayersInside().isEmpty() && m.getDruid() == null), (C, B) -> (C.getTunnelLength() > B.getTunnelLength() && C.getTunnelLength() > 10));
/*  36 */       if (this.mineshaft != null) {
/*  37 */         return super.shouldExecute();
/*     */       }
/*     */     } 
/*     */     
/*  41 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  46 */     BlockPos destBlock = this.mineshaft.getDoorOutside(2);
/*  47 */     if (!isWalkable(destBlock, (EntityVillageNavigator)this.villager)) {
/*  48 */       destBlock = this.mineshaft.getDoorOutside(1);
/*     */     }
/*  50 */     return destBlock;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNearWalkPos() {
/*  55 */     if (getWalkPos() != null) {
/*  56 */       return (getWalkPos().distanceSqToCenter(this.villager.getX(), this.villager.getY(), this.villager.getZ()) < 0.5D);
/*     */     }
/*     */     
/*  59 */     return super.isNearWalkPos();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/*  64 */     return this.destinationPos;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/*  69 */     if (!this.arrived) {
/*  70 */       tryCast();
/*     */     }
/*  72 */     this.arrived = true;
/*  73 */     super.onArrival();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  79 */     this.mineshaft.setDruid(this.druid);
/*  80 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/*  86 */     return (this.castTime <= 0);
/*     */   }
/*     */   
/*     */   protected void tryCast() {
/*  90 */     if (this.villager.isEntityAlive()) {
/*  91 */       this.mineshaft.updateOccupants();
/*  92 */       if (this.mineshaft.getTunnelMiner() == null) {
/*  93 */         this.castTime = 80;
/*     */         
/*  95 */         this.villager.playServerAnimation("villager_cast_forward");
/*  96 */         this.druid.setCastingEarthReform(this.mineshaft.getMiningPos());
/*  97 */         this.villager.modifyHunger(-8);
/*  98 */         this.villager.getNavigator().clearPath();
/*  99 */         this.villager.addJob(new TickJob(4, 0, false, () -> this.villager.playSound(ModSoundEvents.earthRumble)));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 106 */     this.castTime--;
/*     */     
/* 108 */     if (this.castTime > 0) {
/* 109 */       this.villager.faceLocation(this.mineshaft.getMiningPos().getX(), this.mineshaft.getMiningPos().getZ(), 30.0F);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     if (this.castTime == 38) {
/* 121 */       this.mineshaft.updateOccupants();
/* 122 */       if (this.mineshaft.getTunnelMiner() == null) {
/* 123 */         this.villager.tryAddSkill(ProfessionType.DRUID, 9);
/*     */ 
/*     */         
/* 126 */         this.mineshaft.regrow(this.villager);
/*     */ 
/*     */         
/* 129 */         for (int i = 0; i < 3; i++) {
/* 130 */           if (this.villager.getRNG().nextInt(100) < this.villager.getSkill(ProfessionType.DRUID)) {
/* 131 */             this.mineshaft.regrow(this.villager);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 137 */     super.updateTask();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 143 */     this.villager.stopServerAnimation("villager_cast_forward");
/* 144 */     this.arrived = false;
/* 145 */     this.castTime = 0;
/* 146 */     this.druid.setCastingEarthReform(null);
/* 147 */     this.mineshaft.setDruid(null);
/*     */     
/* 149 */     super.resetTask();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 154 */     if (this.mineshaft.getTunnelMiner() != null) {
/* 155 */       return false;
/*     */     }
/* 157 */     if (this.castTime > 0) {
/* 158 */       return true;
/*     */     }
/* 160 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 165 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIEarthReform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */