/**
 * 
 */
package com.zk.mail.service;
 
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.mail.dao.ZKMailTemplateDao;
import com.zk.mail.entity.ZKMailTemplate;
import com.zk.mail.entity.ZKMailTemplate.KeyLocale;
import com.zk.mail.entity.ZKMailType;
import com.zk.sys.org.api.ZKSysOrgCompanyApi;
import com.zk.sys.org.entity.ZKSysOrgCompany;

/**
 * ZKMailTemplateService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKMailTemplateService extends ZKBaseService<String, ZKMailTemplate, ZKMailTemplateDao> {

    @Autowired
    ZKMailTypeService mailTypeService;

    @Autowired
    private ZKSysOrgCompanyApi sysOrgCompanyApi;

    /**
     * 根据公司代码取公司实体
     *
     * @Title: getCompanyByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 18, 2022 12:17:25 AM
     * @param companyCode
     * @return
     * @return ZKSysOrgCompany
     */
    public ZKSysOrgCompany getCompanyByCode(String companyCode) {
        ZKMsgRes res = sysOrgCompanyApi.getCompanyByCode(companyCode);
        if (res.isOk()) {
            ZKSysOrgCompany c = res.getDataByClass(ZKSysOrgCompany.class);
            return c;
        }
        log.error("[>_<:20220526-1351-001] 根据公司代码取公司详情失败:{}", res.toString());
        return null;
    }

    /**
     * 根据邮件类型，公司代码，语言 取模板实体
     *
     * @Title: getByTypeCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 26, 2022 5:04:17 PM
     * @param typeCode
     * @param companyCode
     * @param locale
     * @param delFlag
     * @return
     * @return ZKMailTemplate
     */
    ZKMailTemplate getByTypeCode(String typeCode, String companyCode, String locale, Integer delFlag) {
        if (ZKStringUtils.isEmpty(typeCode)) {
            return null;
        }
        return this.dao.getByTypeCode(ZKMailTemplate.sqlHelper().getTableName(),
                ZKMailTemplate.sqlHelper().getTableAlias(), ZKMailTemplate.sqlHelper().getBlockSqlCols(),
                typeCode, companyCode, locale, delFlag);
    }

    @Override
    @Transactional(readOnly = false)
    public int save(ZKMailTemplate mailTemplate) {
        if (mailTemplate.isNewRecord()) {
            // 新记录，判断代码是否唯一
            ZKMailTemplate old = this.getByTypeCode(mailTemplate.getTypeCode(), mailTemplate.getCompanyCode(),
                    mailTemplate.getLocale(), null);
            if (old != null) {
                if (old.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.normal) {
                    log.error("[>_<:20220524-1510-002] zk.mail.000003=邮件模板 [{}][{}][{}] 已存在；",
                            mailTemplate.getTypeCode(), mailTemplate.getCompanyCode(), mailTemplate.getLocale());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("typeCode", ZKMsgUtils.getMessage(ZKWebUtils.getLocale(), "zk.mail.000003",
                            mailTemplate.getTypeCode(), mailTemplate.getCompanyCode()));
                    throw ZKValidatorException.as(validatorMsg);
                }
                else {
                    // mailTemplate.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
                    mailTemplate.setPkId(old.getPkId());
                    super.restore(mailTemplate);
                }
            }
            
            if(!ZKStringUtils.isEmpty(mailTemplate.getCompanyCode())) {
                // 公司代码不为空，填充公司信息
                ZKSysOrgCompany company = this.getCompanyByCode(mailTemplate.getCompanyCode());
                if(company == null) {
                    // 公司不存在
                    log.error("[>_<:20220526-1720-001] zk.mail.000005=公司[{}]不存在；", mailTemplate.getCompanyCode());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("companyCode",
                            ZKMsgUtils.getMessage("zk.mail.000005", mailTemplate.getCompanyCode()));
                    throw ZKValidatorException.as(validatorMsg);
                }else {
                    if (company.getStatus().intValue() != ZKSysOrgCompany.KeyStatus.normal) {
                        log.error("[>_<:20220526-1720-002] zk.mail.000006=公司[{}]状态异常，请联系管理员；",
                                mailTemplate.getCompanyCode());
                        Map<String, String> validatorMsg = Maps.newHashMap();
                        validatorMsg.put("companyCode",
                                ZKMsgUtils.getMessage("zk.mail.000006", mailTemplate.getCompanyCode()));
                        throw ZKValidatorException.as(validatorMsg);
                    }
                    else {
                        mailTemplate.setCompanyId(company.getPkId());
                        mailTemplate.setGroupCode(company.getGroupCode());
                    }
                }
            }
            
            // 填充邮件模板的类型信息
            ZKMailType mailType = this.mailTypeService.getByCode(mailTemplate.getTypeCode(),
                    ZKBaseEntity.DEL_FLAG.normal);
            if (mailType == null) {
                log.error("[>_<:20220526-1634-002] zk.mail.000004=邮件类型[{}]不存在；", mailTemplate.getTypeCode());
                Map<String, String> validatorMsg = Maps.newHashMap();
                validatorMsg.put("typeCode", ZKMsgUtils.getMessage("zk.mail.000004", mailTemplate.getTypeCode()));
                throw ZKValidatorException.as(validatorMsg);
            }
            else {
                if (mailType.getStatus().intValue() != ZKMailType.KeyStatus.normal) {
                    log.error("[>_<:20220526-1634-003] zk.mail.000002=邮件类型[{}]被禁用；", mailTemplate.getTypeCode());
                    throw ZKBusinessException.as("zk.mail.000002", null, mailTemplate.getTypeCode());
                }
                else {
                    mailTemplate.setTypeId(mailType.getPkId());
                }
            }
            // 设置默认值
            if (mailTemplate.getCompanyId() == null) {
                mailTemplate.setCompanyId("0");
            }
            if (mailTemplate.getLocale() == null) {
                mailTemplate.setLocale(KeyLocale._default);
            }
        }
        return super.save(mailTemplate);
    }

	
	
}