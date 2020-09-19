/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.passive.EntityAnimal;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.tangotek.tektopia.EntityTagType;
/*    */ import net.tangotek.tektopia.ModEntities;
/*    */ 
/*    */ 
/*    */ class CommandAnimals
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandAnimals() {
/* 20 */     super("animals");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 29 */     if (args.length > 0)
/*    */     {
/* 31 */       throw new WrongUsageException("commands.village.animals.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 35 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 36 */     List<EntityAnimal> animals = ((EntityPlayer)entityPlayerMP).world.getEntitiesWithinAABB(EntityAnimal.class, entityPlayerMP.getEntityBoundingBox().grow(15.0D, 16.0D, 15.0D));
/* 37 */     for (EntityAnimal animal : animals)
/* 38 */       ModEntities.makeTaggedEntity((Entity)animal, EntityTagType.VILLAGER); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandAnimals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */