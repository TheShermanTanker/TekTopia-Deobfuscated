/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityBlacksmith;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RenderBlacksmith<T extends EntityBlacksmith>
/*    */   extends RenderVillager<T>
/*    */ {
/* 14 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderBlacksmith(RenderManager manager) {
/* 17 */     super(manager, "blacksmith", false, 64, 64, "blacksmith");
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Factory<T extends EntityBlacksmith>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 25 */       return (Render)new RenderBlacksmith<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderBlacksmith.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */