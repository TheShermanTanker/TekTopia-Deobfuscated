/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
/*    */ import com.leviathanstudio.craftstudio.client.model.ModelCraftStudio;
/*    */ import net.minecraft.client.model.ModelRenderer;
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityBard;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RenderBard<T extends EntityBard>
/*    */   extends RenderVillager<T>
/*    */ {
/* 18 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderBard(RenderManager manager) {
/* 21 */     super(manager, "bard", false, 128, 64, "bard");
/*    */   }
/*    */   
/*    */   private void updateInstrument(CSModelRenderer modelRenderer, EntityBard entityBard) {
/* 25 */     if (modelRenderer.boxName.startsWith("Flute")) {
/* 26 */       modelRenderer.showModel = entityBard.isPerforming();
/*    */     }
/*    */     
/* 29 */     if (modelRenderer.childModels != null) {
/* 30 */       for (ModelRenderer child : modelRenderer.childModels) {
/* 31 */         updateInstrument((CSModelRenderer)child, entityBard);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void preRenderCallback(EntityBard entityBard, float partialTickTime) {
/* 38 */     ModelCraftStudio model = (ModelCraftStudio)getMainModel();
/* 39 */     for (CSModelRenderer parent : model.getParentBlocks()) {
/* 40 */       updateInstrument(parent, entityBard);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory<T extends EntityBard>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 48 */       return (Render)new RenderBard<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderBard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */