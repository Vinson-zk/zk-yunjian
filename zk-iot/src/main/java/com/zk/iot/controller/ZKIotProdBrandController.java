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
import com.zk.iot.entity.ZKIotProdBrand;
import com.zk.iot.service.ZKIotProdBrandService;

/**
 * ZKIotProdBrandController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.iot}/${zk.iot.version}/iotProdBrand")
public class ZKIotProdBrandController extends ZKBaseController {

	@Autowired
	private ZKIotProdBrandService iotProdBrandService;
	
	// 编辑
	@RequestMapping(value="iotProdBrand", method = RequestMethod.POST)
	public ZKMsgRes iotProdBrandPost(@RequestBody ZKIotProdBrand iotProdBrand){
		this.iotProdBrandService.save(iotProdBrand);
        return ZKMsgRes.asOk(iotProdBrand);
	}
	
	// 查询详情
	@RequestMapping(value="iotProdBrand", method = RequestMethod.GET)
    public ZKMsgRes iotProdBrandGet(@RequestParam("pkId") String pkId) {
		ZKIotProdBrand iotProdBrand = this.iotProdBrandService.get(new ZKIotProdBrand(pkId));
        return ZKMsgRes.asOk(iotProdBrand);
	}
	
	// 分页查询 
	@RequestMapping(value="iotProdBrandsPage", method = RequestMethod.GET)
    public ZKMsgRes iotProdBrandsPageGet(ZKIotProdBrand iotProdBrand, ServerWebExchange serverWebExchange) {
        ZKPage<ZKIotProdBrand> resPage = ZKPage.asPage(serverWebExchange.getRequest());
        resPage = this.iotProdBrandService.findPage(resPage, iotProdBrand);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="iotProdBrand", method = RequestMethod.DELETE)
    public ZKMsgRes iotProdBrandDel(@RequestParam("pkId[]") String[] pkIds) {
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.iotProdBrandService.del(new ZKIotProdBrand(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}