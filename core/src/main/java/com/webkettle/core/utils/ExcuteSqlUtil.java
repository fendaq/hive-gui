package com.webkettle.core.utils;

public class ExcuteSqlUtil {

    public static boolean isSelect(String sql) {
        //执行SQL
        String[] s = sql.trim().split(" ");
        boolean isSelect = true;
        for (String sqlItem : s) {
            if (sqlItem.equalsIgnoreCase("DROP") ||
                    sqlItem.equalsIgnoreCase("DELETE") ||
                    sqlItem.equalsIgnoreCase("INSERT") ||
                    sqlItem.equalsIgnoreCase("UPDATE") ||
                    sqlItem.equalsIgnoreCase("CREATE") ||
                    sqlItem.equalsIgnoreCase("USE")) {
                return false;
            }
        }
        return true;
    }

    public static String limitQuerySql(String sql) {
        if (isSelect(sql)) {
            String[] s1 = sql.trim().split(" ");
            boolean hasLimit = true;
            if (s1.length < 2) {
                hasLimit = false;
            } else {
                try {
                    Integer integer = Integer.valueOf(s1[s1.length - 1]);
                    if ("limit".equalsIgnoreCase(s1[s1.length - 2].trim())) {
                        hasLimit = true;
                    }
                } catch (Exception e) {
                    hasLimit = false;
                }
            }
            if (!hasLimit) {
                sql += " LIMIT 10";
            }
        }
        return sql;
    }

    public static boolean isShow(String sql){
        String[] s = sql.trim().split(" ");
        for (String sqlItem : s) {
            if(sqlItem.equalsIgnoreCase("DESC") ||
                    sqlItem.equalsIgnoreCase("SHOW")) {
                return true;
            }
        }
        return false;
    }
}
