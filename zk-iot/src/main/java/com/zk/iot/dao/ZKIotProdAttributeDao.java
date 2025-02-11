/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.iot.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.iot.entity.ZKIotProdAttribute;

/**
 * ZKIotProdAttributeDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKIotProdAttributeDao extends ZKBaseDao<String, ZKIotProdAttribute> {

    // 修改状态
    @Update(" UPDATE ${_tn} SET c_status = #{status} WHERE c_pk_id = #{pkId} ")
    int updateStatus(@Param("_tn") String tn, @Param("pkId") String pkId, @Param("status") int status);

    // 取指定目标ID 下指定类型属性的 最大排序号
    @Select({
            "<script>SELECT max(c_sort) FROM ${_zkSql.tn} WHERE ", //
            "c_attr_from = #{attrFrom} AND c_target_id = #{targetId} AND c_attr_type = #{attrType} ", //
            "</script>" })
    Integer getMaxSort(@Param("attrFrom") Integer attrFrom, @Param("targetId") String targetId,
            @Param("attrType") Integer attrType);

    // 根据代码取实体
    @Select({ "<script>SELECT ${_zkSql.cols} FROM ${_zkSql.tn} ${_zkSql.ta} WHERE ", //
            "${_zkSql.ta}.c_attr_from = #{attrFrom} AND ${_zkSql.ta}.c_target_id = #{targetId} AND ${_zkSql.ta}.c_attr_code = #{attrCode} ", //
            "</script>"
        })
    ZKIotProdAttribute getByCode(@Param("attrFrom") Integer attrFrom, @Param("targetId") String targetId,
            @Param("attrCode") String attrCode);

}




