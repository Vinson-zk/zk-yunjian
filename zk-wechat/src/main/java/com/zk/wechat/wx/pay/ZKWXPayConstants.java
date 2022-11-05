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
* @Title: ZKWXPayConstants.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 20, 2021 11:00:57 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay;
/** 
* @ClassName: ZKWXPayConstants 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXPayConstants {

    /**
     * 响应错误属性
     * 
     * @ClassName: ResErr
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface ResErr {

        public static final String code = "code";

        public static final String message = "message";
    }

    /**
     * 微信支付平台向后台系统发送请求时的一些请求头参数 key
     * 
     * @ClassName: ZKReqHeader
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface ZKReqHeader {

        /**
         * 微信支付的应答签名; 对 Wechatpay-Signature的字段值使用Base64进行解码，得到应答签名。
         */
        public static final String WechatpaySignature = "Wechatpay-Signature";

        /**
         * HTTP头Wechatpay-Timestamp 中的应答时间戳。
         */
        public static final String WechatpayTimestamp = "Wechatpay-Timestamp";

        /**
         * HTTP头Wechatpay-Nonce 中的应答随机串
         */
        public static final String WechatpayNonce = "Wechatpay-Nonce";

        /**
         * 微信支付签名使用微信支付平台私钥，证书序列号包含在应答HTTP头部的 Wechatpay-Serial
         */
        public static final String WechatpaySerial = "Wechatpay-Serial";

    }

    /**
     * 向微信支付平台发送请求时，要求的一些请求头参数 Key
     * 
     * @ClassName: WXReqHeader
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface WXReqHeader {
        /**
         * 请求签名；包含 认证类型和签名信息两个部分;
         */
        public static final String Authorization = "Authorization";

        public static final String schema = "WECHATPAY2-SHA256-RSA2048";

        /**
         * 签名内容 Key
         * 
         * @ClassName: ContentKey
         * @Description: TODO(simple description this class what to do. )
         * @author Vinson
         * @version 1.0
         */
        public static interface ContentKey {
            /**
             * 发起请求的商户（包括直连商户、服务商或渠道商）的商户号
             */
            public static final String mchid = "mchid";

            /**
             * 商户API证书 serial_no，用于声明所使用的证书
             */
            public static final String serial_no = "serial_no";

            /**
             * 请求随机串 nonce_str
             */
            public static final String nonce_str = "nonce_str";

            /**
             * 时间戳 timestamp
             */
            public static final String timestamp = "timestamp";

            /**
             * 签名值 signature
             */
            public static final String signature = "signature";

        }

        /**
         * 商户上送敏感信息时使用微信支付平台公钥加密，证书序列号包含在请求HTTP头部的 Wechatpay-Serial
         */
        public static final String wechatpaySerial = "Wechatpay-Serial";
    }

    /**
     * 返回消息属性
     * 
     * @ClassName: MsgAttr
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface MsgAttr {
        /**
         * 预支付交易会话标识 prepay_id string[1,64] 是
         */
        public static final String prepayId = "prepay_id";

        /**
         * 二维码链接	code_url	string[1,512]	是	此URL用于生成支付二维码，然后提供给用户扫码支付。
         * 注意：code_url并非固定值，使用时按照URL格式转成二维码即可。
         * 示例值：weixin://wxpay/bizpayurl/up?pr=NwY5Mz9&groupid=00
         */
        public static final String codeUrl = "code_url";

        /**
         * 数据
         */
        public static final String data = "data";
        
        /**
         * 统一密文属性
         */
        public static interface EncKey {
            /****** 未解密密前的一些 加密相关属性 */
            // 加密算法类型 algorithm string[1,32] 是
            // 对开启结果数据进行加密的加密算法，目前只支持AEAD_AES_256_GCM；示例值：AEAD_AES_256_GCM
            public static final String algorithm = "algorithm";

            // 数据密文 ciphertext string[1,1048576] 是
            // Base64编码后的开启/停用结果数据密文；示例值：sadsadsadsad
            public static final String ciphertext = "ciphertext";

            // 附加数据 associated_data string[1,16] 否
            // 附加数据；示例值：fdasfwqewlkja484w
            public static final String associatedData = "associated_data";

            // 原始类型 original_type string[1,16] 是
            // 原始回调类型，为transaction；示例值：transaction
            public static final String originalType = "original_type";

            // 随机串 nonce string[1,16] 是 加密使用的随机串；示例值：fdasflkja484w
            public static final String nonce = "nonce";
        }

        public static interface Notify {

            // 通知ID id string[1,36] 是 通知的唯一ID 示例值：EV-2018022511223320873
            public static final String id = "id";

            // 通知创建时间 create_time string[1,32] 是
            // 通知创建的时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss.表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC
            // 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示北京时间2015年05月20日13点29分35秒。示例值：2015-05-20T13:29:35+08:00
            public static final String create_time = "create_time";

            // 通知类型 event_type string[1,32] 是
            // 通知的类型，支付成功通知的类型为TRANSACTION.SUCCESS; 示例值：TRANSACTION.SUCCESS
            // 通知数据类型 resource_type string[1,32] 是
            // 通知的资源数据类型，支付成功通知为encrypt-resource; 示例值：encrypt-resource
            public static final String resource_type = "resource_type";

            // +通知数据 resource object 是 通知资源数据 json格式，见示例
            public static interface resource {
                public static final String _name = "resource";

                /****** 解密后的数据属性 */
                // 应用ID appid string[1,32] 是
                // 直连商户申请的公众号或移动应用appid。示例值：wxd678efh567hg6787
                public static final String appid = "appid";
                
                // 商户号 mchid string[1,32] 是 商户的商户号，由微信支付生成并下发。示例值：1230000109
                
                // 商户订单号 out_trade_no string[6,32] 是
                // 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。特殊规则：最小字符长度为6示例值：1217752501201407033233368018
                
                // 微信支付订单号 transaction_id string[1,32] 否
                // 微信支付系统生成的订单号。示例值：1217752501201407033233368018
                
                // 交易类型 trade_type string[1,16] 否 交易类型，枚举值：JSAPI：公众号支付;NATIVE：扫码支付; APP：APP支付; MICROPAY：付款码支付; MWEB：H5支付;FACEPAY：刷脸支付
                public static final String tradeType = "trade_type";
                // 交易状态 trade_state string[1,32] 是 交易状态，枚举值：SUCCESS：支付成功; REFUND：转入退款; NOTPAY：未支付; CLOSED：已关闭; REVOKED：已撤销（付款码支付）; USERPAYING：用户支付中（付款码支付）; PAYERROR：支付失败(其他原因，如银行返回失败)
                public static final String tradeState = "trade_state";
                // 交易状态描述 trade_state_desc string[1,256] 是

                // 交易状态描述示例值：支付失败，请重新下单支付

            }

            // 回调摘要 summary string[1,64] 是 回调摘要; 示例值：支付成功
            public static final String summary = "summary";
        }

        public static interface CertKey {
            // "serial_no": "5157F09EFDC096DE15EBE81A47057A7232F1B8E1",
            public static final String serialNo = "serial_no";

            // "effective_time ": "2018-06-08T10:34:56+08:00",
            public static final String effectiveTime = "effective_time";

            // "expire_time ": "2018-12-08T10:34:56+08:00",
            public static final String expireTime = "expire_time";

            // "encrypt_certificate"
            public static final String encryptCertificate = "encrypt_certificate";
        }

    }

    /**
     * 参数名称
     * 
     * @ClassName: MsgAttr
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface ParamName {
        /**
         * 预支付交易会话标识 prepay_id string[1,64] 是
         */
        public static final String prepayId = "prepay_id";

        /**
         * 直连商户号 string[1,32] 是 body 直连商户的商户号，由微信支付生成并下发。示例值：1230000109
         */
        public static final String mchid = "mchid";

    }

}
