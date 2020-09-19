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
/*     */ public class ParticleDarkness
/*     */   extends Particle
/*     */ {
/*  19 */   private static final ResourceLocation SAND_TEXTURE = new ResourceLocation("tektopia", "textures/particle/darkness.png");
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
/*     */   public ParticleDarkness(World world, TextureManager textureManagerIn, Vec3d pos, double motionY) {
/*  33 */     super(world, pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
/*  34 */     this.textureManager = textureManagerIn;
/*     */     
/*  36 */     float f = this.rand.nextFloat() * 0.4F + 0.6F;
/*  37 */     this.particleRed = f;
/*  38 */     this.particleGreen = f;
/*  39 */     this.particleBlue = f;
/*  40 */     this.size = (float)(Math.random() * 0.10000000149011612D) + 0.15F;
/*  41 */     this.motionY = motionY;
/*  42 */     this.origin = new Vec3d(this.posX, this.posY, this.posZ);
/*     */     
/*  44 */     this.angle = Math.random() * 360.0D;
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
/*  56 */     this.textureManager.bindTexture(SAND_TEXTURE);
/*  57 */     float f = 0.0F;
/*  58 */     float f1 = 1.0F;
/*  59 */     float f2 = 0.0F;
/*  60 */     float f3 = 1.0F;
/*     */     
/*  62 */     float f4 = this.size;
/*  63 */     float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/*  64 */     float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/*  65 */     float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/*  66 */     GlStateManager.enableBlend();
/*     */ 
/*     */     
/*  69 */     GlStateManager.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
/*  70 */     GlStateManager.pushMatrix();
/*     */ 
/*     */     
/*  73 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  74 */     GlStateManager.disableLighting();
/*  75 */     RenderHelper.disableStandardItemLighting();
/*  76 */     buffer.begin(7, VERTEX_FORMAT);
/*  77 */     buffer.pos((f5 - rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 - rotationYZ * f4 - rotationXZ * f4)).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  78 */     buffer.pos((f5 - rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 - rotationYZ * f4 + rotationXZ * f4)).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  79 */     buffer.pos((f5 + rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 + rotationYZ * f4 + rotationXZ * f4)).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  80 */     buffer.pos((f5 + rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 + rotationYZ * f4 - rotationXZ * f4)).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(0, 240).normal(0.0F, 1.0F, 0.0F).endVertex();
/*  81 */     Tessellator.getInstance().draw();
/*  82 */     GlStateManager.popMatrix();
/*  83 */     GlStateManager.disableBlend();
/*     */     
/*  85 */     GlStateManager.enableLighting();
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
/*  96 */     this.prevPosX = this.posX;
/*  97 */     this.prevPosY = this.posY;
/*  98 */     this.prevPosZ = this.posZ;
/*  99 */     this.life++;
/*     */     
/* 101 */     updatePosition();
/*     */     
/* 103 */     if (this.life == this.lifeTime)
/*     */     {
/* 105 */       setExpired();
/*     */     }
/*     */   }
/*     */   
/*     */   private void updatePosition() {
/* 110 */     double x = this.origin.x + Math.cos(this.angle) * this.radius;
/* 111 */     double z = this.origin.z + Math.sin(this.angle) * this.radius;
/*     */     
/* 113 */     setPosition(x, this.posY + this.motionY, z);
/*     */     
/* 115 */     this.radius += this.radiusGrow;
/* 116 */     this.angle += this.torque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFXLayer() {
/* 125 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldDisableDepth() {
/* 130 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\ParticleDarkness.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */