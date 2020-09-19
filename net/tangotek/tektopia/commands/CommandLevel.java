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
/*    */ class CommandLevel
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandLevel() {
/* 18 */     super("level");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 28 */     if (args.length > 2)
/*    */     {
/* 30 */       throw new WrongUsageException("commands.village.level.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 35 */       int levelMod = Integer.parseInt(args[0]);
/* 36 */       EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 37 */       List<EntityVillagerTek> villagers = ((EntityPlayer)entityPlayerMP).world.getEntitiesWithinAABB(EntityVillagerTek.class, entityPlayerMP.getEntityBoundingBox().grow(3.0D, 3.0D, 3.0D));
/* 38 */       if (!villagers.isEmpty()) {
/* 39 */         for (int i = 0; i < levelMod; i++) {
/* 40 */           ((EntityVillagerTek)villagers.get(0)).incrementSkill(((EntityVillagerTek)villagers.get(0)).getProfessionType());
/*    */         }
/*    */       }
/* 43 */     } catch (NumberFormatException ex) {
/* 44 */       throw new WrongUsageException("commands.village.hunger.usage", new Object[0]);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */