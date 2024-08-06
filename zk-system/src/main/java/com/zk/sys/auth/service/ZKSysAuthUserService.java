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
import com.zk.sys.auth.dao.ZKSysAuthUserDao;
import com.zk.sys.auth.entity.ZKSysAuthCompany;
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
    public ZKSysAuthUser allotAuthToUser(ZKSysOrgUser user, ZKSysAuthDefined authDefined) {
        // 先查询关联关系是否存在
        ZKSysAuthUser old = this.getRelationByAuthIdAndUserId(authDefined.getPkId(), user.getPkId());
        ZKSysAuthUser optRelation;
        if (old == null) { // 新增授权
            // 创建新关系
            optRelation = new ZKSysAuthUser();
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
        optRelation.setUserId(user.getPkId());
        // 设置公司信息
        optRelation.setGroupCode(user.getGroupCode());
        optRelation.setCompanyId(user.getCompanyId());
        optRelation.setCompanyCode(user.getCompanyCode());

        super.save(optRelation);
        return optRelation;
    }

    // 给部门分配 权限关系
    @Transactional(readOnly = false)
    public List<ZKSysAuthUser> allotAuthToUser(ZKSysOrgUser user, List<ZKSysAuthDefined> allotAuths) {
        if (user == null) {
            // zk.sys.010013=职级不存在
            log.error("[>_<:20220504-1217-001] zk.sys.020009=用户不存在");
            throw ZKBusinessException.as("zk.sys.020009");
        }
        // 保存分配的关联关系
        if (allotAuths != null && !allotAuths.isEmpty()) {
            List<ZKSysAuthUser> res = new ArrayList<>();
            for (ZKSysAuthDefined item : allotAuths) {
                res.add(this.allotAuthToUser(user, item));
            }
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
    public List<String> findApiCodesByUserId(String userId, String systemCode) {
        if (ZKStringUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }
        return this.dao.findApiCodesByUserId(ZKSysAuthUser.sqlHelper().getTableName(),
                ZKSysAuthFuncApi.sqlHelper().getTableName(), userId, systemCode, ZKSysAuthUserType.DEL_FLAG.normal);
    }

    // 根据关联关系查询 权限关系实体
    public ZKSysAuthUser getRelationByAuthIdAndUserId(String authId, String userId) {
        return this.dao.getRelationByAuthIdAndUserId(ZKSysAuthUser.sqlHelper().getTableName(),
                ZKSysAuthUser.sqlHelper().getTableAlias(), ZKSysAuthUser.sqlHelper().getBlockSqlCols(),
                authId, userId);
    }

    // 查询给指定用户授权时，公司可分配的权限以及用户已拥有的权限
    public List<ZKSysAuthDefined> findAllotAuthByUser(ZKSysOrgUser user, String searchValue,
            ZKPage<ZKSysAuthDefined> page) {
        if (user == null) {
            log.error("[>_<:20220504-1217-002] zk.sys.020009=用户不存在");
            throw ZKBusinessException.as("zk.sys.020009");
        }
        return this.dao.findAllotAuthByUserId(ZKSysAuthDefined.sqlHelper().getTableName(),
                ZKSysAuthDefined.sqlHelper().getTableAlias(), ZKSysAuthDefined.sqlHelper().getBlockSqlCols(),
                ZKSysAuthCompany.sqlHelper().getTableName(), ZKSysAuthUser.sqlHelper().getTableName(),
                user.getCompanyId(), user.getPkId(), searchValue, page);
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