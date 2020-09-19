/*     */ package net.tangotek.tektopia.pathing;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.init.MobEffects;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class PathingCellMap2 {
/*  16 */   private int nodeCount = 0; private BlockPos origin;
/*  17 */   private BasePathingNode firstNode = null;
/*     */   private static final int VILLAGE2 = 240;
/*  19 */   private Set[] baseNodes = new Set[57600];
/*  20 */   private LinkedList<BasePathingNode> edgeNodes = new LinkedList<>();
/*  21 */   private Random rnd = new Random();
/*     */   
/*     */   public static final int HALF_VILLAGE = 60;
/*     */   
/*     */   public PathingCellMap2(BlockPos orig) {
/*  26 */     this.origin = orig;
/*     */   }
/*     */   
/*     */   public void putNode(BasePathingNode node, World world) {
/*  30 */     if (this.firstNode == null) {
/*  31 */       this.edgeNodes.add(node);
/*  32 */       this.firstNode = node;
/*     */     } 
/*     */     
/*  35 */     Set<BasePathingNode> nodeSet = getXZSet((node.getCell()).x, (node.getCell()).z, true);
/*  36 */     if (nodeSet != null) {
/*  37 */       if (this.rnd.nextInt(10) == 0) {
/*  38 */         double curDist = ((BasePathingNode)this.edgeNodes.getFirst()).getBlockPos().distanceSq((Vec3i)this.firstNode.getBlockPos());
/*  39 */         double thisDist = node.getBlockPos().distanceSq((Vec3i)this.firstNode.getBlockPos());
/*  40 */         int edgeDist = getAxisDistance(this.firstNode.getBlockPos(), node.getBlockPos());
/*  41 */         if (thisDist > curDist && edgeDist < 115 && world.canSeeSky(node.getBlockPos()) && (
/*  42 */           (BasePathingNode)this.edgeNodes.getFirst()).getBlockPos().distanceSq((Vec3i)node.getBlockPos()) > 400.0D) {
/*  43 */           System.out.println("Edge Node: [" + thisDist + "] " + node.getBlockPos());
/*  44 */           this.edgeNodes.addFirst(node);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  51 */       if (!nodeSet.add(node)) {
/*  52 */         throw new IllegalArgumentException("Duplicate BasePathingNode encountered");
/*     */       }
/*  54 */       this.nodeCount++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getAxisDistance(BlockPos bp1, BlockPos bp2) {
/*  59 */     return Math.max(Math.abs(bp1.getX() - bp2.getX()), Math.abs(bp1.getZ() - bp2.getZ()));
/*     */   }
/*     */   
/*     */   public void removeNode(BasePathingNode node, PathingGraph graph) {
/*  63 */     Set<BasePathingNode> nodeSet = getXZSet((node.getCell()).x, (node.getCell()).z, false);
/*  64 */     if (nodeSet != null && nodeSet.remove(node)) {
/*  65 */       node.destroy(graph);
/*  66 */       this.nodeCount--;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int nodeCount() {
/*  71 */     return this.nodeCount;
/*     */   }
/*     */   
/*     */   public BasePathingNode getEdgeNode(BlockPos origin, double minDist) {
/*  75 */     double minDistSq = minDist * minDist;
/*     */     
/*  77 */     while (this.edgeNodes.size() > 10) {
/*     */       
/*  79 */       Random rnd = new Random();
/*  80 */       ArrayList<BasePathingNode> arrayList = new ArrayList<>(this.edgeNodes);
/*  81 */       this.edgeNodes.clear();
/*  82 */       while (this.edgeNodes.size() < 10 && !arrayList.isEmpty()) {
/*  83 */         BasePathingNode node = arrayList.remove(rnd.nextInt(arrayList.size()));
/*  84 */         if (origin.distanceSq((Vec3i)node.getBlockPos()) >= minDistSq) {
/*  85 */           this.edgeNodes.add(node);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  90 */     for (BasePathingNode node : this.edgeNodes) {
/*     */       
/*  92 */       if (origin.distanceSq((Vec3i)node.getBlockPos()) >= minDistSq) {
/*  93 */         this.edgeNodes.remove(node);
/*  94 */         this.edgeNodes.addLast(node);
/*  95 */         return node;
/*     */       } 
/*     */     } 
/*     */     
/*  99 */     return null;
/*     */   }
/*     */   
/*     */   public void debugEdgeNodes(World world) {
/* 103 */     for (BasePathingNode node : this.edgeNodes) {
/* 104 */       System.out.println("Edge Node at " + node.getBlockPos());
/* 105 */       EntityArmorStand ent = new EntityArmorStand(world, node.getBlockPos().getX(), node.getBlockPos().getY(), node.getBlockPos().getZ());
/* 106 */       ent.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 200));
/* 107 */       ent.setHealth(0.0F);
/* 108 */       ent.deathTime = -200;
/* 109 */       world.spawnEntity((Entity)ent);
/*     */     } 
/*     */   }
/*     */   
/*     */   public BasePathingNode getNode(int x, int y, int z) {
/* 114 */     return getNodeYRange(x, y, y, z);
/*     */   }
/*     */   
/*     */   public BasePathingNode getNodeYRange(int x, int y1, int y2, int z) {
/* 118 */     Set<BasePathingNode> nodeSet = getXZSet(x, z, false);
/* 119 */     if (nodeSet != null) {
/* 120 */       for (BasePathingNode node : nodeSet) {
/* 121 */         if ((node.getCell()).y >= y1 && (node.getCell()).y <= y2) {
/* 122 */           return node;
/*     */         }
/*     */       } 
/*     */     }
/* 126 */     return null;
/*     */   }
/*     */   
/*     */   public void updateNodes(int x, int y1, int y2, int z, PathingGraph graph) {
/* 130 */     Set<BasePathingNode> nodeSet = getXZSet(x, z, false);
/* 131 */     if (nodeSet != null) {
/* 132 */       for (BasePathingNode node : nodeSet) {
/* 133 */         if ((node.getCell()).y >= y1 && (node.getCell()).y <= y2) {
/* 134 */           graph.addFirstNode(node);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private Set<BasePathingNode> getXZSet(int x, int z, boolean create) {
/* 141 */     int xx = x - this.origin.getX() + 120;
/* 142 */     int zz = z - this.origin.getZ() + 120;
/*     */     
/* 144 */     if (xx < 0 || xx >= 240 || zz < 0 || zz >= 240)
/*     */     {
/*     */ 
/*     */       
/* 148 */       return null;
/*     */     }
/*     */     
/* 151 */     Set<BasePathingNode> result = this.baseNodes[xx * 240 + zz];
/* 152 */     if (create && result == null) {
/* 153 */       result = new HashSet<>();
/* 154 */       this.baseNodes[xx * 240 + zz] = result;
/*     */     } 
/*     */     
/* 157 */     return result;
/*     */   }
/*     */   
/*     */   public Set<PathingNode> getTopNodes() {
/* 161 */     PathingNode topNode = this.firstNode.getTopParent();
/* 162 */     Set<PathingNode> outNodes = new HashSet<>();
/* 163 */     fillConnections(topNode, outNodes);
/* 164 */     return outNodes;
/*     */   }
/*     */   
/*     */   private void fillConnections(PathingNode node, Set<PathingNode> outNodes) {
/* 168 */     if (!outNodes.contains(node)) {
/* 169 */       outNodes.add(node);
/* 170 */       for (PathingNode peer : node.connections)
/* 171 */         fillConnections(peer, outNodes); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\pathing\PathingCellMap2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */