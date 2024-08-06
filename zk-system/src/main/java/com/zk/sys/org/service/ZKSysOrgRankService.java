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
import com.zk.sys.auth.service.ZKSysAuthRankService;
import com.zk.sys.org.dao.ZKSysOrgRankDao;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgRank;

/**
 * ZKSysOrgRankService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKSysOrgRankService extends ZKBaseService<String, ZKSysOrgRank, ZKSysOrgRankDao> {

    @Autowired
    ZKSysOrgCompanyService sysOrgCompanyService;

    @Autowired
    ZKSysAuthRankService sysAuthRankService;

    /**
     * 根据职级代码取职级，公司下代码唯一；
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 8:33:11 AM
     * @param companyId
     * @param code
     * @return ZKSysOrgRank
     */
    public ZKSysOrgRank getByCode(String companyId, String code) {
        if (ZKStringUtils.isEmpty(code) || ZKStringUtils.isEmpty(companyId)) {
            return null;
        }
        return this.dao.getByCode(ZKSysOrgRank.sqlHelper().getTableName(), ZKSysOrgRank.sqlHelper().getTableAlias(),
            ZKSysOrgRank.sqlHelper().getBlockSqlCols(), companyId, code);
    }

    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysOrgRank rank) {
        // 判断公司是否存及公司状态，存在，初始化公司值
        ZKSysOrgCompany company = this.sysOrgCompanyService.get(new ZKSysOrgCompany(rank.getCompanyId()));
        if (company == null) {
            log.error("[^_^:20220425-0918-001] 公司[{}-{}]不存在;", rank.getCompanyId(), rank.getCompanyCode());
            throw ZKBusinessException.as("zk.sys.010003", "公司不存在");
        } else {
            if (company.getStatus() == null || company.getStatus().intValue() != ZKSysOrgCompany.KeyStatus.normal) {
                log.error("[^_^:20220425-0918-002] 公司[{}-{}]状态异常，请联系管理员;", company.getPkId(), company.getCode());
                throw ZKBusinessException.as("zk.sys.010004", "公司状态异常，请联系管理员");
            } else {
                // 初始化公司值
                rank.setGroupCode(company.getGroupCode());
                rank.setCompanyCode(company.getCode());
            }
        }
        // 判断是否为新增？新增时，判断职级代码是否唯一
        if (rank.isNewRecord()) {
            // 根据公司及职级代码查询职级是否存在
            ZKSysOrgRank oldRank = this.getByCode(company.getPkId(), rank.getCode());
            if (oldRank != null) {
                log.error("[>_<:20220425-0919-001] 公司[{}]下职级代码[{}]已存在；", company.getCode(), oldRank.getCode());
                Map<String, String> validatorMsg = Maps.newHashMap();
                validatorMsg.put("code", ZKMsgUtils.getMessage("zk.sys.010005", oldRank.getCode()));
                throw ZKValidatorException.as(validatorMsg);
            }
        }
        return super.save(rank);
    }

    @Override
    @Transactional(readOnly = false)
    public int del(ZKSysOrgRank rank) {
        sysAuthRankService.diskDelByRankId(rank.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.del(rank);
    }

    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKSysOrgRank rank) {
        sysAuthRankService.diskDelByRankId(rank.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAllAuth();
        return super.diskDel(rank);
    }

}
