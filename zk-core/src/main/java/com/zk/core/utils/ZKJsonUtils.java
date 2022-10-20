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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zk.core.commons.data.ZKJson;

/** 
* @ClassName: ZKJsonUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJsonUtils extends JSON {

    protected static Logger log = LoggerFactory.getLogger(ZKJsonUtils.class);

    public static ZKJson parseToZKJson(String jsonStr) {
        return ZKJsonUtils.jsonStrToObject(jsonStr, ZKJson.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonStrToList(String jsonStr) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将json字符串转成map结合解析出来，并打印(这里以解析成map为例)
            List<T> rList = objectMapper.readValue(jsonStr, List.class);
            return rList;
        }
        catch(Exception e) {
//            e.printStackTrace();
            throw ZKExceptionsUtils.unchecked(e);
        }
    }

    /**
     * 将json字符串转成map结合解析出来，并打印(这里以解析成map为例)
     * 
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> jsonStrToMap(String jsonStr) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将json字符串转成map结合解析出来，并打印(这里以解析成map为例)
            Map<K, V> map = objectMapper.readValue(jsonStr, Map.class);
            return map;
        }
        catch(Exception e) {
//            e.printStackTrace();
            throw ZKExceptionsUtils.unchecked(e);
        }
    }

    /**
     * 适用于 ModelView.addObject 返回的JSON,直接返回到前端时用
     * 
     * @param list
     * @return
     * @throws Exception
     */
    public static String writeObjectJsonForJs(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String result = "";
        try {
            result = mapper.writeValueAsString(obj);
            result = result.replaceAll("\\\\", "\\\\\\\\");
//          result = result.replaceAll("\"", "'");
            return result;
        }
        catch(Exception e) {
//            e.printStackTrace();
            throw ZKExceptionsUtils.unchecked(e);
        }
    }

    /**
     * 适用于ResponseBody返回的JSON
     * 
     * @param o
     * @return
     * @throws Exception
     */
    public static String writeObjectJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String result = "";
        try {
            result = mapper.writeValueAsString(obj);
            return result;
        }
        catch(Exception e) {
            e.printStackTrace();
            throw ZKExceptionsUtils.unchecked(e);
        }
    }

    /**
     * 将json字符串转成map结合解析出来，并打印(这里以解析成map为例)
     * 
     * @throws Exception
     */
    public static <T> T jsonStrToObject(String jsonStr, Class<T> classz) {
        return jsonStrToObject(jsonStr, classz, true);
    }

    public static <T> T jsonStrToObject(String jsonStr, Class<T> classz, boolean failOnUnknownProperties)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);

            // 将json字符串转成map结合解析出来，并打印(这里以解析成map为例)
            T obj = objectMapper.readValue(jsonStr, classz);
            return obj;
        }
        catch(Exception e) {
            e.printStackTrace();
            throw ZKExceptionsUtils.unchecked(e);
        }
    }

}
