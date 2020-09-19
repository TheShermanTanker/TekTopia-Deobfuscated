/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityTradesman;
/*    */ 
/*    */ public class RenderTradesman<T extends EntityTradesman>
/*    */   extends RenderVillager<T> {
/* 10 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderTradesman(RenderManager manager) {
/* 13 */     super(manager, "tradesman", false, 64, 64, "tradesman");
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Factory<T extends EntityTradesman>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 21 */       return (Render)new RenderTradesman<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderTradesman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */