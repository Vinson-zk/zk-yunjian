/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.wechat.platformBusiness.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.wechat.platformBusiness.entity.ZKFuncKeyConfig;

/**
 * ZKFuncKeyConfigDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKFuncKeyConfigDao extends ZKBaseDao<String, ZKFuncKeyConfig> {

    /**
     * 根据 FuncKey 取，功能配置实体
     *
     * @Title: getByFuncKey
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 24, 2022 11:36:33 AM
     * @param tn
     * @param alias
     * @param sCols
     * @param FuncKey
     * @return
     * @return ZKFuncKeyConfig
     */
    @Select(value = {
            "<script>SELECT ${sCols} FROM ${tn} ${alias} WHERE ${alias}.c_func_key = #{funcKey} <if test='delFlag != null'> and ${alias}.c_del_flag = #{delFlag} </if> </script>" })
    ZKFuncKeyConfig getByFuncKey(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("funcKey") String funcKey, @Param("delFlag") Integer delFlag);
	
}