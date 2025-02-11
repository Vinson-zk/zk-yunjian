/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKSerCenCertificateService.java 
 * @author Vinson 
 * @Package com.zk.server.central.service 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:24:48 PM 
 * @version V1.0   
*/
package com.zk.server.central.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.core.encrypt.ZKRSAKey;
import com.zk.core.exception.ZKSecAuthenticationException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.server.central.commons.ZKSerCenCerCipherManager;
import com.zk.server.central.dao.ZKSerCenCertificateDao;
import com.zk.server.central.entity.ZKSerCenCertificate;
import com.zk.server.central.entity.ZKSerCenCertificate.StatusType;

/** 
* @ClassName: ZKSerCenCertificateService 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Service
public class ZKSerCenCertificateService extends ZKBaseService<String, ZKSerCenCertificate, ZKSerCenCertificateDao> {

    /**
     *
     */
    @Autowired
    ZKSerCenCerCipherManager zkSerCenCerCipherManager;

    @Transactional(readOnly = false)
    public int updateStatus(ZKSerCenCertificate serCenCertificate) {
//        serCenCertificate.setStatus(ZKSerCenCertificate.StatusType.Disabled);
//        serCenCertificate.preUpdate();
        this.preUpdate(serCenCertificate);
        return this.dao.updateStatus(serCenCertificate);
    }

    /**
     * 保存数据（插入或更新）
     * 
     * @param serCenCertificate
     * @throws ZKValidatorException
     */
    @Override
    @Transactional(readOnly = false)
    public int save(ZKSerCenCertificate serCenCertificate) throws ZKValidatorException {
        if (serCenCertificate.isNewRecord()) {
            // 生成证书
            genCertificate(serCenCertificate);
        }
        else {
            // 需要验证，但又不修改的字段，用假参数 补齐，以通过数据验证
        }
        return super.save(serCenCertificate);
    }

    /**
     * 生成证书，生成的是一对 RSA 证书；
     *
     * @Title: preInsert
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 29, 2019 1:41:57 PM
     * @param serCenCertificate
     * @return void
     */
    public void genCertificate(ZKSerCenCertificate serCenCertificate) {
        try {
            ZKRSAKey rsaKey = zkSerCenCerCipherManager.genCer();
            serCenCertificate.setPublicKey(rsaKey.getPublicKeyStr());
            serCenCertificate.setPrivateKey(rsaKey.getPrivateKeyStr());
            log.info("[^_^:20190829-1630-001] serCenCertificate: {}", ZKJsonUtils.toJsonStr(serCenCertificate));
        }
        catch(NoSuchAlgorithmException e) {
            throw ZKSecAuthenticationException.as("zk.ser.cen.000011", e, (Object[]) null);
        }
    }

    @Override
    public void preInsert(ZKSerCenCertificate entity) {
        super.preInsert(entity);
        if (entity.getStatus() == null) {
            entity.setStatus(StatusType.Enable);
        }
    }

    @Override
    public void preUpdate(ZKSerCenCertificate entity) {
        super.preUpdate(entity);
        if (entity.getStatus() == null) {
            entity.setStatus(StatusType.Enable);
        }
    }

}


