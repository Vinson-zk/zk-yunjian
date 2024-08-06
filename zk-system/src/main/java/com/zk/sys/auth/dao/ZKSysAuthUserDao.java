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
import com.zk.sys.auth.entity.ZKSysAuthUser;

/**
 * ZKSysAuthUserDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthUserDao extends ZKBaseDao<String, ZKSysAuthUser> {

    // 用户直接拥有的 API 接口代码
    @Select({ " SELECT distinct(t2.c_func_api_code)", "FROM ${authUserTb} t1 ",
            "LEFT JOIN ${authApiTb} t2 on t2.c_auth_id = t1.c_auth_id and  t2.c_del_flag = #{delFlag}",
            "WHERE t2.c_system_code = #{systemCode} and t1.c_user_id = #{userId} and t1.c_del_flag = #{delFlag} " })
    List<String> findApiCodesByUserId(@Param("authUserTb") String authUserTb, @Param("authApiTb") String authRoleTb,
            @Param("userId") String userId, @Param("systemCode") String systemCode, @Param("delFlag") int delFlag);

    // 查询用户拥有的权限代码
    @Select(" SELECT distinct(c_auth_code) FROM ${tn} WHERE c_user_id = #{userId} and c_del_flag = #{delFlag} ")
    List<String> findAuthCodesByUserId(@Param("tn") String tn, @Param("userId") String userId,
            @Param("delFlag") int delFlag);

    // 查询用户拥有的权限ID
    @Select(" SELECT distinct(c_auth_id) FROM ${tn} WHERE c_user_id = #{userId} and c_del_flag = #{delFlag} ")
    List<String> findAuthIdsByUserId(@Param("tn") String tn, @Param("userId") String userId,
            @Param("delFlag") int delFlag);

    // 根据关联关系查询 权限关系实体
    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE ${tAlias}.c_auth_id = #{authId} and ${tAlias}.c_user_id = #{userId} ")
    ZKSysAuthUser getRelationByAuthIdAndUserId(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("authId") String authId, @Param("userId") String userId);

    // 根据权限代码ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} ")
    int diskDelByAuthId(@Param("tn") String tn, @Param("authId") String authId);

    // 根据用户ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_user_id = ${userId} ")
    int diskDelByUserId(@Param("tn") String tn, @Param("userId") String userId);

    // 根据权限代码ID和用户ID 物理删除
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} and c_user_id = ${userId} ")
    int diskDelByAuthIdAndUserId(@Param("tn") String tn, @Param("authId") String authId,
            @Param("userId") String userId);

    // 查询给指定用户授权时，公司可分配的权限以及用户已拥有的权限
    @Select({ "<script>", //
            "SELECT ${authSelCols}, ", //
            "  t2.c_pk_id as 'authRelation.pkId', t2.c_del_flag as 'authRelation.delFlag', ", //
            "  t2.c_auth_id as 'authRelation.leftPkId', t2.c_user_id as 'authRelation.rightPkId' ", //
            "FROM (", // -- 授权公司 完成拥有的权限(关系)
            "  SELECT t.* ", //
            "  FROM ${authCompanyTn} t", //
            "  WHERE t.c_company_id = #{fromCompanyId} AND t.c_del_flag = 0", //
            ") t1 ", //
            "LEFT JOIN ${authTn} ${authTnAlias} ON ${authTnAlias}.c_del_flag = 0 AND t1.c_auth_id = ${authTnAlias}.c_pk_id", //
            "LEFT JOIN (", //
            "  SELECT t.* FROM ${authUserTn} t WHERE t.c_del_flag = 0 AND t.c_user_id = #{toUserId}", //
            ") t2 ON t2.c_auth_id = ${authTnAlias}.c_pk_id ", //
            "<where>", //
            "<if test='searchValue != null and searchValue != \"\" ' >", //
            "  ${authTnAlias}.c_code LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "  OR ${authTnAlias}.c_source_code LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "  OR ${authTnAlias}.c_name LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "</if>", // t1.c_code like '' or t1.c_source_code like '' or t1.c_name like ''
            "</where>", //
            "</script>" })
    List<ZKSysAuthDefined> findAllotAuthByUserId(@Param("authTn") String authTn,
            @Param("authTnAlias") String authTnAlias, @Param("authSelCols") String authSelCols,
            @Param("authCompanyTn") String authCompanyTn, @Param("authUserTn") String authUserTn,
            @Param("fromCompanyId") String fromCompanyId, @Param("toUserId") String toUserId,
            @Param("searchValue") String searchValue, @Param("page") ZKPage<ZKSysAuthDefined> page);
}
