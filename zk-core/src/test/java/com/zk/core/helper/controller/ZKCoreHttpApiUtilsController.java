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
 * @Title: ZKCoreHttpApiUtilsController.java 
 * @author Vinson 
 * @Package com.zk.core.helper.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 3:31:02 PM 
 * @version V1.0   
*/
package com.zk.core.helper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.commons.ZKFileTransfer;
import com.zk.core.helper.ZKCoreEntity;

/** 
* @ClassName: ZKCoreHttpApiUtilsController 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("${zk.path.admin:zkTest}/${zk.path.core:cTest}/httpApi")
public class ZKCoreHttpApiUtilsController {

    @Autowired
    ZKFileTransfer zkFileTransfer;

    public static final String msg_index = "zk http api utils 苍白的月光，忽然让我发冷。";

    @RequestMapping("get")
    public String get(@RequestParam("param") String param) {
        System.out.println("[^_^:20191213-1144-001] ZKHttpApiUtilsController.get -> param:" + param);
        return msg_index + param;
    }

    @RequestMapping(value = "post", method = RequestMethod.POST)
    public String post(@RequestParam("param") String param) {
        System.out.println("[^_^:20191213-1144-002] ZKHttpApiUtilsController.post -> param:" + param);
        return msg_index + param;
    }

    @RequestMapping(value = "postJson", method = RequestMethod.POST)
    public ZKCoreEntity postJson(@RequestParam("param") String param, @RequestBody ZKCoreEntity entity) {
        System.out.println("[^_^:20191213-1144-003] ZKHttpApiUtilsController.postJson -> param:" + param);
        entity.setRemark(param);
        return entity;
    }

}
