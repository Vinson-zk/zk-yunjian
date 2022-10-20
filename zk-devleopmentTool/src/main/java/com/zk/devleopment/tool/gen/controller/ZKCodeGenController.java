/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKCodeGenController.java 
* @author Vinson 
* @Package com.zk.code.generate.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 1, 2021 6:46:59 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zk.core.utils.ZKFileUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.devleopment.tool.gen.service.ZKCodeGenService;

/**
 * 代码生成
 * 
 * @ClassName: ZKCodeGenController
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Controller // 直接下载文件，没有 @responseBody
@RequestMapping("${zk.path.admin}/${zk.path.devleopmentTool}/${zk.devleopmentTool.version}/${zk.path.devleopmentTool.codeGen}/cg")
public class ZKCodeGenController {

    @Autowired
    ZKCodeGenService codeGenService;

    @RequestMapping(value = "genCode/{moduleId}", method = RequestMethod.POST)
    public void genCode(@PathVariable("moduleId")String moduleId,
            @RequestParam("tableIds[]")String[] tableIds,
            HttpServletRequest hReq, HttpServletResponse hRes) throws Exception {
//        System.out.println("======== 代码生成；tableIds" + ZKJsonUtils.writeObjectJson(tableIds));
        String filePath = codeGenService.genCode(moduleId, tableIds);
        ZKWebUtils.downloadFile(hRes, filePath);
        ZKFileUtils.deleteFile(new File(filePath));
    }

}
