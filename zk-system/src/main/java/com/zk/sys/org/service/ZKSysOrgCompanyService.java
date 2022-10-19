/**
 * 
 */
package com.zk.sys.org.service;
 
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.service.ZKBaseTreeService;
import com.zk.core.exception.ZKCodeException;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.auth.service.ZKSysAuthCompanyService;
import com.zk.sys.entity.org.ZKSysOrgCompany;
import com.zk.sys.org.dao.ZKSysOrgCompanyDao;

/**
 * ZKSysOrgCompanyService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKSysOrgCompanyService extends ZKBaseTreeService<String, ZKSysOrgCompany, ZKSysOrgCompanyDao> {

    @Autowired
    ZKSysAuthCompanyService sysAuthCompanyService;

    @Autowired
    ZKSysOrgDeptService sysOrgDeptService;

    @Autowired
    ZKSysOrgUserService sysOrgUserService;

    @Autowired
    ZKSysOrgRoleService sysOrgRoleService;

    @Autowired
    ZKSysOrgRankService sysOrgRankService;

    @Autowired
    ZKSysOrgUserTypeService sysOrgUserTypeService;

	/**
     * 树形查询； 不分页
     */
    @Override
    public List<ZKSysOrgCompany> doFindTree(ZKSysOrgCompany sysOrgCompany) {
        return this.dao.findTree(sysOrgCompany);
    }

	/**
	 * 查询详情，包含父节点
	 */
    public ZKSysOrgCompany getDetail(ZKSysOrgCompany sysOrgCompany) {
        return this.dao.getDetail(sysOrgCompany);
    }

    // 新增/编辑公司一般信息，仅允许编辑公司和修改涉及 licence 相关的信息
    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysOrgCompany entity) {

        // === 新增公司时
        if (entity.isNewRecord()) {
            // 判断公司代码是存在
            ZKSysOrgCompany oldCompany = this.getByCode(entity.getCode());
            if (oldCompany != null) {
                // 公司代码已存在
                log.error("[>_<:20211114-2130-001] 公司代码已存在；code: {} ", entity.getCode());
                Map<String, String> validatorMsg = Maps.newHashMap();
                validatorMsg.put("code", ZKMsgUtils.getMessage("zk.sys.010001", entity.getCode()));
                throw ZKCodeException.asDataValidator(validatorMsg);
            }
            // 判断是否有父公司，有则处理集团代码保持一至；
            if (!ZKStringUtils.isEmpty(entity.getParentId())) {
                // 有父公司，查出父公司
                ZKSysOrgCompany parentCompany = this.get(new ZKSysOrgCompany(entity.getParentId()));
                if (parentCompany == null) {
                    log.error("[>_<:20220411-0956-001] 父公司不存在! parentId:{}", entity.getParentId());
                    throw ZKCodeException.as("zk.sys.010002", "父公司不存在");
                }
                else {
                    // 在这里可以添加父公司的一些校验，如父公司 licence 等
                    // 1、集团代码与父公司保持一至
                    entity.setGroupCode(parentCompany.getGroupCode());
                }
            }
            // 2、设置公司状态为审核中
            entity.setStatus(ZKSysOrgCompany.KeyStatus.auditIng);
        }
        return super.save(entity);
    }

    /**
     * 根据公司代码取公司
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 11, 2022 9:36:11 AM
     * @param code
     * @return
     * @return ZKSysOrgCompany
     */
    public ZKSysOrgCompany getByCode(String code) {
        if(ZKStringUtils.isEmpty(code)) {
            return null;
        }
        return this.dao.getByCode(ZKSysOrgCompany.sqlHelper().getTableName(),
                ZKSysOrgCompany.sqlHelper().getTableAlias(),
                ZKSysOrgCompany.sqlHelper().getBlockSqlCols(), code);
    }

    /**
     * 审核公司，通过
     *
     * @Title: approveCompany
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 5:03:57 PM
     * @param entity
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int approveCompany(ZKSysOrgCompany entity) {
        // 校验licence
        this.checkLicence();
        // 修改公司状态为通过
        return this.dao.approveCompany(ZKSysOrgCompany.sqlHelper().getTableName(), entity.getPkId(),
                ZKSysOrgCompany.KeyStatus.normal, ZKSecSecurityUtils.getUserId(), ZKDateUtils.getToday());
    }

    /**
     * 禁用公司，
     *
     * @Title: disabledCompany
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 5:04:03 PM
     * @param entity
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int disabledCompany(ZKSysOrgCompany entity) {
        // 校验licence
        this.checkLicence();
        // 修改公司状态为通过
        return this.dao.approveCompany(ZKSysOrgCompany.sqlHelper().getTableName(), entity.getPkId(),
                ZKSysOrgCompany.KeyStatus.disabled, ZKSecSecurityUtils.getUserId(), ZKDateUtils.getToday());
    }

    /**
     * 校验公司 Licence
     *
     * @Title: checkLicence
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 5:04:13 PM
     * @return void
     */
    public void checkLicence() {

    }

    @Override
    @Transactional(readOnly = false)
    public int del(ZKSysOrgCompany entity) {
        sysAuthCompanyService.diskDelByCompanyId(entity.getPkId());
        return super.del(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKSysOrgCompany entity) {
        sysAuthCompanyService.diskDelByCompanyId(entity.getPkId());
        return super.diskDel(entity);
    }
	
}