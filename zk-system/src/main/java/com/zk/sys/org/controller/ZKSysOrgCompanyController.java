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
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.annotation.ZKSecApiCode;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.service.ZKSysOrgCompanyOptService;
import com.zk.sys.org.service.ZKSysOrgCompanyOptService.TInfoKey;
import com.zk.sys.org.service.ZKSysOrgCompanyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

    /********************************************************/
    /** 公司 不需要用户登录的操作 ****/
    /********************************************************/
    // 公司注册，发送邮件和手机验证码
    @RequestMapping(value = "n/sendVerifiyCode", method = RequestMethod.POST)
    public ZKMsgRes register(@RequestBody ZKSysOrgCompany sysOrgCompany) {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet(true);
        sysOrgCompany.setStatus(ZKSysOrgCompany.KeyStatus.waitSubmit);
        this.sysOrgCompanyOptService.sendVerifiyCode(sysOrgCompany, tk);
        return ZKMsgRes.asOk();
    }

    /**
     * 重新发送验证码；
     *
     * @Title: sendVerifiyCodeAgain
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 2, 2024 11:22:05 AM
     * @param againFlag
     *            1-重发邮箱验证码；2-重发手机验证码；其他-不发；
     * @return
     * @return ZKMsgRes
     */
    @RequestMapping(value = "n/sendVerifiyCodeAgain", method = RequestMethod.POST)
    public ZKMsgRes sendVerifiyCodeAgain(@RequestParam("againFlag") int againFlag) {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
        this.sysOrgCompanyOptService.rgSendVerifiyCodeAgain(tk, againFlag);
        return ZKMsgRes.asOk();
    }

    // 公司注册，提交验证码
    @RequestMapping(value = "n/submitVerifiyCode", method = RequestMethod.POST)
    public ZKMsgRes submitVerifiyCode(@RequestParam("mailVerifiyCode") String mailVerifiyCode,
            @RequestParam("phoneVerifiyCode") String phoneVerifiyCode, @RequestParam("pwd") String password) {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
        ZKSysOrgCompany sysOrgCompany = this.sysOrgCompanyOptService.submitRegisterVerifiyCode(password,
                mailVerifiyCode, phoneVerifiyCode, tk);
        tk.put(TInfoKey.tempCompany, sysOrgCompany);
        return ZKMsgRes.asOk(sysOrgCompany);
    }

    // 公司提交审核信息，并提交审核
    @RequestMapping(value = "n/submitAuditInfo", method = RequestMethod.POST)
    public ZKMsgRes submitAuditInfo(@RequestBody ZKSysOrgCompany sysOrgCompany) {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
        this.sysOrgCompanyOptService.updateAuditContent(sysOrgCompany, tk);
        return ZKMsgRes.asOk(sysOrgCompany);
    }

    /********************************************************/
    /** 本公司操作 ****/
    /********************************************************/

    // 分页树形查询
    @RequestMapping(value = "sysOrgChildCompanysTree", method = RequestMethod.GET)
    public ZKMsgRes sysOrgChildCompanysTree(ZKSysOrgCompany sysOrgCompany, HttpServletRequest hReq,
            HttpServletResponse hRes) {
        sysOrgCompany.setParentId(ZKSecSecurityUtils.getCompanyId());
        ZKPage<ZKSysOrgCompany> resPage = ZKPage.asPage(hReq);
        resPage = this.sysOrgCompanyService.findTree(resPage, sysOrgCompany);
        return ZKMsgRes.asOk(null, resPage);
    }

    // 分页查询
    @RequestMapping(value = "sysOrgChildCompanys", method = RequestMethod.GET)
    public ZKMsgRes sysOrgChildCompanys(ZKSysOrgCompany sysOrgCompany, HttpServletRequest hReq,
            HttpServletResponse hRes) {
        sysOrgCompany.setParentId(ZKSecSecurityUtils.getCompanyId());
        ZKPage<ZKSysOrgCompany> resPage = ZKPage.asPage(hReq);
        resPage = this.sysOrgCompanyService.findPage(resPage, sysOrgCompany);
        return ZKMsgRes.asOk(null, resPage);
    }

    // 查询本公司详情
    @RequestMapping(value = "sysOrgCompanySelf", method = RequestMethod.GET)
    public ZKMsgRes sysOrgCompanySelfGet() {
        String companyId = ZKSecSecurityUtils.getCompanyId();
        ZKSysOrgCompany sysOrgCompany = this.sysOrgCompanyService.getDetail(new ZKSysOrgCompany(companyId));
        if (sysOrgCompany == null) {
            log.error("[^_^:20220018-1411-001] 公司不存在;");
            throw ZKBusinessException.as("zk.sys.010003");
        }
        return ZKMsgRes.asOk(sysOrgCompany);
    }

    // 编辑本公司
    @RequestMapping(value = "sysOrgCompanySelf", method = RequestMethod.POST)
    public ZKMsgRes sysOrgCompanySelfPost(@RequestBody ZKSysOrgCompany sysOrgCompany) {
        String companyId = ZKSecSecurityUtils.getCompanyId();
        if (ZKStringUtils.isEmpty(companyId) || !companyId.equals(sysOrgCompany.getPkId())) {
            log.error("[^_^:20220018-1411-001] zk.sys.010025=您只能修改本公司的信息;");
            throw ZKBusinessException.as("zk.sys.010025");
        }
        this.sysOrgCompanyService.editCompany(sysOrgCompany, null);
        return ZKMsgRes.asOk(sysOrgCompany);
    }

    // 公司提交审核信息，并提交审核
    @RequestMapping(value = "submitAuditInfoSelf", method = RequestMethod.POST)
    public ZKMsgRes submitAuditInfoSelf(@RequestBody ZKSysOrgCompany sysOrgCompany) {
        this.sysOrgCompanyService.updateAuditContent(sysOrgCompany);
        return ZKMsgRes.asOk(sysOrgCompany);
    }

    /********************************************************/
    /** 公司操作 ****/
    /********************************************************/
    // 查询详情
    @RequestMapping(value = "sysOrgCompanyByCode", method = RequestMethod.GET)
    @ZKSecApiCode("com_zk_sys_org_sysOrgCompany_sysOrgCompanyByCode")
    public ZKMsgRes sysOrgCompanyByCode(@RequestParam("companyCode") String companyCode) {
        ZKSysOrgCompany sysOrgCompany = this.sysOrgCompanyService.getByCode(companyCode);
        if (sysOrgCompany == null) {
            log.error("[^_^:20220018-1411-001] 公司[{}]不存在;", companyCode);
            throw ZKBusinessException.as("zk.sys.010003", "公司不存在");
        }
        return ZKMsgRes.asOk(null, sysOrgCompany);
    }

	// 编辑
	@RequestMapping(value="sysOrgCompany", method = RequestMethod.POST)
    public ZKMsgRes sysOrgCompanyPost(@RequestBody ZKSysOrgCompany sysOrgCompany) {
        this.sysOrgCompanyService.editCompany(sysOrgCompany, null);
        return ZKMsgRes.asOk(null, sysOrgCompany);
	}
	
	// 查询详情
	@RequestMapping(value="sysOrgCompany", method = RequestMethod.GET)
	public ZKMsgRes sysOrgCompanyGet(@RequestParam("pkId") String pkId){
		ZKSysOrgCompany sysOrgCompany = this.sysOrgCompanyService.getDetail(new ZKSysOrgCompany(pkId));
        return ZKMsgRes.asOk(null, sysOrgCompany);
	}
	
    // 分页树形查询
	@RequestMapping(value="sysOrgCompanysTree", method = RequestMethod.GET)
	public ZKMsgRes sysOrgCompanysTree(ZKSysOrgCompany sysOrgCompany, HttpServletRequest hReq, HttpServletResponse hRes){
		ZKPage<ZKSysOrgCompany> resPage = ZKPage.asPage(hReq);
        resPage = this.sysOrgCompanyService.findTree(resPage, sysOrgCompany);
        return ZKMsgRes.asOk(null, resPage);
	}
	
    // 分页查询
    @RequestMapping(value = "sysOrgCompanys", method = RequestMethod.GET)
    public ZKMsgRes sysOrgCompanys(ZKSysOrgCompany sysOrgCompany, HttpServletRequest hReq, HttpServletResponse hRes) {
        ZKPage<ZKSysOrgCompany> resPage = ZKPage.asPage(hReq);
        resPage = this.sysOrgCompanyService.findPage(resPage, sysOrgCompany);
        return ZKMsgRes.asOk(null, resPage);
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
        return ZKMsgRes.asOk(null, count);
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
        String currentCompanyId = ZKSecSecurityUtils.getCompanyId();
        this.sysOrgCompanyService.auditCompany(currentCompanyId, companyId, flag);
        return ZKMsgRes.asOk(null, null);
    }

}
