/**
 * 
 */
package com.zk.sys.settings.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.sys.settings.entity.ZKSysSetItem;
import com.zk.sys.settings.service.ZKSysSetItemService;       

/**
 * ZKSysSetItemController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.set}/sysSetItem")
public class ZKSysSetItemController extends ZKBaseController {

	@Autowired
	private ZKSysSetItemService sysSetItemService;
	
	// 编辑
	@RequestMapping(value="sysSetItem", method = RequestMethod.POST)
	public ZKMsgRes sysSetItemPost(@RequestBody ZKSysSetItem sysSetItem){
		this.sysSetItemService.save(sysSetItem);
        return ZKMsgRes.asOk(null, sysSetItem);
	}
	
	// 查询详情
	@RequestMapping(value="sysSetItem", method = RequestMethod.GET)
	public ZKMsgRes sysSetItemGet(@RequestParam("pkId") String pkId){
		ZKSysSetItem sysSetItem = this.sysSetItemService.get(new ZKSysSetItem(pkId));
        return ZKMsgRes.asOk(null, sysSetItem);
	}
	
	// 分页查询 
	@RequestMapping(value="sysSetItemsPage", method = RequestMethod.GET)
	public ZKMsgRes sysSetItemsPageGet(ZKSysSetItem sysSetItem, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKSysSetItem> resPage = ZKPage.asPage(hReq);
        resPage = this.sysSetItemService.findPage(resPage, sysSetItem);
        return ZKMsgRes.asOk(null, resPage);
	}
	
	// 批量删除
	@RequestMapping(value="sysSetItem", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes sysSetItemDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysSetItemService.del(new ZKSysSetItem(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
	}

}