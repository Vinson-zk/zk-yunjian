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
* @Title: ZKDistributedLock.java 
* @author Vinson 
* @Package com.zk.core.lock 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 16, 2021 3:03:33 PM 
* @version V1.0 
*/
package com.zk.core.lock;
/** 
* @ClassName: ZKDistributedLock 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKDistributedLock {
    /**
     * 加锁
     *
     * @Title: lock
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年4月24日 上午11:32:12
     * @param key
     *            锁 key
     * @param timeOut
     *            锁超时时间
     * @return
     * @return String
     */
    public boolean lock(String key, long timeOut);

    /**
     * 加锁
     *
     * @Title: lock
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年4月24日 上午11:32:12
     * @param key
     *            锁 key
     * @return String
     */
    public boolean lock(String key);

    /**
     * 加锁
     *
     * @Title: lock
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年4月24日 上午11:32:12
     * @return String
     */
    public boolean lock();

    /**
     * 释放锁
     *
     * @Title: unLock
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年4月24日 上午11:32:48
     * @param key
     *            锁 key
     * @return boolen
     */
    public boolean unLock(String key);

    /**
     * 释放锁
     *
     * @Title: unLock
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年4月24日 上午11:32:48
     * @return boolen
     */
    public boolean unLock();

    /**
     * 释放过期锁
     *
     * @Title: unLock
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年4月24日 下午2:55:02
     * @param key
     * @param time
     * @return
     * @return boolean
     */
    public boolean unLock(long time);

    /**
     * 释放指定 key 下的期锁
     *
     * @Title: unLock
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年4月24日 下午2:55:02
     * @param key
     * @param time
     * @return
     * @return boolean
     */
    public boolean unLock(String key, long time);
}
