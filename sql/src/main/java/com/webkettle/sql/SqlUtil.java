package com.webkettle.sql;

import com.alibaba.fastjson.JSON;
import com.webkettle.core.utils.MinFieldUtil;
import com.webkettle.sql.entity.result.SqlAnsyResult;
import com.webkettle.sql.entity.result.SqlAnsyTempResult;
import org.springframework.util.StringUtils;

import javax.validation.ValidationException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * SQL操作类
 */
public class SqlUtil {

    public static void main(String[] args) {
//        String sql = new String(MinFieldUtil.readResource("testSql.sql"));
//        String sql = "USE TEST_NEW_DB1; create table aaaaa( aaaa string, bbbbb string, cccc string)";
//        long startTime = System.currentTimeMillis();
//        List<SqlAnsyResult> SqlAnsyTempResults = ansySqls(sql, "TEST_DB");
//        long endTime = System.currentTimeMillis();
//        System.out.println(JSON.toJSONString(SqlAnsyTempResults));
//        System.out.println("耗时:" + (endTime - startTime));
        System.out.println(Base64.getUrlEncoder().encodeToString(MinFieldUtil.readResource("testSql1.sql")));

    }

    /**
     * 剔除sql中的注释
     * @param sqls
     *          等待剔除注释的sql语句
     * @return
     */
    public static String removeNotes(String sqls){
        String newSqls = sqls.replace("\r\n", "\n");
        String[] split = newSqls.split("\n");
        StringBuffer buffer = new StringBuffer();

        for(String sqlLine: split){
            if ("".equals(sqlLine.trim())){
                continue;
            }
            char[] chars = sqlLine.toCharArray();
            boolean isXb = false;
            boolean isDb = false;
            boolean isNt = false;
            for(char c: chars){
                if (c == '\''){
                    isXb = !isXb;
                }else if (c == '"'){
                    isDb = !isDb;
                }else if (c == '-'){
                    if (!(isDb || isXb)){
                        if (isNt){
                            break;
                        }else{
                            isNt = true;
                            continue;
                        }
                    }
                }else if (isNt){
                    buffer.append('-');
                    isNt = false;
                }
                if (isDb || isXb){
                    buffer.append(c);
                }else{
                    buffer.append(Character.toUpperCase(c));
                }

            }
            buffer.append('\n');
        }
        return buffer.toString();
    }


    /**
     * 分割多条SQL语句
     * @param sql
     *          剔除注释的可能存在多条的sql语句
     * @return
     */
    public static List<String> splitSqls(String sql){
//        sql = sql.replace("\r", " ").replace("\n", " ")
//                .replace("\t", " ").replaceAll("\\s+", " ");

        boolean isXb = false;
        boolean isDb = false;
        boolean isKg = false;

        List<String> result = new ArrayList<>();

        StringBuffer sb = new StringBuffer();
        for(char c: sql.toCharArray()){
            if (c == '\t' || c == ' ' || c == '\r' || c == '\n'){
                if (!isXb && !isDb && !isKg){
                    sb.append(c);
                    isKg = true;
                    continue;
                }
            }else {
                isKg = false;
            }
            if (c == '\''){
                isXb = !isXb;
                sb.append("'");
                continue;
            }
            if (c == '"'){
                isDb = !isDb;
                sb.append("\"");
                continue;
            }
            if (c == ';'){
                if (!(isXb || isDb)){
                    result.add(sb.toString().trim());
                    sb.setLength(0);
                    continue;
                }
            }
            if (c == '(' && !isXb && !isDb){
                sb.append(" ").append(c).append(" ");
                continue;
            }
            if (c == ')' && !isXb && !isDb){
                sb.append(" ").append(c).append(" ");
                continue;
            }
            if (isDb || isXb){
                sb.append(c);
            }else{
                sb.append(Character.toUpperCase(c));
            }
        }
        if(!StringUtils.isEmpty(sb.toString().trim())){
            result.add(sb.toString().trim());
        }
        return result;
    }


    /**
     * 分析sql语句中的所有操作行为
     * @param sqls
     *          sql语句
     * @param defaultDbName
     *          默认选定数据库
     * @return
     */
    public static List<SqlAnsyResult> ansySqls(String sqls, String defaultDbName){
        sqls = sqls.replace("IF NOT EXISTS", "");
        if (StringUtils.isEmpty(defaultDbName)){
            throw new ValidationException("请先选定数据库");
        }
        String newSql = removeNotes(sqls.toUpperCase());
        List<String> sqlList = splitSqls(newSql);
        List<SqlAnsyResult> results = new ArrayList<>();

        // 使用dbName + tabName + action做索引, 提高效率 undefined
        Map<String, SqlAnsyTempResult> resMapping = new HashMap<>();

        for(String sql: sqlList){
            if (null == sql || sql.trim().equals("")){
                continue;
            }

            String[] s = sql.trim().split(" ");

            if (s.length < 2){
                continue;
            }

            SqlAnsyTempResult result = new SqlAnsyTempResult();
            boolean editTable = false;
            boolean isSelect = false;
            boolean isWish = false;
            boolean isWishIng = false;
            boolean isZK = false;
            boolean isXB = false;
            boolean isDb = false;
            boolean editDb = false;
            boolean editDatabase = false;
            boolean isShow = false;
            List<String> wishTables = new ArrayList<>();
            int subScript = 0;
            for(String sqlItem: s){
                if (sqlItem.trim().equals("")){
                    continue;
                }
                if (sqlItem.equals("USE")){
                    editDb = true;
                    continue;
                }
                if (sqlItem.equals("INSERT") || sqlItem.equals("DROP") || sqlItem.equals("CREATE") ||
                        sqlItem.equals("DELETE") || sqlItem.equals("UPDATE")){
                    if (result.getAction() != null){
                        resMapping.put(result.getTabName()
                                + "_" + result.getAction(), result);
                        result = new SqlAnsyTempResult();
                    }
                    result.setAction(sqlItem);
                    isSelect = false;
                    continue;
                }
                if (sqlItem.equals("INTO") || sqlItem.equals("OVERWRITE")){
                    continue;
                }
                if (sqlItem.equals("TABLE")){
                    editTable = true;
                    continue;
                }
                if (sqlItem.equals("DATABASE")){
                    editDatabase = true;
                    continue;
                }
                if (sqlItem.equals("SELECT")){
                    if (isSelect){
                        continue;
                    }
                    if (result.getAction() != null){
                        resMapping.put(result.getTabName()
                                + "_" + result.getAction(), result);
                        result = new SqlAnsyTempResult();
                    }
                    result.setAction("SELECT");
                    isSelect = true;
                    continue;
                }
                if (sqlItem.equals("SHOW")){
                    isShow = true;
//                    editDatabase = true;
                    result.setAction("SHOW");
                    continue;
                }
                if (sqlItem.equals("DATABASES")){
                    if (isShow){
                        throw new ValidationException("禁止执行SHOW DATABASES操作");
                    }
                    isShow = false;
                }
                if (sqlItem.equals("TABLES")){
                    if (isShow){
                        throw new ValidationException("禁止执行SHOW TABLES操作");
                    }
                    isShow = false;
                }
                if (sqlItem.equals("FROM") || (sqlItem.equals("JOIN") && isSelect)){
                    if (!isSelect){
                        if (result.getAction() != null){
                            resMapping.put(result.getTabName()
                                    + "_" + result.getAction(), result);
                            result = new SqlAnsyTempResult();
                        }
                        result.setAction("SELECT");
                    }
                    editTable = true;
                    continue;
                }
                if (sqlItem.equals("WITH")){
                    isWish = true;
                    isWishIng = true;
                    continue;
                }
                if (sqlItem.equals("(") && !isDb && !isXB){
                    isZK = true;
                    editTable = false;
                    subScript += 1;
                    continue;
                }
                if (sqlItem.equals("'")){
                    isXB = !isXB;
                    continue;
                }
                if (sqlItem.equals("\"")){
                    isDb = !isDb;
                    continue;
                }
                if (sqlItem.equals(")") && !isDb && !isXB){
                    isZK = false;
                    subScript -= 1;
                    continue;
                }
                if (isWish){
                    wishTables.add(sqlItem);
                    isWish = false;
                    continue;
                }
                if (sqlItem.equals(",") && isWishIng && subScript == 0){
                    isWish = true;
                    continue;
                }
                if (editDb){
                    defaultDbName = sqlItem;
                }
                if (editDatabase){
                    List<String> tabName = result.getTabName();
                    if (null == tabName){
                        tabName = new ArrayList<>();
                        result.setTabName(tabName);
                    }
                    tabName.add("DB:" + sqlItem);
                    editDatabase = false;
                    continue;
                }
                if (editTable){
                    if (wishTables.contains(sqlItem)){
                        editTable = false;
                        continue;
                    }
                    if (sqlItem.charAt(0) == '('){
                        editTable = false;
                        continue;
                    }
                    List<String> tabName = result.getTabName();
                    if (null == tabName){
                        tabName = new ArrayList<>();
                        result.setTabName(tabName);
                    }
                    if (sqlItem.contains(".")){
                        tabName.add(sqlItem);
                    }else {
                        tabName.add(defaultDbName + "." + sqlItem);
                    }

                    editTable = false;
                    continue;
                }

            }
            resMapping.put(result.getTabName()
                    + "_" + result.getAction(), result);
        }
        Set<String> strings = resMapping.keySet();
        HashMap<String, SqlAnsyResult> resultItem = new HashMap<>();
        for (String key: strings){
            SqlAnsyTempResult tempResult = resMapping.get(key);

            List<String> tabName = tempResult.getTabName();

            for (String tabNameItem: tabName){
                if (tempResult.getAction() == null && tabNameItem == null){
                    continue;
                }
                if (resultItem.get(tempResult.getAction() + ":" + tabNameItem) != null){
                    continue;
                }
                SqlAnsyResult resultfull = new SqlAnsyResult();
                resultfull.setAction(tempResult.getAction().toUpperCase());
                if (tabNameItem.charAt(0) == 'D' && tabNameItem.charAt(1) == 'B' &&
                        tabNameItem.charAt(2) == ':'){
                    resultfull.setDbName(tabNameItem.substring(3));
                    results.add(resultfull);
                    resultItem.put(tempResult.getAction() + ":" + tabNameItem, resultfull);
                    continue;
                }
                String[] dbAndName = tabNameItem.split("\\.");
                resultfull.setDbName(dbAndName[0].toUpperCase());
                resultfull.setTableName(dbAndName[1].toUpperCase());
                results.add(resultfull);
                resultItem.put(tempResult.getAction() + ":" + tabNameItem, resultfull);
            }
        }
        return results;
    }

    /**
     * 模拟预览Sql
     * @param sql
     *          等待预览的SQL
     * @return
     */
    public List<List<LinkedHashMap<String, Object>>> displaySql(String sql){
        List<String> sqls = splitSqls(removeNotes(sql));



        return null;
    }

}
