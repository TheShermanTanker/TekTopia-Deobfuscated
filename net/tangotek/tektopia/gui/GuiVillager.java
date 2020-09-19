/*     */ package net.tangotek.tektopia.gui;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.fml.client.config.HoverChecker;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ @SideOnly(Side.CLIENT)
/*     */ public class GuiVillager extends GuiContainer {
/*  24 */   private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("tektopia", "textures/gui/container/villager_main.png");
/*     */   
/*     */   private final EntityVillagerTek villager;
/*     */   
/*  28 */   private Map<HoverChecker, String> hoverChecks = new HashMap<>();
/*     */ 
/*     */   
/*     */   public GuiVillager(EntityVillagerTek villager) {
/*  32 */     super(new ContainerVillager(villager.getInventory()));
/*  33 */     this.villager = villager;
/*  34 */     this.xSize = 178;
/*  35 */     this.ySize = 157;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/*  42 */     if (this.villager.isRole(VillagerRole.VILLAGER) && isPointInRegion(43, 0, 28, 29, mouseX, mouseY))
/*     */     {
/*  44 */       this.mc.displayGuiScreen((GuiScreen)new GuiVillagerAI(this.villager));
/*     */     }
/*     */     
/*  47 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  55 */     drawDefaultBackground();
/*  56 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*  57 */     renderHoveredToolTip(mouseX, mouseY);
/*     */     
/*  59 */     for (Map.Entry<HoverChecker, String> entry : this.hoverChecks.entrySet()) {
/*  60 */       if (((HoverChecker)entry.getKey()).checkHover(mouseX, mouseY)) {
/*  61 */         drawHoveringText(entry.getValue(), mouseX, mouseY);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/*  74 */     GlStateManager.disableLighting();
/*  75 */     GlStateManager.disableBlend();
/*     */     
/*  77 */     boolean firstDraw = this.hoverChecks.isEmpty();
/*  78 */     this.itemRender.renderItemAndEffectIntoGUI((EntityLivingBase)this.mc.player, new ItemStack((Item)ModItems.getProfessionToken(this.villager.getProfessionType())), 20, 10);
/*  79 */     this.itemRender.renderItemAndEffectIntoGUI((EntityLivingBase)this.mc.player, new ItemStack((Item)ModItems.getProfessionToken(this.villager.getProfessionType())), 5, 34);
/*  80 */     if (firstDraw) {
/*  81 */       HoverChecker hoverChecker = new HoverChecker(this.guiTop + 34, this.guiTop + 34 + 16, this.guiLeft + 5, this.guiLeft + 5 + 16, 350);
/*  82 */       this.hoverChecks.put(hoverChecker, I18n.format("item.prof_" + (this.villager.getProfessionType()).name + ".name", new Object[0]));
/*     */     } 
/*  84 */     this.fontRenderer.drawString(this.villager.getDisplayName().getFormattedText(), 27, 39, 4210752);
/*     */     
/*  86 */     drawStat("ui.villager.health", 185, 29, 2, 52, this.villager.getHealth(), this.villager.getMaxHealth(), firstDraw);
/*  87 */     drawStat("ui.villager.hunger", 196, 29, 6, 63, this.villager.getHunger(), this.villager.getMaxHunger(), firstDraw);
/*  88 */     drawStat("ui.villager.happy", 207, 29, 4, 74, this.villager.getHappy(), this.villager.getMaxHappy(), firstDraw);
/*  89 */     drawStat("ui.villager.intelligence", 218, 29, 0, 85, this.villager.getIntelligence(), this.villager.getMaxIntelligence(), firstDraw);
/*     */     
/*  91 */     drawProfessions(firstDraw);
/*     */     
/*  93 */     GlStateManager.enableLighting();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
/* 101 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 102 */     this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
/* 103 */     int i = (this.width - this.xSize) / 2;
/* 104 */     int j = (this.height - this.ySize) / 2;
/* 105 */     drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void drawProfessions(boolean firstDraw) {
/* 111 */     List<AbstractMap.SimpleEntry<ProfessionType, Integer>> skillList = new ArrayList<>();
/* 112 */     for (ProfessionType pt : ProfessionType.values()) {
/* 113 */       int skill = this.villager.getSkill(pt);
/* 114 */       if (skill > 0 && pt.canCopy) {
/* 115 */         skillList.add(new AbstractMap.SimpleEntry<>(pt, Integer.valueOf(skill)));
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 120 */     Comparator<AbstractMap.SimpleEntry<ProfessionType, Integer>> comp = Comparator.comparing(AbstractMap.SimpleEntry::getValue);
/* 121 */     skillList.sort(comp);
/* 122 */     Collections.reverse(skillList);
/*     */ 
/*     */     
/* 125 */     drawProfession(skillList, 0, 110, 55, firstDraw);
/* 126 */     drawProfession(skillList, 1, 110, 73, firstDraw);
/* 127 */     drawProfession(skillList, 2, 145, 55, firstDraw);
/* 128 */     drawProfession(skillList, 3, 145, 73, firstDraw);
/*     */   }
/*     */   
/*     */   private void drawProfession(List<AbstractMap.SimpleEntry<ProfessionType, Integer>> skillList, int index, int x, int y, boolean firstDraw) {
/* 132 */     if (index < skillList.size()) {
/* 133 */       ProfessionType profType = (ProfessionType)((AbstractMap.SimpleEntry)skillList.get(index)).getKey();
/* 134 */       int level = ((Integer)((AbstractMap.SimpleEntry)skillList.get(index)).getValue()).intValue();
/* 135 */       ItemStack itemStack = new ItemStack((Item)ModItems.getProfessionToken(profType), level);
/* 136 */       this.itemRender.renderItemAndEffectIntoGUI((EntityLivingBase)this.mc.player, itemStack, x, y);
/*     */       
/* 138 */       int color = 16777215;
/* 139 */       if (this.villager.getBlessed() > 0 && profType == this.villager.getProfessionType()) {
/* 140 */         color = EnumDyeColor.LIME.getColorValue();
/*     */       }
/* 142 */       int xPosition = x + 12;
/* 143 */       int yPosition = y - 5;
/* 144 */       String s = String.valueOf(level);
/* 145 */       GlStateManager.disableLighting();
/* 146 */       GlStateManager.disableDepth();
/* 147 */       GlStateManager.disableBlend();
/* 148 */       this.fontRenderer.drawStringWithShadow(s, (xPosition + 19 - 2 - this.fontRenderer.getStringWidth(s)), (yPosition + 6 + 3), color);
/* 149 */       GlStateManager.enableLighting();
/* 150 */       GlStateManager.enableDepth();
/* 151 */       GlStateManager.enableBlend();
/*     */       
/* 153 */       if (firstDraw) {
/* 154 */         HoverChecker hoverChecker = new HoverChecker(this.guiTop + y, this.guiTop + y + 16, this.guiLeft + x, this.guiLeft + x + 16, 350);
/* 155 */         this.hoverChecks.put(hoverChecker, I18n.format("item.prof_" + profType.name + ".name", new Object[0]));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void drawStat(String name, int xTex, int yTex, int colorIndex, int y, float value, float max, boolean firstDraw) {
/* 162 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 163 */     this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
/*     */     
/* 165 */     int BAR_LEFT = 24;
/* 166 */     int BAR_WIDTH = 67;
/* 167 */     int BAR_TEX_LEFT = 182;
/* 168 */     int BAR_TEX_TOP = 53;
/* 169 */     int ICON_LEFT = 8;
/*     */ 
/*     */     
/* 172 */     drawTexturedModalRect(8, y, xTex, yTex, 11, 9);
/*     */     
/* 174 */     if (firstDraw) {
/* 175 */       HoverChecker hoverChecker = new HoverChecker(this.guiTop + y, this.guiTop + y + 9, this.guiLeft + 8, this.guiLeft + 8 + 11, 350);
/* 176 */       this.hoverChecks.put(hoverChecker, I18n.format(name, new Object[0]));
/*     */     } 
/*     */ 
/*     */     
/* 180 */     drawTexturedModalRect(24, y + 2, 182, 53 + colorIndex * 5 * 2, 67, 5);
/*     */     
/* 182 */     int i = (int)(value / max * 67.0F);
/*     */     
/* 184 */     if (i > 0)
/*     */     {
/*     */       
/* 187 */       drawTexturedModalRect(24, y + 2, 182, 53 + colorIndex * 5 * 2 + 5, i, 5);
/*     */     }
/*     */     
/* 190 */     this.fontRenderer.drawString(String.valueOf((int)value), 93, y + 1, 4210752);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\gui\GuiVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */