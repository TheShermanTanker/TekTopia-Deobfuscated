/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommand;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.VillageManager;
/*    */ 
/*    */ class CommandNearby
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandNearby() {
/* 19 */     super("nearby");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 28 */     if (args.length > 0)
/*    */     {
/* 30 */       throw new WrongUsageException("commands.village.nearby.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 34 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 35 */     Integer range = Integer.valueOf(360);
/* 36 */     List<Village> villages = VillageManager.get(((EntityPlayer)entityPlayerMP).world).getVillagesNear(entityPlayerMP.getPosition(), range.intValue());
/* 37 */     if (villages.isEmpty()) {
/* 38 */       notifyCommandListener(sender, (ICommand)this, "commands.nearby.none", new Object[] { range });
/*    */     } else {
/*    */       
/* 41 */       BlockPos p = entityPlayerMP.getPosition();
/* 42 */       villages.stream().forEach(v -> notifyCommandListener(sender, (ICommand)this, "commands.nearby.set", new Object[] { range, v.getOrigin(), Double.valueOf(v.getOrigin().getDistance(p.getX(), p.getY(), p.getZ())) }));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandNearby.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */