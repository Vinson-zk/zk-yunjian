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
 * @Title: ZKTestObject.java 
 * @author Vinson 
 * @Package com.zk.mongo.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:11:29 PM 
 * @version V1.0   
*/
package com.zk.mongo.helper;

import java.io.Serializable;
import java.util.UUID;

/** 
* @ClassName: ZKTestObject 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKTestObject implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String str = UUID.randomUUID().toString();

    private int intValue = 9;

    private int[] ints = { 2, 4, 6 };

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int[] getInts() {
        return ints;
    }

    public void setInts(int[] ints) {
        this.ints = ints;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

}
