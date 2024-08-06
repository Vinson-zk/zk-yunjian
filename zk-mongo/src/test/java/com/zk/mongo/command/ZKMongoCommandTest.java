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
 * @Title: ZKMongoCommandTest.java 
 * @author Vinson 
 * @Package com.zk.mongo.command 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:08:51 PM 
 * @version V1.0   
*/
package com.zk.mongo.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.mongo.helper.ZKMongoTestConfig;
import com.zk.mongo.utils.ZKMongoUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMongoCommandTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMongoCommandTest {

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

    /**
     * 测试 Administration Command listCollections create drop listIndexes
     * createIndexes dropIndexes
     */
    @Test
    public void testAdministrationCommand() {
        try {
            boolean isTrue;
            Document documentCommand = null;
            Document resDoc = null;
            List<Document> resDocs = null;

            /*** 判断集合是否存在，此时可能存在，也可能不存在 ***/
            documentCommand = new Document();
            documentCommand.put("listCollections", 1);
            Document listCollectionsDoc = new Document();
            listCollectionsDoc.put("name", ZKMongoTestConfig.colleationName_command);
            documentCommand.put("filter", listCollectionsDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDocs = ZKMongoUtils.getListResult(resDoc);
            if (resDocs.size() > 0) {
                // 集合存在
                System.out.println(String.format("[^_^:20180529-2223-001] 集合 {} 存在！销毁集合！！！",
                        ZKMongoTestConfig.colleationName_command));
                // 销毁集合
                documentCommand = new Document();
                documentCommand.put("drop", ZKMongoTestConfig.colleationName_command);
                resDoc = mongoTemplate.executeCommand(documentCommand);
                TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            }
            else {
                // 集合不存在
                System.out.println(
                        String.format("[^_^:20180529-2223-001] 集合 {} 不存在！", ZKMongoTestConfig.colleationName_command));
            }

            /*** 销毁一个不存在的集合 抛出异常 ***/
            try {
                documentCommand = new Document();
                documentCommand.put("drop", ZKMongoTestConfig.colleationName_command);
                mongoTemplate.executeCommand(documentCommand);
                TestCase.assertTrue(false);
            }
            catch(UncategorizedMongoDbException e) {
                TestCase.assertTrue(true);
            }

            /*** 创建集合 ***/
            documentCommand = new Document();
            documentCommand.put("create", ZKMongoTestConfig.colleationName_command);
            documentCommand.put("autoIndexId", true);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());

            /*** 重复创建集合 抛出异常 ***/
            try {
                documentCommand = new Document();
                documentCommand.put("create", ZKMongoTestConfig.colleationName_command);
                documentCommand.put("autoIndexId", true);
                mongoTemplate.executeCommand(documentCommand);
                TestCase.assertTrue(false);
            }
            catch(UncategorizedMongoDbException e) {
                TestCase.assertTrue(true);
            }

            /*** 判断集合是否存在，此时已存在 ***/
            documentCommand = new Document();
            documentCommand.put("listCollections", 1);
            Document lcDoc = new Document();
            lcDoc.put("name", ZKMongoTestConfig.colleationName_command);
            documentCommand.put("filter", lcDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDocs = ZKMongoUtils.getListResult(resDoc);
            TestCase.assertEquals(1, resDocs.size());

            /*** 判断一个索引是否存在, 此时不存在 ***/
            documentCommand = new Document();
            documentCommand.put("listIndexes", ZKMongoTestConfig.colleationName_command);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDocs = ZKMongoUtils.getListResult(resDoc);

            isTrue = false;
            for (Document doc : resDocs) {
                if (ZKMongoTestConfig.indexExpireTimeName.equals(doc.getString("name"))) {
                    isTrue = true;
                    break;
                }
            }
            // 如果索引已存在，用这下代码测试
            TestCase.assertFalse(isTrue);
//          if(isTrue){
//              // 索引存在
//              System.out.println(String.format("[^_^:20180529-2223-002] 集合 {} 中索引 {} 存在，销毁！！！", ZKMongoTestConfig.colleationName_command, ZKMongoTestConfig.indexExpireTimeName));
//              // 删除索引
//              documentCommand = new Document();
//              documentCommand.put("dropIndexes", ZKMongoTestConfig.colleationName_command);
//              documentCommand.put("index", ZKMongoTestConfig.indexExpireTimeName);
//              resDoc = mongoTemplate.executeCommand(documentCommand);
//              TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
//          }else{
//              // 索引不存在
//              System.out.println(String.format("[^_^:20180529-2223-002] 集合 {} 中索引 {} 不存在！", ZKMongoTestConfig.colleationName_command, ZKMongoTestConfig.indexExpireTimeName));
//          }

            /*** 销毁不存在的索引 ***/
            try {
                documentCommand = new Document();
                documentCommand.put("dropIndexes", ZKMongoTestConfig.colleationName_command);
                documentCommand.put("index", ZKMongoTestConfig.indexExpireTimeName);
                mongoTemplate.executeCommand(documentCommand);
                TestCase.assertTrue(false);
            }
            catch(UncategorizedMongoDbException e) {
                TestCase.assertTrue(true);
            }

            /*** 创建索引 ***/
            documentCommand = new Document();
            documentCommand.put("createIndexes", ZKMongoTestConfig.colleationName_command);
            Document indexDoc = new Document();
            Document keyDoc = new Document();
            keyDoc.put(ZKMongoTestConfig.attrExpireTimeName, 1);
            indexDoc.put("key", keyDoc);
            indexDoc.put("name", ZKMongoTestConfig.indexExpireTimeName);
            // 0 秒后过期
            indexDoc.put("expireAfterSeconds", 0);
            documentCommand.put("indexes", Arrays.asList(indexDoc));
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());

            /*** 判断一个索引是否存在 ***/
            documentCommand = new Document();
            documentCommand.put("listIndexes", ZKMongoTestConfig.colleationName_command);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDocs = ZKMongoUtils.getListResult(resDoc);
            isTrue = false;
            for (Document doc : resDocs) {
                if (ZKMongoTestConfig.indexExpireTimeName.equals(doc.getString("name"))) {
                    isTrue = true;
                    break;
                }
            }
            TestCase.assertTrue(isTrue);

            /*** 删除索引 ***/
            documentCommand = new Document();
            documentCommand.put("dropIndexes", ZKMongoTestConfig.colleationName_command);
            documentCommand.put("index", ZKMongoTestConfig.indexExpireTimeName);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());

            /*** 销毁集合 ***/
            documentCommand = new Document();
            documentCommand.put("drop", ZKMongoTestConfig.colleationName_command);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 测试 Query and write operation commands insert find delete
     */
    @Test
    public void testQueryWriteCommandsInsertAndFindAndDelete() {

        String id1 = "_id_1";
        String id2 = "_id_2";
        String id3 = "_id_3";

        String attrName1 = "a1";
        String attrValue1 = "v1";
        String attrName2 = "a2";
        String attrValue2 = "v2";
        String attrName3 = "a3";
        int attrValue3 = 3;

        try {

            Document documentCommand = null;
            Document resDoc = null;
            List<Document> resDocs = null;

            // 如果集合不存在先创建集合
            documentCommand = new Document();
            documentCommand.put("listCollections", 1);
            Document listCollectionsDoc = new Document();
            listCollectionsDoc.put("name", ZKMongoTestConfig.colleationName_command);
            documentCommand.put("filter", listCollectionsDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDocs = ZKMongoUtils.getListResult(resDoc);
            if (resDocs.size() > 0) {
                // 集合存在
                System.out.println(String.format("[^_^:20180530-1406-001] 集合 {} 存在！销毁集合！！！",
                        ZKMongoTestConfig.colleationName_command));
                // 销毁集合
                documentCommand = new Document();
                documentCommand.put("drop", ZKMongoTestConfig.colleationName_command);
                resDoc = mongoTemplate.executeCommand(documentCommand);
                TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            }
            else {
                // 集合不存在
                System.out.println(
                        String.format("[^_^:20180530-1406-001] 集合 {} 不存在！", ZKMongoTestConfig.colleationName_command));
            }

            // 创建集合和索引
            documentCommand = new Document();
            documentCommand.put("create", ZKMongoTestConfig.colleationName_command);
            documentCommand.put("autoIndexId", true);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            documentCommand = new Document();
            // 创建索引
            documentCommand.put("createIndexes", ZKMongoTestConfig.colleationName_command);
            Document createIndexesDoc = new Document();
            Document keyDoc = new Document();
            keyDoc.put(ZKMongoTestConfig.attrExpireTimeName, 1);
            createIndexesDoc.put("key", keyDoc);
            createIndexesDoc.put("name", ZKMongoTestConfig.indexExpireTimeName);
            // 0 秒后过期
            createIndexesDoc.put("expireAfterSeconds", 0);
            documentCommand.put("indexes", Arrays.asList(createIndexesDoc));
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());

            /*** insert 插入, 文档不存在 ***/
            // 插入第一个
            documentCommand = new Document();
            documentCommand.put("insert", ZKMongoTestConfig.colleationName_command);
            Document insertDoc = new Document();
            insertDoc.put(ZKMongoUtils.autoIndexIdName, id1);
            insertDoc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            insertDoc.put(attrName1, id1 + ":" + attrValue1);
            insertDoc.put(attrName2, id1 + ":" + attrValue2);
            insertDoc.put(attrName3, attrValue3);
            documentCommand.put("documents", Arrays.asList(insertDoc));
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());
            // 插入第二个
            documentCommand = new Document();
            documentCommand.put("insert", ZKMongoTestConfig.colleationName_command);
            insertDoc = new Document();
            insertDoc.put(ZKMongoUtils.autoIndexIdName, id2);
            insertDoc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            insertDoc.put(attrName1, id2 + ":" + attrValue1);
            insertDoc.put(attrName2, id2 + ":" + attrValue2);
            insertDoc.put(attrName3, attrValue3);
            documentCommand.put("documents", Arrays.asList(insertDoc));
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());
            // 插入第三个
            documentCommand = new Document();
            documentCommand.put("insert", ZKMongoTestConfig.colleationName_command);
            insertDoc = new Document();
            insertDoc.put(ZKMongoUtils.autoIndexIdName, id3);
            insertDoc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            insertDoc.put(attrName1, id3 + ":" + attrValue1);
            insertDoc.put(attrName2, id3 + ":" + attrValue2);
            insertDoc.put(attrName3, attrValue3);
            documentCommand.put("documents", Arrays.asList(insertDoc));
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());

            /*** insert 插入, 文档存在，插入失败 ***/
            documentCommand = new Document();
            documentCommand.put("insert", ZKMongoTestConfig.colleationName_command);
            insertDoc = new Document();
            insertDoc.put(ZKMongoUtils.autoIndexIdName, id1);
            insertDoc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            insertDoc.put(attrName1, id1 + attrValue1 + attrValue1);
            documentCommand.put("documents", Arrays.asList(insertDoc));
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(0, resDoc.getInteger("n").intValue());

            /***
             * find 查询，指定返回字段 *** 1、Return the Specified Fields and the _id
             * Field Only 设置指定返回的字段为 1，_id 会默认返回；如：{attrName:1} 2、Return All But
             * the Excluded Fields 设置不指定返回的字段为 0；如：{attrName:0} 3、Suppress _id
             * Field 设置指定返回的字段为 1，设置 _id 为 0；如：{attrName:1, "_id":0} 其他说明参见官方文档
             */
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
            // 指定查询条件
            Document filterDoc = new Document();
            Document compareDoc = new Document();
            compareDoc.put("$eq", id2);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            // 过虑
            Document projectionDoc = new Document();
            projectionDoc.put(ZKMongoUtils.autoIndexIdName, 0);
            projectionDoc.put(attrName1, 1);
            projectionDoc.put(attrName3, 1);
            documentCommand.put("projection", projectionDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(2, resDoc.size());
            TestCase.assertEquals(attrValue3, resDoc.getInteger(attrName3).intValue());
            TestCase.assertEquals(id2 + ":" + attrValue1, resDoc.getString(attrName1));

            // or 查询
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
            // 指定查询条件
            filterDoc = new Document();
            Document f1Doc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", id1);
            f1Doc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            Document f2Doc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", id2);
            f2Doc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            filterDoc.put("$or", Arrays.asList(f1Doc, f2Doc));
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDocs = ZKMongoUtils.getListResult(resDoc);
            TestCase.assertEquals(2, resDocs.size());

            // or 查询
//                      documentCommand = new Document();
//                      documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
//                      // 指定查询条件
//                      filterDoc = new Document();
//                      f1Doc = new Document();
//                      compareDoc = new Document();
//                      compareDoc.put("$eq", id1);
//                      f1Doc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
////                        f2Doc = new Document();
////                        compareDoc = new Document();
////                        compareDoc.put("$eq", attrValue3);
////                        f2Doc.put(attrName3, compareDoc);
//                      filterDoc.put("$and", Arrays.asList(f1Doc));
//                      f1Doc = new Document();
//                      compareDoc = new Document();
//                      compareDoc.put("$eq", id2);
//                      f1Doc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
//                      filterDoc.put("$or", Arrays.asList(f1Doc));
//                      documentCommand.put("filter", filterDoc);
//                      resDoc = mongoTemplate.executeCommand(documentCommand);
//                      TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
//                      resDocs = ZKMongoUtils.getListResult(resDoc);
//                      TestCase.assertEquals(2, resDocs.size());

            /*** delete 删除 ***/
            documentCommand = new Document();
            documentCommand.put("delete", ZKMongoTestConfig.colleationName_command);
            // 过虑条件
            List<Document> deletes = new ArrayList<>();
            Document deleteDoc = new Document();
            Document qDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", id2);
            qDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            deleteDoc.put("q", qDoc);
            deleteDoc.put("limit", 0);
            deletes.add(deleteDoc);
            deleteDoc = new Document();
            qDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", id3);
            qDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            deleteDoc.put("q", qDoc);
            deleteDoc.put("limit", 0);
            deletes.add(deleteDoc);
            documentCommand.put("deletes", deletes);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(2, resDoc.getInteger("n").intValue());
            // 查询验证删除
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
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
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
            // 指定查询条件
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", id1);
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

    /**
     * 测试 Query and write operation commands Update $setOnInsert 仅在插入时有效 $set
     * 替换指定属性的值，属性不存在，新增,默认为这个值 $inc 以指定值递增，属性不存在添加 $unset 删除指定字段
     */
    @Test
    public void testQueryWriteCommandsUpdate() {

        String idUpdate0 = "_id_update0";
        String idUpdate = "_id_update";

        String attrName = "updateCommand";
        String attrValue = "updateCommand-value";

        String attrIntName = "updateCommand--int";
        int attrIntValue = 2;

        try {

            Document documentCommand = null;
            Document resDoc = null;
            List<Document> resDocs = null;

            // 如果集合不存在先创建集合
            documentCommand = new Document();
            documentCommand.put("listCollections", 1);
            Document listCollectionsDoc = new Document();
            listCollectionsDoc.put("name", ZKMongoTestConfig.colleationName_command);
            documentCommand.put("filter", listCollectionsDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDocs = ZKMongoUtils.getListResult(resDoc);
            if (resDocs.size() > 0) {
                // 集合存在
                System.out.println(String.format("[^_^:20180530-2355-001] 集合 {} 存在！销毁集合！！！",
                        ZKMongoTestConfig.colleationName_command));
                // 销毁集合
                documentCommand = new Document();
                documentCommand.put("drop", ZKMongoTestConfig.colleationName_command);
                resDoc = mongoTemplate.executeCommand(documentCommand);
                TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            }
            else {
                // 集合不存在
                System.out.println(
                        String.format("[^_^:20180530-2355-001] 集合 {} 不存在！", ZKMongoTestConfig.colleationName_command));
            }

            // 创建集合和索引
            documentCommand = new Document();
            documentCommand.put("create", ZKMongoTestConfig.colleationName_command);
            documentCommand.put("autoIndexId", true);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            documentCommand = new Document();
            // 创建索引
            documentCommand.put("createIndexes", ZKMongoTestConfig.colleationName_command);
            Document createIndexesDoc = new Document();
            Document keyDoc = new Document();
            keyDoc.put(ZKMongoTestConfig.attrExpireTimeName, 1);
            createIndexesDoc.put("key", keyDoc);
            createIndexesDoc.put("name", ZKMongoTestConfig.indexExpireTimeName);
            // 0 秒后过期
            createIndexesDoc.put("expireAfterSeconds", 0);
            documentCommand.put("indexes", Arrays.asList(createIndexesDoc));
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());

            /*** update 插入两条不存在在的文档，一个执行插入，一个不执行插入 ***/
            documentCommand = new Document();
            documentCommand.put("update", ZKMongoTestConfig.colleationName_command);
            Document updateDoc = new Document();
            Document qDoc = new Document();
            Document compareDoc = new Document();
            compareDoc.put("$eq", idUpdate0);
            qDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            updateDoc.put("q", qDoc);
            Document uDoc = new Document();
//          uDoc.put(ZKMongoUtils.autoIndexIdName, idUpdate0);
            uDoc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            uDoc.put(attrIntName, attrIntValue);
            uDoc.put(attrName, idUpdate0 + ":" + attrValue);
            updateDoc.put("u", uDoc);
            // 不存在插入
            updateDoc.put("upsert", true);
            List<Document> updateDocs = new ArrayList<>();
            updateDocs.add(updateDoc);
            updateDoc = new Document();
            qDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idUpdate);
            qDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            updateDoc.put("q", qDoc);
            uDoc = new Document();
//          uDoc.put(ZKMongoUtils.autoIndexIdName, idUpdate0);
            uDoc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            uDoc.put(attrIntName, attrIntValue);
            uDoc.put(attrName, idUpdate + ":" + attrValue);
            updateDoc.put("u", uDoc);
            // 不存在不插入
            updateDoc.put("upsert", false);
            updateDocs.add(updateDoc);
            documentCommand.put("updates", updateDocs);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());
            // 查询验证插入
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
            compareDoc = new Document();
            compareDoc.put("$eq", idUpdate0);
            Document filterDoc = new Document();
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDocs = ZKMongoUtils.getListResult(resDoc);
            TestCase.assertEquals(1, resDocs.size());
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
            compareDoc = new Document();
            compareDoc.put("$eq", idUpdate);
            filterDoc = new Document();
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDocs = ZKMongoUtils.getListResult(resDoc);
            TestCase.assertEquals(0, resDocs.size());

            /*** update 文档不存在，插入，并插入属性 ***/
            documentCommand = new Document();
            documentCommand.put("update", ZKMongoTestConfig.colleationName_command);

            updateDoc = new Document();
            qDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idUpdate);
            qDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            updateDoc.put("q", qDoc);
            uDoc = new Document();
            Document setDoc = new Document();
            Document incDoc = new Document();
            // 文档插入时生效
            setDoc.put(attrName, idUpdate + ":" + attrValue);
            setDoc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            // 文档插入时,没有递增，直接插入
            incDoc.put(attrIntName, attrIntValue);
            uDoc.put("$set", setDoc);
            uDoc.put("$inc", incDoc);
            updateDoc.put("u", uDoc);
            updateDoc.put("upsert", true);
            documentCommand.put("updates", Arrays.asList(updateDoc));
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());
            // 查询验证插入
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
            compareDoc = new Document();
            compareDoc.put("$eq", idUpdate);
            filterDoc = new Document();
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(idUpdate + ":" + attrValue, resDoc.getString(attrName));
            TestCase.assertEquals(attrIntValue, resDoc.getInteger(attrIntName).intValue());

            /*** update 文档存在，属性递增，与 setOnInsert 仅插入文档时生效 ***/
            documentCommand = new Document();
            documentCommand.put("update", ZKMongoTestConfig.colleationName_command);
            updateDoc = new Document();
            qDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idUpdate);
            qDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            updateDoc.put("q", qDoc);
            uDoc = new Document();
            Document setOnInsertDoc = new Document();
            incDoc = new Document();
            // 文档插入时生效，修改无效
            setOnInsertDoc.put(attrName, attrValue);
            // 文档插入时,没有递增，直接插入
            incDoc.put(attrIntName, 5);
            uDoc.put("$setOnInsert", setOnInsertDoc);
            uDoc.put("$inc", incDoc);
            updateDoc.put("u", uDoc);
            // 文档不存在时，不插入
            updateDoc.put("upsert", false);
            documentCommand.put("updates", Arrays.asList(updateDoc));
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());
            // 查询验证插入
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
            compareDoc = new Document();
            compareDoc.put("$eq", idUpdate);
            filterDoc = new Document();
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(idUpdate + ":" + attrValue, resDoc.getString(attrName));
            TestCase.assertEquals(attrIntValue + 5, resDoc.getInteger(attrIntName).intValue());

            /*** 删除属性； 删除属性 ***/
            documentCommand = new Document();
            documentCommand.put("update", ZKMongoTestConfig.colleationName_command);
            updateDoc = new Document();
            qDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idUpdate);
            qDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            updateDoc.put("q", qDoc);
            uDoc = new Document();
            Document unsetDoc = new Document();
            unsetDoc.put(attrName, "");
            uDoc.put("$unset", unsetDoc);
            updateDoc.put("u", uDoc);
            // 文档不存在时，不插入
            updateDoc.put("upsert", false);
            documentCommand.put("updates", Arrays.asList(updateDoc));
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());
            // 查询验证插入
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
            compareDoc = new Document();
            compareDoc.put("$eq", idUpdate);
            filterDoc = new Document();
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(null, resDoc.getString(attrName));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 测试 Query and write operation commands findAndModify
     */
    @Test
    public void testQueryWriteCommandsFindAndModify() {

        String idFindAndModify0 = "_id_findAndModify0";
        String idFindAndModify = "_id_findAndModify";

        String attrName = "queryAndWeritCommand";
        String attrValue = "queryAndWeritCommand-value";

        String attrName1 = "queryAndWeritCommand--1";
        String attrValue1 = "queryAndWeritCommand-value--1";

        try {

            Document documentCommand = null;
            Document resDoc = null;
            List<Document> resDocs = null;

            // 如果集合不存在先创建集合
            documentCommand = new Document();
            documentCommand.put("listCollections", 1);
            Document listCollectionsDoc = new Document();
            listCollectionsDoc.put("name", ZKMongoTestConfig.colleationName_command);
            documentCommand.put("filter", listCollectionsDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDocs = ZKMongoUtils.getListResult(resDoc);
            if (resDocs.size() > 0) {
                // 集合存在
                System.out.println(String.format("[^_^:20180530-2354-001] 集合 {} 存在！销毁集合！！！",
                        ZKMongoTestConfig.colleationName_command));
                // 销毁集合
                documentCommand = new Document();
                documentCommand.put("drop", ZKMongoTestConfig.colleationName_command);
                resDoc = mongoTemplate.executeCommand(documentCommand);
                TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            }
            else {
                // 集合不存在
                System.out.println(
                        String.format("[^_^:20180530-2354-001] 集合 {} 不存在！", ZKMongoTestConfig.colleationName_command));
            }

            // 创建集合和索引
            documentCommand = new Document();
            documentCommand.put("create", ZKMongoTestConfig.colleationName_command);
            documentCommand.put("autoIndexId", true);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            documentCommand = new Document();
            // 创建索引
            documentCommand.put("createIndexes", ZKMongoTestConfig.colleationName_command);
            Document createIndexesDoc = new Document();
            Document keyDoc = new Document();
            keyDoc.put(ZKMongoTestConfig.attrExpireTimeName, 1);
            createIndexesDoc.put("key", keyDoc);
            createIndexesDoc.put("name", ZKMongoTestConfig.indexExpireTimeName);
            // 0 秒后过期
            createIndexesDoc.put("expireAfterSeconds", 0);
            documentCommand.put("indexes", Arrays.asList(createIndexesDoc));
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());

            /*** findAndModify 新增一个干扰文档 ***/
            documentCommand = new Document();
            documentCommand.put("findAndModify", ZKMongoTestConfig.colleationName_command);
            Document filterDoc = new Document();
            Document compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify0);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("query", filterDoc);
            Document updateDoc = new Document();
//          updateDoc.put(ZKMongoUtils.autoIndexIdName, idFindAndModify0);
            updateDoc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            updateDoc.put(attrName, attrValue);
            documentCommand.put("update", updateDoc);
            documentCommand.put("upsert", true);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());

            /*** findAndModify 文档不存在, 不新增 ***/
            documentCommand = new Document();
            documentCommand.put("findAndModify", ZKMongoTestConfig.colleationName_command);
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("query", filterDoc);
            updateDoc = new Document();
//          updateDoc.put(ZKMongoUtils.autoIndexIdName, idFindAndModify);
            updateDoc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            documentCommand.put("update", updateDoc);
            // 文档不存在不新增
            documentCommand.put("upsert", false);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(0, resDoc.getInteger("n").intValue());

            /*** findAndModify 文档不存在, 新增 ***/
            documentCommand = new Document();
            documentCommand.put("findAndModify", ZKMongoTestConfig.colleationName_command);
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("query", filterDoc);
            updateDoc = new Document();
//          updateDoc.put(ZKMongoUtils.autoIndexIdName, idFindAndModify);
            updateDoc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            documentCommand.put("update", updateDoc);
            // 文档不存在 新增
            documentCommand.put("upsert", true);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());

            /*** findAndModify 文档存在，添加/修改 属性 ***/
            // $setOnInsert 仅在插入时有效
            // $set 替换指定属性的值，属性不存在，新增,默认为这个值
            // $inc 以指定值递增
            // $unset 删除指定字段
            documentCommand = new Document();
            documentCommand.put("findAndModify", ZKMongoTestConfig.colleationName_command);
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("query", filterDoc);
            updateDoc = new Document();
            updateDoc.put(attrName, idFindAndModify + ":" + attrValue);
            documentCommand.put("update", updateDoc);
            // 文档不存在 不新增
            documentCommand.put("upsert", false);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());
            // 查出修改结果验证
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            Document projectionDoc = new Document();
            projectionDoc.put(attrName, 1);
            documentCommand.put("projection", projectionDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(idFindAndModify + ":" + attrValue, resDoc.getString(attrName));
            // 验证是否影响到其他数据
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify0);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(attrValue, resDoc.getString(attrName));

            /*** findAndModify 文档存在，删除属性 ***/
            documentCommand = new Document();
            documentCommand.put("findAndModify", ZKMongoTestConfig.colleationName_command);
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("query", filterDoc);
            updateDoc = new Document();
            Document dDoc = new Document();
            dDoc.put(attrName, attrValue);
            updateDoc.put("$setOnInsert", dDoc);
            dDoc = new Document();
            dDoc.put(attrName1, idFindAndModify + ":" + attrValue1);
            updateDoc.put("$set", dDoc);
            documentCommand.put("update", updateDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());
            // 查出修改结果验证
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            projectionDoc = new Document();
            projectionDoc.put(attrName, 1);
            projectionDoc.put(attrName1, 1);
            documentCommand.put("projection", projectionDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(idFindAndModify + ":" + attrValue, resDoc.getString(attrName));
            TestCase.assertEquals(idFindAndModify + ":" + attrValue1, resDoc.getString(attrName1));
            // 删除属性 attrName1
            documentCommand = new Document();
            documentCommand.put("findAndModify", ZKMongoTestConfig.colleationName_command);
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("query", filterDoc);
            updateDoc = new Document();
            dDoc = new Document();
            dDoc.put(attrName1, "");
            updateDoc.put("$unset", dDoc);
            documentCommand.put("update", updateDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());
            // 查出修改结果验证
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            projectionDoc = new Document();
            projectionDoc.put(attrName, 1);
            projectionDoc.put(attrName1, 1);
            documentCommand.put("projection", projectionDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(idFindAndModify + ":" + attrValue, resDoc.getString(attrName));
            TestCase.assertEquals(null, resDoc.getString(attrName1));
            // 验证是否影响到其他数据
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify0);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(attrValue, resDoc.getString(attrName));

            /*** findAndModify 文档存在，删除文档 ***/
            documentCommand = new Document();
            documentCommand.put("findAndModify", ZKMongoTestConfig.colleationName_command);
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("query", filterDoc);
            // 此时 update 要求为 null
            documentCommand.put("remove", true);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            // 查出修改结果验证
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertNull(resDoc);

            /*** findAndModify 文档不存在存在，删除文档 ***/
            documentCommand = new Document();
            documentCommand.put("findAndModify", ZKMongoTestConfig.colleationName_command);
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("query", filterDoc);
            // 此时 update 要求为 null
            documentCommand.put("remove", true);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = resDoc.get("lastErrorObject", Document.class);
            TestCase.assertEquals(0, resDoc.getInteger("n").intValue());
            // 验证是否影响到其他数据
            documentCommand = new Document();
            documentCommand.put("find", ZKMongoTestConfig.colleationName_command);
            filterDoc = new Document();
            compareDoc = new Document();
            compareDoc.put("$eq", idFindAndModify0);
            filterDoc.put(ZKMongoUtils.autoIndexIdName, compareDoc);
            documentCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDoc = ZKMongoUtils.getOneResult(resDoc);
            TestCase.assertEquals(attrValue, resDoc.getString(attrName));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testOther() {
        String id1 = "_id_1";

        String attrName1 = "a1";
        String attrValue1 = "v1";
        String attrName2 = "a2";
        String attrValue2 = "v2";
        String attrName3 = "a3";
        int attrValue3 = 3;

        try {
            Document documentCommand = null;
            Document resDoc = null;
            List<Document> resDocs = null;

            // 如果集合不存在先创建集合
            documentCommand = new Document();
            documentCommand.put("listCollections", 1);
            Document listCollectionsDoc = new Document();
            listCollectionsDoc.put("name", ZKMongoTestConfig.colleationName_command);
            documentCommand.put("filter", listCollectionsDoc);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            resDocs = ZKMongoUtils.getListResult(resDoc);
            if (resDocs.size() > 0) {
                // 集合存在
                System.out.println(String.format("[^_^:20180530-1406-001] 集合 {} 存在！销毁集合！！！",
                        ZKMongoTestConfig.colleationName_command));
                // 销毁集合
                documentCommand = new Document();
                documentCommand.put("drop", ZKMongoTestConfig.colleationName_command);
                resDoc = mongoTemplate.executeCommand(documentCommand);
                TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            }
            else {
                // 集合不存在
                System.out.println(
                        String.format("[^_^:20180530-1406-001] 集合 {} 不存在！", ZKMongoTestConfig.colleationName_command));
            }

            // 创建集合和索引
            documentCommand = new Document();
            documentCommand.put("create", ZKMongoTestConfig.colleationName_command);
            documentCommand.put("autoIndexId", true);
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            documentCommand = new Document();
            // 创建索引
            documentCommand.put("createIndexes", ZKMongoTestConfig.colleationName_command);
            Document createIndexesDoc = new Document();
            Document keyDoc = new Document();
            keyDoc.put(ZKMongoTestConfig.attrExpireTimeName, 1);
            createIndexesDoc.put("key", keyDoc);
            createIndexesDoc.put("name", ZKMongoTestConfig.indexExpireTimeName);
            // 0 秒后过期
            createIndexesDoc.put("expireAfterSeconds", 0);
            documentCommand.put("indexes", Arrays.asList(createIndexesDoc));
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());

            /*** insert 插入, 文档不存在 ***/
            // 插入第一个
            documentCommand = new Document();
            documentCommand.put("insert", ZKMongoTestConfig.colleationName_command);
            Document insertDoc = new Document();
            insertDoc.put(ZKMongoUtils.autoIndexIdName, id1);
            insertDoc.put(ZKMongoTestConfig.attrExpireTimeName, new Date(System.currentTimeMillis() + 30000));
            insertDoc.put(attrName1, id1 + ":" + attrValue1);
            insertDoc.put(attrName2, id1 + ":" + attrValue2);
            insertDoc.put(attrName3, attrValue3);
            documentCommand.put("documents", Arrays.asList(insertDoc));
            resDoc = mongoTemplate.executeCommand(documentCommand);
            TestCase.assertEquals(1, resDoc.getDouble("ok").intValue());
            TestCase.assertEquals(1, resDoc.getInteger("n").intValue());

            /*** 保留指定属性 ***/

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void test() {
        try {
            // 插入一个

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
