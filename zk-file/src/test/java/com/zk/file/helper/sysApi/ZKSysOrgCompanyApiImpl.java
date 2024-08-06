package com.zk.file.helper.sysApi;

import com.zk.core.commons.ZKMsgRes;
import com.zk.sys.org.api.ZKSysOrgCompanyApi;
import com.zk.sys.org.entity.ZKSysOrgCompany;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description:
 * @ClassName ZKSysOrgCompanyApiImpl
 * @Package com.zk.file.helper.sysApi
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-10-12 14:59:56
 **/
public class ZKSysOrgCompanyApiImpl implements ZKSysOrgCompanyApi {
    @Override
    public ZKMsgRes getCompanyByCode(String companyCode) {
        ZKSysOrgCompany company = new ZKSysOrgCompany();
        company.setStatus(ZKSysOrgCompany.KeyStatus.normal);
        company.setPkId("-1");
        company.setCode("t-c");
        company.setGroupCode("t-g-c");
        return ZKMsgRes.asOk(null, company);
    }
}
