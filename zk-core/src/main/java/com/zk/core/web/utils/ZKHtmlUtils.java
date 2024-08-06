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
 * @Title: ZKHtmlUtils.java 
 * @author Vinson 
 * @Package com.zk.webmvc.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:54:02 PM 
 * @version V1.0   
*/
package com.zk.core.web.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.util.HtmlUtils;

import com.zk.core.utils.ZKCollectionUtils;
import com.zk.core.utils.ZKStringUtils;

/** 
* @ClassName: ZKHtmlUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKHtmlUtils extends HtmlUtils {

    protected static Logger logger = LogManager.getLogger(ZKHtmlUtils.class);

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     * 
     * @param txt
     * @return
     */
    public static String toHtml(String txt) {
        if (txt == null) {
            return "";
        }
        return ZKStringUtils.replace(ZKStringUtils.replace(escapeHtml4(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
    }

    /**
     * 替换掉HTML标签方法
     */
    public static String replaceHtml(String html) {
        if (ZKStringUtils.isBlank(html)) {
            return "";
        }
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");
        return s;
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     * 
     * @param html
     * @return
     */
    public static String replaceMobileHtml(String html) {
        if (html == null) {
            return "";
        }
        return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
    }

    /**
     * 缩略字符串（不区分中英文字符）
     * 
     * @param str
     *            目标字符串
     * @param length
     *            截取长度
     * @return
     */
    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3) {
                    sb.append(c);
                }
                else {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        }
        catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String abbr2(String param, int length) {
        if (param == null) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        int n = 0;
        char temp;
        boolean isCode = false; // 是不是HTML代码
        boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
        for (int i = 0; i < param.length(); i++) {
            temp = param.charAt(i);
            if (temp == '<') {
                isCode = true;
            }
            else if (temp == '&') {
                isHTML = true;
            }
            else if (temp == '>' && isCode) {
                n = n - 1;
                isCode = false;
            }
            else if (temp == ';' && isHTML) {
                isHTML = false;
            }
            try {
                if (!isCode && !isHTML) {
                    n += String.valueOf(temp).getBytes("GBK").length;
                }
            }
            catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (n <= length - 3) {
                result.append(temp);
            }
            else {
                result.append("...");
                break;
            }
        }
        // 取出截取字符串中的HTML标记
        String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
        // 去掉不需要结素标记的HTML标记
        temp_result = temp_result.replaceAll(
                "</?(ZKEA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|zkea|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
                "");
        // 去掉成对的HTML标记
        temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
        // 用正则表达式取出标记
        Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
        Matcher m = p.matcher(temp_result);
        List<String> endHTML = ZKCollectionUtils.newArrayList();
        while (m.find()) {
            endHTML.add(m.group(1));
        }
        // 补全不成对的HTML标记
        for (int i = endHTML.size() - 1; i >= 0; i--) {
            result.append("</");
            result.append(endHTML.get(i));
            result.append(">");
        }
        return result.toString();
    }

    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     * 
     * @param objectString
     *            对象串 例如：row.user.id
     *            返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString) {
        StringBuilder result = new StringBuilder();
        StringBuilder val = new StringBuilder();
        String[] vals = ZKStringUtils.split(objectString, ".");
        for (int i = 0; i < vals.length; i++) {
            val.append("." + vals[i]);
            result.append("!" + (val.substring(1)) + "?'':");
        }
        result.append(val.substring(1));
        return result.toString();
    }

    /**
     * Html 转码.
     */
    public static String escapeHtml4(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    /**
     * Html 解码.
     */
    public static String unescapeHtml4(String htmlEscaped) {
        return StringEscapeUtils.unescapeHtml4(htmlEscaped);
    }

    /**
     * Xml 转码.
     */
    public static String escapeXml(String xml) {
        return StringEscapeUtils.escapeXml10(xml);
    }

    /**
     * Xml 解码.
     */
    public static String unescapeXml(String xmlEscaped) {
        return StringEscapeUtils.unescapeXml(xmlEscaped);
    }

    /**
     * URL 编码, Encode默认为UTF-8.
     */
    public static String urlEncode(String part) {
        try {
            return URLEncoder.encode(part, ZKStringUtils.DEFAULT_CHARSET_NAME);
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

    /**
     * URL 解码, Encode默认为UTF-8.
     */
    public static String urlDecode(String part) {

        try {
            return URLDecoder.decode(part, ZKStringUtils.DEFAULT_CHARSET_NAME);
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

    public static String urlDecode(String part, String DEFAULT_CHARSET_NAME) {

        try {
            return URLDecoder.decode(part, DEFAULT_CHARSET_NAME);
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

}
