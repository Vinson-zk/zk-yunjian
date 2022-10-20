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
 * @Title: ZKSerCenIndexControllerMockMvcTest.java 
 * @author Vinson 
 * @Package com.zk.server.central.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:32:02 PM 
 * @version V1.0   
*/
package com.zk.server.central.controller;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;

import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.server.central.ZKSerCenSpringBootMain;
import com.zk.server.central.security.ZKSerCenAuthenticationFilter;

import junit.framework.TestCase;

/**
 * @ClassName: ZKSerCenIndexControllerMockMvcTest
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ZKSerCenSpringBootMain.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk.ser.cen" })
@AutoConfigureMockMvc
public class ZKSerCenIndexControllerMockMvcTest {

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
        baseUrl = String.format("/%s/%s", adminPath, modulePath);
//        baseUrl = String.format("/%s/%s/file", ZKEnvironmentUtils.getString("zk.path.admin", "zk"),
//                ZKEnvironmentUtils.getString("zk.path.core", "c"));
        System.out.println("[^_^:20191220-1719-001] baseUrl: " + baseUrl);
    }

    @Test
    public void testAnon() {

        try {
            MockHttpServletRequestBuilder mockReqBuilder = null;
            ResultActions resultActions = null;
            String resStr = null;
            String url = baseUrl + "/anon";

            String msg = ZKSerCenIndexController.msg
                    + ZKEnvironmentUtils.getString("spring.application.name", "zk server central");

            mockReqBuilder = MockMvcRequestBuilders.get(url);
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().isOk());
            resStr = resultActions.andReturn().getResponse().getContentAsString();
            TestCase.assertEquals(msg, resStr);

            mockReqBuilder = MockMvcRequestBuilders.post(url);
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().isOk());
            resStr = resultActions.andReturn().getResponse().getContentAsString();
            TestCase.assertEquals(msg, resStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testIndex() {

        try {
            MockHttpServletRequestBuilder mockReqBuilder = null;
            ResultActions resultActions = null;
            MvcResult mvcResult = null;
            ModelAndView mv = null;
//            MockHttpServletResponse mockRes = null;

            String url = baseUrl;

            /*** 未登录，转发 ***/
            url = baseUrl;
            mockReqBuilder = MockMvcRequestBuilders.get(url);
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is3xxRedirection());

            /*** 登录 ***/
            // 登录
            String user = "admin", password = "admin";
            MockHttpSession session = null;
            url = baseUrl + "/l/login";
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_ACCOUNT, user);
            mockReqBuilder.param(ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_PASSWORD, password);
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
            mvcResult = resultActions.andReturn();
            session = (MockHttpSession) mvcResult.getRequest().getSession();
            TestCase.assertNotNull(session);

            url = baseUrl;
            mockReqBuilder = MockMvcRequestBuilders.get(url);
            mockReqBuilder.session(session);
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().isOk());
            resultActions.andExpect(MockMvcResultMatchers.view().name("index"));
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
            TestCase.assertEquals(null, mv.getModel().get("theme"));
            TestCase.assertEquals("en-US", mv.getModel().get(ZKWebUtils.Locale_Flag_In_Header));

            mockReqBuilder = MockMvcRequestBuilders.post(url);
            mockReqBuilder.session(session);
            mockReqBuilder.param("theme", "theme-vinson");
            resultActions = mvc.perform(mockReqBuilder);
            resultActions.andExpect(MockMvcResultMatchers.status().isOk());
            resultActions.andExpect(MockMvcResultMatchers.view().name("index"));

            mvcResult = resultActions.andReturn();
            mv = mvcResult.getModelAndView();
            TestCase.assertEquals("theme-vinson", mv.getModel().get("theme"));
            TestCase.assertEquals("en-US", mv.getModel().get(ZKWebUtils.Locale_Flag_In_Header));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
