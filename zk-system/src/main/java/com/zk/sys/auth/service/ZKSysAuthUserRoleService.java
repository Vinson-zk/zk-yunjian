/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.auth.dao.ZKSysAuthUserRoleDao;
import com.zk.sys.auth.entity.ZKSysAuthFuncApi;
import com.zk.sys.auth.entity.ZKSysAuthRole;
import com.zk.sys.auth.entity.ZKSysAuthUserRole;
import com.zk.sys.org.entity.ZKSysOrgRole;
import com.zk.sys.org.entity.ZKSysOrgUser;

/**
 * ZKSysAuthUserRoleService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKSysAuthUserRoleService extends ZKBaseService<String, ZKSysAuthUserRole, ZKSysAuthUserRoleDao> {

    /**
     * 根据关系查询用户与角色关系
     *
     * @Title: getRelationByUserIdAndRoleId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 4, 2022 8:42:55 AM
     * @param roleId
     * @param userId
     * @return
     * @return ZKSysAuthUserRole
     */
    public ZKSysAuthUserRole getRelationByUserIdAndRoleId(String userId, String roleId) {
        if (ZKStringUtils.isEmpty(userId) || ZKStringUtils.isEmpty(roleId)) {
            return null;
        }
        return this.dao.getRelationByUserIdAndRoleId(ZKSysAuthUserRole.sqlHelper().getTableName(),
                ZKSysAuthUserRole.sqlHelper().getTableAlias(),
                ZKSysAuthUserRole.sqlHelper().getBlockSqlCols(), userId, roleId);
    }

    @Transactional(readOnly = false)
    public ZKSysAuthUserRole save(ZKSysOrgUser user, ZKSysOrgRole role) {
        if (user.getStatus().intValue() != ZKSysOrgUser.KeyStatus.normal) {
            // 用户状态异常
            log.error("[>_<:20220504-0855-001] 用户 [{}] 状态异常", user.getPkId());
            throw ZKBusinessException.as("zk.sys.020006");
        }

        if (role.getStatus().intValue() != ZKSysOrgRole.KeyStatus.normal) {
            // 角色状态异常
            log.error("[>_<:20220504-0855-001] 角色 [{}] 状态异常", role.getPkId());
            throw ZKBusinessException.as("zk.sys.020007");
        }

        if (!user.getCompanyId().equals(role.getCompanyId())) {
            // 用户和角色是同一个公司
            log.error("[>_<:20220504-0855-001] 用户[{}]和角色[{}] 不是同一个公司", user.getPkId(), role.getPkId());
            throw ZKBusinessException.as("zk.sys.020008");
        }

        // 创建新关系
        ZKSysAuthUserRole newRelation = new ZKSysAuthUserRole();
        // 设置关系值
        newRelation.setUserId(user.getPkId());
        newRelation.setRoleId(role.getPkId());
        newRelation.setRoleCode(role.getCode());
        // 设置关系公司信息
        newRelation.setGroupCode(user.getGroupCode());
        newRelation.setCompanyId(user.getCompanyId());
        newRelation.setCompanyCode(user.getCompanyCode());

        // 先查询关联关系是否存在
        ZKSysAuthUserRole old = this.getRelationByUserIdAndRoleId(newRelation.getUserId(), newRelation.getRoleId());
        if (old != null) {
            if (ZKSysAuthUserRole.DEL_FLAG.normal == old.getDelFlag().intValue()) {
                // 关系已存在；
                return old;
            }
            else {
                newRelation.setPkId(old.getPkId());
                // newRelation.setDelFlag(ZKSysAuthUserRole.DEL_FLAG.normal);
                super.restore(newRelation);
            }
        }
        super.save(newRelation);
        return newRelation;
    }

    /**
     * 给用户添加、删除角色
     *
     * @Title: addRelationByUser
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 4, 2022 11:11:06 AM
     * @param user
     * @param addRoles
     * @param delRoles
     * @return
     * @return List<ZKSysResFuncApiReqChannel>
     */
    @Transactional(readOnly = false)
    public List<ZKSysAuthUserRole> addRelationByUser(ZKSysOrgUser user, List<ZKSysOrgRole> addRoles,
            List<ZKSysOrgRole> delRoles) {
        if (user == null) {
            // zk.sys.020009=用户不存在
            log.error("[>_<:20220504-1111-001] 用户不存在!");
            throw ZKBusinessException.as("zk.sys.020009", "用户不存在");
        }
        // 先删除 需要删除的关联关系
        if (delRoles == null) {
            this.diskDelByUserId(user.getPkId());
        }
        else {
            delRoles.forEach(item -> {
                this.diskDelByUserIdAndRoleId(user.getPkId(), item.getPkId());
            });
        }
        // 保存分配的关联关系
        if (addRoles != null && !addRoles.isEmpty()) {
            List<ZKSysAuthUserRole> res = new ArrayList<>();
            addRoles.forEach(item -> {
                res.add(this.save(user, item));
            });
            return res;
        }
        return Collections.emptyList();
    }

    /**
     * 查询用户拥有的角色ID
     *
     * @Title: findRoleIdsByUserId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 4, 2022 2:37:12 PM
     * @param userId
     * @return
     * @return List<String>
     */
    public List<String> findRoleIdsByUserId(String userId) {
        if (ZKStringUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }
        return this.dao.findRoleIdsByUserId(ZKSysAuthUserRole.sqlHelper().getTableName(),
                ZKSysAuthUserRole.sqlHelper().getTableAlias(), userId, ZKSysAuthUserRole.DEL_FLAG.normal);
    }

    /**
     * 根据用户ID 删除关联关系
     *
     * @Title: diskDelByUserId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 4, 2022 11:09:46 AM
     * @param userId
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int diskDelByUserId(String userId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByUserId(ZKSysAuthUserRole.sqlHelper().getTableName(), userId);
    }

    /**
     * 根据角色ID 删除关联关系
     *
     * @Title: diskDelByRoleId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 4, 2022 11:10:00 AM
     * @param roleId
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int diskDelByRoleId(String roleId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByRoleId(ZKSysAuthUserRole.sqlHelper().getTableName(), roleId);
    }

    /**
     * 根据用户ID和角色ID 物理删除
     *
     * @Title: diskDelByUserIdAndRoleId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 4, 2022 11:16:35 AM
     * @param userId
     * @param roleId
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int diskDelByUserIdAndRoleId(String userId, String roleId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByUserIdAndRoleId(ZKSysAuthUserRole.sqlHelper().getTableName(), userId, roleId);
    }

    // 取用户的角色代码
    public Set<String> findRoleCodesByUserId(String userId) {
        return this.findRoleCodesByUserId(userId, ZKBaseEntity.DEL_FLAG.normal);
    }

    public Set<String> findRoleCodesByUserId(String userId, Integer delFlag) {
        return this.dao.findRoleCodesByUserId(ZKSysAuthUserRole.sqlHelper().getTableName(),
                ZKSysAuthUserRole.sqlHelper().getTableAlias(), userId, delFlag);
    }

    // 查询用户通过角色拥有的权限代码
    public List<String> findAuthCodesByUserId(String userId) {
        if (ZKStringUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthCodesByUserId(ZKSysAuthUserRole.sqlHelper().getTableName(),
                ZKSysAuthRole.sqlHelper().getTableName(), userId, ZKBaseEntity.DEL_FLAG.normal);
    }

    // 查询用户通过角色拥有的权限id
    public List<String> findAuthIdsByUserId(String userId) {
        if (ZKStringUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthIdsByUserId(ZKSysAuthUserRole.sqlHelper().getTableName(),
                ZKSysAuthRole.sqlHelper().getTableName(), userId, ZKBaseEntity.DEL_FLAG.normal);
    }

    // 用户通过角色取拥有的 API 接口代码
    public List<String> findApiCodesByUserId(String userId, String systemCode) {
        if (ZKStringUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }
        return this.dao.findApiCodesByUserId(ZKSysAuthUserRole.sqlHelper().getTableName(),
                ZKSysAuthRole.sqlHelper().getTableName(), ZKSysAuthFuncApi.sqlHelper().getTableName(),
                userId, systemCode, ZKBaseEntity.DEL_FLAG.normal);
    }
	
}
