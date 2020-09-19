/*     */ package net.tangotek.tektopia;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.event.world.BlockEvent;
/*     */ 
/*     */ public class FarmFinder {
/*  15 */   private List<VillageFarm> farms = new ArrayList<>();
/*  16 */   private Random rng = new Random();
/*     */   
/*     */   protected World world;
/*     */   private Village village;
/*     */   private double totalFarmWeight;
/*     */   private int debugTick;
/*     */   
/*     */   public FarmFinder(World w, Village v) {
/*  24 */     this.world = w;
/*  25 */     this.village = v;
/*     */   }
/*     */   
/*     */   public BlockPos getFarmland(Predicate<BlockPos> pred) {
/*  29 */     double roll = this.world.rand.nextDouble() * this.totalFarmWeight;
/*  30 */     for (VillageFarm farm : this.farms) {
/*  31 */       if (farm.getWeightScore() >= roll) {
/*  32 */         BlockPos dirtPos = farm.getFarmland(pred);
/*  33 */         if (dirtPos != null) {
/*  34 */           return dirtPos;
/*     */         }
/*     */       } 
/*  37 */       roll -= farm.getWeightScore();
/*     */     } 
/*     */     
/*  40 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getMaxAgeCrop() {
/*  45 */     double roll = this.world.rand.nextDouble() * this.totalFarmWeight;
/*  46 */     for (VillageFarm farm : this.farms) {
/*  47 */       if (farm.getWeightScore() >= roll) {
/*  48 */         BlockPos cropPos = farm.getMaxAgeCrop();
/*  49 */         if (cropPos != null)
/*  50 */           return cropPos; 
/*     */       } 
/*  52 */       roll -= farm.getWeightScore();
/*     */     } 
/*     */     
/*  55 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  60 */     for (VillageFarm curFarm : this.farms) {
/*  61 */       curFarm.update(this.village.getCenter());
/*     */     }
/*     */ 
/*     */     
/*  65 */     for (int i = 0; i < 15 && 
/*  66 */       !findRandomFarm(0.3F) && !findRandomFarm(0.6F) && !findRandomFarm(1.0F); i++);
/*     */   }
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
/*     */ 
/*     */   
/*     */   private boolean findRandomFarm(float distanceScale) {
/*  81 */     int distance = (int)(this.village.getSize() * distanceScale);
/*  82 */     int height = (int)(20.0F * distanceScale);
/*     */ 
/*     */     
/*  85 */     BlockPos testPos = new BlockPos((this.village.getCenter().getX() + distance - this.rng.nextInt(distance * 2)), (this.village.getAABB()).minY - height + this.rng.nextInt((int)(this.village.getAABB()).maxY - (int)(this.village.getAABB()).minY + height * 2), (this.village.getCenter().getZ() + distance - this.rng.nextInt(distance * 2)));
/*  86 */     if (VillageFarm.isFarmLand(this.world, testPos)) {
/*     */       
/*  88 */       for (VillageFarm farm : this.farms) {
/*  89 */         if (farm.isBlockInside(testPos)) {
/*  90 */           return false;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  96 */       VillageFarm newFarm = new VillageFarm(this.world, testPos, this.village);
/*  97 */       this.farms.add(newFarm);
/*     */ 
/*     */       
/* 100 */       this.farms.removeIf(f -> isFullyContained(f));
/*     */ 
/*     */       
/* 103 */       this.totalFarmWeight = this.farms.stream().mapToDouble(v -> v.getWeightScore()).sum();
/*     */       
/* 105 */       Collections.sort(this.farms, Comparator.comparingDouble(VillageFarm::getWeightScore));
/*     */       
/* 107 */       return true;
/*     */     } 
/*     */     
/* 110 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isFullyContained(VillageFarm farm) {
/* 114 */     for (VillageFarm f : this.farms) {
/* 115 */       if (f != farm && boxContainsBox(f.getAABB(), farm.getAABB())) {
/* 116 */         return true;
/*     */       }
/*     */     } 
/* 119 */     return false;
/*     */   }
/*     */   
/*     */   private boolean boxContainsBox(AxisAlignedBB b1, AxisAlignedBB b2) {
/* 123 */     if (b1.minX <= b2.minX && b1.maxX >= b2.maxX && b1.minZ <= b2.minZ && b1.maxZ >= b2.maxZ && b1.minY <= b2.minY && b1.maxY >= b2.maxY)
/*     */     {
/*     */       
/* 126 */       return true;
/*     */     }
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   public void onCropGrowEvent(BlockEvent.CropGrowEvent event) {
/* 132 */     for (VillageFarm farm : this.farms)
/* 133 */       farm.onCropGrowEvent(event); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\FarmFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */