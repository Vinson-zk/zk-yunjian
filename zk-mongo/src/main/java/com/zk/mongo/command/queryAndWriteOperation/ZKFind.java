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
 * @Title: ZKFind.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.queryAndWriteOperation 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:01:13 PM 
 * @version V1.0   
*/
package com.zk.mongo.command.queryAndWriteOperation;

import java.util.Arrays;
import java.util.Collection;

import org.bson.Document;

import com.zk.mongo.ZKBaseDocument;
import com.zk.mongo.operator.ZKQueryOpt;

/**
 * @ClassName: ZKFind
 * @Description: 只实现了部分 key；其他 key 可用原生方法添加
 * @author Vinson
 * @version 1.0
 */
public class ZKFind extends ZKBaseDocument {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    /**
     * Field Type Description find string The name of the collection or view to
     * query. filter document Optional. The query predicate. If unspecified,
     * then all documents in the collection will match the predicate. sort
     * document Optional. The sort specification for the ordering of the
     * results. projection document Optional. The projection specification to
     * determine which fields to include in the returned documents. See Project
     * Fields to Return from Query and Projection Operators. limit Non-negative
     * integer Optional. The maximum number of documents to return. If
     * unspecified, then defaults to no limit. A limit of 0 is equivalent to
     * setting no limit.
     */

    private static final String commandOpt_find = "find";

    private static final String commandOpt_filter = "filter";

    private static final String commandOpt_projection = "projection";

//  private static final String commandOpt_sort = "sort";
//  private static final String commandOpt_limit = "limit";

    public ZKFind(String collectionName) {
        this.put(commandOpt_find, collectionName);
    }

    public void setFilter(ZKQueryOpt filter) {
        if (filter == null) {
            this.remove(commandOpt_filter);
        }
        else {
            this.put(commandOpt_filter, filter);
        }
    }

    public void setProjectionIncludeKeys(String... includeKeys) {
        if (includeKeys != null) {
            if (includeKeys != null && includeKeys.length > 0) {
                this.setProjection(Arrays.asList(includeKeys), null);
            }
        }
    }

    public void setProjectionExcludeKeys(String... excludeKeys) {
        if (excludeKeys != null) {
            if (excludeKeys != null && excludeKeys.length > 0) {
                this.setProjection(null, Arrays.asList(excludeKeys));
            }
        }
    }

    public void setProjection(Collection<String> includeKeys, Collection<String> excludeKeys) {
        if (includeKeys == null && excludeKeys == null) {
            this.remove(commandOpt_projection);
        }
        else {
            Document projection = this.get(commandOpt_projection, Document.class);
            if (projection == null) {
                projection = new Document();
            }
            if (includeKeys != null && includeKeys.size() > 0) {
                for (String key : includeKeys) {
                    projection.put(key, 1);
                }
            }
            if (excludeKeys != null && excludeKeys.size() > 0) {
                for (String key : excludeKeys) {
                    projection.put(key, 0);
                }
            }
            this.put(commandOpt_projection, projection);
        }
    }

}
