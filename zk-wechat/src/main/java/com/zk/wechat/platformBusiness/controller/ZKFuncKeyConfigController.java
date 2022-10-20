/**
 * 
 */
package com.zk.wechat.platformBusiness.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.web.ZKMsgRes;
import com.zk.wechat.platformBusiness.entity.ZKFuncKeyConfig;
import com.zk.wechat.platformBusiness.service.ZKFuncKeyConfigService;       

/**
 * ZKFuncKeyConfigController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.platformBusiness}/funcKeyConfig")
public class ZKFuncKeyConfigController extends ZKBaseController {

	@Autowired
	private ZKFuncKeyConfigService funcKeyConfigService;
	
	// 编辑
    @RequestMapping(value = "funcKeyConfig", method = RequestMethod.POST)
	public ZKMsgRes funcKeyConfigPost(@RequestBody ZKFuncKeyConfig funcKeyConfig){
		this.funcKeyConfigService.save(funcKeyConfig);
        return ZKMsgRes.asOk(funcKeyConfig);
	}
	
	// 查询详情
    @RequestMapping(value = "funcKeyConfig", method = RequestMethod.GET)
	public ZKMsgRes funcKeyConfigGet(@RequestParam("pkId") String pkId){
		ZKFuncKeyConfig funcKeyConfig = this.funcKeyConfigService.get(new ZKFuncKeyConfig(pkId));
        return ZKMsgRes.asOk(funcKeyConfig);
	}
	
	// 分页查询 
    @RequestMapping(value = "funcKeyConfigsPage", method = RequestMethod.GET)
	public ZKMsgRes funcKeyConfigsPageGet(ZKFuncKeyConfig funcKeyConfig, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKFuncKeyConfig> resPage = ZKPage.asPage(hReq);
        resPage = this.funcKeyConfigService.findPage(resPage, funcKeyConfig);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
    @RequestMapping(value = "funcKeyConfig", method = RequestMethod.DELETE)
	public ZKMsgRes funcKeyConfigDel(@RequestParam("pkId[]") String[] pkIds){
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.funcKeyConfigService.del(new ZKFuncKeyConfig(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}