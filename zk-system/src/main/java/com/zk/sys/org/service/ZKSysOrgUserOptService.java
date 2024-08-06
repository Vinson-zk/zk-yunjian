/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSysOrgUserOptService.java 
* @author Vinson 
* @Package com.zk.sys.org.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 17, 2024 11:56:45 AM 
* @version V1.0 
*/
package com.zk.sys.org.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.core.commons.ZKValidationGroup;
import com.zk.core.commons.data.ZKVerifiyCodeInfo;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKSecAuthenticationException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.utils.ZKValidateCodeUtils;
import com.zk.core.utils.ZKValidatorsBeanUtils;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.entity.ZKSysOrgUserEditLog.ZKUserEditFlag;
import com.zk.sys.org.entity.ZKSysOrgUserType;
import com.zk.sys.service.ZKSysMsgConstants.ZKCodeType;
import com.zk.sys.service.ZKSysSendMailMsgService;
import com.zk.sys.service.ZKSysSendPhoneMsgService;
import com.zk.sys.utils.ZKSysUtils;

import jakarta.validation.Validator;

/**
 * @ClassName: ZKSysOrgUserOptService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
public class ZKSysOrgUserOptService {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    public static interface TInfoKey {
        public static final String sendVerifiyCode = "_tk_sendVerifiyCode";

        public static final String registerType = "_tk_registerType";
    }

    /**
     * 验证Bean实例对象
     */
    @Autowired
    protected Validator validator;

    @Autowired
    ZKSysSendMailMsgService sysSendMailMsgService;

    @Autowired
    ZKSysSendPhoneMsgService sysSendPhoneMsgService;

    @Autowired
    ZKSysOrgUserService sysOrgUserService;

    @Autowired
    ZKSysOrgCompanyService sysOrgCompanyService;

    @Autowired
    private ZKSysOrgUserTypeService sysOrgUserTypeService;

    /********************************************************/
    /** 注册：用户邮箱注册 ****/
    /********************************************************/
    /**
     * 用户注册个人用户：第一步: 发送注册能请码
     *
     * @Title: registerPersonalUser
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 23, 2024 5:31:19 PM
     * @param user
     * @param registerType
     *            ZKUserEditFlag.Base 中的值
     * @return void
     */
    public ZKSysOrgUser sendRegisterPersonalUserVerifiyCode(String companyCode, ZKSysOrgUser user, int registerType,
            ZKSecTicket ticket) {
        /*** 1. 判断公司，公司是否存，或状态是否正常 */
        // 取出目标公司
        if (ZKStringUtils.isEmpty(companyCode)) {
            companyCode = ZKSysUtils.getOwnerCompanyCode();
        }
        ZKSysOrgCompany company = this.sysOrgCompanyService.getByCode(companyCode);
        if (company == null) {
            log.error("[>_<:20240717-1850-001] 公司[{}]不存在;", companyCode);
            throw ZKBusinessException.as("zk.sys.010003", companyCode);
        }
        if (company.getStatus() == null || ZKSysOrgCompany.KeyStatus.normal != company.getStatus().intValue()) {
            log.error("[>_<:20240717-1850-002] zk.sys.010004 公司[{}]状态异常，请联系管理员;", companyCode);
            throw ZKBusinessException.as("zk.sys.010004", companyCode);
        }
        user.setCompanyId(company.getPkId());
        /*** 2. 判断公司是否有个人用户类型；用户类型状态是否正常 */
        // 用户类型
        String userTypeCode = ZKSysUtils.getPersonalUserTypeCode();
        ZKSysOrgUserType userType = sysOrgUserTypeService.getByCode(company.getPkId(), userTypeCode);
        if (userType == null) {
            log.error("[>_<:20240717-1850-003] zk.sys.010022=用户类型[{}]不存在，不能创建此类用户;", userTypeCode);
            throw ZKBusinessException.as("zk.sys.010022", "公司不存在");
        }
        if (userType.getStatus() == null || ZKSysOrgUserType.KeyStatus.normal != userType.getStatus().intValue()) {
            log.error("[>_<:20240717-1850-004] zk.sys.010024=用户类型[{}={}]不可用，请联系管理员;", companyCode, userTypeCode);
            throw ZKBusinessException.as("zk.sys.010024", userTypeCode);
        }
        user.setUserTypeId(userType.getPkId());
        user.setUserTypeCode(userType.getCode());

        // 新增用户
        user.setNewRecord(true);
        user.setPkId(null);
        // 个人用户部门信息为 null
        user.setDeptId(null);
        user.setDeptCode(null);

        /*** 3. 根据注册类型，发送验证码 */
        if (ZKUserEditFlag.Base.mail == registerType) {
            this.rgSendMailVerifiyCode(user, ticket);
        }
        else if (ZKUserEditFlag.Base.phoneNum == registerType) {
            this.rgSendPhoneVerifiyCode(user, ticket);
        }
        else {
            this.sysOrgUserService.editUserSelf(user, registerType, ZKUserEditFlag.Account.self);
        }
        /*** 4. 令牌记录注册类型 */
        ticket.put(TInfoKey.registerType, registerType);
        return user;
    }

    // 邮箱注册，发送邮箱验证码
    protected void rgSendMailVerifiyCode(ZKSysOrgUser user, ZKSecTicket ticket) {
        // 校验邮箱
        ZKValidatorsBeanUtils.validateWithException(validator, user, "mail");
        ZKValidatorsBeanUtils.validateWithException(validator, user, "mail", ZKValidationGroup.CustomModel.class);
        if (ticket == null) {
            log.error("[>_<:20240723-1930-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 在令牌中记录新用户
        String vc = ZKValidateCodeUtils.genVerifyCode(6);
        ZKVerifiyCodeInfo<ZKSysOrgUser> vci = ZKVerifiyCodeInfo.as(vc, user);
        // 追踪令牌，添加验证码相关信息
        ticket.put(TInfoKey.sendVerifiyCode, vci);
        // 向注册邮箱发送验证码
        sysSendMailMsgService.sendMailCode(ZKCodeType.registerUser, user.getMail(), vc, user.getMail());
    }

    // 手机注册，发送手机验证码
    protected void rgSendPhoneVerifiyCode(ZKSysOrgUser user, ZKSecTicket ticket) {
        // 校验手机
        ZKValidatorsBeanUtils.validateWithException(validator, user, "phoneNum");
        ZKValidatorsBeanUtils.validateWithException(validator, user, "phoneNum", ZKValidationGroup.CustomModel.class);
        if (ticket == null) {
            log.error("[>_<:20240723-1930-002] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 在令牌中记录新用户
        String vc = ZKValidateCodeUtils.genVerifyCode(6);
        ZKVerifiyCodeInfo<ZKSysOrgUser> vci = ZKVerifiyCodeInfo.as(vc, user);
        // 追踪令牌，添加验证码相关信息
        ticket.put(TInfoKey.sendVerifiyCode, vci);
        // 向注册手机发送验证码
        this.sysSendPhoneMsgService.sendPhoneCode(ZKCodeType.registerUser, user.getPhoneNum(), vc);
    }

    // 用户注册，提交验证码，并完成注册
    public ZKSysOrgUser submitRegisterVerifiyCode(String verifiyCode, ZKSecTicket tk) {
        if (tk == null) {
            log.error("[>_<:20240714-0954-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 校验验证码
        ZKVerifiyCodeInfo<ZKSysOrgUser> vci = tk.get(TInfoKey.sendVerifiyCode);
        if (vci == null || !vci.isValidity()) {
            log.error("[>_<:20240714-0954-002] zk.sys.010020=验证码不存或已过期");
            throw ZKBusinessException.as("zk.sys.010020");
        }
        if (!vci.doVerifiyCode(ZKSysUtils.isDevEnv(), verifiyCode)) {
            log.error("[>_<:20240714-0954-003] zk.sys.010021，验证码错误；原验证:{}，提交验证码:{}", vci.getVerifiyCode(), verifiyCode);
            throw ZKBusinessException.as("zk.sys.010021");
        }
        // 取注册方式
        Integer registerType = tk.get(TInfoKey.registerType);
        if (registerType == null) {
            log.error("[>_<:20220412-1655-001] 令牌信息丢失，用户注册方式不存在");
            throw ZKBusinessException.as("zk.sec.000022");
        }

        // 完成注册
        ZKSysOrgUser user = vci.getData();
        /*** 根据注册类型，设置默认账号：非账号注册，自动生成一个账号 */
        int accountEditFlag = ZKUserEditFlag.Account.self;
        if (registerType != ZKUserEditFlag.Base.account) {
            // 未设置账号，设置系统默认账号
            if (ZKStringUtils.isEmpty(user.getAccount())) {
                user.setAccount(ZKSysUtils.genSysDefaultAccount());
                accountEditFlag = ZKUserEditFlag.Account.system;
            }
        }

        user.setStatus(ZKSysOrgUser.KeyStatus.normal);
        this.sysOrgUserService.editUserSelf(user, registerType, accountEditFlag);
        return user;
    }

    /********************************************************/
    /** 修改用户邮箱 ****/
    /********************************************************/

    // 修改邮箱，发送新邮箱的验证码
    public void cmSendMailVerifiyCode(ZKSysOrgUser user, String newMail, ZKSecTicket ticket) {
        if (ticket == null) {
            log.error("[>_<:20240714-0929-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 在令牌中记录新邮箱
        String vc = ZKValidateCodeUtils.genVerifyCode(6);
        user.setMail(newMail); // 这里将新邮箱记下，校验验证码时，一并修改
        ZKVerifiyCodeInfo<ZKSysOrgUser> vci = ZKVerifiyCodeInfo.as(vc, user);
        // 追踪令牌，添加验证码相关信息
        ticket.put(TInfoKey.sendVerifiyCode, vci);
        // 向新邮箱发送验证码
        sysSendMailMsgService.sendMailCode(ZKCodeType.changeMail, user.getAccount(), vc, newMail);
    }

    // 修改邮箱，校验验证码同时修改邮箱
    public void cmSubmitVerifiyCode(ZKSecTicket ticket, String verifiyCode) {
        if (ticket == null) {
            log.error("[>_<:20240714-0940-002] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 校验验证码
        ZKVerifiyCodeInfo<ZKSysOrgUser> vci = ticket.get(TInfoKey.sendVerifiyCode);
        if (vci == null || !vci.isValidity()) {
            log.error("[>_<:20240714-0940-002] zk.sys.010020=验证码不存或已过期");
            throw ZKBusinessException.as("zk.sys.010020");
        }
        if (!vci.doVerifiyCode(ZKSysUtils.isDevEnv(), verifiyCode)) {
            log.error("[>_<:20240714-0940-003] zk.sys.010021，验证码错误；原验证:{}，提交验证码:{}", vci.getVerifiyCode(), verifiyCode);
            throw ZKBusinessException.as("zk.sys.010021");

        }
        // 修改邮箱
        this.sysOrgUserService.updateMail(vci.getData(), vci.getData().getMail(), ZKUserEditFlag.Mail.self);
    }

    /********************************************************/
    /** 修改用户手机号 ****/
    /********************************************************/

    // 修改手机号，发送新手机验证码
    public void cpSendPhoneVerifiyCode(ZKSysOrgUser user, String newPhoneNum, ZKSecTicket ticket) {
        if (ticket == null) {
            log.error("[>_<:20240714-0950-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 在令牌中记录新手机号
        String vc = ZKValidateCodeUtils.genVerifyCode(6);
        user.setPhoneNum(newPhoneNum); // 这里将新手机号记下，校验验证码时，一并修改
        ZKVerifiyCodeInfo<ZKSysOrgUser> vci = ZKVerifiyCodeInfo.as(vc, user);
        // 追踪令牌，添加验证码相关信息
        ticket.put(TInfoKey.sendVerifiyCode, vci);
        // 向用户手机发送找回密码的邮件验证码
        this.sysSendPhoneMsgService.sendPhoneCode(ZKCodeType.changePhoneNum, user.getPhoneNum(), vc);
    }

    // 修改手机号，校验手机验证码同时修改手机号
    public void cpSubmitVerifiyCode(ZKSecTicket ticket, String verifiyCode) {
        if (ticket == null) {
            log.error("[>_<:20240714-0951-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 校验验证码
        ZKVerifiyCodeInfo<ZKSysOrgUser> vci = ticket.get(TInfoKey.sendVerifiyCode);
        if (vci == null || !vci.isValidity()) {
            log.error("[>_<:20240714-0952-002] zk.sys.010020=验证码不存或已过期");
            throw ZKBusinessException.as("zk.sys.010020");
        }
        if (!vci.doVerifiyCode(ZKSysUtils.isDevEnv(), verifiyCode)) {
            log.error("[>_<:20240714-0953-003] zk.sys.010021，验证码错误；原验证:{}，提交验证码:{}", vci.getVerifiyCode(), verifiyCode);
            throw ZKBusinessException.as("zk.sys.010021");
        }
        // 修改手机号
        this.sysOrgUserService.updatePhoneNum(vci.getData(), vci.getData().getPhoneNum(), ZKUserEditFlag.Phone.self);
    }

    /********************************************************/
    /** 密码相关操作 ****/
    /********************************************************/

    // 忘记密码，发送邮件验证码
    public void fpSendMailVerifiyCode(String companyCode, String account, ZKSecTicket ticket) {
        //
        if (ticket == null) {
            log.error("[>_<:20240714-0925-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 选择找到用户
        ZKSysOrgUser user = sysOrgUserService.getUserSmartByCompanyCode(companyCode, account);
        if (user == null) {
            // 用户不存在
            log.error("[>_<:20240714-0925-002] 用户[{}-{}] 不存在", companyCode, account);
            throw ZKSecAuthenticationException.as("zk.sys.020001");
        }
        // 向用户邮箱发送找回密码的邮件验证码
        String vc = ZKValidateCodeUtils.genVerifyCode(6);
        ZKVerifiyCodeInfo<ZKSysOrgUser> vci = ZKVerifiyCodeInfo.as(vc, user);
        // 追踪令牌，添加验证码相关信息
        ticket.put(TInfoKey.sendVerifiyCode, vci);

        sysSendMailMsgService.sendMailCode(ZKCodeType.restPassword, user.getAccount(), vc, user.getMail());
    }

    // 忘记密码，发送手机验证码
    public void fpSendPhoneVerifiyCode(String companyCode, String inputValue, ZKSecTicket ticket) {
        //
        if (ticket == null) {
            log.error("[>_<:20240714-0922-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 选择找到用户
        ZKSysOrgUser user = sysOrgUserService.getUserSmartByCompanyCode(companyCode, inputValue);
        if (user == null) {
            // 用户不存在
            log.error("[>_<:20240714-0922-002] 用户[{}-{}] 不存在", companyCode, inputValue);
            throw ZKSecAuthenticationException.as("zk.sys.020001");
        }
        // 向用户手机发送找回密码的手机验证码
        String vc = ZKValidateCodeUtils.genVerifyCode(6);
        ZKVerifiyCodeInfo<ZKSysOrgUser> vci = ZKVerifiyCodeInfo.as(vc, user);
        // 追踪令牌，添加验证码相关信息
        ticket.put(TInfoKey.sendVerifiyCode, vci);
        // 发送手机验证码
        this.sysSendPhoneMsgService.sendPhoneCode(ZKCodeType.restPassword, user.getPhoneNum(), vc);
    }

    // 忘记密码，校验验证码和修改密码
    public void fpSubmitVerifiyCode(ZKSecTicket ticket, String verifiyCode, String newPassword) {
        if (ticket == null) {
            log.error("[>_<:20240714-0923-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 校验验证码
        ZKVerifiyCodeInfo<ZKSysOrgUser> vci = ticket.get(TInfoKey.sendVerifiyCode);
        if (vci == null || !vci.isValidity()) {
            log.error("[>_<:20240714-0923-002] zk.sys.010020=验证码不存或已过期");
            throw ZKBusinessException.as("zk.sys.010020");
        }
        if (!vci.doVerifiyCode(ZKSysUtils.isDevEnv(), verifiyCode)) {
            log.error("[>_<:20240714-0923-003] zk.sys.010021，验证码错误；原验证:{}，提交验证码:{}", vci.getVerifiyCode(), verifiyCode);
            throw ZKBusinessException.as("zk.sys.010021");

        }
        // 修改密码
        this.sysOrgUserService.updatePwd(vci.getData().getPkId(), newPassword, ZKUserEditFlag.Pwd.self);
    }

    // 修改密码
    @Transactional(readOnly = false)
    public void changePassword(ZKSysOrgUser user, String oldPassword, String newPassword) {
        if (user == null) {
            log.error("[>_<:20240714-0925-001] 用户不存在");
            throw ZKSecAuthenticationException.as("zk.sys.020001");
        }
        // 验证旧密码
        if (!this.sysOrgUserService.checkUserPassword(user, oldPassword)) {
            log.error("[>_<:20240714-0925-002] zk.sys.010021，zk.sys.020002=密码错误");
            throw ZKBusinessException.as("zk.sys.020002");
        }
        // 修改新密码
        this.sysOrgUserService.updatePwd(user.getPkId(), newPassword, ZKUserEditFlag.Pwd.self);
    }

}
