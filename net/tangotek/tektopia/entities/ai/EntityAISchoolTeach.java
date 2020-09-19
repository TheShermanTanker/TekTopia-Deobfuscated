/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.ItemTagType;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityChild;
/*     */ import net.tangotek.tektopia.entities.EntityTeacher;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureSchool;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ public class EntityAISchoolTeach
/*     */   extends EntityAIMoveToBlock {
/*  21 */   private VillageStructureSchool school = null;
/*  22 */   private int idleTime = 0;
/*     */   private boolean gestured = false;
/*  24 */   private EntityChild watchStudent = null;
/*     */   
/*     */   protected final EntityTeacher teacher;
/*     */   
/*     */   public EntityAISchoolTeach(EntityTeacher v) {
/*  29 */     super((EntityVillageNavigator)v);
/*  30 */     this.teacher = v;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  36 */     if (this.teacher.isAITick("teach_school") && this.teacher.hasVillage()) if (EntityTeacher.isSchoolTime(this.teacher.world)) {
/*  37 */         return super.shouldExecute();
/*     */       } 
/*  39 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  45 */     if (!this.teacher.hasVillage() || !this.teacher.getVillage().isStructureValid((VillageStructure)this.school)) {
/*  46 */       return false;
/*     */     }
/*  48 */     if (!EntityTeacher.isSchoolTime(this.teacher.world)) {
/*  49 */       return false;
/*     */     }
/*  51 */     if (this.idleTime > 0) {
/*  52 */       return true;
/*     */     }
/*  54 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/*  59 */     this.teacher.setMovementMode(this.teacher.getDefaultMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  64 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/*  69 */     return this.destinationPos;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNearWalkPos() {
/*  74 */     return super.isNearWalkPos();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  79 */     VillageStructure struct = this.teacher.getVillage().getNearestStructure(VillageStructureType.SCHOOL, this.teacher.getPosition());
/*  80 */     if (struct != null) {
/*  81 */       this.school = (VillageStructureSchool)struct;
/*  82 */       BlockPos pos = this.school.getRandomFloorTile();
/*  83 */       if (pos != null) {
/*  84 */         return pos;
/*     */       }
/*     */     } 
/*     */     
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  93 */     super.updateTask();
/*     */     
/*  95 */     if (hasArrived() && this.watchStudent != null && this.watchStudent.isEntityAlive()) {
/*  96 */       this.teacher.faceEntity((Entity)this.watchStudent, 60.0F, 40.0F);
/*     */     }
/*     */     
/*  99 */     if (this.idleTime > 0) {
/* 100 */       this.idleTime--;
/*     */     }
/* 102 */     if (this.idleTime > 240 && !this.gestured && this.teacher.getRNG().nextInt(150) == 0) {
/* 103 */       this.gestured = true;
/*     */       
/* 105 */       int studentsInside = this.school.getEntitiesInside(EntityChild.class).size();
/* 106 */       if (studentsInside > 2) {
/* 107 */         List<ItemStack> bookItems = this.teacher.getInventory().removeItems(p -> (p.getItem() == Items.BOOK && !p.isItemEnchanted()), 1);
/*     */         
/* 109 */         if (!bookItems.isEmpty()) {
/* 110 */           int maxGain = 2;
/* 111 */           if (ModItems.isTaggedItem(bookItems.get(0), ItemTagType.VILLAGER)) {
/* 112 */             maxGain = 1;
/*     */           }
/* 114 */           int maxIntGain = maxGain;
/* 115 */           this.teacher.playServerAnimation("villager_teach");
/*     */           
/* 117 */           this.teacher.addJob(new TickJob(8, 0, false, () -> this.teacher.equipActionItem(new ItemStack(Items.BOOK))));
/* 118 */           this.teacher.addJob(new TickJob(50, 0, false, () -> teachAll(maxIntGain)));
/* 119 */           this.teacher.addJob(new TickJob(90, 0, false, () -> this.teacher.unequipActionItem()));
/* 120 */           this.teacher.addJob(new TickJob(100, 0, false, () -> this.teacher.stopServerAnimation("villager_teach")));
/*     */         } 
/*     */       } 
/*     */       
/* 124 */       if (studentsInside > 0) {
/* 125 */         this.teacher.tryAddSkill(ProfessionType.TEACHER, 4);
/*     */       } else {
/* 127 */         this.teacher.throttledSadness(-2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void teachAll(int max) {
/* 132 */     List<EntityChild> students = this.school.getEntitiesInside(EntityChild.class);
/* 133 */     for (EntityChild child : students) {
/* 134 */       int intGain = this.teacher.getRNG().nextInt(this.teacher.getSkillLerp(ProfessionType.TEACHER, 1, max));
/* 135 */       child.addIntelligence(intGain);
/*     */     } 
/* 137 */     this.teacher.tryAddSkill(ProfessionType.TEACHER, 2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 142 */     this.idleTime = 200 + this.teacher.getRNG().nextInt(200);
/*     */     
/* 144 */     this.teacher.modifyHunger(-1);
/*     */     
/* 146 */     List<EntityChild> students = this.school.getEntitiesInside(EntityChild.class);
/* 147 */     if (!students.isEmpty()) {
/* 148 */       this.watchStudent = students.get(this.teacher.getRNG().nextInt(students.size()));
/*     */     }
/*     */     
/* 151 */     super.onArrival();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 156 */     this.idleTime = 0;
/* 157 */     this.watchStudent = null;
/* 158 */     this.gestured = false;
/* 159 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAISchoolTeach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */