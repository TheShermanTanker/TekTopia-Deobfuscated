/*     */ package net.tangotek.tektopia.items;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumActionResult;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.SoundCategory;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.ModSoundEvents;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.entities.EntityChild;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class ItemHeart extends Item {
/*     */   public ItemHeart(String name) {
/*  30 */     setCreativeTab(CreativeTabs.MISC);
/*  31 */     setRegistryName(name);
/*  32 */     setTranslationKey(name);
/*  33 */     this.name = name;
/*     */   }
/*     */   private String name;
/*     */   public void registerItemModel() {
/*  37 */     TekVillager.proxy.registerItemRenderer(this, 0, this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEntityLifespan(ItemStack itemStack, World world) {
/*  42 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onEntityItemUpdate(EntityItem entityItem) {
/*  48 */     if (entityItem.world.isRemote) {
/*  49 */       if (entityItem.world.rand.nextInt(17) == 0)
/*     */       {
/*  51 */         entityItem.world.playSound(((float)entityItem.posX + 0.5F), ((float)entityItem.posY + 0.5F), ((float)entityItem.posZ + 0.5F), ModSoundEvents.twinkle, SoundCategory.BLOCKS, MathHelper.nextFloat(entityItem.world.rand, 0.8F, 1.1F), MathHelper.nextFloat(entityItem.world.rand, 0.7F, 1.3F), false);
/*     */       }
/*     */       
/*  54 */       if (entityItem.world.rand.nextInt(4) == 0) {
/*  55 */         double d0 = (float)entityItem.posX + MathHelper.nextDouble(itemRand, -3.0D, 3.0D);
/*  56 */         double d1 = (float)entityItem.posY + MathHelper.nextDouble(itemRand, 0.5D, 2.0D);
/*  57 */         double d2 = (float)entityItem.posZ + MathHelper.nextDouble(itemRand, -3.0D, 3.0D);
/*  58 */         entityItem.world.spawnParticle(EnumParticleTypes.HEART, d0, d1, d2, 0.0D, itemRand.nextDouble() * 0.2D, 0.0D, new int[0]);
/*     */       } 
/*     */     } 
/*     */     
/*  62 */     return super.onEntityItemUpdate(entityItem);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
/*  67 */     IBlockState iblockstate = world.getBlockState(pos);
/*  68 */     ItemStack itemstack = player.getHeldItem(hand);
/*     */ 
/*     */     
/*  71 */     if (player.canPlayerEdit(pos.offset(side), side, itemstack) && iblockstate.getBlock() == Blocks.BED) {
/*     */       
/*  73 */       if (world.isRemote) {
/*     */         
/*  75 */         for (int i = 0; i < 10; i++) {
/*     */           
/*  77 */           double d0 = pos.getX() + MathHelper.nextDouble(itemRand, -2.0D, 2.0D);
/*  78 */           double d1 = pos.getY() + MathHelper.nextDouble(itemRand, 0.5D, 1.5D);
/*  79 */           double d2 = pos.getZ() + MathHelper.nextDouble(itemRand, -2.0D, 2.0D);
/*  80 */           world.spawnParticle(EnumParticleTypes.HEART, d0, d1, d2, 0.0D, itemRand.nextDouble() * 0.2D, 0.0D, new int[0]);
/*     */           
/*  82 */           d0 = pos.getX() + MathHelper.nextDouble(itemRand, -1.0D, 1.0D);
/*  83 */           d1 = pos.getY() + MathHelper.nextDouble(itemRand, 0.5D, 1.5D);
/*  84 */           d2 = pos.getZ() + MathHelper.nextDouble(itemRand, -1.0D, 1.0D);
/*  85 */           world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, d0, d1, d2, 0.0D, itemRand.nextDouble() * 0.2D, 0.0D, new int[0]);
/*     */           
/*  87 */           d0 = pos.getX() + MathHelper.nextDouble(itemRand, -1.0D, 1.0D);
/*  88 */           d1 = pos.getY() + MathHelper.nextDouble(itemRand, 0.5D, 1.5D);
/*  89 */           d2 = pos.getZ() + MathHelper.nextDouble(itemRand, -1.0D, 1.0D);
/*  90 */           world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, d0, d1, d2, 0.0D, itemRand.nextDouble() * 0.2D, 0.0D, new int[0]);
/*     */         } 
/*  92 */         return EnumActionResult.SUCCESS;
/*     */       } 
/*     */ 
/*     */       
/*  96 */       String parentLastName = "";
/*  97 */       if (itemstack.getSubCompound("village").hasUniqueId("parent")) {
/*  98 */         UUID parentUUID = itemstack.getSubCompound("village").getUniqueId("parent");
/*  99 */         List<EntityVillagerTek> parents = world.getEntitiesWithinAABB(EntityVillagerTek.class, (new AxisAlignedBB(pos)).grow(200.0D, 255.0D, 200.0D), p -> (p.getUniqueID() == parentUUID));
/* 100 */         if (!parents.isEmpty()) {
/* 101 */           EntityVillagerTek parent = parents.get(0);
/* 102 */           parentLastName = parent.getLastName();
/*     */         } 
/*     */       } 
/*     */       
/* 106 */       itemstack.shrink(1);
/*     */       
/* 108 */       EntityChild child = new EntityChild(world);
/* 109 */       child.setLocationAndAngles(pos.getX() + 0.5D, (pos.getY() + 1), pos.getZ() + 0.5D, 0.0F, 0.0F);
/* 110 */       child.onInitialSpawn(world.getDifficultyForLocation(pos), (IEntityLivingData)null);
/* 111 */       String childFirstName = child.getFirstName();
/* 112 */       if (!parentLastName.isEmpty() && !childFirstName.isEmpty()) {
/* 113 */         child.setCustomNameTag(childFirstName + " " + parentLastName);
/*     */       }
/* 115 */       world.spawnEntity((Entity)child);
/*     */       
/* 117 */       world.playSound((EntityPlayer)null, pos, ModSoundEvents.villagerHeartMagic, SoundCategory.BLOCKS, 1.5F, 1.0F);
/* 118 */       return EnumActionResult.SUCCESS;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 123 */     return EnumActionResult.FAIL;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\items\ItemHeart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */