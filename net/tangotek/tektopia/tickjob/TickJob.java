/*    */ package net.tangotek.tektopia.tickjob;
/*    */ 
/*    */ import java.util.Random;
/*    */ 
/*    */ public class TickJob {
/*    */   private int tick;
/*    */   private final int baseTicks;
/*    */   private final int randomTicks;
/*    */   private final boolean repeat;
/*    */   private final Random rand;
/*    */   private final Runnable runner;
/*    */   
/*    */   public TickJob(int baseTicks, int randomTicks, boolean repeat, Runnable runner) {
/* 14 */     this.baseTicks = baseTicks;
/* 15 */     this.randomTicks = randomTicks;
/* 16 */     this.repeat = repeat;
/* 17 */     this.runner = runner;
/* 18 */     this.rand = new Random();
/* 19 */     refreshTicks();
/*    */   }
/*    */   
/*    */   private void refreshTicks() {
/* 23 */     this.tick = this.baseTicks;
/* 24 */     if (this.randomTicks > 0)
/* 25 */       this.tick += this.rand.nextInt(this.randomTicks); 
/*    */   }
/*    */   
/*    */   public void tick() {
/* 29 */     this.tick--;
/* 30 */     if (this.tick == 0) {
/* 31 */       this.runner.run();
/*    */       
/* 33 */       if (this.repeat)
/* 34 */         refreshTicks(); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isComplete() {
/* 39 */     return (this.tick <= 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\tickjob\TickJob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */