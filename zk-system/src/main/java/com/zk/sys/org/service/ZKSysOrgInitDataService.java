/**
 * Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc. address: All rights reserved.
 * 
 * This software is the confidential and proprietary information of ZK-Vinson Technologies, Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with ZK-Vinson.
 *
 * @Title: ZKSysOrgInitDataService.java
 * @author Vinson
 * @Package com.zk.sys.org.service
 * @Description: TODO(simple description this file what to do. )
 * @date Apr 25, 2022 4:27:03 PM
 * @version V1.0
 */
package com.zk.sys.org.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.core.commons.data.ZKJson;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgUser;

/**
 * 初始化组织结构数据
 * 
 * @ClassName: ZKSysOrgInitDataService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
public class ZKSysOrgInitDataService {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKSysOrgCompanyService sysOrgCompanyService;

    @Autowired
    ZKSysOrgUserService sysOrgUserService;

    /**
     * 初始化平台，在平台搭建时，平台相关公司，用户还是一片空白时，需要初始化；
     *
     * @Title: initOwnerPlatform
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 26, 2022 8:31:33 AM
     * @return void
     */
    @Transactional(readOnly = false)
    public void initOwnerPlatform() {
        try {
            System.out.println("initOwnerPlatform begin =================================================");
            System.out.println("^_^: ----- ----- initOwnerPlatform 初始化平台开始 ... ... ");
            ZKSysOrgCompany company = this.initOwnerCompany();
            this.approveCompany(company);
            @SuppressWarnings("unused")
            ZKSysOrgUser adminUser = this.addAdminUser(company);
            System.out.println("^_^: ----- ----- initOwnerPlatform 初始化平台 end  ");
            System.out.println("initOwnerPlatform end =================================================");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional(readOnly = false)
    protected ZKSysOrgCompany initOwnerCompany() {
        System.out.println("^_^: ----- ----- initOwnerCompany 初始化拥有者公司 begin ... ...");
        ZKSysOrgCompany company = new ZKSysOrgCompany();
        company.setGroupCode("yunjian");
        company.setCode("yunjian");
        company.setName(ZKJson.parse("{\"en-US\": \"yunjian\", \"zh-CN\": \"云笺\"}"));
        company.setPhoneNum("13825659082");
        company.setMail("binary_space@126.com");
        sysOrgCompanyService.save(company);
        System.out.println("^_^: ----- ----- initOwnerCompany 初始化拥有者公司 end ");
        return company;
    }

    @Transactional(readOnly = false)
    protected ZKSysOrgCompany approveCompany(ZKSysOrgCompany company) {
        System.out.println("^_^: ----- ----- approveCompany 审核拥有者公司 begin ... ...");
        sysOrgCompanyService.approveCompany(company);
        System.out.println("^_^: ----- ----- approveCompany 审核拥有者公司 end ");
        return company;
    }

    @Transactional(readOnly = false)
    protected ZKSysOrgUser addAdminUser(ZKSysOrgCompany company) {
        System.out.println("^_^: ----- ----- addAdminUser 初始拥有者公司 admin 用户 begin ... ...");
        ZKSysOrgUser adminUser = new ZKSysOrgUser();
        adminUser.setCompanyId(company.getPkId());
        adminUser.setAccount("admin");
        adminUser.setPassword("admin");
        adminUser.setRealName("征客");
        adminUser.setNickname("征客");
        adminUser.setPhoneNum("13825659082");
        adminUser.setMail("binary_spqce@126.com");
        adminUser.setSex("male");
        adminUser.setStatus(ZKSysOrgUser.KeyStatus.normal);
        sysOrgUserService.save(adminUser);
        System.out.println("^_^: ----- ----- addAdminUser 初始拥有者公司 admin 用户 end ");
        return adminUser;
    }

}
