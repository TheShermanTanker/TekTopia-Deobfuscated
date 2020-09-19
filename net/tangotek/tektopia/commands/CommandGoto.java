/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CommandGoto
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandGoto() {
/* 20 */     super("goto");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 29 */     if (args.length != 1)
/*    */     {
/* 31 */       throw new WrongUsageException("commands.village.goto.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 35 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 36 */     Entity e = ((EntityPlayer)entityPlayerMP).world.getEntityByID(Integer.valueOf(args[0]).intValue());
/* 37 */     if (e != null && entityPlayerMP instanceof EntityPlayerMP)
/* 38 */       entityPlayerMP.connection.setPlayerLocation(e.posX, e.posY, e.posZ, 0.0F, 0.0F); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandGoto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */