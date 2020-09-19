/*     */ package net.tangotek.tektopia.client;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
/*     */ import com.leviathanstudio.craftstudio.client.model.ModelCraftStudio;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.model.ModelRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.entity.RenderLivingBase;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerRenderer;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.inventory.EntityEquipmentSlot;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class LayerVillagerArmor
/*     */   implements LayerRenderer<EntityLivingBase>
/*     */ {
/*     */   private final RenderLivingBase<?> renderer;
/*  22 */   private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();
/*  23 */   private float alpha = 1.0F;
/*  24 */   private float colorR = 1.0F;
/*  25 */   private float colorG = 1.0F;
/*  26 */   private float colorB = 1.0F;
/*     */ 
/*     */   
/*     */   public LayerVillagerArmor(RenderLivingBase<?> rendererIn) {
/*  30 */     this.renderer = rendererIn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doRenderLayer(EntityLivingBase entityVillager, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
/*  38 */     boolean showArmor = true;
/*  39 */     if (entityVillager instanceof EntityVillagerTek) {
/*  40 */       showArmor = !((EntityVillagerTek)entityVillager).isSleeping();
/*     */     }
/*     */     
/*  43 */     if (showArmor) {
/*  44 */       ModelCraftStudio model = (ModelCraftStudio)this.renderer.getMainModel();
/*  45 */       for (CSModelRenderer parent : model.getParentBlocks()) {
/*  46 */         renderArmorModel(parent, entityVillager, scale);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void renderArmorModel(CSModelRenderer modelRenderer, EntityLivingBase entityVillager, float scale) {
/*  52 */     renderArmorSlot(modelRenderer, entityVillager, "ArmorChest", EntityEquipmentSlot.CHEST, scale);
/*  53 */     renderArmorSlot(modelRenderer, entityVillager, "ArmorLeg", EntityEquipmentSlot.LEGS, scale);
/*  54 */     renderArmorSlot(modelRenderer, entityVillager, "ArmorHead", EntityEquipmentSlot.HEAD, scale);
/*  55 */     renderArmorSlot(modelRenderer, entityVillager, "ArmorFeet", EntityEquipmentSlot.FEET, scale);
/*     */     
/*  57 */     if (modelRenderer.childModels != null) {
/*  58 */       for (ModelRenderer child : modelRenderer.childModels) {
/*  59 */         renderArmorModel((CSModelRenderer)child, entityVillager, scale);
/*     */       }
/*     */     }
/*     */     
/*  63 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   private void renderArmorSlot(CSModelRenderer modelRenderer, EntityLivingBase entityVillager, String modelPrefix, EntityEquipmentSlot slot, float scale) {
/*  67 */     if (modelRenderer.boxName.startsWith(modelPrefix)) {
/*  68 */       ItemStack itemStack = entityVillager.getItemStackFromSlot(slot);
/*  69 */       if (itemStack.getItem() instanceof ItemArmor) {
/*  70 */         ItemArmor itemArmor = (ItemArmor)itemStack.getItem();
/*  71 */         if (itemArmor.getEquipmentSlot() == slot) {
/*  72 */           int i = itemArmor.getColor(itemStack);
/*  73 */           float r = (i >> 16 & 0xFF) / 255.0F;
/*  74 */           float g = (i >> 8 & 0xFF) / 255.0F;
/*  75 */           float b = (i & 0xFF) / 255.0F;
/*  76 */           GlStateManager.color(this.colorR * r, this.colorG * g, this.colorB * b, this.alpha);
/*  77 */           modelRenderer.showModel = true;
/*  78 */           modelRenderer.render(scale);
/*  79 */           modelRenderer.showModel = false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldCombineTextures() {
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ResourceLocation getArmorResource(String domain) {
/*  94 */     String s = String.format("%s:textures/models/armor/armor.png", new Object[] { domain });
/*  95 */     ResourceLocation resourcelocation = ARMOR_TEXTURE_RES_MAP.get(s);
/*     */     
/*  97 */     if (resourcelocation == null) {
/*     */       
/*  99 */       resourcelocation = new ResourceLocation(s);
/* 100 */       ARMOR_TEXTURE_RES_MAP.put(s, resourcelocation);
/*     */     } 
/*     */     
/* 103 */     return resourcelocation;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\LayerVillagerArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */