/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityNitwit;
/*    */ 
/*    */ 
/*    */ public class RenderNitwit<T extends EntityNitwit>
/*    */   extends RenderVillager<T>
/*    */ {
/* 12 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderNitwit(RenderManager manager) {
/* 15 */     super(manager, "nitwit", false, 64, 64, "nitwit");
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Factory<T extends EntityNitwit>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 23 */       return (Render)new RenderNitwit<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderNitwit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */