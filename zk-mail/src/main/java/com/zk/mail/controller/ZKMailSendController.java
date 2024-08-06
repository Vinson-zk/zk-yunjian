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
* @Title: ZKMailSendController.java 
* @author Vinson 
* @Package com.zk.mail.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date May 26, 2022 7:08:47 PM 
* @version V1.0 
*/
package com.zk.mail.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.mail.service.ZKMailSendService;

/** 
* @ClassName: ZKMailSendController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.mail}/${zk.mail.version}/mailSend")
public class ZKMailSendController extends ZKBaseController {

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        super.initBinder(binder);
        // map 类型转换
        binder.registerCustomEditor(Map.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                log.info("[^_^:20190808-1759-001]{ ZKBaseController class: String-escapeHtml4} -> " + text);
                setValue(text == null ? null : ZKJsonUtils.parseMap(text));
            }
        });
    }

    @Autowired
    ZKMailSendService mailSendService;

    /**
     * 
     *
     * @Title: mailSendHistoryGet
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 27, 2022 8:57:47 AM
     * @param typeCode
     * @param sendFlag
     * @param sendMailAddr
     *            发件箱
     * @param recipientMailAddr
     * @param companyCode
     * @param locale
     * @param attachments
     *            附件限制不能超过 5个；单个大小不能超过 50M
     * @param params
     * @return
     * @throws IOException
     * @return ZKMsgRes
     */
    @RequestMapping(value = "mailSend/{typeCode}", method = RequestMethod.POST)
    public ZKMsgRes mailSendByTypeCode(@PathVariable(value = "typeCode") String typeCode,
            @RequestParam(value = "sendFlag", required = false) String sendFlag,
            @RequestParam(value = "sendMailAddr", required = false) String sendMailAddr,
            @RequestParam(value = "recipientMailAddr", required = true) String recipientMailAddr,
            @RequestParam(value = "companyCode", required = false) String companyCode,
            @RequestParam(value = "locale", required = false) String locale,
            @RequestPart(value = "contentParams", required = false) Map<String, Object> contentParams,
//            @RequestBody Map<String, Object> contentParams,
//            @RequestParam(value = "contentParams", required = false) Map<String, Object> contentParams,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments)
            throws IOException {
        if (contentParams == null) {
            contentParams = new HashMap<>();
        }
        mailSendService.send(true, sendMailAddr, recipientMailAddr, sendFlag, typeCode, companyCode, locale,
                contentParams, attachments);
        return ZKMsgRes.asOk(null);
    }

}
