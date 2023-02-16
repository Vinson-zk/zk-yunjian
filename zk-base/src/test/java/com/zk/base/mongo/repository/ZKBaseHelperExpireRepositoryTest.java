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
 * @Title: ZKBaseHelperExpireRepositoryTest.java
 * @author Vinson
 * @Package com.zk.base.mongo.repository
 * @Description: TODO(simple description this file what to do.)
 * @date Dec 19, 2019 4:00:08 PM
 * @version V1.0
*/
package com.zk.base.mongo.repository;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.base.helper.ZKBaseHelperCtx;
import com.zk.base.helper.mongo.doc.ZKBaseHelperExpireDoc;
import com.zk.base.helper.mongo.repository.ZKBaseHelperExpireRepository;

import junit.framework.TestCase;

/**
 * 测试 mongo 过期索引
 *
 * @ClassName: ZKBaseHelperExpireRepositoryTest
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKBaseHelperExpireRepositoryTest {

    /**
     * 过期测试
     *
     * @Title: testExpire
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 20, 2019 9:23:52 AM
     * @return void
     */
    @Test
    public void testExpire() {
        try {

            ConfigurableApplicationContext ctx = ZKBaseHelperCtx.run(new String[] {});
            TestCase.assertNotNull(ctx);

            ZKBaseHelperExpireRepository baseHelperExpireRepository = ctx.getBean(ZKBaseHelperExpireRepository.class);
            TestCase.assertNotNull(baseHelperExpireRepository);

            ZKBaseHelperExpireDoc notExpireDoc = null;
            ZKBaseHelperExpireDoc expireDoc = null;
            ZKBaseHelperExpireDoc expireDocUpdate = null;

            int validTime = 5000;
            /*** 过期 ***/
            notExpireDoc = new ZKBaseHelperExpireDoc();
            notExpireDoc.setValidTime(validTime);
            notExpireDoc = baseHelperExpireRepository.save(notExpireDoc);
            TestCase.assertNotNull(notExpireDoc.getPkId());
            notExpireDoc = baseHelperExpireRepository.findByPkId(notExpireDoc.getPkId());
            TestCase.assertNotNull(notExpireDoc.getExpireTime());
            // 开始过期，修改为不过期
            notExpireDoc.setValidTime(-1);
            baseHelperExpireRepository.save(notExpireDoc);
            notExpireDoc = baseHelperExpireRepository.findByPkId(notExpireDoc.getPkId());
            TestCase.assertNull(notExpireDoc.getExpireTime());

            expireDoc = new ZKBaseHelperExpireDoc(validTime);
            expireDoc = baseHelperExpireRepository.save(expireDoc);
            TestCase.assertNotNull(expireDoc.getPkId());

            expireDocUpdate = new ZKBaseHelperExpireDoc(validTime);
            expireDocUpdate = baseHelperExpireRepository.save(expireDocUpdate);
            TestCase.assertNotNull(expireDocUpdate.getPkId());

            // 插入成功，则会查出这条数据
            notExpireDoc = baseHelperExpireRepository.findByPkId(notExpireDoc.getPkId());
            TestCase.assertNotNull(notExpireDoc);
            expireDoc = baseHelperExpireRepository.findByPkId(expireDoc.getPkId());
            TestCase.assertNotNull(expireDoc);
            expireDocUpdate = baseHelperExpireRepository.findByPkId(expireDocUpdate.getPkId());
            TestCase.assertNotNull(expireDocUpdate);

            // 休息 62 秒；数据为1.5 秒后过期；过期数据清理周期为 60 秒；延迟过期 expireAfterSeconds 设置有 0
            // 秒;
            int sleepSeconds = 62 + validTime / 1000;
            System.out.println("[^_^:20190820-1340-001] 第一轮休息 " + sleepSeconds + " 秒！开始 ... ... ");
            for (int i = 0; i < sleepSeconds; ++i) {
                baseHelperExpireRepository.save(expireDocUpdate);
                // 循环休息，间隔 1 秒；
                Thread.sleep(1000);
            }
            System.out.println("[^_^:20190820-1340-001] 第一轮休息 " + sleepSeconds + " 秒 完毕！");
            // 未设置 Expire Time 的对象不会过期
            notExpireDoc = baseHelperExpireRepository.findByPkId(notExpireDoc.getPkId());
            TestCase.assertNotNull(notExpireDoc);
            // 设置有 Expire Time 的对象 过期
            expireDoc = baseHelperExpireRepository.findByPkId(expireDoc.getPkId());
            TestCase.assertNull(expireDoc);
            // 一直有更新的数据不会过期
            expireDocUpdate = baseHelperExpireRepository.findByPkId(expireDocUpdate.getPkId());
            TestCase.assertNotNull(expireDocUpdate);

            /*** 休息 sleepSeconds 秒；数据不更新，过期 ***/
            System.out.println("[^_^:20190820-1340-002] 第二轮休息 " + sleepSeconds + " 秒！开始 ... ... ");
            Thread.sleep(61000 + validTime);
            System.out.println("[^_^:20190820-1340-002] 第二轮休息 " + sleepSeconds + " 秒 完毕！");

            expireDocUpdate = baseHelperExpireRepository.findByPkId(expireDocUpdate.getPkId());
            TestCase.assertNull(expireDocUpdate);

            /*** 删除 ***/
            baseHelperExpireRepository.deleteById(notExpireDoc.getPkId());
            notExpireDoc = baseHelperExpireRepository.findOne(notExpireDoc.getPkId());
            TestCase.assertNull(notExpireDoc);

        }
        catch(Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
