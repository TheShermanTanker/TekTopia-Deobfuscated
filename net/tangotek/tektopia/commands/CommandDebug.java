/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.tangotek.tektopia.VillageManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CommandDebug
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandDebug() {
/* 18 */     super("debug");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 27 */     boolean enable = false;
/* 28 */     if (args.length != 1)
/*    */     {
/* 30 */       throw new WrongUsageException("commands.village.debug.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 34 */     enable = Boolean.parseBoolean(args[0]);
/* 35 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 36 */     VillageManager.get(((EntityPlayer)entityPlayerMP).world).setDebugOn(enable);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandDebug.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */