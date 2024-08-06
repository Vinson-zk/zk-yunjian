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
import com.zk.sys.auth.dao.ZKSysAuthDeptDao;
import com.zk.sys.auth.entity.ZKSysAuthCompany;
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
    public ZKSysAuthDept allotAuthToDept(ZKSysOrgDept dept, ZKSysAuthDefined authDefined) {

        // 先查询关联关系是否存在
        ZKSysAuthDept old = this.getRelationByAuthIdAndDeptId(authDefined.getPkId(), dept.getPkId());
        ZKSysAuthDept optRelation;
        if (old == null) { // 新增授权
            // 创建新关系
            optRelation = new ZKSysAuthDept();
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
        optRelation.setDeptId(dept.getPkId());
        optRelation.setDeptCode(dept.getCode());
        // 设置公司信息
        optRelation.setGroupCode(dept.getGroupCode());
        optRelation.setCompanyId(dept.getCompanyId());
        optRelation.setCompanyCode(dept.getCompanyCode());

        super.save(optRelation);
        return optRelation;
    }

    // 给部门分配 权限关系
    @Transactional(readOnly = false)
    public List<ZKSysAuthDept> allotAuthToDept(ZKSysOrgDept dept, List<ZKSysAuthDefined> allotAuths) {
        if (dept == null) {
            // zk.sys.010006=部门不存在
            log.error("[>_<:20220504-1215-001] 部门不存在");
            throw ZKBusinessException.as("zk.sys.010006");
        }
        // 保存分配的关联关系
        if (allotAuths != null && !allotAuths.isEmpty()) {
            List<ZKSysAuthDept> res = new ArrayList<>();
            for (ZKSysAuthDefined item : allotAuths) {
                res.add(this.allotAuthToDept(dept, item));
            }
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
    public List<String> findApiCodesByDeptId(String deptId, String systemCode) {
        if (ZKStringUtils.isEmpty(deptId)) {
            return Collections.emptyList();
        }
        return this.dao.findApiCodesByDeptId(ZKSysAuthDept.sqlHelper().getTableName(),
                ZKSysAuthFuncApi.sqlHelper().getTableName(), deptId, systemCode, ZKSysAuthUserType.DEL_FLAG.normal);
    }

    // 根据关联关系查询 权限关系实体
    public ZKSysAuthDept getRelationByAuthIdAndDeptId(String authId, String deptId) {
        return this.dao.getRelationByAuthIdAndDeptId(ZKSysAuthDept.sqlHelper().getTableName(),
                ZKSysAuthDept.sqlHelper().getTableAlias(), ZKSysAuthDept.sqlHelper().getBlockSqlCols(),
                authId, deptId);
    }

    // 查询给指定部门授权时，公司可分配的权限以及部门已拥有的权限
    public List<ZKSysAuthDefined> findAllotAuthByDept(ZKSysOrgDept dept, String searchValue,
            ZKPage<ZKSysAuthDefined> page) {
        if (dept == null) {
            log.error("[>_<:20220504-1215-002] zk.sys.010006 部门不存在");
            throw ZKBusinessException.as("zk.sys.010006");
        }
        return this.dao.findAllotAuthByDeptId(ZKSysAuthDefined.sqlHelper().getTableName(),
                ZKSysAuthDefined.sqlHelper().getTableAlias(), ZKSysAuthDefined.sqlHelper().getBlockSqlCols(),
                ZKSysAuthCompany.sqlHelper().getTableName(), ZKSysAuthDept.sqlHelper().getTableName(),
                dept.getCompanyId(), dept.getPkId(), searchValue, page);
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
