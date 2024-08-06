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
 * @Title: ZKIndexElement.java 
 * @author Vinson 
 * @Package com.zk.mongo.element 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 5:59:03 PM 
 * @version V1.0   
*/
package com.zk.mongo.element;

import org.bson.Document;

import com.zk.mongo.ZKBaseDocument;

/**
 * @ClassName: ZKIndexElement
 * @Description: 创建索引命令的索引元素
 * @author Vinson
 * @version 1.0
 */
public class ZKIndexElement extends ZKBaseDocument {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    private static final String elementKey_name = "name";

    private static final String elementKey_key = "key";

//  private static final String elementKey_unique = "unique";
    private static final String elementKey_expireAfterSeconds = "expireAfterSeconds";

    public ZKIndexElement(String indexName, String... keys) {
        this.put(elementKey_name, indexName);
        
        Document keyDoc = new Document();
        
        for(String key:keys){
            keyDoc.put(key, 1);
        }
        this.put(elementKey_key, keyDoc);
    }

    /**
     * 设置多少秒后过期，为 null 移除此设置；单位为 秒
     * 
     * @param seconds
     */
    public void setExpireAfterSeconds(Integer seconds) {
        if (seconds == null) {
            this.remove(elementKey_expireAfterSeconds);
        }
        else {
            this.put(elementKey_expireAfterSeconds, seconds);
        }
    }

}
