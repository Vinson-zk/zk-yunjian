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
 * @Title: ZKMongoRepositoryImpl.java 
 * @author Vinson 
 * @Package com.zk.base.mongo.repository.support 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 1:50:47 PM 
 * @version V1.0   
*/
package com.zk.base.mongo.repository.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.util.StreamUtils;
import org.springframework.data.util.Streamable;
import org.springframework.util.Assert;

import com.zk.base.mongo.doc.ZKBaseMongoDoc;
import com.zk.base.mongo.repository.ZKBaseMongoRepository;

/** 
* @ClassName: ZKMongoRepositoryImpl 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMongoRepositoryImpl<T extends ZKBaseMongoDoc<ID>, ID extends Serializable>
        extends SimpleMongoRepository<T, ID> implements ZKBaseMongoRepository<T, ID> {

    private final MongoOperations mongoOperations;

    private final MongoEntityInformation<T, ID> entityInformation;

    /**
     * 
     * @param metadata
     * @param mongoOperations
     */
    public ZKMongoRepositoryImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.entityInformation = metadata;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public <S extends T> S save(S entity) {

        Assert.notNull(entity, "Entity must not be null!");

        if (entityInformation.isNew(entity)) {
            // Vinson 修改：插入前预处理
            entity.preInsert();
            return mongoOperations.insert(entity, entityInformation.getCollectionName());
        }
        // Vinson 修改：修改前预处理
        entity.preUpdate();
        return mongoOperations.save(entity, entityInformation.getCollectionName());
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {

        Assert.notNull(entities, "The given Iterable of entities not be null!");

        Streamable<S> source = Streamable.of(entities);
        boolean allNew = source.stream().allMatch(it -> entityInformation.isNew(it));

        if (allNew) {

            List<S> result = source.stream().collect(Collectors.toList());
            // Vinson 修改：插入前预处理
            result.forEach(e -> e.preInsert());
            return new ArrayList<>(mongoOperations.insert(result, entityInformation.getCollectionName()));
        }
        // Vinson 修改：此入会进入 save 方法，由 save 方法进行修改前预处理
        return source.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    public <S extends T> S insert(S entity) {

        Assert.notNull(entity, "Entity must not be null!");
        // Vinson 修改：插入前预处理
        entity.preInsert();
        return mongoOperations.insert(entity, entityInformation.getCollectionName());
    }

    @Override
    public <S extends T> List<S> insert(Iterable<S> entities) {

        Assert.notNull(entities, "The given Iterable of entities not be null!");

        List<S> list = Streamable.of(entities).stream().collect(StreamUtils.toUnmodifiableList());

        if (list.isEmpty()) {
            return list;
        }
        // Vinson 修改：插入前预处理
        list.forEach(e -> e.preInsert());
        return new ArrayList<>(mongoOperations.insertAll(list));
    }

    // @Override
    public T findByPkId(ID pkId) {
        Assert.notNull(pkId, "The given id must not be null!");
        return mongoOperations.findById(pkId, entityInformation.getJavaType(), entityInformation.getCollectionName());
    }

    public T findOne(ID pkId) {
        Assert.notNull(pkId, "Sample must not be null!");
        return mongoOperations.findById(pkId, entityInformation.getJavaType(), entityInformation.getCollectionName());
    }

}
