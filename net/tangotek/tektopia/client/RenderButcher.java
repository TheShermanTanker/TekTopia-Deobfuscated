/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityButcher;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RenderButcher<T extends EntityButcher>
/*    */   extends RenderVillager<T>
/*    */ {
/* 14 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderButcher(RenderManager manager) {
/* 17 */     super(manager, "butcher", false, 64, 64, "butcher");
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Factory<T extends EntityButcher>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 25 */       return (Render)new RenderButcher<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderButcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */