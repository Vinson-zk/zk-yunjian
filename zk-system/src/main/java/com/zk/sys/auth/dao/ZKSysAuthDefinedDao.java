/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.auth.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.auth.entity.ZKSysAuthDefined;

/**
 * ZKSysAuthDefinedDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthDefinedDao extends ZKBaseDao<String, ZKSysAuthDefined> {

    /**
     * 根据权限代码取权限定义
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 5, 2022 9:42:28 AM
     * @param tn
     * @param alias
     * @param sCols
     * @param systemCode
     * @param code
     * @return
     * @return ZKSysAuthDefined
     */
    @Select(value = {
            "SELECT ${sCols} FROM ${tn} ${alias} WHERE ${alias}.c_code = #{code}" })
    ZKSysAuthDefined getByCode(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("code") String code);
	
}