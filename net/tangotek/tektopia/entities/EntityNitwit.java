/*     */ package net.tangotek.tektopia.entities;
/*     */ 
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIGenericMove;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityNitwit
/*     */   extends EntityVillagerTek
/*     */ {
/*  20 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityNitwit.class);
/*     */   
/*     */   static {
/*  23 */     EntityVillagerTek.setupAnimations(animHandler, "nitwit_m");
/*     */   }
/*     */   
/*     */   public EntityNitwit(World worldIn) {
/*  27 */     super(worldIn, ProfessionType.NITWIT, VillagerRole.VILLAGER.value);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/*  47 */     return animHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setupServerJobs() {
/*  52 */     super.setupServerJobs();
/*     */ 
/*     */     
/*  55 */     addJob(new TickJob(1200, 0, true, () -> {
/*     */             if (!isSleeping()) {
/*     */               modifyHappy(-1);
/*     */             }
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void modifyHappy(int delta) {
/*  64 */     if (delta > 0) {
/*  65 */       super.modifyHappy(Math.max(delta / 3, 1));
/*     */     } else {
/*  67 */       super.modifyHappy(delta);
/*     */     } 
/*     */   }
/*     */   protected void initEntityAI() {
/*  71 */     super.initEntityAI();
/*  72 */     this.tasks.addTask(50, (EntityAIBase)new EntityAIGenericMove(this, p -> (p.isWorkTime() && p.hasVillage()), v -> this.village.getLastVillagerPos(), EntityVillagerTek.MovementMode.WALK, null, null));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  77 */     super.entityInit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addTask(int priority, EntityAIBase task) {
/*  84 */     if (task instanceof net.tangotek.tektopia.entities.ai.EntityAIReadBook) {
/*     */       return;
/*     */     }
/*  87 */     super.addTask(priority, task);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applySkillsTo(EntityVillagerTek target) {
/*  92 */     super.applySkillsTo(target);
/*     */ 
/*     */ 
/*     */     
/*  96 */     int intel = getIntelligence();
/*     */     
/*  98 */     int jumpStartSkill = intel / 2 + getRNG().nextInt(intel) / 2;
/*  99 */     if (jumpStartSkill > target.getSkill(target.getProfessionType())) {
/* 100 */       target.setSkill(target.getProfessionType(), jumpStartSkill);
/*     */     }
/* 102 */     debugOut("Nitwit converted to " + (target.getProfessionType()).name + " with " + jumpStartSkill + " intelligence");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 107 */     super.onUpdate();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityNitwit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */