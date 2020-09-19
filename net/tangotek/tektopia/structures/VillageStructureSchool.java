/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.entities.EntityTeacher;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ import net.tangotek.tektopia.tickjob.TickJob;
/*    */ 
/*    */ 
/*    */ public class VillageStructureSchool
/*    */   extends VillageStructure
/*    */ {
/* 18 */   protected EntityTeacher teacherInside = null;
/*    */   
/*    */   protected VillageStructureSchool(World world, Village v, EntityItemFrame itemFrame) {
/* 21 */     super(world, v, itemFrame, VillageStructureType.SCHOOL, "School");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setupServerJobs() {
/* 27 */     addJob(new TickJob(30, 50, true, () -> {
/*    */             List<EntityTeacher> teacherList = getEntitiesInside(EntityTeacher.class);
/*    */             if (teacherList.isEmpty()) {
/*    */               this.teacherInside = null;
/*    */             } else {
/*    */               this.teacherInside = teacherList.get(0);
/*    */             } 
/*    */           }));
/* 35 */     super.setupServerJobs();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void scanSpecialBlock(BlockPos pos, Block block) {
/* 40 */     if (block == Blocks.CRAFTING_TABLE) {
/* 41 */       addSpecialBlock(Blocks.CRAFTING_TABLE, pos);
/*    */     }
/*    */     
/* 44 */     super.scanSpecialBlock(pos, block);
/*    */   }
/*    */   
/*    */   public boolean hasTeacherInside() {
/* 48 */     return (this.teacherInside != null);
/*    */   }
/*    */   
/*    */   public EntityTeacher getTeacherInside() {
/* 52 */     return this.teacherInside;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 57 */     super.update();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSitTime(EntityVillagerTek villager) {
/* 62 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldVillagerSit(EntityVillagerTek villager) {
/* 67 */     return villager instanceof net.tangotek.tektopia.entities.EntityChild;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureSchool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */