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
 * @Title: ZKSortMode.java 
 * @author Vinson 
 * @Package com.zk.core.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:18:09 PM 
 * @version V1.0   
*/
package com.zk.core.commons.data;

/** 
* @ClassName: ZKSortMode 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public enum ZKSortMode {

    /**
     * 升序
     */
    ASC("ASC"),
    /**
     * 降序
     */
    DESC("DESC");

    private final String value;

    ZKSortMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ZKSortMode parseKey(String key) {
        if (key != null && !key.isEmpty()) {
            ZKSortMode[] sms = ZKSortMode.values();
            key = key.toUpperCase();
            for (ZKSortMode sm : sms) {
                if (sm.value.equals(key)) {
                    return sm;
                }
            }
        }
        return null;
    }

}
