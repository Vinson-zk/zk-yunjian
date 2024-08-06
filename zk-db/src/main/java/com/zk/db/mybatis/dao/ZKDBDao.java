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
* @Title: ZKDBDao.java 
* @author Vinson 
* @Package com.zk.db.mybatis.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Sep 14, 2020 3:30:26 PM 
* @version V1.0 
*/
package com.zk.db.mybatis.dao;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.zk.db.entity.ZKDBEntity;
import com.zk.db.mybatis.provider.ZKDBMybatisSqlProvider;

/**
 * @ClassName: ZKDBDao
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public interface ZKDBDao<E extends ZKDBEntity<E>> {

    /**
     * 插入
     *
     * @Title: insert
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 20, 2020 11:50:42 PM
     * @param entity
     * @return int
     */
    @InsertProvider(type = ZKDBMybatisSqlProvider.class, method = "insert")
    int insert(E entity);

    /**
     * 修改
     *
     * @Title: update
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 20, 2020 11:50:46 PM
     * @param entity
     * @return int
     */
    @UpdateProvider(type = ZKDBMybatisSqlProvider.class, method = "update")
    int update(E entity);

    /**
     * 逻辑删除
     *
     * @Title: del
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 20, 2020 11:50:49 PM
     * @param entity
     * @return int
     */
    @UpdateProvider(type = ZKDBMybatisSqlProvider.class, method = "del")
    int del(E entity);

    /**
     * 物理删除
     *
     * @Title: diskDel
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 20, 2020 11:50:53 PM
     * @param entity
     * @return int
     */
    @DeleteProvider(type = ZKDBMybatisSqlProvider.class, method = "diskDel")
    int diskDel(E entity);

    /**
     * 按主键查询
     *
     * @Title: get
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 20, 2020 11:50:56 PM
     * @param entity
     * @return T
     */
    @SelectProvider(type = ZKDBMybatisSqlProvider.class, method = "get")
    E get(E entity);

    /**
     * 列表查询
     *
     * @Title: selectList
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 20, 2020 11:51:33 PM
     * @param entity
     * @return List<T>
     */
    @SelectProvider(type = ZKDBMybatisSqlProvider.class, method = "selectList")
//    @Select(value = {
//            "<script>SELECT t0.c_json AS \"json\", t0.c_id AS \"id\", t0.c_type AS \"type\", t0.c_value AS \"value\", t0.c_remarks AS \"remarks\" FROM t_test t0 <where><if test=' type != null '>AND t0.c_type = #{type}</if></where> </script>" })
    List<E> findList(E entity);

}
