/**
 * 
 */
package com.zk.sys.org.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.annotation.ZKSecApiCode;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.service.ZKSysOrgCompanyOptService;
import com.zk.sys.org.service.ZKSysOrgCompanyOptService.TInfoKey;
import com.zk.sys.org.service.ZKSysOrgCompanyService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ZKSysOrgCompanyController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.org}/sysOrgCompany")
public class ZKSysOrgCompanyController extends ZKBaseController {

    @Autowired
    private ZKSysOrgCompanyService sysOrgCompanyService;
	
    @Autowired
    private ZKSysOrgCompanyOptService sysOrgCompanyOptService;

    @Value("${zk.sys.file.server.url.prefix}")
    String fileSeverUrlPrefix;

//    @Autowired(required = false)
//    ZKSecTicketManager ticketManager;

    /********************************************************/
    /** 公司 不需要用户登录的操作 ****/
    /********************************************************/
    // 公司注册，发送邮件和手机验证码
    @RequestMapping(value = "n/sendVerifyCode", method = RequestMethod.POST)
    public ZKMsgRes register(@RequestBody ZKSysOrgCompany sysOrgCompany) {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet(true);
//        sysOrgCompany.setStatus(ZKSysOrgCompany.KeyStatus.waitVerifyCode);
        this.sysOrgCompanyOptService.sendVerifyCode(sysOrgCompany, tk);
        return ZKMsgRes.asOk(sysOrgCompany);
    }

    /**
     * 重新发送验证码；
     *
     * @Title: sendVerifyCodeAgain
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 2, 2024 11:22:05 AM
     * @param againFlag
     *            1-重发邮箱验证码；2-重发手机验证码；其他-不发；
     * @return
     * @return ZKMsgRes
     */
    @RequestMapping(value = "n/sendVerifyCodeAgain", method = RequestMethod.POST)
    public ZKMsgRes sendVerifyCodeAgain(@RequestParam("againFlag") int againFlag) {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
        this.sysOrgCompanyOptService.rgSendVerifyCodeAgain(tk, againFlag);
        return ZKMsgRes.asOk();
    }

    // 公司注册，提交验证码
    @RequestMapping(value = "n/submitVerifyCode", method = RequestMethod.POST)
    public ZKMsgRes submitVerifyCode(@RequestParam("mailVerifyCode") String mailVerifyCode,
            @RequestParam("phoneVerifyCode") String phoneVerifyCode, @RequestParam("pwd") String password,
            HttpServletRequest req) {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
        ZKSysOrgCompany sysOrgCompany = this.sysOrgCompanyOptService.submitRegisterVerifyCode(password, mailVerifyCode,
                phoneVerifyCode, tk, req);
        tk.put(TInfoKey.tempCompany, sysOrgCompany);
        return ZKMsgRes.asOk(sysOrgCompany);
    }

    // 公司提交审核信息，并提交审核
    @RequestMapping(value = "n/submitAuditInfo", method = RequestMethod.POST)
    public ZKMsgRes submitAuditInfo(@RequestPart(value = "company") ZKSysOrgCompany sysOrgCompany,
            @RequestPart(value = "_p_logo", required = false) MultipartFile _p_logo, // 身份证正面(国徽面)
            @RequestPart(value = "_p_legalCertPhotoFront", required = false) MultipartFile _p_legalCertPhotoFront, // 身份证正面(国徽面)
            @RequestPart(value = "_p_legalCertPhotoBack", required = false) MultipartFile _p_legalCertPhotoBack, // 身份证背面(人像面)
            @RequestPart(value = "_p_companyCertPhoto", required = false) MultipartFile _p_companyCertPhoto) {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
        this.sysOrgCompanyOptService.updateAuditContent(sysOrgCompany, tk, _p_logo, _p_legalCertPhotoFront,
                _p_legalCertPhotoBack, _p_companyCertPhoto);
        return ZKMsgRes.asOk(sysOrgCompany);
    }

    // 从临时令牌中取公司信息
    @RequestMapping(value = "n/getCompanyInfoByTk")
    public ZKMsgRes getCompanyInfoByTk() {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();

        if (tk == null) {
            log.error("[>_<:20240724-2352-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        // 如果验证码存在，请先提交验证码
        if (tk.get(TInfoKey.sendMailVerifyCode) != null || tk.get(TInfoKey.sendPhoneVerifyCode) != null) {
            log.error("[>_<:20250120-1541-001] zk.sys.010034，请先提交验证码");
            throw ZKBusinessException.as("zk.sys.010034");
        }
        //
        ZKSysOrgCompany tempCompany = tk.get(TInfoKey.tempCompany);
        if (tempCompany == null) {
            log.error("[>_<:20240724-2352-002] 公司注册异常，令牌信息丢失");
            throw ZKBusinessException.as("zk.sec.000022");
        }
        return ZKMsgRes.asOk(ZKStringUtils.isEmpty(tempCompany.getPkId()) ? tempCompany
            : this.sysOrgCompanyService.get(tempCompany.getPkId()));
    }

    /**
     * 取企业的证件照片
     *
     * @Title: certPhoto
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jan 17, 2025 4:39:48 PM
     * @param index
     *            0-法人证件正面；1-法人证件背面；2-公司证件；3-logo
     * @param hReq
     * @param hRes
     * @return void
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping(value = "n/certPhoto/{index}")
    public void certPhoto(@PathVariable(value = "index") int index, HttpServletRequest hReq, HttpServletResponse hRes)
            throws ServletException, IOException {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();

        if (tk == null) {
            log.error("[>_<:20240724-2352-001] 系统异常，令牌不能为空");
            throw ZKBusinessException.as("zk.sec.000022");
        }

        ZKSysOrgCompany company = tk.get(TInfoKey.tempCompany);
        if (company == null) {
            log.error("[>_<:20240724-2352-002] 公司注册异常，令牌信息丢失");
            throw ZKBusinessException.as("zk.sec.000022");
        }

        tk.put(ZKSecConstants.PARAM_NAME.CompanyCode, company.getCode());
        // 将公司ID做为上传公司文件的用户ID
        tk.put(ZKSecConstants.PARAM_NAME.UserId, company.getPkId());

        String saveUUID = null;
        if (index == 2) {
            saveUUID = company.getCompanyCertPhoto();
        }
        else if (index == 3) {
            saveUUID = company.getLogo();
        }
        else {
            String[] legalCerts = null;
            if (!ZKStringUtils.isEmpty(company.getLegalCertPhoto())) {
                legalCerts = company.getLegalCertPhoto().split(ZKSysOrgCompany.certSeparator);
                if (index == 0 && legalCerts != null && legalCerts.length > 0) {
                    saveUUID = legalCerts[0];
                }
                else if (index == 1 && legalCerts != null && legalCerts.length > 1) {
                    saveUUID = legalCerts[1];
                }
            }
        }

        if (ZKStringUtils.isEmpty(saveUUID)) {
            // fq无
        }
        else {
            // 到文件服务器
            // 到文件服务器
            StringBuffer sb = new StringBuffer();
            sb.append(this.fileSeverUrlPrefix).append("/").append(tk.getTkId());
            sb.append("?saveUuid=").append(saveUUID);

            System.out.println("[^_^:20250120-1726-001] url: " + sb.toString());

////            // 转发无法实现跨域；
//            RequestDispatcher rd = hReq.getRequestDispatcher(sb.toString());
//            rd.forward(hReq, hRes);

            hRes.sendRedirect(sb.toString());
        }

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

//    // 编辑本公司
//    @RequestMapping(value = "sysOrgCompanySelf", method = RequestMethod.POST)
//    public ZKMsgRes sysOrgCompanySelfPost(@RequestBody ZKSysOrgCompany sysOrgCompany) {
//        String companyId = ZKSecSecurityUtils.getCompanyId();
//        if (ZKStringUtils.isEmpty(companyId) || !companyId.equals(sysOrgCompany.getPkId())) {
//            log.error("[^_^:20220018-1411-001] zk.sys.010025=您只能修改本公司的信息;");
//            throw ZKBusinessException.as("zk.sys.010025");
//        }
//        this.sysOrgCompanyService.editCompany(sysOrgCompany, null);
//        return ZKMsgRes.asOk(sysOrgCompany);
//    }

    // 公司提交审核信息，并提交审核
    @RequestMapping(value = "submitAuditInfoSelf", method = RequestMethod.POST)
    public ZKMsgRes submitAuditInfoSelf(@RequestPart(value = "company") ZKSysOrgCompany sysOrgCompany,
            @RequestPart(value = "_p_logo", required = false) MultipartFile _p_logo, // 身份证正面(国徽面)
            @RequestPart(value = "_p_legalCertPhotoFront", required = false) MultipartFile _p_legalCertPhotoFront, // 身份证正面(国徽面)
            @RequestPart(value = "_p_legalCertPhotoBack", required = false) MultipartFile _p_legalCertPhotoBack, // 身份证背面(人像面)
            @RequestPart(value = "_p_companyCertPhoto", required = false) MultipartFile _p_companyCertPhoto) {
        sysOrgCompany.setPkId(ZKSecSecurityUtils.getCompanyId());
        this.sysOrgCompanyService.updateAuditContent(sysOrgCompany, _p_logo, _p_legalCertPhotoFront,
                _p_legalCertPhotoBack, _p_companyCertPhoto);
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

//	// 编辑
//	@RequestMapping(value="sysOrgCompany", method = RequestMethod.POST)
//    public ZKMsgRes sysOrgCompanyPost(@RequestBody ZKSysOrgCompany sysOrgCompany) {
//        this.sysOrgCompanyService.editCompany(sysOrgCompany, null);
//        return ZKMsgRes.asOk(null, sysOrgCompany);
//	}
	
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

