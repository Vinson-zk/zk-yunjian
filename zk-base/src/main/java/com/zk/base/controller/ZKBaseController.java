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
 * @Title: ZKBaseController.java 
 * @author Vinson 
 * @Package com.zk.base.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 2:27:12 PM 
 * @version V1.0   
*/
package com.zk.base.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.commons.data.ZKJsonArray;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;

import jakarta.servlet.http.HttpServletResponse;

/** 
* @ClassName: ZKBaseController 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
//public class ZKBaseController<ID extends Serializable, T extends ZKBaseEntity<ID>, D extends ZKBaseDao<ID, T>, S extends ZKBaseService<ID, T, D>> {
public class ZKBaseController {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

//    /**
//     * Service 对象
//     */
//    @Autowired
//    protected S service;

    /**
     * 客户端返回JSON字符串
     * 
     * @param response
     * @param object
     * @return
     */
    protected String renderString(HttpServletResponse response, Object object) {
        return renderString(response, ZKJsonUtils.toJsonStr(object), "application/json");
    }

    /**
     * 客户端返回字符串
     * 
     * @param response
     * @param string
     * @return
     */
    protected String renderString(HttpServletResponse response, String string, String type) {
        try {
            response.reset();
            response.setContentType(type);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
            return null;
        }
        catch(IOException e) {
            return null;
        }
    }

    /**
     * 分页数据对象
     */
    public static final String PageName = "page";

    /*******************************************************************/
    /*** 异常 绑定；注：设置异常处理适配器 exceptionHandlerResolver 后， */
    /*** 异常 绑定；注：这些绑定后； filter 拦截中拦截不到了，这时不设置，统一由filter 处理 */
    /*******************************************************************/

//    /**
//     * 参数绑定异常; 做为一般异常处理
//     *
//     * @Title: bindException
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Aug 21, 2020 11:12:01 AM
//     * @param e
//     * @return
//     * @return String
//     */
//    @ExceptionHandler({ BindException.class })
//    public String bindException(BindException e) {
//        // zk.000005=请求参数解析失败
//        ZKSystemException se = ZKSystemException.as("zk.000005", e);
//        ZKMsgRes msgRes = ZKMsgRes.as(null, se);
//        return msgRes.toString();
//    }
//
//    /**
//     * 数据验证失败异常
//     *
//     * @Title: bindException
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Aug 21, 2020 11:02:18 AM
//     * @return
//     * @return String
//     */
//    @ExceptionHandler({ ZKValidatorException.class })
//    public String bindException(ZKValidatorException zkValidatorE) {
//        ZKMsgRes msgRes = ZKMsgRes.as(null, zkValidatorE);
//        return msgRes.toString();
//    }
//
//    /**
//     * 业务异常，错消息已国际化处理异常;
//     *
//     * @Title: bindException
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Aug 21, 2020 11:11:50 AM
//     * @param msgE
//     * @return
//     * @return String
//     */
//    @ExceptionHandler({ ZKBusinessException.class })
//    public String bindException(ZKBusinessException be) {
//        ZKMsgRes msgRes = ZKMsgRes.as(null, be);
//        return msgRes.toString();
//    }
//
//    /**
//     * 系统异常
//     *
//     * @Title: bindException
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Aug 21, 2020 11:11:42 AM
//     * @param e
//     * @return
//     * @return String
//     */
//    @ExceptionHandler({ Exception.class })
//    public String bindException(Exception e) {
//        log.error("[>_<:20200821-1121-001] exception class:{}",
//                e.getClass().getName());
//        ZKMsgRes msgRes = ZKMsgRes.as(null, e);
//        return msgRes.toString();
//    }
//
//    @ExceptionHandler({ ZKSystemException.class })
//    public String bindException(ZKSystemException se) {
//        ZKMsgRes msgRes = ZKMsgRes.as(null, se);
//        return msgRes.toString();
//    }

//  /**
//   * 授权登录异常
//   */
//  @ExceptionHandler({ AuthenticationException.class })
//  public String authenticationException() {
//      return "error/403";
//  }

    /*******************************************************************/
    /*** 参数 绑定 */
    /*******************************************************************/
    /**
     * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        
//        if (PageName.equals(binder.getObjectName())) {
//            binder.registerCustomEditor(Object.class, new PropertyEditorSupport() {
//                @Override
//                public void setAsText(String text) {
//
//                }
//                // @Override
//                // public String getAsText() {
//                // Object value = getValue();
//                // return value != null ? value.toString() : null;
//                // }
//            });
//        }
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                log.info("[^_^:20190808-1759-001]{ ZKBaseController class: String-escapeHtml4} -> " + text);
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                log.info("[^_^:20190808-1759-003] { ZKBaseController class: Date-binder } -> " + text);
                try {
                    setValue(ZKDateUtils.parseDate(text));
                }
                catch(ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        // ZKJson 类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(ZKJson.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                log.info("[^_^:20190808-1759-002]{ ZKBaseController class: ZKJson-binder} -> " + text);
                if (ZKStringUtils.isEmpty(text)) {
                    setValue(null);
                }
                else {
                    text = StringEscapeUtils.unescapeHtml4(text);
                    text = ZKEncodingUtils.urlDecoder(text);
                    setValue(ZKJson.parse(text));
                }
            }
        });
        // ZKJson 类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(ZKJsonArray.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                log.info("[^_^:20220115-1521-001]{ ZKBaseController class: ZKJsonArray-binder} -> " + text);
                if (ZKStringUtils.isEmpty(text)) {
                    setValue(null);
                }
                else {
                    text = StringEscapeUtils.unescapeHtml4(text);
                    text = ZKEncodingUtils.urlDecoder(text);
                    setValue(ZKJsonArray.parse(text));
                }
            }
        });
    }

}
