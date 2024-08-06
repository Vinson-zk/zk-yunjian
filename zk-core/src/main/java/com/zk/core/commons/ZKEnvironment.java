/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKEnvironment.java 
* @author Vinson 
* @Package com.zk.core.commons 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2023 8:51:27 AM 
* @version V1.0 
*/
package com.zk.core.commons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import com.zk.core.utils.ZKClassUtils;

/** 
* @ClassName: ZKEnvironment 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEnvironment {

    protected Logger log = LogManager.getLogger(this.getClass());

    protected static interface ServerClass {
        // "javax.servlet.Servlet", "jakarta.servlet.Servlet",
        public static final String[] SERVLET_INDICATOR_CLASSES = { "jakarta.servlet.Servlet",
                "org.springframework.web.context.ConfigurableWebApplicationContext" };

        public static final String WEBMVC_INDICATOR_CLASS = "org.springframework.web.servlet.DispatcherServlet";

        public static final String WEBFLUX_INDICATOR_CLASS = "org.springframework.web.reactive.DispatcherHandler";

        public static final String JERSEY_INDICATOR_CLASS = "org.glassfish.jersey.servlet.ServletContainer";

    }

    private Environment env = null;

    private ApplicationContext applicationContext;

    private WebApplicationType webApplicationType = null;

    public WebApplicationType getWebApplicationType() {
        if (this.webApplicationType == null) {
            this.webApplicationType = deduceFromClasspath();
        }
        return this.webApplicationType;
    }

    public ZKEnvironment(ApplicationContext applicationContext) {
        this(applicationContext, null);
    }

    public ZKEnvironment(ApplicationContext applicationContext, Environment env) {
        this.applicationContext = applicationContext;
        if(env == null) {
            this.env = getApplicationContext().getEnvironment();
        }else {
            this.env = env;
        }
        getWebApplicationType();
    }

    public ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new NullPointerException("applicationContext must not be null!");
        }
        return applicationContext;
    }

    /**
     * @return env
     */
    public Environment getEnvironment() {
        if (env == null) {
            throw new NullPointerException("env must not be null!");
        }
        return env;
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
    public String getString(String key) {
        return getEnvironment().getProperty(key);
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
    public String getString(String key, String defaultValue) {
        try {
            return getEnvironment().getProperty(key, defaultValue);
        }
        catch(Exception e) {
            log.error("[>_<:20190617-1020-001] undefined '{}' in environment", key);
            return defaultValue;
        }

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
    public Integer getInteger(String key) {
        String val = getEnvironment().getProperty(key);
        return toIneger(val, key, null);
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
    public Integer getInteger(String key, Integer defaultValue) {
        String val = getEnvironment().getProperty(key, (String) null);
        if (val == null || "".equals(val)) {
            log.info("[^_^:20190316-1935-001] 配置属性:[{}] 的属性值为 null；返回默认值:[{}]", key, defaultValue);
            return defaultValue;
        }
        return toIneger(val, key, defaultValue);
    }

    /**
     * 将属性转换为 Ineger 型；参数 key 只为打印日志提供的信息；
     *
     * @Title: toIneger
     * @author Vinson
     * @date Mar 16, 2019 7:46:21 PM
     * @param val
     * @param key
     *            只为打印日志提供的信息；
     * @return
     * @return Integer
     */
    private Integer toIneger(String val, String key, Integer defaultValue) {
        try {
            if (val != null && !"".equals(val)) {
                return Integer.valueOf(val);
            }
            log.info("[^_^:20190316-1935-002] 配置属性:[{}] 的属性值:[{}] 为空或null；！", key, val);
        }
        catch(Exception e) {
            log.error("[>_<:20190316-1935-003] 配置属性:[{}] 的属性值:[{}] 转为 Integer 异常！", key, val);
            e.printStackTrace();
        }
        return defaultValue;
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
    public Long getLong(String key) {
        String val = getEnvironment().getProperty(key);
        return toLong(val, key, null);
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
    public Long getLong(String key, Long defaultValue) {
        String val = getEnvironment().getProperty(key, (String) null);
        if (val == null) {
            log.info("[^_^:20190316-1949-001] 配置属性:[{}] 的属性值为 null；返回默认值:[{}]", key, defaultValue);
            return defaultValue;
        }
        return toLong(val, key, defaultValue);
    }

    /**
     * 将属性转换为 Long 型；参数 key 只为打印日志提供的信息；
     *
     * @Title: toLong
     * @author Vinson
     * @date Mar 16, 2019 7:46:21 PM
     * @param val
     * @param key
     *            只为打印日志提供的信息；
     * @return
     * @return Long
     */
    private Long toLong(String val, String key, Long defaultValue) {
        try {
            if (val != null && !"".equals(val)) {
                return Long.valueOf(val);
            }
            log.info("[^_^:20190316-1949-002] 配置属性:[{}] 的属性值:[{}] 为空或null；！", key, val);
        }
        catch(Exception e) {
            log.error("[>_<:20190316-1949-003] 配置属性:[{}] 的属性值:[{}] 转为 Long 异常！", key, val);
            e.printStackTrace();
        }
        return defaultValue;
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
    public Double getDouble(String key) {
        String val = getEnvironment().getProperty(key);
        return toDouble(val, key, null);
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
    public Double getDouble(String key, Double defaultValue) {
        String val = getEnvironment().getProperty(key, (String) null);
        if (val == null) {
            log.info("[^_^:20190316-1950-001] 配置属性:[{}] 的属性值为 null；返回默认值:[{}]", key, defaultValue);
            return defaultValue;
        }
        return toDouble(val, key, defaultValue);
    }

    /**
     * 将属性转换为 Double 型；参数 key 只为打印日志提供的信息；
     *
     * @Title: toDouble
     * @author Vinson
     * @date Mar 16, 2019 7:46:21 PM
     * @param val
     * @param key
     *            只为打印日志提供的信息；
     * @return
     * @return Double
     */
    private Double toDouble(String val, String key, Double defaultValue) {
        try {
            if (val != null && !"".equals(val)) {
                return Double.valueOf(val);
            }
            log.info("[^_^:20190316-1950-002] 配置属性:[{}] 的属性值:[{}] 为空或null；！", key, val);
        }
        catch(Exception e) {
            log.error("[>_<:20190316-1950-003] 配置属性:[{}] 的属性值:[{}] 转为 Double 异常！", key, val);
            e.printStackTrace();
        }
        return defaultValue;
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
    public Float getFloat(String key) {
        String val = getEnvironment().getProperty(key);
        return toFloat(val, key, null);
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
    public Float getFloat(String key, Float defaultValue) {
        String val = getEnvironment().getProperty(key, (String) null);
        if (val == null) {
            log.info("[^_^:20190316-1951-001] 配置属性:[{}] 的属性值为 null；返回默认值:[{}]", key, defaultValue);
            return defaultValue;
        }
        return toFloat(val, key, defaultValue);
    }

    /**
     * 将属性转换为 Float 型；参数 key 只为打印日志提供的信息；
     *
     * @Title: toFloat
     * @author Vinson
     * @date Mar 16, 2019 7:46:21 PM
     * @param val
     * @param key
     *            只为打印日志提供的信息；
     * @return
     * @return Float
     */
    private Float toFloat(String val, String key, Float defaultValue) {
        try {
            if (val != null && !"".equals(val)) {
                return Float.valueOf(val);
            }
            log.info("[^_^:20190316-1951-002] 配置属性:[{}] 的属性值:[{}] 为空或null；！", key, val);
        }
        catch(Exception e) {
            log.error("[>_<:20190316-1951-003] 配置属性:[{}] 的属性值:[{}] 转为 Float 异常！", key, val);
            e.printStackTrace();
        }
        return defaultValue;
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
    public Boolean getBoolean(String key) {
        String val = getEnvironment().getProperty(key);
        return toBoolean(val, key, null);
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
    public Boolean getBoolean(String key, Boolean defaultValue) {
        String val = getEnvironment().getProperty(key, (String) null);
        if (val == null || "".equals(val)) {
            log.info("[^_^:20190316-1952-001] 配置属性:[{}] 的属性值为 null；返回默认值:[{}]", key, defaultValue);
            return defaultValue;
        }
        return toBoolean(val, key, defaultValue);
    }

    /**
     * 将属性转换为 Boolean 型；参数 key 只为打印日志提供的信息；
     *
     * @Title: toBoolean
     * @author Vinson
     * @date Mar 16, 2019 7:46:21 PM
     * @param val
     * @param key
     *            只为打印日志提供的信息；
     * @return
     * @return Boolean
     */
    private Boolean toBoolean(String val, String key, Boolean defaultValue) {
        if (val != null) {
            if ("1".equals(val) || "true".equals(val)) {
                return Boolean.valueOf("true");
            }
            else if ("0".equals(val) || "false".equals(val)) {
                return Boolean.valueOf("false");
            }
        }
        log.info("[^_^:20190316-1952-001] 配置属性:[{}] 的属性值:[{}] 为 null 或不可转化，返回默认值：[{}]；！", key, val, defaultValue);
        return defaultValue;
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
    public boolean isInit() {
        return this.applicationContext != null;
    }

    protected static WebApplicationType deduceFromClasspath() {
        if (ZKClassUtils.isPresent(ServerClass.WEBFLUX_INDICATOR_CLASS, null)
                && !ZKClassUtils.isPresent(ServerClass.WEBMVC_INDICATOR_CLASS, null)
                && !ZKClassUtils.isPresent(ServerClass.JERSEY_INDICATOR_CLASS, null)) {
            return WebApplicationType.REACTIVE;
        }
        for (String className : ServerClass.SERVLET_INDICATOR_CLASSES) {
            if (!ZKClassUtils.isPresent(className, null)) {
                return WebApplicationType.NONE;
            }
        }
        return WebApplicationType.SERVLET;
    }

}
