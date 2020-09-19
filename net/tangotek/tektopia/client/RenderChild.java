/*    */ package net.tangotek.tektopia.client;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityChild;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ public class RenderChild<T extends EntityChild> extends RenderVillager<T> {
/* 13 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderChild(RenderManager manager) {
/* 16 */     super(manager, "child", true, 64, 64, "child", 0.15F);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setupTextures() {
/* 22 */     this.maleTextures = new ResourceLocation[] { new ResourceLocation("tektopia", "textures/entity/" + this.textureName + "0_m.png"), new ResourceLocation("tektopia", "textures/entity/" + this.textureName + "1_m.png") };
/* 23 */     this.femaleTextures = new ResourceLocation[] { new ResourceLocation("tektopia", "textures/entity/" + this.textureName + "0_f.png") };
/*    */   }
/*    */ 
/*    */   
/*    */   protected void preRenderCallback(EntityChild entitylivingbaseIn, float partialTickTime) {
/* 28 */     GlStateManager.scale(0.5D, 0.5D, 0.5D);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(T child) {
/* 33 */     if (!child.isMale()) {
/* 34 */       return this.femaleTextures[0];
/*    */     }
/* 36 */     return this.maleTextures[child.getVariation().byteValue()];
/*    */   }
/*    */   
/*    */   public static class Factory<T extends EntityChild>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 43 */       return (Render)new RenderChild<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderChild.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */