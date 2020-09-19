/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityNomad;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ public class RenderNomad<T extends EntityNomad> extends RenderVillager<T> {
/* 12 */   public static final Factory FACTORY = new Factory<>();
/* 13 */   public static final ResourceLocation[] MALE_TEXTURES = new ResourceLocation[] { new ResourceLocation("tektopia", "textures/entity/nomad0M.png"), new ResourceLocation("tektopia", "textures/entity/nomad1M.png") };
/* 14 */   public static final ResourceLocation FEMALE_TEXTURE = new ResourceLocation("tektopia", "textures/entity/nomad0F.png");
/*    */   
/*    */   public RenderNomad(RenderManager manager) {
/* 17 */     super(manager, "nomad", false, 64, 64, "nomad");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setupTextures() {
/* 24 */     this.maleTextures = new ResourceLocation[] { new ResourceLocation("tektopia", "textures/entity/" + this.textureName + "0_m.png"), new ResourceLocation("tektopia", "textures/entity/" + this.textureName + "1_m.png") };
/* 25 */     this.femaleTextures = new ResourceLocation[] { new ResourceLocation("tektopia", "textures/entity/" + this.textureName + "0_f.png") };
/*    */   }
/*    */ 
/*    */   
/*    */   protected ResourceLocation getEntityTexture(T entity) {
/* 30 */     if (!entity.isMale()) {
/* 31 */       return this.femaleTextures[0];
/*    */     }
/* 33 */     return this.maleTextures[(int)(Math.abs(entity.getUniqueID().getLeastSignificantBits()) % 2L)];
/*    */   }
/*    */   
/*    */   public static class Factory<T extends EntityNomad>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 40 */       return (Render)new RenderNomad<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderNomad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */