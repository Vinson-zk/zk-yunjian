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
import com.zk.sys.auth.entity.ZKSysAuthNav;
import com.zk.sys.res.entity.ZKSysNav;

/**
 * ZKSysAuthNavDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthNavDao extends ZKBaseDao<String, ZKSysAuthNav> {

    // 查询权限拥有的导航栏目ID
    @Select(" SELECT distinct(c_nav_id) FROM ${tn} WHERE c_auth_id = #{authId} and c_del_flag = #{delFlag} ")
    List<String> findNavIdsByAuthId(@Param("tn") String tn, @Param("authId") String authId,
            @Param("delFlag") int delFlag);

    // 根据关联关系查询 权限关系实体
    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE ${tAlias}.c_auth_id = #{authId} and ${tAlias}.c_nav_id = #{navId} ")
    ZKSysAuthNav getRelationByAuthIdAndNavId(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("authId") String authId, @Param("navId") String navId);

    // 根据权限代码ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} ")
    int diskDelByAuthId(@Param("tn") String tn, @Param("authId") String authId);

    // 根据菜单ID 删除关联关系
    @Delete(" DELETE FROM ${tn} WHERE c_nav_id = ${navId} ")
    int diskDelByNavId(@Param("tn") String tn, @Param("navId") String navId);

    // 根据权限代码ID和菜单ID 物理删除
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} and c_nav_id = ${navId} ")
    int diskDelByAuthIdAndNavId(@Param("tn") String tn, @Param("authId") String authId, @Param("navId") String navId);

    // 根据 权限ID数组查询 导航栏列表
    @Select({ "<script>", //
            "SELECT ${navSelCols} ", //
            "FROM ${navTn} ${navAlias} ", //
            "WHERE ${navAlias}.c_del_flag = 0 ", //
            "   <if test='isShow != null' >", //
            "       AND ${navAlias}.c_is_show = #{isShow}", //
            "   </if>", //
            "   AND ${navAlias}.c_pk_id in (", //
            "       SELECT distinct(t.c_nav_id) FROM ${authNavTn} t WHERE t.c_del_flag = 0 AND t.c_auth_id in ", //
            "           <foreach item=\"_v\" index=\"index\" collection=\"authIds\" open=\"(\" separator=\",\" close=\")\">#{_v}</foreach>", //
            "   )", //
            " ORDER BY ${navAlias}.c_sort ASC, ${navAlias}.c_create_date DESC ", //
            "</script>" })
    List<ZKSysNav> findNavByAuthIds(@Param("navTn") String navTn, @Param("navAlias") String navAlias,
            @Param("navSelCols") String navSelCols, @Param("authNavTn") String authNavTn,
            @Param("authIds") List<String> authIds, @Param("isShow") Integer isShow);
	
}