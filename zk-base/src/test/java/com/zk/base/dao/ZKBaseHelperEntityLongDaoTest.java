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
* @Title: ZKBaseHelperEntityLongDaoTest.java 
* @author Vinson 
* @Package com.zk.base.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 3, 2023 2:19:05 PM 
* @version V1.0 
*/
package com.zk.base.dao;

import org.junit.Test;

import com.zk.base.helper.dao.ZKBaseHelperEntityLongDao;
import com.zk.base.helper.entity.ZKBaseHelperEntityLong;
import com.zk.core.utils.ZKClassUtils;
import com.zk.db.entity.ZKDBEntity;
import com.zk.db.mybatis.dao.ZKDBDao;

import junit.framework.TestCase;

/** 
* @ClassName: ZKBaseHelperEntityLongDaoTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKBaseHelperEntityLongDaoTest {

    @Test
    public void testGetSuperclassByName() {
        try {
            Class<ZKDBEntity<?>> classz = ZKClassUtils.getSuperclassByName(ZKDBDao.class,
                    ZKBaseHelperEntityLongDao.class, "E");
            System.out.println("[^_^:20221012-1526-001] classz: " + classz.getName());
            TestCase.assertEquals(ZKBaseHelperEntityLong.class, classz);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
