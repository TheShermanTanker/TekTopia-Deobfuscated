/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import net.minecraft.command.ICommand;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraftforge.server.command.CommandTreeBase;
/*    */ import net.minecraftforge.server.command.CommandTreeHelp;
/*    */ import net.minecraftforge.server.permission.DefaultPermissionLevel;
/*    */ import net.minecraftforge.server.permission.PermissionAPI;
/*    */ 
/*    */ 
/*    */ public class VillageCommands
/*    */   extends CommandTreeBase
/*    */ {
/*    */   public VillageCommands() {
/* 16 */     addSubcommand((ICommand)new CommandStart());
/* 17 */     addSubcommand((ICommand)new CommandItem());
/* 18 */     addSubcommand((ICommand)new CommandAnimals());
/*    */     
/* 20 */     addSubcommand((ICommand)new CommandHappy());
/* 21 */     addSubcommand((ICommand)new CommandHunger());
/* 22 */     addSubcommand((ICommand)new CommandLevel());
/* 23 */     addSubcommand((ICommand)new CommandIntelligence());
/* 24 */     addSubcommand((ICommand)new CommandReport());
/* 25 */     addSubcommand((ICommand)new CommandNearby());
/* 26 */     addSubcommand((ICommand)new CommandTreeHelp(this));
/* 27 */     addSubcommand((ICommand)new CommandDebug());
/* 28 */     addSubcommand((ICommand)new CommandEdgeNodes());
/*    */     
/* 30 */     addSubcommand((ICommand)new CommandKill());
/* 31 */     addSubcommand((ICommand)new CommandGoto());
/* 32 */     addSubcommand((ICommand)new CommandPopulate());
/* 33 */     addSubcommand((ICommand)new CommandRaid());
/* 34 */     addSubcommand((ICommand)new CommandGraph());
/*    */   }
/*    */   
/*    */   public static String commandPrefix() {
/* 38 */     return "tektopia.command.village.";
/*    */   }
/*    */   
/*    */   public void registerNodes() {
/* 42 */     getSubCommands().stream().forEach(c -> PermissionAPI.registerNode(commandPrefix() + c.getName(), DefaultPermissionLevel.OP, c.getName()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 51 */     return "village";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 66 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
/* 75 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage(ICommandSender icommandsender) {
/* 84 */     return "commands.village.usage";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\VillageCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */