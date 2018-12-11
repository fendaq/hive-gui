package com.gsk.bigdata.service.impl;

import com.gsk.bigdata.service.HDFSService;
import com.webkettle.core.utils.BigFileEncodingUtil;
import com.webkettle.core.utils.ContextUtil;
import com.webkettle.core.utils.StrUtil;
import com.webkettle.core.utils.UUIDUtil;
import com.webkettle.sql.entity.jobcreate.TableImport;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * HDFS 操作业务实现类
 * @author gsk
 */
@Service
public class HDFSServiceImpl implements HDFSService {

    @Resource
    private FileSystem fileSystem;

    @Value("${bigdata.hdfs.prefix}")
    private String prefix;

    @Override
    public String put(String prefix, InputStream stream) {
        String fileKey = getFileKey(prefix);
        OutputStream out = getOut(prefix, fileKey);
        try{
            IOUtils.copyBytes(stream, out,4096,true);
        }catch (IOException e){
            throw new RuntimeException("HDFS IO 通信异常", e);
        }finally {
            try{
                stream.close();
                if (null != out){
                    out.flush();
                    out.close();
                }
            }catch (IOException e){
                throw new RuntimeException("HDFS IO 通信异常", e);
            }
        }
        return fileKey;
    }

    @Override
    public String put(String prefix, InputStream stream, String charset) {
        if (StringUtils.isEmpty(charset)){
            return put(prefix, stream);
        }
        String fileKey = getFileKey(prefix);
        OutputStream out = getOut(prefix, fileKey);
        BigFileEncodingUtil.encodingFile(stream, out, charset, "UTF-8");
        return fileKey;
    }

    private String getFileKey(String prefix){
        String fileKey = DateFormatUtils.format(System.currentTimeMillis(), "yyyy/MM/dd");
        fileKey += ("/" + UUIDUtil.next() + ".file");
        return fileKey;
    }

    private OutputStream getOut(String prefix, String fileKey){
        if (prefix.charAt(0) != '/'){
            prefix = "/" + prefix;
        }
        if (prefix.charAt(prefix.length() - 1) != '/'){
            prefix += "/";
        }
        Path path = new Path(prefix + fileKey).getParent();
        OutputStream out = null;
        try{
            if(!fileSystem.exists(path)){
                fileSystem.mkdirs(path);
            }
            out = fileSystem.create(new Path(prefix + fileKey));
        }catch (IOException e){
            throw new RuntimeException("HDFS IO 通信异常", e);
        }
        return out;
    }

    @Override
    public InputStream read(String prefix, String key) {
        if (prefix.charAt(0) != '/'){
            prefix = "/" + prefix;
        }
        if (prefix.charAt(prefix.length() - 1) != '/'){
            prefix += "/";
        }
        if (key.charAt(0) == '/'){
            key = key.substring(1);
        }
        return read(prefix + key);
    }

    @Override
    public InputStream read(String path) {
        FSDataInputStream inputStream = null;
        try {
            // 防止Stream使用完后不关闭造成内存溢出, 转换为ByteArrayInputStream
            inputStream = fileSystem.open(new Path(path));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            for(int i = inputStream.read(buffer); i > 0; i = inputStream.read(buffer)){
                out.write(buffer, 0, i);
            }
            inputStream.close();
            return new ByteArrayInputStream(out.toByteArray());
        }catch (IOException e){
            throw new RuntimeException("HDFS IO 通信异常", e);
        }finally {
            try {
                if (null != inputStream){
                    inputStream.close();
                }
            }catch (IOException e){
                throw new RuntimeException("HDFS IO 通信异常", e);
            }
        }
    }

    @Override
    public void delete(String prefix, String key) {
        FSDataInputStream inputStream = null;
        String dataStr = null;
        try {
            Path filePath = new Path(key);
            if (fileSystem.exists(new Path(key))) {
                filePath = new Path(key = createPrefix() + key);
                if (!fileSystem.exists(filePath)) {
                    return;
                }
            }
            fileSystem.delete(filePath, true);
        }catch (IOException e){
            throw new RuntimeException("HDFS IO 通信异常", e);
        }
    }

    @Override
    public List<LinkedHashMap<String, String>> display(TableImport tableImport) {
        FSDataInputStream inputStream = null;
        String fileKey = tableImport.getFileKey();
        String dataStr = null;
        try {
            if(!fileSystem.exists(new Path(fileKey))){
                fileKey = createPrefix() + fileKey;
                if (!fileSystem.exists(new Path(fileKey))){
                    throw new RuntimeException("HDFS中不存在该文件!");
                }
            }

            // 防止Stream使用完后不关闭造成内存溢出, 转换为ByteArrayInputStream
            inputStream = fileSystem.open(new Path(fileKey));
            byte[] temp = new byte[4096];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 最多取出0.5M数据
            for(int i = inputStream.read(temp); i > 0 && out.toByteArray().length < 548576; i = inputStream.read(temp)){
                out.write(temp, 0, i);
            }
            try{
                dataStr = new String(out.toByteArray(), "UTF-8");
            }catch (Exception e){
                dataStr = new String(out.toByteArray());
            }
        }catch (IOException e){
            throw new RuntimeException("HDFS IO 通信异常", e);
        }finally {
            try {
                if (null != inputStream){
                    inputStream.close();
                }
            }catch (IOException e){
                throw new RuntimeException("HDFS IO 通信异常", e);
            }
        }

        // 或得行
        String[] lines = dataStr.split("\n");
        int showLines = lines.length;
        if (showLines > 10){
            showLines = 10;
        }

        // 获得列
        List<String> columTitles = new ArrayList<>();
        List<List<String>> columValues = new ArrayList<>();
        for(int i = 0; i < showLines; i++){
            String[] colums = StrUtil.split(lines[i], tableImport.getSplitChar());
            if (colums.length <= 0){
                continue;
            }
            List<String> values = new LinkedList<>();
            for(String str: colums){
                if (StringUtils.isEmpty(str)){
                    values.add("null");
                } else{
                    values.add(str);
                }
            }
            columValues.add(values);
            int start = columTitles.size();
            int end = colums.length;
            if (start < end){
                for(int j = start; j < end; j++){
                    columTitles.add("colum_" + j);
                }
            }
        }
        List<LinkedHashMap<String, String>> result = new LinkedList<>();
        for(List<String> lineValues: columValues){
            LinkedHashMap<String, String> line = new LinkedHashMap<>();
            for(int i = 0; i < columTitles.size(); i++){
                String val = null;
                if (lineValues.size() > i){
                    val = lineValues.get(i);
                }
                line.put(columTitles.get(i), val);
            }
            result.add(line);
        }
        return result;
    }

    @Override
    public void createPath(String path) {
        try {
            if(!fileSystem.exists(new Path(path))){
                fileSystem.mkdirs(new Path(path));
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileStatus[] listFields(String path) {
        try {
            return fileSystem.listStatus(new Path(path));
        }catch (IOException e){
            throw new RuntimeException("HDFS 读取失败", e);
        }
    }

    private String createPrefix(){
        String fileKey = prefix + ContextUtil.getUserName();
        if (fileKey.charAt(0) != '/'){
            fileKey = "/" + fileKey;
        }
        if (fileKey.charAt(fileKey.length() - 1) != '/'){
            fileKey += "/";
        }
        return fileKey;
    }

}
