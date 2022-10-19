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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zk.core.exception.ZKCodeException;
import com.zk.sys.entity.org.ZKSysOrgCompany;

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
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ZKSysOrgCompanyService sysOrgCompanyService;

    /**
     * 审核公司
     *
     * @Title: auditCompany
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 12, 2022 4:46:23 PM
     * @param companyId
     *            审核的目标公司ID
     * @param flag
     *            审核的标识；0-通过审核；其他禁用
     * @return void
     */
    public void auditCompany(String companyId, int flag) {
        ZKSysOrgCompany company = this.sysOrgCompanyService.get(new ZKSysOrgCompany(companyId));
        if (company == null) {
            log.error("[>_<:20220412-1648-001] 公司不存在! parentId:{}", companyId);
            throw ZKCodeException.as("zk.sys.010003", "公司不存在");
        }
        if (flag == 0) {
            // 通过审核
            this.sysOrgCompanyService.approveCompany(company);
        } else {
            // 禁用公司
            this.sysOrgCompanyService.disabledCompany(company);
        }
    }

}
