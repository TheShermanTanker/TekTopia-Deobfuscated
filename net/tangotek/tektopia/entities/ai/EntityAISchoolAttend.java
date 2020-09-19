/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.block.BlockHorizontal;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.entities.EntityTeacher;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureSchool;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ public class EntityAISchoolAttend extends EntityAIMoveToBlock {
/*     */   private BlockPos chairPos;
/*  22 */   private int learnTime = 0; private VillageStructureSchool school;
/*     */   protected final EntityVillagerTek villager;
/*     */   private final Predicate<EntityVillagerTek> shouldPred;
/*     */   
/*     */   public EntityAISchoolAttend(EntityVillagerTek v, Predicate<EntityVillagerTek> shouldPred) {
/*  27 */     super((EntityVillageNavigator)v);
/*  28 */     this.villager = v;
/*  29 */     this.shouldPred = shouldPred;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  34 */     if (this.villager.isAITick("attend_school") && this.villager.hasVillage() && this.shouldPred.test(this.villager) && this.villager.isWorkTime() && 
/*  35 */       super.shouldExecute() && this.school != null) {
/*  36 */       if (this.school.hasTeacherInside()) {
/*  37 */         return true;
/*     */       }
/*  39 */       this.villager.setThought(EntityVillagerTek.VillagerThought.TEACHER);
/*     */     } 
/*     */ 
/*     */     
/*  43 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  49 */     if (hasArrived() && 
/*  50 */       this.chairPos != null && this.school.isValid() && this.school.hasTeacherInside() && EntityTeacher.isSchoolTime(this.villager.world)) {
/*  51 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  55 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/*  60 */     if (this.villager.getRNG().nextInt(5) == 0) {
/*  61 */       this.villager.setMovementMode(EntityVillagerTek.MovementMode.SULK);
/*  62 */       this.villager.modifyHappy(-10);
/*     */     } else {
/*     */       
/*  65 */       this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  71 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/*  76 */     return this.destinationPos;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNearWalkPos() {
/*  81 */     if (this.chairPos != null) {
/*  82 */       return (this.chairPos != null && this.chairPos.distanceSq((Vec3i)this.villager.getPosition()) <= 1.0D);
/*     */     }
/*  84 */     return super.isNearWalkPos();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  89 */     VillageStructure struct = this.villager.getVillage().getNearestStructure(VillageStructureType.SCHOOL, this.villager.getPosition());
/*  90 */     if (struct != null) {
/*  91 */       this.school = (VillageStructureSchool)struct;
/*  92 */       if (this.school.hasTeacherInside()) {
/*     */         
/*  94 */         this.chairPos = this.school.tryVillagerSit(this.villager);
/*  95 */         if (this.chairPos != null) {
/*  96 */           return this.chairPos;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/* 107 */     if (this.learnTime > 0) {
/* 108 */       this.learnTime--;
/* 109 */       if (this.learnTime % 10 == 0) {
/* 110 */         moveToSitPos();
/*     */       }
/*     */       
/* 113 */       if (this.learnTime == 90) {
/* 114 */         EntityTeacher teacher = this.school.getTeacherInside();
/* 115 */         if (teacher != null) {
/*     */ 
/*     */           
/* 118 */           int schoolEvent = this.villager.getRNG().nextInt(teacher.getSkillLerp(ProfessionType.TEACHER, 18, 9));
/* 119 */           if (schoolEvent <= 2) {
/* 120 */             this.villager.addIntelligence(1);
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 125 */             if (schoolEvent <= 7) {
/* 126 */               this.villager.playServerAnimation("villager_sit_raise");
/* 127 */               this.villager.addJob(new TickJob(80, 0, false, () -> this.villager.playServerAnimation("villager_sit")));
/*     */               
/* 129 */               teacher.throttledSadness(-1);
/*     */             } 
/*     */ 
/*     */             
/* 133 */             teacher.tryAddSkill(ProfessionType.TEACHER, 50);
/*     */           }
/*     */         
/*     */         } 
/* 137 */       } else if (this.learnTime <= 0) {
/* 138 */         startLearn();
/*     */       } 
/*     */     } 
/*     */     
/* 142 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 147 */     if (this.chairPos != null) {
/* 148 */       this.villager.setMovementMode(this.villager.getDefaultMovement());
/* 149 */       startSit();
/* 150 */       startLearn();
/*     */     } 
/*     */ 
/*     */     
/* 154 */     super.onArrival();
/*     */   }
/*     */   
/*     */   private void startLearn() {
/* 158 */     this.learnTime = 150 + this.villager.getRNG().nextInt(150);
/* 159 */     if (this.school.hasTeacherInside())
/* 160 */       this.learnTime -= this.school.getTeacherInside().getSkill(ProfessionType.TEACHER); 
/*     */   }
/*     */   
/*     */   private EnumFacing getChairFacing() {
/* 164 */     if (this.chairPos != null && this.villager.world.isBlockLoaded(this.chairPos)) {
/* 165 */       IBlockState state = this.villager.world.getBlockState(this.chairPos);
/* 166 */       EnumFacing enumfacing = (state.getBlock() instanceof BlockHorizontal) ? ((EnumFacing)state.getValue((IProperty)BlockHorizontal.FACING)).getOpposite() : null;
/* 167 */       return enumfacing;
/*     */     } 
/*     */     
/* 170 */     return null;
/*     */   }
/*     */   
/*     */   private int getChairAxis() {
/* 174 */     EnumFacing facing = getChairFacing();
/* 175 */     if (facing != null) {
/* 176 */       return facing.getHorizontalIndex();
/*     */     }
/*     */     
/* 179 */     return -1;
/*     */   }
/*     */   
/*     */   private void startSit() {
/* 183 */     int chairAxis = getChairAxis();
/* 184 */     if (chairAxis >= 0) {
/* 185 */       moveToSitPos();
/* 186 */       this.villager.onStartSit(chairAxis);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Vec3d getSitPos() {
/* 191 */     return new Vec3d(this.destinationPos.getX() + 0.5D, this.destinationPos.getY() + this.villager.getSitOffset(), this.destinationPos.getZ() + 0.5D);
/*     */   }
/*     */   
/*     */   private void moveTo(Vec3d pos) {
/* 195 */     this.villager.setLocationAndAngles(pos.x, pos.y, pos.z, this.villager.rotationYaw, this.villager.rotationPitch);
/* 196 */     this.villager.motionX = 0.0D;
/* 197 */     this.villager.motionY = 0.0D;
/* 198 */     this.villager.motionZ = 0.0D;
/*     */   }
/*     */   
/*     */   private void moveToSitPos() {
/* 202 */     Vec3d sitPos = getSitPos();
/* 203 */     if (this.villager.getPositionVector().squareDistanceTo(sitPos.x, sitPos.y, sitPos.z) > 0.05D) {
/* 204 */       moveTo(sitPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 210 */     if (this.chairPos != null && this.school != null && this.school.isValid()) {
/* 211 */       this.school.vacateSpecialBlock(this.chairPos);
/* 212 */       this.chairPos = null;
/*     */     } 
/*     */     
/* 215 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/* 216 */     this.villager.onStopSit();
/* 217 */     this.learnTime = 0;
/*     */     
/* 219 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAISchoolAttend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */