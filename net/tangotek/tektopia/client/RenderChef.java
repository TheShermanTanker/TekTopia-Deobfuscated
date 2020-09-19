/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityChef;
/*    */ 
/*    */ public class RenderChef<T extends EntityChef>
/*    */   extends RenderVillager<T> {
/* 10 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderChef(RenderManager manager) {
/* 13 */     super(manager, "chef", true, 128, 64, "chef");
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Factory<T extends EntityChef>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 21 */       return (Render)new RenderChef<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderChef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */