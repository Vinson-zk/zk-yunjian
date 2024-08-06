/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.settings.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.settings.entity.ZKSysSetItem;

/**
 * ZKSysSetItemDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysSetItemDao extends ZKBaseDao<String, ZKSysSetItem> {

    /**
     * 根据配置组别代码和配置项代码查询配置项
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 11, 2022 4:26:23 PM
     * @param tn
     * @param alias
     * @param sCols
     * @param collectionCode
     * @param code
     * @return
     * @return ZKSysSetItem
     */
    @Select(value = {
            "SELECT ${sCols} FROM ${tn} ${alias} WHERE ${alias}.c_collection_code = #{collectionCode} and ${alias}.c_code = #{code}" })
    ZKSysSetItem getByCode(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("collectionCode") String collectionCode,
            @Param("code") String code);
	
}