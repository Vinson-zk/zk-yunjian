/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKLocaleResolver.java 
 * @author Vinson 
 * @Package com.zk.webmvc.handler 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 6:20:34 PM 
 * @version V1.0   
*/
package com.zk.core.web.resolver;

import java.util.Locale;

/** 
* @ClassName: ZKLocaleResolver 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKLocaleResolver {

    Locale getDefaultLocale();

}
