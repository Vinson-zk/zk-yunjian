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
 * @Title: ZKAdministrationTest.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.administration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:12:38 PM 
 * @version V1.0   
*/
package com.zk.mongo.command.administration;

import java.util.List;

import org.bson.Document;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.mongo.element.ZKIndexElement;
import com.zk.mongo.helper.ZKMongoTestConfig;
import com.zk.mongo.utils.ZKMongoUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKAdministrationTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKAdministrationTest {
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
     * 测试集合判断是否存在，创建、销毁
     */
    @Test
    public void testCommandZKCreateZKDropZKListCollections() {

        try {
            // 判断集合是否存在
            boolean isExist = false;
            ZKListCollections ZKListCollectionsCommand = new ZKListCollections();
            Document filterDoc = new Document();
            filterDoc.put("name", ZKMongoTestConfig.colleationName_Administration);
            ZKListCollectionsCommand.put("filter", filterDoc);
            Document resDoc = mongoTemplate.executeCommand(ZKListCollectionsCommand);
            if (ZKMongoUtils.getListResult(resDoc).size() > 0) {
                // 集合存在
                isExist = true;
            }
            if (isExist) {
                // 集合存在，销毁集合
                ZKDrop ZKDrop = new ZKDrop(ZKMongoTestConfig.colleationName_Administration);
                mongoTemplate.executeCommand(ZKDrop);
            }

            /*** 判断集合是否存在，此时集合不存在 ***/
            isExist = false;
            ZKListCollectionsCommand = new ZKListCollections();
            filterDoc = new Document();
            filterDoc.put("name", ZKMongoTestConfig.colleationName_Administration);
            ZKListCollectionsCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(ZKListCollectionsCommand);
            if (ZKMongoUtils.getListResult(resDoc).size() > 0) {
                // 集合存在
                isExist = true;
            }
            TestCase.assertFalse(isExist);

            /*** 创建集合 ***/
            ZKCreate ZKCreate = new ZKCreate(ZKMongoTestConfig.colleationName_Administration);
            mongoTemplate.executeCommand(ZKCreate);

            /*** 创建已存在的集合，异常 ***/
            ZKCreate = new ZKCreate(ZKMongoTestConfig.colleationName_Administration);
            try {
                mongoTemplate.executeCommand(ZKCreate);
                TestCase.assertTrue(false);
            }
            catch(UncategorizedMongoDbException e) {
                TestCase.assertTrue(true);
            }

            /*** 判断集合是否存在，此时集合存在 ***/
            isExist = false;
            ZKListCollectionsCommand = new ZKListCollections();
            filterDoc = new Document();
            filterDoc.put("name", ZKMongoTestConfig.colleationName_Administration);
            ZKListCollectionsCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(ZKListCollectionsCommand);
            if (ZKMongoUtils.getListResult(resDoc).size() > 0) {
                // 集合存在
                isExist = true;
            }
            TestCase.assertTrue(isExist);

            /*** 销毁集合 ***/
            ZKDrop ZKDrop = new ZKDrop(ZKMongoTestConfig.colleationName_Administration);
            mongoTemplate.executeCommand(ZKDrop);

            /*** 判断集合是否存在，此时集合不存在 ***/
            isExist = false;
            ZKListCollectionsCommand = new ZKListCollections();
            filterDoc = new Document();
            filterDoc.put("name", ZKMongoTestConfig.colleationName_Administration);
            ZKListCollectionsCommand.put("filter", filterDoc);
            resDoc = mongoTemplate.executeCommand(ZKListCollectionsCommand);
            if (ZKMongoUtils.getListResult(resDoc).size() > 0) {
                // 集合存在
                isExist = true;
            }
            TestCase.assertFalse(isExist);

            /*** 销毁不存在的集合，异常 ***/
            ZKDrop = new ZKDrop(ZKMongoTestConfig.colleationName_Administration);
            try {
                mongoTemplate.executeCommand(ZKDrop);
                TestCase.assertTrue(false);
            }
            catch(UncategorizedMongoDbException e) {
                TestCase.assertTrue(true);
            }

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testCommandZKCreateIndexsZKDropIndexListIndexs() {

        try {
            // 判断集合是否存在
            boolean isExist = false;
            ZKListCollections ZKListCollectionsCommand = new ZKListCollections();
            Document filterDoc = new Document();
            filterDoc.put("name", ZKMongoTestConfig.colleationName_Administration);
            ZKListCollectionsCommand.put("filter", filterDoc);
            Document resDoc = mongoTemplate.executeCommand(ZKListCollectionsCommand);
            if (ZKMongoUtils.getListResult(resDoc).size() > 0) {
                // 集合存在
                isExist = true;
            }
            if (isExist) {
                // 集合存在，销毁集合
                ZKDrop ZKDrop = new ZKDrop(ZKMongoTestConfig.colleationName_Administration);
                mongoTemplate.executeCommand(ZKDrop);
            }
            // 创建集合
            ZKCreate ZKCreate = new ZKCreate(ZKMongoTestConfig.colleationName_Administration);
            mongoTemplate.executeCommand(ZKCreate);

            /*** 判断索引是否存在；此时不存在 ***/
            isExist = false;
            ZKListIndexes ZKListIndexesCommand = new ZKListIndexes(ZKMongoTestConfig.colleationName_Administration);
            resDoc = mongoTemplate.executeCommand(ZKListIndexesCommand);
            List<Document> resDocs = ZKMongoUtils.getListResult(resDoc);
            for (Document doc : resDocs) {
                if (ZKMongoTestConfig.indexExpireTimeName.equals(doc.getString("name"))) {
                    isExist = true;
                    break;
                }
            }
            TestCase.assertFalse(isExist);

            /*** 创建索引，索引可以重复创建 ***/
            ZKIndexElement expireZKIndexElement = new ZKIndexElement(ZKMongoTestConfig.indexExpireTimeName,
                    ZKMongoTestConfig.attrExpireTimeName);
            expireZKIndexElement.setExpireAfterSeconds(0);
            ZKCreateIndexes ZKCreateIndexes = new ZKCreateIndexes(ZKMongoTestConfig.colleationName_Administration,
                    expireZKIndexElement);
            mongoTemplate.executeCommand(ZKCreateIndexes);

            /*** 判断索引是否存在；此时存在 ***/
            isExist = false;
            ZKListIndexesCommand = new ZKListIndexes(ZKMongoTestConfig.colleationName_Administration);
            resDoc = mongoTemplate.executeCommand(ZKListIndexesCommand);
            resDocs = ZKMongoUtils.getListResult(resDoc);
            for (Document doc : resDocs) {
                if (ZKMongoTestConfig.indexExpireTimeName.equals(doc.getString("name"))) {
                    isExist = true;
                    break;
                }
            }
            TestCase.assertTrue(isExist);

            /*** 销毁索引 ***/
            ZKDropIndexes ZKDropIndexes = new ZKDropIndexes(ZKMongoTestConfig.colleationName_Administration,
                    ZKMongoTestConfig.indexExpireTimeName);
            mongoTemplate.executeCommand(ZKDropIndexes);

            /*** 销毁不存在的索引，异常 ***/
            try {
                ZKDropIndexes = new ZKDropIndexes(ZKMongoTestConfig.colleationName_Administration,
                        ZKMongoTestConfig.indexExpireTimeName);
                mongoTemplate.executeCommand(ZKDropIndexes);
                TestCase.assertFalse(true);
            }
            catch(UncategorizedMongoDbException e) {
                TestCase.assertTrue(true);
            }

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
