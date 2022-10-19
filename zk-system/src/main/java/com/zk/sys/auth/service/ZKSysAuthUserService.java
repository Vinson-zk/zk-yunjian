/**
 * 
 */
package com.zk.sys.auth.service;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKCodeException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.auth.dao.ZKSysAuthUserDao;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthFuncApi;
import com.zk.sys.auth.entity.ZKSysAuthUser;
import com.zk.sys.auth.entity.ZKSysAuthUserType;
import com.zk.sys.org.entity.ZKSysOrgUser;

/**
 * ZKSysAuthUserService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKSysAuthUserService extends ZKBaseService<String, ZKSysAuthUser, ZKSysAuthUserDao> {

    // 给职级分配权限
    @Transactional(readOnly = false)
    public ZKSysAuthUser save(ZKSysOrgUser user, ZKSysAuthDefined authDefined) {

        // 创建新关系
        ZKSysAuthUser newRelation = new ZKSysAuthUser();
        // 设置关系值
        newRelation.setAuthId(authDefined.getPkId());
        newRelation.setAuthCode(authDefined.getCode());
        newRelation.setUserId(user.getPkId());
        // 设置公司信息
        newRelation.setGroupCode(user.getGroupCode());
        newRelation.setCompanyId(user.getCompanyId());
        newRelation.setCompanyCode(user.getCompanyCode());

        // 先查询关联关系是否存在
        ZKSysAuthUser old = this.getRelationByAuthIdAndUserId(newRelation.getAuthId(), newRelation.getUserId());
        if (old != null) {
            if (ZKBaseEntity.DEL_FLAG.normal == old.getDelFlag().intValue()) {
                // 关系已存在；
                return old;
            }
            else {
                newRelation.setPkId(old.getPkId());
                newRelation.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
            }
        }
        super.save(newRelation);
        return newRelation;
    }

    // 给部门分配 权限关系
    @Transactional(readOnly = false)
    public List<ZKSysAuthUser> addRelationByUser(ZKSysOrgUser user, List<ZKSysAuthDefined> addAuths,
            List<ZKSysAuthDefined> delAuths) {
        if (user == null) {
            // zk.sys.010013=职级不存在
            log.error("[>_<:20220504-1212-002] zk.sys.010013=职级不存在");
            throw ZKCodeException.as("zk.sys.010013");
        }
        // 先删除 需要删除的关联关系
        if (delAuths == null) {
            this.diskDelByUserId(user.getPkId());
        }
        else {
            delAuths.forEach(item -> {
                this.diskDelByAuthIdAndUserId(item.getPkId(), user.getPkId());
            });
        }
        // 保存分配的关联关系
        if (addAuths != null && !addAuths.isEmpty()) {
            List<ZKSysAuthUser> res = new ArrayList<>();
            addAuths.forEach(item -> {
                res.add(this.save(user, item));
            });
            return res;
        }
        return Collections.emptyList();
    }

    // 查询用户拥有的权限代码
    public List<String> findAuthCodesByUserId(String userId) {
        if (ZKStringUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthCodesByUserId(ZKSysAuthUser.sqlHelper().getTableName(), userId,
                ZKSysAuthUser.DEL_FLAG.normal);
    }

    // 查询用户拥有的权限ID
    public List<String> findAuthIdsByUserId(String userId) {
        if (ZKStringUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthIdsByUserId(ZKSysAuthUser.sqlHelper().getTableName(), userId,
                ZKSysAuthUser.DEL_FLAG.normal);
    }

    // 用户直接拥有的 API 接口代码
    public List<String> findApiCodesByUserId(String userId) {
        if (ZKStringUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }
        return this.dao.findApiCodesByUserId(ZKSysAuthUser.sqlHelper().getTableName(),
                ZKSysAuthFuncApi.sqlHelper().getTableName(), userId, ZKSysAuthUserType.DEL_FLAG.normal);
    }

    // 根据关联关系查询 权限关系实体
    public ZKSysAuthUser getRelationByAuthIdAndUserId(String authId, String userId) {
        return this.dao.getRelationByAuthIdAndUserId(ZKSysAuthUser.sqlHelper().getTableName(),
                ZKSysAuthUser.sqlHelper().getTableAlias(), ZKSysAuthUser.sqlHelper().getBlockSqlCols(),
                authId, userId);
    }

    // 根据权限代码ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByAuthId(String authId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByAuthId(ZKSysAuthUser.sqlHelper().getTableName(), authId);
    }

    // 根据用户ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByUserId(String userId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByUserId(ZKSysAuthUser.sqlHelper().getTableName(), userId);
    }

    // 根据权限代码ID和用户ID 物理删除
    @Transactional(readOnly = false)
    public int diskDelByAuthIdAndUserId(String authId, String userId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByAuthIdAndUserId(ZKSysAuthUser.sqlHelper().getTableName(), authId, userId);
    }

	
	
}