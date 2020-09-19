/*     */ package net.tangotek.tektopia;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.text.ITextComponent;
/*     */ import net.minecraft.util.text.TextComponentString;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.entities.EntityNecromancer;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ import net.tangotek.tektopia.tickjob.TickJobQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RaidScheduler
/*     */ {
/*     */   protected final World world;
/*     */   protected final Village village;
/*     */   private boolean forceFail = false;
/*  27 */   private int forceRaid = -1;
/*     */   
/*  29 */   protected TickJobQueue jobs = new TickJobQueue();
/*     */   
/*     */   public RaidScheduler(World w, Village v) {
/*  32 */     this.world = w;
/*  33 */     this.village = v;
/*  34 */     setupJobs();
/*     */   }
/*     */   
/*     */   private void setupJobs() {
/*  38 */     this.jobs.addJob(new TickJob(3000, 1200, true, () -> checkForRaid()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void forceRaid(int raidPoints) {
/*  43 */     this.forceRaid = raidPoints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkForRaid() {
/*  52 */     if (this.village.isValid() && this.world.getDifficulty().getId() > 0) {
/*  53 */       int residentCount = this.village.getResidentCount();
/*  54 */       if (residentCount < 8 || this.forceFail) {
/*     */         return;
/*     */       }
/*  57 */       int chance = Math.min(residentCount / 10, 5);
/*     */ 
/*     */       
/*  60 */       if (Village.isNightTime(this.world)) {
/*  61 */         chance += 3;
/*     */       }
/*  63 */       if (this.world.rand.nextInt(180) < chance) {
/*  64 */         VillageStructure townHall = this.village.getNearestStructure(VillageStructureType.TOWNHALL, this.village.getOrigin());
/*  65 */         if (townHall != null) {
/*  66 */           EntityItemFrame frame = townHall.getItemFrame();
/*  67 */           if (frame != null && frame.getAnalogOutput() != 1) {
/*  68 */             this.village.debugOut("Necromancer raid blocked by town hall marker rotation.");
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*  73 */         if (this.world.getEntitiesWithinAABB(EntityNecromancer.class, (new AxisAlignedBB(this.village.getOrigin())).grow(240.0D)).isEmpty()) {
/*  74 */           this.village.debugOut("========RAID SPAWN ======== Village( " + this.village.getOrigin() + " )");
/*  75 */           int raidPoints = calcRaidPoints();
/*  76 */           spawnRaid(raidPoints);
/*     */         } else {
/*     */           
/*  79 */           this.village.debugOut("========RAID Failed. Necro already in Village ======== Village( " + this.village.getOrigin() + " )");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int calcRaidPoints() {
/*  87 */     int residentCount = this.village.getResidentCount();
/*  88 */     int difficultyFactor = (int)(residentCount * 0.15D * this.world.getDifficulty().getId());
/*  89 */     int guassianFactor = (int)(this.world.rand.nextGaussian() * (residentCount / 5));
/*  90 */     int rp = residentCount + difficultyFactor + guassianFactor;
/*  91 */     this.village.debugOut("================================================================");
/*  92 */     this.village.debugOut("================================================================");
/*  93 */     this.village.debugOut("================================================================");
/*  94 */     this.village.debugOut("================================================================");
/*  95 */     this.village.debugOut("Village Raid Points [" + rp + "] (Population: " + residentCount + ") + (Difficulty: " + difficultyFactor + ") + (Random: " + guassianFactor + ")");
/*  96 */     this.village.debugOut("================================================================");
/*  97 */     this.village.debugOut("================================================================");
/*  98 */     this.village.debugOut("================================================================");
/*  99 */     this.village.debugOut("================================================================");
/* 100 */     return rp;
/*     */   }
/*     */   
/*     */   private void spawnRaid(int raidPoints) {
/* 104 */     BlockPos spawnPos = this.village.getEdgeNode();
/* 105 */     if (spawnPos != null) {
/*     */ 
/*     */       
/* 108 */       EntityNecromancer necro = new EntityNecromancer(this.world);
/* 109 */       necro.setLocationAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0.0F, 0.0F);
/* 110 */       necro.onInitialSpawn(this.world.getDifficultyForLocation(spawnPos), (IEntityLivingData)null);
/* 111 */       necro.enablePersistence();
/* 112 */       necro.setLevel(raidPoints / 10);
/*     */       
/* 114 */       this.village.playEvent(ModSoundEvents.deathSummon, (ITextComponent)new TextComponentString("A Necromancer approaches the village!"));
/*     */       
/* 116 */       int extraZombies = raidPoints / 10;
/*     */ 
/*     */       
/* 119 */       int witherSkel = Math.min((raidPoints - 50) / 10, 12);
/* 120 */       for (int w = 0; w < witherSkel; w++) {
/* 121 */         this.world.spawnEntity((Entity)necro.createWitherSkeleton(necro.getPosition()));
/* 122 */         extraZombies -= 2;
/*     */       } 
/* 124 */       this.village.debugOut("    Spawned " + witherSkel + " extra wither skeletons");
/*     */ 
/*     */       
/* 127 */       extraZombies = Math.min(extraZombies, 10);
/* 128 */       for (int i = 0; i < extraZombies; i++) {
/* 129 */         EntityZombie zombie = necro.createZombie(necro.getPosition());
/* 130 */         this.world.spawnEntity((Entity)zombie);
/*     */       } 
/*     */       
/* 133 */       this.world.spawnEntity((Entity)necro);
/* 134 */       this.village.debugOut("    Spawned " + extraZombies + " extra zombies");
/* 135 */       this.village.debugOut("    Spawned Necromancer level: " + necro.getLevel());
/*     */     } else {
/*     */       
/* 138 */       this.village.debugOut("Could not find a spawn location for raid.   Village( " + this.village.getOrigin() + " )");
/*     */     } 
/*     */ 
/*     */     
/* 142 */     this.forceFail = true;
/* 143 */     this.jobs.addJob(new TickJob(12000, 0, false, () -> this.forceFail = false));
/*     */   }
/*     */   
/*     */   public void update() {
/* 147 */     this.jobs.tick();
/*     */     
/* 149 */     if (this.forceRaid >= 0) {
/* 150 */       int rp = this.forceRaid;
/* 151 */       if (this.forceRaid == 0) {
/* 152 */         rp = calcRaidPoints();
/*     */       }
/* 154 */       spawnRaid(rp);
/* 155 */       this.forceRaid = -1;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\RaidScheduler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */