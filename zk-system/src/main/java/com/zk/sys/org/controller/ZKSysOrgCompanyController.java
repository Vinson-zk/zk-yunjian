/**
 * 
 */
package com.zk.sys.org.controller;

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
import com.zk.core.exception.ZKCodeException;
import com.zk.core.commons.ZKMsgRes;
import com.zk.security.annotation.ZKSecApiCode;
import com.zk.sys.entity.org.ZKSysOrgCompany;
import com.zk.sys.org.service.ZKSysOrgCompanyOptService;
import com.zk.sys.org.service.ZKSysOrgCompanyService;       

/**
 * ZKSysOrgCompanyController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/org/sysOrgCompany")
public class ZKSysOrgCompanyController extends ZKBaseController {

	@Autowired
	private ZKSysOrgCompanyService sysOrgCompanyService;
	
    @Autowired
    private ZKSysOrgCompanyOptService sysOrgCompanyOptService;

    // 查询详情
    @RequestMapping(value = "sysOrgCompanyByCode", method = RequestMethod.GET)
    @ZKSecApiCode("com_zk_sys_org_sysOrgCompany_sysOrgCompanyByCode")
    public ZKMsgRes sysOrgCompanyByCode(@RequestParam("companyCode") String companyCode) {
        ZKSysOrgCompany sysOrgCompany = this.sysOrgCompanyService.getByCode(companyCode);
        if (sysOrgCompany == null) {
            log.error("[^_^:20220018-1411-001] 公司[{}]不存在;", companyCode);
            throw ZKCodeException.as("zk.sys.010003", "公司不存在");
        }
        return ZKMsgRes.asOk(sysOrgCompany);
    }

	// 编辑
	@RequestMapping(value="sysOrgCompany", method = RequestMethod.POST)
    public ZKMsgRes sysOrgCompanyPost(@RequestBody ZKSysOrgCompany sysOrgCompany) {
        this.sysOrgCompanyService.save(sysOrgCompany);
        return ZKMsgRes.asOk(sysOrgCompany);
	}
	
	// 查询详情
	@RequestMapping(value="sysOrgCompany", method = RequestMethod.GET)
	public ZKMsgRes sysOrgCompanyGet(@RequestParam("pkId") String pkId){
		ZKSysOrgCompany sysOrgCompany = this.sysOrgCompanyService.getDetail(new ZKSysOrgCompany(pkId));
        return ZKMsgRes.asOk(sysOrgCompany);
	}
	
    // 分页树形查询
	@RequestMapping(value="sysOrgCompanysTree", method = RequestMethod.GET)
	public ZKMsgRes sysOrgCompanysTree(ZKSysOrgCompany sysOrgCompany, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKSysOrgCompany> resPage = ZKPage.asPage(hReq);
        resPage = this.sysOrgCompanyService.findTree(resPage, sysOrgCompany);
        return ZKMsgRes.asOk(resPage);
	}
	
    // 分页查询
    @RequestMapping(value = "sysOrgCompanys", method = RequestMethod.GET)
    public ZKMsgRes sysOrgCompanys(ZKSysOrgCompany sysOrgCompany, HttpServletRequest hReq, HttpServletResponse hRes) {
        ZKPage<ZKSysOrgCompany> resPage = ZKPage.asPage(hReq);
        resPage = this.sysOrgCompanyService.findPage(resPage, sysOrgCompany);
        return ZKMsgRes.asOk(resPage);
    }

	// 批量删除
	@RequestMapping(value="sysOrgCompany", method = RequestMethod.DELETE)
	public ZKMsgRes sysOrgCompanyDel(@RequestParam("pkId[]") String[] pkIds){
		 int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysOrgCompanyService.del(new ZKSysOrgCompany(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

    /**
     * 审核公司
     *
     * @Title: auditCompanyDel
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 12, 2022 4:39:37 PM
     * @param companyId
     *            审核的目标公司ID
     * @param flag
     *            审核的标识；0-通过审核；其他禁用
     * @return
     * @return ZKMsgRes
     */
    @RequestMapping(value = "auditCompany/{companyId}/{flag}", method = RequestMethod.POST)
    public ZKMsgRes auditCompany(@PathVariable(value = "companyId") String companyId,
            @PathVariable(value = "flag") int flag) {
        this.sysOrgCompanyOptService.auditCompany(companyId, flag);
        return ZKMsgRes.asOk(null);
    }


}
