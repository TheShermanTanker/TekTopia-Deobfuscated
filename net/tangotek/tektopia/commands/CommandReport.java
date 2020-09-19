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
/*    */ 
/*    */ 
/*    */ 
/*    */ class CommandReport
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandReport() {
/* 21 */     super("report");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 29 */     if (args.length > 1) {
/* 30 */       throw new WrongUsageException("commands.village.report.usage", new Object[0]);
/*    */     }
/* 32 */     String reportType = "all";
/* 33 */     if (args.length == 1) {
/* 34 */       reportType = args[0];
/*    */     }
/* 36 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 37 */     VillageManager.get(((EntityPlayer)entityPlayerMP).world).villageReport(reportType);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandReport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */