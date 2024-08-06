/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.sys.auth.dao.ZKSysAuthMenuDao;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthMenu;
import com.zk.sys.res.entity.ZKSysMenu;

/**
 * ZKSysAuthMenuService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKSysAuthMenuService extends ZKBaseService<String, ZKSysAuthMenu, ZKSysAuthMenuDao> {

    // 给权限代码分配菜单
    @Transactional(readOnly = false)
    public ZKSysAuthMenu save(ZKSysAuthDefined authDefined, ZKSysMenu menu) {

        // 创建新关系
        ZKSysAuthMenu newRelation = new ZKSysAuthMenu();
        // 设置关系值
        newRelation.setAuthId(authDefined.getPkId());
        newRelation.setMenuId(menu.getPkId());
        newRelation.setMenuCode(menu.getCode());

        // 先查询关联关系是否存在
        ZKSysAuthMenu old = this.getRelationByAuthIdAndMenuId(newRelation.getAuthId(), newRelation.getMenuId());
        if (old != null) {
            if (ZKSysAuthMenu.DEL_FLAG.normal == old.getDelFlag().intValue()) {
                // 关系已存在；
                return old;
            }
            else {
                newRelation.setPkId(old.getPkId());
                // newRelation.setDelFlag(ZKSysAuthMenu.DEL_FLAG.normal);
                super.restore(newRelation);
            }
        }
        super.save(newRelation);
        return newRelation;
    }

    // 给权限分配 菜单关系
    @Transactional(readOnly = false)
    public List<ZKSysAuthMenu> addRelationByAuthDefined(ZKSysAuthDefined authDefined, List<ZKSysMenu> addMenus,
            List<ZKSysMenu> delMenus) {
        if (authDefined == null) {
            // zk.sys.020011=权限不存在
            log.error("[>_<:20220504-1112-001] 权限不存在");
            throw ZKBusinessException.as("zk.sys.020011", "权限不存在");
        }
        // 先删除 需要删除的关联关系
        if (delMenus == null) {
            this.diskDelByAuthId(authDefined.getPkId());
        }
        else {
            delMenus.forEach(item -> {
                this.diskDelByAuthIdAndMenuId(authDefined.getPkId(), item.getPkId());
            });
        }
        // 保存分配的关联关系
        if (addMenus != null && !addMenus.isEmpty()) {
            List<ZKSysAuthMenu> res = new ArrayList<>();
            addMenus.forEach(item -> {
                res.add(this.save(authDefined, item));
            });
            return res;
        }
        return Collections.emptyList();
    }

    /**
     * 查询用户拥有的角色ID
     *
     * @Title: findMenuIdsByAuthId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 5, 2022 11:25:29 AM
     * @param authId
     * @return
     * @return List<String>
     */
    public List<String> findMenuIdsByAuthId(String authId) {
        if (ZKStringUtils.isEmpty(authId)) {
            return Collections.emptyList();
        }
        return this.dao.findMenuIdsByAuthId(ZKSysAuthMenu.sqlHelper().getTableName(), authId,
                ZKSysAuthMenu.DEL_FLAG.normal);
    }

    /**
     * 根据关联关系查询 权限关系实体
     *
     * @Title: getRelationByAuthIdAndMenuId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 5, 2022 10:11:54 AM
     * @param authId
     * @param menuId
     * @return
     * @return ZKSysAuthMenu
     */
    public ZKSysAuthMenu getRelationByAuthIdAndMenuId(String authId, String menuId) {
        if (ZKStringUtils.isEmpty(authId) || ZKStringUtils.isEmpty(menuId)) {
            return null;
        }
        return this.dao.getRelationByAuthIdAndMenuId(ZKSysAuthMenu.sqlHelper().getTableName(),
                ZKSysAuthMenu.sqlHelper().getTableAlias(), ZKSysAuthMenu.sqlHelper().getBlockSqlCols(),
                authId, menuId);
    }

    // 根据 权限ID数组查询 菜单列表
    public List<ZKSysMenu> findMenuByAuthIds(String navCode, List<String> authIds, Integer isShow) {
        if (authIds == null || authIds.isEmpty()) {
            return Collections.emptyList();
        }
        return this.dao.findMenuByAuthIds(ZKSysMenu.sqlHelper().getTableName(), ZKSysMenu.sqlHelper().getTableAlias(),
                ZKSysMenu.sqlHelper().getBlockSqlCols(), ZKSysAuthMenu.sqlHelper().getTableName(), navCode, authIds,
                isShow);
    }

    /**
     * 根据权限代码ID 删除关联关系
     *
     * @Title: diskDelByAuthId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 5, 2022 11:08:59 AM
     * @param authId
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int diskDelByAuthId(String authId) {
        return this.dao.diskDelByAuthId(ZKSysAuthMenu.sqlHelper().getTableName(), authId);
    }

    /**
     * 根据菜单ID 删除关联关系
     *
     * @Title: diskDelByMenuId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 5, 2022 11:09:03 AM
     * @param menuId
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int diskDelByMenuId(String menuId) {
        return this.dao.diskDelByMenuId(ZKSysAuthMenu.sqlHelper().getTableName(), menuId);
    }

    /**
     * 根据权限代码ID和菜单ID 物理删除
     *
     * @Title: diskDelByAuthIdAndMenuId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 5, 2022 11:09:07 AM
     * @param authId
     * @param menuId
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int diskDelByAuthIdAndMenuId(String authId, String menuId) {
        return this.dao.diskDelByAuthIdAndMenuId(ZKSysAuthMenu.sqlHelper().getTableName(), authId, menuId);
    }


}