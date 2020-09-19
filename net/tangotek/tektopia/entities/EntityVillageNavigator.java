/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.leviathanstudio.craftstudio.client.animation.ClientAnimationHandler;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import com.leviathanstudio.craftstudio.common.animation.IAnimated;
/*     */ import com.leviathanstudio.craftstudio.common.animation.InfoChannel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.util.SoundCategory;
/*     */ import net.minecraft.util.SoundEvent;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.VillageManager;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.pathing.PathFinder;
/*     */ import net.tangotek.tektopia.pathing.PathNavigateVillager2;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ import net.tangotek.tektopia.tickjob.TickJobQueue;
/*     */ 
/*     */ public abstract class EntityVillageNavigator extends EntityCreature implements IAnimated {
/*  34 */   private static final DataParameter<String> ANIM_NAME = EntityDataManager.createKey(EntityVillageNavigator.class, DataSerializers.STRING);
/*     */   
/*     */   protected Village village;
/*  37 */   protected String curAnim = "";
/*  38 */   protected List<AnimationTrigger> animTriggers = new ArrayList<>();
/*     */   protected final TickJobQueue jobs;
/*     */   private int idleTicks;
/*     */   private boolean isWalking;
/*     */   private final int rolesMask;
/*  43 */   private int aiTick = 0;
/*     */   
/*     */   private boolean aiReset = false;
/*     */   
/*     */   private boolean storagePriority = false;
/*     */   private boolean triggeredAnimationRunning = false;
/*     */   
/*     */   private class AnimationTrigger
/*     */   {
/*     */     protected String name;
/*     */     protected int keyFrame;
/*     */     protected Runnable callback;
/*     */     protected boolean triggered = false;
/*  56 */     private float lastFrame = 0.0F;
/*     */     
/*     */     public AnimationTrigger(String name, int keyFrame, Runnable callback) {
/*  59 */       this.name = name;
/*  60 */       this.keyFrame = keyFrame;
/*  61 */       this.callback = callback;
/*     */     }
/*     */     
/*     */     public void update(float curFrame) {
/*  65 */       if (curFrame < this.lastFrame) {
/*  66 */         this.triggered = false;
/*     */       }
/*  68 */       if (curFrame >= this.keyFrame && !this.triggered) {
/*     */         
/*  70 */         this.triggered = true;
/*  71 */         this.callback.run();
/*     */       } 
/*     */       
/*  74 */       this.lastFrame = curFrame;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityVillageNavigator(World worldIn, int rolesMask) {
/*  80 */     super(worldIn);
/*  81 */     this.rolesMask = rolesMask;
/*  82 */     this.jobs = new TickJobQueue();
/*  83 */     if (worldIn.isRemote) {
/*  84 */       setupClientJobs();
/*     */     } else {
/*  86 */       setupServerJobs();
/*     */     } 
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
/*     */   protected PathNavigate createNavigator(World worldIn) {
/* 100 */     return (PathNavigate)new PathNavigateVillager2((EntityLiving)this, this.world, getCanUseDoors());
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
/*     */   public void onAddedToWorld() {
/* 115 */     if (!this.world.isRemote && 
/* 116 */       !this.world.isRemote) {
/* 117 */       debugOut("onAddedToWorld " + getNavigator());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 122 */     super.onAddedToWorld();
/*     */   }
/*     */   
/*     */   protected void applyEntityAttributes() {
/* 126 */     super.applyEntityAttributes();
/* 127 */     this.dataManager.set(ANIM_NAME, "");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/* 133 */     super.entityInit();
/* 134 */     this.dataManager.register(ANIM_NAME, "");
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateClientAnim(String newAnim) {
/* 139 */     ClientAnimationHandler clientAnim = (ClientAnimationHandler)getAnimationHandler();
/* 140 */     Set<String> animKeys = clientAnim.getAnimChannels().keySet();
/* 141 */     animKeys.forEach(a -> clientAnim.stopAnimation(a, this));
/*     */ 
/*     */     
/* 144 */     if (!newAnim.isEmpty()) {
/* 145 */       clientAnim.startAnimation("tektopia", newAnim, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void notifyDataManagerChange(DataParameter<?> key) {
/* 150 */     super.notifyDataManagerChange(key);
/*     */     
/* 152 */     if (isWorldRemote() && 
/* 153 */       ANIM_NAME.equals(key)) {
/* 154 */       updateClientAnim((String)this.dataManager.get(ANIM_NAME));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void setupAnimations(AnimationHandler animHandler, String modelName) {}
/*     */ 
/*     */   
/*     */   public boolean isAITick() {
/* 163 */     if (this.aiTick < 0) {
/* 164 */       this.aiReset = true;
/* 165 */       return true;
/*     */     } 
/*     */     
/* 168 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStoragePriority() {
/* 173 */     return this.storagePriority;
/*     */   }
/*     */   
/*     */   public void setStoragePriority() {
/* 177 */     this.storagePriority = true;
/*     */   }
/*     */   
/*     */   protected void setupClientJobs() {
/* 181 */     this.jobs.clear();
/*     */   }
/*     */   
/*     */   protected void setupServerJobs() {
/* 185 */     this.jobs.clear();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     addJob(new TickJob(50, 100, true, () -> {
/*     */             BlockPos blockPos = new BlockPos((Entity)this);
/*     */             
/*     */             VillageManager vm = VillageManager.get(this.world);
/*     */             Village nearVillage = vm.getNearestVillage(blockPos, 360);
/*     */             if (nearVillage != getVillage()) {
/*     */               detachVillage();
/*     */             }
/* 199 */             boolean inVillageGraph = (nearVillage != null && nearVillage.getPathingGraph().getNearbyBaseNode(getPositionVector(), this.width, this.height, this.width) != null);
/*     */             if (!hasVillage()) {
/*     */               if (inVillageGraph) {
/*     */                 attachToVillage(nearVillage);
/*     */               }
/*     */             } else if (!inVillageGraph) {
/*     */               detachVillage();
/*     */             } 
/*     */             if (!hasVillage()) {
/*     */               detachHome();
/*     */             } else {
/*     */               setHomePosAndDistance(this.village.getOrigin(), -1);
/*     */             } 
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addJob(TickJob job) {
/* 222 */     this.jobs.addJob(job);
/*     */   }
/*     */   
/*     */   public boolean isRole(VillagerRole role) {
/* 226 */     return ((this.rolesMask & role.value) > 0);
/*     */   }
/*     */   protected boolean getCanUseDoors() {
/* 229 */     return true;
/*     */   }
/*     */   public boolean canNavigate() {
/* 232 */     return true;
/*     */   }
/*     */   
/*     */   protected void attachToVillage(Village v) {
/* 236 */     this.village = v;
/* 237 */     debugOut("Attaching to village");
/*     */   }
/*     */   
/*     */   protected void detachVillage() {
/* 241 */     this.village = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debugOut(String text) {
/* 252 */     if (hasVillage())
/* 253 */       getVillage().debugOut(getClass().getSimpleName() + "|" + getDisplayName().getFormattedText() + "|" + getEntityId() + " " + ((text.charAt(0) == ' ') ? text : (" " + text))); 
/*     */   }
/*     */   
/*     */   public void playSound(SoundEvent soundEvent) {
/* 257 */     playSound(soundEvent, getRNG().nextFloat() * 0.4F + 0.8F, getRNG().nextFloat() * 0.4F + 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startMovement() {}
/*     */ 
/*     */   
/*     */   public void updateMovement(boolean arrived) {}
/*     */ 
/*     */   
/*     */   public void resetMovement() {
/* 269 */     this.navigator.clearPath();
/*     */   }
/*     */   
/*     */   public void onLivingUpdate() {
/* 273 */     if (this.storagePriority && isAITick() && hasVillage()) {
/*     */       
/* 275 */       VillageStructure struct = this.village.getNearestStructure(VillageStructureType.STORAGE, getPosition());
/* 276 */       if (struct == null || !struct.isBlockNear(getPosition(), 3.0D)) {
/* 277 */         this.storagePriority = false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 282 */     super.onLivingUpdate();
/*     */     
/* 284 */     if (!isWorldRemote()) {
/* 285 */       this.aiTick--;
/* 286 */       if (this.aiReset) {
/* 287 */         this.aiTick = getRNG().nextInt(10) + 15;
/* 288 */         this.aiReset = false;
/*     */       } 
/*     */     } 
/*     */     
/* 292 */     if (isWorldRemote()) {
/* 293 */       if (this.lastTickPosX == this.posX && this.lastTickPosZ == this.posZ && this.lastTickPosY == this.posY) {
/* 294 */         this.idleTicks++;
/*     */       } else {
/* 296 */         this.idleTicks = 0;
/*     */       } 
/* 298 */       this.isWalking = (this.idleTicks <= 1);
/* 299 */       if (this.isWalking && this.onGround && !this.triggeredAnimationRunning) {
/* 300 */         startWalking();
/*     */       } else {
/* 302 */         stopWalking();
/*     */       } 
/* 304 */       getAnimationHandler().animationsUpdate(this);
/*     */       
/* 306 */       checkAnimationTriggers();
/*     */     } 
/*     */     
/* 309 */     this.jobs.tick();
/*     */   }
/*     */   
/*     */   protected boolean isWalking() {
/* 313 */     return this.isWalking;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   protected void startWalking() {}
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   protected void stopWalking() {}
/*     */ 
/*     */   
/*     */   protected void playClientAnimation(String anim) {
/* 327 */     if (!getAnimationHandler().isAnimationActive("tektopia", anim, this))
/* 328 */       getAnimationHandler().startAnimation("tektopia", anim, this); 
/*     */   }
/*     */   
/*     */   public void stopClientAnimation(String anim) {
/* 332 */     if (getAnimationHandler().isAnimationActive("tektopia", anim, this)) {
/* 333 */       getAnimationHandler().stopAnimation("tektopia", anim, this);
/*     */     }
/*     */   }
/*     */   
/*     */   public SoundCategory getSoundCategory() {
/* 338 */     return SoundCategory.NEUTRAL;
/*     */   }
/*     */   
/*     */   public void stopServerAnimation(String anim) {
/* 342 */     this.dataManager.set(ANIM_NAME, "");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playServerAnimation(String anim) {
/* 348 */     this.dataManager.set(ANIM_NAME, anim);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPlayingAnimation(String anim) {
/* 354 */     return (anim == this.dataManager.get(ANIM_NAME));
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
/*     */   protected void checkAnimationTriggers() {
/* 367 */     ClientAnimationHandler clientAnim = (ClientAnimationHandler)getAnimationHandler();
/* 368 */     Map<InfoChannel, AnimationHandler.AnimInfo> animInfoMap = (Map<InfoChannel, AnimationHandler.AnimInfo>)clientAnim.getCurrentAnimInfo().get(this);
/* 369 */     this.triggeredAnimationRunning = false;
/* 370 */     if (animInfoMap != null) {
/* 371 */       Iterator<Map.Entry<InfoChannel, AnimationHandler.AnimInfo>> it = animInfoMap.entrySet().iterator();
/* 372 */       while (it.hasNext()) {
/* 373 */         Map.Entry<InfoChannel, AnimationHandler.AnimInfo> animInfo = it.next();
/* 374 */         for (AnimationTrigger trigger : this.animTriggers) {
/* 375 */           if (trigger.name.equals(((InfoChannel)animInfo.getKey()).name)) {
/* 376 */             this.triggeredAnimationRunning = true;
/* 377 */             trigger.update(((AnimationHandler.AnimInfo)animInfo.getValue()).currentFrame);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addAnimationTriggerRange(String name, int startFrame, int endFrame, Runnable callback) {
/* 385 */     if (this.world.isRemote) {
/* 386 */       for (int i = startFrame; i <= endFrame; i++) {
/* 387 */         addAnimationTrigger(name, i, callback);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void addAnimationTrigger(String name, int keyFrame, Runnable callback) {
/* 393 */     if (this.world.isRemote) {
/* 394 */       this.animTriggers.add(new AnimationTrigger(name, keyFrame, callback));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
/* 404 */     super.setPositionAndRotationDirect(x, y, z, yaw, pitch, 1, teleport);
/*     */   }
/*     */ 
/*     */   
/*     */   public void faceLocation(double x, double z, float maxYawChange) {
/* 409 */     double dx = x - this.posX;
/* 410 */     double dz = z - this.posZ;
/*     */     
/* 412 */     float f = (float)(MathHelper.atan2(dz, dx) * 57.29577951308232D) - 90.0F;
/* 413 */     this.rotationYaw = updateRotation(this.rotationYaw, f, maxYawChange);
/*     */   }
/*     */ 
/*     */   
/*     */   private float updateRotation(float angle, float targetAngle, float maxIncrease) {
/* 418 */     float f = MathHelper.wrapDegrees(targetAngle - angle);
/* 419 */     return angle + f;
/*     */   }
/*     */   
/*     */   public boolean hasVillage() {
/* 423 */     return (this.village != null && this.village.isLoaded());
/*     */   }
/*     */ 
/*     */   
/*     */   public Village getVillage() {
/* 428 */     if (hasVillage()) {
/* 429 */       return this.village;
/*     */     }
/* 431 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAIMoveSpeed() {
/* 436 */     return 0.35F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDimension() {
/* 441 */     return this.dimension;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getX() {
/* 446 */     return this.posX;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getY() {
/* 451 */     return this.posY;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getZ() {
/* 456 */     return this.posZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWorldRemote() {
/* 461 */     return this.world.isRemote;
/*     */   }
/*     */   
/*     */   public PathFinder getPathFinder() {
/* 465 */     return ((PathNavigateVillager2)getNavigator()).getVillagerPathFinder();
/*     */   }
/*     */   
/*     */   public abstract AnimationHandler getAnimationHandler();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityVillageNavigator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */