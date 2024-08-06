package com.zk.db.entity;

import org.junit.Test;

import com.zk.db.helper.entity.ZKDBTestSampleEntity;
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
 * @ClassName ZKDBTestSampleEntityTest
 * @Package com.zk.db.entity
 * @PROJECT zk-yunjian
 * @Author bs
 * @DATE 2022-12-12 15:34:41
 **/
public class ZKDBTestSampleEntityTest {

    @Test
    public void test(){
        try{

            System.out.println("[^_^:20221212-1523-001]" + ZKMybatisSqlHelper.formatSql(ZKDBTestSampleEntity.sqlHelper().getSqlGet()));
            System.out.println("[^_^:20221212-1524-001]" + ZKMybatisSqlHelper.formatSql(ZKDBTestSampleEntity.sqlHelper().getBlockSqlSelelctList()));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testGetTableName(){
        try{
            System.out.println("[^_^:20221212-1536-001] getTableName: " + ZKDBTestSampleEntity.sqlHelper().getTableName());
            System.out.println("[^_^:20221212-1536-001] getTableAlias: " + ZKDBTestSampleEntity.sqlHelper().getTableAlias());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
