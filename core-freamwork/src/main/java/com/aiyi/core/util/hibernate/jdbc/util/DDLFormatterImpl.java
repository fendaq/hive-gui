package com.aiyi.core.util.hibernate.jdbc.util;

/*     */ 
/*     */ import java.util.StringTokenizer;

/*     */
/*     */ public class DDLFormatterImpl
/*     */   implements Formatter
/*     */ {
/*     */   public String format(String sql)
/*     */   {
/*  47 */     if (sql.toLowerCase().startsWith("create table")) {
/*  48 */       return formatCreateTable(sql);
/*     */     }
/*  50 */     if (sql.toLowerCase().startsWith("alter table")) {
/*  51 */       return formatAlterTable(sql);
/*     */     }
/*  53 */     if (sql.toLowerCase().startsWith("comment on")) {
/*  54 */       return formatCommentOn(sql);
/*     */     }
/*     */ 
/*  57 */     return "\n    " + sql;
/*     */   }
/*     */ 
/*     */   private String formatCommentOn(String sql)
/*     */   {
/*  62 */     StringBuffer result = new StringBuffer(60).append("\n    ");
/*  63 */     StringTokenizer tokens = new StringTokenizer(sql, " '[]\"", true);
/*     */ 
/*  65 */     boolean quoted = false;
/*  66 */     while (tokens.hasMoreTokens()) {
/*  67 */       String token = tokens.nextToken();
/*  68 */       result.append(token);
/*  69 */       if (isQuote(token)) {
/*  70 */         quoted = !quoted;
/*     */       }
/*  72 */       else if ((!quoted) && 
/*  73 */         ("is".equals(token))) {
/*  74 */         result.append("\n       ");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  79 */     return result.toString();
/*     */   }
/*     */ 
/*     */   private String formatAlterTable(String sql) {
/*  83 */     StringBuffer result = new StringBuffer(60).append("\n    ");
/*  84 */     StringTokenizer tokens = new StringTokenizer(sql, " (,)'[]\"", true);
/*     */ 
/*  86 */     boolean quoted = false;
/*  87 */     while (tokens.hasMoreTokens()) {
/*  88 */       String token = tokens.nextToken();
/*  89 */       if (isQuote(token)) {
/*  90 */         quoted = !quoted;
/*     */       }
/*  92 */       else if ((!quoted) && 
/*  93 */         (isBreak(token))) {
/*  94 */         result.append("\n        ");
/*     */       }
/*     */ 
/*  97 */       result.append(token);
/*     */     }
/*     */ 
/* 100 */     return result.toString();
/*     */   }
/*     */ 
/*     */   private String formatCreateTable(String sql) {
/* 104 */     StringBuffer result = new StringBuffer(60).append("\n    ");
/* 105 */     StringTokenizer tokens = new StringTokenizer(sql, "(,)'[]\"", true);
/*     */ 
/* 107 */     int depth = 0;
/* 108 */     boolean quoted = false;
/* 109 */     while (tokens.hasMoreTokens()) {
/* 110 */       String token = tokens.nextToken();
/* 111 */       if (isQuote(token)) {
/* 112 */         quoted = !quoted;
/* 113 */         result.append(token);
/*     */       }
/* 115 */       else if (quoted) {
/* 116 */         result.append(token);
/*     */       }
/*     */       else {
/* 119 */         if (")".equals(token)) {
/* 120 */           depth--;
/* 121 */           if (depth == 0) {
/* 122 */             result.append("\n    ");
/*     */           }
/*     */         }
/* 125 */         result.append(token);
/* 126 */         if ((",".equals(token)) && (depth == 1)) {
/* 127 */           result.append("\n       ");
/*     */         }
/* 129 */         if ("(".equals(token)) {
/* 130 */           depth++;
/* 131 */           if (depth == 1) {
/* 132 */             result.append("\n        ");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 138 */     return result.toString();
/*     */   }
/*     */ 
/*     */   private static boolean isBreak(String token)
/*     */   {
/* 146 */     return ("drop".equals(token)) || 
/* 143 */       ("add".equals(token)) || 
/* 144 */       ("references".equals(token)) || 
/* 145 */       ("foreign".equals(token)) || 
/* 146 */       ("on".equals(token));
/*     */   }
/*     */ 
/*     */   private static boolean isQuote(String tok)
/*     */   {
/* 154 */     return ("\"".equals(tok)) || 
/* 151 */       ("`".equals(tok)) || 
/* 152 */       ("]".equals(tok)) || 
/* 153 */       ("[".equals(tok)) || 
/* 154 */       ("'".equals(tok));
/*     */   }
/*     */ }

/* Location:           C:\Users\AnLuTong\Desktop\
 * Qualified Name:     org.hibernate.jdbc.util.DDLFormatterImpl
 * JD-Core Version:    0.6.2
 */