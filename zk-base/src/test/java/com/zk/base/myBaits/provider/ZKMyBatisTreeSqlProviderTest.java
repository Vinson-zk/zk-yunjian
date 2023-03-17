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
* @Title: ZKMyBatisTreeSqlProviderTest.java 
* @author Vinson 
* @Package com.zk.base.myBaits.provider 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 18, 2022 10:21:42 PM 
* @version V1.0 
*/
package com.zk.base.myBaits.provider;

import org.junit.Test;

import com.zk.base.helper.entity.ZKBaseHelperTreeEntity;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMyBatisTreeSqlProviderTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMyBatisTreeSqlProviderTest {

    @Test
    public void test() {
        try {
            
            /*
FROM t_test a 
<where><trim prefixOverrides="and|or"><if test=" pkId != null and pkId != ''">a.c_pk_id = #{pkId}</if><if test=" parentId != null and parentId != ''"> AND a.c_parent_id = #{parentId}</if><if test=" delFlag != null"> AND a.c_del_flag = #{delFlag}</if><if test="parentId == null or parentId == ''"><if test="parentIdIsEmpty != null  and  parentIdIsEmpty == true ">(a.c_parent_id is null  OR a.c_parent_id = "")</if></if></trim></where> 
=======================================
FROM t_test a 
<where><trim prefixOverrides="and|or"><if test=" pkId != null and pkId != ''">a.c_pk_id = #{pkId}</if><if test=" parentId != null and parentId != ''"> AND a.c_parent_id = #{parentId}</if><if test=" delFlag != null"> AND a.c_del_flag = #{delFlag}</if><if test="parentId == null or parentId == ''"><if test="parentIdIsEmpty != null  and  parentIdIsEmpty == true ">(a.c_parent_id is null  OR a.c_parent_id = "")</if></if></trim></where>  </script>
=======================================
FROM t_test a 
<where><if test="parentId == null or parentId == ''">(a.c_parent_id is null  OR a.c_parent_id = "")</if> OR a.c_parent_id not in (SELECT _t.c_pk_id FROM t_test _t <where><trim prefixOverrides="and|or"><if test=" pkId != null and pkId != ''">_t.c_pk_id = #{pkId}</if><if test=" parentId != null and parentId != ''"> AND _t.c_parent_id = #{parentId}</if><if test=" delFlag != null"> AND _t.c_del_flag = #{delFlag}</if></trim></where>)</where>   </script>
=======================================
            */
            
            ZKMyBatisTreeSqlProvider treeSqlProvider = new ZKMyBatisTreeSqlProvider();
            
            ZKBaseHelperTreeEntity e = new ZKBaseHelperTreeEntity();
            String str;

            str = e.getSqlHelper().getBlockSqlSelelctList();
            System.out.println("[^_^:20220418-2021-001] getSqlBlockSelList: " + str);
            System.out.println("=======================================");

            str = treeSqlProvider.selectTree(e);
            System.out.println("[^_^:20220418-2021-002] selectTree: " + str);
            System.out.println("=======================================");

        }catch (Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
