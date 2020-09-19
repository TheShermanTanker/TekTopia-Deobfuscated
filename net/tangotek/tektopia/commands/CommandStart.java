/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.init.SoundEvents;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.SoundCategory;
/*    */ import net.tangotek.tektopia.structures.VillageStructureType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CommandStart
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandStart() {
/* 29 */     super("start");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 35 */     if (args.length > 0)
/*    */     {
/* 37 */       throw new WrongUsageException("commands.village.start.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 41 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/*    */     
/* 43 */     boolean flag = ((EntityPlayer)entityPlayerMP).inventory.addItemStackToInventory(VillageStructureType.TOWNHALL.itemStack.copy());
/* 44 */     flag |= ((EntityPlayer)entityPlayerMP).inventory.addItemStackToInventory(VillageStructureType.STORAGE.itemStack.copy());
/*    */     
/* 46 */     if (flag) {
/*    */       
/* 48 */       ((EntityPlayer)entityPlayerMP).world.playSound((EntityPlayer)null, ((EntityPlayer)entityPlayerMP).posX, ((EntityPlayer)entityPlayerMP).posY, ((EntityPlayer)entityPlayerMP).posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((entityPlayerMP.getRNG().nextFloat() - entityPlayerMP.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
/* 49 */       ((EntityPlayer)entityPlayerMP).inventoryContainer.detectAndSendChanges();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandStart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */