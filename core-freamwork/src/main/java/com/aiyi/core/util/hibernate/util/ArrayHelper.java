package com.aiyi.core.util.hibernate.util;

/*     */ 
/*     */

import com.aiyi.core.util.hibernate.LockMode;
import com.aiyi.core.util.hibernate.type.Type;

import java.lang.reflect.Array;
import java.util.*;

/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ public final class ArrayHelper
/*     */ {
/* 185 */   public static final boolean[] TRUE = { true };
/* 186 */   public static final boolean[] FALSE = new boolean[1];
/*     */ 
/* 244 */   public static final String[] EMPTY_STRING_ARRAY = new String[0];
/* 245 */   public static final int[] EMPTY_INT_ARRAY = new int[0];
/* 246 */   public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
/* 247 */   @SuppressWarnings("rawtypes")
public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
/* 248 */   public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
/* 249 */   public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
/*     */ 
/* 279 */   private static int SEED = 23;
/* 280 */   private static int PRIME_NUMER = 37;
/*     */ 
/*     */   public static int indexOf(Object[] array, Object object)
/*     */   {
/*  47 */     for (int i = 0; i < array.length; i++) {
/*  48 */       if (array[i].equals(object)) return i;
/*     */     }
/*  50 */     return -1;
/*     */   }
/*     */ 
/*     */   public static String[] toStringArray(Object[] objects)
/*     */   {
/*  60 */     int length = objects.length;
/*  61 */     String[] result = new String[length];
/*  62 */     for (int i = 0; i < length; i++) {
/*  63 */       result[i] = objects[i].toString();
/*     */     }
/*  65 */     return result;
/*     */   }
/*     */ 
/*     */   public static String[] fillArray(String value, int length) {
/*  69 */     String[] result = new String[length];
/*  70 */     Arrays.fill(result, value);
/*  71 */     return result;
/*     */   }
/*     */ 
/*     */   public static int[] fillArray(int value, int length) {
/*  75 */     int[] result = new int[length];
/*  76 */     Arrays.fill(result, value);
/*  77 */     return result;
/*     */   }
/*     */ 
/*     */   public static LockMode[] fillArray(LockMode lockMode, int length) {
/*  81 */     LockMode[] array = new LockMode[length];
/*  82 */     Arrays.fill(array, lockMode);
/*  83 */     return array;
/*     */   }
/*     */ 
/*     */   public static String[] toStringArray(Collection<?> coll) {
/*  87 */     return (String[])coll.toArray(EMPTY_STRING_ARRAY);
/*     */   }
/*     */ 
/*     */   public static String[][] to2DStringArray(Collection<?> coll) {
/*  91 */     return (String[][])coll.toArray(new String[coll.size()][]);
/*     */   }
/*     */ 
/*     */   public static int[][] to2DIntArray(Collection<?> coll) {
/*  95 */     return (int[][])coll.toArray(new int[coll.size()][]);
/*     */   }
/*     */ 
/*     */   public static Type[] toTypeArray(Collection<?> coll) {
/*  99 */     return (Type[])coll.toArray(EMPTY_TYPE_ARRAY);
/*     */   }
/*     */ 
/*     */   public static int[] toIntArray(Collection<?> coll) {
/* 103 */     Iterator<?> iter = coll.iterator();
/* 104 */     int[] arr = new int[coll.size()];
/* 105 */     int i = 0;
/* 106 */     while (iter.hasNext()) {
/* 107 */       arr[(i++)] = ((Integer)iter.next()).intValue();
/*     */     }
/* 109 */     return arr;
/*     */   }
/*     */ 
/*     */   public static boolean[] toBooleanArray(Collection<?> coll) {
/* 113 */     Iterator<?> iter = coll.iterator();
/* 114 */     boolean[] arr = new boolean[coll.size()];
/* 115 */     int i = 0;
/* 116 */     while (iter.hasNext()) {
/* 117 */       arr[(i++)] = ((Boolean)iter.next()).booleanValue();
/*     */     }
/* 119 */     return arr;
/*     */   }
/*     */ 
/*     */   public static Object[] typecast(Object[] array, Object[] to) {
/* 123 */     return Arrays.asList(array).toArray(to);
/*     */   }
/*     */ 
/*     */   public static List<Object> toList(Object array)
/*     */   {
/* 128 */     if ((array instanceof Object[])) return Arrays.asList((Object[])array);
/* 129 */     int size = Array.getLength(array);
/* 130 */     ArrayList<Object> list = new ArrayList<Object>(size);
/* 131 */     for (int i = 0; i < size; i++) {
/* 132 */       list.add(Array.get(array, i));
/*     */     }
/* 134 */     return list;
/*     */   }
/*     */ 
/*     */   public static String[] slice(String[] strings, int begin, int length) {
/* 138 */     String[] result = new String[length];
/* 139 */     for (int i = 0; i < length; i++) {
/* 140 */       result[i] = strings[(begin + i)];
/*     */     }
/* 142 */     return result;
/*     */   }
/*     */ 
/*     */   public static Object[] slice(Object[] objects, int begin, int length) {
/* 146 */     Object[] result = new Object[length];
/* 147 */     for (int i = 0; i < length; i++) {
/* 148 */       result[i] = objects[(begin + i)];
/*     */     }
/* 150 */     return result;
/*     */   }
/*     */ 
/*     */   @SuppressWarnings({ "rawtypes", "unchecked" })
public static List<?> toList(Iterator<?> iter) {
/* 154 */     List list = new ArrayList<>();
/* 155 */     while (iter.hasNext()) {
/* 156 */       list.add(iter.next());
/*     */     }
/* 158 */     return list;
/*     */   }
/*     */ 
/*     */   public static String[] join(String[] x, String[] y) {
/* 162 */     String[] result = new String[x.length + y.length];
/* 163 */     for (int i = 0; i < x.length; i++) result[i] = x[i];
/* 164 */     for (int i = 0; i < y.length; i++) result[(i + x.length)] = y[i];
/* 165 */     return result;
/*     */   }
/*     */ 
/*     */   public static String[] join(String[] x, String[] y, boolean[] use) {
/* 169 */     String[] result = new String[x.length + countTrue(use)];
/* 170 */     for (int i = 0; i < x.length; i++) result[i] = x[i];
/* 171 */     int k = x.length;
/* 172 */     for (int i = 0; i < y.length; i++) {
/* 173 */       if (use[i]){
					result[(k++)] = y[i];
				} 
/*     */     }
/* 175 */     return result;
/*     */   }
/*     */ 
/*     */   public static int[] join(int[] x, int[] y) {
/* 179 */     int[] result = new int[x.length + y.length];
/* 180 */     for (int i = 0; i < x.length; i++) result[i] = x[i];
/* 181 */     for (int i = 0; i < y.length; i++) result[(i + x.length)] = y[i];
/* 182 */     return result;
/*     */   }
/*     */ 
/*     */   public static String toString(Object[] array)
/*     */   {
/* 191 */     StringBuffer sb = new StringBuffer();
/* 192 */     sb.append("[");
/* 193 */     for (int i = 0; i < array.length; i++) {
/* 194 */       sb.append(array[i]);
/* 195 */       if (i < array.length - 1) sb.append(",");
/*     */     }
/* 197 */     sb.append("]");
/* 198 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static boolean isAllNegative(int[] array) {
/* 202 */     for (int i = 0; i < array.length; i++) {
/* 203 */       if (array[i] >= 0) return false;
/*     */     }
/* 205 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isAllTrue(boolean[] array) {
/* 209 */     for (int i = 0; i < array.length; i++) {
/* 210 */       if (!array[i]) return false;
/*     */     }
/* 212 */     return true;
/*     */   }
/*     */ 
/*     */   public static int countTrue(boolean[] array) {
/* 216 */     int result = 0;
/* 217 */     for (int i = 0; i < array.length; i++) {
/* 218 */       if (array[i]) result++;
/*     */     }
/* 220 */     return result;
/*     */   }
/*     */ 
/*     */   public static boolean isAllFalse(boolean[] array)
/*     */   {
/* 232 */     for (int i = 0; i < array.length; i++) {
/* 233 */       if (array[i]) return false;
/*     */     }
/* 235 */     return true;
/*     */   }
/*     */ 
/*     */   public static void addAll(Collection<Object> collection, Object[] array) {
/* 239 */     for (int i = 0; i < array.length; i++)
/* 240 */       collection.add(array[i]);
/*     */   }
/*     */ 
/*     */   public static int[] getBatchSizes(int maxBatchSize)
/*     */   {
/* 252 */     int batchSize = maxBatchSize;
/* 253 */     int n = 1;
/* 254 */     while (batchSize > 1) {
/* 255 */       batchSize = getNextBatchSize(batchSize);
/* 256 */       n++;
/*     */     }
/* 258 */     int[] result = new int[n];
/* 259 */     batchSize = maxBatchSize;
/* 260 */     for (int i = 0; i < n; i++) {
/* 261 */       result[i] = batchSize;
/* 262 */       batchSize = getNextBatchSize(batchSize);
/*     */     }
/* 264 */     return result;
/*     */   }
/*     */ 
/*     */   private static int getNextBatchSize(int batchSize) {
/* 268 */     if (batchSize <= 10) {
/* 269 */       return batchSize - 1;
/*     */     }
/* 271 */     if (batchSize / 2 < 10) {
/* 272 */       return 10;
/*     */     }
/*     */ 
/* 275 */     return batchSize / 2;
/*     */   }
/*     */ 
/*     */   public static int hash(Object[] array)
/*     */   {
/* 286 */     int length = array.length;
/* 287 */     int seed = SEED;
/* 288 */     for (int index = 0; index < length; index++) {
/* 289 */       seed = hash(seed, array[index] == null ? 0 : array[index].hashCode());
/*     */     }
/* 291 */     return seed;
/*     */   }
/*     */ 
/*     */   public static int hash(char[] array)
/*     */   {
/* 298 */     int length = array.length;
/* 299 */     int seed = SEED;
/* 300 */     for (int index = 0; index < length; index++) {
/* 301 */       seed = hash(seed, array[index]);
/*     */     }
/* 303 */     return seed;
/*     */   }
/*     */ 
/*     */   public static int hash(byte[] bytes)
/*     */   {
/* 310 */     int length = bytes.length;
/* 311 */     int seed = SEED;
/* 312 */     for (int index = 0; index < length; index++) {
/* 313 */       seed = hash(seed, bytes[index]);
/*     */     }
/* 315 */     return seed;
/*     */   }
/*     */ 
/*     */   private static int hash(int seed, int i) {
/* 319 */     return PRIME_NUMER * seed + i;
/*     */   }
/*     */ 
/*     */   public static boolean isEquals(Object[] o1, Object[] o2)
/*     */   {
/* 326 */     if (o1 == o2) return true;
/* 327 */     if ((o1 == null) || (o2 == null)) return false;
/* 328 */     int length = o1.length;
/* 329 */     if (length != o2.length) return false;
/* 330 */     for (int index = 0; index < length; index++) {
/* 331 */       if (!o1[index].equals(o2[index])) return false;
/*     */     }
/* 333 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isEquals(char[] o1, char[] o2)
/*     */   {
/* 340 */     if (o1 == o2) return true;
/* 341 */     if ((o1 == null) || (o2 == null)) return false;
/* 342 */     int length = o1.length;
/* 343 */     if (length != o2.length) return false;
/* 344 */     for (int index = 0; index < length; index++) {
/* 345 */       if (o1[index] != o2[index]) return false;
/*     */     }
/* 347 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isEquals(byte[] b1, byte[] b2)
/*     */   {
/* 354 */     if (b1 == b2) return true;
/* 355 */     if ((b1 == null) || (b2 == null)) return false;
/* 356 */     int length = b1.length;
/* 357 */     if (length != b2.length) return false;
/* 358 */     for (int index = 0; index < length; index++) {
/* 359 */       if (b1[index] != b2[index]) return false;
/*     */     }
/* 361 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Users\AnLuTong\Desktop\
 * Qualified Name:     org.hibernate.util.ArrayHelper
 * JD-Core Version:    0.6.2
 */