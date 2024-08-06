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
import com.zk.sys.auth.service.ZKSysAuthUserTypeService;
import com.zk.sys.org.entity.ZKSysOrgUserType;
import com.zk.sys.org.service.ZKSysOrgUserTypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

    // 给用户类型分配 权限
    @RequestMapping(value = "grantAuths/{userTypeId}", method = RequestMethod.POST)
    public ZKMsgRes grantAuths(@PathVariable(value = "userTypeId") String userTypeId,
            @RequestBody List<ZKSysAuthDefined> allotAuths, HttpServletRequest hReq) {
        ZKSysOrgUserType userType = sysOrgUserTypeService.get(userTypeId);
        return ZKMsgRes.asOk(null, this.sysAuthUserTypeService.allotAuthToUserType(userType, allotAuths));
    }

//    // 查询用户类型拥有的权限ID
//    @RequestMapping(value = "findAuthIdsByUserTypeId", method = RequestMethod.GET)
//    public ZKMsgRes findAuthIdsByUserTypeId(@RequestParam("userTypeId") String userTypeId) {
//        return ZKMsgRes.asOk(null, this.sysAuthUserTypeService.findAuthIdsByUserTypeId(userTypeId));
//    }

    // 查询给指定用户类型授权时，公司可分配的权限以及用户类型已拥有的权限
    @RequestMapping(value = "findAllotAuthPage", method = RequestMethod.GET)
    public ZKMsgRes findAllotAuthPageGet(@RequestParam("userTypeId") String userTypeId,
            @RequestParam(value = "searchValue", required = false) String searchValue, HttpServletRequest hReq,
            HttpServletResponse hRes) {
        ZKPage<ZKSysAuthDefined> resPage = ZKPage.asPage(hReq);
        ZKSysOrgUserType userType = this.sysOrgUserTypeService.get(userTypeId);
        List<ZKSysAuthDefined> resList = this.sysAuthUserTypeService.findAllotAuthByUserType(userType, searchValue,
                resPage);
        resPage.setResult(resList);
        return ZKMsgRes.asOk(resPage);
    }

}