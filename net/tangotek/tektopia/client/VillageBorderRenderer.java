/*     */ package net.tangotek.tektopia.client;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.renderer.BufferBuilder;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.tangotek.tektopia.VillageClient;
/*     */ 
/*     */ public class VillageBorderRenderer
/*     */ {
/*  21 */   private static final ResourceLocation FORCEFIELD_TEXTURES = new ResourceLocation("textures/misc/forcefield.png");
/*     */   
/*  23 */   private VillageClient villageClient = null;
/*     */ 
/*     */   
/*     */   public VillageBorderRenderer() {
/*  27 */     MinecraftForge.EVENT_BUS.register(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void renderWorldLast(RenderWorldLastEvent event) {
/*  35 */     if (this.villageClient == null) {
/*     */       return;
/*     */     }
/*  38 */     Minecraft minecraft = Minecraft.getMinecraft();
/*  39 */     WorldClient world = minecraft.world;
/*  40 */     EntityPlayerSP player = minecraft.player;
/*  41 */     if (world == null || player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  45 */     renderBorder((Entity)player, event.getPartialTicks());
/*     */   }
/*     */   
/*     */   public void updateVillage(VillageClient vc) {
/*  49 */     this.villageClient = vc;
/*     */   }
/*     */   
/*     */   private int getBorderDistance(BlockPos pos) {
/*  53 */     int dist = Integer.MAX_VALUE;
/*     */     
/*  55 */     dist = Math.min(dist, Math.abs(pos.getX() - this.villageClient.getMaxX()));
/*  56 */     dist = Math.min(dist, Math.abs(pos.getX() - this.villageClient.getMinX()));
/*  57 */     dist = Math.min(dist, Math.abs(pos.getZ() - this.villageClient.getMaxZ()));
/*  58 */     dist = Math.min(dist, Math.abs(pos.getZ() - this.villageClient.getMinZ()));
/*  59 */     return dist;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderBorder(Entity entityIn, float partialTicks) {
/*  64 */     Tessellator tessellator = Tessellator.getInstance();
/*  65 */     BufferBuilder bufferbuilder = tessellator.getBuffer();
/*  66 */     double RENDER_DISTANCE = 8.0D;
/*  67 */     double RENDER_Y_DELTA = 2.0D;
/*  68 */     double RENDER_WALL_WIDTH = 10.0D;
/*     */     
/*  70 */     int borderDistance = getBorderDistance(entityIn.getPosition());
/*     */     
/*  72 */     if (borderDistance < 8.0D) {
/*     */       
/*  74 */       double colorAlpha = 1.0D - borderDistance / 8.0D;
/*  75 */       colorAlpha = Math.pow(colorAlpha, 2.0D);
/*  76 */       double entityX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
/*  77 */       double entityY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
/*  78 */       double entityZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
/*  79 */       GlStateManager.enableBlend();
/*  80 */       GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
/*  81 */       Minecraft.getMinecraft().getTextureManager().bindTexture(FORCEFIELD_TEXTURES);
/*  82 */       GlStateManager.depthMask(false);
/*  83 */       GlStateManager.pushMatrix();
/*  84 */       int color = 2138367;
/*  85 */       float colorR = (color >> 16 & 0xFF) / 255.0F;
/*  86 */       float colorG = (color >> 8 & 0xFF) / 255.0F;
/*  87 */       float colorB = (color & 0xFF) / 255.0F;
/*  88 */       GlStateManager.color(colorR, colorG, colorB, (float)colorAlpha);
/*  89 */       GlStateManager.doPolygonOffset(-3.0F, -3.0F);
/*  90 */       GlStateManager.enablePolygonOffset();
/*  91 */       GlStateManager.alphaFunc(516, 0.1F);
/*  92 */       GlStateManager.enableAlpha();
/*  93 */       GlStateManager.disableCull();
/*  94 */       float f3 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F;
/*  95 */       float f4 = 0.0F;
/*  96 */       float f5 = 0.0F;
/*  97 */       float f6 = 128.0F;
/*  98 */       bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
/*  99 */       bufferbuilder.setTranslation(-entityX, -entityY, -entityZ);
/* 100 */       double d8 = Math.max(MathHelper.floor(entityZ - 10.0D), this.villageClient.getMinZ());
/* 101 */       double d9 = Math.min(MathHelper.ceil(entityZ + 10.0D), this.villageClient.getMaxZ());
/*     */ 
/*     */       
/* 104 */       if (Math.abs(entityX - this.villageClient.getMaxX()) < 8.0D) {
/*     */         
/* 106 */         float f7 = 0.0F;
/*     */         
/* 108 */         for (double d10 = d8; d10 < d9; f7 += 0.5F) {
/*     */           
/* 110 */           double d11 = Math.min(1.0D, d9 - d10);
/* 111 */           float f8 = (float)d11 * 0.5F;
/* 112 */           bufferbuilder.pos(this.villageClient.getMaxX(), entityY + 2.0D, d10).tex((f3 + f7), (f3 + 0.0F)).endVertex();
/* 113 */           bufferbuilder.pos(this.villageClient.getMaxX(), entityY + 2.0D, d10 + d11).tex((f3 + f8 + f7), (f3 + 0.0F)).endVertex();
/* 114 */           bufferbuilder.pos(this.villageClient.getMaxX(), entityY - 2.0D, d10 + d11).tex((f3 + f8 + f7), (f3 + 128.0F)).endVertex();
/* 115 */           bufferbuilder.pos(this.villageClient.getMaxX(), entityY - 2.0D, d10).tex((f3 + f7), (f3 + 128.0F)).endVertex();
/* 116 */           d10++;
/*     */         } 
/*     */       } 
/*     */       
/* 120 */       if (Math.abs(entityX - this.villageClient.getMinX()) < 8.0D) {
/*     */         
/* 122 */         float f9 = 0.0F;
/*     */         
/* 124 */         for (double d12 = d8; d12 < d9; f9 += 0.5F) {
/*     */           
/* 126 */           double d15 = Math.min(1.0D, d9 - d12);
/* 127 */           float f12 = (float)d15 * 0.5F;
/* 128 */           bufferbuilder.pos(this.villageClient.getMinX(), entityY + 2.0D, d12).tex((f3 + f9), (f3 + 0.0F)).endVertex();
/* 129 */           bufferbuilder.pos(this.villageClient.getMinX(), entityY + 2.0D, d12 + d15).tex((f3 + f12 + f9), (f3 + 0.0F)).endVertex();
/* 130 */           bufferbuilder.pos(this.villageClient.getMinX(), entityY - 2.0D, d12 + d15).tex((f3 + f12 + f9), (f3 + 128.0F)).endVertex();
/* 131 */           bufferbuilder.pos(this.villageClient.getMinX(), entityY - 2.0D, d12).tex((f3 + f9), (f3 + 128.0F)).endVertex();
/* 132 */           d12++;
/*     */         } 
/*     */       } 
/*     */       
/* 136 */       d8 = Math.max(MathHelper.floor(entityX - 10.0D), this.villageClient.getMinX());
/* 137 */       d9 = Math.min(MathHelper.ceil(entityX + 10.0D), this.villageClient.getMaxX());
/*     */       
/* 139 */       if (Math.abs(entityZ - this.villageClient.getMaxZ()) < 8.0D) {
/*     */         
/* 141 */         float f10 = 0.0F;
/*     */         
/* 143 */         for (double d13 = d8; d13 < d9; f10 += 0.5F) {
/*     */           
/* 145 */           double d16 = Math.min(1.0D, d9 - d13);
/* 146 */           float f13 = (float)d16 * 0.5F;
/* 147 */           bufferbuilder.pos(d13, entityY + 2.0D, this.villageClient.getMaxZ()).tex((f3 + f10), (f3 + 0.0F)).endVertex();
/* 148 */           bufferbuilder.pos(d13 + d16, entityY + 2.0D, this.villageClient.getMaxZ()).tex((f3 + f13 + f10), (f3 + 0.0F)).endVertex();
/* 149 */           bufferbuilder.pos(d13 + d16, entityY - 2.0D, this.villageClient.getMaxZ()).tex((f3 + f13 + f10), (f3 + 128.0F)).endVertex();
/* 150 */           bufferbuilder.pos(d13, entityY - 2.0D, this.villageClient.getMaxZ()).tex((f3 + f10), (f3 + 128.0F)).endVertex();
/* 151 */           d13++;
/*     */         } 
/*     */       } 
/*     */       
/* 155 */       if (Math.abs(entityZ - this.villageClient.getMinZ()) < 8.0D) {
/*     */         
/* 157 */         float f11 = 0.0F;
/*     */         
/* 159 */         for (double d14 = d8; d14 < d9; f11 += 0.5F) {
/*     */           
/* 161 */           double d17 = Math.min(1.0D, d9 - d14);
/* 162 */           float f14 = (float)d17 * 0.5F;
/* 163 */           bufferbuilder.pos(d14, entityY + 2.0D, this.villageClient.getMinZ()).tex((f3 + f11), (f3 + 0.0F)).endVertex();
/* 164 */           bufferbuilder.pos(d14 + d17, entityY + 2.0D, this.villageClient.getMinZ()).tex((f3 + f14 + f11), (f3 + 0.0F)).endVertex();
/* 165 */           bufferbuilder.pos(d14 + d17, entityY - 2.0D, this.villageClient.getMinZ()).tex((f3 + f14 + f11), (f3 + 128.0F)).endVertex();
/* 166 */           bufferbuilder.pos(d14, entityY - 2.0D, this.villageClient.getMinZ()).tex((f3 + f11), (f3 + 128.0F)).endVertex();
/* 167 */           d14++;
/*     */         } 
/*     */       } 
/*     */       
/* 171 */       tessellator.draw();
/* 172 */       bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
/* 173 */       GlStateManager.enableCull();
/* 174 */       GlStateManager.disableAlpha();
/* 175 */       GlStateManager.doPolygonOffset(0.0F, 0.0F);
/* 176 */       GlStateManager.disablePolygonOffset();
/* 177 */       GlStateManager.enableAlpha();
/* 178 */       GlStateManager.disableBlend();
/* 179 */       GlStateManager.popMatrix();
/* 180 */       GlStateManager.depthMask(true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\VillageBorderRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */