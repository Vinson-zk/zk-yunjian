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
* @Title: ZKDevleopmentToolTestHelper.java 
* @author Vinson 
* @Package com.zk.devleopment.tool 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 9, 2022 12:01:17 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.core.utils.ZKDateUtils;
import com.zk.devleopment.tool.gen.entity.ZKColInfo;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

/** 
* @ClassName: ZKDevleopmentToolTestHelper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDevleopmentToolTestHelper {

    /**
     * 配置文件路径
     */
    public static String config = "test_mybatis_config.xml";

    protected static Logger log = LogManager.getLogger(ZKDevleopmentToolTestHelper.class);

    public static String testRootPath = "codeGenFile/";

    static ConfigurableApplicationContext ctx = null;

    public static ConfigurableApplicationContext getMainCtx() {
        if (ctx == null) {
            log.info("[^_^:20210330-1508-001]========================================");
            log.info("[^_^:20210330-1508-001]=== zk code gen test  启动测试 ... ... ");
            log.info("[^_^:20210330-1508-001]========================================");
            ctx = ZKDevleopmentToolSpringBootMain.run(new String[] {});
            log.info("[^_^:20210330-1508-001]----------------------------------------");
        }
        return ctx;
    }

    public static ZKModule getTestModule() {
        ZKModule zkModule = null;

        ///////////////////////
        zkModule = new ZKModule();
        zkModule.setUrl(
            "jdbc:mysql://127.0.0.1:3306/sys?useUnicode=true&characterEncoding=utf8&useTimezone=true&serverTimezone=GMT%2B8");
        zkModule.setUsername("root");
        zkModule.setPassword("root");

        ///////////////////////
        zkModule.setPackagePrefix("com.zk.test.gen");
        zkModule.setModuleName("genTestModule");

        zkModule.setTableNamePrefix("t_");
        zkModule.setColNamePrefix("c_");
        zkModule.setIsRemovePrefix(true);

        return zkModule;
    }

    public static ZKTableInfo getTestTable(ZKModule module) {

        ZKTableInfo zkTableInfo;
        ZKColInfo zkCol;
        List<ZKColInfo> cols = new ArrayList<>();
        zkTableInfo = new ZKTableInfo("1");

        zkTableInfo.setModule(module);

        zkTableInfo.setTableName("t_test_table_gen");
        zkTableInfo.setTableComments("测试代码生成");

        zkCol = new ZKColInfo();
        zkCol.setColName("c_testa");
        zkCol.setColJdbcType("varchar(255)");
        zkCol.setColComments("tt");
        zkCol.setColIsNull(false);
        zkCol.setAttrIsUpdate(false);
        zkCol.setEditStrategy(ZKColInfo.KeyEditStrategy.EditSel);
        cols.add(zkCol);
        zkCol = new ZKColInfo();
        zkCol.setColName("c_testb");
        zkCol.setColJdbcType("INT");
        zkCol.setColComments("tt");
        zkCol.setColIsNull(false);
        zkCol.setAttrIsUpdate(false);
        cols.add(zkCol);
        zkCol = new ZKColInfo();
        zkCol.setColName("c_testc");
        zkCol.setColJdbcType("INT");
        zkCol.setColComments("tt");
        zkCol.setColIsNull(false);
        zkCol.setAttrIsUpdate(false);
        cols.add(zkCol);
        zkCol = new ZKColInfo();
        zkCol.setColName("c_create_date");
        zkCol.setColJdbcType("DateTime");
        cols.add(zkCol);
        zkCol = new ZKColInfo();
        zkCol.setColName("c_test_json");
        zkCol.setColJdbcType("json");
        zkCol.setColComments("tt");
        zkCol.setColIsNull(false);
        cols.add(zkCol);
        zkCol = new ZKColInfo();
        zkCol.setColName("c_test_json_2");
        zkCol.setColJdbcType("json");
        zkCol.setColComments("tt_2");
        zkCol.setColIsNull(false);
        cols.add(zkCol);

        zkTableInfo.setPkColName("c_create_date");
        zkTableInfo.setCols(cols);

        zkTableInfo.setSubModuleName("sub");
        zkTableInfo.setUpdateDate(ZKDateUtils.getToday());

        return zkTableInfo;
    }

}
