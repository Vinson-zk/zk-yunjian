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
* @Title: ZKBusinessException.java 
* @author Vinson 
* @Package com.zk.core.exception 
* @Description: TODO(simple description this file what to do. ) 
* @date May 27, 2024 11:36:09 AM 
* @version V1.0 
*/
package com.zk.core.exception;

import java.util.Locale;

import com.zk.core.exception.base.ZKCodeException;
import com.zk.core.utils.ZKMsgUtils;

/**
 * 身份认证异常;
 * 
 * @ClassName: ZKBusinessException
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSecAuthenticationException extends ZKCodeException {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    // =======================================
    public static ZKSecAuthenticationException as(String code) {
        return as(code, null);
    }

    public static ZKSecAuthenticationException as(String code, Object data) {
        return as(code, data, (Object[]) null);
    }

    public static ZKSecAuthenticationException as(String code, Object data, Object... msgArgs) {
        return as(null, code, data, msgArgs);
    }

    public static ZKSecAuthenticationException as(Locale locale, String code, Object data, Object... msgArgs) {
        return asMsg(code, ZKMsgUtils.getMessage(locale, code, msgArgs), data, msgArgs);
    }

    /**
     * 自带 msg，不再为对 msg 进行国际化；
     *
     * @Title: asMsg
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 27, 2024 3:34:26 PM
     * @param code
     * @param msg
     * @param data
     * @param msgArgs
     * @return
     * @return ZKBusinessException
     */
    public static ZKSecAuthenticationException asMsg(String code, String msg, Object data, Object... msgArgs) {
        return new ZKSecAuthenticationException(code, msg, data, msgArgs);
    }

    // ------------------------------------
    protected ZKSecAuthenticationException(String code, String msg, Object data, Object... msgArgs) {
        super(code, msg, data, msgArgs); // TODO Auto-generated constructor stub
    }

}
