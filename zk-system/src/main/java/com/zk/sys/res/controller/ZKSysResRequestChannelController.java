/**
 * 
 */
package com.zk.sys.res.controller;

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
import com.zk.sys.res.entity.ZKSysResRequestChannel;
import com.zk.sys.res.service.ZKSysResRequestChannelService;       

/**
 * ZKSysResRequestChannelController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.res}/sysResRequestChannel")
public class ZKSysResRequestChannelController extends ZKBaseController {

	@Autowired
	private ZKSysResRequestChannelService sysResRequestChannelService;
	
	// 编辑
	@RequestMapping(value="sysResRequestChannel", method = RequestMethod.POST)
	public ZKMsgRes sysResRequestChannelPost(@RequestBody ZKSysResRequestChannel sysResRequestChannel){
		this.sysResRequestChannelService.save(sysResRequestChannel);
        return ZKMsgRes.asOk(null, sysResRequestChannel);
	}
	
	// 查询详情
	@RequestMapping(value="sysResRequestChannel", method = RequestMethod.GET)
	public ZKMsgRes sysResRequestChannelGet(@RequestParam("pkId") String pkId){
		ZKSysResRequestChannel sysResRequestChannel = this.sysResRequestChannelService.get(new ZKSysResRequestChannel(pkId));
        return ZKMsgRes.asOk(null, sysResRequestChannel);
	}
	
	// 分页查询 
	@RequestMapping(value="sysResRequestChannelsPage", method = RequestMethod.GET)
	public ZKMsgRes sysResRequestChannelsPageGet(ZKSysResRequestChannel sysResRequestChannel, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKSysResRequestChannel> resPage = ZKPage.asPage(hReq);
        resPage = this.sysResRequestChannelService.findPage(resPage, sysResRequestChannel);
        return ZKMsgRes.asOk(null, resPage);
	}
	
	// 批量删除
	@RequestMapping(value="sysResRequestChannel", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes sysResRequestChannelDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysResRequestChannelService.del(new ZKSysResRequestChannel(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
	}

}