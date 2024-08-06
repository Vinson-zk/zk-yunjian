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
* @Title: ZKMongoLockUtils.java 
* @author Vinson 
* @Package com.zk.mongo.lock 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 16, 2021 3:13:35 PM 
* @version V1.0 
*/
package com.zk.mongo.lock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/** 
* @ClassName: ZKMongoLockUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMongoLockUtils {

    private static Logger logger = LogManager.getLogger(ZKMongoLockUtils.class);

    /**
     * mongo 操作工具
     */
    // private MongoTemplate mongoTemplate;

    /**
     * 返回指定key的数据
     *
     * @param key
     * @return
     */
    public static ZKMongoLock getByKey(MongoTemplate mongoTemplate, String key) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ZKMongoLock.lock_id_key).is(key));
        List<ZKMongoLock> mls = (List<ZKMongoLock>) mongoTemplate.find(query, ZKMongoLock.class);
        if (mls != null && mls.size() > 1) {
            logger.error("[>_<:20180424-1351-001] 分布式锁异常！同一key的锁出现多个！");
        }
        return (mls == null || mls.size() < 1) ? null : mls.get(0);
    }

    /**
     * 指定key自增increment(原子加),并设置过期时间
     *
     * @param key
     * @param increment
     * @param expire
     * @return
     */
    public static ZKMongoLock incrByWithExpire(MongoTemplate mongoTemplate, ZKMongoLock mongoLock) {
        // 筛选
        Query query = new Query();
        query.addCriteria(new Criteria(ZKMongoLock.lock_id_key).is(mongoLock.getKey()));

        // 更新
        Update update = new Update();
        update.inc(ZKMongoLock.lock_value_key, mongoLock.getValue());
        update.set(ZKMongoLock.lock_expire_key, mongoLock.getExpire());
        // Update.update(fTimeOut_name, mcDoc.getTimeOut());
        // 可选项
        FindAndModifyOptions options = FindAndModifyOptions.options();
        // 没有则新增
        options.upsert(true);
        // 返回更新后的值
        options.returnNew(true);
        try {
            // mongoTemplate.save(mongoLock);
            // return mongoLock;
            ZKMongoLock ml = (ZKMongoLock) mongoTemplate.findAndModify(query, update, options, ZKMongoLock.class);
            return ml;
        }
        catch(Exception e) {
            logger.error("[>_<:20170424-2047-001] 资源已被他人占用，加锁不成功！ERROR:{}", e.getMessage());
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据 key 删除过期的内容
     *
     * @param key
     * @param expireTime
     */
    public static void removeExpire(MongoTemplate mongoTemplate, String key, long expireTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ZKMongoLock.lock_id_key).is(key));
        query.addCriteria(Criteria.where(ZKMongoLock.lock_expire_key).lt(expireTime));
        mongoTemplate.remove(query, ZKMongoLock.class);
    }

    public static void removeExpire(MongoTemplate mongoTemplate, long expireTime) {
        Query query = new Query();
        // query.addCriteria(Criteria.where(MongoLock.lock_id_key).is(key));
        query.addCriteria(Criteria.where(ZKMongoLock.lock_expire_key).lt(expireTime));
        mongoTemplate.remove(query, ZKMongoLock.class);
    }

    public static void remove(MongoTemplate mongoTemplate, String key) {
        Map<String, Object> condition = new HashMap<>();
        condition.put(ZKMongoLock.lock_id_key, key);
        Query query = new Query();
        Set<Map.Entry<String, Object>> set = condition.entrySet();
        int flag = 0;
        for (Map.Entry<String, Object> entry : set) {
            query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
            flag = flag + 1;
        }
        if (flag == 0) {
            query = null;
        }
        mongoTemplate.remove(query, ZKMongoLock.class);
    }

}
