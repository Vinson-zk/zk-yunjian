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
* @Title: ZKTableInfoController.java 
* @author Vinson 
* @Package com.zk.code.generate.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 31, 2021 8:33:43 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.controller;

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
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;
import com.zk.devleopment.tool.gen.service.ZKModuleService;
import com.zk.devleopment.tool.gen.service.ZKTableInfoService;

/**
 * @ClassName: ZKTableInfoController
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@RestController
@RequestMapping("${zk.path.admin}/${zk.path.dev.tool}/${zk.dev.tool.version}/${zk.path.dev.tool.code.gen}/ti")
public class ZKTableInfoController extends ZKBaseController {
    
    @Autowired
    ZKModuleService zkModuleService;

    @Autowired
    ZKTableInfoService zkTableInfoService;
    
    // 取模块表的列表信息；不分页，仅查询已更新到代码生成库的表列表，不会自动取模块数据中新创建的表；
    @RequestMapping(value = "tables/{moduleId}", method = RequestMethod.GET)
    public ZKMsgRes getTables(@PathVariable("moduleId")String moduleId) {
        return ZKMsgRes.asOk(null, this.zkTableInfoService.getTablesByModule(moduleId));
    }
    
    // 更新表列表；从模块的数据库中读取表列表，更新到代码生成的表列表中；
    @RequestMapping(value = "updateTableList/{moduleId}", method = RequestMethod.GET)
    public ZKMsgRes updateTableList(@PathVariable("moduleId")String moduleId) {
        List<ZKTableInfo> res = this.zkTableInfoService.updateTableList(moduleId);
        ZKModule module = this.zkModuleService.get(new ZKModule(moduleId));
        for (ZKTableInfo ti : res) {
            ti.setModule(module);
        }
        return ZKMsgRes.asOk(null, res);
    }

    // 更新表信息
    @RequestMapping(value = "updateTableInfo/{tableId}", method = RequestMethod.GET)
    public ZKMsgRes updateTableInfo(@PathVariable("tableId")String tableId) {
        ZKTableInfo ti = this.zkTableInfoService.updateTableInfo(tableId);
        return ZKMsgRes.asOk(null, ti);
    }

    // 编辑
    @RequestMapping(value = "tableInfo", method = RequestMethod.POST)
    public ZKMsgRes editTableInfo(@RequestBody ZKTableInfo tableInfo) {
        this.zkTableInfoService.save(tableInfo);
        ZKModule module = this.zkModuleService.get(new ZKModule(tableInfo.getModuleId()));
        tableInfo.setModule(module);
        return ZKMsgRes.asOk(tableInfo);
    }

    // 删除表信息，级联删除表信息字段
    @RequestMapping(value = "tableInfo", method = RequestMethod.DELETE)
    public ZKMsgRes delTableInfo(@RequestParam("pkId[]")String[] pkIds) {
        int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.zkTableInfoService.diskDel(new ZKTableInfo(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
    }
}
