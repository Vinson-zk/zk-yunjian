///** 
//* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
//* address: 
//* All rights reserved. 
//* 
//* This software is the confidential and proprietary information of 
//* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
//* disclose such Confidential Information and shall use it only in 
//* accordance with the terms of the license agreement you entered into 
//* with ZK-Vinson. 
//*
//* @Title: ZKLogIndexController.java 
//* @author Vinson 
//* @Package com.zk.log.controller 
//* @Description: TODO(simple description this file what to do. ) 
//* @date Jun 14, 2022 7:26:35 PM 
//* @version V1.0 
//*/
//package com.zk.log.controller;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.zk.core.utils.ZKEnvironmentUtils;
//import com.zk.core.utils.ZKMsgUtils;
//import com.zk.core.web.utils.ZKWebUtils;
//
///** 
//* @ClassName: ZKLogIndexController 
//* @Description: TODO(simple description this class what to do. ) 
//* @author Vinson 
//* @version 1.0 
//*/
//@RestController
//@RequestMapping(value = "${zk.path.admin}/${zk.path.log}/${zk.log.version}")
//public class ZKLogIndexController {
//
//    @RequestMapping({ "", "index" })
//    @ResponseBody
//    public String welcome() {
//        return ZKMsgUtils.getMessage("zk.log.msg.welcome", null, ZKWebUtils.getLocale())
//                + ZKEnvironmentUtils.getString("spring.application.name", "zk log");
//    }
//}
