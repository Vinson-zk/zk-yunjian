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
* @Title: ZKMongoDistributedLockImplTest.java 
* @author Vinson 
* @Package com.zk.mongo.lock 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 16, 2021 3:20:45 PM 
* @version V1.0 
*/
package com.zk.mongo.lock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.core.commons.ZKThreadPool;
import com.zk.core.lock.ZKDistributedLock;
import com.zk.mongo.helper.ZKMongoTestConfig;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMongoDistributedLockImplTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@SuppressWarnings("deprecation")
public class ZKMongoDistributedLockImplTest {

    private int serialNum = 0;

    // private String lockStr = "";

    private ZKDistributedLock distributedLock;

    @Test
    public void testMultiThread() {
        class ModifyThread implements Runnable {

            ZKDistributedLock distributedLock;

            String lockKey;

            List<Integer> serialNums;

            ModifyThread(ZKDistributedLock distributedLock, String lockKey, List<Integer> serialNums) {
                this.distributedLock = distributedLock;
                this.lockKey = lockKey;
                this.serialNums = serialNums;
            }

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (distributedLock.lock(lockKey, 1500)) {
                    ++serialNum;
                    System.out.println(String.format("[^_^: 20180424-1656-001] serialNum 被线程 {%s} 增加了1 {%02d} ",
                            Thread.currentThread().getName(), serialNum));
                    serialNums.add(serialNum);
                    distributedLock.unLock(lockKey);
                }
                else {
                    System.out.println("[>_<: 20180424-1656-002] 资源被锁！");
                }
            }

        }

        try {
            TestCase.assertNotNull(ZKMongoTestConfig.getCtx());
            distributedLock = ZKMongoTestConfig.getCtx().getBean("distributedLock", ZKDistributedLock.class);

            serialNum = 0;
            List<Integer> serialNums = new ArrayList<>();
            int threadCount = 9;
            List<ZKThreadPool> lts = new ArrayList<>();

            for (int i = 0; i < threadCount; ++i) {
                lts.add(new ZKThreadPool(new ModifyThread(distributedLock, "lockKey", serialNums)));
            }

            for (int i = 0; i < threadCount; ++i) {
                lts.get(i).start();
            }

            lts = ZKThreadPool.getThreadPool();
            for (int i = 0; i < lts.size(); ++i) {
                if (lts.get(i).isAlive()) {
                    // 将子线程挂到本线程上，本线程会等子线程运行完毕，再往下执行。
                    lts.get(i).join();
                }
            }
            TestCase.assertTrue(serialNums.size() > 1);
            for (int i = 1; i < serialNums.size(); ++i) {
                TestCase.assertEquals(serialNums.get(i - 1) + 1, serialNums.get(i).intValue());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testSync() {

        class ModifyThread implements Runnable {

            ZKDistributedLock distributedLock;

            String lockKey;

            long time;

            ModifyThread(ZKDistributedLock distributedLock, String lockKey, long time) {
                this.distributedLock = distributedLock;
                this.lockKey = lockKey;
                this.time = time;
            }

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (lockKey != null) {
                    if (distributedLock.lock(lockKey, 1500)) {
                        ++serialNum;
                        System.out.println(String.format("[^_^: 20180424-1656-002] serialNum 被线程 {%s} 增加了1 {%02d} ",
                                Thread.currentThread().getName(), serialNum));
                        try {
                            Thread.sleep(time);
                        }
                        catch(InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        distributedLock.unLock(lockKey);
                    }
                    else {
                        System.out.println("[>_<: 20180424-1656-002] 资源被锁！");
                    }
                }
                else {
                    ++serialNum;
                    System.out.println(String.format("[^_^: 20180424-1656-002] serialNum 被线程 {%s} 增加了1 {%02d} ",
                            Thread.currentThread().getName(), serialNum));
                    try {
                        Thread.sleep(time);
                    }
                    catch(InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

        try {
            TestCase.assertNotNull(ZKMongoTestConfig.getCtx());
            distributedLock = ZKMongoTestConfig.getCtx().getBean("distributedLock", ZKDistributedLock.class);

            serialNum = 0;
            ZKThreadPool tp1 = new ZKThreadPool(new ModifyThread(distributedLock, null, 1000l));
            ZKThreadPool tp2 = new ZKThreadPool(new ModifyThread(distributedLock, null, 0));
            tp1.start();
            Thread.sleep(100l);
            tp2.start();
            Thread.sleep(1500l);
            TestCase.assertEquals(2, serialNum);

            serialNum = 0;
            tp1 = new ZKThreadPool(new ModifyThread(distributedLock, "lockKey", 1000l));
            tp2 = new ZKThreadPool(new ModifyThread(distributedLock, "lockKey", 0));
            tp1.start();
            Thread.sleep(100l);
            tp2.start();
            Thread.sleep(1500l);
            TestCase.assertEquals(1, serialNum);

            serialNum = 0;
            tp1 = new ZKThreadPool(new ModifyThread(distributedLock, "lockKey1", 1000l));
            tp2 = new ZKThreadPool(new ModifyThread(distributedLock, "lockKey2", 0));
            tp1.start();
            Thread.sleep(100l);
            tp2.start();
            Thread.sleep(1500l);
            TestCase.assertEquals(2, serialNum);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testLock() {
        try {
            TestCase.assertNotNull(ZKMongoTestConfig.getCtx());
            distributedLock = ZKMongoTestConfig.getCtx().getBean("distributedLock", ZKDistributedLock.class);
            TestCase.assertNotNull(distributedLock);
            String lockKey = "lockKey";
            boolean lockFlag = distributedLock.lock(lockKey, 1500l);
            TestCase.assertTrue(lockFlag);
            lockFlag = distributedLock.lock(lockKey, 1500l);
            TestCase.assertFalse(lockFlag);
            lockFlag = distributedLock.lock(lockKey, 1500l);
            TestCase.assertFalse(lockFlag);
            Thread.sleep(2000l);
            lockFlag = distributedLock.lock(lockKey, 1500l);
            TestCase.assertTrue(lockFlag);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testUnLock() {
        try {
            TestCase.assertNotNull(ZKMongoTestConfig.getCtx());
            distributedLock = ZKMongoTestConfig.getCtx().getBean("distributedLock", ZKDistributedLock.class);
            TestCase.assertNotNull(distributedLock);
            String lockKey = "lockKey";
            distributedLock.unLock(lockKey);

            boolean lockFlag = distributedLock.lock(lockKey, 1500l);
            TestCase.assertTrue(lockFlag);

            distributedLock.unLock(lockKey);
            lockFlag = distributedLock.lock(lockKey, 1500l);
            TestCase.assertTrue(lockFlag);

            distributedLock.unLock(lockKey, System.currentTimeMillis());
            lockFlag = distributedLock.lock(lockKey, 1500l);
            TestCase.assertFalse(lockFlag);

            distributedLock.unLock(lockKey);
            lockFlag = distributedLock.lock(lockKey, 1500l);
            TestCase.assertTrue(lockFlag);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
