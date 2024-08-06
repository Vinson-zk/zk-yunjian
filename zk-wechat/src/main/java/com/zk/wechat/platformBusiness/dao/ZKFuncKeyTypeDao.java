/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.wechat.platformBusiness.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.wechat.platformBusiness.entity.ZKFuncKeyType;

/**
 * ZKFuncKeyTypeDAO接口
 * 
 * @author
 * @version 1.0
 */
@ZKMyBatisDao
public interface ZKFuncKeyTypeDao extends ZKBaseDao<String, ZKFuncKeyType> {

    /**
     * 根据功能类型代码取功能类型实体
     *
     * @Title: getByFuncTypeCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 24, 2022 11:40:18 AM
     * @param tn
     * @param alias
     * @param sCols
     * @param funcTypeCode
     * @param delFlag
     * @return
     * @return ZKFuncKeyConfig
     */
    @Select(value = {
            "<script>SELECT ${sCols} FROM ${tn} ${alias} WHERE ${alias}.c_func_type_code = #{funcTypeCode} <if test='delFlag != null'> and ${alias}.c_del_flag = #{delFlag} </if> </script>" })
    ZKFuncKeyType getByFuncTypeCode(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("funcTypeCode") String funcTypeCode, @Param("delFlag") Integer delFlag);

}