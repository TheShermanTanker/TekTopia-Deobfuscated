/*    */ package net.tangotek.tektopia.proxy;
/*    */ 
/*    */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*    */ import com.leviathanstudio.craftstudio.common.animation.IAnimated;
/*    */ import net.minecraft.world.chunk.Chunk;
/*    */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*    */ import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
/*    */ import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @SideOnly(Side.SERVER)
/*    */ public class ServerProxy
/*    */   extends CommonProxy
/*    */ {
/*    */   public void preInit(FMLPreInitializationEvent e) {
/* 22 */     super.preInit(e);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void init(FMLInitializationEvent e) {
/* 28 */     super.init(e);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void postInit(FMLPostInitializationEvent e) {
/* 35 */     super.postInit(e);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends IAnimated> AnimationHandler<T> getNewAnimationHandler(Class<T> animatedClass) {
/* 40 */     return (AnimationHandler)new TekServerAnimationHandler<>();
/*    */   }
/*    */   
/*    */   public void onChunkLoaded(Chunk ch) {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\proxy\ServerProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */