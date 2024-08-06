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
 * @Title: ZKEnvironmentUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:49:49 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import com.zk.core.commons.ZKEnvironment;

/** 
* @ClassName: ZKEnvironmentUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEnvironmentUtils {

    protected static Logger log = LogManager.getLogger(ZKEnvironmentUtils.class);

    private static ZKEnvironment zkEnv = null;

    public static WebApplicationType getWebApplicationType() {
        return zkEnv == null ? WebApplicationType.NONE : zkEnv.getWebApplicationType();
    }

    public static void setZKEnvironment(ZKEnvironment zkEnv) {
        ZKEnvironmentUtils.zkEnv = zkEnv;
    }

    public static ZKEnvironment getZKEnvironment() {
        return ZKEnvironmentUtils.zkEnv;
    }

    public static ApplicationContext getApplicationContext() {
        return zkEnv.getApplicationContext();
    }

    /**
     * @return env
     */
    public static Environment getEnvironment() {
        return zkEnv.getEnvironment();
    }

    /**
     * 取 字符串 配置属性
     *
     * @Title: getString
     * @author Vinson
     * @date Mar 16, 2019 7:40:10 PM
     * @param key
     * @return
     * @return String
     */
    public static String getString(String key) {
        return zkEnv.getString(key);
    }

    /**
     * 取 字符串 配置属性；不存在返回 默认值；
     *
     * @Title: getString
     * @author Vinson
     * @date Mar 16, 2019 7:40:36 PM
     * @param key
     * @param defaultValue
     * @return
     * @return String
     */
    public static String getString(String key, String defaultValue) {
        return zkEnv == null ? defaultValue : zkEnv.getString(key, defaultValue);
    }

    /**
     * 取 数字 Integer 配置属性；
     *
     * @Title: getInteger
     * @author Vinson
     * @date Mar 16, 2019 7:41:04 PM
     * @param key
     * @return
     * @return Integer
     */
    public static Integer getInteger(String key) {
        return zkEnv.getInteger(key);
    }

    /**
     * 取 数字 Integer 配置属性；不存在、错误值时返回 默认值；
     *
     * @Title: getInteger
     * @author Vinson
     * @date Mar 16, 2019 7:41:04 PM
     * @param key
     * @param defaultValue
     * @return
     * @return Integer
     */
    public static Integer getInteger(String key, Integer defaultValue) {
        return zkEnv == null ? defaultValue : zkEnv.getInteger(key, defaultValue);
    }

    /**
     * 取 数字 Long 配置属性；
     *
     * @Title: getLong
     * @author Vinson
     * @date Mar 16, 2019 7:41:04 PM
     * @param key
     * @return
     * @return Long
     */
    public static Long getLong(String key) {
        return zkEnv.getLong(key);
    }

    /**
     * 取 数字 Long 配置属性；不存在、错误值时返回 默认值；
     *
     * @Title: getLong
     * @author Vinson
     * @date Mar 16, 2019 7:41:04 PM
     * @param key
     * @param defaultValue
     * @return
     * @return Long
     */
    public static Long getLong(String key, Long defaultValue) {
        return zkEnv == null ? defaultValue : zkEnv.getLong(key, defaultValue);
    }

    /**
     * 取 数字 Double 配置属性；
     *
     * @Title: getDouble
     * @author Vinson
     * @date Mar 16, 2019 7:41:04 PM
     * @param key
     * @return
     * @return Double
     */
    public static Double getDouble(String key) {
        return zkEnv.getDouble(key);
    }

    /**
     * 取 数字 Double 配置属性；不存在、错误值时返回 默认值；
     *
     * @Title: getDouble
     * @author Vinson
     * @date Mar 16, 2019 7:41:04 PM
     * @param key
     * @param defaultValue
     * @return
     * @return Double
     */
    public static Double getDouble(String key, Double defaultValue) {
        return zkEnv == null ? defaultValue : zkEnv.getDouble(key, defaultValue);
    }

    /**
     * 取 数字 Float 配置属性；
     *
     * @Title: getFloat
     * @author Vinson
     * @date Mar 16, 2019 7:41:04 PM
     * @param key
     * @return
     * @return Float
     */
    public static Float getFloat(String key) {
        return zkEnv.getFloat(key);
    }

    /**
     * 取 数字 Float 配置属性；不存在、错误值时返回 默认值；
     *
     * @Title: getFloat
     * @author Vinson
     * @date Mar 16, 2019 7:41:04 PM
     * @param key
     * @param defaultValue
     * @return
     * @return Float
     */
    public static Float getFloat(String key, Float defaultValue) {
        return zkEnv == null ? defaultValue : zkEnv.getFloat(key, defaultValue);
    }

    /**
     * 取 数字 Boolean 配置属性；
     * 
     * 1、true 返回 true；0、false 返回 false
     *
     * @Title: getBoolean
     * @author Vinson
     * @date Mar 16, 2019 7:41:04 PM
     * @param key
     * @return
     * @return Boolean
     */
    public static Boolean getBoolean(String key) {
        return zkEnv.getBoolean(key);
    }

    /**
     * 取 数字 Boolean 配置属性；不存在、为空时返回、不可转换时 默认值；
     * 
     * 1、true 返回 true；0、false 返回 false
     *
     * @Title: getBoolean
     * @author Vinson
     * @date Mar 16, 2019 7:41:04 PM
     * @param key
     * @param defaultValue
     * @return
     * @return Boolean
     */
    public static Boolean getBoolean(String key, Boolean defaultValue) {
        return zkEnv == null ? defaultValue : zkEnv.getBoolean(key, defaultValue);
    }

    /**
     * 判断环境是否初始化；applicationContext 不为 null 为，已初始化；
     * 
     * @Title: isInit
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 21, 2020 2:07:56 PM
     * @return
     * @return boolean
     */
    public static boolean isInit() {
        return zkEnv != null && zkEnv.isInit();
    }

}
