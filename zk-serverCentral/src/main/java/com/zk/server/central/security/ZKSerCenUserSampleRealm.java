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
 * @Title: ZKSerCenUserSampleRealm.java 
 * @author Vinson 
 * @Package com.zk.server.central.security 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:31:17 PM 
 * @version V1.0   
*/
package com.zk.server.central.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.zk.core.utils.ZKStringUtils;
import com.zk.security.principal.ZKSecDefaultUserPrincipal;
import com.zk.security.principal.ZKSecPrincipal.APP_TYPE;
import com.zk.security.principal.ZKSecPrincipal.OS_TYPE;
import com.zk.security.principal.ZKSecUserPrincipal;

/** 
* @ClassName: ZKSerCenUserSampleRealm 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenUserSampleRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && UsernamePasswordToken.class.isAssignableFrom(token.getClass());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        @SuppressWarnings("unchecked")
        ZKSecUserPrincipal<String> zkP = (ZKSecUserPrincipal<String>) getAvailablePrincipal(principals);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        info.addStringPermissions(getPermissions(zkP));
        info.addRoles(getRoles(zkP));

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken tk = (UsernamePasswordToken) token;
        String targetPwd = this.assertAccount(tk.getUsername());
        if (this.assertPwd(String.valueOf(tk.getPassword()), targetPwd)) {
            ZKSecUserPrincipal<String> p = new ZKSecDefaultUserPrincipal<String>(tk.getUsername(), tk.getUsername(),
                    tk.getUsername(), OS_TYPE.UNKNOWN, null, APP_TYPE.UNKNOWN, null, null, null, null);
            return new SimpleAuthenticationInfo(p, tk.getPassword(), getName());
        }
        return null;
    }

    /************************************************************************/

    @SuppressWarnings("unchecked")
    protected Collection<String> getPermissions(ZKSecUserPrincipal<?> zkP) {
        if (zkP.getUsername().equals("admin")) {
            return Arrays.asList("admin");
        }
        return Collections.EMPTY_LIST;
    }

    @SuppressWarnings("unchecked")
    protected Collection<String> getRoles(ZKSecUserPrincipal<?> zkP) {
        if (zkP.getUsername().equals("admin")) {
            return Arrays.asList("admin");
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 判断用户名，返回用户名对应密码
     *
     * @Title: assertAccount
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 26, 2019 12:26:22 AM
     * @param account
     * @return
     * @return String
     */
    protected String assertAccount(String account) {
        if (ZKStringUtils.isEmpty(account)) {
            throw new ZKSerCenAuthenticationException("zk.ser.cen.000012", null, null);
//            throw new ZKMsgResException("zk.ser.cen.000012");
        }
        if ("admin".equals(account)) {
            return "admin";
        }
        throw new ZKSerCenAuthenticationException("zk.ser.cen.000014", null, null);
    }

    /**
     * 判断密码是否正确
     *
     * @Title: assertPwd
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 26, 2019 12:31:01 AM
     * @param pwd
     * @param targetPwd
     * @return
     * @return boolean
     */
    protected boolean assertPwd(String pwd, String targetPwd) {
        if (targetPwd.equals(pwd)) {
            return true;
        }
        throw new ZKSerCenAuthenticationException("zk.ser.cen.000013", null, null);
    }

}
