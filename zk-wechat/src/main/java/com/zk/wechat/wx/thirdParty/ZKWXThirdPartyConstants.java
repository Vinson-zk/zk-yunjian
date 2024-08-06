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
* @Title: ZKWXThirdPartyConstants.java 
* @author Vinson 
* @Package com.zk.wechat.wx.thirdParty 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2021 4:15:54 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.thirdParty;

/** 
* @ClassName: ZKWXThirdPartyConstants 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKWXThirdPartyConstants {
    
    /**
     * 一些配置的 key
     * 
     * @ClassName: ConfigKey
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface ConfigKey {

        /**
         * 微信平台项目域名
         */
        public static final String zkWechatDomainName = "zk.wechat.domain.name.http";

        /**
         * 第三方授权回调
         */
        public static final String thirdPartyAuthCallback = "zk.wechat.wx.thirdParty.auth.callback";

        /**
         * 第三方授权回调后转发的路径
         */
        public static final String thirdPartyAuthCallbackRedirect = "zk.wechat.wx.thirdParty.auth.callback.redirect";

        /**
         * 平台默认的第三方平台账号 appid
         */
        public static final String thirdPartyDefaultAppid = "zk.wechat.wx.thirdParty.default.appid";

        // ===== # ----- 第三方平台 配置

        /**
         * 授权链接构建的方法；1、构建PC端授权链接的方法
         */
        public static final String url_auth_web_componentloginpage = "zk.wechat.wx.thirdParty.url.url_auth_web_componentloginpage";

        /**
         * 授权链接构建的方法；2、构建移动端授权链接的方法
         */
        public static final String url_auth_app_bindcomponent = "zk.wechat.wx.thirdParty.url.url_auth_app_bindcomponent";

        /**
         * 启动ticket推送服务
         */
        public static final String api_start_push_ticket = "zk.wechat.wx.thirdParty.api.api_start_push_ticket";

        /**
         * 获取 令牌（component_access_token）
         */
        public static final String api_component_token = "zk.wechat.wx.thirdParty.api.api_component_token";

        /**
         * 获取 预授权码
         */
        public static final String api_create_preauthcode = "zk.wechat.wx.thirdParty.api.api_create_preauthcode";

        /**
         * 使用授权码获取授权信息
         */
        public static final String api_query_auth = "zk.wechat.wx.thirdParty.api.api_query_auth";

        /**
         * 获取/刷新接口调用令牌
         */
        public static final String api_authorizer_token = "zk.wechat.wx.thirdParty.api.api_authorizer_token";

        /**
         * 获取授权方的帐号基本信息
         */
        public static final String api_get_authorizer_info = "zk.wechat.wx.thirdParty.api.api_get_authorizer_info";


        // ===== # ----- 第三方平台替，微信用户授权
//        /**
//         * # 请求 CODE，一般是在生成授权链接时使用，这里不需要配置，授权链接目前是手动制作
//         */
//        public static final String url_official_accounts_user_auth_authorize = "zk.wechat.wx.thirdParty.url.url_official_accounts_user_auth_authorize";

        /**
         * # 通过 code 换取 access_token
         */
        public static final String api_official_accounts_user_auth_access_token = "zk.wechat.wx.thirdParty.api.api_official_accounts_user_auth_access_token";

        /**
         * # 刷新 access_token（如果需要）
         */
        public static final String api_official_accounts_user_auth_refresh_token = "zk.wechat.wx.thirdParty.api.api_official_accounts_user_auth_refresh_token";

        /**
         * # 通过网页授权 access_token 获取用户基本信息（需授权作用域为 snsapi_userinfo）
         */
//        public static final String api_user_auth_userinfo = "zk.wechat.wx.thirdParty.api.official.accounts.user.auth.userinfo";

        /**
         * 小程序登录, 获取 openid
         */
        public static final String api_miniprogram_user_jscode2session = "zk.wechat.wx.thirdParty.api.api_miniprogram_user_jscode2session";

    }

    /**
     * 消息属性
     * 
     * @ClassName: MsgAttr
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface MsgAttr {
        
        /**
         * 消息类型字段名称
         */
        public static final String info_type = "InfoType";
        
        /**
         * 公众号用户消息类型 节点名称
         */
        public static interface InfoType {
            /**
             * 消息类型 节点名称
             */
            public static final String MsgType = "MsgType";

            /**
             * 事件类型 节点名称
             */
            public static final String Event = "Event";
        }

        /**
         * 微信错误信息
         */
        public static interface Error {
            /**
             * 微信平台推送错误码
             */
            public static final String errcode = "errcode";

            /**
             * 微信平台推送错误消息
             */
            public static final String errmsg = "errmsg";
        }
        
        /**
         * 微信开放平台推送的第三方平台令牌
         */
        public static interface ComponentVerifyTicket {
            /**
             * 第三方平台appid
             */
            public static final String AppId = "AppId";

            /**
             * 时间戳
             */
            public static final String CreateTime = "CreateTime";

            /**
             * Ticket内容
             */
            public static final String ComponentVerifyTicket = "ComponentVerifyTicket";
        }

        /**
         * 获取第三平台接口调用凭证
         */
        public static interface ComponentAccessToken {
            /**
             * 有效时间，单位为秒
             */
            public static final String expires_in = "expires_in";

            /**
             * 微信平台第三方的 component_access_token
             */
            public static final String component_access_token = "component_access_token";
        }

        /**
         * 获取预授权码
         */
        public static interface PreAuthCode {
            /**
             * 有效时间，单位为秒
             */
            public static final String expires_in = "expires_in";

            /**
             * 预授权码
             */
            public static final String pre_auth_code = "pre_auth_code";
        }
        
        /**
         * 授权方授权信息 authorization_info
         */
        public static interface AuthorizationInfo {
            /**
             * 节点名称
             */
            public static final String _name = "authorization_info";

            /**
             * 授权方appid
             */
            public static final String authorizer_appid = "authorizer_appid";

            /**
             * 授权方接口调用凭据（在授权的公众号或小程序具备API权限时，才有此返回值），也简称为令牌
             */
            public static final String authorizer_access_token = "authorizer_access_token";

            /**
             * 有效期（在授权的公众号或小程序具备API权限时，才有此返回值）
             */
            public static final String expires_in = "expires_in";

            /**
             * 接口调用凭据刷新令牌（在授权的公众号具备API权限时，才有此返回值），
             * 刷新令牌主要用于第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，请妥善保存。
             * 一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌
             */
            public static final String authorizer_refresh_token = "authorizer_refresh_token";

            /**
             * 授权给开发者的权限集列表
             */
            public static interface FuncInfo {
                /**
                 * 节点名称
                 */
                public static final String _name = "func_info";

                /**
                 * 权限分类
                 */
                public static interface FuncscopeCategory {
                    /**
                     * 节点名称
                     */
                    public static final String _name = "funcscope_category";

                    /**
                     * 权限ID
                     */
                    public static final String id = "id";
                }
            }
        }

        /**
         * 使用授权码换取公众号或小程序的接口调用凭据和授权信息
         */
        public static interface AuthorizerInfo {
            /**
             * 节点名称
             */
            public static final String _name = "authorizer_info";

            /**
             * 授权方昵称
             */
            public static final String nick_name = "nick_name";

            /**
             * 授权方头像
             */
            public static final String head_img = "head_img";

            /**
             * 授权方类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
             */
            public static final String service_type_info = "service_type_info";

            /**
             * 授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，
             * 3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过
             * 、还未通过名称认证，但通过了腾讯微博认证
             */
            public static final String verify_type_info = "verify_type_info";

            /**
             * 授权方 的原始ID
             */
            public static final String user_name = "user_name";

            /**
             * 主体名称
             */
            public static final String principal_name = "principal_name";

            /**
             * 用以了解以下功能的开通状况（0代表未开通，1代表已开通）： open_store:是否开通微信门店功能；
             * open_scan:是否开通微信扫商品功能 open_pay:是否开通微信支付功能； open_card:是否开通微信卡券功能；
             * open_shake:是否开通微信摇一摇功能；
             */
            public static final String business_info = "business_info";

            /**
             * 授权方 所设置的微信号，可能为空
             */
            public static final String alias = "alias";

            /**
             * 授权方 string 帐号介绍
             */
            public static final String signature = "signature";

            /**
             * 二维码图片的URL，开发者最好自行也进行保存
             */
            public static final String qrcode_url = "qrcode_url";
        }

        /**
         * 授权成功消息 authorizer_info
         */
        public static interface AuthorizedInfo {
            /**
             * 第三方平台appid
             */
            public static final String AppId = "AppId";

            /**
             * 创建时间，秒；示例：1413192760
             */
            public static final String CreateTime = "CreateTime";

            /**
             * 公众号appid
             */
            public static final String AuthorizerAppid = "AuthorizerAppid";

            /**
             * 授权码（code）
             */
            public static final String AuthorizationCode = "AuthorizationCode";

            /**
             * 过期时间
             */
            public static final String AuthorizationCodeExpiredTime = "AuthorizationCodeExpiredTime";

            /**
             * 预授权码
             */
            public static final String PreAuthCode = "PreAuthCode";

        }

        /**
         * 用户授权回调
         */
        public static interface UserAuthCallbak {
            /**
             * 授权码
             */
            public static final String code = "code";

            /**
             * 自定义的参数
             */
            public static final String state = "state";

            /**
             * appId 公众号的appid
             */
            public static final String appid = "appid";
        }

        /**
         * 通过code换取access_token; 刷新与此相同
         */
        public static interface UserAuthToken {
            /**
             * 接口调用凭证
             */
            public static final String access_token = "access_token";

            /**
             * access_token接口调用凭证超时时间，单位（秒）
             */
            public static final String expires_in = "expires_in";

            /**
             * 用户刷新access_token
             */
            public static final String refresh_token = "refresh_token";

            /**
             * 授权用户唯一标识
             */
            public static final String openid = "openid";

            /**
             * 用户授权的作用域，使用逗号（,）分隔
             */
            public static final String scope = "scope";
        }

        /**
         * 通过网页授权access_token获取用户基本信息（需授权作用域为snsapi_userinfo）
         */
        public static interface UserAuthInfo {

            /**
             * 用户的唯一标识
             */
            public static final String openid = "openid";

            /**
             * 用户昵称
             */
            public static final String nickname = "nickname";

            /**
             * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
             */
            public static final String sex = "sex";

            /**
             * 用户个人资料填写的省份
             */
            public static final String province = "province";

            /**
             * 普通用户个人资料填写的城市
             */
            public static final String city = "city";

            /**
             * 国家，如中国为CN
             */
            public static final String country = "country";

            /**
             * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），
             * 用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
             */
            public static final String headimgurl = "headimgurl";

            /**
             * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
             */
            public static final String privilege = "privilege";

            /**
             * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见： 获取用户个人信息（UnionID机制）
             */
            public static final String unionid = "unionid";
        }

        /**
         * 授权方 js ticket 属性
         */
        public static interface JsTicket {
            /**
             * 令牌
             */
            public static final String ticket = "ticket";

            /**
             * 过期时间
             */
            public static final String expires_in = "expires_in";
        }

        /**
         * 用户上报地理位置事件消息，即用户上报 GPS 事件消息 属性名称
         */
        public static interface LocationMsgEvent {
            /**
             * 开发者 微信号
             */
            public static final String ToUserName = "ToUserName";

            /**
             * 发送方帐号（一个OpenID）
             */
            public static final String FromUserName = "FromUserName";

            /**
             * 消息创建时间 （整型）
             */
            public static final String CreateTime = "CreateTime";

            /**
             * 地理位置纬度
             */
            public static final String Latitude = "Latitude";

            /**
             * 地理位置经度
             */
            public static final String Longitude = "Longitude";

            /**
             * 地理位置精度
             */
            public static final String Precision = "Precision";

        }

    }
    
    /**
     * 向微信开放平台发送消息时的参数名
     */
    public static interface ParamName {

        /**
         * 获取第三平台接口调用凭证
         */
        public static interface ComponentAccessToken {
            /**
             * 第三方平台 appId
             */
            public static final String component_appid = "component_appid";

            /**
             * 第三方平台 appsecret key
             */
            public static final String component_appsecret = "component_appsecret";

            /**
             * 第三方平台有效令牌
             */
            public static final String component_verify_ticket = "component_verify_ticket";
        }

        /**
         * 获取第三平台接口调用凭证
         */
        public static interface PreAuthCode {
            /**
             * 第三方平台 appId
             */
            public static final String component_appid = "component_appid";
        }

        /**
         * 获取公众号或小程序授权信息、接口调用凭证、刷新凭证
         */
        public static interface AuthorizationInfo {
            /**
             * 第三方平台 appId
             */
            public static final String component_appid = "component_appid";

            /**
             * 授权码
             */
            public static final String authorization_code = "authorization_code";

        }

        /**
         * 刷新公众号或小程序接口调用凭证
         */
        public static interface RefreshAuthorizerInfo {
            /**
             * 第三方平台 appId
             */
            public static final String component_appid = "component_appid";

            /**
             * 授权公众号的授权APPID，注意不是授权公众号的原始 ID
             */
            public static final String authorizer_appid = "authorizer_appid";

            /**
             * 刷新凭证
             */
            public static final String authorizer_refresh_token = "authorizer_refresh_token";
        }

        /**
         * 获取公众号信息
         * 
         * @ClassName: AuthorizerOfficialInfo
         * @Description: TODO(simple description this class what to do.)
         * @author bs
         * @version 1.0
         */
        public static interface AuthorizerInfo {
            /**
             * 第三方平台 appId
             */
            public static final String component_appid = "component_appid";

            /**
             * 授权公众号的授权APPID，注意不是授权公众号的原始 ID
             */
            public static final String authorizer_appid = "authorizer_appid";
        }
    }
}
