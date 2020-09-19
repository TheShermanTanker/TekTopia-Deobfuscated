/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.passive.EntityAnimal;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.util.math.Vec3i;
/*    */ import net.tangotek.tektopia.entities.EntityNecromancer;
/*    */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*    */ import net.tangotek.tektopia.structures.VillageStructure;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ public class EntityAINecroMove extends EntityAIMoveToBlock {
/*    */   protected final EntityNecromancer necro;
/* 15 */   private int failedPath = 0;
/*    */   
/*    */   public EntityAINecroMove(EntityNecromancer n) {
/* 18 */     super((EntityVillageNavigator)n);
/* 19 */     this.necro = n;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 24 */     if (this.navigator.isAITick() && this.navigator.hasVillage() && !this.navigator.hasPath()) {
/* 25 */       return super.shouldExecute();
/*    */     }
/* 27 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected BlockPos getDestinationBlock() {
/* 33 */     if (this.necro.isAngry() && this.necro.getRNG().nextInt(3) == 0) {
/* 34 */       List<VillageStructure> animalePens = this.necro.getVillage().getStructures(new VillageStructureType[] { VillageStructureType.COW_PEN, VillageStructureType.SHEEP_PEN, VillageStructureType.CHICKEN_COOP, VillageStructureType.PIG_PEN });
/* 35 */       if (!animalePens.isEmpty()) {
/*    */         
/* 37 */         animalePens.removeIf(p -> (p.getEntitiesInside(EntityAnimal.class).size() <= 0));
/* 38 */         VillageStructure closestPen = animalePens.stream().min(Comparator.comparing(p -> Double.valueOf(p.getDoor().distanceSq((Vec3i)this.navigator.getPosition())))).orElse(null);
/* 39 */         if (closestPen != null) {
/* 40 */           return closestPen.getDoor();
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 45 */     return this.navigator.getVillage().getLastVillagerPos();
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 50 */     super.startExecuting();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void updateMovementMode() {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onPathFailed(BlockPos pos) {
/* 60 */     this.failedPath++;
/* 61 */     if (this.failedPath > 50) {
/* 62 */       this.necro.debugOut("Killed Necro. Too many failed pathfindings");
/* 63 */       this.necro.setDead();
/*    */     } 
/*    */     
/* 66 */     super.onPathFailed(pos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isNearWalkPos() {
/* 71 */     return (this.necro.getPosition().distanceSq((Vec3i)this.destinationPos) < 4.0D);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onArrival() {
/* 77 */     super.onArrival();
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 82 */     super.resetTask();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAINecroMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */