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
 * @Title: ZKMongoTemplateTest.java 
 * @author Vinson 
 * @Package com.zk.mongo 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:09:27 PM 
 * @version V1.0   
*/
package com.zk.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.alibaba.fastjson2.JSON;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.zk.core.utils.ZKObjectUtils;
import com.zk.mongo.helper.ZKMongoTestConfig;
import com.zk.mongo.helper.ZKObjectToSave;
import com.zk.mongo.helper.ZKTestObject;
import com.zk.mongo.utils.ZKMongoUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMongoTemplateTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMongoTemplateTest {

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

    public static final String collectionAttributeName_id = "key";

    /**
     * 测试查询
     */
    @Test
    public void testFind() {
        String idPrefix = "test_query_";
        int count = 39;
        List<String> ids = new ArrayList<>();
        String id = null;
        try {
            Query query = null;

            ZKObjectToSave ots = null;
            for (int i = 1; i <= count; ++i) {
                ots = new ZKObjectToSave();
                ids.add(idPrefix + i);
                ots.setKey(idPrefix + i);
                ots.setValue("测试 查询文档 mongoTemplate " + i);
                ots.setInt(i);
                mongoTemplate.save(ots);
            }
            query = Query.query(Criteria.where(collectionAttributeName_id).in(ids));
            List<ZKObjectToSave> otsList = mongoTemplate.find(query, ZKObjectToSave.class);
            TestCase.assertEquals(count, otsList.size());

            // 查询一个
            id = idPrefix + 101;
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            ots = null;
            ots = mongoTemplate.findOne(query, ZKObjectToSave.class);
            TestCase.assertNull(ots);

            id = idPrefix + 10;
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            ots = null;
            ots = mongoTemplate.findOne(query, ZKObjectToSave.class);
            TestCase.assertNotNull(ots);

            // 模糊查询,未完成

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            Query query = Query.query(Criteria.where(collectionAttributeName_id).in(ids));
            mongoTemplate.remove(query, ZKObjectToSave.class);
        }
    }

    /**
     * 测试删除一个文档，remove
     */
    @Test
    public void testRremove() {
        String id = "test_remove_01";
        try {
            Query query = null;
            ZKObjectToSave ots = new ZKObjectToSave();
            ots.setKey(id);
            ots.setValue("测试 删除文档 mongoTemplate");
            mongoTemplate.save(ots);

            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            ots = null;
            ots = mongoTemplate.findOne(query, ZKObjectToSave.class);
            TestCase.assertNotNull(ots);

            DeleteResult dRes = mongoTemplate.remove(ots);
            if (dRes.wasAcknowledged()) {
                System.out.println("[^_^:20180519-1809-001] 删除成功");
            }
            else {
                System.out.println("[^_^:20180519-1809-002] 没有删除数据");
                TestCase.assertTrue(false);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 插入
     */
    @Test
    public void testInsert() {
        String id = "test_insert_01";
        String value = "测试 mongoTemplate 插入";
        try {
            Query query = null;

            ZKObjectToSave ots = new ZKObjectToSave();
            ots.setKey(id);
            ots.setValue(value);

            DeleteResult dRes = mongoTemplate.remove(ots);
            if (dRes.wasAcknowledged()) {
                System.out.println("[^_^:20180519-1755-001] 删除成功");
            }
            else {
                System.out.println("[^_^:20180519-1755-002] 没有删除数据");
            }

            // 主键不冲突插入 insert
            mongoTemplate.insert(ots);
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            ots = null;
            ots = mongoTemplate.findOne(query, ZKObjectToSave.class);
            TestCase.assertNotNull(ots);
            TestCase.assertEquals(value, ots.getValue());

            // 主键冲突插入 insert，会报错
            try {
                mongoTemplate.insert(ots);
                TestCase.assertTrue(false);
            }
            catch(Exception e) {

                if (e instanceof DuplicateKeyException) {
                    DuplicateKeyException dke = (DuplicateKeyException) e;
                    TestCase.assertEquals("E11000 d", dke.getMessage().substring(0, 8));
                }
                else {
                    e.printStackTrace();
                    TestCase.assertTrue(false);
                }
            }

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
//      }finally {
//          ZKObjectToSave ots = new ZKObjectToSave();
//          ots.setKey(id);
//          mongoTemplate.remove(ots);
        }
    }

    /**
     * 保存
     */
    @Test
    public void testSave() {
        String id = "test_save_01";
        String value = "测试 mongoTemplate 保存";
        try {
            Query query = null;
            Update update = null;
            UpdateResult ur = null;

            String jsonCommand;
            Document doc;
            String t;

            ZKObjectToSave ots = new ZKObjectToSave();
            ots.setKey(id);
            ots.setValue(value);

            DeleteResult dRes = mongoTemplate.remove(ots);
            if (dRes.wasAcknowledged()) {
                System.out.println("[^_^:20180519-1937-001] 删除成功");
            }
            else {
                System.out.println("[^_^:20180519-1937-002] 没有删除数据");
            }

            // 主键不冲突保存 save
            mongoTemplate.save(ots);
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            ots = null;
            ots = mongoTemplate.findOne(query, ZKObjectToSave.class);
            TestCase.assertNotNull(ots);
            TestCase.assertEquals(value, ots.getValue());

            // 主键冲突保存 save，正常保存并修改正文档
            value = value + "主键冲突保存";
            ots.setValue(value);
            mongoTemplate.save(ots);
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            ots = null;
            ots = mongoTemplate.findOne(query, ZKObjectToSave.class);
            TestCase.assertNotNull(ots);
            TestCase.assertEquals(value, ots.getValue());

            /*** 测试实体外属性在 mongoTemplate.save 是否会被覆盖,会被覆盖 ***/
            String attributeKeyName = "testAttr";
            String attributeKeyValue = "testAttr 实体外属性";
            jsonCommand = String.format("{find:'%s', filter:{%s:'%s'}, projection:{%s:1}}",
                    ZKObjectToSave.collectionName, "_id", id, attributeKeyName);
            System.out.println("[^_^:20180529-0655-001] jsonCommand:" + jsonCommand);
            // 用 mongoTemplate.upsert(query, update,
            // ZKObjectToSave.collectionName); 设置实体外属性
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            update = new Update();
            update.set(attributeKeyName, attributeKeyValue);
            ur = mongoTemplate.upsert(query, update, ZKObjectToSave.class);
            TestCase.assertTrue(ur.getMatchedCount() > 0);
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            // 查询实体外属性
            doc = mongoTemplate.executeCommand(jsonCommand);
            System.out.println("[^_^:20180529-0655-002] doc:" + JSON.toJSONString(doc));
            doc = ZKMongoUtils.getOneResult(doc);
            t = (String) doc.get(attributeKeyName);
            TestCase.assertEquals(attributeKeyValue, t);
            // mongoTemplate.save(ots);
            value = "测试实体外属性在 mongoTemplate.save 是否会被覆盖";
            ots.setValue(value);
            mongoTemplate.save(ots);
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            ots = null;
            ots = mongoTemplate.findOne(query, ZKObjectToSave.class);
            TestCase.assertNotNull(ots);
            TestCase.assertEquals(value, ots.getValue());
            // 查询实体外属性
            doc = mongoTemplate.executeCommand(jsonCommand);
            System.out.println("[^_^:20180529-0655-003] doc:" + JSON.toJSONString(doc));
            doc = ZKMongoUtils.getOneResult(doc);
            t = (String) doc.get(attributeKeyName);
            TestCase.assertEquals(null, t);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
//      }finally {
//          ZKObjectToSave ots = new ZKObjectToSave();
//          ots.setKey(id);
//          mongoTemplate.remove(ots);
        }
    }

    /**
     * 修改属性，文档存在与不存在两种情况；此方法测试文档不存在
     * 
     * mongoTemplate.upsert 文档存在，修改指定属性，不存不新增文档，[del-新增文档，修改指定属性]
     * 
     * mongoTemplate.findAndModify 文档不存在时，不新增文档
     * 
     * update.setOnInsert 文档新增时生效， update.set 属性存在，修改，不存在新增，
     * 
     */
    @Test
    public void testDocUpdateAttribute() {
        String id = "test_doc_update_attribute_01";
        String value = "测试 mongoTemplate 修改属性，文档存在与不存在两种情况 ";

        String attributeKeyName = "testAttr";
        String attributeKeyValue = "";
        try {
            Query query = null;
            Update update = null;
            FindAndModifyOptions options = null;
            DeleteResult dRes = null;
            ZKObjectToSave tempOTS = null;
            UpdateResult ur = null;

            ZKObjectToSave ots = new ZKObjectToSave();
            ots.setKey(id);
            ots.setValue(value);

            // 此文档不存在，新增并修改属性，方法一
            dRes = mongoTemplate.remove(ots);
            if (dRes.wasAcknowledged()) {
                System.out.println("[^_^:20180520-0827-001] 删除成功");
            }
            else {
                System.out.println("[^_^:20180520-0827-002] 没有删除数据");
            }
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            update = new Update();
            attributeKeyValue = "mongoTemplate.upsert";
            update.set(attributeKeyName, attributeKeyValue);
            ur = mongoTemplate.upsert(query, update, ZKObjectToSave.class);
            System.out.println(ur);
            TestCase.assertTrue(ur.getModifiedCount() == 0);

            // 此文档不存在，新增,并修改属性，方法二
            dRes = mongoTemplate.remove(ots);
            if (dRes.wasAcknowledged()) {
                System.out.println("[^_^:20180520-0827-003] 删除成功 --- ");
            }
            else {
                TestCase.assertTrue(false);
            }
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            update = new Update();
            attributeKeyValue = "mongoTemplate.findAndModify.upsert.true";
            update.set(attributeKeyName, attributeKeyValue);
            options = FindAndModifyOptions.options();
            options.upsert(true);
            options.returnNew(true); // 需要设置返回新值，不然新增文档后，返回的还是 null
            tempOTS = mongoTemplate.findAndModify(query, update, options, ZKObjectToSave.class);
            TestCase.assertNotNull(tempOTS);
            System.out.println("[^_^:20180520-0827-003] " + JSON.toJSONString(tempOTS));

            // 此文档不存在，不新增文档
            dRes = mongoTemplate.remove(ots);
            if (dRes.wasAcknowledged()) {
                System.out.println("[^_^:20180520-0827-004] 删除成功");
            }
            else {
                TestCase.assertTrue(false);
            }
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            update = new Update();
            attributeKeyValue = "mongoTemplate.findAndModify.upsert.false";
            update.set(attributeKeyName, attributeKeyValue);
            options = FindAndModifyOptions.options();
            options.upsert(false);
            options.returnNew(true);
            tempOTS = mongoTemplate.findAndModify(query, update, options, ZKObjectToSave.class);
            TestCase.assertNull(tempOTS);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 文档存在，属性；属性不存在，会自动新增属性 递增
     */
    @Test
    public void testFindAndModifyOptions() {
        String id = "test_update_attribute_01";
        String value = "测试 mongoTemplate 修改属性，属性存在与不存在 ";

        String attributeKeyName = "testAttrValue";
        Object attributeKeyValue = 2;
        try {
            // 插入一个 doc
            Query query = null;
            Update update = null;
            FindAndModifyOptions options = null;
            DeleteResult dRes = null;
            ZKObjectToSave tempOTS = null;
            ZKObjectToSave ur = null;
            Map<String, ?> resDoc;
            String jsonCommand;
            Document doc = null;
            String t = null;

            ZKObjectToSave ots = new ZKObjectToSave();
            ots.setKey(id);
            ots.setValue(value);
            dRes = mongoTemplate.remove(ots);
            if (dRes.wasAcknowledged()) {
                System.out.println("[^_^:20180520-0910-001] 删除成功");
            }
            else {
                System.out.println("[^_^:20180520-0910-002] 没有删除数据");
            }

            // 文档不存在；不新增
            attributeKeyName = "testAttrValue-1";
            attributeKeyValue = "文档不存在；不新增";
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            update = new Update();
            update.set(attributeKeyName, attributeKeyValue);
            options = FindAndModifyOptions.options();
            options.upsert(false);
            ur = mongoTemplate.findAndModify(query, update, options, ZKObjectToSave.class);
            System.out.println(ur);
            TestCase.assertNull(ur);

            // 文档不存在；新增 不生效了
            attributeKeyName = "testAttrValue-1";
            attributeKeyValue = "文档不存在；新增";
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            update = new Update();
            update.set(attributeKeyName, attributeKeyValue);
            options = FindAndModifyOptions.options();
            options.upsert(true);
            options.returnNew(true); // 需要设置返回新值，不然新增文档后，返回的还是 null
            ur = mongoTemplate.findAndModify(query, update, options, ZKObjectToSave.class);
            System.out.println(ur);
            TestCase.assertNotNull(ur);

            // 新增文档
            mongoTemplate.save(ots);
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            tempOTS = mongoTemplate.findOne(query, ZKObjectToSave.class);
            TestCase.assertNotNull(tempOTS);

            attributeKeyName = "testAttrValue";
            // 文档存在；属性不存在，新增
            attributeKeyValue = "属性不存在，新增";
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            update = new Update();
            update.set(attributeKeyName, attributeKeyValue);
            options = FindAndModifyOptions.options();
            options.upsert(true);
            ur = mongoTemplate.findAndModify(query, update, options, ZKObjectToSave.class);
            TestCase.assertNotNull(ur);
            // 查询对象 cResult
            jsonCommand = String.format("{find:'%s', filter:{%s:'%s'}, projection:{%s:1}}",
                    ZKObjectToSave.collectionName, "_id", id, attributeKeyName);
            doc = mongoTemplate.executeCommand(jsonCommand);
            resDoc = ZKMongoUtils.getOneResult(doc);
            t = (String) resDoc.get(attributeKeyName);
            TestCase.assertEquals(attributeKeyValue, t);

            // 文档存在；属性存在，修改
            attributeKeyValue = "属性存在，修改";
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            update = new Update();
            update.set(attributeKeyName, attributeKeyValue);
            options = FindAndModifyOptions.options();
            options.upsert(true);
            ur = mongoTemplate.findAndModify(query, update, options, ZKObjectToSave.class);
            TestCase.assertNotNull(ur);

            // 属性数组操作 ,未完成

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 属性查找
     */
    @Test
    public void testFindAttribute() {

        String id = "test_find_attribute_01";
        String value = "测试 mongoTemplate 查询属性 ";

        String attributeKeyName = "testAttrValue";
        String attributeKeyValue = "attributeKeyValue";

        try {
            // 插入一个
            Query query = null;
            Update update = null;
            FindAndModifyOptions options = null;
            DeleteResult dRes = null;
            ZKObjectToSave tempOTS = null;
            UpdateResult ur = null;
            Document doc = null;
            String t = null;

            ZKObjectToSave ots = new ZKObjectToSave();
            ots.setKey(id);
            ots.setValue(value);
            dRes = mongoTemplate.remove(ots);
            if (dRes.wasAcknowledged()) {
                System.out.println("[^_^:20180520-1010-001] 删除成功");
            }
            else {
                System.out.println("[^_^:20180520-1010-002] 没有删除数据");
            }
            mongoTemplate.save(ots);
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            tempOTS = mongoTemplate.findOne(query, ZKObjectToSave.class);
            TestCase.assertNotNull(tempOTS);

            // 实体不包含的属性，新增
            attributeKeyValue = "实体不包含的属性，新增";
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            update = new Update();
            update.set(attributeKeyName, attributeKeyValue);
            options = FindAndModifyOptions.options();
            options.upsert(true);
            ur = mongoTemplate.upsert(query, update, ZKObjectToSave.class);
            TestCase.assertTrue(ur.getMatchedCount() > 0);

            // 查询对象 cResult
            String jsonCommand = String.format("{find:'%s', filter:{%s:'%s'}, projection:{%s:1}}",
                    ZKObjectToSave.collectionName, "_id", id, attributeKeyName);
            System.out.println("[^_^:20180520-1011-001] jsonCommand:" + jsonCommand);
            doc = mongoTemplate.executeCommand(jsonCommand);
            System.out.println("[^_^:20180520-1651-002] doc:" + JSON.toJSONString(doc));
            Map<String, ?> resDoc = ZKMongoUtils.getOneResult(doc);
            System.out.println("[^_^:20180520-1651-003] doc:" + JSON.toJSONString(resDoc));
            t = (String) resDoc.get(attributeKeyName);
            TestCase.assertEquals(attributeKeyValue, t);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 各种数据类型属性
     */
    @Test
    public void testAttributeType() {
        String id = "test_attribute_type_01";
        String value = "测试 mongoTemplate 不几类型类型属性存取 ";

        String attrStringKeyName = "attrStringKeyName";
        String attrIntKeyName = "attrIntKeyName";
        String attrDoubleKeyName = "attrFloatKeyName";
        String attrDateKeyName = "attrDateKeyName";
        String attrBytesKeyName = "attrBytesKeyName";

        String attrObjectKeyName = "attrObjectKeyName";

//      String attrIntsKeyName = "attrIntsKeyName";
//      String attrObjectsKeyName = "attrObjectsKeyName";
//      String attrListKeyName = "attrListKeyName";
//      String attrMapKeyName = "attrMapKeyName";

        String attrStringKeyValue = "String 属性";
        int attrIntKeyValue = 6;
        double attrDoubleKeyValue = 6.6f;
        Date attrDateKeyValue = new Date();
        byte[] attrBytesKeyValue = "字节数组属性".getBytes();

        ZKTestObject attrObjectKeyValue = new ZKTestObject();

        try {
            // 插入一个 doc
            Query query = null;
            Update update = null;
            FindAndModifyOptions options = null;
            DeleteResult dRes = null;
            ZKObjectToSave tempOTS = null;
            UpdateResult ur = null;
//          Map<String, ?> resDoc;
            String jsonCommand;
            Document doc = null;

            ZKObjectToSave ots = new ZKObjectToSave();
            ots.setKey(id);
            ots.setValue(value);
            dRes = mongoTemplate.remove(ots);
            if (dRes.wasAcknowledged()) {
                System.out.println("[^_^:20180520-0910-001] 删除成功");
            }
            else {
                System.out.println("[^_^:20180520-0910-002] 没有删除数据");
            }
            mongoTemplate.save(ots);
            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            tempOTS = mongoTemplate.findOne(query, ZKObjectToSave.class);
            TestCase.assertNotNull(tempOTS);

            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            update = new Update();
            options = FindAndModifyOptions.options();
            options.upsert(true);
            ur = mongoTemplate.upsert(query, update, ZKObjectToSave.class);
            TestCase.assertTrue(ur.getMatchedCount() > 0);

            jsonCommand = String.format("{find:'%s', filter:{%s:'%s'}}", ZKObjectToSave.collectionName, "_id", id);
            doc = mongoTemplate.executeCommand(jsonCommand);
            // String
            String resStr = ZKMongoUtils.getStringByKey(doc, attrStringKeyName);
            TestCase.assertNull(resStr);
            // Integer
            Integer resInt = ZKMongoUtils.getIntegerByKey(doc, attrIntKeyName);
            TestCase.assertNull(resInt);
            // Double
            Double resDouble = ZKMongoUtils.getDoubleByKey(doc, attrDoubleKeyName);
            TestCase.assertNull(resDouble);
            // Date
            Date resDate = ZKMongoUtils.getDateByKey(doc, attrDateKeyName);
            TestCase.assertNull(resDate);
            // byte[]
            byte[] resBytes = ZKMongoUtils.getByteByKey(doc, attrBytesKeyName);
            TestCase.assertNull(resBytes);
            // ZKTestObject
            Object obj = ZKMongoUtils.getObjectByKey(doc, attrObjectKeyName);
            TestCase.assertNull(obj);

            query = Query.query(Criteria.where(collectionAttributeName_id).is(id));
            update = new Update();
            update.set(attrStringKeyName, attrStringKeyValue);
            update.set(attrIntKeyName, attrIntKeyValue);
            update.set(attrDoubleKeyName, attrDoubleKeyValue);
            update.set(attrDateKeyName, attrDateKeyValue);
            update.set(attrBytesKeyName, attrBytesKeyValue);
            update.set(attrObjectKeyName, ZKObjectUtils.serialize(attrObjectKeyValue));
            options = FindAndModifyOptions.options();
            options.upsert(true);
            ur = mongoTemplate.upsert(query, update, ZKObjectToSave.class);
            TestCase.assertTrue(ur.getMatchedCount() > 0);

            // 查询对象 Document
            jsonCommand = String.format("{find:'%s', filter:{%s:'%s'}}", ZKObjectToSave.collectionName, "_id", id);
            doc = mongoTemplate.executeCommand(jsonCommand);

            // String
            resStr = ZKMongoUtils.getStringByKey(doc, attrStringKeyName);
            TestCase.assertEquals(attrStringKeyValue, resStr);
            // Integer
            resInt = ZKMongoUtils.getIntegerByKey(doc, attrIntKeyName);
            TestCase.assertEquals(attrIntKeyValue, resInt.intValue());
            // Double
            resDouble = ZKMongoUtils.getDoubleByKey(doc, attrDoubleKeyName);
            TestCase.assertTrue(attrDoubleKeyValue - resDouble.doubleValue() < 0.000001);
            TestCase.assertTrue(attrDoubleKeyValue - resDouble.doubleValue() > -0.000001);
            // Date
            resDate = ZKMongoUtils.getDateByKey(doc, attrDateKeyName);
            TestCase.assertEquals(attrDateKeyValue.getTime(), resDate.getTime());
            // byte[]
            resBytes = ZKMongoUtils.getByteByKey(doc, attrBytesKeyName);
            TestCase.assertEquals(new String(attrBytesKeyValue), new String(resBytes));
            // ZKTestObject
            resBytes = ZKMongoUtils.getByteByKey(doc, attrObjectKeyName);
            ZKTestObject to = (ZKTestObject) ZKObjectUtils.unserialize(resBytes);
            TestCase.assertEquals(9, to.getIntValue());

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
