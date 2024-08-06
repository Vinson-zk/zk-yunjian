/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKResultFormat.java 
* @author Vinson 
* @Package com.zk.core.commons.reactor 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 13, 2024 5:10:37 PM 
* @version V1.0 
*/
package com.zk.core.commons.reactor;

import java.util.List;

/** 
* @ClassName: ZKResultFormat 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@FunctionalInterface
public interface ZKResultFormat<R, T> {

    R format(List<T> t);

}
