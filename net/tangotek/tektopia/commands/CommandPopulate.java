/*     */ package net.tangotek.tektopia.commands;
/*     */ import java.util.List;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.VillageManager;
/*     */ import net.tangotek.tektopia.entities.EntityBard;
/*     */ import net.tangotek.tektopia.entities.EntityBlacksmith;
/*     */ import net.tangotek.tektopia.entities.EntityChild;
/*     */ import net.tangotek.tektopia.entities.EntityFarmer;
/*     */ import net.tangotek.tektopia.entities.EntityGuard;
/*     */ import net.tangotek.tektopia.entities.EntityMiner;
/*     */ import net.tangotek.tektopia.entities.EntityRancher;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructureRancherPen;
/*     */ import net.tangotek.tektopia.structures.VillageStructureType;
/*     */ 
/*     */ class CommandPopulate extends CommandVillageBase {
/*     */   public CommandPopulate() {
/*  25 */     super("populate");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/*  35 */     if (args.length > 1)
/*     */     {
/*  37 */       throw new WrongUsageException("commands.village.populate.usage", new Object[0]);
/*     */     }
/*     */     
/*  40 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/*  41 */     VillageManager vm = VillageManager.get(((EntityPlayer)entityPlayerMP).world);
/*  42 */     Village village = vm.getVillageAt(entityPlayerMP.getPosition());
/*  43 */     if (village != null) {
/*  44 */       int skill = 1;
/*  45 */       if (args.length == 1) {
/*  46 */         skill = Math.min(Integer.valueOf(args[0]).intValue(), 100);
/*     */       }
/*     */       
/*  49 */       populateVillagers(village, skill);
/*  50 */       populateAnimals(village);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateAnimals(Village village) {
/*  57 */     populateAnimalPen(village, VillageStructureType.COW_PEN);
/*  58 */     populateAnimalPen(village, VillageStructureType.PIG_PEN);
/*  59 */     populateAnimalPen(village, VillageStructureType.CHICKEN_COOP);
/*  60 */     populateAnimalPen(village, VillageStructureType.SHEEP_PEN);
/*     */   }
/*     */   
/*     */   private void populateAnimalPen(Village v, VillageStructureType penType) {
/*  64 */     List<VillageStructure> pens = v.getStructures(penType);
/*  65 */     for (VillageStructure pen : pens) {
/*  66 */       if (pen instanceof VillageStructureRancherPen) {
/*  67 */         VillageStructureRancherPen rancherPen = (VillageStructureRancherPen)pen;
/*  68 */         List<EntityAnimal> animals = rancherPen.getEntitiesInside(rancherPen.getAnimalClass());
/*  69 */         animals.stream().forEach(a -> a.setDead());
/*     */         
/*  71 */         for (int i = 0; i < 12; i++) {
/*  72 */           rancherPen.spawnAnimal(rancherPen.getDoorInside());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void populateVillagers(Village village, int skill) {
/*  79 */     List<EntityVillagerTek> villagers = village.getWorld().getEntitiesWithinAABB(EntityVillagerTek.class, village.getAABB().grow(100.0D, 100.0D, 100.0D));
/*  80 */     for (EntityVillagerTek villager : villagers) {
/*  81 */       villager.setDead();
/*     */     }
/*     */     
/*  84 */     List<VillageStructure> structs = village.getStructures(VillageStructureType.TOWNHALL);
/*  85 */     if (!structs.isEmpty()) {
/*  86 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityFarmer(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*  87 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityFarmer(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*  88 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityFarmer(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*  89 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityFarmer(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*  90 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityFarmer(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*  91 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityFarmer(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*     */       
/*  93 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityRancher(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*  94 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityRancher(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*  95 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityRancher(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*     */       
/*  97 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityButcher(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*  98 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityBlacksmith(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*  99 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityMiner(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 100 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityMiner(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 101 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityMiner(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 102 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityTeacher(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 103 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityChild(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 104 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityChild(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 105 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityChild(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*     */       
/* 107 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityGuard(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 108 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityGuard(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 109 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityGuard(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 110 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityGuard(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 111 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityGuard(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*     */       
/* 113 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityBard(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 114 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityChef(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 115 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityEnchanter(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 116 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityDruid(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/* 117 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityCleric(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*     */       
/* 119 */       spawnVillager(village, skill, (EntityVillagerTek)new EntityLumberjack(village.getWorld()), ((VillageStructure)structs.get(0)).getRandomFloorTile());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void spawnVillager(Village v, int skill, EntityVillagerTek villager, BlockPos spawnPos) {
/* 125 */     villager.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
/* 126 */     villager.onInitialSpawn(v.getWorld().getDifficultyForLocation(spawnPos), (IEntityLivingData)null);
/* 127 */     if ((villager.getProfessionType()).canCopy) {
/* 128 */       villager.setSkill(villager.getProfessionType(), skill + (int)(villager.getRNG().nextGaussian() * 30.0D));
/*     */     }
/* 130 */     villager.setIntelligence(skill);
/*     */     
/* 132 */     v.getWorld().spawnEntity((Entity)villager);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandPopulate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */