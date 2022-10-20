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
 * @Title: ZKUpdateElement.java 
 * @author Vinson 
 * @Package com.zk.mongo.element 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 5:59:29 PM 
 * @version V1.0   
*/
package com.zk.mongo.element;

import java.util.ArrayList;
import java.util.Collection;

import org.bson.Document;

import com.zk.mongo.ZKBaseDocument;
import com.zk.mongo.operator.ZKQueryOpt;
import com.zk.mongo.operator.ZKUpdateOpt;

/**
 * @ClassName: ZKUpdateElement
 * @Description: 更新命令中的元素，一个更新命令中可以有多个更新元素
 * @author Vinson
 * @version 1.0
 */
public class ZKUpdateElement extends ZKBaseDocument {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    /**
     * Each element of the updates zkray contains the following fields:
     * 
     * q document The query that matches documents to update. Use the same query
     * selectors as used in the find() method. u document The modifications to
     * apply. For details, see Behavior. upsert boolean Optional. If true,
     * perform an insert if no documents match the query. If both upsert and
     * multi zke true and no documents match the query, the update operation
     * inserts only a single document. multi boolean Optional. If true, updates
     * all documents that meet the query criteria. If false, limit the update to
     * one document that meet the query criteria. Defaults to false. collation
     * document Optional. Specifies the collation to use for the operation.
     * Collation allows users to specify language-specific rules for string
     * comparison, such as rules for lettercase and accent marks. zkrayFilters
     * zkray Optional. An zkray of filter documents that determines which zkray
     * elements to modify for an update operation on an zkray field. In the
     * update document, use the $[<identifier>] filtered positional operator to
     * define an identifier, which you then reference in the zkray filter
     * documents. You cannot have an zkray filter document for an identifier if
     * the identifier is not included in the update document.
     */

    private static final String elementKey_q = "q";

    private static final String elementKey_u = "u";

    private static final String elementKey_upsert = "upsert";

    private static final String elementKey_multi = "multi";

    private static final String elementKey_collation = "collation";

    private static final String elementKey_arrayFilters = "zkrayFilters";

    public void setQuery(ZKQueryOpt queryOpt) {
        this.put(elementKey_q, queryOpt);
    }

    public void setUpdate(ZKUpdateOpt updateOpt) {
        this.put(elementKey_u, updateOpt);
    }

    public void setUpsert(Boolean upsert) {
        if (upsert == null) {
            this.remove(elementKey_upsert);
        }
        else {
            this.put(elementKey_upsert, upsert);
        }
    }

    public void setMulti(Boolean multi) {
        if (multi == null) {
            this.remove(elementKey_multi);
        }
        else {
            this.put(elementKey_multi, multi);
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

    @SuppressWarnings("unchecked")
    public void addArrayFilters(Collection<ZKQueryOpt> filters) {
        if (this.get(elementKey_arrayFilters) == null) {
            this.put(elementKey_arrayFilters, new ArrayList<>());
        }
        this.get(elementKey_arrayFilters, Collection.class).addAll(filters);
    }

}
