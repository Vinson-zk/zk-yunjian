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
import com.zk.sys.auth.service.ZKSysAuthRoleService;
import com.zk.sys.org.entity.ZKSysOrgRole;
import com.zk.sys.org.service.ZKSysOrgRoleService;

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

    @Autowired
    private ZKSysAuthDefinedService sysAuthDefinedService;

    // 给角色分配 权限
    @RequestMapping(value = "setRelationByRole/{roleId}", method = RequestMethod.POST)
    public ZKMsgRes setRelationByUser(@PathVariable(value = "roleId") String roleId,
            @RequestBody List<ZKSysAuthDefined> addAuths, HttpServletRequest hReq) {
        ZKSysOrgRole role = sysOrgRoleService.get(roleId);
        return ZKMsgRes.asOk(this.sysAuthRoleService.addRelationByRole(role, addAuths, null));
    }

    // 查询角色拥有的权限ID
    @RequestMapping(value = "findAuthIdsByRoleId", method = RequestMethod.GET)
    public ZKMsgRes findAuthIdsByRoleId(@RequestParam("roleId") String roleId) {
        return ZKMsgRes.asOk(this.sysAuthRoleService.findAuthIdsByRoleId(roleId));
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