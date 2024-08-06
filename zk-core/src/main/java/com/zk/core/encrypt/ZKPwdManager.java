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
 * @Title: ZKPwdManager.java 
 * @author Vinson 
 * @Package com.zk.core.crypto 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 1:28:06 PM 
 * @version V1.0   
*/
package com.zk.core.encrypt;

/** 
* @ClassName: ZKPwdManager 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKPwdManager {

    /**
     * @Title: getPwdStrategy
     * @Description: 取密码策略
     * @author Vinson
     * @date Jun 18, 2019 2:25:34 PM
     * @param group
     *            不同分组，可以定制不同密码策略
     * @return
     * @return ZKPwdStrategy
     */
    ZKPwdStrategy getPwdStrategy(String group);

    /**
     * 加密密码，整个平台密码的加密方式是统一的。
     *
     * @Title: encryptionPwd
     * @Description: 加密密码，整个平台密码的加密方式是统一的。
     * @author Vinson
     * @date Jun 18, 2019 2:29:45 PM
     * @param pwd
     * @return
     * @return char[]
     */
    char[] encryptionPwd(char[] pwd);

    /**
     * 断言密码是否正确
     *
     * @Title: assertPwd
     * @Description: 断言密码是否正确
     * @author Vinson
     * @date Jun 18, 2019 2:31:24 PM
     * @param inputPwd
     *            输入的密码；
     * @param targetPwd
     *            目标密码，加过密的密码
     * @return
     * @return boolean
     */
    boolean assertPwd(char[] inputPwd, char[] targetPwd);

}
