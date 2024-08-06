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
 * @Title: ZKUpdateOpt.java 
 * @author Vinson 
 * @Package com.zk.mongo.operator 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 5:58:06 PM 
 * @version V1.0   
*/
package com.zk.mongo.operator;

import org.bson.Document;

import com.zk.mongo.ZKBaseDocument;

/**
 * @ClassName: ZKUpdateOpt
 * @Description: 更新元素 UpdateElement 中的修改操作
 * @author Vinson
 * @version 1.0
 */
public class ZKUpdateOpt extends ZKBaseDocument {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    /**
     * Field Update Operators For details on specific operator, including syntax
     * and examples, click on the specific operator to go to its reference page.
     * 
     * Name Description $currentDate Sets the value of a field to current date,
     * either as a Date or a Timestamp. $inc Increments the value of the field
     * by the specified amount. $min Only updates the field if the specified
     * value is less than the existing field value. $max Only updates the field
     * if the specified value is greater than the existing field value. $mul
     * Multiplies the value of the field by the specified amount. $rename
     * Renames a field. $set Sets the value of a field in a document.
     * $setOnInsert Sets the value of a field if an update results in an insert
     * of a document. Has no effect on update operations that modify existing
     * documents. $unset Removes the specified field from a document.
     * 
     */

    private static final String optKey_currentDate = "$currentDate";

    private static final String optKey_inc = "$inc";

    private static final String optKey_min = "$min";

    private static final String optKey_max = "$max";

    private static final String optKey_mul = "$mul";

    private static final String optKey_rename = "$rename";

    private static final String optKey_set = "$set";

    private static final String optKey_setOnInsert = "$setOnInsert";

    private static final String optKey_unset = "$unset";

    private <V> void addAttribute(String optKey, String attributeName, V value) {
        Document doc = this.get(optKey, Document.class);
        if (doc == null) {
            doc = new Document();
            doc.put(attributeName, value);
        }
        doc.put(attributeName, value);
        this.put(optKey, doc);
    }

    public <V> void currentDate(String attributeName, V value) {
        this.addAttribute(optKey_currentDate, attributeName, value);
    }

    public void inc(String attributeName, Integer value) {
        this.addAttribute(optKey_inc, attributeName, value);
    }

    public <V> void min(String attributeName, V value) {
        this.addAttribute(optKey_min, attributeName, value);
    }

    public <V> void max(String attributeName, V value) {
        this.addAttribute(optKey_max, attributeName, value);
    }

    public void mul(String attributeName, Integer value) {
        this.addAttribute(optKey_mul, attributeName, value);
    }

    public void rename(String attributeName, String newName) {
        this.addAttribute(optKey_rename, attributeName, newName);
    }

    public <V> void set(String attributeName, V value) {
        this.addAttribute(optKey_set, attributeName, value);
    }

    public <V> void setOnInsert(String attributeName, V value) {
        this.addAttribute(optKey_setOnInsert, attributeName, value);
    }

    public void unset(String... attributeNames) {
        for (String an : attributeNames) {
            this.addAttribute(optKey_unset, an, "");
        }
    }

}
