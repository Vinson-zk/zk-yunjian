/**
 * 
 */
package com.zk.sys.org.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.auth.service.ZKSysAuthUserTypeService;
import com.zk.sys.org.dao.ZKSysOrgUserTypeDao;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgUserType;

/**
 * ZKSysOrgUserTypeService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKSysOrgUserTypeService extends ZKBaseService<String, ZKSysOrgUserType, ZKSysOrgUserTypeDao> {

    @Autowired
    ZKSysOrgCompanyService sysOrgCompanyService;

    @Autowired
    ZKSysAuthUserTypeService sysAuthUserTypeService;

    /**
     * 根据用户类型代码取用户，公司下代码唯一；
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 8:33:11 AM
     * @param companyId
     * @param code
     * @return ZKSysOrgRank
     */
    public ZKSysOrgUserType getByCode(String companyId, String code) {
        if (ZKStringUtils.isEmpty(code) || ZKStringUtils.isEmpty(companyId)) {
            return null;
        }
        return this.dao.getByCode(ZKSysOrgUserType.sqlHelper().getTableName(),
            ZKSysOrgUserType.sqlHelper().getTableAlias(), ZKSysOrgUserType.sqlHelper().getBlockSqlCols(), companyId,
            code);
    }

    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysOrgUserType userType) {
        // 判断公司是否存及公司状态，存在，初始化公司值
        ZKSysOrgCompany company = this.sysOrgCompanyService.get(new ZKSysOrgCompany(userType.getCompanyId()));
        if (company == null) {
            log.error("[^_^:20220425-0914-001] 公司[{}-{}]不存在;", userType.getCompanyId(), userType.getCompanyCode());
            throw ZKBusinessException.as("zk.sys.010003", "公司不存在");
        } else {
            if (company.getStatus() == null || company.getStatus().intValue() != ZKSysOrgCompany.KeyStatus.normal) {
                log.error("[^_^:20220425-0914-001] 公司[{}-{}]状态异常，请联系管理员;", company.getPkId(), company.getCode());
                throw ZKBusinessException.as("zk.sys.010004", "公司状态异常，请联系管理员");
            } else {
                // 初始化公司值
                userType.setGroupCode(company.getGroupCode());
                userType.setCompanyCode(company.getCode());
            }
        }
        // 判断是否为新增？新增时，判断用户类型代码是否唯一
        if (userType.isNewRecord()) {
            // 根据公司及用户类型代码查询用户类型是否存在
            ZKSysOrgUserType oldUserType = this.getByCode(company.getPkId(), userType.getCode());
            if (oldUserType != null) {
                log.error("[>_<:20220425-0915-001] 公司[{}]下用户类型代码[{}]已存在；", company.getCode(), oldUserType.getCode());
                Map<String, String> validatorMsg = Maps.newHashMap();
                validatorMsg.put("code", ZKMsgUtils.getMessage("zk.sys.010005", oldUserType.getCode()));
                throw ZKValidatorException.as(validatorMsg);
            }
        }
        return super.save(userType);
    }

    @Override
    @Transactional(readOnly = false)
    public int del(ZKSysOrgUserType userType) {
        sysAuthUserTypeService.diskDelByUserTypeId(userType.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.del(userType);
    }

    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKSysOrgUserType userType) {
        sysAuthUserTypeService.diskDelByUserTypeId(userType.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.diskDel(userType);
    }
}
