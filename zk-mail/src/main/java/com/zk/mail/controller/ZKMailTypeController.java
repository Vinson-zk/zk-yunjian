/**
 * 
 */
package com.zk.mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.mail.entity.ZKMailType;
import com.zk.mail.service.ZKMailTypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;       

/**
 * ZKMailTypeController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.mail}/${zk.mail.version}/mailType")
public class ZKMailTypeController extends ZKBaseController {

	@Autowired
	private ZKMailTypeService mailTypeService;
	
	// 编辑
	@RequestMapping(value="mailType", method = RequestMethod.POST)
	public ZKMsgRes mailTypePost(@RequestBody ZKMailType mailType){
		this.mailTypeService.save(mailType);
        return ZKMsgRes.asOk(null, mailType);
	}
	
	// 查询详情
	@RequestMapping(value="mailType", method = RequestMethod.GET)
	public ZKMsgRes mailTypeGet(@RequestParam("pkId") String pkId){
		ZKMailType mailType = this.mailTypeService.get(new ZKMailType(pkId));
        return ZKMsgRes.asOk(null, mailType);
	}
	
	// 分页查询 
	@RequestMapping(value="mailTypesPage", method = RequestMethod.GET)
	public ZKMsgRes mailTypesPageGet(ZKMailType mailType, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKMailType> resPage = ZKPage.asPage(hReq);
        resPage = this.mailTypeService.findPage(resPage, mailType);
        return ZKMsgRes.asOk(null, resPage);
	}
	
	// 批量删除
	@RequestMapping(value="mailType", method = RequestMethod.DELETE)
	public ZKMsgRes mailTypeDel(@RequestParam("pkId[]") String[] pkIds){
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.mailTypeService.del(new ZKMailType(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
	}

}