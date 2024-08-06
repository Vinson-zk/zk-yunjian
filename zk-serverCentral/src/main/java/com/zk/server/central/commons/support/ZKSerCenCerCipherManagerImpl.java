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
* @Title: ZKSerCenCerCipherManagerImpl.java 
* @author Vinson 
* @Package com.zk.server.central.commons.support 
* @Description: TODO(simple description this file what to do.) 
* @date Mar 12, 2020 8:58:07 PM 
* @version V1.0 
*/
package com.zk.server.central.commons.support;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.zk.core.encrypt.ZKRSAKey;
import com.zk.core.encrypt.utils.ZKEncryptRsaUtils;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;
import com.zk.server.central.commons.ZKSerCenCerCipherManager;

/** 
* @ClassName: ZKSerCenCerCipherManagerImpl 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenCerCipherManagerImpl extends ZKSerCenSampleCipher implements ZKSerCenCerCipherManager {

    private int keysize = 1024;

    private String randomSeed = null;

    public ZKSerCenCerCipherManagerImpl() {

    }

    public ZKSerCenCerCipherManagerImpl(int keysize, String randomSeed) {
        this.keysize = keysize;
        this.randomSeed = randomSeed;
    }

    public SecureRandom getSecureRandom() {
        return randomSeed == null ? new SecureRandom() : new SecureRandom(randomSeed.getBytes());
    }

    @Override
    public ZKRSAKey genCer() throws NoSuchAlgorithmException {
        return ZKEncryptRsaUtils.genZKRSAKey(keysize, getSecureRandom());
    }

}
