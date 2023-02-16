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
import com.zk.sys.auth.entity.ZKSysAuthRank;

/**
 * ZKSysAuthRankDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthRankDao extends ZKBaseDao<String, ZKSysAuthRank> {

    // 通过职级拥有的 API 接口代码
    @Select({ " SELECT distinct(t2.c_func_api_code)", "FROM ${authRankTb} t1 ",
            "LEFT JOIN ${authApiTb} t2 on t2.c_auth_id = t1.c_auth_id and  t2.c_del_flag = #{delFlag}",
            "WHERE t1.c_rank_id = #{rankId} and t1.c_del_flag = #{delFlag} " })
    List<String> findApiCodesByRankId(@Param("authRankTb") String userRoleTb, @Param("authApiTb") String authRoleTb,
            @Param("rankId") String rankId, @Param("delFlag") int delFlag);

    // 查询职级拥有的权限代码
    @Select(" SELECT c_auth_code FROM ${tn} WHERE c_rank_id = #{rankId} and c_del_flag = #{delFlag} ")
    List<String> findAuthCodesByRankId(@Param("tn") String tn, @Param("rankId") String rankId,
            @Param("delFlag") int delFlag);

    // 查询职级拥有的权限ID
    @Select(" SELECT c_auth_id FROM ${tn} WHERE c_rank_id = #{rankId} and c_del_flag = #{delFlag} ")
    List<String> findAuthIdsByRankId(@Param("tn") String tn, @Param("rankId") String rankId,
            @Param("delFlag") int delFlag);

    // 根据关联关系查询 权限关系实体
    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE ${tAlias}.c_auth_id = #{authId} and ${tAlias}.c_rank_id = #{rankId} ")
    ZKSysAuthRank getRelationByAuthIdAndRankId(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("authId") String authId, @Param("rankId") String rankId);

    // 根据权限代码ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} ")
    int diskDelByAuthId(@Param("tn") String tn, @Param("authId") String authId);

    // 根据职级ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_rank_id = ${rankId} ")
    int diskDelByRankId(@Param("tn") String tn, @Param("rankId") String rankId);

    // 根据权限代码ID和职级ID 物理删除
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} and c_rank_id = ${rankId} ")
    int diskDelByAuthIdAndRankId(@Param("tn") String tn, @Param("authId") String authId,
            @Param("rankId") String rankId);
}