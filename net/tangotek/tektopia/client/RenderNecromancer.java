/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import com.leviathanstudio.craftstudio.client.model.ModelCraftStudio;
/*    */ import net.minecraft.client.model.ModelBase;
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderLiving;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityNecromancer;
/*    */ 
/*    */ public class RenderNecromancer<T extends EntityLiving> extends RenderLiving<T> {
/*    */   protected final String textureName;
/* 16 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderNecromancer(RenderManager manager, String modelName, int texW, int texH, String textureName, float shadowSize) {
/* 19 */     super(manager, (ModelBase)new ModelCraftStudio("tektopia", modelName, texW, texH), shadowSize);
/* 20 */     this.textureName = textureName;
/*    */   }
/*    */   
/*    */   public RenderNecromancer(RenderManager manager, String modelName, int texW, int texH, String textureName) {
/* 24 */     this(manager, modelName, texW, texH, textureName, 0.4F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(T entity) {
/* 29 */     return new ResourceLocation("tektopia", "textures/entity/" + this.textureName + "_" + ((EntityNecromancer)entity).getLevel() + ".png");
/*    */   }
/*    */   
/*    */   public static class Factory<T extends EntityNecromancer>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 36 */       return (Render)new RenderNecromancer<>(manager, "necromancer", 128, 64, "necromancer", 0.4F);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getTeamColor(T entityIn) {
/* 43 */     return 1908001;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderNecromancer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */