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
import com.zk.sys.auth.dao.ZKSysAuthUserTypeDao;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthFuncApi;
import com.zk.sys.auth.entity.ZKSysAuthUserType;
import com.zk.sys.org.entity.ZKSysOrgUserType;

/**
 * ZKSysAuthUserTypeService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKSysAuthUserTypeService extends ZKBaseService<String, ZKSysAuthUserType, ZKSysAuthUserTypeDao> {

    // 给职级分配权限
    @Transactional(readOnly = false)
    public ZKSysAuthUserType save(ZKSysOrgUserType userType, ZKSysAuthDefined authDefined) {

        // 创建新关系
        ZKSysAuthUserType newRelation = new ZKSysAuthUserType();
        // 设置关系值
        newRelation.setAuthId(authDefined.getPkId());
        newRelation.setAuthCode(authDefined.getCode());
        newRelation.setUserTypeId(userType.getPkId());
        newRelation.setUserTypeCode(userType.getCode());
        // 设置公司信息
        newRelation.setGroupCode(userType.getGroupCode());
        newRelation.setCompanyId(userType.getCompanyId());
        newRelation.setCompanyCode(userType.getCompanyCode());

        // 先查询关联关系是否存在
        ZKSysAuthUserType old = this.getRelationByAuthIdAndUserTypeId(newRelation.getAuthId(),
                newRelation.getUserTypeId());
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
    public List<ZKSysAuthUserType> addRelationByUserType(ZKSysOrgUserType userType, List<ZKSysAuthDefined> addAuths,
            List<ZKSysAuthDefined> delAuths) {
        if (userType == null) {
            // zk.sys.010013=职级不存在
            log.error("[>_<:20220504-1212-002] zk.sys.010013=职级不存在");
            throw new ZKCodeException("zk.sys.010013");
        }
        // 先删除 需要删除的关联关系
        if (delAuths == null) {
            this.diskDelByUserTypeId(userType.getPkId());
        }
        else {
            delAuths.forEach(item -> {
                this.diskDelByAuthIdAndUserTypeId(item.getPkId(), userType.getPkId());
            });
        }
        // 保存分配的关联关系
        if (addAuths != null && !addAuths.isEmpty()) {
            List<ZKSysAuthUserType> res = new ArrayList<>();
            addAuths.forEach(item -> {
                res.add(this.save(userType, item));
            });
            return res;
        }
        return Collections.emptyList();
    }

    // 查询用户类型拥有的权限代码
    public List<String> findAuthCodesByUserTypeId(String userTypeId) {
        if (ZKStringUtils.isEmpty(userTypeId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthCodesByUserTypeId(ZKSysAuthUserType.initSqlProvider().getTableName(), userTypeId,
                ZKSysAuthUserType.DEL_FLAG.normal);
    }

    // 查询用户类型拥有的权限ID
    public List<String> findAuthIdsByUserTypeId(String userTypeId) {
        if (ZKStringUtils.isEmpty(userTypeId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthIdsByUserTypeId(ZKSysAuthUserType.initSqlProvider().getTableName(), userTypeId,
                ZKSysAuthUserType.DEL_FLAG.normal);
    }

    // 通过用户类型拥有的 API 接口代码
    public List<String> findApiCodesByUserTypeId(String userTypeId) {
        if (ZKStringUtils.isEmpty(userTypeId)) {
            return Collections.emptyList();
        }
        return this.dao.findApiCodesByUserTypeId(ZKSysAuthUserType.initSqlProvider().getTableName(),
                ZKSysAuthFuncApi.initSqlProvider().getTableName(), userTypeId, ZKSysAuthUserType.DEL_FLAG.normal);
    }

    // 根据关联关系查询 权限关系实体
    public ZKSysAuthUserType getRelationByAuthIdAndUserTypeId(String authId, String userTypeId) {
        return this.dao.getRelationByAuthIdAndUserTypeId(ZKSysAuthUserType.initSqlProvider().getTableName(),
                ZKSysAuthUserType.initSqlProvider().getTableAlias(),
                ZKSysAuthUserType.initSqlProvider().getSqlBlockSelCols(), authId, userTypeId);
    }

    // 根据权限代码ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByAuthId(String authId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByAuthId(ZKSysAuthUserType.initSqlProvider().getTableName(), authId);
    }

    // 根据 用户类型ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByUserTypeId(String userTypeId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByUserTypeId(ZKSysAuthUserType.initSqlProvider().getTableName(), userTypeId);
    }

    // 根据权限代码ID和用户类型ID 物理删除
    @Transactional(readOnly = false)
    public int diskDelByAuthIdAndUserTypeId(String authId, String userTypeId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByAuthIdAndUserTypeId(ZKSysAuthUserType.initSqlProvider().getTableName(), authId,
                userTypeId);
    }

	
	
}