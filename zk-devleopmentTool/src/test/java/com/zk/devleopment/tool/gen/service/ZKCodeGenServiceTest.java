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
* @Title: ZKCodeGenServiceTest.java 
* @author Vinson 
* @Package com.zk.code.generate.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 25, 2021 6:36:30 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.core.utils.ZKStringUtils;
import com.zk.devleopment.tool.ZKDevleopmentToolTestHelper;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

import junit.framework.TestCase;

/** 
* @ClassName: ZKCodeGenServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCodeGenServiceTest {
    
    public static ZKModule configModule() {
        ZKModule module = new ZKModule();
        
        module.setDriver("com.mysql.jdbc.Driver");
        module.setUrl("jdbc:mysql://10.211.55.11:3306/zk-file?useUnicode=true&characterEncoding=utf8&useTimezone=true&serverTimezone=GMT%2B8");
        module.setUsername("zk_r");
        module.setPassword("zk_r");
        module.setDbType("mysql");
        module.setIsRemovePrefix(true);
        
        module.setModuleName("file");
        module.setModulePrefix("ZK");
        module.setLabelName("文件服务");
        module.setTableNamePrefix("t_file");
        module.setColNamePrefix("c_");
        module.setPackagePrefix("com.zk.file");

        return module;
    }

    /**
     * 根据表名，生成代码；
     * 
     * 1、Module 不存在，需要设置 Module 并创建；
     * 
     * 2、根据需要控制是否需要清除历史表信息；并重新生成；
     *
     * @Title: testGenCodeByTableName
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 6, 2023 11:22:14 AM
     * @return void
     */
    @Test
    public void testGenCodeByTableName() {

        ConfigurableApplicationContext ctx = ZKDevleopmentToolTestHelper.getMainCtx();
        ZKCodeGenService s = ctx.getBean(ZKCodeGenService.class);

        ZKModuleService mInfoService = ctx.getBean(ZKModuleService.class);
        ZKTableInfoService tInfoService = ctx.getBean(ZKTableInfoService.class);
        ZKColInfoService cInfoService = ctx.getBean(ZKColInfoService.class);
        
        ZKModule module;
        List<String>  tableIds = new ArrayList<>();
        
        String moduleName = null;
        List<String> tableNames = Arrays.asList("t_file_info");
        
        moduleName = "file";
        if (ZKStringUtils.isEmpty(moduleName)) {
            module = configModule();
            mInfoService.save(module);
        }
        else {
            module = mInfoService.getByModuleName(moduleName);
        }
        
        for (String tableName : tableNames) {
            // 根据需要控制是否需要清除历史表信息；并重新生成；
            ZKTableInfo tableInfo = tInfoService.updateTableInfo(false, module.getPkId(), tableName);
            cInfoService.updateAllByTable(tableInfo.getPkId());
            tableIds.add(tableInfo.getPkId());
        }

        String genCodeFilePath = "";
        try {
            // 生成代码
            genCodeFilePath = s.genCode(module.getPkId(), tableIds.toArray(new String[tableIds.size()]));
            System.out.println("[^_^:20230303-1925-001] 生成文件目录：" + genCodeFilePath);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGenCode() {

        ConfigurableApplicationContext ctx = ZKDevleopmentToolTestHelper.getMainCtx();
        ZKCodeGenService s = ctx.getBean(ZKCodeGenService.class);

//        ZKModuleService  ms = ctx.getBean(ZKModuleService.class);
//        ZKTableInfoService  tis = ctx.getBean(ZKTableInfoService.class);

        String moduleId;
        String[] tableIds;
        
        moduleId = "5520657069075595776";
        tableIds = new String[] { "5716360042878337536" };

//        moduleId = "5713506574702477824";
//        tableIds = new String[] { "5715878812110029312" };

        try {
            s.genCode(moduleId, tableIds);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}

