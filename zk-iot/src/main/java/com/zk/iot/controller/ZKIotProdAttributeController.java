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
import com.zk.iot.entity.ZKIotProdAttribute;
import com.zk.iot.service.ZKIotProdAttributeService;

/**
 * ZKIotProdAttributeController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.iot}/${zk.iot.version}/iotProdAttribute")
public class ZKIotProdAttributeController extends ZKBaseController {

	@Autowired
	private ZKIotProdAttributeService iotProdAttributeService;
	
	// 编辑
	@RequestMapping(value="iotProdAttribute", method = RequestMethod.POST)
	public ZKMsgRes iotProdAttributePost(@RequestBody ZKIotProdAttribute iotProdAttribute){
		this.iotProdAttributeService.save(iotProdAttribute);
        return ZKMsgRes.asOk(iotProdAttribute);
	}
	
	// 查询详情
	@RequestMapping(value="iotProdAttribute", method = RequestMethod.GET)
    public ZKMsgRes iotProdAttributeGet(@RequestParam("pkId") String pkId) {
		ZKIotProdAttribute iotProdAttribute = this.iotProdAttributeService.get(new ZKIotProdAttribute(pkId));
        return ZKMsgRes.asOk(iotProdAttribute);
	}
	
	// 分页查询 
	@RequestMapping(value="iotProdAttributesPage", method = RequestMethod.GET)
    public ZKMsgRes iotProdAttributesPageGet(ZKIotProdAttribute iotProdAttribute, ServerWebExchange serverWebExchange) {
        ZKPage<ZKIotProdAttribute> resPage = ZKPage.asPage(serverWebExchange.getRequest());
        resPage = this.iotProdAttributeService.findPage(resPage, iotProdAttribute);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="iotProdAttribute", method = RequestMethod.DELETE)
    public ZKMsgRes iotProdAttributeDel(@RequestParam("pkId[]") String[] pkIds) {
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.iotProdAttributeService.del(new ZKIotProdAttribute(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}