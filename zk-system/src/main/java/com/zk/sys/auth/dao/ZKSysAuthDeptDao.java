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
import com.zk.sys.auth.entity.ZKSysAuthDept;

/**
 * ZKSysAuthDeptDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthDeptDao extends ZKBaseDao<String, ZKSysAuthDept> {

    // 通过部门拥有的 API 接口代码
    @Select({ //
            " SELECT distinct(t2.c_func_api_code)", //
            "FROM ${authDeptTb} t1 ", //
            "LEFT JOIN ${authApiTb} t2 on t2.c_auth_id = t1.c_auth_id and  t2.c_del_flag = #{delFlag}", //
            "WHERE t2.c_system_code and #{systemCode} and t1.c_dept_id = #{deptId} and t1.c_del_flag = #{delFlag} " //
    })
    List<String> findApiCodesByDeptId(@Param("authDeptTb") String authDeptTb, @Param("authApiTb") String authApiTb,
            @Param("deptId") String deptId, @Param("systemCode") String systemCode, @Param("delFlag") int delFlag);

    // 查询部门拥有的权限代码
    @Select(" SELECT distinct(c_auth_code) FROM ${tn} WHERE c_dept_id = #{deptId} and c_del_flag = #{delFlag} ")
    List<String> findAuthCodesByDeptId(@Param("tn") String tn, @Param("deptId") String deptId,
            @Param("delFlag") int delFlag);

    // 查询部门拥有的权限ID
    @Select(" SELECT distinct(c_auth_id) FROM ${tn} WHERE c_dept_id = #{deptId} and c_del_flag = #{delFlag} ")
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

    // 查询给指定部门授权时，公司可分配的权限以及部门已拥有的权限
    @Select({ "<script>", //
            "SELECT ${authSelCols}, ", //
            "  t2.c_pk_id as 'authRelation.pkId', t2.c_del_flag as 'authRelation.delFlag', ", //
            "  t2.c_auth_id as 'authRelation.leftPkId', t2.c_dept_id as 'authRelation.rightPkId' ", //
            "FROM (", // -- 授权公司 完成拥有的权限(关系)
            "  SELECT t.* ", //
            "  FROM ${authCompanyTn} t", //
            "  WHERE t.c_company_id = #{fromCompanyId} AND t.c_del_flag = 0", //
            ") t1 ", //
            "LEFT JOIN ${authTn} ${authTnAlias} ON ${authTnAlias}.c_del_flag = 0 AND t1.c_auth_id = ${authTnAlias}.c_pk_id", //
            "LEFT JOIN (", //
            "  SELECT t.* FROM ${authDeptTn} t WHERE t.c_del_flag = 0 AND t.c_dept_id = #{toDeptId}", //
            ") t2 ON t2.c_auth_id = ${authTnAlias}.c_pk_id ", //
            "<where>", //
            "<if test='searchValue != null and searchValue != \"\" ' >", //
            "  ${authTnAlias}.c_code LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "  OR ${authTnAlias}.c_source_code LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "  OR ${authTnAlias}.c_name LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "</if>", // t1.c_code like '' or t1.c_source_code like '' or t1.c_name like ''
            "</where>", //
            "</script>" })
    List<ZKSysAuthDefined> findAllotAuthByDeptId(@Param("authTn") String authTn,
            @Param("authTnAlias") String authTnAlias, @Param("authSelCols") String authSelCols,
            @Param("authCompanyTn") String authCompanyTn, @Param("authDeptTn") String authDeptTn,
            @Param("fromCompanyId") String fromCompanyId, @Param("toDeptId") String toDeptId,
            @Param("searchValue") String searchValue, @Param("page") ZKPage<ZKSysAuthDefined> page);
}