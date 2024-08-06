/**
 * 
 */
package com.zk.wechat.pay.controller;

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
import com.zk.wechat.pay.entity.ZKPayGroup;
import com.zk.wechat.pay.service.ZKPayGroupService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ZKPayGroupController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.pay}/payGroup")
public class ZKPayGroupController extends ZKBaseController {

	@Autowired
	private ZKPayGroupService payGroupService;
	
	// 编辑
	@RequestMapping(value="payGroup", method = RequestMethod.POST)
	public ZKMsgRes payGroupPost(@RequestBody ZKPayGroup payGroup){
		this.payGroupService.save(payGroup);
        return ZKMsgRes.asOk(payGroup);
	}
	
	// 查询详情
	@RequestMapping(value="payGroup", method = RequestMethod.GET)
	public ZKMsgRes payGroupGet(@RequestParam("pkId") String pkId){
		ZKPayGroup payGroup = this.payGroupService.get(new ZKPayGroup(pkId));
        return ZKMsgRes.asOk(payGroup);
	}
	
	// 分页查询 
	@RequestMapping(value="payGroupsPage", method = RequestMethod.GET)
	public ZKMsgRes payGroupsPageGet(ZKPayGroup payGroup, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKPayGroup> resPage = ZKPage.asPage(hReq);
        resPage = this.payGroupService.findPage(resPage, payGroup);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="payGroup", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes payGroupDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.payGroupService.del(new ZKPayGroup(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}