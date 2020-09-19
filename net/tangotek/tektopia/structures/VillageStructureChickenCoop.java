/*    */ package net.tangotek.tektopia.structures;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.IEntityLivingData;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.entity.passive.EntityAnimal;
/*    */ import net.minecraft.entity.passive.EntityChicken;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.tangotek.tektopia.EntityTagType;
/*    */ import net.tangotek.tektopia.ItemTagType;
/*    */ import net.tangotek.tektopia.ModItems;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ public class VillageStructureChickenCoop extends VillageStructureRancherPen {
/*    */   protected VillageStructureChickenCoop(World world, Village v, EntityItemFrame itemFrame) {
/* 21 */     super(world, v, itemFrame, VillageStructureType.CHICKEN_COOP, 1, "Chicken Coop");
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityAnimal spawnAnimal(BlockPos pos) {
/* 26 */     EntityChicken animal = new EntityChicken(this.world);
/* 27 */     animal.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
/* 28 */     animal.onInitialSpawn(this.world.getDifficultyForLocation(pos), (IEntityLivingData)null);
/* 29 */     this.world.spawnEntity((Entity)animal);
/* 30 */     ModEntities.makeTaggedEntity((Entity)animal, EntityTagType.VILLAGER);
/* 31 */     return (EntityAnimal)animal;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class getAnimalClass() {
/* 36 */     return EntityChicken.class;
/*    */   }
/*    */   public static Predicate<ItemStack> isFood() {
/* 39 */     return p -> (p.getItem() == Items.WHEAT_SEEDS || p.getItem() == Items.BEETROOT_SEEDS);
/*    */   }
/*    */   
/*    */   public EntityVillagerTek.VillagerThought getNoFoodThought() {
/* 43 */     return EntityVillagerTek.VillagerThought.CHICKEN_FOOD;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public EntityVillagerTek.VillagerThought getNoHarvestThought() {
/* 49 */     return EntityVillagerTek.VillagerThought.BUCKET;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void updateAnimal(EntityAnimal animal) {
/* 54 */     if (animal instanceof EntityChicken) {
/* 55 */       EntityChicken chicken = (EntityChicken)animal;
/* 56 */       chicken.timeUntilNextEgg = 9999999;
/* 57 */       if (!this.world.isRemote && !chicken.isChild() && !chicken.isChickenJockey())
/*    */       {
/*    */         
/* 60 */         if (chicken.getRNG().nextInt(500) == 0) {
/* 61 */           chicken.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (chicken.getRNG().nextFloat() - chicken.getRNG().nextFloat()) * 0.2F + 1.0F);
/* 62 */           chicken.entityDropItem(ModItems.makeTaggedItem(new ItemStack(Items.EGG), ItemTagType.VILLAGER), 0.0F);
/*    */         } 
/*    */       }
/*    */     } 
/* 66 */     super.updateAnimal(animal);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureChickenCoop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */