/**
 * 
 */
package com.zk.wechat.officialAccounts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccountsUserGps;
import com.zk.wechat.officialAccounts.service.ZKOfficialAccountsUserGpsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;       

/**
 * ZKOfficialAccountsUserGpsController
 * 
 * @author
 * @version
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.officialAccounts}/officialAccountsUserGps")
public class ZKOfficialAccountsUserGpsController extends ZKBaseController {

	@Autowired
    private ZKOfficialAccountsUserGpsService officialAccountsUserGpsService;
	
//	// 编辑
//	@RequestMapping(value="0fficialAccountsUserGps", method = RequestMethod.POST)
//	public ZKMsgRes 0fficialAccountsUserGpsPost(@RequestBody ZKOfficialAccountsUserGps officialAccountsUserGps){
//		this.officialAccountsUserGpsService.save(officialAccountsUserGps);
//        return ZKMsgRes.as("zk.0", null, officialAccountsUserGps);
//	}
	
	// 查询详情
    @RequestMapping(value = "officialAccountsUserGps", method = RequestMethod.GET)
    public ZKMsgRes officialAccountsUserGpsGet(@RequestParam("pkId") String pkId) {
        ZKOfficialAccountsUserGps officialAccountsUserGps = this.officialAccountsUserGpsService
                .get(new ZKOfficialAccountsUserGps(pkId));
        return ZKMsgRes.asOk(officialAccountsUserGps);
	}
	
	// 分页查询 
    @RequestMapping(value = "officialAccountsUserGpssPage", method = RequestMethod.GET)
    public ZKMsgRes officialAccountsUserGpssPageGet(ZKOfficialAccountsUserGps officialAccountsUserGps,
            HttpServletRequest hReq, HttpServletResponse hRes) {
		ZKPage<ZKOfficialAccountsUserGps> resPage = ZKPage.asPage(hReq);
        resPage = this.officialAccountsUserGpsService.findPage(resPage, officialAccountsUserGps);
        return ZKMsgRes.asOk(resPage);
	}
	
//	// 批量删除
//	@RequestMapping(value="officialAccountsUserGps", method = RequestMethod.DELETE)
//	@ResponseBody
//	public ZKMsgRes officialAccountsUserGpsDel(@RequestParam("pkId[]") String[] pkIds){
//		 int count = 0;
//        if (pkIds != null && pkIds.length > 0) {
//            for (String pkId : pkIds) {
//                count += this.officialAccountsUserGpsService.del(new ZKOfficialAccountsUserGps(pkId));
//            }
//        }
//        return ZKMsgRes.as("zk.0", null, count);
//	}

}