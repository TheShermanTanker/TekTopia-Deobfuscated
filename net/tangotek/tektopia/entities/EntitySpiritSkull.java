/*     */ package net.tangotek.tektopia.entities;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.particle.Particle;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.projectile.ProjectileHelper;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.DataSerializers;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.client.ParticleDarkness;
/*     */ import net.tangotek.tektopia.client.ParticleSkull;
/*     */ 
/*     */ public class EntitySpiritSkull
/*     */   extends Entity
/*     */ {
/*     */   public enum SkullMode
/*     */   {
/*  30 */     RETURNING,
/*  31 */     PROTECTING,
/*  32 */     ATTACKING;
/*     */   }
/*     */   
/*  35 */   private double acceleration = 0.0D;
/*  36 */   private int glowTimer = 0;
/*     */   
/*     */   private boolean initialSpawn = false;
/*  39 */   private static final DataParameter<Byte> SKULL_MODE = EntityDataManager.createKey(EntitySpiritSkull.class, DataSerializers.BYTE);
/*  40 */   private static final DataParameter<Integer> NECRO = EntityDataManager.createKey(EntitySpiritSkull.class, DataSerializers.VARINT);
/*  41 */   private static final DataParameter<Integer> SKULL_CREATURE = EntityDataManager.createKey(EntitySpiritSkull.class, DataSerializers.VARINT);
/*     */ 
/*     */   
/*     */   public EntitySpiritSkull(World worldIn, boolean init) {
/*  45 */     this(worldIn);
/*  46 */     this.initialSpawn = init;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntitySpiritSkull(World worldIn) {
/*  51 */     super(worldIn);
/*  52 */     setSize(0.3125F, 0.3125F);
/*  53 */     setInvisible(true);
/*     */   }
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public EntitySpiritSkull(World worldIn, double x, double y, double z) {
/*  59 */     super(worldIn);
/*  60 */     setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
/*  61 */     setPosition(x, y, z);
/*  62 */     setSize(0.3125F, 0.3125F);
/*  63 */     setInvisible(true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  68 */     this.dataManager.register(SKULL_MODE, Byte.valueOf((byte)SkullMode.RETURNING.ordinal()));
/*  69 */     this.dataManager.register(NECRO, Integer.valueOf(0));
/*  70 */     this.dataManager.register(SKULL_CREATURE, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  75 */     super.onUpdate();
/*     */     
/*  77 */     boolean valid = true;
/*  78 */     if (!this.world.isBlockLoaded(new BlockPos(this))) {
/*  79 */       setDead();
/*  80 */       valid = false;
/*     */     } 
/*     */     
/*  83 */     SkullMode skullMode = getSkullMode();
/*  84 */     EntityNecromancer necro = getNecro();
/*  85 */     EntityCreature skullCreature = getSkullCreature();
/*     */     
/*  87 */     if (necro == null || !necro.isEntityAlive()) {
/*  88 */       if (!this.world.isRemote) {
/*  89 */         setDead();
/*     */       }
/*  91 */       valid = false;
/*     */     }
/*  93 */     else if (skullCreature == null || !skullCreature.isEntityAlive()) {
/*  94 */       if (!this.world.isRemote) {
/*  95 */         necro.releaseSkull(this);
/*     */       }
/*  97 */       valid = false;
/*     */     }
/*  99 */     else if (this.ticksExisted > 800) {
/* 100 */       necro.releaseSkull(this);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 105 */     if (valid) {
/* 106 */       if (skullMode == SkullMode.RETURNING) {
/* 107 */         if (this.ticksExisted > 23) {
/* 108 */           Vec3d dest = necro.getPositionVector().add(0.0D, necro.getEyeHeight(), 0.0D);
/* 109 */           if (!this.world.isRemote) {
/* 110 */             if (isInvisible()) {
/* 111 */               setInvisible(false);
/* 112 */               setLocationAndAngles(skullCreature.posX, skullCreature.posY + skullCreature.getEyeHeight(), skullCreature.posZ, 0.0F, 0.0F);
/* 113 */               playSound(ModSoundEvents.deathSkullRebound, this.world.rand.nextFloat() * 0.4F + 1.2F, this.world.rand.nextFloat() * 0.4F + 0.8F);
/* 114 */             } else if (getDistanceSq(dest.x, dest.y, dest.z) < 1.0D) {
/*     */               
/* 116 */               setSkullMode(SkullMode.PROTECTING);
/* 117 */               playSound(ModSoundEvents.deathSkullArrive, this.world.rand.nextFloat() * 0.4F + 1.2F, this.world.rand.nextFloat() * 0.4F + 0.8F);
/*     */             } 
/*     */           }
/*     */           
/* 121 */           Vec3d dir = dest.subtract(getPositionVector()).normalize().scale(0.3D);
/* 122 */           this.motionX = dir.x;
/* 123 */           this.motionY = dir.y;
/* 124 */           this.motionZ = dir.z;
/*     */           
/* 126 */           this.posX += this.motionX;
/* 127 */           this.posY += this.motionY;
/* 128 */           this.posZ += this.motionZ;
/* 129 */           ProjectileHelper.rotateTowardsMovement(this, 0.2F);
/*     */           
/* 131 */           setPosition(this.posX, this.posY, this.posZ);
/*     */         } 
/* 133 */       } else if (skullMode == SkullMode.PROTECTING) {
/* 134 */         Vec3d origin = necro.getPositionVector().add(0.0D, necro.getEyeHeight(), 0.0D);
/* 135 */         setPosition(origin.x, origin.y, origin.z);
/* 136 */       } else if (skullMode == SkullMode.ATTACKING) {
/* 137 */         this.posX += this.motionX;
/* 138 */         this.posY += this.motionY;
/* 139 */         this.posZ += this.motionZ;
/* 140 */         ProjectileHelper.rotateTowardsMovement(this, 0.2F);
/*     */         
/* 142 */         this.motionX *= this.acceleration;
/* 143 */         this.motionY *= this.acceleration;
/* 144 */         this.motionZ *= this.acceleration;
/* 145 */         setPosition(this.posX, this.posY, this.posZ);
/*     */       } 
/*     */       
/* 148 */       if (this.world.isRemote) {
/*     */         
/* 150 */         if (skullMode != SkullMode.PROTECTING) {
/* 151 */           createDarknessParticles(5, 0.1F);
/*     */         } else {
/*     */           
/* 154 */           createDarknessParticles(2, 0.07F);
/*     */         } 
/*     */         
/* 157 */         if (skullMode == SkullMode.PROTECTING || skullMode == SkullMode.RETURNING) {
/* 158 */           necro.spawnCloud((Entity)skullCreature, 3);
/* 159 */           if (necro.getRNG().nextInt(4) == 0) {
/* 160 */             necro.skullParticles((Entity)skullCreature, 1);
/*     */           }
/*     */         } 
/*     */         
/* 164 */         if (this.ticksExisted >= 1 && this.ticksExisted <= 20 && 
/* 165 */           necro != null && skullCreature != null) {
/* 166 */           float percentFrom = (this.ticksExisted - 1) / 20.0F;
/* 167 */           float percentTo = this.ticksExisted / 20.0F;
/* 168 */           particleLine((Entity)necro, (Entity)skullCreature, percentFrom, percentTo, 25);
/*     */         } 
/*     */ 
/*     */         
/* 172 */         if (isGlowing()) {
/* 173 */           this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY, this.posZ, this.world.rand.nextGaussian() * 0.1D, 0.08D, this.world.rand.nextGaussian() * 0.1D, new int[0]);
/*     */         }
/*     */         
/* 176 */         if (this.ticksExisted > 23 && this.ticksExisted < 27) {
/* 177 */           createSkullParticles(4);
/*     */         
/*     */         }
/*     */       }
/* 181 */       else if (this.glowTimer > 0) {
/* 182 */         this.glowTimer--;
/* 183 */         if (this.glowTimer == 0) {
/* 184 */           setGlowing(false);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   private void particleLine(Entity source, Entity target, float from, float to, int count) {
/* 194 */     Vec3d start = source.getPositionVector().add(0.0D, source.getEyeHeight(), 0.0D);
/* 195 */     Vec3d end = target.getPositionVector().add(0.0D, target.getEyeHeight(), 0.0D);
/* 196 */     Vec3d line = end.subtract(start);
/*     */     
/* 198 */     for (int i = 0; i < count; i++) {
/* 199 */       Vec3d delta = line.scale(MathHelper.nextFloat(this.world.rand, from, to));
/* 200 */       Vec3d pos = start.add(delta);
/* 201 */       ParticleDarkness part = new ParticleDarkness(this.world, Minecraft.getMinecraft().getTextureManager(), pos, this.motionY);
/* 202 */       part.lifeTime = this.world.rand.nextInt(10) + 10;
/* 203 */       part.setRBGColorF(0.953F, 0.173F, 0.077F);
/* 204 */       part.onUpdate();
/* 205 */       (Minecraft.getMinecraft()).effectRenderer.addEffect((Particle)part);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   private void createDarknessParticles(int count, float radius) {
/* 212 */     for (int i = 0; i < count; i++) {
/* 213 */       double motionY = Math.random() * 0.03D + 0.01D;
/* 214 */       Vec3d pos = new Vec3d(this.posX + this.rand.nextGaussian() * 0.1D, this.posY, this.posZ + this.rand.nextGaussian() * 0.1D);
/* 215 */       ParticleDarkness part = new ParticleDarkness(this.world, Minecraft.getMinecraft().getTextureManager(), pos, motionY);
/* 216 */       part.radius = this.world.rand.nextGaussian() * radius;
/* 217 */       part.radiusGrow = 0.005D;
/* 218 */       part.torque = Math.random() * 0.04D - 0.02D;
/* 219 */       part.lifeTime = this.world.rand.nextInt(15) + 20;
/* 220 */       part.onUpdate();
/* 221 */       (Minecraft.getMinecraft()).effectRenderer.addEffect((Particle)part);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   private void createSkullParticles(int count) {
/* 227 */     for (int i = 0; i < count; i++) {
/* 228 */       double motionY = Math.random() * 0.03D + 0.01D;
/* 229 */       Vec3d pos = new Vec3d(this.posX + this.rand.nextGaussian() * 0.5D, this.posY + this.rand.nextGaussian(), this.posZ + this.rand.nextGaussian() * 0.5D);
/* 230 */       ParticleSkull part = new ParticleSkull(this.world, Minecraft.getMinecraft().getTextureManager(), pos, motionY);
/* 231 */       part.radius = this.world.rand.nextGaussian() * 0.1D;
/* 232 */       part.radiusGrow = 0.005D;
/* 233 */       part.torque = Math.random() * 0.04D - 0.02D;
/* 234 */       part.lifeTime = this.world.rand.nextInt(15) + 20;
/* 235 */       part.onUpdate();
/* 236 */       (Minecraft.getMinecraft()).effectRenderer.addEffect((Particle)part);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public EntityNecromancer getNecro() {
/* 243 */     Integer entityId = (Integer)this.dataManager.get(NECRO);
/* 244 */     if (entityId.intValue() > 0) {
/* 245 */       Entity e = this.world.getEntityByID(entityId.intValue());
/* 246 */       if (e instanceof EntityNecromancer) {
/* 247 */         return (EntityNecromancer)e;
/*     */       }
/*     */     } 
/* 250 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNecro(EntityNecromancer necro) {
/* 255 */     this.dataManager.set(NECRO, Integer.valueOf((necro == null) ? 0 : necro.getEntityId()));
/*     */   }
/*     */   
/*     */   public void setSkullCreature(EntityCreature creature) {
/* 259 */     this.dataManager.set(SKULL_CREATURE, Integer.valueOf((creature == null) ? 0 : creature.getEntityId()));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public EntityCreature getSkullCreature() {
/* 265 */     Integer entityId = (Integer)this.dataManager.get(SKULL_CREATURE);
/* 266 */     if (entityId.intValue() > 0) {
/* 267 */       Entity e = this.world.getEntityByID(entityId.intValue());
/* 268 */       if (e instanceof EntityCreature && e.isEntityAlive()) {
/* 269 */         return (EntityCreature)e;
/*     */       }
/*     */     } 
/* 272 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public SkullMode getSkullMode() {
/* 277 */     int index = ((Byte)this.dataManager.get(SKULL_MODE)).byteValue();
/* 278 */     return SkullMode.values()[index];
/*     */   }
/*     */   
/*     */   public void setSkullMode(SkullMode mode) {
/* 282 */     this.motionX = 0.0D;
/* 283 */     this.motionY = 0.0D;
/* 284 */     this.motionZ = 0.0D;
/* 285 */     this.acceleration = 0.0D;
/* 286 */     this.dataManager.set(SKULL_MODE, Byte.valueOf((byte)mode.ordinal()));
/*     */     
/* 288 */     if (mode == SkullMode.ATTACKING)
/* 289 */       this.acceleration = 1.02D; 
/*     */   }
/*     */   
/*     */   public float getSpinRadius() {
/* 293 */     return 1.3F + (float)Math.abs(getUniqueID().getLeastSignificantBits() % 70L) / 100.0F;
/*     */   }
/*     */   
/*     */   public float getSpinSpeed() {
/* 297 */     float speed = 4.0F + (float)(Math.abs(getUniqueID().getLeastSignificantBits()) % 6L);
/* 298 */     if (getUniqueID().getLeastSignificantBits() % 2L == 0L) {
/* 299 */       speed = -speed;
/*     */     }
/* 301 */     return speed;
/*     */   }
/*     */   
/*     */   public float getSpinAxis() {
/* 305 */     return (float)Math.abs(getUniqueID().getLeastSignificantBits() % 6L) / 10.0F;
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
/*     */   @SideOnly(Side.CLIENT)
/*     */   public boolean isInRangeToRenderDist(double distance) {
/* 324 */     double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
/*     */     
/* 326 */     if (Double.isNaN(d0))
/*     */     {
/* 328 */       d0 = 4.0D;
/*     */     }
/*     */     
/* 331 */     d0 *= 64.0D;
/* 332 */     return (distance < d0 * d0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readEntityFromNBT(NBTTagCompound compound) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeEntityToNBT(NBTTagCompound compound) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canBeCollidedWith() {
/* 351 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 359 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntitySpiritSkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */