/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.pathing.BasePathingNode;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureHome;
/*     */ 
/*     */ public class EntityAIFleeEntity
/*     */   extends EntityAIMoveToBlock
/*     */ {
/*     */   private final Predicate<Entity> entityPredicate;
/*     */   private final EntityVillagerTek villager;
/*     */   private final float avoidDistance;
/*     */   private Entity fleeEntity;
/*     */   private BlockPos destPos;
/*     */   
/*     */   public EntityAIFleeEntity(EntityVillagerTek v, Predicate<Entity> inPred, float avoidDistanceIn) {
/*  29 */     super((EntityVillageNavigator)v);
/*  30 */     this.villager = v;
/*  31 */     this.avoidDistance = avoidDistanceIn;
/*  32 */     setMutexBits(1);
/*     */     
/*  34 */     this.entityPredicate = Predicates.and(new Predicate[] { EntitySelectors.CAN_AI_TARGET, e -> (e.isEntityAlive() && this.villager.getEntitySenses().canSee(e)), inPred });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  41 */     if (this.villager.isAITick() && this.villager.hasVillage()) {
/*  42 */       List<Entity> fleeEnts = this.villager.world.getEntitiesInAABBexcluding((Entity)this.villager, this.villager.getEntityBoundingBox().grow(this.avoidDistance, 8.0D, this.avoidDistance), this.entityPredicate);
/*  43 */       if (!fleeEnts.isEmpty()) {
/*  44 */         this.fleeEntity = fleeEnts.get(0);
/*     */ 
/*     */         
/*  47 */         VillageStructureHome home = this.villager.getHome();
/*  48 */         if (home != null && home.isBlockInside(this.villager.getPosition()) && VillageStructure.isWoodDoor(this.villager.world, home.getDoor()) && 
/*  49 */           !home.isBlockInside(this.fleeEntity.getPosition())) {
/*  50 */           return false;
/*     */         }
/*     */         
/*  53 */         BlockPos fleeBlock = findRandomTargetAwayFrom(this.fleeEntity);
/*  54 */         if (fleeBlock != null) {
/*  55 */           this.destPos = fleeBlock;
/*  56 */           return super.shouldExecute();
/*     */         } 
/*     */       } 
/*     */     } 
/*  60 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findRandomTargetAwayFrom(Entity fleeEntity) {
/*  65 */     Vec3d fleeDelta, villagerPos = this.villager.getPositionVector();
/*  66 */     Vec3d enemyPos = this.fleeEntity.getPositionVector();
/*     */ 
/*     */     
/*  69 */     if (this.villager.getBedPos() != null) {
/*     */       
/*  71 */       if (this.villager.getDistanceSq(this.villager.getBedPos()) < 256.0D) {
/*  72 */         return this.villager.getBedPos();
/*     */       }
/*     */       
/*  75 */       fleeDelta = (new Vec3d((Vec3i)this.villager.getBedPos())).subtract(villagerPos);
/*     */     
/*     */     }
/*  78 */     else if (this.villager.hasVillage()) {
/*  79 */       if (this.villager.getPosition().distanceSq((Vec3i)this.villager.getVillage().getOrigin()) < 1600.0D) {
/*  80 */         fleeDelta = villagerPos.subtract(enemyPos);
/*     */       }
/*     */       else {
/*     */         
/*  84 */         fleeDelta = this.villager.getPositionVector().subtract(new Vec3d((Vec3i)this.villager.getVillage().getOrigin()));
/*     */       } 
/*     */     } else {
/*     */       
/*  88 */       fleeDelta = villagerPos.subtract(enemyPos);
/*     */     } 
/*     */ 
/*     */     
/*  92 */     Vec3d fleeDeltaNorm = fleeDelta.normalize();
/*  93 */     Vec3d fleeDir = new Vec3d(fleeDeltaNorm.x, 0.0D, fleeDeltaNorm.y);
/*     */ 
/*     */     
/*  96 */     if (this.villager.hasVillage() && this.villager.getVillage().getAABB().contains(villagerPos))
/*     */     {
/*  98 */       if (villagerPos.add(fleeDir).squareDistanceTo(enemyPos) < villagerPos.squareDistanceTo(enemyPos)) {
/*  99 */         fleeDir = fleeDir.rotateYaw(60.0F);
/* 100 */         if (villagerPos.add(fleeDir).squareDistanceTo(enemyPos) < villagerPos.squareDistanceTo(enemyPos)) {
/* 101 */           fleeDir = fleeDir.rotateYaw(-120.0F);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 106 */     Vec3d fleePos = this.villager.getPositionVector().add(fleeDir.scale(16.0D));
/* 107 */     BlockPos fleeBlock = new BlockPos(fleePos.x, fleePos.y, fleePos.z);
/*     */     
/* 109 */     for (int i = 0; i < 20; i++) {
/* 110 */       BlockPos testBlock = randomNearbyBlock(fleeBlock, i + 3);
/* 111 */       BasePathingNode baseNode = this.villager.getVillage().getPathingGraph().getNodeYRange(testBlock.getX(), testBlock.getY() - 5, testBlock.getY() + 5, testBlock.getZ());
/* 112 */       if (baseNode != null) {
/* 113 */         return baseNode.getBlockPos();
/*     */       }
/*     */     } 
/*     */     
/* 117 */     return null;
/*     */   }
/*     */   
/*     */   private BlockPos randomNearbyBlock(BlockPos pos, int xz) {
/* 121 */     return pos.add(this.villager.getRNG().nextInt(xz * 2) - xz, 0, this.villager.getRNG().nextInt(xz * 2) - xz);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/* 127 */     if (!(this.fleeEntity instanceof EntityVillagerTek)) {
/*     */       
/* 129 */       if (!this.villager.isRole(VillagerRole.DEFENDER)) {
/* 130 */         this.villager.modifyHappy(-2);
/*     */       }
/* 132 */       if (this.villager.getRNG().nextInt(2) == 0) {
/* 133 */         this.villager.playSound(ModSoundEvents.villagerAfraid);
/*     */       }
/*     */     } 
/* 136 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 141 */     this.villager.setMovementMode(EntityVillagerTek.MovementMode.RUN);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/* 146 */     return this.destPos;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 152 */     this.fleeEntity = null;
/* 153 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIFleeEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */