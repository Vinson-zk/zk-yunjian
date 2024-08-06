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
* @Title: ZKJavaCodeGenUtils.java 
* @author Vinson 
* @Package com.zk.code.generate.code 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 24, 2021 5:27:23 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action.code;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.core.utils.ZKStringUtils;
import com.zk.core.utils.ZKXmlUtils;
import com.zk.devleopment.tool.gen.action.ZKCodeFileUtils;
import com.zk.devleopment.tool.gen.action.ZKCodeTemplate;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

/**
 * react 代码生成
 * 
 * @ClassName: ZKReactCodeGenUtils
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKReactCodeGenUtils {

    protected static Logger log = LogManager.getLogger(ZKReactCodeGenUtils.class);

	public static interface PathKey {
        public static String react = "ui/react";
	}

    /**
     * 生成 react js 代码 detail.js
     * 
     * @param tableInfo
     * @throws Exception
     */
    public static File genDetail(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {
        String filePath = "";
        String fileName = "detail.js";
        String templatName = "react/detail.xml";
        try {
            filePath = getReactCodePath(zkModule, zkTableInfo);
            if (zkTableInfo.getIsTree()) {
                templatName = "react/treeDetail.xml";
            }
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, templatName, ZKCodeTemplate.class);
            return ZKCodeFileUtils.genCodeFile(rootPath + PathKey.react + File.separator + filePath, fileName,
                    template.getContent(), zkModule, zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 react 代码 {}/{}, 异常", filePath, fileName);
            throw e;
        }
    }

    /**
     * 生成 react js 代码 edit.js
     *
     * @Title: genEdit
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2021 5:42:56 PM
     * @param rootPath
     * @param templatePath
     * @param zkModule
     * @param zkTableInfo
     * @return
     * @throws Exception
     * @return File
     */
    public static File genEdit(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {
        String filePath = "";
        String fileName = "edit.js";
        String templatName = "react/edit.xml";
        try {
            filePath = getReactCodePath(zkModule, zkTableInfo);
            if (zkTableInfo.getIsTree()) {
                templatName = "react/treeEdit.xml";
            }
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, templatName, ZKCodeTemplate.class);
            return ZKCodeFileUtils.genCodeFile(rootPath + PathKey.react + File.separator + filePath, fileName,
                    template.getContent(), zkModule, zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 react 代码 {}/{}, 异常", filePath, fileName);
            throw e;
        }
    }

    /**
     * 生成 react js 代码 grid.js
     *
     * @Title: genGrid
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2021 5:43:06 PM
     * @param rootPath
     * @param templatePath
     * @param zkModule
     * @param zkTableInfo
     * @return
     * @throws Exception
     * @return File
     */
    public static File genGrid(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {
        String filePath = "";
        String fileName = "grid.js";
        String templatName = "react/grid.xml";
        try {
            filePath = getReactCodePath(zkModule, zkTableInfo);
            if (zkTableInfo.getIsTree()) {
                templatName = "react/treeGrid.xml";
            }
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, templatName, ZKCodeTemplate.class);
            return ZKCodeFileUtils.genCodeFile(rootPath + PathKey.react + File.separator + filePath, fileName,
                    template.getContent(), zkModule, zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 react 代码 {}/{}, 异常", filePath, fileName);
            throw e;
        }
    }

    /**
     * 生成 react js 代码 index.js
     *
     * @Title: genIndex
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2021 5:43:17 PM
     * @param rootPath
     * @param templatePath
     * @param zkModule
     * @param zkTableInfo
     * @return
     * @throws Exception
     * @return File
     */
    public static File genIndex(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {
        String filePath = "";
        String fileName = "index.js";
        String templatName = "react/index.xml";
        try {
            fileName = "index.js";
            filePath = getReactCodePath(zkModule, zkTableInfo);
            if (zkTableInfo.getIsTree()) {
                templatName = "react/treeIndex.xml";
            }
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, templatName, ZKCodeTemplate.class);
            return ZKCodeFileUtils.genCodeFile(rootPath + PathKey.react + File.separator + filePath, fileName,
                    template.getContent(), zkModule, zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 react 代码 {}/{}, 异常", filePath, fileName);
            throw e;
        }
    }

    /**
     * 生成 react js 代码 model.js
     *
     * @Title: genModel
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2021 5:43:28 PM
     * @param rootPath
     * @param templatePath
     * @param zkModule
     * @param zkTableInfo
     * @return
     * @throws Exception
     * @return File
     */
    public static File genModel(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {
        String filePath = "";
        String fileName = "model.js";
        String templatName = "react/model.xml";
        try {
            filePath = getReactCodePath(zkModule, zkTableInfo);
            if (zkTableInfo.getIsTree()) {
                templatName = "react/treeModel.xml";
            }
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, templatName, ZKCodeTemplate.class);
            return ZKCodeFileUtils.genCodeFile(rootPath + PathKey.react + File.separator + filePath, fileName,
                    template.getContent(), zkModule, zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 react 代码 {}/{}, 异常", filePath, fileName);
            throw e;
        }
    }

    /**
     * 生成 react js 代码 service.js
     *
     * @Title: genService
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2021 5:43:54 PM
     * @param rootPath
     * @param templatePath
     * @param zkModule
     * @param zkTableInfo
     * @return
     * @throws Exception
     * @return File
     */
    public static File genService(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {
        String filePath = "";
        String fileName = "service.js";
        String templatName = "react/service.xml";
        try {
            filePath = getReactCodePath(zkModule, zkTableInfo);
            if (zkTableInfo.getIsTree()) {
                templatName = "react/treeService.xml";
            }
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, templatName, ZKCodeTemplate.class);
            return ZKCodeFileUtils.genCodeFile(rootPath + PathKey.react + File.separator + filePath, fileName,
                    template.getContent(), zkModule, zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 react 代码 {}/{}, 异常", filePath, fileName);
            throw e;
        }
    }

    /**
     * 生成 react js 代码 search.js
     *
     * @Title: genSearch
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2021 5:43:42 PM
     * @param rootPath
     * @param templatePath
     * @param zkModule
     * @param zkTableInfo
     * @return
     * @throws Exception
     * @return File
     */
    public static File genSearch(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {
        String filePath = "";
        String fileName = "search.js";
        String templatName = "react/search.xml";
        try {
            filePath = getReactCodePath(zkModule, zkTableInfo);
            if (zkTableInfo.getIsTree()) {
                templatName = "react/treeSearch.xml";
            }
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, templatName, ZKCodeTemplate.class);
            return ZKCodeFileUtils.genCodeFile(rootPath + PathKey.react + File.separator + filePath, fileName,
                    template.getContent(), zkModule, zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 react 代码 {}/{}, 异常", filePath, fileName);
            throw e;
        }
    }

    /**
     * 生成 react js 代码 locale msg
     *
     * @Title: genLocaleMsg
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2021 5:44:06 PM
     * @param rootPath
     * @param templatePath
     * @param zkModule
     * @param zkTableInfo
     * @return
     * @throws Exception
     * @return List<File>
     */
    public static List<File> genLocaleMsg(String rootPath, String templatePath, ZKModule zkModule,
            ZKTableInfo zkTableInfo) throws Exception {
        String filePath = "";
        String fileName = "";
        try {
            List<File> fs = new ArrayList<>();
            File f;
            ZKCodeTemplate template;

            filePath = "locales/msg/" + getReactCodePath(zkModule, zkTableInfo);
            // filePath += File.separator + zkTableInfo.getClassNameUncap();

            fileName = "zh_CN.js";
            template = ZKXmlUtils.xmlToBean(templatePath, "react/locales_zh_CN.xml", ZKCodeTemplate.class);
            f = ZKCodeFileUtils.genCodeFile(rootPath + PathKey.react + File.separator + filePath, fileName,
                    template.getContent(), zkModule, zkTableInfo);
            fs.add(f);

            fileName = "en_US.js";
            template = ZKXmlUtils.xmlToBean(templatePath, "react/locales_en_US.xml", ZKCodeTemplate.class);
            f = ZKCodeFileUtils.genCodeFile(rootPath + PathKey.react + File.separator + filePath, fileName,
                    template.getContent(), zkModule, zkTableInfo);
            fs.add(f);

            return fs;
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 react locale msg 文件");
            throw e;
        }
    }

    /**
     * 生成 react 功能引用 js 代码
     */
    public static File genReactFunc(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {

        String filePath = "";
        String fileName = "func.js";
        try {
            filePath = getReactCodePath(zkModule, zkTableInfo);
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, "react/func.xml", ZKCodeTemplate.class);
            return ZKCodeFileUtils.genCodeFile(rootPath + PathKey.react + File.separator + filePath, fileName,
                    template.getContent(), zkModule, zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 react 功能引用 js 代码 {}/{}, 异常", filePath, fileName);
            throw e;
        }
    }

    /**
     * 取代码在 classpath 下的路径
     *
     * @Title: getJavaCodePath
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 24, 2021 6:02:53 PM
     * @param zkModule
     * @param zkTableInfo
     * @return
     * @return String
     */
    private static String getReactCodePath(ZKModule zkModule, ZKTableInfo zkTableInfo) {
        String path = zkModule.getModuleNamePath();
        if (!ZKStringUtils.isEmpty(zkTableInfo.getSubModuleName())) {
            path += File.separator + zkTableInfo.getSubModuleNamePath();
        }
        path += File.separator + ZKStringUtils.uncapitalize(zkTableInfo.getFuncName());
        return path;
    }

}
