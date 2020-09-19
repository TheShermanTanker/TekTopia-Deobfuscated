/*     */ package net.tangotek.tektopia.structures;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockBed;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityBed;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ public class VillageStructureHome
/*     */   extends VillageStructure {
/*  26 */   private List<EntityVillagerTek> residents = new ArrayList<>();
/*     */   private final int maxBeds;
/*     */   
/*     */   protected VillageStructureHome(World world, Village v, EntityItemFrame itemFrame, VillageStructureType structureType, String name, int maxBeds) {
/*  30 */     super(world, v, itemFrame, structureType, name);
/*  31 */     this.maxBeds = maxBeds;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setupServerJobs() {
/*  36 */     addJob(new TickJob(40, 40, true, () -> evictResidents()));
/*  37 */     super.setupServerJobs();
/*     */   }
/*     */   
/*     */   public List<EntityVillagerTek> getResidents() {
/*  41 */     return Collections.unmodifiableList(this.residents);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDestroy() {
/*  46 */     for (EntityVillagerTek resident : this.residents) {
/*  47 */       resident.clearHome();
/*     */     }
/*     */     
/*  50 */     List<BlockPos> beds = new ArrayList<>(getSpecialBlocks(Blocks.BED));
/*  51 */     for (BlockPos bed : beds) {
/*  52 */       setBedColor(bed, EnumDyeColor.RED);
/*     */     }
/*     */     
/*  55 */     super.onDestroy();
/*     */   }
/*     */   
/*     */   public static boolean isBed(World w, BlockPos bp) {
/*  59 */     IBlockState state = w.isBlockLoaded(bp) ? w.getBlockState(bp) : null;
/*  60 */     return (state != null && state.getBlock().isBed(state, (IBlockAccess)w, bp, null));
/*     */   }
/*     */   
/*     */   public boolean isBedFoot(BlockPos bp) {
/*  64 */     if (isBed(this.world, bp)) {
/*  65 */       return (Blocks.BED.isBedFoot((IBlockAccess)this.world, bp) && this.world.isAirBlock(bp.up()));
/*     */     }
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void scanSpecialBlock(BlockPos pos, Block block) {
/*  72 */     if (isBedFoot(pos)) {
/*  73 */       List<BlockPos> existingBeds = new ArrayList<>(getSpecialBlocks(Blocks.BED));
/*  74 */       if (existingBeds.size() < this.maxBeds) {
/*  75 */         addSpecialBlock(Blocks.BED, pos);
/*  76 */         boolean claimed = false;
/*  77 */         for (EntityVillagerTek resident : this.residents) {
/*  78 */           if (resident.getBedPos() != null && resident.getBedPos().equals(pos)) {
/*  79 */             setBedColor(pos, EnumDyeColor.GREEN);
/*  80 */             claimed = true;
/*     */           } 
/*     */         } 
/*  83 */         if (!claimed) {
/*  84 */           setBedColor(pos, EnumDyeColor.YELLOW);
/*     */         }
/*     */       } else {
/*  87 */         setBedColor(pos, EnumDyeColor.RED);
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     super.scanSpecialBlock(pos, block);
/*     */   }
/*     */   
/*     */   public boolean canSleepAt(BlockPos pos) {
/*  95 */     for (EntityVillagerTek resident : this.residents) {
/*  96 */       if (resident.isSleeping()) {
/*  97 */         if (resident.getBedPos().equals(pos)) {
/*  98 */           return false;
/*     */         }
/* 100 */         IBlockState blockState = this.village.getWorld().getBlockState(resident.getBedPos());
/* 101 */         EnumFacing facing = (EnumFacing)blockState.getValue((IProperty)BlockBed.FACING);
/* 102 */         if (facing != null) {
/* 103 */           BlockPos headPos = resident.getBedPos().offset(facing);
/* 104 */           if (headPos.equals(pos)) {
/* 105 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canVillagerSleep(EntityVillagerTek villager) {
/* 114 */     return true;
/*     */   }
/*     */   
/*     */   public void addResident(EntityVillagerTek villager) {
/* 118 */     if (!isFull() && !this.residents.contains(villager)) {
/* 119 */       List<BlockPos> beds = new ArrayList<>(getSpecialBlocks(Blocks.BED));
/* 120 */       for (EntityVillagerTek resident : this.residents) {
/* 121 */         ListIterator<BlockPos> bedItr = beds.listIterator();
/* 122 */         while (bedItr.hasNext()) {
/* 123 */           if (((BlockPos)bedItr.next()).equals(resident.getBedPos())) {
/* 124 */             bedItr.remove();
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 131 */       if (!beds.isEmpty()) {
/* 132 */         villager.setHome(beds.get(0), this.framePos);
/* 133 */         this.residents.add(villager);
/*     */         
/* 135 */         if (this.village != null && this.village.isValid()) {
/* 136 */           setBedColor(beds.get(0), EnumDyeColor.GREEN);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setBedColor(BlockPos footPos, EnumDyeColor color) {
/* 143 */     TileEntity tileEntity = this.village.getWorld().getTileEntity(footPos);
/* 144 */     if (tileEntity instanceof TileEntityBed) {
/* 145 */       TileEntityBed bed = (TileEntityBed)tileEntity;
/* 146 */       if (bed.getColor() != color) {
/* 147 */         bed.setColor(color);
/*     */         
/* 149 */         IBlockState blockState = this.village.getWorld().getBlockState(footPos);
/* 150 */         EnumFacing facing = (EnumFacing)blockState.getValue((IProperty)BlockBed.FACING);
/* 151 */         BlockPos headPos = footPos.offset(facing);
/* 152 */         IBlockState headState = this.village.getWorld().getBlockState(headPos);
/* 153 */         tileEntity = this.village.getWorld().getTileEntity(headPos);
/* 154 */         if (tileEntity instanceof TileEntityBed) {
/* 155 */           bed = (TileEntityBed)tileEntity;
/* 156 */           bed.setColor(color);
/*     */         } 
/*     */         
/* 159 */         this.village.getWorld().markAndNotifyBlock(footPos, null, blockState, blockState, 3);
/* 160 */         this.village.getWorld().markAndNotifyBlock(headPos, null, headState, headState, 3);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void evictResidents() {
/* 166 */     ListIterator<EntityVillagerTek> itr = this.residents.listIterator();
/*     */     
/* 168 */     while (itr.hasNext()) {
/* 169 */       EntityVillagerTek resident = itr.next();
/* 170 */       if (!resident.isEntityAlive()) {
/* 171 */         itr.remove(); continue;
/*     */       } 
/* 173 */       if (resident.getBedPos() == null || !isBedFoot(resident.getBedPos())) {
/* 174 */         resident.clearHome();
/* 175 */         itr.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMaxResidents() {
/* 181 */     return getSpecialBlocks(Blocks.BED).size();
/*     */   }
/*     */   
/*     */   public int getCurResidents() {
/* 185 */     return this.residents.size();
/*     */   }
/*     */   
/*     */   public boolean isFull() {
/* 189 */     return (getCurResidents() >= getMaxResidents());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldVillagerSit(EntityVillagerTek villager) {
/* 194 */     return (this.world.rand.nextInt(2) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSitTime(EntityVillagerTek villager) {
/* 199 */     return 100 + villager.getRNG().nextInt(300);
/*     */   }
/*     */   
/*     */   public void villageReport(StringBuilder builder) {
/* 203 */     builder.append("        Home: " + getDoor() + "     " + getCurResidents() + " residents\n");
/* 204 */     for (EntityVillagerTek resident : this.residents)
/* 205 */       builder.append("            " + resident.getClass().getSimpleName() + "|" + resident.getDisplayName().getFormattedText() + "|" + resident.getEntityId() + "        Happy: " + resident.getHappy() + "   Hunger: " + resident.getHunger() + "\n"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\structures\VillageStructureHome.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */