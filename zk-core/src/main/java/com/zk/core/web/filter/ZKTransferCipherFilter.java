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
 * @Package com.zk.core.web.filter 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:59:39 PM 
 * @version V1.0   
*/
package com.zk.core.web.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zk.core.encrypt.ZKTransferCipherManager;
import com.zk.core.encrypt.support.ZKSampleRsaAesTransferCipherManager;
import com.zk.core.exception.ZKMsgException;
import com.zk.core.exception.ZKUnknownException;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.web.ZKMsgRes;
import com.zk.core.web.wrapper.ZKRequestWrapper;
import com.zk.core.web.wrapper.ZKResponseWrapper;

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
            byte[] msgData = ZKJsonUtils.writeObjectJson(msgRes).getBytes("UTF-8");
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
        HttpServletRequest httpReq = (HttpServletRequest) request;
        if (this.transferCipherManager.isEnc(httpReq)) {
            try {
                ZKRequestWrapper zkReq = this.transferCipherManager.decrypt(httpReq);
                HttpServletResponse httpRes = (HttpServletResponse) response;
                ZKResponseWrapper zkRes = new ZKResponseWrapper(httpRes);
                chain.doFilter(zkReq, zkRes);
                this.transferCipherManager.encrypt(zkReq, zkRes);
            }
            catch(ZKMsgException zkMsgE) {
                zkMsgE.printStackTrace();
                printMsg(response, ZKMsgRes.as(zkMsgE));

            }
            catch(ZKUnknownException zkE) {
                zkE.printStackTrace();
                // "数据传输未达安全要求"
                printMsg(response, new ZKMsgRes("zk.000001", null, null));

            }
            catch(Exception e) {
                e.printStackTrace();
                printMsg(response, new ZKMsgRes("1", null, null));
            }
        }
        else {
            chain.doFilter(request, response);
        }
    }

}
