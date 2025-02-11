/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.iot.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.iot.entity.ZKIotProdModel;

/**
 * ZKIotProdModelDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKIotProdModelDao extends ZKBaseDao<String, ZKIotProdModel> {

    // 根据代码取实体及明细
    @Select({
            "SELECT ${_zkSql.cols} FROM ${_zkSql.tn} ${_zkSql.ta} WHERE ${_zkSql.ta}.c_prod_model_code = #{prodModelCode} " })
    ZKIotProdModel getByCode(@Param("prodModelCode") String prodModelCode);

    // 修改状态
    @Update(" UPDATE ${_tn} SET c_status = #{status} WHERE c_pk_id = #{pkId} ")
    int updateStatus(@Param("_tn") String tn, @Param("pkId") String pkId, @Param("status") int status);

    // 取最大排序号
    @Select({ "<script>SELECT max(c_sort) FROM ${_zkSql.tn} ", "</script>" })
    Integer getMaxSort();

    // 数据处理协议
    @Update({ "UPDATE ${_tn} SET ", //
            "c_business_data_protocol_id = #{businessDataProtocolId}, c_business_data_protocol_code = #{businessDataProtocolCode}, ", //
            "c_update_date = #{updateDate}, c_update_user_id = #{userId} ", //
            "WHERE c_pk_id = #{pkId} " })
    int updateDataProtocol(@Param("_tn") String tn, @Param("pkId") String pkId,
            @Param("businessDataProtocolId") String businessDataProtocolId,
            @Param("businessDataProtocolCode") String businessDataProtocolCode, @Param("updateDate") Date updateDate,
            @Param("userId") String userId);
	
}
