/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import com.leviathanstudio.craftstudio.client.model.ModelCraftStudio;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.RenderLivingBase;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerRenderer;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LayerRancherHat
/*    */   implements LayerRenderer<EntityLivingBase>
/*    */ {
/*    */   private final RenderLivingBase<?> renderer;
/*    */   private final ModelCraftStudio model;
/*    */   private final ResourceLocation texture;
/*    */   
/*    */   public LayerRancherHat(RenderLivingBase<?> rendererIn) {
/* 32 */     this.renderer = rendererIn;
/* 33 */     this.model = new ModelCraftStudio("tektopia", "rancher_hat", 128, 64);
/* 34 */     this.texture = new ResourceLocation("tektopia", "textures/entity/rancher_m.png");
/*    */   }
/*    */ 
/*    */   
/*    */   public void doRenderLayer(EntityLivingBase entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
/* 39 */     GlStateManager.pushMatrix();
/* 40 */     this.renderer.bindTexture(this.texture);
/*    */     
/* 42 */     float rotateAngleY = netHeadYaw * 0.017453292F;
/* 43 */     float rotateAngleX = headPitch * 0.017453292F;
/*    */     
/* 45 */     if (entityIn.isSneaking())
/*    */     {
/* 47 */       GlStateManager.translate(0.0F, 1.6F, 0.0F);
/*    */     }
/*    */     
/* 50 */     GlStateManager.rotate(rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
/* 51 */     GlStateManager.rotate(rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
/* 52 */     this.model.render();
/* 53 */     GlStateManager.popMatrix();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldCombineTextures() {
/* 60 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\LayerRancherHat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */