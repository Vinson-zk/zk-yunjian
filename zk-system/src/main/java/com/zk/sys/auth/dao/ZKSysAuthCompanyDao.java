/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.auth.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.core.commons.data.ZKPage;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.auth.entity.ZKSysAuthCompany;
import com.zk.sys.auth.entity.ZKSysAuthDefined;

/**
 * ZKSysAuthCompanyDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthCompanyDao extends ZKBaseDao<String, ZKSysAuthCompany> {

//    // 查询公司拥有的权限ID
//    @Select({ " <script>", "SELECT c_auth_id FROM ${tn} WHERE c_company_id = #{companyId} and c_del_flag = #{delFlag}",
//            "<if test=\"ownerType != null\" >and c_owner_type = #{ownerType} </if>", "</script> " })
//    List<String> findAuthIdsByCompanyId(@Param("tn") String tn, @Param("companyId") String companyId,
//            @Param("ownerType") Integer ownerType, @Param("delFlag") int delFlag);

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

    // 查询指定公司 拥有的 API 接口代码
    @Select({ //
            "SELECT distinct(t2.c_func_api_code)", //
            "FROM ${authCompanyTn} t1 ", //
            "LEFT JOIN ${authApiTn} t2 on t2.c_auth_id = t1.c_auth_id and  t2.c_del_flag = #{delFlag}", //
            "WHERE t2.c_system_code and #{systemCode} and t1.c_company_id = #{companyId} and t1.c_del_flag = #{delFlag} " //
    })
    Set<String> findApiCodesByCompanyId(@Param("authCompanyTn") String authCompanyTn,
            @Param("authApiTn") String authApiTn, @Param("companyId") String companyId,
            @Param("systemCode") String systemCode, @Param("delFlag") int delFlag);

    // 查询指定公司拥有权限ID
    @Select({
            "SELECT distinct(t.c_auth_id) FROM ${tn} t WHERE t.c_company_id = #{companyId} AND t.c_del_flag = ${delFlag}" })
    List<String> findAuthIdsByCompanyId(@Param("tn") String tn, @Param("companyId") String companyId,
            @Param("delFlag") int delFlag);

    // 查询指定公司拥有权限代码
    @Select({
            "SELECT distinct(t.c_auth_code) FROM ${tn} t WHERE t.c_company_id = #{companyId} AND t.c_del_flag = ${delFlag}" })
    Set<String> findAuthCodesByCompanyId(@Param("tn") String tn, @Param("companyId") String companyId,
            @Param("delFlag") int delFlag);

    // 查询指定公司的权限及拥有权限的方式
    @Select({ "<script>", //
            "SELECT ${authSelCols}, ", //
            "  t2.c_pk_id as 'authRelation.pkId', t2.c_del_flag as 'authRelation.delFlag', ", //
            "  t2.c_auth_id as 'authRelation.leftPkId', t2.c_company_id as 'authRelation.rightPkId', ", //
            "  t2.c_owner_type as 'authRelation.ownerType', t2.c_to_child as 'authRelation.defaultToChild' ", //
            "FROM (", // -- 授权公司 拥有的权限(关系)
            "  SELECT t.* ", //
            "  FROM ${authCompanyTn} t", //
            "  WHERE t.c_company_id = #{companyId} AND t.c_del_flag = 0", //
            ") t2 ", //
            "LEFT JOIN ${authTn} ${authTnAlias} ON ${authTnAlias}.c_del_flag = 0 AND t2.c_auth_id = ${authTnAlias}.c_pk_id", //
            "<where>", //
            "<if test='ownerType != null' >", //
            "  AND t2.c_owner_type = #{ownerType}", //
            "</if>", //
            "<if test='searchValue != null and searchValue != \"\" ' >", //
            "  AND (", //
            "    ${authTnAlias}.c_code LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "    OR ${authTnAlias}.c_source_code LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "    OR ${authTnAlias}.c_name LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "  ),", //
            "</if>", // t1.c_code like '' or t1.c_source_code like '' or t1.c_name like ''
            "</where>", //
            "</script>" })
    List<ZKSysAuthDefined> findAuthByCompanyId(@Param("authTn") String authTn, @Param("authTnAlias") String authTnAlias,
            @Param("authSelCols") String authSelCols, @Param("authCompanyTn") String authCompanyTn,
            @Param("companyId") String companyId, @Param("ownerType") Integer ownerType,
            @Param("searchValue") String searchValue, @Param("page") ZKPage<ZKSysAuthDefined> page);
  
    // 根据授权公司与被授权公司，查询可授权的权限列表及被授权公司已拥有的权限状态
    @Select({ "<script>", //
            "SELECT ${authSelCols},", //
            "  t2.c_pk_id as 'authRelation.pkId', t2.c_del_flag as 'authRelation.delFlag',", //
            "  t2.c_auth_id as 'authRelation.leftPkId', t2.c_company_id as 'authRelation.rightPkId',", //
            "  t2.c_owner_type as 'authRelation.ownerType'", //
            "FROM (", // -- 授权公司 可向下级公司分配的权限
            "<choose>", //
            "  <when test='fromCompanyId != null and fromCompanyId != \"\" ' >", // 由授权公司给另一公司授权
            "      SELECT t2.* ", //
            "      FROM (", // -- 授权公司 完全拥有的权限(关系)
            "        SELECT t.* ", //
            "        FROM ${authCompanyTn} t ", //
            "        WHERE t.c_company_id = #{fromCompanyId} AND t.c_del_flag = 0 AND t.c_owner_type = 1 ", //
            "      ) t1 ", //
            "      LEFT JOIN ${authTn} t2 ON t2.c_del_flag = 0 AND t1.c_auth_id = t2.c_pk_id ", //
            "  </when>", //
            "  <otherwise>", // 由平台向平台拥有者授权
            "    SELECT * FROM ${authTn} WHERE c_del_flag = 0 ", //
            "  </otherwise>", //
            "</choose>", //
            ") ${authTnAlias} ", //
            "LEFT JOIN ( ", // -- 被授权公司，已拥有的权限(关系)
            "  SELECT t.* ", //
            "  FROM ${authCompanyTn} t ", //
            "  WHERE t.c_company_id = #{toCompanyId} AND t.c_del_flag = 0 ", //
            ") t2 ", //
            "ON ${authTnAlias}.c_pk_id = t2.c_auth_id ", //
            "<where>", //
            "<if test='searchValue != null and searchValue != \"\" ' >", //
            "  ${authTnAlias}.c_code LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "  OR ${authTnAlias}.c_source_code LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "  OR ${authTnAlias}.c_name LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{searchValue}, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')", //
            "</if>", // t1.c_code like '' or t1.c_source_code like '' or t1.c_name like ''
            "</where>", //
            "</script>" })
    List<ZKSysAuthDefined> findAllotAuthByCompanyId(@Param("authTn") String authTn,
            @Param("authTnAlias") String authTnAlias, @Param("authSelCols") String authSelCols,
            @Param("authCompanyTn") String authCompanyTn, @Param("fromCompanyId") String fromCompanyId,
            @Param("toCompanyId") String toCompanyId, @Param("searchValue") String searchValue,
            @Param("page") ZKPage<ZKSysAuthDefined> page);


}

