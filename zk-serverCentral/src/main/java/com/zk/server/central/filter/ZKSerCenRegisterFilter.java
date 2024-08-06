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
* @Title: ZKSerCenRegisterFilter.java 
* @author Vinson 
* @Package com.zk.server.central.filter 
* @Description: TODO(simple description this file what to do.) 
* @date Mar 12, 2020 9:09:28 PM 
* @version V1.0 
*/
package com.zk.server.central.filter;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.ZKMsgRes.ResCodeType;
import com.zk.core.web.support.servlet.filter.ZKOncePerFilter;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.framework.serCen.ZKSerCenDecode;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
* @ClassName: ZKSerCenRegisterFilter 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenRegisterFilter extends ZKOncePerFilter {

    protected Logger log = LogManager.getLogger(getClass());

    private ZKSerCenDecode zkSerCenDecode;

    public ZKSerCenRegisterFilter(ZKSerCenDecode zkSerCenDecode) {
        this.zkSerCenDecode = zkSerCenDecode;
    }

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest hReq = (HttpServletRequest) request;
        HttpServletResponse hRes = (HttpServletResponse) response;

        log.info("[^_^:20200312-2110-001] ZKSerCenRegisterFilter httpReq -> uri: {}:{}{}", hReq.getRemoteAddr(),
                hReq.getRemotePort(), hReq.getRequestURI());
//        log.info("[^_^:20200312-2110-002] ZKSerCenRegisterFilter getRequestURI:{}", hReq.getRequestURI());
//        log.info("[^_^:20200312-2110-002] ZKSerCenRegisterFilter getServletPath:{}", hReq.getServletPath());

        // 判断需要拦截的 请求
        if (isDoingFilter(hReq.getRequestURI())) {
            if (zkSerCenDecode.assertServerClient(hReq)) {
                chain.doFilter(request, response);
            }
            else {
                log.error("[>_<:20200629-1622-001] 服务中心权限认证失败，请联系管理员；");
                ZKMsgRes zkMsgRes = ZKMsgRes.asCode(null, ResCodeType.busniness, "zk.ser.cen.000001");
                hRes.setStatus(601);
                hRes.setHeader("errCode", "zk.ser.cen.000001");
                ZKServletUtils.renderString(hRes, zkMsgRes.toString(), null);
            }
        }
        else {
            chain.doFilter(request, response);
        }
    }

    final Pattern[] urlPatterns = { Pattern.compile("^/eureka/apps/(.*)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("^/eureka/vips/(.*)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("^/eureka/svips/(.*)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("^/eureka/instances/(.*)", Pattern.CASE_INSENSITIVE) };

    /**
     * 仅拦截处理，客户端注册，下线等相关请求；而服务器之间的请求，如服务同步等请求跳过不处理
     *
     * @Title: isDoingFilter
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 14, 2020 6:00:50 PM
     * @param url
     * @return
     * @return boolean
     */
    protected boolean isDoingFilter(String url) {

        for (Pattern urlPattern : urlPatterns) {
            if (urlPattern.matcher(url).matches()) {
                return true;
            }
        }

        return false;
    }

}
