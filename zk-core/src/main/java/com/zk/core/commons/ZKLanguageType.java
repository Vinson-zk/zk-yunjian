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
 * @Title: ZKLanguageType.java 
 * @author Vinson 
 * @Package com.zk.core.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:48:45 PM 
 * @version V1.0   
*/
package com.zk.core.commons;

/** 
* @ClassName: ZKLanguageType 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public enum ZKLanguageType {
    zh_CN("zh_CN", "简体中文", ""),
    zh_TW("zh_TW", "繁体中文", ""),
    zh("zh", "中文", ""),
    en("en", "English", ""),
    en_US("en_US", "English", "")
    ;
    
    private final String key;
    private final String name;
    private final String description;
    
    ZKLanguageType(String key, String name, String description) {
        this.key = key;
        this.name = name;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static ZKLanguageType parseKey(String key) {
        if(key != null && !key.isEmpty()){
            ZKLanguageType[] lts = ZKLanguageType.values();
            for (ZKLanguageType lt : lts) {
                if(lt.key.equals(key)){
                    return lt;
                }
            }
        }
        return null;
    }
}
