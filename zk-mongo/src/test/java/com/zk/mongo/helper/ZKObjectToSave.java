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
 * @Title: ZKObjectToSave.java 
 * @author Vinson 
 * @Package com.zk.mongo.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:11:02 PM 
 * @version V1.0   
*/
package com.zk.mongo.helper;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** 
* @ClassName: ZKObjectToSave 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Document(collection = ZKObjectToSave.collectionName)
public class ZKObjectToSave {

    public static final String collectionName = "test_mongoTemplate";

    @Id
    private String key;

    private String value;

    private long intValue = 1;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getInt() {
        return intValue;
    }

    public void setInt(long intValue) {
        this.intValue = intValue;
    }
}
