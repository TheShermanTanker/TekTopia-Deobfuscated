/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class EntityAIDeliverToStorage2
/*     */   extends EntityAIMoveToBlock {
/*     */   private TileEntityChest chest;
/*  15 */   private int dropOffTick = -1;
/*     */   private ItemStack itemCarryingCopy;
/*     */   private EntityVillagerTek villager;
/*  18 */   private int deliveryId = 0;
/*     */   
/*     */   public EntityAIDeliverToStorage2(EntityVillagerTek v) {
/*  21 */     super((EntityVillageNavigator)v);
/*  22 */     this.villager = v;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  27 */     if (this.villager.hasVillage() && !this.itemCarryingCopy.isEmpty()) {
/*  28 */       this.chest = this.villager.getVillage().getAvailableStorageChest(this.itemCarryingCopy, this.villager.getPosition());
/*  29 */       if (this.chest != null) {
/*  30 */         return this.chest.getPos();
/*     */       }
/*     */     } 
/*  33 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  38 */     if ((this.villager.isAITick() || this.villager.isStoragePriority()) && this.villager.isDeliveryTime()) {
/*     */       
/*  40 */       this.deliveryId = this.villager.getDesireSet().getDeliveryId(this.villager, this.villager.isStoragePriority() ? 1 : 3);
/*  41 */       if (this.deliveryId > 0) {
/*  42 */         this.itemCarryingCopy = this.villager.getDesireSet().getDeliveryItemCopy(this.villager);
/*  43 */         return super.shouldExecute();
/*     */       } 
/*     */     } 
/*     */     
/*  47 */     return false;
/*     */   }
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  51 */     if (this.dropOffTick >= 0) {
/*  52 */       return true;
/*     */     }
/*  54 */     if (!this.villager.getDesireSet().isDeliveryMatch(this.deliveryId)) {
/*  55 */       return false;
/*     */     }
/*  57 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/*  62 */     BlockPos result = super.findWalkPos();
/*  63 */     if (result == null) {
/*  64 */       BlockPos pos = this.destinationPos;
/*     */ 
/*     */       
/*  67 */       BlockPos testPos = pos.west(2);
/*  68 */       if (isWalkable(testPos, this.navigator)) {
/*  69 */         return testPos;
/*     */       }
/*  71 */       testPos = pos.east(2);
/*  72 */       if (isWalkable(testPos, this.navigator)) {
/*  73 */         return testPos;
/*     */       }
/*  75 */       testPos = pos.north(2);
/*  76 */       if (isWalkable(testPos, this.navigator)) {
/*  77 */         return testPos;
/*     */       }
/*  79 */       testPos = pos.south(2);
/*  80 */       if (isWalkable(testPos, this.navigator)) {
/*  81 */         return testPos;
/*     */       }
/*     */     } 
/*  84 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/*  89 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  94 */     super.startExecuting();
/*     */ 
/*     */     
/*  97 */     if (this.itemCarryingCopy != null)
/*  98 */       this.villager.equipActionItem(this.itemCarryingCopy); 
/*     */   }
/*     */   
/*     */   public boolean isInterruptible() {
/* 102 */     return (this.dropOffTick <= 0);
/*     */   }
/*     */   
/*     */   public void updateTask() {
/* 106 */     super.updateTask();
/*     */     
/* 108 */     this.dropOffTick--;
/* 109 */     if (this.dropOffTick == 0) {
/* 110 */       dropOffItems();
/* 111 */       this.villager.throttledSadness(-1);
/* 112 */       closeChest();
/* 113 */       this.dropOffTick = -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetTask() {
/* 118 */     this.dropOffTick = -1;
/*     */     
/* 120 */     this.deliveryId = 0;
/* 121 */     super.resetTask();
/*     */     
/* 123 */     if (this.itemCarryingCopy != null) {
/* 124 */       this.villager.unequipActionItem(this.itemCarryingCopy);
/* 125 */       this.itemCarryingCopy = null;
/*     */ 
/*     */ 
/*     */       
/* 129 */       this.villager.setStoragePriority();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 135 */     super.onArrival();
/* 136 */     this.dropOffTick = 30;
/*     */     
/* 138 */     openChest();
/*     */   }
/*     */   
/*     */   protected void dropOffItems() {
/* 142 */     if (isNearDestination(5.0D) && !this.chest.isInvalid()) {
/* 143 */       this.villager.setStoragePriority();
/* 144 */       if (!this.villager.getDesireSet().deliverItems(this.villager, this.chest, this.deliveryId))
/* 145 */         this.villager.getDesireSet().forceUpdate(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void openChest() {
/* 150 */     TileEntity te = this.villager.world.getTileEntity(this.destinationPos);
/* 151 */     if (te instanceof TileEntityChest) {
/* 152 */       TileEntityChest tileEntityChest = (TileEntityChest)te;
/*     */       
/* 154 */       EntityPlayer p = this.villager.world.getClosestPlayer(this.destinationPos.getX(), this.destinationPos.getY(), this.destinationPos.getZ(), -1.0D, EntitySelectors.NOT_SPECTATING);
/* 155 */       if (p != null)
/* 156 */         tileEntityChest.openInventory(p); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closeChest() {
/* 161 */     TileEntity te = this.villager.world.getTileEntity(this.destinationPos);
/* 162 */     if (te instanceof TileEntityChest) {
/* 163 */       TileEntityChest tileEntityChest = (TileEntityChest)te;
/* 164 */       EntityPlayer p = this.villager.world.getClosestPlayer(this.destinationPos.getX(), this.destinationPos.getY(), this.destinationPos.getZ(), -1.0D, EntitySelectors.NOT_SPECTATING);
/* 165 */       if (p != null)
/* 166 */         tileEntityChest.closeInventory(p); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIDeliverToStorage2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */