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
import com.zk.sys.auth.entity.ZKSysAuthDept;

/**
 * ZKSysAuthDeptDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthDeptDao extends ZKBaseDao<String, ZKSysAuthDept> {

    // 通过部门拥有的 API 接口代码
    @Select({ " SELECT distinct(t2.c_func_api_code)", "FROM ${authDeptTb} t1 ",
            "LEFT JOIN ${authApiTb} t2 on t2.c_auth_id = t1.c_auth_id and  t2.c_del_flag = #{delFlag}",
            "WHERE t1.c_dept_id = #{deptId} and t1.c_del_flag = #{delFlag} " })
    List<String> findApiCodesByDeptId(@Param("authDeptTb") String userRoleTb, @Param("authApiTb") String authRoleTb,
            @Param("deptId") String deptId, @Param("delFlag") int delFlag);

    // 查询部门拥有的权限代码
    @Select(" SELECT c_auth_code FROM ${tn} WHERE c_dept_id = #{deptId} and c_del_flag = #{delFlag} ")
    List<String> findAuthCodesByDeptId(@Param("tn") String tn, @Param("deptId") String deptId,
            @Param("delFlag") int delFlag);

    // 查询部门拥有的权限ID
    @Select(" SELECT c_auth_id FROM ${tn} WHERE c_dept_id = #{deptId} and c_del_flag = #{delFlag} ")
    List<String> findAuthIdsByDeptId(@Param("tn") String tn, @Param("deptId") String deptId,
            @Param("delFlag") int delFlag);

    // 根据关联关系查询 权限关系实体
    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE ${tAlias}.c_auth_id = #{authId} and ${tAlias}.c_dept_id = #{deptId} ")
    ZKSysAuthDept getRelationByAuthIdAndDeptId(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("authId") String authId, @Param("deptId") String deptId);

    // 根据权限代码ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} ")
    int diskDelByAuthId(@Param("tn") String tn, @Param("authId") String authId);

    // 根据部门ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_dept_id = ${deptId} ")
    int diskDelByDeptId(@Param("tn") String tn, @Param("deptId") String deptId);

    // 根据权限代码ID和部门ID 物理删除
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} and c_dept_id = ${deptId} ")
    int diskDelByAuthIdAndDeptId(@Param("tn") String tn, @Param("authId") String authId,
            @Param("deptId") String deptId);
}