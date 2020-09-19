/*     */ package net.tangotek.tektopia.pathing;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathStep
/*     */ {
/*     */   private final PathingNode node;
/*     */   private final PathStep parentStep;
/*     */   private int totalPathDistance;
/*  10 */   private int distanceToHere = 0;
/*     */   private int heuristic;
/*  12 */   private PathStep previous = null;
/*  13 */   private PathStep nextStep = null;
/*     */   
/*     */   public PathStep(PathingNode node, PathStep neighbor, PathingNode target, PathStep parentStep) {
/*  16 */     this.node = node;
/*  17 */     this.heuristic = calcHeuristic(target, node);
/*     */     
/*  19 */     this.previous = neighbor;
/*  20 */     this.parentStep = parentStep;
/*  21 */     if (neighbor != null) {
/*  22 */       this.distanceToHere = neighborAdjacent(neighbor);
/*     */     }
/*  24 */     this.totalPathDistance = this.distanceToHere + this.heuristic;
/*     */   }
/*     */   
/*     */   public int calcManhattanDistance(PathingNode target, PathingNode here) {
/*  28 */     return Math.abs(target.cell.x - here.cell.x) + Math.abs(target.cell.y - here.cell.y) + Math.abs(target.cell.z - here.cell.z);
/*     */   }
/*     */   
/*     */   public int calcHeuristic(PathingNode target, PathingNode here) {
/*  32 */     return (int)Math.sqrt(Math.pow(Math.abs(target.cell.x - here.cell.x), 2.0D) + Math.pow(Math.abs(target.cell.y - here.cell.y), 2.0D) + Math.pow(Math.abs(target.cell.z - here.cell.z), 2.0D));
/*     */   }
/*     */   
/*     */   public PathStep getParentStep() {
/*  36 */     return this.parentStep;
/*     */   }
/*     */   
/*     */   public PathingNode getNode() {
/*  40 */     return this.node;
/*     */   }
/*     */   
/*     */   public PathStep getNextStep() {
/*  44 */     return this.nextStep;
/*     */   }
/*     */   
/*     */   private int neighborAdjacent(PathStep neighbor) {
/*  48 */     int newDist = neighbor.distanceToHere + 1;
/*  49 */     if (neighbor.node.cell.y != this.node.cell.y) {
/*  50 */       newDist += 8;
/*     */     }
/*  52 */     return newDist;
/*     */   }
/*     */   
/*     */   public boolean updateDistance(PathStep neighbor) {
/*  56 */     int newDist = neighborAdjacent(neighbor);
/*  57 */     if (newDist < this.distanceToHere) {
/*  58 */       this.distanceToHere = newDist;
/*  59 */       this.totalPathDistance = this.distanceToHere + this.heuristic;
/*  60 */       this.previous = neighbor;
/*  61 */       return true;
/*     */     } 
/*     */     
/*  64 */     return false;
/*     */   }
/*     */   
/*     */   public PathStep reverseSteps() {
/*  68 */     PathStep step = this;
/*     */ 
/*     */ 
/*     */     
/*  72 */     while (step.previous != null) {
/*  73 */       step.previous.nextStep = step;
/*  74 */       step = step.previous;
/*     */     } 
/*     */ 
/*     */     
/*  78 */     return step;
/*     */   }
/*     */   
/*     */   public int getDistanceToHere() {
/*  82 */     return this.distanceToHere;
/*     */   }
/*     */   
/*     */   public int getTotalPathDistance() {
/*  86 */     return this.totalPathDistance;
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
/*     */   public boolean equals(Object other) {
/* 100 */     if (!(other instanceof PathStep))
/*     */     {
/* 102 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 106 */     PathStep otherStep = (PathStep)other;
/* 107 */     return (this.node == otherStep.node);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\pathing\PathStep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */