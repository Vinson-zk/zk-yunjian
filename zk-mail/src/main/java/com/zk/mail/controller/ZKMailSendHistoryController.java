/**
 * 
 */
package com.zk.mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.mail.entity.ZKMailSendHistory;
import com.zk.mail.service.ZKMailSendHistoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;       

/**
 * ZKMailSendHistoryController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.mail}/${zk.mail.version}/mailSendHistory")
public class ZKMailSendHistoryController extends ZKBaseController {

	@Autowired
	private ZKMailSendHistoryService mailSendHistoryService;
	
	// 查询详情
	@RequestMapping(value="mailSendHistory", method = RequestMethod.GET)
	public ZKMsgRes mailSendHistoryGet(@RequestParam("pkId") String pkId){
		ZKMailSendHistory mailSendHistory = this.mailSendHistoryService.get(new ZKMailSendHistory(pkId));
        return ZKMsgRes.asOk(null, mailSendHistory);
	}
	
	// 分页查询 
	@RequestMapping(value="mailSendHistorysPage", method = RequestMethod.GET)
	public ZKMsgRes mailSendHistorysPageGet(ZKMailSendHistory mailSendHistory, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKMailSendHistory> resPage = ZKPage.asPage(hReq);
        resPage = this.mailSendHistoryService.findPage(resPage, mailSendHistory);
        return ZKMsgRes.asOk(null, resPage);
	}
	
}