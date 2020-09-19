/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityMiner;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RenderMiner<T extends EntityMiner>
/*    */   extends RenderVillager<T>
/*    */ {
/* 14 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderMiner(RenderManager manager) {
/* 17 */     super(manager, "miner", false, 64, 64, "miner");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Factory<T extends EntityMiner>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 27 */       return (Render)new RenderMiner<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderMiner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */