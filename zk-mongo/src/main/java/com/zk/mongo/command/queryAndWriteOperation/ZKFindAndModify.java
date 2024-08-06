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
 * @Title: ZKFindAndModify.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.queryAndWriteOperation 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:03:36 PM 
 * @version V1.0   
*/
package com.zk.mongo.command.queryAndWriteOperation;

import java.util.Set;

import org.bson.Document;

import com.zk.mongo.ZKBaseDocument;
import com.zk.mongo.operator.ZKQueryOpt;
import com.zk.mongo.operator.ZKUpdateOpt;

/**
 * @ClassName: ZKFindAndModify
 * @Description: 只实现了部分 KEY；其他 key 可用原生方法添加
 * @author Vinson
 * @version 1.0
 */
public class ZKFindAndModify extends ZKBaseDocument {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * The findAndModify command modifies and returns a single document. By
     * default, the returned document does not include the modifications made on
     * the update. To return the document with the modifications made on the
     * update, use the new option.
     * 
     * The findAndModify command takes the following fields:
     * 
     * findAndModify query document Optional. The selection criteria for the
     * modification. The query field employs the same query selectors as used in
     * the db.collection.find() method. Although the query may match multiple
     * documents, findAndModify will only select one document to modify. remove
     * boolean Must specify either the remove or the update field. Removes the
     * document specified in the query field. Set this to true to remove the
     * selected document . The default is false. update document Must specify
     * either the remove or the update field. Performs an update of the selected
     * document. The update field employs the same update operators or field:
     * value specifications to modify the selected document. new boolean
     * Optional. When true, returns the modified document rather than the
     * original. The findAndModify method ignores the new option for remove
     * operations. The default is false. upsert boolean Optional. Used in
     * conjuction with the update field. fields document Optional. A subset of
     * fields to return. The fields document specifies an inclusion of a field
     * with 1, as in: fields: { <field1>: 1, <field2>: 1, ... }. See projection.
     */

    private static final String commandOpt_findAndModify = "findAndModify";

    private static final String commandOpt_query = "query";

    private static final String commandOpt_remove = "remove";

    private static final String commandOpt_update = "update";

    private static final String commandOpt_new = "new";

    private static final String commandOpt_upsert = "upsert";

    private static final String commandOpt_fields = "fields";

    public ZKFindAndModify(String collectionName) {
        this.put(commandOpt_findAndModify, collectionName);
    }

    public void setQuery(ZKQueryOpt query) {
        if (query == null) {
            this.remove(commandOpt_query);
        }
        else {
            this.put(commandOpt_query, query);
        }
    }

    public void setRemove(Boolean remove) {
        if (remove == null) {
            this.remove(commandOpt_remove);
        }
        else {
            this.put(commandOpt_remove, remove);
        }
    }

    public void setUpdate(ZKUpdateOpt updateOpt) {
        if (updateOpt == null) {
            this.remove(commandOpt_update);
        }
        else {
            this.put(commandOpt_update, updateOpt);
        }
    }

    public void setReturnNew(Boolean returnNew) {
        if (returnNew == null) {
            this.remove(commandOpt_new);
        }
        else {
            this.put(commandOpt_new, returnNew);
        }
    }

    public void setUpsert(Boolean upsert) {
        if (upsert == null) {
            this.remove(commandOpt_upsert);
        }
        else {
            this.put(commandOpt_upsert, upsert);
        }
    }

    public void setFields(Set<String> includeKeys, Set<String> excludeKeys) {
        if (includeKeys == null && excludeKeys == null) {
            this.remove(commandOpt_fields);
        }
        else {
            Document projection = this.get(commandOpt_fields, Document.class);
            if (projection == null) {
                projection = new Document();
            }

            for (String key : includeKeys) {
                projection.put(key, 1);
            }

            for (String key : excludeKeys) {
                projection.put(key, 0);
            }
        }
    }

}
