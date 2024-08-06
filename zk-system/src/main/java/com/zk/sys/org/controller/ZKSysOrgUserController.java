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
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.entity.ZKSysOrgUserEditLog.ZKUserEditFlag;
import com.zk.sys.org.service.ZKSysOrgUserEditLogService;
import com.zk.sys.org.service.ZKSysOrgUserOptService;
import com.zk.sys.org.service.ZKSysOrgUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;       

/**
 * ZKSysOrgUserController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/org/sysOrgUser")
public class ZKSysOrgUserController extends ZKBaseController {

    @Autowired
    ZKSysOrgUserService sysOrgUserService;
	
    @Autowired
    ZKSysOrgUserOptService sysOrgUserOptService;

    @Autowired
    ZKSysOrgUserEditLogService sysOrgUserEditLogService;

    /********************************************************/
    /** 个人用户注册 不需要用户登录的操作 ****/
    /********************************************************/

    // 邮箱注册
    @RequestMapping(value = "n/sendRegisterMail/{companyCode}", method = RequestMethod.POST)
    public ZKMsgRes registerByMail(@PathVariable(value = "companyCode", required = false) String companyCode,
            @RequestBody ZKSysOrgUser user) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet(true);
        this.sysOrgUserOptService.sendRegisterPersonalUserVerifiyCode(companyCode, user, ZKUserEditFlag.Base.mail, t);
        return ZKMsgRes.asOk();
    }

    // 手机注册
    @RequestMapping(value = "n/sendRegisterPhone/{companyCode}", method = RequestMethod.POST)
    public ZKMsgRes registerByPhone(@PathVariable(value = "companyCode", required = false) String companyCode,
            @RequestBody ZKSysOrgUser user) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet(true);
        this.sysOrgUserOptService.sendRegisterPersonalUserVerifiyCode(companyCode, user, ZKUserEditFlag.Base.phoneNum,
                t);
        return ZKMsgRes.asOk();
    }

    // 提交验证码并注册
    @RequestMapping(value = "n/submitVerifiyCode", method = RequestMethod.POST)
    public ZKMsgRes submitVerifiyCode(@RequestParam(value = "verifiyCode") String verifiyCode) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet();
        ZKSysOrgUser user = this.sysOrgUserOptService.submitRegisterVerifiyCode(verifiyCode, t);
        return ZKMsgRes.asOk(user);
    }

    /********************************************************/
    /** 用户个人操作 ****/
    /********************************************************/
    // 修改邮箱，发送新邮箱的验证码
    @RequestMapping(value = "cm/sendMailVerifiyCode", method = RequestMethod.POST)
    public ZKMsgRes sendNewMailVerifiyCode(@RequestParam("newMail") String newMail) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet(true);
        ZKSysOrgUser sysOrgUser = this.sysOrgUserService.get((String) ZKSecSecurityUtils.getUserId());
        this.sysOrgUserOptService.cmSendMailVerifiyCode(sysOrgUser, newMail, t);
        return ZKMsgRes.asOk();
    }

    // 修改邮箱，校验验证码同时修改邮箱
    @RequestMapping(value = "cm/submitVerifiyCode", method = RequestMethod.POST)
    public ZKMsgRes verifiyMailCode(@RequestParam("verifiyCode") String verifiyCode) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet();
        this.sysOrgUserOptService.cmSubmitVerifiyCode(t, verifiyCode);
        return ZKMsgRes.asOk();
    }

    // 修改手机号，发送新手机验证码
    @RequestMapping(value = "cp/sendPhoneVerifiyCode", method = RequestMethod.POST)
    public ZKMsgRes sendNewPhoneVerifiyCode(@RequestParam("newPhoneNum") String newPhoneNum) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet(true);
        ZKSysOrgUser sysOrgUser = this.sysOrgUserService.get((String) ZKSecSecurityUtils.getUserId());
        this.sysOrgUserOptService.cpSendPhoneVerifiyCode(sysOrgUser, newPhoneNum, t);
        return ZKMsgRes.asOk();
    }

    // 修改手机号，校验手机验证码同时修改手机号
    @RequestMapping(value = "cp/submitVerifiyCode", method = RequestMethod.POST)
    public ZKMsgRes verifiyPhoneCode(@RequestParam("verifiyCode") String verifiyCode) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet();
        this.sysOrgUserOptService.cpSubmitVerifiyCode(t, verifiyCode);
        return ZKMsgRes.asOk();
    }

    // 取当前登录用户的详情
    @RequestMapping(value = "sysOrgUserSelf", method = RequestMethod.GET)
    public ZKMsgRes sysOrgUserSelfGet(@RequestBody ZKSysOrgUser sysOrgUser) {
        String optUserId = ZKSecSecurityUtils.getUserId();
//        ZKSysOrgUserEditLog accountEditLog = this.sysOrgUserEditLogService.getLatestEditLog(optUserId,
//                ZKUserEditType.account);
        return ZKMsgRes.asOk(this.sysOrgUserService.get(optUserId));
    }

    // 编辑：用户本人修改自己的基本信息
    @RequestMapping(value = "sysOrgUserSelf", method = RequestMethod.POST)
    public ZKMsgRes sysOrgUserSelfPost(@RequestBody ZKSysOrgUser sysOrgUser) {
        String optUserId = ZKSecSecurityUtils.getUserId();
        if (optUserId.equals(sysOrgUser.getPkId())) {
            sysOrgUser.setCompanyId(ZKSecSecurityUtils.getCompanyId());
            this.sysOrgUserService.editUserSelf(sysOrgUser, -1, -1);
            return ZKMsgRes.asOk(null, sysOrgUser);
        }
        else {
            log.error("[>_<:20240723-1956-001] zk.sys.010023=您只能修改本人的信息; 操作用户：{}；目标用户{}", optUserId,
                    sysOrgUser.getPkId());
            return ZKMsgRes.asCode("zk.sys.010023");
        }
    }

    /********************************************************/
    /** 用户操作：公司操作 ****/
    /********************************************************/

    // 查询详情
    @RequestMapping(value = "sysOrgUserByPhoneNum", method = RequestMethod.GET)
    public ZKMsgRes sysOrgUserByPhoneNum(@RequestParam("companyId") String companyId,
            @RequestParam("phoneNum") String phoneNum) {
        ZKSysOrgUser sysOrgUser = this.sysOrgUserService.getByPhoneNum(companyId, phoneNum);
        sysOrgUser = this.sysOrgUserService.getDetail(sysOrgUser);
        return ZKMsgRes.asOk(sysOrgUser);
    }

	// 编辑
    @RequestMapping(value = "sysOrgUser", method = RequestMethod.POST)
    public ZKMsgRes sysOrgUserPost(@RequestBody ZKSysOrgUser sysOrgUser) {
        sysOrgUser.setCompanyId(ZKSecSecurityUtils.getCompanyId());
        this.sysOrgUserService.editUserCompanyOpt(sysOrgUser);
        return ZKMsgRes.asOk(sysOrgUser);
	}
	
	// 查询详情
	@RequestMapping(value="sysOrgUser", method = RequestMethod.GET)
	public ZKMsgRes sysOrgUserGet(@RequestParam("pkId") String pkId){
//        ZKSysOrgUser sysOrgUser = this.sysOrgUserService.getDetail(pkId);
        ZKSysOrgUser sysOrgUser = this.sysOrgUserService.get(new ZKSysOrgUser(pkId));
        return ZKMsgRes.asOk(sysOrgUser);
	}
	
	// 分页查询 
    @RequestMapping(value = "sysOrgUsersPage", method = RequestMethod.GET)
    public ZKMsgRes sysOrgUsersPageGet(ZKSysOrgUser sysOrgUser,
            HttpServletRequest hReq, HttpServletResponse hRes) {
        sysOrgUser.setCompanyId(ZKSecSecurityUtils.getCompanyId());
		ZKPage<ZKSysOrgUser> resPage = ZKPage.asPage(hReq);
        resPage = this.sysOrgUserService.findPage(resPage, sysOrgUser);
        return ZKMsgRes.asOk(resPage);
	}
	
	// 批量删除
	@RequestMapping(value="sysOrgUser", method = RequestMethod.DELETE)
	@ResponseBody
	public ZKMsgRes sysOrgUserDel(@RequestParam("pkId[]") String[] pkIds){
        int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.sysOrgUserService.del(new ZKSysOrgUser(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
	}

}
