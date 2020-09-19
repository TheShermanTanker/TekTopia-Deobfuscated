/*     */ package net.tangotek.tektopia;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.entity.passive.EntityChicken;
/*     */ import net.minecraft.entity.passive.EntityCow;
/*     */ import net.minecraft.entity.passive.EntityPig;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.caps.IVillageData;
/*     */ import net.tangotek.tektopia.entities.EntityMerchant;
/*     */ 
/*     */ public class MerchantScheduler {
/*     */   protected final World world;
/*  19 */   private List<String> pendingMerchantOrders = new ArrayList<>(); protected final Village village; private boolean dayReset = false;
/*     */   
/*     */   public MerchantScheduler(World w, Village v) {
/*  22 */     this.world = w;
/*  23 */     this.village = v;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean spawnCheck() {
/*  29 */     return true;
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
/*     */ 
/*     */   
/*     */   public void addOrder(String order) {
/*  46 */     this.pendingMerchantOrders.add(order);
/*     */   }
/*     */   
/*     */   public void update() {
/*  50 */     IVillageData vd = this.village.getTownData();
/*  51 */     if (vd != null && this.village.isValid() && 
/*  52 */       !Village.isNightTime(this.world)) {
/*  53 */       this.dayReset = false;
/*  54 */       if (!vd.getMerchantCheckedToday()) {
/*  55 */         vd.setMerchantCheckedToday(true);
/*  56 */         if (spawnCheck()) {
/*  57 */           BlockPos pos = this.village.getEdgeNode();
/*  58 */           if (pos != null) {
/*     */             
/*  60 */             List<EntityMerchant> merchants = this.world.getEntitiesWithinAABB(EntityMerchant.class, this.village.getAABB().grow(120.0D));
/*  61 */             merchants.forEach(m -> m.setDead());
/*     */             
/*  63 */             EntityMerchant merchant = new EntityMerchant(this.world);
/*  64 */             merchant.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
/*  65 */             merchant.onInitialSpawn(this.world.getDifficultyForLocation(pos), (IEntityLivingData)null);
/*  66 */             this.world.spawnEntity((Entity)merchant);
/*  67 */             this.village.debugOut("Spawning merchant at " + pos);
/*     */             
/*  69 */             for (String order : this.pendingMerchantOrders) {
/*     */               
/*  71 */               EntityAnimal animal = null;
/*  72 */               switch (order) {
/*     */                 case "Cow":
/*  74 */                   merchant.addAnimalDelivery((EntityAnimal)new EntityCow(this.world));
/*     */                 
/*     */                 case "Sheep":
/*  77 */                   merchant.addAnimalDelivery((EntityAnimal)new EntitySheep(this.world));
/*     */                 
/*     */                 case "Pig":
/*  80 */                   merchant.addAnimalDelivery((EntityAnimal)new EntityPig(this.world));
/*     */                 
/*     */                 case "Chicken":
/*  83 */                   merchant.addAnimalDelivery((EntityAnimal)new EntityChicken(this.world));
/*     */               } 
/*     */ 
/*     */             
/*     */             } 
/*     */           } else {
/*  89 */             this.village.sendChatMessage("Could not find a spawn location for travelling Merchant.");
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetDay() {
/*  98 */     if (!this.dayReset) {
/*  99 */       IVillageData vd = this.village.getTownData();
/* 100 */       if (vd != null && this.village.isValid()) {
/* 101 */         vd.setMerchantCheckedToday(false);
/*     */       }
/* 103 */       this.dayReset = true;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\MerchantScheduler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */