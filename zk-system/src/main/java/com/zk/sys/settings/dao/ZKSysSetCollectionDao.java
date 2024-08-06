/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.settings.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.settings.entity.ZKSysSetCollection;

/**
 * ZKSysSetCollectionDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysSetCollectionDao extends ZKBaseDao<String, ZKSysSetCollection> {
	
    /**
     * 根据组别代码查询配置项组别
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 9, 2022 11:58:05 PM
     * @param tn
     * @param alias
     * @param sCols
     * @param code
     * @return
     * @return ZKSysSetCollection
     */
    @Select(value = { "SELECT ${sCols} FROM ${tn} ${alias} WHERE ${alias}.c_code = #{code}" })
    ZKSysSetCollection getByCode(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("code") String code);
}