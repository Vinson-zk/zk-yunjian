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
 * @Title: ZKSerCenLoginControllerMockTest.java 
 * @author Vinson 
 * @Package com.zk.server.central.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 27, 2019 10:32:21 AM 
 * @version V1.0   
*/
package com.zk.server.central.controller;

import java.util.Arrays;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;

import com.zk.core.commons.ZKLanguageType;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.web.utils.ZKCookieUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.server.central.ZKSerCenSpringBootMain;
import com.zk.server.central.commons.ZKSerCenConstants;
import com.zk.server.central.security.ZKSerCenAuthenticationFilter;

import jakarta.servlet.http.Cookie;
import junit.framework.TestCase;

/** 
* @ClassName: ZKSerCenLoginControllerMockTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ZKSerCenSpringBootMain.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk.ser.cen" })
@AutoConfigureMockMvc
public class ZKSerCenLoginControllerMockTest {

    static {
        try {
            ZKSerCenSpringBootMain.exit();
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Autowired
    private MockMvc mvc;

    @Value("${zk.path.admin}")
    private String adminPath;

    @Value("${zk.path.serCen}")
    private String modulePath;

    private static String baseUrl = "";

    @Before
    public void step() {
        baseUrl = String.format("/%s/%s/l", adminPath, modulePath);
//        baseUrl = String.format("/%s/%s/file", ZKEnvironmentUtils.getString("zk.path.admin", "zk"),
//                ZKEnvironmentUtils.getString("zk.path.core", "c"));
        System.out.println("[^_^:20191220-1719-001] baseUrl: " + baseUrl);
    }

    /**
     * 测试：登录认证视图；
     *
     * @Title: testLogin
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 29, 2019 2:39:17 PM
     * @return void
     */
    @Test
    public void testLoginView() {

        try {
            MockHttpServletRequestBuilder mockReqBuilder = null;
            ResultActions resultActions = null;
            MvcResult mvcResult = null;
            ModelAndView mv = null;
            String localeStr = "";

            String url = baseUrl + "/login";

            localeStr = "en-US";
            /*** 登录视图 ***/
            mockReqBuilder = MockMvcRequestBuilders.get(url);
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            resultActions.andExpect(MockMvcResultMatchers.view().name("login"));
            resultActions.andExpect(
                    MockMvcResultMatchers.model().attribute(ZKWebUtils.Locale_Flag_In_Header, new Matcher<Object>() {
                        @Override
                        public void describeTo(Description description) {
                        }

                        @Override
                        public boolean matches(Object item) {
                            return item == null ? false : "en-US".equals(item.toString());
                        }

                        @Override
                        public void describeMismatch(Object item, Description mismatchDescription) {
                        }

                        @Override
                        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
                        }
                    }));

            mvcResult = resultActions.andReturn();
            mv = mvcResult.getModelAndView();

            TestCase.assertEquals(localeStr, mv.getModel().get(ZKWebUtils.Locale_Flag_In_Header));
            TestCase.assertEquals(null, mv.getModel().get(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_ACCOUNT));
            TestCase.assertEquals(null,
                    mv.getModel().get(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT));
            TestCase.assertEquals(false, mv.getModel().get(ZKSerCenConstants.ParamKey.param_isCaptcha));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {

        }
    }

    /**
     * 测试：登录认证逻辑
     *
     * @Title: testLogin
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 29, 2019 2:39:17 PM
     * @return void
     */
    @Test
    public void testLogin() {

        try {
            MockHttpServletRequestBuilder mockReqBuilder = null;
            ResultActions resultActions = null;
            MvcResult mvcResult = null;
            MockHttpServletResponse mockRes = null;
            ZKMsgRes zkMsgRes = null;

            String url = baseUrl + "/login";

            /***
             * zk.ser.cen.000012 账号不能为空
             * 
             * 不上传账号，或上传空账号
             ***/
            // 默认 ZKLanguageType.en_US 请求
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            mvcResult = resultActions.andReturn();
            mockRes = mvcResult.getResponse();
            zkMsgRes = ZKJsonUtils.parseObject(mockRes.getContentAsString(), ZKMsgRes.class);
            System.out.println("[^_^:20191220-1141-001] " + ZKJsonUtils.toJsonStr(zkMsgRes));
            TestCase.assertEquals("zk.ser.cen.000012", zkMsgRes.getCode());
            TestCase.assertEquals("The account cannot be empty.", zkMsgRes.getMsg());

            // ZKLanguageType.zh_CN 请求
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            mockReqBuilder.header(ZKWebUtils.Locale_Flag_In_Header, ZKLanguageType.zh_CN.getKey());
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            mvcResult = resultActions.andReturn();
            mockRes = mvcResult.getResponse();
            zkMsgRes = ZKJsonUtils.parseObject(mockRes.getContentAsString(), ZKMsgRes.class);
            System.out.println("[^_^:20191220-1141-002] " + ZKJsonUtils.toJsonStr(zkMsgRes));
            TestCase.assertEquals("zk.ser.cen.000012", zkMsgRes.getCode());
            TestCase.assertEquals("账号不能为空。", zkMsgRes.getMsg());

            /***
             * zk.ser.cen.000014 账号不存在。
             * 
             * 上传不存在的账号
             ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_ACCOUNT, "errorAccount");
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            mvcResult = resultActions.andReturn();
            mockRes = mvcResult.getResponse();
            zkMsgRes = ZKJsonUtils.parseObject(mockRes.getContentAsString(), ZKMsgRes.class);
            System.out.println("[^_^:20191220-1141-003] " + ZKJsonUtils.toJsonStr(zkMsgRes));
            TestCase.assertEquals("zk.ser.cen.000014", zkMsgRes.getCode());
            TestCase.assertEquals("Account not found.", zkMsgRes.getMsg());

            /***
             * zk.ser.cen.000013 密码错误
             * 
             * 上传错误密码
             ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_ACCOUNT, "admin");
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_PASSWORD, "errorPassword");
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            mvcResult = resultActions.andReturn();
            mockRes = mvcResult.getResponse();
            zkMsgRes = ZKJsonUtils.parseObject(mockRes.getContentAsString(), ZKMsgRes.class);
            System.out.println("[^_^:20191220-1141-004] " + ZKJsonUtils.toJsonStr(zkMsgRes));
            TestCase.assertEquals("zk.ser.cen.000013", zkMsgRes.getCode());
            TestCase.assertEquals("Password error.", zkMsgRes.getMsg());

            Cookie rememberMeCookie;
            Cookie rememberAccountCookie;
            Cookie[] resCookies;

            /***
             * 登录成功；但不记住账号和不记住我。默认
             ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_ACCOUNT, "admin");
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_PASSWORD, "admin");
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            mvcResult = resultActions.andReturn();
            mockRes = mvcResult.getResponse();
            zkMsgRes = ZKJsonUtils.parseObject(mockRes.getContentAsString(), ZKMsgRes.class);
            System.out.println("[^_^:20191220-1141-004] " + ZKJsonUtils.toJsonStr(zkMsgRes));
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            TestCase.assertEquals("Successful.", zkMsgRes.getMsg());
            rememberAccountCookie = mockRes.getCookie(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT);
            TestCase.assertEquals("", rememberAccountCookie.getValue());
            rememberMeCookie = mockRes.getCookie(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ME);
            TestCase.assertEquals("deleteMe", rememberMeCookie.getValue());

            /***
             * 登录成功；但不记住账号和不记住我。
             ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_ACCOUNT, "admin");
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_PASSWORD, "admin");
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT, "false");
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ME, "false");
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            mvcResult = resultActions.andReturn();
            mockRes = mvcResult.getResponse();
            zkMsgRes = ZKJsonUtils.parseObject(mockRes.getContentAsString(), ZKMsgRes.class);
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            TestCase.assertEquals("Successful.", zkMsgRes.getMsg());
            rememberAccountCookie = mockRes
                    .getCookie(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT);
            TestCase.assertEquals("", rememberAccountCookie.getValue());
            rememberMeCookie = mockRes
                    .getCookie(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ME);
            TestCase.assertEquals("deleteMe", rememberMeCookie.getValue());

            /***
             * 登录成功；记住账号，和记住我
             ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_ACCOUNT, "admin");
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_PASSWORD, "admin");
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT, "true");
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ME, "true");
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            mvcResult = resultActions.andReturn();
            mockRes = mvcResult.getResponse();
            zkMsgRes = ZKJsonUtils.parseObject(mockRes.getContentAsString(), ZKMsgRes.class);
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            TestCase.assertEquals("Successful.", zkMsgRes.getMsg());
            resCookies = mockRes.getCookies();
            System.out.println(
                    "[^_^:20191221-0027-001] resCookies：" + ZKCookieUtils.formatCookies(Arrays.asList(resCookies)));
            rememberAccountCookie = ZKCookieUtils.getCookie(resCookies,
                    ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT, null);
            TestCase.assertEquals("admin", rememberAccountCookie.getValue());
            rememberMeCookie = ZKCookieUtils.getCookie(resCookies,
                    ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ME, null);
            TestCase.assertNotNull(rememberMeCookie);
            TestCase.assertFalse("deleteMe".equals(rememberMeCookie.getValue()));

            String indexUrl = "/" + this.adminPath + "/" + this.modulePath;

            /***
             * 使用记住我 cookie 登录成功
             ***/
            // 不使用记住我访问需要登录的接口，转发
            mockReqBuilder = MockMvcRequestBuilders.post(indexUrl);
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is3xxRedirection());

            // 使用记住我访问需要登录的接口，成功
            mockReqBuilder = MockMvcRequestBuilders.post(indexUrl);
            System.out.println("[^_^:20191221-0028-001] rememberMeCookie：" + rememberMeCookie.getValue());
            mockReqBuilder.cookie(rememberMeCookie);
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            resultActions.andExpect(MockMvcResultMatchers.view().name("index"));
            mvcResult = resultActions.andReturn();
            mockRes = mvcResult.getResponse();
            TestCase.assertNull(mockRes.getCookie(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ME));

            /***
             * 使用记住我 cookie，登录视图跳转到首页
             ***/
            mockReqBuilder = MockMvcRequestBuilders.get(url);
            mockReqBuilder.cookie(rememberMeCookie);
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
            resultActions.andExpect(MockMvcResultMatchers.redirectedUrl(indexUrl));

            /***
             * 登录失败，记住的账号不会被删除；
             ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            System.out.println("[^_^:20191221-0028-001] rememberAccountCookie：" + rememberAccountCookie.getValue());
            mockReqBuilder.cookie(rememberAccountCookie);
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            mvcResult = resultActions.andReturn();
            mockRes = mvcResult.getResponse();
            rememberAccountCookie = ZKCookieUtils.getCookie(resCookies,
                    ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT, null);
            TestCase.assertEquals("admin", rememberAccountCookie.getValue());


            /***
             * 不记住账号登录失败，记住的账号被删除；
             ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT, "false");
            mockReqBuilder.header(HttpHeaders.COOKIE, ZKCookieUtils.formatCookie(rememberAccountCookie));
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            mvcResult = resultActions.andReturn();
            mockRes = mvcResult.getResponse();
            TestCase.assertEquals("",
                    mockRes.getCookie(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT).getValue());

            /***
             * 不记住账号登录成功，记住的账号被删除；
             ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_ACCOUNT, "admin");
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_PASSWORD, "admin");
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT, "false");
            mockReqBuilder.header(HttpHeaders.COOKIE, ZKCookieUtils.formatCookie(rememberAccountCookie));
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            mvcResult = resultActions.andReturn();
            mockRes = mvcResult.getResponse();
            TestCase.assertEquals("",
                    mockRes.getCookie(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT).getValue());

            /***
             * 使用记住账号 cookie，登录视图中包含登录账号
             ***/
            mockReqBuilder = MockMvcRequestBuilders.get(url);
            mockReqBuilder.cookie(rememberAccountCookie);
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            mvcResult = resultActions.andReturn();
            TestCase.assertEquals(rememberAccountCookie.getValue(),
                    mvcResult.getModelAndView().getModel().get(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_ACCOUNT));
            TestCase.assertEquals(true, mvcResult.getModelAndView().getModel()
                    .get(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_REMEMBER_ACCOUNT));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {

        }
    }


}
