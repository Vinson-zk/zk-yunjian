/**
 * 
 */
package com.zk.sys.auth.controller;

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
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.service.ZKSysAuthDefinedService;       

/**
 * ZKSysAuthDefinedController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/auth/sysAuthDefined")
public class ZKSysAuthDefinedController extends ZKBaseController {

	@Autowired
	private ZKSysAuthDefinedService sysAuthDefinedService;
	
	// 编辑
	@RequestMapping(value="sysAuthDefined", method = RequestMethod.POST)
	public ZKMsgRes sysAuthDefinedPost(@RequestBody ZKSysAuthDefined sysAuthDefined){
		this.sysAuthDefinedService.save(sysAuthDefined);
        return ZKMsgRes.asOk(null, sysAuthDefined);
	}
	
	// 查询详情
	@RequestMapping(value="sysAuthDefined", method = RequestMethod.GET)
	public ZKMsgRes sysAuthDefinedGet(@RequestParam("pkId") String pkId){
		ZKSysAuthDefined sysAuthDefined = this.sysAuthDefinedService.get(new ZKSysAuthDefined(pkId));
        return ZKMsgRes.asOk(null, sysAuthDefined);
	}
	
	// 分页查询 
	@RequestMapping(value="sysAuthDefinedsPage", method = RequestMethod.GET)
	public ZKMsgRes sysAuthDefinedsPageGet(ZKSysAuthDefined sysAuthDefined, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKSysAuthDefined> resPage = ZKPage.asPage(hReq);
        resPage = this.sysAuthDefinedService.findPage(resPage, sysAuthDefined);
        return ZKMsgRes.asOk(null, resPage);
	}
	
	// 批量删除
	@RequestMapping(value="sysAuthDefined", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes sysAuthDefinedDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysAuthDefinedService.del(new ZKSysAuthDefined(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
	}

}
