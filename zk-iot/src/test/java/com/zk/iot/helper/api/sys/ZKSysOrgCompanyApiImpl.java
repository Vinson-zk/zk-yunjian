/** 
* Copyright (c) 2012-2025 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSysOrgCompanyApiImpl.java 
* @author Vinson 
* @Package com.zk.iot.helper.api.sys 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 3, 2025 4:18:58 PM 
* @version V1.0 
*/
package com.zk.iot.helper.api.sys;

import com.zk.core.commons.ZKMsgRes;
import com.zk.sys.org.api.ZKSysOrgCompanyApi;
import com.zk.sys.org.entity.ZKSysOrgCompany;

/** 
* @ClassName: ZKSysOrgCompanyApiImpl 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
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
