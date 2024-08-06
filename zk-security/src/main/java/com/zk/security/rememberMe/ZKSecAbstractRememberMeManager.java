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
* @Title: ZKSecAbstractRememberMeManager.java 
* @author Vinson 
* @Package com.zk.security.rememberMe 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 6:51:15 PM 
* @version V1.0 
*/
package com.zk.security.rememberMe;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.encrypt.utils.ZKEncryptAesUtils;
import com.zk.core.exception.ZKSecAuthenticationException;
import com.zk.core.exception.base.ZKCodeException;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKObjectUtils;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.token.ZKSecAuthenticationToken;
import com.zk.security.token.ZKSecRememberMeToken;

/** 
* @ClassName: ZKSecAbstractRememberMeManager 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAbstractRememberMeManager implements ZKSecRememberMeManager {

    protected Logger logger = LogManager.getLogger(getClass());

    /**
     * 密钥，如果没设置； 随机生成一个，此时，如果服务重启，原加密的身份将无法解密
     */
    private byte[] encryptionKey;

    /**
     * 加密盐，固定值；
     */
    private final byte[] salt = "SALT_REMEMBER".getBytes();

    /**
     * 将身份序列化
     * 
     * @param pc
     * @return
     */
    protected <ID> byte[] serialize(ZKSecPrincipalCollection<ID> pc) {
        if (pc != null) {
            return ZKObjectUtils.serialize(pc);
        }
        logger.error("[>_<:20180821-2322-002] pc is null, serialize return null ");
        return null;
    }

    /**
     * 将身份反序列化
     * 
     * @param bs
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <ID> ZKSecPrincipalCollection<ID> deserialize(byte[] bs) {
        if (bs != null) {
            return (ZKSecPrincipalCollection<ID>) ZKObjectUtils.unserialize(bs);
        }
        logger.error("[>_<:20180821-2322-003] bs is null, unserialize return null ");
        return null;
    }

    /**
     * 获取密钥
     * 
     * @return
     */
    protected byte[] getEncryptionKey() {
        if (encryptionKey == null) {
            encryptionKey = UUID.randomUUID().toString().getBytes();
        }
        return encryptionKey;
    }

    /**
     * 设置密钥字符串
     * 
     * @param encryptionKey
     */
    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey.getBytes();
    }

    /**
     * 加密身份
     * 
     * @param bs
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    protected byte[] encrypt(byte[] bs) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return ZKEncryptAesUtils.encrypt(bs, getEncryptionKey(), salt);
    }

    /**
     * 解密身份
     * 
     * @param bs
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    protected byte[] decrypt(byte[] bs) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return ZKEncryptAesUtils.decrypt(bs, getEncryptionKey(), salt);
    }

    @Override
    public boolean isRememberMe(ZKSecAuthenticationToken token) {
        if (token != null) {
            if (token instanceof ZKSecRememberMeToken) {
                return ((ZKSecRememberMeToken) token).isRememberMe();
            }
            else {
                logger.info("[^_^:20180824-1044-001] 不是记住我 Token！token class is {}", token.getClass().getName());
            }
        }
        else {
            logger.error("[>_<:20180821-2322-001] rememberMeToken is null ");
        }
        return false;
    }

    @Override
    public void onLogout(ZKSecSubject subject) {
        forgetIdentity(subject);
    }

    @Override
    public <ID> void onSuccessfulLogin(ZKSecSubject subject, ZKSecAuthenticationToken token,
            ZKSecPrincipalCollection<ID> pc) {
        try {
            // 先清理以前记住的身份；always clear any previous identity;
            forgetIdentity(subject);
            // 如果是记住我；记住新的身份；now save the new identity:
            if (isRememberMe(token)) {
                byte[] pcBytes = serialize(pc);
                pcBytes = encrypt(pcBytes);
                rememberIdentity(subject, token, pcBytes);
            }
            else {
                logger.info("[^_^:20180824-1036-001] 不是记住我登录！");
            }
        }
        catch(Exception e) {
            throw ZKExceptionsUtils.unchecked(e);
        }
    }

    @Override
    public void onFailedLogin(ZKSecSubject subject, ZKSecAuthenticationToken token, ZKCodeException se) {
        forgetIdentity(subject);
    }

    @Override
    public <ID> ZKSecPrincipalCollection<ID> getRememberedPrincipals(ZKSecSubject subject) {
        try {
            byte[] pcBytes = getRememberedSerializedIdentity(subject);
            if (pcBytes != null && pcBytes.length > 0) {
                pcBytes = decrypt(pcBytes);
                return deserialize(pcBytes);
            }
            else {
                return null;
            }
        }
        catch(Exception e) {
            return onRememberedPrincipalFailure(e, subject);
        }
    }

    /**
     * 取记住我身份失败
     * 
     * @param e
     *            异常
     * @param subject
     *            主体
     * @return
     */
    protected <ID> ZKSecPrincipalCollection<ID> onRememberedPrincipalFailure(Exception e, ZKSecSubject subject) {
        logger.error("[>_<:20180824-1015-001] 记住我身份解密失败！");
        e.printStackTrace();
        forgetIdentity(subject);
        // 记住我发生变化，请重新登录
        throw ZKSecAuthenticationException.as("zk.sec.000009", e.getMessage());
    }

    /**
     * 忘记我
     * 
     * @param subject
     */
    protected abstract void forgetIdentity(ZKSecSubject subject);

    /**
     * 记住我
     * 
     * @param subject
     * @param serialized
     */
    protected abstract void rememberIdentity(ZKSecSubject subject, ZKSecAuthenticationToken token, byte[] serialized);

    /**
     * 取记住我身份
     * 
     * @param subject
     * @return
     */
    protected abstract byte[] getRememberedSerializedIdentity(ZKSecSubject subject);

}
