/*    */ package net.tangotek.tektopia.entities;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.particle.Particle;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityAreaEffectCloud;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.MobEffects;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.minecraft.util.EnumParticleTypes;
/*    */ import net.minecraft.util.math.MathHelper;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import net.tangotek.tektopia.client.ParticleSkull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntityDeathCloud
/*    */   extends EntityAreaEffectCloud
/*    */ {
/*    */   private float angleRadians;
/*    */   
/*    */   public EntityDeathCloud(World worldIn) {
/* 28 */     super(worldIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityDeathCloud(World worldIn, double x, double y, double z) {
/* 33 */     super(worldIn, x, y, z);
/* 34 */     setParticle(EnumParticleTypes.TOWN_AURA);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 39 */     super.onUpdate();
/*    */     
/* 41 */     if (this.world.isRemote) {
/* 42 */       for (int i = 0; i < 4; i++) {
/* 43 */         this.angleRadians = (float)(this.angleRadians + 0.0035D);
/* 44 */         if (this.angleRadians > 1.0F) {
/* 45 */           this.angleRadians = 1.0F - this.angleRadians;
/*    */         }
/* 47 */         perimeterParticle(this.angleRadians, 0.0F);
/* 48 */         perimeterParticle(-this.angleRadians, -0.3F);
/* 49 */         interiorParticle();
/*    */       } 
/*    */     } else {
/*    */       
/* 53 */       double radiusSq = (getRadius() * getRadius());
/* 54 */       List<EntityPlayer> entList = this.world.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox());
/* 55 */       for (EntityPlayer insideEnt : entList) {
/* 56 */         if (insideEnt.getDistanceSq((Entity)this) < radiusSq) {
/* 57 */           int duration = (insideEnt instanceof EntityPlayer) ? 160 : 80;
/* 58 */           insideEnt.addPotionEffect(new PotionEffect(MobEffects.WITHER, duration));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @SideOnly(Side.CLIENT)
/*    */   private void perimeterParticle(float angleRadians, float radiusMod) {
/* 67 */     double motionY = Math.random() * 0.01D + 0.01D;
/*    */     
/* 69 */     float f1 = angleRadians * 6.2831855F;
/*    */     
/* 71 */     float xOffset = MathHelper.cos(f1) * (getRadius() + radiusMod);
/* 72 */     float zOffset = MathHelper.sin(f1) * (getRadius() + radiusMod);
/*    */     
/* 74 */     Vec3d pos = new Vec3d(this.posX + xOffset, this.posY, this.posZ + zOffset);
/*    */     
/* 76 */     this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, pos.x, pos.y, pos.z, 0.0D, motionY, 0.0D, new int[0]);
/*    */   }
/*    */   
/*    */   @SideOnly(Side.CLIENT)
/*    */   private void interiorParticle() {
/* 81 */     double motionY = Math.random() * 0.01D + 0.01D;
/* 82 */     float f1 = this.world.rand.nextFloat() * 6.2831855F;
/* 83 */     float xOffset = MathHelper.cos(f1) * getRadius() * this.world.rand.nextFloat();
/* 84 */     float zOffset = MathHelper.sin(f1) * getRadius() * this.world.rand.nextFloat();
/*    */     
/* 86 */     Vec3d pos = new Vec3d(this.posX + xOffset, this.posY, this.posZ + zOffset);
/* 87 */     ParticleSkull part = new ParticleSkull(this.world, Minecraft.getMinecraft().getTextureManager(), pos, motionY);
/* 88 */     part.radius = this.rand.nextGaussian() * 0.05D;
/* 89 */     part.radiusGrow = 0.002D;
/* 90 */     part.torque = Math.random() * 0.04D - 0.02D;
/* 91 */     part.lifeTime = this.rand.nextInt(20) + 10;
/* 92 */     part.onUpdate();
/* 93 */     (Minecraft.getMinecraft()).effectRenderer.addEffect((Particle)part);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityDeathCloud.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */