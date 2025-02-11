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
* @Title: ZKTestSecHelper.java 
* @author Vinson 
* @Package com.zk.test.helper.sec 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 22, 2024 2:49:07 PM 
* @version V1.0 
*/
package com.zk.test.helper.sec;

import org.springframework.http.HttpHeaders;

import com.zk.security.common.ZKSecConstants;
import com.zk.security.principal.ZKSecDefaultServerPrincipal;
import com.zk.security.principal.ZKSecDefaultUserPrincipal;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.pc.ZKSecDefaultPrincipalCollection;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.ticket.ZKSecTicketManager;

/** 
* @ClassName: ZKTestSecHelper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKTestSecHelper {

    // 给请求头设置用户微信信息，满足测试权限校验
    public static void setHeaderUserInfo(ZKSecTicketManager tm, String realmName, HttpHeaders headers) {

        ZKSecTicket tk = tm.createSecTicket(tm.generateTkId());
        ZKSecPrincipalCollection<String> pc = new ZKSecDefaultPrincipalCollection<String>();
        ZKSecPrincipal<String> p = new ZKSecDefaultUserPrincipal<String>("-1", "test-user", "test-user",
                ZKSecPrincipal.OS_TYPE.UNKNOWN, null, ZKSecPrincipal.APP_TYPE.web, null, "test-group-code", "-1",
                "test-company-code");
        pc.add(realmName, p);
        tk.setPrincipalCollection(pc);
        headers.add(ZKSecConstants.PARAM_NAME.TicketId, tk.getTkId().toString());

    }

    // 给请求头设置微服务身份信息，满足测试权限校验
    public static void setHeaderServerInfo(ZKSecTicketManager tm, String realmName, HttpHeaders headers) {

        ZKSecPrincipalCollection<String> sPc = new ZKSecDefaultPrincipalCollection<String>();
        // 取微服务间令牌
        String sTkId = tm.generateTkId().toString();
//        String sTkId = (String) tm.generateTkId(appName);
        ZKSecTicket sTk = tm.getTicket(sTkId);

        // 微服务还未有令牌，创建微服务令牌
        sTk = tm.createSecTicket(sTkId, ZKSecTicket.KeySecurityType.Server);
        // 创建一个微服务身份；
        ZKSecDefaultServerPrincipal<String> sP = new ZKSecDefaultServerPrincipal<String>("test-appName",
                ZKSecPrincipal.OS_TYPE.UNKNOWN, "test-appName", ZKSecPrincipal.KeyType.Distributed_server,
                "test-appName");
        sP.setPrimary(true);
        sPc.add(realmName, sP);
        // 设置身份集合到令牌
        sTk.setPrincipalCollection(sPc);
        headers.add(ZKSecConstants.PARAM_NAME.TicketId, sTkId);
    }

}
