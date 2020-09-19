/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class EntityAIPickUpItem
/*     */   extends EntityAIMoveToBlock
/*     */ {
/*     */   protected final EntityVillagerTek villager;
/*     */   private List<PickUpData> pickUps;
/*     */   protected EntityItem pickUpItem;
/*     */   private final int chance;
/*     */   
/*     */   public static class PickUpData {
/*     */     public final int quantity;
/*     */     public final ItemStack itemStack;
/*     */     public final String aiFilter;
/*     */     
/*     */     public PickUpData(ItemStack itemStack, int quantity, String aiFilter) {
/*  27 */       this.itemStack = itemStack;
/*  28 */       this.quantity = quantity;
/*  29 */       this.aiFilter = aiFilter;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityAIPickUpItem(EntityVillagerTek v, List<PickUpData> pickUpCounts, int chance) {
/*  36 */     super((EntityVillageNavigator)v);
/*  37 */     this.villager = v;
/*  38 */     this.pickUps = pickUpCounts;
/*  39 */     this.chance = chance;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  44 */     if (this.villager.isAITick() && this.navigator.hasVillage() && this.villager.isWorkTime() && 
/*  45 */       this.villager.getRNG().nextInt(this.chance) == 0) {
/*  46 */       return super.shouldExecute();
/*     */     }
/*     */     
/*  49 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  54 */     List<EntityItem> entityItems = this.villager.world.getEntitiesWithinAABB(EntityItem.class, this.villager.getEntityBoundingBox().grow(20.0D, 10.0D, 20.0D), shouldPickUp(this.villager));
/*  55 */     this.pickUpItem = entityItems.stream().min(Comparator.comparing(e -> Double.valueOf(e.getDistanceSq((Entity)this.villager)))).orElse(null);
/*  56 */     if (this.pickUpItem != null) {
/*  57 */       return this.pickUpItem.getPosition();
/*     */     }
/*     */     
/*  60 */     return null;
/*     */   }
/*     */   
/*     */   private Predicate<EntityItem> shouldPickUp(EntityVillagerTek villager) {
/*  64 */     return e -> {
/*     */         if (this.villager.getVillage().getPathingGraph().isInGraph(e.getPosition())) {
/*     */           for (PickUpData pickUp : this.pickUps) {
/*     */             if (villager.isAIFilterEnabled(pickUp.aiFilter) && e.getItem().isItemEqual(pickUp.itemStack)) {
/*     */               int itemCount = villager.getInventory().getItemCount(());
/*     */               if (itemCount < pickUp.quantity) {
/*     */                 return true;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         }
/*     */         return false;
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/*  83 */     return this.destinationPos;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  88 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  93 */     if (this.pickUpItem.isEntityAlive()) {
/*  94 */       return super.shouldContinueExecuting();
/*     */     }
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/* 101 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 106 */     this.villager.pickupItems(4);
/* 107 */     super.onArrival();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 112 */     this.pickUpItem = null;
/* 113 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIPickUpItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */