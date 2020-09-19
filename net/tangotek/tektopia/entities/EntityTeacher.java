/*     */ package net.tangotek.tektopia.entities;
/*     */ 
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.datasync.DataParameter;
/*     */ import net.minecraft.network.datasync.DataSerializers;
/*     */ import net.minecraft.network.datasync.EntityDataManager;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAICraftItems;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAISchoolTeach;
/*     */ import net.tangotek.tektopia.entities.crafting.Recipe;
/*     */ import net.tangotek.tektopia.storage.ItemDesire;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ public class EntityTeacher
/*     */   extends EntityVillagerTek {
/*  28 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityTeacher.class);
/*  29 */   private static final DataParameter<Boolean> TEACH_SCHOOL = EntityDataManager.createKey(EntityTeacher.class, DataSerializers.BOOLEAN);
/*  30 */   private static List<Recipe> craftSet = buildCraftSet();
/*     */   
/*     */   static {
/*  33 */     animHandler.addAnim("tektopia", "villager_teach", "teacher_m", false);
/*  34 */     animHandler.addAnim("tektopia", "villager_craft", "teacher_m", false);
/*  35 */     EntityVillagerTek.setupAnimations(animHandler, "teacher_m");
/*     */   }
/*     */ 
/*     */   
/*  39 */   private static final Map<String, DataParameter<Boolean>> RECIPE_PARAMS = new HashMap<>();
/*     */   static {
/*  41 */     craftSet.forEach(r -> (DataParameter)RECIPE_PARAMS.put(r.getAiFilter(), EntityDataManager.createKey(EntityTeacher.class, DataSerializers.BOOLEAN)));
/*     */   }
/*     */   
/*     */   public EntityTeacher(World worldIn) {
/*  45 */     super(worldIn, ProfessionType.TEACHER, VillagerRole.VILLAGER.value);
/*     */   }
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/*  49 */     return animHandler;
/*     */   }
/*     */   
/*     */   protected void entityInit() {
/*  53 */     super.entityInit();
/*  54 */     craftSet.forEach(r -> registerAIFilter(r.getAiFilter(), RECIPE_PARAMS.get(r.getAiFilter())));
/*  55 */     registerAIFilter("teach_school", TEACH_SCHOOL);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initEntityAI() {
/*  60 */     super.initEntityAI();
/*     */     
/*  62 */     this; craftSet = buildCraftSet();
/*     */     
/*  64 */     getDesireSet().addItemDesire(new ItemDesire("Book", p -> (p.getItem() == Items.BOOK && !p.isItemEnchanted()), 0, 6, 9, p -> { this; return (!isSchoolTime(this.world) && p.isAIFilterEnabled("teach_school"));
/*  65 */           })); craftSet.forEach(r -> getDesireSet().addRecipeDesire(r));
/*     */     
/*  67 */     this.tasks.addTask(50, (EntityAIBase)new EntityAISchoolTeach(this));
/*  68 */     this; this.tasks.addTask(50, (EntityAIBase)new EntityAICraftItems(this, craftSet, "villager_craft", null, 60, VillageStructureType.SCHOOL, Blocks.CRAFTING_TABLE, p -> {
/*     */             if (p.isWorkTime()) {
/*     */               this; if (!isSchoolTime(this.world));
/*     */             } 
/*     */             return false;
/*  73 */           })); } protected void randomizeGoals() { super.randomizeGoals();
/*  74 */     this.wantsLearning = getRNG().nextInt(6) + 3; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLearningTime() {
/*  79 */     if (this.wantsLearning > 0 && !isSchoolTime(this.world) && isWorkTime()) {
/*  80 */       return isAIFilterEnabled("read_book");
/*     */     }
/*     */     
/*  83 */     return false;
/*     */   }
/*     */   
/*     */   private static List<Recipe> buildCraftSet() {
/*  87 */     List<Recipe> recipes = new ArrayList<>();
/*     */ 
/*     */     
/*  90 */     List<ItemStack> ingredients = new ArrayList<>();
/*  91 */     ingredients.add(new ItemStack(Items.PAPER, 2));
/*  92 */     ingredients.add(new ItemStack(Items.FEATHER, 1));
/*  93 */     Recipe recipe = new Recipe(ProfessionType.TEACHER, "craft_name_tag", 10, new ItemStack(Items.NAME_TAG, 1), ingredients, 3, 5, v -> Integer.valueOf(v.getSkillLerp(ProfessionType.TEACHER, 4, 1)), 3);
/*  94 */     recipes.add(recipe);
/*     */ 
/*     */     
/*  97 */     return recipes;
/*     */   }
/*     */   
/*     */   protected boolean canVillagerPickupItem(ItemStack itemIn) {
/* 101 */     return (itemIn.getItem() == Items.BOOK && !itemIn.isItemEnchanted());
/*     */   }
/*     */   public static boolean isSchoolTime(World world) {
/* 104 */     return Village.isTimeOfDay(world, 1000L, 8500L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityTeacher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */