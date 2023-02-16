/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.mail.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.mail.entity.ZKMailType;

/**
 * ZKMailTypeDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKMailTypeDao extends ZKBaseDao<String, ZKMailType> {

    /**
     * 根据邮件类型代码查询实体对象
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 26, 2022 3:44:44 PM
     * @param tn
     * @param alias
     * @param sCols
     * @param typeCode
     * @param delFlag
     * @return ZKMailType
     */
    @Select(value = {
            "<script>SELECT ${sCols} FROM ${tn} ${alias} WHERE ${alias}.c_type_code = #{typeCode} <if test='delFlag != null'> and ${alias}.c_del_flag = #{delFlag} </if> </script>" })
    ZKMailType getByCode(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("typeCode") String typeCode, @Param("delFlag") Integer delFlag);
	
}