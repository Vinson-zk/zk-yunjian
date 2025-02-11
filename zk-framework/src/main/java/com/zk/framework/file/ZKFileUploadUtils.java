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
* @Title: ZKFileUploadUtils.java 
* @author Vinson 
* @Package com.zk.framework.file.api 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 23, 2024 6:17:37 PM 
* @version V1.0 
*/
package com.zk.framework.file;

import java.util.Collections;
import java.util.List;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.framework.file.entity.ZKFileInfo;

/** 
* @ClassName: ZKFileUploadUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFileUploadUtils {


    public static List<ZKFileInfo> getFileInfos(ZKMsgRes resMsg) {

        if (isOk(resMsg)) {
            List<ZKFileInfo> fis = getFileInfos(resMsg.getDataStr());
            return fis;
        }

        return Collections.emptyList();
    }
    
    public static List<ZKFileInfo> getFileInfos(String resStr) {

        List<ZKFileInfo> fis = ZKJsonUtils.parseList(resStr, ZKFileInfo.class);

        return fis;
    }
    
    protected static boolean isOk(ZKMsgRes resMsg) {
        return resMsg.isOk();
    }

}
