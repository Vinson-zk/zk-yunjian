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
* @Title: ClassDemoTestMain.java 
* @author Vinson 
* @Package com.zk.demo.instance 
* @Description: TODO(simple description this file what to do. ) 
* @date Sep 10, 2020 9:13:39 AM 
* @version V1.0 
*/
package com.zk.demo.instance;

import java.lang.reflect.Constructor;

/** 
* @ClassName: ClassDemoTestMain 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ClassDemoTestMain {

    public static void main(String[] args) {
        try {
            Class<ClassDemo> classz = ClassDemo.class;

            ClassDemo d = null;

            Class<?>[] paramsTypes = new Class[1];
            paramsTypes[0] = String.class;
            Constructor<ClassDemo> constructor = classz.getConstructor(paramsTypes);

            String name = "测试通过 class 实例化对象";
            d = constructor.newInstance(name);

            System.out.println("[^_^:20200910-1040-001] d.name: " + d.getName());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        


    }

}
