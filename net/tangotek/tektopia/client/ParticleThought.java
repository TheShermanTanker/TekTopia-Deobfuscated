/*     */ package net.tangotek.tektopia.client;
/*     */ 
/*     */ import net.minecraft.client.particle.Particle;
/*     */ import net.minecraft.client.renderer.BufferBuilder;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.renderer.vertex.VertexFormat;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @SideOnly(Side.CLIENT)
/*     */ public class ParticleThought
/*     */   extends Particle
/*     */ {
/*     */   private final ResourceLocation texture;
/*  27 */   private static final VertexFormat VERTEX_FORMAT = (new VertexFormat()).addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.TEX_2F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.TEX_2S).addElement(DefaultVertexFormats.NORMAL_3B).addElement(DefaultVertexFormats.PADDING_1B);
/*     */   
/*     */   private final TextureManager textureManager;
/*     */   private int life;
/*     */   private final float scale;
/*     */   private final int lifeTime;
/*     */   private final Entity ent;
/*     */   
/*     */   public ParticleThought(World worldIn, TextureManager tm, Entity ent, float scale, String texture) {
/*  36 */     super(worldIn, ent.posX, ent.posY + 2.5D, ent.posZ, 0.0D, 0.0D, 0.0D);
/*  37 */     this.texture = new ResourceLocation("tektopia:textures/particle/" + texture);
/*  38 */     this.textureManager = tm;
/*  39 */     this.ent = ent;
/*  40 */     this.lifeTime = 40;
/*  41 */     this.scale = scale;
/*  42 */     this.particleRed = 1.0F;
/*  43 */     this.particleGreen = 1.0F;
/*  44 */     this.particleBlue = 1.0F;
/*  45 */     this.motionX = 0.0D;
/*  46 */     this.motionY = 0.005D;
/*  47 */     this.motionZ = 0.0D;
/*  48 */     this.particleAlpha = 1.0F;
/*  49 */     this.canCollide = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  54 */     this.posY += this.motionY;
/*     */     
/*  56 */     this.prevPosX = this.posX;
/*  57 */     this.prevPosY = this.posY;
/*  58 */     this.prevPosZ = this.posZ;
/*  59 */     move(this.ent.posX - this.prevPosX, this.motionY, this.ent.posZ - this.prevPosZ);
/*  60 */     this.life++;
/*     */     
/*  62 */     if (this.life * 2 > this.lifeTime) {
/*  63 */       this.particleAlpha -= 2.0F / this.lifeTime;
/*     */       
/*  65 */       if (this.life == this.lifeTime) {
/*  66 */         setExpired();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFXLayer() {
/*  77 */     return 3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
/*  85 */     this.textureManager.bindTexture(this.texture);
/*  86 */     float f = 0.0F;
/*  87 */     float f1 = 1.0F;
/*  88 */     float f2 = 0.0F;
/*  89 */     float f3 = 1.0F;
/*     */     
/*  91 */     float f4 = 0.3F * this.scale;
/*  92 */     float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/*  93 */     float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/*  94 */     float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/*  95 */     GlStateManager.pushMatrix();
/*  96 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*  97 */     GlStateManager.disableLighting();
/*  98 */     GlStateManager.depthMask(true);
/*  99 */     GlStateManager.enableBlend();
/* 100 */     GlStateManager.enableDepth();
/* 101 */     GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
/*     */ 
/*     */     
/* 104 */     buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
/* 105 */     buffer.pos((f5 - rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 - rotationYZ * f4 - rotationXZ * f4)).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).endVertex();
/* 106 */     buffer.pos((f5 - rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 - rotationYZ * f4 + rotationXZ * f4)).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).endVertex();
/* 107 */     buffer.pos((f5 + rotationX * f4 + rotationXY * f4), (f6 + rotationZ * f4), (f7 + rotationYZ * f4 + rotationXZ * f4)).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).endVertex();
/* 108 */     buffer.pos((f5 + rotationX * f4 - rotationXY * f4), (f6 - rotationZ * f4), (f7 + rotationYZ * f4 - rotationXZ * f4)).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).endVertex();
/* 109 */     Tessellator.getInstance().draw();
/* 110 */     GlStateManager.disableBlend();
/* 111 */     GlStateManager.enableLighting();
/* 112 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBrightnessForRender(float p_189214_1_) {
/* 117 */     return 61680;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\ParticleThought.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */