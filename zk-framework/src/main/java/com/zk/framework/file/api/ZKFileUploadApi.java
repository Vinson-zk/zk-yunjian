/** 
* Copyright (c) 2012-2023 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKFileUploadApi.java 
* @author Vinson 
* @Package com.zk.framework.file.api 
* @Description: TODO(simple description this file what to do. ) 
* @date Dec 27, 2023 4:42:49 PM 
* @version V1.0 
*/
package com.zk.framework.file.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zk.core.commons.ZKMsgRes;
import com.zk.framework.common.ZKApiConstants;

import feign.Response;

/** 
* @ClassName: ZKFileUploadApi 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@FeignClient(name = ZKApiConstants.YunJian_App.file.name, contextId = "com.zk.framework.file.api.ZKFileUploadApi")
public interface ZKFileUploadApi {


    @RequestMapping(method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, path = ZKApiConstants.YunJian_App.mail.apiPrefix
                    + "/fileInfo/f/upload")
    ZKMsgRes uploadMultipart(@RequestParam(value = "companyCode", required = true) String companyCode,
            @RequestParam(value = "saveGroupCode", required = false) String saveGroupCode,
            @RequestParam(value = "dirCode", required = false) String dirCode,
            @RequestParam(name = "securityType", required = false, defaultValue = "1") int securityType,
            @RequestParam(name = "actionScope", required = false, defaultValue = "0") int actionScope,
            @RequestParam(value = "mfs") List<MultipartFile> mfs);

    @RequestMapping(method = RequestMethod.GET, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, path = ZKApiConstants.YunJian_App.mail.apiPrefix
                    + "/fileInfo/f/getFile")
    Response getFile(@RequestParam("pkId") String pkId,
            @RequestParam(value = "isDownload", required = false, defaultValue = "false") boolean isDownload);


}
