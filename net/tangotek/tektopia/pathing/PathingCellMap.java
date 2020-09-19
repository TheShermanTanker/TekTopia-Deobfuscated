/*     */ package net.tangotek.tektopia.pathing;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class PathingCellMap {
/*  20 */   private int nodeCount = 0; private final int defaultCapacity;
/*  21 */   private BasePathingNode firstNode = null; private Map<Integer, Map<Integer, Set<BasePathingNode>>> baseNodes; private NavigableSet<BasePathingNode> edgeNodes; private Random rnd;
/*     */   public PathingCellMap(int defaultMapCapacity) {
/*  23 */     this.edgeNodes = new TreeSet<>(Comparator.comparingInt(a -> (int)a.getBlockPos().distanceSq((Vec3i)this.firstNode.getBlockPos())));
/*  24 */     this.rnd = new Random();
/*     */ 
/*     */     
/*  27 */     this.defaultCapacity = defaultMapCapacity;
/*  28 */     this.baseNodes = new HashMap<>(this.defaultCapacity);
/*     */   }
/*     */   
/*     */   public void putNode(BasePathingNode node, World world) {
/*  32 */     if (this.firstNode == null) {
/*  33 */       this.firstNode = node;
/*  34 */       this.edgeNodes.add(node);
/*     */     } 
/*     */     
/*  37 */     Map<Integer, Set<BasePathingNode>> zMap = this.baseNodes.get(Integer.valueOf((node.getCell()).x));
/*  38 */     if (zMap == null) {
/*  39 */       zMap = new HashMap<>(this.defaultCapacity);
/*  40 */       this.baseNodes.put(Integer.valueOf((node.getCell()).x), zMap);
/*     */     } 
/*     */     
/*  43 */     Set<BasePathingNode> nodeSet = zMap.get(Integer.valueOf((node.getCell()).z));
/*  44 */     if (nodeSet == null) {
/*  45 */       nodeSet = new HashSet<>();
/*  46 */       zMap.put(Integer.valueOf((node.getCell()).z), nodeSet);
/*     */     } 
/*     */     
/*  49 */     if (this.rnd.nextInt(30) == 0) {
/*  50 */       int edgeDist = getAxisDistance(this.firstNode.getBlockPos(), node.getBlockPos());
/*  51 */       if (edgeDist < 115 && world.canSeeSky(node.getBlockPos())) {
/*  52 */         this.edgeNodes.add(node);
/*  53 */         if (this.edgeNodes.size() > 10) {
/*  54 */           this.edgeNodes.pollFirst();
/*     */         }
/*     */       } 
/*     */     } 
/*  58 */     if (!nodeSet.add(node)) {
/*  59 */       throw new IllegalArgumentException("Duplicate BasePathingNode encountered");
/*     */     }
/*  61 */     this.nodeCount++;
/*     */   }
/*     */   
/*     */   private int getAxisDistance(BlockPos bp1, BlockPos bp2) {
/*  65 */     return Math.max(Math.abs(bp1.getX() - bp2.getX()), Math.abs(bp1.getZ() - bp2.getZ()));
/*     */   }
/*     */   
/*     */   public void removeNode(BasePathingNode node, PathingGraph graph) {
/*  69 */     Set<BasePathingNode> nodeSet = getXZSet((node.getCell()).x, (node.getCell()).z);
/*  70 */     if (nodeSet != null && nodeSet.remove(node)) {
/*  71 */       node.destroy(graph);
/*  72 */       this.nodeCount--;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int nodeCount() {
/*  77 */     return this.nodeCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasePathingNode getEdgeNode(BlockPos origin, double minDist) {
/*  82 */     if (!this.edgeNodes.isEmpty()) {
/*     */       
/*  84 */       int index = this.rnd.nextInt(this.edgeNodes.size());
/*  85 */       int i = 0;
/*  86 */       for (BasePathingNode edgeNode : this.edgeNodes) {
/*  87 */         if (i == index) {
/*  88 */           return edgeNode;
/*     */         }
/*  90 */         i++;
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   public void debugEdgeNodes(World world) {
/*  98 */     for (BasePathingNode node : this.edgeNodes) {
/*  99 */       System.out.println("Edge Node at " + node.getBlockPos());
/* 100 */       EntityArmorStand ent = new EntityArmorStand(world, node.getBlockPos().getX(), node.getBlockPos().getY(), node.getBlockPos().getZ());
/* 101 */       ent.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 200));
/* 102 */       ent.setHealth(0.0F);
/* 103 */       ent.deathTime = -200;
/* 104 */       world.spawnEntity((Entity)ent);
/*     */     } 
/*     */   }
/*     */   
/*     */   public BasePathingNode getNode(int x, int y, int z) {
/* 109 */     return getNodeYRange(x, y, y, z);
/*     */   }
/*     */   
/*     */   public BasePathingNode getNodeYRange(int x, int y1, int y2, int z) {
/* 113 */     Set<BasePathingNode> nodeSet = getXZSet(x, z);
/* 114 */     if (nodeSet != null) {
/* 115 */       for (BasePathingNode node : nodeSet) {
/* 116 */         if ((node.getCell()).y >= y1 && (node.getCell()).y <= y2) {
/* 117 */           return node;
/*     */         }
/*     */       } 
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */   
/*     */   public void updateNodes(int x, int y1, int y2, int z, PathingGraph graph) {
/* 125 */     Set<BasePathingNode> nodeSet = getXZSet(x, z);
/* 126 */     if (nodeSet != null) {
/* 127 */       for (BasePathingNode node : nodeSet) {
/* 128 */         if ((node.getCell()).y >= y1 && (node.getCell()).y <= y2) {
/* 129 */           graph.addFirstNode(node);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private Set<BasePathingNode> getXZSet(int x, int z) {
/* 136 */     Map<Integer, Set<BasePathingNode>> zMap = this.baseNodes.get(Integer.valueOf(x));
/* 137 */     if (zMap != null) {
/* 138 */       return zMap.get(Integer.valueOf(z));
/*     */     }
/*     */     
/* 141 */     return null;
/*     */   }
/*     */   
/*     */   public Set<PathingNode> getTopNodes() {
/* 145 */     PathingNode topNode = this.firstNode.getTopParent();
/* 146 */     Set<PathingNode> outNodes = new HashSet<>();
/* 147 */     fillConnections(topNode, outNodes);
/* 148 */     return outNodes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void notifyListenerInitial(World world, EntityPlayerMP player) {
/* 154 */     List<EntityPlayerMP> listeners = new ArrayList<>(1);
/* 155 */     listeners.add(player);
/* 156 */     for (Map<Integer, Set<BasePathingNode>> zMap : this.baseNodes.values()) {
/* 157 */       for (Set<BasePathingNode> nodeSet : zMap.values()) {
/* 158 */         for (BasePathingNode node : nodeSet) {
/* 159 */           node.notifyListeners(world, listeners);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void fillConnections(PathingNode node, Set<PathingNode> outNodes) {
/* 166 */     if (!outNodes.contains(node)) {
/* 167 */       outNodes.add(node);
/* 168 */       for (PathingNode peer : node.connections) {
/* 169 */         fillConnections(peer, outNodes);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public BasePathingNode randomNode() {
/* 175 */     int numX = (int)(Math.random() * this.baseNodes.size());
/* 176 */     for (Map<Integer, Set<BasePathingNode>> xMap : this.baseNodes.values()) {
/* 177 */       if (--numX < 0) {
/* 178 */         int numZ = (int)(Math.random() * xMap.size());
/* 179 */         for (Set<BasePathingNode> zSet : xMap.values()) {
/* 180 */           if (--numZ < 0) {
/* 181 */             int numY = (int)(Math.random() * zSet.size());
/* 182 */             for (BasePathingNode node : zSet) {
/* 183 */               if (--numY < 0) {
/* 184 */                 return node;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 191 */     throw new AssertionError();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\pathing\PathingCellMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */