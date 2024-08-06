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
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccounts;
import com.zk.wechat.officialAccounts.service.ZKOfficialAccountsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;       

/**
 * ZKOfficialAccountsController
 * 
 * @author
 * @version
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.officialAccounts}/accounts")
public class ZKOfficialAccountsController extends ZKBaseController {

	@Autowired
    private ZKOfficialAccountsService officialAccountsService;
	
//	// 编辑
//	@RequestMapping(value="accounts", method = RequestMethod.POST)
//	public ZKMsgRes accountsPost(@RequestBody ZKOfficialAccounts officialAccounts){
//		this.officialAccountsService.save(officialAccounts);
//        return ZKMsgRes.as("zk.0", null, officialAccounts);
//	}
	
	// 查询详情
    @RequestMapping(value = "accounts", method = RequestMethod.GET)
    public ZKMsgRes accountsGet(@RequestParam("pkId") String pkId) {
        ZKOfficialAccounts officialAccounts = this.officialAccountsService.get(new ZKOfficialAccounts(pkId));
        return ZKMsgRes.asOk(officialAccounts);
	}
	
	// 分页查询 
    @RequestMapping(value = "accountsPage", method = RequestMethod.GET)
    public ZKMsgRes accountsPageGet(ZKOfficialAccounts officialAccounts, HttpServletRequest hReq,
            HttpServletResponse hRes) {
		ZKPage<ZKOfficialAccounts> resPage = ZKPage.asPage(hReq);
        resPage = this.officialAccountsService.findPage(resPage, officialAccounts);
        return ZKMsgRes.asOk(resPage);
	}
	
//	// 批量删除
//	@RequestMapping(value="accounts", method = RequestMethod.DELETE)
//	@ResponseBody
//	public ZKMsgRes accountsDel(@RequestParam("pkId[]") String[] pkIds){
//		 int count = 0;
//        if (pkIds != null && pkIds.length > 0) {
//            for (String pkId : pkIds) {
//                count += this.officialAccountsService.del(new ZKOfficialAccounts(pkId));
//            }
//        }
//        return ZKMsgRes.as("zk.0", null, count);
//	}

}