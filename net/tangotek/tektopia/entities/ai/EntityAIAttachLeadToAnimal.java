/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public abstract class EntityAIAttachLeadToAnimal
/*     */   extends EntityAIFollow {
/*  15 */   protected EntityAnimal targetAnimal = null;
/*     */   private boolean active = false;
/*  17 */   private ItemStack leadItem = new ItemStack(Items.LEAD);
/*  18 */   private int leashTime = 0;
/*     */   private Predicate<EntityVillagerTek> shouldPred;
/*     */   protected final EntityVillagerTek villager;
/*     */   
/*     */   public EntityAIAttachLeadToAnimal(EntityVillagerTek v, Predicate<EntityVillagerTek> shouldPred) {
/*  23 */     super((EntityVillageNavigator)v);
/*  24 */     this.villager = v;
/*  25 */     this.shouldPred = shouldPred;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  30 */     if (this.villager.isAITick() && this.villager.hasVillage() && this.shouldPred != null && this.shouldPred.test(this.villager)) {
/*  31 */       return super.shouldExecute();
/*     */     }
/*     */     
/*  34 */     return false;
/*     */   }
/*     */   
/*     */   public void startExecuting() {
/*  38 */     this.active = true;
/*  39 */     this.villager.equipActionItem(this.leadItem);
/*  40 */     super.startExecuting();
/*     */   }
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  44 */     return (this.targetAnimal != null && this.active);
/*     */   }
/*     */   
/*     */   public void updateTask() {
/*  48 */     if (this.leashTime > 0) {
/*  49 */       this.villager.getLookHelper().setLookPosition(this.destinationPos.getX(), this.destinationPos.getY(), this.destinationPos.getZ(), 30.0F, 30.0F);
/*  50 */       this.leashTime--;
/*  51 */       if (this.leashTime == 0) {
/*  52 */         attachLeash();
/*  53 */         this.active = false;
/*     */       } 
/*     */     } else {
/*     */       
/*  57 */       super.updateTask();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterruptible() {
/*  63 */     return (this.leashTime <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/*  68 */     if (this.targetAnimal.getLeashHolder() instanceof net.tangotek.tektopia.entities.EntityMerchant) {
/*  69 */       this.targetAnimal.clearLeashed(true, false);
/*     */     }
/*  71 */     if (this.targetAnimal.getLeashHolder() != null) {
/*  72 */       this.active = false;
/*     */     } else {
/*  74 */       startLeash();
/*     */     } 
/*  76 */     super.onArrival();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onStuck() {
/*  81 */     this.active = false;
/*  82 */     super.onStuck();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPathFailed(BlockPos pos) {
/*  87 */     this.active = false;
/*  88 */     super.onPathFailed(pos);
/*     */   }
/*     */   
/*     */   protected void attachLeash() {
/*  92 */     this.targetAnimal.setLeashHolder((Entity)this.villager, true);
/*  93 */     this.villager.setLeadAnimal(this.targetAnimal);
/*     */     
/*  95 */     this.villager.throttledSadness(-3);
/*     */ 
/*     */     
/*  98 */     if (this.targetAnimal.getNavigator() instanceof PathNavigateGround) {
/*  99 */       PathNavigateGround navGround = (PathNavigateGround)this.targetAnimal.getNavigator();
/* 100 */       navGround.getNodeProcessor().setCanEnterDoors(true);
/* 101 */       navGround.getNodeProcessor().setCanOpenDoors(true);
/*     */     } 
/* 103 */     this.active = false;
/*     */   }
/*     */   
/*     */   private void startLeash() {
/* 107 */     this.villager.debugOut("Attaching lead to animal [ " + this.targetAnimal.getPositionVector() + "]");
/* 108 */     this.leashTime = 36;
/* 109 */     this.villager.getNavigator().clearPath();
/* 110 */     this.villager.playServerAnimation("villager_take");
/*     */   }
/*     */   
/*     */   private void stopLeash() {
/* 114 */     this.villager.stopServerAnimation("villager_take");
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 119 */     this.leashTime = 0;
/* 120 */     this.active = false;
/* 121 */     this.targetAnimal = null;
/* 122 */     this.villager.unequipActionItem(this.leadItem);
/* 123 */     stopLeash();
/* 124 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIAttachLeadToAnimal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */