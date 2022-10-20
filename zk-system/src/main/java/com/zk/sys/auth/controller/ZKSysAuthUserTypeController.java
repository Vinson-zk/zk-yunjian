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
import com.zk.sys.auth.service.ZKSysAuthUserTypeService;
import com.zk.sys.org.entity.ZKSysOrgUserType;
import com.zk.sys.org.service.ZKSysOrgUserTypeService;

/**
 * ZKSysAuthUserTypeController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/auth/sysAuthUserType")
public class ZKSysAuthUserTypeController extends ZKBaseController {

	@Autowired
	private ZKSysAuthUserTypeService sysAuthUserTypeService;

    @Autowired
    private ZKSysOrgUserTypeService sysOrgUserTypeService;

    @Autowired
    private ZKSysAuthDefinedService sysAuthDefinedService;
	
    // 给用户类型分配 权限
    @RequestMapping(value = "setRelationByUserType/{userTypeId}", method = RequestMethod.POST)
    public ZKMsgRes setRelationByUserType(@PathVariable(value = "userTypeId") String userTypeId,
            @RequestBody List<ZKSysAuthDefined> addAuths, HttpServletRequest hReq) {
        ZKSysOrgUserType userType = sysOrgUserTypeService.get(userTypeId);
        return ZKMsgRes.asOk(this.sysAuthUserTypeService.addRelationByUserType(userType, addAuths, null));
    }

    // 查询用户类型拥有的权限ID
    @RequestMapping(value = "findAuthIdsByUserTypeId", method = RequestMethod.GET)
    public ZKMsgRes findAuthIdsByUserTypeId(@RequestParam("userTypeId") String userTypeId) {
        return ZKMsgRes.asOk(this.sysAuthUserTypeService.findAuthIdsByUserTypeId(userTypeId));
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