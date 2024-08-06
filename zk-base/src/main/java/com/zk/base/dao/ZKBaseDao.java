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
 * @Title: ZKBaseDao.java 
 * @author Vinson 
 * @Package com.zk.base.dao 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 2:20:33 PM 
 * @version V1.0   
*/
package com.zk.base.dao;

import java.io.Serializable;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.db.mybatis.dao.ZKDBDao;

/** 
* @ClassName: ZKBaseDao 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKBaseDao<ID extends Serializable, E extends ZKBaseEntity<ID, E>> extends ZKDBDao<E> {
    
//    @Override
//    @SelectProvider(type = ZKDBMybatisSqlProvider.class, method = "get")
//    E get(E entity);
//    
//    @Override
//    @SelectProvider(type = ZKDBMybatisSqlProvider.class, method = "selectList")
//    List<E> findList(E entity);

}
