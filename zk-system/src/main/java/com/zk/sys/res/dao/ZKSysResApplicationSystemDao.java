/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.res.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.res.entity.ZKSysResApplicationSystem;

/**
 * ZKSysResApplicationSystemDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysResApplicationSystemDao extends ZKBaseDao<String, ZKSysResApplicationSystem> {

    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE c_code = #{code} ")
    ZKSysResApplicationSystem getByCode(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("code") String code);
}