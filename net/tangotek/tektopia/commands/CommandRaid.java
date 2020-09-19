/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.VillageManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CommandRaid
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandRaid() {
/* 20 */     super("raid");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 30 */     if (args.length > 1)
/*    */     {
/* 32 */       throw new WrongUsageException("commands.village.raid.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 36 */     int raidLevel = 0;
/* 37 */     if (args.length == 1) {
/* 38 */       raidLevel = Integer.parseInt(args[0]);
/*    */     }
/* 40 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 41 */     VillageManager vm = VillageManager.get(((EntityPlayer)entityPlayerMP).world);
/* 42 */     Village village = vm.getVillageAt(entityPlayerMP.getPosition());
/* 43 */     if (village != null)
/* 44 */       village.forceRaid(raidLevel); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandRaid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */