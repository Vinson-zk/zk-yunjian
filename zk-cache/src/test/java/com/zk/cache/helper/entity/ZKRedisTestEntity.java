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
* @Title: ZKRedisTestEntity.java 
* @author Vinson 
* @Package com.zk.core.helper.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 13, 2021 12:55:15 PM 
* @version V1.0 
*/
package com.zk.cache.helper.entity;

import java.io.Serializable;

/** 
* @ClassName: ZKRedisTestEntity 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKRedisTestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    int v1;

    String v2;

    ZKRedisTestChildEntity v3;

    public int test() {
        this.v1 = 6;
        return this.v1;
    }

    public int getV1() {
        return v1;
    }

    public String getV2() {
        return v2;
    }

    public ZKRedisTestChildEntity getV3() {
        return v3;
    }

    public void setV2(String v2) {
        this.v2 = v2;
    }

    public void setV3(ZKRedisTestChildEntity v3) {
        this.v3 = v3;
    }
}

