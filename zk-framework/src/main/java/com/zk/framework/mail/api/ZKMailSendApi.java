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
* @Title: ZKMailSendApi.java 
* @author Vinson 
* @Package com.zk.framework.mail.api 
* @Description: TODO(simple description this file what to do. ) 
* @date May 27, 2022 3:52:10 PM 
* @version V1.0 
*/
package com.zk.framework.mail.api;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.zk.core.commons.ZKMsgRes;
import com.zk.framework.common.ZKApiConstants;

/** 
* @ClassName: ZKMailSendApi 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@FeignClient( //
        name = ZKApiConstants.YunJian_App.mail.name, //
        contextId = "com.zk.framework.mail.api.ZKMailSendApi" //
//        configuration = ZKFeignMultipartSupportConfig.class //
)
public interface ZKMailSendApi {

    // locale values
//    默认: public static final String _default = "_default";
//    简体中文: public static final String zh_CN = "zh_CN";
//    英语 English: public static final String en_US = "en_US";
//    繁体中文: public static final String zh_TW = "zh_TW";

//    @RequestMapping(method = RequestMethod.POST, consumes = {
//            MediaType.APPLICATION_FORM_URLENCODED_VALUE }, path = ZKApiConstants.YunJian_App.mail.apiPrefix
//                    + "/mailSend/mailSend/{typeCode}")
//    ZKMsgRes mailSendByTypeCode(@PathVariable(value = "typeCode") String typeCode,
//            @RequestParam(value = "sendFlag", required = false) String sendFlag,
//            @RequestParam(value = "sendMailAddr", required = false) String sendMailAddr,
//            @RequestParam(value = "recipientMailAddr", required = true) String recipientMailAddr,
//            @RequestParam(value = "companyCode", required = false) String companyCode,
//            @RequestParam(value = "locale", required = false) String locale);

    @RequestMapping(method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, path = ZKApiConstants.YunJian_App.mail.apiPrefix
                    + "/mailSend/mailSend/{typeCode}")
    ZKMsgRes mailSendByTypeCode(@PathVariable(value = "typeCode") String typeCode,
            @RequestParam(value = "sendFlag", required = false) String sendFlag,
            @RequestParam(value = "sendMailAddr", required = false) String sendMailAddr,
            @RequestParam(value = "recipientMailAddr", required = true) String recipientMailAddr,
            @RequestParam(value = "companyCode", required = false) String companyCode,
            @RequestParam(value = "locale", required = false) String locale,
            @RequestPart(value = "contentParams", required = false) Map<String, Object> contentParams);

    @RequestMapping(method = RequestMethod.POST, consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, path = ZKApiConstants.YunJian_App.mail.apiPrefix
                    + "/mailSend/mailSend/{typeCode}")
    ZKMsgRes mailSendByTypeCode(@PathVariable(value = "typeCode") String typeCode,
            @RequestParam(value = "sendFlag", required = false) String sendFlag,
            @RequestParam(value = "sendMailAddr", required = false) String sendMailAddr,
            @RequestParam(value = "recipientMailAddr", required = true) String recipientMailAddr,
            @RequestParam(value = "companyCode", required = false) String companyCode,
            @RequestParam(value = "locale", required = false) String locale,
            @RequestPart(value = "contentParams", required = false) Map<String, Object> contentParams,
//            @RequestBody Map<String, Object> params,
//            @RequestParam(value = "contentParams", required = false) Map<String, Object> params,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments);

}
