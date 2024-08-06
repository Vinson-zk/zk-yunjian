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
import com.zk.sys.auth.dao.ZKSysAuthNavDao;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthNav;
import com.zk.sys.res.entity.ZKSysNav;

/**
 * ZKSysAuthNavService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKSysAuthNavService extends ZKBaseService<String, ZKSysAuthNav, ZKSysAuthNavDao> {

    // 给权限代码分配导航栏目
    @Transactional(readOnly = false)
    public ZKSysAuthNav save(ZKSysAuthDefined authDefined, ZKSysNav nav) {

        // 创建新关系
        ZKSysAuthNav newRelation = new ZKSysAuthNav();
        // 设置关系值
        newRelation.setAuthId(authDefined.getPkId());
        newRelation.setNavId(nav.getPkId());
        newRelation.setNavCode(nav.getCode());

        // 先查询关联关系是否存在
        ZKSysAuthNav old = this.getRelationByAuthIdAndNavId(newRelation.getAuthId(), newRelation.getNavId());
        if (old != null) {
            if (ZKSysAuthNav.DEL_FLAG.normal == old.getDelFlag().intValue()) {
                // 关系已存在；
                return old;
            }
            else {
                newRelation.setPkId(old.getPkId());
                // newRelation.setDelFlag(ZKSysAuthNav.DEL_FLAG.normal);
                super.restore(newRelation);
            }
        }
        super.save(newRelation);
        return newRelation;
    }

    // 给权限分配 导航栏目关系
    @Transactional(readOnly = false)
    public List<ZKSysAuthNav> addRelationByAuthDefined(ZKSysAuthDefined authDefined, List<ZKSysNav> addNavs,
            List<ZKSysNav> delNavs) {
        if (authDefined == null) {
            // zk.sys.020011=权限不存在
            log.error("[>_<:20220504-1112-001] 权限不存在");
            throw ZKBusinessException.as("zk.sys.020011", "权限不存在");
        }
        // 先删除 需要删除的关联关系
        if (delNavs == null) {
            this.diskDelByAuthId(authDefined.getPkId());
        }
        else {
            delNavs.forEach(item -> {
                this.diskDelByAuthIdAndNavId(authDefined.getPkId(), item.getPkId());
            });
        }
        // 保存分配的关联关系
        if (addNavs != null && !addNavs.isEmpty()) {
            List<ZKSysAuthNav> res = new ArrayList<>();
            addNavs.forEach(item -> {
                res.add(this.save(authDefined, item));
            });
            return res;
        }
        return Collections.emptyList();
    }

    // 根据关联关系查询 权限关系实体
    public List<String> findNavIdsByAuthId(String authId) {
        if (ZKStringUtils.isEmpty(authId)) {
            return Collections.emptyList();
        }
        return this.dao.findNavIdsByAuthId(ZKSysAuthNav.sqlHelper().getTableName(), authId,
                ZKSysAuthNav.DEL_FLAG.normal);
    }

    // 根据关联关系查询 权限关系实体
    public ZKSysAuthNav getRelationByAuthIdAndNavId(String authId, String navId) {
        if (ZKStringUtils.isEmpty(authId) || ZKStringUtils.isEmpty(navId)) {
            return null;
        }
        return this.dao.getRelationByAuthIdAndNavId(ZKSysAuthNav.sqlHelper().getTableName(),
                ZKSysAuthNav.sqlHelper().getTableAlias(), ZKSysAuthNav.sqlHelper().getBlockSqlCols(),
                authId, navId);
    }

    // 根据 权限ID数组查询 导航栏列表
    public List<ZKSysNav> findNavByAuthIds(List<String> authIds, Integer isShow) {
        if (authIds == null || authIds.isEmpty()) {
            return Collections.emptyList();
        }
        return this.dao.findNavByAuthIds(ZKSysNav.sqlHelper().getTableName(), ZKSysNav.sqlHelper().getTableAlias(),
                ZKSysNav.sqlHelper().getBlockSqlCols(), ZKSysAuthNav.sqlHelper().getTableName(), authIds, isShow);
    }

    // 根据权限代码ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByAuthId(String authId) {
        return this.dao.diskDelByAuthId(ZKSysAuthNav.sqlHelper().getTableName(), authId);
    }

    // 根据导航栏目ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByNavId(String navId) {
        return this.dao.diskDelByNavId(ZKSysAuthNav.sqlHelper().getTableName(), navId);
    }

    // 根据权限代码ID和导航栏目ID 物理删除
    @Transactional(readOnly = false)
    public int diskDelByAuthIdAndNavId(String authId, String navId) {
        return this.dao.diskDelByAuthIdAndNavId(ZKSysAuthNav.sqlHelper().getTableName(), authId, navId);
    }
	
	
}