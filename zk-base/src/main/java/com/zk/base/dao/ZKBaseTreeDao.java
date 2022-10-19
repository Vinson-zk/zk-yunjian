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
* @Title: ZKBaseTreeDao.java 
* @author Vinson 
* @Package com.zk.base.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Sep 23, 2020 4:46:23 PM 
* @version V1.0 
*/
package com.zk.base.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.base.myBaits.provider.ZKMyBatisTreeSqlProvider;

/** 
* @ClassName: ZKBaseTreeDao 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKBaseTreeDao<ID extends Serializable, E extends ZKBaseTreeEntity<ID, E>> extends ZKBaseDao<ID, E> {

    /**
     * 树形所有节点，统一过滤，过滤结果中不是根结点时，如果父节点不在过滤结果中，升级为结果中的根节点；如果父节点在过滤结果中，则不做为返回结果; 且不递归查询子节点；
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTreeFilter")
    List<E> findTreeFilter(E entity);

}
