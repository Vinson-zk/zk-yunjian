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
import com.zk.mail.entity.ZKMailTemplate;
import com.zk.mail.service.ZKMailTemplateService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;       

/**
 * ZKMailTemplateController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.mail}/${zk.mail.version}/mailTemplate")
public class ZKMailTemplateController extends ZKBaseController {

	@Autowired
	private ZKMailTemplateService mailTemplateService;
	
	// 编辑
	@RequestMapping(value="mailTemplate", method = RequestMethod.POST)
	public ZKMsgRes mailTemplatePost(@RequestBody ZKMailTemplate mailTemplate){
		this.mailTemplateService.save(mailTemplate);
        return ZKMsgRes.asOk(null, mailTemplate);
	}
	
	// 查询详情
	@RequestMapping(value="mailTemplate", method = RequestMethod.GET)
	public ZKMsgRes mailTemplateGet(@RequestParam("pkId") String pkId){
		ZKMailTemplate mailTemplate = this.mailTemplateService.get(new ZKMailTemplate(pkId));
        return ZKMsgRes.asOk(null, mailTemplate);
	}
	
	// 分页查询 
	@RequestMapping(value="mailTemplatesPage", method = RequestMethod.GET)
	public ZKMsgRes mailTemplatesPageGet(ZKMailTemplate mailTemplate, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKMailTemplate> resPage = ZKPage.asPage(hReq);
        resPage = this.mailTemplateService.findPage(resPage, mailTemplate);
        return ZKMsgRes.asOk(null, resPage);
	}
	
	// 批量删除
	@RequestMapping(value="mailTemplate", method = RequestMethod.DELETE)
	public ZKMsgRes mailTemplateDel(@RequestParam("pkId[]") String[] pkIds){
		int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.mailTemplateService.del(new ZKMailTemplate(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
	}

}