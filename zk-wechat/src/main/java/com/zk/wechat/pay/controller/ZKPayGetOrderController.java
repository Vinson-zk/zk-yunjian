/**
 * 
 */
package com.zk.wechat.pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.wechat.pay.entity.ZKPayGetOrder;
import com.zk.wechat.pay.service.ZKPayGetOrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ZKPayGetOrderController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.pay}/payGetOrder")
public class ZKPayGetOrderController extends ZKBaseController {

	@Autowired
	private ZKPayGetOrderService payGetOrderService;
	
//	// 编辑
//	@RequestMapping(value="payGetOrder", method = RequestMethod.POST)
//	public ZKMsgRes payGetOrderPost(@RequestBody ZKPayGetOrder payGetOrder){
//		this.payGetOrderService.save(payGetOrder);
//        return ZKMsgRes.asOk(payGetOrder);
//	}
	
	// 查询详情
	@RequestMapping(value="payGetOrder", method = RequestMethod.GET)
	public ZKMsgRes payGetOrderGet(@RequestParam("pkId") String pkId){
		ZKPayGetOrder payGetOrder = this.payGetOrderService.get(new ZKPayGetOrder(pkId));
        return ZKMsgRes.asOk(payGetOrder);
	}
	
	// 分页查询 
	@RequestMapping(value="payGetOrdersPage", method = RequestMethod.GET)
	public ZKMsgRes payGetOrdersPageGet(ZKPayGetOrder payGetOrder, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKPayGetOrder> resPage = ZKPage.asPage(hReq);
        resPage = this.payGetOrderService.findPage(resPage, payGetOrder);
        return ZKMsgRes.asOk(resPage);
	}
	
//	// 批量删除
//	@RequestMapping(value="payGetOrder", method = RequestMethod.DELETE)
//	@ResponseBody
//	public ZKMsgRes payGetOrderDel(@RequestParam("pkId[]") String[] pkIds){
//		 int count = 0;
//        if (pkIds != null && pkIds.length > 0) {
//            for (String pkId : pkIds) {
//                count += this.payGetOrderService.del(new ZKPayGetOrder(pkId));
//            }
//        }
//        return ZKMsgRes.asOk(count);
//	}

}