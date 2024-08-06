/**
 * 
 */
package com.zk.sys.org.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.security.utils.ZKSecPrincipalUtils;
import com.zk.sys.auth.service.ZKSysAuthRoleService;
import com.zk.sys.auth.service.ZKSysAuthUserRoleService;
import com.zk.sys.org.dao.ZKSysOrgRoleDao;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgDept;
import com.zk.sys.org.entity.ZKSysOrgRole;

/**
 * ZKSysOrgRoleService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKSysOrgRoleService extends ZKBaseService<String, ZKSysOrgRole, ZKSysOrgRoleDao> {

    @Autowired
    ZKSysOrgCompanyService sysOrgCompanyService;

    @Autowired
    ZKSysOrgDeptService sysOrgDeptService;

    @Autowired
    ZKSysAuthUserRoleService sysAuthUserRoleService;

    @Autowired
    ZKSysAuthRoleService sysAuthRoleService;

    /**
     * 根据角色代码取角色，公司下代码唯一；
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 8:33:11 AM
     * @param companyId
     * @param code
     * @return ZKSysOrgRank
     */
    public ZKSysOrgRole getByCode(String companyId, String code) {
        if (ZKStringUtils.isEmpty(code) || ZKStringUtils.isEmpty(companyId)) {
            return null;
        }
        return this.dao.getByCode(ZKSysOrgRole.sqlHelper().getTableName(), ZKSysOrgRole.sqlHelper().getTableAlias(),
            ZKSysOrgRole.sqlHelper().getBlockSqlCols(), companyId, code);
    }

    // 系统初始化
    @Transactional(readOnly = false)
    public int saveInitBySystem(ZKSysOrgRole role) {
        return super.save(role);
    }

    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysOrgRole role) {
        // 判断公司是否存及公司状态，存在，初始化公司值
        ZKSysOrgCompany company = this.sysOrgCompanyService.get(new ZKSysOrgCompany(role.getCompanyId()));
        if (company == null) {
            log.error("[^_^:20220425-0916-001] 公司[{}-{}]不存在;", role.getCompanyId(), role.getCompanyCode());
            throw ZKBusinessException.as("zk.sys.010003", "公司不存在");
        } else {
            if (company.getStatus() == null || company.getStatus().intValue() != ZKSysOrgCompany.KeyStatus.normal) {
                log.error("[^_^:20220425-0916-002] 公司[{}-{}]状态异常，请联系管理员;", company.getPkId(), company.getCode());
                throw ZKBusinessException.as("zk.sys.010004", "公司状态异常，请联系管理员");
            } else {
                // 初始化公司值
                role.setGroupCode(company.getGroupCode());
                role.setCompanyCode(company.getCode());
            }
        }

        // 判断是否为新增？新增时，判断角色代码是否唯一
        if (role.isNewRecord()) {
            // 根据公司及角色代码查询角色是否存在
            ZKSysOrgRole oldRole = this.getByCode(company.getPkId(), role.getCode());
            if (oldRole != null) {
                log.error("[>_<:20220425-0917-001] 公司[{}]下角色代码[{}]已存在；", company.getCode(), oldRole.getCode());
                Map<String, String> validatorMsg = Maps.newHashMap();
                validatorMsg.put("code", ZKMsgUtils.getMessage("zk.sys.010005", oldRole.getCode()));
                throw ZKValidatorException.as(validatorMsg);
            }
        }

        // 部门级角色，判断是否有填写部门
        if (role.getType().intValue() == ZKSysOrgRole.KeyType.dept) {
            if (ZKStringUtils.isEmpty(role.getDeptId())) {
                log.error("[>_<:20220427-1039-001] 新增公司的部门及角色[{}-{}]，请选择部门；", role.getCompanyCode(), role.getCode());
                Map<String, String> validatorMsg = Maps.newHashMap();
                validatorMsg.put("deptId", ZKMsgUtils.getMessage("zk.sys.010011"));
                throw ZKValidatorException.as(validatorMsg);
            }
            // 判断部门是否存在及部门状态； zk.sys.010007=部门状态异常，请联系管理员
            ZKSysOrgDept dept = this.sysOrgDeptService.get(new ZKSysOrgDept(role.getDeptId()));
            if (dept == null) {
                log.error("[^_^:20220425-0916-003] 部门[{}-{}]不存在;", role.getDeptId(), role.getDeptCode());
                throw ZKBusinessException.as("zk.sys.010006", "部门不存在");
            } else {
                if (dept.getStatus().intValue() != ZKSysOrgDept.KeyStatus.normal) {
                    log.error("[^_^:20220425-0916-004] 部门[{}-{}]状态异常，请联系管理员;", dept.getPkId(), dept.getCode());
                    throw ZKBusinessException.as("zk.sys.010007", "部门状态异常，请联系管理员");
                } else {
                    // 初始化部门值
                    role.setDeptId(dept.getPkId());
                    role.setDeptCode(dept.getCode());
                }
            }
        } else {
            role.setDeptId(null);
            role.setDeptCode(null);
        }
        return super.save(role);
    }

    @Override
    @Transactional(readOnly = false)
    public int del(ZKSysOrgRole role) {
        sysAuthUserRoleService.diskDelByRoleId(role.getPkId());
        sysAuthRoleService.diskDelByRoleId(role.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.del(role);
    }

    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKSysOrgRole role) {
        sysAuthUserRoleService.diskDelByRoleId(role.getPkId());
        sysAuthRoleService.diskDelByRoleId(role.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.diskDel(role);
    }

    // 根据公司ID做逻辑删除
    public int delByCompanyId(String companyId) {
        return this.dao.delByCompanyId(ZKSysOrgRole.sqlHelper().getTableName(), companyId, ZKBaseEntity.DEL_FLAG.delete,
                ZKSecPrincipalUtils.getSecPrincipalService().getUserId(), ZKDateUtils.getToday());
    }

    // 根据公司ID做物理删除
    public int diskDelByCompanyId(String companyId) {
        return this.dao.diskDelByCompanyId(ZKSysOrgRole.sqlHelper().getTableName(), companyId);
    }

}
