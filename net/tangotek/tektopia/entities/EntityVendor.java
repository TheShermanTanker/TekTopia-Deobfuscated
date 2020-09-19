/*     */ package net.tangotek.tektopia.entities;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IMerchant;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.text.ITextComponent;
/*     */ import net.minecraft.util.text.TextComponentTranslation;
/*     */ import net.minecraft.village.MerchantRecipe;
/*     */ import net.minecraft.village.MerchantRecipeList;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import net.tangotek.tektopia.ProfessionType;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIWanderStructure;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ public abstract class EntityVendor
/*     */   extends EntityVillagerTek
/*     */   implements IMerchant {
/*     */   @Nullable
/*     */   protected EntityPlayer buyingPlayer;
/*     */   @Nullable
/*     */   protected MerchantRecipeList buyingList;
/*     */   
/*     */   public EntityVendor(World worldIn, int roleMask) {
/*  38 */     super(worldIn, (ProfessionType)null, roleMask | VillagerRole.VENDOR.value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setupServerJobs() {
/*  43 */     super.setupServerJobs();
/*     */     
/*  45 */     addJob(new TickJob(200, 200, true, () -> {
/*     */             if (getCurrentStructure() == null) {
/*     */               setDead();
/*     */             }
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMale() {
/*  54 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initEntityAIBase() {}
/*     */ 
/*     */   
/*     */   protected void initEntityAI() {
/*  63 */     super.initEntityAI();
/*     */     
/*  65 */     addTask(50, (EntityAIBase)new EntityAIWanderStructure(this, p -> p.getCurrentStructure(), p -> (p.getRNG().nextInt(5) == 0), 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ItemStack createEmerald(int qty) {
/*  72 */     return new ItemStack(Items.EMERALD, qty);
/*     */   }
/*     */   
/*     */   protected MerchantRecipe createMerchantRecipe(ItemStack item, int emeraldCost) {
/*  76 */     if (emeraldCost <= 64)
/*  77 */       return new MerchantRecipe(createEmerald(emeraldCost), ItemStack.EMPTY, item, 0, 99999); 
/*  78 */     if (emeraldCost % 9 == 0) {
/*  79 */       return new MerchantRecipe(new ItemStack(Item.getItemFromBlock(Blocks.EMERALD_BLOCK), emeraldCost / 9), ItemStack.EMPTY, item, 0, 99999);
/*     */     }
/*  81 */     return new MerchantRecipe(new ItemStack(Item.getItemFromBlock(Blocks.EMERALD_BLOCK), emeraldCost / 9), createEmerald(emeraldCost % 9), item, 0, 99999);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canConvertProfession(ProfessionType pt) {
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addVillagerPosition() {}
/*     */ 
/*     */   
/*     */   public void setCustomer(@Nullable EntityPlayer player) {
/*  95 */     this.buyingPlayer = player;
/*  96 */     this.buyingList = null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public EntityPlayer getCustomer() {
/* 102 */     return this.buyingPlayer;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTrading() {
/* 107 */     return (this.buyingPlayer != null);
/*     */   }
/*     */   
/*     */   public boolean isSleepingTime() {
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bedCheck() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHunger() {
/* 121 */     return getMaxHunger();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MerchantRecipeList getRecipes(EntityPlayer player) {
/* 127 */     if (this.buyingList == null)
/*     */     {
/* 129 */       populateBuyingList();
/*     */     }
/*     */     
/* 132 */     return this.buyingList;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean processInteract(EntityPlayer player, EnumHand hand) {
/* 137 */     ItemStack itemstack = player.getHeldItem(hand);
/* 138 */     boolean flag = (itemstack.getItem() == Items.NAME_TAG);
/*     */     
/* 140 */     if (flag) {
/*     */       
/* 142 */       itemstack.interactWithEntity(player, (EntityLivingBase)this, hand);
/*     */     }
/* 144 */     else if (isEntityAlive() && !isTrading() && !isChild() && !player.isSneaking()) {
/*     */       
/* 146 */       if (this.buyingList == null)
/*     */       {
/* 148 */         populateBuyingList();
/*     */       }
/*     */       
/* 151 */       if (this.buyingList != null) {
/* 152 */         if (!this.world.isRemote && !this.buyingList.isEmpty()) {
/* 153 */           setCustomer(player);
/* 154 */           getNavigator().clearPath();
/* 155 */           player.displayVillagerTradeGui(this);
/* 156 */         } else if (this.buyingList.isEmpty()) {
/* 157 */           return super.processInteract(player, hand);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 162 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void setRecipes(@Nullable MerchantRecipeList recipeList) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public World getWorld() {
/* 174 */     return this.world;
/*     */   }
/*     */ 
/*     */   
/*     */   public void useRecipe(MerchantRecipe recipe) {
/* 179 */     recipe.incrementToolUses();
/* 180 */     this.livingSoundTime = -getTalkInterval();
/* 181 */     playSound(SoundEvents.ENTITY_VILLAGER_YES, getSoundVolume(), getSoundPitch());
/* 182 */     setHappy(getMaxHappy());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void verifySellingItem(ItemStack stack) {
/* 188 */     if (!this.world.isRemote && this.livingSoundTime > -getTalkInterval() + 20) {
/*     */       
/* 190 */       this.livingSoundTime = -getTalkInterval();
/* 191 */       playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, getSoundVolume(), getSoundPitch());
/*     */     } 
/*     */   }
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
/*     */   
/*     */   public ITextComponent getDisplayName() {
/* 206 */     TextComponentTranslation textComponentTranslation = new TextComponentTranslation(getTranslationKey(), new Object[0]);
/* 207 */     textComponentTranslation.getStyle().setHoverEvent(getHoverEvent());
/* 208 */     textComponentTranslation.getStyle().setInsertion(getCachedUniqueIdString());
/*     */     
/* 210 */     return (ITextComponent)textComponentTranslation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos getPos() {
/* 217 */     return new BlockPos((Entity)this);
/*     */   }
/*     */   
/*     */   protected abstract void populateBuyingList();
/*     */   
/*     */   protected abstract String getTranslationKey();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityVendor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */