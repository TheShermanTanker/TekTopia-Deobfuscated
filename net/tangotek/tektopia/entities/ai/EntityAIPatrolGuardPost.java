/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ import net.tangotek.tektopia.structures.VillageStructure;
/*    */ import net.tangotek.tektopia.structures.VillageStructureGuardPost;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ public class EntityAIPatrolGuardPost
/*    */   extends EntityAIPatrolPoint {
/*    */   protected final EntityVillagerTek villager;
/*    */   protected VillageStructureGuardPost guardPost;
/*    */   
/*    */   public EntityAIPatrolGuardPost(EntityVillagerTek v, Predicate<EntityVillagerTek> shouldPred, int distanceFromPoint, int waitTime) {
/* 18 */     super(v, shouldPred, distanceFromPoint, waitTime);
/* 19 */     this.villager = v;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 24 */     return (this.villager.isAITick("patrol_guard_post") && super.shouldExecute());
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockPos getPatrolPoint() {
/* 29 */     List<VillageStructure> posts = this.villager.getVillage().getStructures(VillageStructureType.GUARD_POST);
/* 30 */     Collections.shuffle(posts);
/*    */     
/* 32 */     long minVisitTime = 400L;
/* 33 */     VillageStructureGuardPost resultPost = null;
/* 34 */     for (int i = 0; i < posts.size(); i++) {
/* 35 */       VillageStructureGuardPost post = (VillageStructureGuardPost)posts.get(i);
/* 36 */       if (!post.isSpecialBlockOccupied(post.getDoor())) {
/* 37 */         long timeSince = post.getTimeSinceVisit();
/* 38 */         if (timeSince > minVisitTime) {
/* 39 */           minVisitTime = timeSince;
/* 40 */           resultPost = post;
/*    */         }
/* 42 */         else if (post == this.guardPost) {
/* 43 */           resultPost = post;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 48 */     this.guardPost = resultPost;
/* 49 */     if (this.guardPost != null) {
/* 50 */       return this.guardPost.getDoor();
/*    */     }
/* 52 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 57 */     super.startExecuting();
/* 58 */     this.villager.throttledSadness(-1);
/* 59 */     if (this.guardPost != null) {
/* 60 */       this.guardPost.occupySpecialBlock(this.guardPost.getDoor());
/*    */     }
/*    */   }
/*    */   
/*    */   public void resetTask() {
/* 65 */     if (this.guardPost != null) {
/* 66 */       this.guardPost.vacateSpecialBlock(this.guardPost.getDoor());
/*    */     }
/* 68 */     super.resetTask();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIPatrolGuardPost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */