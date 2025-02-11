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
import com.zk.iot.entity.ZKIotProdModel;
import com.zk.iot.service.ZKIotProdModelService;

/**
 * ZKIotProdModelController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.iot}/${zk.iot.version}/iotProdModel")
public class ZKIotProdModelController extends ZKBaseController {

	@Autowired
	private ZKIotProdModelService iotProdModelService;
	
	// 编辑
	@RequestMapping(value="iotProdModel", method = RequestMethod.POST)
	public ZKMsgRes iotProdModelPost(@RequestBody ZKIotProdModel iotProdModel){
		this.iotProdModelService.save(iotProdModel);
        return ZKMsgRes.asOk(iotProdModel);
	}
	
	// 查询详情
	@RequestMapping(value="iotProdModel", method = RequestMethod.GET)
    public ZKMsgRes iotProdModelGet(@RequestParam("pkId") String pkId) {
		ZKIotProdModel iotProdModel = this.iotProdModelService.get(new ZKIotProdModel(pkId));
        return ZKMsgRes.asOk(iotProdModel);
	}
	
	// 分页查询 
	@RequestMapping(value="iotProdModelsPage", method = RequestMethod.GET)
    public ZKMsgRes iotProdModelsPageGet(ZKIotProdModel iotProdModel, ServerWebExchange serverWebExchange) {
        ZKPage<ZKIotProdModel> resPage = ZKPage.asPage(serverWebExchange.getRequest());
        resPage = this.iotProdModelService.findPage(resPage, iotProdModel);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="iotProdModel", method = RequestMethod.DELETE)
    public ZKMsgRes iotProdModelDel(@RequestParam("pkId[]") String[] pkIds) {
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.iotProdModelService.del(new ZKIotProdModel(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}