/*     */ package com.websina.util;
/*     */ 
/*     */ import java.sql.Date;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
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
/*     */ public class DateParser
/*     */ {
/*     */   private Calendar calendar;
/*     */   
/*     */   public DateParser(Date date) {
/*  26 */     this.calendar = Calendar.getInstance();
/*  27 */     this.calendar.setTime(date);
/*     */   }
/*     */   
/*     */   public DateParser(String dateStr) {
/*  31 */     this(toUtilDate(dateStr));
/*     */   }
/*     */   
/*     */   public static Date toUtilDate(String dateStr) {
/*  35 */     Date date = null;
/*     */     
/*     */     try {
/*  38 */       DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
/*  39 */       date = formater.parse(dateStr);
/*  40 */     } catch (Exception e) {
/*  41 */       e.printStackTrace();
/*     */       try {
/*  43 */         DateFormat formater = new SimpleDateFormat("MM/dd/yyyy");
/*  44 */         date = formater.parse(dateStr);
/*  45 */       } catch (Exception e2) {
/*  46 */         e2.printStackTrace();
/*     */       } 
/*     */     } 
/*     */     
/*  50 */     return date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date toSqlDate(Date date) {
/*  59 */     if (date == null) {
/*  60 */       return null;
/*     */     }
/*  62 */     return new Date(date.getTime());
/*     */   }
/*     */ 
/*     */   
/*     */   public static Date toSqlDate(String dateStr) {
/*  67 */     return toSqlDate(toUtilDate(dateStr));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getYear() {
/*  74 */     int year = this.calendar.get(1);
/*  75 */     return Integer.toString(year);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getXXYear() {
/*  82 */     return getYear().substring(2, 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMonth() {
/*  89 */     return this.calendar.get(2) + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDayOfMonth() {
/*  96 */     return this.calendar.get(5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDayOfWeek() {
/* 103 */     return this.calendar.get(7);
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 107 */     DateParser parser = new DateParser(args[0]);
/* 108 */     System.out.println("year=" + parser.getYear());
/* 109 */     System.out.println("year2=" + parser.getXXYear());
/* 110 */     System.out.println("month=" + parser.getMonth());
/* 111 */     System.out.println("day=" + parser.getDayOfMonth());
/* 112 */     System.out.println("week=" + parser.getDayOfWeek());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\tektopia-1.1.0-deobf.jar!\com\websin\\util\DateParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */