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
 * @Title: ZKParamsUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 4:14:42 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/** 
* @ClassName: ZKParamsUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKParamsUtils {

    public static interface Param_Name {
        /**
         * 查询参数前缀
         */
        public static final String searchParamPrefix = "s_";
    }

    /**
     * 取出查询参数
     * 
     * @param params
     * @return
     */
    public static Map<String, Object> getSearchParams(Map<String, ?> params, String searchParamPrefix) {
        Map<String, Object> searchParams = new TreeMap<String, Object>();
        for (String key : params.keySet()) {
            if (ZKStringUtils.startsWith(key, searchParamPrefix)) {
                String name = ZKStringUtils.substringAfter(key, searchParamPrefix);
                Object value = params.get(key);
                searchParams.put(name, value);
            }
        }
        return searchParams;
    }

    /**
     * 将map集合的键值对转化成：key1=value1&key2=value2 的形式，并对值进行编码。默认字符集 UTF-8
     *
     * @Title: convertStringParamter
     * @Description: 将map集合的键值对转化成：key1=value1&key2=value2 的形式，并对值进行编码。默认字符集
     *               UTF-8
     * @author Vinson
     * @date Jun 29, 2019 11:38:20 AM
     * @param parameterMap
     *            需要转化的键值对集合
     * @return 结果字符串
     * @throws UnsupportedEncodingException
     * @return String
     */
    public static String convertStringParamter(Map<String, Object> parameterMap) throws UnsupportedEncodingException {
        return convertStringParamter(parameterMap, "UTF-8");
    }

    /**
     * 将map集合的键值对转化成：key1=value1&key2=value2 的形式，并对值进行编码。
     *
     * @Title: convertStringParamter
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 29, 2019 11:39:02 AM
     * @param parameterMap
     *            需要转化的键值对集合
     * @param enc
     *            字符集
     * @return
     * @throws UnsupportedEncodingException
     * @return String
     */
    public static String convertStringParamter(Map<String, Object> parameterMap, String enc)
            throws UnsupportedEncodingException {
        StringBuffer parameterBuffer = new StringBuffer();

        if (parameterMap == null) {
            return parameterBuffer.toString();
        }

        Iterator<Entry<String, Object>> iterator = parameterMap.entrySet().iterator();
        Entry<String, Object> e = null;
        Object value = null;
        Collection<?> vCollection = null;
        Object[] vArray = null;
        Iterator<?> vIterator = null;
        while (iterator.hasNext()) {
            e = iterator.next();
            value = e.getValue();

            if (value instanceof Collection<?>) {
                vCollection = (Collection<?>) value;
                vIterator = vCollection.iterator();

                while (vIterator.hasNext()) {
                    value = vIterator.next();
                    parameterBuffer.append(e.getKey()).append("=")
                            .append(URLEncoder.encode(getStringByObject(value), enc));
                    if (vIterator.hasNext()) {
                        parameterBuffer.append("&");
                    }
                }
            }
            else if (value.getClass().isArray()) {
                vArray = (Object[]) value;

                for (int i = 0; i < vArray.length; ++i) {
                    parameterBuffer.append(e.getKey()).append("=")
                            .append(URLEncoder.encode(getStringByObject(vArray[i]), enc));

                    if (i < (vArray.length - 1)) {
                        parameterBuffer.append("&");
                    }
                }
            }
            else {
                parameterBuffer.append(e.getKey()).append("=")
                        .append(URLEncoder.encode(getStringByObject(value), enc));
            }

            if (iterator.hasNext()) {
                parameterBuffer.append("&");
            }
        }

        return parameterBuffer.toString();
    }

    private static String getStringByObject(Object o) {
        if (o == null) {
            return "";
        }
        else if (ZKObjectUtils.isBaseType(o) || o instanceof String) {
            return o.toString();
        }
        else {
            return ZKJsonUtils.toJsonStr(o);
        }
    }

}
