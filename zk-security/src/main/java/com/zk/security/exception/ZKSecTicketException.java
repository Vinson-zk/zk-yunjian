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
* @Title: ZKSecTicketException.java 
* @author Vinson 
* @Package com.zk.security.exception 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 14, 2021 7:28:58 PM 
* @version V1.0 
*/
package com.zk.security.exception;
/** 
* @ClassName: ZKSecTicketException 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecTicketException extends ZKSecCodeException {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = -3408817770987132128L;

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param code
     */
    public ZKSecTicketException(String code) {
        super(code); // TODO Auto-generated constructor stub
    }

    public ZKSecTicketException(String code, String msg, Object[] msgArgs, Object data) {
        super(code, msg, msgArgs, data);
    }

}
