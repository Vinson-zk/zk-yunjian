/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.res.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.res.entity.ZKSysResDictType;

/**
 * ZKSysResDictTypeDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysResDictTypeDao extends ZKBaseDao<String, ZKSysResDictType> {
    
    /**
     * 根据字典类型代码查询字典类型
     *
     * @Title: getByTypeCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 12:01:55 AM
     * @param tn
     * @param tAlias
     * @param sCols
     * @param typeCode
     * @return
     * @return ZKSysResDictType
     */
    @Select(value = { "SELECT ${sCols} FROM ${tn} ${tAlias} WHERE c_type_code = #{typeCode}" })
    ZKSysResDictType getByTypeCode(@Param("tn") String tn, @Param("tAlias") String tAlias, @Param("sCols") String sCols,
            @Param("typeCode") String typeCode);
	
    
}