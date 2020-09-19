/*    */ package net.tangotek.tektopia.structures;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.IEntityLivingData;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.entity.passive.EntityAnimal;
/*    */ import net.minecraft.entity.passive.EntitySheep;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.EntityTagType;
/*    */ import net.tangotek.tektopia.ModEntities;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ public class VillageStructureSheepPen
/*    */   extends VillageStructureRancherPen
/*    */ {
/*    */   protected VillageStructureSheepPen(World world, Village v, EntityItemFrame itemFrame) {
/* 22 */     super(world, v, itemFrame, VillageStructureType.SHEEP_PEN, 3, "Sheep Pen");
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityAnimal spawnAnimal(BlockPos pos) {
/* 27 */     EntitySheep animal = new EntitySheep(this.world);
/* 28 */     animal.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
/* 29 */     animal.onInitialSpawn(this.world.getDifficultyForLocation(pos), (IEntityLivingData)null);
/* 30 */     this.world.spawnEntity((Entity)animal);
/* 31 */     ModEntities.makeTaggedEntity((Entity)animal, EntityTagType.VILLAGER);
/* 32 */     return (EntityAnimal)animal;
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityVillagerTek.VillagerThought getNoFoodThought() {
/* 37 */     return EntityVillagerTek.VillagerThought.SHEEP_FOOD;
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityVillagerTek.VillagerThought getNoHarvestThought() {
/* 42 */     return EntityVillagerTek.VillagerThought.SHEARS;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class getAnimalClass() {
/* 47 */     return EntitySheep.class;
/*    */   }
/*    */   public static Predicate<ItemStack> isFood() {
/* 50 */     return p -> (p.getItem() == Items.WHEAT);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureSheepPen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */