/**
 * 
 */
package com.zk.sys.auth.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.service.ZKSysAuthDefinedService;
import com.zk.sys.auth.service.ZKSysAuthMenuService;
import com.zk.sys.res.entity.ZKSysMenu;

/**
 * ZKSysAuthMenuController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/auth/sysAuthMenu")
public class ZKSysAuthMenuController extends ZKBaseController {

	@Autowired
	private ZKSysAuthMenuService sysAuthMenuService;
	
    @Autowired
    private ZKSysAuthDefinedService sysAuthDefinedService;

    // 给权限代码分配菜单
    @RequestMapping(value = "setRelationByAuth/{authId}", method = RequestMethod.POST)
    public ZKMsgRes setRelationByAuth(@PathVariable(value = "authId") String authId,
            @RequestBody List<ZKSysMenu> addMenus, HttpServletRequest hReq) {
        ZKSysAuthDefined authDefined = sysAuthDefinedService.get(authId);
        return ZKMsgRes.asOk(null, this.sysAuthMenuService.addRelationByAuthDefined(authDefined, addMenus, null));
    }

    // 查询权限代码拥有的菜单ID
    @RequestMapping(value = "findMenuIdsByAuthId", method = RequestMethod.GET)
    public ZKMsgRes findMenuIdsByAuthId(@RequestParam("authId") String authId) {
        return ZKMsgRes.asOk(null, this.sysAuthMenuService.findMenuIdsByAuthId(authId));
    }

}