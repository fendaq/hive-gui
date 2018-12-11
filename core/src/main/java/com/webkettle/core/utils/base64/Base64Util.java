package com.webkettle.core.utils.base64;

import java.io.File;

/**
 * Base64操作类
 * @author AnLuTong
 *
 */
public interface Base64Util {
    /**
     * 功能：编码字符串
     *
     * @author 宋立君
     * @date 2014年07月03日
     * @param data
     *      源字符串
     * @return String
     */
    public String encode(String data);

    /**
     * 编码字符串
     * @param data
     * @param charsetName
     * @return
     */
    public String encode(String data, String charsetName);

    /**
     * 功能：解码字符串
     *
     * @author 宋立君
     * @date 2014年07月03日
     * @param data
     *      源字符串
     * @return String
     */
    public String decode(String data);

    /**
     * 解码字符串
     * @param data
     * @param charsetName
     * @return
     */
    public String decode(String data, String charsetName);

    /**
     * 功能：编码byte[]
     *
     * @author 宋立君
     * @date 2014年07月03日
     * @param data
     *      源
     * @return char[]
     */
    public char[] encode(byte[] data);

    /**
     * 功能：解码
     *
     * @author 宋立君
     * @date 2014年07月03日
     * @param data
     *      编码后的字符数组
     * @return byte[]
     */
    public byte[] decode(char[] data);

    /**
     * 功能：编码文件
     *
     * @author 宋立君
     * @date 2014年07月03日
     * @param file
     *      源文件
     */
    public void encode(File file);

    /**
     * 功能：解码文件。
     *
     * @author 宋立君
     * @date 2014年07月03日
     * @param file
     *      源文件
     * @throws IOException
     */
    public void decode(File file);



}

