/*     */ package net.tangotek.tektopia.structures;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.block.BlockFenceGate;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.FMLCommonHandler;
/*     */ import net.tangotek.tektopia.EntityTagType;
/*     */ import net.tangotek.tektopia.ModEntities;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.pathing.BasePathingNode;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ public abstract class VillageStructureRancherPen extends VillageStructureFencedArea {
/*  29 */   private int glowCheck = 0;
/*  30 */   private int gateOpen = 0;
/*  31 */   protected int animalCount = 0;
/*     */   protected final int animalSize;
/*  33 */   private List<Map<EntityAnimal, Integer>> animalSchedule = new ArrayList<>();
/*     */   
/*     */   public enum AnimalScheduleType {
/*  36 */     FEED(0),
/*  37 */     BUTCHER(1),
/*  38 */     HARVEST(2);
/*     */     private final int index;
/*     */     
/*     */     AnimalScheduleType(int idx) {
/*  42 */       this.index = idx;
/*     */     } public int index() {
/*  44 */       return this.index;
/*     */     } }
/*     */   
/*     */   protected VillageStructureRancherPen(World world, Village v, EntityItemFrame itemFrame, VillageStructureType structType, int animalSize, String name) {
/*  48 */     super(world, v, itemFrame, structType, name);
/*  49 */     this.animalSize = animalSize;
/*     */     
/*  51 */     for (int i = 0; i < (AnimalScheduleType.values()).length; i++) {
/*  52 */       this.animalSchedule.add(new HashMap<>());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setupServerJobs() {
/*  57 */     addJob(new TickJob(80, 120, true, () -> updateAnimals()));
/*  58 */     addJob(new TickJob(15, 15, true, () -> closeGate()));
/*  59 */     super.setupServerJobs();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityVillagerTek.VillagerThought getNoHarvestThought() {
/*  68 */     return EntityVillagerTek.VillagerThought.CHICKEN_FOOD;
/*     */   }
/*     */   
/*     */   public BlockPos getSpawnLocation(World world) {
/*  72 */     BlockPos spawnPos = this.door.offset(this.signFacing, -2);
/*  73 */     if (BasePathingNode.isPassable(world, spawnPos)) {
/*  74 */       return spawnPos;
/*     */     }
/*  76 */     spawnPos = this.door.offset(this.signFacing, -1).offset(this.signFacing.rotateY(), 1);
/*  77 */     if (BasePathingNode.isPassable(world, spawnPos)) {
/*  78 */       return spawnPos;
/*     */     }
/*  80 */     spawnPos = this.door.offset(this.signFacing, -1).offset(this.signFacing.rotateY(), -1);
/*  81 */     if (BasePathingNode.isPassable(world, spawnPos)) {
/*  82 */       return spawnPos;
/*     */     }
/*  84 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update() {
/*  89 */     super.update();
/*     */     
/*  91 */     this.glowCheck--;
/*  92 */     if (this.glowCheck < 0) {
/*  93 */       glowAnimals();
/*  94 */       this.glowCheck = 40;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void glowAnimals() {
/*  99 */     if (!this.world.getEntitiesWithinAABB(EntityPlayer.class, getAABB()).isEmpty()) {
/* 100 */       List<EntityAnimal> animals = getEntitiesInside(getAnimalClass());
/* 101 */       animals.forEach(a -> {
/*     */             if (ModEntities.isTaggedEntity((Entity)a, EntityTagType.VILLAGER))
/*     */               a.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 60)); 
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void updateAnimals() {
/* 109 */     List<EntityAnimal> animals = getEntitiesInside(getAnimalClass());
/* 110 */     this.animalCount = animals.size();
/* 111 */     animals.forEach(o -> updateAnimal(o));
/*     */     
/* 113 */     updateAnimalSchedule();
/*     */   }
/*     */   
/*     */   protected void closeGate() {
/* 117 */     if (VillageStructure.isGate(this.world, this.door)) {
/* 118 */       IBlockState state = this.world.getBlockState(this.door);
/* 119 */       if (((Boolean)state.getValue((IProperty)BlockFenceGate.OPEN)).booleanValue()) {
/* 120 */         this.gateOpen++;
/* 121 */         if (this.gateOpen > 4) {
/* 122 */           state = state.withProperty((IProperty)BlockFenceGate.OPEN, Boolean.valueOf(false));
/* 123 */           this.world.setBlockState(this.door, state, 10);
/* 124 */           this.world.playEvent((EntityPlayer)null, ((Boolean)state.getValue((IProperty)BlockFenceGate.OPEN)).booleanValue() ? 1008 : 1014, this.door, 0);
/* 125 */           this.gateOpen = 0;
/*     */         } 
/*     */       } else {
/* 128 */         this.gateOpen = 0;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void updateAnimal(EntityAnimal animal) {
/* 134 */     if (!Village.isNightTime(this.world)) {
/* 135 */       int hungerChance = 20;
/* 136 */       if (animal.getRNG().nextInt(hungerChance) == 0) {
/* 137 */         int newHunger = ModEntities.modifyAnimalHunger(animal, -3 - this.world.rand.nextInt(4));
/* 138 */         if (newHunger < 40) {
/* 139 */           getVillage().debugOut(getAnimalClass().getSimpleName() + " hunger -> " + newHunger);
/*     */         }
/* 141 */         if (ModEntities.isAnimalStarving(animal)) {
/* 142 */           animal.attackEntityFrom(DamageSource.STARVE, 1.0F);
/* 143 */           if (!animal.isEntityAlive()) {
/* 144 */             this.village.sendChatMessage("A " + animal.getDisplayName().getUnformattedText() + " has died of starvation.");
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateAnimalSchedule() {
/* 153 */     for (Map<EntityAnimal, Integer> map : this.animalSchedule) {
/* 154 */       Iterator<Map.Entry<EntityAnimal, Integer>> itr = map.entrySet().iterator();
/* 155 */       while (itr.hasNext()) {
/* 156 */         Map.Entry<EntityAnimal, Integer> entry = itr.next();
/* 157 */         if (!((EntityAnimal)entry.getKey()).isEntityAlive()) {
/* 158 */           itr.remove(); continue;
/*     */         } 
/* 160 */         if (((Integer)entry.getValue()).intValue() >= 3) {
/* 161 */           itr.remove(); continue;
/*     */         } 
/* 163 */         entry.setValue(Integer.valueOf(((Integer)entry.getValue()).intValue() + 1));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void scheduleAnimal(EntityAnimal animal, AnimalScheduleType scheduleType) {
/* 170 */     if (!isAnimalScheduled(animal, scheduleType))
/* 171 */       ((Map<EntityAnimal, Integer>)this.animalSchedule.get(scheduleType.index())).put(animal, Integer.valueOf(0)); 
/*     */   }
/*     */   
/*     */   public void clearAnimalSchedule(EntityAnimal animal, AnimalScheduleType scheduleType) {
/* 175 */     ((Map)this.animalSchedule.get(scheduleType.index())).remove(animal);
/*     */   }
/*     */   
/*     */   public boolean isAnimalScheduled(EntityAnimal animal, AnimalScheduleType scheduleType) {
/* 179 */     return ((Map)this.animalSchedule.get(scheduleType.index())).containsKey(animal);
/*     */   }
/*     */   
/*     */   public boolean isAnimalHome(EntityAnimal animal) {
/* 183 */     return getAnimalClass().isInstance(animal);
/*     */   }
/*     */   
/*     */   public boolean isPenFull(float percent) {
/* 187 */     int pct = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getGameRules().getInt("villagerPenPercent");
/* 188 */     int goalCount = (int)((this.floorTiles.size() * pct) * percent / (getAnimalSize() * 100));
/* 189 */     return (this.animalCount > 2 && this.animalCount >= goalCount);
/*     */   }
/*     */   public int getAnimalSize() {
/* 192 */     return this.animalSize;
/*     */   }
/*     */   
/*     */   public int getFeedCost(EntityVillagerTek villager) {
/* 196 */     return 3;
/*     */   }
/*     */   
/*     */   public abstract EntityAnimal spawnAnimal(BlockPos paramBlockPos);
/*     */   
/*     */   public abstract Class getAnimalClass();
/*     */   
/*     */   public abstract EntityVillagerTek.VillagerThought getNoFoodThought();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureRancherPen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */