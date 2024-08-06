/**
 * 
 */
package com.zk.sys.org.controller;

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
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.org.entity.ZKSysOrgRole;
import com.zk.sys.org.service.ZKSysOrgRoleService;       

/**
 * ZKSysOrgRoleController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/org/sysOrgRole")
public class ZKSysOrgRoleController extends ZKBaseController {

	@Autowired
	private ZKSysOrgRoleService sysOrgRoleService;
	
	// 编辑
    @RequestMapping(value = "sysOrgRole", method = RequestMethod.POST)
    public ZKMsgRes sysOrgRolePost(@RequestBody ZKSysOrgRole sysOrgRole) {
        sysOrgRole.setCompanyId(ZKSecSecurityUtils.getCompanyId());
		this.sysOrgRoleService.save(sysOrgRole);
        return ZKMsgRes.asOk(null, sysOrgRole);
	}
	
	// 查询详情
	@RequestMapping(value="sysOrgRole", method = RequestMethod.GET)
	public ZKMsgRes sysOrgRoleGet(@RequestParam("pkId") String pkId){
		ZKSysOrgRole sysOrgRole = this.sysOrgRoleService.get(new ZKSysOrgRole(pkId));
        return ZKMsgRes.asOk(null, sysOrgRole);
	}
	
	// 分页查询 
    @RequestMapping(value = "sysOrgRolesPage", method = RequestMethod.GET)
    public ZKMsgRes sysOrgRolesPageGet(ZKSysOrgRole sysOrgRole, HttpServletRequest hReq, HttpServletResponse hRes) {
        sysOrgRole.setCompanyId(ZKSecSecurityUtils.getCompanyId());
        ZKPage<ZKSysOrgRole> resPage = ZKPage.asPage(hReq);
        resPage = this.sysOrgRoleService.findPage(resPage, sysOrgRole);
        return ZKMsgRes.asOk(null, resPage);
	}
	
	// 批量删除
	@RequestMapping(value="sysOrgRole", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes sysOrgRoleDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysOrgRoleService.del(new ZKSysOrgRole(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
	}

}