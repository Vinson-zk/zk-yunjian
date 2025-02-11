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
* @Title: ZKSecAbstractTicketManager.java 
* @author Vinson 
* @Package com.zk.security.ticket 
* @Description: TODO(simple description this file what to do. ) 
* @date May 16, 2022 10:33:22 AM 
* @version V1.0 
*/
package com.zk.security.ticket;

import java.io.Serializable;
import java.util.UUID;

/** 
* @ClassName: ZKSecAbstractTicketManager 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAbstractTicketManager implements ZKSecTicketManager {


    public static interface Key {
        /**
         * 自动生成令牌ID的ID前缀
         */
        public static final String TK_ID_auto_prefix = "zk_sec_TK_ID_auto_prefix_";

        /**
         * 自定令牌ID的ID前缀
         */
        public static final String TK_ID_pack_prefix = "zk_sec_TK_ID_pack_prefix_";
    }

    @Override
    public String generateTkId() {
        return Key.TK_ID_auto_prefix + UUID.randomUUID().toString().replaceAll("-", "");
    }

    @Override
    public String generateTkId(Serializable tkId) {
        return Key.TK_ID_pack_prefix + (String) tkId;
    }

    public ZKSecTicket createTicket() {
        return this.createSecTicket(this.generateTkId());
    }

    public ZKSecTicket createSecTicket() {
        return this.createSecTicket(this.generateTkId());
    }

}
