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
* @Title: ZKMongoLock.java 
* @author Vinson 
* @Package com.zk.core.lock.monogo 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 16, 2021 3:04:12 PM 
* @version V1.0 
*/
package com.zk.mongo.lock;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/** 
* @ClassName: ZKMongoLock 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Document(collection = "distributed_lock_doc")
public class ZKMongoLock {

    public final static String lock_id_key = "key";

    public final static String lock_value_key = "value";

    public final static String lock_expire_key = "expire";

    public ZKMongoLock() {

    }

    public ZKMongoLock(String key, long value, long expire) {
        this.key = key;
        this.value = value;
        this.expire = expire;
    }

    /**
     * 缓存唯一ID, 锁的key
     */
    @Id
    private String key;

    private long value;

    @Indexed(name = "expire_index_distributed_lock_timeout_", expireAfterSeconds = 0)
    private long expire;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
