/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityFarmer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RenderFarmer<T extends EntityFarmer>
/*    */   extends RenderVillager<T>
/*    */ {
/* 14 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderFarmer(RenderManager manager) {
/* 17 */     super(manager, "farmer", true, 64, 64, "farmer");
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Factory<T extends EntityFarmer>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 25 */       return (Render)new RenderFarmer<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderFarmer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */