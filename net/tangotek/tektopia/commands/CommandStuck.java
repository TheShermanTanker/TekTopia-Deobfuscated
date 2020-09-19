/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.tangotek.tektopia.VillageManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ class CommandStuck
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandStuck() {
/* 18 */     super("stuck");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 27 */     if (args.length > 0)
/*    */     {
/* 29 */       throw new WrongUsageException("commands.village.stuck.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 33 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 34 */     BlockPos stuckPos = VillageManager.get(((EntityPlayer)entityPlayerMP).world).getLastStuck();
/* 35 */     if (stuckPos != null && entityPlayerMP instanceof EntityPlayerMP)
/* 36 */       entityPlayerMP.connection.setPlayerLocation(stuckPos.getX(), stuckPos.getY(), stuckPos.getZ(), 0.0F, 0.0F); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandStuck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */