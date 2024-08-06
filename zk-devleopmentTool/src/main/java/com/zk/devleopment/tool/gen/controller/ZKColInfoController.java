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
* @Title: ZKColInfoController.java 
* @author Vinson 
* @Package com.zk.code.generate.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 1, 2021 12:48:14 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.devleopment.tool.gen.entity.ZKColInfo;
import com.zk.devleopment.tool.gen.service.ZKColInfoService;

/** 
* @ClassName: ZKColInfoController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("${zk.path.admin}/${zk.path.dev.tool}/${zk.dev.tool.version}/${zk.path.dev.tool.code.gen}/c")
public class ZKColInfoController extends ZKBaseController {

    @Autowired
    ZKColInfoService zkColInfoService;

    // 增量更新
    @RequestMapping(value = "updateAddCols/{tableId}", method = RequestMethod.GET)
    public ZKMsgRes updateAddCols(@PathVariable("tableId") String tableId) {
        List<ZKColInfo> res = this.zkColInfoService.updateAddByTable(tableId);
        return ZKMsgRes.asOk(null, res);
    }

    // 全量更新
    @RequestMapping(value = "updateAllCols/{tableId}", method = RequestMethod.GET)
    public ZKMsgRes updateAllCols(@PathVariable("tableId") String tableId) {
        List<ZKColInfo> res = this.zkColInfoService.updateAllByTable(tableId);
        return ZKMsgRes.asOk(null, res);
    }
    
    // 查询
    @RequestMapping(value = "colInfos/{tableId}", method = RequestMethod.GET)
    public ZKMsgRes colInfos(@PathVariable("tableId") String tableId) {
        List<ZKColInfo> res = this.zkColInfoService.findByTableId(tableId);
        if (res == null || res.isEmpty()) {
            res = this.zkColInfoService.updateAddByTable(tableId);
        }
        return ZKMsgRes.asOk(null, res);
    }
    
    // 编辑
    @RequestMapping(value = "colInfo", method = RequestMethod.POST)
    public ZKMsgRes editColInfo(@RequestBody ZKColInfo[] colInfos) {
        for (ZKColInfo item : colInfos) {
            this.zkColInfoService.save(item);
        }
        return ZKMsgRes.asOk();
    }

}
