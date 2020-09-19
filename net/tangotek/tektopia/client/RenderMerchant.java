/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityMerchant;
/*    */ 
/*    */ public class RenderMerchant<T extends EntityMerchant>
/*    */   extends RenderVillager<T>
/*    */ {
/* 11 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderMerchant(RenderManager manager) {
/* 14 */     super(manager, "merchant", false, 64, 64, "merchant");
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Factory<T extends EntityMerchant>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 22 */       return (Render)new RenderMerchant<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderMerchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */