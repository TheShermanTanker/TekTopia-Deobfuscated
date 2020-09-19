/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityEnchanter;
/*    */ 
/*    */ public class RenderEnchanter<T extends EntityEnchanter>
/*    */   extends RenderVillager<T>
/*    */ {
/* 11 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderEnchanter(RenderManager manager) {
/* 14 */     super(manager, "enchanter", false, 64, 64, "enchanter");
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Factory<T extends EntityEnchanter>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 22 */       return (Render)new RenderEnchanter<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderEnchanter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */