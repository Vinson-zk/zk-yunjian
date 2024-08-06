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
import com.zk.sys.res.entity.ZKSysResFuncApi;
import com.zk.sys.res.service.ZKSysResFuncApiService;       

/**
 * ZKSysResFuncApiController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.res}/sysResFuncApi")
public class ZKSysResFuncApiController extends ZKBaseController {

	@Autowired
	private ZKSysResFuncApiService sysResFuncApiService;
	
	// 编辑
	@RequestMapping(value="sysResFuncApi", method = RequestMethod.POST)
	public ZKMsgRes sysResFuncApiPost(@RequestBody ZKSysResFuncApi sysResFuncApi){
		this.sysResFuncApiService.save(sysResFuncApi);
        return ZKMsgRes.asOk(null, sysResFuncApi);
	}
	
	// 查询详情
	@RequestMapping(value="sysResFuncApi", method = RequestMethod.GET)
	public ZKMsgRes sysResFuncApiGet(@RequestParam("pkId") String pkId){
		ZKSysResFuncApi sysResFuncApi = this.sysResFuncApiService.get(new ZKSysResFuncApi(pkId));
        return ZKMsgRes.asOk(null, sysResFuncApi);
	}
	
	// 分页查询 
	@RequestMapping(value="sysResFuncApisPage", method = RequestMethod.GET)
	public ZKMsgRes sysResFuncApisPageGet(ZKSysResFuncApi sysResFuncApi, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKSysResFuncApi> resPage = ZKPage.asPage(hReq);
        resPage = this.sysResFuncApiService.findPage(resPage, sysResFuncApi);
        return ZKMsgRes.asOk(null, resPage);
	}
	
	// 批量删除
	@RequestMapping(value="sysResFuncApi", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes sysResFuncApiDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysResFuncApiService.del(new ZKSysResFuncApi(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
	}

}