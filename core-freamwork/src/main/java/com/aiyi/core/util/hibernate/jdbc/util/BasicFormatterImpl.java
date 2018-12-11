package com.aiyi.core.util.hibernate.jdbc.util;

/*     */ 
/*     */

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

/*     */
/*     */
/*     */

/*     */
/*     */ @SuppressWarnings("unchecked")
public class BasicFormatterImpl
/*     */   implements Formatter
/*     */ {
/*  42 */   @SuppressWarnings("rawtypes")
private static final Set BEGIN_CLAUSES = new HashSet();
/*  43 */   @SuppressWarnings("rawtypes")
private static final Set END_CLAUSES = new HashSet();
/*  44 */   @SuppressWarnings("rawtypes")
private static final Set LOGICAL = new HashSet();
/*  45 */   @SuppressWarnings("rawtypes")
private static final Set QUANTIFIERS = new HashSet();
/*  46 */   @SuppressWarnings("rawtypes")
private static final Set DML = new HashSet();
/*  47 */   @SuppressWarnings("rawtypes")
private static final Set MISC = new HashSet();
/*     */   static final String indentString = "    ";
/*     */   static final String initial = "\n    ";
/*     */ 
/*     */   static
/*     */   {
/*  50 */     BEGIN_CLAUSES.add("left");
/*  51 */     BEGIN_CLAUSES.add("right");
/*  52 */     BEGIN_CLAUSES.add("inner");
/*  53 */     BEGIN_CLAUSES.add("outer");
/*  54 */     BEGIN_CLAUSES.add("group");
/*  55 */     BEGIN_CLAUSES.add("order");
/*     */ 
/*  57 */     END_CLAUSES.add("where");
/*  58 */     END_CLAUSES.add("set");
/*  59 */     END_CLAUSES.add("having");
/*  60 */     END_CLAUSES.add("join");
/*  61 */     END_CLAUSES.add("from");
/*  62 */     END_CLAUSES.add("by");
/*  63 */     END_CLAUSES.add("join");
/*  64 */     END_CLAUSES.add("into");
/*  65 */     END_CLAUSES.add("union");
/*     */ 
/*  67 */     LOGICAL.add("and");
/*  68 */     LOGICAL.add("or");
/*  69 */     LOGICAL.add("when");
/*  70 */     LOGICAL.add("else");
/*  71 */     LOGICAL.add("end");
/*     */ 
/*  73 */     QUANTIFIERS.add("in");
/*  74 */     QUANTIFIERS.add("all");
/*  75 */     QUANTIFIERS.add("exists");
/*  76 */     QUANTIFIERS.add("some");
/*  77 */     QUANTIFIERS.add("any");
/*     */ 
/*  79 */     DML.add("insert");
/*  80 */     DML.add("update");
/*  81 */     DML.add("delete");
/*     */ 
/*  83 */     MISC.add("select");
/*  84 */     MISC.add("on");
/*     */   }
/*     */ 
/*     */   public String format(String source)
/*     */   {
/*  91 */     return new FormatProcess(source).perform(); } 
/*  95 */   private static class FormatProcess { boolean beginLine = true;
/*  96 */     boolean afterBeginBeforeEnd = false;
/*  97 */     boolean afterByOrSetOrFromOrSelect = false;
/*  99 */     boolean afterOn = false;
/* 100 */     boolean afterBetween = false;
/* 101 */     boolean afterInsert = false;
/* 102 */     int inFunction = 0;
/* 103 */     int parensSinceSelect = 0;
/* 104 */     @SuppressWarnings("rawtypes")
private LinkedList parenCounts = new LinkedList();
/* 105 */     @SuppressWarnings("rawtypes")
private LinkedList afterByOrFromOrSelects = new LinkedList();
/*     */ 
/* 107 */     int indent = 1;
/*     */ 
/* 109 */     StringBuffer result = new StringBuffer();
/*     */     StringTokenizer tokens;
/*     */     String lastToken;
/*     */     String token;
/*     */     String lcToken;
/*     */ 
/* 116 */     public FormatProcess(String sql) { this.tokens = new StringTokenizer(
/* 117 */         sql, 
/* 118 */         "()+*/-=<>'`\"[], \n\r\f\t", 
/* 119 */         true);
/*     */     }
/*     */ 
/*     */     public String perform()
/*     */     {
/* 125 */       this.result.append("\n    ");
/*     */ 
/* 127 */       while (this.tokens.hasMoreTokens()) {
/* 128 */         this.token = this.tokens.nextToken();
/* 129 */         this.lcToken = this.token.toLowerCase();
/*     */ 
/* 131 */         if ("'".equals(this.token))
/*     */         {
/*     */           do {
/* 134 */             String t = this.tokens.nextToken();
/* 135 */             this.token += t;
/*     */ 
/* 137 */             if ("'".equals(t))
/*     */               break;
/*     */           }
/* 133 */           while (
/* 137 */             this.tokens.hasMoreTokens());
/*     */         }
/* 139 */         else if ("\"".equals(this.token)) {
/*     */           String t;
/*     */           do {
/* 142 */             t = this.tokens.nextToken();
/* 143 */             this.token += t;
/*     */           }
/* 141 */           while (!
/* 145 */             "\"".equals(t));
/*     */         }
/*     */ 
/* 148 */         if ((this.afterByOrSetOrFromOrSelect) && (",".equals(this.token))) {
/* 149 */           commaAfterByOrFromOrSelect();
/*     */         }
/* 151 */         else if ((this.afterOn) && (",".equals(this.token))) {
/* 152 */           commaAfterOn();
/*     */         }
/* 155 */         else if ("(".equals(this.token)) {
/* 156 */           openParen();
/*     */         }
/* 158 */         else if (")".equals(this.token)) {
/* 159 */           closeParen();
/*     */         }
/* 162 */         else if (BasicFormatterImpl.BEGIN_CLAUSES.contains(this.lcToken)) {
/* 163 */           beginNewClause();
/*     */         }
/* 166 */         else if (BasicFormatterImpl.END_CLAUSES.contains(this.lcToken)) {
/* 167 */           endNewClause();
/*     */         }
/* 170 */         else if ("select".equals(this.lcToken)) {
/* 171 */           select();
/*     */         }
/* 174 */         else if (BasicFormatterImpl.DML.contains(this.lcToken)) {
/* 175 */           updateOrInsertOrDelete();
/*     */         }
/* 178 */         else if ("values".equals(this.lcToken)) {
/* 179 */           values();
/*     */         }
/* 182 */         else if ("on".equals(this.lcToken)) {
/* 183 */           on();
/*     */         }
/* 186 */         else if ((this.afterBetween) && (this.lcToken.equals("and"))) {
/* 187 */           misc();
/* 188 */           this.afterBetween = false;
/*     */         }
/* 191 */         else if (BasicFormatterImpl.LOGICAL.contains(this.lcToken)) {
/* 192 */           logical();
/*     */         }
/* 195 */         else if (isWhitespace(this.token)) {
/* 196 */           white();
/*     */         }
/*     */         else
/*     */         {
/* 200 */           misc();
/*     */         }
/*     */ 
/* 203 */         if (!isWhitespace(this.token)) {
/* 204 */           this.lastToken = this.lcToken;
/*     */         }
/*     */       }
/*     */ 
/* 208 */       return this.result.toString();
/*     */     }
/*     */ 
/*     */     private void commaAfterOn() {
/* 212 */       out();
/* 213 */       this.indent -= 1;
/* 214 */       newline();
/* 215 */       this.afterOn = false;
/* 216 */       this.afterByOrSetOrFromOrSelect = true;
/*     */     }
/*     */ 
/*     */     private void commaAfterByOrFromOrSelect() {
/* 220 */       out();
/* 221 */       newline();
/*     */     }
/*     */ 
/*     */     private void logical() {
/* 225 */       if ("end".equals(this.lcToken)) {
/* 226 */         this.indent -= 1;
/*     */       }
/* 228 */       newline();
/* 229 */       out();
/* 230 */       this.beginLine = false;
/*     */     }
/*     */ 
/*     */     private void on() {
/* 234 */       this.indent += 1;
/* 235 */       this.afterOn = true;
/* 236 */       newline();
/* 237 */       out();
/* 238 */       this.beginLine = false;
/*     */     }
/*     */ 
/*     */     private void misc() {
/* 242 */       out();
/* 243 */       if ("between".equals(this.lcToken)) {
/* 244 */         this.afterBetween = true;
/*     */       }
/* 246 */       if (this.afterInsert) {
/* 247 */         newline();
/* 248 */         this.afterInsert = false;
/*     */       }
/*     */       else {
/* 251 */         this.beginLine = false;
/* 252 */         if ("case".equals(this.lcToken))
/* 253 */           this.indent += 1;
/*     */       }
/*     */     }
/*     */ 
/*     */     private void white()
/*     */     {
/* 259 */       if (!this.beginLine)
/* 260 */         this.result.append(" ");
/*     */     }
/*     */ 
/*     */     private void updateOrInsertOrDelete()
/*     */     {
/* 265 */       out();
/* 266 */       this.indent += 1;
/* 267 */       this.beginLine = false;
/* 268 */       if ("update".equals(this.lcToken)) {
/* 269 */         newline();
/*     */       }
/* 271 */       if ("insert".equals(this.lcToken))
/* 272 */         this.afterInsert = true;
/*     */     }
/*     */ 
/*     */     private void select()
/*     */     {
/* 277 */       out();
/* 278 */       this.indent += 1;
/* 279 */       newline();
/* 280 */       this.parenCounts.addLast(new Integer(this.parensSinceSelect));
/* 281 */       this.afterByOrFromOrSelects.addLast(Boolean.valueOf(this.afterByOrSetOrFromOrSelect));
/* 282 */       this.parensSinceSelect = 0;
/* 283 */       this.afterByOrSetOrFromOrSelect = true;
/*     */     }
/*     */ 
/*     */     private void out() {
/* 287 */       this.result.append(this.token);
/*     */     }
/*     */ 
/*     */     private void endNewClause() {
/* 291 */       if (!this.afterBeginBeforeEnd) {
/* 292 */         this.indent -= 1;
/* 293 */         if (this.afterOn) {
/* 294 */           this.indent -= 1;
/* 295 */           this.afterOn = false;
/*     */         }
/* 297 */         newline();
/*     */       }
/* 299 */       out();
/* 300 */       if (!"union".equals(this.lcToken)) {
/* 301 */         this.indent += 1;
/*     */       }
/* 303 */       newline();
/* 304 */       this.afterBeginBeforeEnd = false;
/* 305 */       this.afterByOrSetOrFromOrSelect = (("by".equals(this.lcToken)) || 
/* 306 */         ("set".equals(this.lcToken)) || 
/* 307 */         ("from".equals(this.lcToken)));
/*     */     }
/*     */ 
/*     */     private void beginNewClause() {
/* 311 */       if (!this.afterBeginBeforeEnd) {
/* 312 */         if (this.afterOn) {
/* 313 */           this.indent -= 1;
/* 314 */           this.afterOn = false;
/*     */         }
/* 316 */         this.indent -= 1;
/* 317 */         newline();
/*     */       }
/* 319 */       out();
/* 320 */       this.beginLine = false;
/* 321 */       this.afterBeginBeforeEnd = true;
/*     */     }
/*     */ 
/*     */     private void values() {
/* 325 */       this.indent -= 1;
/* 326 */       newline();
/* 327 */       out();
/* 328 */       this.indent += 1;
/* 329 */       newline();     }
/*     */ 
/*     */     private void closeParen() {
/* 334 */       this.parensSinceSelect -= 1;
/* 335 */       if (this.parensSinceSelect < 0) {
/* 336 */         this.indent -= 1;
/* 337 */         this.parensSinceSelect = ((Integer)this.parenCounts.removeLast()).intValue();
/* 338 */         this.afterByOrSetOrFromOrSelect = ((Boolean)this.afterByOrFromOrSelects.removeLast()).booleanValue();
/*     */       }
/* 340 */       if (this.inFunction > 0) {
/* 341 */         this.inFunction -= 1;
/* 342 */         out();
/*     */       }
/*     */       else {
/* 345 */         if (!this.afterByOrSetOrFromOrSelect) {
/* 346 */           this.indent -= 1;
/* 347 */           newline();
/*     */         }
/* 349 */         out();
/*     */       }
/* 351 */       this.beginLine = false;
/*     */     }
/*     */ 
/*     */     private void openParen() {
/* 355 */       if ((isFunctionName(this.lastToken)) || (this.inFunction > 0)) {
/* 356 */         this.inFunction += 1;
/*     */       }
/* 358 */       this.beginLine = false;
/* 359 */       if (this.inFunction > 0) {
/* 360 */         out();
/*     */       }
/*     */       else {
/* 363 */         out();
/* 364 */         if (!this.afterByOrSetOrFromOrSelect) {
/* 365 */           this.indent += 1;
/* 366 */           newline();
/* 367 */           this.beginLine = true;
/*     */         }
/*     */       }
/* 370 */       this.parensSinceSelect += 1;
/*     */     }
/*     */ 
/*     */     private static boolean isFunctionName(String tok) {
/* 374 */       char begin = tok.charAt(0);
/* 375 */       boolean isIdentifier = (Character.isJavaIdentifierStart(begin)) || ('"' == begin);
/*     */ 
/* 381 */       return (isIdentifier) && 
/* 377 */         (!BasicFormatterImpl.LOGICAL.contains(tok)) &&
/* 378 */         (!BasicFormatterImpl.END_CLAUSES.contains(tok)) &&
/* 379 */         (!BasicFormatterImpl.QUANTIFIERS.contains(tok)) &&
/* 380 */         (!BasicFormatterImpl.DML.contains(tok)) &&
/* 381 */         (!BasicFormatterImpl.MISC.contains(tok));
/*     */     }
/*     */ 
/*     */     private static boolean isWhitespace(String token) {
/* 385 */       return " \n\r\f\t".indexOf(token) >= 0;
/*     */     }
/*     */ 
/*     */     private void newline() {
/* 389 */       this.result.append("\n");
/* 390 */       for (int i = 0; i < this.indent; i++) {
/* 391 */         this.result.append("    ");
/*     */       }
/* 393 */       this.beginLine = true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\AnLuTong\Desktop\
 * Qualified Name:     org.hibernate.jdbc.util.BasicFormatterImpl
 * JD-Core Version:    0.6.2
 */