/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.auth.entity.ZKSysAuthCompany;

/**
 * ZKSysAuthCompanyDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthCompanyDao extends ZKBaseDao<String, ZKSysAuthCompany> {

    // 查询公司拥有的权限ID
    @Select({ " <script>", "SELECT c_auth_id FROM ${tn} WHERE c_company_id = #{companyId} and c_del_flag = #{delFlag}",
            "<if test=\"ownerType != null\" >and c_owner_type = #{ownerType} </if>", "</script> " })
    List<String> findAuthIdsByCompanyId(@Param("tn") String tn, @Param("companyId") String companyId,
            @Param("ownerType") Integer ownerType, @Param("delFlag") int delFlag);

    // 根据关联关系查询 权限关系实体
    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE ${tAlias}.c_auth_id = #{authId} and ${tAlias}.c_company_id = #{companyId} ")
    ZKSysAuthCompany getRelationByAuthIdAndCompanyId(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("authId") String authId, @Param("companyId") String companyId);

    // 根据权限代码ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} ")
    int diskDelByAuthId(@Param("tn") String tn, @Param("authId") String authId);

    // 根据公司ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_company_id = ${companyId} ")
    int diskDelByCompanyId(@Param("tn") String tn, @Param("companyId") String companyId);

    // 根据权限代码ID和菜单ID 物理删除
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} and c_company_id = ${companyId} ")
    int diskDelByAuthIdAndCompanyId(@Param("tn") String tn, @Param("authId") String authId,
            @Param("companyId") String companyId);
}
