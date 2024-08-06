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
 * @Title: ZKDeleteTest.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.queryAndWriteOperation 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:13:11 PM 
 * @version V1.0   
*/
package com.zk.mongo.command.queryAndWriteOperation;

import java.util.Date;

import org.bson.Document;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.mongo.ZKMongoTemplateTest;
import com.zk.mongo.command.administration.ZKCreate;
import com.zk.mongo.command.administration.ZKCreateIndexes;
import com.zk.mongo.command.administration.ZKDrop;
import com.zk.mongo.element.ZKDeleteElement;
import com.zk.mongo.element.ZKIndexElement;
import com.zk.mongo.helper.ZKMongoTestConfig;
import com.zk.mongo.operator.ZKQueryOpt;
import com.zk.mongo.utils.ZKMongoUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKDeleteTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDeleteTest {

    public static ApplicationContext ctx;

    public static MongoTemplate mongoTemplate;

    static {
        try {
            ctx = ZKMongoTemplateTest.ctx;
            TestCase.assertNotNull(ctx);

            mongoTemplate = ctx.getBean(MongoTemplate.class);
            TestCase.assertNotNull(mongoTemplate);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommandBSDelete() {

        String id1 = "ZKDelete_id_1";
        String id2 = "ZKDelete_id_2";
        String id3 = "ZKDelete_id_3";

        String attrName1 = "a1";
        String attrValue1 = "v1";
        String attrName2 = "a2";
        String attrValue2 = "v2";
        String attrName3 = "a3";
        int attrValue3 = 3;

        try {
            // 创建测试集合
            if (ZKMongoUtils.isExist(mongoTemplate, ZKMongoTestConfig.colleationName_QueryAndWriteOperation)) {
                ZKDrop ZKDrop = new ZKDrop(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
                mongoTemplate.executeCommand(ZKDrop);
            }
            ZKCreate ZKCreate = new ZKCreate(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            mongoTemplate.executeCommand(ZKCreate);
            // 创建过期索引
            ZKIndexElement expireIndex = ZKCreateIndexes.IndexElement(ZKMongoTestConfig.indexExpireTimeName,
                    ZKMongoTestConfig.attrExpireTimeName);
            expireIndex.setExpireAfterSeconds(0);
            ZKCreateIndexes ZKCreateIndexes = new ZKCreateIndexes(
                    ZKMongoTestConfig.colleationName_QueryAndWriteOperation, expireIndex);
            mongoTemplate.executeCommand(ZKCreateIndexes);

            /*** 插入数据准备查询 ***/
            ZKInsert ZKInsert = new ZKInsert(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            Document doc = new Document();
            doc.put(ZKMongoUtils.autoIndexIdName, id1);
            doc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            doc.put(attrName1, id1 + ":" + attrValue1);
            doc.put(attrName2, id1 + ":" + attrValue2);
            doc.put(attrName3, attrValue3);
            ZKInsert.addDoc(doc);
            doc = new Document();
            doc.put(ZKMongoUtils.autoIndexIdName, id2);
            doc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            doc.put(attrName1, id2 + ":" + attrValue1);
            doc.put(attrName2, id2 + ":" + attrValue2);
            doc.put(attrName3, attrValue3);
            ZKInsert.addDoc(doc);
            doc = new Document();
            doc.put(ZKMongoUtils.autoIndexIdName, id3);
            doc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            doc.put(attrName1, id3 + ":" + attrValue1);
            doc.put(attrName2, id3 + ":" + attrValue2);
//          doc.put(attrName3, attrValue3);
            ZKInsert.addDoc(doc);

            Document resDoc = mongoTemplate.executeCommand(ZKInsert);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(3, resDoc.getInteger("n").intValue());

            /*** ZKDelete ***/
            ZKQueryOpt queryOpt;

            ZKDelete ZKDelete = new ZKDelete(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            queryOpt = ZKQueryOpt.or(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id1).exists(true),
                    ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id2),
                    ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id3));
            ZKDeleteElement de = new ZKDeleteElement(
                    ZKQueryOpt.and(queryOpt, ZKQueryOpt.where(attrName3).exists(true)));
            ZKDelete.addDeletes(de);
            resDoc = mongoTemplate.executeCommand(ZKDelete);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(2, resDoc.getInteger("n").intValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }
}
