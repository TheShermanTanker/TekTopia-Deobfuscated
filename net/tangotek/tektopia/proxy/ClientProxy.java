/*     */ package net.tangotek.tektopia.proxy;
/*     */ 
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import com.leviathanstudio.craftstudio.client.exception.CSMalformedJsonException;
/*     */ import com.leviathanstudio.craftstudio.client.json.CSJsonReader;
/*     */ import com.leviathanstudio.craftstudio.client.json.CSReadedModel;
/*     */ import com.leviathanstudio.craftstudio.client.json.CSReadedModelBlock;
/*     */ import com.leviathanstudio.craftstudio.client.registry.CSRegistryHelper;
/*     */ import com.leviathanstudio.craftstudio.client.util.EnumRenderType;
/*     */ import com.leviathanstudio.craftstudio.client.util.EnumResourceType;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import com.leviathanstudio.craftstudio.common.animation.IAnimated;
/*     */ import com.sun.javafx.geom.Vec3f;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import javax.vecmath.Vector3f;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.block.model.ModelResourceLocation;
/*     */ import net.minecraft.client.resources.IResource;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.client.model.ModelLoader;
/*     */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/*     */ import net.tangotek.tektopia.ModEntities;
/*     */ import net.tangotek.tektopia.VillageClient;
/*     */ import net.tangotek.tektopia.client.VillageBorderRenderer;
/*     */ import net.tangotek.tektopia.pathing.PathingNodeClient;
/*     */ import net.tangotek.tektopia.pathing.PathingOverlayRenderer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClientProxy
/*     */   extends CommonProxy
/*     */ {
/*  39 */   public static final Integer PARTICLE_OFFSET = Integer.valueOf(1400);
/*  40 */   public static final Integer PARTICLE_THOUGHT = Integer.valueOf(PARTICLE_OFFSET.intValue() + 1);
/*     */   
/*  42 */   private PathingOverlayRenderer overlayRenderer = new PathingOverlayRenderer();
/*  43 */   private VillageBorderRenderer villageBorderRenderer = new VillageBorderRenderer();
/*     */ 
/*     */   
/*  46 */   private VillageClient villageClient = null;
/*     */ 
/*     */   
/*     */   public void preInit(FMLPreInitializationEvent e) {
/*  50 */     super.preInit(e);
/*     */     
/*  52 */     ModEntities.initModels();
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(FMLInitializationEvent e) {
/*  57 */     super.init(e);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postInit(FMLPostInitializationEvent e) {
/*  63 */     super.postInit(e);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends IAnimated> AnimationHandler<T> getNewAnimationHandler(Class<T> animatedClass) {
/*  69 */     return (AnimationHandler)new TekClientAnimationHandler<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerItemRenderer(Item item, int meta, String id) {
/*  74 */     ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation("tektopia:" + id, "inventory"));
/*     */   }
/*     */   
/*  77 */   CSRegistryHelper registry = new CSRegistryHelper("tektopia");
/*     */ 
/*     */   
/*     */   public void registerModels() {
/*  81 */     super.registerModels();
/*  82 */     CSRegistryHelper registry = new CSRegistryHelper("tektopia");
/*  83 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "lumberjack_m");
/*  84 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "farmer_m");
/*  85 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "farmer_f");
/*  86 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "miner_m");
/*  87 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "blacksmith_m");
/*  88 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "guard_m");
/*  89 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "merchant_m");
/*  90 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "rancher_m");
/*  91 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "butcher_m");
/*  92 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "architect_m");
/*  93 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "tradesman_m");
/*  94 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "enchanter_m");
/*  95 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "child_m");
/*  96 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "child_f");
/*  97 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "chef_m");
/*  98 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "chef_f");
/*  99 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "teacher_m");
/* 100 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "nitwit_m");
/* 101 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "druid_m");
/* 102 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "druid_f");
/* 103 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "cleric_m");
/* 104 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "cleric_f");
/* 105 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "nomad_m");
/* 106 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "bard_m");
/*     */     
/* 108 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "necromancer");
/* 109 */     registry.register(EnumResourceType.MODEL, EnumRenderType.ENTITY, "rancher_hat");
/*     */     
/* 111 */     registerBlock("chair");
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     return super.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleNodeUpdate(PathingNodeClient node) {
/* 121 */     this.overlayRenderer.handleNodeUpdate(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleVillage(VillageClient vc) {
/* 126 */     this.villageBorderRenderer.updateVillage(vc);
/*     */   }
/*     */ 
/*     */   
/*     */   private void registerBlock(String resourceNameIn) {
/* 131 */     this.registry.register(EnumResourceType.MODEL, EnumRenderType.BLOCK, resourceNameIn);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     ResourceLocation res = new ResourceLocation("tektopia", "models/block/" + resourceNameIn + ".json");
/*     */     try {
/* 138 */       IResource iResource = Minecraft.getMinecraft().getResourceManager().getResource(res);
/*     */     }
/* 140 */     catch (IOException var5) {
/* 141 */       convertBlock(resourceNameIn);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void convertBlock(String resourceNameIn) {
/*     */     try {
/* 147 */       ResourceLocation resOld = new ResourceLocation("tektopia", EnumResourceType.MODEL.getPath() + EnumRenderType.BLOCK.getFolderName() + resourceNameIn + EnumResourceType.MODEL.getExtension());
/* 148 */       CSJsonReader jsonReader = new CSJsonReader(resOld);
/* 149 */       CSReadedModel model = jsonReader.readModel();
/*     */       
/* 151 */       File outFile = new File((Minecraft.getMinecraft()).gameDir, "../src/main/resources/assets/tektopia/models/block/" + resourceNameIn + ".json");
/*     */       
/* 153 */       if (!outFile.exists()) {
/* 154 */         JsonWriter writer = new JsonWriter(new FileWriter(outFile));
/*     */         
/* 156 */         writer.setIndent("    ");
/* 157 */         writer.beginObject();
/*     */ 
/*     */         
/* 160 */         String textureName = resourceNameIn + "_tex";
/* 161 */         writer.name("textures").beginObject();
/* 162 */         writer.name("particle").value("tektopia:blocks/" + resourceNameIn);
/* 163 */         writer.name(textureName).value("tektopia:blocks/" + resourceNameIn);
/* 164 */         writer.endObject();
/*     */ 
/*     */ 
/*     */         
/* 168 */         writer.name("elements").beginArray();
/* 169 */         for (CSReadedModelBlock block : model.getParents()) {
/* 170 */           writeBlockElement(writer, block, textureName, new Vector3f(8.0F, 24.0F, 8.0F));
/*     */         }
/* 172 */         writer.endArray();
/*     */         
/* 174 */         writer.endObject();
/* 175 */         writer.close();
/*     */       }
/*     */     
/* 178 */     } catch (CSMalformedJsonException|IOException ex) {
/* 179 */       ex.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeBlockElement(JsonWriter writer, CSReadedModelBlock block, String textureName, Vector3f offset) throws IOException {
/* 185 */     writer.beginObject();
/* 186 */     writer.name("name").value(block.getName());
/*     */ 
/*     */     
/* 189 */     offset.x += (block.getRotationPoint()).x;
/* 190 */     offset.y -= (block.getRotationPoint()).y;
/* 191 */     offset.z -= (block.getRotationPoint()).z;
/*     */ 
/*     */     
/* 194 */     Vector3f size = block.getSize();
/* 195 */     size.y = -size.y;
/* 196 */     size.z = -size.z;
/*     */     
/* 198 */     writer.name("from").beginArray();
/* 199 */     Vec3f from = new Vec3f(offset.x - size.x / 2.0F, offset.y - size.y / 2.0F, offset.z - size.z / 2.0F);
/* 200 */     writer.value(from.x).value(from.y).value(from.z);
/* 201 */     writer.endArray();
/*     */     
/* 203 */     writer.name("to").beginArray();
/* 204 */     writer.value((from.x + size.x)).value((from.y + size.y)).value((from.z + size.z));
/* 205 */     writer.endArray();
/*     */ 
/*     */     
/* 208 */     writer.name("faces").beginObject();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 216 */     writeFace(writer, "up", 0.0F, 0.0F, size.x, size.z, textureName);
/* 217 */     writeFace(writer, "down", 0.0F, 0.0F, size.x, size.z, textureName);
/* 218 */     writeFace(writer, "west", 0.0F, 0.0F, size.z, size.y, textureName);
/* 219 */     writeFace(writer, "south", 0.0F, 0.0F, size.x, size.y, textureName);
/* 220 */     writeFace(writer, "east", 0.0F, 0.0F, size.z, size.y, textureName);
/* 221 */     writeFace(writer, "north", 0.0F, 0.0F, size.x, size.y, textureName);
/* 222 */     writer.endObject();
/*     */     
/* 224 */     writer.endObject();
/*     */     
/* 226 */     for (CSReadedModelBlock child : block.getChilds()) {
/* 227 */       writeBlockElement(writer, child, textureName, offset);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeFace(JsonWriter writer, String name, float u1, float v1, float w, float h, String textureName) throws IOException {
/* 233 */     writer.name(name).beginObject();
/* 234 */     writer.name("uv").beginArray();
/* 235 */     writer.value(u1).value(v1).value((u1 + w)).value((v1 + h));
/* 236 */     writer.endArray();
/*     */     
/* 238 */     writer.name("texture").value("#" + textureName);
/* 239 */     writer.endObject();
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerAnims() {
/* 244 */     super.registerAnims();
/* 245 */     CSRegistryHelper registry = new CSRegistryHelper("tektopia");
/* 246 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_walk");
/* 247 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_chop");
/* 248 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_thor_jump");
/* 249 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_hoe");
/* 250 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_pickup");
/* 251 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_eat");
/* 252 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_hammer");
/* 253 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_sleep");
/* 254 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_sit");
/* 255 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_sit_cheer");
/* 256 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_take");
/* 257 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_summon");
/* 258 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_salute");
/* 259 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_skip");
/* 260 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_run");
/* 261 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_cook");
/* 262 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_teach");
/* 263 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_sit_raise");
/* 264 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_cast_forward");
/* 265 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_flute_1");
/* 266 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_walk_sad");
/* 267 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_read");
/* 268 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_cast_grow");
/* 269 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_cast_bless");
/* 270 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "villager_craft");
/*     */     
/* 272 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "necro_summon");
/* 273 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "necro_walk");
/* 274 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "necro_idle");
/* 275 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "necro_siphon");
/* 276 */     registry.register(EnumResourceType.ANIM, EnumRenderType.ENTITY, "necro_cast_forward");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\proxy\ClientProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */