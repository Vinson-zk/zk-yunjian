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
 * @Title: ZKMongoUtils.java 
 * @author Vinson 
 * @Package com.zk.mongo.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 5:55:51 PM 
 * @version V1.0   
*/
package com.zk.mongo.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.mongo.command.administration.ZKListCollections;
import com.zk.mongo.command.administration.ZKListIndexes;

/** 
* @ClassName: ZKMongoUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMongoUtils {

    public static final String autoIndexIdName = "_id";

    private static final String documentResultAttrNameCursor = "cursor";

    private static final String documentResultAttrNameFirstBatch = "firstBatch";

    /**
     * 判断集合是否存在
     * 
     * @param mongoTemplate
     * @param colleationName
     * @return true-存在；false-不存在
     */
    public static boolean isExist(MongoTemplate mongoTemplate, String colleationName) {
        ZKListCollections listCollectionsCommand = new ZKListCollections();
        Document filterDoc = new Document();
        filterDoc.put("name", colleationName);
        listCollectionsCommand.put("filter", filterDoc);
        Document resDoc = mongoTemplate.executeCommand(listCollectionsCommand);
        if (ZKMongoUtils.getListResult(resDoc).size() > 0) {
            // 集合存在
            return true;
        }
        return false;
    }

    /**
     * 判断集合中索引是否存在
     * 
     * @param mongoTemplate
     * @param colleationName
     * @param indexName
     * @return true-存在；false-不存在
     */
    public static boolean isExistIndexes(MongoTemplate mongoTemplate, String colleationName, String indexName) {
        ZKListIndexes listIndexesCommand = new ZKListIndexes(colleationName);
        Document resDoc = mongoTemplate.executeCommand(listIndexesCommand);
        List<Document> resDocs = ZKMongoUtils.getListResult(resDoc);
        for (Document doc : resDocs) {
            if (indexName.equals(doc.getString("name"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从 mongo 查询命令结果集中取 单一结果
     * 
     * @param doc
     * @return
     */
    public static Document getOneResult(Document doc) {
        Document cursor = doc.get(documentResultAttrNameCursor, Document.class);
        if (cursor != null) {
            @SuppressWarnings("unchecked")
            List<Document> firstBatch = (List<Document>) cursor.get(documentResultAttrNameFirstBatch, List.class);
            if (firstBatch != null) {
                if (firstBatch.size() > 1) {
                    throw new IllegalArgumentException("The mongo result is more; not single! ");
                }
                else {
                    return firstBatch.size() < 1 ? null : firstBatch.get(0);
                }
            }
        }
        return null;
    }

    /**
     * 从 mongo 查询命令结果集中取 List 结果
     * 
     * @param doc
     * @return
     */
    public static List<Document> getListResult(Document doc) {
        Document cursor = doc.get(documentResultAttrNameCursor, Document.class);
        if (cursor != null) {
            @SuppressWarnings("unchecked")
            List<Document> firstBatch = (List<Document>) cursor.get(documentResultAttrNameFirstBatch, List.class);
            if (firstBatch != null) {
                return firstBatch;
            }
        }
        return new ArrayList<>();
    }

//    /**
//     * 从 mongo 查询命令结果集中取 单一结果
//     * 
//     * @param cRslt
//     * @return
//     */
//    public static <V> V getOneResult(CommandResult cRslt) {
//        Map<?, ?> cursor = (Map<?, ?>) cRslt.get(documentResultAttrNameCursor);
//        if (cursor != null) {
//            @SuppressWarnings("unchecked")
//            List<V> firstBatch = (List<V>) cursor.get(documentResultAttrNameFirstBatch);
//            if (firstBatch != null) {
//                if (firstBatch.size() > 1) {
//                    throw new IllegalArgumentException("The mongo result is more; not single! ");
//                }
//                else {
//                    return firstBatch.size() < 1 ? null : firstBatch.get(0);
//                }
//            }
//        }
//        return null;
//    }

//    /**
//     * 从 mongo 查询命令结果集中取 List 结果
//     * 
//     * @param cRslt
//     * @return
//     */
//    public static <V> List<V> getListResult(CommandResult cRslt) {
//        Map<?, ?> cursor = (Map<?, ?>) cRslt.get(documentResultAttrNameCursor);
//        if (cursor != null) {
//            @SuppressWarnings("unchecked")
//            List<V> firstBatch = (List<V>) cursor.get(documentResultAttrNameFirstBatch);
//            if (firstBatch != null) {
//                return firstBatch;
//            }
//        }
//        return new ArrayList<>();
//    }

    /**
     * 从 mongo 查询命令结果集中取 KEY 属性的 byte[] 值；要求查询结果 单一
     * 
     * @param cRslt
     * @param key
     * @return
     */
    public static <K> byte[] getByteByKey(Document doc, K key) {
        Binary b = getObjectByKey(doc, key, Binary.class);
        if (b != null) {
            return b.getData();
        }
        return null;
    }

    /**
     * 从 mongo 查询命令结果集中取 KEY 属性的 Date 值；要求查询结果 单一
     * 
     * @param cRslt
     * @param key
     * @return
     */
    public static <K> Date getDateByKey(Document doc, K key) {
        return getObjectByKey(doc, key, Date.class);
    }

    /**
     * 从 mongo 查询命令结果集中取 KEY 属性的 Double 值；要求查询结果 单一
     * 
     * @param cRslt
     * @param key
     * @return
     */
    public static <K> Double getDoubleByKey(Document doc, K key) {
        return getObjectByKey(doc, key, Double.class);
    }

    /**
     * 从 mongo 查询命令结果集中取 KEY 属性的 Long 值；要求查询结果 单一
     * 
     * @param cRslt
     * @param key
     * @return
     */
    public static <K> Long getLongByKey(Document doc, K key) {
        return getObjectByKey(doc, key, Long.class);
    }

    /**
     * 从 mongo 查询命令结果集中取 KEY 属性的 String 值；要求查询结果 单一
     * 
     * @param cRslt
     * @param key
     * @return
     */
    public static <K> String getStringByKey(Document doc, K key) {
        return getObjectByKey(doc, key, String.class);
    }

    /**
     * 从 mongo 查询命令结果集中取 KEY 属性的 Integer 值；要求查询结果 单一
     * 
     * @param cRslt
     * @param key
     * @return
     */
    public static <K> Integer getIntegerByKey(Document doc, K key) {
        return getObjectByKey(doc, key, Integer.class);
    }

    /**
     * 从 mongo 查询命令结果集中取 KEY 属性的 Object 值；要求查询结果 单一
     * 
     * @param cRslt
     * @param key
     * @return
     */
    public static <K> Object getObjectByKey(Document doc, K key) {
        Document resDoc = getOneResult(doc);
        if (resDoc != null) {
            return resDoc.get(key);
        }
        return null;
    }

    public static <K, V> V getObjectByKey(Document doc, K key, Class<V> classz) {
        Document resDoc = getOneResult(doc);
        if (resDoc != null) {
            return resDoc.get(key, classz);
        }
        return null;
    }

}
