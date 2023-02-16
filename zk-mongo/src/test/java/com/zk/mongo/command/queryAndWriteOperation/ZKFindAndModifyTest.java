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
 * @Title: ZKFindAndModifyTest.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.queryAndWriteOperation 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:13:30 PM 
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
import com.zk.mongo.element.ZKIndexElement;
import com.zk.mongo.helper.ZKMongoTestConfig;
import com.zk.mongo.operator.ZKQueryOpt;
import com.zk.mongo.operator.ZKUpdateOpt;
import com.zk.mongo.utils.ZKMongoUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKFindAndModifyTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFindAndModifyTest {

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
    public void testCommandUpdate() {

        String id1 = "ZKFindAndModify_id_1";
        String id2 = "ZKFindAndModify_id_2";

        String attrName1 = "a1";
        String attrValue1 = "v1";
        String attrName2 = "a2";
        String attrValue2 = "v2";
        String attrName3 = "a3";
        int attrValue3 = 3;

        String attrName4 = "a4";
        String attrValue4 = "-v4";

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

            Document resDoc;

            /*** findAndModify 新增，文档不存在，新增 ***/
            ZKFindAndModify findAndModify = new ZKFindAndModify(
                    ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            ZKQueryOpt queryOpt = ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id1);
            findAndModify.setQuery(queryOpt);
            ZKUpdateOpt updateOpt = new ZKUpdateOpt();
            updateOpt.set(attrName1, id1 + attrValue1);
            updateOpt.setOnInsert(attrName2, id1 + attrValue2);
            updateOpt.inc(attrName3, attrValue3);
            findAndModify.setUpdate(updateOpt);
            // 文档不存在，新增
            findAndModify.setUpsert(true);
            resDoc = mongoTemplate.executeCommand(findAndModify);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());
            // 查询结果验证
            Document documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            // 指定查询条件
            Document filterDoc = new Document();
            Document compareDoc = new Document();
            compareDoc.put("$eq", id1);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertNotNull(resDoc);

            /*** findAndModify 新增，文档不存在，不新增 ***/
            findAndModify = new ZKFindAndModify(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            queryOpt = ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id2);
            findAndModify.setQuery(queryOpt);
            updateOpt = new ZKUpdateOpt();
            updateOpt.set(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            updateOpt.set(attrName1, id2 + attrValue1);
            updateOpt.setOnInsert(attrName2, id2 + attrValue2);
            updateOpt.inc(attrName3, attrValue3);
            findAndModify.setUpdate(updateOpt);
            // 文档不存在，不新增新增
            findAndModify.setUpsert(false);
            resDoc = mongoTemplate.executeCommand(findAndModify);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(0, resDoc.getInteger("n").intValue());
            // 查询结果验证
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            // 指定查询条件
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", id2);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertNull(resDoc);

            /*** findAndModify 新增，多插入个干扰文档 ***/
            findAndModify = new ZKFindAndModify(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            queryOpt = ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id2);
            findAndModify.setQuery(queryOpt);
            updateOpt = new ZKUpdateOpt();
            updateOpt.set(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            updateOpt.set(attrName1, id2 + attrValue1);
            updateOpt.setOnInsert(attrName2, id2 + attrValue2);
            updateOpt.inc(attrName3, attrValue3);
            findAndModify.setUpdate(updateOpt);
            // 文档不存在，不新增新增
            findAndModify.setUpsert(true);
            resDoc = mongoTemplate.executeCommand(findAndModify);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());

            /*** findAndModify 文档存在，添加/修改 属性 ***/
            findAndModify = new ZKFindAndModify(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            queryOpt = ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id1);
            findAndModify.setQuery(queryOpt);
            updateOpt = new ZKUpdateOpt();
            updateOpt.set(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            updateOpt.set(attrName4, id1 + attrValue4);
            findAndModify.setUpdate(updateOpt);
            // 文档不存在，不新增新增
            findAndModify.setUpsert(false);
            resDoc = mongoTemplate.executeCommand(findAndModify);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());
            // 查询结果验证
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            // 指定查询条件
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", id1);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(id1 + attrValue4, resDoc.getString(attrName4));

            /*** findAndModify 文档存在，删除属性 ***/
            findAndModify = new ZKFindAndModify(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            queryOpt = ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id1);
            findAndModify.setQuery(queryOpt);
            updateOpt = new ZKUpdateOpt();
            updateOpt.unset(attrName4);
            findAndModify.setUpdate(updateOpt);
            findAndModify.setUpsert(false);
            resDoc = mongoTemplate.executeCommand(findAndModify);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());
            // 查询结果验证
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            // 指定查询条件
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", id1);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(null, resDoc.getString(attrName4));

            /*** findAndModify 文档存在，删除文档 ***/
            findAndModify = new ZKFindAndModify(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            queryOpt = ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id1);
            findAndModify.setQuery(queryOpt);
            findAndModify.setRemove(true);
            resDoc = mongoTemplate.executeCommand(findAndModify);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());
            // 查询结果验证
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            // 指定查询条件
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", id1);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(null, resDoc);

            /*** findAndModify 文档不存在存在，删除文档 ***/
            findAndModify = new ZKFindAndModify(ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            queryOpt = ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(id1);
            findAndModify.setQuery(queryOpt);
            findAndModify.setRemove(true);
            resDoc = mongoTemplate.executeCommand(findAndModify);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(0, resDoc.getInteger("n").intValue());

            // 查询结果验证
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_QueryAndWriteOperation);
            // 指定查询条件
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", id2);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertNotNull(resDoc);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
