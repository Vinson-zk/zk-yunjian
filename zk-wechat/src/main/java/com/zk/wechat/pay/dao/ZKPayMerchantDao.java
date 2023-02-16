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
* @Title: ZKPayMerchantDao.java 
* @author Vinson 
* @Package com.zk.wechat.pay.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 5:36:48 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.wechat.pay.entity.ZKPayMerchant;

/**
 * @ClassName: ZKPayMerchantDao
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@ZKMyBatisDao
public interface ZKPayMerchantDao extends ZKBaseDao<String, ZKPayMerchant> {
    
    @Update("UPDATE t_wx_merchant SET c_wx_apiv3_aes_key = #{apiv3Aeskey}, c_update_date = #{updateDate} where c_pk_id = #{mchid} ")
    int updateApiv3Aeskey(@Param("mchid")String mchid, @Param("apiv3Aeskey")String apiv3Aeskey, @Param("updateDate")Date updateDate);
    
    @Update("UPDATE t_wx_merchant SET c_wx_cert_my_private_path = #{certMyPrivatePath}, c_wx_cert_my_private_update_date = #{updateDate} where c_pk_id = #{mchid} ")
    int updateCertMyPrivatePath(@Param("mchid")String mchid, @Param("certMyPrivatePath") String certMyPrivatePath, @Param("updateDate")Date updateDate);

}
