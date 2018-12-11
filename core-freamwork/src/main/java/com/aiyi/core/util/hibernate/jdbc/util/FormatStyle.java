package com.aiyi.core.util.hibernate.jdbc.util;

/*    */ 
/*    */ public class FormatStyle
/*    */ {
/* 33 */   public static final FormatStyle BASIC = new FormatStyle("basic", new BasicFormatterImpl());
/* 34 */   public static final FormatStyle DDL = new FormatStyle("ddl", new DDLFormatterImpl());
/* 35 */   public static final FormatStyle NONE = new FormatStyle("none", new NoFormatImpl());
/*    */   private final String name;
/*    */   private final Formatter formatter;
/*    */ 
/*    */   private FormatStyle(String name, Formatter formatter)
/*    */   {
/* 41 */     this.name = name;
/* 42 */     this.formatter = formatter;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 46 */     return this.name;
/*    */   }
/*    */ 
/*    */   public Formatter getFormatter() {
/* 50 */     return this.formatter;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o) {
/* 54 */     if (this == o) {
/* 55 */       return true;
/*    */     }
/* 57 */     if ((o == null) || (getClass() != o.getClass())) {
/* 58 */       return false;
/*    */     }
/*    */ 
/* 61 */     FormatStyle that = (FormatStyle)o;
/*    */ 
/* 63 */     return this.name.equals(that.name);
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 68 */     return this.name.hashCode();
/*    */   }
/*    */ 
/*    */   private static class NoFormatImpl implements Formatter {
/*    */     public String format(String source) {
/* 73 */       return source;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\AnLuTong\Desktop\
 * Qualified Name:     org.hibernate.jdbc.util.FormatStyle
 * JD-Core Version:    0.6.2
 */