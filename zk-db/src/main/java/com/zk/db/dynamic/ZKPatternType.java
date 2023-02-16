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
 * @Title: ZKPatternType.java 
 * @author Vinson 
 * @Package com.zk.db.dynamic 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:32:52 AM 
 * @version V1.0   
*/
package com.zk.db.dynamic;

/** 
* @ClassName: ZKPatternType 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public enum ZKPatternType {

    READ("read", 1), WRITE("write", 2);

    private String name;

    private int key;

    ZKPatternType(String type, int key) {
        this.name = type;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public int getKey() {
        return key;
    }

    public static ZKPatternType parseByKey(int key) {
        for (ZKPatternType t : ZKPatternType.values()) {
            if (t.getKey() == key) {
                return t;
            }
        }
        return null;
    }

    public static ZKPatternType parseByName(String type) {
        if (type != null && "".equals(type)) {
            for (ZKPatternType t : ZKPatternType.values()) {
                if (t.getName().toLowerCase().equals(type.toLowerCase())) {
                    return t;
                }
            }
        }
        return null;
    }

}
