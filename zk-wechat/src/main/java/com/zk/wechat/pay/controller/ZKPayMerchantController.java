/**
 * 
 */
package com.zk.wechat.pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.wechat.pay.entity.ZKPayMerchant;
import com.zk.wechat.pay.service.ZKPayMerchantService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ZKPayMerchantController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.pay}/payMerchant")
public class ZKPayMerchantController extends ZKBaseController {

	@Autowired
	private ZKPayMerchantService payMerchantService;
	
	// 编辑
	@RequestMapping(value="payMerchant", method = RequestMethod.POST)
	public ZKMsgRes payMerchantPost(@RequestBody ZKPayMerchant payMerchant){
		this.payMerchantService.save(payMerchant);
        return ZKMsgRes.asOk(payMerchant);
	}
	
	// 查询详情
	@RequestMapping(value="payMerchant", method = RequestMethod.GET)
	public ZKMsgRes payMerchantGet(@RequestParam("pkId") String pkId){
		ZKPayMerchant payMerchant = this.payMerchantService.get(new ZKPayMerchant(pkId));
        return ZKMsgRes.asOk(payMerchant);
	}
	
	// 分页查询 
	@RequestMapping(value="payMerchantsPage", method = RequestMethod.GET)
	public ZKMsgRes payMerchantsPageGet(ZKPayMerchant payMerchant, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKPayMerchant> resPage = ZKPage.asPage(hReq);
        resPage = this.payMerchantService.findPage(resPage, payMerchant);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="payMerchant", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes payMerchantDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.payMerchantService.del(new ZKPayMerchant(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}