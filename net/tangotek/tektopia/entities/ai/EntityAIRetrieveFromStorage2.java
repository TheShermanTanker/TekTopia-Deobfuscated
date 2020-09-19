/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.storage.ItemDesire;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIRetrieveFromStorage2
/*     */   extends EntityAIMoveToBlock
/*     */ {
/*  26 */   private TileEntityChest chest = null;
/*  27 */   protected ItemDesire retrieveDesire = null;
/*     */   protected final EntityVillagerTek villager;
/*  29 */   private int pickUpTick = 0;
/*     */   protected boolean autoCheck = false;
/*  31 */   protected ItemStack itemTaken = null;
/*     */ 
/*     */   
/*     */   public EntityAIRetrieveFromStorage2(EntityVillagerTek v) {
/*  35 */     super((EntityVillageNavigator)v);
/*  36 */     this.villager = v;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  41 */     if (this.chest != null) {
/*  42 */       return this.chest.getPos();
/*     */     }
/*     */     
/*  45 */     return null;
/*     */   }
/*     */   
/*     */   protected void updateMovementMode() {
/*  49 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  54 */     if ((this.villager.isAITick() || this.villager.isStoragePriority()) && this.villager.hasVillage()) {
/*  55 */       this.autoCheck = false;
/*  56 */       this.retrieveDesire = this.villager.getDesireSet().getNeededDesire(this.villager);
/*  57 */       if (this.retrieveDesire != null && this.villager.getInventory().hasSlotFree()) {
/*  58 */         this.chest = this.retrieveDesire.getPickUpChest(this.villager);
/*  59 */         if (this.chest != null) {
/*  60 */           return super.shouldExecute();
/*     */         }
/*     */       } 
/*     */     } 
/*  64 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isInterruptible() {
/*  68 */     return (this.pickUpTick <= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  73 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  78 */     if (this.pickUpTick > 0) {
/*  79 */       return true;
/*     */     }
/*  81 */     if (this.chest != this.retrieveDesire.getPickUpChest(this.villager)) {
/*  82 */       return false;
/*     */     }
/*  84 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/*  89 */     BlockPos result = super.findWalkPos();
/*  90 */     if (result == null) {
/*  91 */       BlockPos pos = this.destinationPos;
/*     */ 
/*     */       
/*  94 */       BlockPos testPos = pos.west(2);
/*  95 */       if (isWalkable(testPos, this.navigator)) {
/*  96 */         return testPos;
/*     */       }
/*  98 */       testPos = pos.east(2);
/*  99 */       if (isWalkable(testPos, this.navigator)) {
/* 100 */         return testPos;
/*     */       }
/* 102 */       testPos = pos.north(2);
/* 103 */       if (isWalkable(testPos, this.navigator)) {
/* 104 */         return testPos;
/*     */       }
/* 106 */       testPos = pos.south(2);
/* 107 */       if (isWalkable(testPos, this.navigator)) {
/* 108 */         return testPos;
/*     */       }
/*     */     } 
/* 111 */     return result;
/*     */   }
/*     */   
/*     */   public void updateTask() {
/* 115 */     super.updateTask();
/*     */     
/* 117 */     this.pickUpTick--;
/* 118 */     if (this.pickUpTick == 15) {
/* 119 */       pickUpItems();
/*     */     }
/* 121 */     else if (this.pickUpTick == 1) {
/* 122 */       closeChest();
/*     */     } 
/*     */   }
/*     */   public void resetTask() {
/* 126 */     this.pickUpTick = 0;
/*     */     
/* 128 */     if (this.itemTaken != null) {
/* 129 */       this.villager.unequipActionItem(this.itemTaken);
/* 130 */       this.itemTaken = null;
/*     */ 
/*     */ 
/*     */       
/* 134 */       this.villager.setStoragePriority();
/*     */     } 
/*     */     
/* 137 */     super.resetTask();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 142 */     super.onArrival();
/* 143 */     this.pickUpTick = 30;
/* 144 */     openChest();
/*     */   }
/*     */   
/*     */   protected void pickUpItems() {
/* 148 */     if (isNearDestination(5.0D) && !this.chest.isInvalid()) {
/* 149 */       this.itemTaken = this.retrieveDesire.pickUpItems(this.villager);
/* 150 */       if (this.itemTaken != ItemStack.EMPTY) {
/* 151 */         itemAcquired(this.itemTaken);
/*     */       } else {
/* 153 */         this.villager.getDesireSet().forceUpdate();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   protected boolean itemAcquired(ItemStack itemStack) {
/* 158 */     this.autoCheck = true;
/* 159 */     this.villager.setStoragePriority();
/*     */     
/* 161 */     boolean result = (this.villager.getInventory().addItem(itemStack) == ItemStack.EMPTY);
/* 162 */     return result;
/*     */   }
/*     */   
/*     */   private void openChest() {
/* 166 */     TileEntity te = this.villager.world.getTileEntity(this.destinationPos);
/* 167 */     if (te instanceof TileEntityChest) {
/* 168 */       TileEntityChest tileEntityChest = (TileEntityChest)te;
/*     */ 
/*     */       
/* 171 */       EntityPlayer p = this.villager.world.playerEntities.stream().filter((Predicate<? super EntityPlayer>)EntitySelectors.NOT_SPECTATING).findFirst().orElse(null);
/* 172 */       if (p != null)
/* 173 */         tileEntityChest.openInventory(p); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closeChest() {
/* 178 */     TileEntity te = this.villager.world.getTileEntity(this.destinationPos);
/* 179 */     if (te instanceof TileEntityChest) {
/* 180 */       TileEntityChest tileEntityChest = (TileEntityChest)te;
/* 181 */       EntityPlayer p = this.villager.world.playerEntities.stream().filter((Predicate<? super EntityPlayer>)EntitySelectors.NOT_SPECTATING).findFirst().orElse(null);
/* 182 */       if (p != null)
/* 183 */         tileEntityChest.closeInventory(p); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIRetrieveFromStorage2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */