package com.webkettle.core.utils;

import java.util.LinkedList;
import java.util.UUID;

/**
 * UUID工具类
 * @author gsk
 */
public class UUIDUtil {

    // ID缓冲容器
    private static final LinkedList<String> cacheListIds = new LinkedList<>();

    /**
     * 获取下一个Id值
     * @return
     *          UUID
     */
    public static synchronized final String next(){
        if (cacheListIds.size() < 1){
            initIdList();
        }
        return cacheListIds.removeFirst();
    }

    /**
     * 在ID序列里初始化一百个等待使用的ID
     */
    private static void initIdList(){
        for(int i = 0; i < 100; i ++){
            cacheListIds.addLast(UUID.randomUUID().toString().replace("-", ""));
        }
    }


}
