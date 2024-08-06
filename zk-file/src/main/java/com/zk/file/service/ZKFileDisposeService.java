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
* @Title: ZKFileDisposeService.java 
* @author Vinson 
* @Package com.zk.file.service 
* @Description: TODO(simple description this file what to do. ) 
* @date May 12, 2023 11:51:01 AM 
* @version V1.0 
*/
package com.zk.file.service;

import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.zk.core.commons.ZKFileTransfer;
import com.zk.core.commons.ZKFileType;
import com.zk.file.entity.ZKFileInfo;

/** 
* @ClassName: ZKFileDisposeService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFileDisposeService {

    @Autowired
    ZKFileTransfer zkFileTransfer;

    public String rootPath = ""; // 要以 "/" 结尾

    public ZKFileDisposeService() {
//        this.rootPath = "/Users/bs/bs_temp/zk-yunjian/fileUpload";
//        /Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-file/target/test-classes/fileUpload
        this("target/fileUpload/");
    }

    public ZKFileDisposeService(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * @return rootPath sa
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     *
     * @Title: doDisposeFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 1, 2024 4:14:50 PM
     * @param path
     *            路径，以 / 开头
     * @param fileName
     * @param inputStream
     * @param isOverride
     * @param appendFlag
     * @param outFileSize
     * @param outFileType
     * @return
     * @return String
     */
    public String doDisposeFile(String path, String fileName, InputStream inputStream, long[] outFileSize,
            ZKFileType[] outFileType) {
        return zkFileTransfer.transferFile(inputStream, this.getRootPath() + path, fileName, true, false, outFileSize, outFileType);
    }

    public long doGetFile(ZKFileInfo zkFileInfo, OutputStream os) {
        return this.zkFileTransfer.getFile(this.getRootPath() + zkFileInfo.getUri(), os);
    }
}
