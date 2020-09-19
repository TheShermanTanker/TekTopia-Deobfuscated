/*     */ package net.tangotek.tektopia.pathing;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.pathfinding.Path;
/*     */ import net.minecraft.pathfinding.PathFinder;
/*     */ import net.minecraft.pathfinding.PathPoint;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.tangotek.tektopia.entities.EntityVillageNavigator;
/*     */ 
/*     */ public class PathFinder extends PathFinder {
/*     */   protected EntityVillageNavigator entity;
/*     */   protected IBlockAccess blockAccess;
/*     */   
/*     */   public PathFinder(EntityVillageNavigator entityNav) {
/*  29 */     super(null); this.openSteps = new PriorityQueue<>(Comparator.comparingInt(a -> a.getTotalPathDistance()));
/*     */     this.closedNodes = new HashSet<>();
/*  31 */     this.entity = entityNav;
/*  32 */     this.blockAccess = (IBlockAccess)entityNav.world;
/*     */   }
/*     */   protected PriorityQueue<PathStep> openSteps; protected Set<PathingNode> closedNodes;
/*     */   public PathingGraph getGraph() {
/*  36 */     if (this.entity.hasVillage()) {
/*  37 */       return this.entity.getVillage().getPathingGraph();
/*     */     }
/*  39 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Path findPath(IBlockAccess worldIn, EntityLiving entitylivingIn, Entity targetEntity, float maxDistance) {
/*  45 */     return findPath(worldIn, entitylivingIn, targetEntity.posX, (targetEntity.getEntityBoundingBox()).minY, targetEntity.posZ);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Path findPath(IBlockAccess worldIn, EntityLiving entitylivingIn, BlockPos targetPos, float maxDistance) {
/*  51 */     return findPath(worldIn, entitylivingIn, (targetPos.getX() + 0.5F), (targetPos.getY() + 0.5F), (targetPos.getZ() + 0.5F));
/*     */   }
/*     */   
/*     */   private Path findPath(IBlockAccess worldIn, EntityLiving entityLivingIn, double x, double y, double z) {
/*  55 */     this.blockAccess = worldIn;
/*  56 */     PathingGraph graph = getGraph();
/*  57 */     if (graph != null) {
/*  58 */       PathingNode endNode = graph.getBaseNode(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
/*  59 */       PathingNode startNode = getStart(graph);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  65 */       return findPath(worldIn, startNode, endNode);
/*     */     } 
/*     */     
/*  68 */     return null;
/*     */   }
/*     */   
/*     */   public Path findPath(IBlockAccess worldIn, PathingNode startNode, PathingNode endNode) {
/*  72 */     if (endNode == null || startNode == null) {
/*  73 */       return null;
/*     */     }
/*     */     
/*  76 */     PathStep firstStep = null;
/*  77 */     int maxLevel = (endNode.getTopParent().getCell()).level;
/*     */     
/*  79 */     for (int level = maxLevel; level >= 0; level--) {
/*  80 */       PathingNode localStart = startNode.getParent(level);
/*  81 */       PathingNode localEnd = endNode.getParent(level);
/*     */       
/*  83 */       firstStep = findLevelPath(new PathStep(localStart, null, localEnd, firstStep), localEnd);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  88 */     return finalizePath(firstStep);
/*     */   }
/*     */ 
/*     */   
/*     */   private PathStep findLevelPath(PathStep startPoint, PathingNode endNode) {
/*  93 */     this.openSteps.clear();
/*  94 */     this.closedNodes.clear();
/*  95 */     this.openSteps.add(startPoint);
/*     */ 
/*     */     
/*     */     while (true) {
/*  99 */       PathStep current = this.openSteps.poll();
/* 100 */       if (current == null) {
/*     */         break;
/*     */       }
/* 103 */       this.closedNodes.add(current.getNode());
/*     */       
/* 105 */       if (current.getNode() == endNode) {
/* 106 */         return current.reverseSteps();
/*     */       }
/* 108 */       for (PathingNode connectedNode : (current.getNode()).connections) {
/* 109 */         if (!this.closedNodes.contains(connectedNode)) {
/* 110 */           boolean isOpen = false;
/* 111 */           PathStep stepToAdd = null;
/* 112 */           Iterator<PathStep> itr = this.openSteps.iterator();
/* 113 */           while (itr.hasNext()) {
/* 114 */             PathStep step = itr.next();
/* 115 */             if (step.getNode().equals(connectedNode)) {
/* 116 */               if (step.updateDistance(current)) {
/* 117 */                 itr.remove();
/* 118 */                 stepToAdd = step;
/*     */               } 
/* 120 */               isOpen = true;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 125 */           if (!isOpen) {
/* 126 */             if (current.getParentStep() == null) {
/* 127 */               stepToAdd = new PathStep(connectedNode, current, endNode, null);
/* 128 */             } else if (connectedNode.getParent() == current.getParentStep().getNode()) {
/* 129 */               stepToAdd = new PathStep(connectedNode, current, endNode, current.getParentStep());
/* 130 */             } else if (current.getParentStep().getNextStep() != null && connectedNode.getParent() == current.getParentStep().getNextStep().getNode()) {
/* 131 */               stepToAdd = new PathStep(connectedNode, current, endNode, current.getParentStep().getNextStep());
/*     */             } 
/*     */           }
/* 134 */           if (stepToAdd != null) {
/* 135 */             this.openSteps.add(stepToAdd);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 140 */     return null;
/*     */   }
/*     */   
/*     */   private Path finalizePath(PathStep firstStep) {
/* 144 */     ArrayList<PathPoint> list = new ArrayList<>();
/* 145 */     PathStep step = firstStep;
/* 146 */     while (step != null) {
/* 147 */       PathingCell cell = step.getNode().getCell();
/* 148 */       list.add(new PathPoint(cell.x, cell.y, cell.z));
/* 149 */       step = step.getNextStep();
/*     */     } 
/*     */     
/* 152 */     return new Path(list.<PathPoint>toArray(new PathPoint[0]));
/*     */   }
/*     */ 
/*     */   
/*     */   private PathingNode getStart(PathingGraph graph) {
/*     */     int i;
/* 158 */     if (this.entity.isInWater()) {
/* 159 */       i = (int)(this.entity.getEntityBoundingBox()).minY;
/* 160 */       BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));
/*     */       
/* 162 */       for (Block block = this.blockAccess.getBlockState((BlockPos)blockpos$mutableblockpos).getBlock(); block == Blocks.FLOWING_WATER || block == Blocks.WATER; block = this.blockAccess.getBlockState((BlockPos)blockpos$mutableblockpos).getBlock()) {
/* 163 */         i++;
/* 164 */         blockpos$mutableblockpos.setPos(MathHelper.floor(this.entity.posX), i, MathHelper.floor(this.entity.posZ));
/*     */       } 
/* 166 */     } else if (this.entity.onGround) {
/* 167 */       i = MathHelper.floor((this.entity.getEntityBoundingBox()).minY + 0.5D);
/*     */     } else {
/*     */       BlockPos blockpos;
/*     */       
/* 171 */       for (blockpos = new BlockPos((Entity)this.entity); (this.blockAccess.getBlockState(blockpos).getMaterial() == Material.AIR || this.blockAccess.getBlockState(blockpos).getBlock().isPassable(this.blockAccess, blockpos)) && blockpos.getY() > 0; blockpos = blockpos.down());
/*     */ 
/*     */ 
/*     */       
/* 175 */       i = blockpos.up().getY();
/*     */     } 
/*     */     
/* 178 */     BlockPos blockpos2 = new BlockPos((Entity)this.entity);
/* 179 */     PathingNode node = graph.getBaseNode(blockpos2.getX(), i, blockpos2.getZ());
/* 180 */     if (node == null) {
/* 181 */       node = graph.getBaseNode(blockpos2.getX(), i + 1, blockpos2.getZ());
/*     */     }
/*     */     
/* 184 */     if (node == null) {
/* 185 */       Set<BlockPos> set = Sets.newHashSet();
/* 186 */       set.add(new BlockPos((this.entity.getEntityBoundingBox()).minX, i, (this.entity.getEntityBoundingBox()).minZ));
/* 187 */       set.add(new BlockPos((this.entity.getEntityBoundingBox()).minX, i, (this.entity.getEntityBoundingBox()).maxZ));
/* 188 */       set.add(new BlockPos((this.entity.getEntityBoundingBox()).maxX, i, (this.entity.getEntityBoundingBox()).minZ));
/* 189 */       set.add(new BlockPos((this.entity.getEntityBoundingBox()).maxX, i, (this.entity.getEntityBoundingBox()).maxZ));
/*     */       
/* 191 */       for (BlockPos blockpos1 : set) {
/* 192 */         node = graph.getNodeYRange(blockpos1.getX(), blockpos1.getY() - 1, blockpos1.getY(), blockpos1.getZ());
/* 193 */         if (node != null) {
/* 194 */           return node;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 199 */     return node;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\pathing\PathFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */