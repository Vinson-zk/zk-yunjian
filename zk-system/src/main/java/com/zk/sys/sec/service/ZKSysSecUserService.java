/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSysSecUserService.java 
* @author Vinson 
* @Package com.zk.sys.sec.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 25, 2022 5:13:03 PM 
* @version V1.0 
*/
package com.zk.sys.sec.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.security.exception.ZKSecCodeException;
import com.zk.security.token.ZKSecAuthcUserToken;
import com.zk.sys.entity.org.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.service.ZKSysOrgCompanyService;
import com.zk.sys.org.service.ZKSysOrgUserService;

/** 
* @ClassName: ZKSysSecUserService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKSysSecUserService {

    /**
     * 日志对象
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ZKSysOrgCompanyService sysOrgCompanyService;

    @Autowired
    ZKSysOrgUserService sysOrgUserService;

    /**
     * 用户登录
     *
     * @Title: login
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 26, 2022 8:54:21 AM
     * @param authcUserToken
     * @return
     * @return ZKSysOrgUser
     */
    public ZKSysOrgUser login(ZKSecAuthcUserToken authcUserToken) {
        /*** 公司部分检验 ---------------------------------- */
        /* --- 根据公司代码取出对应公司 */
        ZKSysOrgCompany company = this.sysOrgCompanyService.getByCode(authcUserToken.getCompanyCode());
        if (company == null) {
            log.error("[>_<:2022425-1723-001] 公司代码 {} 不存在", authcUserToken.getCompanyCode());
            throw new ZKSecCodeException("zk.sys.020004", null, new String[] { authcUserToken.getCompanyCode() },
                    null);
        }
        /* --- 公司状态校验 */
        if (company.getStatus() == null || ZKSysOrgCompany.KeyStatus.normal != company.getStatus()) {
            throw new ZKSecCodeException("zk.sys.020005");
        }

        /*** 用户部分检验 ---------------------------------- */
        ZKSysOrgUser loginUser = null;
        /* --- 查询用户 */
        // 根据用户账号取用户
        loginUser = this.sysOrgUserService.getByAccount(company.getPkId(), authcUserToken.getUsername());
        if (loginUser == null) {
            // 根据手机号取用户
            loginUser = this.sysOrgUserService.getByPhoneNum(company.getPkId(), authcUserToken.getUsername());
        }
        if (loginUser == null) {
            // 根据邮箱取用户
            loginUser = this.sysOrgUserService.getByMail(company.getPkId(), authcUserToken.getUsername());
        }
        if (loginUser == null) {
            // 用户不存在
            log.error("[>_<:2022426-0922-001] 用户[{}-{}] 不存在", authcUserToken.getCompanyCode(),
                    authcUserToken.getUsername());
            throw new ZKSecCodeException("zk.sys.020003");
        }
        /* --- 校验用户密码 */
        if (!this.checkUserPassword(loginUser, authcUserToken.getPwd())) {
            // 密码错误
            log.error("[>_<:2022426-0922-001] 用户[{}-{}]密码错误", authcUserToken.getCompanyCode(),
                    authcUserToken.getUsername());
            throw new ZKSecCodeException("zk.sys.020003");
        }
        /* --- 校验用户状态 */
        if (loginUser.getStatus() == null || ZKSysOrgUser.KeyStatus.normal != loginUser.getStatus().intValue()) {
            throw new ZKSecCodeException("zk.sys.020006");
        }
        return loginUser;
    }

    private boolean checkUserPassword(ZKSysOrgUser loginUser, String pwd) {
        pwd = this.sysOrgUserService.encryptionPwd(pwd);
        if (pwd.equals(loginUser.getPassword())) {
            return true;
        }
        return false;
    }

}
