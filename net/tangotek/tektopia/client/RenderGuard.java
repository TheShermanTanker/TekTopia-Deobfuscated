/*    */ package net.tangotek.tektopia.client;
/*    */ 
/*    */ import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
/*    */ import com.leviathanstudio.craftstudio.client.model.ModelCraftStudio;
/*    */ import net.minecraft.client.model.ModelRenderer;
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.inventory.EntityEquipmentSlot;
/*    */ import net.minecraft.item.ItemArmor;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import net.tangotek.tektopia.entities.EntityGuard;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RenderGuard<T extends EntityGuard>
/*    */   extends RenderVillager<T>
/*    */ {
/* 22 */   public static final Factory FACTORY = new Factory<>();
/*    */   
/*    */   public RenderGuard(RenderManager manager) {
/* 25 */     super(manager, "guard", false, 128, 64, "guard");
/*    */   }
/*    */ 
/*    */   
/*    */   private void updateArmorSlot(CSModelRenderer modelRenderer, EntityGuard entityGuard, String modelPrefix, EntityEquipmentSlot slot) {
/* 30 */     if (modelRenderer.boxName.startsWith(modelPrefix)) {
/* 31 */       ItemStack itemStack = entityGuard.getItemStackFromSlot(slot);
/* 32 */       if (itemStack.getItem() instanceof ItemArmor) {
/* 33 */         ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
/* 34 */         if (itemArmor.getEquipmentSlot() == slot) {
/* 35 */           String matName = itemArmor.getArmorMaterial().getName();
/* 36 */           String modelMat = modelRenderer.boxName.substring(modelPrefix.length());
/* 37 */           modelRenderer.showModel = modelMat.toLowerCase().startsWith(matName);
/* 38 */           if (modelRenderer.showModel && 
/* 39 */             modelRenderer.boxName.substring(modelPrefix.length() + matName.length()).startsWith("Capt")) {
/* 40 */             modelRenderer.showModel = entityGuard.isCaptain();
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void updateArmor(CSModelRenderer modelRenderer, EntityGuard entityGuard) {
/* 48 */     modelRenderer.showModel = !modelRenderer.boxName.startsWith("Armor");
/*    */     
/* 50 */     if (!entityGuard.isSleeping()) {
/* 51 */       updateArmorSlot(modelRenderer, entityGuard, "ArmorChest", EntityEquipmentSlot.CHEST);
/*    */       
/* 53 */       if (!modelRenderer.showModel) {
/* 54 */         updateArmorSlot(modelRenderer, entityGuard, "ArmorLeg", EntityEquipmentSlot.LEGS);
/*    */       }
/* 56 */       if (!modelRenderer.showModel) {
/* 57 */         updateArmorSlot(modelRenderer, entityGuard, "ArmorHead", EntityEquipmentSlot.HEAD);
/*    */       }
/* 59 */       if (!modelRenderer.showModel) {
/* 60 */         updateArmorSlot(modelRenderer, entityGuard, "ArmorFeet", EntityEquipmentSlot.FEET);
/*    */       }
/*    */     } 
/* 63 */     if (modelRenderer.childModels != null) {
/* 64 */       for (ModelRenderer child : modelRenderer.childModels) {
/* 65 */         updateArmor((CSModelRenderer)child, entityGuard);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void preRenderCallback(EntityGuard entityGuard, float partialTickTime) {
/* 72 */     ModelCraftStudio model = (ModelCraftStudio)getMainModel();
/* 73 */     for (CSModelRenderer parent : model.getParentBlocks()) {
/* 74 */       updateArmor(parent, entityGuard);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory<T extends EntityGuard>
/*    */     implements IRenderFactory<T>
/*    */   {
/*    */     public Render<? super T> createRenderFor(RenderManager manager) {
/* 82 */       return (Render)new RenderGuard<>(manager);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\RenderGuard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */