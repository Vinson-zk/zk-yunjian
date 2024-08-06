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
import com.zk.sys.auth.service.ZKSysAuthUserRoleService;
import com.zk.sys.org.entity.ZKSysOrgRole;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.service.ZKSysOrgUserService;

/**
 * ZKSysAuthUserRoleController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/auth/sysAuthUserRole")
public class ZKSysAuthUserRoleController extends ZKBaseController {

	@Autowired
	private ZKSysAuthUserRoleService sysAuthUserRoleService;
	
    @Autowired
    private ZKSysOrgUserService sysOrgUserService;
	
    /**
     * 给用户分配角色；
     *
     * @Title: setRelationByReqChannel
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 7, 2022 5:47:09 PM
     * @param reqChannelId
     * @param addFuncApis
     * @param hReq
     * @return
     * @return ZKMsgRes
     * @throws InterruptedException
     */
    @RequestMapping(value = "setRelationByUser/{userId}", method = RequestMethod.POST)
    public ZKMsgRes setRelationByUser(@PathVariable(value = "userId") String userId,
            @RequestBody List<ZKSysOrgRole> addRoles, HttpServletRequest hReq) {
        ZKSysOrgUser user = sysOrgUserService.get(userId);
        return ZKMsgRes.asOk(null, this.sysAuthUserRoleService.addRelationByUser(user, addRoles, null));
    }

    /**
     * 查询用户拥有的角色ID
     *
     * @Title: findRoleIdsByUserId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 4, 2022 2:37:37 PM
     * @param userId
     * @return
     * @return ZKMsgRes
     */
    @RequestMapping(value = "findRoleIdsByUserId", method = RequestMethod.GET)
    public ZKMsgRes findRoleIdsByUserId(@RequestParam("userId") String userId) {
        return ZKMsgRes.asOk(null, this.sysAuthUserRoleService.findRoleIdsByUserId(userId));
    }

}
