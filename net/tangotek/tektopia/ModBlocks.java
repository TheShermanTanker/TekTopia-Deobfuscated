/*    */ package net.tangotek.tektopia;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraftforge.event.world.BlockEvent;
/*    */ import net.minecraftforge.registries.IForgeRegistry;
/*    */ import net.minecraftforge.registries.IForgeRegistryEntry;
/*    */ import net.tangotek.tektopia.blocks.BlockChair;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModBlocks
/*    */ {
/*    */   public static BlockPos villagerBlockBreak;
/*    */   public static EntityVillagerTek villagerBreaker;
/* 20 */   public static BlockChair blockChair = new BlockChair("chair");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void register(IForgeRegistry<Block> registry) {
/* 28 */     System.out.println("Registering Blocks");
/* 29 */     registry.registerAll((IForgeRegistryEntry[])new Block[] { (Block)blockChair });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void registerItemBlocks(IForgeRegistry<Item> registry) {
/* 37 */     registry.registerAll((IForgeRegistryEntry[])new Item[] { blockChair
/* 38 */           .createItemBlock() });
/*    */   }
/*    */ 
/*    */   
/*    */   public static void registerModels() {
/* 43 */     blockChair.registerItemModel(Item.getItemFromBlock((Block)blockChair));
/*    */   }
/*    */ 
/*    */   
/*    */   public static void villagerDestroyBlock(BlockPos pos, EntityVillagerTek villager, boolean dropBlock, boolean makeVillagerItem) {
/* 48 */     if (dropBlock && makeVillagerItem) {
/* 49 */       villagerBlockBreak = pos;
/* 50 */       villagerBreaker = villager;
/*    */     } 
/* 52 */     villager.world.destroyBlock(pos, dropBlock);
/*    */   }
/*    */   
/*    */   public static void onHarvestDropsEvent(BlockEvent.HarvestDropsEvent event) {
/* 56 */     if (event.getHarvester() == null && event.getPos() == villagerBlockBreak) {
/* 57 */       for (ItemStack itemStack : event.getDrops()) {
/* 58 */         ModItems.makeTaggedItem(itemStack, ItemTagType.VILLAGER);
/*    */       }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 64 */       villagerBlockBreak = null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\ModBlocks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */