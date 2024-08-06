/**
 * 
 */
package com.zk.sys.org.service;
 
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseTreeService;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.auth.entity.ZKSysAuthCompany;
import com.zk.sys.auth.service.ZKSysAuthCompanyService;
import com.zk.sys.org.dao.ZKSysOrgCompanyDao;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgRole;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.entity.ZKSysOrgUserEditLog.ZKUserEditFlag;
import com.zk.sys.res.entity.ZKSysResDict;
import com.zk.sys.res.service.ZKSysResDictService;
import com.zk.sys.sec.common.ZKSysSecConstants;
import com.zk.sys.utils.ZKSysUtils;

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

    @Autowired
    ZKSysResDictService sysResDictService;

    /********************************************************/
    /** 公司 的一些修改 ****/
    /********************************************************/

    // 修改需要审核的内容
    @Transactional(readOnly = false)
    public int updateAuditContent(ZKSysOrgCompany company) {
        if (ZKStringUtils.isEmpty(company.getPkId())) {
            return 0;
        }
        
        // 判断法人证件字典是否存在
//        ZKSysResDict legalCertType = sysResDictService.getByTypeCodeAndDictCode(ZKSysUtils.getDictTypeCertType(),
//                company.getLegalCertType());
        ZKSysResDict legalCertType = sysResDictService.get(company.getLegalCertType());
        if (legalCertType == null || legalCertType.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.delete) {
            log.error("[>_<:20240729-0011-001] zk.sys.010026=证件类型[{}]不存在", company.getLegalCertType());
            throw ZKBusinessException.as("zk.sys.010026", null, company.getLegalCertType());
        }
        // 判断公司证件字典是否存在
//        ZKSysResDict companyCertType = sysResDictService
//                .getByTypeCodeAndDictCode(ZKSysUtils.getDictTypeCompanyCertType(), company.getCompanyCertType());
        ZKSysResDict companyCertType = sysResDictService.get(company.getCompanyCertType());
        if (companyCertType == null || companyCertType.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.delete) {
            log.error("[>_<:20240729-0011-001] zk.sys.010027=公司证件类型[{}]不存在", company.getCompanyCertType());
            throw ZKBusinessException.as("zk.sys.010027", null, company.getCompanyCertType());
        }

        company.setStatus(ZKSysOrgCompany.KeyStatus.waitSubmit);
        company.setStatus(company.statusNext());
        return this.dao.updateAuditContent(ZKSysOrgCompany.sqlHelper().getTableName(), company);
    }

    // 编辑公司
    @Transactional(readOnly = false)
    public int editCompany(ZKSysOrgCompany company, String pwd) {
        boolean isNewReacord = company.isNewRecord();
        int count = this.save(company);
        if (count > 0 && isNewReacord) {
            if (ZKStringUtils.isEmpty(pwd)) {
                pwd = ZKSysUtils.getUserDefaultPwd();
            }
            // 新增公司，初始化公司
            this.initCompanyCreateAdminUser(company, pwd);
            // 2、分配公司默认权限
            this.initCompanyAuth(company);
            // 3、创建 superAdmin 角色
            this.initCompanyCreateAdminRole(company);
        }
        return count;
    }

    /**
     * 新增/编辑公司 基础信息
     * 
     * @param entity
     * @return
     * @see com.zk.base.service.ZKBaseService#save(com.zk.base.entity.ZKBaseEntity)
     */
    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysOrgCompany entity) {
        // === 新增公司时
        if (entity.isNewRecord()) {
            // 判断公司代码是存在
            this.checkUniqueCode(entity);
            // 1. 判断是否有父公司，有则处理集团代码保持一至；
            if (ZKStringUtils.isEmpty(entity.getParentId())) {
                // 一级公司，判断集团代码是否存在
                this.checkUniqueGroupCode(entity);
            }
            else {
                // 有父公司，查出父公司
                ZKSysOrgCompany parentCompany = this.get(new ZKSysOrgCompany(entity.getParentId()));
                if (parentCompany == null) {
                    log.error("[>_<:20220411-0956-001] 父公司不存在! parentId:{}", entity.getParentId());
                    throw ZKBusinessException.as("zk.sys.010002");
                }
                else {
                    // 在这里可以添加父公司的一些校验，如父公司 licence 等
                    // 1、集团代码与父公司保持一至
                    entity.setGroupCode(parentCompany.getGroupCode());
                }
            }
        }
        return super.save(entity);
    }

    // 1、创建 admin 用户
    @Transactional(readOnly = false)
    protected ZKSysOrgUser initCompanyCreateAdminUser(ZKSysOrgCompany company, String pwd) {
        ZKSysOrgUser adminUser = new ZKSysOrgUser();
        adminUser.setGroupCode(company.getGroupCode());
        adminUser.setCompanyCode(company.getCode());
        adminUser.setCompanyId(company.getPkId());
        adminUser.setAccount(ZKSysSecConstants.KeyAuth.adminAccount);
        adminUser.setNickname(ZKSysSecConstants.KeyAuth.adminAccount);
        adminUser.setMail(company.getMail());
        adminUser.setPhoneNum(company.getPhoneNum());
        adminUser.setSex(ZKSysUtils.getSexUnknownDictCode()); // 字典项值
        adminUser.setStatus(ZKSysOrgUser.KeyStatus.normal);
        this.sysOrgUserService.save(adminUser);
        this.sysOrgUserService.updatePwd(adminUser.getPkId(), pwd, ZKUserEditFlag.Pwd.self);
        return adminUser;
    }
    // 2、分配公司默认权限
    @Transactional(readOnly = false)
    protected int initCompanyAuth(ZKSysOrgCompany company) {
        // 找出权限来源公司，顶级公司时，从拥有者公司那里获得权限
        String fromCompanyId = company.getParentId();
        if(ZKStringUtils.isEmpty(fromCompanyId)) {
            ZKSysOrgCompany ownerCompany = this.getByCode(ZKSysUtils.getOwnerCompanyCode());
            if(ownerCompany == null) {
                log.error("[^_^:20220724-2301-001] 拥有者公司[{}]不存在;", ZKSysUtils.getOwnerCompanyCode());
                throw ZKBusinessException.as("zk.sys.010003", ZKSysUtils.getOwnerCompanyCode());
            }
            fromCompanyId = ownerCompany.getPkId();
        }
        
        ZKSysAuthCompany ac = new ZKSysAuthCompany();
        ac.setCompanyId(fromCompanyId);
        // 完全拥有
        ac.setOwnerType(ZKSysAuthCompany.KeyOwnerType.all);
        // 默认传递给子公司
        ac.setDefaultToChild(ZKSysAuthCompany.KeyDefaultToChild.transfer);
        // 未删除
        ac.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
        // 一次处理 999 条
        ZKPage<ZKSysAuthCompany> page = ZKPage.asPage();
        page.setPageSize(999);
        
        int count = 0;
        do {
            page = this.sysAuthCompanyService.findPage(page, ac);
            for (ZKSysAuthCompany item : page.getResult()) {
                // 改变主键，变为新实体，改变公司ID和公司代码；不改变权限授权的拥有方式和默认给子公司的方式；
                item.setPkId(null);
                item.setCompanyId(company.getPkId());
                item.setCompanyCode(company.getCode());
                count += this.sysAuthCompanyService.save(item);
            }
            page.setPageNo(page.getPageNo() + 1);
        } while (!page.getResult().isEmpty());
        return count;
    }
    // 3、创建 superAdmin 角色
    @Transactional(readOnly = false)
    protected ZKSysOrgRole initCompanyCreateAdminRole(ZKSysOrgCompany company) {
        ZKSysOrgRole adminRole = new ZKSysOrgRole();
        adminRole.setGroupCode(company.getGroupCode());
        adminRole.setCompanyCode(company.getCode());
        adminRole.setCompanyId(company.getPkId());
        adminRole.setType(ZKSysOrgRole.KeyType.company);
        adminRole.setStatus(ZKSysOrgRole.KeyStatus.normal);
        adminRole.setCode(ZKSysSecConstants.KeyAuth.adminRoleCode);
        // zk.sys.msg.role.name.superAdmin "{\"en-US\": \"superAdmin\", \"zh-CN\": \"超级管理员\"}"
        adminRole.setName(ZKJson.parse("{}"));
        Locale locale = ZKLocaleUtils.valueOf("zh-CN");
        adminRole.getName().put(locale.toLanguageTag(),
                ZKMsgUtils.getMessage(locale, "zk.sys.msg.role.name.superAdmin"));
        locale = ZKLocaleUtils.valueOf("en-US");
        adminRole.getName().put(locale.toLanguageTag(),
                ZKMsgUtils.getMessage(locale, "zk.sys.msg.role.name.superAdmin"));
        this.sysOrgRoleService.saveInitBySystem(adminRole);
        return adminRole;
    }

    /**
     * 审核公司
     *
     * @Title: auditCompany
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 12, 2022 4:46:23 PM
     * 
     * @param currentCompanyId
     *            当前登录公司
     * 
     * @param companyId
     *            审核的目标公司ID
     * @param flag
     *            审核的标识；0-通过审核；其他禁用
     * @return void
     */
    @Transactional(readOnly = false)
    public void auditCompany(String currentCompanyId, String companyId, int flag) {

        ZKSysOrgCompany company = this.get(new ZKSysOrgCompany(companyId));
        if (company == null) {
            log.error("[>_<:20220412-1648-001] 公司不存在! companyId:{}", companyId);
            throw ZKBusinessException.as("zk.sys.010003");
        }

        if (ZKStringUtils.isEmpty(company.getParentId())) {
            ZKSysOrgCompany currentCompany = this.get(new ZKSysOrgCompany(currentCompanyId));
            if (currentCompany == null) {
                log.error("[>_<:20220412-1648-002] 公司不存在! currentCompanyId:{}", currentCompanyId);
                throw ZKBusinessException.as("zk.sys.010003");
            }
            // 取平台拥有者公司代码
            String ownerCode = ZKSysUtils.getOwnerCompanyCode();
            if (!currentCompany.getCode().equals(ownerCode)) {
                log.error(
                        "[>_<:20220412-1648-003] zk.sys.010019=非平台拥有者，不能审核平台公司 currentCompanyCode:{}, companyCode: {}",
                        currentCompany.getCode(), company.getCode());
                throw ZKBusinessException.as("zk.sys.010019");
            }
        }
        else {
            // 只可审核本公司的子公司
            if (!company.getParentId().equals(currentCompanyId)) {
                log.error("[>_<:20220412-1648-004] zk.sys.010018=非本公司子公司，不能审核 currentCompanyId:{}, companyCode: {}",
                        currentCompanyId, company.getCode());
                throw ZKBusinessException.as("zk.sys.010018");
            }
        }

        if (flag == 0) {
            // 审核通过
            this.approveCompany(company);
        }
        else {
            // 审核不通过
            this.prevCompany(company);
        }

    }

    /**
     * 审核公司，通过
     *
     * @Title: approveCompany
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 5:03:57 PM
     * @param entity
     * @return int
     */
    @Transactional(readOnly = false)
    public int approveCompany(ZKSysOrgCompany company) {
        // 校验licence
        this.checkLicence();
        company.setStatus(company.statusNext());
        // 修改公司状态为通过
        int result = this.dao.approveCompany(ZKSysOrgCompany.sqlHelper().getTableName(), company.getPkId(),
                company.getStatus(), ZKSecSecurityUtils.getUserId(), ZKDateUtils.getToday());

//        if (result == 1 && company.getStatus().intValue() == ZKSysOrgCompany.KeyStatus.normal) {
//            
//        }

        return result;
    }

    /**
     * 前一状态
     *
     * @Title: disabledCompany
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 5:04:03 PM
     * @param entity
     * @return int
     */
    @Transactional(readOnly = false)
    public int prevCompany(ZKSysOrgCompany entity) {
        // 校验licence
//        this.checkLicence();
        entity.setStatus(entity.statusPrev());
        // 修改公司状态为通过
        return this.dao.approveCompany(ZKSysOrgCompany.sqlHelper().getTableName(), entity.getPkId(),
                entity.getStatus(), ZKSecSecurityUtils.getUserId(), ZKDateUtils.getToday());
    }

    /**
     * 禁用公司，
     *
     * @Title: disabledCompany
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 5:04:03 PM
     * @param entity
     * @return int
     */
    @Transactional(readOnly = false)
    public int disabledCompany(ZKSysOrgCompany entity) {
        // 校验licence
//        this.checkLicence();
        // 修改公司状态为通过
        return this.dao.approveCompany(ZKSysOrgCompany.sqlHelper().getTableName(), entity.getPkId(),
                ZKSysOrgCompany.KeyStatus.disabled, ZKSecSecurityUtils.getUserId(), ZKDateUtils.getToday());
    }

    /********************************************************/
    /** 公司 的一些其他方法 ****/
    /********************************************************/
    // 判断集团代码是否存在; 一级公司注册时需要；
    public boolean checkUniqueGroupCode(ZKSysOrgCompany company) {
        if (!ZKStringUtils.isEmpty(company.getParentId())) {
            return true;
        }
        if (ZKStringUtils.isEmpty(company.getGroupCode())) {
            return false;
        }
        ZKSysOrgCompany oldCompany = this.getByGroupCode(company.getGroupCode());
        if (oldCompany != null) {
            log.error("[>_<:20240630-2151-001] 集团代码已存在:{}", company.getGroupCode());
            Map<String, String> validatorMsg = Maps.newHashMap();
            validatorMsg.put("groupCode", ZKMsgUtils.getMessage("zk.sys.010016", company.getGroupCode()));
            throw ZKValidatorException.as(validatorMsg);
        }
        return true;
    }

    // 判断公司代码是存在
    public boolean checkUniqueCode(ZKSysOrgCompany company) {
        if (ZKStringUtils.isEmpty(company.getCode())) {
            return false;
        }
        ZKSysOrgCompany oldCompany = this.getByCode(company.getCode());
        if (oldCompany != null) {
            // 公司代码已存在
            log.error("[>_<:20211114-2130-001] 公司代码已存在；code: {} ", company.getCode());
            Map<String, String> validatorMsg = Maps.newHashMap();
            validatorMsg.put("code", ZKMsgUtils.getMessage("zk.sys.010001", company.getCode()));
            throw ZKValidatorException.as(validatorMsg);
        }
        return true;
    }

    /********************************************************/
    /** 公司 的一些查询 ****/
    /********************************************************/

    /**
     * 查询详情，包含父节点
     */
    public ZKSysOrgCompany getDetail(ZKSysOrgCompany sysOrgCompany) {
        return this.dao.getDetail(sysOrgCompany);
    }

    public ZKSysOrgCompany getByGroupCode(String groupCode) {
        if (ZKStringUtils.isEmpty(groupCode)) {
            return null;
        }
        ZKSysOrgCompany oc = new ZKSysOrgCompany();
        oc.setDelFlag(null);
        oc.setGroupCode(groupCode);
        oc.setParentId(null);
        oc.setParentIdIsEmpty(true);

        List<ZKSysOrgCompany> ocs = this.dao.findList(oc);
        if (ocs.isEmpty()) {
            return null;
        }
        else {
            return ocs.get(0);
        }
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
        if (ZKStringUtils.isEmpty(code)) {
            return null;
        }
        return this.dao.getByCode(ZKSysOrgCompany.sqlHelper().getTableName(),
                ZKSysOrgCompany.sqlHelper().getTableAlias(), ZKSysOrgCompany.sqlHelper().getBlockSqlCols(), code);
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

    /********************************************************/
    /** 公司 的一些删除 ****/
    /********************************************************/

    @Override
    @Transactional(readOnly = false)
    public int del(ZKSysOrgCompany entity) {
        sysAuthCompanyService.diskDelByCompanyId(entity.getPkId());
        sysOrgUserService.delByCompanyId(entity.getPkId());
        sysOrgRoleService.delByCompanyId(entity.getPkId());
        return super.del(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKSysOrgCompany entity) {
        sysOrgUserService.diskDelByCompanyId(entity.getPkId());
        sysOrgRoleService.diskDelByCompanyId(entity.getPkId());
        sysAuthCompanyService.diskDelByCompanyId(entity.getPkId());
        return super.diskDel(entity);
    }
	
}