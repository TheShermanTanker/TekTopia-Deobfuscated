/*     */ package net.tangotek.tektopia.entities.ai;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.block.BlockHorizontal;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ 
/*     */ public class EntityAIWanderStructure
/*     */   extends EntityAIMoveToBlock {
/*     */   private final int happyChance;
/*  19 */   private int sitTime = 0; private final Function<EntityVillagerTek, VillageStructure> wanderFunc; private BlockPos chairPos;
/*     */   private boolean forceExecute = false;
/*     */   private VillageStructure structure;
/*     */   protected final EntityVillagerTek villager;
/*     */   private final Predicate<EntityVillagerTek> shouldPred;
/*     */   
/*     */   public EntityAIWanderStructure(EntityVillagerTek v, Function<EntityVillagerTek, VillageStructure> whereFunc, Predicate<EntityVillagerTek> shouldPred, int happyChance) {
/*  26 */     super((EntityVillageNavigator)v);
/*  27 */     this.villager = v;
/*  28 */     this.shouldPred = shouldPred;
/*  29 */     this.happyChance = happyChance;
/*  30 */     this.wanderFunc = whereFunc;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  35 */     if (this.villager.isAITick() && this.villager.hasVillage() && this.shouldPred.test(this.villager) && this.villager.getRNG().nextInt(2) == 0) {
/*  36 */       return super.shouldExecute();
/*     */     }
/*     */     
/*  39 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldContinueExecuting() {
/*  45 */     if (this.sitTime > 0) {
/*  46 */       return true;
/*     */     }
/*  48 */     return super.shouldContinueExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   void updateMovementMode() {
/*  53 */     this.villager.setMovementMode(this.villager.getDefaultMovement());
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  58 */     this.forceExecute = false;
/*  59 */     super.startExecuting();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos findWalkPos() {
/*  64 */     return this.destinationPos;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isNearWalkPos() {
/*  69 */     if (this.chairPos != null) {
/*  70 */       return (this.chairPos != null && this.chairPos.distanceSq((Vec3i)this.villager.getPosition()) <= 1.0D);
/*     */     }
/*  72 */     return super.isNearWalkPos();
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockPos getDestinationBlock() {
/*  77 */     this.structure = this.wanderFunc.apply(this.villager);
/*  78 */     if (this.structure != null) {
/*     */       
/*  80 */       this.chairPos = this.structure.tryVillagerSit(this.villager);
/*  81 */       if (this.chairPos != null) {
/*  82 */         return this.chairPos;
/*     */       }
/*     */       
/*  85 */       BlockPos pos = this.structure.getRandomFloorTile();
/*  86 */       if (pos != null) {
/*  87 */         return pos;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  98 */     if (this.sitTime > 0) {
/*  99 */       this.sitTime--;
/* 100 */       if (this.sitTime % 10 == 0) {
/* 101 */         moveToSitPos();
/*     */         
/* 103 */         if (this.villager.getRNG().nextInt(120) == 0) {
/* 104 */           this.villager.modifyHappy(1);
/*     */         }
/*     */       } 
/*     */     } 
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
/* 118 */     super.updateTask();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onArrival() {
/* 123 */     if (this.chairPos != null) {
/* 124 */       this.sitTime = this.structure.getSitTime(this.villager);
/* 125 */       startSit();
/* 126 */       testHappy(3);
/*     */     } else {
/*     */       
/* 129 */       testHappy(2);
/*     */     } 
/*     */     
/* 132 */     super.onArrival();
/*     */   }
/*     */   
/*     */   private void testHappy(int happyVal) {
/* 136 */     if (this.happyChance > 0 && this.villager.getRNG().nextInt(this.happyChance) == 0) {
/* 137 */       this.villager.modifyHappy(happyVal);
/*     */     }
/*     */   }
/*     */   
/*     */   private EnumFacing getChairFacing() {
/* 142 */     if (this.chairPos != null && this.villager.world.isBlockLoaded(this.chairPos)) {
/* 143 */       IBlockState state = this.villager.world.getBlockState(this.chairPos);
/* 144 */       EnumFacing enumfacing = (state.getBlock() instanceof BlockHorizontal) ? ((EnumFacing)state.getValue((IProperty)BlockHorizontal.FACING)).getOpposite() : null;
/* 145 */       return enumfacing;
/*     */     } 
/*     */     
/* 148 */     return null;
/*     */   }
/*     */   
/*     */   private int getChairAxis() {
/* 152 */     EnumFacing facing = getChairFacing();
/* 153 */     if (facing != null) {
/* 154 */       return facing.getHorizontalIndex();
/*     */     }
/*     */     
/* 157 */     return -1;
/*     */   }
/*     */   
/*     */   private void startSit() {
/* 161 */     int chairAxis = getChairAxis();
/* 162 */     if (chairAxis >= 0) {
/* 163 */       moveToSitPos();
/* 164 */       this.villager.onStartSit(chairAxis);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Vec3d getSitPos() {
/* 169 */     return new Vec3d(this.destinationPos.getX() + 0.5D, this.destinationPos.getY() + this.villager.getSitOffset(), this.destinationPos.getZ() + 0.5D);
/*     */   }
/*     */   
/*     */   private void moveTo(Vec3d pos) {
/* 173 */     this.villager.setLocationAndAngles(pos.x, pos.y, pos.z, this.villager.rotationYaw, this.villager.rotationPitch);
/* 174 */     this.villager.motionX = 0.0D;
/* 175 */     this.villager.motionY = 0.0D;
/* 176 */     this.villager.motionZ = 0.0D;
/*     */   }
/*     */   
/*     */   private void moveToSitPos() {
/* 180 */     Vec3d sitPos = getSitPos();
/* 181 */     if (this.villager.getPositionVector().squareDistanceTo(sitPos.x, sitPos.y, sitPos.z) > 0.05D) {
/* 182 */       moveTo(sitPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/* 188 */     if (this.chairPos != null && this.structure != null) {
/* 189 */       this.structure.vacateSpecialBlock(this.chairPos);
/*     */     }
/*     */     
/* 192 */     this.villager.onStopSit();
/* 193 */     this.sitTime = 0;
/*     */     
/* 195 */     super.resetTask();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\ai\EntityAIWanderStructure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */