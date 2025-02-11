/**
 * 
 */
package com.zk.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.iot.entity.ZKIotProdCategores;
import com.zk.iot.service.ZKIotProdCategoresService;

/**
 * ZKIotProdCategoresController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.iot}/${zk.iot.version}/iotProdCategores")
public class ZKIotProdCategoresController extends ZKBaseController {

	@Autowired
	private ZKIotProdCategoresService iotProdCategoresService;
	
	// 编辑
	@RequestMapping(value="iotProdCategores", method = RequestMethod.POST)
	public ZKMsgRes iotProdCategoresPost(@RequestBody ZKIotProdCategores iotProdCategores){
		this.iotProdCategoresService.save(iotProdCategores);
        return ZKMsgRes.asOk(iotProdCategores);
	}
	
	// 查询详情
	@RequestMapping(value="iotProdCategores", method = RequestMethod.GET)
    public ZKMsgRes iotProdCategoresGet(@RequestParam("pkId") String pkId) {
		ZKIotProdCategores iotProdCategores = this.iotProdCategoresService.getDetail(new ZKIotProdCategores(pkId));
        return ZKMsgRes.asOk(iotProdCategores);
	}
	
	// 分页 树形查询 
	@RequestMapping(value="iotProdCategoressTree", method = RequestMethod.GET)
    public ZKMsgRes iotProdCategoressTree(ZKIotProdCategores iotProdCategores, ServerWebExchange serverWebExchange) {
        ZKPage<ZKIotProdCategores> resPage = ZKPage.asPage(serverWebExchange.getRequest());
        resPage = this.iotProdCategoresService.findTree(resPage, iotProdCategores);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 分页 列表查询 
	@RequestMapping(value="iotProdCategoress", method = RequestMethod.GET)
    public ZKMsgRes iotProdCategoress(ZKIotProdCategores iotProdCategores, ServerWebExchange serverWebExchange) {
        ZKPage<ZKIotProdCategores> resPage = ZKPage.asPage(serverWebExchange.getRequest());
        resPage = this.iotProdCategoresService.findPage(resPage, iotProdCategores);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="iotProdCategores", method = RequestMethod.DELETE)
    public ZKMsgRes iotProdCategoresDel(@RequestParam("pkId[]") String[] pkIds) {
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.iotProdCategoresService.del(new ZKIotProdCategores(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}