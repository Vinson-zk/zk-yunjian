package com.zk.demo.yzf;

public interface ZKYzfPayConstant {
	
    /**
     * 请求参数名称
     * 
     * @ClassName: ParamsName
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
	public static interface ParamsName {
		/**
		* 商户号	merchantNo	String(32)	M	　
		*/
		static final String merchantNo = "merchantNo";
		/**
		* 商户订单号	outTradeNo	String(32)	M	商户生成的订单号,商户内部需保证唯一
		*/
		static final String outTradeNo = "outTradeNo";
		/**
		* 订单超时时间	timeOut	Long(10)	O	详情见4.9 《订单超时说明》
		*/
		static final String timeOut = "timeOut";
		/**
		* 交易金额	tradeAmt	Long(11)	M	订单金额,单位分
		*/
		static final String tradeAmt = "tradeAmt";
		/**
		* 操作人	operator	String(32)	M	操作员的工号
		*/
		static final String operator = "operator";
		/**
		* 支付平台类型	tradeType	String(32)	M	详见附录《支付平台类型描述》
		*/
		static final String tradeType = "tradeType";
		/**
		* 订单主题	subject	String(128)	M	　
		*/
		static final String subject = "subject";
		/**
		* 请求时间	requestDate	String(19)	M	yyyy-MM-dd HH:mm:ss
		*/
		static final String requestDate = "requestDate";
		/**
		* 回调通知地址	notifyUrl	String(256)	O	异步接收支付结果通知的回调地址
		*/
		static final String notifyUrl = "notifyUrl";
		/**
		* 商品信息	goodsInfo	String(256)	M	　
		*/
		static final String goodsInfo = "goodsInfo";
		/**
		* 风控信息	riskInfo	String(256)	M	详见附录4.6《风控参数说明》
		*/
		static final String riskInfo = "riskInfo";
		/**
		* 分账信息	ledgerAccount	String(256)	O	详见附录4.3《分账信息列表描述》
		*/
		static final String ledgerAccount = "ledgerAccount";

        public static interface ledgerAccountParams {
            static final String merchantNo = "merchantNo";
            static final String amount = "amount";
            static final String memo = "memo";
        }
		/**
		* 请求IP	reqIp	String(32)	M	线上请求IP地址
		*/
		static final String reqIp = "reqIp";
		/**
		* 交易前端信息	tradeFrontInfo	String(256)	C	详见附录《交易前端信息字段说明》
		*/
		static final String tradeFrontInfo = "tradeFrontInfo";
		/**
		* 交易场景	tradeScene	String(32)	M	详见附录《交易场景说明》
		*/
		static final String tradeScene = "tradeScene";
		/**
		* 应用ID	appId	String(32)	O	微信支付宝时必填。app接口:由微信、支付宝平台分配,选择微信、支付宝渠道该字段必传。小程序接口:小程序支付使用,该appId为商户小程序的appId,公众号非必填
		*/
		static final String appId = "appId";
		/**
		* 营业厅编号	storeCode	String(32)	O	建议采用电信集团统一编制的渠道,ID(营业厅编号),13 位数字,可从电信渠道视图中获取
		*/
		static final String storeCode = "storeCode";
		/**
		* 营业厅名称	storeName	String(256)	O	可从电信渠道视图中获取
		*/
		static final String storeName = "storeName";
		/**
		* 商品详情	goodsDetails	String(256)	C	4.10 《商品信息说明》
		*/
		static final String goodsDetails = "goodsDetails";
		/**
		* 用户ID	userCode	String(64)	O	
		* 1.支付平台类型为翼支付时忽略,用户使用微信支付宝 app 时必填,微信叫openid,支付宝userid;
		* 2.支付平台为非翼支付且公众号支付时,此参数必传,此参数为用户在服务商即翼支付对应appid下的唯一标识,交易类型是微信/支付宝时,先调用《获取授权id(H5)》;
		* 3.支付平台类型为非翼支付且为小程序支付,此参数必传,此参数为用户在商户小程序下的用户id,商户自行参考微信/支付宝小程序的获取openid的方式获取
		*/
		static final String userCode = "userCode";
		/**
		* 省	provinceCode	String(64)	O	省、市、区/县请传实际中文字符,如不传值则对账文件不推送该字段。
		*/
		static final String provinceCode = "provinceCode";
		/**
		* 市	cityCode	String(64)	O	省、市、区请传实际中文字符,如不传值则对账文件不推送该字段。
		*/
		static final String cityCode = "cityCode";
		/**
		* 区/县	areaCode	String(64)	O	省、市、区/县请传实际中文字符,如不传值则对账文件不推送该字段。
		*/
		static final String areaCode = "areaCode";
		/**
		* 受理终端标识码	terminalCode	String(64)	O	受理终端唯一编号
		*/
		static final String terminalCode = "terminalCode";
		/**
		* 受理终端流水号	terminalNo	String(64)	O	受理终端唯一流水号
		*/
		static final String terminalNo = "terminalNo";
		/**
		* 备注信息	remark	String(32)	O	　static final String ; 
		*/
		static final String remark = "remark";

        /**
         * 业务标识
         */
        static final String business = "business";

        static final String signMerchantNo = "signMerchantNo";

        static final String institutionCode = "institutionCode";

        static final String signType = "signType";

        static final String agreeId = "agreeId";

        /**
         * 支付成功回跳地址
         */
        static final String returnUrl = "returnUrl";

	}
	
    /**
     * 返回参数名称
     * 
     * @ClassName: BgParamaName
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface BgParamaName {
        /**
         * 调用是否成功 success Boolean M
         */
        static final String success = "success";

        /**
         * 错误码 errorCode String O success为true时errorCode和errorMsg为空
         */
        static final String errorCode = "errorCode";

        /**
         * 错误描述 errorMsg String O
         */
        static final String errorMsg = "errorMsg";

        /**
         * 商户订单号 outTradeNo String(32) M 商户下单单号
         */
        static final String outTradeNo = "outTradeNo";

        /**
         * 银行流水号 bankRequestNo String(64) M 微信/支付宝商户订单号,翼支付支付时该字段为null
         */
        static final String bankRequestNo = "bankRequestNo";

        /**
         * 交易号 tradeNo String(32) O 交易订单号
         */
        static final String tradeNo = "tradeNo";

        /**
         * 商户号 merchantNo String(32) M
         */
        static final String merchantNo = "merchantNo";

        /**
         * 交易金额 tradeAmt Long/string M 单位【分】
         */
        static final String tradeAmt = "tradeAmt";

        /**
         * 交易状态 tradeStatus String M 《交易状态描述》
         */
        static final String tradeStatus = "tradeStatus";

        /**
         * 支付链接 paymentUrl String(256) M 线下收款码返回支付链接,详情见附录《支付链接》
         */
        static final String paymentUrl = "paymentUrl";

        /**
         * 返回码 resultCode String O 如非空,为交易标识
         */
        static final String resultCode = "resultCode";

        /**
         * 返回信息 resultMsg String O 如非空,为错误原因
         */
        static final String resultMsg = "resultMsg";
    }

    public static interface ConfigKeyValue {
        /**
         * 支付平台类型描述
         */
        public static interface tradeType {
            /**
             * 翼支付用户
             */
            static final String BESTPAY = "BESTPAY";

            /**
             * 支付宝用户
             */
            static final String ALIPAY = "ALIPAY";

            /**
             * 微信用户
             */
            static final String WECHAT = "WECHAT";

            /**
             * 云闪付用户
             */
            static final String QUICKPASS = "QUICKPASS";

            /**
             * 数字货币 – 这种类型不支持分账
             */
            static final String CTDIGICCY = "CTDIGICCY";

            /**
             * PC端超级收银台
             */
            static final String WEBCASHIER = "WEBCASHIER";

            /**
             * 手机端超级收银台
             */
            static final String MOBILECASHIER = "MOBILECASHIER";

            /**
             * 线上三码合一
             */
            static final String AGGCODE = "AGGCODE";
        }

        /**
         * 交易状态
         */
        public static interface PayStatus {
            /**
             * 交易成功
             */
            public static final String SUCCESS = "SUCCESS";

            /**
             * 交易失败
             */
            public static final String FAIL = "FAIL";

            /**
             * 订单关闭
             */
            public static final String CLOSE = "CLOSE";

            /**
             * 未支付
             */
            public static final String NOTPAY = "NOTPAY";
        }

        /**
         * 交易渠道; 说明
         */
        public static interface PayChannel {
            /**
             * 翼支付用户
             */
            public static final String BESTPAY = "BESTPAY";

            /**
             * 支付宝用户
             */
            public static final String ALIPAY = "ALIPAY";

            /**
             * 微信用户
             */
            public static final String WECHAT = "WECHAT";
        }

        /**
         * 交易场景说明
         */
        public static interface tradeScene {
            /**
             * SMALLPAY 小程序（线上）
             */
            public static final String SMALLPAY = "SMALLPAY";

            /**
             * SMALLPAYOUT 小程序外置支付
             */
            public static final String SMALLPAYOUT = "SMALLPAYOUT";

            /**
             * JSAPI 公众号（线上）
             */
            public static final String JSAPI = "JSAPI";

            /**
             * NATIVE 订单二维码（线上）
             */
            public static final String NATIVE = "NATIVE";

            /**
             * APP app支付（线上）
             */
            public static final String APP = "APP";

            /**
             * H5 h5支付（线上）
             */
            public static final String H5 = "H5";

            /**
             * BARCODE 付款码（线下） （B扫C）
             */
            public static final String BARCODE = "BARCODE";

            /**
             * QRCODE 收款码（线下） （C扫B）
             */
            public static final String QRCODE = "QRCODE";

            /**
             * WEBCASHIER PC端超级收银台
             */
            public static final String WEBCASHIER = "WEBCASHIER";

            /**
             * MOBILECASHIER 手机端超级收银台
             */
            public static final String MOBILECASHIER = "MOBILECASHIER";

            /**
             * AGGCODE 线上三码合一
             */
            public static final String AGGCODE = "AGGCODE";

            /**
             * MIXCODE 聚合码
             */
            public static final String MIXCODE = "MIXCODE";
        }

        public static interface business {
            /**
             * 统一收银
             */
            public static final String v01 = "01";

            /**
             * 新销售平台
             */
            public static final String v02 = "02";
        }
	}

}
