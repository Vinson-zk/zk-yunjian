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
 * @Title: ZKExceptionHandlerResolver.java 
 * @author Vinson 
 * @Package com.zk.core.web.resolver 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 3:08:35 PM 
 * @version V1.0   
*/
package com.zk.core.web.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.zk.core.exception.ZKCodeException;
import com.zk.core.exception.ZKMsgException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.web.ZKMsgRes;

/** 
* @ClassName: ZKExceptionHandlerResolver 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKExceptionHandlerResolver implements HandlerExceptionResolver, Ordered {

    /** Logger available to subclasses. */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private int order = Ordered.HIGHEST_PRECEDENCE + 66;

    /**
     * (not Javadoc)
     * <p>
     * Title: getOrder
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see org.springframework.core.Ordered#getOrder()
     */
    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: resolveException
     * </p>
     * <p>
     * Description: d
     * </p>
     * 
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     * @see org.springframework.web.servlet.HandlerExceptionResolver#resolveException(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      java.lang.Exception)
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest hReq, HttpServletResponse hRes, Object handler,
            Exception ex) {

        /* 使用response返回 */
        hRes.setStatus(HttpStatus.OK.value()); // 设置状态码
        hRes.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置ContentType
        hRes.setCharacterEncoding("UTF-8"); // 避免乱码
        hRes.setHeader("Cache-Control", "no-cache, must-revalidate");

        // 数据验证异常：com.zk.utils.exception.ZKValidatorException
        if (ex instanceof ZKValidatorException) {
            ZKValidatorException zkValidatorE = (ZKValidatorException) ex;
            // 数据验证异常
            log.error("[>_<:20191218-1424-002] {}",
                    ZKJsonUtils.writeObjectJson(zkValidatorE.getMessagePropertyAndMessageAsMap()));
            zkValidatorE.printStackTrace();
//            ZKMsgRes msgRes = ZKMsgRes.as("zk.000002", null, zkValidatorE.getMessagePropertyAndMessageAsList());
            ZKMsgRes msgRes = ZKMsgRes.as("zk.000002", null, zkValidatorE.getMessagePropertyAndMessageAsMap());
            msgRes.write(hRes);
        }
        else if (ex instanceof ZKCodeException) {
            ZKCodeException codeE = (ZKCodeException) ex;
            // 根据异常码，国际化异常信息
//            ZKMsgRes msgRes = new ZKMsgRes(codeE.getCode(), null, codeE.getMsgArgs(), codeE.getData());
            ZKMsgRes msgRes = ZKMsgRes.as(codeE);
            msgRes.write(hRes);
        }
        else if (ex instanceof ZKMsgException) {
            ZKMsgException msgE = (ZKMsgException) ex;
            ZKMsgRes msgRes = ZKMsgRes.as(msgE);
            msgRes.write(hRes);
        }
        else {
            log.error("[>_<:20191218-1424-001] ZKExceptionHandler -> status code:{}; exception class:{}",
                    hRes.getStatus(), ex.getClass().getName());
            ex.printStackTrace();
            ZKMsgRes msgRes = new ZKMsgRes("zk.1", null, ex.getMessage());
            msgRes.write(hRes);
        }
        return new ModelAndView();

    }

}
