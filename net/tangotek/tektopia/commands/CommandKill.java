/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*    */ 
/*    */ 
/*    */ 
/*    */ class CommandKill
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandKill() {
/* 18 */     super("kill");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 24 */     if (args.length > 0)
/*    */     {
/* 26 */       throw new WrongUsageException("commands.village.kill.usage", new Object[0]);
/*    */     }
/*    */     
/* 29 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 30 */     List<EntityVillageNavigator> villagers = ((EntityPlayer)entityPlayerMP).world.getEntitiesWithinAABB(EntityVillageNavigator.class, entityPlayerMP.getEntityBoundingBox().grow(200.0D, 200.0D, 200.0D));
/* 31 */     for (EntityVillageNavigator villager : villagers)
/* 32 */       villager.setDead(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandKill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */