/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.RenderLivingBase;
/*    */ import net.minecraft.client.renderer.entity.layers.LayerRenderer;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ 
/*    */ public class LayerVillagerDestination
/*    */   implements LayerRenderer<EntityLivingBase>
/*    */ {
/*    */   protected final RenderLivingBase<?> livingEntityRenderer;
/*    */   
/*    */   public LayerVillagerDestination(RenderLivingBase<?> livingEntityRendererIn) {
/* 17 */     this.livingEntityRenderer = livingEntityRendererIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
/* 22 */     if (entitylivingbaseIn instanceof EntityVillagerTek) {
/* 23 */       EntityVillagerTek villager = (EntityVillagerTek)entitylivingbaseIn;
/*    */ 
/*    */       
/* 26 */       GL11.glPushMatrix();
/* 27 */       GL11.glPushAttrib(8192);
/* 28 */       Vec3d pos = villager.getPositionVector();
/*    */       
/* 30 */       GL11.glTranslated(-pos.x, -pos.y, -pos.z);
/* 31 */       GL11.glDisable(2896);
/* 32 */       GL11.glDisable(3553);
/*    */ 
/*    */       
/* 35 */       GL11.glPopAttrib();
/* 36 */       GL11.glPopMatrix();
/*    */     } 
/*    */   }
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
/*    */ 
/*    */   
/*    */   public boolean shouldCombineTextures() {
/* 57 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\LayerVillagerDestination.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */