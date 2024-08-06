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
* @Title: ZKSecAbstractTicket.java 
* @author Vinson 
* @Package com.zk.security.ticket 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 14, 2021 6:46:13 PM 
* @version V1.0 
*/
package com.zk.security.ticket;

import java.io.Serializable;

/** 
* @ClassName: ZKSecAbstractTicket 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAbstractTicket implements ZKSecTicket {

    private static final long serialVersionUID = 1L;

    protected ZKSecAbstractTicket(Serializable key) {
        this.key = key;
    }

    private Serializable key;

    @Override
    public Serializable getTkId() {
        return key;
    }

    protected void setTkId(Serializable key) {
        this.key = key;
    }

}
