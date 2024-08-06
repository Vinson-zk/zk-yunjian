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
* @Title: ZKAssetsGenUtilsTest.java 
* @author Vinson 
* @Package com.zk.code.generate.gen.code 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 18, 2021 5:41:13 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action.code;

import java.io.File;

import org.junit.Test;

import com.zk.devleopment.tool.ZKDevleopmentToolTestHelper;
import com.zk.devleopment.tool.gen.action.ZKCodeFileUtils;
import com.zk.devleopment.tool.gen.action.ZKConvertUtils;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

import junit.framework.TestCase;

/** 
* @ClassName: ZKAssetsGenUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKAssetsGenUtilsTest {


    @Test
    public void testGenMenuAssets() {

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
            // assets
            f = ZKAssetsGenUtils.genMenuAssetsSql(rootPath, templatePath, zkModule, zkTableInfo);
            System.out.println("[^_^:20210421-2057-001] ZKCodeFileUtils.getCodeRootPath: " + f.getPath());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
