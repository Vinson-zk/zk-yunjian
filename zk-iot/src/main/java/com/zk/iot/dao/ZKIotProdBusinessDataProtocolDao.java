/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.iot.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.iot.entity.ZKIotProdBusinessDataProtocol;

/**
 * ZKIotProdBusinessDataProtocolDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKIotProdBusinessDataProtocolDao extends ZKBaseDao<String, ZKIotProdBusinessDataProtocol> {

    // 根据代码取实体及明细
    @Select({
            "SELECT ${_zkSql.cols} FROM ${_zkSql.tn} ${_zkSql.ta} WHERE ${_zkSql.ta}.c_protocol_code = #{protocolCode} " })
    ZKIotProdBusinessDataProtocol getByCode(@Param("protocolCode") String protocolCode);

    // 修改状态
    @Update(" UPDATE ${_tn} SET c_status = #{status} WHERE c_pk_id = #{pkId} ")
    int updateStatus(@Param("_tn") String tn, @Param("pkId") String pkId, @Param("status") int status);

    // 取最大排序号
    @Select({ "<script>SELECT max(c_sort) FROM ${_zkSql.tn} ", "</script>" })
    Integer getMaxSort();
	
}