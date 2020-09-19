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
/*    */ class CommandStructure
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandStructure() {
/* 20 */     super("structure");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 29 */     if (args.length > 0)
/*    */     {
/* 31 */       throw new WrongUsageException("commands.village.structure.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 35 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 36 */     ModItems.makeTaggedItem(entityPlayerMP.getHeldItem(EnumHand.MAIN_HAND), ItemTagType.STRUCTURE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandStructure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */