/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIGenericMove;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ public class EntityNomad extends EntityVillagerTek {
/*  20 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityNomad.class);
/*     */   private BlockPos firstCheck;
/*  22 */   private BlockPos spawnPos = null;
/*     */   
/*     */   static {
/*  25 */     EntityVillagerTek.setupAnimations(animHandler, "nomad_m");
/*     */   }
/*     */   
/*     */   public EntityNomad(World worldIn) {
/*  29 */     super(worldIn, ProfessionType.NOMAD, VillagerRole.VISITOR.value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setupServerJobs() {
/*  34 */     super.setupServerJobs();
/*  35 */     addJob(new TickJob(20, 0, false, () -> postSetup()));
/*  36 */     addJob(new TickJob(100, 0, false, () -> prepStuck()));
/*  37 */     addJob(new TickJob(400, 0, false, () -> checkStuck()));
/*  38 */     addJob(new TickJob(50, 0, true, () -> { if (isSleepingTime())
/*  39 */               setDead();  })); addJob(new TickJob(300, 100, true, () -> {
/*     */             if (!hasVillage() || !getVillage().isValid())
/*     */               setDead(); 
/*     */           }));
/*     */   }
/*     */   
/*     */   private void prepStuck() {
/*  46 */     this.firstCheck = getPosition();
/*     */   }
/*     */   
/*     */   private void checkStuck() {
/*  50 */     if (hasVillage() && this.firstCheck.distanceSq((Vec3i)getPosition()) < 20.0D) {
/*  51 */       this.village.sendChatMessage("Nomad failed to find a way to the village.");
/*  52 */       setDead();
/*     */     } 
/*     */   }
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/*  57 */     return animHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initEntityAI() {
/*  62 */     super.initEntityAI();
/*     */ 
/*     */     
/*  65 */     addTask(50, (EntityAIBase)new EntityAIGenericMove(this, p -> Village.isNightTime(this.world), v -> this.spawnPos, EntityVillagerTek.MovementMode.WALK, null, () -> setDead()));
/*     */ 
/*     */     
/*  68 */     addTask(50, (EntityAIBase)new EntityAIGenericMove(this, p -> (!Village.isNightTime(this.world) && p.hasVillage()), v -> this.village.getLastVillagerPos(), EntityVillagerTek.MovementMode.WALK, null, null));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initEntityAIBase() {}
/*     */   
/*     */   @Nullable
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
/*  76 */     int skillCount = getRNG().nextInt(2) + 1;
/*     */ 
/*     */     
/*  79 */     int profCount = (ProfessionType.values()).length;
/*  80 */     for (int i = 0; i < skillCount; i++) {
/*     */       
/*  82 */       ProfessionType prof = ProfessionType.values()[getRNG().nextInt(profCount)];
/*     */       
/*  84 */       if (prof.canCopy) {
/*  85 */         int newSkill = getBaseSkill(prof) + 3 + getRNG().nextInt(12);
/*  86 */         while (getRNG().nextInt(7) <= 3 && newSkill < 25) {
/*  87 */           newSkill++;
/*     */         }
/*  89 */         setSkill(prof, newSkill);
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     return super.onInitialSpawn(difficulty, livingdata);
/*     */   }
/*     */ 
/*     */   
/*     */   public Predicate<Entity> isHostile() {
/*  98 */     return e -> false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFleeFrom(Entity e) {
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAIMoveSpeed() {
/* 108 */     return super.getAIMoveSpeed() * 0.9F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 113 */     super.onLivingUpdate();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addTask(int priority, EntityAIBase task) {
/* 119 */     if (task instanceof net.tangotek.tektopia.entities.ai.EntityAIWanderStructure && priority <= 100) {
/*     */       return;
/*     */     }
/* 122 */     if (task instanceof net.tangotek.tektopia.entities.ai.EntityAIReadBook) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     super.addTask(priority, task);
/*     */   }
/*     */   
/*     */   private void postSetup() {
/* 134 */     this.spawnPos = getPosition();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addVillagerPosition() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bedCheck() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound compound) {
/* 150 */     super.writeEntityToNBT(compound);
/*     */     
/* 152 */     if (this.spawnPos == null) {
/* 153 */       this.spawnPos = BlockPos.ORIGIN;
/*     */     }
/* 155 */     writeBlockPosNBT(compound, "spawnPos", this.spawnPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound compound) {
/* 160 */     super.readEntityFromNBT(compound);
/*     */     
/* 162 */     this.spawnPos = readBlockPosNBT(compound, "spawnPos");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityNomad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */