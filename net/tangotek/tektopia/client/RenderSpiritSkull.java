/*     */ package net.tangotek.tektopia.client;
/*     */ 
/*     */ import net.minecraft.client.model.ModelSkeletonHead;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.entity.Render;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import net.tangotek.tektopia.entities.EntitySpiritSkull;
/*     */ 
/*     */ @SideOnly(Side.CLIENT)
/*     */ public class RenderSpiritSkull
/*     */   extends Render<EntitySpiritSkull> {
/*  17 */   private static final ResourceLocation SKULL_TEXTURE = new ResourceLocation("tektopia", "textures/entity/spirit_skull.png");
/*  18 */   public static final Factory FACTORY = new Factory<>();
/*     */ 
/*     */   
/*  21 */   private final ModelSkeletonHead skeletonHeadModel = new ModelSkeletonHead();
/*     */ 
/*     */   
/*     */   public RenderSpiritSkull(RenderManager renderManagerIn) {
/*  25 */     super(renderManagerIn);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private float getRenderYaw(float p_82400_1_, float p_82400_2_, float p_82400_3_) {
/*     */     float f;
/*  32 */     for (f = p_82400_2_ - p_82400_1_; f < -180.0F; f += 360.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  37 */     while (f >= 180.0F)
/*     */     {
/*  39 */       f -= 360.0F;
/*     */     }
/*     */     
/*  42 */     return p_82400_1_ + p_82400_3_ * f;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doRender(EntitySpiritSkull entity, double x, double y, double z, float entityYaw, float partialTicks) {
/*  50 */     if (entity.isInvisible()) {
/*     */       return;
/*     */     }
/*  53 */     GlStateManager.pushMatrix();
/*  54 */     GlStateManager.disableCull();
/*  55 */     float f = getRenderYaw(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
/*  56 */     float f1 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
/*  57 */     GlStateManager.translate((float)x, (float)y, (float)z);
/*     */     
/*  59 */     if (entity.getSkullMode() == EntitySpiritSkull.SkullMode.PROTECTING) {
/*  60 */       float radius = entity.getSpinRadius();
/*  61 */       float speed = entity.getSpinSpeed();
/*  62 */       float xAxis = entity.getSpinAxis();
/*  63 */       GlStateManager.rotate((entity.ticksExisted + partialTicks) * speed, xAxis, 1.0F, 0.0F);
/*  64 */       GlStateManager.translate(0.0F, 0.0F, radius);
/*     */     } 
/*     */     
/*  67 */     float f2 = 0.0625F;
/*  68 */     GlStateManager.enableRescaleNormal();
/*  69 */     float scale = 0.6F;
/*  70 */     GlStateManager.scale(-scale, -scale, scale);
/*  71 */     GlStateManager.enableAlpha();
/*  72 */     bindEntityTexture((Entity)entity);
/*     */     
/*  74 */     if (this.renderOutlines) {
/*     */       
/*  76 */       GlStateManager.enableColorMaterial();
/*  77 */       GlStateManager.enableOutlineMode(getTeamColor((Entity)entity));
/*     */     } 
/*     */     
/*  80 */     this.skeletonHeadModel.render((Entity)entity, 0.0F, 0.0F, 0.0F, f, f1, 0.0625F);
/*     */     
/*  82 */     if (this.renderOutlines) {
/*     */       
/*  84 */       GlStateManager.disableOutlineMode();
/*  85 */       GlStateManager.disableColorMaterial();
/*     */     } 
/*     */     
/*  88 */     GlStateManager.popMatrix();
/*  89 */     super.doRender((Entity)entity, x, y, z, entityYaw, partialTicks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourceLocation getEntityTexture(EntitySpiritSkull entity) {
/*  97 */     return SKULL_TEXTURE;
/*     */   }
/*     */   
/*     */   public static class Factory<T extends EntitySpiritSkull>
/*     */     implements IRenderFactory<T>
/*     */   {
/*     */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 104 */       return new RenderSpiritSkull(manager);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderSpiritSkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */