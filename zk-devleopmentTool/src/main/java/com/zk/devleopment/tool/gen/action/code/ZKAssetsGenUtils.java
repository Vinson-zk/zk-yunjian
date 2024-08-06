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
* @Title: ZKAssetsGenUtils.java 
* @author Vinson 
* @Package com.zk.code.generate.gen.code 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 18, 2021 4:47:00 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action.code;

import java.io.File;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.core.utils.ZKStringUtils;
import com.zk.core.utils.ZKXmlUtils;
import com.zk.devleopment.tool.gen.action.ZKCodeFileUtils;
import com.zk.devleopment.tool.gen.action.ZKCodeTemplate;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

/**
 * 一些引用的资源生成
 * 
 * @ClassName: ZKAssetsGenUtils
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKAssetsGenUtils {

    protected static Logger log = LogManager.getLogger(ZKReactCodeGenUtils.class);

    public static interface PathKey {
        public static String assets = "assets";
    }

    /**
     * 生成菜单数据内容 sql
     */
    public static File genMenuAssetsSql(String rootPath, String templatePath, ZKModule zkModule,
            ZKTableInfo zkTableInfo) throws Exception {
        String filePath = "";
        String fileName = "menu-sql.sql";
        try {
            File f;
            ZKCodeTemplate template;

            filePath = PathKey.assets + File.separator + getReactCodePath(zkModule, zkTableInfo);
            template = ZKXmlUtils.xmlToBean(templatePath, "assets/menu-sql.xml", ZKCodeTemplate.class);
            f = ZKCodeFileUtils.genCodeFile(rootPath + filePath, fileName, template.getContent(), zkModule,
                    zkTableInfo);
            return f;
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 menu-sql 文件");
            throw e;
        }
    }

    // 生成静态菜单
    public static File genMenuAssetsStatic(String rootPath, String templatePath, ZKModule zkModule,
            ZKTableInfo zkTableInfo) throws Exception {
        String filePath = "";
        String fileName = "menu-static.txt";
        try {
            File f;
            ZKCodeTemplate template;

            filePath = PathKey.assets + File.separator + getReactCodePath(zkModule, zkTableInfo);
            template = ZKXmlUtils.xmlToBean(templatePath, "assets/menu-static.xml", ZKCodeTemplate.class);
            f = ZKCodeFileUtils.genCodeFile(rootPath + filePath, fileName,
                    template.getContent(), zkModule, zkTableInfo);
            return f;
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-002] 生成 menu-static 文件");
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
