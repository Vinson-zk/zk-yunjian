/**
 * 
 */
package com.zk.sys.org.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.service.ZKBaseTreeService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.auth.service.ZKSysAuthDeptService;
import com.zk.sys.org.dao.ZKSysOrgDeptDao;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgDept;

/**
 * ZKSysOrgDeptService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKSysOrgDeptService extends ZKBaseTreeService<String, ZKSysOrgDept, ZKSysOrgDeptDao> {

    @Autowired
    ZKSysOrgCompanyService sysOrgCompanyService;

    @Autowired
    ZKSysAuthDeptService sysAuthDeptService;

    /**
     * 查询详情，包含父节点
     */
    public ZKSysOrgDept getDetail(ZKSysOrgDept sysOrgDept) {
        ZKSysOrgDept dept = this.dao.getDetail(sysOrgDept);
        if (dept != null) {
            dept.setCompany(this.sysOrgCompanyService.get(new ZKSysOrgCompany(dept.getCompanyId())));
        }
        return dept;
    }

    // 新增/编辑公司一般信息，仅允许编辑公司和修改涉及 licence 相关的信息
    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysOrgDept sysOrgDept) {
        // 判断公司是否存及公司状态，存在，初始部门公司值
        ZKSysOrgCompany company = this.sysOrgCompanyService.get(new ZKSysOrgCompany(sysOrgDept.getCompanyId()));
        if (company == null) {
            log.error("[^_^:20220420-0802-001] 公司[{}-{}]不存在;", sysOrgDept.getCompanyId(), sysOrgDept.getCompanyCode());
            throw ZKBusinessException.as("zk.sys.010003", "公司不存在");
        } else {
            if (company.getStatus() == null || company.getStatus().intValue() != ZKSysOrgCompany.KeyStatus.normal) {
                log.error("[^_^:20220420-0802-001] 公司[{}-{}]状态异常，请联系管理员;", company.getPkId(), company.getCode());
                throw ZKBusinessException.as("zk.sys.010004", "公司状态异常，请联系管理员");
            } else {
                // 初始化公司值
                sysOrgDept.setGroupCode(company.getGroupCode());
                sysOrgDept.setCompanyCode(company.getCode());
            }
        }
        // 判断是否为新增？新增时，判断部门代码是否唯一
        if (sysOrgDept.isNewRecord()) {
            // 根据公司及部门代码查询部门是否存在
            ZKSysOrgDept oldDept = this.getByCode(company.getPkId(), sysOrgDept.getCode());
            if (oldDept != null) {
                log.error("[>_<:2022-0420-001] 公司[{}]下部门代码[{}]已存在；", company.getCode(), sysOrgDept.getCode());
                Map<String, String> validatorMsg = Maps.newHashMap();
                validatorMsg.put("code", ZKMsgUtils.getMessage("zk.sys.010005", sysOrgDept.getCode()));
                throw ZKValidatorException.as(validatorMsg);
            }
        }
        return super.save(sysOrgDept);
    }

    /**
     * 根据部门代码取部门
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 20, 2022 8:49:54 AM
     * @param companyId
     * @param code
     * @return ZKSysOrgDept
     */
    public ZKSysOrgDept getByCode(String companyId, String code) {
        if (ZKStringUtils.isEmpty(code) || ZKStringUtils.isEmpty(companyId)) {
            return null;
        }
        return this.dao.getByCode(ZKSysOrgDept.sqlHelper().getTableName(), ZKSysOrgDept.sqlHelper().getTableAlias(),
            ZKSysOrgDept.sqlHelper().getBlockSqlCols(), companyId, code);
    }

    @Override
    @Transactional(readOnly = false)
    public int del(ZKSysOrgDept sysOrgDept) {
        sysAuthDeptService.diskDelByDeptId(sysOrgDept.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.del(sysOrgDept);
    }

    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKSysOrgDept sysOrgDept) {
        sysAuthDeptService.diskDelByDeptId(sysOrgDept.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.diskDel(sysOrgDept);
    }

}
