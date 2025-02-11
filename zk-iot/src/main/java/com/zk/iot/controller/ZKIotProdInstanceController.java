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
import com.zk.iot.entity.ZKIotProdInstance;
import com.zk.iot.service.ZKIotProdInstanceService;

/**
 * ZKIotProdInstanceController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.iot}/${zk.iot.version}/iotProdInstance")
public class ZKIotProdInstanceController extends ZKBaseController {

	@Autowired
	private ZKIotProdInstanceService iotProdInstanceService;
	
	// 编辑
	@RequestMapping(value="iotProdInstance", method = RequestMethod.POST)
	public ZKMsgRes iotProdInstancePost(@RequestBody ZKIotProdInstance iotProdInstance){
		this.iotProdInstanceService.save(iotProdInstance);
        return ZKMsgRes.asOk(iotProdInstance);
	}
	
	// 查询详情
	@RequestMapping(value="iotProdInstance", method = RequestMethod.GET)
    public ZKMsgRes iotProdInstanceGet(@RequestParam("pkId") String pkId) {
        ZKIotProdInstance iotProdInstance = this.iotProdInstanceService.getDetailById(pkId);
        return ZKMsgRes.asOk(iotProdInstance);
	}
	
	// 分页查询 
	@RequestMapping(value="iotProdInstancesPage", method = RequestMethod.GET)
    public ZKMsgRes iotProdInstancesPageGet(ZKIotProdInstance iotProdInstance, ServerWebExchange serverWebExchange) {
        ZKPage<ZKIotProdInstance> resPage = ZKPage.asPage(serverWebExchange.getRequest());
        resPage = this.iotProdInstanceService.findPage(resPage, iotProdInstance);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="iotProdInstance", method = RequestMethod.DELETE)
    public ZKMsgRes iotProdInstanceDel(@RequestParam("pkId[]") String[] pkIds) {
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.iotProdInstanceService.del(new ZKIotProdInstance(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}