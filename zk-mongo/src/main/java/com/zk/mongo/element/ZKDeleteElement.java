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
 * @Title: ZKDeleteElement.java 
 * @author Vinson 
 * @Package com.zk.mongo.element 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 5:58:36 PM 
 * @version V1.0   
*/
package com.zk.mongo.element;

import org.bson.Document;

import com.zk.mongo.ZKBaseDocument;
import com.zk.mongo.operator.ZKQueryOpt;

/**
 * @ClassName: ZKDeleteElement
 * @Description: 删除命令的元素
 * @author Vinson
 * @version 1.0
 */
public class ZKDeleteElement extends ZKBaseDocument {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    /**
     * Each element of the deletes zkray contains the following fields: q
     * document The query that matches documents to delete. limit integer The
     * number of matching documents to delete. Specify either a 0 to delete all
     * matching documents or 1 to delete a single document. collation document
     * Optional.
     */

    private static final String elementKey_q = "q";

    private static final String elementKey_limit = "limit";

    private static final String elementKey_collation = "collation";

    public ZKDeleteElement(ZKQueryOpt queryOpt) {
        this.put(elementKey_q, queryOpt);
        this.put(elementKey_limit, 0);
    }

    public void setLimit(Integer limit) {
        if (limit == null) {
            this.remove(elementKey_limit);
        }
        else {
            this.put(elementKey_limit, limit);
        }
    }

    public void setCollation(Document collation) {
        if (collation == null) {
            this.remove(elementKey_collation);
        }
        else {
            this.put(elementKey_collation, collation);
        }
    }

}
