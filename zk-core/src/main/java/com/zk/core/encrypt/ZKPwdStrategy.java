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
 * @Title: ZKPwdStrategy.java 
 * @author Vinson 
 * @Package com.zk.core.crypto 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 1:28:41 PM 
 * @version V1.0   
*/
package com.zk.core.encrypt;

/** 
* @ClassName: ZKPwdStrategy 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKPwdStrategy {

    /**
     * 取密码策略说明
     *
     * @Title: getDeclare
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 18, 2019 1:49:02 PM
     * @return
     * @return String
     */
    String getDeclare();

    /**
     * 断言密码输入是否正常
     *
     * @Title: assertPwdInput
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 18, 2019 2:08:15 PM
     * @param pwd
     * @return
     * @return boolean
     */
    boolean assertPwdInput(char[] pwd);

}
