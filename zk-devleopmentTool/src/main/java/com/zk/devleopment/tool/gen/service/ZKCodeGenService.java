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
* @Title: ZKCodeGenService.java 
* @author Vinson 
* @Package com.zk.code.generate.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 2, 2021 12:01:13 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.service;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.core.exception.ZKBusinessException;
import com.zk.devleopment.tool.gen.action.ZKCodeFileUtils;
import com.zk.devleopment.tool.gen.action.code.ZKAssetsGenUtils;
import com.zk.devleopment.tool.gen.action.code.ZKJavaCodeGenUtils;
import com.zk.devleopment.tool.gen.action.code.ZKReactCodeGenUtils;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

/** 
* @ClassName: ZKCodeGenService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKCodeGenService {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Value("${zk.dev.tool.code.gen.root.path:codeGenFile/}")
    String genCodeRootPath = "";

    @Value("${zk.dev.tool.code.gen.code.template.path:\"\"}")
    String genCodeTemplatePath = "";

	@Autowired
	ZKModuleService zkModuleService;

	@Autowired
	ZKTableInfoService zkTableInfoService;

	@Autowired
    ZKColInfoService zkColInfoService;

    public String genCode(String moduleId, String... tableIds) throws Exception {
        ZKModule module = zkModuleService.get(new ZKModule(moduleId));
        if (module == null) {
            log.error("[^_^:20210417-1702-001] 功能模块：{}，不存在；", moduleId);
            throw ZKBusinessException.as("zk.codeGen.000001", null, moduleId);
        }
        return this.genCode(module, tableIds);
    }

    public String genCode(ZKModule module, String... tableIds) throws Exception {

        if (module == null) {
            log.error("[^_^:20210417-1702-002] 功能模块为 null");
            throw ZKBusinessException.as("zk.codeGen.000001", null);
        }

        String codeRootPath = ZKCodeFileUtils.getCodeRootPath(genCodeRootPath, module);
        ;
        for (String tableId : tableIds) {
            ZKTableInfo tableInfo = zkTableInfoService.get(new ZKTableInfo(tableId));
            if (tableInfo == null) {
                log.error("[^_^:20210408-0704-002] 表:{}, 信息不存在；", tableId);
                throw ZKBusinessException.as("zk.codeGen.000002", null, tableId);
            }

            if (!module.getPkId().equals(tableInfo.getModuleId())) {
                log.error("[^_^:20210408-0704-002] 非本模块的表；moduleId:{}, tableId:{} ", module.getPkId(), tableId);
                throw ZKBusinessException.as("zk.codeGen.000004", null, module.getModuleName(),
                        tableInfo.getTableName());
            }
            tableInfo.setModule(module);
            tableInfo.setCols(zkColInfoService.findByTableId(tableInfo.getPkId()));

            this.genJavaCode(codeRootPath, module, tableInfo);
            this.genReactCode(codeRootPath, module, tableInfo);
            this.genAssets(codeRootPath, module, tableInfo);
        }

        return codeRootPath;
    }

    /**
     * 生成 java 代码
     */
    public String genJavaCode(String codeRootPath, ZKModule module, ZKTableInfo tableInfo) throws Exception {

        File f = null;
        f = ZKJavaCodeGenUtils.genMapper(codeRootPath, genCodeTemplatePath, module, tableInfo);
        log.info("[^_^:20211639-0417-001] 生成 mapper: {}", f.getPath());
        f = ZKJavaCodeGenUtils.genEntity(codeRootPath, genCodeTemplatePath, module, tableInfo);
        log.info("[^_^:20211639-0417-001] 生成 Entity: {}", f.getPath());
        f = ZKJavaCodeGenUtils.genDao(codeRootPath, genCodeTemplatePath, module, tableInfo);
        log.info("[^_^:20211639-0417-001] 生成 Dao: {}", f.getPath());
        f = ZKJavaCodeGenUtils.genService(codeRootPath, genCodeTemplatePath, module, tableInfo);
        log.info("[^_^:20211639-0417-001] 生成 Service: {}", f.getPath());
        f = ZKJavaCodeGenUtils.genController(codeRootPath, genCodeTemplatePath, module, tableInfo);
        log.info("[^_^:20211639-0417-001] 生成 Controller: {}", f.getPath());
        f = ZKJavaCodeGenUtils.genServiceTest(codeRootPath, genCodeTemplatePath, module, tableInfo);
        log.info("[^_^:20211639-0417-001] 生成 ServiceTest: {}", f.getPath());

        return codeRootPath;
    }

    /**
     * 生成 react js 代码
     */
    public String genReactCode(String codeRootPath, ZKModule module, ZKTableInfo tableInfo) throws Exception {

        List<File> fs;
        File f = null;
        // detail
        f = ZKReactCodeGenUtils.genDetail(codeRootPath, genCodeTemplatePath, module, tableInfo);
        System.out.println("[^_^:20210421-2057-001] 生成 react js detail.js: " + f.getPath());
        // edit
        f = ZKReactCodeGenUtils.genEdit(codeRootPath, genCodeTemplatePath, module, tableInfo);
        System.out.println("[^_^:20210421-2057-001] 生成 react js edit.js: " + f.getPath());
        // grid
        f = ZKReactCodeGenUtils.genGrid(codeRootPath, genCodeTemplatePath, module, tableInfo);
        System.out.println("[^_^:20210421-2057-001] 生成 react js grid.js: " + f.getPath());
        // index
        f = ZKReactCodeGenUtils.genIndex(codeRootPath, genCodeTemplatePath, module, tableInfo);
        System.out.println("[^_^:20210421-2057-001] 生成 react js index.js: " + f.getPath());
        // model
        f = ZKReactCodeGenUtils.genModel(codeRootPath, genCodeTemplatePath, module, tableInfo);
        System.out.println("[^_^:20210421-2057-001] 生成 react js model.js: " + f.getPath());
        // model
        f = ZKReactCodeGenUtils.genSearch(codeRootPath, genCodeTemplatePath, module, tableInfo);
        System.out.println("[^_^:20210421-2057-001] 生成 react js search.js: " + f.getPath());
        // service
        f = ZKReactCodeGenUtils.genService(codeRootPath, genCodeTemplatePath, module, tableInfo);
        System.out.println("[^_^:20210421-2057-001] 生成 react js service.js: " + f.getPath());
        // 生成 react 功能引用 js 代码
        f = ZKReactCodeGenUtils.genReactFunc(codeRootPath, genCodeTemplatePath, module, tableInfo);
        System.out.println("[^_^:20210421-2057-001] 生成 react js 一些引用的代码: " + f.getPath());

        // locales
        fs = ZKReactCodeGenUtils.genLocaleMsg(codeRootPath, genCodeTemplatePath, module, tableInfo);
        for (File f1 : fs) {
            System.out.println("[^_^:20210421-2057-001] 生成 react js localeMsg.js: " + f1.getPath());
        }

        return codeRootPath;
    }
    
    /**
     * 生成一些数据或引用的资源代码
     */
    public String genAssets(String codeRootPath, ZKModule module, ZKTableInfo tableInfo) throws Exception {
        File f = null;
        // 生成 相关数据资源 内容
        f = ZKAssetsGenUtils.genMenuAssetsSql(codeRootPath, genCodeTemplatePath, module, tableInfo);
        f = ZKAssetsGenUtils.genMenuAssetsStatic(codeRootPath, genCodeTemplatePath, module, tableInfo);
        System.out.println("[^_^:20210421-2057-001] 生成 相关数据资源 内容: " + f.getPath());

        return codeRootPath;
    }

}
