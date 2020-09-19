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
/*    */ class CommandEdgeNodes
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandEdgeNodes() {
/* 19 */     super("edges");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 28 */     boolean enable = false;
/* 29 */     if (args.length != 0)
/*    */     {
/* 31 */       throw new WrongUsageException("commands.village.edge.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 35 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 36 */     Village village = VillageManager.get(((EntityPlayer)entityPlayerMP).world).getNearestVillage(entityPlayerMP.getPosition(), 200);
/* 37 */     if (village != null)
/* 38 */       village.getPathingGraph().debugEdgeNodes(((EntityPlayer)entityPlayerMP).world); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandEdgeNodes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */