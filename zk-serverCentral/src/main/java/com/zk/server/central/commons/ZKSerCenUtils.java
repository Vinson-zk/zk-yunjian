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
 * @Title: ZKSerCenUtils.java 
 * @author Vinson 
 * @Package com.zk.server.central.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:09:12 PM 
 * @version V1.0   
*/
package com.zk.server.central.commons;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.session.Session;

import com.zk.core.encrypt.utils.ZKEncryptCipherUtils;
import com.zk.core.exception.base.ZKUnknownException;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.utils.ZKValidateCodeUtils;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.server.central.security.ZKSerCenSecurityUtils;

import jakarta.servlet.http.HttpServletRequest;

/** 
* @ClassName: ZKSerCenUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenUtils {

    private static Logger logger = LogManager.getLogger(ZKSerCenUtils.class);

    /**
     * 取登录失败次数
     */
    public static int getLoginFailNum() {
        Session s = ZKSerCenSecurityUtils.getSession();
        if (s == null) {
            return 0;
        }

        Integer num = (Integer) s.getAttribute(ZKSerCenConstants.captchaKey.KEY_LOGIN_FAIL_NUM);
        logger.info("[^_^:20171219-1004-001] get login fail num by num -> {} ", num);
        return num == null ? 0 : num.intValue();
    }

    /**
     * 登录失败次数增加 count 次；
     *
     * @Title: addLoginFailNum
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 30, 2019 2:17:36 PM
     * @param count
     *            增加的次数
     * @return void
     */
    public static void addLoginFailNum(int count) {
        int loginFailNum = getLoginFailNum();
        setLoginFailNum(loginFailNum + count);
    }

    /**
     * 设置缓存中，请求地址登录失败的次数
     */
    public static void setLoginFailNum(int num) {
        Session s = ZKSerCenSecurityUtils.getSession();
        if (s != null) {
            s.setAttribute(ZKSerCenConstants.captchaKey.KEY_LOGIN_FAIL_NUM, num);
        }
        logger.error("[>_<:20191030-0025-001] 设置失败次数失败，session 为 null ");
    }

    /**
     * 清空登录失败的次数
     */
    public static void cleanLoginFailNum() {
        Session s = ZKSerCenSecurityUtils.getSession();
        if (s != null) {
            s.removeAttribute(ZKSerCenConstants.captchaKey.KEY_LOGIN_FAIL_NUM);
        }
    }

    /**
     * 取缓存对应验证码
     * 
     * @return
     */
    public static String getCaptcha() {
        Session s = ZKSerCenSecurityUtils.getSession();
        if (s != null) {
            Long validityTime = (Long) s.getAttribute(ZKSerCenConstants.captchaKey.KEY_CAPTCHA_VALIDITY_DATE);

            if (validityTime != null) {
                Date d = new Date();
                if (validityTime < d.getTime()) {
                    // 验证码过期
                    logger.error("[>_<:201910301440-001] 验证码过期了；有效期：{}；验证时间：{}",
                            ZKDateUtils.formatDate(new Date(validityTime), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS),
                            ZKDateUtils.formatDate(d, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS));
                    return null;
                }
            }
            // 永不过期

            return (String) s.getAttribute(ZKSerCenConstants.captchaKey.KEY_CAPTCHA);

        }
        return null;
    }

    /**
     * 设置对应验证码
     *
     * @Title: putCaptcha
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 30, 2019 2:35:28 PM
     * @param captcha
     *            验证码
     * @param validityTime
     *            有效时长，毫秒，小于 0 永不过期
     * @return void
     */
    public static void putCaptcha(String captcha, long validityTime) {
        Session s = ZKSerCenSecurityUtils.getSession();
        if (s != null) {
            s.setAttribute(ZKSerCenConstants.captchaKey.KEY_CAPTCHA, captcha);
            if (validityTime < 0) {
                s.removeAttribute(ZKSerCenConstants.captchaKey.KEY_CAPTCHA_VALIDITY_DATE);
            }
            else {
                s.setAttribute(ZKSerCenConstants.captchaKey.KEY_CAPTCHA_VALIDITY_DATE,
                        (new Date()).getTime() + validityTime);
            }
        }
        else {
            logger.error("[>_<:20191030-0025-001] 设置验证码失败，session 为 null ");
        }
    }

    /**
     * 是否是验证码登录
     *
     * @Title: isNeedCaptcha
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 30, 2019 2:18:44 PM
     * @param maxFailNum
     *            允许的最大失败次数
     * @return
     * @return boolean
     */
    public static boolean isNeedCaptcha(int maxFailNum) {
        int loginFailNum = getLoginFailNum();
        return loginFailNum >= maxFailNum;
    }

    /**
     * 验证验证码，
     *
     * @Title: checkCaptcha
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 30, 2019 2:19:07 PM
     * @param hReq
     * @param maxFailNum
     * @return
     * @return boolean
     */
    public static boolean checkCaptcha(HttpServletRequest hReq, int maxFailNum) {
        // 不改变登录失败次数，判断否需要
        if (!isNeedCaptcha(maxFailNum)) {
            return true;
        }

        Session s = ZKSerCenSecurityUtils.getSession();
        if (s != null) {
            Object targetCaptcha = s.getAttribute(ZKSerCenConstants.captchaKey.KEY_CAPTCHA);
            String reqCaptcha = getCaptchaParam(hReq);
            if (ZKValidateCodeUtils.checkVerifyCode(targetCaptcha, reqCaptcha, 1)) {
                cleanLoginFailNum();
                return true;
            }
            else {
                logger.error("[>_<:20191030-0044-001] 校验验证码失败，上送验证码为：{}, 实际验证码为：{} ", targetCaptcha, reqCaptcha);
            }
        }
        else {
            logger.error("[>_<:20191030-0044-002] 校验验证码失败，session 为 null ");
        }

        return false;
    }

    /**
     * 从请求中取 captcha
     * 
     * @param request
     * @return
     */
    public static String getCaptchaParam(HttpServletRequest hReq) {
        // 先从请求参数中取 captchaId,没有再从中请求头中取
        return ZKServletUtils.getCleanParam(hReq, ZKSerCenConstants.ParamKey.param_captcha);
    }

    /**
     * 添加 用户 SecretKey
     * 
     * @param hReq
     * @return 用户 SecretKey
     * @throws NoSuchAlgorithmException
     */
    public static SecretKey setUserSecretKey(HttpServletRequest hReq) throws NoSuchAlgorithmException {
        /*** 用户 SecretKey 一些接口中的特殊字段加密处理； ***/
        String skStr = (String) hReq.getSession().getAttribute(ZKSerCenConstants.ParamKey.userSecretKey);
        if (ZKStringUtils.isEmpty(skStr)) {
            SecretKey sk = ZKEncryptCipherUtils.generateKey(ZKEncryptCipherUtils.MODE.AES, 128, null);
            // 在服务器使用 session 保存 用户 SecretKey；key 使用 hex 编码
            hReq.getSession().setAttribute(ZKSerCenConstants.ParamKey.userSecretKey,
                    ZKEncodingUtils.encodeHex(sk.getEncoded()));
            return sk;
        }
        return new SecretKeySpec(ZKEncodingUtils.decodeHex(skStr), ZKEncryptCipherUtils.MODE.AES);
    }

    /**
     * 取 用户 SecretKey
     * 
     * @param hReq
     * @return 用户 SecretKey
     * @throws NoSuchAlgorithmException
     */
    public static SecretKey getUserSecretKey(HttpServletRequest hReq) throws NoSuchAlgorithmException {
        String skStr = (String) hReq.getSession().getAttribute(ZKSerCenConstants.ParamKey.userSecretKey);
        if (ZKStringUtils.isEmpty(skStr)) {
            return null;
        }
        return new SecretKeySpec(ZKEncodingUtils.decodeHex(skStr), ZKEncryptCipherUtils.MODE.AES);
    }

    /**
     * 前后相互加密的便宜 key；16 位 byte; 前后台相同
     */
    private static final String ivKey = "ihaierForTodo_Iv";

    public static byte[] getIvKey() {
        byte[] key = ivKey.getBytes();
        if (key.length != 16) {
            throw new ZKUnknownException("前后端相互加解密偏移 key 有效长度为 16 位；");
        }
        return key;
    }

    public static String encode(String content, SecretKey key) {
        byte[] zk = ZKEncryptCipherUtils.cipher(Cipher.ENCRYPT_MODE, "AES/CBC/PKCS5Padding",
                ZKEncodingUtils.decodeHex(content), key, new IvParameterSpec(getIvKey()), null);

        return ZKEncodingUtils.encodeHex(zk);
    }

    public static String decrypt(String content, SecretKey key) {

        byte[] zk = ZKEncryptCipherUtils.cipher(Cipher.DECRYPT_MODE, "AES/CBC/PKCS5Padding",
                ZKEncodingUtils.decodeHex(content), key, new IvParameterSpec(getIvKey()), null);
        return new String(zk);
    }

}
