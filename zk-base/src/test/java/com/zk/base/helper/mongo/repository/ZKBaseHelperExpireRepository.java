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
 * @Title: ZKBaseHelperExpireRepository.java
 * @author Vinson
 * @Package com.zk.base.helper.mongo.repository
 * @Description: TODO(simple description this file what to do.)
 * @date Dec 19, 2019 3:34:15 PM
 * @version V1.0
*/
package com.zk.base.helper.mongo.repository;

import com.zk.base.helper.mongo.doc.ZKBaseHelperExpireDoc;
import com.zk.base.mongo.repository.ZKBaseMongoRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
* @ClassName: ZKBaseHelperExpireRepository
* @Description: TODO(simple description this class what to do.)
* @author Vinson
* @version 1.0
*/
@ConditionalOnBean(value = {MongoTemplate.class})
public interface ZKBaseHelperExpireRepository extends ZKBaseMongoRepository<ZKBaseHelperExpireDoc, String> {

}
