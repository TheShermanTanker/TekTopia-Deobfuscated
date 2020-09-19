/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.entities.EntityChild;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class EntityAIPlayTag extends EntityAIFollow {
/*  15 */   protected EntityChild targetChild = null;
/*  16 */   protected EntityChild thisChild = null;
/*  17 */   protected EntityChild preferredTarget = null;
/*     */   private boolean active = false;
/*     */   protected final Predicate<Entity> canChase;
/*     */   protected final EntityVillagerTek villager;
/*  21 */   private int activeTick = 0;
/*     */   
/*     */   public EntityAIPlayTag(EntityVillagerTek v) {
/*  24 */     super((EntityVillageNavigator)v);
/*  25 */     this.villager = v;
/*  26 */     Predicate<Entity> isChasable = e -> (e.isEntityAlive() && e instanceof EntityChild);
/*  27 */     this.canChase = Predicates.and(EntitySelectors.CAN_AI_TARGET, isChasable);
/*     */     
/*  29 */     if (this.villager instanceof EntityChild) {
/*  30 */       this.thisChild = (EntityChild)this.villager;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldExecute() {
/*  35 */     if (this.villager.isAITick("play_tag") && this.villager.hasVillage() && this.villager.isWorkTime() && this.thisChild != null)
/*     */     {
/*  37 */       if (this.thisChild.getChasedBy() != null) {
/*     */         
/*  39 */         this.preferredTarget = this.thisChild.getChasedBy();
/*     */       } else {
/*     */         
/*  42 */         this.targetChild = seekChaseTarget();
/*  43 */         if (this.targetChild != null) {
/*  44 */           return super.shouldExecute();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*  49 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected EntityLivingBase getFollowTarget() {
/*  54 */     return (EntityLivingBase)this.targetChild;
/*     */   }
/*     */   
/*     */   private EntityChild seekChaseTarget() {
/*  58 */     EntityChild result = null;
/*  59 */     if (this.preferredTarget != null) {
/*  60 */       if (this.canChase.test(this.preferredTarget) && this.thisChild.getDistanceSq((Entity)this.preferredTarget) < 256.0D) {
/*  61 */         result = this.preferredTarget;
/*     */       }
/*  63 */       this.preferredTarget = null;
/*     */     } 
/*     */     
/*  66 */     if (result == null) {
/*  67 */       List<Entity> children = this.villager.world.getEntitiesInAABBexcluding((Entity)this.villager, this.villager.getEntityBoundingBox().grow(16.0D), this.canChase);
/*  68 */       if (!children.isEmpty()) {
/*  69 */         return (EntityChild)children.get(this.villager.world.rand.nextInt(children.size()));
/*     */       }
/*     */     } 
/*     */     
/*  73 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  80 */     this.active = true;
/*  81 */     this.activeTick = this.villager.world.rand.nextInt(100) + 80;
/*  82 */     this.villager.setMovementMode(EntityVillagerTek.MovementMode.RUN);
/*  83 */     this.targetChild.setChasedBy((EntityChild)this.villager);
/*     */     
/*  85 */     if (this.villager.getRNG().nextInt(4) == 0) {
/*  86 */       this.villager.modifyHappy(4);
/*     */     }
/*  88 */     super.startExecuting();
/*     */   }
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  92 */     return (this.targetChild != null && this.active);
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/*  97 */     this.villager.setMovementMode(EntityVillagerTek.MovementMode.RUN);
/*     */   }
/*     */   
/*     */   public void updateTask() {
/* 101 */     this.activeTick--;
/* 102 */     if (this.activeTick <= 0) {
/* 103 */       this.active = false;
/*     */     }
/* 105 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 110 */     super.onArrival();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStuck() {
/* 115 */     this.active = false;
/* 116 */     super.onStuck();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPathFailed(BlockPos pos) {
/* 121 */     this.active = false;
/* 122 */     super.onPathFailed(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 127 */     this.active = false;
/* 128 */     this.targetChild.setChasedBy(null);
/* 129 */     this.preferredTarget = null;
/* 130 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/* 131 */     this.targetChild = null;
/* 132 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIPlayTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */