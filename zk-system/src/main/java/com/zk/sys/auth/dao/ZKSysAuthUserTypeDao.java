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
import com.zk.sys.auth.entity.ZKSysAuthUserType;

/**
 * ZKSysAuthUserTypeDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthUserTypeDao extends ZKBaseDao<String, ZKSysAuthUserType> {

    // 通过用户类型拥有的 API 接口代码
    @Select({ " SELECT distinct(t2.c_func_api_code)", "FROM ${authUserTypeTb} t1 ",
            "LEFT JOIN ${authApiTb} t2 on t2.c_auth_id = t1.c_auth_id and  t2.c_del_flag = #{delFlag}",
            "WHERE t1.c_user_type_id = #{userTypeId} and t1.c_del_flag = #{delFlag} " })
    List<String> findApiCodesByUserTypeId(@Param("authUserTypeTb") String authUserTypeTb,
            @Param("authApiTb") String authRoleTb, @Param("userTypeId") String userTypeId,
            @Param("delFlag") int delFlag);

    // 查询用户类型拥有的权限代码
    @Select(" SELECT c_auth_code FROM ${tn} WHERE c_user_type_id = #{userTypeId} and c_del_flag = #{delFlag} ")
    List<String> findAuthCodesByUserTypeId(@Param("tn") String tn, @Param("userTypeId") String userTypeId,
            @Param("delFlag") int delFlag);

    // 查询用户类型拥有的权限ID
    @Select(" SELECT c_auth_id FROM ${tn} WHERE c_user_type_id = #{userTypeId} and c_del_flag = #{delFlag} ")
    List<String> findAuthIdsByUserTypeId(@Param("tn") String tn, @Param("userTypeId") String userTypeId,
            @Param("delFlag") int delFlag);

    // 根据关联关系查询 权限关系实体
    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE ${tAlias}.c_auth_id = #{authId} and ${tAlias}.c_user_type_id = #{userTypeId} ")
    ZKSysAuthUserType getRelationByAuthIdAndUserTypeId(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("authId") String authId, @Param("userTypeId") String userTypeId);

    // 根据权限代码ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} ")
    int diskDelByAuthId(@Param("tn") String tn, @Param("authId") String authId);

    // 根据 用户类型ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_user_type_id = ${userTypeId} ")
    int diskDelByUserTypeId(@Param("tn") String tn, @Param("userTypeId") String userTypeId);

    // 根据权限代码ID和用户类型ID 物理删除
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} and c_user_type_id = ${userTypeId} ")
    int diskDelByAuthIdAndUserTypeId(@Param("tn") String tn, @Param("authId") String authId,
            @Param("userTypeId") String userTypeId);
}
