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
 * @Title: ZKBaseHelperRepository.java
 * @author Vinson
 * @Package com.zk.base.helper.mongo.repository
 * @Description: TODO(simple description this file what to do.)
 * @date Dec 19, 2019 3:34:42 PM
 * @version V1.0
*/
package com.zk.base.helper.mongo.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.Query;

import com.zk.base.helper.mongo.doc.ZKBaseHelperDoc;
import com.zk.base.mongo.repository.ZKBaseMongoRepository;

/**
* @ClassName: ZKBaseHelperRepository
* @Description: TODO(simple description this class what to do.)
* @author Vinson
* @version 1.0
*/
@ConditionalOnBean(value = {MongoTemplate.class})
public interface ZKBaseHelperRepository extends ZKBaseMongoRepository<ZKBaseHelperDoc, String> {

    /**
     *
     *
     * @Title: findFieldsInclude
     * @Description: 按条件查询，并指定返回包含的字段
     * @author Vinson
     * @date Aug 19, 2019 5:35:08 PM
     * @param pkId
     * @return
     * @return ZKBaseHelperDoc
     */
    @Query(value = "{_id:?0}", fields = "{name:1, _id:1}")
    ZKBaseHelperDoc findFieldsInclude(String pkId);

    /**
     *
     *
     * @Title: findFieldsExclude
     * @Description: 按条件查询，并指定返回排除的字段
     * @author Vinson
     * @date Aug 19, 2019 6:04:47 PM
     * @param pkId
     * @return
     * @return ZKBaseHelperDoc
     */
    @Query(value = "{_id:?0}", fields = "{name:0}")
    ZKBaseHelperDoc findFieldsExclude(ObjectId pkId);

    @Query(value = "{_id:{$eq:?0}}")
    ZKBaseHelperDoc findByIdObj(ObjectId pkId);

    @Query(value = "{value:{$lt:?0}}")
    List<ZKBaseHelperDoc> findByValue(int value);

}
