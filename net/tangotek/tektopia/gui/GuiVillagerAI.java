/*     */ package net.tangotek.tektopia.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.text.TextComponentTranslation;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.network.PacketAIFilter;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class GuiVillagerAI extends GuiContainer {
/*  27 */   public static final ResourceLocation GUI_VILLAGER_AI_TEXTURE = new ResourceLocation("tektopia", "textures/gui/container/villager_ai.png");
/*     */   
/*     */   private final EntityVillagerTek villager;
/*  30 */   private float currentScroll = 0.0F;
/*  31 */   private int scrollIndex = 0;
/*     */   private List<String> aiFilters;
/*  33 */   private final int MAX_VISIBLE_ROWS = 10;
/*     */   
/*     */   private boolean isScrolling;
/*     */   
/*     */   private boolean wasClicking;
/*     */ 
/*     */   
/*     */   public GuiVillagerAI(EntityVillagerTek villager) {
/*  41 */     super(new Container()
/*     */         {
/*     */           public boolean canInteractWith(EntityPlayer playerIn)
/*     */           {
/*  45 */             return false;
/*     */           }
/*     */         });
/*  48 */     this.xSize = 178;
/*  49 */     this.ySize = 157;
/*  50 */     this.villager = villager;
/*  51 */     this.aiFilters = villager.getAIFilters();
/*  52 */     Collections.sort(this.aiFilters);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  57 */     super.initGui();
/*  58 */     for (int buttonId = 0; buttonId < 10; buttonId++) {
/*  59 */       addButton(new GuiTekCheckBox(buttonId, this.guiLeft + 8, this.guiTop + 11 * buttonId + 39, 10, 10, true, 190, 57, 180, 57, GUI_VILLAGER_AI_TEXTURE));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  65 */     if (button.enabled && 
/*  66 */       button instanceof GuiTekCheckBox) {
/*  67 */       GuiTekCheckBox checkBox = (GuiTekCheckBox)button;
/*  68 */       int index = checkBox.id + this.scrollIndex;
/*  69 */       String aiFilter = this.aiFilters.get(index);
/*     */ 
/*     */       
/*  72 */       TekVillager.NETWORK.sendToServer((IMessage)new PacketAIFilter((Entity)this.villager, aiFilter, checkBox.isChecked()));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean needsScrollBar() {
/*  78 */     return (this.aiFilters.size() > 10);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  83 */     super.handleMouseInput();
/*  84 */     int i = Mouse.getEventDWheel();
/*     */     
/*  86 */     if (i != 0 && needsScrollBar()) {
/*     */       
/*  88 */       int j = this.aiFilters.size() - 10;
/*     */ 
/*     */       
/*  91 */       if (i > 0)
/*     */       {
/*  93 */         i = 1;
/*     */       }
/*     */       
/*  96 */       if (i < 0)
/*     */       {
/*  98 */         i = -1;
/*     */       }
/*     */       
/* 101 */       this.currentScroll = (float)(this.currentScroll - i / j);
/* 102 */       this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
/* 103 */       scrollTo(this.currentScroll);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scrollTo(float scroll) {
/* 108 */     this.scrollIndex = (int)((this.aiFilters.size() - 10) * scroll);
/* 109 */     if (this.scrollIndex < 0) {
/* 110 */       this.scrollIndex = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 116 */     if (isPointInRegion(14, 0, 28, 29, mouseX, mouseY))
/*     */     {
/* 118 */       this.mc.displayGuiScreen((GuiScreen)new GuiVillager(this.villager));
/*     */     }
/*     */     
/* 121 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 129 */     drawDefaultBackground();
/*     */     
/* 131 */     boolean flag = Mouse.isButtonDown(0);
/* 132 */     int i = this.guiLeft;
/* 133 */     int j = this.guiTop;
/* 134 */     int k = i + 160;
/* 135 */     int l = j + 39;
/* 136 */     int i1 = k + 14;
/* 137 */     int j1 = l + 112;
/*     */     
/* 139 */     if (!this.wasClicking && flag && mouseX >= k && mouseY >= l && mouseX < i1 && mouseY < j1)
/*     */     {
/* 141 */       this.isScrolling = needsScrollBar();
/*     */     }
/*     */     
/* 144 */     if (!flag)
/*     */     {
/* 146 */       this.isScrolling = false;
/*     */     }
/*     */     
/* 149 */     this.wasClicking = flag;
/*     */     
/* 151 */     if (this.isScrolling) {
/*     */       
/* 153 */       this.currentScroll = ((mouseY - l) - 7.5F) / ((j1 - l) - 15.0F);
/* 154 */       this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
/* 155 */       scrollTo(this.currentScroll);
/*     */     } 
/*     */     
/* 158 */     updateChecks();
/* 159 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   private void updateChecks() {
/* 163 */     for (GuiButton button : this.buttonList) {
/* 164 */       if (button instanceof GuiTekCheckBox) {
/* 165 */         GuiTekCheckBox checkBox = (GuiTekCheckBox)button;
/* 166 */         int filterIndex = checkBox.id + this.scrollIndex;
/* 167 */         if (filterIndex < this.aiFilters.size()) {
/* 168 */           checkBox.visible = true;
/* 169 */           checkBox.setIsChecked(this.villager.isAIFilterEnabled(this.aiFilters.get(filterIndex)));
/*     */           continue;
/*     */         } 
/* 172 */         checkBox.visible = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/* 183 */     GlStateManager.disableLighting();
/* 184 */     GlStateManager.disableBlend();
/*     */     
/* 186 */     this.itemRender.renderItemAndEffectIntoGUI((EntityLivingBase)this.mc.player, new ItemStack((Item)ModItems.getProfessionToken(this.villager.getProfessionType())), 20, 10);
/*     */     
/* 188 */     int topY = 40;
/*     */     
/* 190 */     for (int i = 0; i < 10; i++) {
/* 191 */       int filterIndex = this.scrollIndex + i;
/* 192 */       if (filterIndex < this.aiFilters.size()) {
/* 193 */         String filterName = this.aiFilters.get(filterIndex);
/* 194 */         TextComponentTranslation textComponentTranslation = new TextComponentTranslation("ai.filter." + filterName, new Object[0]);
/* 195 */         this.fontRenderer.drawString(textComponentTranslation.getUnformattedText(), 20, 40 + i * 11, 4210752);
/*     */       } 
/*     */     } 
/*     */     
/* 199 */     GlStateManager.enableLighting();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/* 207 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 208 */     this.mc.getTextureManager().bindTexture(GUI_VILLAGER_AI_TEXTURE);
/* 209 */     int i = (this.width - this.xSize) / 2;
/* 210 */     int j = (this.height - this.ySize) / 2;
/* 211 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*     */     
/* 213 */     int scrollLeft = this.guiLeft + 160;
/* 214 */     int scrollTop = this.guiTop + 39;
/* 215 */     int scrollHeight = scrollTop + 112;
/*     */     
/* 217 */     drawTexturedModalRect(scrollLeft, scrollTop + (int)((scrollHeight - scrollTop - 17) * this.currentScroll), 180 + (needsScrollBar() ? 0 : 12), 38, 12, 15);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\gui\GuiVillagerAI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */