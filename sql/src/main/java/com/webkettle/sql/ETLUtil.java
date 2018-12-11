package com.webkettle.sql;

import com.aiyi.core.util.MD5;
import com.aiyi.core.util.SqlLog;
import com.alibaba.fastjson.JSON;
import com.webkettle.core.commons.CommAttr;
import com.webkettle.sql.entity.jobcreate.*;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.util.StringUtils;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * ETL配置工具类
 * @author gsk
 */
public class ETLUtil {

    /**
     * Etl配置转换为SQL
     * @param conf
     * @return
     *          转换出的SQL(多条)
     */
    public static List<String> conf2Sql(TableEtlConf conf){
        if (null == conf){
            throw new ValidationException("ETL配置不能为空");
        }
        if (StringUtils.isEmpty(conf.getDatabaseName())){
            throw new ValidationException("数据库名不能为空");
        }
        if (StringUtils.isEmpty(conf.getTableName())){
            throw new ValidationException("数据表名不能为空");
        }

        List<String> sqlList = new LinkedList<>();

        // 选择库
        StringBuffer sql = new StringBuffer();
        sql.append("USE ").append(conf.getDatabaseName());
        sqlList.add(SqlLog.formatSql(sql.toString()));

        if (null == conf.getInfo()){
            throw new ValidationException("ETL配置详情不能为空");
        }

        TableEtlConfInfo info = conf.getInfo();
        if (null == info.getColums() || info.getColums().size() < 1){
            throw new ValidationException("至少选择一列要处理的数据");
        }

        // 建表语句
        sql.setLength(0);
        sql.append("CREATE TABLE IF NOT EXISTS ").append(conf.getDatabaseName()).append(".").append(conf.getTableName());
        sql.append("( ");
        StringBuffer columsCreateBuffer = new StringBuffer();
        List<TableEtlColum> colums = new ArrayList<>();
        colums.addAll(info.getColums());
        if (null != info.getGroup() && null != info.getGroup().getSummary()){
            colums.addAll(info.getGroup().getSummary());
        }

        for(int i = 0; i < colums.size(); i++){
            columsCreateBuffer.append(colums.get(i).getColumName()).append(" ").append("string");
            if (i < colums.size() - 1){
                columsCreateBuffer.append(",");
            }
        }
        sql.append(columsCreateBuffer).append(" )");
        sql.append("PARTITIONED BY(event_date string)");
        sql.append(" STORED AS orc");
        sqlList.add(SqlLog.formatSql(sql.toString()));

        // ETL导入语句
        sql.setLength(0);
        sql.append("INSERT ");
        if (StringUtils.isEmpty(conf.getLoadType()) ||
                conf.getLoadType().equalsIgnoreCase(CommAttr.SQL.SQL_LOAD_APPEND)){
            sql.append("INTO TABLE ");
        }else{
            sql.append("OVERWRITE TABLE ");
        }
        sql.append(conf.getDatabaseName()).append(".").append(conf.getTableName());
        sql.append(" PARTITION(event_date='").append(DateFormatUtils.format(new Date(), "yyyyMMdd")).append("')");
        sql.append(" SELECT ");

        //转换列
        StringBuffer columsSelectBuffer = new StringBuffer();
        for(int i = 0; i < colums.size(); i++){
            columsSelectBuffer.setLength(0);
            if (StringUtils.isEmpty(colums.get(i).getType())){
                continue;
            }
            if (colums.get(i).getType().equalsIgnoreCase(CommAttr.ETL.COLUM_TYPE_FIELD)){
                columsSelectBuffer.append(colums.get(i).getTableName()).append(".").append(colums.get(i).getColumName());
            }else if (colums.get(i).getType().equalsIgnoreCase(CommAttr.ETL.COLUM_TYPE_VAR)){
                // TODO 值列, 这期不做
//                columsSelectBuffer.append(" '").append(colums.get(i).getColumName());
            }else{
                // 函数列
                String fun = colums.get(i).getType().substring(9);
                fun = fun.substring(0, fun.length() - 1);
                columsSelectBuffer.append(fun).append("(");
                List<TableEtlFunParams> params = colums.get(i).getParams();
                for(int j = 0; j < params.size(); j++){
                    if (params.get(j).getType().equalsIgnoreCase(CommAttr.ETL.COLUM_TYPE_FIELD)){
                        columsSelectBuffer.append(info.getSourceTable()).append(".").append(params.get(j).getValue());
                    }else{
                        columsSelectBuffer.append("'").append(params.get(j).getValue()).append("'");
                    }
                    if (j < params.size() - 1){
                        columsSelectBuffer.append(",");
                    }
                }
                columsSelectBuffer.append(") ");
            }

            if (null != colums.get(i).getQuality()){
                // 质量处理
                List<TableEtlQualityConf> quality = colums.get(i).getQuality();
                String rulesRel = "";
                for(TableEtlQualityConf q: quality){
                    String rel = " " + q.getRulesRel() + " ";
                    for(int j = 0; j < q.getRules().size(); j ++){
                        rulesRel += colums.get(i).getTableName() + "." + colums.get(i).getColumName() + " " +
                                q.getRules().get(j).getCalc() + " " + q.getRules().get(j).getRefer();
                        if (q.getRules().size() - 1 > j){
                            rulesRel += rel;
                        }
                    }

                    rulesRel = " CASE WHEN " + rulesRel + " THEN '" + q.getFullValue() + "' ELSE " +
                            columsSelectBuffer.toString() + " END ";
                    columsSelectBuffer.setLength(0);
                    columsSelectBuffer.append(rulesRel);

                }

//                for(TableEtlQualityRuleConf ruleConf: quality.getRules()){
//
////                    rulesRel = rulesRel.replace(ruleConf.getId(), ruleConf.getColum().getTableName() + "." +
////                            ruleConf.getColum().getColumName() + ruleConf.getCalc() + "'" + ruleConf.getRefer() + "' ");
//                }

            }

            columsSelectBuffer.append(" AS ").append(colums.get(i).getColumName()).append(" ");
            if (i < colums.size() - 1){
                columsSelectBuffer.append(",");
            }

            sql.append(columsSelectBuffer);
        }
        // 源表
        sql.append(" FROM ").append(info.getSourceDatabase()).append(".").append(info.getSourceTable()).append(" ")
                .append(info.getSourceTable()).append(" ");



        // 链表
        if (null != info.getJoins() && info.getJoins().size() > 0){
            for(TableEtlJoinConf joinConf: info.getJoins()){
                sql.append(joinConf.getType().replace("_", " ")).append(" ");
                sql.append(joinConf.getDatabaseName()).append(".").append(joinConf.getTableName()).append(" ");
                sql.append(joinConf.getTableName()).append(" ");
                if (null != joinConf.getOn() && joinConf.getOn().size() > 0){
                    sql.append(" ON ");

                    for(int i = 0; i < joinConf.getOn().size(); i++){
                        TableEtlJoinOnConf onConf = joinConf.getOn().get(i);
                        sql.append(joinConf.getDatabaseName()).append(".").append(onConf.getJoinColumName())
                                .append(" = ").append(info.getSourceDatabase()).append(".").append(onConf.getSourceColumName());
                        if (i < joinConf.getOn().size() - 1){
                            sql.append(" AND ");
                        }
                    }
                }
            }
        }

        // 分组
        if (null != info.getGroup()){
            sql.append(" GROUP BY ");
            TableEtlGroupConf group = info.getGroup();

            for(int i = 0; i < group.getGroup().size(); i++){
                TableEtlColum tableEtlColum = group.getGroup().get(i);
                sql.append(tableEtlColum.getTableName()).append(".").append(tableEtlColum.getColumName());
                if (i < group.getGroup().size() - 1){
                    sql.append(",");
                }
            }
        }

        sqlList.add(SqlLog.formatSql(sql.toString()));
        System.out.println(JSON.toJSONString(sqlList));
        return sqlList;
    }

    /**
     * 解析建表SQL语句, 并给出SQL中对应的表列
     * @param sql
     * @return
     */
    public static List<TableEtlColum> analysisCreateTableSql(String sql){
        List<TableEtlColum> result = new LinkedList<>();
        // 将SQL格式化为标准格式
        sql = sql.toUpperCase().replace("\t", " ").replace("\n", " ").
                replace("\r", " ").replaceAll(" +"," ");

        // 查找建表语句位置
        int startIndex = sql.indexOf("CREATE TABLE");
        if (startIndex == -1){
            throw new RuntimeException("未找到建表语句");
        }

        // 查找列位置
        int startClum = sql.indexOf("(");
        if (startClum == -1){
            throw new RuntimeException("请使用正确的建表语句");
        }

        char[] chars = sql.toCharArray();

        boolean isZk = false;
        boolean isZy = false;
        boolean isZxy = false;

        // 存储每列的SQL
        List<String> columStrList = new LinkedList<>();

        StringBuffer columBuffer = new StringBuffer();
        for(int i = startClum + 1; i < chars.length; i++){
            char c = chars[i];
            if (c == '('){
                isZk = true;
            }
            if (c == ')'){
                if (isZk){
                    isZk = false;
                }else{
                    columStrList.add(columBuffer.toString().trim());
                    break;
                }
            }
            if (c == '"'){
                isZy = !isZy;
            }
            if (c == '\''){
                isZxy = !isZxy;
            }

            if (c == ','){
                if (!(isZk || isZxy || isZy)){
                    columStrList.add(columBuffer.toString().trim());
                    columBuffer.setLength(0);
                    continue;
                }
            }

            columBuffer.append(c);
        }

        // 将列SQL解析为列对象
        for(String columSql: columStrList){
            String[] columItems = columSql.split(" ");
            if (columItems.length < 2 || columItems.length > 4){
                throw new RuntimeException("建表语句语法有误, 位置:" + columSql);
            }
            TableEtlColum colum = new TableEtlColum();
            colum.setTableName(columItems[0]);
            colum.setType(columItems[1]);
            try {
                if (columItems.length > 2){
                    String comment = "";
                    if (columItems.length == 3){
                        if (columItems[2].indexOf("COMMENT") != -1){
                            comment = columItems[3].replace("COMMENT", "");
                        }
                    }else{
                        StringBuffer commentBuffer = new StringBuffer();
                        for(int i = 3; i < columItems.length; i++){
                            commentBuffer.append(columItems[i]);
                        }
                        comment = commentBuffer.toString();
                    }
                    colum.setColumComment(trimComment(comment));
                }
                result.add(colum);
            }catch (Exception e){
                throw new RuntimeException("建表语句语法有误, 位置:" + columSql);
            }
        }
        return result;
    }

    /**
     * 去掉描述两边引号
     * @param comment
     * @return
     */
    private static String trimComment(String comment){
        if (comment.length() > 0){
            if (comment.charAt(0) == '\'' || comment.charAt(0) == '"' || comment.charAt(0) == '`'){
                comment = comment.substring(1, comment.length() - 1);
            }
        }
        return comment;
    }

    /**
     * 测试 ETL配置 序列化SQL
     * @param args
     */
    public static void main(String[] args){
//        Logger logger = Logger.getLogger("SQL");
//        String json = new String(MinFieldUtil.readFile(
//                new File("/Users/gsk/dev/project/big-data/webui/src/main/resources/static/test.json")));
//
//        logger.info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-ETL配置信息输出");
//        logger.info(json);
//        List<String> strings = conf2Sql(JSON.parseObject(json, TableEtlConf.class));
//
//
//        for(String sql: strings){
//            logger.info("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-SQL格式化语句输出");
//            logger.info(SqlLog.formatSql(sql));
//        }

//        String sql = "CREATE TABLE 数据库名.数据表名(\n" +
//                "    data_dt      date    comment \"数据日期\",\n" +
//                "    queriercertno    varchar(100)    comment \"被查询者证件号\",\n" +
//                "    borr_loan_currdelinq     decimal(38,6) , " +
//                "    comm_loan_currdelinq     decimal(38,6)    comment ''," +
//                "    part_loan_currdelinq     decimal(38,6)    comment \"配偶人行个贷当前逾期期数\",\n" +
//                "    guar_loan_currdelinq     decimal(38,6)    comment \"保证人人行个贷当前逾期期数\",\n" +
//                "    borr_crd_currdue_amt     decimal(38,6)    comment \"借款人人行信用卡当前逾期金额\",\n" +
//                "    comm_crd_currdue_amt     decimal(38,6)    comment \"共同借款人人行信用卡当前逾期金额\",\n" +
//                "    part_crd_currdue_amt     decimal(38,6)    comment \"配偶人行信用卡当前逾期金额\",\n" +
//                "    guar_crd_currdue_amt     decimal(38,6)    comment \"保证人人行信用卡当前逾期金额\"\n" +
//                "\n" +
//                "\n" +
//                ")comment \"贷后管理征信变量\"\n" +
//                "partitioned by (partition_day string)\n" +
//                "row format  serde 'org.apache.hadoop.hive.contrib.serde2.MultiDelimitSerDe' with  serdeproperties (\"field.delim\"=\"^|\")\n" +
//                "STORED AS TEXTFILE;\n";
//
//        List<TableEtlColum> tableEtlColums = analysisCreateTableSql(sql);
//
//        logger.info(JSON.toJSONString(tableEtlColums));
        System.out.println(MD5.getMd5("admin"));
    }



}
