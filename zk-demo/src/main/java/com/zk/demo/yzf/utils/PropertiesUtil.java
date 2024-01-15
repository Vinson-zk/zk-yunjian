/**
 * Bestpay.com.cn Inc.
 * Copyright (c) 2011-2017 All Rights Reserved.
 */
package com.zk.demo.yzf.utils;

import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取配置文件的工具类
 *
 * @author byang2018
 * @date 2017年09月06日
 */
public class PropertiesUtil {
    private Properties props;

    public PropertiesUtil(String fileName) {
        readProperties(fileName);
    }

    /**
     * 加载配置文件
     *
     * @param fileName
     */
    private void readProperties(String fileName) {
        try {
            props = new Properties();
            InputStreamReader inputStream = new InputStreamReader(
                    this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
//            // 2023-04-23 Vinson
//            InputStreamReader inputStream = new InputStreamReader(new FileInputStream(new File(fileName)), "UTF-8");
            props.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key读取对应的value
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return props.getProperty(key);
    }

    /**
     * 得到所有的配置信息
     *
     * @return
     */
    public Map<?, ?> getAll() {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<?> enu = props.propertyNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            String value = props.getProperty(key);
            map.put(key, value);
        }
        return map;
    }
}
