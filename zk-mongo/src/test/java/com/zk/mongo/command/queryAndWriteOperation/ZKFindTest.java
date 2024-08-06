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
 * @Title: ZKFindTest.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.queryAndWriteOperation 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:13:51 PM 
 * @version V1.0   
*/
package com.zk.mongo.command.queryAndWriteOperation;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.mongo.ZKMongoTemplateTest;
import com.zk.mongo.command.administration.ZKCreate;
import com.zk.mongo.command.administration.ZKCreateIndexes;
import com.zk.mongo.command.administration.ZKDrop;
import com.zk.mongo.element.ZKIndexElement;
import com.zk.mongo.helper.ZKMongoTestConfig;
import com.zk.mongo.operator.ZKQueryOpt;
import com.zk.mongo.utils.ZKMongoUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKFindTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFindTest {

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
    public void testCommandZKFind() {

        String id1 = "ZKFind_id_1";
        String id2 = "ZKFind_id_2";
        String id3 = "ZKFind_id_3";

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

            /*** 插入数据准备查询 ***/
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
            doc = new Document();
            doc.put(ZKMongoUtils.autoIndexIdName, id3);
            doc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            doc.put(attrName1, id3 + ":" + attrValue1);
            doc.put(attrName2, id3 + ":" + attrValue2);
//          doc.put(attrName3, attrValue3);
            insert.addDoc(doc);
            Document resDoc = mongoTemplate.executeCommand(insert);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(3, resDoc.getInteger("n").intValue());

            List<Document> resDocs = null;

            /*** find 查询，指定返回字段 ***/
            ZKFind find = new ZKFind(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            // 返回 指定属性， 和 _id
            find.setProjection(Arrays.asList(attrName1, attrName2), null);
            find.setFilter(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id1));
            resDoc = mongoTemplate.executeCommand(find);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(3, resDoc.size());
            TestCase.assertEquals(id1, resDoc.getString(ZKMongoUtils.autoIndexIdName));
            TestCase.assertEquals(id1 + ":" + attrValue1, resDoc.getString(attrName1));
            TestCase.assertEquals(id1 + ":" + attrValue2, resDoc.getString(attrName2));
            // 返回 指定属性，不返回 _id
            find.setProjection(null, null);
            find.setProjection(Arrays.asList(attrName1, attrName2), Arrays.asList(ZKMongoUtils.autoIndexIdName));
            find.setFilter(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id1));
            resDoc = mongoTemplate.executeCommand(find);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(2, resDoc.size());
            TestCase.assertEquals(null, resDoc.getString(ZKMongoUtils.autoIndexIdName));
            TestCase.assertEquals(id1 + ":" + attrValue1, resDoc.getString(attrName1));
            TestCase.assertEquals(id1 + ":" + attrValue2, resDoc.getString(attrName2));
            // 不返回 指定属性
            find.setProjection(null, null);
            find.setProjection(null, Arrays.asList(ZKMongoUtils.autoIndexIdName));
            find.setFilter(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id1));
            resDoc = mongoTemplate.executeCommand(find);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(4, resDoc.size());
            TestCase.assertEquals(null, resDoc.getString(ZKMongoUtils.autoIndexIdName));
            TestCase.assertEquals(id1 + ":" + attrValue1, resDoc.getString(attrName1));
            TestCase.assertEquals(id1 + ":" + attrValue2, resDoc.getString(attrName2));
            TestCase.assertNotNull(resDoc.get(attrName3));
            TestCase.assertNotNull(resDoc.get(ZKMongoTestConfig.attrExpireTimeName));

            /*** find or and 查询 ***/
            ZKQueryOpt queryOpt;

            find = new ZKFind(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            queryOpt = ZKQueryOpt.or(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id1).exists(true),
                    ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id2),
                    ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id3));
            find.setFilter(queryOpt);
            resDoc = mongoTemplate.executeCommand(find);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDocs = ZKMongoUtils.getListResult(resDoc);
            TestCase.assertEquals(3, resDocs.size());

            find = new ZKFind(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            find.setFilter(ZKQueryOpt.and(queryOpt, ZKQueryOpt.where(attrName3).exists(true)));
            resDoc = mongoTemplate.executeCommand(find);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDocs = ZKMongoUtils.getListResult(resDoc);
            TestCase.assertEquals(2, resDocs.size());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
