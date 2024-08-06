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
* @Title: JavaVMStackSOF.java 
* @author Vinson 
* @Package com.zk.demo.memory 
* @Description: TODO(simple description this file what to do.) 
* @date Jul 29, 2020 1:53:41 PM 
* @version V1.0 
*/
package com.zk.demo.memory;
/** 
* @ClassName: JavaVMStackSOF 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class JavaVMStackSOF {
    /**
     * Java 虚机栈和本地方法栈 OOM 测试
     * 
     * VM Args: -Xss128k
     */

    private int stackLength = 1;

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        JavaVMStackSOF sof = new JavaVMStackSOF();
        try {
            sof.stackLeak();
        }
        catch(Exception e) {
            System.out.println("[>_<:20200729-1356-001] stackLength:" + sof.stackLength);
            e.printStackTrace();
        }
    }

}
