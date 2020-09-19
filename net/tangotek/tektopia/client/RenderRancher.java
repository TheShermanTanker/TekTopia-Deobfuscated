/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityRancher;
/*    */ 
/*    */ public class RenderRancher<T extends EntityRancher>
/*    */   extends RenderVillager<T> {
/* 10 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderRancher(RenderManager manager) {
/* 13 */     super(manager, "rancher", false, 128, 64, "rancher");
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Factory<T extends EntityRancher>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 21 */       return (Render)new RenderRancher<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderRancher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */