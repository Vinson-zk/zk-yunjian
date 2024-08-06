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
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.auth.entity.ZKSysAuthUserRole;

/**
 * ZKSysAuthUserRoleDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthUserRoleDao extends ZKBaseDao<String, ZKSysAuthUserRole> {


    // 通过用户查询用户拥有的 API 接口代码
    @Select({ " SELECT distinct(t3.c_func_api_code)", "FROM ${userRoleTb} t1",
            "LEFT JOIN ${authRoleTb} t2 on t2.c_role_id = t1.c_role_id and t2.c_del_flag = #{delFlag}",
            "LEFT JOIN ${authApiTb} t3 on t3.c_auth_id = t2.c_auth_id and t3.c_del_flag = #{delFlag}",
            "WHERE t3.c_system_code = #{systemCode} and t1.c_user_id = #{userId} and t1.c_del_flag = #{delFlag} " })
    List<String> findApiCodesByUserId(@Param("userRoleTb") String userRoleTb, @Param("authRoleTb") String authRoleTb,
            @Param("authApiTb") String authApiTb, @Param("userId") String userId,
            @Param("systemCode") String systemCode, @Param("delFlag") int delFlag);

    // 查询用户通过角色拥有的权限代码
    @Select({ " SELECT distinct(t2.c_auth_code)", "FROM ${userRoleTb} t1",
            "LEFT JOIN ${authRoleTb} t2 on t2.c_role_id = t1.c_role_id and t2.c_del_flag = #{delFlag}",
            "WHERE t1.c_user_id = #{userId} and t1.c_del_flag = #{delFlag} " })
    List<String> findAuthCodesByUserId(@Param("userRoleTb") String userRoleTb, @Param("authRoleTb") String authRoleTb,
            @Param("userId") String userId, @Param("delFlag") int delFlag);

    // 查询用户通过角色拥有的权限id
    @Select({ " SELECT distinct(t2.c_auth_id)", "FROM ${userRoleTb} t1",
            "LEFT JOIN ${authRoleTb} t2 on t2.c_role_id = t1.c_role_id and t2.c_del_flag = #{delFlag}",
            "WHERE t1.c_user_id = #{userId} and t1.c_del_flag = #{delFlag} " })
    List<String> findAuthIdsByUserId(@Param("userRoleTb") String userRoleTb, @Param("authRoleTb") String authRoleTb,
            @Param("userId") String userId, @Param("delFlag") int delFlag);

    // 查询用户拥有的角色代码
    @Select(" SELECT distinct(${tAlias}.c_role_code) FROM ${tn} ${tAlias} WHERE ${tAlias}.c_user_id = #{userId} and ${tAlias}.c_del_flag = #{delFlag} ")
    Set<String> findRoleCodesByUserId(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("userId") String userId, @Param("delFlag") int delFlag);

    /**
     * 查询用户拥有的角色ID
     *
     * @Title: findRoleIdsByUserId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 4, 2022 2:37:18 PM
     * @param tn
     * @param tAlias
     * @param userId
     * @param delFlag
     * @return
     * @return List<String>
     */
    @Select(" SELECT distinct(${tAlias}.c_role_id) FROM ${tn} ${tAlias} WHERE ${tAlias}.c_user_id = #{userId} and ${tAlias}.c_del_flag = #{delFlag} ")
    List<String> findRoleIdsByUserId(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("userId") String userId, @Param("delFlag") int delFlag);

    /**
     * 根据用户和角色ID查询
     *
     * @Title: getRelationByUserIdAndRoleId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 4, 2022 11:02:53 AM
     * @param tn
     * @param tAlias
     * @param sCols
     * @param userId
     * @param roleId
     * @return
     * @return ZKSysAuthUserRole
     */
    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE ${tAlias}.c_user_id = #{userId} and ${tAlias}.c_role_id = #{roleId} ")
    ZKSysAuthUserRole getRelationByUserIdAndRoleId(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("userId") String userId, @Param("roleId") String roleId);

    /**
     * 根据用户ID 删除关联关系
     *
     * @Title: diskDelByUserId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 4, 2022 11:13:41 AM
     * @param tn
     * @param userId
     * @return
     * @return int
     */
    @Delete(" DELETE FROM ${tn} WHERE c_user_id = ${userId} ")
    int diskDelByUserId(@Param("tn") String tn, @Param("userId") String userId);

    /**
     * 根据角色ID 删除关联关系
     *
     * @Title: diskDelByRoleId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 4, 2022 11:13:59 AM
     * @param tn
     * @param roleId
     * @return
     * @return int
     */
    @Delete(" DELETE FROM ${tn} WHERE c_role_id = ${roleId} ")
    int diskDelByRoleId(@Param("tn") String tn, @Param("roleId") String roleId);

    /**
     * 根据用户ID和角色ID 物理删除
     *
     * @Title: diskDelByUserIdAndRoleId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 4, 2022 11:15:14 AM
     * @param tn
     * @param userId
     * @param roleId
     * @return
     * @return int
     */
    @Delete(" DELETE FROM ${tn} WHERE c_user_id = ${userId} and c_role_id = ${roleId} ")
    int diskDelByUserIdAndRoleId(@Param("tn") String tn, @Param("userId") String userId,
            @Param("roleId") String roleId);
	
}