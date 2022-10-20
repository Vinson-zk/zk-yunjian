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
import com.zk.sys.auth.dao.ZKSysAuthDeptDao;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.entity.ZKSysAuthDept;
import com.zk.sys.auth.entity.ZKSysAuthFuncApi;
import com.zk.sys.auth.entity.ZKSysAuthUserType;
import com.zk.sys.org.entity.ZKSysOrgDept;

/**
 * ZKSysAuthDeptService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKSysAuthDeptService extends ZKBaseService<String, ZKSysAuthDept, ZKSysAuthDeptDao> {

    // 给部门分配权限代码
    @Transactional(readOnly = false)
    public ZKSysAuthDept save(ZKSysOrgDept dept, ZKSysAuthDefined authDefined) {

        // 创建新关系
        ZKSysAuthDept newRelation = new ZKSysAuthDept();
        // 设置关系值
        newRelation.setAuthId(authDefined.getPkId());
        newRelation.setAuthCode(authDefined.getCode());
        newRelation.setDeptId(dept.getPkId());
        newRelation.setDeptCode(dept.getCode());
        // 设置公司信息
        newRelation.setGroupCode(dept.getGroupCode());
        newRelation.setCompanyId(dept.getCompanyId());
        newRelation.setCompanyCode(dept.getCompanyCode());

        // 先查询关联关系是否存在
        ZKSysAuthDept old = this.getRelationByAuthIdAndDeptId(newRelation.getAuthId(), newRelation.getDeptId());
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
    public List<ZKSysAuthDept> addRelationByDept(ZKSysOrgDept dept, List<ZKSysAuthDefined> addAuths,
            List<ZKSysAuthDefined> delAuths) {
        if (dept == null) {
            // zk.sys.010006=部门不存在
            log.error("[>_<:20220504-1212-002] 部门不存在");
            throw ZKCodeException.as("zk.sys.010006");
        }
        // 先删除 需要删除的关联关系
        if (delAuths == null) {
            this.diskDelByDeptId(dept.getPkId());
        }
        else {
            delAuths.forEach(item -> {
                this.diskDelByAuthIdAndDeptId(item.getPkId(), dept.getPkId());
            });
        }
        // 保存分配的关联关系
        if (addAuths != null && !addAuths.isEmpty()) {
            List<ZKSysAuthDept> res = new ArrayList<>();
            addAuths.forEach(item -> {
                res.add(this.save(dept, item));
            });
            return res;
        }
        return Collections.emptyList();
    }

    // 查询部门拥有的权限代码
    public List<String> findAuthCodesByDeptId(String deptId) {
        if (ZKStringUtils.isEmpty(deptId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthCodesByDeptId(ZKSysAuthDept.sqlHelper().getTableName(), deptId,
                ZKSysAuthDept.DEL_FLAG.normal);
    }

    // 查询部门拥有的权限ID
    public List<String> findAuthIdsByDeptId(String deptId) {
        if (ZKStringUtils.isEmpty(deptId)) {
            return Collections.emptyList();
        }
        return this.dao.findAuthIdsByDeptId(ZKSysAuthDept.sqlHelper().getTableName(), deptId,
                ZKSysAuthDept.DEL_FLAG.normal);
    }

    // 通过部门拥有的 API 接口代码
    public List<String> findApiCodesByDeptId(String deptId) {
        if (ZKStringUtils.isEmpty(deptId)) {
            return Collections.emptyList();
        }
        return this.dao.findApiCodesByDeptId(ZKSysAuthDept.sqlHelper().getTableName(),
                ZKSysAuthFuncApi.sqlHelper().getTableName(), deptId, ZKSysAuthUserType.DEL_FLAG.normal);
    }

    // 根据关联关系查询 权限关系实体
    public ZKSysAuthDept getRelationByAuthIdAndDeptId(String authId, String deptId) {
        return this.dao.getRelationByAuthIdAndDeptId(ZKSysAuthDept.sqlHelper().getTableName(),
                ZKSysAuthDept.sqlHelper().getTableAlias(), ZKSysAuthDept.sqlHelper().getBlockSqlCols(),
                authId, deptId);
    }

    // 根据权限代码ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByAuthId(String authId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByAuthId(ZKSysAuthDept.sqlHelper().getTableName(), authId);
    }

    // 根据部门ID 删除关联关系
    @Transactional(readOnly = false)
    public int diskDelByDeptId(String deptId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByDeptId(ZKSysAuthDept.sqlHelper().getTableName(), deptId);
    }

    // 根据权限代码ID和部门ID 物理删除
    @Transactional(readOnly = false)
    public int diskDelByAuthIdAndDeptId(String authId, String deptId) {
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return this.dao.diskDelByAuthIdAndDeptId(ZKSysAuthDept.sqlHelper().getTableName(), authId, deptId);
    }
}
