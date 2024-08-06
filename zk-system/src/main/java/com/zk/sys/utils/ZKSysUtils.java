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
* @Title: ZKSysUtils.java 
* @author Vinson 
* @Package com.zk.sys.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 21, 2022 10:10:28 AM 
* @version V1.0 
*/
package com.zk.sys.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.zk.core.utils.ZKEnvironmentUtils;

/** 
* @ClassName: ZKSysUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSysUtils {

    private static Map<String, String> configMap = new HashMap<>();

    public static interface ConfigKey {
        // 公司拥有者代码
        public static final String ownerCompanyCode = "zk.sys.owner.company.code";

        // 系统默认代码
        public static final String defaultSysPwd = "zk.sys.user.default.sys.pwd";

        // 个人用户类型
        public static final String personalUserCode = "zk.sys.personal.user.code";

        // # 人员证件的字典类型
        public static final String dictTypeCertType = "zk.sys.dict.type.cert"; // cert.type

        // # 公司证件的字典类型
        public static final String dictTypeCompanyCertType = "zk.sys.dict.type.company.cert"; // company.cert.type

        // 环境模式
        public static final String envModel = "zk.sys.env.model";

        // 通用验证码邮件
        public static final String mailVerifyCode = "zk.sys.mail.type.code.verify.code";// mail.verifiy.code
        // 公司注册邮件

        public static final String mailCompanyRegister = "zk.sys.mail.type.code.company.register"; // mail.company.register
        // 公司通过审核邮件

        public static final String mailCompanyPassVerification = "zk.sys.mail.type.code.company.pass.verification"; // mail.company.pass.verification
    }

    /**
     * 取拥有者公司代码
     *
     * @Title: getOwnerCompanyCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 21, 2022 10:11:14 AM
     * @return
     * @return String
     */
    public static String getOwnerCompanyCode() {
        if (configMap.get(ConfigKey.ownerCompanyCode) == null) {
            configMap.put(ConfigKey.ownerCompanyCode, ZKEnvironmentUtils.getString(ConfigKey.ownerCompanyCode));
        }
        return configMap.get(ConfigKey.ownerCompanyCode);
    }

    /**
     * 取系统默认密码
     *
     * @Title: getSysDefaultPwd
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 20, 2024 9:26:24 PM
     * @return
     * @return String
     */
    public static String getUserDefaultPwd() {
        if (configMap.get(ConfigKey.defaultSysPwd) == null) {
            configMap.put(ConfigKey.defaultSysPwd, ZKEnvironmentUtils.getString(ConfigKey.defaultSysPwd, "123456"));
        }
        return configMap.get(ConfigKey.defaultSysPwd);
    }

    // 取个人用户的 用户代码
    public static String getPersonalUserTypeCode() {
        if (configMap.get(ConfigKey.personalUserCode) == null) {
            configMap.put(ConfigKey.personalUserCode, ZKEnvironmentUtils.getString(ConfigKey.personalUserCode));
        }
        return configMap.get(ConfigKey.personalUserCode);
    }

    // 取人员证件的字典类型
    public static String getDictTypeCertType() {
        if (configMap.get(ConfigKey.dictTypeCertType) == null) {
            configMap.put(ConfigKey.dictTypeCertType, ZKEnvironmentUtils.getString(ConfigKey.dictTypeCertType));
        }
        return configMap.get(ConfigKey.dictTypeCertType);
    }

    // 取公司证件的字典类型
    public static String getDictTypeCompanyCertType() {
        if (configMap.get(ConfigKey.dictTypeCompanyCertType) == null) {
            configMap.put(ConfigKey.dictTypeCompanyCertType,
                    ZKEnvironmentUtils.getString(ConfigKey.dictTypeCompanyCertType));
        }
        return configMap.get(ConfigKey.dictTypeCompanyCertType);
    }

    // 环境模式
    public static String getSysEnvModel() {
        if (configMap.get(ConfigKey.envModel) == null) {
            configMap.put(ConfigKey.envModel, ZKEnvironmentUtils.getString(ConfigKey.envModel));
        }
        return configMap.get(ConfigKey.envModel);
    }

    //
    public static String getMailVerifyCode() {
        if (configMap.get(ConfigKey.mailVerifyCode) == null) {
            configMap.put(ConfigKey.mailVerifyCode,
                    ZKEnvironmentUtils.getString(ConfigKey.mailVerifyCode, "mail.verifiy.code"));
        }
        return configMap.get(ConfigKey.mailVerifyCode);
    }

    //
    public static String getMailCompanyRegister() {
        if (configMap.get(ConfigKey.mailCompanyRegister) == null) {
            configMap.put(ConfigKey.mailCompanyRegister,
                    ZKEnvironmentUtils.getString(ConfigKey.mailCompanyRegister, "mail.company.register"));
        }
        return configMap.get(ConfigKey.mailCompanyRegister);
    }

    //
    public static String getMailCompanyPassVerification() {
        if (configMap.get(ConfigKey.mailCompanyPassVerification) == null) {
            configMap.put(ConfigKey.mailCompanyPassVerification, ZKEnvironmentUtils
                    .getString(ConfigKey.mailCompanyPassVerification, "mail.company.pass.verification"));
        }
        return configMap.get(ConfigKey.mailCompanyPassVerification);
    }

    // 生成默认系统账号
    public static String genSysDefaultAccount() {
        return "zk" + UUID.randomUUID().toString().replaceAll("-", "");
    }

    // 取保秘性别的字典代码，与字典项要保持一至
    public static String getSexUnknownDictCode() {
        return "unknown";
    }

    public static interface SysEnvModel {
        // 测试环境
        public static final String dev = "dev";

        public static final String prod = "prod";
    }

    // 判断是不是开发环境
    public static boolean isDevEnv() {
        return SysEnvModel.dev.equals(getSysEnvModel());
    }

}
