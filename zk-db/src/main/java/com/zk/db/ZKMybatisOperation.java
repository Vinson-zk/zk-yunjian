/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKMybatisOperation.java 
 * @author Vinson 
 * @Package com.zk.db 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 11:01:24 AM 
 * @version V1.0   
*/
package com.zk.db;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

/**
 * @ClassName: ZKMybatisOperation
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKMybatisOperation {

    public static int insert(SqlSession sqlSession, String str, Object obj) {
        try {
            int result = 0;
            if (obj == null) {
                result = sqlSession.insert(str);
            }
            else {
                result = sqlSession.insert(str, obj);
            }
            sqlSession.commit();
            return result;
        }
        catch(Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        }
        return 0;
    }

    public static int update(SqlSession sqlSession, String str, Object obj) {
        try {
            int result = 0;
            if (obj == null) {
                result = sqlSession.update(str);
            }
            else {
                result = sqlSession.update(str, obj);
            }
            sqlSession.commit();
            return result;
        }
        catch(Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        }
        return 0;
    }

    public static int delete(SqlSession sqlSession, String str, Object obj) {
        try {
            int result = 0;
            if (obj == null) {
                result = sqlSession.delete(str);
            }
            else {
                result = sqlSession.delete(str, obj);
            }
            sqlSession.commit();
            return result;
        }
        catch(Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        }
        return 0;
    }

    public static List<?> selectList(SqlSession sqlSession, String str, Object obj) {
        try {
            List<?> result = null;
            if (obj == null) {
                result = sqlSession.selectList(str);
            }
            else {
                result = sqlSession.selectList(str, obj);
            }
            sqlSession.commit();
            return result;
        }
        catch(Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T selectOne(SqlSession sqlSession, String str, Object obj) {
        try {
            Object result = null;
            if (obj == null) {
                result = sqlSession.selectOne(str);
            }
            else {
                result = sqlSession.selectOne(str, obj);
            }
            sqlSession.commit();
            return (T) result;
        }
        catch(Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        }
        return null;
    }

    /***
     * select sqlSession.selectOne(arg0) sqlSession.selectOne(arg0, arg1)
     * sqlSession.select(arg0, arg1); sqlSession.select(arg0, arg1, arg2);
     * sqlSession.select(arg0, arg1, arg2, arg3); sqlSession.selectList(arg0)
     * sqlSession.selectList(arg0, arg1) sqlSession.selectList(arg0, arg1, arg2)
     * sqlSession.selectMap(arg0, arg1) sqlSession.selectMap(arg0, arg1, arg2)
     * sqlSession.selectMap(arg0, arg1, arg2, arg3)
     ***/

}
