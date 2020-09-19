/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.ai.EntityAITarget;
/*    */ import net.minecraft.pathfinding.Path;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ public class EntityAIProtectVillage
/*    */   extends EntityAITarget {
/*    */   EntityVillagerTek villager;
/*    */   
/*    */   public EntityAIProtectVillage(EntityVillagerTek villager, Predicate<EntityVillagerTek> shouldPred) {
/* 18 */     super((EntityCreature)villager, false, false);
/* 19 */     this.villager = villager;
/* 20 */     setMutexBits(1);
/* 21 */     this.shouldPred = shouldPred;
/*    */   }
/*    */ 
/*    */   
/*    */   EntityLivingBase villageEnemy;
/*    */   protected final Predicate<EntityVillagerTek> shouldPred;
/*    */   
/*    */   public boolean shouldExecute() {
/* 29 */     if (this.villager.hasVillage() && this.shouldPred.test(this.villager)) {
/* 30 */       this.villageEnemy = this.villager.getVillage().getEnemyTarget(this.villager);
/* 31 */       if (this.villageEnemy != null && isSuitableTarget(this.villageEnemy, false)) {
/*    */         
/* 33 */         Path path = this.villager.getPathFinder().findPath((IBlockAccess)this.villager.world, (EntityLiving)this.villager, (Entity)this.villageEnemy, 240.0F);
/* 34 */         if (path != null) {
/* 35 */           return true;
/*    */         }
/*    */       } 
/*    */     } 
/* 39 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected double getTargetDistance() {
/* 44 */     return 240.0D;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 52 */     this.villager.setAttackTarget(this.villageEnemy);
/* 53 */     super.startExecuting();
/*    */   }
/*    */   
/*    */   public boolean shouldContinueExecuting() {
/* 57 */     if (this.villager.isAITick() && this.villager.hasVillage())
/*    */     {
/* 59 */       if (this.villageEnemy != this.villager.getVillage().getEnemyTarget(this.villager)) {
/* 60 */         return false;
/*    */       }
/*    */     }
/*    */     
/* 64 */     return super.shouldContinueExecuting();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIProtectVillage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */