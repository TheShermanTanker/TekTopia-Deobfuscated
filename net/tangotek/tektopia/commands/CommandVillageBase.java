/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.PlayerNotFoundException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraftforge.server.permission.PermissionAPI;
/*    */ 
/*    */ 
/*    */ abstract class CommandVillageBase
/*    */   extends CommandBase
/*    */ {
/*    */   protected final String name;
/*    */   
/*    */   public CommandVillageBase(String n) {
/* 21 */     this.name = n;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 30 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getAliases() {
/* 39 */     return Collections.singletonList(this.name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage(ICommandSender sender) {
/* 48 */     return "commands.tektopia." + this.name + ".usage";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
/*    */     try {
/* 55 */       return PermissionAPI.hasPermission((EntityPlayer)getCommandSenderAsPlayer(sender), VillageCommands.commandPrefix() + getName());
/* 56 */     } catch (PlayerNotFoundException e) {
/* 57 */       e.printStackTrace();
/*    */ 
/*    */       
/* 60 */       return false;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 70 */     return 4;
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
/*    */   public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
/* 82 */     return Collections.emptyList();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandVillageBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */