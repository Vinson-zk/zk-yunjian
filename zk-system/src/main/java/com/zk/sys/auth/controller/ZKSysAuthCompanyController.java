/**
 * 
 */
package com.zk.sys.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.zk.sys.auth.entity.ZKSysAuthCompany;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.service.ZKSysAuthCompanyService;
import com.zk.sys.auth.service.ZKSysAuthDefinedService;
import com.zk.sys.entity.org.ZKSysOrgCompany;
import com.zk.sys.org.service.ZKSysOrgCompanyService;

/**
 * ZKSysAuthCompanyController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/auth/sysAuthCompany")
public class ZKSysAuthCompanyController extends ZKBaseController {

	@Autowired
	private ZKSysAuthCompanyService sysAuthCompanyService;
	
    @Autowired
    private ZKSysOrgCompanyService sysOrgCompanyService;

    @Autowired
    private ZKSysAuthDefinedService sysAuthDefinedService;

    // 给公司分配 权限
    @RequestMapping(value = "setRelationByCompany/{companyId}", method = RequestMethod.POST)
    public ZKMsgRes setRelationByUser(@PathVariable(value = "companyId") String companyId,
            @RequestBody List<ZKSysAuthDefined> addAuths, HttpServletRequest hReq) {
        ZKSysOrgCompany company = sysOrgCompanyService.get(companyId);
        return ZKMsgRes.asOk(this.sysAuthCompanyService.addRelationByCompany(company, addAuths, null));
    }

    /**
     * 查询公司拥有的权限ID及方式;
     *
     * @Title: findAuthIdsByCompanyId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 9, 2022 2:17:36 PM
     * @param companyId
     * @param ownerType
     *            传入时，查询指定拥有方式的权限IDS；不传时，以拥有方式为 key-其拥有的IDS List 为值；
     * @return
     * @return ZKMsgRes
     */
    @RequestMapping(value = "findAuthIdsByCompanyId", method = RequestMethod.GET)
    public ZKMsgRes findAuthIdsByCompanyId(@RequestParam("companyId") String companyId,
            @RequestParam(value = "ownerType", required = false) Integer ownerType) {
        if (ownerType == null) {
            Map<Integer, List<String>> resDataMap = new HashMap<>();
            resDataMap.put(ZKSysAuthCompany.KeyOwnerType.normal,
                    this.sysAuthCompanyService.findAuthIdsByCompanyId(companyId, ZKSysAuthCompany.KeyOwnerType.normal));
            resDataMap.put(ZKSysAuthCompany.KeyOwnerType.all,
                    this.sysAuthCompanyService.findAuthIdsByCompanyId(companyId, ZKSysAuthCompany.KeyOwnerType.all));
            return ZKMsgRes.asOk(resDataMap);
        }
        else {
            return ZKMsgRes.asOk(this.sysAuthCompanyService.findAuthIdsByCompanyId(companyId, ownerType));
        }
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
