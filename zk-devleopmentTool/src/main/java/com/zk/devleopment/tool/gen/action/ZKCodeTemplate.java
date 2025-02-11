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
* @Title: ZKCodeTemplate.java 
* @author Vinson 
* @Package com.zk.code.generate.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 24, 2021 5:05:49 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * 代码模板信息读取
 * 
 * @ClassName: ZKCodeTemplate
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@XmlRootElement(name = "zkCodeTemplate")
public class ZKCodeTemplate {

    private String name; // 名称

    private String fileName; // 文件名

    private String content; // 内容

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
