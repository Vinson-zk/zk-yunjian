package com.zk.demo.yzf;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;

import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.demo.yzf.ZKYzfHelper.Urls;
import com.zk.demo.yzf.ZKYzfPayConstant.BgParamaName;
import com.zk.demo.yzf.ZKYzfPayConstant.ConfigKeyValue;
import com.zk.demo.yzf.ZKYzfPayConstant.ParamsName;
import com.zk.demo.yzf.common.BestpayResult;
import com.zk.demo.yzf.utils.CertificateUtil;
import com.zk.demo.yzf.utils.TradeCertificate;

import junit.framework.TestCase;
import net.sf.json.JSONObject;

public class ZKYzfPayTests {
	
	public TradeCertificate getTradeCertificate(String merchantNo) {
		
        String certConfigPath = "yzf/merchant/" + merchantNo + "/yzf.config.properties";
//        certConfigPath = ZKUtils.getAbsolutePath(certConfigPath);
        System.out.println("[^_^:20230423-0926-001] certConfigPath: " + certConfigPath);
		TradeCertificate tradeCertificate= CertificateUtil.getTradeCertificate(certConfigPath, CertificateUtil.KEYSTORETYPE_PKCS12);
		return tradeCertificate;
	}
	
	/*
     * // 电子口岸 --------------------------------
     * 
     * merchantNo 3178037580313054
     * 
     * // 国码 --------------------------------
     * 
     * merchantNo 3178037809033684
     */
	
    /**
     * 
     *
     * @Title: testPayMain
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 21, 2023 10:49:19 AM
     * @return void
     */
	@Test
    public void testPay() {
	    
		try {
            String merchantNo = "";
            String outTradeNo = "";
            TreeMap<String, String> params;

            merchantNo = "3178037580313054";
            params = new TreeMap<String, String>();
            // 商户号 merchantNo String(32) M
            params.put(ParamsName.merchantNo, merchantNo);
            // 交易金额 tradeAmt Long(11) M 订单金额,单位分
            params.put(ParamsName.tradeAmt, "1");
            // 操作人 operator String(32) M 操作员的工号
            params.put(ParamsName.operator, "9527");
            // 订单主题 subject String(128) M
            params.put(ParamsName.subject, "测试支付-几阵秋风能应候");
            // 请求时间 requestDate String(19) M yyyy-MM-dd HH:mm:ss
            params.put(ParamsName.requestDate,
                    ZKDateUtils.formatDate(ZKDateUtils.getToday(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            // 商品信息 goodsInfo String(256) M
            params.put(ParamsName.goodsInfo, "测试支付-汾水鼎");
            // 风控信息 riskInfo String(256) M 详见附录4.6《风控参数说明》
            params.put(ParamsName.riskInfo,
                    "[{\"boby\":\"\",\"goods_count\":1,\"product_type\":\"2\",\"service_cardno\":\"9000000001774046\",\"service_identify\":\"10000000\",\"subject\":\"中石油100元加油卡\"}]");
            // 请求IP reqIp String(32) M 线上请求IP地址
            params.put(ParamsName.reqIp, "120.198.213.34");

            params.put(ParamsName.signType, "S002");
//            params.put(ParamsName.agreeId, "20201103030100063047385974047788");
//            params.put(ParamsName.business, ConfigKeyValue.business.v01);
            params.put(ParamsName.signMerchantNo, merchantNo);
            params.put(ParamsName.institutionCode, merchantNo);

            // 支付平台类型 tradeType String(32) M 详见附录《支付平台类型描述》
            params.put(ParamsName.tradeType, ConfigKeyValue.tradeType.WEBCASHIER);
            // 交易场景 tradeScene String(32) M 详见附录《交易场景说明》
            params.put(ParamsName.tradeScene, ConfigKeyValue.tradeScene.WEBCASHIER);
            // 商户订单号 outTradeNo String(32) M 商户生成的订单号,商户内部需保证唯一
            outTradeNo = ZKDateUtils.formatDate(ZKDateUtils.getToday(), "yyyyMMddHHmmss");
            outTradeNo = "20230506";
            params.put(ParamsName.outTradeNo, outTradeNo + "000001");
            params.put(ParamsName.returnUrl, "https://product.zheport.com/tdwzh/#/");

            params.put(ParamsName.ledgerAccount,
                    String.format("[{\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"\"}]",
                            ParamsName.ledgerAccountParams.merchantNo, "3178037809033684",
                            ParamsName.ledgerAccountParams.amount, "1", ParamsName.ledgerAccountParams.memo));

            ZKYzfBestpayHandler yzfBestpayHandler = new ZKYzfBestpayHandler(this.getTradeCertificate(merchantNo));

            this.sing(yzfBestpayHandler, params);
//            this.doPay(yzfBestpayHandler, params);
            this.doPay(Urls.gapiRequestUrl + "/pay/tradeCreate", yzfBestpayHandler, JSONObject.fromObject(params));
			
		}catch (Exception e) {
			e.printStackTrace();
			TestCase.assertTrue(false);
		}
	}
	
    private void sing(ZKYzfBestpayHandler yzfBestpayHandler, TreeMap<String, String> params)
            throws UnsupportedEncodingException, GeneralSecurityException {
        System.out.println("[^_^:20230505-1758-001] params: " + ZKJsonUtils.writeObjectJson(params));
        StringBuilder signData = new StringBuilder();
        for (Entry<String, String> v : params.entrySet()) {
            signData.append(v.getKey()).append("=").append(v.getValue()).append("&");
        }
        String tobeSigned = signData.deleteCharAt(signData.length() - 1).toString();
        System.out.println("[^_^:20230421-1047-001] TreeMap.tobeSigned: " + tobeSigned);
        String sign = yzfBestpayHandler.sign(tobeSigned, ZKYzfBestpayHandler.SIGNATURE_ALGORITHM_SHA256);
        System.out.println("[^_^:20230421-1047-001] TreeMap.sign: " + sign);
        params.put("sign", sign);
    }

    private void doPay(String requestUrl, ZKYzfBestpayHandler yzfBestpayHandler, JSONObject params)
            throws UnsupportedEncodingException, GeneralSecurityException {
        BestpayResult<String> bestpayResult = yzfBestpayHandler.doService(requestUrl,
                params.toString());
        System.out.println("[^_^:20230421-0923-001] bestpayResult: " + ZKJsonUtils.writeObjectJson(bestpayResult));
        if (!"200".equals(bestpayResult.getCode())) {
            TestCase.assertTrue(false);
        }
        else {
            System.out.println("[^_^:20230421-0923-002] bestpayResult.data: " + bestpayResult.getData());
            JSONObject data = JSONObject.fromObject(bestpayResult.getData());
            if (!data.getBoolean(BgParamaName.success)) {
                System.out
                        .println("[>_<:20230423-1017-001] errorCode: " + data.getString(BgParamaName.errorCode));
                System.out.println("[>_<:20230423-1017-001] errorMsg: " + data.getString(BgParamaName.errorMsg));
                TestCase.assertTrue(false);
            }

            System.out.println("[^_^:20230421-0923-003] bestpayResult.data.result.paymentUrl: "
                    + data.getJSONObject("result").getString(BgParamaName.paymentUrl));
        }
    }

    /*
      #3178002072268273
        institutionCode =3178002070492308
        #湖北要测试  3178002072268273  3178002073155499  间连nativie 时用：3178002073155501 ,专门用jsapi封装的.数字货币 3178002069271800   
        测试环境代扣：3178002074539102  3178002072012993
        #测试环境线下收款码三码合一，要用这个商户号：3178002073155501   这商户号是建立支付宝的，真实支付宝可以支付，目前只知道这个商户号支付宝间连进件成功
        #小云测试环境 线上微信封装H5场景 3178002073155499 3178002071869644，pc手机端超级收银台专用测试商户号 3178002073155499 ,老企客聚合接口：3178002073155499   延时分账 3178002074497299
        merchantNo=3178002072268273
        shoudanMerchantNo=3178002072012993
        jianrongMerchantNo=3178002071869644
        laojuheMerchantNo=3178002073155499
        #增加延迟分账测试商户好
        yanchifenzhangMerchantNo=3178002074497299
     */
    @Test
    public void testPayTest() {

        try {
            String merchantNo = "";
            String outTradeNo = "";
            TreeMap<String, String> params;

            merchantNo = "3178002072268273";
            params = new TreeMap<String, String>();
            // 商户号 merchantNo String(32) M
            params.put(ParamsName.merchantNo, merchantNo);
            // 商户订单号 outTradeNo String(32) M 商户生成的订单号,商户内部需保证唯一
            outTradeNo = ZKDateUtils.formatDate(ZKDateUtils.getToday(), "yyyyMMddHHmmss");
            outTradeNo = "20230421101926";
            params.put(ParamsName.outTradeNo, outTradeNo + "000001");
            // 交易金额 tradeAmt Long(11) M 订单金额,单位分
            params.put(ParamsName.tradeAmt, "1");
            // 操作人 operator String(32) M 操作员的工号
            params.put(ParamsName.operator, "9527");
            // 支付平台类型 tradeType String(32) M 详见附录《支付平台类型描述》
            params.put(ParamsName.tradeType, ConfigKeyValue.tradeType.AGGCODE);
            // 订单主题 subject String(128) M
            params.put(ParamsName.subject, "几阵秋风能应候");
            // 请求时间 requestDate String(19) M yyyy-MM-dd HH:mm:ss
            params.put(ParamsName.requestDate,
                    ZKDateUtils.formatDate(ZKDateUtils.getToday(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            // 商品信息 goodsInfo String(256) M
            params.put(ParamsName.goodsInfo, "汾水鼎");
            // 风控信息 riskInfo String(256) M 详见附录4.6《风控参数说明》
            params.put(ParamsName.riskInfo,
                    "[{\"boby\":\"\",\"goods_count\":1,\"product_type\":\"2\",\"service_cardno\":\"9000000001774046\",\"service_identify\":\"10000000\",\"subject\":\"中石油100元加油卡\"}]");

            // 请求IP reqIp String(32) M 线上请求IP地址
            params.put(ParamsName.reqIp, "120.198.213.34");
            // 交易场景 tradeScene String(32) M 详见附录《交易场景说明》
            params.put(ParamsName.tradeScene, ConfigKeyValue.tradeScene.AGGCODE);

            params.put(ParamsName.signMerchantNo, "3178002070492308");
            params.put(ParamsName.institutionCode, "3178002070492308");
            params.put(ParamsName.signType, "S002");
//            params.put(ParamsName.agreeId, "");

            /**********************/
//            params.put("agreeId", "20201023030100058967803171962904");
//            params.put("business", "01");
//            params.put("goodsInfo", "测试产品12222");
//            params.put("institutionCode", "3178002070492308");
//            params.put("merchantNo", merchantNo);
//            params.put("notifyUrl", "http://134.176.127.162:9086/bss/billing/payToBank/notifyCallback");
//            params.put("operator", "hzqdemoTest");
//            params.put("outTradeNo", "P_20230423142036507");
//            params.put("remark", "aa20230423142036506");
//            params.put("reqIp", "120.198.213.34");
//            params.put("requestDate",
//                    ZKDateUtils.formatDate(ZKDateUtils.getToday(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
//            params.put("riskInfo",
//                    "[{\"boby\":\"\",\"goods_count\":1,\"product_type\":\"2\",\"service_cardno\":\"9000000001774046\",\"service_identify\":\"10000000\",\"subject\":\"中石油100元加油卡\"}]");
//            params.put("signMerchantNo", "3178002070492308");
//            params.put("signType", "S002");
//            params.put("subject", "aaaa");
//            params.put("timeOut", "300");
//            params.put("tradeAmt", "1");
//            params.put("tradeScene", "AGGCODE");
//            params.put("tradeType", "AGGCODE");

            ZKYzfBestpayHandler yzfBestpayHandler = new ZKYzfBestpayHandler(this.getTradeCertificate(merchantNo));

            this.sing(yzfBestpayHandler, params);
//            this.doPay(yzfBestpayHandler, params);
            this.doPay(Urls.gapiRequestUrlTest + "/pay/tradeCreate", yzfBestpayHandler, JSONObject.fromObject(params));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
