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
* @Title: ZKFileTransferService.java 
* @author Vinson 
* @Package com.zk.file.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 10, 2023 5:51:27 PM 
* @version V1.0 
*/
package com.zk.file.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zk.core.commons.ZKFileTransfer;
import com.zk.file.entity.ZKFileInfo;

/** 
* @ClassName: ZKFileTransferService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
public class ZKFileTransferService {

    public static final String rootPath = "/Users/bs/bs_temp/zk-yunjian/fileUpload";

    @Autowired
    ZKFileTransfer zkFileTransfer;

    public File getFile(ZKFileInfo zkFileInfo) {
        return null;
    }

    public File transferFile(ZKFileInfo zkFileInfo, MultipartFile multipartFile) {
        zkFileTransfer.transferFile(path, isNewName, isOverride, multipartFile);
        return null;
    }

}
