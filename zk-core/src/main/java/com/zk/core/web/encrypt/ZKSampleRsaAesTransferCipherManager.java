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
 * @Title: ZKSampleRsaAesTransferCipherManager.java 
 * @author Vinson 
 * @Package com.zk.core.encrypt.support 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:52:27 PM 
 * @version V1.0   
*/
package com.zk.core.web.encrypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.encrypt.ZKRSAKey;
import com.zk.core.encrypt.utils.ZKEncryptAesUtils;
import com.zk.core.encrypt.utils.ZKEncryptRsaUtils;
import com.zk.core.encrypt.utils.ZKEncryptUtils;
import com.zk.core.exception.sub.ZKTransferCipherException;
import com.zk.core.utils.ZKCollectionUtils;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.wrapper.ZKEncRequestWrapper;
import com.zk.core.web.wrapper.ZKEncResponseWrapper;

/** 
* @ClassName: ZKSampleRsaAesTransferCipherManager 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSampleRsaAesTransferCipherManager implements ZKTransferCipherManager {

    protected Logger log = LogManager.getLogger(getClass());

    /**
     * rsa 传输加解密在请求头的字段名定义
     */
    public static interface ZKRSA_ReqHeader {

        /**
         * RSA 证书ID，上送时在请求头中的字段名
         */
        public static final String pn_id = "_rsa";

        /**
         * 数据密钥，上送时在请求头中的字段名
         */
        public static final String pn_key = "_encKey";

        /**
         * 参数名
         */
        public static final String pn_parameter = "_encData";
    }

    private static interface EncInfo {

        // 加密密钥
        public static final String dataEncKey = "_dataEncKey";

        // 加密密钥 盐
        public static final String dataEncKeySalt = "_dataEncKeySalt";
    }

    @Override
    public void decrypt(ZKEncRequestWrapper request) {

        try {
//            log.info("[^_^:20190625-1723-001] ZKTransferCipherFilter 加解密处理。");

            String paramStr = request.getParameter(ZKRSA_ReqHeader.pn_parameter);
            String encKeyStr = request.getHeader(ZKRSA_ReqHeader.pn_key);
            String rsaId = request.getHeader(ZKRSA_ReqHeader.pn_id);

            // 取 rsa 证书
            ZKRSAKey zkRsaKey = this.getZKRSAKeyByRsaId(rsaId);
            // 解密数据加密密钥
//            log.info("[^_^:20190625-1540-001-0] 解密前 密钥 -> " + encKeyStr);
            byte[] dataEncKey = this.decryptDataKey(zkRsaKey.getPrivateKey(), ZKEncodingUtils.decodeHex(encKeyStr));
//            log.info("[^_^:20190625-1540-001-1] 解密后 密钥 -> " + ZKEncodingUtils.encodeHex(dataEncKey));

            // 加盐
            byte[] salt = ZKEncryptUtils.genSalt(dataEncKey);
            Map<String, String[]> zkParameterMap = decryptParameterMap(dataEncKey, salt, paramStr);
            byte[] reqBody = decryptRequestBody(dataEncKey, salt, request);

            request.afterDecryptSet(zkParameterMap, reqBody);

            request.getEncInfo().put(EncInfo.dataEncKey, dataEncKey);
            request.getEncInfo().put(EncInfo.dataEncKeySalt, salt);
        }
        catch(UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw ZKExceptionsUtils.unchecked(e);
        }
    }

    // 请求体解密，HttpServletRequest requestBody 解密
    protected byte[] decryptRequestBody(byte[] dataEncKey, byte[] salt, ZKEncRequestWrapper req) {

        InputStream sis = null;
        ByteArrayOutputStream os = null;
        try {
            sis = req.getZKInputStream();
            os = new ByteArrayOutputStream();
            ZKStreamUtils.readAndWrite(sis, os);
            byte[] reqBody = os.toByteArray();
//            log.info("[^_^:20190627-1031-003-0] 解密前 reqBody -> " + new String(reqBody));
            reqBody = this.decryptData(dataEncKey, salt, ZKEncodingUtils.decodeHex(new String(reqBody)));
//            log.info("[^_^:20190627-1031-003-1] 解密后 reqBody -> " + new String(reqBody));

            return reqBody;
        }
        catch(IOException e) {
            throw new ZKTransferCipherException("解密 request body 失败!", e);
        } finally {
            ZKStreamUtils.closeStream(sis);
            ZKStreamUtils.closeStream(os);
        }

    }

    /**
     * 请求参数解密，HttpServletRequest request.getParameterMap() 解密与生成参数 map
     *
     * @Title: decryptParameterMap
     * @Description: 请求参数解密，HttpServletRequest request.getParameterMap() 解密与生成参数
     *               map
     * @author Vinson
     * @date Jun 27, 2019 9:46:08 AM
     * @param encKey
     *            数据加密密钥，是解密后的密钥
     * @param paramStr
     *            上送参数密文
     * @return
     * @throws UnsupportedEncodingException
     * @return Map<String,String[]>
     */
    protected Map<String, String[]> decryptParameterMap(byte[] dataEncKey, byte[] salt, String paramStr)
            throws UnsupportedEncodingException {

        if (ZKStringUtils.isEmpty(paramStr)) {
            return new HashMap<String, String[]>();
        }

        // 解密参数 parameter
//        log.info("[^_^:20190625-1540-002-0] 解密前 数据：paramStr -> " + paramStr);
        paramStr = new String(this.decryptData(dataEncKey, salt, ZKEncodingUtils.decodeHex(paramStr)));
//        log.info("[^_^:20190625-1540-002-1] 解密后 数据：paramStr -> " + paramStr);
        return ZKCollectionUtils.mapToReqParametMap(ZKJsonUtils.parseMap(paramStr));

    }

    @Override
    public void encrypt(ZKEncRequestWrapper zkReq, ZKEncResponseWrapper zkRes) {
        OutputStream out = null;
        try {
            byte[] dataEncKey = (byte[]) zkReq.getEncInfo().get(EncInfo.dataEncKey);
            byte[] salt = (byte[]) zkReq.getEncInfo().get(EncInfo.dataEncKeySalt);
            byte[] resData = zkRes.getData();
            resData = this.encryptData(dataEncKey, salt, resData);
            resData = ZKEncodingUtils.encodeHex(resData).getBytes();
            out = zkRes.getZKOutputStream();
            out.write(resData);
            out.flush();
            out.close();
        }
        catch(IOException e) {
            throw ZKExceptionsUtils.unchecked(e);
        } finally {
            ZKStreamUtils.closeStream(out);
        }
    }

    @Override
    public boolean isEnc(ZKEncRequestWrapper request) {
        String rsaId = request.getHeader(ZKRSA_ReqHeader.pn_id);
        if (rsaId == null) {
            return false;
        }
        return true;
    }

    /*** ************************************************ ***/
    private ZKRSAKey zkRSAKey = null;

    // 根据 rsa ID 取RSA 证书
    public ZKRSAKey getZKRSAKeyByRsaId(String rsaId) {
        try {
            if (zkRSAKey == null) {
                zkRSAKey = ZKEncryptRsaUtils.genZKRSAKey(2048, new SecureRandom());
            }
            return zkRSAKey;
        }
        catch(Exception e) {
            throw new ZKTransferCipherException("取 RSA 证书失败", e);
        }

    }

    // 根据 终端标识 取对应可用的 RSA 证书；一个 终端标识 在单位时间内最多只有一个可用的 rsa 证书。
    protected ZKRSAKey getZKRSAKeyByAppId(String appId) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * AES 解密数据
     *
     * @Title: decryptData
     * @Description: AES 解密数据
     * @author Vinson
     * @date Jun 27, 2019 9:44:26 AM
     * @param encKey
     * @param content
     * @return
     * @return byte[]
     */
    private byte[] decryptData(byte[] encKey, byte[] salt, byte[] content) {

        try {
            return ZKEncryptAesUtils.decrypt(content, encKey, salt);
        }
        catch(Exception e) {
            throw new ZKTransferCipherException("AES 解密数据失败", e);
        }
    }

    /**
     * AES 加密数据
     *
     * @Title: encryptData
     * @Description: AES 加密数据
     * @author Vinson
     * @date Jun 27, 2019 9:44:44 AM
     * @param encKey
     * @param content
     * @return
     * @return byte[]
     */
    public byte[] encryptData(byte[] encKey, byte[] content) {
        try {
            return encryptData(encKey, ZKEncryptUtils.genSalt(encKey), content);
        }
        catch(Exception e) {
            throw new ZKTransferCipherException("AES 响应数据加密 失败", e);
        }
    }

    public byte[] encryptData(byte[] encKey, byte[] salt, byte[] content) {
        try {
            return ZKEncryptAesUtils.encrypt(content, encKey, salt);
        }
        catch(Exception e) {
            throw new ZKTransferCipherException("AES 响应数据加密 失败", e);
        }
    }

    /**
     * RSA 解密数据加密密钥
     *
     * @Title: decryptDataKey
     * @Description: RSA 解密数据加密密钥
     * @author Vinson
     * @date Jun 27, 2019 9:45:02 AM
     * @param privateKey
     * @param encKey
     * @return
     * @return byte[]
     */
    private byte[] decryptDataKey(byte[] privateKey, byte[] encKey) {
        try {
            return ZKEncryptRsaUtils.decrypt(encKey, privateKey);
        }
        catch(Exception e) {
            throw new ZKTransferCipherException("RSA 解密数据加密 密钥 失败", e);
        }
    }

    /**
     * RSA 加密数据加密密钥
     *
     * @Title: encryptDataKey
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 27, 2019 9:45:24 AM
     * @param publicKey
     * @param encKey
     * @return
     * @return byte[]
     */
    public byte[] encryptDataKey(byte[] publicKey, byte[] encKey) {
        try {
            return ZKEncryptRsaUtils.encrypt(encKey, publicKey);
        }
        catch(Exception e) {
            throw new ZKTransferCipherException("RSA 加密数据加密 密钥 失败", e);
        }
    }

}
