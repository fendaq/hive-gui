package com.aiyi.core.util.hibernate.util;

/*     */ 
/*     */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;

/*     */
/*     */
/*     */

/*     */
/*     */ public final class StringHelper
/*     */ {
/*     */   @SuppressWarnings("unused")
private static final int ALIAS_TRUNCATE_LENGTH = 10;
/*     */   public static final String WHITESPACE = " \n\r\f\t";
/*     */ 
/*     */   public static int lastIndexOfLetter(String string)
/*     */   {
/*  48 */     for (int i = 0; i < string.length(); i++) {
/*  49 */       char character = string.charAt(i);
/*  50 */       if (!Character.isLetter(character)) return i - 1;
/*     */     }
/*  52 */     return string.length() - 1;
/*     */   }
/*     */ 
/*     */   public static String join(String seperator, String[] strings) {
/*  56 */     int length = strings.length;
/*  57 */     if (length == 0) return "";
/*  58 */     StringBuffer buf = new StringBuffer(length * strings[0].length())
/*  59 */       .append(strings[0]);
/*  60 */     for (int i = 1; i < length; i++) {
/*  61 */       buf.append(seperator).append(strings[i]);
/*     */     }
/*  63 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public static String join(String seperator, Iterator<?> objects) {
/*  67 */     StringBuffer buf = new StringBuffer();
/*  68 */     if (objects.hasNext()) buf.append(objects.next());
/*  69 */     while (objects.hasNext()) {
/*  70 */       buf.append(seperator).append(objects.next());
/*     */     }
/*  72 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public static String[] add(String[] x, String sep, String[] y) {
/*  76 */     String[] result = new String[x.length];
/*  77 */     for (int i = 0; i < x.length; i++) {
/*  78 */       result[i] = (x[i] + sep + y[i]);
/*     */     }
/*  80 */     return result;
/*     */   }
/*     */ 
/*     */   public static String repeat(String string, int times) {
/*  84 */     StringBuffer buf = new StringBuffer(string.length() * times);
/*  85 */     for (int i = 0; i < times; i++) buf.append(string);
/*  86 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public static String repeat(char character, int times) {
/*  90 */     char[] buffer = new char[times];
/*  91 */     Arrays.fill(buffer, character);
/*  92 */     return new String(buffer);
/*     */   }
/*     */ 
/*     */   public static String replace(String template, String placeholder, String replacement) {
/*  96 */     return replace(template, placeholder, replacement, false);
/*     */   }
/*     */ 
/*     */   public static String[] replace(String[] templates, String placeholder, String replacement) {
/* 100 */     String[] result = new String[templates.length];
/* 101 */     for (int i = 0; i < templates.length; i++) {
/* 102 */       result[i] = replace(templates[i], placeholder, replacement);
/*     */     }
/* 104 */     return result;
/*     */   }
/*     */ 
/*     */   public static String replace(String template, String placeholder, String replacement, boolean wholeWords) {
/* 108 */     if (template == null) {
/* 109 */       return template;
/*     */     }
/* 111 */     int loc = template.indexOf(placeholder);
/* 112 */     if (loc < 0) {
/* 113 */       return template;
/*     */     }
/*     */ 
/* 116 */     boolean actuallyReplace = (!wholeWords) || 
/* 117 */       (loc + placeholder.length() == template.length()) || 
/* 118 */       (!Character.isJavaIdentifierPart(template.charAt(loc + placeholder.length())));
/* 119 */     String actualReplacement = actuallyReplace ? replacement : placeholder;
/* 120 */     return template.substring(0, loc) + 
/* 121 */       actualReplacement + 
/* 122 */       replace(template.substring(loc + placeholder.length()), 
/* 123 */       placeholder, 
/* 124 */       replacement, 
/* 125 */       wholeWords);
/*     */   }
/*     */ 
/*     */   public static String replaceOnce(String template, String placeholder, String replacement)
/*     */   {
/* 131 */     if (template == null) {
/* 132 */       return template;
/*     */     }
/* 134 */     int loc = template.indexOf(placeholder);
/* 135 */     if (loc < 0) {
/* 136 */       return template;
/*     */     }
/*     */ 
/* 139 */     return template.substring(0, loc) + 
/* 140 */       replacement + 
/* 141 */       template.substring(loc + placeholder.length());
/*     */   }
/*     */ 
/*     */   public static String[] split(String seperators, String list)
/*     */   {
/* 148 */     return split(seperators, list, false);
/*     */   }
/*     */ 
/*     */   public static String[] split(String seperators, String list, boolean include) {
/* 152 */     StringTokenizer tokens = new StringTokenizer(list, seperators, include);
/* 153 */     String[] result = new String[tokens.countTokens()];
/* 154 */     int i = 0;
/* 155 */     while (tokens.hasMoreTokens()) {
/* 156 */       result[(i++)] = tokens.nextToken();
/*     */     }
/* 158 */     return result;
/*     */   }
/*     */ 
/*     */   public static String unqualify(String qualifiedName) {
/* 162 */     int loc = qualifiedName.lastIndexOf(".");
/* 163 */     return loc < 0 ? qualifiedName : qualifiedName.substring(loc + 1);
/*     */   }
/*     */ 
/*     */   public static String qualifier(String qualifiedName) {
/* 167 */     int loc = qualifiedName.lastIndexOf(".");
/* 168 */     return loc < 0 ? "" : qualifiedName.substring(0, loc);
/*     */   }
/*     */ 
/*     */   public static String collapse(String name)
/*     */   {
/* 180 */     if (name == null) {
/* 181 */       return null;
/*     */     }
/* 183 */     int breakPoint = name.lastIndexOf('.');
/* 184 */     if (breakPoint < 0) {
/* 185 */       return name;
/*     */     }
/* 187 */     return collapseQualifier(name.substring(0, breakPoint), true) + name.substring(breakPoint);
/*     */   }
/*     */ 
/*     */   public static String collapseQualifier(String qualifier, boolean includeDots)
/*     */   {
/* 199 */     StringTokenizer tokenizer = new StringTokenizer(qualifier, ".");
/* 200 */     String collapsed = Character.toString(tokenizer.nextToken().charAt(0));
/* 201 */     while (tokenizer.hasMoreTokens()) {
/* 202 */       if (includeDots) {
/* 203 */         collapsed = collapsed + '.';
/*     */       }
/* 205 */       collapsed = collapsed + tokenizer.nextToken().charAt(0);
/*     */     }
/* 207 */     return collapsed;
/*     */   }
/*     */ 
/*     */   public static String partiallyUnqualify(String name, String qualifierBase)
/*     */   {
/* 220 */     if ((name == null) || (!name.startsWith(qualifierBase))) {
/* 221 */       return name;
/*     */     }
/* 223 */     return name.substring(qualifierBase.length() + 1);
/*     */   }
/*     */ 
/*     */   public static String collapseQualifierBase(String name, String qualifierBase)
/*     */   {
/* 237 */     if ((name == null) || (!name.startsWith(qualifierBase))) {
/* 238 */       return collapse(name);
/*     */     }
/* 240 */     return collapseQualifier(qualifierBase, true) + name.substring(qualifierBase.length());
/*     */   }
/*     */ 
/*     */   public static String[] suffix(String[] columns, String suffix) {
/* 244 */     if (suffix == null) return columns;
/* 245 */     String[] qualified = new String[columns.length];
/* 246 */     for (int i = 0; i < columns.length; i++) {
/* 247 */       qualified[i] = suffix(columns[i], suffix);
/*     */     }
/* 249 */     return qualified;
/*     */   }
/*     */ 
/*     */   private static String suffix(String name, String suffix) {
/* 253 */     return name + suffix;
/*     */   }
/*     */ 
/*     */   public static String root(String qualifiedName) {
/* 257 */     int loc = qualifiedName.indexOf(".");
/* 258 */     return loc < 0 ? qualifiedName : qualifiedName.substring(0, loc);
/*     */   }
/*     */ 
/*     */   public static String unroot(String qualifiedName) {
/* 262 */     int loc = qualifiedName.indexOf(".");
/* 263 */     return loc < 0 ? qualifiedName : qualifiedName.substring(loc + 1, qualifiedName.length());
/*     */   }
/*     */ 
/*     */   public static boolean booleanValue(String tfString) {
/* 267 */     String trimmed = tfString.trim().toLowerCase();
/* 268 */     return (trimmed.equals("true")) || (trimmed.equals("t"));
/*     */   }
/*     */ 
/*     */   public static String toString(Object[] array) {
/* 272 */     int len = array.length;
/* 273 */     if (len == 0) return "";
/* 274 */     StringBuffer buf = new StringBuffer(len * 12);
/* 275 */     for (int i = 0; i < len - 1; i++) {
/* 276 */       buf.append(array[i]).append(", ");
/*     */     }
/* 278 */     return String.valueOf(array[(len - 1)]);
/*     */   }
/*     */ 
/*     */   public static String[] multiply(String string, Iterator<?> placeholders, Iterator<?> replacements) {
/* 282 */     String[] result = { string };
/* 283 */     while (placeholders.hasNext()) {
/* 284 */       result = multiply(result, (String)placeholders.next(), (String[])replacements.next());
/*     */     }
/* 286 */     return result;
/*     */   }
/*     */ 
/*     */   private static String[] multiply(String[] strings, String placeholder, String[] replacements) {
/* 290 */     String[] results = new String[replacements.length * strings.length];
/* 291 */     int n = 0;
/* 292 */     for (int i = 0; i < replacements.length; i++) {
/* 293 */       for (int j = 0; j < strings.length; j++) {
/* 294 */         results[(n++)] = replaceOnce(strings[j], placeholder, replacements[i]);
/*     */       }
/*     */     }
/* 297 */     return results;
/*     */   }
/*     */ 
/*     */   public static int countUnquoted(String string, char character) {
/* 301 */     if ('\'' == character) {
/* 302 */       throw new IllegalArgumentException("Unquoted count of quotes is invalid");
/*     */     }
/* 304 */     if (string == null) {
/* 305 */       return 0;
/*     */     }
/*     */ 
/* 309 */     int count = 0;
/* 310 */     int stringLength = string.length();
/* 311 */     boolean inQuote = false;
/* 312 */     for (int indx = 0; indx < stringLength; indx++) {
/* 313 */       char c = string.charAt(indx);
/* 314 */       if (inQuote) {
/* 315 */         if ('\'' == c) {
/* 316 */           inQuote = false;
/*     */         }
/*     */       }
/* 319 */       else if ('\'' == c) {
/* 320 */         inQuote = true;
/*     */       }
/* 322 */       else if (c == character) {
/* 323 */         count++;
/*     */       }
/*     */     }
/* 326 */     return count;
/*     */   }
/*     */ 
/*     */   @SuppressWarnings({ "rawtypes", "unchecked" })
public static int[] locateUnquoted(String string, char character) {
/* 330 */     if ('\'' == character) {
/* 331 */       throw new IllegalArgumentException("Unquoted count of quotes is invalid");
/*     */     }
/* 333 */     if (string == null) {
/* 334 */       return new int[0];
/*     */     }
/*     */ 
/* 337 */     ArrayList locations = new ArrayList(20);
/*     */ 
/* 342 */     int stringLength = string.length();
/* 343 */     boolean inQuote = false;
/* 344 */     for (int indx = 0; indx < stringLength; indx++) {
/* 345 */       char c = string.charAt(indx);
/* 346 */       if (inQuote) {
/* 347 */         if ('\'' == c) {
/* 348 */           inQuote = false;
/*     */         }
/*     */       }
/* 351 */       else if ('\'' == c) {
/* 352 */         inQuote = true;
/*     */       }
/* 354 */       else if (c == character) {
/* 355 */         locations.add(new Integer(indx));
/*     */       }
/*     */     }
/* 358 */     return ArrayHelper.toIntArray(locations);
/*     */   }
/*     */ 
/*     */   public static boolean isNotEmpty(String string) {
/* 362 */     return (string != null) && (string.length() > 0);
/*     */   }
/*     */ 
/*     */   public static boolean isEmpty(String string) {
/* 366 */     return (string == null) || (string.length() == 0);
/*     */   }
/*     */ 
/*     */   public static String qualify(String prefix, String name) {
/* 370 */     if ((name == null) || (prefix == null)) {
/* 371 */       throw new NullPointerException();
/*     */     }
/* 373 */     return prefix.length() + name.length() + 1 + 
/* 374 */       prefix + 
/* 375 */       '.' + 
/* 376 */       name;
/*     */   }
/*     */ 
/*     */   public static String[] qualify(String prefix, String[] names)
/*     */   {
/* 381 */     if (prefix == null) return names;
/* 382 */     int len = names.length;
/* 383 */     String[] qualified = new String[len];
/* 384 */     for (int i = 0; i < len; i++) {
/* 385 */       qualified[i] = qualify(prefix, names[i]);
/*     */     }
/* 387 */     return qualified;
/*     */   }
/*     */ 
/*     */   public static int firstIndexOfChar(String sqlString, String string, int startindex) {
/* 391 */     int matchAt = -1;
/* 392 */     for (int i = 0; i < string.length(); i++) {
/* 393 */       int curMatch = sqlString.indexOf(string.charAt(i), startindex);
/* 394 */       if (curMatch >= 0) {
/* 395 */         if (matchAt == -1) {
/* 396 */           matchAt = curMatch;
/*     */         }
/*     */         else {
/* 399 */           matchAt = Math.min(matchAt, curMatch);
/*     */         }
/*     */       }
/*     */     }
/* 403 */     return matchAt;
/*     */   }
/*     */ 
/*     */   public static String truncate(String string, int length) {
/* 407 */     if (string.length() <= length) {
/* 408 */       return string;
/*     */     }
/*     */ 
/* 411 */     return string.substring(0, length);
/*     */   }
/*     */ 
/*     */   public static String generateAlias(String description)
/*     */   {
/* 416 */     return generateAliasRoot(description) + '_';
/*     */   }
/*     */ 
/*     */   public static String generateAlias(String description, int unique)
/*     */   {
/* 429 */     return generateAliasRoot(description) + 
/* 430 */       Integer.toString(unique) + 
/* 431 */       '_';
/*     */   }
/*     */ 
/*     */   private static String generateAliasRoot(String description)
/*     */   {
/* 443 */     String result = truncate(unqualifyEntityName(description), 10)
/* 444 */       .toLowerCase()
/* 445 */       .replace('/', '_')
/* 446 */       .replace('$', '_');
/* 447 */     result = cleanAlias(result);
/* 448 */     if (Character.isDigit(result.charAt(result.length() - 1))) {
/* 449 */       return result + "x";
/*     */     }
/*     */ 
/* 452 */     return result;
/*     */   }
/*     */ 
/*     */   private static String cleanAlias(String alias)
/*     */   {
/* 464 */     char[] chars = alias.toCharArray();
/*     */ 
/* 466 */     if (!Character.isLetter(chars[0])) {
/* 467 */       for (int i = 1; i < chars.length; i++)
/*     */       {
/* 470 */         if (Character.isLetter(chars[i])) {
/* 471 */           return alias.substring(i);
/*     */         }
/*     */       }
/*     */     }
/* 475 */     return alias;
/*     */   }
/*     */ 
/*     */   public static String unqualifyEntityName(String entityName) {
/* 479 */     String result = unqualify(entityName);
/* 480 */     int slashPos = result.indexOf('/');
/* 481 */     if (slashPos > 0) {
/* 482 */       result = result.substring(0, slashPos - 1);
/*     */     }
/* 484 */     return result;
/*     */   }
/*     */ 
/*     */   public static String toUpperCase(String str) {
/* 488 */     return str == null ? null : str.toUpperCase();
/*     */   }
/*     */ 
/*     */   public static String toLowerCase(String str) {
/* 492 */     return str == null ? null : str.toLowerCase();
/*     */   }
/*     */ 
/*     */   public static String moveAndToBeginning(String filter) {
/* 496 */     if (filter.trim().length() > 0) {
/* 497 */       filter = filter + " and ";
/* 498 */       if (filter.startsWith(" and ")) filter = filter.substring(4);
/*     */     }
/* 500 */     return filter;
/*     */   }
/*     */ }

/* Location:           C:\Users\AnLuTong\Desktop\
 * Qualified Name:     org.hibernate.util.StringHelper
 * JD-Core Version:    0.6.2
 */