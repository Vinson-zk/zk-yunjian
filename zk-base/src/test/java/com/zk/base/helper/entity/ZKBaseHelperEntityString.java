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
 * @Title: ZKBaseHelperEntityString.java 
 * @author Vinson 
 * @Package com.zk.base.helper.entity 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 2:58:00 PM 
 * @version V1.0   
*/
package com.zk.base.helper.entity;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

/** 
* @ClassName: ZKBaseHelperEntityString 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKBaseHelperEntityString extends ZKBaseEntity<String, ZKBaseHelperEntityString> {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    static ZKDBSqlHelper sqlHelper;

    @Override
    public ZKDBSqlHelper getSqlHelper() {
        return sqlHelper();
    }

    public static ZKDBSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKBaseHelperEntityString());
        }
        return sqlHelper;
    }

}
