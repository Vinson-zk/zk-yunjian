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
* @Title: ZKAbstractWebFilter.java 
* @author Vinson 
* @Package com.zk.core.web.support.webFlux.filter
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 28, 2023 2:45:37 PM 
* @version V1.0 
*/
package com.zk.core.web.support.webFlux.filter;

import com.zk.core.utils.ZKStringUtils;

/** 
* @ClassName: ZKAbstractWebFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKAbstractWebFilter extends ZKOrderWebFilter implements ZKNameWebFilter, ZKWebFilter {

    String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        if (ZKStringUtils.isEmpty(this.getName())) {
            this.setName(this.getClass().getName());
        }
        return this.name;
    }

}
