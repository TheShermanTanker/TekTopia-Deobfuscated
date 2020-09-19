/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.google.common.base.Optional;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Enchantments;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.DataSerializers;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.SoundCategory;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAICraftItems;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIEnchant;
/*     */ import net.tangotek.tektopia.entities.crafting.Recipe;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityEnchanter extends EntityVillagerTek {
/*  32 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityEnchanter.class);
/*  33 */   protected static final DataParameter<Optional<BlockPos>> SPELL_BLOCK = EntityDataManager.createKey(EntityEnchanter.class, DataSerializers.OPTIONAL_BLOCK_POS);
/*     */   
/*  35 */   private static List<Recipe> enchantSet = buildEnchantSet();
/*  36 */   private static List<Recipe> craftSet = buildCraftSet();
/*     */   
/*  38 */   private static final Map<String, DataParameter<Boolean>> RECIPE_PARAMS = new HashMap<>();
/*     */   static {
/*  40 */     enchantSet.forEach(r -> (DataParameter)RECIPE_PARAMS.put(r.getAiFilter(), EntityDataManager.createKey(EntityEnchanter.class, DataSerializers.BOOLEAN)));
/*  41 */     craftSet.forEach(r -> (DataParameter)RECIPE_PARAMS.put(r.getAiFilter(), EntityDataManager.createKey(EntityEnchanter.class, DataSerializers.BOOLEAN)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  49 */     animHandler.addAnim("tektopia", "villager_summon", "enchanter_m", true);
/*  50 */     animHandler.addAnim("tektopia", "villager_craft", "enchanter_m", true);
/*  51 */     EntityVillagerTek.setupAnimations(animHandler, "enchanter_m");
/*     */   }
/*     */   private int blockParticleTick = 0; private int handParticleTick = 0; private BlockPos clientSpellBlock;
/*     */   public EntityEnchanter(World worldIn) {
/*  55 */     super(worldIn, ProfessionType.ENCHANTER, VillagerRole.VILLAGER.value);
/*     */     
/*  57 */     Runnable enchantSound = () -> this.world.playSound(this.posX, this.posY, this.posZ, ModSoundEvents.villagerEnchant, SoundCategory.NEUTRAL, 1.5F, this.rand.nextFloat() * 0.2F + 0.9F, false);
/*  58 */     Runnable enchantApplySound = () -> this.world.playSound(this.posX, this.posY, this.posZ, ModSoundEvents.villagerEnchantApply, SoundCategory.NEUTRAL, 1.5F, this.rand.nextFloat() * 0.2F + 0.9F, false);
/*     */     
/*  60 */     if (this.world.isRemote) {
/*  61 */       addAnimationTrigger("tektopia:villager_summon", 12, enchantSound);
/*  62 */       addAnimationTrigger("tektopia:villager_summon", 20, () -> this.handParticleTick = 25);
/*  63 */       addAnimationTrigger("tektopia:villager_summon", 90, enchantSound);
/*  64 */       addAnimationTrigger("tektopia:villager_summon", 130, enchantApplySound);
/*     */     } 
/*     */   }
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/*  69 */     return animHandler;
/*     */   }
/*     */   
/*     */   protected void initEntityAI() {
/*  73 */     super.initEntityAI();
/*     */     
/*  75 */     Predicate<ItemStack> isLapis = p -> (p.getItem() == Items.DYE && p.getMetadata() == EnumDyeColor.BLUE.getDyeDamage());
/*  76 */     getDesireSet().addItemDesire(new ItemDesire("Lapis", isLapis, 1, 10, 20, null));
/*     */     
/*  78 */     craftSet.forEach(r -> getDesireSet().addRecipeDesire(r));
/*  79 */     enchantSet.forEach(r -> getDesireSet().addRecipeDesire(r));
/*     */     
/*  81 */     this; this.tasks.addTask(50, (EntityAIBase)new EntityAICraftItems(this, craftSet, "villager_craft", null, 80, VillageStructureType.LIBRARY, Blocks.CRAFTING_TABLE, p -> p.isWorkTime()));
/*  82 */     this; this.tasks.addTask(50, (EntityAIBase)new EntityAIEnchant(this, enchantSet, "villager_summon", new ItemStack(Items.AIR), 80, VillageStructureType.LIBRARY, Blocks.ENCHANTING_TABLE, p -> p.isWorkTime(), 1));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  87 */     super.entityInit();
/*  88 */     this.dataManager.register(SPELL_BLOCK, Optional.absent());
/*     */     
/*  90 */     craftSet.forEach(r -> registerAIFilter(r.getAiFilter(), RECIPE_PARAMS.get(r.getAiFilter())));
/*  91 */     enchantSet.forEach(r -> registerAIFilter(r.getAiFilter(), RECIPE_PARAMS.get(r.getAiFilter())));
/*     */   }
/*     */   
/*     */   private static List<Recipe> buildEnchantSet() {
/*  95 */     List<Recipe> recipes = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/*  99 */     List<ItemStack> ingredients = new ArrayList<>();
/* 100 */     ingredients.add(new ItemStack(Items.IRON_SWORD, 1));
/* 101 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 103 */     ItemStack enchanted = new ItemStack(Items.IRON_SWORD, 1);
/* 104 */     enchanted.addEnchantment(Enchantments.SHARPNESS, 1);
/* 105 */     Recipe recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_iron_sword", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 12)), 3);
/* 106 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 109 */     ingredients = new ArrayList<>();
/* 110 */     ingredients.add(new ItemStack(Items.IRON_AXE, 1));
/* 111 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 113 */     enchanted = new ItemStack(Items.IRON_AXE, 1);
/* 114 */     enchanted.addEnchantment(Enchantments.EFFICIENCY, 1);
/* 115 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_iron_axe", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 116 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 119 */     ingredients = new ArrayList<>();
/* 120 */     ingredients.add(new ItemStack(Items.IRON_PICKAXE, 1));
/* 121 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 123 */     enchanted = new ItemStack(Items.IRON_PICKAXE, 1);
/* 124 */     enchanted.addEnchantment(Enchantments.EFFICIENCY, 1);
/* 125 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_iron_pickaxe", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 126 */     recipes.add(recipe);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     ingredients = new ArrayList<>();
/* 132 */     ingredients.add(new ItemStack((Item)Items.IRON_CHESTPLATE, 1));
/* 133 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 135 */     enchanted = new ItemStack((Item)Items.IRON_CHESTPLATE, 1);
/* 136 */     enchanted.addEnchantment(Enchantments.PROTECTION, 1);
/* 137 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_iron_chestplate", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 138 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 141 */     ingredients = new ArrayList<>();
/* 142 */     ingredients.add(new ItemStack((Item)Items.IRON_HELMET, 1));
/* 143 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 145 */     enchanted = new ItemStack((Item)Items.IRON_HELMET, 1);
/* 146 */     enchanted.addEnchantment(Enchantments.PROTECTION, 1);
/* 147 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_iron_helmet", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 148 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 151 */     ingredients = new ArrayList<>();
/* 152 */     ingredients.add(new ItemStack((Item)Items.IRON_BOOTS, 1));
/* 153 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 155 */     enchanted = new ItemStack((Item)Items.IRON_BOOTS, 1);
/* 156 */     enchanted.addEnchantment(Enchantments.PROTECTION, 1);
/* 157 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_iron_boots", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 158 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 161 */     ingredients = new ArrayList<>();
/* 162 */     ingredients.add(new ItemStack((Item)Items.IRON_LEGGINGS, 1));
/* 163 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 165 */     enchanted = new ItemStack((Item)Items.IRON_LEGGINGS, 1);
/* 166 */     enchanted.addEnchantment(Enchantments.PROTECTION, 1);
/* 167 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_iron_leggings", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 168 */     recipes.add(recipe);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     ingredients = new ArrayList<>();
/* 174 */     ingredients.add(new ItemStack((Item)Items.LEATHER_CHESTPLATE, 1));
/* 175 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 177 */     enchanted = new ItemStack((Item)Items.LEATHER_CHESTPLATE, 1);
/* 178 */     enchanted.addEnchantment(Enchantments.PROTECTION, 1);
/* 179 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_leather_chestplate", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 180 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 183 */     ingredients = new ArrayList<>();
/* 184 */     ingredients.add(new ItemStack((Item)Items.LEATHER_HELMET, 1));
/* 185 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 187 */     enchanted = new ItemStack((Item)Items.LEATHER_HELMET, 1);
/* 188 */     enchanted.addEnchantment(Enchantments.PROTECTION, 1);
/* 189 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_leather_helmet", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 190 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 193 */     ingredients = new ArrayList<>();
/* 194 */     ingredients.add(new ItemStack((Item)Items.LEATHER_BOOTS, 1));
/* 195 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 197 */     enchanted = new ItemStack((Item)Items.LEATHER_BOOTS, 1);
/* 198 */     enchanted.addEnchantment(Enchantments.PROTECTION, 1);
/* 199 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_leather_boots", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 200 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 203 */     ingredients = new ArrayList<>();
/* 204 */     ingredients.add(new ItemStack((Item)Items.LEATHER_LEGGINGS, 1));
/* 205 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 207 */     enchanted = new ItemStack((Item)Items.LEATHER_LEGGINGS, 1);
/* 208 */     enchanted.addEnchantment(Enchantments.PROTECTION, 1);
/* 209 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_leather_leggings", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 210 */     recipes.add(recipe);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     ingredients = new ArrayList<>();
/* 216 */     ingredients.add(new ItemStack(Items.DIAMOND_SWORD, 1));
/* 217 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 219 */     enchanted = new ItemStack(Items.DIAMOND_SWORD, 1);
/* 220 */     enchanted.addEnchantment(Enchantments.SHARPNESS, 1);
/* 221 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_diamond_sword", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 222 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 225 */     ingredients = new ArrayList<>();
/* 226 */     ingredients.add(new ItemStack(Items.DIAMOND_AXE, 1));
/* 227 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 229 */     enchanted = new ItemStack(Items.DIAMOND_AXE, 1);
/* 230 */     enchanted.addEnchantment(Enchantments.EFFICIENCY, 1);
/* 231 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_diamond_axe", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 232 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 235 */     ingredients = new ArrayList<>();
/* 236 */     ingredients.add(new ItemStack(Items.DIAMOND_PICKAXE, 1));
/* 237 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 239 */     enchanted = new ItemStack(Items.DIAMOND_PICKAXE, 1);
/* 240 */     enchanted.addEnchantment(Enchantments.EFFICIENCY, 1);
/* 241 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_diamond_pickaxe", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 242 */     recipes.add(recipe);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     ingredients = new ArrayList<>();
/* 248 */     ingredients.add(new ItemStack((Item)Items.DIAMOND_CHESTPLATE, 1));
/* 249 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 251 */     enchanted = new ItemStack((Item)Items.DIAMOND_CHESTPLATE, 1);
/* 252 */     enchanted.addEnchantment(Enchantments.PROTECTION, 1);
/* 253 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_diamond_chestplate", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 254 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 257 */     ingredients = new ArrayList<>();
/* 258 */     ingredients.add(new ItemStack((Item)Items.DIAMOND_HELMET, 1));
/* 259 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 261 */     enchanted = new ItemStack((Item)Items.DIAMOND_HELMET, 1);
/* 262 */     enchanted.addEnchantment(Enchantments.PROTECTION, 1);
/* 263 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_diamond_helmet", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 264 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 267 */     ingredients = new ArrayList<>();
/* 268 */     ingredients.add(new ItemStack((Item)Items.DIAMOND_BOOTS, 1));
/* 269 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 271 */     enchanted = new ItemStack((Item)Items.DIAMOND_BOOTS, 1);
/* 272 */     enchanted.addEnchantment(Enchantments.PROTECTION, 1);
/* 273 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_diamond_boots", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 274 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 277 */     ingredients = new ArrayList<>();
/* 278 */     ingredients.add(new ItemStack((Item)Items.DIAMOND_LEGGINGS, 1));
/* 279 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */     
/* 281 */     enchanted = new ItemStack((Item)Items.DIAMOND_LEGGINGS, 1);
/* 282 */     enchanted.addEnchantment(Enchantments.PROTECTION, 1);
/* 283 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_diamond_leggings", 1, enchanted, ingredients, 1, 1, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 3);
/* 284 */     recipes.add(recipe);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 289 */     ingredients = new ArrayList<>();
/* 290 */     ingredients.add(new ItemStack(Items.BOOK, 1));
/* 291 */     ingredients.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
/*     */ 
/*     */     
/* 294 */     enchanted = new ItemStack(Items.ENCHANTED_BOOK, 1);
/* 295 */     enchanted.addEnchantment(Enchantments.MENDING, 1);
/* 296 */     recipe = new Recipe(ProfessionType.ENCHANTER, "enchant_book", 1, enchanted, ingredients, 2, 3, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.ENCHANTER, 4, 15)), 10);
/* 297 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 300 */     return recipes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<Recipe> buildCraftSet() {
/* 311 */     List<Recipe> recipes = new ArrayList<>();
/*     */ 
/*     */     
/* 314 */     List<ItemStack> ingredients = new ArrayList<>();
/* 315 */     ingredients.add(new ItemStack(Items.REEDS, 3));
/* 316 */     Recipe recipe = new Recipe(ProfessionType.ENCHANTER, "craft_paper", 8, new ItemStack(Items.PAPER, 3), ingredients, 2, 6, v -> Integer.valueOf(2), 128);
/* 317 */     recipes.add(recipe);
/*     */ 
/*     */     
/* 320 */     ingredients = new ArrayList<>();
/* 321 */     ingredients.add(new ItemStack(Items.PAPER, 3));
/* 322 */     ingredients.add(new ItemStack(Items.LEATHER, 1));
/* 323 */     recipe = new Recipe(ProfessionType.ENCHANTER, "craft_book", 3, new ItemStack(Items.BOOK, 1), ingredients, 3, 5, v -> Integer.valueOf(2), 32);
/* 324 */     recipes.add(recipe);
/*     */     
/* 326 */     return recipes;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSpellcasting() {
/* 331 */     BlockPos blockPos = getSpellBlock();
/* 332 */     return (blockPos != null);
/*     */   }
/*     */   
/*     */   public BlockPos getSpellBlock() {
/* 336 */     return (BlockPos)((Optional)this.dataManager.get(SPELL_BLOCK)).orNull();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCasting(BlockPos castingPos) {
/* 341 */     if (castingPos == null) {
/* 342 */       this.dataManager.set(SPELL_BLOCK, Optional.absent());
/*     */     } else {
/* 344 */       this.dataManager.set(SPELL_BLOCK, Optional.of(castingPos));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onUpdate() {
/* 349 */     super.onUpdate();
/*     */     
/* 351 */     if (this.world.isRemote) {
/*     */       
/* 353 */       int skill = getSkill(ProfessionType.ENCHANTER);
/* 354 */       if (isSpellcasting()) {
/* 355 */         this.clientSpellBlock = getSpellBlock();
/* 356 */         this.blockParticleTick = 10;
/* 357 */         if (this.handParticleTick > 0) {
/* 358 */           double d0 = 0.7D;
/* 359 */           double d1 = 0.7D;
/* 360 */           double d2 = 0.7D;
/* 361 */           float f = this.renderYawOffset * 0.017453292F + MathHelper.cos(this.ticksExisted * 0.6662F) * 0.25F;
/* 362 */           float f1 = MathHelper.cos(f);
/* 363 */           float f2 = MathHelper.sin(f);
/*     */ 
/*     */           
/* 366 */           double particles = 1.0D;
/* 367 */           while (particles > 1.0D) {
/* 368 */             this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + f1 * 0.6D, this.posY + 2.0D, this.posZ + f2 * 0.6D, d0, d1, d2, new int[] { 0, 1, 1 });
/* 369 */             this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX - f1 * 0.6D, this.posY + 2.0D, this.posZ - f2 * 0.6D, d0, d1, d2, new int[] { 0, 1, 1 });
/* 370 */             particles--;
/*     */           } 
/* 372 */           if (getRNG().nextDouble() < particles) {
/* 373 */             this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + f1 * 0.6D, this.posY + 2.0D, this.posZ + f2 * 0.6D, d0, d1, d2, new int[] { 0, 1, 1 });
/* 374 */             this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX - f1 * 0.6D, this.posY + 2.0D, this.posZ - f2 * 0.6D, d0, d1, d2, new int[] { 0, 1, 1 });
/*     */           } 
/* 376 */           this.handParticleTick--;
/*     */         } 
/*     */       } 
/*     */       
/* 380 */       this.blockParticleTick--;
/* 381 */       if (this.blockParticleTick > 0)
/* 382 */         this.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.clientSpellBlock
/* 383 */             .getX() + 0.5D, this.clientSpellBlock
/* 384 */             .getY() + 3.1D, this.clientSpellBlock
/* 385 */             .getZ() + 0.5D, -1.2D + this.world.rand
/* 386 */             .nextFloat() * 2.4D, (this.rand.nextFloat() - 1.0F), -1.2D + this.world.rand.nextFloat() * 2.4D, new int[0]); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityEnchanter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */