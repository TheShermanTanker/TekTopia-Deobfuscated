/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import com.leviathanstudio.craftstudio.client.model.ModelCraftStudio;
/*    */ import net.minecraft.client.model.ModelBiped;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ public class ModelRancherHat
/*    */   extends ModelBiped
/*    */ {
/*    */   private final ModelCraftStudio modelHat;
/*    */   
/*    */   public ModelRancherHat() {
/* 22 */     super(0.0F, 0.0F, 64, 64);
/* 23 */     this.modelHat = new ModelCraftStudio("tektopia", "rancher_hat", 64, 64);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
/* 29 */     GlStateManager.pushMatrix();
/* 30 */     float rotateAngleY = netHeadYaw * 0.017453292F;
/* 31 */     float rotateAngleX = headPitch * 0.017453292F;
/*    */     
/* 33 */     if (entityIn.isSneaking())
/*    */     {
/* 35 */       GlStateManager.translate(0.0F, 0.25F, 0.0F);
/*    */     }
/*    */     
/* 38 */     GlStateManager.rotate(rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
/* 39 */     GlStateManager.rotate(rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
/* 40 */     this.modelHat.render();
/* 41 */     GlStateManager.popMatrix();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
/* 51 */     if (entityIn instanceof net.minecraft.entity.item.EntityArmorStand) {
/*    */       
/* 53 */       this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
/* 54 */       this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;
/*    */ 
/*    */       
/* 57 */       this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
/* 58 */       copyModelAngles(this.bipedHead, this.bipedHeadwear);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\ModelRancherHat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */