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
* @Title: ZKBaseHelperEntityStringDto.java 
* @author Vinson 
* @Package com.zk.base.helper.dto 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 2, 2023 5:27:38 PM 
* @version V1.0 
*/
package com.zk.base.helper.dto;

import com.zk.base.helper.entity.ZKBaseHelperEntityString;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

/** 
* @ClassName: ZKBaseHelperEntityStringDto 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKTable(name = "t_zk_db_test", alias = "t")
public class ZKBaseHelperEntityStringDto extends ZKBaseHelperEntityString<ZKBaseHelperEntityStringDto> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKBaseHelperEntityStringDto());
        }
        return sqlHelper;
    }

}
