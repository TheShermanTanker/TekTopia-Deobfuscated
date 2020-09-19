/*    */ package net.tangotek.tektopia.entities;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityAreaEffectCloud;
/*    */ import net.minecraft.init.MobEffects;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.minecraft.util.EnumParticleTypes;
/*    */ import net.minecraft.util.math.MathHelper;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntityCaptainAura
/*    */   extends EntityAreaEffectCloud
/*    */ {
/* 22 */   private float radius = 3.0F;
/* 23 */   private final float RADIUS_PER_TICK = 0.5F;
/*    */ 
/*    */   
/*    */   public EntityCaptainAura(World worldIn) {
/* 27 */     super(worldIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityCaptainAura(World worldIn, double x, double y, double z) {
/* 32 */     super(worldIn, x, y, z);
/* 33 */     setParticle(EnumParticleTypes.FIREWORKS_SPARK);
/* 34 */     setRadius(this.radius);
/* 35 */     setRadiusPerTick(0.5F);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 41 */     this.radius += 0.5F;
/* 42 */     if (this.world.isRemote) {
/* 43 */       for (int i = 0; i < 10; i++) {
/* 44 */         perimeterParticle();
/*    */       }
/*    */     } else {
/*    */       
/* 48 */       if (this.ticksExisted >= getDuration()) {
/*    */         
/* 50 */         setDead();
/*    */         
/*    */         return;
/*    */       } 
/* 54 */       double radiusSq = (this.radius * this.radius);
/* 55 */       List<EntityVillagerTek> villagerList = this.world.getEntitiesWithinAABB(EntityVillagerTek.class, getEntityBoundingBox());
/*    */       
/* 57 */       villagerList.stream().filter(g -> (g.getDistanceSq((Entity)this) < radiusSq)).forEach(g -> g.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 100)));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @SideOnly(Side.CLIENT)
/*    */   private void perimeterParticle() {
/* 64 */     double motionY = Math.random() * 0.01D + 0.01D;
/* 65 */     float f1 = this.world.rand.nextFloat() * 6.2831855F;
/* 66 */     float xOffset = MathHelper.cos(f1) * this.radius;
/* 67 */     float zOffset = MathHelper.sin(f1) * this.radius;
/*    */     
/* 69 */     Vec3d pos = new Vec3d(this.posX + xOffset, this.posY, this.posZ + zOffset);
/* 70 */     this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, pos.x, pos.y, pos.z, 0.0D, motionY, 0.0D, new int[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityCaptainAura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */