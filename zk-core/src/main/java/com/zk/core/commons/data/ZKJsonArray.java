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
* @Title: ZKJsonArray.java 
* @author Vinson 
* @Package com.zk.core.commons.data 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 11, 2022 10:01:20 PM 
* @version V1.0 
*/
package com.zk.core.commons.data;
/** 
* @ClassName: ZKJsonArray 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

import com.alibaba.fastjson2.JSONArray;
import com.zk.core.utils.ZKStringUtils;

public class ZKJsonArray extends JSONArray {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    public ZKJsonArray() {

    }

    public ZKJsonArray(String value) {
        if (!ZKStringUtils.isEmpty(value)) {
            this.addAll(JSONArray.parseArray(value));
        }
    }

    public static ZKJsonArray parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        ZKJsonArray zkJsonArray = new ZKJsonArray();
        zkJsonArray.addAll(jsonArray);
        return zkJsonArray;
    }

    public static ZKJsonArray parse(String jsonArrayStr) {
        ZKJsonArray zkJsonArray = new ZKJsonArray();
        zkJsonArray.addAll(JSONArray.parseArray(jsonArrayStr));
        return zkJsonArray;
    }

}
