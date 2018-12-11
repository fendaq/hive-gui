package com.gsk.bigdata.web;

import com.aiyi.core.beans.ResultBean;
import com.gsk.bigdata.service.HDFSService;
import com.webkettle.core.commons.CommAttr;
import com.webkettle.core.utils.ContextUtil;
import com.webkettle.sql.entity.jobcreate.TableImport;
import org.apache.hadoop.fs.FileStatus;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * 文件系统接口
 * @author gsk
 */
@RestController
@RequestMapping("api/data")
public class APIHdfsController {

    @Resource
    private HDFSService hdfsService;

    @Value("${bigdata.hdfs.prefix}")
    private String prefix;


    /**
     * 文件上传到hdfs
     * @param file
     *          客户端上传的文件
     * @return
     */
    @PostMapping("upload")
    public ResultBean upload(@RequestParam(value = "file", required = true) MultipartFile file, String charset){
        if (!(file.getContentType().toUpperCase().contains("TEXT") || file.getContentType().toUpperCase().contains("CSV"))){
            throw new RuntimeException("请上传文本格式的文件");
        }
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            String fileKey = hdfsService.put(prefix + ContextUtil.getUserName(), inputStream, charset);
            return ResultBean.success("文件保存成功").putResponseBody("fileKey", fileKey);
        }catch (IOException e){
            throw new RuntimeException("文件上传失败");
        }finally {
            try {
                inputStream.close();
            }catch (IOException e){
                throw new RuntimeException("网络异常");
            }
        }
    }

    /**
     * 预览文件
     * @param tableImport
     *          文件上传信息
     * @return
     */
    @PostMapping("display")
    public ResultBean display(@RequestBody TableImport tableImport){
        List<LinkedHashMap<String, String>> display = hdfsService.display(tableImport);
        List<LinkedHashMap<String, Object>> titles = new ArrayList<>();
        if (display.size() > 0){
            Set<String> keySet = display.get(0).keySet();
            for(String key: keySet){
                LinkedHashMap<String, Object> title = new LinkedHashMap<>();
                title.put("name", key);
                title.put("type", CommAttr.SQL.FIELD_TYPE.STRING.TYPE);
                title.put("typeDscp", CommAttr.SQL.FIELD_TYPE.STRING.DSCP);
                titles.add(title);
            }
        }
        return ResultBean.success("文件摘要预览")
                .putResponseBody("list", display)
                .putResponseBody("keyList", titles)
                .putResponseBody("isNewTable", "Y");
    }

    /**
     * 从hdfs中下载文件
     * @param value1
     * @param request
     * @param response
     */
    @RequestMapping(value = "/download/{fileKey}/**", method = RequestMethod.GET)
    public void getValue(@PathVariable("fileKey") String value1, HttpServletRequest request, HttpServletResponse response) {
        String fileKey = value1 + "/" + extractPathFromPattern(request);
        download(fileKey, response, request);
    }

    private String extractPathFromPattern(final HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }


    /**
     * 下载文件
     * @param fileKey
     *          文件标识
     */
    public void download(@PathVariable("*") String fileKey, HttpServletResponse response, HttpServletRequest request){
        int nameSi = fileKey.lastIndexOf('/');
        if (nameSi == -1){
            throw new RuntimeException("FileKey有误或文件不存在");
        }
        String fileName = fileKey.substring(nameSi  + 1);
        String prefixPath = prefix + ContextUtil.getUserName();
        if (prefixPath.charAt(0) != '/'){
            prefixPath = "/" + prefixPath;
        }
        if (prefixPath.charAt(prefixPath.length() - 1) != '/'){
            prefixPath += "/";
        }
        if (fileKey.charAt(0) == '/'){
            fileKey = fileKey.substring(1);
        }
        // 响应头
        response.setContentType("application/x-msdownload");

        // 解决火狐中文问题
        String agent = request.getHeader("USER-AGENT").toUpperCase();
        if(agent != null && agent.toLowerCase().indexOf("FIREFOX") > 0){
            fileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes(Charset.forName("UTF-8"))))) + "?=";
        }else{
            try{
                fileName =  URLEncoder.encode(fileName, "UTF-8");
            }catch (UnsupportedEncodingException e){
                throw new RuntimeException(e);
            }
        }

        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream out = null;
        FileStatus[] fileStatuses = hdfsService.listFields(prefixPath + fileKey);

        try {
            // 传输流
            out = response.getOutputStream();
            for (FileStatus fileStatus: fileStatuses){
                InputStream read = hdfsService.read(fileStatus.getPath().toString());
                byte[] buffer = new byte[4096];
                for(int i = read.read(buffer); i > 0; i = read.read(buffer)){
                    out.write(buffer, 0, i);
                    out.flush();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                out.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

}
