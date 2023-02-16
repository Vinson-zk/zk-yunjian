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
* @Title: ZKWechatUtils.java 
* @author Vinson 
* @Package com.zk.wechat.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 23, 2021 2:12:55 PM 
* @version V1.0 
*/
package com.zk.wechat.common;

import java.io.File;

import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKStringUtils;

/** 
* @ClassName: ZKWechatUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWechatUtils {

    private static String format(String path) {
        if (path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }

    // 返回的路径结尾没有斜扛
    public static String getFilePath() {
        String path = ZKEnvironmentUtils.getString("zk.wechat.wx.file.path", "");
        return format(path);
    }

    public static String getFilePath(String path) {
        String pathPrefix = getFilePath();
        if (ZKStringUtils.isEmpty(path)) {
            return path;
        }
        return pathPrefix + File.separator + path;
    }

}
