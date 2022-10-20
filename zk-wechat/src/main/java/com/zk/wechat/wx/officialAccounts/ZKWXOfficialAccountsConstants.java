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
* @Title: ZKWXOfficialAccountConstants.java 
* @author Vinson 
* @Package com.zk.wechat.wx 
* @Description: TODO(simple description this file what to do. ) 
* @date May 19, 2022 12:57:14 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.officialAccounts;
/** 
* @ClassName: ZKWXOfficialAccountConstants 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKWXOfficialAccountsConstants {
    
    /**
     * 一些配置的 key
     * 
     * @ClassName: ConfigKey
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface ConfigKey {

        // ===== # ----- 微信用户授权
        /**
         * # 请求 CODE
         */
//        public static final String api_user_auth_authorize = "zk.wechat.wx.officialAccounts.api.user.auth.authorize";

        /**
         * # 通过 code 换取 access_token
         */
        public static final String api_user_auth_access_token = "zk.wechat.wx.officialAccounts.api.api_user_auth_access_token";

        /**
         * # 刷新 access_token（如果需要）
         */
        public static final String api_user_auth_refresh_token = "zk.wechat.wx.officialAccounts.api.api_user_auth_refresh_token";

        /**
         * # 通过网页授权 access_token 获取用户基本信息（需授权作用域为 snsapi_userinfo）
         */
        public static final String api_user_auth_userinfo = "zk.wechat.wx.officialAccounts.api.api_user_auth_userinfo";

        /**
         * # 获取用户基本信息（包括 UnionID 机制）
         */
        public static final String api_user_auth_info = "zk.wechat.wx.officialAccounts.api.api_user_auth_info";

    }

    /**
     * 用户授权作用域
     * 
     * @ClassName: KeyAuthUserScope
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface KeyAuthUserScope {
        /**
         * 是用来获取进入页面的用户的 openid 的，并且是静默授权并自动跳转到回调页的。用户感知的就是直接进入了回调页（往往是业务页面）
         */
        public static final String snsapi_base = "snsapi_base";

        /**
         * 是用来获取用户的基本信息的。但这种授权需要用户手动同意，并且由于用户同意过，所以无须关注，就可在授权后获取该用户的基本信息。
         */
        public static final String snsapi_userinfo = "snsapi_userinfo";
    }

    /**
     * 事件或消息属性
     * 
     * @ClassName: MsgAttr
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface MsgAttr {
        /**
         * 通知类型字段名称
         */
        public static final String MsgType = "MsgType";

        /**
         * 事件类型字段名称
         */
        public static final String Event = "Event";

        /**
         * 开发者微信号
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
         * 事件KEY值，qrscene_为前缀，后面为二维码的参数值
         */
        public static final String EventKey = "EventKey";

        /**
         * 二维码的ticket，可用来换取二维码图片
         */
        public static final String Ticket = "Ticket";

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

        /**
         * 文本消息内容
         */
        public static final String Content = "Content";

        /**
         * 消息id，64位整型
         */
        public static final String MsgId = "MsgId";

        /**
         * 文章id（消息如果来自文章时才有）
         */
        public static final String MsgDataId = "MsgDataId";

        /**
         * 多图文时第几篇文章，从1开始（消息如果来自文章时才有）
         */
        public static final String Idx = "Idx";

        /**
         * 图片链接（由系统生成）
         */
        public static final String PicUrl = "PicUrl";

        /**
         * 图片消息媒体id，可以调用获取临时素材接口拉取数据。
         */
        public static final String MediaId = "MediaId";

        /**
         * 语音格式，如amr，speex等
         */
        public static final String Format = "Format";

        /**
         * 语音识别结果，UTF8编码
         */
        public static final String Recognition = "Recognition";

        /**
         * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
         */
        public static final String ThumbMediaId = "ThumbMediaId";

        /**
         * 地理位置纬度
         */
        public static final String Location_X = "Location_X";

        /**
         * 地理位置经度
         */
        public static final String Location_Y = "Location_Y";

        /**
         * 地图缩放大小
         */
        public static final String Scale = "Scale";

        /**
         * 地理位置信息
         */
        public static final String Label = "Label";

        /**
         * 消息标题
         */
        public static final String Title = "Title";

        /**
         * 消息描述
         */
        public static final String Description = "Description";

        /**
         * 消息链接
         */
        public static final String Url = "Url";

        /**
         * 用户授权信息 access_tokenInfo
         */
        public static interface access_tokenInfo {
            /**
             * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
             */
            public static final String access_token = "access_token";

            /**
             * access_token 接口调用凭证超时时间，单位（秒）
             */
            public static final String expires_in = "expires_in";

            /**
             * 用户刷新access_token
             */
            public static final String refresh_token = "refresh_token";

            /**
             * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
             */
            public static final String openid = "openid";

            /**
             * 用户授权的作用域，使用逗号（,）分隔
             */
            public static final String scope = "scope";
        }

        /**
         * 用户信息 access_tokenInfo
         */
        public static interface userInfo {
            /**
             * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
             */
            public static final String subscribe = "subscribe";

            /**
             * 用户的标识，对当前公众号唯一
             */
            public static final String openid = "openid";

            /**
             * 用户的语言，简体中文为zh_CN
             */
            public static final String language = "language";
            /**
             * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
             */
            public static final String subscribe_time = "subscribe_time";

            /**
             * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
             */
            public static final String unionid = "unionid";
            
            /**
             * 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
             */
            public static final String remark = "remark";
            
            /**
             * 用户所在的分组ID（兼容旧的用户分组接口）
             */
            public static final String groupid = "groupid";
            
            /**
             * 用户被打上的标签 ID 列表
             */
            public static final String tagid_list = "tagid_list";
            
            /**
             * 返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK
             * 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_REPRINT 他人转载 ,ADD_SCENE_LIVESTREAM
             * 视频号直播，ADD_SCENE_CHANNELS 视频号 , ADD_SCENE_OTHERS 其他
             */
            public static final String subscribe_scene = "subscribe_scene";

            /**
             * 二维码扫码场景（开发者自定义）
             */
            public static final String qr_scene = "qr_scene";

            /**
             * 二维码扫码场景描述（开发者自定义）
             */
            public static final String qr_scene_str = "qr_scene_str";

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
             * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像 URL 将失效。
             */
            public static final String headimgurl = "headimgurl";

            /**
             * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
             */
            public static final String privilege = "privilege";

        }

        /**
         * 小程序登录 第三方平台开发者的服务器使用登录凭证（code）以及第三方平台的 component_access_token 可以代替小程序实现登录功能 获取 session_key 和 openid
         * 
         * @ClassName: jscode2session
         * @Description: TODO(simple description this class what to do. )
         * @author Vinson
         * @version 1.0
         */
        public static interface jscode2session {
            /**
             * string 用户唯一标识的 openid
             */
            public static final String openid = "openid";

            /**
             * string 会话密钥
             */
            public static final String session_key = "session_key";

            /**
             * string 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。
             */
            public static final String unionid = "unionid";
        }

    }

}
