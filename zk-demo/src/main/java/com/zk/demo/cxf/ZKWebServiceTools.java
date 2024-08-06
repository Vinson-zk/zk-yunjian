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
* @Title: ZKWebServiceTools.java 
* @author Vinson 
* @Package com.zk.demo.cxf 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 27, 2021 12:43:01 AM 
* @version V1.0 
*/
package com.zk.demo.cxf;

import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.core.utils.ZKStringUtils;

/** 
* @ClassName: ZKWebServiceTools 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWebServiceTools {

    /**
     * 日志对象
     */
    protected static Logger log = LogManager.getLogger(ZKWebServiceTools.class);

    public static String callWebSV(String wsdUrl, String targetNamespace, String methodName, Object... params)
            throws Exception {
        log.info("[^_^:20210127-0044-001] ZKWebServiceTools.callWebSV: url:{}, targetNamespace:{},  operationName:{}",
                wsdUrl, targetNamespace, methodName);
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(wsdUrl);
        // client.getOutInterceptors().add(new ClientLoginInterceptor(USER_NAME,
        // PASS_WORD));

        Object[] objects;
        // invoke("方法名",参数1,参数2,参数3....);
        if (ZKStringUtils.isEmpty(targetNamespace)) {
            objects = client.invoke(methodName, params);
        }
        else {
            // namespace是命名空间，methodName 是方法名
            QName operationName = new QName(targetNamespace, methodName);
            objects = client.invoke(operationName, params);
        }

        log.info("[^_^:20210127-0044-002] ZKWebServiceTools.callWebSV end;");

        return objects[0].toString();
    }
    
    public static void main(String[] args) {
        try {
            System.out.println("----------------------------------------");
            String url = "http://gfdev06.zhgxfz.com/nc/uapws/service/nc.itf.gfnc.hello.IHelloNc?wsdl";
            String targetNamespace = "http://hello.gfnc.itf.nc/IHelloNc";
            String operationName = "";
            String resStr = null;

            operationName = "hello";
            resStr = ZKWebServiceTools.callWebSV(url, targetNamespace, operationName);
            System.out.println("[^_^:20210127-0051-001] resStr: " + resStr);

            operationName = "msg";
            resStr = ZKWebServiceTools.callWebSV(url, targetNamespace, operationName, " 测试消息");
            System.out.println("[^_^:20210127-0051-002] resStr: " + resStr);

            System.out.println("----------------------------------------");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


}
