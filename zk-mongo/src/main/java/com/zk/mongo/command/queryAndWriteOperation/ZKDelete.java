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
 * @Title: ZKDelete.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.queryAndWriteOperation 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:00:54 PM 
 * @version V1.0   
*/
package com.zk.mongo.command.queryAndWriteOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.bson.Document;

import com.zk.mongo.ZKBaseDocument;
import com.zk.mongo.element.ZKDeleteElement;

/** 
* @ClassName: ZKDelete 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDelete extends ZKBaseDocument {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    /**
     * delete string The name of the target collection. deletes zkray An zkray
     * of one or more delete statements to perform in the named collection.
     * ordered boolean Optional. If true, then when a delete statement fails,
     * return without performing the remaining delete statements. If false, then
     * when a delete statement fails, continue with the remaining delete
     * statements, if any. Defaults to true. writeConcern document Optional. A
     * document expressing the write concern of the delete command. Omit to use
     * the default write concern.
     */
    private static final String commandOpt_delete = "delete";

    private static final String commandOpt_deletes = "deletes";

    private static final String commandOpt_ordered = "ordered";

    private static final String commandOpt_writeConcern = "writeConcern";

    public ZKDelete(String colleationName, ZKDeleteElement... deleteElements) {
        this(colleationName, Arrays.asList(deleteElements));
    }

    public ZKDelete(String colleationName, Collection<ZKDeleteElement> deleteElements) {
        this.put(commandOpt_delete, colleationName);
        this.put(commandOpt_deletes, new ArrayList<>());
        if(deleteElements != null){
            this.addDeletes(deleteElements);
        }
    }

    public void addDeletes(ZKDeleteElement... deleteElements) {
        this.addDeletes(Arrays.asList(deleteElements));
    }

    @SuppressWarnings("unchecked")
    public void addDeletes(Collection<ZKDeleteElement> deleteElements) {
        this.get(commandOpt_deletes, Collection.class).addAll(deleteElements);
    }

    public void setOrdered(boolean ordered) {
        this.put(commandOpt_ordered, ordered);
    }

    public void setWriteConcern(Document writeConcern) {
        if (writeConcern == null) {
            this.remove(commandOpt_writeConcern);
        }
        else {
            this.put(commandOpt_writeConcern, writeConcern);
        }
    }

}
