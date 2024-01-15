package com.zk.demo.yzf;

public class ZKYzfHelper {

    public static interface Urls {
		/**
		 * #测试使用综测回调地址，还会模拟返回 收到回调应答到开放平台     
		 * # {"resultCode":"SUCCESS","resultMsg":"OK"}
		 * # {"resultCode":"FAILED","resultMsg":"FAILED"}  ,最多重试2次,重试时间间隔为5分钟
		 * #notifyUrl=http://byang.free.idcfengye.com/notify/myNotify
		 */
		final static String notifyUrl = "http://172.17.179.175:8030/notify";
		
        /**
         * #测试环境域名地址
         * 
         * https://mapi.bestpay.com.cn/mapi
         * 
         * https://mapi.test.bestpay.net/mapi
         * 
         */
        final static String requestUrlTest = "https://mapi.test.bestpay.net/mapi";

        final static String requestUrl = "https://mapi.bestpay.com.cn/mapi";
		/**
		 * #测试环境开放平台域名地址
		 */
        final static String gapiRequestUrlTest = "https://mapi.test.bestpay.net/gapi";

        final static String gapiRequestUrl = "https://mapi.bestpay.com.cn/gapi";
		/**
		 * #获取openid地址
		 */
		final static String openIdUrl = "https://h5.bestpay.com.cn/subapps/merchant-collectpay-h5/index.html#/auth";
		/**
		 * #云闪付配置回调地址
		 */
		final static String quickPayUrl = "http://192.168.4 .15:8081/pay/publicorderQRCODE";
		
        /**
         * #翼支付h5收银台拉起地址
         * 
         * https://mapi.test.bestpay.net/mapi/form/cashier/H5/pay
         * 
         */
        final static String bestpayGatewayUrl = "https://mapi.test.bestpay.net/mapi/form/cashier/H5/pay";

	}
}
