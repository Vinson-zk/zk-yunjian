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
* @Title: ZKJavaCodeGenUtilsTest.java 
* @author Vinson 
* @Package com.zk.code.generate.code 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 24, 2021 10:07:55 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action.code;

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipOutputStream;

import org.junit.Test;

import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.devleopment.tool.ZKDevleopmentToolTestHelper;
import com.zk.devleopment.tool.gen.action.ZKCodeFileUtils;
import com.zk.devleopment.tool.gen.action.ZKConvertUtils;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

import junit.framework.TestCase;

/** 
* @ClassName: ZKJavaCodeGenUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJavaCodeGenUtilsTest {

    @Test
    public void test() {

        try {
            String rootPath;
            String templatePath;
            ZKModule zkModule;
            ZKTableInfo zkTableInfo;

            rootPath = ZKDevleopmentToolTestHelper.testRootPath;
            templatePath = "gen/template";
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);
            ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);

            rootPath = ZKCodeFileUtils.getCodeRootPath(rootPath, zkModule);
            ZKJavaCodeGenUtils.genMapper(rootPath, templatePath, zkModule, zkTableInfo);
            ZKJavaCodeGenUtils.genEntity(rootPath, templatePath, zkModule, zkTableInfo);
            ZKJavaCodeGenUtils.genDao(rootPath, templatePath, zkModule, zkTableInfo);
            ZKJavaCodeGenUtils.genService(rootPath, templatePath, zkModule, zkTableInfo);
            ZKJavaCodeGenUtils.genController(rootPath, templatePath, zkModule, zkTableInfo);

            File f = new File(rootPath);

            FileOutputStream fileOutputStream = null;
            CheckedOutputStream cos = null;
            ZipOutputStream out = null;

            File zipFile = new File(f.getParent() + File.separator + f.getName() + ".zip");
            System.out.println("[^_^:20210417-1453-001] f.getPath: " + f.getPath());
            System.out.println("[^_^:20210417-1453-001] f.getParent: " + f.getParent());
            System.out.println("[^_^:20210417-1453-001] f.getName: " + f.getName());
            System.out.println("[^_^:20210417-1453-001] zipFile.getParent: " + zipFile.getParent());
            System.out.println("[^_^:20210417-1453-001] zipFile.getName: " + zipFile.getName());
            try {
                fileOutputStream = new FileOutputStream(zipFile);
                cos = new CheckedOutputStream(fileOutputStream, new CRC32());
                out = new ZipOutputStream(cos);
                ZKFileUtils.compress(f, out, "");

                System.out.println("=================: " + ZKFileUtils.deleteFile(f));
//                f.delete();
            } finally {
                ZKStreamUtils.closeStream(out);
                ZKStreamUtils.closeStream(cos);
                ZKStreamUtils.closeStream(fileOutputStream);
            }

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGenMapper() {

        try {
            String rootPath;
            String templatePath;
            ZKModule zkModule;
            ZKTableInfo zkTableInfo;

            rootPath = ZKDevleopmentToolTestHelper.testRootPath;
            templatePath = "gen/template";
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);
            ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);

            rootPath = ZKCodeFileUtils.getCodeRootPath(rootPath, zkModule);
            ZKJavaCodeGenUtils.genMapper(rootPath, templatePath, zkModule, zkTableInfo);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGenEntity() {
        
        try {
            String rootPath;
            String templatePath;
            ZKModule zkModule;
            ZKTableInfo zkTableInfo;
            
            rootPath = ZKDevleopmentToolTestHelper.testRootPath;
            templatePath = "gen/template";
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);
            ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);

            rootPath = ZKCodeFileUtils.getCodeRootPath(rootPath, zkModule);
            ZKJavaCodeGenUtils.genEntity(rootPath, templatePath, zkModule, zkTableInfo);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGenDao() {

        try {
            String rootPath;
            String templatePath;
            ZKModule zkModule;
            ZKTableInfo zkTableInfo;
            
            rootPath = ZKDevleopmentToolTestHelper.testRootPath;
            templatePath = "gen/template";
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);
            ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);

            rootPath = ZKCodeFileUtils.getCodeRootPath(rootPath, zkModule);
            ZKJavaCodeGenUtils.genDao(rootPath, templatePath, zkModule, zkTableInfo);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGenService() {

        try {
            String rootPath;
            String templatePath;
            ZKModule zkModule;
            ZKTableInfo zkTableInfo;

            rootPath = ZKDevleopmentToolTestHelper.testRootPath;
            templatePath = "gen/template";
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);
            ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);

            rootPath = ZKCodeFileUtils.getCodeRootPath(rootPath, zkModule);
            ZKJavaCodeGenUtils.genService(rootPath, templatePath, zkModule, zkTableInfo);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGetController() {

        try {
            String rootPath;
            String templatePath;
            ZKModule zkModule;
            ZKTableInfo zkTableInfo;

            rootPath = ZKDevleopmentToolTestHelper.testRootPath;
            templatePath = "gen/template";
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);
            ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);

            rootPath = ZKCodeFileUtils.getCodeRootPath(rootPath, zkModule);
            ZKJavaCodeGenUtils.genController(rootPath, templatePath, zkModule, zkTableInfo);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
