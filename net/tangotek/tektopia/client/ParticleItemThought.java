/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.particle.Particle;
/*    */ import net.minecraft.client.renderer.BufferBuilder;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ public class ParticleItemThought
/*    */   extends Particle
/*    */ {
/*    */   private final int lifeTime;
/*    */   private final Entity ent;
/*    */   
/*    */   public ParticleItemThought(World worldIn, Entity ent, Item item) {
/* 27 */     super(worldIn, ent.posX, (ent.getEntityBoundingBox()).maxY + 0.8D, ent.posZ, 0.0D, 0.0D, 0.0D);
/* 28 */     setParticleTexture(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(item));
/* 29 */     this.ent = ent;
/* 30 */     this.lifeTime = 40;
/* 31 */     this.particleRed = 1.0F;
/* 32 */     this.particleGreen = 1.0F;
/* 33 */     this.particleBlue = 1.0F;
/* 34 */     this.motionX = 0.0D;
/* 35 */     this.motionY = 0.013D;
/* 36 */     this.motionZ = 0.0D;
/* 37 */     this.particleGravity = 0.0F;
/* 38 */     this.particleMaxAge = 40;
/* 39 */     this.particleScale = 0.2F;
/* 40 */     multipleParticleScaleBy(0.15F);
/* 41 */     this.canCollide = false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getFXLayer() {
/* 50 */     return 1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 57 */     this.particleAlpha -= 0.02F;
/* 58 */     super.onUpdate();
/*    */     
/* 60 */     this.prevPosX = this.posX;
/* 61 */     this.prevPosY = this.posY;
/* 62 */     this.prevPosZ = this.posZ;
/* 63 */     move(this.ent.posX - this.prevPosX, this.motionY, this.ent.posZ - this.prevPosZ);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/* 71 */     GlStateManager.pushMatrix();
/*    */     
/* 73 */     float f = this.particleTexture.getMinU();
/* 74 */     float f1 = this.particleTexture.getMaxU();
/* 75 */     float f2 = this.particleTexture.getMinV();
/* 76 */     float f3 = this.particleTexture.getMaxV();
/* 77 */     float f4 = 0.5F;
/* 78 */     float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/* 79 */     float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/* 80 */     float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/* 81 */     int i = getBrightnessForRender(partialTicks);
/* 82 */     int j = i >> 16 & 0xFFFF;
/* 83 */     int k = i & 0xFFFF;
/* 84 */     float scale = 0.3F;
/* 85 */     buffer.pos((f5 - rotationX * scale - rotationXY * scale), (f6 - rotationZ * scale), (f7 - rotationYZ * scale - rotationXZ * scale)).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 86 */     buffer.pos((f5 - rotationX * scale + rotationXY * scale), (f6 + rotationZ * scale), (f7 - rotationYZ * scale + rotationXZ * scale)).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 87 */     buffer.pos((f5 + rotationX * scale + rotationXY * scale), (f6 + rotationZ * scale), (f7 + rotationYZ * scale + rotationXZ * scale)).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 88 */     buffer.pos((f5 + rotationX * scale - rotationXY * scale), (f6 - rotationZ * scale), (f7 + rotationYZ * scale - rotationXZ * scale)).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 89 */     GlStateManager.popMatrix();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\ParticleItemThought.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */