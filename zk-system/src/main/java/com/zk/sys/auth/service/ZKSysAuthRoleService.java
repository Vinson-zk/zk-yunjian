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
import com.zk.core.commons.data.ZKPage;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.auth.dao.ZKSysAuthRoleDao;
import com.zk.sys.auth.entity.ZKSysAuthCompany;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthRole;
import com.zk.sys.org.entity.ZKSysOrgRole;

/**
 * ZKSysAuthRoleService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKSysAuthRoleService extends ZKBaseService<String, ZKSysAuthRole, ZKSysAuthRoleDao> {

    /**
     * 给职级分配权限
     *
     * @Title: save
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:52:45 AM
     * @param role
     * @param authDefined
     * @return
     * @return ZKSysAuthRole
     */
    @Transactional(readOnly = false)
    public ZKSysAuthRole allotAuthToRole(ZKSysOrgRole role, ZKSysAuthDefined authDefined) {
        // 先查询关联关系是否存在
        ZKSysAuthRole old = this.getRelationByAuthIdAndRoleId(authDefined.getPkId(), role.getPkId());
        ZKSysAuthRole optRelation;
        if (old == null) { // 新增授权
            // 创建新关系
            optRelation = new ZKSysAuthRole();
        }
        else { // 修改或删除授权
            if (authDefined.getAuthRelation() != null && authDefined.getAuthRelation().isDel()) {
                // 删除授权
                super.del(old);
                return old;
            }
            else {
                if (old.isDel()) {
                    // 恢复授权
                    super.restore(old);
                }
                optRelation = old;
            }
        }
        // 设置关系值
        optRelation.setAuthId(authDefined.getPkId());
        optRelation.setAuthCode(authDefined.getCode());
        optRelation.setRoleId(role.getPkId());
        optRelation.setRoleCode(role.getCode());
        // 设置公司信息
        optRelation.setGroupCode(role.getGroupCode());
        optRelation.setCompanyId(role.getCompanyId());
        optRelation.setCompanyCode(role.getCompanyCode());

        super.save(optRelation);
        return optRelation;
    }

    /**
     * 给部门分配 权限关系
     *
     * @Title: addRelationByRole
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:52:42 AM
     * @param role
     * @param addAuths
     * @param delAuths
     * @return
     * @return List<ZKSysAuthRole>
     */
    @Transactional(readOnly = false)
    public List<ZKSysAuthRole> allotAuthToRole(ZKSysOrgRole role, List<ZKSysAuthDefined> allotAuths) {
        if (role == null) {
            // zk.sys.010013=职级不存在
            log.error("[>_<:20220504-1213-001] zk.sys.010015=角色不存在");
            throw ZKBusinessException.as("zk.sys.010015");
        }
        // 保存分配的关联关系
        if (allotAuths != null && !allotAuths.isEmpty()) {
            List<ZKSysAuthRole> res = new ArrayList<>();
            for (ZKSysAuthDefined item : allotAuths) {
                res.add(this.allotAuthToRole(role, item));
            }
            return res;
        }
        return Collections.emptyList();
    }

    // 查询角色拥有的权限代码
    public List<String> findAuthCodesByRoleId(String roleId) {
        if (ZKStringUtils.isEmpty(roleId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthCodesByRoleId(ZKSysAuthRole.sqlHelper().getTableName(), roleId,
                ZKSysAuthRole.DEL_FLAG.normal);
    }

    /**
     * 查询角色拥有的权限ID
     *
     * @Title: findAuthIdsByRoleId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:52:38 AM
     * @param roleId
     * @return
     * @return List<String>
     */
    public List<String> findAuthIdsByRoleId(String roleId) {
        if (ZKStringUtils.isEmpty(roleId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthIdsByRoleId(ZKSysAuthRole.sqlHelper().getTableName(), roleId,
                ZKSysAuthRole.DEL_FLAG.normal);
    }

    /**
     * 根据关联关系查询 权限关系实体
     *
     * @Title: getRelationByAuthIdAndRoleId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:52:35 AM
     * @param authId
     * @param roleId
     * @return
     * @return ZKSysAuthRole
     */
    public ZKSysAuthRole getRelationByAuthIdAndRoleId(String authId, String roleId) {
        return this.dao.getRelationByAuthIdAndRoleId(ZKSysAuthRole.sqlHelper().getTableName(),
                ZKSysAuthRole.sqlHelper().getTableAlias(), ZKSysAuthRole.sqlHelper().getBlockSqlCols(),
                authId, roleId);
    }

    // 查询给指定角色授权时，公司可分配的权限以及角色已拥有的权限
    public List<ZKSysAuthDefined> findAllotAuthByRole(ZKSysOrgRole role, String searchValue,
            ZKPage<ZKSysAuthDefined> page) {
        if (role == null) {
            log.error("[>_<:20220504-1213-002] zk.sys.010015=角色不存在");
            throw ZKBusinessException.as("zk.sys.010015");
        }
        return this.dao.findAllotAuthByRoleId(ZKSysAuthDefined.sqlHelper().getTableName(),
                ZKSysAuthDefined.sqlHelper().getTableAlias(), ZKSysAuthDefined.sqlHelper().getBlockSqlCols(),
                ZKSysAuthCompany.sqlHelper().getTableName(), ZKSysAuthRole.sqlHelper().getTableName(),
                role.getCompanyId(), role.getPkId(), searchValue, page);
    }

    /**
     * 根据权限代码ID 删除关联关系
     *
     * @Title: diskDelByAuthId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:52:33 AM
     * @param authId
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int diskDelByAuthId(String authId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByAuthId(ZKSysAuthRole.sqlHelper().getTableName(), authId);
    }

    /**
     * 根据角色ID 删除关联关系
     *
     * @Title: diskDelByRoleId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:52:29 AM
     * @param roleId
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int diskDelByRoleId(String roleId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByRoleId(ZKSysAuthRole.sqlHelper().getTableName(), roleId);
    }

    /**
     * 根据权限代码ID和角色ID 物理删除
     *
     * @Title: diskDelByAuthIdAndRoleId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:52:24 AM
     * @param authId
     * @param roleId
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int diskDelByAuthIdAndRoleId(String authId, String roleId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByAuthIdAndRoleId(ZKSysAuthRole.sqlHelper().getTableName(), authId, roleId);
    }
	
}