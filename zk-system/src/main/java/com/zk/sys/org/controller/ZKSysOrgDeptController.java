/**
 * 
 */
package com.zk.sys.org.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.zk.sys.org.entity.ZKSysOrgDept;
import com.zk.sys.org.service.ZKSysOrgDeptService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;       

/**
 * ZKSysOrgDeptController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.org}/sysOrgDept")
public class ZKSysOrgDeptController extends ZKBaseController {

	@Autowired
	private ZKSysOrgDeptService sysOrgDeptService;
	
	// 编辑
    @RequestMapping(value = "sysOrgDept/{companyId}", method = RequestMethod.POST)
    public ZKMsgRes sysOrgDeptPost(@PathVariable("companyId") String companyId, @RequestBody ZKSysOrgDept sysOrgDept) {
        sysOrgDept.setCompanyId(companyId);
        this.sysOrgDeptService.save(sysOrgDept);
        return ZKMsgRes.asOk(null, sysOrgDept);
	}

    // 分页查询
    @RequestMapping(value = "sysOrgDeptsTree/{companyId}", method = RequestMethod.GET)
    public ZKMsgRes sysOrgDeptsTree(@PathVariable("companyId") String companyId, ZKSysOrgDept sysOrgDept,
            HttpServletRequest hReq, HttpServletResponse hRes) {
        sysOrgDept.setCompanyId(companyId);
        ZKPage<ZKSysOrgDept> resPage = ZKPage.asPage(hReq);
        resPage = this.sysOrgDeptService.findTree(resPage, sysOrgDept);
        return ZKMsgRes.asOk(null, resPage);
    }
	
	// 查询详情
	@RequestMapping(value="sysOrgDept", method = RequestMethod.GET)
	public ZKMsgRes sysOrgDeptGet(@RequestParam("pkId") String pkId){
		ZKSysOrgDept sysOrgDept = this.sysOrgDeptService.getDetail(new ZKSysOrgDept(pkId));
        return ZKMsgRes.asOk(null, sysOrgDept);
	}

	// 批量删除
	@RequestMapping(value="sysOrgDept", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes sysOrgDeptDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysOrgDeptService.del(new ZKSysOrgDept(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
	}

    /********************************************************/
    /** 查询 本公司的数据 ****/
    /********************************************************/

    // 树型查询，查询本公司的部门
    @RequestMapping(value = "sysOrgDeptsTreeSelf", method = RequestMethod.GET)
    public ZKMsgRes sysOrgDeptsTreeSelf(ZKSysOrgDept sysOrgDept, HttpServletRequest hReq, HttpServletResponse hRes) {
        sysOrgDept.setCompanyId(ZKSecSecurityUtils.getCompanyId());
        ZKPage<ZKSysOrgDept> resPage = ZKPage.asPage(hReq);
        resPage = this.sysOrgDeptService.findTree(resPage, sysOrgDept);
        return ZKMsgRes.asOk(null, resPage);
    }

    // 编辑，编辑本公司的部门
    @RequestMapping(value = "sysOrgDeptSelf", method = RequestMethod.POST)
    public ZKMsgRes sysOrgDeptSelfPost(@RequestBody ZKSysOrgDept sysOrgDept) {
        sysOrgDept.setCompanyId(ZKSecSecurityUtils.getCompanyId());
        this.sysOrgDeptService.save(sysOrgDept);
        return ZKMsgRes.asOk(null, sysOrgDept);
    }

}
