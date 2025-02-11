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
import com.zk.iot.entity.ZKIotProd;
import com.zk.iot.service.ZKIotProdService;

/**
 * ZKIotProdController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.iot}/${zk.iot.version}/iotProd")
public class ZKIotProdController extends ZKBaseController {

	@Autowired
	private ZKIotProdService iotProdService;
	
	// 编辑
	@RequestMapping(value="iotProd", method = RequestMethod.POST)
	public ZKMsgRes iotProdPost(@RequestBody ZKIotProd iotProd){
		this.iotProdService.save(iotProd);
        return ZKMsgRes.asOk(iotProd);
	}
	
	// 查询详情
	@RequestMapping(value="iotProd", method = RequestMethod.GET)
    public ZKMsgRes iotProdGet(@RequestParam("pkId") String pkId) {
		ZKIotProd iotProd = this.iotProdService.get(new ZKIotProd(pkId));
        return ZKMsgRes.asOk(iotProd);
	}
	
	// 分页查询 
	@RequestMapping(value="iotProdsPage", method = RequestMethod.GET)
    public ZKMsgRes iotProdsPageGet(ZKIotProd iotProd, ServerWebExchange serverWebExchange) {
        ZKPage<ZKIotProd> resPage = ZKPage.asPage(serverWebExchange.getRequest());
        resPage = this.iotProdService.findPage(resPage, iotProd);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="iotProd", method = RequestMethod.DELETE)
    public ZKMsgRes iotProdDel(@RequestParam("pkId[]") String[] pkIds) {
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.iotProdService.del(new ZKIotProd(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}