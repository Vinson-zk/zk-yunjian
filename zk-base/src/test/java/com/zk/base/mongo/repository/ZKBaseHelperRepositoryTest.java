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
 * @Title: ZKBaseHelperRepositoryTest.java
 * @author Vinson
 * @Package com.zk.base.mongo.repository
 * @Description: TODO(simple description this file what to do.)
 * @date Dec 19, 2019 4:00:38 PM
 * @version V1.0
*/
package com.zk.base.mongo.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.base.helper.ZKBaseHelperCtx;
import com.zk.base.helper.mongo.doc.ZKBaseHelperDoc;
import com.zk.base.helper.mongo.repository.ZKBaseHelperRepository;

import junit.framework.TestCase;

/**
 * 测试一般方法及 ZKMongoRepositoryImpl 中的方法
 *
 * @ClassName: ZKBaseHelperRepositoryTest
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKBaseHelperRepositoryTest {

    /**
     * 普通方法测试
     *
     * @Title: testGeneric
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 20, 2019 9:24:35 AM
     * @return void
     */
    @Test
    public void testGeneric() {
        try {

            ConfigurableApplicationContext ctx = ZKBaseHelperCtx.run(new String[] {});
            TestCase.assertNotNull(ctx);

            ZKBaseHelperRepository zkBaseHelperRepository = ctx.getBean(ZKBaseHelperRepository.class);
            TestCase.assertNotNull(zkBaseHelperRepository);

            ZKBaseHelperDoc zkBaseHelperDoc = null;
            ZKBaseHelperDoc resultDoc = null;
            List<ZKBaseHelperDoc> resultDocList = null;

            /*** 插入 ***/
            zkBaseHelperDoc = new ZKBaseHelperDoc();
            TestCase.assertNull(zkBaseHelperDoc.getPkId());
            /*
             * Inserts the given entity. Assumes the instance to be new to be
             * able to apply insertion optimizations. Use the returned instance
             * for further operations as the save operation might have changed
             * the entity instance completely. Prefer using save(Object) instead
             * to avoid the usage of store-specific API.
             */
//            zkBaseHelperDoc = zkBaseHelperRepository.insert(zkBaseHelperDoc);

            /*
             * Saves a given entity. Use the returned instance for further
             * operations as the save operation might have changed the entity
             * instance completely.
             */
            zkBaseHelperDoc.setValue(2);
            zkBaseHelperDoc.setIgnoreValue("IgnoreValue");
            zkBaseHelperDoc = zkBaseHelperRepository.save(zkBaseHelperDoc);
            TestCase.assertNotNull(zkBaseHelperDoc.getPkId());

            /*** 查询 ***/
            resultDoc = null;
            resultDoc = zkBaseHelperRepository.findById(zkBaseHelperDoc.getPkId()).get();
            TestCase.assertNotNull(resultDoc);
            TestCase.assertEquals(zkBaseHelperDoc.getValue(), resultDoc.getValue());
            TestCase.assertNull(resultDoc.getName());
            // 存储时，忽略该属性
            TestCase.assertNull(resultDoc.getIgnoreValue());

            /*** 修改 ***/
            zkBaseHelperDoc.setName("zkBaseHelperDoc");
            zkBaseHelperDoc.setValue(3);
            resultDoc = zkBaseHelperRepository.save(zkBaseHelperDoc);
            TestCase.assertEquals(zkBaseHelperDoc.getPkId(), resultDoc.getPkId());

            /*** 查询 ***/
            // findById
            resultDoc = null;
            resultDoc = zkBaseHelperRepository.findById(zkBaseHelperDoc.getPkId()).get();
            TestCase.assertNotNull(resultDoc);
            TestCase.assertEquals(zkBaseHelperDoc.getValue(), resultDoc.getValue());

            // findFieldsInclude
            resultDoc = null;
            resultDoc = zkBaseHelperRepository.findFieldsInclude(zkBaseHelperDoc.getPkId());
//            System.out.println("[^_^:20190819-1806-001]:" + ZKJsonUtils.toJsonStr(resultDoc));
            TestCase.assertNotNull(resultDoc);
            TestCase.assertEquals(zkBaseHelperDoc.getName(), resultDoc.getName());
            TestCase.assertNull(resultDoc.getValue());

            // findFieldsExclude
            resultDoc = null;
            resultDoc = zkBaseHelperRepository.findFieldsExclude(new ObjectId(zkBaseHelperDoc.getPkId()));
//            System.out.println("[^_^:20190819-1806-002]:" + ZKJsonUtils.toJsonStr(resultDoc));
            TestCase.assertNotNull(resultDoc);
            TestCase.assertEquals(zkBaseHelperDoc.getValue(), resultDoc.getValue());
            TestCase.assertNull(resultDoc.getName());

            // findByIdObj
            resultDoc = null;
            resultDoc = zkBaseHelperRepository.findByIdObj(new ObjectId(zkBaseHelperDoc.getPkId()));
            TestCase.assertNotNull(resultDoc);

            // findByValue
            resultDocList = null;
            resultDocList = zkBaseHelperRepository.findByValue(2);
            TestCase.assertNotNull(resultDocList);
            TestCase.assertEquals(0, resultDocList.size());

            resultDocList = null;
            resultDocList = zkBaseHelperRepository.findByValue(5);
            TestCase.assertNotNull(resultDocList);
            TestCase.assertTrue(resultDocList.size() > 0);

            // findByPkId
            resultDoc = null;
            resultDoc = zkBaseHelperRepository.findByPkId(zkBaseHelperDoc.getPkId());
            TestCase.assertNotNull(resultDoc);

            // findOne
            resultDoc = null;
            resultDoc = zkBaseHelperRepository.findOne(zkBaseHelperDoc.getPkId());
            TestCase.assertNotNull(resultDoc);

            /*** 删除 ***/
            zkBaseHelperRepository.deleteById(zkBaseHelperDoc.getPkId());
            zkBaseHelperDoc = zkBaseHelperRepository.findByIdObj(new ObjectId(zkBaseHelperDoc.getPkId()));
            TestCase.assertNull(zkBaseHelperDoc);

            /*** 指定 ID 插入 ***/
            zkBaseHelperDoc = new ZKBaseHelperDoc();
            zkBaseHelperDoc.setPkId("pkId-arBaseHelperDoc");
            zkBaseHelperDoc.setName("zkBaseHelperDoc");
            resultDoc = zkBaseHelperRepository.save(zkBaseHelperDoc);
            TestCase.assertEquals(zkBaseHelperDoc.getPkId(), resultDoc.getPkId());

            // findByPkId
            resultDoc = null;
            resultDoc = zkBaseHelperRepository.findByPkId(zkBaseHelperDoc.getPkId());
            TestCase.assertNotNull(resultDoc);

            // findOne
            resultDoc = null;
            resultDoc = zkBaseHelperRepository.findOne(zkBaseHelperDoc.getPkId());
            TestCase.assertNotNull(resultDoc);

            // 删除
            zkBaseHelperRepository.deleteById(zkBaseHelperDoc.getPkId());
            zkBaseHelperDoc = zkBaseHelperRepository.findByPkId(zkBaseHelperDoc.getPkId());
            TestCase.assertNull(zkBaseHelperDoc);

        }
        catch(Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
