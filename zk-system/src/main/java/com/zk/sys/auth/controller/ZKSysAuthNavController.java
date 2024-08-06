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
import com.zk.sys.auth.service.ZKSysAuthNavService;
import com.zk.sys.res.entity.ZKSysNav;

/**
 * ZKSysAuthNavController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/auth/sysAuthNav")
public class ZKSysAuthNavController extends ZKBaseController {

	@Autowired
	private ZKSysAuthNavService sysAuthNavService;
	
    @Autowired
    private ZKSysAuthDefinedService sysAuthDefinedService;

    // 给权限分配 导航栏目关系
    @RequestMapping(value = "setRelationByAuth/{authId}", method = RequestMethod.POST)
    public ZKMsgRes setRelationByAuth(@PathVariable(value = "authId") String authId,
            @RequestBody List<ZKSysNav> addNavs, HttpServletRequest hReq) {
        ZKSysAuthDefined authDefined = sysAuthDefinedService.get(authId);
        return ZKMsgRes.asOk(null, this.sysAuthNavService.addRelationByAuthDefined(authDefined, addNavs, null));
    }

    // 查询权限代码拥有的导航栏目ID
    @RequestMapping(value = "findNavIdsByAuthId", method = RequestMethod.GET)
    public ZKMsgRes findNavIdsByAuthId(@RequestParam("authId") String authId) {
        return ZKMsgRes.asOk(null, this.sysAuthNavService.findNavIdsByAuthId(authId));
    }

}
