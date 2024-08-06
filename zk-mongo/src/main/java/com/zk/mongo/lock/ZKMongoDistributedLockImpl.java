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
* @Title: ZKMongoDistributedLockImpl.java 
* @author Vinson 
* @Package com.zk.mongo.lock 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 16, 2021 3:16:13 PM 
* @version V1.0 
*/
package com.zk.mongo.lock;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.core.lock.ZKDistributedLock;

/** 
* @ClassName: ZKMongoDistributedLockImpl 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMongoDistributedLockImpl implements ZKDistributedLock {

    private Logger logger = LogManager.getLogger(getClass());

    MongoTemplate mongoTemplate;

    public ZKMongoDistributedLockImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private final static String default_lock_key = "_default_lock_key_";

    /**
     * (not Javadoc)
     * <p>
     * Title: lock
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param key
     * @param timeOut
     *            毫秒
     * @return
     * @see com.isprint.ar.core.distributed.lock.DistributedLock#lock(java.lang.String,
     *      long)
     */
    @Override
    public boolean lock(String key, long timeOut) {

        if (lockIng(key, timeOut)) {
            return true;
        }
        else {
            try {
                Thread.sleep(500l);
            }
            catch(InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return lockIng(key, timeOut);
        }
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: lock
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param key
     * @param timeOut
     *            毫秒
     * @return
     * @see com.isprint.ar.core.distributed.lock.DistributedLock#lock(java.lang.String,
     *      long)
     */
    private boolean lockIng(String key, long timeOut) {

        // 释放过期的锁
        unLock(System.currentTimeMillis());
        ZKMongoLock mongoLock = ZKMongoLockUtils.getByKey(mongoTemplate, key);

        // 判断该锁是否被获得,锁已经被其他请求获得，直接返回
        if (mongoLock == null || mongoLock.getExpire() < System.currentTimeMillis()) {

            if (mongoLock != null && mongoLock.getExpire() < System.currentTimeMillis()) {
                unLock(key, System.currentTimeMillis());
                // unLock(key);
            }
            /*
             * ！！(在高并发前提下)在当前请求已经获得锁的前提下，还可能有其他请求尝试去获得锁，
             * 此时会导致当前锁的过期时间被延长，由于延长时间在毫秒级， 可以忽略。
             */
            ZKMongoLock ml = ZKMongoLockUtils.incrByWithExpire(mongoTemplate,
                    new ZKMongoLock(key, 1, System.currentTimeMillis() + timeOut));
            // 如果结果是1，代表当前请求获得锁
            if (ml != null && ml.getValue() == 1) {
                // 如果结果>1，表示当前请求在获取锁的过程中，锁已被其他请求获得。
                logger.info("[^_^:20180425-1353-001] 线程：{} 获得资源：{} 的锁", Thread.currentThread().getName(), key);
                return true;
            }
        }
        return false;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: lock
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param key
     * @return
     * @see com.isprint.ar.core.distributed.lock.DistributedLock#lock(java.lang.String)
     */
    @Override
    public boolean lock(String key) {
        // TODO Auto-generated method stub
        return lock(key, 3000);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: lock
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.isprint.ar.core.distributed.lock.DistributedLock#lock()
     */
    @Override
    public boolean lock() {
        // TODO Auto-generated method stub
        return lock(default_lock_key);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: unLock
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param key
     * @return
     * @see com.isprint.ar.core.distributed.lock.DistributedLock#unLock(java.lang.String)
     */
    @Override
    public boolean unLock(String key) {
        ZKMongoLockUtils.remove(mongoTemplate, key);
        logger.info("[^_^:20180425-1353-002] 线程：{} 释放了资源：{} 的锁", Thread.currentThread().getName(), key);
        return true;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: unLock
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.isprint.ar.core.distributed.lock.DistributedLock#unLock()
     */
    @Override
    public boolean unLock() {
        // TODO Auto-generated method stub
        return unLock(default_lock_key);
    }

    @Override
    public boolean unLock(String key, long currentTime) {
        ZKMongoLockUtils.removeExpire(mongoTemplate, key, currentTime);
        return true;
    }

    @Override
    public boolean unLock(long currentTime) {
        ZKMongoLockUtils.removeExpire(mongoTemplate, currentTime);
        return true;
    }

}
