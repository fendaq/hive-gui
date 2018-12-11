package com.webkettle.core.utils;

import com.webkettle.core.entity.CollectionProp;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数据采集工具类
 * @author gsk
 */
public class CollectionUtil {

    private static long readTime;
    private static Map<String, CollectionProp> readCache;

    /**
     * 根据数据库和数据表获取对应的采集配置
     * @param tableName
     *          数据表名
     * @return
     */
    public static CollectionProp hasProp(String tableName){
        return readProp().get(tableName.toUpperCase());
    }


    /**
     * 读取表采集配置信息
     * @return
     */
    private static Map<String, CollectionProp> readProp(){
        if (readTime > System.currentTimeMillis()){
            return readCache;
        }
        Map<String, CollectionProp> cache = new LinkedHashMap<>();
        String props = new String(MinFieldUtil.readFile(
                new File(System.getProperty("user.dir"), "load-prop.txt"))).toUpperCase();
        props.replace("\r\n", "\n").replace("\n+", "\n");

        String[] split = props.split("\n");
        for(String line: split){
            line = line.trim();
            if (line.charAt(0) == '#'){
                continue;
            }
            int zs = line.indexOf('#');
            if (zs != -1){
                line = line.substring(0, zs);
            }
            String[] lineItem = line.split(":");

            if (lineItem.length >= 3){
                CollectionProp prop = new CollectionProp();
                prop.setDbName(lineItem[0]);
                prop.setTableName(lineItem[1]);
                prop.setType(lineItem[2]);
                if (lineItem.length >= 4){
                    prop.setTypeDscp(lineItem[3]);
                }
                cache.put(prop.getTableName(), prop);
            }
        }
        readCache = cache;
        readTime = System.currentTimeMillis() + 10000;
        return readCache;
    }
}
