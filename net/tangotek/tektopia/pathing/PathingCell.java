/*    */ package net.tangotek.tektopia.pathing;
/*    */ 
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ 
/*    */ public class PathingCell {
/*    */   public final int x;
/*    */   public final int y;
/*    */   public final int z;
/*    */   public final byte level;
/*    */   
/*    */   public PathingCell(BlockPos bp, byte level) {
/* 12 */     this(bp.getX(), bp.getY(), bp.getZ(), level);
/*    */   }
/*    */   
/*    */   private PathingCell(int x, int y, int z, byte level) {
/* 16 */     this.x = x;
/* 17 */     this.y = y;
/* 18 */     this.z = z;
/* 19 */     this.level = level;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getBlockPos() {
/* 24 */     return new BlockPos(this.x, this.y, this.z);
/*    */   }
/*    */   
/*    */   public PathingCell up() {
/* 28 */     return up((byte)1);
/*    */   }
/*    */   public PathingCell up(byte levels) {
/* 31 */     return new PathingCell(this.x >> levels, this.y >> levels, this.z >> levels, (byte)(this.level + levels));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 37 */     if (!(o instanceof PathingCell)) {
/* 38 */       return false;
/*    */     }
/* 40 */     PathingCell other = (PathingCell)o;
/* 41 */     return (this.x == other.x && this.y == other.y && this.z == other.z && this.level == other.level);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     return "[" + this.level + "][" + this.x + ", " + this.y + ", " + this.z + "]";
/*    */   }
/*    */   
/*    */   public static int hashCode(int x, int z) {
/* 50 */     int result = x;
/* 51 */     result = 31 * result + z;
/* 52 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 57 */     return hashCode(this.x, this.z);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\pathing\PathingCell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */