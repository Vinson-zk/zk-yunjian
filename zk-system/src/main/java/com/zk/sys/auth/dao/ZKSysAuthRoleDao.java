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
import com.zk.sys.auth.entity.ZKSysAuthRole;

/**
 * ZKSysAuthRoleDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthRoleDao extends ZKBaseDao<String, ZKSysAuthRole> {

    // 查询角色拥有的权限代码
    @Select(" SELECT c_auth_code FROM ${tn} WHERE c_role_id = #{roleId} and c_del_flag = #{delFlag} ")
    List<String> findAuthCodesByRoleId(@Param("tn") String tn, @Param("roleId") String roleId,
            @Param("delFlag") int delFlag);

    // 查询角色拥有的权限ID
    @Select(" SELECT c_auth_id FROM ${tn} WHERE c_role_id = #{roleId} and c_del_flag = #{delFlag} ")
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
}