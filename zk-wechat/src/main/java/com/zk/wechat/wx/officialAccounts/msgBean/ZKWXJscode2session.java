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
* @Title: ZKWXJscode2session.java 
* @author Vinson 
* @Package com.zk.wechat.wx.officialAccounts.msgBean 
* @Description: TODO(simple description this file what to do. ) 
* @date May 23, 2022 9:45:02 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.officialAccounts.msgBean;

import java.io.Serializable;

/** 
* @ClassName: ZKWXJscode2session 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXJscode2session implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识的 openid
     */
    String openid;

    /**
     * 会话密钥
     */
    String sessionKey;

    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。
     */
    String unionid;

    /**
     * @return openid sa
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @return sessionKey sa
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * @return unionid sa
     */
    public String getUnionid() {
        return unionid;
    }

    /**
     * @param openid
     *            the openid to set
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @param sessionKey
     *            the sessionKey to set
     */
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    /**
     * @param unionid
     *            the unionid to set
     */
    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }


}
