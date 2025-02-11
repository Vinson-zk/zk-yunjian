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
import com.zk.iot.entity.ZKIotProdBusinessDataProtocol;
import com.zk.iot.service.ZKIotProdBusinessDataProtocolService;

/**
 * ZKIotProdBusinessDataProtocolController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.iot}/${zk.iot.version}/iotProdBusinessDataProtocol")
public class ZKIotProdBusinessDataProtocolController extends ZKBaseController {

	@Autowired
	private ZKIotProdBusinessDataProtocolService iotProdBusinessDataProtocolService;
	
	// 编辑
	@RequestMapping(value="iotProdBusinessDataProtocol", method = RequestMethod.POST)
	public ZKMsgRes iotProdBusinessDataProtocolPost(@RequestBody ZKIotProdBusinessDataProtocol iotProdBusinessDataProtocol){
		this.iotProdBusinessDataProtocolService.save(iotProdBusinessDataProtocol);
        return ZKMsgRes.asOk(iotProdBusinessDataProtocol);
	}
	
	// 查询详情
	@RequestMapping(value="iotProdBusinessDataProtocol", method = RequestMethod.GET)
    public ZKMsgRes iotProdBusinessDataProtocolGet(@RequestParam("pkId") String pkId) {
		ZKIotProdBusinessDataProtocol iotProdBusinessDataProtocol = this.iotProdBusinessDataProtocolService.get(new ZKIotProdBusinessDataProtocol(pkId));
        return ZKMsgRes.asOk(iotProdBusinessDataProtocol);
	}
	
	// 分页查询 
	@RequestMapping(value="iotProdBusinessDataProtocolsPage", method = RequestMethod.GET)
    public ZKMsgRes iotProdBusinessDataProtocolsPageGet(ZKIotProdBusinessDataProtocol iotProdBusinessDataProtocol,
            ServerWebExchange serverWebExchange) {
        ZKPage<ZKIotProdBusinessDataProtocol> resPage = ZKPage.asPage(serverWebExchange.getRequest());
        resPage = this.iotProdBusinessDataProtocolService.findPage(resPage, iotProdBusinessDataProtocol);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="iotProdBusinessDataProtocol", method = RequestMethod.DELETE)
    public ZKMsgRes iotProdBusinessDataProtocolDel(@RequestParam("pkId[]") String[] pkIds) {
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.iotProdBusinessDataProtocolService.del(new ZKIotProdBusinessDataProtocol(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}