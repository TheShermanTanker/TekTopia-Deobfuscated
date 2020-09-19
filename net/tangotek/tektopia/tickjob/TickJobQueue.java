/*    */ package net.tangotek.tektopia.tickjob;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.ListIterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TickJobQueue
/*    */ {
/* 11 */   private List<TickJob> jobs = new ArrayList<>();
/* 12 */   private List<TickJob> pendingJobs = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addJob(TickJob job) {
/* 19 */     this.pendingJobs.add(job);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 23 */     this.jobs.addAll(this.pendingJobs);
/* 24 */     this.pendingJobs.clear();
/*    */     
/* 26 */     synchronized (this.jobs) {
/* 27 */       ListIterator<TickJob> itr = this.jobs.listIterator();
/* 28 */       while (itr.hasNext()) {
/* 29 */         TickJob job = itr.next();
/* 30 */         job.tick();
/* 31 */         if (job.isComplete())
/* 32 */           itr.remove(); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void clear() {
/* 38 */     this.jobs.clear();
/* 39 */     this.pendingJobs.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\net\tangotek\tektopia\tickjob\TickJobQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */