/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.pathing.BasePathingNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIFollowLeader
/*     */   extends EntityAIBase
/*     */ {
/*     */   private final EntityCreature creature;
/*     */   private final EntityVillageNavigator leader;
/*     */   private final double movementSpeed;
/*     */   private final int clusterSize;
/*  26 */   private int nextTick = 0;
/*     */   
/*     */   private BlockPos followPos;
/*     */   private boolean farAway = false;
/*     */   
/*     */   public EntityAIFollowLeader(EntityCreature creatureIn, EntityVillageNavigator leader, int clusterSize, double speedIn) {
/*  32 */     this.creature = creatureIn;
/*  33 */     this.movementSpeed = speedIn;
/*  34 */     this.clusterSize = clusterSize;
/*  35 */     this.leader = leader;
/*  36 */     this.nextTick = getTickRate(creatureIn);
/*  37 */     setMutexBits(1);
/*  38 */     MinecraftForge.EVENT_BUS.register(this);
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
/*  44 */     if (event.getEntity() == this.creature && this.farAway && event.getTarget() != null) {
/*  45 */       this.creature.setAttackTarget(null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  53 */     if (this.leader.isDead) {
/*  54 */       this.creature.setDead();
/*     */     }
/*  56 */     if (this.creature.isEntityAlive() && this.leader.hasVillage() && (this.farAway || this.creature.ticksExisted > this.nextTick)) {
/*  57 */       this.nextTick += 20;
/*     */       
/*  59 */       this.farAway = (this.creature.getDistanceSq((Entity)this.leader) > 900.0D);
/*  60 */       if (this.farAway) {
/*  61 */         this.creature.setAttackTarget(null);
/*     */       }
/*     */       
/*  64 */       if (this.creature.getAttackTarget() == null) {
/*  65 */         this.followPos = getFollowPos(this.leader, this.leader.getNavigator().noPath() ? 4.0F : 10.0F, this.clusterSize);
/*  66 */         if (this.followPos != null && this.creature.getDistanceSq(this.followPos) > (this.clusterSize * this.clusterSize)) {
/*  67 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  72 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private BlockPos getFollowPos(EntityVillageNavigator leader, float forwardDist, int clusterSize) {
/*  77 */     float distance = forwardDist;
/*  78 */     float f1 = MathHelper.sin(leader.rotationYaw * 0.017453292F);
/*  79 */     float f2 = MathHelper.cos(leader.rotationYaw * 0.017453292F);
/*  80 */     double behindX = (-distance * f1);
/*  81 */     double behindZ = (distance * f2);
/*     */     
/*  83 */     BlockPos testPos = new BlockPos(leader.posX + behindX, leader.posY, leader.posZ + behindZ);
/*     */     
/*  85 */     for (int i = 0; i < 20; i++) {
/*  86 */       BlockPos testBlock = randomNearbyBlock(testPos, clusterSize);
/*  87 */       BasePathingNode baseNode = leader.getVillage().getPathingGraph().getNodeYRange(testBlock.getX(), testBlock.getY() - 5, testBlock.getY() + 5, testBlock.getZ());
/*  88 */       if (baseNode != null && !leader.getVillage().isInStructure(testBlock)) {
/*  89 */         return baseNode.getBlockPos();
/*     */       }
/*     */     } 
/*     */     
/*  93 */     return null;
/*     */   }
/*     */   
/*     */   private BlockPos randomNearbyBlock(BlockPos pos, int xz) {
/*  97 */     Random rnd = this.leader.getRNG();
/*  98 */     int x = MathHelper.getInt(rnd, -xz, xz);
/*  99 */     int z = MathHelper.getInt(rnd, -xz, xz);
/* 100 */     return pos.add(x, 0, z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/* 108 */     return !this.creature.getNavigator().noPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/* 116 */     this.nextTick = this.creature.ticksExisted + getTickRate(this.creature);
/*     */     
/* 118 */     this.creature.getNavigator().tryMoveToXYZ(this.followPos.getX(), this.followPos.getY(), this.followPos.getZ(), this.movementSpeed);
/*     */   }
/*     */   
/*     */   private int getTickRate(EntityCreature creature) {
/* 122 */     return creature.world.rand.nextInt(30) + 60;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIFollowLeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */