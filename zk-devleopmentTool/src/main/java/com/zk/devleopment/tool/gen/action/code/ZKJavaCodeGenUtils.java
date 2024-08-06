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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.core.utils.ZKStringUtils;
import com.zk.core.utils.ZKXmlUtils;
import com.zk.devleopment.tool.gen.action.ZKCodeFileUtils;
import com.zk.devleopment.tool.gen.action.ZKCodeTemplate;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

/**
 * java 代码生成；
 * 
 * @ClassName: ZKJavaCodeGenUtils
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKJavaCodeGenUtils {

    protected static Logger log = LogManager.getLogger(ZKJavaCodeGenUtils.class);

	public static interface PathKey {
		public static String mapper = "main/db/mappers";
		public static String java = "main/java";
		public static String testJava = "test/java";
	}

    /**
     * 生成 java 类 Service 文件 单元测试文件
     *
     * @param rootPath
     * @param templatePath
     * @param zkModule
     * @param zkTableInfo
     * @throws Exception
     */
    public static File genServiceTest(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {
        String javaFilePath = "";
        String fileName = "";
        try {
            fileName = zkTableInfo.getClassName() + "ServiceTest.java";
            javaFilePath = getJavaCodePath(zkModule, zkTableInfo);
            javaFilePath += File.separator + "service";
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, "java/serviceTest.xml", ZKCodeTemplate.class);
            return ZKCodeFileUtils.genCodeFile(rootPath + PathKey.testJava + File.separator + javaFilePath, fileName,
                    template.getContent(), zkModule, zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 java 测试代码 {}/{}, 异常", javaFilePath, fileName);
            throw e;
        }
    }

    /**
     * 生成 java 类实体文件
     * 
     * @param rootPath
     * @param templatePath
     * @param zkModule
     * @param zkTableInfo
     * @throws Exception
     */
    public static File genEntity(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {
        String javaFilePath = "";
        String fileName = "";
        String templatName = "java/entity.xml";
        try {
            fileName = zkTableInfo.getClassName() + ".java";
            javaFilePath = getJavaCodePath(zkModule, zkTableInfo);
            javaFilePath += File.separator + "entity";
            if (zkTableInfo.getIsTree()) {
                templatName = "java/treeEntity.xml";
            }
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, templatName, ZKCodeTemplate.class);
			return ZKCodeFileUtils.genCodeFile(
                    rootPath + PathKey.java + File.separator + javaFilePath,
					fileName, template.getContent(), zkModule,
                    zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 java 代码 {}/{}, 异常；templatName：{};", javaFilePath, fileName,
                    templatName);
            throw e;
        }
    }

    /**
     * 生成 java 类 dao 文件；
     *
     * @param rootPath
     * @param templatePath
     * @param zkModule
     * @param zkTableInfo
     * @throws Exception
     */
    public static File genDao(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {
        String javaFilePath = "";
        String fileName = "";
        String templatName = "java/dao.xml";
        try {
            fileName = zkTableInfo.getClassName() + "Dao.java";
            javaFilePath = getJavaCodePath(zkModule, zkTableInfo);
            javaFilePath += File.separator + "dao";
            if (zkTableInfo.getIsTree()) {
                templatName = "java/treeDao.xml";
            }
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, templatName, ZKCodeTemplate.class);
			return ZKCodeFileUtils.genCodeFile(
                    rootPath + PathKey.java + File.separator + javaFilePath,
					fileName, template.getContent(), zkModule,
                    zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 java 代码 {}/{}, 异常；templatName：{};", javaFilePath, fileName,
                    templatName);
            throw e;
        }
    }

    /**
     * 生成 java 类 Service 文件
     *
     * @param rootPath
     * @param templatePath
     * @param zkModule
     * @param zkTableInfo
     * @throws Exception
     */
    public static File genService(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {
        String javaFilePath = "";
        String fileName = "";
        String templatName = "java/service.xml";
        try {
            fileName = zkTableInfo.getClassName() + "Service.java";
            javaFilePath = getJavaCodePath(zkModule, zkTableInfo);
            javaFilePath += File.separator + "service";
            if (zkTableInfo.getIsTree()) {
                templatName = "java/treeService.xml";
            }
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, templatName, ZKCodeTemplate.class);
			return ZKCodeFileUtils.genCodeFile(
                    rootPath + PathKey.java + File.separator + javaFilePath,
					fileName, template.getContent(), zkModule,
                    zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 java 代码 {}/{}, 异常；templatName：{};", javaFilePath, fileName,
                    templatName);
            throw e;
        }
    }

    /**
     * 生成 java 类 接口 controller 文件
     *
     * @param rootPath
     * @param templatePath
     * @param zkModule
     * @param zkTableInfo
     * @throws Exception
     */
    public static File genController(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {
        String javaFilePath = "";
        String fileName = "";
        String templatName = "java/controller.xml";
        try {
            fileName = zkTableInfo.getClassName() + "Controller.java";
            javaFilePath = getJavaCodePath(zkModule, zkTableInfo);
            javaFilePath += File.separator + "controller";
            if (zkTableInfo.getIsTree()) {
                templatName = "java/treeController.xml";
            }
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, templatName, ZKCodeTemplate.class);
			return ZKCodeFileUtils.genCodeFile(
                    rootPath + PathKey.java + File.separator + javaFilePath,
					fileName, template.getContent(), zkModule,
                    zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 java 代码 {}/{}, 异常；templatName：{};", javaFilePath, fileName,
                    templatName);
            throw e;
        }
    }

    /**
     * 生成表映射 mapper 文件；
     *
     * @Title: genMapper
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 17, 2021 2:40:09 PM
     * @param rootPath
     *            以 / 结尾；不会自动添加 /
     * @param templatePath
     * @param zkModule
     * @param zkTableInfo
     * @return
     * @throws Exception
     * @return File
     */
    public static File genMapper(String rootPath, String templatePath, ZKModule zkModule, ZKTableInfo zkTableInfo)
            throws Exception {
        String javaFilePath = "";
        String fileName = "";
        try {
            fileName = zkTableInfo.getClassName() + ".xml";
            javaFilePath = getJavaCodePath(zkModule, zkTableInfo);
            ZKCodeTemplate template = ZKXmlUtils.xmlToBean(templatePath, "mybatis/mapper.xml", ZKCodeTemplate.class);
            return ZKCodeFileUtils.genCodeFile(rootPath + PathKey.mapper + File.separator + javaFilePath, fileName,
                    template.getContent(), zkModule, zkTableInfo);
        }
        catch(Exception e) {
            log.error("[>_<:20210425-1138-001] 生成 mybatis mapper 文件 {}/{}, 异常", javaFilePath, fileName);
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
    private static String getJavaCodePath(ZKModule zkModule, ZKTableInfo zkTableInfo) {
        String path = zkModule.getPackagePrefixPath();
//        if (!ZKStringUtils.isEmpty(zkModule.getModuleName())) {
//            path += File.separator + zkModule.getModuleNamePath();
//        }
        if (!ZKStringUtils.isEmpty(zkTableInfo.getSubModuleName())) {
            path += File.separator + zkTableInfo.getSubModuleNamePath();
        }
        return path;
    }

}
