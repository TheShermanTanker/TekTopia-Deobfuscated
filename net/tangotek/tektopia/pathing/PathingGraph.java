/*     */ package net.tangotek.tektopia.pathing;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.tangotek.tektopia.Village;
/*     */ 
/*     */ public class PathingGraph {
/*     */   protected final World world;
/*     */   protected final Village village;
/*  16 */   private int nodesVerified = 0;
/*     */   
/*     */   private final PathingCellMap baseCellMap;
/*  19 */   private Deque<PathingNode> nodeProcessQueue = new LinkedList<>();
/*     */   private boolean initialQueueComplete = false;
/*  21 */   private List<EntityPlayerMP> listeners = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public PathingGraph(World worldIn, Village v) {
/*  25 */     this.world = worldIn;
/*  26 */     this.village = v;
/*     */     
/*  28 */     this.baseCellMap = new PathingCellMap(120);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nodeCount() {
/*  43 */     return this.baseCellMap.nodeCount();
/*     */   }
/*     */   
/*     */   public boolean isProcessing() {
/*  47 */     return (!this.nodeProcessQueue.isEmpty() || this.baseCellMap.nodeCount() <= 0);
/*     */   }
/*     */   
/*     */   public void addListener(EntityPlayerMP player) {
/*  51 */     this.listeners.add(player);
/*  52 */     this.baseCellMap.notifyListenerInitial(this.world, player);
/*     */   }
/*     */   
/*     */   public void removeListener(EntityPlayerMP player) {
/*  56 */     this.listeners.remove(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void seedVillage(BlockPos bp) {
/*  61 */     byte clearanceHeight = 0;
/*  62 */     if (BasePathingNode.isPassable(this.world, bp) && BasePathingNode.isPassable(this.world, bp.up())) {
/*  63 */       clearanceHeight = 2;
/*  64 */       if (BasePathingNode.isPassable(this.world, bp.up(2))) {
/*  65 */         clearanceHeight = (byte)(clearanceHeight + 1);
/*     */       }
/*     */     } 
/*  68 */     if (clearanceHeight >= 2) {
/*  69 */       BasePathingNode baseNode = new BasePathingNode(bp, clearanceHeight);
/*  70 */       this.baseCellMap.putNode(baseNode, this.world);
/*  71 */       this.nodeProcessQueue.addLast(baseNode);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update() {
/*  86 */     processNodeQueue();
/*     */   }
/*     */   
/*     */   private void processNodeQueue() {
/*  90 */     int nodesProcessed = 0;
/*  91 */     int throttle = 16000;
/*     */ 
/*     */ 
/*     */     
/*  95 */     while (!this.nodeProcessQueue.isEmpty() && nodesProcessed < 16000) {
/*  96 */       PathingNode node = this.nodeProcessQueue.pollFirst();
/*  97 */       if (node != null) {
/*  98 */         if (node.isDestroyed()) {
/*  99 */           boolean bool = true;
/*     */         }
/*     */         else {
/*     */           
/* 103 */           node.process(this.world, this.baseCellMap, this);
/* 104 */           if (!this.listeners.isEmpty()) {
/* 105 */             node.notifyListeners(this.world, this.listeners);
/*     */           }
/*     */         } 
/*     */       }
/* 109 */       nodesProcessed++;
/*     */     } 
/*     */     
/* 112 */     if (this.nodeProcessQueue.isEmpty() && this.baseCellMap.nodeCount() > 1000) {
/* 113 */       this.initialQueueComplete = true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInitialQueueComplete() {
/* 122 */     return this.initialQueueComplete;
/*     */   }
/*     */   
/*     */   public boolean isInRange(BlockPos bp) {
/* 126 */     return this.village.isInVillage(bp);
/*     */   }
/*     */   
/*     */   public void addFirstNode(PathingNode node) {
/* 130 */     if (node.isDestroyed()) {
/*     */       return;
/*     */     }
/* 133 */     if (!node.isQueued()) {
/* 134 */       node.queue();
/* 135 */       this.nodeProcessQueue.addFirst(node);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addLastNode(PathingNode node) {
/* 140 */     if (node.isDestroyed()) {
/*     */       return;
/*     */     }
/* 143 */     if (!node.isQueued()) {
/* 144 */       node.queue();
/*     */       
/* 146 */       for (PathingNode child : node.children) {
/* 147 */         assert child.parent == node;
/*     */       }
/*     */       
/* 150 */       this.nodeProcessQueue.addLast(node);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void verifyNode(PathingNode node) {
/* 183 */     for (int i = 0; i < 4 - (node.getCell()).level; i++) {
/* 184 */       System.out.print("    ");
/*     */     }
/* 186 */     System.out.print("->" + node.getCell());
/* 187 */     this.nodesVerified++;
/*     */     
/* 189 */     if ((node.getCell()).level == 1 && node.children.size() > 4) {
/* 190 */       System.err.println("Node with > 4 children " + node);
/*     */     }
/* 192 */     if ((node.getCell()).level > 0 && node.children.size() < 1) {
/* 193 */       System.err.println("Level " + (node.getCell()).level + " with no children");
/*     */     }
/* 195 */     System.out.print("      Connections: ");
/* 196 */     for (PathingNode connect : node.connections) {
/* 197 */       System.out.print(connect.cell + "  ");
/*     */     }
/* 199 */     System.out.print("\n");
/*     */ 
/*     */     
/* 202 */     for (PathingNode child : node.children) {
/* 203 */       if (child.parent != node) {
/* 204 */         System.err.println("child/parent mismatch");
/*     */       }
/* 206 */       for (PathingNode childConnect : child.connections) {
/* 207 */         if (childConnect.parent != node && 
/* 208 */           !node.isConnected(childConnect.parent)) {
/* 209 */           System.err.println("Node " + node + " not connected to neighbor child " + child + " parent " + childConnect.parent);
/*     */         }
/*     */       } 
/*     */       
/* 213 */       verifyNode(child);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onBlockUpdate(World world, BlockPos bp) {
/* 218 */     BasePathingNode baseNode = this.baseCellMap.getNodeYRange(bp.getX(), bp.getY() - 2, bp.getY() + 1, bp.getZ());
/* 219 */     while (baseNode != null) {
/* 220 */       this.baseCellMap.removeNode(baseNode, this);
/* 221 */       baseNode.notifyListeners(world, this.listeners);
/* 222 */       baseNode = this.baseCellMap.getNodeYRange(bp.getX(), bp.getY() - 2, bp.getY() + 1, bp.getZ());
/*     */     } 
/*     */ 
/*     */     
/* 226 */     this.baseCellMap.updateNodes(bp.getX() + 1, bp.getY() - 2, bp.getY() + 1, bp.getZ(), this);
/* 227 */     this.baseCellMap.updateNodes(bp.getX() - 1, bp.getY() - 2, bp.getY() + 1, bp.getZ(), this);
/* 228 */     this.baseCellMap.updateNodes(bp.getX(), bp.getY() - 2, bp.getY() + 1, bp.getZ() + 1, this);
/* 229 */     this.baseCellMap.updateNodes(bp.getX(), bp.getY() - 2, bp.getY() + 1, bp.getZ() - 1, this);
/*     */ 
/*     */     
/* 232 */     if (isInitialQueueComplete()) {
/* 233 */       processNodeQueue();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChunkUnloaded(Chunk chunk) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChunkLoaded(Chunk chunk) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInGraph(BlockPos bp) {
/* 270 */     return (getBaseNode(bp.getX(), bp.getY(), bp.getZ()) != null);
/*     */   }
/*     */   
/*     */   public BasePathingNode getBaseNode(int x, int y, int z) {
/* 274 */     return this.baseCellMap.getNode(x, y, z);
/*     */   }
/*     */   
/*     */   public BasePathingNode getNodeYRange(int x, int y1, int y2, int z) {
/* 278 */     return this.baseCellMap.getNodeYRange(x, y1, y2, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public BasePathingNode getNearbyBaseNode(Vec3d pos, double widthX, double height, double widthZ) {
/* 283 */     BasePathingNode node = getBaseNode((int)pos.x, (int)pos.y, (int)pos.z);
/* 284 */     if (node == null) {
/* 285 */       double halfX = widthX / 2.0D;
/* 286 */       double halfZ = widthZ / 2.0D;
/* 287 */       BlockPos corner1 = new BlockPos(pos.x - halfX, pos.y - 1.0D, pos.z - halfZ);
/* 288 */       BlockPos corner2 = new BlockPos(pos.x + halfX, pos.y + height, pos.z + halfZ);
/*     */       
/* 290 */       for (BlockPos blockPos : BlockPos.getAllInBox(corner1, corner2)) {
/* 291 */         node = getBaseNode(blockPos.getX(), blockPos.getY(), blockPos.getZ());
/* 292 */         if (node != null) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/* 297 */     return node;
/*     */   }
/*     */   public void debugEdgeNodes(World world) {
/* 300 */     this.baseCellMap.debugEdgeNodes(world);
/*     */   } public BasePathingNode getEdgeNode(BlockPos origin, Double minDist) {
/* 302 */     return this.baseCellMap.getEdgeNode(origin, minDist.doubleValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\pathing\PathingGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */