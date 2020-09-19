/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.monster.EntityMob;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.pathfinding.Path;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.entities.EntityNecromancer;
/*     */ import net.tangotek.tektopia.entities.EntitySpiritSkull;
/*     */ 
/*     */ public class EntityAISoulLink
/*     */   extends EntityAIBase
/*     */ {
/*     */   private EntityNecromancer necro;
/*     */   private EntitySpiritSkull skull;
/*     */   private EntityCreature target;
/*  22 */   private int abilityTime = 0;
/*  23 */   private final int ABILITY_LENGTH = 22;
/*     */   
/*     */   public EntityAISoulLink(EntityNecromancer v) {
/*  26 */     this.necro = v;
/*  27 */     setMutexBits(1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  33 */     this.target = null;
/*  34 */     if (this.necro.isAITick() && this.necro.isReadyForSkull()) {
/*     */       
/*  36 */       List<EntityAnimal> animals = this.necro.world.getEntitiesWithinAABB(EntityAnimal.class, this.necro.getEntityBoundingBox().grow(20.0D));
/*  37 */       this.target = animals.stream().filter(a -> (isCreatureInRange((EntityCreature)a) && !this.necro.isCreatureSkulled((EntityCreature)a))).findAny().orElse(null);
/*  38 */       if (this.target == null) {
/*     */         
/*  40 */         List<EntityMob> mobs = this.necro.world.getEntitiesWithinAABB(EntityMob.class, this.necro.getEntityBoundingBox().grow(20.0D));
/*  41 */         Collections.shuffle(mobs);
/*  42 */         this.target = mobs.stream().filter(m -> { if (isCreatureInRange((EntityCreature)m) && m.isEntityAlive()) if (EntityNecromancer.isMinion((EntityLivingBase)m) && !this.necro.isCreatureSkulled((EntityCreature)m));  return false; }).findAny().orElse(null);
/*     */       } 
/*     */       
/*  45 */       if (this.target != null) {
/*  46 */         Path path = this.necro.getNavigator().getPathToPos(this.target.getPosition());
/*  47 */         if (path == null || path.getCurrentPathLength() <= 1) {
/*  48 */           this.target = null;
/*     */         }
/*     */       } 
/*     */     } 
/*  52 */     return (this.target != null);
/*     */   }
/*     */   
/*     */   private boolean isCreatureInRange(EntityCreature c) {
/*  56 */     return (c.getDistanceSq((Entity)this.necro) < 400.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  62 */     startAbility();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  68 */     return (this.abilityTime > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  80 */     if (this.abilityTime > 0) {
/*  81 */       this.abilityTime--;
/*  82 */       if (this.abilityTime == 12) {
/*  83 */         spawnSkull();
/*     */       }
/*     */     } 
/*     */     
/*  87 */     this.necro.getLookHelper().setLookPositionWithEntity((Entity)this.target, 30.0F, 30.0F);
/*     */ 
/*     */     
/*  90 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   private void startAbility() {
/*  95 */     this.abilityTime = 22;
/*  96 */     this.necro.getNavigator().clearPath();
/*  97 */     this.necro.playServerAnimation("necro_siphon");
/*  98 */     this.necro.playSound(ModSoundEvents.deathSkullLeave, this.necro.world.rand.nextFloat() * 0.4F + 1.2F, this.necro.world.rand.nextFloat() * 0.4F + 0.8F);
/*  99 */     this.necro.setSpellTarget((Entity)this.target);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void stopAbility() {
/* 105 */     this.necro.stopServerAnimation("necro_siphon");
/* 106 */     this.necro.setSpellTarget(null);
/*     */   }
/*     */   
/*     */   private void spawnSkull() {
/* 110 */     this.skull = new EntitySpiritSkull(this.necro.world);
/* 111 */     this.skull.setLocationAndAngles(this.target.posX, this.target.posY + this.target.getEyeHeight(), this.target.posZ, this.target.rotationYaw, 0.0F);
/* 112 */     this.necro.world.spawnEntity((Entity)this.skull);
/* 113 */     this.skull.setSkullCreature(this.target);
/* 114 */     this.necro.addSkull(this.skull);
/* 115 */     EntityNecromancer.makeMinion((EntityLivingBase)this.target, this.necro);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 120 */     stopAbility();
/* 121 */     this.abilityTime = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAISoulLink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */