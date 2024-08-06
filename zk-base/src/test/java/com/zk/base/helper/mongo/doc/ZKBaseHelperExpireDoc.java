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
 * @Title: ZKBaseHelperExpireDoc.java
 * @author Vinson
 * @Package com.zk.base.helper.mongo.doc
 * @Description: TODO(simple description this file what to do.)
 * @date Dec 19, 2019 3:33:28 PM
 * @version V1.0
*/
package com.zk.base.helper.mongo.doc;

import org.springframework.data.mongodb.core.mapping.Document;

import com.zk.base.mongo.doc.ZKBaseMongoExpireDoc;

/**
* @ClassName: ZKBaseHelperExpireDoc
* @Description: TODO(simple description this class what to do.)
* @author Vinson
* @version 1.0
*/
@Document(value = "test_ZKBaseHelperExpireDoc")
public class ZKBaseHelperExpireDoc extends ZKBaseMongoExpireDoc<String> {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    public ZKBaseHelperExpireDoc() {
        super();
    }

    public ZKBaseHelperExpireDoc(int validTime) {
        super(validTime);
    }

}
