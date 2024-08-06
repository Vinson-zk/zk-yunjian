/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKEncodingUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:43:24 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

// import org.apache.commons.codec.binary.Base64.encodeBase64
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** 
* @ClassName: ZKEncodingUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEncodingUtils {

    protected static Logger log = LogManager.getLogger(ZKEncodingUtils.class);

    // 64 位编码
    // hex 编码
    // html 编码
    // 转双字节

    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    // private static final char[] hexChars = "0123456789abcdef".toCharArray();

    /**
     * 字节转为双字节
     *
     * @Title: byteInDouble
     * @Description: TODO(simple description this method what to do.)
     * @author zk
     * @date 2018年9月6日 上午11:38:18
     * @param input
     * @return
     * @return byte[]
     */
    public static byte[] byteInDouble(byte[] input) {
        byte[] bb = new byte[input.length * 2];
        for (int i = 0; i < input.length; i++) {
            bb[i] = (byte) ((input[i] & 0xf0) >> 4);
            bb[i + 1] = (byte) ((input[i] & 0xff >> 4));
        }
        return bb;
    }

    /**
     * 双字节转为单字节
     *
     * @Title: byteOutDouble
     * @Description: TODO(simple description this method what to do.)
     * @author zk
     * @date 2018年9月6日 上午11:38:13
     * @param input
     *            如果原字节数组长度不为双数，抛出异常
     * @return
     * @return byte[]
     */
    public static byte[] byteOutDouble(byte[] input) {
        if (input.length % 2 != 0) {
            throw new RuntimeException("字节长度不为双数，不是双字节，无法转单字节");
        }
        byte[] bb = new byte[input.length / 2];
        for (int i = 0; i < bb.length; i++) {
            bb[i] = (byte) (bb[i] & 0x00);
            bb[i] = (byte) (bb[i] | (input[i] & 0x0f));
            bb[i] = (byte) ((bb[i] << 4) | (input[i + 1] & 0x0f));
        }
        return bb;

    }

    /*** hex begin ***/
    /**
     * Hex编码.
     */
    public static String encodeHex(byte[] bytes) {
        return new String(Hex.encodeHex(bytes));
    }

    /**
     * Hex解码.
     */
    public static byte[] decodeHex(String hexStr) {
        try {
            return Hex.decodeHex(hexStr.toCharArray());
        }
        catch(Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            else {
                throw new RuntimeException(e);
            }
        }
    }

    /*** hex end ***/

    /*** Base64 begin ***/
    /**
     * Base64 编码.
     */
    public static byte[] encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encode(bytes);
//        return org.apache.commons.codec.binary.Base64.encodeBase64(bytes);
    }

    /**
     * Base64 编码.
     */
    public static String encodeBase64ToStr(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Base64 编码.
     */
    public static byte[] encodeBase64(String content) {

        try {
            return encodeBase64(content.getBytes(DEFAULT_URL_ENCODING));
//            return new String(Base64.encodeBase64(content.getBytes(DEFAULT_URL_ENCODING)));
        }
        catch(Exception e) {
            log.error("[>_<: 20180310-0955-001] {} encodeBase64 is fail; msg->{}", content, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Base64 编码.
     */
    public static String encodeBase64ToStr(String content) {

        try {
            return encodeBase64ToStr(content.getBytes(DEFAULT_URL_ENCODING));
//            return new String(Base64.encodeBase64(content.getBytes(DEFAULT_URL_ENCODING)));
        }
        catch(Exception e) {
            log.error("[>_<: 20180310-0955-001] {} encodeBase64 is fail; msg->{}", content, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Base64解码.
     */
    public static byte[] decodeBase64(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
//        return Base64.decodeBase64(bytes);
    }

    /**
     * Base64解码.
     */
    public static byte[] decodeBase64(String content) {
        return Base64.getDecoder().decode(content);
//        return Base64.decodeBase64(content);
    }

    /**
     * Base64解码.
     */
    public static String decodeBase64ToStr(byte[] bytes) {
        try {
            return new String(decodeBase64(bytes), DEFAULT_URL_ENCODING);
        }
        catch(Exception e) {
            log.error("[>_<: 20210219-0955-002] {} decodeBase64 is fail; msg->{}", bytes, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Base64解码.
     */
    public static String decodeBase64ToStr(String content) {
        try {
            return new String(decodeBase64(content), DEFAULT_URL_ENCODING);
        }
        catch(Exception e) {
            log.error("[>_<: 20180310-0955-002] {} decodeBase64 is fail; msg->{}", content, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
     */
    public static byte[] encodeUrlSafeBase64(byte[] input) {
        return Base64.getUrlEncoder().encode(input);
//        return Base64.encodeBase64URLSafe(input);
    }

    /*** Base64 end ***/

    /*** Base62 begin ***/
    /**
     * Base62编码。
     */
    public static String encodeBase62(byte[] input) {
        char[] chars = new char[input.length];
        for (int i = 0; i < input.length; i++) {
            chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
        }
        return new String(chars);
    }
    /*** Base62 end ***/

    /**
     * 
     * url 参数编码；
     * 
     * @Title: urlEncode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 21, 2020 3:17:07 PM
     * @param s
     * @return
     * @return String
     */
    public static String urlEncode(String s) {
        return urlEncode(s, "UTF-8");
    }

    /**
     * url 参数编码；
     *
     * @Title: urlEncode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 21, 2020 3:16:20 PM
     * @param s
     * @param enc
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String urlEncode(String s, String enc) {

        try {
            return URLEncoder.encode(s, enc);
        }
        catch(Exception e) {
            throw ZKExceptionsUtils.unchecked(e);
        }
    }

    /**
     * url 参数解码；
     *
     * @Title: urlDecoder
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 21, 2020 3:39:16 PM
     * @param s
     * @return
     * @throws UnsupportedEncodingException
     * @return String
     */
    public static String urlDecoder(String s) {
        return urlDecoder(s, "UTF-8");
    }

    /**
     * url 参数解码；
     *
     * @Title: urlDecoder
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 21, 2020 3:39:37 PM
     * @param s
     * @param enc
     * @return
     * @throws UnsupportedEncodingException
     * @return String
     */
    public static String urlDecoder(String s, String enc) {
        try {
            return URLDecoder.decode(s, enc);
        }
        catch(Exception e) {
            throw ZKExceptionsUtils.unchecked(e);
        }
    }
}
