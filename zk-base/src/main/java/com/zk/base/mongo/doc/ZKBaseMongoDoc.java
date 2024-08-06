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
 * @Title: ZKBaseMongoDoc.java 
 * @author Vinson 
 * @Package com.zk.base.mongo.doc 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 1:48:04 PM 
 * @version V1.0   
*/
package com.zk.base.mongo.doc;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

/** 
* @ClassName: ZKBaseMongoDoc 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKBaseMongoDoc<ID extends Serializable> implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    @Id
    private ID pkId;

    /**
     * @return pkId
     */
    public ID getPkId() {
        return pkId;
    }

    /**
     * @param pkId
     *            the pkId to set
     */
    public void setPkId(ID pkId) {
        this.pkId = pkId;
    }

    /**
     * 插入前预处理
     *
     * @Title: preInsert
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 20, 2019 9:50:16 AM
     * @return void
     */
    public void preInsert() {

    }

    /**
     * 修改前预处理
     *
     * @Title: preUpdate
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 20, 2019 9:50:32 AM
     * @return void
     */
    public void preUpdate() {

    }

}
