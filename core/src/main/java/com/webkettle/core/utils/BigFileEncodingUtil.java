package com.webkettle.core.utils;

import javax.validation.ValidationException;
import java.io.*;
import java.nio.charset.Charset;

/**
 * 超大文件编码类
 * @author gsk
 */
public class BigFileEncodingUtil {

    /**
     * 重新编码大文件
     * @param in
     *          文件读入流
     * @param out
     *          文件输出流
     * @param inEncode
     *          文件读入编码
     * @param outEncode
     *          文件输出编码
     */
    public static void encodingFile(InputStream in, OutputStream out, String inEncode, String outEncode){
        Charset ie, oe = null;
        try {
            ie = Charset.forName(inEncode);
            oe = Charset.forName(outEncode);
        }catch (Exception e){
            throw new ValidationException("请传入正确的编码名称");
        }
        encodingFile(in, out, ie, oe);
    }

    /**
     * 重新编码大文件
     * @param in
     *          文件读入流
     * @param out
     *          文件输出流
     * @param inEncode
     *          文件读入编码
     * @param outEncode
     *          文件输出编码
     */
    public static void encodingFile(InputStream in, OutputStream out, Charset inEncode, Charset outEncode){
        try (BufferedReader inReader = new BufferedReader(new InputStreamReader(in, inEncode));
             BufferedWriter outReader = new BufferedWriter(new OutputStreamWriter(out, outEncode))) {
            char[] buffer = new char[4096];
            for (int i = inReader.read(buffer); i > 0; i = inReader.read(buffer)){
                outReader.write(buffer, 0, i);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }


}
