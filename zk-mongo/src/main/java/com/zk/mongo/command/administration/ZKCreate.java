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
 * @Title: ZKCreate.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.administration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:04:42 PM 
 * @version V1.0   
*/
package com.zk.mongo.command.administration;

import com.zk.mongo.ZKBaseDocument;

/** 
* @ClassName: ZKCreate 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCreate extends ZKBaseDocument {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    private static final String commandOpt_create = "create";

    private static final String commandOpt_autoIndexId = "autoIndexId";

    /**
     * 设置创建集合时，是否创建主键索引；true-创建；false-不创建；默认为 true
     * @param colleationName
     */
    public ZKCreate(String collectionName) {
        this.put(commandOpt_create, collectionName);
        this.put(commandOpt_autoIndexId, true);
    }

    /**
     * 设置创建集合时，是否创建主键索引；true-创建；false-不创建；默认为 true
     * 
     * @param autoIndexId
     */
    public void setAutoIndexId(boolean autoIndexId) {
        this.put(commandOpt_autoIndexId, autoIndexId);
    }

}
