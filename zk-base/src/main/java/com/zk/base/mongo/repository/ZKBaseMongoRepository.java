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
 * @Title: ZKBaseMongoRepository.java 
 * @author Vinson 
 * @Package com.zk.base.mongo.repository 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 1:41:49 PM 
 * @version V1.0   
*/
package com.zk.base.mongo.repository;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.zk.base.mongo.doc.ZKBaseMongoDoc;

/** 
* @ClassName: ZKBaseMongoRepository 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKBaseMongoRepository<T extends ZKBaseMongoDoc<ID>, ID extends Serializable>
        extends MongoRepository<T, ID> {

    T findByPkId(ID pkId);

    T findOne(ID pkId);

}
