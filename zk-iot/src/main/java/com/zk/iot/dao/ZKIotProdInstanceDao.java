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
import com.zk.iot.entity.ZKIotProdInstance;

/**
 * ZKIotProdInstanceDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKIotProdInstanceDao extends ZKBaseDao<String, ZKIotProdInstance> {

    // 根据型号和序列号取实体
    @Select({
            "SELECT ${_zkSql.cols} FROM ${_zkSql.tn} ${_zkSql.ta} WHERE ${_zkSql.ta}.c_sn_num = #{snNum} AND ${_zkSql.ta}.c_prod_model_code = #{prodModelCode} " })
    ZKIotProdInstance getBySnAndModelCode(@Param("prodModelCode") String prodModelCode, @Param("snNum") String snNum);

    @Select({
            "SELECT ${_zkSql.cols} FROM ${_zkSql.tn} ${_zkSql.ta} WHERE ${_zkSql.ta}.c_sn_num = #{snNum} AND ${_zkSql.ta}.c_prod_model_id = #{prodModelId} " })
    ZKIotProdInstance getBySnAndModelId(@Param("prodModelId") String prodModelId, @Param("snNum") String snNum);

    // 根据 mac 地址取实体
    @Select({ "SELECT ${_zkSql.cols} FROM ${_zkSql.tn} ${_zkSql.ta} WHERE ${_zkSql.ta}.c_mac_addr = #{macAddr} " })
    ZKIotProdInstance getByMacAddr(@Param("macAddr") String macAddr);

    // 修改状态
    @Update(" UPDATE ${_tn} SET c_status = #{status} WHERE c_pk_id = #{pkId} ")
    int updateStatus(@Param("_tn") String tn, @Param("pkId") String pkId, @Param("status") int status);

    // 取最大排序号
    @Select({ "<script>SELECT max(c_sort) FROM ${_zkSql.tn} ", "</script>" })
    Integer getMaxSort();

    // 修改最后心跳时间
    @Update(" UPDATE ${_tn} SET c_last_in_time = #{lastInTime} WHERE c_pk_id = #{pkId} ")
    int updateLastInTime(@Param("_tn") String tn, @Param("pkId") String pkId, @Param("lastInTime") Date lastInTime);

    @Update(" UPDATE ${_tn} SET c_last_in_time = #{lastInTime} WHERE c_mac_addr = #{macAddr} ")
    int updateLastInTimeByMacAddr(@Param("_tn") String tn, @Param("macAddr") String macAddr,
            @Param("lastInTime") Date lastInTime);

    @Update(" UPDATE ${_tn} SET c_last_in_time = #{lastInTime} WHERE c_sn_num = #{snNum} AND c_prod_model_code = #{prodModelCode} ")
    int updateLastInTimeBySn(@Param("_tn") String tn, @Param("prodModelCode") String prodModelCode,
            @Param("snNum") String snNum, @Param("lastInTime") Date lastInTime);
	
}
