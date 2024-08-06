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
import com.zk.sys.auth.entity.ZKSysAuthFuncApi;

/**
 * ZKSysAuthFuncApiDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthFuncApiDao extends ZKBaseDao<String, ZKSysAuthFuncApi> {

    // 查询权限拥有的API接口代码
    @Select(" SELECT distinct(c_func_api_code) FROM ${tn} WHERE c_auth_id = #{authId} and c_del_flag = #{delFlag} ")
    List<String> findApiCodesByAuthId(@Param("tn") String tn, @Param("authId") String authId,
            @Param("delFlag") int delFlag);

    // 查询权限拥有的API接口ID
    @Select(" SELECT distinct(c_func_api_id) FROM ${tn} WHERE c_auth_id = #{authId} and c_del_flag = #{delFlag} ")
    List<String> findApiIdsByAuthId(@Param("tn") String tn, @Param("authId") String authId,
            @Param("delFlag") int delFlag);

    // 根据关联关系查询 权限关系实体
    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE ${tAlias}.c_auth_id = #{authId} and ${tAlias}.c_func_api_id = #{funcApiId} ")
    ZKSysAuthFuncApi getRelationByAuthIdAndApiId(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("authId") String authId, @Param("funcApiId") String funcApiId);

    // 根据权限代码ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} ")
    int diskDelByAuthId(@Param("tn") String tn, @Param("authId") String authId);

    // 根据Api接口ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_func_api_id = ${funcApiId} ")
    int diskDelByApiId(@Param("tn") String tn, @Param("funcApiId") String funcApiId);

    // 根据权限代码ID和Api接口ID 物理删除
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} and c_func_api_id = ${funcApiId} ")
    int diskDelByAuthIdAndApiId(@Param("tn") String tn, @Param("authId") String authId,
            @Param("funcApiId") String funcApiId);
}