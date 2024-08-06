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
import com.zk.wechat.pay.entity.ZKPayGetNotify;
import com.zk.wechat.pay.service.ZKPayGetNotifyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ZKPayGetNotifyController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.pay}/payGetNotify")
public class ZKPayGetNotifyController extends ZKBaseController {

	@Autowired
	private ZKPayGetNotifyService payGetNotifyService;
	
//	// 编辑
//	@RequestMapping(value="payGetNotify", method = RequestMethod.POST)
//	public ZKMsgRes payGetNotifyPost(@RequestBody ZKPayGetNotify payGetNotify){
//		this.payGetNotifyService.save(payGetNotify);
//        return ZKMsgRes.as("zk.0", null, payGetNotify);
//	}
	
	// 查询详情
	@RequestMapping(value="payGetNotify", method = RequestMethod.GET)
	public ZKMsgRes payGetNotifyGet(@RequestParam("pkId") String pkId){
		ZKPayGetNotify payGetNotify = this.payGetNotifyService.get(new ZKPayGetNotify(pkId));
        return ZKMsgRes.asOk(payGetNotify);
	}
	
	// 分页查询 
	@RequestMapping(value="payGetNotifysPage", method = RequestMethod.GET)
	public ZKMsgRes payGetNotifysPageGet(ZKPayGetNotify payGetNotify, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKPayGetNotify> resPage = ZKPage.asPage(hReq);
        resPage = this.payGetNotifyService.findPage(resPage, payGetNotify);
        return ZKMsgRes.asOk(resPage);
	}
	
//	// 批量删除
//	@RequestMapping(value="payGetNotify", method = RequestMethod.DELETE)
//	@ResponseBody
//	public ZKMsgRes payGetNotifyDel(@RequestParam("pkId[]") String[] pkIds){
//		 int count = 0;
//        if (pkIds != null && pkIds.length > 0) {
//            for (String pkId : pkIds) {
//                count += this.payGetNotifyService.del(new ZKPayGetNotify(pkId));
//            }
//        }
//        return ZKMsgRes.asOk(count);
//	}

}