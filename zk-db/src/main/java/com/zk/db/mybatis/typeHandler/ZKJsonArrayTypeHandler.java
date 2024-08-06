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
* @Title: ZKJsonArrayTypeHandler.java 
* @author Vinson 
* @Package com.zk.db.mybatis.typeHandler 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 11, 2022 10:12:34 PM 
* @version V1.0 
*/
package com.zk.db.mybatis.typeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.core.commons.data.ZKJsonArray;

/** 
* @ClassName: ZKJsonArrayTypeHandler 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJsonArrayTypeHandler extends BaseTypeHandler<ZKJsonArray> {

    protected Logger logger = LogManager.getLogger(getClass());


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ZKJsonArray parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter == null) {
            return;
        }
        ps.setString(i, parameter.toJSONString());
    }

    @Override
    public ZKJsonArray getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String vStr = rs.getString(columnName);
        return vStr == null ? null : ZKJsonArray.parse(vStr);
    }

    @Override
    public ZKJsonArray getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String vStr = rs.getString(columnIndex);
        return vStr == null ? null : ZKJsonArray.parse(vStr);
    }

    @Override
    public ZKJsonArray getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String vStr = cs.getString(columnIndex);
        return vStr == null ? null : ZKJsonArray.parse(vStr);
    }

}
