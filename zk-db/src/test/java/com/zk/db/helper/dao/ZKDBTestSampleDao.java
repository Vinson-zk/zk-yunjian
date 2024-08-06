package com.zk.db.helper.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.db.helper.entity.ZKDBTestSampleEntity;
import com.zk.db.mybatis.dao.ZKDBDao;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: imya dao
 * @ClassName ZKDBTestSampleDao
 * @Package com.zk.db.helper.dao
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-27 18:39:57
 **/
@ZKMyBatisDao
public interface ZKDBTestSampleDao extends ZKDBDao<ZKDBTestSampleEntity> {

    /*
    getBlockSqlCols
    getTableName
    getTableAlias
     */
    @Select({" select ${_zkSql.cols} from ${_zkSql.tn} ${_zkSql.ta} where ",
        "${_zkSql.ta}.c_id = #{id}"})
    ZKDBTestSampleEntity getById(@Param("id") String id, @Param("mInt") Integer[] mInt);
}
