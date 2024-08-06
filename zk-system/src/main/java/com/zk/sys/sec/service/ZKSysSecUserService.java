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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.exception.ZKSecAuthenticationException;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.token.ZKSecAuthcUserToken;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.service.ZKSysOrgCompanyOptService.TInfoKey;
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
    protected Logger log = LogManager.getLogger(getClass());

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
            throw ZKSecAuthenticationException.as("zk.sys.020004", (Object) null, authcUserToken.getCompanyCode());
        }

        /*** 用户部分检验 ---------------------------------- */
        ZKSysOrgUser loginUser = this.sysOrgUserService.getUserSmartByCompanyId(company.getPkId(),
                authcUserToken.getUsername());
        if (loginUser == null || loginUser.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.delete) {
            // 用户不存在
            log.error("[>_<:2022426-0922-001] 用户[{}-{}] 不存在", authcUserToken.getCompanyCode(),
                    authcUserToken.getUsername());
            throw ZKSecAuthenticationException.as("zk.sys.020003");
        }
        /* --- 校验用户密码 */
        if (!this.sysOrgUserService.checkUserPassword(loginUser, authcUserToken.getPwd())) {
            // 密码错误
            log.error("[>_<:2022426-0922-001] 用户[{}-{}]密码错误", authcUserToken.getCompanyCode(),
                    authcUserToken.getUsername());
            throw ZKSecAuthenticationException.as("zk.sys.020003");
        }
        /* --- 校验用户状态 */
        if (loginUser.getStatus() == null || ZKSysOrgUser.KeyStatus.normal != loginUser.getStatus().intValue()) {
            throw ZKSecAuthenticationException.as("zk.sys.020006");
        }

        /* --- 公司状态校验 */
        if (company.getStatus() == null || ZKSysOrgCompany.KeyStatus.normal != company.getStatus()) {
//            if (ZKSysOrgCompany.KeyStatus.waitSubmit == company.getStatus().intValue()) {
//                
//            }
            // 在令牌中记录将要注册的公司
            ZKSecTicket tk = ZKSecSecurityUtils.getTikcet(true);
            tk.put(TInfoKey.tempCompany, company);
            throw ZKSecAuthenticationException.as("zk.sys.020005", company);
        }

        return loginUser;
    }

}
