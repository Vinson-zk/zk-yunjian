/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.res.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.res.entity.ZKSysResFuncApi;

/**
 * ZKSysResFuncApiDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysResFuncApiDao extends ZKBaseDao<String, ZKSysResFuncApi> {
	
    /**
     * 根据接口代码取接口
     *
     * @Title: getBySysAndCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 30, 2021 10:57:24 AM
     * @param tn
     * @param tAlias
     * @param sCols
     * @param apiCode
     * @return
     * @return ZKSysResFuncApi
     */
    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE ${tAlias}.c_code = #{apiCode} ")
    ZKSysResFuncApi getBySysAndCode(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("apiCode") String apiCode);

}