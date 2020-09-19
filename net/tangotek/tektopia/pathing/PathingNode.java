/*     */ package net.tangotek.tektopia.pathing;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class PathingNode
/*     */ {
/*     */   protected final PathingCell cell;
/*  13 */   protected PathingNode parent = null;
/*     */   protected boolean queued = false;
/*     */   protected boolean destroyed = false;
/*  16 */   public Set<PathingNode> connections = new HashSet<>();
/*  17 */   public Set<PathingNode> children = new HashSet<>();
/*     */   
/*     */   public PathingNode(PathingCell cell) {
/*  20 */     this.cell = cell;
/*     */   }
/*     */   
/*     */   public PathingCell getCell() {
/*  24 */     return this.cell;
/*     */   }
/*     */   
/*     */   public BlockPos getBlockPos() {
/*  28 */     return getCell().getBlockPos();
/*     */   }
/*     */   
/*     */   public void process(World world, PathingCellMap cellMap, PathingGraph graph) {
/*  32 */     this.queued = false;
/*  33 */     updateConnections(world, cellMap, graph);
/*     */   }
/*     */ 
/*     */   
/*     */   public int updateConnections(World world, PathingCellMap cellMap, PathingGraph graph) {
/*  38 */     Set<PathingNode> lastConnections = new HashSet<>(this.connections);
/*     */     
/*  40 */     for (PathingNode child : this.children) {
/*  41 */       for (PathingNode childConnection : child.connections) {
/*  42 */         if (childConnection.parent != null && 
/*  43 */           childConnection.parent != this) {
/*  44 */           lastConnections.remove(childConnection.parent);
/*  45 */           if (this.connections.contains(childConnection.parent)) {
/*  46 */             checkParentLink(childConnection.parent);
/*     */             continue;
/*     */           } 
/*  49 */           connectNodes(this, childConnection.parent, graph);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     for (PathingNode toBreak : lastConnections) {
/*  60 */       breakConnection(toBreak, graph);
/*     */     }
/*     */ 
/*     */     
/*  64 */     if (this.parent == null && this.cell.level < 4) {
/*  65 */       this.parent = new PathingNode(getCell().up());
/*  66 */       this.parent.addChild(this);
/*  67 */       graph.addLastNode(this.parent);
/*     */     } 
/*     */ 
/*     */     
/*  71 */     return this.connections.size();
/*     */   }
/*     */   
/*  74 */   public void queue() { this.queued = true; } public boolean isQueued() {
/*  75 */     return this.queued;
/*     */   }
/*     */   public PathingNode getConnection(int x, int z) {
/*  78 */     for (PathingNode node : this.connections) {
/*  79 */       if ((node.getCell()).x == this.cell.x + x && (node.getCell()).z == this.cell.z + z) {
/*  80 */         return node;
/*     */       }
/*     */     } 
/*  83 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isConnected(PathingNode node) {
/*  87 */     return this.connections.contains(node);
/*     */   }
/*     */   
/*     */   protected static void connectNodes(PathingNode node1, PathingNode node2, PathingGraph graph) {
/*  91 */     node1.connections.add(node2);
/*  92 */     node1.checkParentLink(node2);
/*     */     
/*  94 */     node2.connections.add(node1);
/*  95 */     node2.checkParentLink(node1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     if (node1.parent != node2.parent) {
/* 101 */       if (node1.parent != null && node1.connections.size() > 0) {
/* 102 */         graph.addLastNode(node1.parent);
/*     */       }
/* 104 */       if (node2.parent != null && node2.connections.size() > 0) {
/* 105 */         graph.addLastNode(node2.parent);
/*     */       }
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
/*     */   protected void notifyListeners(World world, List<EntityPlayerMP> listeners) {
/* 122 */     for (PathingNode child : this.children) {
/* 123 */       child.notifyListeners(world, listeners);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void breakConnection(PathingNode node2, PathingGraph graph) {
/* 128 */     this.connections.remove(node2);
/* 129 */     node2.connections.remove(this);
/*     */     
/* 131 */     if (this.parent != node2.parent && node2.parent != null) {
/* 132 */       graph.addLastNode(node2.parent);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkParentLink(PathingNode node) {
/* 137 */     if (this.parent == null && node.parent != null && 
/* 138 */       node.parent.cell.equals(this.cell.up()))
/*     */     {
/* 140 */       node.parent.addChild(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void removeChild(PathingNode child) {
/* 146 */     child.parent = null;
/* 147 */     this.children.remove(child);
/*     */   }
/*     */   
/*     */   protected void addChild(PathingNode child) {
/* 151 */     child.parent = this;
/* 152 */     this.children.add(child);
/*     */   }
/*     */   
/*     */   public PathingNode getParent() {
/* 156 */     return this.parent;
/*     */   }
/*     */   public PathingNode getParent(int levels) {
/* 159 */     PathingNode p = this;
/* 160 */     while (p.parent != null && levels > 0) {
/* 161 */       levels--;
/* 162 */       p = p.parent;
/*     */     } 
/*     */     
/* 165 */     return p;
/*     */   }
/*     */   
/*     */   public PathingNode getTopParent() {
/* 169 */     PathingNode p = this;
/* 170 */     while (p.parent != null) {
/* 171 */       p = p.parent;
/*     */     }
/* 173 */     return p;
/*     */   }
/*     */   
/*     */   public boolean isDestroyed() {
/* 177 */     return this.destroyed;
/*     */   }
/*     */   
/*     */   public void destroy(PathingGraph graph) {
/* 181 */     this.destroyed = true;
/* 182 */     (new HashSet(this.connections)).forEach(c -> breakConnection(c, graph));
/*     */     
/* 184 */     if (this.parent != null) {
/* 185 */       PathingNode par = this.parent;
/* 186 */       this.parent.removeChild(this);
/* 187 */       if (par.children.size() <= 0) {
/* 188 */         par.destroy(graph);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 194 */     return this.cell.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\pathing\PathingNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */