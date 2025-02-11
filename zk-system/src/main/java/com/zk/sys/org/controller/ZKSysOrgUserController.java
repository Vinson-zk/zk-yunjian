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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.utils.ZKUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.token.ZKSecAuthenticationToken;
import com.zk.security.token.ZKSecDefaultAuthcUserToken;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.security.web.filter.ZKSecAuthenticationFilter;
import com.zk.security.web.support.servlet.wrapper.ZKSecHttpServletRequestWrapper;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.entity.ZKSysOrgUserOptLog;
import com.zk.sys.org.entity.ZKSysOrgUserOptLog.ZKUserOptTypeFlag;
import com.zk.sys.org.service.ZKSysOrgUserOptLogService;
import com.zk.sys.org.service.ZKSysOrgUserOptService;
import com.zk.sys.org.service.ZKSysOrgUserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;       

/**
 * ZKSysOrgUserController
 * @author 
 * @version 
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.org}/sysOrgUser")
public class ZKSysOrgUserController extends ZKBaseController {

    @Autowired
    ZKSysOrgUserService sysOrgUserService;
	
    @Autowired
    ZKSysOrgUserOptService sysOrgUserOptService;

    @Autowired
    ZKSysOrgUserOptLogService sysOrgUserOptLogService;

    @Value("${zk.path.admin}")
    String pathAdmin;

    @Value("${zk.path.sys}")
    String pathSys;

    @Value("${zk.sys.version}")
    String pathVersion;

    /********************************************************/
    /** 个人用户注册 不需要用户登录的操作 ****/
    /********************************************************/

    // 邮箱注册
    @RequestMapping(value = { "n/sendRegisterMail/{companyCode}", "n/sendRegisterMail" }, method = RequestMethod.POST)
    public ZKMsgRes registerByMail(@PathVariable(value = "companyCode", required = false) String companyCode,
            @RequestBody ZKSysOrgUser user) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet(true);
        user = this.sysOrgUserOptService.sendRegisterPersonalUserVerifyCode(companyCode, user,
                ZKUserOptTypeFlag.Base.mail, t);
        return ZKMsgRes.asOk(user);
    }

    // 手机注册
    @RequestMapping(value = { "n/sendRegisterPhone/{companyCode}", "n/sendRegisterPhone" }, method = RequestMethod.POST)
    public ZKMsgRes registerByPhone(@PathVariable(value = "companyCode", required = false) String companyCode,
            @RequestBody ZKSysOrgUser user) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet(true);
        user = this.sysOrgUserOptService.sendRegisterPersonalUserVerifyCode(companyCode, user,
                ZKUserOptTypeFlag.Base.phoneNum, t);
        return ZKMsgRes.asOk(user);
    }

    // againFlag: 1-邮箱验证码；2-手机验证码；
    @RequestMapping(value = "n/sendVerifyCodeAgain")
    public ZKMsgRes sendVerifyCodeAgain(@RequestParam("againFlag") int againFlag) {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
        this.sysOrgUserOptService.sendVerifyCodeAgainByUser(tk, againFlag);
        return ZKMsgRes.asOk();
    }

    // 提交验证码并注册
    @RequestMapping(value = "n/submitVerifyCode", method = RequestMethod.POST)
    public void submitVerifyCode(@RequestParam(value = "verifyCode") String verifyCode, HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet();
        ZKSysOrgUser user = this.sysOrgUserOptService.submitRegisterVerifyCode(verifyCode, t, req);

        // 转到登录
        ZKSecHttpServletRequestWrapper zkSecReq = new ZKSecHttpServletRequestWrapper(req);
        ZKSecSubject subject = ZKSecSecurityUtils.getSubject();

        String companyCode = user.getCompanyCode();
        long osType = ZKSecAuthenticationFilter.getOsType(zkSecReq);
        String udid = ZKSecAuthenticationFilter.getUdid(zkSecReq);
        long appType = ZKSecAuthenticationFilter.getAppType(zkSecReq);
        String appId = ZKSecAuthenticationFilter.getAppId(zkSecReq);

        String username = user.getAccount();

        boolean rememberMe = ZKUtils.isTrue(zkSecReq.getCleanParam(ZKSecConstants.PARAM_NAME.RememberMe));
        String pwd = user.getPassword();

        ZKSecAuthenticationToken token = new ZKSecDefaultAuthcUserToken(companyCode, username, pwd, rememberMe, osType,
                udid, appType, appId);
        subject.login(token);

        RequestDispatcher fd = req.getRequestDispatcher(
                String.format("/%s/%s/%s/sec/login", this.pathAdmin, this.pathSys, this.pathVersion));
        fd.forward(req, res);
//        return ZKMsgRes.asOk(user);
    }

    /********************************************************/
    /** 用户个人操作 ****/
    /********************************************************/
    // 修改邮箱，发送新邮箱的验证码
    @RequestMapping(value = "cm/sendVerifyCode", method = RequestMethod.POST)
    public ZKMsgRes sendNewMailVerifyCode(@RequestParam("newMail") String newMail) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet();
        ZKSysOrgUser sysOrgUser = this.sysOrgUserService.get((String) ZKSecSecurityUtils.getUserId());
        this.sysOrgUserOptService.cmSendMailVerifyCode(sysOrgUser, newMail, t);
        return ZKMsgRes.asOk();
    }

    // 修改邮箱，校验验证码同时修改邮箱
    @RequestMapping(value = "cm/submitVerifyCode", method = RequestMethod.POST)
    public ZKMsgRes verifiyMailCode(@RequestParam("verifyCode") String verifyCode, HttpServletRequest req) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet();
        this.sysOrgUserOptService.cmSubmitVerifyCode(t, verifyCode, req);
        return ZKMsgRes.asOk();
    }

    // 修改手机号，发送新手机验证码
    @RequestMapping(value = "cp/sendVerifyCode", method = RequestMethod.POST)
    public ZKMsgRes sendNewPhoneVerifyCode(@RequestParam("newPhoneNum") String newPhoneNum) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet();
        ZKSysOrgUser sysOrgUser = this.sysOrgUserService.get((String) ZKSecSecurityUtils.getUserId());
        this.sysOrgUserOptService.cpSendPhoneVerifyCode(sysOrgUser, newPhoneNum, t);
        return ZKMsgRes.asOk();
    }

    // 修改手机号，校验手机验证码同时修改手机号
    @RequestMapping(value = "cp/submitVerifyCode", method = RequestMethod.POST)
    public ZKMsgRes verifiyPhoneCode(@RequestParam("verifyCode") String verifyCode, HttpServletRequest req) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet();
        this.sysOrgUserOptService.cpSubmitVerifyCode(t, verifyCode, req);
        return ZKMsgRes.asOk();
    }

    // 重新发送验证码；againFlag: 1-邮箱验证码；2-手机验证码；
    @RequestMapping(value = "c/sendVerifyCodeAgain")
    public ZKMsgRes cSendVerifyCodeAgain(@RequestParam("againFlag") int againFlag) {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
        this.sysOrgUserOptService.sendVerifyCodeAgainByUser(tk, againFlag);
        return ZKMsgRes.asOk();
    }

    // 修改账号
    @RequestMapping(value = "changeAccountSelf", method = RequestMethod.POST)
    public ZKMsgRes changeAccountSelfPost(@RequestParam("newAccount") String newAccount, HttpServletRequest req) {
        String optUserId = ZKSecSecurityUtils.getUserId();
        ZKSysOrgUser sysOrgUser = this.sysOrgUserService.get(optUserId);
        this.sysOrgUserService.updateAccount(sysOrgUser, newAccount, ZKUserOptTypeFlag.Account.self, req);
        return ZKMsgRes.asOk();
    }

    // 取当前登录用户的详情
    @RequestMapping(value = "sysOrgUserSelf", method = RequestMethod.GET)
    public ZKMsgRes sysOrgUserSelfGet(@RequestBody ZKSysOrgUser sysOrgUser) {
        String optUserId = ZKSecSecurityUtils.getUserId();
        return ZKMsgRes.asOk(this.sysOrgUserService.get(optUserId));
    }

    // 编辑：用户本人修改自己的基本信息
    @RequestMapping(value = "sysOrgUserSelf", method = RequestMethod.POST)
    public ZKMsgRes sysOrgUserSelfPost(@RequestBody ZKSysOrgUser sysOrgUser, HttpServletRequest req) {
        String optUserId = ZKSecSecurityUtils.getUserId();
        if (optUserId.equals(sysOrgUser.getPkId())) {
            sysOrgUser.setCompanyId(ZKSecSecurityUtils.getCompanyId());
            this.sysOrgUserService.editUserSelf(sysOrgUser, -1, -1, req);
            return ZKMsgRes.asOk(null, sysOrgUser);
        }
        else {
            log.error("[>_<:20240723-1956-001] zk.sys.010023=您只能修改本人的信息; 操作用户：{}；目标用户{}", optUserId,
                    sysOrgUser.getPkId());
            return ZKMsgRes.asCode("zk.sys.010023");
        }
    }

    // 注销账号
    @RequestMapping(value = "closeAccount", method = RequestMethod.POST)
    public ZKMsgRes closeAccount() {
        String optUserId = ZKSecSecurityUtils.getUserId();
        ZKSysOrgUser sysOrgUser = this.sysOrgUserService.get(optUserId);
        return ZKMsgRes.asOk(this.sysOrgUserService.closeAccount(sysOrgUser));
    }

    // 登录日志
    @RequestMapping(value = "loginRecords", method = RequestMethod.POST)
    public ZKMsgRes loginRecords(ZKSysOrgUserOptLog sysOrgUserOptLog, HttpServletRequest hReq) {
        ZKPage<ZKSysOrgUserOptLog> resPage = ZKPage.asPage(hReq);
        sysOrgUserOptLog.setOptType(ZKSysOrgUserOptLog.ZKUserOptType.login);
        resPage = this.sysOrgUserOptLogService.findPage(resPage, sysOrgUserOptLog);
        return ZKMsgRes.asOk(resPage);
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
    public ZKMsgRes sysOrgUserPost(@RequestBody ZKSysOrgUser sysOrgUser, HttpServletRequest req) {
        sysOrgUser.setCompanyId(ZKSecSecurityUtils.getCompanyId());
        this.sysOrgUserService.editUserCompanyOpt(sysOrgUser, req);
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
