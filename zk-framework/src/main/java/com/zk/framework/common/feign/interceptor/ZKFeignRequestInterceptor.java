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
* @Title: ZKFeignRequestInterceptor.java 
* @author Vinson 
* @Package com.zk.framework.common.feign.interceptor
* @Description: TODO(simple description this file what to do. ) 
* @date May 16, 2022 9:16:20 AM 
* @version V1.0 
*/
package com.zk.framework.common.feign.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.security.common.ZKSecConstants;
import com.zk.security.principal.ZKSecDefaultServerPrincipal;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.pc.ZKSecDefaultPrincipalCollection;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.security.utils.ZKSecSecurityUtils;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/** 
* @ClassName: ZKFeignRequestInterceptor 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFeignRequestInterceptor implements RequestInterceptor {

    protected Logger log = LogManager.getLogger(this.getClass());

    String appName;

    ZKSecTicketManager tm;

    public ZKFeignRequestInterceptor(ZKSecTicketManager tm, String appName) {
        this.tm = tm;
        this.appName = appName;
    }

    /** (not Javadoc) 
    * <p>Title: apply</p> 
    * <p>Description: </p> 
    * @param template 
    * @see feign.RequestInterceptor#apply(feign.RequestTemplate) 
    */
    @Override
    public void apply(RequestTemplate requestTemplate) {
//        HttpServletRequest hReq = ZKWebUtils.getRequest();
//        if (hReq == null) {
//            return;
//        }
        ZKSecPrincipalCollection<String> sPc = new ZKSecDefaultPrincipalCollection<String>();
        // 取微服务间令牌
        String sTkId = tm.generateTkId().toString();
//        String sTkId = (String) tm.generateTkId(appName);
        ZKSecTicket sTk = tm.getTicket(sTkId);
        if (sTk == null) {
            // 微服务还未有令牌，创建微服务令牌
            sTk = tm.createSecTicket(sTkId, ZKSecTicket.KeySecurityType.Server);
            // 创建一个微服务身份；
            ZKSecDefaultServerPrincipal<String> sP = new ZKSecDefaultServerPrincipal<String>(appName,
                    ZKSecPrincipal.OS_TYPE.UNKNOWN, appName, ZKSecPrincipal.KeyType.Distributed_server, appName);
            sP.setPrimary(true);

            sPc.add("distributedRealm", sP);
            log.info("[^_^:20220516-1120-001] 服务[{}]请求其他微服务，创建微服务身份", this.appName);
        }

        if (sTkId != null) {
            // 将当前令牌 同步到请求中
            System.out.println("[^_^:20240715-0134-001] 微服务身份信息，sTkId：" + sTkId);
            requestTemplate.header(ZKSecConstants.PARAM_NAME.TicketId, sTkId);
        }
        // 取当前会话令牌标识
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
        // 将当前会话所有身份添加到服务器令牌中
        if (tk != null) {
            log.info("[^_^:20220516-1120-002] 服务[{}]请求其他微服务，将当前会话所有身份添加到服务器令牌中", this.appName);
            sPc.addAll(tk.getPrincipalCollection());
        }
        // 设置身份集合到令牌
        sTk.setPrincipalCollection(sPc);
        log.info("[^_^:20220516-1116-001] 服务[{}]请求其他微服务，添加请求头令牌ID[{}]", this.appName, sTkId);
    }

}
