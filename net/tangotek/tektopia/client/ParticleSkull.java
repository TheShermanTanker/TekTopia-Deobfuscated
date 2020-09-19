/*     */ package net.tangotek.tektopia.client;
/*     */ 
/*     */ import net.minecraft.client.particle.Particle;
/*     */ import net.minecraft.client.renderer.BufferBuilder;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.renderer.vertex.VertexFormat;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ParticleSkull
/*     */   extends Particle
/*     */ {
/*  19 */   private static final ResourceLocation SAND_TEXTURE = new ResourceLocation("tektopia", "textures/particle/skull.png");
/*  20 */   private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);
/*     */   
/*     */   private int life;
/*     */   public double radius;
/*     */   public double radiusGrow;
/*     */   public double angle;
/*     */   public double torque;
/*     */   public int lifeTime;
/*     */   private final TextureManager textureManager;
/*     */   public final float size;
/*     */   private final Vec3d origin;
/*     */   
/*     */   public ParticleSkull(World world, TextureManager textureManagerIn, Vec3d pos, double motionY) {
/*  33 */     super(world, pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
/*  34 */     this.textureManager = textureManagerIn;
/*  35 */     float f = this.rand.nextFloat() * 0.4F + 0.6F;
/*  36 */     this.particleRed = f;
/*  37 */     this.particleGreen = f;
/*  38 */     this.particleBlue = f;
/*  39 */     this.size = 0.1F + (float)(Math.random() * 0.15000000596046448D);
/*  40 */     this.motionY = motionY;
/*  41 */     this.origin = new Vec3d(this.posX, this.posY, this.posZ);
/*     */     
/*  43 */     this.angle = Math.random() * 360.0D;
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
/*     */   public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/*  55 */     this.textureManager.bindTexture(SAND_TEXTURE);
/*  56 */     float f = 0.0F;
/*  57 */     float f1 = 1.0F;
/*  58 */     float f2 = 0.0F;
/*  59 */     float f3 = 1.0F;
/*     */     
/*  61 */     float f4 = this.size;
/*  62 */     float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/*  63 */     float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/*  64 */     float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/*  65 */     GlStateManager.enableBlend();
/*     */     
/*  67 */     GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
/*     */     
/*  69 */     GlStateManager.pushMatrix();
/*     */ 
/*     */     
/*  72 */     GlStateManager.color(1.0F, 1.0F, 1.0F, (this.lifeTime - this.life) / this.lifeTime);
/*  73 */     GlStateManager.disableLighting();
/*  74 */     RenderHelper.disableStandardItemLighting();
/*  75 */     buffer.begin(7, VERTEX_FORMAT);
/*  76 */     buffer.pos((f5 - rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 - rotationYZ * f4 - rotationXZ * f4)).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  77 */     buffer.pos((f5 - rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 - rotationYZ * f4 + rotationXZ * f4)).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  78 */     buffer.pos((f5 + rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 + rotationYZ * f4 + rotationXZ * f4)).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  79 */     buffer.pos((f5 + rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 + rotationYZ * f4 - rotationXZ * f4)).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  80 */     Tessellator.getInstance().draw();
/*  81 */     GlStateManager.popMatrix();
/*  82 */     GlStateManager.disableBlend();
/*     */     
/*  84 */     GlStateManager.enableLighting();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  95 */     this.prevPosX = this.posX;
/*  96 */     this.prevPosY = this.posY;
/*  97 */     this.prevPosZ = this.posZ;
/*  98 */     this.life++;
/*     */     
/* 100 */     updatePosition();
/*     */     
/* 102 */     if (this.life == this.lifeTime)
/*     */     {
/* 104 */       setExpired();
/*     */     }
/*     */   }
/*     */   
/*     */   private void updatePosition() {
/* 109 */     double x = this.origin.x + Math.cos(this.angle) * this.radius;
/* 110 */     double z = this.origin.z + Math.sin(this.angle) * this.radius;
/*     */     
/* 112 */     setPosition(x, this.posY + this.motionY, z);
/*     */     
/* 114 */     this.radius += this.radiusGrow;
/* 115 */     this.angle += this.torque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFXLayer() {
/* 124 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldDisableDepth() {
/* 129 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\ParticleSkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */