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
* @Title: ZKTreeSqlProvider.java 
* @author Vinson 
* @Package com.zk.base.commons 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 28, 2021 1:46:32 PM 
* @version V1.0 
*/
package com.zk.base.commons;

import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.db.commons.ZKSqlConvert;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

/** 
* @ClassName: ZKTreeSqlHelper
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKTreeSqlHelper extends ZKDBSqlHelper {

    /**
     * Title:
     * Description:
     *
     * @param sqlConvert
     * @param entity
     */
    public ZKTreeSqlHelper(ZKSqlConvert sqlConvert, ZKBaseTreeEntity<?, ?> entity) {
        super(sqlConvert, entity); // TODO Auto-generated constructor stub
        // 目前可以和 list 查询共用
        this.blockSqlWhereTree = entity.getZKDbWhereTree(sqlConvert, this.getMapInfo())
            .toQueryCondition(sqlConvert, this.getMapInfo().getAlias());

//        this.blockSqlWhereTreeFilter = entity.getZKDbWhereTreeFilter(sqlConvert, this.getMapInfo()).toQueryCondition(sqlConvert, this.getMapInfo().getAlias());
    }

    // 递归查询树形节点; 1.不支持子节点过虑; 2.仅支持根节点过滤与分页；
    String blockSqlWhereTree;

    // 树形所有节点，统一过滤，过滤结果中不是根结点时，如果父节点不在过滤结果中，升级为结果中的根节点；如果父节点在过滤结果中，则不做为返回结果; 且不递归查询子节点；
//    String blockSqlWhereTreeFilter;

    public String getBlockSqlWhereTree() {
        return blockSqlWhereTree;
    }

//    public String getBlockSqlWhereTreeFilter() {
//        return blockSqlWhereTreeFilter;
//    }

}
