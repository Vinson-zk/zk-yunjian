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
import com.zk.wechat.pay.entity.ZKPayGetBusinessType;
import com.zk.wechat.pay.service.ZKPayGetBusinessTypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ZKPayGetBusinessTypeController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.pay}/payGetBusinessType")
public class ZKPayGetBusinessTypeController extends ZKBaseController {

	@Autowired
	private ZKPayGetBusinessTypeService payGetBusinessTypeService;
	
	// 编辑
	@RequestMapping(value="payGetBusinessType", method = RequestMethod.POST)
	public ZKMsgRes payGetBusinessTypePost(@RequestBody ZKPayGetBusinessType payGetBusinessType){
		this.payGetBusinessTypeService.save(payGetBusinessType);
        return ZKMsgRes.asOk(payGetBusinessType);
	}
	
	// 查询详情
	@RequestMapping(value="payGetBusinessType", method = RequestMethod.GET)
	public ZKMsgRes payGetBusinessTypeGet(@RequestParam("pkId") String pkId){
		ZKPayGetBusinessType payGetBusinessType = this.payGetBusinessTypeService.get(new ZKPayGetBusinessType(pkId));
        return ZKMsgRes.asOk(payGetBusinessType);
	}
	
	// 分页查询 
	@RequestMapping(value="payGetBusinessTypesPage", method = RequestMethod.GET)
	public ZKMsgRes payGetBusinessTypesPageGet(ZKPayGetBusinessType payGetBusinessType, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKPayGetBusinessType> resPage = ZKPage.asPage(hReq);
        resPage = this.payGetBusinessTypeService.findPage(resPage, payGetBusinessType);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="payGetBusinessType", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes payGetBusinessTypeDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.payGetBusinessTypeService.del(new ZKPayGetBusinessType(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}