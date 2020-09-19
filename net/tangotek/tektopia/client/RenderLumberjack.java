/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityLumberjack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RenderLumberjack<T extends EntityLumberjack>
/*    */   extends RenderVillager<T>
/*    */ {
/* 16 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderLumberjack(RenderManager manager) {
/* 19 */     super(manager, "lumberjack", false, 64, 64, "lumberjack");
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Factory<T extends EntityLumberjack>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 27 */       return (Render)new RenderLumberjack<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderLumberjack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */