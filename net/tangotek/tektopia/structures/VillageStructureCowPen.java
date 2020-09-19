/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.IEntityLivingData;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.entity.passive.EntityAnimal;
/*    */ import net.minecraft.entity.passive.EntityCow;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.EntityTagType;
/*    */ import net.tangotek.tektopia.ModEntities;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ public class VillageStructureCowPen
/*    */   extends VillageStructureRancherPen
/*    */ {
/*    */   protected VillageStructureCowPen(World world, Village v, EntityItemFrame itemFrame) {
/* 22 */     super(world, v, itemFrame, VillageStructureType.COW_PEN, 3, "Cow Pen");
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityAnimal spawnAnimal(BlockPos pos) {
/* 27 */     EntityCow cow = new EntityCow(this.world);
/* 28 */     cow.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
/* 29 */     cow.onInitialSpawn(this.world.getDifficultyForLocation(pos), (IEntityLivingData)null);
/* 30 */     this.world.spawnEntity((Entity)cow);
/* 31 */     ModEntities.makeTaggedEntity((Entity)cow, EntityTagType.VILLAGER);
/* 32 */     return (EntityAnimal)cow;
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityVillagerTek.VillagerThought getNoFoodThought() {
/* 37 */     return EntityVillagerTek.VillagerThought.COW_FOOD;
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityVillagerTek.VillagerThought getNoHarvestThought() {
/* 42 */     return EntityVillagerTek.VillagerThought.BUCKET;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class getAnimalClass() {
/* 47 */     return EntityCow.class;
/*    */   }
/*    */   public static Predicate<ItemStack> isFood() {
/* 50 */     return p -> (p.getItem() == Items.WHEAT);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureCowPen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */