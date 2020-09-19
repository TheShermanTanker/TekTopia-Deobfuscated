/*    */ package net.tangotek.tektopia.entities;
/*    */ 
/*    */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*    */ import net.minecraft.entity.ai.EntityAIBase;
/*    */ import net.minecraft.network.datasync.DataParameter;
/*    */ import net.minecraft.network.datasync.DataSerializers;
/*    */ import net.minecraft.network.datasync.EntityDataManager;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.ModSoundEvents;
/*    */ import net.tangotek.tektopia.ProfessionType;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ import net.tangotek.tektopia.VillagerRole;
/*    */ import net.tangotek.tektopia.entities.ai.EntityAIPerformTavern;
/*    */ import net.tangotek.tektopia.entities.ai.EntityAIPerformWander;
/*    */ 
/*    */ public class EntityBard extends EntityVillagerTek {
/* 17 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityBard.class);
/*    */   
/* 19 */   private static final DataParameter<Byte> PERFORMANCE = EntityDataManager.createKey(EntityBard.class, DataSerializers.BYTE);
/* 20 */   private static final DataParameter<Boolean> PERFORM_WANDER = EntityDataManager.createKey(EntityBard.class, DataSerializers.BOOLEAN);
/* 21 */   private static final DataParameter<Boolean> PERFORM_TAVERN = EntityDataManager.createKey(EntityBard.class, DataSerializers.BOOLEAN);
/* 22 */   private int lastPerformanceTick = 0;
/*    */   
/*    */   static {
/* 25 */     animHandler.addAnim("tektopia", "villager_flute_1", "bard_m", true);
/* 26 */     EntityVillagerTek.setupAnimations(animHandler, "bard_m");
/*    */   }
/*    */   
/*    */   public EntityBard(World worldIn) {
/* 30 */     super(worldIn, ProfessionType.BARD, VillagerRole.VILLAGER.value);
/*    */   }
/*    */   
/*    */   public AnimationHandler getAnimationHandler() {
/* 34 */     return animHandler;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void entityInit() {
/* 40 */     super.entityInit();
/* 41 */     registerAIFilter("perform_wander", PERFORM_WANDER);
/* 42 */     registerAIFilter("perform_tavern", PERFORM_TAVERN);
/*    */     
/* 44 */     this.dataManager.register(PERFORMANCE, Byte.valueOf((byte)0));
/*    */   }
/*    */   
/*    */   protected void initEntityAI() {
/* 48 */     super.initEntityAI();
/*    */     
/* 50 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIPerformTavern(this));
/* 51 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIPerformWander(this));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addTask(int priority, EntityAIBase task) {
/* 60 */     if (task instanceof net.tangotek.tektopia.entities.ai.EntityAIWanderStructure) {
/*    */       return;
/*    */     }
/* 63 */     super.addTask(priority, task);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPerforming() {
/* 68 */     return (((Byte)this.dataManager.get(PERFORMANCE)).byteValue() != 0);
/*    */   }
/*    */   
/*    */   public ModSoundEvents.Performance getPerformance() {
/* 72 */     Byte perf = (Byte)this.dataManager.get(PERFORMANCE);
/* 73 */     return ModSoundEvents.Performance.valueOf(perf.byteValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPerformance(ModSoundEvents.Performance perf) {
/* 78 */     if (perf == null) {
/* 79 */       this.dataManager.set(PERFORMANCE, Byte.valueOf((byte)0));
/* 80 */       this.lastPerformanceTick = this.ticksExisted;
/*    */     } else {
/*    */       
/* 83 */       this.dataManager.set(PERFORMANCE, Byte.valueOf(perf.id));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean wantsTavern() {
/* 89 */     return hasTavern();
/*    */   }
/*    */   
/*    */   public int timeSincePerformance() {
/* 93 */     return this.ticksExisted - this.lastPerformanceTick;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 98 */     super.onUpdate();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityBard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */