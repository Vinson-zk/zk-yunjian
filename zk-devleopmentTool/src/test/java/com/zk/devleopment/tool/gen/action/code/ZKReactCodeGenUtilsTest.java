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
* @Title: ZKReactCodeGenUtilsTest.java 
* @author Vinson 
* @Package com.zk.code.generate.gen.code 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 21, 2021 8:50:38 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action.code;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.zk.devleopment.tool.ZKDevleopmentToolTestHelper;
import com.zk.devleopment.tool.gen.action.ZKCodeFileUtils;
import com.zk.devleopment.tool.gen.action.ZKConvertUtils;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

import junit.framework.TestCase;

/**
 * @ClassName: ZKReactCodeGenUtilsTest
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1. 0
 */
public class ZKReactCodeGenUtilsTest {

    // detail
    @Test
    public void testGenDetail() {

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
            File f = ZKReactCodeGenUtils.genDetail(rootPath, templatePath, zkModule, zkTableInfo);
            System.out.println("[^_^:20210421-2057-001] ZKReactCodeGenUtils.genDetail: " + f.getPath());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // edit
    @Test
    public void testGenEdit() {

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
            // edit
            File f = ZKReactCodeGenUtils.genEdit(rootPath, templatePath, zkModule, zkTableInfo);
            System.out.println("[^_^:20210421-2057-001] ZKReactCodeGenUtils.genEdit: " + f.getPath());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // grid
    @Test
    public void testGrid() {

        try {
            String rootPath;
            String templatePath;
            ZKModule zkModule;
            ZKTableInfo zkTableInfo;
            File f;

            rootPath = ZKDevleopmentToolTestHelper.testRootPath;
            templatePath = "gen/template";
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);
            ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);

            rootPath = ZKCodeFileUtils.getCodeRootPath(rootPath, zkModule);

            // grid
            f = ZKReactCodeGenUtils.genGrid(rootPath, templatePath, zkModule, zkTableInfo);
            System.out.println("[^_^:20210421-2057-001] ZKReactCodeGenUtils.genGrid: " + f.getPath());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // index
    @Test
    public void testIndex() {

        try {
            String rootPath;
            String templatePath;
            ZKModule zkModule;
            ZKTableInfo zkTableInfo;
            File f;

            rootPath = ZKDevleopmentToolTestHelper.testRootPath;
            templatePath = "gen/template";
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);
            ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);

            rootPath = ZKCodeFileUtils.getCodeRootPath(rootPath, zkModule);
            // index
            f = ZKReactCodeGenUtils.genIndex(rootPath, templatePath, zkModule, zkTableInfo);
            System.out.println("[^_^:20210421-2057-001] ZKReactCodeGenUtils.genIndex: " + f.getPath());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // model
    @Test
    public void testModel() {

        try {
            String rootPath;
            String templatePath;
            ZKModule zkModule;
            ZKTableInfo zkTableInfo;
            File f;

            rootPath = ZKDevleopmentToolTestHelper.testRootPath;
            templatePath = "gen/template";
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);
            ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);

            rootPath = ZKCodeFileUtils.getCodeRootPath(rootPath, zkModule);
            // model
            f = ZKReactCodeGenUtils.genModel(rootPath, templatePath, zkModule, zkTableInfo);
            System.out.println("[^_^:20210421-2057-001] ZKReactCodeGenUtils.genModel: " + f.getPath());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // search
    @Test
    public void testSearch() {

        try {
            String rootPath;
            String templatePath;
            ZKModule zkModule;
            ZKTableInfo zkTableInfo;
            File f;

            rootPath = ZKDevleopmentToolTestHelper.testRootPath;
            templatePath = "gen/template";
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);
            ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);

            rootPath = ZKCodeFileUtils.getCodeRootPath(rootPath, zkModule);
            // model
            f = ZKReactCodeGenUtils.genSearch(rootPath, templatePath, zkModule, zkTableInfo);
            System.out.println("[^_^:20210421-2057-001] ZKReactCodeGenUtils.genSearch: " + f.getPath());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // service
    @Test
    public void testService() {

        try {
            String rootPath;
            String templatePath;
            ZKModule zkModule;
            ZKTableInfo zkTableInfo;
            File f;

            rootPath = ZKDevleopmentToolTestHelper.testRootPath;
            templatePath = "gen/template";
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);
            ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);

            rootPath = ZKCodeFileUtils.getCodeRootPath(rootPath, zkModule);
            // service
            f = ZKReactCodeGenUtils.genService(rootPath, templatePath, zkModule, zkTableInfo);
            System.out.println("[^_^:20210421-2057-001] ZKReactCodeGenUtils.genService: " + f.getPath());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 生成 react 功能引用 js 代码
    @Test
    public void testGenReactFunc() {

        try {
            String rootPath;
            String templatePath;
            ZKModule zkModule;
            ZKTableInfo zkTableInfo;
            File f;

            rootPath = ZKDevleopmentToolTestHelper.testRootPath;
            templatePath = "gen/template";
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);
            ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);

            rootPath = ZKCodeFileUtils.getCodeRootPath(rootPath, zkModule);
            // 生成 react 功能引用 js 代码
            f = ZKReactCodeGenUtils.genReactFunc(rootPath, templatePath, zkModule, zkTableInfo);
            System.out.println("[^_^:20210421-2057-001] ZKCodeFileUtils.genReactFunc: " + f.getPath());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // locales
    @Test
    public void testLocales() {

        try {
            String rootPath;
            String templatePath;
            ZKModule zkModule;
            ZKTableInfo zkTableInfo;
            List<File> fs;

            rootPath = ZKDevleopmentToolTestHelper.testRootPath;
            templatePath = "gen/template";
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);
            ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);

            rootPath = ZKCodeFileUtils.getCodeRootPath(rootPath, zkModule);
            // locales
            fs = ZKReactCodeGenUtils.genLocaleMsg(rootPath, templatePath, zkModule, zkTableInfo);
            for (File f1 : fs) {
                System.out.println("[^_^:20210421-2057-001] ZKReactCodeGenUtils.genLocaleMsg: " + f1.getPath());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
