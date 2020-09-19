/*     */ package net.tangotek.tektopia.entities;
/*     */ import com.leviathanstudio.craftstudio.common.animation.AnimationHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.ListIterator;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.IMerchant;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.entity.passive.EntityChicken;
/*     */ import net.minecraft.entity.passive.EntityCow;
/*     */ import net.minecraft.entity.passive.EntityPig;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.nbt.NBTTagString;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.text.ITextComponent;
/*     */ import net.minecraft.util.text.TextComponentTranslation;
/*     */ import net.minecraft.village.MerchantRecipe;
/*     */ import net.minecraft.village.MerchantRecipeList;
/*     */ import net.minecraft.world.World;
/*     */ import net.tangotek.tektopia.Village;
/*     */ import net.tangotek.tektopia.VillagerRole;
/*     */ import net.tangotek.tektopia.caps.IVillageData;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIGenericMove;
/*     */ import net.tangotek.tektopia.entities.ai.EntityAIVisitMerchantStall;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ import net.tangotek.tektopia.tickjob.TickJob;
/*     */ 
/*     */ public class EntityMerchant extends EntityVillagerTek implements IMerchant {
/*  39 */   protected static AnimationHandler animHandler = TekVillager.getNewAnimationHandler(EntityMerchant.class);
/*     */   private BlockPos firstCheck;
/*  41 */   private int stallLevel = 0;
/*     */   
/*     */   @Nullable
/*     */   private EntityPlayer buyingPlayer;
/*     */   
/*     */   @Nullable
/*     */   private MerchantRecipeList merchantList;
/*     */   
/*  49 */   private List<EntityAnimal> animalDeliveries = new ArrayList<>();
/*     */   
/*     */   static {
/*  52 */     EntityVillagerTek.setupAnimations(animHandler, "merchant_m");
/*     */   }
/*     */   
/*     */   public EntityMerchant(World worldIn) {
/*  56 */     super(worldIn, (ProfessionType)null, VillagerRole.VENDOR.value | VillagerRole.VISITOR.value);
/*  57 */     this.sleepOffset = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setupServerJobs() {
/*  62 */     super.setupServerJobs();
/*  63 */     addJob(new TickJob(50, 50, true, () -> releashAnimals()));
/*  64 */     addJob(new TickJob(100, 0, false, () -> prepStuck()));
/*  65 */     addJob(new TickJob(400, 0, false, () -> checkStuck()));
/*     */ 
/*     */     
/*  68 */     addJob(new TickJob(50, 0, true, () -> { if (isSleepingTime())
/*  69 */               setDead();  })); addJob(new TickJob(300, 100, true, () -> {
/*     */             if (!hasVillage() || !getVillage().isValid()) {
/*     */               debugOut("Killing Self.  No village");
/*     */               setDead();
/*     */             } 
/*     */           }));
/*     */   }
/*     */   
/*     */   private void prepStuck() {
/*  78 */     this.firstCheck = getPos();
/*     */   }
/*     */   
/*     */   private void checkStuck() {
/*  82 */     if (this.firstCheck.distanceSq((Vec3i)getPos()) < 20.0D) {
/*  83 */       debugOut("Merchant failed to find a way to the village.");
/*  84 */       setDead();
/*     */     } 
/*     */   }
/*     */   
/*     */   public AnimationHandler getAnimationHandler() {
/*  89 */     return animHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initEntityAI() {
/*  94 */     super.initEntityAI();
/*     */ 
/*     */     
/*  97 */     addTask(50, (EntityAIBase)new EntityAIGenericMove(this, p -> (p.hasVillage() && Village.isNightTime(this.world)), v -> this.village.getEdgeNode(), EntityVillagerTek.MovementMode.WALK, null, () -> {
/*     */             debugOut("Killing Self.  Left the village");
/*     */             
/*     */             setDead();
/*     */           }));
/* 102 */     addTask(50, (EntityAIBase)new EntityAIVisitMerchantStall(this, p -> hasVillage(), 3, 60));
/*     */     
/* 104 */     addTask(50, (EntityAIBase)new EntityAIGenericMove(this, p -> (!Village.isNightTime(this.world) && p.hasVillage() && !isTrading()), v -> this.animalDeliveries.isEmpty() ? this.village.getLastVillagerPos() : getDeliveryPos(), EntityVillagerTek.MovementMode.WALK, null, () -> this.animalDeliveries.clear()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initEntityAIBase() {}
/*     */ 
/*     */   
/*     */   public boolean canNavigate() {
/* 112 */     if (isTrading()) {
/* 113 */       return false;
/*     */     }
/* 115 */     return super.canNavigate();
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigate createNavigator(World worldIn) {
/* 120 */     PathNavigate nav = super.createNavigator(worldIn);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     return nav;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAIMoveSpeed() {
/* 131 */     return super.getAIMoveSpeed() * 0.9F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 136 */     super.onLivingUpdate();
/*     */     
/* 138 */     if (!this.world.isRemote) {
/* 139 */       ListIterator<EntityAnimal> itr = this.animalDeliveries.listIterator();
/* 140 */       while (itr.hasNext()) {
/* 141 */         EntitySheep entitySheep; EntityAnimal animal = itr.next();
/* 142 */         if (!animal.isEntityAlive()) {
/* 143 */           EntityCow entityCow; itr.remove();
/* 144 */           if (animal instanceof EntityCow)
/* 145 */           { entityCow = new EntityCow(this.world); }
/* 146 */           else { EntityChicken entityChicken; if (entityCow instanceof EntityChicken) {
/* 147 */               entityChicken = new EntityChicken(this.world);
/* 148 */             } else if (entityChicken instanceof EntitySheep) {
/* 149 */               entitySheep = new EntitySheep(this.world);
/* 150 */             } else if (entitySheep instanceof EntityPig) {
/* 151 */               EntityPig entityPig = new EntityPig(this.world);
/*     */             } else {
/* 153 */               entitySheep = null;
/*     */             }  }
/* 155 */            if (entitySheep != null) {
/* 156 */             itr.add(entitySheep);
/* 157 */             spawnAnimal((EntityAnimal)entitySheep);
/*     */           }  continue;
/* 159 */         }  if (entitySheep.getLeashHolder() == null) {
/* 160 */           teleportAnimal((EntityAnimal)entitySheep);
/* 161 */           entitySheep.setLeashHolder((Entity)this, true); continue;
/* 162 */         }  if (entitySheep.getLeashHolder() != this) {
/*     */           
/* 164 */           itr.remove(); continue;
/* 165 */         }  if (entitySheep.getDistanceSq((Entity)this) > 40.0D) {
/* 166 */           teleportAnimal((EntityAnimal)entitySheep);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addTask(int priority, EntityAIBase task) {
/* 175 */     if (task instanceof net.tangotek.tektopia.entities.ai.EntityAIWanderStructure && priority <= 100) {
/*     */       return;
/*     */     }
/* 178 */     if (task instanceof net.tangotek.tektopia.entities.ai.EntityAIReadBook) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 187 */     super.addTask(priority, task);
/*     */   }
/*     */ 
/*     */   
/*     */   private void releashAnimals() {
/* 192 */     for (EntityAnimal animal : this.animalDeliveries) {
/* 193 */       if (animal.isEntityAlive()) {
/* 194 */         animal.setLeashHolder(null, true);
/* 195 */         animal.setLeashHolder((Entity)this, true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMale() {
/* 202 */     return true;
/*     */   }
/*     */   
/*     */   private void teleportAnimal(EntityAnimal animal) {
/* 206 */     animal.setPositionAndUpdate(getX(), getY(), getZ());
/*     */   }
/*     */   
/*     */   public void addAnimalDelivery(EntityAnimal animal) {
/* 210 */     this.animalDeliveries.add(animal);
/* 211 */     spawnAnimal(animal);
/*     */   }
/*     */   
/*     */   private BlockPos getDeliveryPos() {
/* 215 */     if (this.animalDeliveries.size() > 0) {
/* 216 */       VillageStructure struct = EntityAIReturnLostAnimal.getDestinationStructure(getVillage(), this.animalDeliveries.get(0), getPosition());
/* 217 */       if (struct != null) {
/* 218 */         return struct.getDoorOutside();
/*     */       }
/*     */     } 
/* 221 */     return getVillage().getOrigin();
/*     */   }
/*     */   
/*     */   private void spawnAnimal(EntityAnimal animal) {
/* 225 */     animal.setLocationAndAngles(getX() + 0.5D, getY(), getZ() + 0.5D, 0.0F, 0.0F);
/* 226 */     animal.onInitialSpawn(this.world.getDifficultyForLocation(getPos()), (IEntityLivingData)null);
/* 227 */     this.world.spawnEntity((Entity)animal);
/* 228 */     ModEntities.makeTaggedEntity((Entity)animal, EntityTagType.VILLAGER);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addVillagerPosition() {}
/*     */ 
/*     */   
/*     */   public void setStall(int level) {
/* 236 */     this.stallLevel = level;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomer(@Nullable EntityPlayer player) {
/* 241 */     this.buyingPlayer = player;
/* 242 */     getNavigator().clearPath();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public EntityPlayer getCustomer() {
/* 248 */     return this.buyingPlayer;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTrading() {
/* 253 */     return (this.buyingPlayer != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bedCheck() {}
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MerchantRecipeList getRecipes(EntityPlayer player) {
/* 264 */     if (this.merchantList == null)
/*     */     {
/* 266 */       populateBuyingList(this.stallLevel);
/*     */     }
/*     */     
/* 269 */     return this.merchantList;
/*     */   }
/*     */   protected boolean getCanUseDoors() {
/* 272 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean processInteract(EntityPlayer player, EnumHand hand) {
/* 277 */     if (isEntityAlive() && !isTrading() && !isChild() && !player.isSneaking())
/*     */     {
/* 279 */       if (!this.world.isRemote) {
/* 280 */         if (this.merchantList == null) {
/* 281 */           populateBuyingList(this.stallLevel);
/*     */         }
/*     */         
/* 284 */         if (this.merchantList != null && 
/* 285 */           !this.merchantList.isEmpty()) {
/* 286 */           setCustomer(player);
/* 287 */           player.displayVillagerTradeGui(this);
/* 288 */           getNavigator().clearPath();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 295 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void populateBuyingList(int stallLevel) {
/* 300 */     if (this.merchantList == null && hasVillage()) {
/*     */       
/* 302 */       IVillageData vd = getVillage().getTownData();
/* 303 */       if (vd != null) {
/* 304 */         vd.initEconomy();
/* 305 */         this.merchantList = vd.getEconomy().getMerchantList(getVillage(), stallLevel);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void setRecipes(@Nullable MerchantRecipeList recipeList) {}
/*     */ 
/*     */   
/*     */   public World getWorld() {
/* 317 */     return this.world;
/*     */   }
/*     */ 
/*     */   
/*     */   public void useRecipe(MerchantRecipe recipe) {
/* 322 */     recipe.incrementToolUses();
/* 323 */     this.livingSoundTime = -getTalkInterval();
/* 324 */     playSound(SoundEvents.ENTITY_VILLAGER_YES, getSoundVolume(), getSoundPitch());
/* 325 */     int i = 3 + this.rand.nextInt(4);
/*     */     
/* 327 */     if (recipe.getToolUses() == 1 || this.rand.nextInt(5) == 0)
/*     */     {
/* 329 */       i += 5;
/*     */     }
/*     */     
/* 332 */     if (recipe.getRewardsExp())
/*     */     {
/* 334 */       this.world.spawnEntity((Entity)new EntityXPOrb(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
/*     */     }
/*     */     
/* 337 */     if (hasVillage()) {
/* 338 */       getVillage().purchaseFromMerchant(recipe, this, getCustomer());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void verifySellingItem(ItemStack stack) {
/* 344 */     if (!this.world.isRemote && this.livingSoundTime > -getTalkInterval() + 20) {
/*     */       
/* 346 */       this.livingSoundTime = -getTalkInterval();
/* 347 */       playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, getSoundVolume(), getSoundPitch());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ITextComponent getDisplayName() {
/* 354 */     TextComponentTranslation textComponentTranslation = new TextComponentTranslation("entity.villager.merchant", new Object[0]);
/* 355 */     textComponentTranslation.getStyle().setHoverEvent(getHoverEvent());
/* 356 */     textComponentTranslation.getStyle().setInsertion(getCachedUniqueIdString());
/*     */ 
/*     */ 
/*     */     
/* 360 */     return (ITextComponent)textComponentTranslation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Predicate<Entity> isHostile() {
/* 365 */     return e -> false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFleeFrom(Entity e) {
/* 370 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos getPos() {
/* 376 */     return new BlockPos((Entity)this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound compound) {
/* 382 */     super.writeEntityToNBT(compound);
/*     */     
/* 384 */     NBTTagList nbttaglist = new NBTTagList();
/* 385 */     for (EntityAnimal animal : this.animalDeliveries) {
/* 386 */       nbttaglist.appendTag((NBTBase)new NBTTagString(animal.getUniqueID().toString()));
/*     */     }
/* 388 */     compound.setTag("animals", (NBTBase)nbttaglist);
/*     */     
/* 390 */     if (this.merchantList != null)
/*     */     {
/* 392 */       compound.setTag("Offers", (NBTBase)this.merchantList.getRecipiesAsTags());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound compound) {
/* 399 */     super.readEntityFromNBT(compound);
/*     */     
/* 401 */     NBTTagList nbttaglist = compound.getTagList("animals", 10);
/* 402 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/* 404 */       UUID uuid = UUID.fromString(nbttaglist.getStringTagAt(i));
/* 405 */       for (EntityAnimal animal : this.world.getEntitiesWithinAABB(EntityAnimal.class, getEntityBoundingBox().grow(12.0D))) {
/*     */         
/* 407 */         if (animal.getUniqueID().equals(uuid)) {
/*     */           
/* 409 */           this.animalDeliveries.add(animal);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 415 */     if (compound.hasKey("Offers", 10)) {
/*     */       
/* 417 */       NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
/* 418 */       this.merchantList = new MerchantRecipeList(nbttagcompound);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\entities\EntityMerchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */