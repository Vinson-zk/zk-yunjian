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
 * @Title: ZKBaseHelperDoc.java
 * @author Vinson
 * @Package com.zk.base.helper.mongo.doc
 * @Description: TODO(simple description this file what to do.)
 * @date Dec 19, 2019 3:32:45 PM
 * @version V1.0
*/
package com.zk.base.helper.mongo.doc;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.zk.base.mongo.doc.ZKBaseMongoDoc;

/**
* @ClassName: ZKBaseHelperDoc
* @Description: TODO(simple description this class what to do.)
* @author Vinson
* @version 1.0
*/
@Document(value = "test_ZKBaseHelperDoc")
public class ZKBaseHelperDoc extends ZKBaseMongoDoc<String> {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    private String name;

    private Integer value;

    /**
     * @ org.springframework.data.annotation.Transient
     *
     *   存储时，忽略该属性
     */
    @Transient
    private String ignoreValue;

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * @return ignoreValue
     */
    public String getIgnoreValue() {
        return ignoreValue;
    }

    /**
     * @param ignoreValue
     *            the ignoreValue to set
     */
    public void setIgnoreValue(String ignoreValue) {
        this.ignoreValue = ignoreValue;
    }

}
