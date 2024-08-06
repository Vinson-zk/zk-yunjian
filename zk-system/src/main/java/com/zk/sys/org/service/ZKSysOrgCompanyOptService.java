/**
 * Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc. address: All rights reserved.
 * 
 * This software is the confidential and proprietary information of ZK-Vinson Technologies, Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with ZK-Vinson.
 *
 * @Title: ZKSysOrgCompanyOptService.java
 * @author Vinson
 * @Package com.zk.sys.org.service
 * @Description: TODO(simple description this file what to do. )
 * @date Apr 12, 2022 4:44:34 PM
 * @version V1.0
 */
package com.zk.sys.org.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zk.core.commons.data.ZKVerifiyCodeInfo;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.utils.ZKValidateCodeUtils;
import com.zk.core.utils.ZKValidatorsBeanUtils;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.service.ZKSysMsgConstants.ZKCodeType;
import com.zk.sys.service.ZKSysSendMailMsgService;
import com.zk.sys.service.ZKSysSendPhoneMsgService;
import com.zk.sys.utils.ZKSysUtils;

import jakarta.validation.Validator;

/**
 * 公司一些无事务的操作处理
 * 
 * @ClassName: ZKSysOrgCompanyOptService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
public class ZKSysOrgCompanyOptService {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKSysSendMailMsgService sysSendMailMsgService;

    @Autowired
    ZKSysSendPhoneMsgService sysSendPhoneMsgService;

    @Autowired
    private ZKSysOrgCompanyService sysOrgCompanyService;

    /**
     * 验证Bean实例对象
     */
    @Autowired
    protected Validator validator;

    public static interface TInfoKey {
        public static final String sendMailVerifiyCode = "_tk_sendMailVerifiyCode";
        public static final String sendPhoneVerifiyCode = "_tk_sendPhoneVerifiyCode";
        public static final String tempCompany = "_tk_tempCompany";
    }

    // 发送注册验证码
    public void sendVerifiyCode(ZKSysOrgCompany company, ZKSecTicket tk) {
        if (tk == null) {
            log.error("[>_<:20240724-2347-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 核验基本输入
        ZKValidatorsBeanUtils.validateWithException(validator, company);
        // 校验集团代码，是否唯一
        this.sysOrgCompanyService.checkUniqueGroupCode(company);
        // 校验公司代码是否唯一
        this.sysOrgCompanyService.checkUniqueCode(company);
        // 在令牌中记录将要注册的公司
        tk.put(TInfoKey.tempCompany, company);

        this.rgSendMailVerifiyCode(company, tk);
        this.rgSendPhoneVerifiyCode(company, tk);

    }

    /**
     * 重新发送验证码；
     *
     * @Title: rgSendVerifiyCodeAgain
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 2, 2024 11:24:35 AM
     * @param tk
     * @param againFlag
     *            1-重发邮箱验证码；2-重发手机验证码；其他-不发；
     * @return void
     */
    public void rgSendVerifiyCodeAgain(ZKSecTicket tk, int againFlag) {
        if (tk == null) {
            log.error("[>_<:20240724-2348-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 公司
        ZKSysOrgCompany company = tk.get(TInfoKey.tempCompany);
        if (company == null) {
            log.error("[>_<:20240724-2348-002] 公司注册异常，令牌信息丢失");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        if (againFlag == 1) {
            this.rgSendMailVerifiyCode(company, tk);
        }
        else if (againFlag == 2) {
            this.rgSendPhoneVerifiyCode(company, tk);
        }
        log.error("[>_<:20240724-2348-003] 无需再次发送");
    }

    // 邮箱注册，发送邮箱验证码
    protected void rgSendMailVerifiyCode(ZKSysOrgCompany company, ZKSecTicket ticket) {
        // 校验邮箱
        ZKValidatorsBeanUtils.validateWithException(validator, company, "mail");
//        ZKValidatorsBeanUtils.validateWithException(validator, company, "mail", ZKValidationGroup.CustomModel.class);
        if (ticket == null) {
            log.error("[>_<:20240724-2349-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 在令牌中记录新用户
        String vc = ZKValidateCodeUtils.genVerifyCode(6);
        ZKVerifiyCodeInfo<Void> vci = ZKVerifiyCodeInfo.as(vc, null);
        // 追踪令牌，添加验证码相关信息
        ticket.put(TInfoKey.sendMailVerifiyCode, vci);
        // 向注册邮箱发送验证码
        sysSendMailMsgService.sendMailCode(ZKCodeType.registerCompany, company.getCode(), vc, company.getMail());
    }

    // 手机注册，发送手机验证码
    protected void rgSendPhoneVerifiyCode(ZKSysOrgCompany company, ZKSecTicket ticket) {
        // 校验手机
        ZKValidatorsBeanUtils.validateWithException(validator, company, "phoneNum");
//        ZKValidatorsBeanUtils.validateWithException(validator, user, "phoneNum", ZKValidationGroup.CustomModel.class);
        if (ticket == null) {
            log.error("[>_<:20240724-2350-002] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 在令牌中记录新用户
        String vc = ZKValidateCodeUtils.genVerifyCode(6);
        ZKVerifiyCodeInfo<Void> vci = ZKVerifiyCodeInfo.as(vc, null);
        // 追踪令牌，添加验证码相关信息
        ticket.put(TInfoKey.sendPhoneVerifiyCode, vci);
        // 向注册手机发送验证码
        this.sysSendPhoneMsgService.sendPhoneCode(ZKCodeType.registerCompany, company.getPhoneNum(), vc);
    }

    // 提交验证码，并完成公司注册
    public ZKSysOrgCompany submitRegisterVerifiyCode(String password, String mailVerifiyCode, String phoneVerifiyCode,
            ZKSecTicket tk) {
        if (tk == null) {
            log.error("[>_<:20240724-2351-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 公司
        ZKSysOrgCompany company = tk.get(TInfoKey.tempCompany);
        if (company == null) {
            log.error("[>_<:20240724-2351-002] 公司注册异常，令牌信息丢失");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        if (ZKStringUtils.isEmpty(company.getParentId())) {
            company.setParentId(null);
        }
        // 校验邮件验证码
        ZKVerifiyCodeInfo<ZKSysOrgUser> vci = tk.get(TInfoKey.sendMailVerifiyCode);
        if (vci == null || !vci.isValidity()) {
            log.error("[>_<:20240724-2351-003] zk.sys.010020=验证码不存或已过期");
            throw ZKBusinessException.as("zk.sys.010020");
        }
        if (!vci.doVerifiyCode(ZKSysUtils.isDevEnv(), mailVerifiyCode)) {
            log.error("[>_<:20240724-2351-004] zk.sys.010021，验证码错误；原验证:{}，提交验证码:{}", vci.getVerifiyCode(),
                    mailVerifiyCode);
            throw ZKBusinessException.as("zk.sys.010021");
        }
        // 校验手机验证码
        vci = tk.get(TInfoKey.sendPhoneVerifiyCode);
        if (vci == null || !vci.isValidity()) {
            log.error("[>_<:20240724-2351-005] zk.sys.010020=验证码不存或已过期");
            throw ZKBusinessException.as("zk.sys.010020");
        }
        if (!vci.doVerifiyCode(ZKSysUtils.isDevEnv(), mailVerifiyCode)) {
            log.error("[>_<:20240724-2351-006] zk.sys.010021，验证码错误；原验证:{}，提交验证码:{}", vci.getVerifiyCode(),
                    phoneVerifiyCode);
            throw ZKBusinessException.as("zk.sys.010021");
        }
        // 完成注册
        company.setStatus(ZKSysOrgCompany.KeyStatus.waitSubmit);
        this.sysOrgCompanyService.editCompany(company, password);
        // 在令牌中记录将要注册的公司
        tk.put(TInfoKey.tempCompany, company);
        return company;
    }

    public ZKSysOrgCompany updateAuditContent(ZKSysOrgCompany company, ZKSecTicket tk) {
        if (tk == null) {
            log.error("[>_<:20240724-2352-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        ZKSysOrgCompany tempCompany = tk.get(TInfoKey.tempCompany);
        if (company == null) {
            log.error("[>_<:20240724-2352-002] 公司注册异常，令牌信息丢失");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        company.setPkId(tempCompany.getPkId());
        this.sysOrgCompanyService.updateAuditContent(company);
        return company;
    }

}
