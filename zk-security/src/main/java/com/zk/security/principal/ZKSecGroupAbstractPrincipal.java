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
* @Title: ZKSecGroupAbstractPrincipal.java 
* @author Vinson 
* @Package com.zk.security.principal 
* @Description: TODO(simple description this file what to do. ) 
* @date May 13, 2022 5:28:37 PM 
* @version V1.0 
*/
package com.zk.security.principal;

/** 
* @ClassName: ZKSecGroupAbstractPrincipal 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecGroupAbstractPrincipal<ID> extends ZKSecAbstractPrincipal<ID>
        implements ZKSecGroupPrincipal<ID> {

    private static final long serialVersionUID = 1L;

    // 集团代码
    String groupCode;

    // 公司ID
    ID companyId;

    // 公司代码
    String companyCode;

    public ZKSecGroupAbstractPrincipal(ID pkId, int type, long osType, String udid, long appType, String appId,
            String groupCode, ID companyId, String companyCode) {
        super(pkId, type, osType, udid, appType, appId);
        this.groupCode = groupCode;
        this.companyId = companyId;
        this.companyCode = companyCode;
    }

    /**
     * 集团代码
     * <p>
     * Title: getGrouCode
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.security.principal.ZKSecGroupPrincipal#getGrouCode()
     */
    @Override
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * 组织代码
     */
    @Override
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * @return 公司ID
     */
    @Override
    public ID getCompanyId() {
        return companyId;
    }

}
