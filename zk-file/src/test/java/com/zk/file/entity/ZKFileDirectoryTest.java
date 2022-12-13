package com.zk.file.entity;

import org.junit.Test;

import com.zk.db.mybatis.ZKMybatisSqlHelper;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description:
 * @ClassName ZKFileDirectoryTest
 * @Package com.zk.file.entity
 * @PROJECT zk-yunjian
 * @Author bs
 * @DATE 2022-12-12 15:22:22
 **/
public class ZKFileDirectoryTest {

    @Test
    public void test(){
        try{
            System.out.println("[^_^:20221212-1523-001]" + ZKMybatisSqlHelper.formatSql(ZKFileDirectory.sqlHelper().getSqlGet()));
            System.out.println("[^_^:20221212-1524-001]" + ZKMybatisSqlHelper.formatSql(ZKFileDirectory.sqlHelper().getBlockSqlSelelctList()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
