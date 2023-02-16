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
* @Title: ZKThreadPool.java 
* @author Vinson 
* @Package com.zk.core.commons 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 16, 2021 3:23:01 PM 
* @version V1.0 
*/
package com.zk.core.commons;

import java.util.ArrayList;
import java.util.List;

/** 
 * 不建议使用，不完善
 * @ClassName: ZKThreadPool
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Deprecated
public class ZKThreadPool extends Thread {

    final static List<ZKThreadPool> tPool = new ArrayList<>();

    public static List<ZKThreadPool> getThreadPool() {
        return tPool;
    }

    Runnable ru;

    public ZKThreadPool(Runnable ru) {
        tPool.add(this);
        this.ru = ru;
    }

    @Override
    public void run() {
        try {
            this.ru.run();
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            tPool.remove(this);
        }
    }

}
