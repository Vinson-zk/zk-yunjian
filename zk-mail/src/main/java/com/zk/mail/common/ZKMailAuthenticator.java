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
* @Title: ZKMailAuthenticator.java 
* @author Vinson 
* @Package com.zk.mail.common 
* @Description: TODO(simple description this file what to do. ) 
* @date May 25, 2022 1:54:14 AM 
* @version V1.0 
*/
package com.zk.mail.common;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;

/**
 * 邮件发送服务器 Authenticator
 * 
 * @ClassName: ZKMailAuthenticator
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKMailAuthenticator extends Authenticator {
    /**
     * @Fields account : email account
     */
    String account;

    /**
     * @Fields password : email password
     */
    String password;

    public ZKMailAuthenticator(String account, String password) {
        super();
        this.account = account;
        this.password = password;
    }

    /**
     * @return account sa
     */
    public String getAccount() {
        return account;
    }

    /**
     * @return password sa
     */
    public String getPassword() {
        return password;
    }


    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(getAccount(), getPassword());
    }
}
