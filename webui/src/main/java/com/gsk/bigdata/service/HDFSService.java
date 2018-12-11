package com.gsk.bigdata.service;

import com.webkettle.sql.entity.jobcreate.TableImport;
import org.apache.hadoop.fs.FileStatus;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * HDFS 业务操作类
 * @author gsk
 */
public interface HDFSService {

    /**
     * 向HDFS中put文件, 并返回这个文件在Hdfs中的唯一标识
     * @param prefix
     *          文件前缀(前缀路径, 比如用户名等)
     * @param stream
     *          要存储文件的流
     * @return
     */
    String put(String prefix, InputStream stream);

    /**
     * 向HDFS中put指定编码的文件, 然后返回这个文件在Hdfs中的唯一标识
     * @param prefix
     * @param stream
     * @param charset
     * @return
     */
    String put(String prefix, InputStream stream, String charset);

    /**
     * 从HDFS中读取某个文件
     * @param prefix
     *          文件前缀, 比如用户名等.
     * @param key
     *          文件在hdfs中的标识
     * @return
     */
    InputStream read(String prefix, String key);

    /**
     * 用绝对路径从HDFS中读取某个文件
     * @param path
     *          文件绝对路径
     * @return
     */
    InputStream read(String path);

    /**
     * 从HDFS中删除一个文件
     * @param prefix
     *          文件前缀
     * @param key
     *          文件在HDFS中的标识
     */
    void delete(String prefix, String key);

    /**
     * 预览准备导入到表的文件
     * @param tableImport
     *          文件导入配置
     * @return
     */
    List<LinkedHashMap<String, String>> display(TableImport tableImport);

    /**
     * 创建目录
     * @param path
     *          目录路径
     */
    void createPath(String path);

    /**
     * 列出目录下的文件列表
     * @param path
     *          目录
     * @return
     */
    FileStatus[] listFields(String path);
}
