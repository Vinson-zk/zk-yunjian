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
import com.zk.sys.res.entity.ZKSysResApplicationSystem;
import com.zk.sys.res.service.ZKSysResApplicationSystemService;       

/**
 * ZKSysResApplicationSystemController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.res}/sysResApplicationSystem")
public class ZKSysResApplicationSystemController extends ZKBaseController {

	@Autowired
	private ZKSysResApplicationSystemService sysResApplicationSystemService;
	
	// 编辑
	@RequestMapping(value="sysResApplicationSystem", method = RequestMethod.POST)
	public ZKMsgRes sysResApplicationSystemPost(@RequestBody ZKSysResApplicationSystem sysResApplicationSystem){
		this.sysResApplicationSystemService.save(sysResApplicationSystem);
        return ZKMsgRes.asOk(null, sysResApplicationSystem);
	}
	
	// 查询详情
	@RequestMapping(value="sysResApplicationSystem", method = RequestMethod.GET)
	public ZKMsgRes sysResApplicationSystemGet(@RequestParam("pkId") String pkId){
		ZKSysResApplicationSystem sysResApplicationSystem = this.sysResApplicationSystemService.get(new ZKSysResApplicationSystem(pkId));
        return ZKMsgRes.asOk(null, sysResApplicationSystem);
	}
	
	// 分页查询 
	@RequestMapping(value="sysResApplicationSystemsPage", method = RequestMethod.GET)
	public ZKMsgRes sysResApplicationSystemsPageGet(ZKSysResApplicationSystem sysResApplicationSystem, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKSysResApplicationSystem> resPage = ZKPage.asPage(hReq);
        resPage = this.sysResApplicationSystemService.findPage(resPage, sysResApplicationSystem);
        return ZKMsgRes.asOk(null, resPage);
	}
	
	// 批量删除
	@RequestMapping(value="sysResApplicationSystem", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes sysResApplicationSystemDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysResApplicationSystemService.del(new ZKSysResApplicationSystem(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
	}

}