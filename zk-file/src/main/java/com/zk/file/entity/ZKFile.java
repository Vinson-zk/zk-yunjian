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
* @Title: ZKFile.java 
* @author Vinson 
* @Package com.zk.file.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 28, 2023 5:01:34 PM 
* @version V1.0 
*/
package com.zk.file.entity;

import javax.xml.bind.annotation.XmlTransient;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zk.base.commons.ZKTreeSqlHelper;
import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKSqlConvertDelegating;

/** 
* @ClassName: ZKFile 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_file", alias = "f", orderBy = " c_type DESC, c_sort ASC ")
public class ZKFile extends ZKBaseTreeEntity<String, ZKFile> {

    static ZKTreeSqlHelper sqlHelper;

    @Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKTreeSqlHelper getTreeSqlHelper() {
        return sqlHelper();
    }

    @Transient
    @XmlTransient
    @JsonIgnore
    public static ZKTreeSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKTreeSqlHelper(new ZKSqlConvertDelegating(), new ZKFile());
        }
        return sqlHelper;
    }

    private static final long serialVersionUID = 1L;

}
