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
 * @Title: ZKCreateIndexes.java 
 * @author Vinson 
 * @Package com.zk.mongo.command.administration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:05:03 PM 
 * @version V1.0   
*/
package com.zk.mongo.command.administration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.zk.mongo.ZKBaseDocument;
import com.zk.mongo.element.ZKIndexElement;

/** 
* @ClassName: ZKCreateIndexes 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCreateIndexes extends ZKBaseDocument {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    private static final String commandOpt_createIndexes = "createIndexes";

    private static final String commandOpt_indexes = "indexes";

    public ZKCreateIndexes(String colleationName, ZKIndexElement... indexes) {
        this(colleationName, Arrays.asList(indexes));
    }

    public ZKCreateIndexes(String colleationName, Collection<ZKIndexElement> indexes) {
        this.put(commandOpt_createIndexes, colleationName);
        this.put(commandOpt_indexes, new ArrayList<>());
        this.addIndexs(indexes);
    }

    /**
     * 添加要新增的索引
     * 
     * @param indexs
     */
    @SuppressWarnings("unchecked")
    public void addIndexs(Collection<ZKIndexElement> indexes) {
        this.get(commandOpt_indexes, Collection.class).addAll(indexes);
    }

    public static ZKIndexElement IndexElement(String indexName, String... keys) {
        return new ZKIndexElement(indexName, keys);
    }

}
