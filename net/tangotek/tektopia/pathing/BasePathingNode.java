/*     */ package net.tangotek.tektopia.pathing;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import net.tangotek.tektopia.TekVillager;
/*     */ import net.tangotek.tektopia.network.PacketPathingNode;
/*     */ import net.tangotek.tektopia.structures.VillageStructure;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasePathingNode
/*     */   extends PathingNode
/*     */ {
/*     */   private final byte clearanceHeight;
/*  23 */   private long updateTick = 0L;
/*     */ 
/*     */   
/*     */   public BasePathingNode(BlockPos bp, byte ch) {
/*  27 */     super(new PathingCell(bp, (byte)0));
/*  28 */     this.clearanceHeight = ch;
/*  29 */     this.updateTick = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   public byte getClearanceHeight() {
/*  33 */     return this.clearanceHeight;
/*     */   }
/*     */   
/*     */   public long getUpdateTick() {
/*  37 */     return this.updateTick;
/*     */   }
/*     */ 
/*     */   
/*     */   public int updateConnections(World world, PathingCellMap cellMap, PathingGraph graph) {
/*  42 */     this.updateTick = System.currentTimeMillis();
/*  43 */     checkConnection(world, cellMap, graph, 1, 0);
/*  44 */     checkConnection(world, cellMap, graph, -1, 0);
/*  45 */     checkConnection(world, cellMap, graph, 0, 1);
/*  46 */     checkConnection(world, cellMap, graph, 0, -1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     if (this.parent == null) {
/*  52 */       this.parent = new PathingNode(getCell().up());
/*  53 */       this.parent.addChild(this);
/*  54 */       graph.addLastNode(this.parent);
/*     */     } 
/*     */ 
/*     */     
/*  58 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean checkConnection(World world, PathingCellMap cellMap, PathingGraph graph, int x, int z) {
/*  63 */     if (!graph.isInRange(getBlockPos().add(x, 0, z))) {
/*  64 */       return false;
/*     */     }
/*  66 */     PathingNode connected = getConnection(x, z);
/*  67 */     if (connected == null) {
/*     */       
/*  69 */       boolean newNode = false;
/*  70 */       BasePathingNode node = getExistingNeighbor(cellMap, x, z);
/*  71 */       if (node == null) {
/*  72 */         node = checkWalkableNeighbor(world, x, z);
/*  73 */         if (node != null) {
/*  74 */           newNode = true;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       if (node != null && canWalkTo(node)) {
/*     */         
/*  84 */         connectNodes(this, node, graph);
/*     */ 
/*     */ 
/*     */         
/*  88 */         if (newNode)
/*     */         {
/*  90 */           graph.addFirstNode(node);
/*  91 */           cellMap.putNode(node, world);
/*  92 */           return true;
/*     */         }
/*     */       
/*     */       } 
/*     */     } else {
/*     */       
/*  98 */       checkParentLink(connected);
/*     */     } 
/*     */     
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void notifyListeners(World world, List<EntityPlayerMP> listeners) {
/* 106 */     listeners.forEach(p -> TekVillager.NETWORK.sendTo((IMessage)new PacketPathingNode(new PathingNodeClient(this)), p));
/*     */   }
/*     */   
/*     */   private BasePathingNode checkWalkableNeighbor(World world, int x, int z) {
/* 110 */     BlockPos bp = getBlockPos().add(x, 0, z);
/* 111 */     if (!canWalkOn(world, bp)) {
/* 112 */       bp = bp.down();
/* 113 */       if (!canWalkOn(world, bp)) {
/* 114 */         bp = bp.down();
/* 115 */         if (!canWalkOn(world, bp)) {
/* 116 */           bp = null;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 121 */     if (bp != null) {
/* 122 */       bp = bp.up();
/* 123 */       byte clearance = 0;
/* 124 */       if (isPassable(world, bp) && isPassable(world, bp.up(1))) {
/* 125 */         clearance = 2;
/* 126 */         if (isPassable(world, bp.up(2))) {
/* 127 */           clearance = (byte)(clearance + 1);
/*     */         }
/*     */       } 
/* 130 */       if (clearance >= 2) {
/* 131 */         return new BasePathingNode(bp, clearance);
/*     */       }
/*     */     } 
/* 134 */     return null;
/*     */   }
/*     */   
/*     */   public static boolean isPassable(World world, BlockPos bp) {
/* 138 */     IBlockState blockState = world.getBlockState(bp);
/* 139 */     if (blockState.getMaterial().isLiquid())
/* 140 */       return false; 
/* 141 */     if (blockState.getBlock().isPassable((IBlockAccess)world, bp))
/* 142 */       return true; 
/* 143 */     if (isPortal(world, bp)) {
/* 144 */       return true;
/*     */     }
/* 146 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isPortal(World world, BlockPos bp) {
/* 150 */     if (VillageStructure.isWoodDoor(world, bp))
/* 151 */       return true; 
/* 152 */     if (VillageStructure.isGate(world, bp)) {
/* 153 */       return true;
/*     */     }
/* 155 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean canWalkOn(World world, BlockPos bp) {
/* 159 */     if (!isPassable(world, bp)) {
/* 160 */       IBlockState blockState = world.getBlockState(bp);
/* 161 */       if (blockState.getMaterial().isLiquid()) {
/* 162 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 168 */       if (blockState.getBlock() instanceof net.minecraft.block.BlockFence || blockState.getBlock() instanceof net.minecraft.block.BlockWall) {
/* 169 */         return false;
/*     */       }
/*     */       
/* 172 */       return true;
/*     */     } 
/*     */     
/* 175 */     return false;
/*     */   }
/*     */   
/*     */   private boolean canWalkTo(BasePathingNode node) {
/* 179 */     if (((node.getCell()).y == (getCell()).y - 1 && node.getClearanceHeight() >= 3) || 
/* 180 */       (node.getCell()).y == (getCell()).y || (
/* 181 */       (node.getCell()).y == (getCell()).y + 1 && getClearanceHeight() >= 3)) {
/* 182 */       return true;
/*     */     }
/* 184 */     return false;
/*     */   }
/*     */   
/*     */   private BasePathingNode getExistingNeighbor(PathingCellMap cellMap, int x, int z) {
/* 188 */     return cellMap.getNodeYRange((getCell()).x + x, (getCell()).y - 1, (getCell()).y + 1, (getCell()).z + z);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\pathing\BasePathingNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */