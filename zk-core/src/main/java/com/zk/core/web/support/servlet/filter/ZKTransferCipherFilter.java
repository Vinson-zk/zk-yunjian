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
 * @Title: ZKTransferCipherFilter.java 
 * @author Vinson 
 * @Package com.zk.core.web.support.servlet.filter
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:59:39 PM 
 * @version V1.0   
*/
package com.zk.core.web.support.servlet.filter;

import java.io.IOException;
import java.io.OutputStream;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.exception.ZKSystemException;
import com.zk.core.exception.sub.ZKTransferCipherException;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.web.encrypt.ZKSampleRsaAesTransferCipherManager;
import com.zk.core.web.encrypt.ZKTransferCipherManager;
import com.zk.core.web.support.servlet.wrapper.ZKEncHttpServletRequestWrapper;
import com.zk.core.web.support.servlet.wrapper.ZKEncHttpServletResponseWrapper;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.core.web.utils.ZKWebUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/** 
* @ClassName: ZKTransferCipherFilter 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKTransferCipherFilter extends ZKOncePerFilter implements Filter {

    ZKTransferCipherManager transferCipherManager = new ZKSampleRsaAesTransferCipherManager();

    public ZKTransferCipherFilter() {

    }

    public ZKTransferCipherFilter(ZKTransferCipherManager transferCipherManager) {
        this.transferCipherManager = transferCipherManager;
    }

    /**
     * @return zkTransferCipherManager
     */
    public ZKTransferCipherManager getArTransferCipherManager() {
        return transferCipherManager;
    }

    /**
     * @param zkTransferCipherManager
     *            the zkTransferCipherManager to set
     */
    public void setArTransferCipherManager(ZKTransferCipherManager zkTransferCipherManager) {
        this.transferCipherManager = zkTransferCipherManager;
    }

    private void printMsg(ServletResponse response, ZKMsgRes msgRes) {
        OutputStream out = null;
        try {
            byte[] msgData = ZKJsonUtils.toJsonStr(msgRes).getBytes("UTF-8");
            out = response.getOutputStream();
            out.write(msgData);
            out.flush();
            out.close();
        }
        catch(IOException e) {
            throw ZKExceptionsUtils.unchecked(e);
        } finally {
            ZKStreamUtils.closeStream(out);
        }
    }

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        ZKEncHttpServletRequestWrapper zkEncReq = new ZKEncHttpServletRequestWrapper(ZKServletUtils.toHttp(request));
        if (this.transferCipherManager.isEnc(zkEncReq)) {
            try {
                this.transferCipherManager.decrypt(zkEncReq);
                ZKEncHttpServletResponseWrapper zkEncRes = new ZKEncHttpServletResponseWrapper(
                        ZKServletUtils.toHttp(response));
                chain.doFilter(zkEncReq, zkEncRes);
                this.transferCipherManager.encrypt(zkEncReq, zkEncRes);
            }
            catch(ZKTransferCipherException zkE) {
                zkE.printStackTrace();
                // "数据传输未达安全要求"
                ZKSystemException se = ZKSystemException.as(ZKWebUtils.getLocale(zkEncReq), "zk.000001", null,
                        (Object[]) null);
                printMsg(response, ZKMsgRes.as(null, se));

            }
            catch(Exception e) {
                e.printStackTrace();
                printMsg(response, ZKMsgRes.as(ZKWebUtils.getLocale(zkEncReq), e));
            }
        }
        else {
            chain.doFilter(request, response);
        }
    }

    /**
     * 
     * @param filterConfig
     * @throws ServletException
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

    /**
     * 
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

}
