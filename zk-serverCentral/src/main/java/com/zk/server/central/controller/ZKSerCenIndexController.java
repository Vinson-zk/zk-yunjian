/**   
d * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKSerCenIndexController.java 
 * @author Vinson 
 * @Package com.zk.server.central.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:17:14 PM 
 * @version V1.0   
*/
package com.zk.server.central.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleContextResolver;

import com.zk.core.commons.ZKLanguageType;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.server.central.controller.base.ZKSerCenBaseController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
* @ClassName: ZKSerCenIndexController 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
//@RestController // 使用 字符串 返回 ModelAndView 路径时，不能使用 @RestController 因为 @RestController 包含了 @ResponseBody
@Controller
@RequestMapping("${zk.path.admin}/${zk.path.serCen}")
public class ZKSerCenIndexController extends ZKSerCenBaseController {

    @Autowired
    private LocaleContextResolver localeResolver;
//    private SessionLocaleResolver localeResolver;

    public static final String msg = "欢迎来到 ";

    @RequestMapping("anon")
    @ResponseBody
    public String welcome() {
        return "index: " + msg + ZKEnvironmentUtils.getString("spring.application.name", "zk server central");
    }

    @RequestMapping("user")
    @ResponseBody
    public String user() {
        return "user: " + msg + ZKEnvironmentUtils.getString("spring.application.name", "zk server central");
    }

    @RequestMapping({ "", "index" })
    public String index(@RequestParam(value = "theme", required = false) String theme, HttpServletRequest req,
            Model model) {

        // model.addAttribute("locale",
        // localeResolver.resolveLocale(req).toLanguageTag());
//        mv.addObject("locale", req.getLocale().toLanguageTag());
        if (!ZKStringUtils.isEmpty(theme)) {
            model.addAttribute("theme", theme);
        }
        return "index";
    }

    @RequestMapping("locale")
    public void locale(@RequestParam(value = "locale") String language, HttpServletRequest req,
            HttpServletResponse rep) {

        Locale locale = null;
        switch (ZKLanguageType.parseKey(language)) {
            case zh_TW:
                locale = Locale.TRADITIONAL_CHINESE;
                break;
            case zh:
                locale = Locale.CHINESE;
                break;
            case en_US:
                locale = Locale.US;
                break;
            case en:
                locale = Locale.ENGLISH;
                break;
            default:
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
        }
        localeResolver.setLocale(req, rep, locale);
//        return "redirect:/";
    }



    @RequestMapping(value = "data")
    @ResponseBody
    public ZKMsgRes data(HttpServletRequest req) {
        try {
            Thread.sleep(1500);
        }
        catch(InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        ZKMsgRes zkMsgRes = ZKMsgRes.asOk();
        zkMsgRes.setData(msg);
        return zkMsgRes;
    }

}
