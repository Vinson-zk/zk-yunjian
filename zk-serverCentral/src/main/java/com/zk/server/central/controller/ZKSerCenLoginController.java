/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKSerCenLoginController.java 
 * @author Vinson 
 * @Package com.zk.server.central.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 27, 2019 9:59:35 AM 
 * @version V1.0   
*/
package com.zk.server.central.controller;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.exception.base.ZKCodeException;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.utils.ZKValidateCodeUtils;
import com.zk.core.utils.ZKValidateCodeUtils.ZKImgUtils;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.security.principal.ZKSecUserPrincipal;
import com.zk.server.central.commons.ZKSerCenConstants;
import com.zk.server.central.commons.ZKSerCenUtils;
import com.zk.server.central.controller.base.ZKSerCenBaseController;
import com.zk.server.central.security.ZKSerCenAuthenticationFilter;
import com.zk.server.central.security.ZKSerCenSecurityUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
* @ClassName: ZKSerCenLoginController 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Controller
@RequestMapping("${zk.path.admin}/${zk.path.serCen}/l")
public class ZKSerCenLoginController extends ZKSerCenBaseController {

    @Value("${zk.ser.cen.auth.login.fail.max.num}")
    private int maxLoginFailNum;

    @Value("${zk.ser.cen.auth.captcha.validity.time}")
    private long captchaValidityTime;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest hReq, HttpServletResponse hRes, Model model)
            throws NoSuchAlgorithmException {
//        System.out.println("============= " + hReq.getRequestURI());
        ZKSecUserPrincipal<String> zkPrincipal = ZKSerCenSecurityUtils.getPrincipal();
        if (zkPrincipal != null) {
            // 如果已经登录，则跳转到首页
            ZKServletUtils.redirectUrl(hReq, hRes, "/" + this.adminPath + "/" + this.modulePath);
            return null;
        }

        /*** 用户登录账号参数 ***/
        Cookie rememberAccountCookie = null;
        // 如果没有上送 account，则从请求 cookie 中取；
        rememberAccountCookie = ZKServletUtils.getCookieByRequest(hReq,
                ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT);
        if(rememberAccountCookie != null) {
            model.addAttribute(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_ACCOUNT, rememberAccountCookie.getValue());
            /***
             * 用户是否记住了账号；注：如果上一次请求是记住了账号，刚需要一次不记住账号的请求后，才会消除记住的账号；
             ***/
            // 并设置用户记住了账号;
            model.addAttribute(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT, true);
        }

        /*** 用户是否记住我 ***/
//        boolean rememberMe = ZKWebUtils.isTrue(hReq, ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ME);
//        model.addAttribute(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ME, rememberMe);

        /*** 是否需要验证码处理 ***/
        boolean isCaptcha = ZKSerCenUtils.isNeedCaptcha(maxLoginFailNum); // 允许最大失败次数；
        model.addAttribute(ZKSerCenConstants.ParamKey.param_isCaptcha, isCaptcha);

        /*** 用户 SecretKey 一些接口中的特殊字段加密处理； ***/
        ZKSerCenUtils.setUserSecretKey(hReq);

        log.info("[^_^:20200224-0810-001] AES userSecretKey: {}",
                hReq.getSession().getAttribute(ZKSerCenConstants.ParamKey.userSecretKey));

        /*** 设置请求标记 ***/
        ZKServletUtils.setRequestSign(hReq);

        return new ModelAndView("login");

//        ModelAndView mv = new ModelAndView("login");
//        return mv;
    }

    /**
     * 登录失败，真正登录的POST请求由Filter完成
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ZKMsgRes loginPost(HttpServletRequest hReq, HttpServletResponse hRes, Model model,
            RedirectAttributes attr) {

//        // 测试修改时间 loading
//        try {
//            Thread.sleep(3 * 1000);
//        }
//        catch(InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        // 返回的数据；
        Map<String, Object> data = new HashMap<String, Object>();

        ZKSecUserPrincipal<String> zkPrincipal = ZKSerCenSecurityUtils.getPrincipal();
        if (zkPrincipal != null) {
            String __url = "/" + this.adminPath + "/" + this.modulePath;
            // 如果已经登录，则跳转到指定页面
            data.put(ZKSerCenConstants.ParamKey.__url, __url);
            return ZKMsgRes.asOk(data);
//            ZKWebUtils.redirectUrl(hReq, hRes, "/" + this.adminPath + "/" + this.modulePath);
//            return null;
        }

//      // 登录操作如果是Ajax操作，直接返回登录信息字符串。
//      if (ZKWebUtils.isAjaxRequest(hReq)) {
//          return ZKWebUtils.renderObject(hRes, zkMsgE == null ? "" : zkMsgE);
//      }
//        Session session = ZKSerCenSecurityUtils.getSession();

        /*** 登录失败次数+1 ***/
        ZKSerCenUtils.addLoginFailNum(1);
        
        boolean isCaptcha = ZKSerCenUtils.isNeedCaptcha(maxLoginFailNum); // 允许最大失败次数；
        data.put(ZKSerCenConstants.ParamKey.param_isCaptcha, isCaptcha);

        ZKCodeException zkE = (ZKCodeException) hReq
                .getAttribute(ZKSerCenAuthenticationFilter.ZKAuthKeys.KEY_EXCEPTION);

        if (zkE != null) {
            zkE.setData(data);
            throw zkE;
        }
        // 未知道原因，登录失败；
        return ZKMsgRes.asSysErr(data);


//        /*** 下面是 ModelAndView 重定向跳转 ***/
//        /*** 参数 ***/
//        ModelAndView mv = new ModelAndView("redirect:login");
//        // 将 model 中的参数转移到 mv 中；
//        mv.addAllObjects(model.asMap());
//        return mv;

//        /*** 下面是 ViewResolver 重定向跳转 ***/
//        /**
//         * addAttribute 跳转地址带上test参数:
//         * 
//         * 使用RedirectAttributes的addAttribute方法传递参数会跟随在URL后面，如上代码即为http:/index.action?account=account
//         * 
//         * addFlashAttribute 跳转地址不带上u2参数:
//         * 
//         * 使用addFlashAttribute不会跟随在URL后面，会把该参数值暂时保存于session，待重定向url获取该参数后从session中移除，
//         * 这里的redirect必须是方法映射路径，jsp无效。你会发现redirect后的jsp页面中b只会出现一次，刷新后b再也不会出现了，这验证了上面说的，
//         * 被访问后就会从session中移除。对于重复提交可以使用此来完成.
//         */
//        // spring mvc 设置下 RequestMappingHandlerAdapter
//        // 的ignoreDefaultModelOnRedirect=true,这样可以提高效率，避免不必要的检索。
//        // 将 model 中的参数转移到 addAttribute 中；
//        attr.addAllAttributes(model.asMap());
//        // 下面两种写法都可以
//        return "redirect:login";
////      return "redirect:/" + this.adminPath + "/" + this.modulePath + "/login";

    }

    @RequestMapping(value = "captcha", method = RequestMethod.GET)
    public void validateCode(HttpServletRequest hReq, HttpServletResponse hRes) {

        hRes.setHeader("Pragma", "no-cache");
        hRes.setHeader("Cache-Control", "no-cache");
        hRes.setDateHeader("Expires", 0);

        String captcha = ZKValidateCodeUtils.genVerifyCode(5); // 5位验证码
        ZKSerCenUtils.putCaptcha(captcha, captchaValidityTime); // 有效时长
//        ZKEnvironmentUtils.getString("zk.validate.code.font", "Arial")
        BufferedImage vcImg = ZKImgUtils.genValidateCodeImg(captcha);

        OutputStream os = null;
        try {
            os = hRes.getOutputStream();
            ZKImgUtils.write(vcImg, os);
        }
        catch(Exception e) {
            log.error("[>_<:20191015-1627-001] 验证码图片响应失败：{}", captcha);
            e.printStackTrace();
        } finally {
            ZKStreamUtils.closeStream(os);
        }
    }

    @RequestMapping(value = "doingCaptcha")
    @ResponseBody
    public boolean doingCaptcha(HttpServletRequest hReq, HttpServletResponse hRes) {

//        ZKMsgRes zkMsgRes = new ZKMsgRes("0");

        if (ZKSerCenUtils.checkCaptcha(hReq, maxLoginFailNum)) {
            // 验证验证码成功
            ZKSerCenUtils.cleanLoginFailNum();
            return true;
        }
        else {
            // 验证验证码失败，更新验证
//            zkMsgRes.setCode("3");
            return false;
        }
//        return zkMsgRes;
    }

}
