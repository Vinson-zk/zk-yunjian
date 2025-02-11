/**
 * 
 */
package com.zk.sys.org.controller;

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
import com.zk.sys.org.entity.ZKSysOrgUserType;
import com.zk.sys.org.service.ZKSysOrgUserTypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;       

/**
 * ZKSysOrgUserTypeController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.org}/sysOrgUserType")
public class ZKSysOrgUserTypeController extends ZKBaseController {

	@Autowired
	private ZKSysOrgUserTypeService sysOrgUserTypeService;
	
	// 编辑
    @RequestMapping(value = "sysOrgUserType", method = RequestMethod.POST)
    public ZKMsgRes sysOrgUserTypePost(@RequestBody ZKSysOrgUserType sysOrgUserType) {
        sysOrgUserType.setCompanyId(ZKSecSecurityUtils.getCompanyId());
		this.sysOrgUserTypeService.save(sysOrgUserType);
        return ZKMsgRes.asOk(null, sysOrgUserType);
	}
	
	// 查询详情
	@RequestMapping(value="sysOrgUserType", method = RequestMethod.GET)
	public ZKMsgRes sysOrgUserTypeGet(@RequestParam("pkId") String pkId){
		ZKSysOrgUserType sysOrgUserType = this.sysOrgUserTypeService.get(new ZKSysOrgUserType(pkId));
        return ZKMsgRes.asOk(null, sysOrgUserType);
	}
	
	// 分页查询 
    @RequestMapping(value = "sysOrgUserTypesPage", method = RequestMethod.GET)
    public ZKMsgRes sysOrgUserTypesPageGet(ZKSysOrgUserType sysOrgUserType,
            HttpServletRequest hReq, HttpServletResponse hRes) {
        sysOrgUserType.setCompanyId(ZKSecSecurityUtils.getCompanyId());
		ZKPage<ZKSysOrgUserType> resPage = ZKPage.asPage(hReq);
        resPage = this.sysOrgUserTypeService.findPage(resPage, sysOrgUserType);
        return ZKMsgRes.asOk(null, resPage);
	}
	
	// 批量删除
	@RequestMapping(value="sysOrgUserType", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes sysOrgUserTypeDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysOrgUserTypeService.del(new ZKSysOrgUserType(pkId));
            }
        }
        return ZKMsgRes.asOk(null, count);
	}

}