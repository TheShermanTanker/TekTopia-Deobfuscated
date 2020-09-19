/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.IEntityLivingData;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.entities.EntityArchitect;
/*    */ import net.tangotek.tektopia.entities.EntityTradesman;
/*    */ import net.tangotek.tektopia.entities.EntityVendor;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ import net.tangotek.tektopia.tickjob.TickJob;
/*    */ 
/*    */ public class VillageStructureTownHall
/*    */   extends VillageStructure
/*    */ {
/*    */   protected VillageStructureTownHall(World world, Village v, EntityItemFrame itemFrame) {
/* 21 */     super(world, v, itemFrame, VillageStructureType.TOWNHALL, "Town Hall");
/*    */   }
/*    */   
/*    */   protected void onFloorScanEnd() {
/* 25 */     super.onFloorScanEnd();
/*    */     
/* 27 */     if (this.village.isValid() && this.village.getResidentCount() < 1 && !this.village.getTownData().isEmpty() && !this.village.getTownData().completedStartingGifts()) {
/* 28 */       this.village.getTownData().executeStartingGifts(this.world, this.village, this.safeSpot);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setupServerJobs() {
/* 35 */     addJob(new TickJob(200, 0, true, () -> {
/*    */             trySpawnVendor(EntityArchitect.class, ());
/*    */             
/*    */             trySpawnVendor(EntityTradesman.class, ());
/*    */           }));
/* 40 */     super.setupServerJobs();
/*    */   }
/*    */   
/*    */   private <T extends EntityVendor> void trySpawnVendor(Class<T> clazz, Function<World, T> createFunc) {
/* 44 */     List<T> merchantList = this.world.getEntitiesWithinAABB(clazz, getAABB().grow(2.0D, 3.0D, 2.0D));
/*    */     
/* 46 */     while (merchantList.size() > 1) {
/* 47 */       ((EntityVendor)merchantList.get(0)).setDead();
/* 48 */       merchantList.remove(0);
/*    */     } 
/*    */     
/* 51 */     if (merchantList.isEmpty()) {
/* 52 */       BlockPos pos = getRandomFloorTile();
/* 53 */       if (pos != null) {
/* 54 */         EntityVendor entityVendor = (EntityVendor)createFunc.apply(this.world);
/* 55 */         entityVendor.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
/* 56 */         entityVendor.onInitialSpawn(this.world.getDifficultyForLocation(pos), (IEntityLivingData)null);
/* 57 */         this.world.spawnEntity((Entity)entityVendor);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldVillagerSit(EntityVillagerTek villager) {
/* 64 */     return (this.world.rand.nextInt(3) == 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSitTime(EntityVillagerTek villager) {
/* 69 */     return 100 + villager.getRNG().nextInt(300);
/*    */   }
/*    */   
/*    */   public int getMaxAllowed() {
/* 73 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureTownHall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */