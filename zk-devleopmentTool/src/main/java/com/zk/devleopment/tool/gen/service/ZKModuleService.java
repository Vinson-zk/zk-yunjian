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
* @Title: ZKModuleService.java 
* @author Vinson 
* @Package com.zk.code.generate.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 30, 2021 10:47:55 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.core.utils.ZKStringUtils;
import com.zk.devleopment.tool.gen.dao.ZKModuleDao;
import com.zk.devleopment.tool.gen.entity.ZKModule;

/** 
* @ClassName: ZKModuleService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKModuleService extends ZKBaseService<String, ZKModule, ZKModuleDao> {

    /**
     * 根据模块名称取模块实体
     * @MethodName getByModuleName
     * @param moduleName
     * @return com.zk.devleopment.tool.gen.entity.ZKModule
     * @throws
     * @Author bs
     * @DATE 2022-09-03 15:42:819
     */
    public ZKModule getByModuleName(String moduleName){
        if(ZKStringUtils.isEmpty(moduleName)){
            return null;
        }
        return this.dao.getByModuleName(moduleName);
    }
}
