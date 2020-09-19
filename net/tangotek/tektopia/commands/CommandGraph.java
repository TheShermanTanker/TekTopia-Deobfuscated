/*    */ package net.tangotek.tektopia.commands;
/*    */ 
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.command.WrongUsageException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*    */ import net.tangotek.tektopia.TekVillager;
/*    */ import net.tangotek.tektopia.Village;
/*    */ import net.tangotek.tektopia.VillageManager;
/*    */ import net.tangotek.tektopia.network.PacketPathingNode;
/*    */ 
/*    */ 
/*    */ class CommandGraph
/*    */   extends CommandVillageBase
/*    */ {
/*    */   public CommandGraph() {
/* 20 */     super("graph");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
/* 26 */     if (args.length > 1)
/*    */     {
/* 28 */       throw new WrongUsageException("commands.village.graph.usage", new Object[0]);
/*    */     }
/*    */     
/* 31 */     EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(sender);
/* 32 */     VillageManager vm = VillageManager.get(((EntityPlayer)entityPlayerMP).world);
/* 33 */     Village village = vm.getVillageAt(entityPlayerMP.getPosition());
/* 34 */     if (village != null && 
/* 35 */       entityPlayerMP instanceof EntityPlayerMP) {
/* 36 */       int mode = 1;
/* 37 */       if (args.length == 1) {
/* 38 */         mode = Integer.valueOf(args[0]).intValue();
/*    */       }
/*    */       
/* 41 */       EntityPlayerMP playerMP = entityPlayerMP;
/* 42 */       if (mode == 1) {
/* 43 */         village.getPathingGraph().addListener(playerMP);
/* 44 */       } else if (mode == 0) {
/* 45 */         village.getPathingGraph().removeListener(playerMP);
/* 46 */         TekVillager.NETWORK.sendTo((IMessage)new PacketPathingNode(null), playerMP);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\commands\CommandGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */