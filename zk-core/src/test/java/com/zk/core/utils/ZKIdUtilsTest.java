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
 * @Title: ZKIdUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 4:57:54 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKIdUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKIdUtilsTest {
    
    @Test
    public void testGenId() {
        try {
            List<Long> idList = new ArrayList<Long>();

            class GenId extends Thread {

                public boolean isStart = false;

                public List<Long> idList = new ArrayList<Long>();

                @SuppressWarnings({ "static-access", "deprecation" })
                @Override
                public void run() {
                    isStart = true;
                    for (int i = 0; i < 100; ++i) {
                        idList.add(ZKIdUtils.genLongId());
                        try {
                            Thread.sleep(10);
                        }
                        catch(InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    isStart = false;
                    System.out.println("[^_^ 20170524-001] " + this.currentThread().getId() + "     isStart=" + isStart
                            + "   size=" + idList.size());
                }
            }
            GenId g9 = new GenId();
            g9.start();
            GenId g8 = new GenId();
            g8.start();
            GenId g7 = new GenId();
            g7.start();
            GenId g6 = new GenId();
            g6.start();
            GenId g5 = new GenId();
            g5.start();
            GenId g4 = new GenId();
            g4.start();
            GenId g3 = new GenId();
            g3.start();
            GenId g2 = new GenId();
            g2.start();
            GenId g1 = new GenId();
            g1.start();
            GenId g0 = new GenId();
            g0.start();

            while (true) {
                if (!g0.isStart && !g1.isStart && !g2.isStart && !g3.isStart && !g4.isStart && !g5.isStart
                        && !g6.isStart && !g7.isStart && !g8.isStart && !g9.isStart) {
                    Thread.sleep(1000);
                    idList.addAll(g0.idList);
                    idList.addAll(g1.idList);
                    idList.addAll(g2.idList);
                    idList.addAll(g3.idList);
                    idList.addAll(g4.idList);
                    idList.addAll(g5.idList);
                    idList.addAll(g6.idList);
                    idList.addAll(g7.idList);
                    idList.addAll(g8.idList);
                    idList.addAll(g9.idList);
                    break;
                }
                else {
                    System.out.println("[^_^ 20170524-004] 生成ID线程未全部结束！");
                    Thread.sleep(1000);
                }
            }

            System.out.println("[^_^ 20170524-003] idList.size=" + idList.size());

            TestCase.assertEquals(1000, idList.size());
            for (int i = 0; i < idList.size(); ++i) {
                for (int j = i + 1; j < idList.size(); ++j) {
                    if (idList.get(i).longValue() == idList.get(j).longValue()) {
                        System.out.println("[^_^ 20170524-002] i=" + i + "   id=" + idList.get(i).longValue());
                        System.out.println("[^_^ 20170524-002] j=" + j + "   id=" + idList.get(j).longValue());
                        TestCase.assertTrue(false);
                    }
                }
            }

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
