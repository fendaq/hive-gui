package com.webkettle.core.utils;

import java.nio.charset.Charset;
import java.util.Base64;

public class Test {

    public static void main(String[] args){
        String base64 = "c2VsZWN0ICogZnJvbSAiyse/7MDWtcS+zbeiwKy7+MnPtcSJhbfFvNnKsbzkt6jCySI=";

//        String decode1 = Hash.BASE_UTIL.decode(base64);
        byte[] decode = Base64.getDecoder().decode(base64);

        System.out.println(new String(decode, Charset.forName("GBK")));




    }
}
