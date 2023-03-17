package com.zk.base.entity;

import org.junit.Test;

import com.zk.base.helper.entity.ZKBaseHelperTreeEntity;

import junit.framework.TestCase;

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
 * @ClassName ZKBaseHelperTreeEntityTest
 * @Package com.zk.base.entity
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-10-17 21:44:44
 **/
public class ZKBaseHelperTreeEntityTest {

    @Test
    public void test() {
        try {
            ZKBaseHelperTreeEntity helperTreeEntity = new ZKBaseHelperTreeEntity();
            System.out.println("[^_^:20221017-2146-001] " + helperTreeEntity.getTreeSqlHelper().getBlockSqlWhereTree());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }
}
