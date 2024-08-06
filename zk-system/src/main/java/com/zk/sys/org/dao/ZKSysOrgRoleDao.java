/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.org.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Delete;
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

    // 根据公司ID做逻辑删除
    @Delete("UPDATE ${tn} SET c_del_flag = #{delFlag}, c_update_user_id = #{updateUserId}, c_update_date = #{updateDate} WHERE c_company_id = #{companyId} ")
    int delByCompanyId(@Param("tn") String tn, @Param("companyId") String companyId, @Param("delFlag") int delFlag,
            @Param("updateUserId") String updateUserId, @Param("updateDate") Date updateDate);

    // 根据公司ID做物理删除
    @Delete("DELETE FROM ${tn} WHERE c_company_id = #{companyId}")
    int diskDelByCompanyId(@Param("tn") String tn, @Param("companyId") String companyId);
	
}