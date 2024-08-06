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
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.auth.entity.ZKSysAuthDefined;
import com.zk.sys.auth.service.ZKSysAuthCompanyService;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.service.ZKSysOrgCompanyService;
import com.zk.sys.utils.ZKSysUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

    // 给公司分配 权限
    @RequestMapping(value = "grantAuths/{companyId}", method = RequestMethod.POST)
    public ZKMsgRes grantAuths(@PathVariable(value = "companyId") String companyId,
            @RequestBody List<ZKSysAuthDefined> allotAuths, HttpServletRequest hReq) {
        ZKSysOrgCompany company = sysOrgCompanyService.get(companyId);
        return ZKMsgRes.asOk(null, this.sysAuthCompanyService.allotAuthToCompany(company, allotAuths));
    }

//    /**
//     * 查询公司拥有的权限ID及方式;
//     *
//     * @Title: findAuthIdsByCompanyId
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date May 9, 2022 2:17:36 PM
//     * @param companyId
//     * @param ownerType
//     *            传入时，查询指定拥有方式的权限IDS；不传时，以拥有方式为 key-其拥有的IDS List 为值；
//     * @return
//     * @return ZKMsgRes
//     */
//    @RequestMapping(value = "findAuthIdsByCompanyId", method = RequestMethod.GET)
//    public ZKMsgRes findAuthIdsByCompanyId(@RequestParam("companyId") String companyId,
//            @RequestParam(value = "ownerType", required = false) Integer ownerType) {
//        if (ownerType == null) {
//            Map<Integer, List<String>> resDataMap = new HashMap<>();
//            resDataMap.put(ZKSysAuthCompany.KeyOwnerType.normal,
//                    this.sysAuthCompanyService.findAuthIdsByCompanyId(companyId, ZKSysAuthCompany.KeyOwnerType.normal));
//            resDataMap.put(ZKSysAuthCompany.KeyOwnerType.all,
//                    this.sysAuthCompanyService.findAuthIdsByCompanyId(companyId, ZKSysAuthCompany.KeyOwnerType.all));
//            return ZKMsgRes.asOk(null, resDataMap);
//        }
//        else {
//            return ZKMsgRes.asOk(null, this.sysAuthCompanyService.findAuthIdsByCompanyId(companyId, ownerType));
//        }
//    }

    // 查询当前登录公司所拥有的权限及拥有权限的方式
    @RequestMapping(value = "findAuthPage", method = RequestMethod.GET)
    public ZKMsgRes findAuthPageGet(@RequestParam(value = "ownerType", required = false) Integer ownerType,
            @RequestParam(value = "searchValue", required = false) String searchValue, HttpServletRequest hReq,
            HttpServletResponse hRes) {
        ZKPage<ZKSysAuthDefined> resPage = ZKPage.asPage(hReq);
        String companyId = ZKSecSecurityUtils.getCompanyId();
        List<ZKSysAuthDefined> resList = this.sysAuthCompanyService.findAuthByCompanyId(companyId, ownerType, searchValue,
                resPage);
        resPage.setResult(resList);
        return ZKMsgRes.asOk(null, resPage);
    }

    // 查询当前登录公司向指定公司授权时，可授权的权限列表及被授权公司已拥有的权限状态
    @RequestMapping(value = "findAllotAuthPage", method = RequestMethod.GET)
    public ZKMsgRes findAllotAuthPageGet(@RequestParam(value = "toCompanyId") String toCompanyId,
            @RequestParam(value = "searchValue", required = false) String searchValue, HttpServletRequest hReq,
            HttpServletResponse hRes) {
        ZKPage<ZKSysAuthDefined> resPage = ZKPage.asPage(hReq);
        // 取平台拥有者公司代码
        String ownerCode = ZKSysUtils.getOwnerCompanyCode();
        // 取被授权公司
        ZKSysOrgCompany toCompany = sysOrgCompanyService.get(toCompanyId);
        if (toCompany == null) {
            log.error("[>_<:20240703-2301-001] zk.sys.010003 公司不存在; companyId: [{}]", toCompanyId);
            throw ZKBusinessException.as("zk.sys.010003");
        }
        String fromCompanyId = null;
        if (toCompany.getCode().equals(ownerCode)) {
            // 给拥有者公司授权
            fromCompanyId = null;
        }else {
            // 取出当前登录公司，做为授权公司
            fromCompanyId = ZKSecSecurityUtils.getCompanyId();
            ZKSysOrgCompany fromCompany = sysOrgCompanyService.get(fromCompanyId);
            if (fromCompany == null) {
                log.error("[>_<:20240703-2301-002] zk.sys.010003 公司不存在; companyId: [{}]", toCompanyId);
                throw ZKBusinessException.as("zk.sys.010003");
            }
            if(ZKStringUtils.isEmpty(toCompany.getParentId())) {
                // 给非平台拥有者的一级公司授权
                if (!fromCompany.getCode().equals(ownerCode)) {
                    // 不是平台拥有者，不能给平台公司分配权限
                    log.error(
                            "[>_<:20240703-2301-002] zk.sys.020013 不是平台拥有者，不能给平台公司分配权限; fromCompany-toCompany: [{}-{}]",
                            fromCompany.getPkId(), toCompanyId);
                    throw ZKBusinessException.as("zk.sys.020013");
                }

            }else {
                // 给下级公司授权
                if (!fromCompany.getPkId().equals(toCompany.getParentId())) {
                    // 不能给非本公司的子公司授权
                    log.error("[>_<:20240703-2301-002] zk.sys.020012 不能给非本公司的子公司授权; fromCompany-toCompany: [{}-{}]",
                            fromCompany.getPkId(), toCompanyId);
                    throw ZKBusinessException.as("zk.sys.020012");

                }
            }
        }

        List<ZKSysAuthDefined> resList = this.sysAuthCompanyService.findAllotAuthByCompanyId(fromCompanyId, toCompanyId,
                searchValue, resPage);
        resPage.setResult(resList);
        return ZKMsgRes.asOk(null, resPage);
    }

    // 设置权限是否默认传递给子公司
    @RequestMapping(value = "setAuthsDefaultTransfer", method = RequestMethod.POST)
    public ZKMsgRes setAuthsDefaultTransfer(
            @RequestParam(value = "transferPkId[]", required = false) String[] transferPkIds,
            @RequestParam(value = "notTransferPkId[]", required = false) String[] notTransferPkIds,
            HttpServletRequest hReq) {
        String currentCompanyId = ZKSecSecurityUtils.getCompanyId();
        return ZKMsgRes.asOk(null,
                this.sysAuthCompanyService.setAuthsDefaultTransfer(currentCompanyId, transferPkIds, notTransferPkIds));
    }

}
