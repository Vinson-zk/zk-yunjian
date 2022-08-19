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
* @Title: ZKAesException.java 
* @author Vinson 
* @Package com.zk.wechat.wx.aes 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2021 2:36:42 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.aes;

import com.qq.weixin.mp.aes.AesException;

/**
 * 重写 com.qq.weixin.mp.aes 下的类，以实现支持开放平台的加解密
 * 
 * @ClassName: ZKAesException
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKAesException extends Exception {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;
    private int code;

    private static String getMessage(int code) {
        switch (code) {
            case AesException.ValidateSignatureError:
                return "签名验证错误";
            case AesException.ParseXmlError:
                return "xml解析失败";
            case AesException.ComputeSignatureError:
                return "sha加密生成签名失败";
            case AesException.IllegalAesKey:
                return "SymmetricKey非法";
            case AesException.ValidateAppidError:
                return "appid校验失败";
            case AesException.EncryptAESError:
                return "aes加密失败";
            case AesException.DecryptAESError:
                return "aes解密失败";
            case AesException.IllegalBuffer:
                return "解密后得到的buffer非法";
//      case EncodeBase64Error:
//          return "base64加密错误";
//      case DecodeBase64Error:
//          return "base64解密错误";
//      case GenReturnXmlError:
//          return "xml生成失败";
            default:
                return null; // cannot be
        }
    }

    public int getCode() {
        return code;
    }

    public ZKAesException(int code) {
        super(getMessage(code));
        this.code = code;
    }

}
