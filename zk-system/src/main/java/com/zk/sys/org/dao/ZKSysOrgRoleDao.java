/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.org.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.org.entity.ZKSysOrgRole;

/**
 * ZKSysOrgRoleDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysOrgRoleDao extends ZKBaseDao<String, ZKSysOrgRole> {

    /**
     * 根据角色代码取角色，公司下角色代码唯一
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 8:23:47 AM
     * @param tn
     * @param alias
     * @param sCols
     * @param companyId
     * @param code
     * @return
     * @return ZKSysOrgRank
     */
    @Select(value = { "SELECT ${sCols} FROM ${tn} ${alias} WHERE c_company_id = #{companyId} AND c_code = #{code}" })
    ZKSysOrgRole getByCode(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("companyId") String companyId, @Param("code") String code);
	
}