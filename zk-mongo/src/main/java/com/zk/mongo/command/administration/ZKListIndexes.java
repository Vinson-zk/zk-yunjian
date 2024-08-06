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
 * @Title: ZKListIndexes.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.administration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:06:16 PM 
 * @version V1.0   
*/
package com.zk.mongo.command.administration;

import com.zk.mongo.ZKBaseDocument;

/** 
* @ClassName: ZKListIndexes 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKListIndexes extends ZKBaseDocument {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    private static final String commandOpt_listIndexes = "listIndexes";

    public ZKListIndexes(String colleationName) {
        this.put(commandOpt_listIndexes, colleationName);
    }

}
