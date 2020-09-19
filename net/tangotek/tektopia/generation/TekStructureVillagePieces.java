/*    */ package net.tangotek.tektopia.generation;
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.entity.passive.EntityVillager;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.gen.structure.MapGenStructureIO;
/*    */ import net.minecraft.world.gen.structure.StructureBoundingBox;
/*    */ import net.minecraftforge.fml.common.registry.VillagerRegistry;
/*    */ import net.tangotek.tektopia.ModItems;
/*    */ import net.tangotek.tektopia.entities.EntityBard;
/*    */ import net.tangotek.tektopia.entities.EntityBlacksmith;
/*    */ import net.tangotek.tektopia.entities.EntityCleric;
/*    */ import net.tangotek.tektopia.entities.EntityDruid;
/*    */ import net.tangotek.tektopia.entities.EntityFarmer;
/*    */ import net.tangotek.tektopia.entities.EntityGuard;
/*    */ import net.tangotek.tektopia.entities.EntityLumberjack;
/*    */ import net.tangotek.tektopia.entities.EntityNitwit;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ public class TekStructureVillagePieces {
/*    */   public static void registerVillagePieces() {
/* 27 */     System.out.println("Registering Tek Village Structures");
/* 28 */     MapGenStructureIO.registerStructureComponent(TekTownHall.class, "TekTownHall");
/* 29 */     MapGenStructureIO.registerStructureComponent(TekStorageHall.class, "TekStorage");
/* 30 */     MapGenStructureIO.registerStructureComponent(TekHouse6.class, "TekHouse6");
/* 31 */     MapGenStructureIO.registerStructureComponent(TekHouse2.class, "TekHouse2");
/* 32 */     MapGenStructureIO.registerStructureComponent(TekHouse2b.class, "TekHouse2b");
/*    */ 
/*    */     
/* 35 */     VillagerRegistry.instance().registerVillageCreationHandler(new TekTownHallHandler());
/* 36 */     VillagerRegistry.instance().registerVillageCreationHandler(new TekStorageHallHandler());
/* 37 */     VillagerRegistry.instance().registerVillageCreationHandler(new TekHouse6Handler());
/* 38 */     VillagerRegistry.instance().registerVillageCreationHandler(new TekHouse2Handler());
/* 39 */     VillagerRegistry.instance().registerVillageCreationHandler(new TekHouse2bHandler());
/*    */   }
/*    */   
/*    */   public static EntityItemFrame addStructureFrame(World worldIn, StructureBoundingBox bbox, BlockPos doorPos, VillageStructureType structType) {
/* 43 */     EnumFacing facing = BlockDoor.getFacing((IBlockAccess)worldIn, doorPos).getOpposite();
/* 44 */     BlockPos framePos = doorPos.offset(facing, 1).offset(facing.rotateY(), 1).up();
/* 45 */     if (bbox.isVecInside((Vec3i)framePos)) {
/* 46 */       EntityItemFrame itemFrame = new EntityItemFrame(worldIn, framePos, facing);
/* 47 */       itemFrame.setDisplayedItem(structType.itemStack);
/* 48 */       worldIn.spawnEntity((Entity)itemFrame);
/* 49 */       return itemFrame;
/*    */     } 
/*    */     
/* 52 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void replaceVillagers(World world, Random ran, double x1, double y1, double z1, double x2, double y2, double z2) {
/* 58 */     List<EntityVillager> villagerList = world.getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
/* 59 */     for (EntityVillager vil : villagerList) {
/*    */       
/* 61 */       EntityVillagerTek newVillager = generateVillager(world, ran);
/* 62 */       newVillager.copyLocationAndAnglesFrom((Entity)vil);
/* 63 */       newVillager.onInitialSpawn(world.getDifficultyForLocation(newVillager.getPosition()), (IEntityLivingData)null);
/* 64 */       world.spawnEntity((Entity)newVillager);
/* 65 */       vil.setDead();
/*    */     } 
/*    */   }
/*    */   public static EntityVillagerTek generateVillager(World world, Random ran) {
/*    */     EntityNitwit entityNitwit;
/* 70 */     int rnd = ran.nextInt(80);
/*    */     
/* 72 */     if (rnd < 40) {
/* 73 */       EntityFarmer entityFarmer = new EntityFarmer(world);
/*    */     }
/* 75 */     else if (rnd < 60) {
/* 76 */       EntityLumberjack entityLumberjack = new EntityLumberjack(world);
/* 77 */       entityLumberjack.getInventory().addItem(ModItems.createTaggedItem(Items.STONE_AXE, 1, ItemTagType.VILLAGER));
/*    */     }
/* 79 */     else if (rnd < 70) {
/* 80 */       EntityGuard entityGuard = new EntityGuard(world);
/* 81 */       entityGuard.getInventory().addItem(ModItems.createTaggedItem(Items.STONE_SWORD, 1, ItemTagType.VILLAGER));
/*    */     }
/* 83 */     else if (rnd == 70) {
/* 84 */       EntityBard entityBard = new EntityBard(world);
/*    */     }
/* 86 */     else if (rnd == 71) {
/* 87 */       EntityCleric entityCleric = new EntityCleric(world);
/*    */     }
/* 89 */     else if (rnd == 72) {
/* 90 */       EntityBlacksmith entityBlacksmith = new EntityBlacksmith(world);
/*    */     }
/* 92 */     else if (rnd == 73) {
/* 93 */       EntityDruid entityDruid = new EntityDruid(world);
/*    */     } else {
/*    */       
/* 96 */       entityNitwit = new EntityNitwit(world);
/*    */     } 
/*    */     
/* 99 */     return (EntityVillagerTek)entityNitwit;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\generation\TekStructureVillagePieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */