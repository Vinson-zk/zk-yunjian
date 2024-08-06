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
 * @Title: ZKInsertTest.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.queryAndWriteOperation 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:14:10 PM 
 * @version V1.0   
*/
package com.zk.mongo.command.queryAndWriteOperation;

import java.util.Date;

import org.bson.Document;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.mongo.command.administration.ZKCreate;
import com.zk.mongo.command.administration.ZKCreateIndexes;
import com.zk.mongo.command.administration.ZKDrop;
import com.zk.mongo.element.ZKIndexElement;
import com.zk.mongo.helper.ZKMongoTestConfig;
import com.zk.mongo.utils.ZKMongoUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKInsertTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKInsertTest {

    public static ApplicationContext ctx;

    public static MongoTemplate mongoTemplate;

    static {
        try {
            ctx = ZKMongoTestConfig.getCtx();
            TestCase.assertNotNull(ctx);

            mongoTemplate = ctx.getBean(MongoTemplate.class);
            TestCase.assertNotNull(mongoTemplate);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommandZKInsert() {

        String id1 = "ZKInsert_id_1";
        String id2 = "ZKInsert_id_2";
        String id3 = "ZKInsert_id_3";

        String attrName1 = "a1";
        String attrValue1 = "v1";
        String attrName2 = "a2";
        String attrValue2 = "v2";
        String attrName3 = "a3";
        int attrValue3 = 3;

        try {
            // 创建测试集合
            if (ZKMongoUtils.isExist(mongoTemplate, ZKMongoTestConfig.colleationName_QueryAndWriteOperation)) {
                ZKDrop drop = new ZKDrop(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
                mongoTemplate.executeCommand(drop);
            }
            ZKCreate create = new ZKCreate(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            mongoTemplate.executeCommand(create);
            // 创建过期索引
            ZKIndexElement expireIndex = ZKCreateIndexes.IndexElement(ZKMongoTestConfig.indexExpireTimeName,
                    ZKMongoTestConfig.attrExpireTimeName);
            expireIndex.setExpireAfterSeconds(0);
            ZKCreateIndexes createIndexes = new ZKCreateIndexes(ZKMongoTestConfig.colleationName_QueryAndWriteOperation,
                    expireIndex);
            mongoTemplate.executeCommand(createIndexes);

            /*** 插入 ***/
            // 一次插入两个
            ZKInsert insert = new ZKInsert(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            Document doc = new Document();
            doc.put(ZKMongoUtils.autoIndexIdName, id1);
            doc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            doc.put(attrName1, id1 + ":" + attrValue1);
            doc.put(attrName2, id1 + ":" + attrValue2);
            doc.put(attrName3, attrValue3);
            insert.addDoc(doc);
            doc = new Document();
            doc.put(ZKMongoUtils.autoIndexIdName, id2);
            doc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            doc.put(attrName1, id2 + ":" + attrValue1);
            doc.put(attrName2, id2 + ":" + attrValue2);
            doc.put(attrName3, attrValue3);
            insert.addDoc(doc);
            Document resDoc = mongoTemplate.executeCommand(insert);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(2, resDoc.getInteger("n").intValue());
            // 一次插入一个
            insert = new ZKInsert(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            doc = new Document();
            doc.put(ZKMongoUtils.autoIndexIdName, id3);
            doc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            doc.put(attrName1, id3 + ":" + attrValue1);
            doc.put(attrName2, id3 + ":" + attrValue2);
            doc.put(attrName3, attrValue3);
            insert.addDoc(doc);
            resDoc = mongoTemplate.executeCommand(insert);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());

            /*** 插入主键冲突 ***/
            insert = new ZKInsert(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            doc = new Document();
            doc.put(ZKMongoUtils.autoIndexIdName, id3);
            doc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            doc.put(attrName1, id3 + ":" + attrValue1);
            doc.put(attrName2, id3 + ":" + attrValue2);
            doc.put(attrName3, attrValue3);
            insert.addDoc(doc);
            resDoc = mongoTemplate.executeCommand(insert);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(0, resDoc.getInteger("n").intValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
