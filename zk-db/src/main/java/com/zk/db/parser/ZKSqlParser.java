/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSqlParser.java 
* @author Vinson 
* @Package com.zk.db.parser 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 3, 2024 3:11:24 PM 
* @version V1.0 
*/
package com.zk.db.parser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.Statement;

/**
 * 
 * 参考：https://github.com/pagehelper/Mybatis-PageHelper
 * 
 * @ClassName: ZKSqlParser
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public interface ZKSqlParser {

    /**
     * 不使用单线程池，不支持超时控制
     */
    ZKSqlParser DEFAULT = statementReader -> {
        CCJSqlParser parser = CCJSqlParserUtil.newParser(statementReader);
        parser.withSquareBracketQuotation(true);
        return parser.Statement();
    };

    /**
     * 解析 SQL
     *
     * @param statementReader
     *            SQL
     * @return
     * @throws JSQLParserException
     */
    Statement parse(String statementReader) throws JSQLParserException, ParseException;

}
