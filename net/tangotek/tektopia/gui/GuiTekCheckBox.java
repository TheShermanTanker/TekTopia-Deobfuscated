/*    */ package net.tangotek.tektopia.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class GuiTekCheckBox
/*    */   extends GuiButton
/*    */ {
/*    */   private boolean isChecked;
/*    */   ResourceLocation resourceLocation;
/*    */   private int checkedTexX;
/*    */   private int checkedTexY;
/*    */   private int uncheckedTexX;
/*    */   private int uncheckedTexY;
/*    */   
/*    */   public GuiTekCheckBox(int buttonId, int x, int y, int widthIn, int heightIn, boolean isChecked, int checkedTexX, int checkedTexY, int uncheckedTexX, int uncheckedTexY, ResourceLocation resourceLocation) {
/* 19 */     super(buttonId, x, y, widthIn, heightIn, "");
/* 20 */     this.resourceLocation = resourceLocation;
/* 21 */     this.isChecked = isChecked;
/* 22 */     this.width = widthIn;
/* 23 */     this.height = heightIn;
/* 24 */     this.checkedTexX = checkedTexX;
/* 25 */     this.checkedTexY = checkedTexY;
/* 26 */     this.uncheckedTexX = uncheckedTexX;
/* 27 */     this.uncheckedTexY = uncheckedTexY;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
/* 36 */     if (this.visible) {
/*    */       
/* 38 */       this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
/* 39 */       mc.getTextureManager().bindTexture(this.resourceLocation);
/* 40 */       GlStateManager.disableDepth();
/*    */       
/* 42 */       if (this.isChecked) {
/* 43 */         drawTexturedModalRect(this.x, this.y, this.checkedTexX, this.checkedTexY, this.width, this.height);
/*    */       } else {
/*    */         
/* 46 */         drawTexturedModalRect(this.x, this.y, this.uncheckedTexX, this.uncheckedTexY, this.width, this.height);
/*    */       } 
/*    */       
/* 49 */       GlStateManager.enableDepth();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/* 60 */     if (this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
/*    */       
/* 62 */       this.isChecked = !this.isChecked;
/* 63 */       return true;
/*    */     } 
/*    */     
/* 66 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isChecked() {
/* 71 */     return this.isChecked;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setIsChecked(boolean isChecked) {
/* 76 */     this.isChecked = isChecked;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\gui\GuiTekCheckBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */