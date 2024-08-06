/**
 * 
 */
package com.zk.sys.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.service.ZKSysAuthUserService;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.service.ZKSysOrgUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ZKSysAuthUserController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/auth/sysAuthUser")
public class ZKSysAuthUserController extends ZKBaseController {

	@Autowired
	private ZKSysAuthUserService sysAuthUserService;
	
    @Autowired
    private ZKSysOrgUserService sysOrgUserService;

    // 给用户分配 权限
    @RequestMapping(value = "grantAuths/{userId}", method = RequestMethod.POST)
    public ZKMsgRes grantAuths(@PathVariable(value = "userId") String userId,
            @RequestBody List<ZKSysAuthDefined> allotAuths, HttpServletRequest hReq) {
        ZKSysOrgUser user = sysOrgUserService.get(userId);
        return ZKMsgRes.asOk(null, this.sysAuthUserService.allotAuthToUser(user, allotAuths));
    }

//    // 查询用户拥有的权限ID
//    @RequestMapping(value = "findAuthIdsByUserId", method = RequestMethod.GET)
//    public ZKMsgRes findAuthIdsByUserId(@RequestParam("userId") String userId) {
//        return ZKMsgRes.asOk(null, this.sysAuthUserService.findAuthIdsByUserId(userId));
//    }

    // 查询给指定用户授权时，公司可分配的权限以及用户已拥有的权限
    @RequestMapping(value = "findAllotAuthPage", method = RequestMethod.GET)
    public ZKMsgRes findAllotAuthPageGet(@RequestParam("userId") String userId,
            @RequestParam(value = "searchValue", required = false) String searchValue, HttpServletRequest hReq,
            HttpServletResponse hRes) {
        ZKPage<ZKSysAuthDefined> resPage = ZKPage.asPage(hReq);
        ZKSysOrgUser user = this.sysOrgUserService.get(userId);
        List<ZKSysAuthDefined> resList = this.sysAuthUserService.findAllotAuthByUser(user, searchValue, resPage);
        resPage.setResult(resList);
        return ZKMsgRes.asOk(resPage);
    }

}