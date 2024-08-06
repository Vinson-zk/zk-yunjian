/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKPayGetOrderDao.java 
* @author Vinson 
* @Package com.zk.wechat.pay.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 11:53:43 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.wechat.pay.entity.ZKPayGetOrder;
import com.zk.wechat.pay.enumType.ZKPayGetChannel;
import com.zk.wechat.pay.enumType.ZKPayStatus;

/** 
* @ClassName: ZKPayGetOrderDao 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKMyBatisDao
public interface ZKPayGetOrderDao extends ZKBaseDao<String, ZKPayGetOrder> {

    @Update({ "UPDATE t_wx_pay_get_order SET ",
            "c_pay_status = #{payStatus}, c_wx_res_status_code = #{wxResStatusCode}, c_wx_prepay_id = #{prepayId},",
            " c_wx_prepay_id_date = #{prepayIdDate}", " where c_pk_id = #{pkId}"})
    int updatePrepay(@Param("pkId") String pkId, @Param("payStatus") ZKPayStatus payStatus,
            @Param("wxResStatusCode") String wxResStatusCode, @Param("prepayId") String prepayId,
            @Param("prepayIdDate") Date prepayIdDate);
    
    @Update({ "UPDATE t_wx_pay_get_order SET c_wx_time_expire = #{timeExpire} where c_pk_id = #{pkId}" })
    int updateTimeExpire(@Param("pkId") String pkId, @Param("timeExpire") Date timeExpire);

    // 根据业务类型获取支付订单
    @Select(value = { "SELECT ${sCols} FROM ${tn} WHERE c_business_code = #{businessCode} and c_business_no = #{businessNo}" })
    ZKPayGetOrder getByBusiness(@Param("sCols")String sCols, @Param("tn")String tn, 
            @Param("businessCode")String businessCode, @Param("businessNo") String businessNo);
    
    @Update("UPDATE t_wx_pay_get_order SET c_pay_sign_date = #{paySignDate}  where c_pk_id = #{pkId}")
    int updatePaySignDate(@Param("pkId")String pkId, @Param("paySignDate")Date paySignDate);
    
    @Update("UPDATE t_wx_pay_get_order SET c_pay_status = #{payStatus}, c_update_date = #{updateDate} where c_pk_id = #{pkId}")
    int updatePayStatus(@Param("pkId")String pkId, @Param("payStatus")ZKPayStatus payStatus, @Param("updateDate")Date updateDate);
    
    @Update("UPDATE t_wx_pay_get_order SET c_pay_status = #{payStatus}, c_wx_channel = #{wxChannel}, c_update_date = #{updateDate} where c_pk_id = #{pkId}")
    int updatePayChannel(@Param("pkId")String pkId, @Param("payStatus")ZKPayStatus payStatus, 
            @Param("wxChannel")ZKPayGetChannel wxChannel, @Param("updateDate")Date updateDate);

    
}
