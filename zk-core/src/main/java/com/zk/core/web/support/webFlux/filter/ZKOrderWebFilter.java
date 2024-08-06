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
* @Title: ZKOrderWebFilter.java 
* @author Vinson 
* @Package com.zk.core.web.support.webFlux.filter
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 28, 2023 2:39:53 PM 
* @version V1.0 
*/
package com.zk.core.web.support.webFlux.filter;

import org.springframework.core.Ordered;

/**
 * @ClassName: ZKOrderWebFilter
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKOrderWebFilter implements Ordered {

    int order = -1;

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

}
