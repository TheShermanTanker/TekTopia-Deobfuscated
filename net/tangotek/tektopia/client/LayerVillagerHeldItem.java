/*     */ package net.tangotek.tektopia.client;
/*     */ 
/*     */ import com.leviathanstudio.craftstudio.client.model.CSModelRenderer;
/*     */ import com.leviathanstudio.craftstudio.client.model.ModelCraftStudio;
/*     */ import com.leviathanstudio.craftstudio.client.util.MathHelper;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.model.ModelBook;
/*     */ import net.minecraft.client.model.ModelRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
/*     */ import net.minecraft.client.renderer.entity.RenderLivingBase;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerRenderer;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumHandSide;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ @SideOnly(Side.CLIENT)
/*     */ public class LayerVillagerHeldItem
/*     */   implements LayerRenderer<EntityLivingBase> {
/*     */   protected final RenderLivingBase<?> livingEntityRenderer;
/*  32 */   private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation("textures/entity/enchanting_table_book.png");
/*  33 */   private final ModelBook modelBook = new ModelBook();
/*     */ 
/*     */ 
/*     */   
/*     */   public LayerVillagerHeldItem(RenderLivingBase<?> livingEntityRendererIn) {
/*  38 */     this.livingEntityRenderer = livingEntityRendererIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
/*  43 */     boolean flag = (entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT);
/*  44 */     ItemStack leftHandItem = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
/*  45 */     ItemStack rightHandItem = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();
/*     */     
/*  47 */     boolean showLayer = true;
/*  48 */     if (entitylivingbaseIn instanceof EntityVillagerTek) {
/*  49 */       EntityVillagerTek villager = (EntityVillagerTek)entitylivingbaseIn;
/*     */       
/*  51 */       showLayer = !villager.isSleeping();
/*  52 */       if (!villager.getActionItem().isEmpty()) {
/*  53 */         rightHandItem = villager.getActionItem();
/*  54 */         if (rightHandItem.getItem() == ModItems.EMPTY_HAND_ITEM.getItem()) {
/*  55 */           rightHandItem = ItemStack.EMPTY;
/*     */         }
/*     */       } 
/*     */     } 
/*  59 */     if (showLayer && (
/*  60 */       !leftHandItem.isEmpty() || !rightHandItem.isEmpty())) {
/*  61 */       GlStateManager.pushMatrix();
/*     */       
/*  63 */       if ((this.livingEntityRenderer.getMainModel()).isChild) {
/*  64 */         float f = 0.5F;
/*  65 */         GlStateManager.translate(0.0F, 0.75F, 0.0F);
/*  66 */         GlStateManager.scale(0.5F, 0.5F, 0.5F);
/*     */       } 
/*     */       
/*  69 */       renderHeldItem(entitylivingbaseIn, rightHandItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT, ageInTicks, scale);
/*  70 */       renderHeldItem(entitylivingbaseIn, leftHandItem, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT, ageInTicks, scale);
/*  71 */       GlStateManager.popMatrix();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderHeldItem(EntityLivingBase entity, ItemStack itemStack, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide, float ageInTicks, float scale) {
/*  78 */     if (!itemStack.isEmpty()) {
/*     */       
/*  80 */       GlStateManager.pushMatrix();
/*     */       
/*  82 */       if (entity.isSneaking())
/*     */       {
/*  84 */         GlStateManager.translate(0.0F, 0.2F, 0.0F);
/*     */       }
/*     */       
/*  87 */       translateToHand(handSide, scale);
/*  88 */       GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
/*  89 */       GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/*  90 */       boolean flag = (handSide == EnumHandSide.LEFT);
/*     */       
/*  92 */       if (itemStack.getItem() == Items.BOOK) {
/*  93 */         renderBook(ageInTicks);
/*     */       } else {
/*  95 */         Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, itemStack, transformType, flag);
/*     */       } 
/*  97 */       GlStateManager.popMatrix();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void translateToHand(EnumHandSide handSide, float scale) {
/* 103 */     ModelCraftStudio model = (ModelCraftStudio)this.livingEntityRenderer.getMainModel();
/* 104 */     Deque<CSModelRenderer> stack = new ArrayDeque<>();
/* 105 */     for (CSModelRenderer parent : model.getParentBlocks()) {
/* 106 */       if (findChildChain("ArmRightWrist", parent, stack)) {
/* 107 */         stack.push(parent);
/*     */         
/* 109 */         while (!stack.isEmpty()) {
/* 110 */           CSModelRenderer modelRenderer = stack.pop();
/* 111 */           GlStateManager.translate(modelRenderer.rotationPointX * scale, modelRenderer.rotationPointY * scale, modelRenderer.rotationPointZ * scale);
/* 112 */           FloatBuffer buf = MathHelper.makeFloatBuffer(modelRenderer.getRotationMatrix());
/* 113 */           GlStateManager.multMatrix(buf);
/* 114 */           GlStateManager.translate(modelRenderer.offsetX * scale, modelRenderer.offsetY * scale, modelRenderer.offsetZ * scale);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean findChildChain(String name, CSModelRenderer modelRenderer, Deque<CSModelRenderer> stack) {
/* 126 */     if (modelRenderer.boxName.equals(name)) {
/* 127 */       return true;
/*     */     }
/* 129 */     if (modelRenderer.childModels != null) {
/* 130 */       for (ModelRenderer child : modelRenderer.childModels) {
/* 131 */         CSModelRenderer csModel = (CSModelRenderer)child;
/* 132 */         if (findChildChain(name, csModel, stack)) {
/* 133 */           stack.push(csModel);
/* 134 */           return true;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 139 */     return false;
/*     */   }
/*     */   
/*     */   protected void renderBook(float ageInTicks) {
/* 143 */     GlStateManager.pushMatrix();
/*     */ 
/*     */ 
/*     */     
/* 147 */     float f = 25.5F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
/*     */     
/* 165 */     float rot = 80.0F;
/* 166 */     GlStateManager.rotate(rot, 1.0F, 0.0F, 0.0F);
/* 167 */     GlStateManager.rotate(40.0F, 0.0F, 0.0F, 1.0F);
/* 168 */     GlStateManager.translate(0.0D, -0.2D, -0.3D);
/* 169 */     this.livingEntityRenderer.bindTexture(TEXTURE_BOOK);
/*     */ 
/*     */     
/* 172 */     float f3 = f + 0.25F;
/* 173 */     float f4 = f + 0.75F;
/*     */     
/* 175 */     f3 = (f3 - MathHelper.fastFloor(f3)) * 1.6F - 0.3F;
/* 176 */     f4 = (f4 - MathHelper.fastFloor(f4)) * 1.6F - 0.3F;
/*     */     
/* 178 */     if (f3 < 0.0F)
/*     */     {
/* 180 */       f3 = 0.0F;
/*     */     }
/*     */     
/* 183 */     if (f4 < 0.0F)
/*     */     {
/* 185 */       f4 = 0.0F;
/*     */     }
/*     */     
/* 188 */     if (f3 > 1.0F)
/*     */     {
/* 190 */       f3 = 1.0F;
/*     */     }
/*     */     
/* 193 */     if (f4 > 1.0F)
/*     */     {
/* 195 */       f4 = 1.0F;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 200 */     float f5 = 12.2F;
/* 201 */     GlStateManager.enableCull();
/* 202 */     this.modelBook.render((Entity)null, 1500.0F, 0.17F, 0.9F, 1.0F, 0.0F, 0.0625F);
/* 203 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldCombineTextures() {
/* 210 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\client\LayerVillagerHeldItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */