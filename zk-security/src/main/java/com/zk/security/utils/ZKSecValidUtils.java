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
* @Title: ZKSecValidUtils.java 
* @author Vinson 
* @Package com.zk.security.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 1:14:36 PM 
* @version V1.0 
*/
package com.zk.security.utils;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.cache.ZKCache;
import com.zk.cache.utils.ZKCacheUtils;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;

/** 
* @ClassName: ZKSecValidUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecValidUtils {

    private static Logger logger = LogManager.getLogger(ZKSecValidUtils.class);

    /**
     * 请求失败次数缓存
     */
    public static final String CACHE_NAME_PREFIX_REQUEST_LOGIN_FAIL = "_FAIL_REQUEST_CACHE_";

    /**
     * 请求失败次数KEY
     */
    public static final String KEY_LOGIN_FAIL_NUM = "_KEY_LOGIN_FAIL_NUM_";

    /**
     * 验证码缓存名
     */
    public static final String CAPTCHA_NAME_PREFIX_VALID_CODE = "_VALID_CODE_CACHE_";

    /**
     * 请求失败次数KEY
     */
    public static final String KEY_CAPTCHA = "_KEY_captcha_";

    /**
     * 取失败请求次数缓存
     * 
     * @param host
     * @return
     */
    private static ZKCache<String, Integer> getLoginFailNumCache(String id) {
        return ZKCacheUtils.getCache(CACHE_NAME_PREFIX_REQUEST_LOGIN_FAIL + id,
                ZKEnvironmentUtils.getLong("zk.sec.fail.request.num.time", 3600000l));
    }

    /**
     * 从缓存中根据请求地址取登录失败次数
     */
    public static int getLoginFailNum(String id) {
        if (ZKStringUtils.isEmpty(id)) {
            return 0;
        }
        try {
            Integer num = getLoginFailNumCache(id).get(KEY_LOGIN_FAIL_NUM);
            logger.info("[^_^:20171219-1004-001] get login fail num by key -> {}, num -> {} ", id, num);
            return num == null ? 0 : num.intValue();
        }
        catch(Exception e) {
            e.printStackTrace();
            logger.error("[^_^:20171219-1004-002] get login fail num by key -> {}", id);
            return 0;
        }
    }

    /**
     * 设置缓存中，请求地址登录失败的次数
     */
    public static void putLoginFailNum(String id, int num) {
        logger.info("[^_^:20171219-1004-002] put login fail num by key -> {}, num -> {}", id, num);
        try {
            getLoginFailNumCache(id).put(KEY_LOGIN_FAIL_NUM, Integer.valueOf(num));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空指定，请求地址登录失败的次数
     */
    public static void cleanLoginFailNum(String id) {
        logger.info("[^_^:20171219-1004-003] clean login fail num by key -> {} ", id);
        try {
            getLoginFailNumCache(id).clear();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取验证码缓存
     * 
     * @param captchaId
     * @return
     */
    private static ZKCache<String, String> getValidCodeCache(String captchaId) {
        return ZKCacheUtils.getCache(CAPTCHA_NAME_PREFIX_VALID_CODE + captchaId,
                ZKEnvironmentUtils.getLong("zk.sec.valid.code.time", 300000l));
    }

    /**
     * 取缓存，captchaId 对应验证码
     * 
     * @param captchaId
     * @return
     */
    public static String getValidCode(String captchaId) {
        logger.info("[^_^:20171219-1004-004] get valid code cache by captchaId -> {}", captchaId);
        try {
            return getValidCodeCache(captchaId).get(KEY_CAPTCHA);
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置缓存 对应验证码
     * 
     * @param captchaId
     * @param code
     */
    public static void putValidCode(String captchaId, String code) {
        logger.info("[^_^:20171219-1004-005] put valid code cache by captchaId -> {}, code -> {}", captchaId, code);
        try {
            getValidCodeCache(captchaId).put(KEY_CAPTCHA, code);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空指定 对应 valid code 缓存
     * 
     * @param captchaId
     */
    public static void cleanValidCodeCache(String captchaId) {
        logger.info("[^_^:20171219-1004-006] clean valid code cache by captchaId -> {} ", captchaId);
        try {
            getValidCodeCache(captchaId).clear();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否是验证码登录
     * 
     * @param req
     *            请求 request
     * @param isFail
     *            计数加1
     * @param clean
     *            计数清零
     * @return
     */

    public static boolean isNeedValidCode(String id, boolean isFail, boolean clean) {

        int loginFailNum = getLoginFailNum(id);
        int failNumThreshold = ZKEnvironmentUtils.getInteger("zk.sec.fail.request.num.max", 3);
        if (isFail) {
            if (loginFailNum <= failNumThreshold) {
                putLoginFailNum(id, ++loginFailNum);
            }
            logger.info("[^_^:20171219-1004-007] add login fail num; host -> {}, loginFailNum -> {}", id, loginFailNum);
        }
        if (clean) {
            cleanLoginFailNum(id);
        }
        return loginFailNum >= failNumThreshold;
    }

    /**
     * 从请求中取 captchaId
     * 
     * @param request
     * @return
     */
    public static String getCaptchaIdByRequest(ZKSecRequestWrapper zkSecReq) {
        // 先从请求参数中取 captchaId,没有再从中请求头中取
        String captchaId = zkSecReq.getCleanParam(ZKSecConstants.PARAM_NAME.CaptchaId);
        // captchaId 为空，再从中请求头中取
        if (ZKStringUtils.isEmpty(captchaId)) {
            captchaId = zkSecReq.getHeader(ZKSecConstants.PARAM_NAME.CaptchaId);
        }
        return captchaId;
    }
}
