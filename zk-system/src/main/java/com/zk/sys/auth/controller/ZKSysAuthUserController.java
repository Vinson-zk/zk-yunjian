/**
 * 
 */
package com.zk.sys.auth.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.web.ZKMsgRes;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.service.ZKSysAuthDefinedService;
import com.zk.sys.auth.service.ZKSysAuthUserService;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.service.ZKSysOrgUserService;

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

    @Autowired
    private ZKSysAuthDefinedService sysAuthDefinedService;

    // 给用户分配 权限
    @RequestMapping(value = "setRelationByUser/{userId}", method = RequestMethod.POST)
    public ZKMsgRes setRelationByUser(@PathVariable(value = "userId") String userId,
            @RequestBody List<ZKSysAuthDefined> addAuths, HttpServletRequest hReq) {
        ZKSysOrgUser user = sysOrgUserService.get(userId);
        return ZKMsgRes.asOk(this.sysAuthUserService.addRelationByUser(user, addAuths, null));
    }

    // 查询用户拥有的权限ID
    @RequestMapping(value = "findAuthIdsByUserId", method = RequestMethod.GET)
    public ZKMsgRes findAuthIdsByUserId(@RequestParam("userId") String userId) {
        return ZKMsgRes.asOk(this.sysAuthUserService.findAuthIdsByUserId(userId));
    }

    // 分页查询权限
    @RequestMapping(value = "sysAuthDefinedsPage", method = RequestMethod.GET)
    public ZKMsgRes sysAuthDefinedsPageGet(ZKSysAuthDefined sysAuthDefined, HttpServletRequest hReq,
            HttpServletResponse hRes) {
        ZKPage<ZKSysAuthDefined> resPage = ZKPage.asPage(hReq);
        resPage = this.sysAuthDefinedService.findPage(resPage, sysAuthDefined);
        return ZKMsgRes.asOk(resPage);
    }

}