package com.aiyi.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**
     * 普通格式化：yyyy-MM-dd
     * @return
     */
    public static String format(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    /**
     * 普通格式化：yyyy-MM-dd
     * @return
     */
    public static String format(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(time);
    }

    /**
     * 普通格式化：yyyy-MM-dd
     * @return
     */
    public static String format(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * 带时分秒：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatAll(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    /**
     * 带时分秒：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatAll(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(time);
    }

    /**
     * 带时分秒：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatAll(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 中文：yyyy年MM月dd日
     * @return
     */
    public static String formatCN(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    /**
     * 中文：yyyy年MM月dd日
     * @return
     */
    public static String formatCN(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return simpleDateFormat.format(time);
    }

    /**
     * 中文：yyyy年MM月dd日
     * @return
     */
    public static String formatCN(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return simpleDateFormat.format(date);
    }

    /**
     * 中文带时分秒：yyyy年MM月dd日 HH时mm分ss秒
     * @return
     */
    public static String formatCNAll(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    /**
     * 中文带时分秒：yyyy年MM月dd日 HH时mm分ss秒
     * @return
     */
    public static String formatCNAll(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return simpleDateFormat.format(time);
    }

    /**
     * 中文带时分秒：yyyy年MM月dd日 HH时mm分ss秒
     * @return
     */
    public static String formatCNAll(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return simpleDateFormat.format(date);
    }

    /**
     * 自定义
     * @param pra
     * @return
     */
    public static String formatPramm(String pra){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pra);
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    /**
     * 自定义
     * @param pra
     * @return
     */
    public static String formatPramm(long time, String pra){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pra);
        return simpleDateFormat.format(time);
    }

    /**
     * 自定义
     * @param pra
     * @return
     */
    public static String formatPramm(Date time, String pra){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pra);
        return simpleDateFormat.format(time);
    }

}
