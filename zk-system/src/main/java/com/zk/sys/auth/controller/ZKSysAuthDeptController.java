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
import com.zk.sys.auth.service.ZKSysAuthDeptService;
import com.zk.sys.org.entity.ZKSysOrgDept;
import com.zk.sys.org.service.ZKSysOrgDeptService;

/**
 * ZKSysAuthDeptController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/auth/sysAuthDept")
public class ZKSysAuthDeptController extends ZKBaseController {

	@Autowired
	private ZKSysAuthDeptService sysAuthDeptService;
	
    @Autowired
    private ZKSysOrgDeptService sysOrgDeptService;

    @Autowired
    private ZKSysAuthDefinedService sysAuthDefinedService;

    // 给部门分配 权限
    @RequestMapping(value = "setRelationByDept/{deptId}", method = RequestMethod.POST)
    public ZKMsgRes setRelationByUser(@PathVariable(value = "deptId") String deptId,
            @RequestBody List<ZKSysAuthDefined> addAuths, HttpServletRequest hReq) {
        ZKSysOrgDept dept = sysOrgDeptService.get(deptId);
        return ZKMsgRes.asOk(this.sysAuthDeptService.addRelationByDept(dept, addAuths, null));
    }

    // 查询部门拥有的权限ID
    @RequestMapping(value = "findAuthIdsByDeptId", method = RequestMethod.GET)
    public ZKMsgRes findAuthIdsByDeptId(@RequestParam("deptId") String deptId) {
        return ZKMsgRes.asOk(this.sysAuthDeptService.findAuthIdsByDeptId(deptId));
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