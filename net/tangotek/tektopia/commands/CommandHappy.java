/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ 
/*    */ 
/*    */ 
/*    */ class CommandHappy
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandHappy() {
/* 18 */     super("happy");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 24 */     int happyMod = 0;
/* 25 */     if (args.length > 2)
/*    */     {
/* 27 */       throw new WrongUsageException("commands.village.happy.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 32 */       happyMod = Integer.parseInt(args[0]);
/* 33 */       int ticks = 1;
/*    */       
/* 35 */       if (args.length > 1) {
/* 36 */         ticks = Integer.parseInt(args[1]);
/*    */       }
/* 38 */       EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 39 */       List<EntityVillagerTek> villagers = ((EntityPlayer)entityPlayerMP).world.getEntitiesWithinAABB(EntityVillagerTek.class, entityPlayerMP.getEntityBoundingBox().grow(12.0D, 12.0D, 12.0D));
/* 40 */       for (EntityVillagerTek villager : villagers) {
/* 41 */         villager.modifyHappyDelay(happyMod, ticks);
/*    */       }
/* 43 */     } catch (NumberFormatException ex) {
/* 44 */       throw new WrongUsageException("commands.village.happy.usage", new Object[0]);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandHappy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */