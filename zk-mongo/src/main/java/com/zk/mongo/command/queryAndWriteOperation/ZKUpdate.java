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
 * @Title: ZKUpdate.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.queryAndWriteOperation 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:04:23 PM 
 * @version V1.0   
*/
package com.zk.mongo.command.queryAndWriteOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.bson.Document;

import com.zk.mongo.ZKBaseDocument;
import com.zk.mongo.element.ZKUpdateElement;

/** 
* @ClassName: ZKUpdate 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKUpdate extends ZKBaseDocument {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    /**
     * The update command modifies documents in a collection. A single update
     * command can contain multiple update statements. The update methods
     * provided by the MongoDB drivers use this command internally.
     *
     * The update command has the following syntax: { update: <collection>,
     * updates: [ { q: <query>, u: <update>, upsert: <boolean>, multi:
     * <boolean>, collation: <document>, zkrayFilters: <array> }, { q: <query>,
     * u: <update>, upsert: <boolean>, multi: <boolean>, collation: <document>,
     * zkrayFilters: <array> }, { q: <query>, u: <update>, upsert: <boolean>,
     * multi: <boolean>, collation: <document>, zkrayFilters: <array> }, ... ],
     * ordered: <boolean>, writeConcern: { <write concern> },
     * bypassDocumentValidation: <boolean> }
     *
     * Field Type Description
     * 
     * update string The name of the target collection. updates zkray An zkray
     * of one or more update statements to perform in the named collection.
     * ordered boolean Optional. If true, then when an update statement fails,
     * return without performing the remaining update statements. If false, then
     * when an update fails, continue with the remaining update statements, if
     * any. Defaults to true. writeConcern document Optional. A document
     * expressing the write concern of the update command. Omit to use the
     * default write concern. bypassDocumentValidation boolean Optional. Enables
     * update to bypass document validation during the operation. This lets you
     * update documents that do not meet the validation requirements.
     * 
     */

    private static final String commandOpt_update = "update";

    private static final String commandOpt_updates = "updates";

    private static final String commandOpt_ordered = "ordered";

    private static final String commandOpt_writeConcern = "writeConcern";

    private static final String commandOpt_bypassDocumentValidation = "bypassDocumentValidation";

    public ZKUpdate(String colleationName) {
        this.put(commandOpt_update, colleationName);
        this.put(commandOpt_updates, new ArrayList<>());
    }

    @SuppressWarnings("unchecked")
    public void addUpdates(Collection<ZKUpdateElement> updates) {
        this.get(commandOpt_updates, Collection.class).addAll(updates);
    }

    public void addUpdates(ZKUpdateElement... updates) {
        this.addUpdates(Arrays.asList(updates));
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
