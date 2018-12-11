package com.webkettle.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串工具类
 * @author gsk
 */
public class StrUtil {

    /**
     * 不是正则的字符串分割
     * @param old
     *          待分割的字符串
     * @param splitChar
     *          分隔符
     * @return
     */
    public static String[] split(String old, String splitChar){
        List<String> result = new ArrayList<>();
        int readSplitIndex = 0;
        char[] oldChars = old.toCharArray();
        char[] splitChars = splitChar.toCharArray();
        StringBuffer buffer = new StringBuffer();

        for(char c: oldChars){
            if (c == splitChars[readSplitIndex]){
                if (readSplitIndex == splitChars.length - 1){
                    readSplitIndex = 0;
                    result.add(buffer.toString());
                    buffer.setLength(0);
                    continue;
                }else{
                    readSplitIndex ++;
                    continue;
                }
            }
            buffer.append(c);
        }
        if (buffer.length() > 0){
            result.add(buffer.toString());
        }
        return result.toArray(new String[]{});
    }
}
