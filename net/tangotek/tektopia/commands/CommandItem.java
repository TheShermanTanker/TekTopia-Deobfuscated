/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import net.tangotek.tektopia.ItemTagType;
/*    */ import net.tangotek.tektopia.ModItems;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CommandItem
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandItem() {
/* 23 */     super("item");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 33 */     if (args.length > 0)
/*    */     {
/* 35 */       throw new WrongUsageException("commands.village.item.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 39 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 40 */     ModItems.makeTaggedItem(entityPlayerMP.getHeldItem(EnumHand.MAIN_HAND), ItemTagType.VILLAGER);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */