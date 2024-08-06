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
* @Title: ZKShiroRealm.java 
* @author Vinson 
* @Package com.zk.demo.shiro.realm 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 6:52:14 PM 
* @version V1.0 
*/
package com.zk.demo.shiro.realm;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

import com.zk.demo.shiro.common.ZKShiroPrincipal;

/** 
* @ClassName: ZKShiroRealm 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKShiroRealm extends AuthorizingRealm {

    /**
     * 登录认证回调函数
     * 
     * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {

        UsernamePasswordToken userPwdToken = (UsernamePasswordToken) authcToken;
        if ("admin".equals(userPwdToken.getUsername()) && "admin".equals(new String(userPwdToken.getPassword()))) {
            ZKShiroPrincipal p = new ZKShiroPrincipal();
            p.setName(userPwdToken.getUsername());
            return new SimpleAuthenticationInfo(p, userPwdToken.getPassword(), getName());
        }

        // 用户不存在
        if (!"admin".equals(userPwdToken.getUsername())) {
            throw new AuthenticationException("zk.test.000001");
        }

        // 密码错误
        if (!"admin".equals(new String(userPwdToken.getPassword()))) {
            throw new AuthenticationException("zk.test.000002");
        }

        return null;
    }

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
     * 
     * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ZKShiroPrincipal p = (ZKShiroPrincipal) getAvailablePrincipal(principals);

//        // 用户登录数量控制
//        userLonginCount(principal, ShiroSecurityUtils.getSession(), false);

        if (!SecurityUtils.getSubject().isAuthenticated() && !SecurityUtils.getSubject().isRemembered()) {
            return null;
        }

        if (p != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            if ("admin".equals(p.getName())) {
                info.addStringPermission("permission");
                info.addRole("role");
            }
            return info;
        }
        else {
            return null;
        }
    }

    /**
     * If
     * !{@link #isPermitted(org.apache.shiro.subject.PrincipalCollection, String)
     * isPermitted(permission)}, throws an <code>UnauthorizedException</code>
     * otherwise returns quietly.
     */
    @Override
    public void checkPermission(PrincipalCollection principals, String permission) {
        super.checkPermission(principals, permission);
    }

    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        super.checkPermission(permission, info);
    }

    @Override
    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        return super.isPermitted(permissions, info);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        return super.isPermitted(principals, permission);
    }

    @Override
    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        return super.isPermittedAll(permissions, info);
    }

    /**
     * 同一用户登录个数控制
     * 
     * @param principal
     *            用户
     * @param filterSession
     *            过虑的session
     * @param isLogin
     *            true-登录验证；false-rememberMe验证
     */
    protected void userLonginCount(ZKShiroPrincipal principal, Session filterSession, boolean isLogin) {

//        if (principal != null) {
//            if (principal instanceof ZKTestPrincipal) {
//                Collection<Session> sessions = sessionDao.getActiveSessions(true, principal, filterSession);
//                for (Session s : sessions) {
//                    if (ShiroSecurityUtils.getSubject().isAuthenticated()) {
//                        // 如果是登录进来的，踢出其他用户
//                        s.stop();
//                        s.setAttribute("ERR-CODE", "zk.sec.000012"); // zk.sec.000012=用户已在其他地方登录，请重新登录
//                    }
//                    else {
//                        // 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
//                        ShiroSecurityUtils.getSubject().logout();
//                        throw new MsgException("120008");
//                    }
//                }
//            }
//        }
    }

}
