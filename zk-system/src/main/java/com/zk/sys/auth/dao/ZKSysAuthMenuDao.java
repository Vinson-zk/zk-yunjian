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
import com.zk.sys.auth.entity.ZKSysAuthMenu;

/**
 * ZKSysAuthMenuDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysAuthMenuDao extends ZKBaseDao<String, ZKSysAuthMenu> {

    /**
     * 查询权限拥有的菜单ID
     *
     * @Title: findMenuIdsByAuthId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 5, 2022 2:16:39 PM
     * @param tn
     * @param authId
     * @param delFlag
     * @return
     * @return List<String>
     */
    @Select(" SELECT c_menu_id FROM ${tn} WHERE c_auth_id = #{authId} and c_del_flag = #{delFlag} ")
    List<String> findMenuIdsByAuthId(@Param("tn") String tn, @Param("authId") String authId,
            @Param("delFlag") int delFlag);
    
    /**
     * 根据关联关系查询 权限关系实体
     *
     * @Title: getRelationByAuthIdAndMenuId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 5, 2022 2:16:42 PM
     * @param tn
     * @param tAlias
     * @param sCols
     * @param authId
     * @param menuId
     * @return
     * @return ZKSysAuthMenu
     */
    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE ${tAlias}.c_auth_id = #{authId} and ${tAlias}.c_menu_id = #{menuId} ")
    ZKSysAuthMenu getRelationByAuthIdAndMenuId(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("authId") String authId, @Param("menuId") String menuId);

    /**
     * 根据权限代码ID 删除关联关系
     *
     * @Title: diskDelByAuthId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 5, 2022 2:16:45 PM
     * @param tn
     * @param authId
     * @return
     * @return int
     */
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} ")
    int diskDelByAuthId(@Param("tn") String tn, @Param("authId") String authId);

    /**
     * 根据菜单ID 删除关联关系
     *
     * @Title: diskDelByMenuId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 5, 2022 2:16:49 PM
     * @param tn
     * @param menuId
     * @return
     * @return int
     */
    @Delete(" DELETE FROM ${tn} WHERE c_menu_id = ${menuId} ")
    int diskDelByMenuId(@Param("tn") String tn, @Param("menuId") String menuId);

    /**
     * 根据权限代码ID和菜单ID 物理删除
     *
     * @Title: diskDelByAuthIdAndMenuId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 5, 2022 2:16:52 PM
     * @param tn
     * @param authId
     * @param menuId
     * @return
     * @return int
     */
    @Delete(" DELETE FROM ${tn} WHERE c_auth_id = ${authId} and c_menu_id = ${menuId} ")
    int diskDelByAuthIdAndMenuId(@Param("tn") String tn, @Param("authId") String authId,
            @Param("menuId") String menuId);
}
