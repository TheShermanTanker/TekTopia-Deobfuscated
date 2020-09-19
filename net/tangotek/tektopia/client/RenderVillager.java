/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import com.leviathanstudio.craftstudio.client.model.ModelCraftStudio;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.entity.RenderLiving;
/*    */ import net.minecraft.client.renderer.entity.RenderLivingBase;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ public class RenderVillager<T extends EntityVillagerTek>
/*    */   extends RenderLiving<T> {
/*    */   protected final String textureName;
/*    */   protected final ModelCraftStudio maleModel;
/*    */   
/*    */   public RenderVillager(RenderManager manager, String modelName, boolean hasGenderModels, int texW, int texH, String textureName, float shadowSize) {
/* 21 */     super(manager, (ModelBase)new ModelCraftStudio("tektopia", modelName + "_m", texW, texH), shadowSize);
/* 22 */     addLayer(new LayerVillagerHeldItem((RenderLivingBase<?>)this));
/* 23 */     this.textureName = textureName;
/* 24 */     this.maleModel = (ModelCraftStudio)this.mainModel;
/* 25 */     if (hasGenderModels) {
/* 26 */       this.femaleModel = new ModelCraftStudio("tektopia", modelName + "_f", texW, texH);
/*    */     } else {
/* 28 */       this.femaleModel = null;
/*    */     } 
/* 30 */     setupTextures();
/*    */   }
/*    */   protected final ModelCraftStudio femaleModel; protected ResourceLocation[] maleTextures; protected ResourceLocation[] femaleTextures;
/*    */   public RenderVillager(RenderManager manager, String modelName, boolean hasGenders, int texW, int texH, String textureName) {
/* 34 */     this(manager, modelName, hasGenders, texW, texH, textureName, 0.4F);
/*    */   }
/*    */   
/*    */   protected void setupTextures() {
/* 38 */     this.maleTextures = new ResourceLocation[] { new ResourceLocation("tektopia", "textures/entity/" + this.textureName + "_m.png") };
/* 39 */     this.femaleTextures = new ResourceLocation[] { new ResourceLocation("tektopia", "textures/entity/" + this.textureName + "_f.png") };
/*    */   }
/*    */ 
/*    */   
/*    */   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
/* 44 */     if (!entity.isMale() && this.femaleModel != null) {
/* 45 */       this.mainModel = (ModelBase)this.femaleModel;
/*    */     } else {
/* 47 */       this.mainModel = (ModelBase)this.maleModel;
/*    */     } 
/* 49 */     super.doRender((EntityLiving)entity, x, y, z, entityYaw, partialTicks);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(T entity) {
/* 54 */     return entity.isMale() ? this.maleTextures[0] : this.femaleTextures[0];
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void applyRotations(T entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
/* 60 */     T t = entityLiving;
/* 61 */     if (t.getForceAxis() >= 0) {
/* 62 */       GlStateManager.rotate((t.getForceAxis() * -90), 0.0F, 1.0F, 0.0F);
/*    */     }
/*    */     else {
/*    */       
/* 66 */       super.applyRotations((EntityLivingBase)entityLiving, p_77043_2_, rotationYaw, partialTicks);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */