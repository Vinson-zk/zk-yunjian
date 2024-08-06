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
import com.zk.sys.auth.service.ZKSysAuthFuncApiService;
import com.zk.sys.res.entity.ZKSysResFuncApi;

/**
 * ZKSysAuthFuncApiController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/auth/sysAuthFuncApi")
public class ZKSysAuthFuncApiController extends ZKBaseController {

	@Autowired
	private ZKSysAuthFuncApiService sysAuthFuncApiService;
	
    @Autowired
    private ZKSysAuthDefinedService sysAuthDefinedService;

    // 给权限代码分配 Api 接口
    @RequestMapping(value = "setRelationByAuth/{authId}", method = RequestMethod.POST)
    public ZKMsgRes setRelationByAuth(@PathVariable(value = "authId") String authId,
            @RequestBody List<ZKSysResFuncApi> addFuncApis, HttpServletRequest hReq) {
        ZKSysAuthDefined authDefined = sysAuthDefinedService.get(authId);
        return ZKMsgRes.asOk(null, this.sysAuthFuncApiService.addRelationByAuthDefined(authDefined, addFuncApis, null));
    }

    // 查询权限代码拥有的Api 接口ID
    @RequestMapping(value = "findApiIdsByAuthId", method = RequestMethod.GET)
    public ZKMsgRes findApiIdsByAuthId(@RequestParam("authId") String authId) {
        return ZKMsgRes.asOk(null, this.sysAuthFuncApiService.findApiIdsByAuthId(authId));
    }

//    // 查询权限代码拥有的Api 接口Code
//    @RequestMapping(value = "findApiCodesByAuthId", method = RequestMethod.GET)
//    public ZKMsgRes findApiCodesByAuthId(@RequestParam("authId") String authId) {
//        return ZKMsgRes.asOk(null, this.sysAuthFuncApiService.findApiCodesByAuthId(authId));
//    }

}
