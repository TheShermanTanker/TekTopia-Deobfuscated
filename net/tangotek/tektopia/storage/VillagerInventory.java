/*     */ package net.tangotek.tektopia.storage;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.inventory.InventoryBasic;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.Tuple;
/*     */ import net.minecraft.util.text.ITextComponent;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import net.tangotek.tektopia.ModItems;
/*     */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*     */ 
/*     */ public class VillagerInventory
/*     */   extends InventoryBasic
/*     */ {
/*     */   private final EntityVillagerTek owner;
/*     */   
/*     */   public VillagerInventory(EntityVillagerTek villager, String title, boolean customName, int slotCount) {
/*  27 */     super(title, customName, slotCount);
/*  28 */     this.owner = villager;
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public VillagerInventory(ITextComponent title, int slotCount) {
/*  33 */     this((EntityVillagerTek)null, title.getUnformattedText(), true, slotCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ItemStack> removeItems(Function<ItemStack, Integer> func, int itemCount) {
/*  41 */     return findItems(func, itemCount, true);
/*     */   }
/*     */   
/*     */   public List<ItemStack> removeItems(Predicate<ItemStack> pred, int itemCount) {
/*  45 */     return findItems(p -> Integer.valueOf(pred.test(p) ? 1 : 0), itemCount, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ItemStack> getItems(Predicate<ItemStack> pred, int itemCount) {
/*  53 */     return findItems(p -> Integer.valueOf(pred.test(p) ? 1 : 0), itemCount, false);
/*     */   }
/*     */   
/*     */   public ItemStack getItem(Function<ItemStack, Integer> func) {
/*  57 */     List<ItemStack> items = findItems(func, 1, false);
/*  58 */     if (!items.isEmpty()) {
/*  59 */       return items.get(0);
/*     */     }
/*  61 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public List<ItemStack> getItems(Function<ItemStack, Integer> func, int itemCount) {
/*  65 */     return findItems(func, itemCount, false);
/*     */   }
/*     */   
/*     */   public List<ItemStack> getItemsByStack(Function<ItemStack, Integer> func, int itemCount) {
/*  69 */     return findItems(func, itemCount, false);
/*     */   }
/*     */   
/*     */   public int getItemCount(Predicate<ItemStack> pred) {
/*  73 */     return getItemCount(item -> Integer.valueOf(pred.test(item) ? 1 : -1));
/*     */   }
/*     */   
/*     */   public int getItemCount(Function<ItemStack, Integer> func) {
/*  77 */     List<ItemStack> itemList = getItems(func, 0);
/*  78 */     int count = 0;
/*  79 */     for (ItemStack itemStack : itemList) {
/*  80 */       count += itemStack.getCount();
/*     */     }
/*  82 */     return count;
/*     */   }
/*     */   
/*     */   public void cloneFrom(VillagerInventory other) {
/*  86 */     for (int i = 0; i < other.getSizeInventory(); i++) {
/*  87 */       addItem(other.getStackInSlot(i));
/*     */     }
/*     */   }
/*     */   
/*     */   public static int countItems(List<ItemStack> items) {
/*  92 */     return items.stream().mapToInt(ItemStack::func_190916_E).sum();
/*     */   }
/*     */   
/*     */   private List<ItemStack> findItems(Function<ItemStack, Integer> func, int itemCount, boolean remove) {
/*  96 */     ArrayList<ItemStack> outList = new ArrayList<>();
/*  97 */     int needed = (itemCount > 0) ? itemCount : Integer.MAX_VALUE;
/*  98 */     List<Tuple<ItemStack, Integer>> matchedItems = new ArrayList<>(); int i;
/*  99 */     for (i = 0; i < getSizeInventory(); i++) {
/* 100 */       ItemStack itemStack = getStackInSlot(i);
/* 101 */       if (ModItems.canVillagerSee(itemStack) && (
/* 102 */         (Integer)func.apply(itemStack)).intValue() > 0) {
/* 103 */         matchedItems.add(new Tuple(itemStack, Integer.valueOf(i)));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 109 */     matchedItems.sort(Comparator.comparing(pair -> (Integer)func.apply(pair.getFirst())));
/*     */     
/* 111 */     for (i = matchedItems.size() - 1; i >= 0 && needed > 0; i--) {
/* 112 */       Tuple<ItemStack, Integer> pair = matchedItems.get(i);
/* 113 */       ItemStack itemStack = (ItemStack)pair.getFirst();
/* 114 */       int slot = ((Integer)pair.getSecond()).intValue();
/*     */       
/* 116 */       if (remove) {
/* 117 */         if (itemStack.getCount() <= needed) {
/* 118 */           outList.add(itemStack);
/* 119 */           setInventorySlotContents(slot, ItemStack.EMPTY);
/* 120 */           needed -= itemStack.getCount();
/*     */         } else {
/*     */           
/* 123 */           itemStack.shrink(needed);
/* 124 */           ItemStack newItem = itemStack.copy();
/* 125 */           newItem.setCount(needed);
/* 126 */           outList.add(newItem);
/* 127 */           needed = 0;
/* 128 */           onInventoryUpdated(newItem);
/*     */         } 
/*     */       } else {
/*     */         
/* 132 */         outList.add(itemStack);
/* 133 */         needed -= itemStack.getCount();
/*     */       } 
/*     */     } 
/*     */     
/* 137 */     return outList;
/*     */   }
/*     */   
/*     */   private void onInventoryUpdated(ItemStack itemStack) {
/* 141 */     if (this.owner != null && !this.owner.world.isRemote) {
/* 142 */       this.owner.onInventoryUpdated(itemStack);
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteOverstock(Item itemOuter, int max) {
/* 147 */     deleteOverstock(p -> (p.getItem() == itemOuter), max);
/*     */   }
/*     */   
/*     */   public void deleteOverstock(Predicate<ItemStack> predicate, int max) {
/* 151 */     int count = 0;
/* 152 */     for (int i = 0; i < getSizeInventory(); i++) {
/* 153 */       ItemStack itemStack = getStackInSlot(i);
/*     */       
/* 155 */       if (predicate.test(itemStack)) {
/* 156 */         if (count >= max) {
/* 157 */           setInventorySlotContents(i, ItemStack.EMPTY);
/*     */         }
/* 159 */         else if (count + itemStack.getCount() > max) {
/* 160 */           itemStack.setCount(max - count);
/* 161 */           onInventoryUpdated(itemStack);
/* 162 */           count = max;
/*     */         } else {
/*     */           
/* 165 */           count += itemStack.getCount();
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasSlotFree() {
/* 172 */     for (int i = 0; i < getSizeInventory(); i++) {
/* 173 */       ItemStack itemStack = getStackInSlot(i);
/* 174 */       if (itemStack.isEmpty()) {
/* 175 */         return true;
/*     */       }
/*     */     } 
/* 178 */     return false;
/*     */   }
/*     */   
/*     */   public void mergeItems(VillagerInventory other) {
/* 182 */     for (int i = 0; i < other.getSizeInventory(); i++) {
/* 183 */       ItemStack itemStack = other.getStackInSlot(i);
/* 184 */       if (itemStack.isEmpty()) {
/* 185 */         addItem(itemStack);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack addItem(ItemStack stack) {
/* 193 */     ItemStack newItem = stack.copy();
/* 194 */     int emptySlot = -1;
/* 195 */     for (int i = 0; i < getSizeInventory(); i++) {
/*     */       
/* 197 */       ItemStack oldItem = getStackInSlot(i);
/*     */       
/* 199 */       if (oldItem.isEmpty() && emptySlot < 0) {
/*     */         
/* 201 */         emptySlot = i;
/*     */       }
/* 203 */       else if (areItemsStackable(oldItem, newItem)) {
/*     */         
/* 205 */         int j = Math.min(getInventoryStackLimit(), oldItem.getMaxStackSize());
/* 206 */         int k = Math.min(newItem.getCount(), j - oldItem.getCount());
/*     */         
/* 208 */         if (k > 0) {
/*     */           
/* 210 */           oldItem.grow(k);
/* 211 */           newItem.shrink(k);
/* 212 */           if (newItem.isEmpty()) {
/*     */             
/* 214 */             onInventoryUpdated(oldItem);
/* 215 */             markDirty();
/* 216 */             return ItemStack.EMPTY;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 222 */     if (emptySlot >= 0 && !newItem.isEmpty()) {
/* 223 */       setInventorySlotContents(emptySlot, newItem);
/* 224 */       return ItemStack.EMPTY;
/*     */     } 
/*     */ 
/*     */     
/* 228 */     if (newItem.getCount() != stack.getCount())
/*     */     {
/* 230 */       markDirty();
/*     */     }
/*     */     
/* 233 */     return newItem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack removeStackFromSlot(int index) {
/* 240 */     ItemStack itemStack = super.removeStackFromSlot(index);
/* 241 */     if (!itemStack.isEmpty()) {
/* 242 */       onInventoryUpdated(itemStack);
/*     */     }
/*     */     
/* 245 */     return itemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInventorySlotContents(int index, ItemStack stack) {
/* 250 */     ItemStack oldItem = getStackInSlot(index);
/* 251 */     super.setInventorySlotContents(index, stack);
/*     */     
/* 253 */     if (!oldItem.isEmpty()) {
/* 254 */       onInventoryUpdated(oldItem);
/*     */     }
/*     */     
/* 257 */     onInventoryUpdated(stack);
/*     */   }
/*     */   
/*     */   public static boolean areItemsStackable(ItemStack itemStack1, ItemStack itemStack2) {
/* 261 */     if (ItemStack.areItemsEqual(itemStack1, itemStack2) && 
/* 262 */       ItemStack.areItemStackTagsEqual(itemStack1, itemStack2)) {
/* 263 */       return true;
/*     */     }
/*     */     
/* 266 */     return false;
/*     */   }
/*     */   
/*     */   public void debugSpam() {
/* 270 */     for (int i = 0; i < getSizeInventory(); i++) {
/* 271 */       ItemStack itemStack = getStackInSlot(i);
/* 272 */       if (!itemStack.isEmpty())
/* 273 */         System.out.println("    Item: " + itemStack.toString()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writeNBT(NBTTagCompound compound) {
/* 278 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 280 */     for (int i = 0; i < getSizeInventory(); i++) {
/*     */       
/* 282 */       ItemStack itemstack = getStackInSlot(i);
/*     */       
/* 284 */       if (!itemstack.isEmpty())
/*     */       {
/* 286 */         nbttaglist.appendTag((NBTBase)itemstack.writeToNBT(new NBTTagCompound()));
/*     */       }
/*     */     } 
/*     */     
/* 290 */     compound.setTag("Inventory", (NBTBase)nbttaglist);
/*     */   }
/*     */   
/*     */   public void readNBT(NBTTagCompound compound) {
/* 294 */     clear();
/* 295 */     NBTTagList nbttaglist = compound.getTagList("Inventory", 10);
/*     */     
/* 297 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */       
/* 299 */       ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));
/*     */       
/* 301 */       if (!itemstack.isEmpty())
/*     */       {
/* 303 */         addItem(itemstack);
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\storage\VillagerInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */