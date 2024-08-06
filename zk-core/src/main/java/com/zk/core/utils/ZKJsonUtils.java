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
 * @Title: ZKJsonUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 2:53:09 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.zk.core.commons.ZKJsonObjectMapper;
import com.zk.core.commons.data.ZKJson;

/** 
* @ClassName: ZKJsonUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJsonUtils {

    protected static Logger log = LogManager.getLogger(ZKJsonUtils.class);

    
    /**
     * json 字符串转 对象；
     *
     * @Title: parseObject
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 13, 2024 12:38:34 PM
     * @param <T>
     * @param jsonStr
     * @param classz
     * @return
     * @return T
     */
    public static <T> T parseObject(String jsonStr, Class<T> classz) {
        return ZKJsonObjectMapper.getInstance().fromJson(jsonStr, classz);
//        return JSON.parseObject(jsonStr, classz);
    }

    public static <T> T parseObject(String jsonStr, Class<T> classz, boolean failOnUnknownProperties) {
        ZKJsonObjectMapper objectMapper = ZKJsonObjectMapper.newMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
        return objectMapper.fromJson(jsonStr, classz);
    }

    public static <T> List<T> parseList(String jsonStr, Class<T> classz) {
        return JSON.parseArray(jsonStr, classz);
    }

    /**
     * 对象转 json 字符串
     *
     * @Title: toJsonStr
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 13, 2024 12:43:00 PM
     * @param obj
     * @return
     * @return String
     */
    public static String toJsonStr(Object obj) {
        return ZKJsonObjectMapper.getInstance().toJson(obj);
    }

    // 重载 ==================================================
    public static ZKJson parseZKJson(String jsonStr) {
        return parseObject(jsonStr, ZKJson.class);
    }

    public static JSONObject parseJSONObject(String jsonStr) {
        return parseObject(jsonStr, JSONObject.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> parseList(String jsonStr) {
//        return JSON.parseArray(jsonStr, null)
        return parseObject(jsonStr, List.class);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> parseMap(String jsonStr) {
        return parseObject(jsonStr, Map.class);
    }

}
