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
import com.zk.sys.auth.dao.ZKSysAuthUserTypeDao;
import com.zk.sys.auth.entity.ZKSysAuthCompany;
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
    public ZKSysAuthUserType allotAuthToUserType(ZKSysOrgUserType userType, ZKSysAuthDefined authDefined) {
        // 先查询关联关系是否存在
        ZKSysAuthUserType old = this.getRelationByAuthIdAndUserTypeId(authDefined.getPkId(), userType.getPkId());
        ZKSysAuthUserType optRelation;
        if (old == null) { // 新增授权
            // 创建新关系
            optRelation = new ZKSysAuthUserType();
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
        optRelation.setUserTypeId(userType.getPkId());
        optRelation.setUserTypeCode(userType.getCode());
        // 设置公司信息
        optRelation.setGroupCode(userType.getGroupCode());
        optRelation.setCompanyId(userType.getCompanyId());
        optRelation.setCompanyCode(userType.getCompanyCode());

        super.save(optRelation);
        return optRelation;
    }

    // 给部门分配 权限关系
    @Transactional(readOnly = false)
    public List<ZKSysAuthUserType> allotAuthToUserType(ZKSysOrgUserType userType, List<ZKSysAuthDefined> allotAuths) {
        if (userType == null) {
            // zk.sys.010013=职级不存在
            log.error("[>_<:20220504-1216-001] zk.sys.010014=用户类型不存在");
            throw ZKBusinessException.as("zk.sys.010014");
        }
        // 保存分配的关联关系
        if (allotAuths != null && !allotAuths.isEmpty()) {
            List<ZKSysAuthUserType> res = new ArrayList<>();
            for (ZKSysAuthDefined item : allotAuths) {
                res.add(this.allotAuthToUserType(userType, item));
            }
            return res;
        }
        return Collections.emptyList();
    }

    // 查询用户类型拥有的权限代码
    public List<String> findAuthCodesByUserTypeId(String userTypeId) {
        if (ZKStringUtils.isEmpty(userTypeId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthCodesByUserTypeId(ZKSysAuthUserType.sqlHelper().getTableName(), userTypeId,
                ZKSysAuthUserType.DEL_FLAG.normal);
    }

    // 查询用户类型拥有的权限ID
    public List<String> findAuthIdsByUserTypeId(String userTypeId) {
        if (ZKStringUtils.isEmpty(userTypeId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthIdsByUserTypeId(ZKSysAuthUserType.sqlHelper().getTableName(), userTypeId,
                ZKSysAuthUserType.DEL_FLAG.normal);
    }

    // 通过用户类型拥有的 API 接口代码
    public List<String> findApiCodesByUserTypeId(String userTypeId, String systemCode) {
        if (ZKStringUtils.isEmpty(userTypeId)) {
            return Collections.emptyList();
        }
        return this.dao.findApiCodesByUserTypeId(ZKSysAuthUserType.sqlHelper().getTableName(),
                ZKSysAuthFuncApi.sqlHelper().getTableName(), userTypeId, systemCode, ZKSysAuthUserType.DEL_FLAG.normal);
    }

    // 根据关联关系查询 权限关系实体
    public ZKSysAuthUserType getRelationByAuthIdAndUserTypeId(String authId, String userTypeId) {
        return this.dao.getRelationByAuthIdAndUserTypeId(ZKSysAuthUserType.sqlHelper().getTableName(),
                ZKSysAuthUserType.sqlHelper().getTableAlias(),
                ZKSysAuthUserType.sqlHelper().getBlockSqlCols(), authId, userTypeId);
    }

    // 查询给指定用户类型授权时，公司可分配的权限以及用户类型已拥有的权限
    public List<ZKSysAuthDefined> findAllotAuthByUserType(ZKSysOrgUserType userType, String searchValue,
            ZKPage<ZKSysAuthDefined> page) {
        if (userType == null) {
            log.error("[>_<:20220504-1216-002] zk.sys.010014=用户类型不存在");
            throw ZKBusinessException.as("zk.sys.010014");
        }
        return this.dao.findAllotAuthByUserTypeId(ZKSysAuthDefined.sqlHelper().getTableName(),
                ZKSysAuthDefined.sqlHelper().getTableAlias(), ZKSysAuthDefined.sqlHelper().getBlockSqlCols(),
                ZKSysAuthCompany.sqlHelper().getTableName(), ZKSysAuthUserType.sqlHelper().getTableName(),
                userType.getCompanyId(), userType.getPkId(), searchValue, page);
    }

    // 根据权限代码ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByAuthId(String authId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByAuthId(ZKSysAuthUserType.sqlHelper().getTableName(), authId);
    }

    // 根据 用户类型ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByUserTypeId(String userTypeId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByUserTypeId(ZKSysAuthUserType.sqlHelper().getTableName(), userTypeId);
    }

    // 根据权限代码ID和用户类型ID 物理删除
    @Transactional(readOnly = false)
    public int diskDelByAuthIdAndUserTypeId(String authId, String userTypeId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByAuthIdAndUserTypeId(ZKSysAuthUserType.sqlHelper().getTableName(), authId,
                userTypeId);
    }

	
	
}