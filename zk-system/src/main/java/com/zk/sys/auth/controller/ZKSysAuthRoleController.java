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
import com.zk.sys.auth.service.ZKSysAuthRoleService;
import com.zk.sys.org.entity.ZKSysOrgRole;
import com.zk.sys.org.service.ZKSysOrgRoleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ZKSysAuthRoleController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/auth/sysAuthRole")
public class ZKSysAuthRoleController extends ZKBaseController {

	@Autowired
	private ZKSysAuthRoleService sysAuthRoleService;
	
    @Autowired
    private ZKSysOrgRoleService sysOrgRoleService;

    // 给角色分配 权限
    @RequestMapping(value = "grantAuths/{roleId}", method = RequestMethod.POST)
    public ZKMsgRes grantAuths(@PathVariable(value = "roleId") String roleId,
            @RequestBody List<ZKSysAuthDefined> allotAuths, HttpServletRequest hReq) {
        ZKSysOrgRole role = sysOrgRoleService.get(roleId);
        return ZKMsgRes.asOk(null, this.sysAuthRoleService.allotAuthToRole(role, allotAuths));
    }

//    // 查询角色拥有的权限ID
//    @RequestMapping(value = "findAuthIdsByRoleId", method = RequestMethod.GET)
//    public ZKMsgRes findAuthIdsByRoleId(@RequestParam("roleId") String roleId) {
//        return ZKMsgRes.asOk(null, this.sysAuthRoleService.findAuthIdsByRoleId(roleId));
//    }

    // 查询给指定角色授权时，公司可分配的权限以及角色已拥有的权限
    @RequestMapping(value = "findAllotAuthPage", method = RequestMethod.GET)
    public ZKMsgRes findAllotAuthPageGet(@RequestParam("roleId") String roleId,
            @RequestParam(value = "searchValue", required = false) String searchValue, HttpServletRequest hReq,
            HttpServletResponse hRes) {
        ZKPage<ZKSysAuthDefined> resPage = ZKPage.asPage(hReq);
        ZKSysOrgRole role = this.sysOrgRoleService.get(roleId);
        List<ZKSysAuthDefined> resList = this.sysAuthRoleService.findAllotAuthByRole(role, searchValue, resPage);
        resPage.setResult(resList);
        return ZKMsgRes.asOk(resPage);
    }

}