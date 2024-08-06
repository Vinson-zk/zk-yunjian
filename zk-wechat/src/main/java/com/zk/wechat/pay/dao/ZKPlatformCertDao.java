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
* @Title: ZKPlatformCertDao.java 
* @author Vinson 
* @Package com.zk.wechat.pay.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2021 6:33:08 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.wechat.pay.entity.ZKPlatformCert;

/** 
* @ClassName: ZKPlatformCertDao 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKMyBatisDao
public interface ZKPlatformCertDao extends ZKBaseDao<String, ZKPlatformCert> {
    
    @Select(value = {
            "SELECT ${sCols} FROM ${tn} WHERE c_wx_mchid = #{mchid} and c_wx_cert_serial_no = #{serialNo} and c_del_flag = #{delFlag} " })
    ZKPlatformCert getBySerial(@Param("tn") String tn, @Param("sCols") String sCols,
            @Param("mchid") String mchid, @Param("serialNo") String serialNo, @Param("delFlag") int delFlag);

}
