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
 * @Title: ZKListCollections.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.administration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:05:54 PM 
 * @version V1.0   
*/
package com.zk.mongo.command.administration;

import org.bson.Document;

import com.zk.mongo.ZKBaseDocument;

/** 
* @ClassName: ZKListCollections 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKListCollections extends ZKBaseDocument {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    private static final String commandOpt_listCollections = "listCollections";

    private static final String commandOpt_filter = "filter";

    public ZKListCollections() {
        this.put(commandOpt_listCollections, 1);
    }

    public void setFilter(Document filter) {
        this.put(commandOpt_filter, filter);
    }

}
