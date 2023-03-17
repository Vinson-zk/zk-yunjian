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
* @Title: ZKBaseHelperTreeEntityDao.java 
* @author Vinson 
* @Package com.zk.base.helper.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 2, 2023 5:29:33 PM 
* @version V1.0 
*/
package com.zk.base.helper.dao;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.FetchType;

import com.zk.base.dao.ZKBaseTreeDao;
import com.zk.base.helper.entity.ZKBaseHelperTreeEntity;
import com.zk.base.myBaits.provider.ZKMyBatisTreeSqlProvider;
import com.zk.db.annotation.ZKMyBatisDao;

/** 
* @ClassName: ZKBaseHelperTreeEntityDao 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKMyBatisDao
public interface ZKBaseHelperTreeEntityDao extends ZKBaseTreeDao<String, ZKBaseHelperTreeEntity> {

    /**
     * 树形查询，此方法为递归查询树型节点；1.不支持子节点过虑; 2.仅支持根节点过滤与分页； fetchType = FetchType.EEAGER 不能改为懒加截，不然会报错：com.linde.wms.dto.BinGroupInfoDto_$$_jvst38e_0["handler"])
     * 虽然可以用 @JsonIgnoreProperties(value = "handler") 解决报错，但查出的数据还是错的；
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTree")
    @Results(id = "treeResult", value = {
            @Result(column = "{parentId=pkId}", property = "children", javaType = List.class, many = @Many(select = "com.zk.base.helper.dao.ZKBaseHelperTreeEntityDao.findTree", fetchType = FetchType.EAGER)) })
//    @ResultMap("treeResult")
    List<ZKBaseHelperTreeEntity> findTree(ZKBaseHelperTreeEntity entity);

    /**
     * 查询详情，包含父节点 fetchType = FetchType.EEAGER
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTreeDetail")
    @Results(value = {
            @Result(column = "{pkId=parentId}", property = "parent", javaType = ZKBaseHelperTreeEntity.class, one = @One(select = "com.zk.base.helper.dao.ZKBaseHelperTreeEntityDao.getDetail", fetchType = FetchType.EAGER)) })
    ZKBaseHelperTreeEntity getDetail(ZKBaseHelperTreeEntity entity);

}
