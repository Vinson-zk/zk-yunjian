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
* @Title: ZKSecAbstractFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 6:53:05 AM 
* @version V1.0 
*/
package com.zk.security.web.filter;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.security.common.ZKSecNameable;

/** 
* @ClassName: ZKSecAbstractFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAbstractFilter extends ZKSecOncePerFilter implements ZKSecNameable {

    protected Logger logger = LogManager.getLogger(getClass());

    /**
     * The name of this filter, unique within an application.
     */
    private String name;

    @Override
    public void init() {
        super.init();
    }

    public String getName() {
//        if (this.name == null) {
////            FilterConfig config = getFilterConfig();
////            if (config != null) {
////                this.name = config.getFilterName();
////            }
//        }
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
