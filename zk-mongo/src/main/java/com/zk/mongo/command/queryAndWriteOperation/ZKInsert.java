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
 * @Title: ZKInsert.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.queryAndWriteOperation 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:04:03 PM 
 * @version V1.0   
*/
package com.zk.mongo.command.queryAndWriteOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.bson.Document;

import com.zk.mongo.ZKBaseDocument;

/** 
* @ClassName: ZKInsert 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKInsert extends ZKBaseDocument {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    /**
     * The insert command inserts one or more documents and returns a document
     * containing the status of all inserts. The insert methods provided by the
     * MongoDB drivers use this command internally.
     * 
     * insert string The name of the target collection. documents zkray An zkray
     * of one or more documents to insert into the named collection. ordered
     * boolean Optional. If true, then when an insert of a document fails,
     * return without inserting any remaining documents listed in the inserts
     * zkray. If false, then when an insert of a document fails, continue to
     * insert the remaining documents. Defaults to true. writeConcern document
     * Optional. A document that expresses the write concern of the insert
     * command. Omit to use the default write concern. bypassDocumentValidation
     * boolean Optional. Enables insert to bypass document validation during the
     * operation. This lets you insert documents that do not meet the validation
     * requirements.
     * 
     * Returns: A document that contains the status of the operation. See Output
     * for details.
     */
    private static final String commandOpt_insert = "insert";

    private static final String commandOpt_documents = "documents";

    private static final String commandOpt_ordered = "ordered";

    private static final String commandOpt_writeConcern = "writeConcern";

    private static final String commandOpt_bypassDocumentValidation = "bypassDocumentValidation";

    /**
     * @param collectionName
     */
    public ZKInsert(String collectionName) {
        this.put(commandOpt_insert, collectionName);
        this.put(commandOpt_documents, new ArrayList<Document>());
    }

    public void setCollectionName(String collectionName) {
        this.put(commandOpt_insert, collectionName);
    }

    @SuppressWarnings("unchecked")
    public void addDoc(Document... docs) {
        this.get(commandOpt_documents, Collection.class).addAll(Arrays.asList(docs));
    }

    public void setOrdered(Boolean ordered) {
        if (ordered == null) {
            this.remove(commandOpt_ordered);
        }
        else {
            this.put(commandOpt_ordered, ordered);
        }
    }

    public void setWriteConcern(Document writeConcern) {
        if (writeConcern == null) {
            this.remove(commandOpt_writeConcern);
        }
        else {
            this.put(commandOpt_writeConcern, writeConcern);
        }
    }

    public void setBypassDocumentValidation(Boolean bypassDocumentValidation) {
        if (bypassDocumentValidation == null) {
            this.remove(commandOpt_bypassDocumentValidation);
        }
        else {
            this.put(commandOpt_bypassDocumentValidation, bypassDocumentValidation);
        }
    }

}
