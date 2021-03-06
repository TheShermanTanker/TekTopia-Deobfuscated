/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityCleric;
/*    */ 
/*    */ public class RenderCleric<T extends EntityCleric>
/*    */   extends RenderVillager<T> {
/* 10 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderCleric(RenderManager manager) {
/* 13 */     super(manager, "cleric", true, 128, 64, "cleric");
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Factory<T extends EntityCleric>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 21 */       return (Render)new RenderCleric<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderCleric.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */