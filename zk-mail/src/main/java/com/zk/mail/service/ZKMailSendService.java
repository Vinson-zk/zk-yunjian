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
* @Title: ZKMailSendService.java 
* @author Vinson 
* @Package com.zk.mail.service 
* @Description: TODO(simple description this file what to do. ) 
* @date May 24, 2022 12:30:58 AM 
* @version V1.0 
*/
package com.zk.mail.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKStreamDataSource;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.mail.common.ZKMailAuthenticator;
import com.zk.mail.entity.ZKMailSendHistory;
import com.zk.mail.entity.ZKMailTemplate;
import com.zk.mail.entity.ZKMailType;
import com.zk.mail.utils.ZKMailUtils;

import jakarta.activation.DataSource;

/**
 * @ClassName: ZKMailSendService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
public class ZKMailSendService {

    protected Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    ZKMailSendHistoryService mailSendHistoryService;

    @Autowired
    ZKMailTemplateService mailTemplateService;

    @Autowired
    ZKMailTypeService mailTypeService;

    @Autowired
    ZKMailAuthenticator mailAuthenticator;

    @Autowired
    Properties mailProperties;

    // 允许一封邮件发送的附件个数; 默认3个
    @Value("${zk.mail.attachment.count:3}")
    int attachmentCount;

    // 允许的邮件中单个附件大小，单位 b; 默认10M
    @Value("${zk.mail.attachment.size:10485760}")
    long attachmentSize;

    /**
     * 校验附件个数
     *
     * @Title: checkAttachmentCount
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 27, 2022 9:24:46 AM
     * @param length
     * @return
     * @return boolean
     */
    private boolean checkAttachmentCount(int length) {
        if (length > this.attachmentCount) {
            // 邮件附件过多; zk.mail.000010=邮件附件最多允许[{0}]个
            log.error("[>_<:20220527-0915-001] 邮件附件过多; zk.mail.000010=邮件附件最多允许[{}]个", this.attachmentCount);
            throw ZKBusinessException.as("zk.mail.000010", null, this.attachmentCount);
//            return false;
        }
        return true;
    }

    /**
     * 校验附件大小
     *
     * @Title: checkAttachmentSize
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 27, 2022 9:24:59 AM
     * @param size
     *            文件大小 b
     * @return
     * @return boolean
     */
    private boolean checkAttachmentSize(long size) {
        if (size > this.attachmentSize) {
            // 邮件附件过大 zk.mail.000011=邮件附件最大允许[{0}]M
            log.error("[>_<:20220527-0915-001] 邮件附件过大 zk.mail.000011=邮件附件最大允许[{}]M",
                    (this.attachmentSize / 1024 / 1024));
            throw ZKBusinessException.as("zk.mail.000011", null, (this.attachmentSize / 1024 / 1024));
        }
        return true;
    }

    /**
     * 校验附件大小
     *
     * @Title: checkAttachment
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 27, 2022 9:21:41 AM
     * @param attachments
     * @return
     * @return boolean
     */
    public boolean checkAttachment(List<MultipartFile> attachments) {
        if (attachments != null && attachments.size() > 0) {
            if (this.checkAttachmentCount(attachments.size())) {
                for (MultipartFile mf : attachments) {
                    if (!this.checkAttachmentSize(mf.getSize())) {
                        return false;
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }

    /**
     * 转换上传附件为邮件发送的数据源
     *
     * @Title: transferToDataSource
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 27, 2022 9:26:14 AM
     * @param attachments
     * @return
     * @throws IOException
     * @return ZKStreamDataSource[]
     */
    public ZKStreamDataSource[] transferToDataSource(List<MultipartFile> attachments) throws IOException {
        if (this.checkAttachment(attachments)) {
            if (attachments != null && attachments.size() > 0) {
                ZKStreamDataSource[] ads = null;
                if (attachments != null && attachments.size() > 0) {
                    ads = new ZKStreamDataSource[attachments.size()];
                    int index = 0;
                    for (MultipartFile mf : attachments) {
//                      ads[index] = new ZKStreamDataSource(mf.getOriginalFilename(), mf.getContentType(),
//                      mf.getInputStream(), null);
//                        ads[index] = new ZKStreamDataSource(mf.getOriginalFilename(), mf.getContentType(),
//                                new DataInputStream(mf.getInputStream()), null);
                        ads[index] = new ZKStreamDataSource(mf.getOriginalFilename(), mf.getContentType(),
                                new ByteArrayInputStream(mf.getBytes()), null);
                        ++index;
                    }
                }
                return ads;
            }
        }
        return null;
    }

    public void send(String sendMailAddr, String recipientMailAddr, String sendFlag, String typeCode,
            String companyCode, String locale, Map<String, Object> params, List<MultipartFile> attachments)
            throws IOException {
        this.send(true, sendMailAddr, recipientMailAddr, sendFlag, typeCode, companyCode, locale, params, attachments);
    }

    /**
     * 发送邮件
     *
     * @Title: send
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 27, 2022 10:36:44 AM
     * @param async
     *            是否异步，true-异步；false-同步
     * @param sendMailAddr
     *            发件人
     * @param recipientMailAddr
     *            收件人
     * @param sendFlag
     *            发送标识，非必须
     * @param typeCode
     *            邮件类型代码
     * @param companyCode
     *            公司代码
     * @param locale
     *            模板语言
     * @param params
     *            参数映射
     * @param attachments
     *            附件
     * @throws IOException
     * @return void
     */
    public void send(boolean async, String sendMailAddr, String recipientMailAddr, String sendFlag, String typeCode,
            String companyCode, String locale, Map<String, Object> params, List<MultipartFile> attachments)
            throws IOException {
        ZKStreamDataSource[] ads = this.transferToDataSource(attachments);
        this.send(async, sendMailAddr, recipientMailAddr, sendFlag, typeCode, companyCode, locale, params, ads);
    }


    /**
     * 发送邮件
     * 
     * // 按邮件类型代码，公司代码，国际化语言查找
     * 
     * // 按邮件类型代码，公司代码，国际化语言 为null 查找
     * 
     * // 按邮件类型代码，公司代码 为null，国际化语言查找
     * 
     * // 按邮件类型代码，公司代码 为null，国际化语言 为null 查找
     *
     * @Title: send
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 26, 2022 10:14:54 PM
     * @param async
     *            是否异步，true-异步；false-同步
     * @param sendMailAddr
     *            发件人
     * @param recipientMailAddr
     *            收件人
     * @param sendFlag
     *            发送标识，非必须
     * @param typeCode
     *            邮件类型代码
     * @param companyCode
     *            公司代码
     * @param locale
     *            模板语言
     * @param params
     *            参数映射
     * @param attachments
     *            附件
     * @return void
     */
    protected void send(boolean async, String sendMailAddr, String recipientMailAddr, String sendFlag, String typeCode,
            String companyCode, String locale, Map<String, Object> params, DataSource... attachments) {
        ZKMailType mailType = mailTypeService.getByCode(typeCode, ZKBaseEntity.DEL_FLAG.normal);
        if (mailType == null) {
            log.error("[>_<:20220526-2058-001] zk.mail.000004=邮件类型[{}]不存在；", typeCode);
            throw ZKBusinessException.as("zk.mail.000004", null, typeCode);
        }

        if (mailType.getStatus().intValue() != ZKMailType.KeyStatus.normal) {
            log.error("[>_<:20220526-2058-002] zk.mail.000002=邮件类型[{}]被禁用；", typeCode);
            throw ZKBusinessException.as("zk.mail.000002", null, typeCode);
        }

        ZKMailTemplate mailTemplate = this.mailTemplateService.getByTypeCode(typeCode, companyCode, locale,
                ZKBaseEntity.DEL_FLAG.normal);
        if (mailTemplate == null) {
            log.error("[>_<:20220526-2058-003] zk.mail.000007=邮件模板[{0}][{1}][{2}]不存在；", typeCode, companyCode, locale);
            throw ZKBusinessException.as("zk.mail.000007", null, typeCode, companyCode, locale);
        }

        if (mailTemplate.getStatus().intValue() != ZKMailType.KeyStatus.normal) {
            log.error("[>_<:20220526-2058-003] zk.mail.000008=邮件模板[{0}][{1}][{2}]已被禁用；", typeCode, companyCode, locale);
            throw ZKBusinessException.as("zk.mail.000008", null, typeCode, companyCode, locale);
        }
        this.send(async, sendMailAddr, recipientMailAddr, sendFlag, companyCode, mailTemplate, params, attachments);
    }

    /**
     * 发送邮件
     *
     * @Title: send
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 27, 2022 10:37:43 AM
     * @param async
     *            是否异步，true-异步；false-同步
     * @param sendMailAddr
     *            发件人
     * @param recipientMailAddr
     *            收件人
     * @param sendFlag
     *            发送标识，非必须
     * @param companyCode
     *            公司代码
     * @param mailTemplate
     *            邮件模板
     * @param params
     *            邮件参数
     * @param attachments
     *            文件
     * @return void
     */
    @Transactional(readOnly = false)
    protected void send(boolean async, String sendMailAddr, String recipientMailAddr, String sendFlag,
            String companyCode, ZKMailTemplate mailTemplate, Map<String, Object> params, DataSource... attachments) {
        try {
            ZKMailSendHistory mailSendHistory = ZKMailSendHistory.as(sendFlag, mailTemplate, params);
            if (ZKStringUtils.isEmpty(sendMailAddr)) {
                // 如果发件邮箱为空，用服务账号做为默认的发送邮件箱
                if (ZKStringUtils.isEmpty(mailTemplate.getSendAddress())) {
                    mailSendHistory.setSendAddress(mailAuthenticator.getAccount());
                }
            }
            else {
                mailSendHistory.setSendAddress(sendMailAddr);
            }

            log.info("[^_^:20220527-0216-001] 发送邮件：getSendAddress：{}", mailSendHistory.getSendAddress());
            ZKMailUtils.sendMail(recipientMailAddr, mailProperties, mailAuthenticator, mailSendHistory.getSendAddress(),
                    mailSendHistory.getSendName(), mailSendHistory.getSubject(), mailSendHistory.getContent(), async,
                    attachments);
            this.mailSendHistoryService.save(mailSendHistory);
        }
        catch(Exception e) {
            log.error("[>_<:20220526-2125-001] 发送邮件失败！");
            e.printStackTrace();
            throw ZKBusinessException.as("zk.mail.000009", e);
        }
    }

}
