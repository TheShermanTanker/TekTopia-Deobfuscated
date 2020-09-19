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
/*    */ class CommandIntelligence
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandIntelligence() {
/* 18 */     super("intelligence");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 27 */     int intelligenceMod = 0;
/* 28 */     if (args.length > 2)
/*    */     {
/* 30 */       throw new WrongUsageException("commands.village.intelligence.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 35 */       intelligenceMod = Integer.parseInt(args[0]);
/* 36 */       int ticks = 1;
/*    */       
/* 38 */       if (args.length > 1) {
/* 39 */         ticks = Integer.parseInt(args[1]);
/*    */       }
/* 41 */       EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 42 */       List<EntityVillagerTek> villagers = ((EntityPlayer)entityPlayerMP).world.getEntitiesWithinAABB(EntityVillagerTek.class, entityPlayerMP.getEntityBoundingBox().grow(6.0D, 4.0D, 6.0D));
/* 43 */       for (EntityVillagerTek villager : villagers) {
/* 44 */         villager.addIntelligenceDelay(intelligenceMod, ticks);
/*    */       }
/* 46 */     } catch (NumberFormatException ex) {
/* 47 */       throw new WrongUsageException("commands.village.intelligence.usage", new Object[0]);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandIntelligence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */