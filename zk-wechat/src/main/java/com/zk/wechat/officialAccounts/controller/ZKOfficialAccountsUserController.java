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
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccountsUser;
import com.zk.wechat.officialAccounts.service.ZKOfficialAccountsUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;       

/**
 * ZKOfficialAccountsUserController
 * 
 * @author
 * @version
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.officialAccounts}/officialAccountsUser")
public class ZKOfficialAccountsUserController extends ZKBaseController {

	@Autowired
    private ZKOfficialAccountsUserService officialAccountsUserService;
	
//	// 编辑
//	@RequestMapping(value="officialAccountsUser", method = RequestMethod.POST)
//	public ZKMsgRes officialAccountsUserPost(@RequestBody ZKOfficialAccountsUser officialAccountsUser){
//		this.officialAccountsUserService.save(officialAccountsUser);
//        return ZKMsgRes.as("zk.0", null, officialAccountsUser);
//	}
	
	// 查询详情
    @RequestMapping(value = "officialAccountsUser", method = RequestMethod.GET)
    public ZKMsgRes officialAccountsUserGet(@RequestParam("pkId") String pkId) {
        ZKOfficialAccountsUser officialAccountsUser = this.officialAccountsUserService
                .get(new ZKOfficialAccountsUser(pkId));
        return ZKMsgRes.asOk(officialAccountsUser);
	}
	
	// 分页查询 
    @RequestMapping(value = "officialAccountsUsersPage", method = RequestMethod.GET)
    public ZKMsgRes officialAccountsUsersPageGet(ZKOfficialAccountsUser officialAccountsUser, HttpServletRequest hReq,
            HttpServletResponse hRes) {
		ZKPage<ZKOfficialAccountsUser> resPage = ZKPage.asPage(hReq);
        resPage = this.officialAccountsUserService.findPage(resPage, officialAccountsUser);
        return ZKMsgRes.asOk(resPage);
	}
	
//	// 批量删除
//	@RequestMapping(value="officialAccountsUser", method = RequestMethod.DELETE)
//	@ResponseBody
//	public ZKMsgRes officialAccountsUserDel(@RequestParam("pkId[]") String[] pkIds){
//		 int count = 0;
//        if (pkIds != null && pkIds.length > 0) {
//            for (String pkId : pkIds) {
//                count += this.officialAccountsUserService.del(new ZKOfficialAccountsUser(pkId));
//            }
//        }
//        return ZKMsgRes.as("zk.0", null, count);
//	}

}