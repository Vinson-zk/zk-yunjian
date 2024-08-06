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
import com.zk.sys.auth.service.ZKSysAuthDeptService;
import com.zk.sys.org.entity.ZKSysOrgDept;
import com.zk.sys.org.service.ZKSysOrgDeptService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

    // 给部门分配 权限
    @RequestMapping(value = "grantAuths/{deptId}", method = RequestMethod.POST)
    public ZKMsgRes grantAuths(@PathVariable(value = "deptId") String deptId,
            @RequestBody List<ZKSysAuthDefined> allotAuths, HttpServletRequest hReq) {
        ZKSysOrgDept dept = sysOrgDeptService.get(deptId);
        return ZKMsgRes.asOk(null, this.sysAuthDeptService.allotAuthToDept(dept, allotAuths));
    }

    // 查询给指定部门授权时，公司可分配的权限以及部门已拥有的权限
    @RequestMapping(value = "findAllotAuthPage", method = RequestMethod.GET)
    public ZKMsgRes findAllotAuthPageGet(@RequestParam("deptId") String deptId,
            @RequestParam(value = "searchValue", required = false) String searchValue, HttpServletRequest hReq,
            HttpServletResponse hRes) {
        ZKPage<ZKSysAuthDefined> resPage = ZKPage.asPage(hReq);
        ZKSysOrgDept dept = this.sysOrgDeptService.get(deptId);
        List<ZKSysAuthDefined> resList = this.sysAuthDeptService.findAllotAuthByDept(dept, searchValue, resPage);
        resPage.setResult(resList);
        return ZKMsgRes.asOk(resPage);
    }

}