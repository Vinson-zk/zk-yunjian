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
 * @Title: ZKStringUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 2:36:28 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/** 
* @ClassName: ZKStringUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKStringUtils extends StringUtils {

    protected static Logger logger = LogManager.getLogger(ZKStringUtils.class);

    public static final char SEPARATOR = '_';

    public static final String EMPTY_STRING = "";

    /**
     * 默认字符编码级
     */
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";

    /**
     * 转换为字节数组
     * 
     * @param str
     * @return
     */
    public static byte[] getBytes(String str) {
        return getBytes(str, DEFAULT_CHARSET_NAME);
    }

    public static byte[] getBytes(String str, String enc) {
        try {
            return str.getBytes(enc);
        }
        catch(UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 对象转字符串
     *
     * @Title: toString
     * @Description:
     * @author Vinson
     * @date Aug 5, 2020 10:00:14 AM
     * @param obj
     * @return
     * @return String
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof String) {
            return (String) obj;
        }
        else {
            return ZKJsonUtils.toJsonStr(obj);
        }
    }

    /**
     * 转换为字节数组
     * 
     * @param str
     * @return
     */
    public static String toString(byte[] bytes) {
        return toString(bytes, DEFAULT_CHARSET_NAME);
    }

    public static String toString(byte[] bytes, String enc) {
        try {
            return new String(bytes, enc);
        }
        catch(UnsupportedEncodingException e) {
            return EMPTY;
        }
    }

    public static String toString(String str, String enc) {
        return toString(str.getBytes(), enc);
    }

    /**
     * str 字符串是否包含在 strs 字符串数组中；
     * 
     * @param str
     *            验证字符串
     * @param strs
     *            字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs) {
        if (str != null) {
            for (String s : strs) {
                if (str.equals(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 转换为Double类型
     */
    public static Double toDouble(Object val) {
        if (val == null) {
            return 0D;
        }
        try {
            return Double.valueOf(trim(val.toString()));
        }
        catch(Exception e) {
            return 0D;
        }
    }

    /**
     * 转换为Float类型
     */
    public static Float toFloat(Object val) {
        return toDouble(val).floatValue();
    }

    /**
     * 转换为Long类型
     */
    public static Long toLong(Object val) {
        return toDouble(val).longValue();
    }

    /**
     * 转换为Integer类型
     */
    public static Integer toInteger(Object val) {
        return toLong(val).intValue();
    }

    /**
     * 驼峰命名法工具
     * 
     * @return toCamelCase("hello_world") == "helloWorld"
     *         toCapitalizeCamelCase("hello_world") == "HelloWorld"
     *         toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            }
            else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     * 
     * @return toCamelCase("hello_world") == "helloWorld"
     *         toCapitalizeCamelCase("hello_world") == "HelloWorld"
     *         toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     * 
     * @return toCamelCase("hello_world") == "helloWorld"
     *         toCapitalizeCamelCase("hello_world") == "HelloWorld"
     *         toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            }
            else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * 左边补位
     * 
     * @param source
     * @param length
     * @param fillChar
     * @return
     */
    public static String leftFill(String source, long length, char fillChar) {
        if (source == null)
            source = "";
        for (long i = source.length(); i < length; ++i) {
            source = fillChar + source;
        }
        return source;
    }

    /**
     * 右边补位
     * 
     * @param source
     * @param length
     * @param fillChar
     * @return
     */
    public static String rightFill(String source, long length, char fillChar) {
        if (source == null)
            source = "";
        for (long i = source.length(); i < length; ++i) {
            source = source + fillChar;
        }
        return source;
    }

    /**
     * 四个字节时间转换
     * 
     * @param b1
     * @param b2
     * @param b3
     * @param b4
     * @return
     */
    public static int parse4ByteInt(byte b1, byte b2, byte b3, byte b4) {
        return ((b1 << 24) & 0xFF000000) | ((b2 << 16) & 0x00FF0000) | ((b3 << 8) & 0x0000FF00) | (b4 & 0x000000FF);
    }

    /**
     * 默认密码字符集合
     */
    public static final char[] passwordSourceChar = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890`-=[]\\;',./~!@#$%^&*()_+{}|:\"<>?"
            .toCharArray();

    /**
     * 密码验证
     * 
     * @param pwChar
     *            待验证的字符 char 数组
     * @param sourceChar
     *            允许的字符 char 数组，默认允许
     *            "qwertyuiopasdfghjklzxcvbnm1234567890`-=[]\\;',./~!@#$%^&*()_+{}|:\"<>?"，字符为英文字符
     * @param minLength
     *            允许的最小长度
     * @param maxLength
     *            允许的最大长度
     * @return 0-验证通过；1-长度不够；2-长度超长；3-格式不正确；4-密码为空；5-验证资源不足；
     */
    public static int checkPassword(char[] pwChar, char[] sourceChar, int minLength, int maxLength) {
        if (sourceChar == null || sourceChar.length == 0)
            sourceChar = passwordSourceChar;

        int result = 0;
        if (pwChar == null || pwChar.length == 0) {
            result = 4; // 4-密码为空
        }
        else {
            if (pwChar.length < minLength) {
                result = 1; // 1-长度不够
            }
            else if (pwChar.length > maxLength) {
                result = 2; // 2-长度超长
            }
            else {
                for (char pwC : pwChar) {
                    result = 3;
                    for (char c : sourceChar) {
                        if (c == pwC) {
                            result = 0;
                            break;
                        }
                    }
                    if (result == 3) {
                        break; // 格式不正确，不用继续验证；
                    }
                }
            }
        }

        return result;
    }

    static final Pattern is_number_pattern = Pattern.compile("((-?)|(\\+?))[0-9]+.?[0-9]+"); // 判断是不是数字的正则表达
                                                                                             // 式

    /**
     * 判断是不是数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        return is_number_pattern.matcher(str).matches();
    }

    /**
     * 数组结构标识字符
     */
    public static final String STRUCT_ATTAY_FLAG = "array";

    /**
     * 对象结构标识字符
     */
    public static final String STRUCT_OBJECT_FLAG = "struct";

    /**
     * 结构分隔开始标识字符
     */
    public static final String STRUCT_SEPARATE_FLAG_BEGIN = "<";

    /**
     * 结构分隔结束标识字符
     */
    public static final String STRUCT_SEPARATE_FLAG_END = ">";

    /**
     * 传入 json 字符串，转为 json 数据格式字符串
     * 
     * @param source
     *            传入 json 字符串
     * @return 返回的 json 数据格式字符串
     */
    public static String jsonStrToStructStr(String source) {
        try {
            // 统一转换 json 字符为 json 对象字符串
            JSONObject jsonObject = JSON.parseObject("{\"source\": " + source + "}");
            Object obj = jsonObject.get("source");
            return getStruct(obj);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据对象取出结构字符
     * 
     * @param obj
     * @return
     */
    private static String getStruct(Object obj) {
        StringBuffer resultStrBuffer = new StringBuffer();
        try {
            if (obj instanceof JSONArray) {
                resultStrBuffer.append(STRUCT_ATTAY_FLAG + STRUCT_SEPARATE_FLAG_BEGIN);
                resultStrBuffer.append(getStruct(((JSONArray) obj).get(0)));
                resultStrBuffer.append(STRUCT_SEPARATE_FLAG_END);
            }
            else if (obj instanceof JSONObject) {
                resultStrBuffer.append(STRUCT_OBJECT_FLAG + STRUCT_SEPARATE_FLAG_BEGIN);
//              Iterator<?> iterator = ((JSONObject) obj).keys();
                Set<String> keys = ((JSONObject) obj).keySet();
                Iterator<?> iterator = keys.iterator();
                Object key = iterator.next();
                while (key != null) {
                    resultStrBuffer.append(key);
                    resultStrBuffer.append(":");
                    resultStrBuffer.append(getStruct(((JSONObject) obj).get(key)));
                    key = null;
                    if (iterator.hasNext()) {
                        key = iterator.next();
                        resultStrBuffer.append(",");
                    }
                }
                resultStrBuffer.append(STRUCT_SEPARATE_FLAG_END);
            }
            else {
                resultStrBuffer.append("string");
            }
            return resultStrBuffer.toString();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean hasText(String str) {
        if (isEmpty(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 重写一下，给 JSP tld 使用；不能隐式做类型转换，真烦！
     *
     * @Title: isEmpty
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 30, 2019 4:15:39 PM
     * @param cs
     * @return
     * @return Boolean
     */
//    @Override
    public static boolean isEmpty(String cs) {
        return StringUtils.isEmpty(cs);
    }

    /**
     * 按指定分隔符分隔字符串
     *
     * @Title: tokenizeToStringArray
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 15, 2019 2:27:03 PM
     * @param str
     * @param delimiters
     *            指定分隔符
     * @return
     * @return String[]
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * 按指定分隔符分隔字符串
     *
     * @Title: tokenizeToStringArray
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 15, 2019 2:28:01 PM
     * @param str
     * @param delimiters
     *            指定分隔符
     * @param trimTokens
     *            是否移除前后空格
     * @param ignoreEmptyTokens
     *            是否忽略分隔时出现的空字符串
     * @return
     * @return String[]
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
            boolean ignoreEmptyTokens) {

        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List tokens = new ArrayList();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String[] toStringArray(Collection collection) {
        if (collection == null) {
            return null;
        }
        return (String[]) collection.toArray(new String[collection.size()]);
    }

    public static String clean(String in) {
        String out = in;

        if (in != null) {
            out = in.trim();
            if (out.equals(EMPTY_STRING)) {
                out = null;
            }
        }

        return out;
    }

    public static String[] split(String aLine, char delimiter, char beginQuoteChar, char endQuoteChar,
            boolean retainQuotes, boolean trimTokens) {
        String line = clean(aLine);
        if (line == null) {
            return null;
        }

        List<String> tokens = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char c = line.charAt(i);
            if (c == beginQuoteChar) {
                // this gets complex... the quote may end a quoted block, or
                // escape another quote.
                // do a 1-char lookahead:
                if (inQuotes // we zke in quotes, therefore there can be escaped
                             // quotes in here.
                        && line.length() > (i + 1) // there is indeed another
                                                   // character to check.
                        && line.charAt(i + 1) == beginQuoteChar) { // ..and that
                                                                   // char. is a
                                                                   // quote
                                                                   // also.
                    // we have two quote chars in a row == one quote char, so
                    // consume them both and
                    // put one on the token. we do *not* exit the quoted text.
                    sb.append(line.charAt(i + 1));
                    i++;
                }
                else {
                    inQuotes = !inQuotes;
                    if (retainQuotes) {
                        sb.append(c);
                    }
                }
            }
            else if (c == endQuoteChar) {
                inQuotes = !inQuotes;
                if (retainQuotes) {
                    sb.append(c);
                }
            }
            else if (c == delimiter && !inQuotes) {
                String s = sb.toString();
                if (trimTokens) {
                    s = s.trim();
                }
                tokens.add(s);
                sb = new StringBuilder(); // start work on next token
            }
            else {
                sb.append(c);
            }
        }
        String s = sb.toString();
        if (trimTokens) {
            s = s.trim();
        }
        tokens.add(s);
        return tokens.toArray(new String[tokens.size()]);
    }

    /**
     * 替换目标字符串中指定的字符
     *
     * @Title: replaceAll
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 26, 2019 8:06:01 PM
     * @param sourecStr
     * @param regex
     * @param replacement
     * @return
     * @return String
     */
    public static String replaceAll(String sourecStr, String regex, String replacement) {
        if (sourecStr != null) {
            return sourecStr.replaceAll(regex, replacement);
        }
        return sourecStr;
    }

    /**
     * 字符串转大写
     *
     * @Title: toUpperCase
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 26, 2019 8:05:58 PM
     * @param str
     * @return
     * @return String
     */
    public static String toUpperCase(String str) {
        if (str != null) {
            return str.toUpperCase();
        }
        return str;
    }

    /**
     * 字符串转小写
     *
     * @Title: toUpperCase
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 26, 2019 8:05:58 PM
     * @param str
     * @return
     * @return String
     */
    public static String toLowerCase(String str) {
        if (str != null) {
            return str.toLowerCase();
        }
        return str;
    }

    /**
     * 按正则表达式，匹配替换参数；
     * 
     * 不区分大小写匹配
     *
     * @Title: replaceByRegex
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 31, 2019 9:51:54 PM
     * @param str
     * @param regex
     * @param params
     * @return
     * @return String
     */
    public static String replaceByRegex(String str, String regex, String... params) {
        // 不区分大小写匹配
        return replaceByRegex(str, Pattern.compile(regex, Pattern.CASE_INSENSITIVE), params);
    }

    /**
     * 按正则表达式，顺序匹配替换参数；
     * 
     * @Title: replaceByRegex
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 31, 2019 9:53:19 PM
     * @param str
     * @param regex
     * @param params
     * @return
     * @return String
     */
    public static String replaceByRegex(String str, Pattern regex, String... params) {

        if (str == null || params.length < 1) {
            return str;
        }

        Matcher matcher = regex.matcher(str);
        StringBuffer sb = new StringBuffer();
        for (String param : params) {
//            matcher = regex.matcher(str);
            if (matcher.find()) {
                matcher = matcher.appendReplacement(sb, Matcher.quoteReplacement(param));
//                str = matcher.replaceFirst(Matcher.quoteReplacement(param));
            }
            else {
                break;
            }
        }
        matcher.appendTail(sb);
        return sb.toString();

    }

    /**
     * 替换标识符
     * 
     * @ClassName: Prefiix
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    private static interface Prefiix {

        public static final String dollarPrefix = "${";

        public static final String bracePrefix = "{";

        public static final String braceSuffix = "}";
    }

    /**
     * 根据参数名替换，注意，可里只做一级替换，不嵌套替换；
     *
     * @Title: replaceByName
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 5, 2020 10:15:55 AM
     * @param str
     * @param params
     * @return
     * @return String
     */
    public static String replaceByName(String str, Map<String, Object> params) {
        if (str == null) {
            return str;
        }

        if(params == null || params.isEmpty()) {
            return str;
        }
        for(Entry<String, Object> item:params.entrySet()) {
            str = str.replace(Prefiix.dollarPrefix + item.getKey() + Prefiix.braceSuffix, toString(item.getValue()));
        }
        return str;
    }

    /**
     * 正则替换占位符，占位符定义 {位置序号}，位置序号从 0 开始的有序自然数，替换参数；
     *
     * @Title: replaceByPoint
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 1, 2019 12:50:53 AM
     * @param str
     * @param params
     * @return
     * @return String
     */
    public static String replaceByPoint(String str, String... params) {
        return replaceByPoint(str, 0, params);
    }

    public static String replaceByPoint(String str, int paramOffset, String... params) {

        if (str == null) {
            return str;
        }

//      Pattern regex = null;
//      Matcher matcher = null;
//      for (int i = 0; i < params.length; ++i) {
//          regex = Pattern.compile("\\" + pointPrefix + i + "\\" + pointSuffix);
//          matcher = regex.matcher(str);
//          if (matcher.find()) {
//              str = matcher.replaceAll(Matcher.quoteReplacement(params[i]));
//          }
//      }
//
//      return str;

        for (int i = 0; i < (params.length - paramOffset); ++i) {
            str = str.replace(Prefiix.bracePrefix + i + Prefiix.braceSuffix, params[i + paramOffset]);
        }
        return str;
    }

    /**
     * 是否包含字符串
     * 
     * @param str
     *            验证字符串
     * @param strs
     *            字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定字符串编码字符集
     *
     * @Title: encodedString
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 17, 2019 10:41:19 PM
     * @param str
     * @param charset
     * @return
     * @return String
     */
    public static String encodedString(String str, String charset) {
        return ZKStringUtils.toEncodedString(str.getBytes(), Charset.forName(charset));
    }

}
