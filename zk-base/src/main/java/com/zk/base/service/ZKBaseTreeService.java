/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKBaseTreeService.java 
* @author Vinson 
* @Package com.zk.base.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Oct 20, 2020 2:55:09 PM 
* @version V1.0 
*/
package com.zk.base.service;

import java.io.Serializable;
import java.util.List;

import com.zk.base.dao.ZKBaseTreeDao;
import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.core.commons.data.ZKPage;

/** 
* @ClassName: ZKBaseTreeService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKBaseTreeService<ID extends Serializable, E extends ZKBaseTreeEntity<ID, E>, D extends ZKBaseTreeDao<ID, E>>
        extends ZKBaseService<ID, E, D> {

    /**
     * 树形查询； 不分页
     */
    public List<E> findTree(E entity) {
        entity.setParentIdIsEmpty(true);
        return this.dao.findTree(entity);
    }

    /**
     * 树形查询；分页
     */
    public ZKPage<E> findTree(ZKPage<E> page, E entity) {
        entity.setParentIdIsEmpty(true);
        entity.setPage(page);
        page.setResult(this.dao.findTree(entity));
        return page;
    }

}
