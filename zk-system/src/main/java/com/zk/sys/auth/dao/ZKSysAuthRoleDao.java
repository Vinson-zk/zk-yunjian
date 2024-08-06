/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.core.commons.data.ZKPage;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthRole;

/**
 * ZKSysAuthRoleDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthRoleDao extends ZKBaseDao<String, ZKSysAuthRole> {

    // 查询角色拥有的权限代码
    @Select(" SELECT distinct(c_auth_code) FROM ${tn} WHERE c_role_id = #{roleId} and c_del_flag = #{delFlag} ")
    List<String> findAuthCodesByRoleId(@Param("tn") String tn, @Param("roleId") String roleId,
            @Param("delFlag") int delFlag);

    // 查询角色拥有的权限ID
    @Select(" SELECT distinct(c_auth_id) FROM ${tn} WHERE c_role_id = #{roleId} and c_del_flag = #{delFlag} ")
    List<String> findAuthIdsByRoleId(@Param("tn") String tn, @Param("roleId") String roleId,
            @Param("delFlag") int delFlag);

    // 根据关联关系查询 权限关系实体
    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE ${tAlias}.c_auth_id = #{authId} and ${tAlias}.c_role_id = #{roleId} ")
    ZKSysAuthRole getRelationByAuthIdAndRoleId(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("authId") String authId, @Param("roleId") String roleId);

    // 根据权限代码ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} ")
    int diskDelByAuthId(@Param("tn") String tn, @Param("authId") String authId);

    // 根据角色ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_role_id = ${roleId} ")
    int diskDelByRoleId(@Param("tn") String tn, @Param("roleId") String roleId);

    // 根据权限代码ID和角色ID 物理删除
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} and c_role_id = ${roleId} ")
    int diskDelByAuthIdAndRoleId(@Param("tn") String tn, @Param("authId") String authId,
            @Param("roleId") String roleId);

    // 查询给指定角色授权时，公司可分配的权限以及角色已拥有的权限
    @Select({ "<script>", //
            "SELECT ${authSelCols}, ", //
            "  t2.c_pk_id as 'authRelation.pkId', t2.c_del_flag as 'authRelation.delFlag', ", //
            "  t2.c_auth_id as 'authRelation.leftPkId', t2.c_role_id as 'authRelation.rightPkId' ", //
            "FROM (", // -- 授权公司 完成拥有的权限(关系)
            "  SELECT t.* ", //
            "  FROM ${authCompanyTn} t", //
            "  WHERE t.c_company_id = #{fromCompanyId} AND t.c_del_flag = 0", //
            ") t1 ", //
            "LEFT JOIN ${authTn} ${authTnAlias} ON ${authTnAlias}.c_del_flag = 0 AND t1.c_auth_id = ${authTnAlias}.c_pk_id", //
            "LEFT JOIN (", //
            "  SELECT t.* FROM ${authRoleTn} t WHERE t.c_del_flag = 0 AND t.c_role_id = #{toRoleId}", //
            ") t2 ON t2.c_auth_id = ${authTnAlias}.c_pk_id ", //
            "<where>", //
            "<if test='searchValue != null and searchValue != \"\" ' >", //
            "  ${authTnAlias}.c_code LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "  OR ${authTnAlias}.c_source_code LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "  OR ${authTnAlias}.c_name LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "</if>", // t1.c_code like '' or t1.c_source_code like '' or t1.c_name like ''
            "</where>", //
            "</script>" })
    List<ZKSysAuthDefined> findAllotAuthByRoleId(@Param("authTn") String authTn,
            @Param("authTnAlias") String authTnAlias, @Param("authSelCols") String authSelCols,
            @Param("authCompanyTn") String authCompanyTn, @Param("authRoleTn") String authRoleTn,
            @Param("fromCompanyId") String fromCompanyId, @Param("toRoleId") String toRoleId,
            @Param("searchValue") String searchValue, @Param("page") ZKPage<ZKSysAuthDefined> page);

}
