/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.passive.EntityAnimal;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.tangotek.tektopia.ModEntities;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.VillageManager;
/*    */ import net.tangotek.tektopia.entities.EntityVillagerTek;
/*    */ import net.tangotek.tektopia.structures.VillageStructure;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CommandHunger
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandHunger() {
/* 24 */     super("hunger");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 33 */     if (args.length > 2)
/*    */     {
/* 35 */       throw new WrongUsageException("commands.village.hunger.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/*    */     try {
/* 40 */       int hungerMod = Integer.parseInt(args[0]);
/* 41 */       int ticks = 1;
/*    */       
/* 43 */       if (args.length > 1) {
/* 44 */         ticks = Integer.parseInt(args[1]);
/*    */       }
/* 46 */       EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 47 */       List<EntityVillagerTek> villagers = ((EntityPlayer)entityPlayerMP).world.getEntitiesWithinAABB(EntityVillagerTek.class, entityPlayerMP.getEntityBoundingBox().grow(12.0D, 12.0D, 12.0D));
/* 48 */       for (EntityVillagerTek villager : villagers) {
/* 49 */         villager.modifyHungerDelay(hungerMod, ticks);
/*    */       }
/*    */       
/* 52 */       VillageManager vm = VillageManager.get(((EntityPlayer)entityPlayerMP).world);
/* 53 */       Village village = vm.getVillageAt(entityPlayerMP.getPosition());
/* 54 */       if (village != null) {
/* 55 */         VillageStructure struct = village.getStructure(entityPlayerMP.getPosition());
/* 56 */         if (struct != null) {
/* 57 */           List<EntityAnimal> animals = struct.getEntitiesInside(EntityAnimal.class);
/* 58 */           animals.forEach(a -> ModEntities.modifyAnimalHunger(a, hungerMod));
/*    */         }
/*    */       
/*    */       }
/*    */     
/* 63 */     } catch (NumberFormatException ex) {
/* 64 */       throw new WrongUsageException("commands.village.hunger.usage", new Object[0]);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandHunger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */