/**
 * 
 */
package com.zk.wechat.thirdParty.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.web.ZKMsgRes;
import com.zk.wechat.thirdParty.entity.ZKThirdPartyAuthAccountUserGps;
import com.zk.wechat.thirdParty.service.ZKThirdPartyAuthAccountUserGpsService;       

/**
 * ZKThirdPartyAuthAccountUserGpsController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.thirdParty}/thirdPartyAuthAccountUserGps")
public class ZKThirdPartyAuthAccountUserGpsController extends ZKBaseController {

	@Autowired
	private ZKThirdPartyAuthAccountUserGpsService thirdPartyAuthAccountUserGpsService;
	
//	// 编辑
//	@RequestMapping(value="thirdPartyAuthAccountUserGps", method = RequestMethod.POST)
//	public ZKMsgRes thirdPartyAuthAccountUserGpsPost(@RequestBody ZKThirdPartyAuthAccountUserGps thirdPartyAuthAccountUserGps){
//		this.thirdPartyAuthAccountUserGpsService.save(thirdPartyAuthAccountUserGps);
//        return ZKMsgRes.as("zk.0", null, thirdPartyAuthAccountUserGps);
//	}
	
	// 查询详情
	@RequestMapping(value="thirdPartyAuthAccountUserGps", method = RequestMethod.GET)
	public ZKMsgRes thirdPartyAuthAccountUserGpsGet(@RequestParam("pkId") String pkId){
		ZKThirdPartyAuthAccountUserGps thirdPartyAuthAccountUserGps = this.thirdPartyAuthAccountUserGpsService.get(new ZKThirdPartyAuthAccountUserGps(pkId));
        return ZKMsgRes.as("zk.0", null, thirdPartyAuthAccountUserGps);
	}
	
	// 分页查询 
	@RequestMapping(value="thirdPartyAuthAccountUserGpssPage", method = RequestMethod.GET)
	public ZKMsgRes thirdPartyAuthAccountUserGpssPageGet(ZKThirdPartyAuthAccountUserGps thirdPartyAuthAccountUserGps, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKThirdPartyAuthAccountUserGps> resPage = ZKPage.asPage(hReq);
        resPage = this.thirdPartyAuthAccountUserGpsService.findPage(resPage, thirdPartyAuthAccountUserGps);
        return ZKMsgRes.as("zk.0", null, resPage);
	}
	
//	// 批量删除
//	@RequestMapping(value="thirdPartyAuthAccountUserGps", method = RequestMethod.DELETE)
//	@ResponseBody
//	public ZKMsgRes thirdPartyAuthAccountUserGpsDel(@RequestParam("pkId[]") String[] pkIds){
//		 int count = 0;
//        if (pkIds != null && pkIds.length > 0) {
//            for (String pkId : pkIds) {
//                count += this.thirdPartyAuthAccountUserGpsService.del(new ZKThirdPartyAuthAccountUserGps(pkId));
//            }
//        }
//        return ZKMsgRes.as("zk.0", null, count);
//	}

}