/*    */ package net.tangotek.tektopia;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.IEntityLivingData;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.caps.IVillageData;
/*    */ import net.tangotek.tektopia.entities.EntityNomad;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NomadScheduler
/*    */ {
/*    */   protected final World world;
/*    */   protected final Village village;
/*    */   private boolean dayReset = false;
/*    */   
/*    */   public NomadScheduler(World w, Village v) {
/* 23 */     this.world = w;
/* 24 */     this.village = v;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean spawnCheck(int villagerCount) {
/* 30 */     if (villagerCount <= 3) {
/* 31 */       return true;
/*    */     }
/* 33 */     int sqrtClamp = (int)Math.sqrt(villagerCount);
/* 34 */     if (this.world.rand.nextInt(sqrtClamp) == 0) {
/* 35 */       return true;
/*    */     }
/*    */     
/* 38 */     return false;
/*    */   }
/*    */   
/*    */   public void update() {
/* 42 */     IVillageData vd = this.village.getTownData();
/* 43 */     if (vd != null && this.village.isValid() && 
/* 44 */       !Village.isNightTime(this.world)) {
/* 45 */       this.dayReset = false;
/* 46 */       if (!vd.getNomadsCheckedToday()) {
/* 47 */         vd.setNomadsCheckedToday(true);
/* 48 */         int villagerCount = this.village.getResidentCount();
/* 49 */         if (spawnCheck(villagerCount)) {
/* 50 */           BlockPos pos = this.village.getEdgeNode();
/* 51 */           if (pos != null) {
/*    */             
/* 53 */             spawnNomad(pos);
/*    */ 
/*    */             
/* 56 */             if (villagerCount <= 1) {
/* 57 */               spawnNomad(pos);
/*    */             }
/* 59 */             int count = 0;
/* 60 */             while (this.world.rand.nextInt(10) < 5 && count < 5) {
/* 61 */               spawnNomad(pos);
/* 62 */               count++;
/*    */             } 
/*    */           } else {
/*    */             
/* 66 */             this.village.sendChatMessage("Could not find a spawn location for Nomad.");
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetDay() {
/* 75 */     if (!this.dayReset) {
/* 76 */       IVillageData vd = this.village.getTownData();
/* 77 */       if (vd != null && this.village.isValid()) {
/* 78 */         vd.setNomadsCheckedToday(false);
/*    */       }
/* 80 */       this.dayReset = true;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void spawnNomad(BlockPos pos) {
/* 85 */     EntityNomad nomad = new EntityNomad(this.world);
/* 86 */     nomad.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
/* 87 */     nomad.onInitialSpawn(this.world.getDifficultyForLocation(pos), (IEntityLivingData)null);
/* 88 */     this.world.spawnEntity((Entity)nomad);
/* 89 */     this.village.debugOut("Spawning Nomad at " + pos);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\NomadScheduler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */