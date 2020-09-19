/*    */ package net.tangotek.tektopia.entities.ai;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.entity.ai.EntityAIBase;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ public class EntityAICreateItem
/*    */   extends EntityAIBase
/*    */ {
/*    */   protected final Predicate<EntityVillagerTek> shouldPred;
/*    */   protected final Supplier<ItemStack> supplier;
/*    */   protected final EntityVillagerTek villager;
/*    */   
/*    */   public EntityAICreateItem(EntityVillagerTek v, Predicate<EntityVillagerTek> shouldPred, Supplier<ItemStack> supplier) {
/* 17 */     this.villager = v;
/* 18 */     this.shouldPred = shouldPred;
/* 19 */     this.supplier = supplier;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 24 */     if (this.villager.isAITick() && this.villager.hasVillage() && this.shouldPred.test(this.villager)) {
/* 25 */       return true;
/*    */     }
/* 27 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldContinueExecuting() {
/* 32 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 37 */     this.villager.getInventory().addItem(this.supplier.get());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAICreateItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */